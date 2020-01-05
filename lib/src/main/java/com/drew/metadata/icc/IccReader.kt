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
package com.drew.metadata.icc

import com.drew.imaging.jpeg.JpegSegmentMetadataReader
import com.drew.imaging.jpeg.JpegSegmentType
import com.drew.lang.ByteArrayReader
import com.drew.lang.RandomAccessReader
import com.drew.lang.isValidDate
import com.drew.lang.isValidTime
import com.drew.metadata.Directory
import com.drew.metadata.Metadata
import com.drew.metadata.MetadataReader
import java.io.IOException

/**
 * Reads an ICC profile.
 *
 *
 * More information about ICC:
 *
 *  * http://en.wikipedia.org/wiki/ICC_profile
 *  * http://www.sno.phy.queensu.ca/~phil/exiftool/TagNames/ICC_Profile.html
 *  * https://developer.apple.com/library/mac/samplecode/ImageApp/Listings/ICC_h.html
 *
 *
 * @author Yuri Binev
 * @author Drew Noakes https://drewnoakes.com
 */
class IccReader : JpegSegmentMetadataReader, MetadataReader {
  override val segmentTypes: Iterable<JpegSegmentType>
    get() = listOf(JpegSegmentType.APP2)

  override fun readJpegSegments(segments: Iterable<ByteArray>, metadata: Metadata, segmentType: JpegSegmentType) {
    val preambleLength = JPEG_SEGMENT_PREAMBLE.length
    // ICC data can be spread across multiple JPEG segments.
// We concat them together in this buffer for later processing.
    var buffer: ByteArray? = null
    for (segmentBytes in segments) { // Skip any segments that do not contain the required preamble
      if (segmentBytes.size < preambleLength || !JPEG_SEGMENT_PREAMBLE.equals(String(segmentBytes, 0, preambleLength), ignoreCase = true)) continue
      // NOTE we ignore three bytes here -- are they useful for anything?
// Grow the buffer
      if (buffer == null) {
        buffer = ByteArray(segmentBytes.size - 14)
        // skip the first 14 bytes
        System.arraycopy(segmentBytes, 14, buffer, 0, segmentBytes.size - 14)
      } else {
        val newBuffer = ByteArray(buffer.size + segmentBytes.size - 14)
        System.arraycopy(buffer, 0, newBuffer, 0, buffer.size)
        System.arraycopy(segmentBytes, 14, newBuffer, buffer.size, segmentBytes.size - 14)
        buffer = newBuffer
      }
    }
    if (buffer != null) extract(ByteArrayReader(buffer), metadata)
  }

  override fun extract(reader: RandomAccessReader, metadata: Metadata) {
    extract(reader, metadata, null)
  }

