/*
 * Copyright 2002-2015 Drew Noakes
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

import com.drew.metadata.TagDescriptor
import java.text.DecimalFormat
import java.util.*

/**
 * Provides human-readable String representations of tag values stored in a [OlympusCameraSettingsMakernoteDirectory].
 *
 *
 * Some Description functions and the Extender and Lens types lists converted from Exiftool version 10.10 created by Phil Harvey
 * http://www.sno.phy.queensu.ca/~phil/exiftool/
 * lib\Image\ExifTool\Olympus.pm
 *
 * @author Kevin Mott https://github.com/kwhopper
 * @author Drew Noakes https://drewnoakes.com
 */
class OlympusCameraSettingsMakernoteDescriptor(directory: OlympusCameraSettingsMakernoteDirectory) : TagDescriptor<OlympusCameraSettingsMakernoteDirectory>(directory) {
  override fun getDescription(tagType: Int): String? {
    return when (tagType) {
      OlympusCameraSettingsMakernoteDirectory.TagCameraSettingsVersion -> cameraSettingsVersionDescription
      OlympusCameraSettingsMakernoteDirectory.TagPreviewImageValid -> previewImageValidDescription
      OlympusCameraSettingsMakernoteDirectory.TagExposureMode -> exposureModeDescription
      OlympusCameraSettingsMakernoteDirectory.TagAeLock -> aeLockDescription
      OlympusCameraSettingsMakernoteDirectory.TagMeteringMode -> meteringModeDescription
      OlympusCameraSettingsMakernoteDirectory.TagExposureShift -> exposureShiftDescription
      OlympusCameraSettingsMakernoteDirectory.TagNdFilter -> ndFilterDescription
      OlympusCameraSettingsMakernoteDirectory.TagMacroMode -> macroModeDescription
      OlympusCameraSettingsMakernoteDirectory.TagFocusMode -> focusModeDescription
      OlympusCameraSettingsMakernoteDirectory.TagFocusProcess -> focusProcessDescription
      OlympusCameraSettingsMakernoteDirectory.TagAfSearch -> afSearchDescription
      OlympusCameraSettingsMakernoteDirectory.TagAfAreas -> afAreasDescription
      OlympusCameraSettingsMakernoteDirectory.TagAfPointSelected -> afPointSelectedDescription
      OlympusCameraSettingsMakernoteDirectory.TagAfFineTune -> afFineTuneDescription
      OlympusCameraSettingsMakernoteDirectory.TagFlashMode -> flashModeDescription
      OlympusCameraSettingsMakernoteDirectory.TagFlashRemoteControl -> flashRemoteControlDescription
      OlympusCameraSettingsMakernoteDirectory.TagFlashControlMode -> flashControlModeDescription
      OlympusCameraSettingsMakernoteDirectory.TagFlashIntensity -> flashIntensityDescription
      OlympusCameraSettingsMakernoteDirectory.TagManualFlashStrength -> manualFlashStrengthDescription
      OlympusCameraSettingsMakernoteDirectory.TagWhiteBalance2 -> whiteBalance2Description
      OlympusCameraSettingsMakernoteDirectory.TagWhiteBalanceTemperature -> whiteBalanceTemperatureDescription
      OlympusCameraSettingsMakernoteDirectory.TagCustomSaturation -> customSaturationDescription
      OlympusCameraSettingsMakernoteDirectory.TagModifiedSaturation -> modifiedSaturationDescription
      OlympusCameraSettingsMakernoteDirectory.TagContrastSetting -> contrastSettingDescription
      OlympusCameraSettingsMakernoteDirectory.TagSharpnessSetting -> sharpnessSettingDescription
      OlympusCameraSettingsMakernoteDirectory.TagColorSpace -> colorSpaceDescription
      OlympusCameraSettingsMakernoteDirectory.TagSceneMode -> sceneModeDescription
      OlympusCameraSettingsMakernoteDirectory.TagNoiseReduction -> noiseReductionDescription
      OlympusCameraSettingsMakernoteDirectory.TagDistortionCorrection -> distortionCorrectionDescription
      OlympusCameraSettingsMakernoteDirectory.TagShadingCompensation -> shadingCompensationDescription
      OlympusCameraSettingsMakernoteDirectory.TagGradation -> gradationDescription
      OlympusCameraSettingsMakernoteDirectory.TagPictureMode -> pictureModeDescription
      OlympusCameraSettingsMakernoteDirectory.TagPictureModeSaturation -> pictureModeSaturationDescription
      OlympusCameraSettingsMakernoteDirectory.TagPictureModeContrast -> pictureModeContrastDescription
      OlympusCameraSettingsMakernoteDirectory.TagPictureModeSharpness -> pictureModeSharpnessDescription
      OlympusCameraSettingsMakernoteDirectory.TagPictureModeBWFilter -> pictureModeBWFilterDescription
      OlympusCameraSettingsMakernoteDirectory.TagPictureModeTone -> pictureModeToneDescription
      OlympusCameraSettingsMakernoteDirectory.TagNoiseFilter -> noiseFilterDescription
      OlympusCameraSettingsMakernoteDirectory.TagArtFilter -> artFilterDescription
      OlympusCameraSettingsMakernoteDirectory.TagMagicFilter -> magicFilterDescription
      OlympusCameraSettingsMakernoteDirectory.TagPictureModeEffect -> pictureModeEffectDescription
      OlympusCameraSettingsMakernoteDirectory.TagToneLevel -> toneLevelDescription
      OlympusCameraSettingsMakernoteDirectory.TagArtFilterEffect -> artFilterEffectDescription
      OlympusCameraSettingsMakernoteDirectory.TagColorCreatorEffect -> colorCreatorEffectDescription
      OlympusCameraSettingsMakernoteDirectory.TagDriveMode -> driveModeDescription
      OlympusCameraSettingsMakernoteDirectory.TagPanoramaMode -> panoramaModeDescription
      OlympusCameraSettingsMakernoteDirectory.TagImageQuality2 -> imageQuality2Description
      OlympusCameraSettingsMakernoteDirectory.TagImageStabilization -> imageStabilizationDescription
      OlympusCameraSettingsMakernoteDirectory.TagStackedImage -> stackedImageDescription
      OlympusCameraSettingsMakernoteDirectory.TagManometerPressure -> manometerPressureDescription
      OlympusCameraSettingsMakernoteDirectory.TagManometerReading -> manometerReadingDescription
      OlympusCameraSettingsMakernoteDirectory.TagExtendedWBDetect -> extendedWBDetectDescription
      OlympusCameraSettingsMakernoteDirectory.TagRollAngle -> rollAngleDescription
      OlympusCameraSettingsMakernoteDirectory.TagPitchAngle -> pitchAngleDescription
      OlympusCameraSettingsMakernoteDirectory.TagDateTimeUtc -> dateTimeUTCDescription
      else -> super.getDescription(tagType)
    }
  }

