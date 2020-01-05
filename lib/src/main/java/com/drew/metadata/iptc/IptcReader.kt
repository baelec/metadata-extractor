/*
 * Copyright 2002-2019 Drew Noakes and contributors
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 * More information about this project is available at:
 *
 *    https://drewnoakes.com/code/exif/
 *    https://github.com/drewnoakes/metadata-extractor
 */
package com.drew.metadata.iptc

import com.drew.imaging.jpeg.JpegSegmentMetadataReader
import com.drew.imaging.jpeg.JpegSegmentType
import com.drew.lang.SequentialByteArrayReader
import com.drew.lang.SequentialReader
import com.drew.metadata.Directory
import com.drew.metadata.Metadata
import com.drew.metadata.StringValue
import java.io.IOException
import java.nio.charset.Charset

/**
 * Decodes IPTC binary data, populating a [Metadata] object with tag values in an [IptcDirectory].
 *
 *
 * http://www.iptc.org/std/IIM/4.1/specification/IIMV4.1.pdf
 *
 * @author Drew Noakes https://drewnoakes.com
 */
class IptcReader : JpegSegmentMetadataReader {
  override val segmentTypes: Iterable<JpegSegmentType>
    get() = listOf(JpegSegmentType.APPD)

  override fun readJpegSegments(segments: Iterable<ByteArray>, metadata: Metadata, segmentType: JpegSegmentType) {
    for (segmentBytes in segments) { // Ensure data starts with the IPTC marker byte
      if (segmentBytes.isNotEmpty() && segmentBytes[0] == IptcMarkerByte) {
        extract(SequentialByteArrayReader(segmentBytes), metadata, segmentBytes.size.toLong())
      }
    }
  }
  /**
   * Performs the IPTC data extraction, adding found values to the specified instance of [Metadata].
   */
  /**
   * Performs the IPTC data extraction, adding found values to the specified instance of [Metadata].
   */
  @JvmOverloads
  fun extract(reader: SequentialReader, metadata: Metadata, length: Long, parentDirectory: Directory? = null) {
    val directory = IptcDirectory()
    metadata.addDirectory(directory)
    if (parentDirectory != null) directory.setParent(parentDirectory)
    var offset = 0
    // for each tag
    while (offset < length) { // identifies start of a tag
      var startByte: Short
      try {
        startByte = reader.getUInt8()
        offset++
      } catch (e: IOException) {
        directory.addError("Unable to read starting byte of IPTC tag")
        return
      }
      if (startByte != IptcMarkerByte.toShort()) { // NOTE have seen images where there was one extra byte at the end, giving
// offset==length at this point, which is not worth logging as an error.
        if (offset.toLong() != length) directory.addError("Invalid IPTC tag marker at offset " + (offset - 1) + ". Expected '0x" + Integer.toHexString(IptcMarkerByte.toInt()) + "' but got '0x" + Integer.toHexString(startByte.toInt()) + "'.")
        return
      }
      // we need at least four bytes left to read a tag
      if (offset + 4 > length) {
        directory.addError("Too few bytes remain for a valid IPTC tag")
        return
      }
      var directoryType: Int
      var tagType: Int
      var tagByteCount: Int
      try {
        directoryType = reader.getUInt8().toInt()
        tagType = reader.getUInt8().toInt()
        tagByteCount = reader.getUInt16()
        if (tagByteCount > 32767) { // Extended DataSet Tag (see 1.5(c), p14, IPTC-IIMV4.2.pdf)
          tagByteCount = tagByteCount and 0x7FFF shl 16 or reader.getUInt16()
          offset += 2
        }
        offset += 4
      } catch (e: IOException) {
        directory.addError("IPTC data segment ended mid-way through tag descriptor")
        return
      }
      if (offset + tagByteCount > length) {
        directory.addError("Data for tag extends beyond end of IPTC segment")
        return
      }
      try {
        processTag(reader, directory, directoryType, tagType, tagByteCount)
      } catch (e: IOException) {
        directory.addError("Error processing IPTC tag")
        return
      }
      offset += tagByteCount
    }
  }

