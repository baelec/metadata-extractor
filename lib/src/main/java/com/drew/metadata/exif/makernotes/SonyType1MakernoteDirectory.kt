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
 * Describes tags specific to Sony cameras that use the Sony Type 1 makernote tags.
 *
 * @author Drew Noakes https://drewnoakes.com
 */
class SonyType1MakernoteDirectory : Directory() {
  override val tagNameMap = Companion.tagNameMap

  companion object {
    const val TAG_CAMERA_INFO = 0x0010
    const val TAG_FOCUS_INFO = 0x0020
    const val TAG_IMAGE_QUALITY = 0x0102
    const val TAG_FLASH_EXPOSURE_COMP = 0x0104
    const val TAG_TELECONVERTER = 0x0105
    const val TAG_WHITE_BALANCE_FINE_TUNE = 0x0112
    const val TAG_CAMERA_SETTINGS = 0x0114
    const val TAG_WHITE_BALANCE = 0x0115
    const val TAG_EXTRA_INFO = 0x0116
    const val TAG_PRINT_IMAGE_MATCHING_INFO = 0x0E00
    const val TAG_MULTI_BURST_MODE = 0x1000
    const val TAG_MULTI_BURST_IMAGE_WIDTH = 0x1001
    const val TAG_MULTI_BURST_IMAGE_HEIGHT = 0x1002
    const val TAG_PANORAMA = 0x1003
    const val TAG_PREVIEW_IMAGE = 0x2001
    const val TAG_RATING = 0x2002
    const val TAG_CONTRAST = 0x2004
    const val TAG_SATURATION = 0x2005
    const val TAG_SHARPNESS = 0x2006
    const val TAG_BRIGHTNESS = 0x2007
    const val TAG_LONG_EXPOSURE_NOISE_REDUCTION = 0x2008
    const val TAG_HIGH_ISO_NOISE_REDUCTION = 0x2009
    const val TAG_HDR = 0x200a
    const val TAG_MULTI_FRAME_NOISE_REDUCTION = 0x200b
    const val TAG_PICTURE_EFFECT = 0x200e
    const val TAG_SOFT_SKIN_EFFECT = 0x200f
    const val TAG_VIGNETTING_CORRECTION = 0x2011
    const val TAG_LATERAL_CHROMATIC_ABERRATION = 0x2012
    const val TAG_DISTORTION_CORRECTION = 0x2013
    const val TAG_WB_SHIFT_AMBER_MAGENTA = 0x2014
    const val TAG_AUTO_PORTRAIT_FRAMED = 0x2016
    const val TAG_FOCUS_MODE = 0x201b
    const val TAG_AF_POINT_SELECTED = 0x201e
    const val TAG_SHOT_INFO = 0x3000
    const val TAG_FILE_FORMAT = 0xb000
    const val TAG_SONY_MODEL_ID = 0xb001
    const val TAG_COLOR_MODE_SETTING = 0xb020
    const val TAG_COLOR_TEMPERATURE = 0xb021
    const val TAG_COLOR_COMPENSATION_FILTER = 0xb022
    const val TAG_SCENE_MODE = 0xb023
    const val TAG_ZONE_MATCHING = 0xb024
    const val TAG_DYNAMIC_RANGE_OPTIMISER = 0xb025
    const val TAG_IMAGE_STABILISATION = 0xb026
    const val TAG_LENS_ID = 0xb027
    const val TAG_MINOLTA_MAKERNOTE = 0xb028
    const val TAG_COLOR_MODE = 0xb029
    const val TAG_LENS_SPEC = 0xb02a
    const val TAG_FULL_IMAGE_SIZE = 0xb02b
    const val TAG_PREVIEW_IMAGE_SIZE = 0xb02c
    const val TAG_MACRO = 0xb040
    const val TAG_EXPOSURE_MODE = 0xb041
    const val TAG_FOCUS_MODE_2 = 0xb042
    const val TAG_AF_MODE = 0xb043
    const val TAG_AF_ILLUMINATOR = 0xb044
    const val TAG_JPEG_QUALITY = 0xb047
    const val TAG_FLASH_LEVEL = 0xb048
    const val TAG_RELEASE_MODE = 0xb049
    const val TAG_SEQUENCE_NUMBER = 0xb04a
    const val TAG_ANTI_BLUR = 0xb04b
    /**
     * (FocusMode for RX100)
     * 0 = Manual
     * 2 = AF-S
     * 3 = AF-C
     * 5 = Semi-manual
     * 6 = Direct Manual Focus
     * (LongExposureNoiseReduction for other models)
     * 0 = Off
     * 1 = On
     * 2 = On 2
     * 65535 = n/a
     */
    const val TAG_LONG_EXPOSURE_NOISE_REDUCTION_OR_FOCUS_MODE = 0xb04e
    const val TAG_DYNAMIC_RANGE_OPTIMIZER = 0xb04f
    const val TAG_HIGH_ISO_NOISE_REDUCTION_2 = 0xb050
    const val TAG_INTELLIGENT_AUTO = 0xb052
    const val TAG_WHITE_BALANCE_2 = 0xb054
    const val TAG_NO_PRINT = 0xFFFF
    protected val tagNameMap = HashMap<Int, String>()

    init {
      tagNameMap[TAG_CAMERA_INFO] = "Camera Info"
      tagNameMap[TAG_FOCUS_INFO] = "Focus Info"
      tagNameMap[TAG_IMAGE_QUALITY] = "Image Quality"
      tagNameMap[TAG_FLASH_EXPOSURE_COMP] = "Flash Exposure Compensation"
      tagNameMap[TAG_TELECONVERTER] = "Teleconverter Model"
      tagNameMap[TAG_WHITE_BALANCE_FINE_TUNE] = "White Balance Fine Tune Value"
      tagNameMap[TAG_CAMERA_SETTINGS] = "Camera Settings"
      tagNameMap[TAG_WHITE_BALANCE] = "White Balance"
      tagNameMap[TAG_EXTRA_INFO] = "Extra Info"
      tagNameMap[TAG_PRINT_IMAGE_MATCHING_INFO] = "Print Image Matching (PIM) Info"
      tagNameMap[TAG_MULTI_BURST_MODE] = "Multi Burst Mode"
      tagNameMap[TAG_MULTI_BURST_IMAGE_WIDTH] = "Multi Burst Image Width"
      tagNameMap[TAG_MULTI_BURST_IMAGE_HEIGHT] = "Multi Burst Image Height"
      tagNameMap[TAG_PANORAMA] = "Panorama"
      tagNameMap[TAG_PREVIEW_IMAGE] = "Preview Image"
      tagNameMap[TAG_RATING] = "Rating"
      tagNameMap[TAG_CONTRAST] = "Contrast"
      tagNameMap[TAG_SATURATION] = "Saturation"
      tagNameMap[TAG_SHARPNESS] = "Sharpness"
      tagNameMap[TAG_BRIGHTNESS] = "Brightness"
      tagNameMap[TAG_LONG_EXPOSURE_NOISE_REDUCTION] = "Long Exposure Noise Reduction"
      tagNameMap[TAG_HIGH_ISO_NOISE_REDUCTION] = "High ISO Noise Reduction"
      tagNameMap[TAG_HDR] = "HDR"
      tagNameMap[TAG_MULTI_FRAME_NOISE_REDUCTION] = "Multi Frame Noise Reduction"
      tagNameMap[TAG_PICTURE_EFFECT] = "Picture Effect"
      tagNameMap[TAG_SOFT_SKIN_EFFECT] = "Soft Skin Effect"
      tagNameMap[TAG_VIGNETTING_CORRECTION] = "Vignetting Correction"
      tagNameMap[TAG_LATERAL_CHROMATIC_ABERRATION] = "Lateral Chromatic Aberration"
      tagNameMap[TAG_DISTORTION_CORRECTION] = "Distortion Correction"
      tagNameMap[TAG_WB_SHIFT_AMBER_MAGENTA] = "WB Shift Amber/Magenta"
      tagNameMap[TAG_AUTO_PORTRAIT_FRAMED] = "Auto Portrait Framing"
      tagNameMap[TAG_FOCUS_MODE] = "Focus Mode"
      tagNameMap[TAG_AF_POINT_SELECTED] = "AF Point Selected"
      tagNameMap[TAG_SHOT_INFO] = "Shot Info"
      tagNameMap[TAG_FILE_FORMAT] = "File Format"
      tagNameMap[TAG_SONY_MODEL_ID] = "Sony Model ID"
      tagNameMap[TAG_COLOR_MODE_SETTING] = "Color Mode Setting"
      tagNameMap[TAG_COLOR_TEMPERATURE] = "Color Temperature"
      tagNameMap[TAG_COLOR_COMPENSATION_FILTER] = "Color Compensation Filter"
      tagNameMap[TAG_SCENE_MODE] = "Scene Mode"
      tagNameMap[TAG_ZONE_MATCHING] = "Zone Matching"
      tagNameMap[TAG_DYNAMIC_RANGE_OPTIMISER] = "Dynamic Range Optimizer"
      tagNameMap[TAG_IMAGE_STABILISATION] = "Image Stabilisation"
      tagNameMap[TAG_LENS_ID] = "Lens ID"
      tagNameMap[TAG_MINOLTA_MAKERNOTE] = "Minolta Makernote"
      tagNameMap[TAG_COLOR_MODE] = "Color Mode"
      tagNameMap[TAG_LENS_SPEC] = "Lens Spec"
      tagNameMap[TAG_FULL_IMAGE_SIZE] = "Full Image Size"
      tagNameMap[TAG_PREVIEW_IMAGE_SIZE] = "Preview Image Size"
      tagNameMap[TAG_MACRO] = "Macro"
      tagNameMap[TAG_EXPOSURE_MODE] = "Exposure Mode"
      tagNameMap[TAG_FOCUS_MODE_2] = "Focus Mode"
      tagNameMap[TAG_AF_MODE] = "AF Mode"
      tagNameMap[TAG_AF_ILLUMINATOR] = "AF Illuminator"
      tagNameMap[TAG_JPEG_QUALITY] = "Quality"
      tagNameMap[TAG_FLASH_LEVEL] = "Flash Level"
      tagNameMap[TAG_RELEASE_MODE] = "Release Mode"
      tagNameMap[TAG_SEQUENCE_NUMBER] = "Sequence Number"
      tagNameMap[TAG_ANTI_BLUR] = "Anti Blur"
      tagNameMap[TAG_LONG_EXPOSURE_NOISE_REDUCTION_OR_FOCUS_MODE] = "Long Exposure Noise Reduction"
      tagNameMap[TAG_DYNAMIC_RANGE_OPTIMIZER] = "Dynamic Range Optimizer"
      tagNameMap[TAG_HIGH_ISO_NOISE_REDUCTION_2] = "High ISO Noise Reduction"
      tagNameMap[TAG_INTELLIGENT_AUTO] = "Intelligent Auto"
      tagNameMap[TAG_WHITE_BALANCE_2] = "White Balance 2"
      tagNameMap[TAG_NO_PRINT] = "No Print"
    }
  }

  override val name: String
    get() = "Sony Makernote"

  init {
    setDescriptor(SonyType1MakernoteDescriptor(this))
  }
}
