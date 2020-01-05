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
 * Describes tags specific to Canon cameras.
 *
 * Thanks to Bill Richards for his contribution to this makernote directory.
 *
 * Many tag definitions explained here: http://www.ozhiker.com/electronics/pjmt/jpeg_info/canon_mn.html
 *
 * @author Drew Noakes https://drewnoakes.com
 */
class CanonMakernoteDirectory : Directory() {
  override val tagNameMap = Companion.tagNameMap

  object CameraSettings {
    // These 'sub'-tag values have been created for consistency -- they don't exist within the exif segment
    const val OFFSET = 0xC100
    /**
     * 1 = Macro
     * 2 = Normal
     */
    const val TAG_MACRO_MODE = OFFSET + 0x01
    const val TAG_SELF_TIMER_DELAY = OFFSET + 0x02
    /**
     * 2 = Normal
     * 3 = Fine
     * 5 = Superfine
     */
    const val TAG_QUALITY = OFFSET + 0x03
    /**
     * 0 = Flash Not Fired
     * 1 = Auto
     * 2 = On
     * 3 = Red Eye Reduction
     * 4 = Slow Synchro
     * 5 = Auto + Red Eye Reduction
     * 6 = On + Red Eye Reduction
     * 16 = External Flash
     */
    const val TAG_FLASH_MODE = OFFSET + 0x04
    /**
     * 0 = Single Frame or Timer Mode
     * 1 = Continuous
     */
    const val TAG_CONTINUOUS_DRIVE_MODE = OFFSET + 0x05
    const val TAG_UNKNOWN_2 = OFFSET + 0x06
    /**
     * 0 = One-Shot
     * 1 = AI Servo
     * 2 = AI Focus
     * 3 = Manual Focus
     * 4 = Single
     * 5 = Continuous
     * 6 = Manual Focus
     */
    const val TAG_FOCUS_MODE_1 = OFFSET + 0x07
    const val TAG_UNKNOWN_3 = OFFSET + 0x08
    const val TAG_RECORD_MODE = OFFSET + 0x09
    /**
     * 0 = Large
     * 1 = Medium
     * 2 = Small
     */
    const val TAG_IMAGE_SIZE = OFFSET + 0x0A
    /**
     * 0 = Full Auto
     * 1 = Manual
     * 2 = Landscape
     * 3 = Fast Shutter
     * 4 = Slow Shutter
     * 5 = Night
     * 6 = Black &amp; White
     * 7 = Sepia
     * 8 = Portrait
     * 9 = Sports
     * 10 = Macro / Close-Up
     * 11 = Pan Focus
     */
    const val TAG_EASY_SHOOTING_MODE = OFFSET + 0x0B
    /**
     * 0 = No Digital Zoom
     * 1 = 2x
     * 2 = 4x
     */
    const val TAG_DIGITAL_ZOOM = OFFSET + 0x0C
    /**
     * 0 = Normal
     * 1 = High
     * 65535 = Low
     */
    const val TAG_CONTRAST = OFFSET + 0x0D
    /**
     * 0 = Normal
     * 1 = High
     * 65535 = Low
     */
    const val TAG_SATURATION = OFFSET + 0x0E
    /**
     * 0 = Normal
     * 1 = High
     * 65535 = Low
     */
    const val TAG_SHARPNESS = OFFSET + 0x0F
    /**
     * 0 = Check ISOSpeedRatings EXIF tag for ISO Speed
     * 15 = Auto ISO
     * 16 = ISO 50
     * 17 = ISO 100
     * 18 = ISO 200
     * 19 = ISO 400
     */
    const val TAG_ISO = OFFSET + 0x10
    /**
     * 3 = Evaluative
     * 4 = Partial
     * 5 = Centre Weighted
     */
    const val TAG_METERING_MODE = OFFSET + 0x11
    /**
     * 0 = Manual
     * 1 = Auto
     * 3 = Close-up (Macro)
     * 8 = Locked (Pan Mode)
     */
    const val TAG_FOCUS_TYPE = OFFSET + 0x12
    /**
     * 12288 = None (Manual Focus)
     * 12289 = Auto Selected
     * 12290 = Right
     * 12291 = Centre
     * 12292 = Left
     */
    const val TAG_AF_POINT_SELECTED = OFFSET + 0x13
    /**
     * 0 = Easy Shooting (See Easy Shooting Mode)
     * 1 = Program
     * 2 = Tv-Priority
     * 3 = Av-Priority
     * 4 = Manual
     * 5 = A-DEP
     */
    const val TAG_EXPOSURE_MODE = OFFSET + 0x14
    const val TAG_UNKNOWN_7 = OFFSET + 0x15
    const val TAG_LENS_TYPE = OFFSET + 0x16
    const val TAG_LONG_FOCAL_LENGTH = OFFSET + 0x17
    const val TAG_SHORT_FOCAL_LENGTH = OFFSET + 0x18
    const val TAG_FOCAL_UNITS_PER_MM = OFFSET + 0x19
    const val TAG_MAX_APERTURE = OFFSET + 0x1A
    const val TAG_MIN_APERTURE = OFFSET + 0x1B
    /**
     * 0 = Flash Did Not Fire
     * 1 = Flash Fired
     */
    const val TAG_FLASH_ACTIVITY = OFFSET + 0x1C
    const val TAG_FLASH_DETAILS = OFFSET + 0x1D
    const val TAG_FOCUS_CONTINUOUS = OFFSET + 0x1E
    const val TAG_AE_SETTING = OFFSET + 0x1F
    /**
     * 0 = Focus Mode: Single
     * 1 = Focus Mode: Continuous
     */
    const val TAG_FOCUS_MODE_2 = OFFSET + 0x20
    const val TAG_DISPLAY_APERTURE = OFFSET + 0x21
    const val TAG_ZOOM_SOURCE_WIDTH = OFFSET + 0x22
    const val TAG_ZOOM_TARGET_WIDTH = OFFSET + 0x23
    const val TAG_SPOT_METERING_MODE = OFFSET + 0x25
    const val TAG_PHOTO_EFFECT = OFFSET + 0x26
    const val TAG_MANUAL_FLASH_OUTPUT = OFFSET + 0x27
    const val TAG_COLOR_TONE = OFFSET + 0x29
    const val TAG_SRAW_QUALITY = OFFSET + 0x2D
  }

