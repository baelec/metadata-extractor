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
package com.drew.metadata.pcx

import com.drew.metadata.Directory
import java.util.*

/**
 * @author Drew Noakes https://drewnoakes.com
 */
class PcxDirectory : Directory() {
  override val tagNameMap = Companion.tagNameMap
  companion object {
    const val TAG_VERSION = 1
    const val TAG_BITS_PER_PIXEL = 2
    const val TAG_XMIN = 3
    const val TAG_YMIN = 4
    const val TAG_XMAX = 5
    const val TAG_YMAX = 6
    const val TAG_HORIZONTAL_DPI = 7
    const val TAG_VERTICAL_DPI = 8
    const val TAG_PALETTE = 9
    const val TAG_COLOR_PLANES = 10
    const val TAG_BYTES_PER_LINE = 11
    const val TAG_PALETTE_TYPE = 12
    const val TAG_HSCR_SIZE = 13
    const val TAG_VSCR_SIZE = 14
    protected val tagNameMap = HashMap<Int, String>()

    init {
      tagNameMap[TAG_VERSION] = "Version"
      tagNameMap[TAG_BITS_PER_PIXEL] = "Bits Per Pixel"
      tagNameMap[TAG_XMIN] = "X Min"
      tagNameMap[TAG_YMIN] = "Y Min"
      tagNameMap[TAG_XMAX] = "X Max"
      tagNameMap[TAG_YMAX] = "Y Max"
      tagNameMap[TAG_HORIZONTAL_DPI] = "Horizontal DPI"
      tagNameMap[TAG_VERTICAL_DPI] = "Vertical DPI"
      tagNameMap[TAG_PALETTE] = "Palette"
      tagNameMap[TAG_COLOR_PLANES] = "Color Planes"
      tagNameMap[TAG_BYTES_PER_LINE] = "Bytes Per Line"
      tagNameMap[TAG_PALETTE_TYPE] = "Palette Type"
      tagNameMap[TAG_HSCR_SIZE] = "H Scr Size"
      tagNameMap[TAG_VSCR_SIZE] = "V Scr Size"
    }
  }

  override val name: String
    get() = "PCX"

  init {
    setDescriptor(PcxDescriptor(this))
  }
}
