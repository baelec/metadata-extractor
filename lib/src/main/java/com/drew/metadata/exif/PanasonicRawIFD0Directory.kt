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

import com.drew.metadata.Directory
import java.util.*

/**
 * These tags are found in IFD0 of Panasonic/Leica RAW, RW2 and RWL images.
 *
 * @author Kevin Mott https://github.com/kwhopper
 * @author Drew Noakes https://drewnoakes.com
 */
class PanasonicRawIFD0Directory : Directory() {
  override val tagNameMap = Companion.tagNameMap

  companion object {
    const val TagPanasonicRawVersion = 0x0001
    const val TagSensorWidth = 0x0002
    const val TagSensorHeight = 0x0003
    const val TagSensorTopBorder = 0x0004
    const val TagSensorLeftBorder = 0x0005
    const val TagSensorBottomBorder = 0x0006
    const val TagSensorRightBorder = 0x0007
    const val TagBlackLevel1 = 0x0008
    const val TagBlackLevel2 = 0x0009
    const val TagBlackLevel3 = 0x000a
    const val TagLinearityLimitRed = 0x000e
    const val TagLinearityLimitGreen = 0x000f
    const val TagLinearityLimitBlue = 0x0010
    const val TagRedBalance = 0x0011
    const val TagBlueBalance = 0x0012
    const val TagWbInfo = 0x0013
    const val TagIso = 0x0017
    const val TagHighIsoMultiplierRed = 0x0018
    const val TagHighIsoMultiplierGreen = 0x0019
    const val TagHighIsoMultiplierBlue = 0x001a
    const val TagBlackLevelRed = 0x001c
    const val TagBlackLevelGreen = 0x001d
    const val TagBlackLevelBlue = 0x001e
    const val TagWbRedLevel = 0x0024
    const val TagWbGreenLevel = 0x0025
    const val TagWbBlueLevel = 0x0026
    const val TagWbInfo2 = 0x0027
    const val TagJpgFromRaw = 0x002e
    const val TagCropTop = 0x002f
    const val TagCropLeft = 0x0030
    const val TagCropBottom = 0x0031
    const val TagCropRight = 0x0032
    const val TagMake = 0x010f
    const val TagModel = 0x0110
    const val TagStripOffsets = 0x0111
    const val TagOrientation = 0x0112
    const val TagRowsPerStrip = 0x0116
    const val TagStripByteCounts = 0x0117
    const val TagRawDataOffset = 0x0118
    const val TagDistortionInfo = 0x0119
    protected val tagNameMap = HashMap<Int, String>()

    init {
      tagNameMap[TagPanasonicRawVersion] = "Panasonic Raw Version"
      tagNameMap[TagSensorWidth] = "Sensor Width"
      tagNameMap[TagSensorHeight] = "Sensor Height"
      tagNameMap[TagSensorTopBorder] = "Sensor Top Border"
      tagNameMap[TagSensorLeftBorder] = "Sensor Left Border"
      tagNameMap[TagSensorBottomBorder] = "Sensor Bottom Border"
      tagNameMap[TagSensorRightBorder] = "Sensor Right Border"
      tagNameMap[TagBlackLevel1] = "Black Level 1"
      tagNameMap[TagBlackLevel2] = "Black Level 2"
      tagNameMap[TagBlackLevel3] = "Black Level 3"
      tagNameMap[TagLinearityLimitRed] = "Linearity Limit Red"
      tagNameMap[TagLinearityLimitGreen] = "Linearity Limit Green"
      tagNameMap[TagLinearityLimitBlue] = "Linearity Limit Blue"
      tagNameMap[TagRedBalance] = "Red Balance"
      tagNameMap[TagBlueBalance] = "Blue Balance"
      tagNameMap[TagIso] = "ISO"
      tagNameMap[TagHighIsoMultiplierRed] = "High ISO Multiplier Red"
      tagNameMap[TagHighIsoMultiplierGreen] = "High ISO Multiplier Green"
      tagNameMap[TagHighIsoMultiplierBlue] = "High ISO Multiplier Blue"
      tagNameMap[TagBlackLevelRed] = "Black Level Red"
      tagNameMap[TagBlackLevelGreen] = "Black Level Green"
      tagNameMap[TagBlackLevelBlue] = "Black Level Blue"
      tagNameMap[TagWbRedLevel] = "WB Red Level"
      tagNameMap[TagWbGreenLevel] = "WB Green Level"
      tagNameMap[TagWbBlueLevel] = "WB Blue Level"
      tagNameMap[TagJpgFromRaw] = "Jpg From Raw"
      tagNameMap[TagCropTop] = "Crop Top"
      tagNameMap[TagCropLeft] = "Crop Left"
      tagNameMap[TagCropBottom] = "Crop Bottom"
      tagNameMap[TagCropRight] = "Crop Right"
      tagNameMap[TagMake] = "Make"
      tagNameMap[TagModel] = "Model"
      tagNameMap[TagStripOffsets] = "Strip Offsets"
      tagNameMap[TagOrientation] = "Orientation"
      tagNameMap[TagRowsPerStrip] = "Rows Per Strip"
      tagNameMap[TagStripByteCounts] = "Strip Byte Counts"
      tagNameMap[TagRawDataOffset] = "Raw Data Offset"
    }
  }

  override val name: String
    get() = "PanasonicRaw Exif IFD0"

  init {
    setDescriptor(PanasonicRawIFD0Descriptor(this))
  }
}
