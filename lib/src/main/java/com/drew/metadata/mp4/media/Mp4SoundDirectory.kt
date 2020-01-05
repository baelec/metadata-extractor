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
package com.drew.metadata.mp4.media

import java.util.*

class Mp4SoundDirectory : Mp4MediaDirectory() {
  companion object {
    // Sound Sample Description Atom
    const val TAG_AUDIO_FORMAT = 301
    const val TAG_NUMBER_OF_CHANNELS = 302
    const val TAG_AUDIO_SAMPLE_SIZE = 303
    const val TAG_AUDIO_SAMPLE_RATE = 304
    const val TAG_SOUND_BALANCE = 305
    protected val tagNameMap = HashMap<Int, String>()

    init {
      addMp4MediaTags(this.tagNameMap)
      tagNameMap[TAG_AUDIO_FORMAT] = "Format"
      tagNameMap[TAG_NUMBER_OF_CHANNELS] = "Number of Channels"
      tagNameMap[TAG_AUDIO_SAMPLE_SIZE] = "Sample Size"
      tagNameMap[TAG_AUDIO_SAMPLE_RATE] = "Sample Rate"
      tagNameMap[TAG_SOUND_BALANCE] = "Balance"
    }
  }

  override val name: String
    get() = "MP4 Sound"

  init {
    setDescriptor(Mp4SoundDescriptor(this))
  }
}
