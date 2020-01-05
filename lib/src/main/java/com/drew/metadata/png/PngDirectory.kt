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

import com.drew.imaging.png.PngChunkType
import com.drew.metadata.Directory
import java.util.*

/**
 * @author Drew Noakes https://drewnoakes.com
 */
class PngDirectory(val pngChunkType: PngChunkType) : Directory() {
  override val tagNameMap = Companion.tagNameMap
  companion object {
    const val TAG_IMAGE_WIDTH = 1
    const val TAG_IMAGE_HEIGHT = 2
    const val TAG_BITS_PER_SAMPLE = 3
    const val TAG_COLOR_TYPE = 4
    const val TAG_COMPRESSION_TYPE = 5
    const val TAG_FILTER_METHOD = 6
    const val TAG_INTERLACE_METHOD = 7
    const val TAG_PALETTE_SIZE = 8
    const val TAG_PALETTE_HAS_TRANSPARENCY = 9
    const val TAG_SRGB_RENDERING_INTENT = 10
    const val TAG_GAMMA = 11
    const val TAG_ICC_PROFILE_NAME = 12
    const val TAG_TEXTUAL_DATA = 13
    const val TAG_LAST_MODIFICATION_TIME = 14
    const val TAG_BACKGROUND_COLOR = 15
    const val TAG_PIXELS_PER_UNIT_X = 16
    const val TAG_PIXELS_PER_UNIT_Y = 17
    const val TAG_UNIT_SPECIFIER = 18
    const val TAG_SIGNIFICANT_BITS = 19
    protected val tagNameMap = HashMap<Int, String>()

    init {
      tagNameMap[TAG_IMAGE_HEIGHT] = "Image Height"
      tagNameMap[TAG_IMAGE_WIDTH] = "Image Width"
      tagNameMap[TAG_BITS_PER_SAMPLE] = "Bits Per Sample"
      tagNameMap[TAG_COLOR_TYPE] = "Color Type"
      tagNameMap[TAG_COMPRESSION_TYPE] = "Compression Type"
      tagNameMap[TAG_FILTER_METHOD] = "Filter Method"
      tagNameMap[TAG_INTERLACE_METHOD] = "Interlace Method"
      tagNameMap[TAG_PALETTE_SIZE] = "Palette Size"
      tagNameMap[TAG_PALETTE_HAS_TRANSPARENCY] = "Palette Has Transparency"
      tagNameMap[TAG_SRGB_RENDERING_INTENT] = "sRGB Rendering Intent"
      tagNameMap[TAG_GAMMA] = "Image Gamma"
      tagNameMap[TAG_ICC_PROFILE_NAME] = "ICC Profile Name"
      tagNameMap[TAG_TEXTUAL_DATA] = "Textual Data"
      tagNameMap[TAG_LAST_MODIFICATION_TIME] = "Last Modification Time"
      tagNameMap[TAG_BACKGROUND_COLOR] = "Background Color"
      tagNameMap[TAG_PIXELS_PER_UNIT_X] = "Pixels Per Unit X"
      tagNameMap[TAG_PIXELS_PER_UNIT_Y] = "Pixels Per Unit Y"
      tagNameMap[TAG_UNIT_SPECIFIER] = "Unit Specifier"
      tagNameMap[TAG_SIGNIFICANT_BITS] = "Significant Bits"
    }
  }

  override val name: String
    get() = "PNG-" + pngChunkType.identifier

  init {
    setDescriptor(PngDescriptor(this))
  }
}
