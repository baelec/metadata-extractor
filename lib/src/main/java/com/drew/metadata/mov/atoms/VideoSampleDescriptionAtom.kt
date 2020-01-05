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
package com.drew.metadata.mov.atoms

import com.drew.lang.SequentialReader
import com.drew.metadata.mov.QuickTimeDictionary.setLookup
import com.drew.metadata.mov.atoms.VideoSampleDescriptionAtom.VideoSampleDescription
import com.drew.metadata.mov.media.QuickTimeVideoDirectory
import java.io.IOException
import kotlin.math.pow

/**
 * https://developer.apple.com/library/content/documentation/QuickTime/QTFF/QTFFChap3/qtff3.html#//apple_ref/doc/uid/TP40000939-CH205-BBCGICBJ
 *
 * @author Payton Garland
 */
class VideoSampleDescriptionAtom(reader: SequentialReader, atom: Atom) : SampleDescriptionAtom<VideoSampleDescription>(reader, atom) {
  @Throws(IOException::class)
  override fun getSampleDescription(reader: SequentialReader): VideoSampleDescription? {
    return VideoSampleDescription(reader)
  }

  fun addMetadata(directory: QuickTimeVideoDirectory) {
    val sampleDescription = sampleDescriptions[0] ?: return
    setLookup(QuickTimeVideoDirectory.TAG_VENDOR, sampleDescription.vendor, directory)
    setLookup(QuickTimeVideoDirectory.TAG_COMPRESSION_TYPE, sampleDescription.dataFormat, directory)
    directory.setLong(QuickTimeVideoDirectory.TAG_TEMPORAL_QUALITY, sampleDescription.temporalQuality)
    directory.setLong(QuickTimeVideoDirectory.TAG_SPATIAL_QUALITY, sampleDescription.spatialQuality)
    directory.setInt(QuickTimeVideoDirectory.TAG_WIDTH, sampleDescription.width)
    directory.setInt(QuickTimeVideoDirectory.TAG_HEIGHT, sampleDescription.height)
    val compressorName = sampleDescription.compressorName.trim { it <= ' ' }
    if (compressorName.isNotEmpty()) {
      directory.setString(QuickTimeVideoDirectory.TAG_COMPRESSOR_NAME, compressorName)
    }
    directory.setInt(QuickTimeVideoDirectory.TAG_DEPTH, sampleDescription.depth)
    directory.setInt(QuickTimeVideoDirectory.TAG_COLOR_TABLE, sampleDescription.colorTableID)
    val horizontalInteger = (sampleDescription.horizontalResolution and -0x10000 shr 16.toDouble().toInt()).toDouble()
    val horizontalFraction = (sampleDescription.horizontalResolution and 0xFFFF) / 2.0.pow(4.0)
    directory.setDouble(QuickTimeVideoDirectory.TAG_HORIZONTAL_RESOLUTION, horizontalInteger + horizontalFraction)
    val verticalInteger = (sampleDescription.verticalResolution and -0x10000 shr 16.toDouble().toInt()).toDouble()
    val verticalFraction = (sampleDescription.verticalResolution and 0xFFFF) / 2.0.pow(4.0)
    directory.setDouble(QuickTimeVideoDirectory.TAG_VERTICAL_RESOLUTION, verticalInteger + verticalFraction)
  }

  class VideoSampleDescription(reader: SequentialReader) : SampleDescription(reader) {
    var version: Int = reader.getUInt16()
    var revisionLevel: Int = reader.getUInt16()
    var vendor: String = reader.getString(4)
    var temporalQuality: Long = reader.getUInt32()
    var spatialQuality: Long = reader.getUInt32()
    var width: Int = reader.getUInt16()
    var height: Int = reader.getUInt16()
    var horizontalResolution: Long = reader.getUInt32()
    var verticalResolution: Long = reader.getUInt32()
    var dataSize: Long = reader.getUInt32()
    var frameCount: Int = reader.getUInt16()
    var compressorName: String = reader.getString(32)
    var depth: Int = reader.getUInt16()
    var colorTableID: Int = reader.getInt16().toInt()
  }
}
