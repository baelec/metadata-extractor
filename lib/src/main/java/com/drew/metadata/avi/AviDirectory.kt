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
package com.drew.metadata.avi

import com.drew.metadata.Directory
import java.util.*

/**
 * Holds basic metadata from Avi files
 *
 * @author Payton Garland
 */
class AviDirectory : Directory() {
  override val tagNameMap: HashMap<Int, String> = Companion.tagNameMap

  companion object {
    const val TAG_FRAMES_PER_SECOND = 1
    const val TAG_SAMPLES_PER_SECOND = 2
    const val TAG_DURATION = 3
    const val TAG_VIDEO_CODEC = 4
    const val TAG_AUDIO_CODEC = 5
    const val TAG_WIDTH = 6
    const val TAG_HEIGHT = 7
    const val TAG_STREAMS = 8
    const val TAG_DATETIME_ORIGINAL = 320
    const val CHUNK_STREAM_HEADER = "strh"
    const val CHUNK_MAIN_HEADER = "avih"
    const val CHUNK_DATETIME_ORIGINAL = "IDIT"
    const val LIST_HEADER = "hdrl"
    const val LIST_STREAM_HEADER = "strl"
    const val FORMAT = "AVI "
    private val tagNameMap = HashMap<Int, String>()

    init {
      tagNameMap[TAG_FRAMES_PER_SECOND] = "Frames Per Second"
      tagNameMap[TAG_SAMPLES_PER_SECOND] = "Samples Per Second"
      tagNameMap[TAG_DURATION] = "Duration"
      tagNameMap[TAG_VIDEO_CODEC] = "Video Codec"
      tagNameMap[TAG_AUDIO_CODEC] = "Audio Codec"
      tagNameMap[TAG_WIDTH] = "Width"
      tagNameMap[TAG_HEIGHT] = "Height"
      tagNameMap[TAG_STREAMS] = "Stream Count"
      tagNameMap[TAG_DATETIME_ORIGINAL] = "Datetime Original"
    }
  }

  override val name: String
    get() = "AVI"

  init {
    setDescriptor(AviDescriptor(this))
  }
}
