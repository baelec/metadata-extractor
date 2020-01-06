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
 */
class GifHeaderDirectory : Directory() {
  override val tagNameMap = Companion.tagNameMap

  companion object {
    const val TAG_GIF_FORMAT_VERSION = 1
    const val TAG_IMAGE_WIDTH = 2
    const val TAG_IMAGE_HEIGHT = 3
    const val TAG_COLOR_TABLE_SIZE = 4
    const val TAG_IS_COLOR_TABLE_SORTED = 5
    const val TAG_BITS_PER_PIXEL = 6
    const val TAG_HAS_GLOBAL_COLOR_TABLE = 7

    @Deprecated("use {@link #TAG_BACKGROUND_COLOR_INDEX} instead.")
    val TAG_TRANSPARENT_COLOR_INDEX = 8
    const val TAG_BACKGROUND_COLOR_INDEX = 8
    const val TAG_PIXEL_ASPECT_RATIO = 9
    protected val tagNameMap = HashMap<Int, String>()

    init {
      tagNameMap[TAG_GIF_FORMAT_VERSION] = "GIF Format Version"
      tagNameMap[TAG_IMAGE_HEIGHT] = "Image Height"
      tagNameMap[TAG_IMAGE_WIDTH] = "Image Width"
      tagNameMap[TAG_COLOR_TABLE_SIZE] = "Color Table Size"
      tagNameMap[TAG_IS_COLOR_TABLE_SORTED] = "Is Color Table Sorted"
      tagNameMap[TAG_BITS_PER_PIXEL] = "Bits per Pixel"
      tagNameMap[TAG_HAS_GLOBAL_COLOR_TABLE] = "Has Global Color Table"
      tagNameMap[TAG_BACKGROUND_COLOR_INDEX] = "Background Color Index"
      tagNameMap[TAG_PIXEL_ASPECT_RATIO] = "Pixel Aspect Ratio"
    }
  }

  override val name: String
    get() = "GIF Header"

  init {
    setDescriptor(GifHeaderDescriptor(this))
  }
}
