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
package com.drew.metadata.xmp

import com.adobe.internal.xmp.XMPException
import com.adobe.internal.xmp.XMPMeta
import com.adobe.internal.xmp.XMPMetaFactory
import com.adobe.internal.xmp.impl.ByteBuffer
import com.adobe.internal.xmp.options.ParseOptions
import com.adobe.internal.xmp.properties.XMPPropertyInfo
import com.drew.imaging.jpeg.JpegSegmentMetadataReader
import com.drew.imaging.jpeg.JpegSegmentType
import com.drew.lang.SequentialByteArrayReader
import com.drew.lang.SequentialReader
import com.drew.metadata.Directory
import com.drew.metadata.Metadata
import com.drew.metadata.StringValue
import java.io.IOException
import java.util.*

/**
 * Extracts XMP data from JPEG APP1 segments.
 *
 *
 * Note that XMP uses a namespace and path format for identifying values, which does not map to metadata-extractor's
 * integer based tag identifiers. Therefore, XMP data is extracted and exposed via [XmpDirectory.getXMPMeta]
 * which returns an instance of Adobe's [XMPMeta] which exposes the full XMP data set.
 *
 *
 * The extraction is done with Adobe's XmpCore-Library (XMP-Toolkit)
 * Copyright (c) 1999 - 2007, Adobe Systems Incorporated All rights reserved.
 *
 * @author Torsten Skadell
 * @author Drew Noakes https://drewnoakes.com
 * @author https://github.com/bezineb5
 */
class XmpReader : JpegSegmentMetadataReader {
  override val segmentTypes: Iterable<JpegSegmentType>
    get() = listOf(JpegSegmentType.APP1)

  /**
   * Version specifically for dealing with XMP found in JPEG segments. This form of XMP has a peculiar preamble, which
   * must be removed before parsing the XML.
   *
   * @param segments The byte array from which the metadata should be extracted.
   * @param metadata The [Metadata] object into which extracted values should be merged.
   * @param segmentType The [JpegSegmentType] being read.
   */
  override fun readJpegSegments(segments: Iterable<ByteArray>, metadata: Metadata, segmentType: JpegSegmentType) {
    val preambleLength = XMP_JPEG_PREAMBLE.length
    val extensionPreambleLength = XMP_EXTENSION_JPEG_PREAMBLE.length
    var extendedXMPGUID: String? = null
    var extendedXMPBuffer: ByteArray? = null
    for (segmentBytes in segments) { // XMP in a JPEG file has an identifying preamble which is not valid XML
      if (segmentBytes.size >= preambleLength) { // NOTE we expect the full preamble here, but some images (such as that reported on GitHub #102)
        // start with "XMP\0://ns.adobe.com/xap/1.0/" which appears to be an error but is easily recovered
        // from. In such cases, the actual XMP data begins at the same offset.
        if (XMP_JPEG_PREAMBLE.equals(String(segmentBytes, 0, preambleLength), ignoreCase = true) ||
          "XMP".equals(String(segmentBytes, 0, 3), ignoreCase = true)) {
          val xmlBytes = ByteArray(segmentBytes.size - preambleLength)
          System.arraycopy(segmentBytes, preambleLength, xmlBytes, 0, xmlBytes.size)
          extract(xmlBytes, metadata)
          // Check in the Standard XMP if there should be a Extended XMP part in other chunks.
          extendedXMPGUID = getExtendedXMPGUID(metadata)
          continue
        }
      }
      // If we know that there's Extended XMP chunks, look for them.
      if (extendedXMPGUID != null && segmentBytes.size >= extensionPreambleLength &&
        XMP_EXTENSION_JPEG_PREAMBLE.equals(String(segmentBytes, 0, extensionPreambleLength), ignoreCase = true)) {
        extendedXMPBuffer = processExtendedXMPChunk(metadata, segmentBytes, extendedXMPGUID, extendedXMPBuffer)
      }
    }
    // Now that the Extended XMP chunks have been concatenated, let's parse and merge with the Standard XMP.
    extendedXMPBuffer?.let { extract(it, metadata) }
  }
  /**
   * Performs the XMP data extraction, adding found values to the specified instance of [Metadata].
   *
   *
   * The extraction is done with Adobe's XMPCore library.
   */
  /**
   * Performs the XMP data extraction, adding found values to the specified instance of [Metadata].
   *
   *
   * The extraction is done with Adobe's XMPCore library.
   */
  @JvmOverloads
  fun extract(xmpBytes: ByteArray, metadata: Metadata, parentDirectory: Directory? = null) {
    extract(xmpBytes, 0, xmpBytes.size, metadata, parentDirectory)
  }

  /**
   * Performs the XMP data extraction, adding found values to the specified instance of [Metadata].
   *
   *
   * The extraction is done with Adobe's XMPCore library.
   */
  fun extract(xmpBytes: ByteArray, offset: Int, length: Int, metadata: Metadata, parentDirectory: Directory?) {
    val directory = XmpDirectory()
    if (parentDirectory != null) directory.setParent(parentDirectory)
    try {
      val xmpMeta: XMPMeta
      // If all xmpBytes are requested, no need to make a new ByteBuffer
      xmpMeta = if (offset == 0 && length == xmpBytes.size) {
        XMPMetaFactory.parseFromBuffer(xmpBytes, PARSE_OPTIONS)
      } else {
        val buffer = ByteBuffer(xmpBytes, offset, length)
        XMPMetaFactory.parse(buffer.byteStream, PARSE_OPTIONS)
      }
      directory.xmpMeta = xmpMeta
    } catch (e: XMPException) {
      directory.addError("Error processing XMP data: " + e.message)
    }
    if (!directory.isEmpty) metadata.addDirectory(directory)
  }

