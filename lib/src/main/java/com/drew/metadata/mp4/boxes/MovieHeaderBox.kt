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

import com.drew.lang.Rational
import com.drew.lang.SequentialReader
import com.drew.lang.get1Jan1904EpochDate
import com.drew.metadata.mp4.Mp4Directory

/**
 * ISO/IED 14496-12:2015 pg.23
 */
class MovieHeaderBox(reader: SequentialReader, box: Box) : FullBox(reader, box) {
  protected var creationTime: Long = 0
  protected var modificationTime: Long = 0
  protected var timescale: Long = 0
  protected var duration: Long = 0
  protected var rate: Int
  protected var volume: Int
  protected var matrix: IntArray
  protected var nextTrackID: Long
  fun addMetadata(directory: Mp4Directory) { // Get creation/modification times
    directory.setDate(Mp4Directory.TAG_CREATION_TIME, get1Jan1904EpochDate(creationTime))
    directory.setDate(Mp4Directory.TAG_MODIFICATION_TIME, get1Jan1904EpochDate(modificationTime))
    // Get duration and time scale
    directory.setLong(Mp4Directory.TAG_DURATION, duration)
    directory.setLong(Mp4Directory.TAG_TIME_SCALE, timescale)
    directory.setRational(Mp4Directory.TAG_DURATION_SECONDS, Rational(duration, timescale))
    directory.setIntArray(Mp4Directory.TAG_TRANSFORMATION_MATRIX, matrix)
    // Calculate preferred rate fixed point
    val preferredRateInteger = (rate and -0x10000 shr 16.toDouble().toInt()).toDouble()
    val preferredRateFraction = (rate and 0x0000FFFF) / Math.pow(2.0, 4.0)
    directory.setDouble(Mp4Directory.TAG_PREFERRED_RATE, preferredRateInteger + preferredRateFraction)
    // Calculate preferred volume fixed point
    val preferredVolumeInteger = (volume and 0xFF00 shr 8.toDouble().toInt()).toDouble()
    val preferredVolumeFraction = (volume and 0x00FF) / Math.pow(2.0, 2.0)
    directory.setDouble(Mp4Directory.TAG_PREFERRED_VOLUME, preferredVolumeInteger + preferredVolumeFraction)
    directory.setLong(Mp4Directory.TAG_NEXT_TRACK_ID, nextTrackID)
  }

  init {
    if (version == 1) {
      creationTime = reader.getInt64()
      modificationTime = reader.getInt64()
      timescale = reader.getUInt32()
      duration = reader.getInt64()
    } else {
      creationTime = reader.getUInt32()
      modificationTime = reader.getUInt32()
      timescale = reader.getUInt32()
      duration = reader.getUInt32()
    }
    rate = reader.getInt32()
    volume = reader.getInt16().toInt()
    reader.skip(2) // Reserved
    reader.skip(8) // Reserved
    matrix = intArrayOf(reader.getInt32(),
      reader.getInt32(),
      reader.getInt32(),
      reader.getInt32(),
      reader.getInt32(),
      reader.getInt32(),
      reader.getInt32(),
      reader.getInt32(),
      reader.getInt32()
    )
    reader.skip(24) // Pre-defined
    nextTrackID = reader.getUInt32()
  }
}
