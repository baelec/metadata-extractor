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
 * Provides human-readable string representations of tag values stored in a [FujifilmMakernoteDirectory].
 *
 *
 * Fujifilm added their Makernote tag from the Year 2000's models (e.g.Finepix1400,
 * Finepix4700). It uses IFD format and start from ASCII character 'FUJIFILM', and next 4
 * bytes (value 0x000c) points the offset to first IFD entry.
 * <pre>`
 * :0000: 46 55 4A 49 46 49 4C 4D-0C 00 00 00 0F 00 00 00 :0000: FUJIFILM........
 * :0010: 07 00 04 00 00 00 30 31-33 30 00 10 02 00 08 00 :0010: ......0130......
`</pre> *
 * There are two big differences to the other manufacturers.
 *
 *  * Fujifilm's Exif data uses Motorola align, but Makernote ignores it and uses Intel align.
 *  *
 * The other manufacturer's Makernote counts the "offset to data" from the first byte of TIFF header
 * (same as the other IFD), but Fujifilm counts it from the first byte of Makernote itself.
 *
 *
 *
 * @author Drew Noakes https://drewnoakes.com
 */
class FujifilmMakernoteDescriptor(directory: FujifilmMakernoteDirectory) : TagDescriptor<FujifilmMakernoteDirectory>(directory) {
  override fun getDescription(tagType: Int): String? {
    return when (tagType) {
      FujifilmMakernoteDirectory.TAG_MAKERNOTE_VERSION -> makernoteVersionDescription
      FujifilmMakernoteDirectory.TAG_SHARPNESS -> sharpnessDescription
      FujifilmMakernoteDirectory.TAG_WHITE_BALANCE -> whiteBalanceDescription
      FujifilmMakernoteDirectory.TAG_COLOR_SATURATION -> colorSaturationDescription
      FujifilmMakernoteDirectory.TAG_TONE -> toneDescription
      FujifilmMakernoteDirectory.TAG_CONTRAST -> contrastDescription
      FujifilmMakernoteDirectory.TAG_NOISE_REDUCTION -> noiseReductionDescription
      FujifilmMakernoteDirectory.TAG_HIGH_ISO_NOISE_REDUCTION -> highIsoNoiseReductionDescription
      FujifilmMakernoteDirectory.TAG_FLASH_MODE -> flashModeDescription
      FujifilmMakernoteDirectory.TAG_FLASH_EV -> flashExposureValueDescription
      FujifilmMakernoteDirectory.TAG_MACRO -> macroDescription
      FujifilmMakernoteDirectory.TAG_FOCUS_MODE -> focusModeDescription
      FujifilmMakernoteDirectory.TAG_SLOW_SYNC -> slowSyncDescription
      FujifilmMakernoteDirectory.TAG_PICTURE_MODE -> pictureModeDescription
      FujifilmMakernoteDirectory.TAG_EXR_AUTO -> exrAutoDescription
      FujifilmMakernoteDirectory.TAG_EXR_MODE -> exrModeDescription
      FujifilmMakernoteDirectory.TAG_AUTO_BRACKETING -> autoBracketingDescription
      FujifilmMakernoteDirectory.TAG_FINE_PIX_COLOR -> finePixColorDescription
      FujifilmMakernoteDirectory.TAG_BLUR_WARNING -> blurWarningDescription
      FujifilmMakernoteDirectory.TAG_FOCUS_WARNING -> focusWarningDescription
      FujifilmMakernoteDirectory.TAG_AUTO_EXPOSURE_WARNING -> autoExposureWarningDescription
      FujifilmMakernoteDirectory.TAG_DYNAMIC_RANGE -> dynamicRangeDescription
      FujifilmMakernoteDirectory.TAG_FILM_MODE -> filmModeDescription
      FujifilmMakernoteDirectory.TAG_DYNAMIC_RANGE_SETTING -> dynamicRangeSettingDescription
      else -> super.getDescription(tagType)
    }
  }

  private val makernoteVersionDescription: String?
    private get() = getVersionBytesDescription(FujifilmMakernoteDirectory.TAG_MAKERNOTE_VERSION, 2)

  val sharpnessDescription: String?
    get() {
      val value = _directory.getInteger(FujifilmMakernoteDirectory.TAG_SHARPNESS) ?: return null
      return when (value) {
        1 -> "Softest"
        2 -> "Soft"
        3 -> "Normal"
        4 -> "Hard"
        5 -> "Hardest"
        0x82 -> "Medium Soft"
        0x84 -> "Medium Hard"
        0x8000 -> "Film Simulation"
        0xFFFF -> "N/A"
        else -> "Unknown ($value)"
      }
    }

