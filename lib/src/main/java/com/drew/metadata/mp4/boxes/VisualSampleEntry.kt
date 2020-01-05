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
package com.drew.metadata.mp4.boxes

import com.drew.lang.SequentialReader
import com.drew.metadata.mp4.Mp4Dictionary
import com.drew.metadata.mp4.media.Mp4VideoDirectory

/**
 * ISO/IED 14496-12:2015 pg.156
 */
class VisualSampleEntry(reader: SequentialReader, box: Box) : SampleEntry(reader, box) {
  override var version: Int = reader.getInt16().toInt()
  var revisionLevel: Int
  var vendor: String
  var temporalQuality: Int
  var spatialQuality: Int
  var width: Int
  var height: Int
  var horizresolution: Long
  var vertresolution: Long
  var frameCount: Int
  var compressorname: String
  var depth: Int
  fun addMetadata(directory: Mp4VideoDirectory) {
    Mp4Dictionary.setLookup(Mp4VideoDirectory.TAG_COMPRESSION_TYPE, format, directory)
    directory.setInt(Mp4VideoDirectory.TAG_WIDTH, width)
    directory.setInt(Mp4VideoDirectory.TAG_HEIGHT, height)
    val compressorName = compressorname.trim { it <= ' ' }
    if (!compressorName.isEmpty()) {
      directory.setString(Mp4VideoDirectory.TAG_COMPRESSOR_NAME, compressorName)
    }
    directory.setInt(Mp4VideoDirectory.TAG_DEPTH, depth)
    // Calculate horizontal res
    val horizontalInteger = (horizresolution and -0x10000 shr 16.toDouble().toInt()).toDouble()
    val horizontalFraction = (horizresolution and 0xFFFF) / Math.pow(2.0, 4.0)
    directory.setDouble(Mp4VideoDirectory.TAG_HORIZONTAL_RESOLUTION, horizontalInteger + horizontalFraction)
    val verticalInteger = (vertresolution and -0x10000 shr 16.toDouble().toInt()).toDouble()
    val verticalFraction = (vertresolution and 0xFFFF) / Math.pow(2.0, 4.0)
    directory.setDouble(Mp4VideoDirectory.TAG_VERTICAL_RESOLUTION, verticalInteger + verticalFraction)
  }

  init {
    revisionLevel = reader.getInt16().toInt()
    vendor = reader.getString(4)
    temporalQuality = reader.getInt32()
    spatialQuality = reader.getInt32()
    width = reader.getUInt16()
    height = reader.getUInt16()
    horizresolution = reader.getUInt32()
    vertresolution = reader.getUInt32()
    reader.skip(4) // Reserved
    frameCount = reader.getUInt16()
    compressorname = reader.getString(32)
    depth = reader.getUInt16()
    reader.skip(2) // Pre-defined
  }
}
