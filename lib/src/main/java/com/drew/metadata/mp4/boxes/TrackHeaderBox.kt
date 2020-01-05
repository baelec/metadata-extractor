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
import com.drew.metadata.mp4.Mp4Directory
import kotlin.math.abs
import kotlin.math.atan2

/**
 * ISO/IED 14496-12:2005 pg.17-18
 */
class TrackHeaderBox(reader: SequentialReader, box: Box) : FullBox(reader, box) {
  var creationTime: Long = 0
  var modificationTime: Long = 0
  var trackID: Long = 0
  var duration: Long = 0
  var layer: Int
  var alternateGroup: Int
  var volume: Int
  var matrix = IntArray(9)
  var width: Long
  var height: Long
  fun addMetadata(directory: Mp4Directory) {
    if (width != 0L && height != 0L && directory.getDoubleObject(Mp4Directory.TAG_ROTATION) == null) {
      val x = matrix[1] + matrix[4]
      val y = matrix[0] + matrix[3]
      val theta = atan2(y.toDouble(), x.toDouble())
      var degree = Math.toDegrees(theta)
      degree -= 45.0
      directory.setDouble(Mp4Directory.TAG_ROTATION, abs(degree))
    }
  }

  init {
    if (version == 1) {
      creationTime = reader.getInt64()
      modificationTime = reader.getInt64()
      trackID = reader.getInt32().toLong()
      reader.skip(4) // reserved
      duration = reader.getInt64()
    } else {
      creationTime = reader.getUInt32()
      modificationTime = reader.getUInt32()
      trackID = reader.getUInt32()
      reader.skip(4)
      duration = reader.getUInt32()
    }
    reader.skip(8) // reserved
    layer = reader.getInt16().toInt()
    alternateGroup = reader.getInt16().toInt()
    volume = reader.getInt16().toInt()
    reader.skip(2) // reserved
    for (i in 0..8) {
      matrix[i] = reader.getInt32()
    }
    width = reader.getInt32().toLong()
    height = reader.getInt32().toLong()
  }
}
