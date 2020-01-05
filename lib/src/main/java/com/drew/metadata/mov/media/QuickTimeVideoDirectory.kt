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
package com.drew.metadata.mov.media

import java.util.*

/**
 * @author Payton Garland
 */
class QuickTimeVideoDirectory : QuickTimeMediaDirectory() {
  companion object {
    // Video Sample Description Atom
    const val TAG_VENDOR = 1
    const val TAG_TEMPORAL_QUALITY = 2
    const val TAG_SPATIAL_QUALITY = 3
    const val TAG_WIDTH = 4
    const val TAG_HEIGHT = 5
    const val TAG_HORIZONTAL_RESOLUTION = 6
    const val TAG_VERTICAL_RESOLUTION = 7
    const val TAG_COMPRESSOR_NAME = 8
    const val TAG_DEPTH = 9
    const val TAG_COMPRESSION_TYPE = 10
    // Video Media Information Header Atom
    const val TAG_GRAPHICS_MODE = 11
    const val TAG_OPCOLOR = 12
    const val TAG_COLOR_TABLE = 13
    const val TAG_FRAME_RATE = 14
    private val tagNameMap = HashMap<Int, String>()

    init {
      addQuickTimeMediaTags(this.tagNameMap)
      tagNameMap[TAG_VENDOR] = "Vendor"
      tagNameMap[TAG_TEMPORAL_QUALITY] = "Temporal Quality"
      tagNameMap[TAG_SPATIAL_QUALITY] = "Spatial Quality"
      tagNameMap[TAG_WIDTH] = "Width"
      tagNameMap[TAG_HEIGHT] = "Height"
      tagNameMap[TAG_HORIZONTAL_RESOLUTION] = "Horizontal Resolution"
      tagNameMap[TAG_VERTICAL_RESOLUTION] = "Vertical Resolution"
      tagNameMap[TAG_COMPRESSOR_NAME] = "Compressor Name"
      tagNameMap[TAG_DEPTH] = "Depth"
      tagNameMap[TAG_COMPRESSION_TYPE] = "Compression Type"
      tagNameMap[TAG_GRAPHICS_MODE] = "Graphics Mode"
      tagNameMap[TAG_OPCOLOR] = "Opcolor"
      tagNameMap[TAG_COLOR_TABLE] = "Color Table"
      tagNameMap[TAG_FRAME_RATE] = "Frame Rate"
    }
  }

  override val name: String
    get() = "QuickTime Video"

  init {
    setDescriptor(QuickTimeVideoDescriptor(this))
  }
}
