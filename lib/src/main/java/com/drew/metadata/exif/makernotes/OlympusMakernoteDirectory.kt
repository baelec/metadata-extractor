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

import com.drew.lang.SequentialByteArrayReader
import com.drew.metadata.Directory
import java.io.IOException
import java.util.*

/**
 * The Olympus makernote is used by many manufacturers (Epson, Konica, Minolta and Agfa...), and as such contains some tags
 * that appear specific to those manufacturers.
 *
 * @author Drew Noakes https://drewnoakes.com
 */
class OlympusMakernoteDirectory : Directory() {
  override val tagNameMap = Companion.tagNameMap

  object CameraSettings {
    // These 'sub'-tag values have been created for consistency -- they don't exist within the Makernote IFD
    const val OFFSET = 0xF000
    const val TAG_EXPOSURE_MODE = OFFSET + 2
    const val TAG_FLASH_MODE = OFFSET + 3
    const val TAG_WHITE_BALANCE = OFFSET + 4
    const val TAG_IMAGE_SIZE = OFFSET + 5
    const val TAG_IMAGE_QUALITY = OFFSET + 6
    const val TAG_SHOOTING_MODE = OFFSET + 7
    const val TAG_METERING_MODE = OFFSET + 8
    const val TAG_APEX_FILM_SPEED_VALUE = OFFSET + 9
    const val TAG_APEX_SHUTTER_SPEED_TIME_VALUE = OFFSET + 10
    const val TAG_APEX_APERTURE_VALUE = OFFSET + 11
    const val TAG_MACRO_MODE = OFFSET + 12
    const val TAG_DIGITAL_ZOOM = OFFSET + 13
    const val TAG_EXPOSURE_COMPENSATION = OFFSET + 14
    const val TAG_BRACKET_STEP = OFFSET + 15
    // 16 missing
    const val TAG_INTERVAL_LENGTH = OFFSET + 17
    const val TAG_INTERVAL_NUMBER = OFFSET + 18
    const val TAG_FOCAL_LENGTH = OFFSET + 19
    const val TAG_FOCUS_DISTANCE = OFFSET + 20
    const val TAG_FLASH_FIRED = OFFSET + 21
    const val TAG_DATE = OFFSET + 22
    const val TAG_TIME = OFFSET + 23
    const val TAG_MAX_APERTURE_AT_FOCAL_LENGTH = OFFSET + 24
    // 25, 26 missing
    const val TAG_FILE_NUMBER_MEMORY = OFFSET + 27
    const val TAG_LAST_FILE_NUMBER = OFFSET + 28
    const val TAG_WHITE_BALANCE_RED = OFFSET + 29
    const val TAG_WHITE_BALANCE_GREEN = OFFSET + 30
    const val TAG_WHITE_BALANCE_BLUE = OFFSET + 31
    const val TAG_SATURATION = OFFSET + 32
    const val TAG_CONTRAST = OFFSET + 33
    const val TAG_SHARPNESS = OFFSET + 34
    const val TAG_SUBJECT_PROGRAM = OFFSET + 35
    const val TAG_FLASH_COMPENSATION = OFFSET + 36
    const val TAG_ISO_SETTING = OFFSET + 37
    const val TAG_CAMERA_MODEL = OFFSET + 38
    const val TAG_INTERVAL_MODE = OFFSET + 39
    const val TAG_FOLDER_NAME = OFFSET + 40
    const val TAG_COLOR_MODE = OFFSET + 41
    const val TAG_COLOR_FILTER = OFFSET + 42
    const val TAG_BLACK_AND_WHITE_FILTER = OFFSET + 43
    const val TAG_INTERNAL_FLASH = OFFSET + 44
    const val TAG_APEX_BRIGHTNESS_VALUE = OFFSET + 45
    const val TAG_SPOT_FOCUS_POINT_X_COORDINATE = OFFSET + 46
    const val TAG_SPOT_FOCUS_POINT_Y_COORDINATE = OFFSET + 47
    const val TAG_WIDE_FOCUS_ZONE = OFFSET + 48
    const val TAG_FOCUS_MODE = OFFSET + 49
    const val TAG_FOCUS_AREA = OFFSET + 50
    const val TAG_DEC_SWITCH_POSITION = OFFSET + 51
  }

