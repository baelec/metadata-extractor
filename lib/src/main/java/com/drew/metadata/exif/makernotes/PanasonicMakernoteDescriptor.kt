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

import com.drew.lang.ByteArrayReader
import com.drew.lang.RandomAccessReader
import com.drew.lang.UTF_8
import com.drew.metadata.Face
import com.drew.metadata.TagDescriptor
import java.io.IOException
import java.text.DecimalFormat

/**
 * Provides human-readable string representations of tag values stored in a [PanasonicMakernoteDirectory].
 *
 *
 * Some information about this makernote taken from here:
 *
 *  * [http://www.ozhiker.com/electronics/pjmt/jpeg_info/panasonic_mn.html](http://www.ozhiker.com/electronics/pjmt/jpeg_info/panasonic_mn.html)
 *  * [http://www.sno.phy.queensu.ca/~phil/exiftool/TagNames/Panasonic.html](http://www.sno.phy.queensu.ca/~phil/exiftool/TagNames/Panasonic.html)
 *
 *
 * @author Drew Noakes https://drewnoakes.com
 * @author Philipp Sandhaus
 */
class PanasonicMakernoteDescriptor(directory: PanasonicMakernoteDirectory) : TagDescriptor<PanasonicMakernoteDirectory>(directory) {
  override fun getDescription(tagType: Int): String? {
    return when (tagType) {
      PanasonicMakernoteDirectory.TAG_QUALITY_MODE -> qualityModeDescription
      PanasonicMakernoteDirectory.TAG_FIRMWARE_VERSION -> versionDescription
      PanasonicMakernoteDirectory.TAG_WHITE_BALANCE -> whiteBalanceDescription
      PanasonicMakernoteDirectory.TAG_FOCUS_MODE -> focusModeDescription
      PanasonicMakernoteDirectory.TAG_AF_AREA_MODE -> afAreaModeDescription
      PanasonicMakernoteDirectory.TAG_IMAGE_STABILIZATION -> imageStabilizationDescription
      PanasonicMakernoteDirectory.TAG_MACRO_MODE -> macroModeDescription
      PanasonicMakernoteDirectory.TAG_RECORD_MODE -> recordModeDescription
      PanasonicMakernoteDirectory.TAG_AUDIO -> audioDescription
      PanasonicMakernoteDirectory.TAG_UNKNOWN_DATA_DUMP -> unknownDataDumpDescription
      PanasonicMakernoteDirectory.TAG_COLOR_EFFECT -> colorEffectDescription
      PanasonicMakernoteDirectory.TAG_UPTIME -> uptimeDescription
      PanasonicMakernoteDirectory.TAG_BURST_MODE -> burstModeDescription
      PanasonicMakernoteDirectory.TAG_CONTRAST_MODE -> contrastModeDescription
      PanasonicMakernoteDirectory.TAG_NOISE_REDUCTION -> noiseReductionDescription
      PanasonicMakernoteDirectory.TAG_SELF_TIMER -> selfTimerDescription
      PanasonicMakernoteDirectory.TAG_ROTATION -> rotationDescription
      PanasonicMakernoteDirectory.TAG_AF_ASSIST_LAMP -> afAssistLampDescription
      PanasonicMakernoteDirectory.TAG_COLOR_MODE -> colorModeDescription
      PanasonicMakernoteDirectory.TAG_OPTICAL_ZOOM_MODE -> opticalZoomModeDescription
      PanasonicMakernoteDirectory.TAG_CONVERSION_LENS -> conversionLensDescription
      PanasonicMakernoteDirectory.TAG_CONTRAST -> contrastDescription
      PanasonicMakernoteDirectory.TAG_WORLD_TIME_LOCATION -> worldTimeLocationDescription
      PanasonicMakernoteDirectory.TAG_ADVANCED_SCENE_MODE -> advancedSceneModeDescription
      PanasonicMakernoteDirectory.TAG_FACE_DETECTION_INFO -> detectedFacesDescription
      PanasonicMakernoteDirectory.TAG_TRANSFORM -> transformDescription
      PanasonicMakernoteDirectory.TAG_TRANSFORM_1 -> transform1Description
      PanasonicMakernoteDirectory.TAG_INTELLIGENT_EXPOSURE -> intelligentExposureDescription
      PanasonicMakernoteDirectory.TAG_FLASH_WARNING -> flashWarningDescription
      PanasonicMakernoteDirectory.TAG_COUNTRY -> countryDescription
      PanasonicMakernoteDirectory.TAG_STATE -> stateDescription
      PanasonicMakernoteDirectory.TAG_CITY -> cityDescription
      PanasonicMakernoteDirectory.TAG_LANDMARK -> landmarkDescription
      PanasonicMakernoteDirectory.TAG_INTELLIGENT_RESOLUTION -> intelligentResolutionDescription
      PanasonicMakernoteDirectory.TAG_FACE_RECOGNITION_INFO -> recognizedFacesDescription
      PanasonicMakernoteDirectory.TAG_SCENE_MODE -> sceneModeDescription
      PanasonicMakernoteDirectory.TAG_FLASH_FIRED -> flashFiredDescription
      PanasonicMakernoteDirectory.TAG_TEXT_STAMP -> textStampDescription
      PanasonicMakernoteDirectory.TAG_TEXT_STAMP_1 -> textStamp1Description
      PanasonicMakernoteDirectory.TAG_TEXT_STAMP_2 -> textStamp2Description
      PanasonicMakernoteDirectory.TAG_TEXT_STAMP_3 -> textStamp3Description
      PanasonicMakernoteDirectory.TAG_MAKERNOTE_VERSION -> makernoteVersionDescription
      PanasonicMakernoteDirectory.TAG_EXIF_VERSION -> exifVersionDescription
      PanasonicMakernoteDirectory.TAG_INTERNAL_SERIAL_NUMBER -> internalSerialNumberDescription
      PanasonicMakernoteDirectory.TAG_TITLE -> titleDescription
      PanasonicMakernoteDirectory.TAG_BRACKET_SETTINGS -> bracketSettingsDescription
      PanasonicMakernoteDirectory.TAG_FLASH_CURTAIN -> flashCurtainDescription
      PanasonicMakernoteDirectory.TAG_LONG_EXPOSURE_NOISE_REDUCTION -> longExposureNoiseReductionDescription
      PanasonicMakernoteDirectory.TAG_BABY_NAME -> babyNameDescription
      PanasonicMakernoteDirectory.TAG_LOCATION -> locationDescription
      PanasonicMakernoteDirectory.TAG_LENS_FIRMWARE_VERSION -> lensFirmwareVersionDescription
      PanasonicMakernoteDirectory.TAG_INTELLIGENT_D_RANGE -> intelligentDRangeDescription
      PanasonicMakernoteDirectory.TAG_CLEAR_RETOUCH -> clearRetouchDescription
      PanasonicMakernoteDirectory.TAG_PHOTO_STYLE -> photoStyleDescription
      PanasonicMakernoteDirectory.TAG_SHADING_COMPENSATION -> shadingCompensationDescription
      PanasonicMakernoteDirectory.TAG_ACCELEROMETER_Z -> accelerometerZDescription
      PanasonicMakernoteDirectory.TAG_ACCELEROMETER_X -> accelerometerXDescription
      PanasonicMakernoteDirectory.TAG_ACCELEROMETER_Y -> accelerometerYDescription
      PanasonicMakernoteDirectory.TAG_CAMERA_ORIENTATION -> cameraOrientationDescription
      PanasonicMakernoteDirectory.TAG_ROLL_ANGLE -> rollAngleDescription
      PanasonicMakernoteDirectory.TAG_PITCH_ANGLE -> pitchAngleDescription
      PanasonicMakernoteDirectory.TAG_SWEEP_PANORAMA_DIRECTION -> sweepPanoramaDirectionDescription
      PanasonicMakernoteDirectory.TAG_TIMER_RECORDING -> timerRecordingDescription
      PanasonicMakernoteDirectory.TAG_HDR -> hDRDescription
      PanasonicMakernoteDirectory.TAG_SHUTTER_TYPE -> shutterTypeDescription
      PanasonicMakernoteDirectory.TAG_TOUCH_AE -> touchAeDescription
      PanasonicMakernoteDirectory.TAG_BABY_AGE -> babyAgeDescription
      PanasonicMakernoteDirectory.TAG_BABY_AGE_1 -> babyAge1Description
      else -> super.getDescription(tagType)
    }
  }

