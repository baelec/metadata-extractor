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
 * Describes tags specific to Sanyo cameras.
 *
 * @author Drew Noakes https://drewnoakes.com
 */
class SanyoMakernoteDirectory : Directory() {
  override val tagNameMap = Companion.tagNameMap

  companion object {
    const val TAG_MAKERNOTE_OFFSET = 0x00ff
    const val TAG_SANYO_THUMBNAIL = 0x0100
    const val TAG_SPECIAL_MODE = 0x0200
    const val TAG_SANYO_QUALITY = 0x0201
    const val TAG_MACRO = 0x0202
    const val TAG_DIGITAL_ZOOM = 0x0204
    const val TAG_SOFTWARE_VERSION = 0x0207
    const val TAG_PICT_INFO = 0x0208
    const val TAG_CAMERA_ID = 0x0209
    const val TAG_SEQUENTIAL_SHOT = 0x020e
    const val TAG_WIDE_RANGE = 0x020f
    const val TAG_COLOR_ADJUSTMENT_MODE = 0x0210
    const val TAG_QUICK_SHOT = 0x0213
    const val TAG_SELF_TIMER = 0x0214
    const val TAG_VOICE_MEMO = 0x0216
    const val TAG_RECORD_SHUTTER_RELEASE = 0x0217
    const val TAG_FLICKER_REDUCE = 0x0218
    const val TAG_OPTICAL_ZOOM_ON = 0x0219
    const val TAG_DIGITAL_ZOOM_ON = 0x021b
    const val TAG_LIGHT_SOURCE_SPECIAL = 0x021d
    const val TAG_RESAVED = 0x021e
    const val TAG_SCENE_SELECT = 0x021f
    const val TAG_MANUAL_FOCUS_DISTANCE_OR_FACE_INFO = 0x0223
    const val TAG_SEQUENCE_SHOT_INTERVAL = 0x0224
    const val TAG_FLASH_MODE = 0x0225
    const val TAG_PRINT_IMAGE_MATCHING_INFO = 0x0E00
    const val TAG_DATA_DUMP = 0x0f00
    protected val tagNameMap = HashMap<Int, String>()

    init {
      tagNameMap[TAG_MAKERNOTE_OFFSET] = "Makernote Offset"
      tagNameMap[TAG_SANYO_THUMBNAIL] = "Sanyo Thumbnail"
      tagNameMap[TAG_SPECIAL_MODE] = "Special Mode"
      tagNameMap[TAG_SANYO_QUALITY] = "Sanyo Quality"
      tagNameMap[TAG_MACRO] = "Macro"
      tagNameMap[TAG_DIGITAL_ZOOM] = "Digital Zoom"
      tagNameMap[TAG_SOFTWARE_VERSION] = "Software Version"
      tagNameMap[TAG_PICT_INFO] = "Pict Info"
      tagNameMap[TAG_CAMERA_ID] = "Camera ID"
      tagNameMap[TAG_SEQUENTIAL_SHOT] = "Sequential Shot"
      tagNameMap[TAG_WIDE_RANGE] = "Wide Range"
      tagNameMap[TAG_COLOR_ADJUSTMENT_MODE] = "Color Adjustment Node"
      tagNameMap[TAG_QUICK_SHOT] = "Quick Shot"
      tagNameMap[TAG_SELF_TIMER] = "Self Timer"
      tagNameMap[TAG_VOICE_MEMO] = "Voice Memo"
      tagNameMap[TAG_RECORD_SHUTTER_RELEASE] = "Record Shutter Release"
      tagNameMap[TAG_FLICKER_REDUCE] = "Flicker Reduce"
      tagNameMap[TAG_OPTICAL_ZOOM_ON] = "Optical Zoom On"
      tagNameMap[TAG_DIGITAL_ZOOM_ON] = "Digital Zoom On"
      tagNameMap[TAG_LIGHT_SOURCE_SPECIAL] = "Light Source Special"
      tagNameMap[TAG_RESAVED] = "Resaved"
      tagNameMap[TAG_SCENE_SELECT] = "Scene Select"
      tagNameMap[TAG_MANUAL_FOCUS_DISTANCE_OR_FACE_INFO] = "Manual Focus Distance or Face Info"
      tagNameMap[TAG_SEQUENCE_SHOT_INTERVAL] = "Sequence Shot Interval"
      tagNameMap[TAG_FLASH_MODE] = "Flash Mode"
      tagNameMap[TAG_PRINT_IMAGE_MATCHING_INFO] = "Print IM"
      tagNameMap[TAG_DATA_DUMP] = "Data Dump"
    }
  }

  override val name: String
    get() = "Sanyo Makernote"

  init {
    setDescriptor(SanyoMakernoteDescriptor(this))
  }
}
