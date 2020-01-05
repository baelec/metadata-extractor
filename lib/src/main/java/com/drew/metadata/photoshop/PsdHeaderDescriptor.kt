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
package com.drew.metadata.photoshop

import com.drew.metadata.TagDescriptor

/**
 * @author Drew Noakes https://drewnoakes.com
 */
class PsdHeaderDescriptor(directory: PsdHeaderDirectory) : TagDescriptor<PsdHeaderDirectory>(directory) {
  override fun getDescription(tagType: Int): String? {
    return when (tagType) {
      PsdHeaderDirectory.TAG_CHANNEL_COUNT -> channelCountDescription
      PsdHeaderDirectory.TAG_BITS_PER_CHANNEL -> bitsPerChannelDescription
      PsdHeaderDirectory.TAG_COLOR_MODE -> colorModeDescription
      PsdHeaderDirectory.TAG_IMAGE_HEIGHT -> imageHeightDescription
      PsdHeaderDirectory.TAG_IMAGE_WIDTH -> imageWidthDescription
      else -> super.getDescription(tagType)
    }
  }

  // Supported range is 1 to 56.
  val channelCountDescription: String?
    get() {
      // Supported range is 1 to 56.
      val value = _directory.getInteger(PsdHeaderDirectory.TAG_CHANNEL_COUNT) ?: return null
      return "$value channel${if (value == 1) "" else "s"}"
    }

  // Supported values are 1, 8, 16 and 32.
  val bitsPerChannelDescription: String?
    get() { // Supported values are 1, 8, 16 and 32.
      val value = _directory.getInteger(PsdHeaderDirectory.TAG_BITS_PER_CHANNEL) ?: return null
      return "$value bit${if (value == 1) "" else "s"} per channel"
    }

  val colorModeDescription: String?
    get() = getIndexedDescription(PsdHeaderDirectory.TAG_COLOR_MODE,
      "Bitmap",
      "Grayscale",
      "Indexed",
      "RGB",
      "CMYK",
      null,
      null,
      "Multichannel",
      "Duotone",
      "Lab")

  val imageHeightDescription: String?
    get() {
      val value = _directory.getInteger(PsdHeaderDirectory.TAG_IMAGE_HEIGHT) ?: return null
      return "$value pixel${if (value == 1) "" else "s"}"
    }

  val imageWidthDescription: String?
    get() {
      try {
        val value = _directory.getInteger(PsdHeaderDirectory.TAG_IMAGE_WIDTH) ?: return null
        return "$value pixel${if (value == 1) "" else "s"}"
      } catch (e: Exception) {
        return null
      }
    }
}
