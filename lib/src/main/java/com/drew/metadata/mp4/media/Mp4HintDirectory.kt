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

class Mp4HintDirectory : Mp4MediaDirectory() {
  companion object {
    const val TAG_MAX_PDU_SIZE = 101
    const val TAG_AVERAGE_PDU_SIZE = 102
    const val TAG_MAX_BITRATE = 103
    const val TAG_AVERAGE_BITRATE = 104
    private val tagNameMap = HashMap<Int, String>()

    init {
      addMp4MediaTags(this.tagNameMap)
      tagNameMap[TAG_MAX_PDU_SIZE] = "Max PDU Size"
      tagNameMap[TAG_AVERAGE_PDU_SIZE] = "Average PDU Size"
      tagNameMap[TAG_MAX_BITRATE] = "Max Bitrate"
      tagNameMap[TAG_AVERAGE_BITRATE] = "Average Bitrate"
    }
  }

  override val name: String
    get() = "MP4 Hint"

  init {
    setDescriptor(Mp4HintDescriptor(this))
  }
}
