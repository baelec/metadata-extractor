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
 * Provides human-readable string representations of tag values stored in a [NikonType1MakernoteDirectory].
 *
 *
 * Type-1 is for E-Series cameras prior to (not including) E990.  For example: E700, E800, E900,
 * E900S, E910, E950.
 *
 *
 * Makernote starts from ASCII string "Nikon". Data format is the same as IFD, but it starts from
 * offset 0x08. This is the same as Olympus except start string. Example of actual data
 * structure is shown below.
 * <pre>`
 * :0000: 4E 69 6B 6F 6E 00 01 00-05 00 02 00 02 00 06 00 Nikon...........
 * :0010: 00 00 EC 02 00 00 03 00-03 00 01 00 00 00 06 00 ................
`</pre> *
 *
 * @author Drew Noakes https://drewnoakes.com
 */
class NikonType1MakernoteDescriptor(directory: NikonType1MakernoteDirectory) : TagDescriptor<NikonType1MakernoteDirectory>(directory) {
  override fun getDescription(tagType: Int): String? {
    return when (tagType) {
      NikonType1MakernoteDirectory.TAG_QUALITY -> qualityDescription
      NikonType1MakernoteDirectory.TAG_COLOR_MODE -> colorModeDescription
      NikonType1MakernoteDirectory.TAG_IMAGE_ADJUSTMENT -> imageAdjustmentDescription
      NikonType1MakernoteDirectory.TAG_CCD_SENSITIVITY -> ccdSensitivityDescription
      NikonType1MakernoteDirectory.TAG_WHITE_BALANCE -> whiteBalanceDescription
      NikonType1MakernoteDirectory.TAG_FOCUS -> focusDescription
      NikonType1MakernoteDirectory.TAG_DIGITAL_ZOOM -> digitalZoomDescription
      NikonType1MakernoteDirectory.TAG_CONVERTER -> converterDescription
      else -> super.getDescription(tagType)
    }
  }

  val converterDescription: String?
    get() = getIndexedDescription(NikonType1MakernoteDirectory.TAG_CONVERTER, "None", "Fisheye converter")

  val digitalZoomDescription: String?
    get() {
      val value = _directory.getRational(NikonType1MakernoteDirectory.TAG_DIGITAL_ZOOM)
      return if (value == null) null else if (value.numerator == 0L) "No digital zoom" else "${value.toSimpleString(true)}x digital zoom"
    }

  val focusDescription: String?
    get() {
      val value = _directory.getRational(NikonType1MakernoteDirectory.TAG_FOCUS)
      return if (value == null) null else if (value.numerator == 1L && value.denominator == 0L) "Infinite" else value.toSimpleString(true)
    }

  val whiteBalanceDescription: String?
    get() = getIndexedDescription(NikonType1MakernoteDirectory.TAG_WHITE_BALANCE,
      "Auto",
      "Preset",
      "Daylight",
      "Incandescence",
      "Florescence",
      "Cloudy",
      "SpeedLight"
    )

  val ccdSensitivityDescription: String?
    get() = getIndexedDescription(NikonType1MakernoteDirectory.TAG_CCD_SENSITIVITY,
      "ISO80",
      null,
      "ISO160",
      null,
      "ISO320",
      "ISO100"
    )

  val imageAdjustmentDescription: String?
    get() = getIndexedDescription(NikonType1MakernoteDirectory.TAG_IMAGE_ADJUSTMENT,
      "Normal",
      "Bright +",
      "Bright -",
      "Contrast +",
      "Contrast -"
    )

  val colorModeDescription: String?
    get() = getIndexedDescription(NikonType1MakernoteDirectory.TAG_COLOR_MODE,
      1,
      "Color",
      "Monochrome"
    )

  val qualityDescription: String?
    get() = getIndexedDescription(NikonType1MakernoteDirectory.TAG_QUALITY,
      1,
      "VGA Basic",
      "VGA Normal",
      "VGA Fine",
      "SXGA Basic",
      "SXGA Normal",
      "SXGA Fine"
    )
}