  object FocalLength {
    // These 'sub'-tag values have been created for consistency -- they don't exist within the exif segment
    const val OFFSET = 0xC200
    /**
     * 0 = Auto
     * 1 = Sunny
     * 2 = Cloudy
     * 3 = Tungsten
     * 4 = Florescent
     * 5 = Flash
     * 6 = Custom
     */
    const val TAG_WHITE_BALANCE = OFFSET + 0x07
    const val TAG_SEQUENCE_NUMBER = OFFSET + 0x09
    const val TAG_AF_POINT_USED = OFFSET + 0x0E
    /**
     * The value of this tag may be translated into a flash bias value, in EV.
     *
     * 0xffc0 = -2 EV
     * 0xffcc = -1.67 EV
     * 0xffd0 = -1.5 EV
     * 0xffd4 = -1.33 EV
     * 0xffe0 = -1 EV
     * 0xffec = -0.67 EV
     * 0xfff0 = -0.5 EV
     * 0xfff4 = -0.33 EV
     * 0x0000 = 0 EV
     * 0x000c = 0.33 EV
     * 0x0010 = 0.5 EV
     * 0x0014 = 0.67 EV
     * 0x0020 = 1 EV
     * 0x002c = 1.33 EV
     * 0x0030 = 1.5 EV
     * 0x0034 = 1.67 EV
     * 0x0040 = 2 EV
     */
    const val TAG_FLASH_BIAS = OFFSET + 0x0F
    const val TAG_AUTO_EXPOSURE_BRACKETING = OFFSET + 0x10
    const val TAG_AEB_BRACKET_VALUE = OFFSET + 0x11
    const val TAG_SUBJECT_DISTANCE = OFFSET + 0x13
  }

  object ShotInfo {
    // These 'sub'-tag values have been created for consistency -- they don't exist within the exif segment
    const val OFFSET = 0xC400
    const val TAG_AUTO_ISO = OFFSET + 1
    const val TAG_BASE_ISO = OFFSET + 2
    const val TAG_MEASURED_EV = OFFSET + 3
    const val TAG_TARGET_APERTURE = OFFSET + 4
    const val TAG_TARGET_EXPOSURE_TIME = OFFSET + 5
    const val TAG_EXPOSURE_COMPENSATION = OFFSET + 6
    const val TAG_WHITE_BALANCE = OFFSET + 7
    const val TAG_SLOW_SHUTTER = OFFSET + 8
    const val TAG_SEQUENCE_NUMBER = OFFSET + 9
    const val TAG_OPTICAL_ZOOM_CODE = OFFSET + 10
    const val TAG_CAMERA_TEMPERATURE = OFFSET + 12
    const val TAG_FLASH_GUIDE_NUMBER = OFFSET + 13
    const val TAG_AF_POINTS_IN_FOCUS = OFFSET + 14
    const val TAG_FLASH_EXPOSURE_BRACKETING = OFFSET + 15
    const val TAG_AUTO_EXPOSURE_BRACKETING = OFFSET + 16
    const val TAG_AEB_BRACKET_VALUE = OFFSET + 17
    const val TAG_CONTROL_MODE = OFFSET + 18
    const val TAG_FOCUS_DISTANCE_UPPER = OFFSET + 19
    const val TAG_FOCUS_DISTANCE_LOWER = OFFSET + 20
    const val TAG_F_NUMBER = OFFSET + 21
    const val TAG_EXPOSURE_TIME = OFFSET + 22
    const val TAG_MEASURED_EV_2 = OFFSET + 23
    const val TAG_BULB_DURATION = OFFSET + 24
    const val TAG_CAMERA_TYPE = OFFSET + 26
    const val TAG_AUTO_ROTATE = OFFSET + 27
    const val TAG_ND_FILTER = OFFSET + 28
    const val TAG_SELF_TIMER_2 = OFFSET + 29
    const val TAG_FLASH_OUTPUT = OFFSET + 33
  }

  object Panorama {
    // These 'sub'-tag values have been created for consistency -- they don't exist within the exif segment
    const val OFFSET = 0xC500
    const val TAG_PANORAMA_FRAME_NUMBER = OFFSET + 2
    const val TAG_PANORAMA_DIRECTION = OFFSET + 5
  }

