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
package com.drew.metadata.exif.makernotes

import com.drew.metadata.TagDescriptor

/**
 * Provides human-readable string representations of tag values stored in a [KodakMakernoteDirectory].
 *
 * @author Drew Noakes https://drewnoakes.com
 */
class KodakMakernoteDescriptor(directory: KodakMakernoteDirectory) : TagDescriptor<KodakMakernoteDirectory>(directory) {
  override fun getDescription(tagType: Int): String? {
    return when (tagType) {
      KodakMakernoteDirectory.TAG_QUALITY -> qualityDescription
      KodakMakernoteDirectory.TAG_BURST_MODE -> burstModeDescription
      KodakMakernoteDirectory.TAG_SHUTTER_MODE -> shutterModeDescription
      KodakMakernoteDirectory.TAG_FOCUS_MODE -> focusModeDescription
      KodakMakernoteDirectory.TAG_WHITE_BALANCE -> whiteBalanceDescription
      KodakMakernoteDirectory.TAG_FLASH_MODE -> flashModeDescription
      KodakMakernoteDirectory.TAG_FLASH_FIRED -> flashFiredDescription
      KodakMakernoteDirectory.TAG_COLOR_MODE -> colorModeDescription
      KodakMakernoteDirectory.TAG_SHARPNESS -> sharpnessDescription
      else -> super.getDescription(tagType)
    }
  }

  val sharpnessDescription: String?
    get() = getIndexedDescription(KodakMakernoteDirectory.TAG_SHARPNESS, "Normal")

  val colorModeDescription: String?
    get() {
      val value = _directory.getInteger(KodakMakernoteDirectory.TAG_COLOR_MODE) ?: return null
      return when (value) {
        0x001, 0x2000 -> "B&W"
        0x002, 0x4000 -> "Sepia"
        0x003 -> "B&W Yellow Filter"
        0x004 -> "B&W Red Filter"
        0x020 -> "Saturated Color"
        0x040, 0x200 -> "Neutral Color"
        0x100 -> "Saturated Color"
        else -> "Unknown ($value)"
      }
    }

  val flashFiredDescription: String?
    get() = getIndexedDescription(KodakMakernoteDirectory.TAG_FLASH_FIRED, "No", "Yes")

  val flashModeDescription: String?
    get() {
      val value = _directory.getInteger(KodakMakernoteDirectory.TAG_FLASH_MODE) ?: return null
      return when (value) {
        0x00 -> "Auto"
        0x10, 0x01 -> "Fill Flash"
        0x20, 0x02 -> "Off"
        0x40, 0x03 -> "Red Eye"
        else -> "Unknown ($value)"
      }
    }

  val whiteBalanceDescription: String?
    get() = getIndexedDescription(KodakMakernoteDirectory.TAG_WHITE_BALANCE, "Auto", "Flash", "Tungsten", "Daylight")

  val focusModeDescription: String?
    get() = getIndexedDescription(KodakMakernoteDirectory.TAG_FOCUS_MODE, "Normal", null, "Macro")

  val shutterModeDescription: String?
    get() {
      val value = _directory.getInteger(KodakMakernoteDirectory.TAG_SHUTTER_MODE) ?: return null
      return when (value) {
        0 -> "Auto"
        8 -> "Aperture Priority"
        32 -> "Manual"
        else -> "Unknown ($value)"
      }
    }

  val burstModeDescription: String?
    get() = getIndexedDescription(KodakMakernoteDirectory.TAG_BURST_MODE, "Off", "On")

  val qualityDescription: String?
    get() = getIndexedDescription(KodakMakernoteDirectory.TAG_QUALITY, 1, "Fine", "Normal")
}