  val whiteBalanceDescription: String?
    get() {
      val value = _directory.getInteger(FujifilmMakernoteDirectory.TAG_WHITE_BALANCE) ?: return null
      return when (value) {
        0x000 -> "Auto"
        0x100 -> "Daylight"
        0x200 -> "Cloudy"
        0x300 -> "Daylight Fluorescent"
        0x301 -> "Day White Fluorescent"
        0x302 -> "White Fluorescent"
        0x303 -> "Warm White Fluorescent"
        0x304 -> "Living Room Warm White Fluorescent"
        0x400 -> "Incandescence"
        0x500 -> "Flash"
        0xf00 -> "Custom White Balance"
        0xf01 -> "Custom White Balance 2"
        0xf02 -> "Custom White Balance 3"
        0xf03 -> "Custom White Balance 4"
        0xf04 -> "Custom White Balance 5"
        0xff0 -> "Kelvin"
        else -> "Unknown ($value)"
      }
    }

  val colorSaturationDescription: String?
    get() {
      val value = _directory.getInteger(FujifilmMakernoteDirectory.TAG_COLOR_SATURATION) ?: return null
      return when (value) {
        0x000 -> "Normal"
        0x080 -> "Medium High"
        0x100 -> "High"
        0x180 -> "Medium Low"
        0x200 -> "Low"
        0x300 -> "None (B&W)"
        0x301 -> "B&W Green Filter"
        0x302 -> "B&W Yellow Filter"
        0x303 -> "B&W Blue Filter"
        0x304 -> "B&W Sepia"
        0x8000 -> "Film Simulation"
        else -> "Unknown ($value)"
      }
    }

  val toneDescription: String?
    get() {
      val value = _directory.getInteger(FujifilmMakernoteDirectory.TAG_TONE) ?: return null
      return when (value) {
        0x000 -> "Normal"
        0x080 -> "Medium High"
        0x100 -> "High"
        0x180 -> "Medium Low"
        0x200 -> "Low"
        0x300 -> "None (B&W)"
        0x8000 -> "Film Simulation"
        else -> "Unknown ($value)"
      }
    }

  val contrastDescription: String?
    get() {
      val value = _directory.getInteger(FujifilmMakernoteDirectory.TAG_CONTRAST) ?: return null
      return when (value) {
        0x000 -> "Normal"
        0x100 -> "High"
        0x300 -> "Low"
        else -> "Unknown ($value)"
      }
    }

  val noiseReductionDescription: String?
    get() {
      val value = _directory.getInteger(FujifilmMakernoteDirectory.TAG_NOISE_REDUCTION) ?: return null
      return when (value) {
        0x040 -> "Low"
        0x080 -> "Normal"
        0x100 -> "N/A"
        else -> "Unknown ($value)"
      }
    }

  val highIsoNoiseReductionDescription: String?
    get() {
      val value = _directory.getInteger(FujifilmMakernoteDirectory.TAG_HIGH_ISO_NOISE_REDUCTION) ?: return null
      return when (value) {
        0x000 -> "Normal"
        0x100 -> "Strong"
        0x200 -> "Weak"
        else -> "Unknown ($value)"
      }
    }

  val flashModeDescription: String?
    get() = getIndexedDescription(
      FujifilmMakernoteDirectory.TAG_FLASH_MODE,
      "Auto",
      "On",
      "Off",
      "Red-eye Reduction",
      "External"
    )

  val flashExposureValueDescription: String?
    get() {
      val value = _directory.getRational(FujifilmMakernoteDirectory.TAG_FLASH_EV)
      return if (value == null) null else "${value.toSimpleString(false)} EV (Apex)"
    }

  val macroDescription: String?
    get() = getIndexedDescription(FujifilmMakernoteDirectory.TAG_MACRO, "Off", "On")

  val focusModeDescription: String?
    get() = getIndexedDescription(FujifilmMakernoteDirectory.TAG_FOCUS_MODE, "Auto Focus", "Manual Focus")

  val slowSyncDescription: String?
    get() = getIndexedDescription(FujifilmMakernoteDirectory.TAG_SLOW_SYNC, "Off", "On")