  object AFInfo {
    // These 'sub'-tag values have been created for consistency -- they don't exist within the exif segment
    const val OFFSET = 0xD200
    const val TAG_NUM_AF_POINTS = OFFSET
    const val TAG_VALID_AF_POINTS = OFFSET + 1
    const val TAG_IMAGE_WIDTH = OFFSET + 2
    const val TAG_IMAGE_HEIGHT = OFFSET + 3
    const val TAG_AF_IMAGE_WIDTH = OFFSET + 4
    const val TAG_AF_IMAGE_HEIGHT = OFFSET + 5
    const val TAG_AF_AREA_WIDTH = OFFSET + 6
    const val TAG_AF_AREA_HEIGHT = OFFSET + 7
    const val TAG_AF_AREA_X_POSITIONS = OFFSET + 8
    const val TAG_AF_AREA_Y_POSITIONS = OFFSET + 9
    const val TAG_AF_POINTS_IN_FOCUS = OFFSET + 10
    const val TAG_PRIMARY_AF_POINT_1 = OFFSET + 11
    const val TAG_PRIMARY_AF_POINT_2 = OFFSET + 12 // not sure why there are two of these
  }

  companion object {
    // These TAG_*_ARRAY Exif tags map to arrays of int16 values which are split out into separate 'fake' tags.
// When an attempt is made to set one of these on the directory, it is split and the corresponding offset added to the tagType.
// So the resulting tag is the offset + the index into the array.
    private const val TAG_CAMERA_SETTINGS_ARRAY = 0x0001
    private const val TAG_FOCAL_LENGTH_ARRAY = 0x0002
    //    private static final int TAG_FLASH_INFO                     = 0x0003;
    private const val TAG_SHOT_INFO_ARRAY = 0x0004
    private const val TAG_PANORAMA_ARRAY = 0x0005
    const val TAG_CANON_IMAGE_TYPE = 0x0006
    const val TAG_CANON_FIRMWARE_VERSION = 0x0007
    const val TAG_CANON_IMAGE_NUMBER = 0x0008
    const val TAG_CANON_OWNER_NAME = 0x0009
    const val TAG_CANON_SERIAL_NUMBER = 0x000C
    const val TAG_CAMERA_INFO_ARRAY = 0x000D // depends upon model, so leave for now
    const val TAG_CANON_FILE_LENGTH = 0x000E
    const val TAG_CANON_CUSTOM_FUNCTIONS_ARRAY = 0x000F // depends upon model, so leave for now
    const val TAG_MODEL_ID = 0x0010
    const val TAG_MOVIE_INFO_ARRAY = 0x0011 // not currently decoded as not sure we see it in still images
    private const val TAG_AF_INFO_ARRAY = 0x0012 // not currently decoded
    const val TAG_THUMBNAIL_IMAGE_VALID_AREA = 0x0013
    const val TAG_SERIAL_NUMBER_FORMAT = 0x0015
    const val TAG_SUPER_MACRO = 0x001A
    const val TAG_DATE_STAMP_MODE = 0x001C
    const val TAG_MY_COLORS = 0x001D
    const val TAG_FIRMWARE_REVISION = 0x001E
    const val TAG_CATEGORIES = 0x0023
    const val TAG_FACE_DETECT_ARRAY_1 = 0x0024
    const val TAG_FACE_DETECT_ARRAY_2 = 0x0025
    const val TAG_AF_INFO_ARRAY_2 = 0x0026
    const val TAG_IMAGE_UNIQUE_ID = 0x0028
    const val TAG_RAW_DATA_OFFSET = 0x0081
    const val TAG_ORIGINAL_DECISION_DATA_OFFSET = 0x0083
    const val TAG_CUSTOM_FUNCTIONS_1D_ARRAY = 0x0090 // not currently decoded
    const val TAG_PERSONAL_FUNCTIONS_ARRAY = 0x0091 // not currently decoded
    const val TAG_PERSONAL_FUNCTION_VALUES_ARRAY = 0x0092 // not currently decoded
    const val TAG_FILE_INFO_ARRAY = 0x0093 // not currently decoded
    const val TAG_AF_POINTS_IN_FOCUS_1D = 0x0094
    const val TAG_LENS_MODEL = 0x0095
    const val TAG_SERIAL_INFO_ARRAY = 0x0096 // not currently decoded
    const val TAG_DUST_REMOVAL_DATA = 0x0097
    const val TAG_CROP_INFO = 0x0098 // not currently decoded
    const val TAG_CUSTOM_FUNCTIONS_ARRAY_2 = 0x0099 // not currently decoded
    const val TAG_ASPECT_INFO_ARRAY = 0x009A // not currently decoded
    const val TAG_PROCESSING_INFO_ARRAY = 0x00A0 // not currently decoded
    const val TAG_TONE_CURVE_TABLE = 0x00A1
    const val TAG_SHARPNESS_TABLE = 0x00A2
    const val TAG_SHARPNESS_FREQ_TABLE = 0x00A3
    const val TAG_WHITE_BALANCE_TABLE = 0x00A4
    const val TAG_COLOR_BALANCE_ARRAY = 0x00A9 // not currently decoded
    const val TAG_MEASURED_COLOR_ARRAY = 0x00AA // not currently decoded
    const val TAG_COLOR_TEMPERATURE = 0x00AE
    const val TAG_CANON_FLAGS_ARRAY = 0x00B0 // not currently decoded
    const val TAG_MODIFIED_INFO_ARRAY = 0x00B1 // not currently decoded
    const val TAG_TONE_CURVE_MATCHING = 0x00B2
    const val TAG_WHITE_BALANCE_MATCHING = 0x00B3
    const val TAG_COLOR_SPACE = 0x00B4
    const val TAG_PREVIEW_IMAGE_INFO_ARRAY = 0x00B6 // not currently decoded
    const val TAG_VRD_OFFSET = 0x00D0
    const val TAG_SENSOR_INFO_ARRAY = 0x00E0 // not currently decoded
    const val TAG_COLOR_DATA_ARRAY_2 = 0x4001 // depends upon camera model, not currently decoded
    const val TAG_CRW_PARAM = 0x4002 // depends upon camera model, not currently decoded
    const val TAG_COLOR_INFO_ARRAY_2 = 0x4003 // not currently decoded
    const val TAG_BLACK_LEVEL = 0x4008 // not currently decoded
    const val TAG_CUSTOM_PICTURE_STYLE_FILE_NAME = 0x4010
    const val TAG_COLOR_INFO_ARRAY = 0x4013 // not currently decoded
    const val TAG_VIGNETTING_CORRECTION_ARRAY_1 = 0x4015 // not currently decoded
    const val TAG_VIGNETTING_CORRECTION_ARRAY_2 = 0x4016 // not currently decoded
    const val TAG_LIGHTING_OPTIMIZER_ARRAY = 0x4018 // not currently decoded
    const val TAG_LENS_INFO_ARRAY = 0x4019 // not currently decoded
    const val TAG_AMBIANCE_INFO_ARRAY = 0x4020 // not currently decoded
    const val TAG_FILTER_INFO_ARRAY = 0x4024 // not currently decoded
    //    /**
//     * Long Exposure Noise Reduction
//     * 0 = Off
//     * 1 = On
//     */
//    public static final int TAG_CANON_CUSTOM_FUNCTION_LONG_EXPOSURE_NOISE_REDUCTION = 0xC301;
//
//    /**
//     * Shutter/Auto Exposure-lock buttons
//     * 0 = AF/AE lock
//     * 1 = AE lock/AF
//     * 2 = AF/AF lock
//     * 3 = AE+release/AE+AF
//     */
//    public static final int TAG_CANON_CUSTOM_FUNCTION_SHUTTER_AUTO_EXPOSURE_LOCK_BUTTONS = 0xC302;
//
//    /**
//     * Mirror lockup
//     * 0 = Disable
//     * 1 = Enable
//     */
//    public static final int TAG_CANON_CUSTOM_FUNCTION_MIRROR_LOCKUP = 0xC303;
//
//    /**
//     * Tv/Av and exposure level
//     * 0 = 1/2 stop
//     * 1 = 1/3 stop
//     */
//    public static final int TAG_CANON_CUSTOM_FUNCTION_TV_AV_AND_EXPOSURE_LEVEL = 0xC304;
//
//    /**
//     * AF-assist light
//     * 0 = On (Auto)
//     * 1 = Off
//     */
//    public static final int TAG_CANON_CUSTOM_FUNCTION_AF_ASSIST_LIGHT = 0xC305;
//
//    /**
//     * Shutter speed in Av mode
//     * 0 = Automatic
//     * 1 = 1/200 (fixed)
//     */
//    public static final int TAG_CANON_CUSTOM_FUNCTION_SHUTTER_SPEED_IN_AV_MODE = 0xC306;
//
//    /**
//     * Auto-Exposure Bracketing sequence/auto cancellation
//     * 0 = 0,-,+ / Enabled
//     * 1 = 0,-,+ / Disabled
//     * 2 = -,0,+ / Enabled
//     * 3 = -,0,+ / Disabled
//     */
//    public static final int TAG_CANON_CUSTOM_FUNCTION_BRACKETING = 0xC307;
//
//    /**
//     * Shutter Curtain Sync
//     * 0 = 1st Curtain Sync
//     * 1 = 2nd Curtain Sync
//     */
//    public static final int TAG_CANON_CUSTOM_FUNCTION_SHUTTER_CURTAIN_SYNC = 0xC308;
//
//    /**
//     * Lens Auto-Focus stop button Function Switch
//     * 0 = AF stop
//     * 1 = Operate AF
//     * 2 = Lock AE and start timer
//     */
//    public static final int TAG_CANON_CUSTOM_FUNCTION_AF_STOP = 0xC309;
//
//    /**
//     * Auto reduction of fill flash
//     * 0 = Enable
//     * 1 = Disable
//     */
//    public static final int TAG_CANON_CUSTOM_FUNCTION_FILL_FLASH_REDUCTION = 0xC30A;
//
//    /**
//     * Menu button return position
//     * 0 = Top
//     * 1 = Previous (volatile)
//     * 2 = Previous
//     */
//    public static final int TAG_CANON_CUSTOM_FUNCTION_MENU_BUTTON_RETURN = 0xC30B;
//
//    /**
//     * SET button function when shooting
//     * 0 = Not Assigned
//     * 1 = Change Quality
//     * 2 = Change ISO Speed
//     * 3 = Select Parameters
//     */
//    public static final int TAG_CANON_CUSTOM_FUNCTION_SET_BUTTON_FUNCTION = 0xC30C;
//
//    /**
//     * Sensor cleaning
//     * 0 = Disable
//     * 1 = Enable
//     */
//    public static final int TAG_CANON_CUSTOM_FUNCTION_SENSOR_CLEANING = 0xC30D;
    protected val tagNameMap = HashMap<Int, String>()

    init {
      tagNameMap[TAG_CANON_FIRMWARE_VERSION] = "Firmware Version"
      tagNameMap[TAG_CANON_IMAGE_NUMBER] = "Image Number"
      tagNameMap[TAG_CANON_IMAGE_TYPE] = "Image Type"
      tagNameMap[TAG_CANON_OWNER_NAME] = "Owner Name"
      tagNameMap[TAG_CANON_SERIAL_NUMBER] = "Camera Serial Number"
      tagNameMap[TAG_CAMERA_INFO_ARRAY] = "Camera Info Array"
      tagNameMap[TAG_CANON_FILE_LENGTH] = "File Length"
      tagNameMap[TAG_CANON_CUSTOM_FUNCTIONS_ARRAY] = "Custom Functions"
      tagNameMap[TAG_MODEL_ID] = "Canon Model ID"
      tagNameMap[TAG_MOVIE_INFO_ARRAY] = "Movie Info Array"
      tagNameMap[CameraSettings.TAG_AF_POINT_SELECTED] = "AF Point Selected"
      tagNameMap[CameraSettings.TAG_CONTINUOUS_DRIVE_MODE] = "Continuous Drive Mode"
      tagNameMap[CameraSettings.TAG_CONTRAST] = "Contrast"
      tagNameMap[CameraSettings.TAG_EASY_SHOOTING_MODE] = "Easy Shooting Mode"
      tagNameMap[CameraSettings.TAG_EXPOSURE_MODE] = "Exposure Mode"
      tagNameMap[CameraSettings.TAG_FLASH_DETAILS] = "Flash Details"
      tagNameMap[CameraSettings.TAG_FLASH_MODE] = "Flash Mode"
      tagNameMap[CameraSettings.TAG_FOCAL_UNITS_PER_MM] = "Focal Units per mm"
      tagNameMap[CameraSettings.TAG_FOCUS_MODE_1] = "Focus Mode"
      tagNameMap[CameraSettings.TAG_FOCUS_MODE_2] = "Focus Mode"
      tagNameMap[CameraSettings.TAG_IMAGE_SIZE] = "Image Size"
      tagNameMap[CameraSettings.TAG_ISO] = "Iso"
      tagNameMap[CameraSettings.TAG_LONG_FOCAL_LENGTH] = "Long Focal Length"
      tagNameMap[CameraSettings.TAG_MACRO_MODE] = "Macro Mode"
      tagNameMap[CameraSettings.TAG_METERING_MODE] = "Metering Mode"
      tagNameMap[CameraSettings.TAG_SATURATION] = "Saturation"
      tagNameMap[CameraSettings.TAG_SELF_TIMER_DELAY] = "Self Timer Delay"
      tagNameMap[CameraSettings.TAG_SHARPNESS] = "Sharpness"
      tagNameMap[CameraSettings.TAG_SHORT_FOCAL_LENGTH] = "Short Focal Length"
      tagNameMap[CameraSettings.TAG_QUALITY] = "Quality"
      tagNameMap[CameraSettings.TAG_UNKNOWN_2] = "Unknown Camera Setting 2"
      tagNameMap[CameraSettings.TAG_UNKNOWN_3] = "Unknown Camera Setting 3"
      tagNameMap[CameraSettings.TAG_RECORD_MODE] = "Record Mode"
      tagNameMap[CameraSettings.TAG_DIGITAL_ZOOM] = "Digital Zoom"
      tagNameMap[CameraSettings.TAG_FOCUS_TYPE] = "Focus Type"
      tagNameMap[CameraSettings.TAG_UNKNOWN_7] = "Unknown Camera Setting 7"
      tagNameMap[CameraSettings.TAG_LENS_TYPE] = "Lens Type"
      tagNameMap[CameraSettings.TAG_MAX_APERTURE] = "Max Aperture"
      tagNameMap[CameraSettings.TAG_MIN_APERTURE] = "Min Aperture"
      tagNameMap[CameraSettings.TAG_FLASH_ACTIVITY] = "Flash Activity"
      tagNameMap[CameraSettings.TAG_FOCUS_CONTINUOUS] = "Focus Continuous"
      tagNameMap[CameraSettings.TAG_AE_SETTING] = "AE Setting"
      tagNameMap[CameraSettings.TAG_DISPLAY_APERTURE] = "Display Aperture"
      tagNameMap[CameraSettings.TAG_ZOOM_SOURCE_WIDTH] = "Zoom Source Width"
      tagNameMap[CameraSettings.TAG_ZOOM_TARGET_WIDTH] = "Zoom Target Width"
      tagNameMap[CameraSettings.TAG_SPOT_METERING_MODE] = "Spot Metering Mode"
      tagNameMap[CameraSettings.TAG_PHOTO_EFFECT] = "Photo Effect"
      tagNameMap[CameraSettings.TAG_MANUAL_FLASH_OUTPUT] = "Manual Flash Output"
      tagNameMap[CameraSettings.TAG_COLOR_TONE] = "Color Tone"
      tagNameMap[CameraSettings.TAG_SRAW_QUALITY] = "SRAW Quality"
      tagNameMap[FocalLength.TAG_WHITE_BALANCE] = "White Balance"
      tagNameMap[FocalLength.TAG_SEQUENCE_NUMBER] = "Sequence Number"
      tagNameMap[FocalLength.TAG_AF_POINT_USED] = "AF Point Used"
      tagNameMap[FocalLength.TAG_FLASH_BIAS] = "Flash Bias"
      tagNameMap[FocalLength.TAG_AUTO_EXPOSURE_BRACKETING] = "Auto Exposure Bracketing"
      tagNameMap[FocalLength.TAG_AEB_BRACKET_VALUE] = "AEB Bracket Value"
      tagNameMap[FocalLength.TAG_SUBJECT_DISTANCE] = "Subject Distance"
      tagNameMap[ShotInfo.TAG_AUTO_ISO] = "Auto ISO"
      tagNameMap[ShotInfo.TAG_BASE_ISO] = "Base ISO"
      tagNameMap[ShotInfo.TAG_MEASURED_EV] = "Measured EV"
      tagNameMap[ShotInfo.TAG_TARGET_APERTURE] = "Target Aperture"
      tagNameMap[ShotInfo.TAG_TARGET_EXPOSURE_TIME] = "Target Exposure Time"
      tagNameMap[ShotInfo.TAG_EXPOSURE_COMPENSATION] = "Exposure Compensation"
      tagNameMap[ShotInfo.TAG_WHITE_BALANCE] = "White Balance"
      tagNameMap[ShotInfo.TAG_SLOW_SHUTTER] = "Slow Shutter"
      tagNameMap[ShotInfo.TAG_SEQUENCE_NUMBER] = "Sequence Number"
      tagNameMap[ShotInfo.TAG_OPTICAL_ZOOM_CODE] = "Optical Zoom Code"
      tagNameMap[ShotInfo.TAG_CAMERA_TEMPERATURE] = "Camera Temperature"
      tagNameMap[ShotInfo.TAG_FLASH_GUIDE_NUMBER] = "Flash Guide Number"
      tagNameMap[ShotInfo.TAG_AF_POINTS_IN_FOCUS] = "AF Points in Focus"
      tagNameMap[ShotInfo.TAG_FLASH_EXPOSURE_BRACKETING] = "Flash Exposure Compensation"
      tagNameMap[ShotInfo.TAG_AUTO_EXPOSURE_BRACKETING] = "Auto Exposure Bracketing"
      tagNameMap[ShotInfo.TAG_AEB_BRACKET_VALUE] = "AEB Bracket Value"
      tagNameMap[ShotInfo.TAG_CONTROL_MODE] = "Control Mode"
      tagNameMap[ShotInfo.TAG_FOCUS_DISTANCE_UPPER] = "Focus Distance Upper"
      tagNameMap[ShotInfo.TAG_FOCUS_DISTANCE_LOWER] = "Focus Distance Lower"
      tagNameMap[ShotInfo.TAG_F_NUMBER] = "F Number"
      tagNameMap[ShotInfo.TAG_EXPOSURE_TIME] = "Exposure Time"
      tagNameMap[ShotInfo.TAG_MEASURED_EV_2] = "Measured EV 2"
      tagNameMap[ShotInfo.TAG_BULB_DURATION] = "Bulb Duration"
      tagNameMap[ShotInfo.TAG_CAMERA_TYPE] = "Camera Type"
      tagNameMap[ShotInfo.TAG_AUTO_ROTATE] = "Auto Rotate"
      tagNameMap[ShotInfo.TAG_ND_FILTER] = "ND Filter"
      tagNameMap[ShotInfo.TAG_SELF_TIMER_2] = "Self Timer 2"
      tagNameMap[ShotInfo.TAG_FLASH_OUTPUT] = "Flash Output"
      tagNameMap[Panorama.TAG_PANORAMA_FRAME_NUMBER] = "Panorama Frame Number"
      tagNameMap[Panorama.TAG_PANORAMA_DIRECTION] = "Panorama Direction"
      tagNameMap[AFInfo.TAG_NUM_AF_POINTS] = "AF Point Count"
      tagNameMap[AFInfo.TAG_VALID_AF_POINTS] = "Valid AF Point Count"
      tagNameMap[AFInfo.TAG_IMAGE_WIDTH] = "Image Width"
      tagNameMap[AFInfo.TAG_IMAGE_HEIGHT] = "Image Height"
      tagNameMap[AFInfo.TAG_AF_IMAGE_WIDTH] = "AF Image Width"
      tagNameMap[AFInfo.TAG_AF_IMAGE_HEIGHT] = "AF Image Height"
      tagNameMap[AFInfo.TAG_AF_AREA_WIDTH] = "AF Area Width"
      tagNameMap[AFInfo.TAG_AF_AREA_HEIGHT] = "AF Area Height"
      tagNameMap[AFInfo.TAG_AF_AREA_X_POSITIONS] = "AF Area X Positions"
      tagNameMap[AFInfo.TAG_AF_AREA_Y_POSITIONS] = "AF Area Y Positions"
      tagNameMap[AFInfo.TAG_AF_POINTS_IN_FOCUS] = "AF Points in Focus"
      tagNameMap[AFInfo.TAG_PRIMARY_AF_POINT_1] = "Primary AF Point 1"
      tagNameMap[AFInfo.TAG_PRIMARY_AF_POINT_2] = "Primary AF Point 2"
      //        _tagNameMap.put(TAG_CANON_CUSTOM_FUNCTION_LONG_EXPOSURE_NOISE_REDUCTION, "Long Exposure Noise Reduction");
//        _tagNameMap.put(TAG_CANON_CUSTOM_FUNCTION_SHUTTER_AUTO_EXPOSURE_LOCK_BUTTONS, "Shutter/Auto Exposure-lock Buttons");
//        _tagNameMap.put(TAG_CANON_CUSTOM_FUNCTION_MIRROR_LOCKUP, "Mirror Lockup");
//        _tagNameMap.put(TAG_CANON_CUSTOM_FUNCTION_TV_AV_AND_EXPOSURE_LEVEL, "Tv/Av And Exposure Level");
//        _tagNameMap.put(TAG_CANON_CUSTOM_FUNCTION_AF_ASSIST_LIGHT, "AF-Assist Light");
//        _tagNameMap.put(TAG_CANON_CUSTOM_FUNCTION_SHUTTER_SPEED_IN_AV_MODE, "Shutter Speed in Av Mode");
//        _tagNameMap.put(TAG_CANON_CUSTOM_FUNCTION_BRACKETING, "Auto-Exposure Bracketing Sequence/Auto Cancellation");
//        _tagNameMap.put(TAG_CANON_CUSTOM_FUNCTION_SHUTTER_CURTAIN_SYNC, "Shutter Curtain Sync");
//        _tagNameMap.put(TAG_CANON_CUSTOM_FUNCTION_AF_STOP, "Lens Auto-Focus Stop Button Function Switch");
//        _tagNameMap.put(TAG_CANON_CUSTOM_FUNCTION_FILL_FLASH_REDUCTION, "Auto Reduction of Fill Flash");
//        _tagNameMap.put(TAG_CANON_CUSTOM_FUNCTION_MENU_BUTTON_RETURN, "Menu Button Return Position");
//        _tagNameMap.put(TAG_CANON_CUSTOM_FUNCTION_SET_BUTTON_FUNCTION, "SET Button Function When Shooting");
//        _tagNameMap.put(TAG_CANON_CUSTOM_FUNCTION_SENSOR_CLEANING, "Sensor Cleaning");
      tagNameMap[TAG_THUMBNAIL_IMAGE_VALID_AREA] = "Thumbnail Image Valid Area"
      tagNameMap[TAG_SERIAL_NUMBER_FORMAT] = "Serial Number Format"
      tagNameMap[TAG_SUPER_MACRO] = "Super Macro"
      tagNameMap[TAG_DATE_STAMP_MODE] = "Date Stamp Mode"
      tagNameMap[TAG_MY_COLORS] = "My Colors"
      tagNameMap[TAG_FIRMWARE_REVISION] = "Firmware Revision"
      tagNameMap[TAG_CATEGORIES] = "Categories"
      tagNameMap[TAG_FACE_DETECT_ARRAY_1] = "Face Detect Array 1"
      tagNameMap[TAG_FACE_DETECT_ARRAY_2] = "Face Detect Array 2"
      tagNameMap[TAG_AF_INFO_ARRAY_2] = "AF Info Array 2"
      tagNameMap[TAG_IMAGE_UNIQUE_ID] = "Image Unique ID"
      tagNameMap[TAG_RAW_DATA_OFFSET] = "Raw Data Offset"
      tagNameMap[TAG_ORIGINAL_DECISION_DATA_OFFSET] = "Original Decision Data Offset"
      tagNameMap[TAG_CUSTOM_FUNCTIONS_1D_ARRAY] = "Custom Functions (1D) Array"
      tagNameMap[TAG_PERSONAL_FUNCTIONS_ARRAY] = "Personal Functions Array"
      tagNameMap[TAG_PERSONAL_FUNCTION_VALUES_ARRAY] = "Personal Function Values Array"
      tagNameMap[TAG_FILE_INFO_ARRAY] = "File Info Array"
      tagNameMap[TAG_AF_POINTS_IN_FOCUS_1D] = "AF Points in Focus (1D)"
      tagNameMap[TAG_LENS_MODEL] = "Lens Model"
      tagNameMap[TAG_SERIAL_INFO_ARRAY] = "Serial Info Array"
      tagNameMap[TAG_DUST_REMOVAL_DATA] = "Dust Removal Data"
      tagNameMap[TAG_CROP_INFO] = "Crop Info"
      tagNameMap[TAG_CUSTOM_FUNCTIONS_ARRAY_2] = "Custom Functions Array 2"
      tagNameMap[TAG_ASPECT_INFO_ARRAY] = "Aspect Information Array"
      tagNameMap[TAG_PROCESSING_INFO_ARRAY] = "Processing Information Array"
      tagNameMap[TAG_TONE_CURVE_TABLE] = "Tone Curve Table"
      tagNameMap[TAG_SHARPNESS_TABLE] = "Sharpness Table"
      tagNameMap[TAG_SHARPNESS_FREQ_TABLE] = "Sharpness Frequency Table"
      tagNameMap[TAG_WHITE_BALANCE_TABLE] = "White Balance Table"
      tagNameMap[TAG_COLOR_BALANCE_ARRAY] = "Color Balance Array"
      tagNameMap[TAG_MEASURED_COLOR_ARRAY] = "Measured Color Array"
      tagNameMap[TAG_COLOR_TEMPERATURE] = "Color Temperature"
      tagNameMap[TAG_CANON_FLAGS_ARRAY] = "Canon Flags Array"
      tagNameMap[TAG_MODIFIED_INFO_ARRAY] = "Modified Information Array"
      tagNameMap[TAG_TONE_CURVE_MATCHING] = "Tone Curve Matching"
      tagNameMap[TAG_WHITE_BALANCE_MATCHING] = "White Balance Matching"
      tagNameMap[TAG_COLOR_SPACE] = "Color Space"
      tagNameMap[TAG_PREVIEW_IMAGE_INFO_ARRAY] = "Preview Image Info Array"
      tagNameMap[TAG_VRD_OFFSET] = "VRD Offset"
      tagNameMap[TAG_SENSOR_INFO_ARRAY] = "Sensor Information Array"
      tagNameMap[TAG_COLOR_DATA_ARRAY_2] = "Color Data Array 1"
      tagNameMap[TAG_CRW_PARAM] = "CRW Parameters"
      tagNameMap[TAG_COLOR_INFO_ARRAY_2] = "Color Data Array 2"
      tagNameMap[TAG_BLACK_LEVEL] = "Black Level"
      tagNameMap[TAG_CUSTOM_PICTURE_STYLE_FILE_NAME] = "Custom Picture Style File Name"
      tagNameMap[TAG_COLOR_INFO_ARRAY] = "Color Info Array"
      tagNameMap[TAG_VIGNETTING_CORRECTION_ARRAY_1] = "Vignetting Correction Array 1"
      tagNameMap[TAG_VIGNETTING_CORRECTION_ARRAY_2] = "Vignetting Correction Array 2"
      tagNameMap[TAG_LIGHTING_OPTIMIZER_ARRAY] = "Lighting Optimizer Array"
      tagNameMap[TAG_LENS_INFO_ARRAY] = "Lens Info Array"
      tagNameMap[TAG_AMBIANCE_INFO_ARRAY] = "Ambiance Info Array"
      tagNameMap[TAG_FILTER_INFO_ARRAY] = "Filter Info Array"
    }
  }