  val textStampDescription: String?
    get() = getIndexedDescription(PanasonicMakernoteDirectory.TAG_TEXT_STAMP, 1, "Off", "On")

  val textStamp1Description: String?
    get() = getIndexedDescription(PanasonicMakernoteDirectory.TAG_TEXT_STAMP_1, 1, "Off", "On")

  val textStamp2Description: String?
    get() = getIndexedDescription(PanasonicMakernoteDirectory.TAG_TEXT_STAMP_2, 1, "Off", "On")

  val textStamp3Description: String?
    get() = getIndexedDescription(PanasonicMakernoteDirectory.TAG_TEXT_STAMP_3, 1, "Off", "On")

  val macroModeDescription: String?
    get() = getIndexedDescription(PanasonicMakernoteDirectory.TAG_MACRO_MODE, 1, "Off", "On")

  val flashFiredDescription: String?
    get() = getIndexedDescription(PanasonicMakernoteDirectory.TAG_FLASH_FIRED, 1, "Off", "On")

  val imageStabilizationDescription: String?
    get() = getIndexedDescription(PanasonicMakernoteDirectory.TAG_IMAGE_STABILIZATION,
      2,
      "On, Mode 1",
      "Off",
      "On, Mode 2"
    )

  val audioDescription: String?
    get() = getIndexedDescription(PanasonicMakernoteDirectory.TAG_AUDIO, 1, "Off", "On")

