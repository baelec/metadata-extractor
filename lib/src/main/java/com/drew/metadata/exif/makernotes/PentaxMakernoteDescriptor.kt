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
 * Provides human-readable string representations of tag values stored in a [PentaxMakernoteDirectory].
 *
 *
 * Some information about this makernote taken from here:
 * http://www.ozhiker.com/electronics/pjmt/jpeg_info/pentax_mn.html
 *
 * @author Drew Noakes https://drewnoakes.com
 */
class PentaxMakernoteDescriptor(directory: PentaxMakernoteDirectory) : TagDescriptor<PentaxMakernoteDirectory>(directory) {
  override fun getDescription(tagType: Int): String? {
    return when (tagType) {
      PentaxMakernoteDirectory.TAG_CAPTURE_MODE -> captureModeDescription
      PentaxMakernoteDirectory.TAG_QUALITY_LEVEL -> qualityLevelDescription
      PentaxMakernoteDirectory.TAG_FOCUS_MODE -> focusModeDescription
      PentaxMakernoteDirectory.TAG_FLASH_MODE -> flashModeDescription
      PentaxMakernoteDirectory.TAG_WHITE_BALANCE -> whiteBalanceDescription
      PentaxMakernoteDirectory.TAG_DIGITAL_ZOOM -> digitalZoomDescription
      PentaxMakernoteDirectory.TAG_SHARPNESS -> sharpnessDescription
      PentaxMakernoteDirectory.TAG_CONTRAST -> contrastDescription
      PentaxMakernoteDirectory.TAG_SATURATION -> saturationDescription
      PentaxMakernoteDirectory.TAG_ISO_SPEED -> isoSpeedDescription
      PentaxMakernoteDirectory.TAG_COLOUR -> colourDescription
      else -> super.getDescription(tagType)
    }
  }

  val colourDescription: String?
    get() = getIndexedDescription(PentaxMakernoteDirectory.TAG_COLOUR, 1, "Normal", "Black & White", "Sepia")

  val isoSpeedDescription: String?
    get() {
      val value = _directory.getInteger(PentaxMakernoteDirectory.TAG_ISO_SPEED) ?: return null
      return when (value) {
        10 -> "ISO 100"
        16 -> "ISO 200"
        100 -> "ISO 100"
        200 -> "ISO 200"
        else -> "Unknown ($value)"
      }
    }

  val saturationDescription: String?
    get() = getIndexedDescription(PentaxMakernoteDirectory.TAG_SATURATION, "Normal", "Low", "High")

  val contrastDescription: String?
    get() = getIndexedDescription(PentaxMakernoteDirectory.TAG_CONTRAST, "Normal", "Low", "High")

  val sharpnessDescription: String?
    get() = getIndexedDescription(PentaxMakernoteDirectory.TAG_SHARPNESS, "Normal", "Soft", "Hard")

  val digitalZoomDescription: String?
    get() {
      val value = _directory.getFloatObject(PentaxMakernoteDirectory.TAG_DIGITAL_ZOOM) ?: return null
      return if (value == 0f) "Off" else java.lang.Float.toString(value)
    }

  val whiteBalanceDescription: String?
    get() = getIndexedDescription(PentaxMakernoteDirectory.TAG_WHITE_BALANCE,
      "Auto", "Daylight", "Shade", "Tungsten", "Fluorescent", "Manual")

  val flashModeDescription: String?
    get() = getIndexedDescription(PentaxMakernoteDirectory.TAG_FLASH_MODE,
      1, "Auto", "Flash On", null, "Flash Off", null, "Red-eye Reduction")

  val focusModeDescription: String?
    get() = getIndexedDescription(PentaxMakernoteDirectory.TAG_FOCUS_MODE, 2, "Custom", "Auto")

  val qualityLevelDescription: String?
    get() = getIndexedDescription(PentaxMakernoteDirectory.TAG_QUALITY_LEVEL, "Good", "Better", "Best")

  val captureModeDescription: String?
    get() = getIndexedDescription(PentaxMakernoteDirectory.TAG_CAPTURE_MODE,
      "Auto", "Night-scene", "Manual", null, "Multiple")
}