  fun extract(reader: RandomAccessReader, metadata: Metadata, parentDirectory: Directory?) {
    // TODO review whether the 'tagPtr' values below really do require RandomAccessReader or whether SequentialReader may be used instead
    val directory = IccDirectory()
    if (parentDirectory != null) directory.setParent(parentDirectory)
    try {
      val profileByteCount = reader.getInt32(IccDirectory.TAG_PROFILE_BYTE_COUNT)
      directory.setInt(IccDirectory.TAG_PROFILE_BYTE_COUNT, profileByteCount)
      // For these tags, the int value of the tag is in fact it's offset within the buffer.
      set4ByteString(directory, IccDirectory.TAG_CMM_TYPE, reader)
      setInt32(directory, IccDirectory.TAG_PROFILE_VERSION, reader)
      set4ByteString(directory, IccDirectory.TAG_PROFILE_CLASS, reader)
      set4ByteString(directory, IccDirectory.TAG_COLOR_SPACE, reader)
      set4ByteString(directory, IccDirectory.TAG_PROFILE_CONNECTION_SPACE, reader)
      setDate(directory, IccDirectory.TAG_PROFILE_DATETIME, reader)
      set4ByteString(directory, IccDirectory.TAG_SIGNATURE, reader)
      set4ByteString(directory, IccDirectory.TAG_PLATFORM, reader)
      setInt32(directory, IccDirectory.TAG_CMM_FLAGS, reader)
      set4ByteString(directory, IccDirectory.TAG_DEVICE_MAKE, reader)
      val temp = reader.getInt32(IccDirectory.TAG_DEVICE_MODEL)
      if (temp != 0) {
        if (temp <= 0x20202020) {
          directory.setInt(IccDirectory.TAG_DEVICE_MODEL, temp)
        } else {
          directory.setString(IccDirectory.TAG_DEVICE_MODEL, getStringFromInt32(temp))
        }
      }
      setInt32(directory, IccDirectory.TAG_RENDERING_INTENT, reader)
      setInt64(directory, IccDirectory.TAG_DEVICE_ATTR, reader)
      val xyz = floatArrayOf(
        reader.getS15Fixed16(IccDirectory.TAG_XYZ_VALUES),
        reader.getS15Fixed16(IccDirectory.TAG_XYZ_VALUES + 4),
        reader.getS15Fixed16(IccDirectory.TAG_XYZ_VALUES + 8)
      )
      directory.setObject(IccDirectory.TAG_XYZ_VALUES, xyz)
      // Process 'ICC tags'
      val tagCount = reader.getInt32(IccDirectory.TAG_TAG_COUNT)
      directory.setInt(IccDirectory.TAG_TAG_COUNT, tagCount)
      for (i in 0 until tagCount) {
        val pos: Int = IccDirectory.TAG_TAG_COUNT + 4 + i * 12
        val tagType = reader.getInt32(pos)
        val tagPtr = reader.getInt32(pos + 4)
        val tagLen = reader.getInt32(pos + 8)
        val b = reader.getBytes(tagPtr, tagLen)
        directory.setByteArray(tagType, b)
      }
    } catch (ex: IOException) {
      directory.addError("Exception reading ICC profile: ${ex.message}")
    }
    metadata.addDirectory(directory)
  }

  @Throws(IOException::class)
  private fun set4ByteString(directory: Directory, tagType: Int, reader: RandomAccessReader) {
    val i = reader.getInt32(tagType)
    if (i != 0) directory.setString(tagType, getStringFromInt32(i))
  }

  @Throws(IOException::class)
  private fun setInt32(directory: Directory, tagType: Int, reader: RandomAccessReader) {
    val i = reader.getInt32(tagType)
    if (i != 0) directory.setInt(tagType, i)
  }

  @Throws(IOException::class)
  private fun setInt64(directory: Directory, tagType: Int, reader: RandomAccessReader) {
    val l = reader.getInt64(tagType)
    if (l != 0L) directory.setLong(tagType, l)
  }

  @Throws(IOException::class)
  private fun setDate(directory: IccDirectory, tagType: Int, reader: RandomAccessReader) {
    val y = reader.getUInt16(tagType)
    val m = reader.getUInt16(tagType + 2)
    val d = reader.getUInt16(tagType + 4)
    val h = reader.getUInt16(tagType + 6)
    val M = reader.getUInt16(tagType + 8)
    val s = reader.getUInt16(tagType + 10)
    if (isValidDate(y, m - 1, d) && isValidTime(h, M, s)) {
      val dateString = "%04d:%02d:%02d %02d:%02d:%02d".format(y, m, d, h, M, s)
      directory.setString(tagType, dateString)
    } else {
      directory.addError(
        "ICC data describes an invalid date/time: year=%d month=%d day=%d hour=%d minute=%d second=%d".format(
        y, m, d, h, M, s))
    }
  }

  companion object {
    const val JPEG_SEGMENT_PREAMBLE = "ICC_PROFILE"
    fun getStringFromInt32(d: Int): String { // MSB
      val b = byteArrayOf(
        (d and -0x1000000 shr 24).toByte(),
        (d and 0x00FF0000 shr 16).toByte(),
        (d and 0x0000FF00 shr 8).toByte(),
        (d and 0x000000FF).toByte()
      )
      return String(b)
    }
  }
}