  /**
   * Performs the XMP data extraction, adding found values to the specified instance of [Metadata].
   *
   *
   * The extraction is done with Adobe's XMPCore library.
   */
  fun extract(xmpString: StringValue, metadata: Metadata) {
    extract(xmpString.bytes, metadata, null)
  }
  /**
   * Performs the XMP data extraction, adding found values to the specified instance of [Metadata].
   *
   *
   * The extraction is done with Adobe's XMPCore library.
   */
  /**
   * Performs the XMP data extraction, adding found values to the specified instance of [Metadata].
   *
   *
   * The extraction is done with Adobe's XMPCore library.
   */
  @JvmOverloads
  fun extract(xmpString: String, metadata: Metadata, parentDirectory: Directory? = null) {
    val directory = XmpDirectory()
    if (parentDirectory != null) directory.setParent(parentDirectory)
    try {
      val xmpMeta = XMPMetaFactory.parseFromString(xmpString, PARSE_OPTIONS)
      directory.xmpMeta = xmpMeta
    } catch (e: XMPException) {
      directory.addError("Error processing XMP data: " + e.message)
    }
    if (!directory.isEmpty) metadata.addDirectory(directory)
  }

  companion object {
    private const val XMP_JPEG_PREAMBLE = "http://ns.adobe.com/xap/1.0/\u0000"
    private const val XMP_EXTENSION_JPEG_PREAMBLE = "http://ns.adobe.com/xmp/extension/\u0000"
    private const val SCHEMA_XMP_NOTES = "http://ns.adobe.com/xmp/note/"
    private const val ATTRIBUTE_EXTENDED_XMP = "xmpNote:HasExtendedXMP"
    // Limit photoshop:DocumentAncestors node as it can reach over 100000 items and make parsing extremely slow.
// This is not a typical value but it may happen https://forums.adobe.com/thread/2081839
    private val PARSE_OPTIONS = ParseOptions().setXMPNodesToLimit(Collections.singletonMap("photoshop:DocumentAncestors", 1000))
    /**
     * Extended XMP constants
     */
    private const val EXTENDED_XMP_GUID_LENGTH = 32
    private const val EXTENDED_XMP_INT_LENGTH = 4
    /**
     * Determine if there is an extended XMP section based on the standard XMP part.
     * The xmpNote:HasExtendedXMP attribute contains the GUID of the Extended XMP chunks.
     */
    private fun getExtendedXMPGUID(metadata: Metadata): String? {
      val xmpDirectories = metadata.getDirectoriesOfType(XmpDirectory::class.java)
      for (directory in xmpDirectories) {
        val xmpMeta = directory.xmpMeta
        try {
          val itr = xmpMeta.iterator(SCHEMA_XMP_NOTES, null, null) ?: continue
          while (itr.hasNext()) {
            val pi = itr.next() as XMPPropertyInfo
            if (ATTRIBUTE_EXTENDED_XMP == pi.path) {
              return pi.value
            }
          }
        } catch (e: XMPException) { // Fail silently here: we had a reading issue, not a decoding issue.
        }
      }
      return null
    }

    /**
     * Process an Extended XMP chunk. It will read the bytes from segmentBytes and validates that the GUID the requested one.
     * It will progressively fill the buffer with each chunk.
     * The format is specified in this document:
     * http://www.adobe.com/content/dam/Adobe/en/devnet/xmp/pdfs/XMPSpecificationPart3.pdf
     * at page 19
     */
    private fun processExtendedXMPChunk(metadata: Metadata, segmentBytes: ByteArray, extendedXMPGUID: String, extendedXMPBuffer: ByteArray?): ByteArray? {
      var extendedXMPBuffer = extendedXMPBuffer
      val extensionPreambleLength = XMP_EXTENSION_JPEG_PREAMBLE.length
      val segmentLength = segmentBytes.size
      val totalOffset = extensionPreambleLength + EXTENDED_XMP_GUID_LENGTH + EXTENDED_XMP_INT_LENGTH + EXTENDED_XMP_INT_LENGTH
      if (segmentLength >= totalOffset) {
        try { /*
                 * The chunk contains:
                 * - A null-terminated signature string of "http://ns.adobe.com/xmp/extension/".
                 * - A 128-bit GUID stored as a 32-byte ASCII hex string, capital A-F, no null termination.
                 *   The GUID is a 128-bit MD5 digest of the full ExtendedXMP serialization.
                 * - The full length of the ExtendedXMP serialization as a 32-bit unsigned integer
                 * - The offset of this portion as a 32-bit unsigned integer
                 * - The portion of the ExtendedXMP
                 */
          val reader: SequentialReader = SequentialByteArrayReader(segmentBytes)
          reader.skip(extensionPreambleLength.toLong())
          val segmentGUID = reader.getString(EXTENDED_XMP_GUID_LENGTH)
          if (extendedXMPGUID == segmentGUID) {
            val fullLength = reader.getUInt32().toInt()
            val chunkOffset = reader.getUInt32().toInt()
            if (extendedXMPBuffer == null) extendedXMPBuffer = ByteArray(fullLength)
            if (extendedXMPBuffer.size == fullLength) {
              System.arraycopy(segmentBytes, totalOffset, extendedXMPBuffer, chunkOffset, segmentLength - totalOffset)
            } else {
              val directory = XmpDirectory()
              directory.addError(String.format("Inconsistent length for the Extended XMP buffer: %d instead of %d", fullLength, extendedXMPBuffer.size))
              metadata.addDirectory(directory)
            }
          }
        } catch (ex: IOException) {
          val directory = XmpDirectory()
          ex.message?.let {
            directory.addError(it)
            metadata.addDirectory(directory)
          }
        }
      }
      return extendedXMPBuffer
    }
  }
}