  val transformDescription: String?
    get() = getTransformDescription(PanasonicMakernoteDirectory.TAG_TRANSFORM)

  val transform1Description: String?
    get() = getTransformDescription(PanasonicMakernoteDirectory.TAG_TRANSFORM_1)

  private fun getTransformDescription(tag: Int): String? {
    val values = _directory.getByteArray(tag) ?: return null
    val reader: RandomAccessReader = ByteArrayReader(values)
    return try {
      val val1 = reader.getUInt16(0)
      val val2 = reader.getUInt16(2)
      if (val1 == -1 && val2 == 1) return "Slim Low"
      if (val1 == -3 && val2 == 2) return "Slim High"
      if (val1 == 0 && val2 == 0) return "Off"
      if (val1 == 1 && val2 == 1) return "Stretch Low"
      if (val1 == 3 && val2 == 2) "Stretch High" else "Unknown ($val1 $val2)"
    } catch (e: IOException) {
      null
    }
  }

  val intelligentExposureDescription: String?
    get() = getIndexedDescription(PanasonicMakernoteDirectory.TAG_INTELLIGENT_EXPOSURE,
      "Off", "Low", "Standard", "High")

  val flashWarningDescription: String?
    get() = getIndexedDescription(PanasonicMakernoteDirectory.TAG_FLASH_WARNING,
      "No", "Yes (Flash required but disabled)")

  val countryDescription: String?
    get() = trim(getStringFromBytes(PanasonicMakernoteDirectory.TAG_COUNTRY, UTF_8))

  val stateDescription: String?
    get() = trim(getStringFromBytes(PanasonicMakernoteDirectory.TAG_STATE, UTF_8))

  val cityDescription: String?
    get() = trim(getStringFromBytes(PanasonicMakernoteDirectory.TAG_CITY, UTF_8))

