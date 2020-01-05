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
package com.drew.metadata.mp4

import com.drew.metadata.Directory
import java.util.*

open class Mp4Directory : Directory() {
  override val tagNameMap: HashMap<Int, String> = Companion.tagNameMap
  companion object {
    const val TAG_CREATION_TIME = 0x0100
    const val TAG_MODIFICATION_TIME = 0x0101
    const val TAG_TIME_SCALE = 0x0102
    const val TAG_DURATION = 0x0103
    const val TAG_DURATION_SECONDS = 0x0104
    const val TAG_PREFERRED_RATE = 0x0105
    const val TAG_PREFERRED_VOLUME = 0x0106
    const val TAG_PREVIEW_TIME = 0x0108
    const val TAG_PREVIEW_DURATION = 0x0109
    const val TAG_POSTER_TIME = 0x010A
    const val TAG_SELECTION_TIME = 0x010B
    const val TAG_SELECTION_DURATION = 0x010C
    const val TAG_CURRENT_TIME = 0x010D
    const val TAG_NEXT_TRACK_ID = 0x010E
    const val TAG_TRANSFORMATION_MATRIX = 0x010F
    const val TAG_ROTATION = 0x0200
    const val TAG_MEDIA_TIME_SCALE = 0x0306
    const val TAG_MAJOR_BRAND = 1
    const val TAG_MINOR_VERSION = 2
    const val TAG_COMPATIBLE_BRANDS = 3
    protected val tagNameMap = HashMap<Int, String>()

    init {
      tagNameMap[TAG_MAJOR_BRAND] = "Major Brand"
      tagNameMap[TAG_MINOR_VERSION] = "Minor Version"
      tagNameMap[TAG_COMPATIBLE_BRANDS] = "Compatible Brands"
      tagNameMap[TAG_CREATION_TIME] = "Creation Time"
      tagNameMap[TAG_MODIFICATION_TIME] = "Modification Time"
      tagNameMap[TAG_TIME_SCALE] = "Media Time Scale"
      tagNameMap[TAG_DURATION] = "Duration"
      tagNameMap[TAG_DURATION_SECONDS] = "Duration in Seconds"
      tagNameMap[TAG_PREFERRED_RATE] = "Preferred Rate"
      tagNameMap[TAG_PREFERRED_VOLUME] = "Preferred Volume"
      tagNameMap[TAG_PREVIEW_TIME] = "Preview Time"
      tagNameMap[TAG_PREVIEW_DURATION] = "Preview Duration"
      tagNameMap[TAG_POSTER_TIME] = "Poster Time"
      tagNameMap[TAG_SELECTION_TIME] = "Selection Time"
      tagNameMap[TAG_SELECTION_DURATION] = "Selection Duration"
      tagNameMap[TAG_CURRENT_TIME] = "Current Time"
      tagNameMap[TAG_NEXT_TRACK_ID] = "Next Track ID"
      tagNameMap[TAG_TRANSFORMATION_MATRIX] = "Transformation Matrix"
      tagNameMap[TAG_ROTATION] = "Rotation"
      tagNameMap[TAG_MEDIA_TIME_SCALE] = "Media Time Scale"
    }
  }

  override val name: String
    get() = "MP4"

  init {
    setDescriptor(Mp4Descriptor<Mp4Directory>(this))
  }
}
