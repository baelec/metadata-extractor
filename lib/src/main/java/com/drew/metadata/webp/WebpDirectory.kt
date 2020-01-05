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
package com.drew.metadata.webp

import com.drew.metadata.Directory
import java.util.*

/**
 * @author Drew Noakes https://drewnoakes.com
 */
class WebpDirectory : Directory() {
  override val tagNameMap = Companion.tagNameMap
  companion object {
    const val TAG_IMAGE_HEIGHT = 1
    const val TAG_IMAGE_WIDTH = 2
    const val TAG_HAS_ALPHA = 3
    const val TAG_IS_ANIMATION = 4
    const val CHUNK_VP8X = "VP8X"
    const val CHUNK_VP8L = "VP8L"
    const val CHUNK_VP8 = "VP8 "
    const val CHUNK_EXIF = "EXIF"
    const val CHUNK_ICCP = "ICCP"
    const val CHUNK_XMP = "XMP "
    const val FORMAT = "WEBP"
    protected val tagNameMap = HashMap<Int, String>()

    init {
      tagNameMap[TAG_IMAGE_HEIGHT] = "Image Height"
      tagNameMap[TAG_IMAGE_WIDTH] = "Image Width"
      tagNameMap[TAG_HAS_ALPHA] = "Has Alpha"
      tagNameMap[TAG_IS_ANIMATION] = "Is Animation"
    }
  }

  override val name: String
    get() = "WebP"

  init {
    setDescriptor(WebpDescriptor(this))
  }
}