  companion object {
    /** Used by Konica / Minolta cameras.  */
    const val TAG_MAKERNOTE_VERSION = 0x0000
    /** Used by Konica / Minolta cameras.  */
    const val TAG_CAMERA_SETTINGS_1 = 0x0001
    /** Alternate Camera Settings Tag. Used by Konica / Minolta cameras.  */
    const val TAG_CAMERA_SETTINGS_2 = 0x0003
    /** Used by Konica / Minolta cameras.  */
    const val TAG_COMPRESSED_IMAGE_SIZE = 0x0040
    /** Used by Konica / Minolta cameras.  */
    const val TAG_MINOLTA_THUMBNAIL_OFFSET_1 = 0x0081
    /** Alternate Thumbnail Offset. Used by Konica / Minolta cameras.  */
    const val TAG_MINOLTA_THUMBNAIL_OFFSET_2 = 0x0088
    /** Length of thumbnail in bytes. Used by Konica / Minolta cameras.  */
    const val TAG_MINOLTA_THUMBNAIL_LENGTH = 0x0089
    const val TAG_THUMBNAIL_IMAGE = 0x0100
    /**
     * Used by Konica / Minolta cameras
     * 0 = Natural Colour
     * 1 = Black &amp; White
     * 2 = Vivid colour
     * 3 = Solarization
     * 4 = AdobeRGB
     */
    const val TAG_COLOUR_MODE = 0x0101
    /**
     * Used by Konica / Minolta cameras.
     * 0 = Raw
     * 1 = Super Fine
     * 2 = Fine
     * 3 = Standard
     * 4 = Extra Fine
     */
    const val TAG_IMAGE_QUALITY_1 = 0x0102
    /**
     * Not 100% sure about this tag.
     *
     *
     * Used by Konica / Minolta cameras.
     * 0 = Raw
     * 1 = Super Fine
     * 2 = Fine
     * 3 = Standard
     * 4 = Extra Fine
     */
    const val TAG_IMAGE_QUALITY_2 = 0x0103
    const val TAG_BODY_FIRMWARE_VERSION = 0x0104
    /**
     * Three values:
     * Value 1: 0=Normal, 2=Fast, 3=Panorama
     * Value 2: Sequence Number Value 3:
     * 1 = Panorama Direction: Left to Right
     * 2 = Panorama Direction: Right to Left
     * 3 = Panorama Direction: Bottom to Top
     * 4 = Panorama Direction: Top to Bottom
     */
    const val TAG_SPECIAL_MODE = 0x0200
    /**
     * 1 = Standard Quality
     * 2 = High Quality
     * 3 = Super High Quality
     */
    const val TAG_JPEG_QUALITY = 0x0201
    /**
     * 0 = Normal (Not Macro)
     * 1 = Macro
     */
    const val TAG_MACRO_MODE = 0x0202
    /**
     * 0 = Off, 1 = On
     */
    const val TAG_BW_MODE = 0x0203
    /** Zoom Factor (0 or 1 = normal)  */
    const val TAG_DIGITAL_ZOOM = 0x0204
    const val TAG_FOCAL_PLANE_DIAGONAL = 0x0205
    const val TAG_LENS_DISTORTION_PARAMETERS = 0x0206
    const val TAG_CAMERA_TYPE = 0x0207
    const val TAG_PICT_INFO = 0x0208
    const val TAG_CAMERA_ID = 0x0209
    /**
     * Used by Epson cameras
     * Units = pixels
     */
    const val TAG_IMAGE_WIDTH = 0x020B
    /**
     * Used by Epson cameras
     * Units = pixels
     */
    const val TAG_IMAGE_HEIGHT = 0x020C
    /** A string. Used by Epson cameras.  */
    const val TAG_ORIGINAL_MANUFACTURER_MODEL = 0x020D
    const val TAG_PREVIEW_IMAGE = 0x0280
    const val TAG_PRE_CAPTURE_FRAMES = 0x0300
    const val TAG_WHITE_BOARD = 0x0301
    const val TAG_ONE_TOUCH_WB = 0x0302
    const val TAG_WHITE_BALANCE_BRACKET = 0x0303
    const val TAG_WHITE_BALANCE_BIAS = 0x0304
    const val TAG_SCENE_MODE = 0x0403
    const val TAG_SERIAL_NUMBER_1 = 0x0404
    const val TAG_FIRMWARE = 0x0405
    /**
     * See the PIM specification here:
     * http://www.ozhiker.com/electronics/pjmt/jpeg_info/pim.html
     */
    const val TAG_PRINT_IMAGE_MATCHING_INFO = 0x0E00
    const val TAG_DATA_DUMP_1 = 0x0F00
    const val TAG_DATA_DUMP_2 = 0x0F01
    const val TAG_SHUTTER_SPEED_VALUE = 0x1000
    const val TAG_ISO_VALUE = 0x1001
    const val TAG_APERTURE_VALUE = 0x1002
    const val TAG_BRIGHTNESS_VALUE = 0x1003
    const val TAG_FLASH_MODE = 0x1004
    const val TAG_FLASH_DEVICE = 0x1005
    const val TAG_BRACKET = 0x1006
    const val TAG_SENSOR_TEMPERATURE = 0x1007
    const val TAG_LENS_TEMPERATURE = 0x1008
    const val TAG_LIGHT_CONDITION = 0x1009
    const val TAG_FOCUS_RANGE = 0x100A
    const val TAG_FOCUS_MODE = 0x100B
    const val TAG_FOCUS_DISTANCE = 0x100C
    const val TAG_ZOOM = 0x100D
    const val TAG_MACRO_FOCUS = 0x100E
    const val TAG_SHARPNESS = 0x100F
    const val TAG_FLASH_CHARGE_LEVEL = 0x1010
    const val TAG_COLOUR_MATRIX = 0x1011
    const val TAG_BLACK_LEVEL = 0x1012
    const val TAG_COLOR_TEMPERATURE_BG = 0x1013
    const val TAG_COLOR_TEMPERATURE_RG = 0x1014
    const val TAG_WB_MODE = 0x1015
    //    public static final int TAG_ = 0x1016;
    const val TAG_RED_BALANCE = 0x1017
    const val TAG_BLUE_BALANCE = 0x1018
    const val TAG_COLOR_MATRIX_NUMBER = 0x1019
    const val TAG_SERIAL_NUMBER_2 = 0x101A
    const val TAG_EXTERNAL_FLASH_AE1_0 = 0x101B
    const val TAG_EXTERNAL_FLASH_AE2_0 = 0x101C
    const val TAG_INTERNAL_FLASH_AE1_0 = 0x101D
    const val TAG_INTERNAL_FLASH_AE2_0 = 0x101E
    const val TAG_EXTERNAL_FLASH_AE1 = 0x101F
    const val TAG_EXTERNAL_FLASH_AE2 = 0x1020
    const val TAG_INTERNAL_FLASH_AE1 = 0x1021
    const val TAG_INTERNAL_FLASH_AE2 = 0x1022
    const val TAG_FLASH_BIAS = 0x1023
    const val TAG_INTERNAL_FLASH_TABLE = 0x1024
    const val TAG_EXTERNAL_FLASH_G_VALUE = 0x1025
    const val TAG_EXTERNAL_FLASH_BOUNCE = 0x1026
    const val TAG_EXTERNAL_FLASH_ZOOM = 0x1027
    const val TAG_EXTERNAL_FLASH_MODE = 0x1028
    const val TAG_CONTRAST = 0x1029
    const val TAG_SHARPNESS_FACTOR = 0x102A
    const val TAG_COLOUR_CONTROL = 0x102B
    const val TAG_VALID_BITS = 0x102C
    const val TAG_CORING_FILTER = 0x102D
    const val TAG_OLYMPUS_IMAGE_WIDTH = 0x102E
    const val TAG_OLYMPUS_IMAGE_HEIGHT = 0x102F
    const val TAG_SCENE_DETECT = 0x1030
    const val TAG_SCENE_AREA = 0x1031
    //    public static final int TAG_ = 0x1032;
    const val TAG_SCENE_DETECT_DATA = 0x1033
    const val TAG_COMPRESSION_RATIO = 0x1034
    const val TAG_PREVIEW_IMAGE_VALID = 0x1035
    const val TAG_PREVIEW_IMAGE_START = 0x1036
    const val TAG_PREVIEW_IMAGE_LENGTH = 0x1037
    const val TAG_AF_RESULT = 0x1038
    const val TAG_CCD_SCAN_MODE = 0x1039
    const val TAG_NOISE_REDUCTION = 0x103A
    const val TAG_INFINITY_LENS_STEP = 0x103B
    const val TAG_NEAR_LENS_STEP = 0x103C
    const val TAG_LIGHT_VALUE_CENTER = 0x103D
    const val TAG_LIGHT_VALUE_PERIPHERY = 0x103E
    const val TAG_FIELD_COUNT = 0x103F
    const val TAG_EQUIPMENT = 0x2010
    const val TAG_CAMERA_SETTINGS = 0x2020
    const val TAG_RAW_DEVELOPMENT = 0x2030
    const val TAG_RAW_DEVELOPMENT_2 = 0x2031
    const val TAG_IMAGE_PROCESSING = 0x2040
    const val TAG_FOCUS_INFO = 0x2050
    const val TAG_RAW_INFO = 0x3000
    const val TAG_MAIN_INFO = 0x4000
    protected val tagNameMap = HashMap<Int, String>()
    // <summary>
// These values are currently decoded only for Olympus models.  Models with
// Olympus-style maker notes from other brands such as Acer, BenQ, Hitachi, HP,
// Premier, Konica-Minolta, Maginon, Ricoh, Rollei, SeaLife, Sony, Supra,
// Vivitar are not listed.
// </summary>
// <remarks>
// Converted from Exiftool version 10.33 created by Phil Harvey
// http://www.sno.phy.queensu.ca/~phil/exiftool/
// lib\Image\ExifTool\Olympus.pm
// </remarks>
    @kotlin.jvm.JvmField
    val OlympusCameraTypes = HashMap<String, String>()

    init {
      tagNameMap[TAG_MAKERNOTE_VERSION] = "Makernote Version"
      tagNameMap[TAG_CAMERA_SETTINGS_1] = "Camera Settings"
      tagNameMap[TAG_CAMERA_SETTINGS_2] = "Camera Settings"
      tagNameMap[TAG_COMPRESSED_IMAGE_SIZE] = "Compressed Image Size"
      tagNameMap[TAG_MINOLTA_THUMBNAIL_OFFSET_1] = "Thumbnail Offset"
      tagNameMap[TAG_MINOLTA_THUMBNAIL_OFFSET_2] = "Thumbnail Offset"
      tagNameMap[TAG_MINOLTA_THUMBNAIL_LENGTH] = "Thumbnail Length"
      tagNameMap[TAG_THUMBNAIL_IMAGE] = "Thumbnail Image"
      tagNameMap[TAG_COLOUR_MODE] = "Colour Mode"
      tagNameMap[TAG_IMAGE_QUALITY_1] = "Image Quality"
      tagNameMap[TAG_IMAGE_QUALITY_2] = "Image Quality"
      tagNameMap[TAG_BODY_FIRMWARE_VERSION] = "Body Firmware Version"
      tagNameMap[TAG_SPECIAL_MODE] = "Special Mode"
      tagNameMap[TAG_JPEG_QUALITY] = "JPEG Quality"
      tagNameMap[TAG_MACRO_MODE] = "Macro"
      tagNameMap[TAG_BW_MODE] = "BW Mode"
      tagNameMap[TAG_DIGITAL_ZOOM] = "Digital Zoom"
      tagNameMap[TAG_FOCAL_PLANE_DIAGONAL] = "Focal Plane Diagonal"
      tagNameMap[TAG_LENS_DISTORTION_PARAMETERS] = "Lens Distortion Parameters"
      tagNameMap[TAG_CAMERA_TYPE] = "Camera Type"
      tagNameMap[TAG_PICT_INFO] = "Pict Info"
      tagNameMap[TAG_CAMERA_ID] = "Camera Id"
      tagNameMap[TAG_IMAGE_WIDTH] = "Image Width"
      tagNameMap[TAG_IMAGE_HEIGHT] = "Image Height"
      tagNameMap[TAG_ORIGINAL_MANUFACTURER_MODEL] = "Original Manufacturer Model"
      tagNameMap[TAG_PREVIEW_IMAGE] = "Preview Image"
      tagNameMap[TAG_PRE_CAPTURE_FRAMES] = "Pre Capture Frames"
      tagNameMap[TAG_WHITE_BOARD] = "White Board"
      tagNameMap[TAG_ONE_TOUCH_WB] = "One Touch WB"
      tagNameMap[TAG_WHITE_BALANCE_BRACKET] = "White Balance Bracket"
      tagNameMap[TAG_WHITE_BALANCE_BIAS] = "White Balance Bias"
      tagNameMap[TAG_SCENE_MODE] = "Scene Mode"
      tagNameMap[TAG_SERIAL_NUMBER_1] = "Serial Number"
      tagNameMap[TAG_FIRMWARE] = "Firmware"
      tagNameMap[TAG_PRINT_IMAGE_MATCHING_INFO] = "Print Image Matching (PIM) Info"
      tagNameMap[TAG_DATA_DUMP_1] = "Data Dump"
      tagNameMap[TAG_DATA_DUMP_2] = "Data Dump 2"
      tagNameMap[TAG_SHUTTER_SPEED_VALUE] = "Shutter Speed Value"
      tagNameMap[TAG_ISO_VALUE] = "ISO Value"
      tagNameMap[TAG_APERTURE_VALUE] = "Aperture Value"
      tagNameMap[TAG_BRIGHTNESS_VALUE] = "Brightness Value"
      tagNameMap[TAG_FLASH_MODE] = "Flash Mode"
      tagNameMap[TAG_FLASH_DEVICE] = "Flash Device"
      tagNameMap[TAG_BRACKET] = "Bracket"
      tagNameMap[TAG_SENSOR_TEMPERATURE] = "Sensor Temperature"
      tagNameMap[TAG_LENS_TEMPERATURE] = "Lens Temperature"
      tagNameMap[TAG_LIGHT_CONDITION] = "Light Condition"
      tagNameMap[TAG_FOCUS_RANGE] = "Focus Range"
      tagNameMap[TAG_FOCUS_MODE] = "Focus Mode"
      tagNameMap[TAG_FOCUS_DISTANCE] = "Focus Distance"
      tagNameMap[TAG_ZOOM] = "Zoom"
      tagNameMap[TAG_MACRO_FOCUS] = "Macro Focus"
      tagNameMap[TAG_SHARPNESS] = "Sharpness"
      tagNameMap[TAG_FLASH_CHARGE_LEVEL] = "Flash Charge Level"
      tagNameMap[TAG_COLOUR_MATRIX] = "Colour Matrix"
      tagNameMap[TAG_BLACK_LEVEL] = "Black Level"
      tagNameMap[TAG_COLOR_TEMPERATURE_BG] = "Color Temperature BG"
      tagNameMap[TAG_COLOR_TEMPERATURE_RG] = "Color Temperature RG"
      tagNameMap[TAG_WB_MODE] = "White Balance Mode"
      tagNameMap[TAG_RED_BALANCE] = "Red Balance"
      tagNameMap[TAG_BLUE_BALANCE] = "Blue Balance"
      tagNameMap[TAG_COLOR_MATRIX_NUMBER] = "Color Matrix Number"
      tagNameMap[TAG_SERIAL_NUMBER_2] = "Serial Number"
      tagNameMap[TAG_EXTERNAL_FLASH_AE1_0] = "External Flash AE1 0"
      tagNameMap[TAG_EXTERNAL_FLASH_AE2_0] = "External Flash AE2 0"
      tagNameMap[TAG_INTERNAL_FLASH_AE1_0] = "Internal Flash AE1 0"
      tagNameMap[TAG_INTERNAL_FLASH_AE2_0] = "Internal Flash AE2 0"
      tagNameMap[TAG_EXTERNAL_FLASH_AE1] = "External Flash AE1"
      tagNameMap[TAG_EXTERNAL_FLASH_AE2] = "External Flash AE2"
      tagNameMap[TAG_INTERNAL_FLASH_AE1] = "Internal Flash AE1"
      tagNameMap[TAG_INTERNAL_FLASH_AE2] = "Internal Flash AE2"
      tagNameMap[TAG_FLASH_BIAS] = "Flash Bias"
      tagNameMap[TAG_INTERNAL_FLASH_TABLE] = "Internal Flash Table"
      tagNameMap[TAG_EXTERNAL_FLASH_G_VALUE] = "External Flash G Value"
      tagNameMap[TAG_EXTERNAL_FLASH_BOUNCE] = "External Flash Bounce"
      tagNameMap[TAG_EXTERNAL_FLASH_ZOOM] = "External Flash Zoom"
      tagNameMap[TAG_EXTERNAL_FLASH_MODE] = "External Flash Mode"
      tagNameMap[TAG_CONTRAST] = "Contrast"
      tagNameMap[TAG_SHARPNESS_FACTOR] = "Sharpness Factor"
      tagNameMap[TAG_COLOUR_CONTROL] = "Colour Control"
      tagNameMap[TAG_VALID_BITS] = "Valid Bits"
      tagNameMap[TAG_CORING_FILTER] = "Coring Filter"
      tagNameMap[TAG_OLYMPUS_IMAGE_WIDTH] = "Olympus Image Width"
      tagNameMap[TAG_OLYMPUS_IMAGE_HEIGHT] = "Olympus Image Height"
      tagNameMap[TAG_SCENE_DETECT] = "Scene Detect"
      tagNameMap[TAG_SCENE_AREA] = "Scene Area"
      tagNameMap[TAG_SCENE_DETECT_DATA] = "Scene Detect Data"
      tagNameMap[TAG_COMPRESSION_RATIO] = "Compression Ratio"
      tagNameMap[TAG_PREVIEW_IMAGE_VALID] = "Preview Image Valid"
      tagNameMap[TAG_PREVIEW_IMAGE_START] = "Preview Image Start"
      tagNameMap[TAG_PREVIEW_IMAGE_LENGTH] = "Preview Image Length"
      tagNameMap[TAG_AF_RESULT] = "AF Result"
      tagNameMap[TAG_CCD_SCAN_MODE] = "CCD Scan Mode"
      tagNameMap[TAG_NOISE_REDUCTION] = "Noise Reduction"
      tagNameMap[TAG_INFINITY_LENS_STEP] = "Infinity Lens Step"
      tagNameMap[TAG_NEAR_LENS_STEP] = "Near Lens Step"
      tagNameMap[TAG_LIGHT_VALUE_CENTER] = "Light Value Center"
      tagNameMap[TAG_LIGHT_VALUE_PERIPHERY] = "Light Value Periphery"
      tagNameMap[TAG_FIELD_COUNT] = "Field Count"
      tagNameMap[TAG_EQUIPMENT] = "Equipment"
      tagNameMap[TAG_CAMERA_SETTINGS] = "Camera Settings"
      tagNameMap[TAG_RAW_DEVELOPMENT] = "Raw Development"
      tagNameMap[TAG_RAW_DEVELOPMENT_2] = "Raw Development 2"
      tagNameMap[TAG_IMAGE_PROCESSING] = "Image Processing"
      tagNameMap[TAG_FOCUS_INFO] = "Focus Info"
      tagNameMap[TAG_RAW_INFO] = "Raw Info"
      tagNameMap[TAG_MAIN_INFO] = "Main Info"
      tagNameMap[CameraSettings.TAG_EXPOSURE_MODE] = "Exposure Mode"
      tagNameMap[CameraSettings.TAG_FLASH_MODE] = "Flash Mode"
      tagNameMap[CameraSettings.TAG_WHITE_BALANCE] = "White Balance"
      tagNameMap[CameraSettings.TAG_IMAGE_SIZE] = "Image Size"
      tagNameMap[CameraSettings.TAG_IMAGE_QUALITY] = "Image Quality"
      tagNameMap[CameraSettings.TAG_SHOOTING_MODE] = "Shooting Mode"
      tagNameMap[CameraSettings.TAG_METERING_MODE] = "Metering Mode"
      tagNameMap[CameraSettings.TAG_APEX_FILM_SPEED_VALUE] = "Apex Film Speed Value"
      tagNameMap[CameraSettings.TAG_APEX_SHUTTER_SPEED_TIME_VALUE] = "Apex Shutter Speed Time Value"
      tagNameMap[CameraSettings.TAG_APEX_APERTURE_VALUE] = "Apex Aperture Value"
      tagNameMap[CameraSettings.TAG_MACRO_MODE] = "Macro Mode"
      tagNameMap[CameraSettings.TAG_DIGITAL_ZOOM] = "Digital Zoom"
      tagNameMap[CameraSettings.TAG_EXPOSURE_COMPENSATION] = "Exposure Compensation"
      tagNameMap[CameraSettings.TAG_BRACKET_STEP] = "Bracket Step"
      tagNameMap[CameraSettings.TAG_INTERVAL_LENGTH] = "Interval Length"
      tagNameMap[CameraSettings.TAG_INTERVAL_NUMBER] = "Interval Number"
      tagNameMap[CameraSettings.TAG_FOCAL_LENGTH] = "Focal Length"
      tagNameMap[CameraSettings.TAG_FOCUS_DISTANCE] = "Focus Distance"
      tagNameMap[CameraSettings.TAG_FLASH_FIRED] = "Flash Fired"
      tagNameMap[CameraSettings.TAG_DATE] = "Date"
      tagNameMap[CameraSettings.TAG_TIME] = "Time"
      tagNameMap[CameraSettings.TAG_MAX_APERTURE_AT_FOCAL_LENGTH] = "Max Aperture at Focal Length"
      tagNameMap[CameraSettings.TAG_FILE_NUMBER_MEMORY] = "File Number Memory"
      tagNameMap[CameraSettings.TAG_LAST_FILE_NUMBER] = "Last File Number"
      tagNameMap[CameraSettings.TAG_WHITE_BALANCE_RED] = "White Balance Red"
      tagNameMap[CameraSettings.TAG_WHITE_BALANCE_GREEN] = "White Balance Green"
      tagNameMap[CameraSettings.TAG_WHITE_BALANCE_BLUE] = "White Balance Blue"
      tagNameMap[CameraSettings.TAG_SATURATION] = "Saturation"
      tagNameMap[CameraSettings.TAG_CONTRAST] = "Contrast"
      tagNameMap[CameraSettings.TAG_SHARPNESS] = "Sharpness"
      tagNameMap[CameraSettings.TAG_SUBJECT_PROGRAM] = "Subject Program"
      tagNameMap[CameraSettings.TAG_FLASH_COMPENSATION] = "Flash Compensation"
      tagNameMap[CameraSettings.TAG_ISO_SETTING] = "ISO Setting"
      tagNameMap[CameraSettings.TAG_CAMERA_MODEL] = "Camera Model"
      tagNameMap[CameraSettings.TAG_INTERVAL_MODE] = "Interval Mode"
      tagNameMap[CameraSettings.TAG_FOLDER_NAME] = "Folder Name"
      tagNameMap[CameraSettings.TAG_COLOR_MODE] = "Color Mode"
      tagNameMap[CameraSettings.TAG_COLOR_FILTER] = "Color Filter"
      tagNameMap[CameraSettings.TAG_BLACK_AND_WHITE_FILTER] = "Black and White Filter"
      tagNameMap[CameraSettings.TAG_INTERNAL_FLASH] = "Internal Flash"
      tagNameMap[CameraSettings.TAG_APEX_BRIGHTNESS_VALUE] = "Apex Brightness Value"
      tagNameMap[CameraSettings.TAG_SPOT_FOCUS_POINT_X_COORDINATE] = "Spot Focus Point X Coordinate"
      tagNameMap[CameraSettings.TAG_SPOT_FOCUS_POINT_Y_COORDINATE] = "Spot Focus Point Y Coordinate"
      tagNameMap[CameraSettings.TAG_WIDE_FOCUS_ZONE] = "Wide Focus Zone"
      tagNameMap[CameraSettings.TAG_FOCUS_MODE] = "Focus Mode"
      tagNameMap[CameraSettings.TAG_FOCUS_AREA] = "Focus Area"
      tagNameMap[CameraSettings.TAG_DEC_SWITCH_POSITION] = "DEC Switch Position"
    }

    init {
      OlympusCameraTypes["D4028"] = "X-2,C-50Z"
      OlympusCameraTypes["D4029"] = "E-20,E-20N,E-20P"
      OlympusCameraTypes["D4034"] = "C720UZ"
      OlympusCameraTypes["D4040"] = "E-1"
      OlympusCameraTypes["D4041"] = "E-300"
      OlympusCameraTypes["D4083"] = "C2Z,D520Z,C220Z"
      OlympusCameraTypes["D4106"] = "u20D,S400D,u400D"
      OlympusCameraTypes["D4120"] = "X-1"
      OlympusCameraTypes["D4122"] = "u10D,S300D,u300D"
      OlympusCameraTypes["D4125"] = "AZ-1"
      OlympusCameraTypes["D4141"] = "C150,D390"
      OlympusCameraTypes["D4193"] = "C-5000Z"
      OlympusCameraTypes["D4194"] = "X-3,C-60Z"
      OlympusCameraTypes["D4199"] = "u30D,S410D,u410D"
      OlympusCameraTypes["D4205"] = "X450,D535Z,C370Z"
      OlympusCameraTypes["D4210"] = "C160,D395"
      OlympusCameraTypes["D4211"] = "C725UZ"
      OlympusCameraTypes["D4213"] = "FerrariMODEL2003"
      OlympusCameraTypes["D4216"] = "u15D"
      OlympusCameraTypes["D4217"] = "u25D"
      OlympusCameraTypes["D4220"] = "u-miniD,Stylus V"
      OlympusCameraTypes["D4221"] = "u40D,S500,uD500"
      OlympusCameraTypes["D4231"] = "FerrariMODEL2004"
      OlympusCameraTypes["D4240"] = "X500,D590Z,C470Z"
      OlympusCameraTypes["D4244"] = "uD800,S800"
      OlympusCameraTypes["D4256"] = "u720SW,S720SW"
      OlympusCameraTypes["D4261"] = "X600,D630,FE5500"
      OlympusCameraTypes["D4262"] = "uD600,S600"
      OlympusCameraTypes["D4301"] = "u810/S810" // (yes, "/".  Olympus is not consistent in the notation)
      OlympusCameraTypes["D4302"] = "u710,S710"
      OlympusCameraTypes["D4303"] = "u700,S700"
      OlympusCameraTypes["D4304"] = "FE100,X710"
      OlympusCameraTypes["D4305"] = "FE110,X705"
      OlympusCameraTypes["D4310"] = "FE-130,X-720"
      OlympusCameraTypes["D4311"] = "FE-140,X-725"
      OlympusCameraTypes["D4312"] = "FE150,X730"
      OlympusCameraTypes["D4313"] = "FE160,X735"
      OlympusCameraTypes["D4314"] = "u740,S740"
      OlympusCameraTypes["D4315"] = "u750,S750"
      OlympusCameraTypes["D4316"] = "u730/S730"
      OlympusCameraTypes["D4317"] = "FE115,X715"
      OlympusCameraTypes["D4321"] = "SP550UZ"
      OlympusCameraTypes["D4322"] = "SP510UZ"
      OlympusCameraTypes["D4324"] = "FE170,X760"
      OlympusCameraTypes["D4326"] = "FE200"
      OlympusCameraTypes["D4327"] = "FE190/X750" // (also SX876)
      OlympusCameraTypes["D4328"] = "u760,S760"
      OlympusCameraTypes["D4330"] = "FE180/X745" // (also SX875)
      OlympusCameraTypes["D4331"] = "u1000/S1000"
      OlympusCameraTypes["D4332"] = "u770SW,S770SW"
      OlympusCameraTypes["D4333"] = "FE240/X795"
      OlympusCameraTypes["D4334"] = "FE210,X775"
      OlympusCameraTypes["D4336"] = "FE230/X790"
      OlympusCameraTypes["D4337"] = "FE220,X785"
      OlympusCameraTypes["D4338"] = "u725SW,S725SW"
      OlympusCameraTypes["D4339"] = "FE250/X800"
      OlympusCameraTypes["D4341"] = "u780,S780"
      OlympusCameraTypes["D4343"] = "u790SW,S790SW"
      OlympusCameraTypes["D4344"] = "u1020,S1020"
      OlympusCameraTypes["D4346"] = "FE15,X10"
      OlympusCameraTypes["D4348"] = "FE280,X820,C520"
      OlympusCameraTypes["D4349"] = "FE300,X830"
      OlympusCameraTypes["D4350"] = "u820,S820"
      OlympusCameraTypes["D4351"] = "u1200,S1200"
      OlympusCameraTypes["D4352"] = "FE270,X815,C510"
      OlympusCameraTypes["D4353"] = "u795SW,S795SW"
      OlympusCameraTypes["D4354"] = "u1030SW,S1030SW"
      OlympusCameraTypes["D4355"] = "SP560UZ"
      OlympusCameraTypes["D4356"] = "u1010,S1010"
      OlympusCameraTypes["D4357"] = "u830,S830"
      OlympusCameraTypes["D4359"] = "u840,S840"
      OlympusCameraTypes["D4360"] = "FE350WIDE,X865"
      OlympusCameraTypes["D4361"] = "u850SW,S850SW"
      OlympusCameraTypes["D4362"] = "FE340,X855,C560"
      OlympusCameraTypes["D4363"] = "FE320,X835,C540"
      OlympusCameraTypes["D4364"] = "SP570UZ"
      OlympusCameraTypes["D4366"] = "FE330,X845,C550"
      OlympusCameraTypes["D4368"] = "FE310,X840,C530"
      OlympusCameraTypes["D4370"] = "u1050SW,S1050SW"
      OlympusCameraTypes["D4371"] = "u1060,S1060"
      OlympusCameraTypes["D4372"] = "FE370,X880,C575"
      OlympusCameraTypes["D4374"] = "SP565UZ"
      OlympusCameraTypes["D4377"] = "u1040,S1040"
      OlympusCameraTypes["D4378"] = "FE360,X875,C570"
      OlympusCameraTypes["D4379"] = "FE20,X15,C25"
      OlympusCameraTypes["D4380"] = "uT6000,ST6000"
      OlympusCameraTypes["D4381"] = "uT8000,ST8000"
      OlympusCameraTypes["D4382"] = "u9000,S9000"
      OlympusCameraTypes["D4384"] = "SP590UZ"
      OlympusCameraTypes["D4385"] = "FE3010,X895"
      OlympusCameraTypes["D4386"] = "FE3000,X890"
      OlympusCameraTypes["D4387"] = "FE35,X30"
      OlympusCameraTypes["D4388"] = "u550WP,S550WP"
      OlympusCameraTypes["D4390"] = "FE5000,X905"
      OlympusCameraTypes["D4391"] = "u5000"
      OlympusCameraTypes["D4392"] = "u7000,S7000"
      OlympusCameraTypes["D4396"] = "FE5010,X915"
      OlympusCameraTypes["D4397"] = "FE25,X20"
      OlympusCameraTypes["D4398"] = "FE45,X40"
      OlympusCameraTypes["D4401"] = "XZ-1"
      OlympusCameraTypes["D4402"] = "uT6010,ST6010"
      OlympusCameraTypes["D4406"] = "u7010,S7010 / u7020,S7020"
      OlympusCameraTypes["D4407"] = "FE4010,X930"
      OlympusCameraTypes["D4408"] = "X560WP"
      OlympusCameraTypes["D4409"] = "FE26,X21"
      OlympusCameraTypes["D4410"] = "FE4000,X920,X925"
      OlympusCameraTypes["D4411"] = "FE46,X41,X42"
      OlympusCameraTypes["D4412"] = "FE5020,X935"
      OlympusCameraTypes["D4413"] = "uTough-3000"
      OlympusCameraTypes["D4414"] = "StylusTough-6020"
      OlympusCameraTypes["D4415"] = "StylusTough-8010"
      OlympusCameraTypes["D4417"] = "u5010,S5010"
      OlympusCameraTypes["D4418"] = "u7040,S7040"
      OlympusCameraTypes["D4419"] = "u9010,S9010"
      OlympusCameraTypes["D4423"] = "FE4040"
      OlympusCameraTypes["D4424"] = "FE47,X43"
      OlympusCameraTypes["D4426"] = "FE4030,X950"
      OlympusCameraTypes["D4428"] = "FE5030,X965,X960"
      OlympusCameraTypes["D4430"] = "u7030,S7030"
      OlympusCameraTypes["D4432"] = "SP600UZ"
      OlympusCameraTypes["D4434"] = "SP800UZ"
      OlympusCameraTypes["D4439"] = "FE4020,X940"
      OlympusCameraTypes["D4442"] = "FE5035"
      OlympusCameraTypes["D4448"] = "FE4050,X970"
      OlympusCameraTypes["D4450"] = "FE5050,X985"
      OlympusCameraTypes["D4454"] = "u-7050"
      OlympusCameraTypes["D4464"] = "T10,X27"
      OlympusCameraTypes["D4470"] = "FE5040,X980"
      OlympusCameraTypes["D4472"] = "TG-310"
      OlympusCameraTypes["D4474"] = "TG-610"
      OlympusCameraTypes["D4476"] = "TG-810"
      OlympusCameraTypes["D4478"] = "VG145,VG140,D715"
      OlympusCameraTypes["D4479"] = "VG130,D710"
      OlympusCameraTypes["D4480"] = "VG120,D705"
      OlympusCameraTypes["D4482"] = "VR310,D720"
      OlympusCameraTypes["D4484"] = "VR320,D725"
      OlympusCameraTypes["D4486"] = "VR330,D730"
      OlympusCameraTypes["D4488"] = "VG110,D700"
      OlympusCameraTypes["D4490"] = "SP-610UZ"
      OlympusCameraTypes["D4492"] = "SZ-10"
      OlympusCameraTypes["D4494"] = "SZ-20"
      OlympusCameraTypes["D4496"] = "SZ-30MR"
      OlympusCameraTypes["D4498"] = "SP-810UZ"
      OlympusCameraTypes["D4500"] = "SZ-11"
      OlympusCameraTypes["D4504"] = "TG-615"
      OlympusCameraTypes["D4508"] = "TG-620"
      OlympusCameraTypes["D4510"] = "TG-820"
      OlympusCameraTypes["D4512"] = "TG-1"
      OlympusCameraTypes["D4516"] = "SH-21"
      OlympusCameraTypes["D4519"] = "SZ-14"
      OlympusCameraTypes["D4520"] = "SZ-31MR"
      OlympusCameraTypes["D4521"] = "SH-25MR"
      OlympusCameraTypes["D4523"] = "SP-720UZ"
      OlympusCameraTypes["D4529"] = "VG170"
      OlympusCameraTypes["D4531"] = "XZ-2"
      OlympusCameraTypes["D4535"] = "SP-620UZ"
      OlympusCameraTypes["D4536"] = "TG-320"
      OlympusCameraTypes["D4537"] = "VR340,D750"
      OlympusCameraTypes["D4538"] = "VG160,X990,D745"
      OlympusCameraTypes["D4541"] = "SZ-12"
      OlympusCameraTypes["D4545"] = "VH410"
      OlympusCameraTypes["D4546"] = "XZ-10" //IB
      OlympusCameraTypes["D4547"] = "TG-2"
      OlympusCameraTypes["D4548"] = "TG-830"
      OlympusCameraTypes["D4549"] = "TG-630"
      OlympusCameraTypes["D4550"] = "SH-50"
      OlympusCameraTypes["D4553"] = "SZ-16,DZ-105"
      OlympusCameraTypes["D4562"] = "SP-820UZ"
      OlympusCameraTypes["D4566"] = "SZ-15"
      OlympusCameraTypes["D4572"] = "STYLUS1"
      OlympusCameraTypes["D4574"] = "TG-3"
      OlympusCameraTypes["D4575"] = "TG-850"
      OlympusCameraTypes["D4579"] = "SP-100EE"
      OlympusCameraTypes["D4580"] = "SH-60"
      OlympusCameraTypes["D4581"] = "SH-1"
      OlympusCameraTypes["D4582"] = "TG-835"
      OlympusCameraTypes["D4585"] = "SH-2 / SH-3"
      OlympusCameraTypes["D4586"] = "TG-4"
      OlympusCameraTypes["D4587"] = "TG-860"
      OlympusCameraTypes["D4591"] = "TG-870"
      OlympusCameraTypes["D4809"] = "C2500L"
      OlympusCameraTypes["D4842"] = "E-10"
      OlympusCameraTypes["D4856"] = "C-1"
      OlympusCameraTypes["D4857"] = "C-1Z,D-150Z"
      OlympusCameraTypes["DCHC"] = "D500L"
      OlympusCameraTypes["DCHT"] = "D600L / D620L"
      OlympusCameraTypes["K0055"] = "AIR-A01"
      OlympusCameraTypes["S0003"] = "E-330"
      OlympusCameraTypes["S0004"] = "E-500"
      OlympusCameraTypes["S0009"] = "E-400"
      OlympusCameraTypes["S0010"] = "E-510"
      OlympusCameraTypes["S0011"] = "E-3"
      OlympusCameraTypes["S0013"] = "E-410"
      OlympusCameraTypes["S0016"] = "E-420"
      OlympusCameraTypes["S0017"] = "E-30"
      OlympusCameraTypes["S0018"] = "E-520"
      OlympusCameraTypes["S0019"] = "E-P1"
      OlympusCameraTypes["S0023"] = "E-620"
      OlympusCameraTypes["S0026"] = "E-P2"
      OlympusCameraTypes["S0027"] = "E-PL1"
      OlympusCameraTypes["S0029"] = "E-450"
      OlympusCameraTypes["S0030"] = "E-600"
      OlympusCameraTypes["S0032"] = "E-P3"
      OlympusCameraTypes["S0033"] = "E-5"
      OlympusCameraTypes["S0034"] = "E-PL2"
      OlympusCameraTypes["S0036"] = "E-M5"
      OlympusCameraTypes["S0038"] = "E-PL3"
      OlympusCameraTypes["S0039"] = "E-PM1"
      OlympusCameraTypes["S0040"] = "E-PL1s"
      OlympusCameraTypes["S0042"] = "E-PL5"
      OlympusCameraTypes["S0043"] = "E-PM2"
      OlympusCameraTypes["S0044"] = "E-P5"
      OlympusCameraTypes["S0045"] = "E-PL6"
      OlympusCameraTypes["S0046"] = "E-PL7" //IB
      OlympusCameraTypes["S0047"] = "E-M1"
      OlympusCameraTypes["S0051"] = "E-M10"
      OlympusCameraTypes["S0052"] = "E-M5MarkII" //IB
      OlympusCameraTypes["S0059"] = "E-M10MarkII"
      OlympusCameraTypes["S0061"] = "PEN-F" //forum7005
      OlympusCameraTypes["S0065"] = "E-PL8"
      OlympusCameraTypes["S0067"] = "E-M1MarkII"
      OlympusCameraTypes["SR45"] = "D220"
      OlympusCameraTypes["SR55"] = "D320L"
      OlympusCameraTypes["SR83"] = "D340L"
      OlympusCameraTypes["SR85"] = "C830L,D340R"
      OlympusCameraTypes["SR852"] = "C860L,D360L"
      OlympusCameraTypes["SR872"] = "C900Z,D400Z"
      OlympusCameraTypes["SR874"] = "C960Z,D460Z"
      OlympusCameraTypes["SR951"] = "C2000Z"
      OlympusCameraTypes["SR952"] = "C21"
      OlympusCameraTypes["SR953"] = "C21T.commu"
      OlympusCameraTypes["SR954"] = "C2020Z"
      OlympusCameraTypes["SR955"] = "C990Z,D490Z"
      OlympusCameraTypes["SR956"] = "C211Z"
      OlympusCameraTypes["SR959"] = "C990ZS,D490Z"
      OlympusCameraTypes["SR95A"] = "C2100UZ"
      OlympusCameraTypes["SR971"] = "C100,D370"
      OlympusCameraTypes["SR973"] = "C2,D230"
      OlympusCameraTypes["SX151"] = "E100RS"
      OlympusCameraTypes["SX351"] = "C3000Z / C3030Z"
      OlympusCameraTypes["SX354"] = "C3040Z"
      OlympusCameraTypes["SX355"] = "C2040Z"
      OlympusCameraTypes["SX357"] = "C700UZ"
      OlympusCameraTypes["SX358"] = "C200Z,D510Z"
      OlympusCameraTypes["SX374"] = "C3100Z,C3020Z"
      OlympusCameraTypes["SX552"] = "C4040Z"
      OlympusCameraTypes["SX553"] = "C40Z,D40Z"
      OlympusCameraTypes["SX556"] = "C730UZ"
      OlympusCameraTypes["SX558"] = "C5050Z"
      OlympusCameraTypes["SX571"] = "C120,D380"
      OlympusCameraTypes["SX574"] = "C300Z,D550Z"
      OlympusCameraTypes["SX575"] = "C4100Z,C4000Z"
      OlympusCameraTypes["SX751"] = "X200,D560Z,C350Z"
      OlympusCameraTypes["SX752"] = "X300,D565Z,C450Z"
      OlympusCameraTypes["SX753"] = "C750UZ"
      OlympusCameraTypes["SX754"] = "C740UZ"
      OlympusCameraTypes["SX755"] = "C755UZ"
      OlympusCameraTypes["SX756"] = "C5060WZ"
      OlympusCameraTypes["SX757"] = "C8080WZ"
      OlympusCameraTypes["SX758"] = "X350,D575Z,C360Z"
      OlympusCameraTypes["SX759"] = "X400,D580Z,C460Z"
      OlympusCameraTypes["SX75A"] = "AZ-2ZOOM"
      OlympusCameraTypes["SX75B"] = "D595Z,C500Z"
      OlympusCameraTypes["SX75C"] = "X550,D545Z,C480Z"
      OlympusCameraTypes["SX75D"] = "IR-300"
      OlympusCameraTypes["SX75F"] = "C55Z,C5500Z"
      OlympusCameraTypes["SX75G"] = "C170,D425"
      OlympusCameraTypes["SX75J"] = "C180,D435"
      OlympusCameraTypes["SX771"] = "C760UZ"
      OlympusCameraTypes["SX772"] = "C770UZ"
      OlympusCameraTypes["SX773"] = "C745UZ"
      OlympusCameraTypes["SX774"] = "X250,D560Z,C350Z"
      OlympusCameraTypes["SX775"] = "X100,D540Z,C310Z"
      OlympusCameraTypes["SX776"] = "C460ZdelSol"
      OlympusCameraTypes["SX777"] = "C765UZ"
      OlympusCameraTypes["SX77A"] = "D555Z,C315Z"
      OlympusCameraTypes["SX851"] = "C7070WZ"
      OlympusCameraTypes["SX852"] = "C70Z,C7000Z"
      OlympusCameraTypes["SX853"] = "SP500UZ"
      OlympusCameraTypes["SX854"] = "SP310"
      OlympusCameraTypes["SX855"] = "SP350"
      OlympusCameraTypes["SX873"] = "SP320"
      OlympusCameraTypes["SX875"] = "FE180/X745" // (also D4330)
      OlympusCameraTypes["SX876"] = "FE190/X750" // (also D4327)
      //   other brands
//    4MP9Q3", "Camera 4MP-9Q3'
//    4MP9T2", "BenQ DC C420 / Camera 4MP-9T2'
//    5MP9Q3", "Camera 5MP-9Q3" },
//    5MP9X9", "Camera 5MP-9X9" },
//   '5MP-9T'=> 'Camera 5MP-9T3" },
//   '5MP-9Y'=> 'Camera 5MP-9Y2" },
//   '6MP-9U'=> 'Camera 6MP-9U9" },
//    7MP9Q3", "Camera 7MP-9Q3" },
//   '8MP-9U'=> 'Camera 8MP-9U4" },
//    CE5330", "Acer CE-5330" },
//   'CP-853'=> 'Acer CP-8531" },
//    CS5531", "Acer CS5531" },
//    DC500 ", "SeaLife DC500" },
//    DC7370", "Camera 7MP-9GA" },
//    DC7371", "Camera 7MP-9GM" },
//    DC7371", "Hitachi HDC-751E" },
//    DC7375", "Hitachi HDC-763E / Rollei RCP-7330X / Ricoh Caplio RR770 / Vivitar ViviCam 7330" },
//   'DC E63'=> 'BenQ DC E63+" },
//   'DC P86'=> 'BenQ DC P860" },
//    DS5340", "Maginon Performic S5 / Premier 5MP-9M7" },
//    DS5341", "BenQ E53+ / Supra TCM X50 / Maginon X50 / Premier 5MP-9P8" },
//    DS5346", "Premier 5MP-9Q2" },
//    E500  ", "Konica Minolta DiMAGE E500" },
//    MAGINO", "Maginon X60" },
//    Mz60  ", "HP Photosmart Mz60" },
//    Q3DIGI", "Camera 5MP-9Q3" },
//    SLIMLI", "Supra Slimline X6" },
//    V8300s", "Vivitar V8300s" },
    }
  }

  override val name: String
    get() = "Olympus Makernote"

  override fun setByteArray(tagType: Int, bytes: ByteArray) {
    if (tagType == TAG_CAMERA_SETTINGS_1 || tagType == TAG_CAMERA_SETTINGS_2) {
      processCameraSettings(bytes)
    } else {
      super.setByteArray(tagType, bytes)
    }
  }

  private fun processCameraSettings(bytes: ByteArray) {
    val reader = SequentialByteArrayReader(bytes)
    reader.isMotorolaByteOrder = true
    val count = bytes.size / 4
    try {
      for (i in 0 until count) {
        val value = reader.getInt32()
        setInt(CameraSettings.OFFSET + i, value)
      }
    } catch (e: IOException) { // Should never happen, given that we check the length of the bytes beforehand.
      e.printStackTrace()
    }
  }

  val isIntervalMode: Boolean
    get() {
      val value = getLongObject(CameraSettings.TAG_SHOOTING_MODE)
      return value != null && value == 5L
    }

  init {
    setDescriptor(OlympusMakernoteDescriptor(this))
  }
}