  val cameraSettingsVersionDescription: String?
    get() = getVersionBytesDescription(OlympusCameraSettingsMakernoteDirectory.TagCameraSettingsVersion, 4)

  val previewImageValidDescription: String?
    get() = getIndexedDescription(OlympusCameraSettingsMakernoteDirectory.TagPreviewImageValid,
      "No", "Yes")

  val exposureModeDescription: String?
    get() = getIndexedDescription(OlympusCameraSettingsMakernoteDirectory.TagExposureMode, 1,
      "Manual", "Program", "Aperture-priority AE", "Shutter speed priority", "Program-shift")

  val aeLockDescription: String?
    get() = getIndexedDescription(OlympusCameraSettingsMakernoteDirectory.TagAeLock,
      "Off", "On")

  val meteringModeDescription: String?
    get() {
      val value = _directory.getInteger(OlympusCameraSettingsMakernoteDirectory.TagMeteringMode) ?: return null
      return when (value) {
        2 -> "Center-weighted average"
        3 -> "Spot"
        5 -> "ESP"
        261 -> "Pattern+AF"
        515 -> "Spot+Highlight control"
        1027 -> "Spot+Shadow control"
        else -> "Unknown ($value)"
      }
    }

  val exposureShiftDescription: String?
    get() = getRationalOrDoubleString(OlympusCameraSettingsMakernoteDirectory.TagExposureShift)

  val ndFilterDescription: String?
    get() = getIndexedDescription(OlympusCameraSettingsMakernoteDirectory.TagNdFilter, "Off", "On")

  val macroModeDescription: String?
    get() = getIndexedDescription(OlympusCameraSettingsMakernoteDirectory.TagMacroMode, "Off", "On", "Super Macro")

