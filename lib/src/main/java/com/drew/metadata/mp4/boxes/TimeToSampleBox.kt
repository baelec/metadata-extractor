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
import com.drew.metadata.mp4.Mp4Context
import com.drew.metadata.mp4.media.Mp4SoundDirectory
import com.drew.metadata.mp4.media.Mp4VideoDirectory
import java.util.*

/**
 * ISO/IED 14496-12:2015 pg.37
 */
class TimeToSampleBox(reader: SequentialReader, box: Box) : FullBox(reader, box) {
  private val entryCount: Long = reader.getUInt32()
  private val entries: ArrayList<EntryCount> = ArrayList()
  fun addMetadata(directory: Mp4VideoDirectory, context: Mp4Context) {
    var sampleCount = 0f
    for (ec in entries) {
      sampleCount += ec.sampleCount.toFloat()
    }
    val timeScale = context.timeScale
    val duration = context.duration
    if (timeScale != null && duration != null) {
      val frameRate = timeScale.toFloat() / (duration.toFloat() / sampleCount)
      directory.setFloat(Mp4VideoDirectory.TAG_FRAME_RATE, frameRate)
    }
  }

  fun addMetadata(directory: Mp4SoundDirectory, context: Mp4Context) {
    context.timeScale?.let {
      directory.setDouble(Mp4SoundDirectory.TAG_AUDIO_SAMPLE_RATE, it.toDouble())
    }
  }

  internal class EntryCount(var sampleCount: Long, var sampleDelta: Long)

  init {
    for (i in 0 until entryCount) {
      entries.add(EntryCount(reader.getUInt32(), reader.getUInt32()))
    }
  }
}
