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
import com.drew.metadata.mp4.media.Mp4SoundDirectory

/**
 * ISO/IED 14496-12:2015 pg.161
 */
class AudioSampleEntry(reader: SequentialReader, box: Box) : SampleEntry(reader, box) {
  var channelcount: Int
  var samplesize: Int
  var samplerate: Long
  fun addMetadata(directory: Mp4SoundDirectory) {
    Mp4Dictionary.setLookup(Mp4SoundDirectory.TAG_AUDIO_FORMAT, format, directory)
    directory.setInt(Mp4SoundDirectory.TAG_NUMBER_OF_CHANNELS, channelcount)
    directory.setInt(Mp4SoundDirectory.TAG_AUDIO_SAMPLE_SIZE, samplesize)
  }

  init {
    reader.skip(8) // Reserved
    channelcount = reader.getUInt16()
    samplesize = reader.getInt16().toInt()
    reader.skip(2) // Pre-defined
    reader.skip(2) // Reserved
    samplerate = reader.getUInt32()
    // ChannelLayout()
    // DownMix and/or DRC boxes
    // More boxes as needed
  }
}