  val pictureModeDescription: String?
    get() {
      val value = _directory.getInteger(FujifilmMakernoteDirectory.TAG_PICTURE_MODE) ?: return null
      return when (value) {
        0x000 -> "Auto"
        0x001 -> "Portrait scene"
        0x002 -> "Landscape scene"
        0x003 -> "Macro"
        0x004 -> "Sports scene"
        0x005 -> "Night scene"
        0x006 -> "Program AE"
        0x007 -> "Natural Light"
        0x008 -> "Anti-blur"
        0x009 -> "Beach & Snow"
        0x00a -> "Sunset"
        0x00b -> "Museum"
        0x00c -> "Party"
        0x00d -> "Flower"
        0x00e -> "Text"
        0x00f -> "Natural Light & Flash"
        0x010 -> "Beach"
        0x011 -> "Snow"
        0x012 -> "Fireworks"
        0x013 -> "Underwater"
        0x014 -> "Portrait with Skin Correction"
        0x016 -> "Panorama"
        0x017 -> "Night (Tripod)"
        0x018 -> "Pro Low-light"
        0x019 -> "Pro Focus"
        0x01b -> "Dog Face Detection"
        0x01c -> "Cat Face Detection"
        0x100 -> "Aperture priority AE"
        0x200 -> "Shutter priority AE"
        0x300 -> "Manual exposure"
        else -> "Unknown ($value)"
      }
    }

  val exrAutoDescription: String?
    get() = getIndexedDescription(FujifilmMakernoteDirectory.TAG_EXR_AUTO, "Auto", "Manual")

  val exrModeDescription: String?
    get() {
      val value = _directory.getInteger(FujifilmMakernoteDirectory.TAG_EXR_MODE) ?: return null
      return when (value) {
        0x100 -> "HR (High Resolution)"
        0x200 -> "SN (Signal to Noise Priority)"
        0x300 -> "DR (Dynamic Range Priority)"
        else -> "Unknown ($value)"
      }
    }

  val autoBracketingDescription: String?
    get() = getIndexedDescription(
      FujifilmMakernoteDirectory.TAG_AUTO_BRACKETING,
      "Off",
      "On",
      "No Flash & Flash"
    )

  val finePixColorDescription: String?
    get() {
      val value = _directory.getInteger(FujifilmMakernoteDirectory.TAG_FINE_PIX_COLOR) ?: return null
      return when (value) {
        0x00 -> "Standard"
        0x10 -> "Chrome"
        0x30 -> "B&W"
        else -> "Unknown ($value)"
      }
    }

  val blurWarningDescription: String?
    get() = getIndexedDescription(
      FujifilmMakernoteDirectory.TAG_BLUR_WARNING,
      "No Blur Warning",
      "Blur warning"
    )

  val focusWarningDescription: String?
    get() = getIndexedDescription(
      FujifilmMakernoteDirectory.TAG_FOCUS_WARNING,
      "Good Focus",
      "Out Of Focus"
    )

  val autoExposureWarningDescription: String?
    get() = getIndexedDescription(
      FujifilmMakernoteDirectory.TAG_AUTO_EXPOSURE_WARNING,
      "AE Good",
      "Over Exposed"
    )

  val dynamicRangeDescription: String?
    get() = getIndexedDescription(
      FujifilmMakernoteDirectory.TAG_DYNAMIC_RANGE,
      1,
      "Standard",
      null,
      "Wide"
    )

  val filmModeDescription: String?
    get() {
      val value = _directory.getInteger(FujifilmMakernoteDirectory.TAG_FILM_MODE) ?: return null
      return when (value) {
        0x000 -> "F0/Standard (Provia) "
        0x100 -> "F1/Studio Portrait"
        0x110 -> "F1a/Studio Portrait Enhanced Saturation"
        0x120 -> "F1b/Studio Portrait Smooth Skin Tone (Astia)"
        0x130 -> "F1c/Studio Portrait Increased Sharpness"
        0x200 -> "F2/Fujichrome (Velvia)"
        0x300 -> "F3/Studio Portrait Ex"
        0x400 -> "F4/Velvia"
        0x500 -> "Pro Neg. Std"
        0x501 -> "Pro Neg. Hi"
        else -> "Unknown ($value)"
      }
    }

  val dynamicRangeSettingDescription: String?
    get() {
      val value = _directory.getInteger(FujifilmMakernoteDirectory.TAG_DYNAMIC_RANGE_SETTING) ?: return null
      return when (value) {
        0x000 -> "Auto (100-400%)"
        0x001 -> "Manual"
        0x100 -> "Standard (100%)"
        0x200 -> "Wide 1 (230%)"
        0x201 -> "Wide 2 (400%)"
        0x8000 -> "Film Simulation"
        else -> "Unknown ($value)"
      }
    }
}
