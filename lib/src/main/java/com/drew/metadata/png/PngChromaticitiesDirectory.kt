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
package com.drew.metadata.png

import com.drew.metadata.Directory
import com.drew.metadata.TagDescriptor
import java.util.*

/**
 * @author Drew Noakes https://drewnoakes.com
 */
class PngChromaticitiesDirectory : Directory() {
  override val tagNameMap = Companion.tagNameMap
  companion object {
    const val TAG_WHITE_POINT_X = 1
    const val TAG_WHITE_POINT_Y = 2
    const val TAG_RED_X = 3
    const val TAG_RED_Y = 4
    const val TAG_GREEN_X = 5
    const val TAG_GREEN_Y = 6
    const val TAG_BLUE_X = 7
    const val TAG_BLUE_Y = 8
    protected val tagNameMap = HashMap<Int, String>()

    init {
      tagNameMap[TAG_WHITE_POINT_X] = "White Point X"
      tagNameMap[TAG_WHITE_POINT_Y] = "White Point Y"
      tagNameMap[TAG_RED_X] = "Red X"
      tagNameMap[TAG_RED_Y] = "Red Y"
      tagNameMap[TAG_GREEN_X] = "Green X"
      tagNameMap[TAG_GREEN_Y] = "Green Y"
      tagNameMap[TAG_BLUE_X] = "Blue X"
      tagNameMap[TAG_BLUE_Y] = "Blue Y"
    }
  }

  override val name: String
    get() = "PNG Chromaticities"

  init {
    setDescriptor(TagDescriptor(this))
  }
}