  val landmarkDescription: String?
    get() = trim(getStringFromBytes(PanasonicMakernoteDirectory.TAG_LANDMARK, UTF_8))

  val titleDescription: String?
    get() = trim(getStringFromBytes(PanasonicMakernoteDirectory.TAG_TITLE, UTF_8))

  val bracketSettingsDescription: String?
    get() = getIndexedDescription(PanasonicMakernoteDirectory.TAG_BRACKET_SETTINGS,
      "No Bracket", "3 Images, Sequence 0/-/+", "3 Images, Sequence -/0/+", "5 Images, Sequence 0/-/+",
      "5 Images, Sequence -/0/+", "7 Images, Sequence 0/-/+", "7 Images, Sequence -/0/+")

  val flashCurtainDescription: String?
    get() = getIndexedDescription(PanasonicMakernoteDirectory.TAG_FLASH_CURTAIN,
      "n/a", "1st", "2nd")

  val longExposureNoiseReductionDescription: String?
    get() = getIndexedDescription(PanasonicMakernoteDirectory.TAG_LONG_EXPOSURE_NOISE_REDUCTION, 1,
      "Off", "On")

  // lens version has 4 parts separated by periods
  val lensFirmwareVersionDescription:
    //return string.Join(".", bytes.Select(b => b.ToString()).ToArray());
    String?
    get() { // lens version has 4 parts separated by periods
      val bytes = _directory.getByteArray(PanasonicMakernoteDirectory.TAG_LENS_FIRMWARE_VERSION) ?: return null
      val sb = StringBuilder()
      for (i in bytes.indices) {
        sb.append(bytes[i])
        if (i < bytes.size - 1) sb.append(".")
      }
      return sb.toString()
      //return string.Join(".", bytes.Select(b => b.ToString()).ToArray());
    }

  val intelligentDRangeDescription: String?
    get() = getIndexedDescription(PanasonicMakernoteDirectory.TAG_INTELLIGENT_D_RANGE,
      "Off", "Low", "Standard", "High")

  val clearRetouchDescription: String?
    get() = getIndexedDescription(PanasonicMakernoteDirectory.TAG_CLEAR_RETOUCH,
      "Off", "On")

  val photoStyleDescription: String?
    get() = getIndexedDescription(PanasonicMakernoteDirectory.TAG_PHOTO_STYLE,
      "Auto", "Standard or Custom", "Vivid", "Natural", "Monochrome", "Scenery", "Portrait")

  val shadingCompensationDescription: String?
    get() = getIndexedDescription(PanasonicMakernoteDirectory.TAG_SHADING_COMPENSATION,
      "Off", "On")

  // positive is acceleration upwards
  val accelerometerZDescription: String?
    get() {
      val value = _directory.getInteger(PanasonicMakernoteDirectory.TAG_ACCELEROMETER_Z) ?: return null
      // positive is acceleration upwards
      return value.toShort().toString()
    }

  // positive is acceleration to the left
  val accelerometerXDescription: String?
    get() {
      val value = _directory.getInteger(PanasonicMakernoteDirectory.TAG_ACCELEROMETER_X) ?: return null
      // positive is acceleration to the left
      return value.toShort().toString()
    }

  // positive is acceleration backwards
  val accelerometerYDescription: String?
    get() {
      val value = _directory.getInteger(PanasonicMakernoteDirectory.TAG_ACCELEROMETER_Y) ?: return null
      // positive is acceleration backwards
      return value.toShort().toString()
    }

  val cameraOrientationDescription: String?
    get() = getIndexedDescription(PanasonicMakernoteDirectory.TAG_CAMERA_ORIENTATION,
      "Normal", "Rotate CW", "Rotate 180", "Rotate CCW", "Tilt Upwards", "Tile Downwards")

