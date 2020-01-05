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

import com.drew.metadata.Directory
import java.util.*

/**
 * Describes tags specific to Casio (type 1) cameras.
 *
 * A standard TIFF IFD directory but always uses Motorola (Big-Endian) Byte Alignment.
 * Makernote data begins immediately (no header).
 *
 * @author Drew Noakes https://drewnoakes.com
 */
class CasioType1MakernoteDirectory : Directory() {
  override val tagNameMap = Companion.tagNameMap

  companion object {
    const val TAG_RECORDING_MODE = 0x0001
    const val TAG_QUALITY = 0x0002
    const val TAG_FOCUSING_MODE = 0x0003
    const val TAG_FLASH_MODE = 0x0004
    const val TAG_FLASH_INTENSITY = 0x0005
    const val TAG_OBJECT_DISTANCE = 0x0006
    const val TAG_WHITE_BALANCE = 0x0007
    const val TAG_UNKNOWN_1 = 0x0008
    const val TAG_UNKNOWN_2 = 0x0009
    const val TAG_DIGITAL_ZOOM = 0x000A
    const val TAG_SHARPNESS = 0x000B
    const val TAG_CONTRAST = 0x000C
    const val TAG_SATURATION = 0x000D
    const val TAG_UNKNOWN_3 = 0x000E
    const val TAG_UNKNOWN_4 = 0x000F
    const val TAG_UNKNOWN_5 = 0x0010
    const val TAG_UNKNOWN_6 = 0x0011
    const val TAG_UNKNOWN_7 = 0x0012
    const val TAG_UNKNOWN_8 = 0x0013
    const val TAG_CCD_SENSITIVITY = 0x0014
    protected val tagNameMap = HashMap<Int, String>()

    init {
      tagNameMap[TAG_CCD_SENSITIVITY] = "CCD Sensitivity"
      tagNameMap[TAG_CONTRAST] = "Contrast"
      tagNameMap[TAG_DIGITAL_ZOOM] = "Digital Zoom"
      tagNameMap[TAG_FLASH_INTENSITY] = "Flash Intensity"
      tagNameMap[TAG_FLASH_MODE] = "Flash Mode"
      tagNameMap[TAG_FOCUSING_MODE] = "Focusing Mode"
      tagNameMap[TAG_OBJECT_DISTANCE] = "Object Distance"
      tagNameMap[TAG_QUALITY] = "Quality"
      tagNameMap[TAG_RECORDING_MODE] = "Recording Mode"
      tagNameMap[TAG_SATURATION] = "Saturation"
      tagNameMap[TAG_SHARPNESS] = "Sharpness"
      tagNameMap[TAG_UNKNOWN_1] = "Makernote Unknown 1"
      tagNameMap[TAG_UNKNOWN_2] = "Makernote Unknown 2"
      tagNameMap[TAG_UNKNOWN_3] = "Makernote Unknown 3"
      tagNameMap[TAG_UNKNOWN_4] = "Makernote Unknown 4"
      tagNameMap[TAG_UNKNOWN_5] = "Makernote Unknown 5"
      tagNameMap[TAG_UNKNOWN_6] = "Makernote Unknown 6"
      tagNameMap[TAG_UNKNOWN_7] = "Makernote Unknown 7"
      tagNameMap[TAG_UNKNOWN_8] = "Makernote Unknown 8"
      tagNameMap[TAG_WHITE_BALANCE] = "White Balance"
    }
  }

  override val name: String
    get() = "Casio Makernote"

  init {
    setDescriptor(CasioType1MakernoteDescriptor(this))
  }
}