  // check if it's only one value long also
  val focusModeDescription: String?
    get() {
      var values = _directory.getIntArray(OlympusCameraSettingsMakernoteDirectory.TagFocusMode)
      if (values == null) {
        // check if it's only one value long also
        val value = _directory.getInteger(OlympusCameraSettingsMakernoteDirectory.TagFocusMode) ?: return null
        values = intArrayOf(value)
      }
      if (values.isEmpty()) return null
      val sb = StringBuilder()
      when (values[0]) {
        0 -> sb.append("Single AF")
        1 -> sb.append("Sequential shooting AF")
        2 -> sb.append("Continuous AF")
        3 -> sb.append("Multi AF")
        4 -> sb.append("Face detect")
        10 -> sb.append("MF")
        else -> sb.append("Unknown (" + values[0] + ")")
      }
      if (values.size > 1) {
        sb.append("; ")
        val value1 = values[1]
        if (value1 == 0) {
          sb.append("(none)")
        } else {
          if (value1 and 1 > 0) sb.append("S-AF, ")
          if (value1 shr 2 and 1 > 0) sb.append("C-AF, ")
          if (value1 shr 4 and 1 > 0) sb.append("MF, ")
          if (value1 shr 5 and 1 > 0) sb.append("Face detect, ")
          if (value1 shr 6 and 1 > 0) sb.append("Imager AF, ")
          if (value1 shr 7 and 1 > 0) sb.append("Live View Magnification Frame, ")
          if (value1 shr 8 and 1 > 0) sb.append("AF sensor, ")
          sb.setLength(sb.length - 2)
        }
      }
      return sb.toString()
    }

  // check if it's only one value long also
  val focusProcessDescription: String?
    get() {
      var values = _directory.getIntArray(OlympusCameraSettingsMakernoteDirectory.TagFocusProcess)
      if (values == null) { // check if it's only one value long also
        val value = _directory.getInteger(OlympusCameraSettingsMakernoteDirectory.TagFocusProcess) ?: return null
        values = intArrayOf(value)
      }
      if (values.isEmpty()) return null
      val sb = StringBuilder()
      when (values[0]) {
        0 -> sb.append("AF not used")
        1 -> sb.append("AF used")
        else -> sb.append("Unknown (" + values[0] + ")")
      }
      if (values.size > 1) sb.append("; " + values[1])
      return sb.toString()
    }

  val afSearchDescription: String?
    get() = getIndexedDescription(OlympusCameraSettingsMakernoteDirectory.TagAfSearch, "Not Ready", "Ready")

  /// <summary>
  /// coordinates range from 0 to 255
  /// </summary>
  /// <returns></returns>
  val afAreasDescription: String?
    get() {
      val obj = _directory.getObject(OlympusCameraSettingsMakernoteDirectory.TagAfAreas)
      if (obj == null || obj !is LongArray) return null
      val sb = StringBuilder()
      for (point in obj) {
        if (point == 0L) continue
        if (sb.isNotEmpty()) {
          sb.append(", ")
        }
        if (point == 0x36794285L) sb.append("Left ") else if (point == 0x79798585L) sb.append("Center ") else if (point == 0xBD79C985L) sb.append("Right ")
        sb.append("(%d/255,%d/255)-(%d/255,%d/255)".format(
          point shr 24 and 0xFF,
          point shr 16 and 0xFF,
          point shr 8 and 0xFF,
          point and 0xFF))
      }
      return if (sb.isEmpty()) null else sb.toString()
    }

  /// <summary>
  /// coordinates expressed as a percent
  /// </summary>
  /// <returns></returns>
  val afPointSelectedDescription: String?
    get() {
      val values = _directory.getRationalArray(OlympusCameraSettingsMakernoteDirectory.TagAfPointSelected)
        ?: return "n/a"
      if (values.size < 4) return null
      var index = 0
      if (values.size == 5 && values[0].toLong() == 0L) index = 1
      val p1 = (values[index].toDouble() * 100).toInt()
      val p2 = (values[index + 1].toDouble() * 100).toInt()
      val p3 = (values[index + 2].toDouble() * 100).toInt()
      val p4 = (values[index + 3].toDouble() * 100).toInt()
      return if (p1 + p2 + p3 + p4 == 0) "n/a" else "(%d%%,%d%%) (%d%%,%d%%)".format(p1, p2, p3, p4)
    }

  val afFineTuneDescription: String?
    get() = getIndexedDescription(OlympusCameraSettingsMakernoteDirectory.TagAfFineTune, "Off", "On")

  val flashModeDescription: String?
    get() {
      val value = _directory.getInteger(OlympusCameraSettingsMakernoteDirectory.TagFlashMode) ?: return null
      if (value == 0) return "Off"
      val sb = StringBuilder()
      if (value and 1 != 0) sb.append("On, ")
      if (value shr 1 and 1 != 0) sb.append("Fill-in, ")
      if (value shr 2 and 1 != 0) sb.append("Red-eye, ")
      if (value shr 3 and 1 != 0) sb.append("Slow-sync, ")
      if (value shr 4 and 1 != 0) sb.append("Forced On, ")
      if (value shr 5 and 1 != 0) sb.append("2nd Curtain, ")
      return sb.substring(0, sb.length - 2)
    }

