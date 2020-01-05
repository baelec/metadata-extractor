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
package com.drew.metadata.mp4.media

import com.drew.metadata.TagDescriptor

class Mp4VideoDescriptor(directory: Mp4VideoDirectory) : TagDescriptor<Mp4VideoDirectory>(directory) {
  override fun getDescription(tagType: Int): String? {
    return when (tagType) {
      Mp4VideoDirectory.TAG_HEIGHT, Mp4VideoDirectory.TAG_WIDTH -> getPixelDescription(tagType)
      Mp4VideoDirectory.TAG_DEPTH -> depthDescription
      Mp4VideoDirectory.TAG_COLOR_TABLE -> colorTableDescription
      Mp4VideoDirectory.TAG_GRAPHICS_MODE -> graphicsModeDescription
      else -> super.getDescription(tagType)
    }
  }

  private fun getPixelDescription(tagType: Int): String? {
    val value = _directory.getString(tagType)
    return if (value == null) null else "$value pixels"
  }

  private val depthDescription: String?
    get() {
      val value = _directory.getInteger(Mp4VideoDirectory.TAG_DEPTH) ?: return null
      return when (value) {
        1, 2, 4, 8, 16, 24, 32 -> "$value-bit color"
        40, 36, 34 -> (value - 32).toString() + "-bit grayscale"
        else -> "Unknown ($value)"
      }
    }

  private val colorTableDescription: String?
    get() {
      val value = _directory.getInteger(Mp4VideoDirectory.TAG_COLOR_TABLE) ?: return null
      return when (value) {
        -1 -> {
          val depth = _directory.getInteger(Mp4VideoDirectory.TAG_DEPTH) ?: return "None"
          if (depth < 16) {
            "Default"
          } else {
            "None"
          }
        }
        0 -> "Color table within file"
        else -> "Unknown ($value)"
      }
    }

  private val graphicsModeDescription: String?
    get() {
      val value = _directory.getInteger(Mp4VideoDirectory.TAG_GRAPHICS_MODE) ?: return null
      return when (value) {
        0x00 -> "Copy"
        0x40 -> "Dither copy"
        0x20 -> "Blend"
        0x24 -> "Transparent"
        0x100 -> "Straight alpha"
        0x101 -> "Premul white alpha"
        0x102 -> "Premul black alpha"
        0x104 -> "Straight alpha blend"
        0x103 -> "Composition (dither copy)"
        else -> "Unknown ($value)"
      }
    }
}
