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
package com.drew.metadata.jpeg

import com.drew.imaging.jpeg.JpegSegmentMetadataReader
import com.drew.imaging.jpeg.JpegSegmentType
import com.drew.lang.SequentialByteArrayReader
import com.drew.lang.SequentialReader
import com.drew.metadata.Metadata
import java.io.IOException

/**
 * Decodes JPEG SOFn data, populating a [Metadata] object with tag values in a [JpegDirectory].
 *
 * @author Drew Noakes https://drewnoakes.com
 * @author Darrell Silver http://www.darrellsilver.com
 */
class JpegReader : JpegSegmentMetadataReader {
  // JpegSegmentType.SOF4,
  // JpegSegmentType.JPG,
  // JpegSegmentType.SOF12,
  // NOTE that some SOFn values do not exist
  override val segmentTypes: Iterable<JpegSegmentType>
    get() =// NOTE that some SOFn values do not exist
      listOf(
        JpegSegmentType.SOF0,
        JpegSegmentType.SOF1,
        JpegSegmentType.SOF2,
        JpegSegmentType.SOF3,
        // JpegSegmentType.SOF4,
        JpegSegmentType.SOF5,
        JpegSegmentType.SOF6,
        JpegSegmentType.SOF7,
        // JpegSegmentType.JPG,
        JpegSegmentType.SOF9,
        JpegSegmentType.SOF10,
        JpegSegmentType.SOF11,
        // JpegSegmentType.SOF12,
        JpegSegmentType.SOF13,
        JpegSegmentType.SOF14,
        JpegSegmentType.SOF15
      )

  override fun readJpegSegments(segments: Iterable<ByteArray>, metadata: Metadata, segmentType: JpegSegmentType) {
    for (segmentBytes in segments) {
      extract(segmentBytes, metadata, segmentType)
    }
  }

  fun extract(segmentBytes: ByteArray, metadata: Metadata, segmentType: JpegSegmentType) {
    val directory = JpegDirectory()
    metadata.addDirectory(directory)
    // The value of TAG_COMPRESSION_TYPE is determined by the segment type found
    directory.setInt(JpegDirectory.TAG_COMPRESSION_TYPE, segmentType.byteValue - JpegSegmentType.SOF0.byteValue)
    val reader: SequentialReader = SequentialByteArrayReader(segmentBytes)
    try {
      directory.setInt(JpegDirectory.TAG_DATA_PRECISION, reader.getUInt8().toInt())
      directory.setInt(JpegDirectory.TAG_IMAGE_HEIGHT, reader.getUInt16())
      directory.setInt(JpegDirectory.TAG_IMAGE_WIDTH, reader.getUInt16())
      val componentCount = reader.getUInt8()
      directory.setInt(JpegDirectory.TAG_NUMBER_OF_COMPONENTS, componentCount.toInt())
      // for each component, there are three bytes of data:
      // 1 - Component ID: 1 = Y, 2 = Cb, 3 = Cr, 4 = I, 5 = Q
      // 2 - Sampling factors: bit 0-3 vertical, 4-7 horizontal
      // 3 - Quantization table number
      for (i in 0 until componentCount.toInt()) {
        val componentId = reader.getUInt8().toInt()
        val samplingFactorByte = reader.getUInt8().toInt()
        val quantizationTableNumber = reader.getUInt8().toInt()
        val component = JpegComponent(componentId, samplingFactorByte, quantizationTableNumber)
        directory.setObject(JpegDirectory.TAG_COMPONENT_DATA_1 + i, component)
      }
    } catch (ex: IOException) {
      ex.message?.let {
        directory.addError(it)
      }
    }
  }
}
