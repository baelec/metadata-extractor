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
package com.drew.metadata.jfif

import com.drew.imaging.jpeg.JpegSegmentMetadataReader
import com.drew.imaging.jpeg.JpegSegmentType
import com.drew.lang.ByteArrayReader
import com.drew.lang.RandomAccessReader
import com.drew.metadata.Metadata
import com.drew.metadata.MetadataReader
import java.io.IOException

/**
 * Reader for JFIF data, found in the APP0 JPEG segment.
 *
 *
 *  * http://en.wikipedia.org/wiki/JPEG_File_Interchange_Format
 *  * http://www.w3.org/Graphics/JPEG/jfif3.pdf
 *
 *
 * @author Yuri Binev, Drew Noakes, Markus Meyer
 */
class JfifReader : JpegSegmentMetadataReader, MetadataReader {
  override val segmentTypes: Iterable<JpegSegmentType>
    get() = listOf(JpegSegmentType.APP0)

  override fun readJpegSegments(segments: Iterable<ByteArray>, metadata: Metadata, segmentType: JpegSegmentType) {
    for (segmentBytes in segments) { // Skip segments not starting with the required header
      if (segmentBytes.size >= PREAMBLE.length && PREAMBLE == String(segmentBytes, 0, PREAMBLE.length)) extract(ByteArrayReader(segmentBytes), metadata)
    }
  }

  /**
   * Performs the Jfif data extraction, adding found values to the specified
   * instance of [Metadata].
   */
  override fun extract(reader: RandomAccessReader, metadata: Metadata) {
    val directory = JfifDirectory()
    metadata.addDirectory(directory)
    try { // For JFIF, the tag number is also the offset into the segment
      directory.setInt(JfifDirectory.TAG_VERSION, reader.getUInt16(JfifDirectory.TAG_VERSION))
      directory.setInt(JfifDirectory.TAG_UNITS, reader.getUInt8(JfifDirectory.TAG_UNITS).toInt())
      directory.setInt(JfifDirectory.TAG_RESX, reader.getUInt16(JfifDirectory.TAG_RESX))
      directory.setInt(JfifDirectory.TAG_RESY, reader.getUInt16(JfifDirectory.TAG_RESY))
      directory.setInt(JfifDirectory.TAG_THUMB_WIDTH, reader.getUInt8(JfifDirectory.TAG_THUMB_WIDTH).toInt())
      directory.setInt(JfifDirectory.TAG_THUMB_HEIGHT, reader.getUInt8(JfifDirectory.TAG_THUMB_HEIGHT).toInt())
    } catch (me: IOException) {
      me.message?.let {
        directory.addError(it)
      }
    }
  }

  companion object {
    const val PREAMBLE = "JFIF"
  }
}
