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

import com.drew.lang.Rational
import com.drew.lang.SequentialReader
import com.drew.lang.get1Jan1904EpochDate
import com.drew.metadata.mov.QuickTimeDirectory

/**
 * https://developer.apple.com/library/content/documentation/QuickTime/QTFF/QTFFChap2/qtff2.html#//apple_ref/doc/uid/TP40000939-CH204-BBCGFGJG
 *
 * @author Payton Garland
 */
class MovieHeaderAtom(reader: SequentialReader, atom: Atom) : FullAtom(reader, atom) {
  private val creationTime: Long = reader.getUInt32()
  private val modificationTime: Long = reader.getUInt32()
  private val timescale: Long = reader.getUInt32()
  private val duration: Long = reader.getUInt32()
  private val preferredRate: Int = reader.getInt32()
  private val preferredVolume: Int = reader.getInt16().toInt()
  // TODO this matrix data is not currently used anywhere
  private val matrixStructure: IntArray
  private val previewTime: Long
  private val previewDuration: Long
  private val posterTime: Long
  private val selectionTime: Long
  private val selectionDuration: Long
  private val currentTime: Long
  private val nextTrackID: Long
  fun addMetadata(directory: QuickTimeDirectory) { // Get creation/modification times
    directory.setDate(QuickTimeDirectory.TAG_CREATION_TIME, get1Jan1904EpochDate(creationTime))
    directory.setDate(QuickTimeDirectory.TAG_MODIFICATION_TIME, get1Jan1904EpochDate(modificationTime))
    // Get duration and time scale
    directory.setLong(QuickTimeDirectory.TAG_DURATION, duration)
    directory.setLong(QuickTimeDirectory.TAG_TIME_SCALE, timescale)
    directory.setRational(QuickTimeDirectory.TAG_DURATION_SECONDS, Rational(duration, timescale))
    // Calculate preferred rate fixed point
    val preferredRateInteger = (preferredRate and -0x10000 shr 16.toDouble().toInt()).toDouble()
    val preferredRateFraction = (preferredRate and 0x0000FFFF) / 16.0
    directory.setDouble(QuickTimeDirectory.TAG_PREFERRED_RATE, preferredRateInteger + preferredRateFraction)
    // Calculate preferred volume fixed point
    val preferredVolumeInteger = (preferredVolume and 0xFF00 shr 8.toDouble().toInt()).toDouble()
    val preferredVolumeFraction = (preferredVolume and 0x00FF) / 8.0
    directory.setDouble(QuickTimeDirectory.TAG_PREFERRED_VOLUME, preferredVolumeInteger + preferredVolumeFraction)
    directory.setLong(QuickTimeDirectory.TAG_PREVIEW_TIME, previewTime)
    directory.setLong(QuickTimeDirectory.TAG_PREVIEW_DURATION, previewDuration)
    directory.setLong(QuickTimeDirectory.TAG_POSTER_TIME, posterTime)
    directory.setLong(QuickTimeDirectory.TAG_SELECTION_TIME, selectionTime)
    directory.setLong(QuickTimeDirectory.TAG_SELECTION_DURATION, selectionDuration)
    directory.setLong(QuickTimeDirectory.TAG_CURRENT_TIME, currentTime)
    directory.setLong(QuickTimeDirectory.TAG_NEXT_TRACK_ID, nextTrackID)
  }

  init {
    reader.skip(10) // Reserved
    matrixStructure = intArrayOf(
      reader.getInt32(),
      reader.getInt32(),
      reader.getInt32(),
      reader.getInt32(),
      reader.getInt32(),
      reader.getInt32(),
      reader.getInt32(),
      reader.getInt32(),
      reader.getInt32()
    )
    previewTime = reader.getUInt32()
    previewDuration = reader.getUInt32()
    posterTime = reader.getUInt32()
    selectionTime = reader.getUInt32()
    selectionDuration = reader.getUInt32()
    currentTime = reader.getUInt32()
    nextTrackID = reader.getUInt32()
  }
}
