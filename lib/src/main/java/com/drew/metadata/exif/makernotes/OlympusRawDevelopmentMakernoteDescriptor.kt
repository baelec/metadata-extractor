/*
 * Copyright 2002-2015 Drew Noakes
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
 * Provides human-readable String representations of tag values stored in a [OlympusRawDevelopmentMakernoteDirectory].
 *
 *
 * Some Description functions converted from Exiftool version 10.10 created by Phil Harvey
 * http://www.sno.phy.queensu.ca/~phil/exiftool/
 * lib\Image\ExifTool\Olympus.pm
 *
 * @author Kevin Mott https://github.com/kwhopper
 * @author Drew Noakes https://drewnoakes.com
 */
class OlympusRawDevelopmentMakernoteDescriptor(directory: OlympusRawDevelopmentMakernoteDirectory) : TagDescriptor<OlympusRawDevelopmentMakernoteDirectory>(directory) {
  override fun getDescription(tagType: Int): String? {
    return when (tagType) {
      OlympusRawDevelopmentMakernoteDirectory.TagRawDevVersion -> rawDevVersionDescription
      OlympusRawDevelopmentMakernoteDirectory.TagRawDevColorSpace -> rawDevColorSpaceDescription
      OlympusRawDevelopmentMakernoteDirectory.TagRawDevEngine -> rawDevEngineDescription
      OlympusRawDevelopmentMakernoteDirectory.TagRawDevNoiseReduction -> rawDevNoiseReductionDescription
      OlympusRawDevelopmentMakernoteDirectory.TagRawDevEditStatus -> rawDevEditStatusDescription
      OlympusRawDevelopmentMakernoteDirectory.TagRawDevSettings -> rawDevSettingsDescription
      else -> super.getDescription(tagType)
    }
  }

  val rawDevVersionDescription: String?
    get() = getVersionBytesDescription(OlympusRawDevelopmentMakernoteDirectory.TagRawDevVersion, 4)

  val rawDevColorSpaceDescription: String?
    get() = getIndexedDescription(OlympusRawDevelopmentMakernoteDirectory.TagRawDevColorSpace,
      "sRGB", "Adobe RGB", "Pro Photo RGB")

  val rawDevEngineDescription: String?
    get() = getIndexedDescription(OlympusRawDevelopmentMakernoteDirectory.TagRawDevEngine,
      "High Speed", "High Function", "Advanced High Speed", "Advanced High Function")

  val rawDevNoiseReductionDescription: String?
    get() {
      val value = _directory.getInteger(OlympusRawDevelopmentMakernoteDirectory.TagRawDevNoiseReduction)
        ?: return null
      if (value == 0) return "(none)"
      val sb = StringBuilder()
      if (value and 1 != 0) sb.append("Noise Reduction, ")
      if (value shr 1 and 1 != 0) sb.append("Noise Filter, ")
      if (value shr 2 and 1 != 0) sb.append("Noise Filter (ISO Boost), ")
      return sb.substring(0, sb.length - 2)
    }

  val rawDevEditStatusDescription: String?
    get() {
      val value = _directory.getInteger(OlympusRawDevelopmentMakernoteDirectory.TagRawDevEditStatus) ?: return null
      return when (value) {
        0 -> "Original"
        1 -> "Edited (Landscape)"
        6, 8 -> "Edited (Portrait)"
        else -> "Unknown ($value)"
      }
    }

  val rawDevSettingsDescription: String?
    get() {
      val value = _directory.getInteger(OlympusRawDevelopmentMakernoteDirectory.TagRawDevSettings) ?: return null
      if (value == 0) return "(none)"
      val sb = StringBuilder()
      if (value and 1 != 0) sb.append("WB Color Temp, ")
      if (value shr 1 and 1 != 0) sb.append("WB Gray Point, ")
      if (value shr 2 and 1 != 0) sb.append("Saturation, ")
      if (value shr 3 and 1 != 0) sb.append("Contrast, ")
      if (value shr 4 and 1 != 0) sb.append("Sharpness, ")
      if (value shr 5 and 1 != 0) sb.append("Color Space, ")
      if (value shr 6 and 1 != 0) sb.append("High Function, ")
      if (value shr 7 and 1 != 0) sb.append("Noise Reduction, ")
      return sb.substring(0, sb.length - 2)
    }
}
