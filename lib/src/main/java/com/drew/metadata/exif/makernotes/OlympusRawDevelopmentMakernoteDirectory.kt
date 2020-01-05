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

import com.drew.metadata.Directory
import java.util.*

/**
 * The Olympus raw development makernote is used by many manufacturers (Epson, Konica, Minolta and Agfa...), and as such contains some tags
 * that appear specific to those manufacturers.
 *
 * @author Kevin Mott https://github.com/kwhopper
 * @author Drew Noakes https://drewnoakes.com
 */
class OlympusRawDevelopmentMakernoteDirectory : Directory() {
  override val tagNameMap = Companion.tagNameMap

  companion object {
    const val TagRawDevVersion = 0x0000
    const val TagRawDevExposureBiasValue = 0x0100
    const val TagRawDevWhiteBalanceValue = 0x0101
    const val TagRawDevWbFineAdjustment = 0x0102
    const val TagRawDevGrayPoint = 0x0103
    const val TagRawDevSaturationEmphasis = 0x0104
    const val TagRawDevMemoryColorEmphasis = 0x0105
    const val TagRawDevContrastValue = 0x0106
    const val TagRawDevSharpnessValue = 0x0107
    const val TagRawDevColorSpace = 0x0108
    const val TagRawDevEngine = 0x0109
    const val TagRawDevNoiseReduction = 0x010a
    const val TagRawDevEditStatus = 0x010b
    const val TagRawDevSettings = 0x010c
    protected val tagNameMap = HashMap<Int, String>()

    init {
      tagNameMap[TagRawDevVersion] = "Raw Dev Version"
      tagNameMap[TagRawDevExposureBiasValue] = "Raw Dev Exposure Bias Value"
      tagNameMap[TagRawDevWhiteBalanceValue] = "Raw Dev White Balance Value"
      tagNameMap[TagRawDevWbFineAdjustment] = "Raw Dev WB Fine Adjustment"
      tagNameMap[TagRawDevGrayPoint] = "Raw Dev Gray Point"
      tagNameMap[TagRawDevSaturationEmphasis] = "Raw Dev Saturation Emphasis"
      tagNameMap[TagRawDevMemoryColorEmphasis] = "Raw Dev Memory Color Emphasis"
      tagNameMap[TagRawDevContrastValue] = "Raw Dev Contrast Value"
      tagNameMap[TagRawDevSharpnessValue] = "Raw Dev Sharpness Value"
      tagNameMap[TagRawDevColorSpace] = "Raw Dev Color Space"
      tagNameMap[TagRawDevEngine] = "Raw Dev Engine"
      tagNameMap[TagRawDevNoiseReduction] = "Raw Dev Noise Reduction"
      tagNameMap[TagRawDevEditStatus] = "Raw Dev Edit Status"
      tagNameMap[TagRawDevSettings] = "Raw Dev Settings"
    }
  }

  override val name: String
    get() = "Olympus Raw Development"

  init {
    setDescriptor(OlympusRawDevelopmentMakernoteDescriptor(this))
  }
}
