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
package com.drew.metadata.exif

import com.drew.lang.Rational
import com.drew.metadata.TagDescriptor

/**
 * Provides human-readable string representations of tag values stored in a [PanasonicRawDistortionDirectory].
 *
 * @author Kevin Mott https://github.com/kwhopper
 * @author Drew Noakes https://drewnoakes.com
 */
class PanasonicRawDistortionDescriptor(directory: PanasonicRawDistortionDirectory) : TagDescriptor<PanasonicRawDistortionDirectory>(directory) {
  override fun getDescription(tagType: Int): String? {
    return when (tagType) {
      PanasonicRawDistortionDirectory.TagDistortionParam02 -> distortionParam02Description
      PanasonicRawDistortionDirectory.TagDistortionParam04 -> distortionParam04Description
      PanasonicRawDistortionDirectory.TagDistortionScale -> distortionScaleDescription
      PanasonicRawDistortionDirectory.TagDistortionCorrection -> distortionCorrectionDescription
      PanasonicRawDistortionDirectory.TagDistortionParam08 -> distortionParam08Description
      PanasonicRawDistortionDirectory.TagDistortionParam09 -> distortionParam09Description
      PanasonicRawDistortionDirectory.TagDistortionParam11 -> distortionParam11Description
      else -> super.getDescription(tagType)
    }
  }

  val distortionParam02Description: String?
    get() {
      val value = _directory.getInteger(PanasonicRawDistortionDirectory.TagDistortionParam02) ?: return null
      return Rational(value.toLong(), 32678).toString()
    }

  val distortionParam04Description: String?
    get() {
      val value = _directory.getInteger(PanasonicRawDistortionDirectory.TagDistortionParam04) ?: return null
      return Rational(value.toLong(), 32678).toString()
    }

  //return (1 / (1 + value / 32768)).toString();
  val distortionScaleDescription: String?
    get() {
      val value = _directory.getInteger(PanasonicRawDistortionDirectory.TagDistortionScale) ?: return null
      //return (1 / (1 + value / 32768)).toString();
      return (1 / (1 + value / 32768)).toString()
    }

  // (have seen the upper 4 bits set for GF5 and GX1, giving a value of -4095 - PH)
  val distortionCorrectionDescription: String?
    get() {
      val value = _directory.getInteger(PanasonicRawDistortionDirectory.TagDistortionCorrection) ?: return null
      // (have seen the upper 4 bits set for GF5 and GX1, giving a value of -4095 - PH)
      val mask = 0x000f
      return when (value and mask) {
        0 -> "Off"
        1 -> "On"
        else -> "Unknown ($value)"
      }
    }

  val distortionParam08Description: String?
    get() {
      val value = _directory.getInteger(PanasonicRawDistortionDirectory.TagDistortionParam08) ?: return null
      return Rational(value.toLong(), 32678).toString()
    }

  val distortionParam09Description: String?
    get() {
      val value = _directory.getInteger(PanasonicRawDistortionDirectory.TagDistortionParam09) ?: return null
      return Rational(value.toLong(), 32678).toString()
    }

  val distortionParam11Description: String?
    get() {
      val value = _directory.getInteger(PanasonicRawDistortionDirectory.TagDistortionParam11) ?: return null
      return Rational(value.toLong(), 32678).toString()
    }
}