  // converted to degrees of clockwise camera rotation
  val rollAngleDescription: String?
    get() {
      val value = _directory.getInteger(PanasonicMakernoteDirectory.TAG_ROLL_ANGLE) ?: return null
      val format = DecimalFormat("0.#")
      // converted to degrees of clockwise camera rotation
      return format.format(value.toShort() / 10.0)
    }

  // converted to degrees of upward camera tilt
  val pitchAngleDescription: String?
    get() {
      val value = _directory.getInteger(PanasonicMakernoteDirectory.TAG_PITCH_ANGLE) ?: return null
      val format = DecimalFormat("0.#")
      // converted to degrees of upward camera tilt
      return format.format(-value.toShort() / 10.0)
    }

  val sweepPanoramaDirectionDescription: String?
    get() = getIndexedDescription(PanasonicMakernoteDirectory.TAG_SWEEP_PANORAMA_DIRECTION,
      "Off", "Left to Right", "Right to Left", "Top to Bottom", "Bottom to Top")

  val timerRecordingDescription: String?
    get() = getIndexedDescription(PanasonicMakernoteDirectory.TAG_TIMER_RECORDING,
      "Off", "Time Lapse", "Stop-motion Animation")

  val hDRDescription: String?
    get() {
      val value = _directory.getInteger(PanasonicMakernoteDirectory.TAG_HDR) ?: return null
      return when (value) {
        0 -> "Off"
        100 -> "1 EV"
        200 -> "2 EV"
        300 -> "3 EV"
        32868 -> "1 EV (Auto)"
        32968 -> "2 EV (Auto)"
        33068 -> "3 EV (Auto)"
        else -> "Unknown (%d)".format(value)
      }
    }

  val shutterTypeDescription: String?
    get() = getIndexedDescription(PanasonicMakernoteDirectory.TAG_SHUTTER_TYPE,
      "Mechanical", "Electronic", "Hybrid")

  val touchAeDescription: String?
    get() = getIndexedDescription(PanasonicMakernoteDirectory.TAG_TOUCH_AE,
      "Off", "On")

  val babyNameDescription: String?
    get() = trim(getStringFromBytes(PanasonicMakernoteDirectory.TAG_BABY_NAME, UTF_8))

  val locationDescription: String?
    get() = trim(getStringFromBytes(PanasonicMakernoteDirectory.TAG_LOCATION, UTF_8))

  val intelligentResolutionDescription: String?
    get() = getIndexedDescription(PanasonicMakernoteDirectory.TAG_INTELLIGENT_RESOLUTION,
      "Off", null, "Auto", "On")

  val contrastDescription: String?
    get() = getIndexedDescription(PanasonicMakernoteDirectory.TAG_CONTRAST, "Normal")

  val worldTimeLocationDescription: String?
    get() = getIndexedDescription(PanasonicMakernoteDirectory.TAG_WORLD_TIME_LOCATION,
      1, "Home", "Destination")

  val advancedSceneModeDescription: String?
    get() = getIndexedDescription(PanasonicMakernoteDirectory.TAG_ADVANCED_SCENE_MODE,
      1,
      "Normal",
      "Outdoor/Illuminations/Flower/HDR Art",
      "Indoor/Architecture/Objects/HDR B&W",
      "Creative",
      "Auto",
      null,
      "Expressive",
      "Retro",
      "Pure",
      "Elegant",
      null,
      "Monochrome",
      "Dynamic Art",
      "Silhouette"
    )

  val unknownDataDumpDescription: String?
    get() = getByteLengthDescription(PanasonicMakernoteDirectory.TAG_UNKNOWN_DATA_DUMP)

  val colorEffectDescription: String?
    get() = getIndexedDescription(PanasonicMakernoteDirectory.TAG_COLOR_EFFECT,
      1, "Off", "Warm", "Cool", "Black & White", "Sepia"
    )

  val uptimeDescription: String?
    get() {
      val value = _directory.getInteger(PanasonicMakernoteDirectory.TAG_UPTIME) ?: return null
      return "${(value / 100f)} s"
    }