  val flashRemoteControlDescription: String?
    get() {
      val value = _directory.getInteger(OlympusCameraSettingsMakernoteDirectory.TagFlashRemoteControl) ?: return null
      return when (value) {
        0 -> "Off"
        0x01 -> "Channel 1, Low"
        0x02 -> "Channel 2, Low"
        0x03 -> "Channel 3, Low"
        0x04 -> "Channel 4, Low"
        0x09 -> "Channel 1, Mid"
        0x0a -> "Channel 2, Mid"
        0x0b -> "Channel 3, Mid"
        0x0c -> "Channel 4, Mid"
        0x11 -> "Channel 1, High"
        0x12 -> "Channel 2, High"
        0x13 -> "Channel 3, High"
        0x14 -> "Channel 4, High"
        else -> "Unknown ($value)"
      }
    }

  /// <summary>
  /// 3 or 4 values
  /// </summary>
  /// <returns></returns>
  val flashControlModeDescription: String?
    get() {
      val values = _directory.getIntArray(OlympusCameraSettingsMakernoteDirectory.TagFlashControlMode) ?: return null
      if (values.isEmpty()) return null
      val sb = StringBuilder()
      when (values[0]) {
        0 -> sb.append("Off")
        3 -> sb.append("TTL")
        4 -> sb.append("Auto")
        5 -> sb.append("Manual")
        else -> sb.append("Unknown (").append(values[0]).append(")")
      }
      for (i in 1 until values.size) sb.append("; ").append(values[i])
      return sb.toString()
    }

  /// <summary>
/// 3 or 4 values
/// </summary>
/// <returns></returns>
  val flashIntensityDescription: String?
    get() {
      val values = _directory.getRationalArray(OlympusCameraSettingsMakernoteDirectory.TagFlashIntensity)
      if (values == null || values.isEmpty()) return null
      if (values.size == 3) {
        if (values[0].denominator == 0L && values[1].denominator == 0L && values[2].denominator == 0L) return "n/a"
      } else if (values.size == 4) {
        if (values[0].denominator == 0L && values[1].denominator == 0L && values[2].denominator == 0L && values[3].denominator == 0L) return "n/a (x4)"
      }
      val sb = StringBuilder()
      for (t in values) sb.append(t).append(", ")
      return sb.substring(0, sb.length - 2)
    }

  val manualFlashStrengthDescription: String?
    get() {
      val values = _directory.getRationalArray(OlympusCameraSettingsMakernoteDirectory.TagManualFlashStrength)
      if (values == null || values.isEmpty()) return "n/a"
      if (values.size == 3) {
        if (values[0].denominator == 0L && values[1].denominator == 0L && values[2].denominator == 0L) return "n/a"
      } else if (values.size == 4) {
        if (values[0].denominator == 0L && values[1].denominator == 0L && values[2].denominator == 0L && values[3].denominator == 0L) return "n/a (x4)"
      }
      val sb = StringBuilder()
      for (t in values) sb.append(t).append(", ")
      return sb.substring(0, sb.length - 2)
    }

  val whiteBalance2Description: String?
    get() {
      val value = _directory.getInteger(OlympusCameraSettingsMakernoteDirectory.TagWhiteBalance2) ?: return null
      return when (value) {
        0 -> "Auto"
        1 -> "Auto (Keep Warm Color Off)"
        16 -> "7500K (Fine Weather with Shade)"
        17 -> "6000K (Cloudy)"
        18 -> "5300K (Fine Weather)"
        20 -> "3000K (Tungsten light)"
        21 -> "3600K (Tungsten light-like)"
        22 -> "Auto Setup"
        23 -> "5500K (Flash)"
        33 -> "6600K (Daylight fluorescent)"
        34 -> "4500K (Neutral white fluorescent)"
        35 -> "4000K (Cool white fluorescent)"
        36 -> "White Fluorescent"
        48 -> "3600K (Tungsten light-like)"
        67 -> "Underwater"
        256 -> "One Touch WB 1"
        257 -> "One Touch WB 2"
        258 -> "One Touch WB 3"
        259 -> "One Touch WB 4"
        512 -> "Custom WB 1"
        513 -> "Custom WB 2"
        514 -> "Custom WB 3"
        515 -> "Custom WB 4"
        else -> "Unknown ($value)"
      }
    }

  val whiteBalanceTemperatureDescription: String?
    get() {
      val value = _directory.getInteger(OlympusCameraSettingsMakernoteDirectory.TagWhiteBalanceTemperature)
        ?: return null
      return if (value == 0) "Auto" else value.toString()
    }

  // TODO: if model is /^E-1\b/  then
// $a-=$b; $c-=$b;
// return "CS$a (min CS0, max CS$c)"
  val customSaturationDescription: String?
    get() =// TODO: if model is /^E-1\b/  then
// $a-=$b; $c-=$b;
// return "CS$a (min CS0, max CS$c)"
      getValueMinMaxDescription(OlympusCameraSettingsMakernoteDirectory.TagCustomSaturation)

