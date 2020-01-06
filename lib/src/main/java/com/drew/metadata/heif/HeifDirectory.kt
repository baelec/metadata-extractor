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
package com.drew.metadata.heif

import com.drew.metadata.Directory
import java.util.*

class HeifDirectory : Directory() {
  override val tagNameMap = Companion.tagNameMap

  companion object {
    const val TAG_MAJOR_BRAND = 1
    const val TAG_MINOR_VERSION = 2
    const val TAG_COMPATIBLE_BRANDS = 3
    const val TAG_IMAGE_WIDTH = 4
    const val TAG_IMAGE_HEIGHT = 5
    const val TAG_IMAGE_ROTATION = 6
    const val TAG_BITS_PER_CHANNEL = 7
    protected val tagNameMap = HashMap<Int, String>()

    init {
      tagNameMap[TAG_MAJOR_BRAND] = "Major Brand"
      tagNameMap[TAG_MINOR_VERSION] = "Minor Version"
      tagNameMap[TAG_COMPATIBLE_BRANDS] = "Compatible Brands"
      tagNameMap[TAG_IMAGE_WIDTH] = "Width"
      tagNameMap[TAG_IMAGE_HEIGHT] = "Height"
      tagNameMap[TAG_IMAGE_ROTATION] = "Rotation"
      tagNameMap[TAG_BITS_PER_CHANNEL] = "Bits Per Channel"
    }
  }

  override val name: String
    get() = "HEIF"

  init {
    setDescriptor(HeifDescriptor(this))
  }
}
