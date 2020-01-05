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
 * Describes tags specific to Fujifilm cameras.
 *
 * @author Drew Noakes https://drewnoakes.com
 */
class FujifilmMakernoteDirectory : Directory() {
  override val tagNameMap = Companion.tagNameMap

  companion object {
    const val TAG_MAKERNOTE_VERSION = 0x0000
    const val TAG_SERIAL_NUMBER = 0x0010
    const val TAG_QUALITY = 0x1000
    const val TAG_SHARPNESS = 0x1001
    const val TAG_WHITE_BALANCE = 0x1002
    const val TAG_COLOR_SATURATION = 0x1003
    const val TAG_TONE = 0x1004
    const val TAG_COLOR_TEMPERATURE = 0x1005
    const val TAG_CONTRAST = 0x1006
    const val TAG_WHITE_BALANCE_FINE_TUNE = 0x100a
    const val TAG_NOISE_REDUCTION = 0x100b
    const val TAG_HIGH_ISO_NOISE_REDUCTION = 0x100e
    const val TAG_FLASH_MODE = 0x1010
    const val TAG_FLASH_EV = 0x1011
    const val TAG_MACRO = 0x1020
    const val TAG_FOCUS_MODE = 0x1021
    const val TAG_FOCUS_PIXEL = 0x1023
    const val TAG_SLOW_SYNC = 0x1030
    const val TAG_PICTURE_MODE = 0x1031
    const val TAG_EXR_AUTO = 0x1033
    const val TAG_EXR_MODE = 0x1034
    const val TAG_AUTO_BRACKETING = 0x1100
    const val TAG_SEQUENCE_NUMBER = 0x1101
    const val TAG_FINE_PIX_COLOR = 0x1210
    const val TAG_BLUR_WARNING = 0x1300
    const val TAG_FOCUS_WARNING = 0x1301
    const val TAG_AUTO_EXPOSURE_WARNING = 0x1302
    const val TAG_GE_IMAGE_SIZE = 0x1304
    const val TAG_DYNAMIC_RANGE = 0x1400
    const val TAG_FILM_MODE = 0x1401
    const val TAG_DYNAMIC_RANGE_SETTING = 0x1402
    const val TAG_DEVELOPMENT_DYNAMIC_RANGE = 0x1403
    const val TAG_MIN_FOCAL_LENGTH = 0x1404
    const val TAG_MAX_FOCAL_LENGTH = 0x1405
    const val TAG_MAX_APERTURE_AT_MIN_FOCAL = 0x1406
    const val TAG_MAX_APERTURE_AT_MAX_FOCAL = 0x1407
    const val TAG_AUTO_DYNAMIC_RANGE = 0x140b
    const val TAG_FACES_DETECTED = 0x4100
    /**
     * Left, top, right and bottom coordinates in full-sized image for each face detected.
     */
    const val TAG_FACE_POSITIONS = 0x4103
    const val TAG_FACE_REC_INFO = 0x4282
    const val TAG_FILE_SOURCE = 0x8000
    const val TAG_ORDER_NUMBER = 0x8002
    const val TAG_FRAME_NUMBER = 0x8003
    const val TAG_PARALLAX = 0xb211
    protected val tagNameMap = HashMap<Int, String>()

    init {
      tagNameMap[TAG_MAKERNOTE_VERSION] = "Makernote Version"
      tagNameMap[TAG_SERIAL_NUMBER] = "Serial Number"
      tagNameMap[TAG_QUALITY] = "Quality"
      tagNameMap[TAG_SHARPNESS] = "Sharpness"
      tagNameMap[TAG_WHITE_BALANCE] = "White Balance"
      tagNameMap[TAG_COLOR_SATURATION] = "Color Saturation"
      tagNameMap[TAG_TONE] = "Tone (Contrast)"
      tagNameMap[TAG_COLOR_TEMPERATURE] = "Color Temperature"
      tagNameMap[TAG_CONTRAST] = "Contrast"
      tagNameMap[TAG_WHITE_BALANCE_FINE_TUNE] = "White Balance Fine Tune"
      tagNameMap[TAG_NOISE_REDUCTION] = "Noise Reduction"
      tagNameMap[TAG_HIGH_ISO_NOISE_REDUCTION] = "High ISO Noise Reduction"
      tagNameMap[TAG_FLASH_MODE] = "Flash Mode"
      tagNameMap[TAG_FLASH_EV] = "Flash Strength"
      tagNameMap[TAG_MACRO] = "Macro"
      tagNameMap[TAG_FOCUS_MODE] = "Focus Mode"
      tagNameMap[TAG_FOCUS_PIXEL] = "Focus Pixel"
      tagNameMap[TAG_SLOW_SYNC] = "Slow Sync"
      tagNameMap[TAG_PICTURE_MODE] = "Picture Mode"
      tagNameMap[TAG_EXR_AUTO] = "EXR Auto"
      tagNameMap[TAG_EXR_MODE] = "EXR Mode"
      tagNameMap[TAG_AUTO_BRACKETING] = "Auto Bracketing"
      tagNameMap[TAG_SEQUENCE_NUMBER] = "Sequence Number"
      tagNameMap[TAG_FINE_PIX_COLOR] = "FinePix Color Setting"
      tagNameMap[TAG_BLUR_WARNING] = "Blur Warning"
      tagNameMap[TAG_FOCUS_WARNING] = "Focus Warning"
      tagNameMap[TAG_AUTO_EXPOSURE_WARNING] = "AE Warning"
      tagNameMap[TAG_GE_IMAGE_SIZE] = "GE Image Size"
      tagNameMap[TAG_DYNAMIC_RANGE] = "Dynamic Range"
      tagNameMap[TAG_FILM_MODE] = "Film Mode"
      tagNameMap[TAG_DYNAMIC_RANGE_SETTING] = "Dynamic Range Setting"
      tagNameMap[TAG_DEVELOPMENT_DYNAMIC_RANGE] = "Development Dynamic Range"
      tagNameMap[TAG_MIN_FOCAL_LENGTH] = "Minimum Focal Length"
      tagNameMap[TAG_MAX_FOCAL_LENGTH] = "Maximum Focal Length"
      tagNameMap[TAG_MAX_APERTURE_AT_MIN_FOCAL] = "Maximum Aperture at Minimum Focal Length"
      tagNameMap[TAG_MAX_APERTURE_AT_MAX_FOCAL] = "Maximum Aperture at Maximum Focal Length"
      tagNameMap[TAG_AUTO_DYNAMIC_RANGE] = "Auto Dynamic Range"
      tagNameMap[TAG_FACES_DETECTED] = "Faces Detected"
      tagNameMap[TAG_FACE_POSITIONS] = "Face Positions"
      tagNameMap[TAG_FACE_REC_INFO] = "Face Detection Data"
      tagNameMap[TAG_FILE_SOURCE] = "File Source"
      tagNameMap[TAG_ORDER_NUMBER] = "Order Number"
      tagNameMap[TAG_FRAME_NUMBER] = "Frame Number"
      tagNameMap[TAG_PARALLAX] = "Parallax"
    }
  }

  override val name: String
    get() = "Fujifilm Makernote"

  init {
    setDescriptor(FujifilmMakernoteDescriptor(this))
  }
}
