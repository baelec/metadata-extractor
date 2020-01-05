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
 * The Olympus raw development 2 makernote is used by many manufacturers (Epson, Konica, Minolta and Agfa...), and as such contains some tags
 * that appear specific to those manufacturers.
 *
 * @author Kevin Mott https://github.com/kwhopper
 * @author Drew Noakes https://drewnoakes.com
 */
class OlympusRawDevelopment2MakernoteDirectory : Directory() {
  override val tagNameMap = Companion.tagNameMap

  companion object {
    const val TagRawDevVersion = 0x0000
    const val TagRawDevExposureBiasValue = 0x0100
    const val TagRawDevWhiteBalance = 0x0101
    const val TagRawDevWhiteBalanceValue = 0x0102
    const val TagRawDevWbFineAdjustment = 0x0103
    const val TagRawDevGrayPoint = 0x0104
    const val TagRawDevContrastValue = 0x0105
    const val TagRawDevSharpnessValue = 0x0106
    const val TagRawDevSaturationEmphasis = 0x0107
    const val TagRawDevMemoryColorEmphasis = 0x0108
    const val TagRawDevColorSpace = 0x0109
    const val TagRawDevNoiseReduction = 0x010a
    const val TagRawDevEngine = 0x010b
    const val TagRawDevPictureMode = 0x010c
    const val TagRawDevPmSaturation = 0x010d
    const val TagRawDevPmContrast = 0x010e
    const val TagRawDevPmSharpness = 0x010f
    const val TagRawDevPmBwFilter = 0x0110
    const val TagRawDevPmPictureTone = 0x0111
    const val TagRawDevGradation = 0x0112
    const val TagRawDevSaturation3 = 0x0113
    const val TagRawDevAutoGradation = 0x0119
    const val TagRawDevPmNoiseFilter = 0x0120
    const val TagRawDevArtFilter = 0x0121
    protected val tagNameMap = HashMap<Int, String>()

    init {
      tagNameMap[TagRawDevVersion] = "Raw Dev Version"
      tagNameMap[TagRawDevExposureBiasValue] = "Raw Dev Exposure Bias Value"
      tagNameMap[TagRawDevWhiteBalance] = "Raw Dev White Balance"
      tagNameMap[TagRawDevWhiteBalanceValue] = "Raw Dev White Balance Value"
      tagNameMap[TagRawDevWbFineAdjustment] = "Raw Dev WB Fine Adjustment"
      tagNameMap[TagRawDevGrayPoint] = "Raw Dev Gray Point"
      tagNameMap[TagRawDevContrastValue] = "Raw Dev Contrast Value"
      tagNameMap[TagRawDevSharpnessValue] = "Raw Dev Sharpness Value"
      tagNameMap[TagRawDevSaturationEmphasis] = "Raw Dev Saturation Emphasis"
      tagNameMap[TagRawDevMemoryColorEmphasis] = "Raw Dev Memory Color Emphasis"
      tagNameMap[TagRawDevColorSpace] = "Raw Dev Color Space"
      tagNameMap[TagRawDevNoiseReduction] = "Raw Dev Noise Reduction"
      tagNameMap[TagRawDevEngine] = "Raw Dev Engine"
      tagNameMap[TagRawDevPictureMode] = "Raw Dev Picture Mode"
      tagNameMap[TagRawDevPmSaturation] = "Raw Dev PM Saturation"
      tagNameMap[TagRawDevPmContrast] = "Raw Dev PM Contrast"
      tagNameMap[TagRawDevPmSharpness] = "Raw Dev PM Sharpness"
      tagNameMap[TagRawDevPmBwFilter] = "Raw Dev PM BW Filter"
      tagNameMap[TagRawDevPmPictureTone] = "Raw Dev PM Picture Tone"
      tagNameMap[TagRawDevGradation] = "Raw Dev Gradation"
      tagNameMap[TagRawDevSaturation3] = "Raw Dev Saturation 3"
      tagNameMap[TagRawDevAutoGradation] = "Raw Dev Auto Gradation"
      tagNameMap[TagRawDevPmNoiseFilter] = "Raw Dev PM Noise Filter"
      tagNameMap[TagRawDevArtFilter] = "Raw Dev Art Filter"
    }
  }

  override val name: String
    get() = "Olympus Raw Development 2"

  init {
    setDescriptor(OlympusRawDevelopment2MakernoteDescriptor(this))
  }
}