  val modifiedSaturationDescription: String?
    get() = getIndexedDescription(OlympusCameraSettingsMakernoteDirectory.TagModifiedSaturation,
      "Off", "CM1 (Red Enhance)", "CM2 (Green Enhance)", "CM3 (Blue Enhance)", "CM4 (Skin Tones)")

  val contrastSettingDescription: String?
    get() = getValueMinMaxDescription(OlympusCameraSettingsMakernoteDirectory.TagContrastSetting)

  val sharpnessSettingDescription: String?
    get() = getValueMinMaxDescription(OlympusCameraSettingsMakernoteDirectory.TagSharpnessSetting)

  val colorSpaceDescription: String?
    get() = getIndexedDescription(OlympusCameraSettingsMakernoteDirectory.TagColorSpace,
      "sRGB", "Adobe RGB", "Pro Photo RGB")

  val sceneModeDescription: String?
    get() {
      val value = _directory.getInteger(OlympusCameraSettingsMakernoteDirectory.TagSceneMode) ?: return null
      return when (value) {
        0 -> "Standard"
        6 -> "Auto"
        7 -> "Sport"
        8 -> "Portrait"
        9 -> "Landscape+Portrait"
        10 -> "Landscape"
        11 -> "Night Scene"
        12 -> "Self Portrait"
        13 -> "Panorama"
        14 -> "2 in 1"
        15 -> "Movie"
        16 -> "Landscape+Portrait"
        17 -> "Night+Portrait"
        18 -> "Indoor"
        19 -> "Fireworks"
        20 -> "Sunset"
        21 -> "Beauty Skin"
        22 -> "Macro"
        23 -> "Super Macro"
        24 -> "Food"
        25 -> "Documents"
        26 -> "Museum"
        27 -> "Shoot & Select"
        28 -> "Beach & Snow"
        29 -> "Self Portrait+Timer"
        30 -> "Candle"
        31 -> "Available Light"
        32 -> "Behind Glass"
        33 -> "My Mode"
        34 -> "Pet"
        35 -> "Underwater Wide1"
        36 -> "Underwater Macro"
        37 -> "Shoot & Select1"
        38 -> "Shoot & Select2"
        39 -> "High Key"
        40 -> "Digital Image Stabilization"
        41 -> "Auction"
        42 -> "Beach"
        43 -> "Snow"
        44 -> "Underwater Wide2"
        45 -> "Low Key"
        46 -> "Children"
        47 -> "Vivid"
        48 -> "Nature Macro"
        49 -> "Underwater Snapshot"
        50 -> "Shooting Guide"
        54 -> "Face Portrait"
        57 -> "Bulb"
        59 -> "Smile Shot"
        60 -> "Quick Shutter"
        63 -> "Slow Shutter"
        64 -> "Bird Watching"
        65 -> "Multiple Exposure"
        66 -> "e-Portrait"
        67 -> "Soft Background Shot"
        142 -> "Hand-held Starlight"
        154 -> "HDR"
        else -> "Unknown ($value)"
      }
    }

  val noiseReductionDescription: String?
    get() {
      val value = _directory.getInteger(OlympusCameraSettingsMakernoteDirectory.TagNoiseReduction) ?: return null
      if (value == 0) return "(none)"
      val sb = StringBuilder()
      if (value and 1 != 0) sb.append("Noise Reduction, ")
      if (value shr 1 and 1 != 0) sb.append("Noise Filter, ")
      if (value shr 2 and 1 != 0) sb.append("Noise Filter (ISO Boost), ")
      if (value shr 3 and 1 != 0) sb.append("Auto, ")
      return if (sb.isNotEmpty()) sb.substring(0, sb.length - 2) else "(none)"
    }

  val distortionCorrectionDescription: String?
    get() = getIndexedDescription(OlympusCameraSettingsMakernoteDirectory.TagDistortionCorrection, "Off", "On")

  val shadingCompensationDescription: String?
    get() = getIndexedDescription(OlympusCameraSettingsMakernoteDirectory.TagShadingCompensation, "Off", "On")

  /// <summary>
/// 3 or 4 values
/// </summary>
/// <returns></returns>
  val gradationDescription: String?
    get() {
      val values = _directory.getIntArray(OlympusCameraSettingsMakernoteDirectory.TagGradation)
      if (values == null || values.size < 3) return null
      val join = "%d %d %d".format(values[0], values[1], values[2])
      var ret: String
      ret = when (join) {
        "0 0 0" -> "n/a"
        "-1 -1 1" -> "Low Key"
        "0 -1 1" -> "Normal"
        "1 -1 1" -> "High Key"
        else -> "Unknown ($join)"
      }
      if (values.size > 3) {
        if (values[3] == 0) ret += "; User-Selected" else if (values[3] == 1) ret += "; Auto-Override"
      }
      return ret
    }// check if it's only one value long also

