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
package com.drew.metadata.mp3

import com.drew.metadata.Directory
import java.util.*

/**
 * @author Payton Garland
 */
class Mp3Directory : Directory() {
  override val tagNameMap = Companion.tagNameMap
  companion object {
    const val TAG_ID = 1
    const val TAG_LAYER = 2
    const val TAG_BITRATE = 3
    const val TAG_FREQUENCY = 4
    const val TAG_MODE = 5
    const val TAG_EMPHASIS = 6
    const val TAG_COPYRIGHT = 7
    const val TAG_FRAME_SIZE = 8
    protected val tagNameMap = HashMap<Int, String>()

    init {
      tagNameMap[TAG_ID] = "ID"
      tagNameMap[TAG_LAYER] = "Layer"
      tagNameMap[TAG_BITRATE] = "Bitrate"
      tagNameMap[TAG_FREQUENCY] = "Frequency"
      tagNameMap[TAG_MODE] = "Mode"
      tagNameMap[TAG_EMPHASIS] = "Emphasis Method"
      tagNameMap[TAG_COPYRIGHT] = "Copyright"
      tagNameMap[TAG_FRAME_SIZE] = "Frame Size"
    }
  }

  override val name: String
    get() = "MP3"

  init {
    setDescriptor(Mp3Descriptor(this))
  }
}