  override val name: String
    get() = "Canon Makernote"

  override fun setObjectArray(tagType: Int, array: Any) { // TODO is there some way to drop out 'null' or 'zero' values that are present in the array to reduce the noise?
    if (array !is IntArray) { // no special handling...
      super.setObjectArray(tagType, array)
      return
    }
    when (tagType) {
      TAG_CAMERA_SETTINGS_ARRAY -> {
        val ints = array
        var i = 0
        while (i < ints.size) {
          setInt(CameraSettings.OFFSET + i, ints[i])
          i++
        }
      }
      TAG_FOCAL_LENGTH_ARRAY -> {
        val ints = array
        var i = 0
        while (i < ints.size) {
          setInt(FocalLength.OFFSET + i, ints[i])
          i++
        }
      }
      TAG_SHOT_INFO_ARRAY -> {
        val ints = array
        var i = 0
        while (i < ints.size) {
          setInt(ShotInfo.OFFSET + i, ints[i])
          i++
        }
      }
      TAG_PANORAMA_ARRAY -> {
        val ints = array
        var i = 0
        while (i < ints.size) {
          setInt(Panorama.OFFSET + i, ints[i])
          i++
        }
      }
      TAG_AF_INFO_ARRAY -> {
        // Notes from Exiftool 10.10 by Phil Harvey, lib\Image\Exiftool\Canon.pm:
// Auto-focus information used by many older Canon models. The values in this
// record are sequential, and some have variable sizes based on the value of
// numafpoints (which may be 1,5,7,9,15,45, or 53). The AFArea coordinates are
// given in a system where the image has dimensions given by AFImageWidth and
// AFImageHeight, and 0,0 is the image center. The direction of the Y axis
// depends on the camera model, with positive Y upwards for EOS models, but
// apparently downwards for PowerShot models.
// AFInfo is another array with 'fake' tags. The first int of the array contains
// the number of AF points. Iterate through the array one byte at a time, generally
// assuming one byte corresponds to one tag UNLESS certain tag numbers are encountered.
// For these, read specific subsequent bytes from the array based on the tag type. The
// number of bytes read can vary.
        val values = array
        val numafpoints = values[0]
        var tagnumber = 0
        var i = 0
        while (i < values.size) {
          // These two tags store 'numafpoints' bytes of data in the array
          if (AFInfo.OFFSET + tagnumber == AFInfo.TAG_AF_AREA_X_POSITIONS ||
            AFInfo.OFFSET + tagnumber == AFInfo.TAG_AF_AREA_Y_POSITIONS) { // There could be incorrect data in the array, so boundary check
            if (values.size - 1 >= i + numafpoints) {
              val areaPositions = ShortArray(numafpoints)
              var j = 0
              while (j < areaPositions.size) {
                areaPositions[j] = values[i + j].toShort()
                j++
              }
              super.setObjectArray(AFInfo.OFFSET + tagnumber, areaPositions)
            }
            i += numafpoints - 1 // assume these bytes are processed and skip
          } else if (AFInfo.OFFSET + tagnumber == AFInfo.TAG_AF_POINTS_IN_FOCUS) {
            val pointsInFocus = ShortArray((numafpoints + 15) / 16)
            // There could be incorrect data in the array, so boundary check
            if (values.size - 1 >= i + pointsInFocus.size) {
              var j = 0
              while (j < pointsInFocus.size) {
                pointsInFocus[j] = values[i + j].toShort()
                j++
              }
              super.setObjectArray(AFInfo.OFFSET + tagnumber, pointsInFocus)
            }
            i += pointsInFocus.size - 1 // assume these bytes are processed and skip
          } else super.setObjectArray(AFInfo.OFFSET + tagnumber, values[i])
          tagnumber++
          i++
        }
      }
      else -> {
        // no special handling...
        super.setObjectArray(tagType, array)
      }
    }
  }

  init {
    setDescriptor(CanonMakernoteDescriptor(this))
  }
}