  /// <summary>
/// 1 or 2 values
/// </summary>
/// <returns></returns>
  val pictureModeDescription: String?
    get() {
      var values = _directory.getIntArray(OlympusCameraSettingsMakernoteDirectory.TagPictureMode)
      if (values == null) { // check if it's only one value long also
        val value = _directory.getInteger(OlympusCameraSettingsMakernoteDirectory.TagNoiseReduction) ?: return null
        values = intArrayOf(value)
      }
      if (values.isEmpty()) return null
      val sb = StringBuilder()
      when (values[0]) {
        1 -> sb.append("Vivid")
        2 -> sb.append("Natural")
        3 -> sb.append("Muted")
        4 -> sb.append("Portrait")
        5 -> sb.append("i-Enhance")
        256 -> sb.append("Monotone")
        512 -> sb.append("Sepia")
        else -> sb.append("Unknown (").append(values[0]).append(")")
      }
      if (values.size > 1) sb.append("; ").append(values[1])
      return sb.toString()
    }

  val pictureModeSaturationDescription: String?
    get() = getValueMinMaxDescription(OlympusCameraSettingsMakernoteDirectory.TagPictureModeSaturation)

  val pictureModeContrastDescription: String?
    get() = getValueMinMaxDescription(OlympusCameraSettingsMakernoteDirectory.TagPictureModeContrast)

  val pictureModeSharpnessDescription: String?
    get() = getValueMinMaxDescription(OlympusCameraSettingsMakernoteDirectory.TagPictureModeSharpness)

  val pictureModeBWFilterDescription: String?
    get() = getIndexedDescription(OlympusCameraSettingsMakernoteDirectory.TagPictureModeBWFilter,
      "n/a", "Neutral", "Yellow", "Orange", "Red", "Green")

  val pictureModeToneDescription: String?
    get() = getIndexedDescription(OlympusCameraSettingsMakernoteDirectory.TagPictureModeTone,
      "n/a", "Neutral", "Sepia", "Blue", "Purple", "Green")

  val noiseFilterDescription: String?
    get() {
      val values = _directory.getIntArray(OlympusCameraSettingsMakernoteDirectory.TagNoiseFilter) ?: return null
      val join = String.format("%d %d %d", values[0], values[1], values[2])
      if (join == "0 0 0") return "n/a"
      if (join == "-2 -2 1") return "Off"
      if (join == "-1 -2 1") return "Low"
      if (join == "0 -2 1") return "Standard"
      return if (join == "1 -2 1") "High" else "Unknown ($join)"
    }

  val artFilterDescription: String?
    get() = getFiltersDescription(OlympusCameraSettingsMakernoteDirectory.TagArtFilter)

  val magicFilterDescription: String?
    get() = getFiltersDescription(OlympusCameraSettingsMakernoteDirectory.TagMagicFilter)

  val pictureModeEffectDescription: String?
    get() {
      val values = _directory.getIntArray(OlympusCameraSettingsMakernoteDirectory.TagPictureModeEffect) ?: return null
      val key = "%d %d %d".format(values[0], values[1], values[2])
      if (key == "0 0 0") return "n/a"
      if (key == "-1 -1 1") return "Low"
      if (key == "0 -1 1") return "Standard"
      return if (key == "1 -1 1") "High" else "Unknown ($key)"
    }

  val toneLevelDescription: String?
    get() {
      val values = _directory.getIntArray(OlympusCameraSettingsMakernoteDirectory.TagToneLevel)
      if (values == null || values.isEmpty()) return null
      val sb = StringBuilder()
      for (i in values.indices) {
        if (i == 0 || i == 4 || i == 8 || i == 12 || i == 16 || i == 20 || i == 24) sb.append(_toneLevelType[values[i]]).append("; ") else sb.append(values[i]).append("; ")
      }
      return sb.substring(0, sb.length - 2)
    }

