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
import java.util.*

/**
 * Provides human-readable String representations of tag values stored in a [OlympusRawDevelopment2MakernoteDirectory].
 *
 *
 * Some Description functions converted from Exiftool version 10.10 created by Phil Harvey
 * http://www.sno.phy.queensu.ca/~phil/exiftool/
 * lib\Image\ExifTool\Olympus.pm
 *
 * @author Kevin Mott https://github.com/kwhopper
 * @author Drew Noakes https://drewnoakes.com
 */
class OlympusRawDevelopment2MakernoteDescriptor(directory: OlympusRawDevelopment2MakernoteDirectory) : TagDescriptor<OlympusRawDevelopment2MakernoteDirectory>(directory) {
  override fun getDescription(tagType: Int): String? {
    return when (tagType) {
      OlympusRawDevelopment2MakernoteDirectory.TagRawDevVersion -> rawDevVersionDescription
      OlympusRawDevelopment2MakernoteDirectory.TagRawDevExposureBiasValue -> rawDevExposureBiasValueDescription
      OlympusRawDevelopment2MakernoteDirectory.TagRawDevColorSpace -> rawDevColorSpaceDescription
      OlympusRawDevelopment2MakernoteDirectory.TagRawDevNoiseReduction -> rawDevNoiseReductionDescription
      OlympusRawDevelopment2MakernoteDirectory.TagRawDevEngine -> rawDevEngineDescription
      OlympusRawDevelopment2MakernoteDirectory.TagRawDevPictureMode -> rawDevPictureModeDescription
      OlympusRawDevelopment2MakernoteDirectory.TagRawDevPmBwFilter -> rawDevPmBwFilterDescription
      OlympusRawDevelopment2MakernoteDirectory.TagRawDevPmPictureTone -> rawDevPmPictureToneDescription
      OlympusRawDevelopment2MakernoteDirectory.TagRawDevArtFilter -> rawDevArtFilterDescription
      else -> super.getDescription(tagType)
    }
  }

  val rawDevVersionDescription: String?
    get() = getVersionBytesDescription(OlympusRawDevelopment2MakernoteDirectory.TagRawDevVersion, 4)

  val rawDevExposureBiasValueDescription: String?
    get() = getIndexedDescription(OlympusRawDevelopment2MakernoteDirectory.TagRawDevExposureBiasValue,
      1, "Color Temperature", "Gray Point")

  val rawDevColorSpaceDescription: String?
    get() = getIndexedDescription(OlympusRawDevelopment2MakernoteDirectory.TagRawDevColorSpace,
      "sRGB", "Adobe RGB", "Pro Photo RGB")

  val rawDevNoiseReductionDescription: String?
    get() {
      val value = _directory.getInteger(OlympusRawDevelopment2MakernoteDirectory.TagRawDevNoiseReduction)
        ?: return null
      if (value == 0) return "(none)"
      val sb = StringBuilder()
      if (value and 1 != 0) sb.append("Noise Reduction, ")
      if (value shr 1 and 1 != 0) sb.append("Noise Filter, ")
      if (value shr 2 and 1 != 0) sb.append("Noise Filter (ISO Boost), ")
      if (value shr 3 and 1 != 0) sb.append("Noise Filter (Auto), ")
      if (sb.length > 2) {
        sb.delete(sb.length - 2, sb.length)
      }
      return sb.toString()
    }

  val rawDevEngineDescription: String?
    get() = getIndexedDescription(OlympusRawDevelopment2MakernoteDirectory.TagRawDevEngine,
      "High Speed", "High Function", "Advanced High Speed", "Advanced High Function")

  val rawDevPictureModeDescription: String?
    get() {
      val value = _directory.getInteger(OlympusRawDevelopment2MakernoteDirectory.TagRawDevPictureMode) ?: return null
      return when (value) {
        1 -> "Vivid"
        2 -> "Natural"
        3 -> "Muted"
        256 -> "Monotone"
        512 -> "Sepia"
        else -> "Unknown ($value)"
      }
    }

  val rawDevPmBwFilterDescription: String?
    get() = getIndexedDescription(OlympusRawDevelopment2MakernoteDirectory.TagRawDevPmBwFilter,
      "Neutral", "Yellow", "Orange", "Red", "Green")

  val rawDevPmPictureToneDescription: String?
    get() = getIndexedDescription(OlympusRawDevelopment2MakernoteDirectory.TagRawDevPmPictureTone,
      "Neutral", "Sepia", "Blue", "Purple", "Green")

  val rawDevArtFilterDescription: String?
    get() = getFilterDescription(OlympusRawDevelopment2MakernoteDirectory.TagRawDevArtFilter)

  fun getFilterDescription(tag: Int): String? {
    val values = _directory.getIntArray(tag)
    if (values == null || values.isEmpty()) return null
    val sb = StringBuilder()
    for (i in values.indices) {
      if (i == 0) sb.append(if (_filters.containsKey(values[i])) _filters[values[i]] else "[unknown]") else sb.append(values[i]).append("; ")
      sb.append("; ")
    }
    return sb.substring(0, sb.length - 2)
  }

  companion object {
    // RawDevArtFilter values
    private val _filters = HashMap<Int, String>()

    init {
      _filters[0] = "Off"
      _filters[1] = "Soft Focus"
      _filters[2] = "Pop Art"
      _filters[3] = "Pale & Light Color"
      _filters[4] = "Light Tone"
      _filters[5] = "Pin Hole"
      _filters[6] = "Grainy Film"
      _filters[9] = "Diorama"
      _filters[10] = "Cross Process"
      _filters[12] = "Fish Eye"
      _filters[13] = "Drawing"
      _filters[14] = "Gentle Sepia"
      _filters[15] = "Pale & Light Color II"
      _filters[16] = "Pop Art II"
      _filters[17] = "Pin Hole II"
      _filters[18] = "Pin Hole III"
      _filters[19] = "Grainy Film II"
      _filters[20] = "Dramatic Tone"
      _filters[21] = "Punk"
      _filters[22] = "Soft Focus 2"
      _filters[23] = "Sparkle"
      _filters[24] = "Watercolor"
      _filters[25] = "Key Line"
      _filters[26] = "Key Line II"
      _filters[27] = "Miniature"
      _filters[28] = "Reflection"
      _filters[29] = "Fragmented"
      _filters[31] = "Cross Process II"
      _filters[32] = "Dramatic Tone II"
      _filters[33] = "Watercolor I"
      _filters[34] = "Watercolor II"
      _filters[35] = "Diorama II"
      _filters[36] = "Vintage"
      _filters[37] = "Vintage II"
      _filters[38] = "Vintage III"
      _filters[39] = "Partial Color"
      _filters[40] = "Partial Color II"
      _filters[41] = "Partial Color III"
    }
  }
}