  @Throws(IOException::class)
  private fun processTag(reader: SequentialReader, directory: Directory, directoryType: Int, tagType: Int, tagByteCount: Int) {
    val tagIdentifier = tagType or (directoryType shl 8)
    // Some images have been seen that specify a zero byte tag, which cannot be of much use.
// We elect here to completely ignore the tag. The IPTC specification doesn't mention
// anything about the interpretation of this situation.
// https://raw.githubusercontent.com/wiki/drewnoakes/metadata-extractor/docs/IPTC-IIMV4.2.pdf
    if (tagByteCount == 0) {
      directory.setString(tagIdentifier, "")
      return
    }
    when (tagIdentifier) {
      IptcDirectory.TAG_CODED_CHARACTER_SET -> {
        val bytes = reader.getBytes(tagByteCount)
        var charsetName = Iso2022Converter.convertISO2022CharsetToJavaCharset(bytes)
        if (charsetName == null) { // Unable to determine the charset, so fall through and treat tag as a regular string
          charsetName = String(bytes)
        }
        directory.setString(tagIdentifier, charsetName)
        return
      }
      IptcDirectory.TAG_ENVELOPE_RECORD_VERSION, IptcDirectory.TAG_APPLICATION_RECORD_VERSION, IptcDirectory.TAG_FILE_VERSION, IptcDirectory.TAG_ARM_VERSION, IptcDirectory.TAG_PROGRAM_VERSION ->  // short
        if (tagByteCount >= 2) {
          val shortValue = reader.getUInt16()
          reader.skip(tagByteCount - 2.toLong())
          directory.setInt(tagIdentifier, shortValue)
          return
        }
      IptcDirectory.TAG_URGENCY -> {
        // byte
        directory.setInt(tagIdentifier, reader.getUInt8().toInt())
        reader.skip(tagByteCount - 1.toLong())
        return
      }
      else -> {
      }
    }
    // If we haven't returned yet, treat it as a string
// NOTE that there's a chance we've already loaded the value as a string above, but failed to parse the value
    val charSetName = directory.getString(IptcDirectory.TAG_CODED_CHARACTER_SET)
    var charset: Charset? = null
    try {
      if (charSetName != null) charset = Charset.forName(charSetName)
    } catch (ignored: Throwable) {
    }
    val string: StringValue
    string = if (charSetName != null) {
      reader.getStringValue(tagByteCount, charset)
    } else {
      val bytes = reader.getBytes(tagByteCount)
      val charSet = Iso2022Converter.guessCharSet(bytes)
      charSet?.let { StringValue(bytes, it) } ?: StringValue(bytes, null)
    }
    if (directory.containsTag(tagIdentifier)) { // this fancy StringValue[] business avoids using an ArrayList for performance reasons
      val oldStrings = directory.getStringValueArray(tagIdentifier)
      val newStrings: Array<StringValue?>
      if (oldStrings == null) { // TODO hitting this block means any prior value(s) are discarded
        newStrings = arrayOfNulls(1)
      } else {
        newStrings = arrayOfNulls(oldStrings.size + 1)
        System.arraycopy(oldStrings, 0, newStrings, 0, oldStrings.size)
      }
      newStrings[newStrings.size - 1] = string
      directory.setStringValueArray(tagIdentifier, newStrings)
    } else {
      directory.setStringValue(tagIdentifier, string)
    }
  }

  companion object {
    // TODO consider breaking the IPTC section up into multiple directories and providing segregation of each IPTC directory
    /*
    public static final int DIRECTORY_IPTC = 2;

    public static final int ENVELOPE_RECORD = 1;
    public static final int APPLICATION_RECORD_2 = 2;
    public static final int APPLICATION_RECORD_3 = 3;
    public static final int APPLICATION_RECORD_4 = 4;
    public static final int APPLICATION_RECORD_5 = 5;
    public static final int APPLICATION_RECORD_6 = 6;
    public static final int PRE_DATA_RECORD = 7;
    public static final int DATA_RECORD = 8;
    public static final int POST_DATA_RECORD = 9;
    */
    private const val IptcMarkerByte: Byte = 0x1c
  }
}
