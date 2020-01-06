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

import com.drew.imaging.apertureToFStop
import com.drew.lang.isValidDate
import com.drew.lang.isValidTime
import com.drew.metadata.TagDescriptor
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.pow
import kotlin.math.roundToLong

/**
 * Provides human-readable string representations of tag values stored in a [OlympusMakernoteDirectory].
 *
 * @author Drew Noakes https://drewnoakes.com
 */
class OlympusMakernoteDescriptor  // TODO extend support for some offset-encoded byte[] tags: http://www.ozhiker.com/electronics/pjmt/jpeg_info/olympus_mn.html
(directory: OlympusMakernoteDirectory) : TagDescriptor<OlympusMakernoteDirectory>(directory) {
  override fun getDescription(tagType: Int): String? {
    return when (tagType) {
      OlympusMakernoteDirectory.TAG_MAKERNOTE_VERSION -> makernoteVersionDescription
      OlympusMakernoteDirectory.TAG_COLOUR_MODE -> colorModeDescription
      OlympusMakernoteDirectory.TAG_IMAGE_QUALITY_1 -> imageQuality1Description
      OlympusMakernoteDirectory.TAG_IMAGE_QUALITY_2 -> imageQuality2Description
      OlympusMakernoteDirectory.TAG_SPECIAL_MODE -> specialModeDescription
      OlympusMakernoteDirectory.TAG_JPEG_QUALITY -> jpegQualityDescription
      OlympusMakernoteDirectory.TAG_MACRO_MODE -> macroModeDescription
      OlympusMakernoteDirectory.TAG_BW_MODE -> bWModeDescription
      OlympusMakernoteDirectory.TAG_DIGITAL_ZOOM -> digitalZoomDescription
      OlympusMakernoteDirectory.TAG_FOCAL_PLANE_DIAGONAL -> focalPlaneDiagonalDescription
      OlympusMakernoteDirectory.TAG_CAMERA_TYPE -> cameraTypeDescription
      OlympusMakernoteDirectory.TAG_CAMERA_ID -> cameraIdDescription
      OlympusMakernoteDirectory.TAG_ONE_TOUCH_WB -> oneTouchWbDescription
      OlympusMakernoteDirectory.TAG_SHUTTER_SPEED_VALUE -> shutterSpeedDescription
      OlympusMakernoteDirectory.TAG_ISO_VALUE -> isoValueDescription
      OlympusMakernoteDirectory.TAG_APERTURE_VALUE -> apertureValueDescription
      OlympusMakernoteDirectory.TAG_FLASH_MODE -> flashModeDescription
      OlympusMakernoteDirectory.TAG_FOCUS_RANGE -> focusRangeDescription
      OlympusMakernoteDirectory.TAG_FOCUS_MODE -> focusModeDescription
      OlympusMakernoteDirectory.TAG_SHARPNESS -> sharpnessDescription
      OlympusMakernoteDirectory.TAG_COLOUR_MATRIX -> colorMatrixDescription
      OlympusMakernoteDirectory.TAG_WB_MODE -> wbModeDescription
      OlympusMakernoteDirectory.TAG_RED_BALANCE -> redBalanceDescription
      OlympusMakernoteDirectory.TAG_BLUE_BALANCE -> blueBalanceDescription
      OlympusMakernoteDirectory.TAG_CONTRAST -> contrastDescription
      OlympusMakernoteDirectory.TAG_PREVIEW_IMAGE_VALID -> previewImageValidDescription
      OlympusMakernoteDirectory.CameraSettings.TAG_EXPOSURE_MODE -> exposureModeDescription
      OlympusMakernoteDirectory.CameraSettings.TAG_FLASH_MODE -> flashModeCameraSettingDescription
      OlympusMakernoteDirectory.CameraSettings.TAG_WHITE_BALANCE -> whiteBalanceDescription
      OlympusMakernoteDirectory.CameraSettings.TAG_IMAGE_SIZE -> imageSizeDescription
      OlympusMakernoteDirectory.CameraSettings.TAG_IMAGE_QUALITY -> imageQualityDescription
      OlympusMakernoteDirectory.CameraSettings.TAG_SHOOTING_MODE -> shootingModeDescription
      OlympusMakernoteDirectory.CameraSettings.TAG_METERING_MODE -> meteringModeDescription
      OlympusMakernoteDirectory.CameraSettings.TAG_APEX_FILM_SPEED_VALUE -> apexFilmSpeedDescription
      OlympusMakernoteDirectory.CameraSettings.TAG_APEX_SHUTTER_SPEED_TIME_VALUE -> apexShutterSpeedTimeDescription
      OlympusMakernoteDirectory.CameraSettings.TAG_APEX_APERTURE_VALUE -> apexApertureDescription
      OlympusMakernoteDirectory.CameraSettings.TAG_MACRO_MODE -> macroModeCameraSettingDescription
      OlympusMakernoteDirectory.CameraSettings.TAG_DIGITAL_ZOOM -> digitalZoomCameraSettingDescription
      OlympusMakernoteDirectory.CameraSettings.TAG_EXPOSURE_COMPENSATION -> exposureCompensationDescription
      OlympusMakernoteDirectory.CameraSettings.TAG_BRACKET_STEP -> bracketStepDescription
      OlympusMakernoteDirectory.CameraSettings.TAG_INTERVAL_LENGTH -> intervalLengthDescription
      OlympusMakernoteDirectory.CameraSettings.TAG_INTERVAL_NUMBER -> intervalNumberDescription
      OlympusMakernoteDirectory.CameraSettings.TAG_FOCAL_LENGTH -> focalLengthDescription
      OlympusMakernoteDirectory.CameraSettings.TAG_FOCUS_DISTANCE -> focusDistanceDescription
      OlympusMakernoteDirectory.CameraSettings.TAG_FLASH_FIRED -> flashFiredDescription
      OlympusMakernoteDirectory.CameraSettings.TAG_DATE -> dateDescription
      OlympusMakernoteDirectory.CameraSettings.TAG_TIME -> timeDescription
      OlympusMakernoteDirectory.CameraSettings.TAG_MAX_APERTURE_AT_FOCAL_LENGTH -> maxApertureAtFocalLengthDescription
      OlympusMakernoteDirectory.CameraSettings.TAG_FILE_NUMBER_MEMORY -> fileNumberMemoryDescription
      OlympusMakernoteDirectory.CameraSettings.TAG_LAST_FILE_NUMBER -> lastFileNumberDescription
      OlympusMakernoteDirectory.CameraSettings.TAG_WHITE_BALANCE_RED -> whiteBalanceRedDescription
      OlympusMakernoteDirectory.CameraSettings.TAG_WHITE_BALANCE_GREEN -> whiteBalanceGreenDescription
      OlympusMakernoteDirectory.CameraSettings.TAG_WHITE_BALANCE_BLUE -> whiteBalanceBlueDescription
      OlympusMakernoteDirectory.CameraSettings.TAG_SATURATION -> saturationDescription
      OlympusMakernoteDirectory.CameraSettings.TAG_CONTRAST -> contrastCameraSettingDescription
      OlympusMakernoteDirectory.CameraSettings.TAG_SHARPNESS -> sharpnessCameraSettingDescription
      OlympusMakernoteDirectory.CameraSettings.TAG_SUBJECT_PROGRAM -> subjectProgramDescription
      OlympusMakernoteDirectory.CameraSettings.TAG_FLASH_COMPENSATION -> flashCompensationDescription
      OlympusMakernoteDirectory.CameraSettings.TAG_ISO_SETTING -> isoSettingDescription
      OlympusMakernoteDirectory.CameraSettings.TAG_CAMERA_MODEL -> cameraModelDescription
      OlympusMakernoteDirectory.CameraSettings.TAG_INTERVAL_MODE -> intervalModeDescription
      OlympusMakernoteDirectory.CameraSettings.TAG_FOLDER_NAME -> folderNameDescription
      OlympusMakernoteDirectory.CameraSettings.TAG_COLOR_MODE -> colorModeCameraSettingDescription
      OlympusMakernoteDirectory.CameraSettings.TAG_COLOR_FILTER -> colorFilterDescription
      OlympusMakernoteDirectory.CameraSettings.TAG_BLACK_AND_WHITE_FILTER -> blackAndWhiteFilterDescription
      OlympusMakernoteDirectory.CameraSettings.TAG_INTERNAL_FLASH -> internalFlashDescription
      OlympusMakernoteDirectory.CameraSettings.TAG_APEX_BRIGHTNESS_VALUE -> apexBrightnessDescription
      OlympusMakernoteDirectory.CameraSettings.TAG_SPOT_FOCUS_POINT_X_COORDINATE -> spotFocusPointXCoordinateDescription
      OlympusMakernoteDirectory.CameraSettings.TAG_SPOT_FOCUS_POINT_Y_COORDINATE -> spotFocusPointYCoordinateDescription
      OlympusMakernoteDirectory.CameraSettings.TAG_WIDE_FOCUS_ZONE -> wideFocusZoneDescription
      OlympusMakernoteDirectory.CameraSettings.TAG_FOCUS_MODE -> focusModeCameraSettingDescription
      OlympusMakernoteDirectory.CameraSettings.TAG_FOCUS_AREA -> focusAreaDescription
      OlympusMakernoteDirectory.CameraSettings.TAG_DEC_SWITCH_POSITION -> decSwitchPositionDescription
      else -> super.getDescription(tagType)
    }
  }

  val exposureModeDescription: String?
    get() = getIndexedDescription(OlympusMakernoteDirectory.CameraSettings.TAG_EXPOSURE_MODE, "P", "A", "S", "M")

  val flashModeCameraSettingDescription: String?
    get() = getIndexedDescription(OlympusMakernoteDirectory.CameraSettings.TAG_FLASH_MODE,
      "Normal", "Red-eye reduction", "Rear flash sync", "Wireless")

  // 0
  // 5
  // 10
  val whiteBalanceDescription: String?
    get() = getIndexedDescription(OlympusMakernoteDirectory.CameraSettings.TAG_WHITE_BALANCE,
      "Auto",  // 0
      "Daylight",
      "Cloudy",
      "Tungsten",
      null,
      "Custom",  // 5
      null,
      "Fluorescent",
      "Fluorescent 2",
      null,
      null,  // 10
      "Custom 2",
      "Custom 3"
    )

  // This is a pretty weird way to store this information!
  val imageSizeDescription: String?
    get() =// This is a pretty weird way to store this information!
      getIndexedDescription(OlympusMakernoteDirectory.CameraSettings.TAG_IMAGE_SIZE, "2560 x 1920", "1600 x 1200", "1280 x 960", "640 x 480")

  val imageQualityDescription: String?
    get() = getIndexedDescription(OlympusMakernoteDirectory.CameraSettings.TAG_IMAGE_QUALITY, "Raw", "Super Fine", "Fine", "Standard", "Economy", "Extra Fine")

  val shootingModeDescription: String?
    get() = getIndexedDescription(OlympusMakernoteDirectory.CameraSettings.TAG_SHOOTING_MODE,
      "Single",
      "Continuous",
      "Self Timer",
      null,
      "Bracketing",
      "Interval",
      "UHS Continuous",
      "HS Continuous"
    )

  val meteringModeDescription: String?
    get() = getIndexedDescription(OlympusMakernoteDirectory.CameraSettings.TAG_METERING_MODE, "Multi-Segment", "Centre Weighted", "Spot")

  // http://www.ozhiker.com/electronics/pjmt/jpeg_info/minolta_mn.html#Minolta_Camera_Settings
  // Apex Speed value = value/8-1 ,
  // ISO = (2^(value/8-1))*3.125
  val apexFilmSpeedDescription: String?
    get() { // http://www.ozhiker.com/electronics/pjmt/jpeg_info/minolta_mn.html#Minolta_Camera_Settings
      // Apex Speed value = value/8-1 ,
      // ISO = (2^(value/8-1))*3.125
      val value = _directory.getLongObject(OlympusMakernoteDirectory.CameraSettings.TAG_APEX_FILM_SPEED_VALUE)
        ?: return null
      val iso = (value / 8.0 - 1).pow(2.0) * 3.125
      val format = DecimalFormat("0.##")
      format.roundingMode = RoundingMode.HALF_UP
      return format.format(iso)
    }

  // http://www.ozhiker.com/electronics/pjmt/jpeg_info/minolta_mn.html#Minolta_Camera_Settings
  // Apex Time value = value/8-6 ,
  // ShutterSpeed = 2^( (48-value)/8 ),
  // Due to rounding error value=8 should be displayed as 30 sec.
  val apexShutterSpeedTimeDescription: String?
    get() { // http://www.ozhiker.com/electronics/pjmt/jpeg_info/minolta_mn.html#Minolta_Camera_Settings
      // Apex Time value = value/8-6 ,
      // ShutterSpeed = 2^( (48-value)/8 ),
      // Due to rounding error value=8 should be displayed as 30 sec.
      val value = _directory.getLongObject(OlympusMakernoteDirectory.CameraSettings.TAG_APEX_SHUTTER_SPEED_TIME_VALUE)
        ?: return null
      val shutterSpeed = ((49 - value) / 8.0).pow(2.0)
      val format = DecimalFormat("0.###")
      format.roundingMode = RoundingMode.HALF_UP
      return format.format(shutterSpeed) + " sec"
    }

  // http://www.ozhiker.com/electronics/pjmt/jpeg_info/minolta_mn.html#Minolta_Camera_Settings
  // Apex Aperture Value = value/8-1 ,
  // Aperture F-stop = 2^( value/16-0.5 )
  val apexApertureDescription: String?
    get() { // http://www.ozhiker.com/electronics/pjmt/jpeg_info/minolta_mn.html#Minolta_Camera_Settings
      // Apex Aperture Value = value/8-1 ,
      // Aperture F-stop = 2^( value/16-0.5 )
      val value = _directory.getLongObject(OlympusMakernoteDirectory.CameraSettings.TAG_APEX_APERTURE_VALUE)
        ?: return null
      val fStop = (value / 16.0 - 0.5).pow(2.0)
      return getFStopDescription(fStop)
    }

  val macroModeCameraSettingDescription: String?
    get() = getIndexedDescription(OlympusMakernoteDirectory.CameraSettings.TAG_MACRO_MODE, "Off", "On")

  val digitalZoomCameraSettingDescription: String?
    get() = getIndexedDescription(OlympusMakernoteDirectory.CameraSettings.TAG_DIGITAL_ZOOM, "Off", "Electronic magnification", "Digital zoom 2x")

  val exposureCompensationDescription: String?
    get() {
      val value = _directory.getLongObject(OlympusMakernoteDirectory.CameraSettings.TAG_EXPOSURE_COMPENSATION)
      val format = DecimalFormat("0.##")
      return if (value == null) null else "${format.format(value / 3.0 - 2)} EV"
    }

  val bracketStepDescription: String?
    get() = getIndexedDescription(OlympusMakernoteDirectory.CameraSettings.TAG_BRACKET_STEP, "1/3 EV", "2/3 EV", "1 EV")

  val intervalLengthDescription: String?
    get() {
      if (!_directory.isIntervalMode) return "N/A"
      val value = _directory.getLongObject(OlympusMakernoteDirectory.CameraSettings.TAG_INTERVAL_LENGTH)
      return if (value == null) null else "$value min"
    }

  val intervalNumberDescription: String?
    get() {
      if (!_directory.isIntervalMode) return "N/A"
      val value = _directory.getLongObject(OlympusMakernoteDirectory.CameraSettings.TAG_INTERVAL_NUMBER)
      return value?.toString()
    }

  val focalLengthDescription: String?
    get() {
      val value = _directory.getLongObject(OlympusMakernoteDirectory.CameraSettings.TAG_FOCAL_LENGTH)
      return if (value == null) null else getFocalLengthDescription(value / 256.0)
    }

  val focusDistanceDescription: String?
    get() {
      val value = _directory.getLongObject(OlympusMakernoteDirectory.CameraSettings.TAG_FOCUS_DISTANCE)
      return if (value == null) null else if (value == 0L) "Infinity" else "$value mm"
    }

  val flashFiredDescription: String?
    get() = getIndexedDescription(OlympusMakernoteDirectory.CameraSettings.TAG_FLASH_FIRED, "No", "Yes")

  // day = value%256,
// month = floor( (value - floor( value/65536 )*65536 )/256 )
// year = floor( value/65536)
  val dateDescription: String?
    get() { // day = value%256,
// month = floor( (value - floor( value/65536 )*65536 )/256 )
// year = floor( value/65536)
      val value = _directory.getLongObject(OlympusMakernoteDirectory.CameraSettings.TAG_DATE) ?: return null
      val day = (value and 0xFF).toInt()
      val month = (value shr 16 and 0xFF).toInt()
      val year = (value shr 8 and 0xFF).toInt() + 1970
      return if (!isValidDate(year, month, day)) "Invalid date" else "%04d-%02d-%02d".format(year, month + 1, day)
    }

  // hours = floor( value/65536 ),
// minutes = floor( ( value - floor( value/65536 )*65536 )/256 ),
// seconds = value%256
  val timeDescription: String?
    get() { // hours = floor( value/65536 ),
// minutes = floor( ( value - floor( value/65536 )*65536 )/256 ),
// seconds = value%256
      val value = _directory.getLongObject(OlympusMakernoteDirectory.CameraSettings.TAG_TIME) ?: return null
      val hours = (value shr 8 and 0xFF).toInt()
      val minutes = (value shr 16 and 0xFF).toInt()
      val seconds = (value and 0xFF).toInt()
      return if (!isValidTime(hours, minutes, seconds)) "Invalid time" else "%02d:%02d:%02d".format(hours, minutes, seconds)
    }

  // Aperture F-Stop = 2^(value/16-0.5)
  val maxApertureAtFocalLengthDescription: String?
    get() { // Aperture F-Stop = 2^(value/16-0.5)
      val value = _directory.getLongObject(OlympusMakernoteDirectory.CameraSettings.TAG_TIME) ?: return null
      val fStop = (value / 16.0 - 0.5).pow(2.0)
      return getFStopDescription(fStop)
    }

  val fileNumberMemoryDescription: String?
    get() = getIndexedDescription(OlympusMakernoteDirectory.CameraSettings.TAG_FILE_NUMBER_MEMORY, "Off", "On")

  val lastFileNumberDescription: String?
    get() {
      val value = _directory.getLongObject(OlympusMakernoteDirectory.CameraSettings.TAG_LAST_FILE_NUMBER)
      return if (value == null) null else if (value == 0L) "File Number Memory Off" else value.toString()
    }

  val whiteBalanceRedDescription: String?
    get() {
      val value = _directory.getLongObject(OlympusMakernoteDirectory.CameraSettings.TAG_WHITE_BALANCE_RED)
      val format = DecimalFormat("0.##")
      return if (value == null) null else format.format(value / 256.0)
    }

  val whiteBalanceGreenDescription: String?
    get() {
      val value = _directory.getLongObject(OlympusMakernoteDirectory.CameraSettings.TAG_WHITE_BALANCE_GREEN)
      val format = DecimalFormat("0.##")
      return if (value == null) null else format.format(value / 256.0)
    }

  val whiteBalanceBlueDescription: String?
    get() {
      val value = _directory.getLongObject(OlympusMakernoteDirectory.CameraSettings.TAG_WHITE_BALANCE_BLUE)
      val format = DecimalFormat("0.##")
      return if (value == null) null else format.format(value / 256.0)
    }

  val saturationDescription: String?
    get() {
      val value = _directory.getLongObject(OlympusMakernoteDirectory.CameraSettings.TAG_SATURATION)
      return if (value == null) null else (value - 3).toString()
    }

  val contrastCameraSettingDescription: String?
    get() {
      val value = _directory.getLongObject(OlympusMakernoteDirectory.CameraSettings.TAG_CONTRAST)
      return if (value == null) null else (value - 3).toString()
    }

  val sharpnessCameraSettingDescription: String?
    get() = getIndexedDescription(OlympusMakernoteDirectory.CameraSettings.TAG_SHARPNESS, "Hard", "Normal", "Soft")

  val subjectProgramDescription: String?
    get() = getIndexedDescription(OlympusMakernoteDirectory.CameraSettings.TAG_SUBJECT_PROGRAM, "None", "Portrait", "Text", "Night Portrait", "Sunset", "Sports Action")

  val flashCompensationDescription: String?
    get() {
      val value = _directory.getLongObject(OlympusMakernoteDirectory.CameraSettings.TAG_FLASH_COMPENSATION)
      val format = DecimalFormat("0.##")
      return if (value == null) null else "${format.format((value - 6) / 3.0)} EV"
    }

  val isoSettingDescription: String?
    get() = getIndexedDescription(OlympusMakernoteDirectory.CameraSettings.TAG_ISO_SETTING, "100", "200", "400", "800", "Auto", "64")

  val cameraModelDescription: String?
    get() = getIndexedDescription(OlympusMakernoteDirectory.CameraSettings.TAG_CAMERA_MODEL,
      "DiMAGE 7",
      "DiMAGE 5",
      "DiMAGE S304",
      "DiMAGE S404",
      "DiMAGE 7i",
      "DiMAGE 7Hi",
      "DiMAGE A1",
      "DiMAGE S414")

  val intervalModeDescription: String?
    get() = getIndexedDescription(OlympusMakernoteDirectory.CameraSettings.TAG_INTERVAL_MODE, "Still Image", "Time Lapse Movie")

  val folderNameDescription: String?
    get() = getIndexedDescription(OlympusMakernoteDirectory.CameraSettings.TAG_FOLDER_NAME, "Standard Form", "Data Form")

  val colorModeCameraSettingDescription: String?
    get() = getIndexedDescription(OlympusMakernoteDirectory.CameraSettings.TAG_COLOR_MODE, "Natural Color", "Black & White", "Vivid Color", "Solarization", "AdobeRGB")

  val colorFilterDescription: String?
    get() {
      val value = _directory.getLongObject(OlympusMakernoteDirectory.CameraSettings.TAG_COLOR_FILTER)
      return if (value == null) null else (value - 3).toString()
    }

  val blackAndWhiteFilterDescription: String?
    get() = super.getDescription(OlympusMakernoteDirectory.CameraSettings.TAG_BLACK_AND_WHITE_FILTER)

  val internalFlashDescription: String?
    get() = getIndexedDescription(OlympusMakernoteDirectory.CameraSettings.TAG_INTERNAL_FLASH, "Did Not Fire", "Fired")

  val apexBrightnessDescription: String?
    get() {
      val value = _directory.getLongObject(OlympusMakernoteDirectory.CameraSettings.TAG_APEX_BRIGHTNESS_VALUE)
      val format = DecimalFormat("0.##")
      return if (value == null) null else format.format(value / 8.0 - 6)
    }

  val spotFocusPointXCoordinateDescription: String?
    get() = super.getDescription(OlympusMakernoteDirectory.CameraSettings.TAG_SPOT_FOCUS_POINT_X_COORDINATE)

  val spotFocusPointYCoordinateDescription: String?
    get() = super.getDescription(OlympusMakernoteDirectory.CameraSettings.TAG_SPOT_FOCUS_POINT_Y_COORDINATE)

  val wideFocusZoneDescription: String?
    get() = getIndexedDescription(OlympusMakernoteDirectory.CameraSettings.TAG_WIDE_FOCUS_ZONE,
      "No Zone or AF Failed",
      "Center Zone (Horizontal Orientation)",
      "Center Zone (Vertical Orientation)",
      "Left Zone",
      "Right Zone"
    )

  val focusModeCameraSettingDescription: String?
    get() = getIndexedDescription(OlympusMakernoteDirectory.CameraSettings.TAG_FOCUS_MODE, "Auto Focus", "Manual Focus")

  val focusAreaDescription: String?
    get() = getIndexedDescription(OlympusMakernoteDirectory.CameraSettings.TAG_FOCUS_AREA, "Wide Focus (Normal)", "Spot Focus")

  val decSwitchPositionDescription: String?
    get() = getIndexedDescription(OlympusMakernoteDirectory.CameraSettings.TAG_DEC_SWITCH_POSITION, "Exposure", "Contrast", "Saturation", "Filter")

  val makernoteVersionDescription: String?
    get() = getVersionBytesDescription(OlympusMakernoteDirectory.TAG_MAKERNOTE_VERSION, 2)

  val imageQuality2Description: String?
    get() = getIndexedDescription(OlympusMakernoteDirectory.TAG_IMAGE_QUALITY_2,
      "Raw",
      "Super Fine",
      "Fine",
      "Standard",
      "Extra Fine")

  val imageQuality1Description: String?
    get() = getIndexedDescription(OlympusMakernoteDirectory.TAG_IMAGE_QUALITY_1,
      "Raw",
      "Super Fine",
      "Fine",
      "Standard",
      "Extra Fine")

  val colorModeDescription: String?
    get() = getIndexedDescription(OlympusMakernoteDirectory.TAG_COLOUR_MODE,
      "Natural Colour",
      "Black & White",
      "Vivid Colour",
      "Solarization",
      "AdobeRGB")

  val sharpnessDescription: String?
    get() = getIndexedDescription(OlympusMakernoteDirectory.TAG_SHARPNESS, "Normal", "Hard", "Soft")

  val colorMatrixDescription: String?
    get() {
      val obj = _directory.getIntArray(OlympusMakernoteDirectory.TAG_COLOUR_MATRIX) ?: return null
      val sb = StringBuilder()
      for (i in obj.indices) {
        sb.append(obj[i])
        if (i < obj.size - 1) sb.append(" ")
      }
      return if (sb.isEmpty()) null else sb.toString()
    }

  val wbModeDescription: String?
    get() {
      val obj = _directory.getIntArray(OlympusMakernoteDirectory.TAG_WB_MODE) ?: return null
      return when (val value = "%d %d".format(obj[0], obj[1])) {
        "1 0" -> "Auto"
        "1 2" -> "Auto (2)"
        "1 4" -> "Auto (4)"
        "2 2" -> "3000 Kelvin"
        "2 3" -> "3700 Kelvin"
        "2 4" -> "4000 Kelvin"
        "2 5" -> "4500 Kelvin"
        "2 6" -> "5500 Kelvin"
        "2 7" -> "6500 Kelvin"
        "2 8" -> "7500 Kelvin"
        "3 0" -> "One-touch"
        else -> "Unknown $value"
      }
    }

  val redBalanceDescription: String?
    get() {
      val values = _directory.getIntArray(OlympusMakernoteDirectory.TAG_RED_BALANCE) ?: return null
      val value = values[0].toShort()
      return (value.toDouble() / 256.0).toString()
    }

  val blueBalanceDescription: String?
    get() {
      val values = _directory.getIntArray(OlympusMakernoteDirectory.TAG_BLUE_BALANCE) ?: return null
      val value = values[0].toShort()
      return (value.toDouble() / 256.0).toString()
    }

  val contrastDescription: String?
    get() = getIndexedDescription(OlympusMakernoteDirectory.TAG_CONTRAST, "High", "Normal", "Low")

  val previewImageValidDescription: String?
    get() = getIndexedDescription(OlympusMakernoteDirectory.TAG_PREVIEW_IMAGE_VALID, "No", "Yes")

  val focusModeDescription: String?
    get() = getIndexedDescription(OlympusMakernoteDirectory.TAG_FOCUS_MODE, "Auto", "Manual")

  val focusRangeDescription: String?
    get() = getIndexedDescription(OlympusMakernoteDirectory.TAG_FOCUS_RANGE, "Normal", "Macro")

  val flashModeDescription: String?
    get() = getIndexedDescription(OlympusMakernoteDirectory.TAG_FLASH_MODE, null, null, "On", "Off")

  val digitalZoomDescription: String?
    get() {
      val value = _directory.getRational(OlympusMakernoteDirectory.TAG_DIGITAL_ZOOM) ?: return null
      return value.toSimpleString(false)
    }

  val focalPlaneDiagonalDescription: String?
    get() {
      val value = _directory.getRational(OlympusMakernoteDirectory.TAG_FOCAL_PLANE_DIAGONAL) ?: return null
      val format = DecimalFormat("0.###")
      return format.format(value.toDouble()) + " mm"
    }

  val cameraTypeDescription: String?
    get() {
      val cameratype = _directory.getString(OlympusMakernoteDirectory.TAG_CAMERA_TYPE) ?: return null
      return if (OlympusMakernoteDirectory.OlympusCameraTypes.containsKey(cameratype)) OlympusMakernoteDirectory.OlympusCameraTypes[cameratype] else cameratype
    }

  val cameraIdDescription: String?
    get() {
      val bytes = _directory.getByteArray(OlympusMakernoteDirectory.TAG_CAMERA_ID) ?: return null
      return String(bytes)
    }

  val oneTouchWbDescription: String?
    get() = getIndexedDescription(OlympusMakernoteDirectory.TAG_ONE_TOUCH_WB, "Off", "On", "On (Preset)")

  val shutterSpeedDescription: String?
    get() = super.getShutterSpeedDescription(OlympusMakernoteDirectory.TAG_SHUTTER_SPEED_VALUE)

  val isoValueDescription: String?
    get() {
      val value = _directory.getRational(OlympusMakernoteDirectory.TAG_ISO_VALUE) ?: return null
      return (2.0.pow(value.toDouble() - 5) * 100).roundToLong().toString()
    }

  val apertureValueDescription: String?
    get() {
      val aperture = _directory.getDoubleObject(OlympusMakernoteDirectory.TAG_APERTURE_VALUE) ?: return null
      val fStop = apertureToFStop(aperture)
      return getFStopDescription(fStop)
    }

  val macroModeDescription: String?
    get() = getIndexedDescription(OlympusMakernoteDirectory.TAG_MACRO_MODE, "Normal (no macro)", "Macro")

  val bWModeDescription: String?
    get() = getIndexedDescription(OlympusMakernoteDirectory.TAG_BW_MODE, "Off", "On")

  val jpegQualityDescription: String?
    get() {
      val cameratype = _directory.getString(OlympusMakernoteDirectory.TAG_CAMERA_TYPE)
      return if (cameratype != null) {
        val value = _directory.getInteger(OlympusMakernoteDirectory.TAG_JPEG_QUALITY) ?: return null
        if (cameratype.startsWith("SX") && !cameratype.startsWith("SX151")
          || cameratype.startsWith("D4322")) {
          when (value) {
            0 -> "Standard Quality (Low)"
            1 -> "High Quality (Normal)"
            2 -> "Super High Quality (Fine)"
            6 -> "RAW"
            else -> "Unknown ($value)"
          }
        } else {
          when (value) {
            0 -> "Standard Quality (Low)"
            1 -> "High Quality (Normal)"
            2 -> "Super High Quality (Fine)"
            4 -> "RAW"
            5 -> "Medium-Fine"
            6 -> "Small-Fine"
            33 -> "Uncompressed"
            else -> "Unknown ($value)"
          }
        }
      } else getIndexedDescription(OlympusMakernoteDirectory.TAG_JPEG_QUALITY,
        1,
        "Standard Quality",
        "High Quality",
        "Super High Quality")
    }

  val specialModeDescription: String?
    get() {
      val values = _directory.getObject(OlympusMakernoteDirectory.TAG_SPECIAL_MODE) as LongArray? ?: return null
      if (values.isEmpty()) return ""
      val desc = StringBuilder()
      when (values[0].toInt()) {
        0 -> desc.append("Normal picture taking mode")
        1 -> desc.append("Unknown picture taking mode")
        2 -> desc.append("Fast picture taking mode")
        3 -> desc.append("Panorama picture taking mode")
        else -> desc.append("Unknown picture taking mode")
      }
      if (values.size >= 2) {
        when (values[1].toInt()) {
          0 -> {
          }
          1 -> desc.append(" / 1st in a sequence")
          2 -> desc.append(" / 2nd in a sequence")
          3 -> desc.append(" / 3rd in a sequence")
          else -> {
            desc.append(" / ")
            desc.append(values[1])
            desc.append("th in a sequence")
          }
        }
      }
      if (values.size >= 3) {
        when (values[2].toInt()) {
          1 -> desc.append(" / Left to right panorama direction")
          2 -> desc.append(" / Right to left panorama direction")
          3 -> desc.append(" / Bottom to top panorama direction")
          4 -> desc.append(" / Top to bottom panorama direction")
        }
      }
      return desc.toString()
    }
}
