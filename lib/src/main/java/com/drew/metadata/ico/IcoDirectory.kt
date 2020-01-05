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
package com.drew.metadata.ico

import com.drew.metadata.Directory
import java.util.*

/**
 * @author Drew Noakes https://drewnoakes.com
 */
class IcoDirectory : Directory() {
  override val tagNameMap = Companion.tagNameMap
  companion object {
    const val TAG_IMAGE_TYPE = 1
    const val TAG_IMAGE_WIDTH = 2
    const val TAG_IMAGE_HEIGHT = 3
    const val TAG_COLOUR_PALETTE_SIZE = 4
    const val TAG_COLOUR_PLANES = 5
    const val TAG_CURSOR_HOTSPOT_X = 6
    const val TAG_BITS_PER_PIXEL = 7
    const val TAG_CURSOR_HOTSPOT_Y = 8
    const val TAG_IMAGE_SIZE_BYTES = 9
    const val TAG_IMAGE_OFFSET_BYTES = 10
    protected val tagNameMap = HashMap<Int, String>()

    init {
      tagNameMap[TAG_IMAGE_TYPE] = "Image Type"
      tagNameMap[TAG_IMAGE_WIDTH] = "Image Width"
      tagNameMap[TAG_IMAGE_HEIGHT] = "Image Height"
      tagNameMap[TAG_COLOUR_PALETTE_SIZE] = "Colour Palette Size"
      tagNameMap[TAG_COLOUR_PLANES] = "Colour Planes"
      tagNameMap[TAG_CURSOR_HOTSPOT_X] = "Hotspot X"
      tagNameMap[TAG_BITS_PER_PIXEL] = "Bits Per Pixel"
      tagNameMap[TAG_CURSOR_HOTSPOT_Y] = "Hotspot Y"
      tagNameMap[TAG_IMAGE_SIZE_BYTES] = "Image Size Bytes"
      tagNameMap[TAG_IMAGE_OFFSET_BYTES] = "Image Offset Bytes"
    }
  }

  override val name: String
    get() = "ICO"

  init {
    setDescriptor(IcoDescriptor(this))
  }
}