  val artFilterEffectDescription: String?
    get() {
      val values = _directory.getIntArray(OlympusCameraSettingsMakernoteDirectory.TagArtFilterEffect) ?: return null
      val sb = StringBuilder()
      for (i in values.indices) {
        if (i == 0) {
          sb.append(if (_filters.containsKey(values[i])) _filters[values[i]] else "[unknown]").append("; ")
        } else if (i == 3) {
          sb.append("Partial Color ").append(values[i]).append("; ")
        } else if (i == 4) {
          when (values[i]) {
            0x0000 -> sb.append("No Effect")
            0x8010 -> sb.append("Star Light")
            0x8020 -> sb.append("Pin Hole")
            0x8030 -> sb.append("Frame")
            0x8040 -> sb.append("Soft Focus")
            0x8050 -> sb.append("White Edge")
            0x8060 -> sb.append("B&W")
            else -> sb.append("Unknown (").append(values[i]).append(")")
          }
          sb.append("; ")
        } else if (i == 6) {
          when (values[i]) {
            0 -> sb.append("No Color Filter")
            1 -> sb.append("Yellow Color Filter")
            2 -> sb.append("Orange Color Filter")
            3 -> sb.append("Red Color Filter")
            4 -> sb.append("Green Color Filter")
            else -> sb.append("Unknown (").append(values[i]).append(")")
          }
          sb.append("; ")
        } else {
          sb.append(values[i]).append("; ")
        }
      }
      return sb.substring(0, sb.length - 2)
    }

  val colorCreatorEffectDescription: String?
    get() {
      val values = _directory.getIntArray(OlympusCameraSettingsMakernoteDirectory.TagColorCreatorEffect)
        ?: return null
      val sb = StringBuilder()
      for (i in values.indices) {
        when (i) {
          0 -> sb.append("Color ").append(values[i]).append("; ")
          3 -> sb.append("Strength ").append(values[i]).append("; ")
          else -> sb.append(values[i]).append("; ")
        }
      }
      return sb.substring(0, sb.length - 2)
    }

  /// <summary>
  /// 2 or 3 numbers: 1. Mode, 2. Shot number, 3. Mode bits
  /// </summary>
  /// <returns></returns>
  val driveModeDescription: String?
    get() {
      val values = _directory.getIntArray(OlympusCameraSettingsMakernoteDirectory.TagDriveMode) ?: return null
      if (values.isEmpty() || values[0] == 0) return "Single Shot"
      val a = StringBuilder()
      if (values[0] == 5 && values.size >= 3) {
        val c = values[2]
        if (c and 1 > 0) a.append("AE")
        if (c shr 1 and 1 > 0) a.append("WB")
        if (c shr 2 and 1 > 0) a.append("FL")
        if (c shr 3 and 1 > 0) a.append("MF")
        if (c shr 6 and 1 > 0) a.append("Focus")
        a.append(" Bracketing")
      } else {
        when (values[0]) {
          1 -> a.append("Continuous Shooting")
          2 -> a.append("Exposure Bracketing")
          3 -> a.append("White Balance Bracketing")
          4 -> a.append("Exposure+WB Bracketing")
          else -> a.append("Unknown (").append(values[0]).append(")")
        }
      }
      a.append(", Shot ").append(values[1])
      return a.toString()
    }

  /// <summary>
  /// 2 numbers: 1. Mode, 2. Shot number
  /// </summary>
  /// <returns></returns>
  val panoramaModeDescription: String?
    get() {
      val values = _directory.getIntArray(OlympusCameraSettingsMakernoteDirectory.TagPanoramaMode) ?: return null
      if (values.isEmpty() || values[0] == 0) return "Off"
      val a: String
      a = when (values[0]) {
        1 -> "Left to Right"
        2 -> "Right to Left"
        3 -> "Bottom to Top"
        4 -> "Top to Bottom"
        else -> "Unknown (${values[0]})"
      }
      return String.format("%s, Shot %d", a, values[1])
    }

  val imageQuality2Description: String?
    get() = getIndexedDescription(OlympusCameraSettingsMakernoteDirectory.TagImageQuality2, 1,
      "SQ", "HQ", "SHQ", "RAW", "SQ (5)")

  val imageStabilizationDescription: String?
    get() = getIndexedDescription(OlympusCameraSettingsMakernoteDirectory.TagImageStabilization,
      "Off", "On, Mode 1", "On, Mode 2", "On, Mode 3", "On, Mode 4")

  val stackedImageDescription: String?
    get() {
      val values = _directory.getIntArray(OlympusCameraSettingsMakernoteDirectory.TagStackedImage)
      if (values == null || values.size < 2) return null
      val v1 = values[0]
      val v2 = values[1]
      if (v1 == 0 && v2 == 0) return "No"
      return if (v1 == 9 && v2 == 8) "Focus-stacked (8 images)" else "Unknown (%d %d)".format(v1, v2)
    }

  /// <remarks>
  /// TODO: need better image examples to test this function
  /// </remarks>
  /// <returns></returns>
  val manometerPressureDescription: String?
    get() {
      val value = _directory.getInteger(OlympusCameraSettingsMakernoteDirectory.TagManometerPressure) ?: return null
      return "%s kPa".format(DecimalFormat("#.##").format(value / 10.0))
    }

