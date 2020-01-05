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
 * Describes tags specific to Nikon (type 1) cameras.  Type-1 is for E-Series cameras prior to (not including) E990.
 *
 * There are 3 formats of Nikon's Makernote. Makernote of E700/E800/E900/E900S/E910/E950
 * starts from ASCII string "Nikon". Data format is the same as IFD, but it starts from
 * offset 0x08. This is the same as Olympus except start string. Example of actual data
 * structure is shown below.
 * <pre>`
 * :0000: 4E 69 6B 6F 6E 00 01 00-05 00 02 00 02 00 06 00 Nikon...........
 * :0010: 00 00 EC 02 00 00 03 00-03 00 01 00 00 00 06 00 ................
`</pre> *
 *
 * @author Drew Noakes https://drewnoakes.com
 */
class NikonType1MakernoteDirectory : Directory() {
  override val tagNameMap = Companion.tagNameMap

  companion object {
    const val TAG_UNKNOWN_1 = 0x0002
    const val TAG_QUALITY = 0x0003
    const val TAG_COLOR_MODE = 0x0004
    const val TAG_IMAGE_ADJUSTMENT = 0x0005
    const val TAG_CCD_SENSITIVITY = 0x0006
    const val TAG_WHITE_BALANCE = 0x0007
    const val TAG_FOCUS = 0x0008
    const val TAG_UNKNOWN_2 = 0x0009
    const val TAG_DIGITAL_ZOOM = 0x000A
    const val TAG_CONVERTER = 0x000B
    const val TAG_UNKNOWN_3 = 0x0F00
    protected val tagNameMap = HashMap<Int, String>()

    init {
      tagNameMap[TAG_CCD_SENSITIVITY] = "CCD Sensitivity"
      tagNameMap[TAG_COLOR_MODE] = "Color Mode"
      tagNameMap[TAG_DIGITAL_ZOOM] = "Digital Zoom"
      tagNameMap[TAG_CONVERTER] = "Fisheye Converter"
      tagNameMap[TAG_FOCUS] = "Focus"
      tagNameMap[TAG_IMAGE_ADJUSTMENT] = "Image Adjustment"
      tagNameMap[TAG_QUALITY] = "Quality"
      tagNameMap[TAG_UNKNOWN_1] = "Makernote Unknown 1"
      tagNameMap[TAG_UNKNOWN_2] = "Makernote Unknown 2"
      tagNameMap[TAG_UNKNOWN_3] = "Makernote Unknown 3"
      tagNameMap[TAG_WHITE_BALANCE] = "White Balance"
    }
  }

  override val name: String
    get() = "Nikon Makernote"

  init {
    setDescriptor(NikonType1MakernoteDescriptor(this))
  }
}
