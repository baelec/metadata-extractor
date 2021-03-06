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

import com.drew.metadata.TagDescriptor

/**
 * @author Drew Noakes https://drewnoakes.com
 */
class IcoDescriptor(directory: IcoDirectory) : TagDescriptor<IcoDirectory>(directory) {
  override fun getDescription(tagType: Int): String? {
    return when (tagType) {
      IcoDirectory.TAG_IMAGE_TYPE -> imageTypeDescription
      IcoDirectory.TAG_IMAGE_WIDTH -> imageWidthDescription
      IcoDirectory.TAG_IMAGE_HEIGHT -> imageHeightDescription
      IcoDirectory.TAG_COLOUR_PALETTE_SIZE -> colourPaletteSizeDescription
      else -> super.getDescription(tagType)
    }
  }

  val imageTypeDescription: String?
    get() = getIndexedDescription(IcoDirectory.TAG_IMAGE_TYPE, 1, "Icon", "Cursor")

  val imageWidthDescription: String?
    get() {
      val width = _directory.getInteger(IcoDirectory.TAG_IMAGE_WIDTH) ?: return null
      return "${(if (width == 0) 256 else width)} pixels"
    }

  val imageHeightDescription: String?
    get() {
      val width = _directory.getInteger(IcoDirectory.TAG_IMAGE_HEIGHT) ?: return null
      return "${(if (width == 0) 256 else width)} pixels"
    }

  val colourPaletteSizeDescription: String?
    get() {
      val size = _directory.getInteger(IcoDirectory.TAG_COLOUR_PALETTE_SIZE) ?: return null
      return if (size == 0) "No palette" else "$size colour${if (size == 1) "" else "s"}"
    }
}