  /// <remarks>
  /// TODO: need better image examples to test this function
  /// </remarks>
  /// <returns></returns>
  val manometerReadingDescription: String?
    get() {
      val values = _directory.getIntArray(OlympusCameraSettingsMakernoteDirectory.TagManometerReading)
      if (values == null || values.size < 2) return null
      val format = DecimalFormat("#.##")
      return "%s m, %s ft".format(
        format.format(values[0] / 10.0),
        format.format(values[1] / 10.0))
    }

  val extendedWBDetectDescription: String?
    get() = getIndexedDescription(OlympusCameraSettingsMakernoteDirectory.TagExtendedWBDetect, "Off", "On")

  /// <summary>
  /// converted to degrees of clockwise camera rotation
  /// </summary>
  /// <remarks>
  /// TODO: need better image examples to test this function
  /// </remarks>
  /// <returns></returns>
  val rollAngleDescription: String?
    get() {
      val values = _directory.getIntArray(OlympusCameraSettingsMakernoteDirectory.TagRollAngle)
      if (values == null || values.size < 2) return null
      val ret = if (values[0] != 0) (-values[0] / 10.0).toString() else "n/a"
      return "%s %d".format(ret, values[1])
    }
  // (second value is 0 if level gauge is off)

  /// <summary>
  /// converted to degrees of upward camera tilt
  /// </summary>
  /// <remarks>
  /// TODO: need better image examples to test this function
  /// </remarks>
  /// <returns></returns>
  val pitchAngleDescription: String?
    get() {
      val values = _directory.getIntArray(OlympusCameraSettingsMakernoteDirectory.TagPitchAngle)
      if (values == null || values.size < 2) return null
      // (second value is 0 if level gauge is off)
      val ret = if (values[0] != 0) (values[0] / 10.0).toString() else "n/a"
      return "%s %d".format(ret, values[1])
    }

  val dateTimeUTCDescription: String?
    get() {
      val value = _directory.getObject(OlympusCameraSettingsMakernoteDirectory.TagDateTimeUtc) ?: return null
      return value.toString()
    }

  private fun getValueMinMaxDescription(tagId: Int): String? {
    val values = _directory.getIntArray(tagId)
    return if (values == null || values.size < 3) null else "%d (min %d, max %d)".format(values[0], values[1], values[2])
  }

  private fun getFiltersDescription(tagId: Int): String? {
    val values = _directory.getIntArray(tagId)
    if (values == null || values.isEmpty()) return null
    val sb = StringBuilder()
    for (i in values.indices) {
      if (i == 0) sb.append(if (_filters.containsKey(values[i])) _filters[values[i]] else "[unknown]") else sb.append(values[i])
      sb.append("; ")
    }
    return sb.substring(0, sb.length - 2)
  }

  companion object {
    private val _toneLevelType = HashMap<Int, String>()
    // ArtFilter, ArtFilterEffect and MagicFilter values
    private val _filters = HashMap<Int, String>()

    init {
      _filters[0] = "Off"
      _filters[1] = "Soft Focus"
      _filters[2] = "Pop Art"
      _filters[3] = "Pale & Light Color"
      _filters[4] = "Light Tone"
      _filters[5] = "Pin Hole"
      _filters[6] = "Grainy Film"
      _filters[9] = "Diorama"
      _filters[10] = "Cross Process"
      _filters[12] = "Fish Eye"
      _filters[13] = "Drawing"
      _filters[14] = "Gentle Sepia"
      _filters[15] = "Pale & Light Color II"
      _filters[16] = "Pop Art II"
      _filters[17] = "Pin Hole II"
      _filters[18] = "Pin Hole III"
      _filters[19] = "Grainy Film II"
      _filters[20] = "Dramatic Tone"
      _filters[21] = "Punk"
      _filters[22] = "Soft Focus 2"
      _filters[23] = "Sparkle"
      _filters[24] = "Watercolor"
      _filters[25] = "Key Line"
      _filters[26] = "Key Line II"
      _filters[27] = "Miniature"
      _filters[28] = "Reflection"
      _filters[29] = "Fragmented"
      _filters[31] = "Cross Process II"
      _filters[32] = "Dramatic Tone II"
      _filters[33] = "Watercolor I"
      _filters[34] = "Watercolor II"
      _filters[35] = "Diorama II"
      _filters[36] = "Vintage"
      _filters[37] = "Vintage II"
      _filters[38] = "Vintage III"
      _filters[39] = "Partial Color"
      _filters[40] = "Partial Color II"
      _filters[41] = "Partial Color III"
      _toneLevelType[0] = "0"
      _toneLevelType[-31999] = "Highlights "
      _toneLevelType[-31998] = "Shadows "
      _toneLevelType[-31997] = "Midtones "
    }
  }
}
