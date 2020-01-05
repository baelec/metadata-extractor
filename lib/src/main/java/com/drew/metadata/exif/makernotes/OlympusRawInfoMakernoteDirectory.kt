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
 * These tags are found only in ORF images of some models (eg. C8080WZ)
 *
 * @author Kevin Mott https://github.com/kwhopper
 * @author Drew Noakes https://drewnoakes.com
 */
class OlympusRawInfoMakernoteDirectory : Directory() {
  override val tagNameMap = Companion.tagNameMap

  companion object {
    const val TagRawInfoVersion = 0x0000
    const val TagWbRbLevelsUsed = 0x0100
    const val TagWbRbLevelsAuto = 0x0110
    const val TagWbRbLevelsShade = 0x0120
    const val TagWbRbLevelsCloudy = 0x0121
    const val TagWbRbLevelsFineWeather = 0x0122
    const val TagWbRbLevelsTungsten = 0x0123
    const val TagWbRbLevelsEveningSunlight = 0x0124
    const val TagWbRbLevelsDaylightFluor = 0x0130
    const val TagWbRbLevelsDayWhiteFluor = 0x0131
    const val TagWbRbLevelsCoolWhiteFluor = 0x0132
    const val TagWbRbLevelsWhiteFluorescent = 0x0133
    const val TagColorMatrix2 = 0x0200
    const val TagCoringFilter = 0x0310
    const val TagCoringValues = 0x0311
    const val TagBlackLevel2 = 0x0600
    const val TagYCbCrCoefficients = 0x0601
    const val TagValidPixelDepth = 0x0611
    const val TagCropLeft = 0x0612
    const val TagCropTop = 0x0613
    const val TagCropWidth = 0x0614
    const val TagCropHeight = 0x0615
    const val TagLightSource = 0x1000
    //the following 5 tags all have 3 values: val, min, max
    const val TagWhiteBalanceComp = 0x1001
    const val TagSaturationSetting = 0x1010
    const val TagHueSetting = 0x1011
    const val TagContrastSetting = 0x1012
    const val TagSharpnessSetting = 0x1013
    // settings written by Camedia Master 4.x
    const val TagCmExposureCompensation = 0x2000
    const val TagCmWhiteBalance = 0x2001
    const val TagCmWhiteBalanceComp = 0x2002
    const val TagCmWhiteBalanceGrayPoint = 0x2010
    const val TagCmSaturation = 0x2020
    const val TagCmHue = 0x2021
    const val TagCmContrast = 0x2022
    const val TagCmSharpness = 0x2023
    protected val tagNameMap = HashMap<Int, String>()

    init {
      tagNameMap[TagRawInfoVersion] = "Raw Info Version"
      tagNameMap[TagWbRbLevelsUsed] = "WB RB Levels Used"
      tagNameMap[TagWbRbLevelsAuto] = "WB RB Levels Auto"
      tagNameMap[TagWbRbLevelsShade] = "WB RB Levels Shade"
      tagNameMap[TagWbRbLevelsCloudy] = "WB RB Levels Cloudy"
      tagNameMap[TagWbRbLevelsFineWeather] = "WB RB Levels Fine Weather"
      tagNameMap[TagWbRbLevelsTungsten] = "WB RB Levels Tungsten"
      tagNameMap[TagWbRbLevelsEveningSunlight] = "WB RB Levels Evening Sunlight"
      tagNameMap[TagWbRbLevelsDaylightFluor] = "WB RB Levels Daylight Fluor"
      tagNameMap[TagWbRbLevelsDayWhiteFluor] = "WB RB Levels Day White Fluor"
      tagNameMap[TagWbRbLevelsCoolWhiteFluor] = "WB RB Levels Cool White Fluor"
      tagNameMap[TagWbRbLevelsWhiteFluorescent] = "WB RB Levels White Fluorescent"
      tagNameMap[TagColorMatrix2] = "Color Matrix 2"
      tagNameMap[TagCoringFilter] = "Coring Filter"
      tagNameMap[TagCoringValues] = "Coring Values"
      tagNameMap[TagBlackLevel2] = "Black Level 2"
      tagNameMap[TagYCbCrCoefficients] = "YCbCrCoefficients"
      tagNameMap[TagValidPixelDepth] = "Valid Pixel Depth"
      tagNameMap[TagCropLeft] = "Crop Left"
      tagNameMap[TagCropTop] = "Crop Top"
      tagNameMap[TagCropWidth] = "Crop Width"
      tagNameMap[TagCropHeight] = "Crop Height"
      tagNameMap[TagLightSource] = "Light Source"
      tagNameMap[TagWhiteBalanceComp] = "White Balance Comp"
      tagNameMap[TagSaturationSetting] = "Saturation Setting"
      tagNameMap[TagHueSetting] = "Hue Setting"
      tagNameMap[TagContrastSetting] = "Contrast Setting"
      tagNameMap[TagSharpnessSetting] = "Sharpness Setting"
      tagNameMap[TagCmExposureCompensation] = "CM Exposure Compensation"
      tagNameMap[TagCmWhiteBalance] = "CM White Balance"
      tagNameMap[TagCmWhiteBalanceComp] = "CM White Balance Comp"
      tagNameMap[TagCmWhiteBalanceGrayPoint] = "CM White Balance Gray Point"
      tagNameMap[TagCmSaturation] = "CM Saturation"
      tagNameMap[TagCmHue] = "CM Hue"
      tagNameMap[TagCmContrast] = "CM Contrast"
      tagNameMap[TagCmSharpness] = "CM Sharpness"
    }
  }

  override val name: String
    get() = "Olympus Raw Info"

  init {
    setDescriptor(OlympusRawInfoMakernoteDescriptor(this))
  }
}
