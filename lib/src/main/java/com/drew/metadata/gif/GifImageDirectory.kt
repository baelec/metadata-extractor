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
package com.drew.metadata.gif

import com.drew.metadata.Directory
import java.util.*

/**
 * @author Drew Noakes https://drewnoakes.com
 * @author Kevin Mott https://github.com/kwhopper
 */
class GifImageDirectory : Directory() {
  override val tagNameMap = Companion.tagNameMap

  companion object {
    const val TAG_LEFT = 1
    const val TAG_TOP = 2
    const val TAG_WIDTH = 3
    const val TAG_HEIGHT = 4
    const val TAG_HAS_LOCAL_COLOUR_TABLE = 5
    const val TAG_IS_INTERLACED = 6
    const val TAG_IS_COLOR_TABLE_SORTED = 7
    const val TAG_LOCAL_COLOUR_TABLE_BITS_PER_PIXEL = 8
    private val tagNameMap = HashMap<Int, String>()

    init {
      tagNameMap[TAG_LEFT] = "Left"
      tagNameMap[TAG_TOP] = "Top"
      tagNameMap[TAG_WIDTH] = "Width"
      tagNameMap[TAG_HEIGHT] = "Height"
      tagNameMap[TAG_HAS_LOCAL_COLOUR_TABLE] = "Has Local Colour Table"
      tagNameMap[TAG_IS_INTERLACED] = "Is Interlaced"
      tagNameMap[TAG_IS_COLOR_TABLE_SORTED] = "Is Local Colour Table Sorted"
      tagNameMap[TAG_LOCAL_COLOUR_TABLE_BITS_PER_PIXEL] = "Local Colour Table Bits Per Pixel"
    }
  }

  override val name: String
    get() = "GIF Image"

  init {
    setDescriptor(GifImageDescriptor(this))
  }
}
