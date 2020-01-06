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
 * Provides human-readable string representations of tag values stored in a [CasioType1MakernoteDirectory].
 *
 * @author Drew Noakes https://drewnoakes.com
 */
class CasioType1MakernoteDescriptor(directory: CasioType1MakernoteDirectory) : TagDescriptor<CasioType1MakernoteDirectory>(directory) {
  override fun getDescription(tagType: Int): String? {
    return when (tagType) {
      CasioType1MakernoteDirectory.TAG_RECORDING_MODE -> recordingModeDescription
      CasioType1MakernoteDirectory.TAG_QUALITY -> qualityDescription
      CasioType1MakernoteDirectory.TAG_FOCUSING_MODE -> focusingModeDescription
      CasioType1MakernoteDirectory.TAG_FLASH_MODE -> flashModeDescription
      CasioType1MakernoteDirectory.TAG_FLASH_INTENSITY -> flashIntensityDescription
      CasioType1MakernoteDirectory.TAG_OBJECT_DISTANCE -> objectDistanceDescription
      CasioType1MakernoteDirectory.TAG_WHITE_BALANCE -> whiteBalanceDescription
      CasioType1MakernoteDirectory.TAG_DIGITAL_ZOOM -> digitalZoomDescription
      CasioType1MakernoteDirectory.TAG_SHARPNESS -> sharpnessDescription
      CasioType1MakernoteDirectory.TAG_CONTRAST -> contrastDescription
      CasioType1MakernoteDirectory.TAG_SATURATION -> saturationDescription
      CasioType1MakernoteDirectory.TAG_CCD_SENSITIVITY -> ccdSensitivityDescription
      else -> super.getDescription(tagType)
    }
  }

  val ccdSensitivityDescription: String?
    get() {
      val value = _directory.getInteger(CasioType1MakernoteDirectory.TAG_CCD_SENSITIVITY) ?: return null
      return when (value) {
        64 -> "Normal"
        125 -> "+1.0"
        250 -> "+2.0"
        244 -> "+3.0"
        80 -> "Normal (ISO 80 equivalent)"
        100 -> "High"
        else -> "Unknown ($value)"
      }
    }

  val saturationDescription: String?
    get() = getIndexedDescription(CasioType1MakernoteDirectory.TAG_SATURATION, "Normal", "Low", "High")

  val contrastDescription: String?
    get() = getIndexedDescription(CasioType1MakernoteDirectory.TAG_CONTRAST, "Normal", "Low", "High")

  val sharpnessDescription: String?
    get() = getIndexedDescription(CasioType1MakernoteDirectory.TAG_SHARPNESS, "Normal", "Soft", "Hard")

  val digitalZoomDescription: String?
    get() {
      val value = _directory.getInteger(CasioType1MakernoteDirectory.TAG_DIGITAL_ZOOM) ?: return null
      return when (value) {
        0x10000 -> "No digital zoom"
        0x10001 -> "2x digital zoom"
        0x20000 -> "2x digital zoom"
        0x40000 -> "4x digital zoom"
        else -> "Unknown ($value)"
      }
    }

  val whiteBalanceDescription: String?
    get() {
      val value = _directory.getInteger(CasioType1MakernoteDirectory.TAG_WHITE_BALANCE) ?: return null
      return when (value) {
        1 -> "Auto"
        2 -> "Tungsten"
        3 -> "Daylight"
        4 -> "Florescent"
        5 -> "Shade"
        129 -> "Manual"
        else -> "Unknown ($value)"
      }
    }

  val objectDistanceDescription: String?
    get() {
      val value = _directory.getInteger(CasioType1MakernoteDirectory.TAG_OBJECT_DISTANCE)
      return if (value == null) null else getFocalLengthDescription(value.toDouble())
    }

  val flashIntensityDescription: String?
    get() {
      val value = _directory.getInteger(CasioType1MakernoteDirectory.TAG_FLASH_INTENSITY) ?: return null
      return when (value) {
        11 -> "Weak"
        13 -> "Normal"
        15 -> "Strong"
        else -> "Unknown ($value)"
      }
    }

  val flashModeDescription: String?
    get() = getIndexedDescription(CasioType1MakernoteDirectory.TAG_FLASH_MODE, 1, "Auto", "On", "Off", "Red eye reduction")

  val focusingModeDescription: String?
    get() = getIndexedDescription(CasioType1MakernoteDirectory.TAG_FOCUSING_MODE, 2, "Macro", "Auto focus", "Manual focus", "Infinity")

  val qualityDescription: String?
    get() = getIndexedDescription(CasioType1MakernoteDirectory.TAG_QUALITY, 1, "Economy", "Normal", "Fine")

  val recordingModeDescription: String?
    get() = getIndexedDescription(CasioType1MakernoteDirectory.TAG_RECORDING_MODE, 1, "Single shutter", "Panorama", "Night scene", "Portrait", "Landscape")
}