  val burstModeDescription: String?
    get() = getIndexedDescription(PanasonicMakernoteDirectory.TAG_BURST_MODE,
      "Off", null, "On", "Indefinite", "Unlimited"
    )

  val contrastModeDescription: String?
    get() {
      val value = _directory.getInteger(PanasonicMakernoteDirectory.TAG_CONTRAST_MODE) ?: return null
      return when (value) {
        0x0 -> "Normal"
        0x1 -> "Low"
        0x2 -> "High"
        0x6 -> "Medium Low"
        0x7 -> "Medium High"
        0x100 -> "Low"
        0x110 -> "Normal"
        0x120 -> "High"
        else -> "Unknown ($value)"
      }
    }

  val noiseReductionDescription: String?
    get() = getIndexedDescription(PanasonicMakernoteDirectory.TAG_NOISE_REDUCTION,
      "Standard (0)", "Low (-1)", "High (+1)", "Lowest (-2)", "Highest (+2)"
    )

  val selfTimerDescription: String?
    get() = getIndexedDescription(PanasonicMakernoteDirectory.TAG_SELF_TIMER,
      1, "Off", "10 s", "2 s"
    )

  val rotationDescription: String?
    get() {
      val value = _directory.getInteger(PanasonicMakernoteDirectory.TAG_ROTATION) ?: return null
      return when (value) {
        1 -> "Horizontal"
        3 -> "Rotate 180"
        6 -> "Rotate 90 CW"
        8 -> "Rotate 270 CW"
        else -> "Unknown ($value)"
      }
    }

  val afAssistLampDescription: String?
    get() = getIndexedDescription(PanasonicMakernoteDirectory.TAG_AF_ASSIST_LAMP,
      1, "Fired", "Enabled but not used", "Disabled but required", "Disabled and not required"
    )

  val colorModeDescription: String?
    get() = getIndexedDescription(PanasonicMakernoteDirectory.TAG_COLOR_MODE,
      "Normal", "Natural", "Vivid"
    )

  val opticalZoomModeDescription: String?
    get() = getIndexedDescription(PanasonicMakernoteDirectory.TAG_OPTICAL_ZOOM_MODE,
      1, "Standard", "Extended"
    )

  val conversionLensDescription: String?
    get() = getIndexedDescription(PanasonicMakernoteDirectory.TAG_CONVERSION_LENS,
      1, "Off", "Wide", "Telephoto", "Macro"
    )

  val detectedFacesDescription: String?
    get() = buildFacesDescription(_directory.detectedFaces)

  val recognizedFacesDescription: String?
    get() = buildFacesDescription(_directory.recognizedFaces)

  private fun buildFacesDescription(faces: Array<Face?>?): String? {
    if (faces == null) return null
    val result = StringBuilder()
    for (i in faces.indices) result.append("Face ").append(i + 1).append(": ").append(faces[i].toString()).append("\n")
    return if (result.isNotEmpty()) result.substring(0, result.length - 1) else null
  }

  val recordModeDescription: String?
    get() = getIndexedDescription(PanasonicMakernoteDirectory.TAG_RECORD_MODE, 1, *_sceneModes)

  val sceneModeDescription: String?
    get() = getIndexedDescription(PanasonicMakernoteDirectory.TAG_SCENE_MODE, 1, *_sceneModes)

  val focusModeDescription: String?
    get() = getIndexedDescription(PanasonicMakernoteDirectory.TAG_FOCUS_MODE, 1,
      "Auto", "Manual", null, "Auto, Focus Button", "Auto, Continuous")

