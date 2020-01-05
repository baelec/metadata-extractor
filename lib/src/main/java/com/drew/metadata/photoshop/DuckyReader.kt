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
package com.drew.metadata.photoshop

import com.drew.imaging.jpeg.JpegSegmentMetadataReader
import com.drew.imaging.jpeg.JpegSegmentType
import com.drew.lang.SequentialByteArrayReader
import com.drew.lang.SequentialReader
import com.drew.lang.UTF_16BE
import com.drew.metadata.Metadata
import java.io.IOException

/**
 * Reads Photoshop "ducky" segments, created during Save-for-Web.
 *
 * @author Drew Noakes https://drewnoakes.com
 */
class DuckyReader : JpegSegmentMetadataReader {
  override val segmentTypes: Iterable<JpegSegmentType>
    get() = listOf(JpegSegmentType.APPC)

  override fun readJpegSegments(segments: Iterable<ByteArray>, metadata: Metadata, segmentType: JpegSegmentType) {
    val preambleLength = JPEG_SEGMENT_PREAMBLE.length
    for (segmentBytes in segments) { // Ensure data starts with the necessary preamble
      if (segmentBytes.size < preambleLength || JPEG_SEGMENT_PREAMBLE != String(segmentBytes, 0, preambleLength)) continue
      extract(
        SequentialByteArrayReader(segmentBytes, preambleLength),
        metadata)
    }
  }

  fun extract(reader: SequentialReader, metadata: Metadata) {
    val directory = DuckyDirectory()
    metadata.addDirectory(directory)
    try {
      while (true) {
        val tag = reader.getUInt16()
        // End of Segment is marked with zero
        if (tag == 0) break
        val length = reader.getUInt16()
        when (tag) {
          DuckyDirectory.TAG_QUALITY -> {
            if (length != 4) {
              directory.addError("Unexpected length for the quality tag")
              return
            }
            directory.setInt(tag, reader.getInt32())
          }
          DuckyDirectory.TAG_COMMENT, DuckyDirectory.TAG_COPYRIGHT -> {
            reader.skip(4)
            directory.setStringValue(tag, reader.getStringValue(length - 4, UTF_16BE))
          }
          else -> {
            // Unexpected tag
            directory.setByteArray(tag, reader.getBytes(length))
          }
        }
      }
    } catch (e: IOException) {
      e.message?.let {
        directory.addError(it)
      }
    }
  }

  companion object {
    private const val JPEG_SEGMENT_PREAMBLE = "Ducky"
  }
}