  val afAreaModeDescription: String?
    get() {
      val value = _directory.getIntArray(PanasonicMakernoteDirectory.TAG_AF_AREA_MODE)
      return if (value == null || value.size < 2) null else when (value[0]) {
        0 -> when (value[1]) {
          1 -> "Spot Mode On"
          16 -> "Spot Mode Off"
          else -> "Unknown (" + value[0] + " " + value[1] + ")"
        }
        1 -> when (value[1]) {
          0 -> "Spot Focusing"
          1 -> "5-area"
          else -> "Unknown (${value[0]} ${value[1]})"
        }
        16 -> when (value[1]) {
          0 -> "1-area"
          16 -> "1-area (high speed)"
          else -> "Unknown (${value[0]} ${value[1]})"
        }
        32 -> when (value[1]) {
          0 -> "Auto or Face Detect"
          1 -> "3-area (left)"
          2 -> "3-area (center)"
          3 -> "3-area (right)"
          else -> "Unknown (${value[0]} ${value[1]})"
        }
        64 -> "Face Detect"
        else -> "Unknown (${value[0]} ${value[1]})"
      }
    }

  // 2
  // 9
  val qualityModeDescription: String?
    get() = getIndexedDescription(PanasonicMakernoteDirectory.TAG_QUALITY_MODE,
      2,
      "High",  // 2
      "Normal",
      null,
      null,
      "Very High",
      "Raw",
      null,
      "Motion Picture" // 9
    )

  val versionDescription: String?
    get() = getVersionBytesDescription(PanasonicMakernoteDirectory.TAG_FIRMWARE_VERSION, 2)

  val makernoteVersionDescription: String?
    get() = getVersionBytesDescription(PanasonicMakernoteDirectory.TAG_MAKERNOTE_VERSION, 2)

  val exifVersionDescription: String?
    get() = getVersionBytesDescription(PanasonicMakernoteDirectory.TAG_EXIF_VERSION, 2)

  val internalSerialNumberDescription: String?
    get() = get7BitStringFromBytes(PanasonicMakernoteDirectory.TAG_INTERNAL_SERIAL_NUMBER)

  // 1
  // 10
  // 12
  val whiteBalanceDescription: String?
    get() = getIndexedDescription(PanasonicMakernoteDirectory.TAG_WHITE_BALANCE,
      1,
      "Auto",  // 1
      "Daylight",
      "Cloudy",
      "Incandescent",
      "Manual",
      null,
      null,
      "Flash",
      null,
      "Black & White",  // 10
      "Manual",
      "Shade" // 12
    )

  val babyAgeDescription: String?
    get() {
      val age = _directory.getAge(PanasonicMakernoteDirectory.TAG_BABY_AGE)
      return age?.toFriendlyString()
    }

  val babyAge1Description: String?
    get() {
      val age = _directory.getAge(PanasonicMakernoteDirectory.TAG_BABY_AGE_1)
      return age?.toFriendlyString()
    }

  companion object {
    private fun trim(s: String?): String? {
      return s?.trim { it <= ' ' }
    }

    private val _sceneModes = arrayOf(
      "Normal",  // 1
      "Portrait",
      "Scenery",
      "Sports",
      "Night Portrait",
      "Program",
      "Aperture Priority",
      "Shutter Priority",
      "Macro",
      "Spot",  // 10
      "Manual",
      "Movie Preview",
      "Panning",
      "Simple",
      "Color Effects",
      "Self Portrait",
      "Economy",
      "Fireworks",
      "Party",
      "Snow",  // 20
      "Night Scenery",
      "Food",
      "Baby",
      "Soft Skin",
      "Candlelight",
      "Starry Night",
      "High Sensitivity",
      "Panorama Assist",
      "Underwater",
      "Beach",  // 30
      "Aerial Photo",
      "Sunset",
      "Pet",
      "Intelligent ISO",
      "Clipboard",
      "High Speed Continuous Shooting",
      "Intelligent Auto",
      null,
      "Multi-aspect",
      null,  // 40
      "Transform",
      "Flash Burst",
      "Pin Hole",
      "Film Grain",
      "My Color",
      "Photo Frame",
      null,
      null,
      null,
      null,  // 50
      "HDR"
    )
  }
}
