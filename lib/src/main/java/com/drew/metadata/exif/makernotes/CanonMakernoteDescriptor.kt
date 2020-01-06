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

import com.drew.metadata.TagDescriptor
import com.drew.metadata.exif.makernotes.CanonMakernoteDirectory.AFInfo
import com.drew.metadata.exif.makernotes.CanonMakernoteDirectory.FocalLength
import java.text.DecimalFormat
import java.util.*
import kotlin.math.exp
import kotlin.math.ln

/**
 * Provides human-readable string representations of tag values stored in a [CanonMakernoteDirectory].
 *
 * @author Drew Noakes https://drewnoakes.com
 */
class CanonMakernoteDescriptor(directory: CanonMakernoteDirectory) : TagDescriptor<CanonMakernoteDirectory>(directory) {
  override fun getDescription(tagType: Int): String? {
    return when (tagType) {
      CanonMakernoteDirectory.TAG_CANON_SERIAL_NUMBER -> serialNumberDescription
      CanonMakernoteDirectory.CameraSettings.TAG_FLASH_ACTIVITY -> flashActivityDescription
      CanonMakernoteDirectory.CameraSettings.TAG_FOCUS_TYPE -> focusTypeDescription
      CanonMakernoteDirectory.CameraSettings.TAG_DIGITAL_ZOOM -> digitalZoomDescription
      CanonMakernoteDirectory.CameraSettings.TAG_RECORD_MODE -> recordModeDescription
      CanonMakernoteDirectory.CameraSettings.TAG_QUALITY -> qualityDescription
      CanonMakernoteDirectory.CameraSettings.TAG_MACRO_MODE -> macroModeDescription
      CanonMakernoteDirectory.CameraSettings.TAG_SELF_TIMER_DELAY -> selfTimerDelayDescription
      CanonMakernoteDirectory.CameraSettings.TAG_FLASH_MODE -> flashModeDescription
      CanonMakernoteDirectory.CameraSettings.TAG_CONTINUOUS_DRIVE_MODE -> continuousDriveModeDescription
      CanonMakernoteDirectory.CameraSettings.TAG_FOCUS_MODE_1 -> focusMode1Description
      CanonMakernoteDirectory.CameraSettings.TAG_IMAGE_SIZE -> imageSizeDescription
      CanonMakernoteDirectory.CameraSettings.TAG_EASY_SHOOTING_MODE -> easyShootingModeDescription
      CanonMakernoteDirectory.CameraSettings.TAG_CONTRAST -> contrastDescription
      CanonMakernoteDirectory.CameraSettings.TAG_SATURATION -> saturationDescription
      CanonMakernoteDirectory.CameraSettings.TAG_SHARPNESS -> sharpnessDescription
      CanonMakernoteDirectory.CameraSettings.TAG_ISO -> isoDescription
      CanonMakernoteDirectory.CameraSettings.TAG_METERING_MODE -> meteringModeDescription
      CanonMakernoteDirectory.CameraSettings.TAG_AF_POINT_SELECTED -> afPointSelectedDescription
      CanonMakernoteDirectory.CameraSettings.TAG_EXPOSURE_MODE -> exposureModeDescription
      CanonMakernoteDirectory.CameraSettings.TAG_LENS_TYPE -> lensTypeDescription
      CanonMakernoteDirectory.CameraSettings.TAG_LONG_FOCAL_LENGTH -> longFocalLengthDescription
      CanonMakernoteDirectory.CameraSettings.TAG_SHORT_FOCAL_LENGTH -> shortFocalLengthDescription
      CanonMakernoteDirectory.CameraSettings.TAG_FOCAL_UNITS_PER_MM -> focalUnitsPerMillimetreDescription
      CanonMakernoteDirectory.CameraSettings.TAG_FLASH_DETAILS -> flashDetailsDescription
      CanonMakernoteDirectory.CameraSettings.TAG_FOCUS_MODE_2 -> focusMode2Description
      FocalLength.TAG_WHITE_BALANCE -> whiteBalanceDescription
      FocalLength.TAG_AF_POINT_USED -> afPointUsedDescription
      FocalLength.TAG_FLASH_BIAS -> flashBiasDescription
      AFInfo.TAG_AF_POINTS_IN_FOCUS -> tagAfPointsInFocus
      CanonMakernoteDirectory.CameraSettings.TAG_MAX_APERTURE -> maxApertureDescription
      CanonMakernoteDirectory.CameraSettings.TAG_MIN_APERTURE -> minApertureDescription
      CanonMakernoteDirectory.CameraSettings.TAG_FOCUS_CONTINUOUS -> focusContinuousDescription
      CanonMakernoteDirectory.CameraSettings.TAG_AE_SETTING -> aESettingDescription
      CanonMakernoteDirectory.CameraSettings.TAG_DISPLAY_APERTURE -> displayApertureDescription
      CanonMakernoteDirectory.CameraSettings.TAG_SPOT_METERING_MODE -> spotMeteringModeDescription
      CanonMakernoteDirectory.CameraSettings.TAG_PHOTO_EFFECT -> photoEffectDescription
      CanonMakernoteDirectory.CameraSettings.TAG_MANUAL_FLASH_OUTPUT -> manualFlashOutputDescription
      CanonMakernoteDirectory.CameraSettings.TAG_COLOR_TONE -> colorToneDescription
      CanonMakernoteDirectory.CameraSettings.TAG_SRAW_QUALITY -> sRawQualityDescription
      else -> super.getDescription(tagType)
    }
  }

  // http://www.ozhiker.com/electronics/pjmt/jpeg_info/canon_mn.html
  val serialNumberDescription: String?
    get() {
      // http://www.ozhiker.com/electronics/pjmt/jpeg_info/canon_mn.html
      val value = _directory.getInteger(CanonMakernoteDirectory.TAG_CANON_SERIAL_NUMBER) ?: return null
      return "%04X%05d".format(value shr 8 and 0xFF, value and 0xFF)
    }// this tag is interesting in that the values returned are:
//  0, 0.375, 0.5, 0.626, 1
// not
//  0, 0.33,  0.5, 0.66,  1

  /*
    @Nullable
    public String getLongExposureNoiseReductionDescription()
    {
        Integer value = _directory.getInteger(TAG_CANON_CUSTOM_FUNCTION_LONG_EXPOSURE_NOISE_REDUCTION);
        if (value==null)
            return null;
        switch (value) {
            case 0:     return "Off";
            case 1:     return "On";
            default:    return "Unknown (" + value + ")";
        }
    }

    @Nullable
    public String getShutterAutoExposureLockButtonDescription()
    {
        Integer value = _directory.getInteger(TAG_CANON_CUSTOM_FUNCTION_SHUTTER_AUTO_EXPOSURE_LOCK_BUTTONS);
        if (value==null)
            return null;
        switch (value) {
            case 0:     return "AF/AE lock";
            case 1:     return "AE lock/AF";
            case 2:     return "AF/AF lock";
            case 3:     return "AE+release/AE+AF";
            default:    return "Unknown (" + value + ")";
        }
    }

    @Nullable
    public String getMirrorLockupDescription()
    {
        Integer value = _directory.getInteger(TAG_CANON_CUSTOM_FUNCTION_MIRROR_LOCKUP);
        if (value==null)
            return null;
        switch (value) {
            case 0:     return "Disabled";
            case 1:     return "Enabled";
            default:    return "Unknown (" + value + ")";
        }
    }

    @Nullable
    public String getTvAndAvExposureLevelDescription()
    {
        Integer value = _directory.getInteger(TAG_CANON_CUSTOM_FUNCTION_TV_AV_AND_EXPOSURE_LEVEL);
        if (value==null)
            return null;
        switch (value) {
            case 0:     return "1/2 stop";
            case 1:     return "1/3 stop";
            default:    return "Unknown (" + value + ")";
        }
    }

    @Nullable
    public String getAutoFocusAssistLightDescription()
    {
        Integer value = _directory.getInteger(TAG_CANON_CUSTOM_FUNCTION_AF_ASSIST_LIGHT);
        if (value==null)
            return null;
        switch (value) {
            case 0:     return "On (Auto)";
            case 1:     return "Off";
            default:    return "Unknown (" + value + ")";
        }
    }

    @Nullable
    public String getShutterSpeedInAvModeDescription()
    {
        Integer value = _directory.getInteger(TAG_CANON_CUSTOM_FUNCTION_SHUTTER_SPEED_IN_AV_MODE);
        if (value==null)
            return null;
        switch (value) {
            case 0:     return "Automatic";
            case 1:     return "1/200 (fixed)";
            default:    return "Unknown (" + value + ")";
        }
    }

    @Nullable
    public String getAutoExposureBracketingSequenceAndAutoCancellationDescription()
    {
        Integer value = _directory.getInteger(TAG_CANON_CUSTOM_FUNCTION_BRACKETING);
        if (value==null)
            return null;
        switch (value) {
            case 0:     return "0,-,+ / Enabled";
            case 1:     return "0,-,+ / Disabled";
            case 2:     return "-,0,+ / Enabled";
            case 3:     return "-,0,+ / Disabled";
            default:    return "Unknown (" + value + ")";
        }
    }

    @Nullable
    public String getShutterCurtainSyncDescription()
    {
        Integer value = _directory.getInteger(TAG_CANON_CUSTOM_FUNCTION_SHUTTER_CURTAIN_SYNC);
        if (value==null)
            return null;
        switch (value) {
            case 0:     return "1st Curtain Sync";
            case 1:     return "2nd Curtain Sync";
            default:    return "Unknown (" + value + ")";
        }
    }

    @Nullable
    public String getLensAutoFocusStopButtonDescription()
    {
        Integer value = _directory.getInteger(TAG_CANON_CUSTOM_FUNCTION_AF_STOP);
        if (value==null)
            return null;
        switch (value) {
            case 0:     return "AF stop";
            case 1:     return "Operate AF";
            case 2:     return "Lock AE and start timer";
            default:    return "Unknown (" + value + ")";
        }
    }

    @Nullable
    public String getFillFlashReductionDescription()
    {
        Integer value = _directory.getInteger(TAG_CANON_CUSTOM_FUNCTION_FILL_FLASH_REDUCTION);
        if (value==null)
            return null;
        switch (value) {
            case 0:     return "Enabled";
            case 1:     return "Disabled";
            default:    return "Unknown (" + value + ")";
        }
    }

    @Nullable
    public String getMenuButtonReturnPositionDescription()
    {
        Integer value = _directory.getInteger(TAG_CANON_CUSTOM_FUNCTION_MENU_BUTTON_RETURN);
        if (value==null)
            return null;
        switch (value) {
            case 0:     return "Top";
            case 1:     return "Previous (volatile)";
            case 2:     return "Previous";
            default:    return "Unknown (" + value + ")";
        }
    }

    @Nullable
    public String getSetButtonFunctionWhenShootingDescription()
    {
        Integer value = _directory.getInteger(TAG_CANON_CUSTOM_FUNCTION_SET_BUTTON_FUNCTION);
        if (value==null)
            return null;
        switch (value) {
            case 0:     return "Not Assigned";
            case 1:     return "Change Quality";
            case 2:     return "Change ISO Speed";
            case 3:     return "Select Parameters";
            default:    return "Unknown (" + value + ")";
        }
    }

    @Nullable
    public String getSensorCleaningDescription()
    {
        Integer value = _directory.getInteger(TAG_CANON_CUSTOM_FUNCTION_SENSOR_CLEANING);
        if (value==null)
            return null;
        switch (value) {
            case 0:     return "Disabled";
            case 1:     return "Enabled";
            default:    return "Unknown (" + value + ")";
        }
    }
*/
  val flashBiasDescription: String?
    get() {
      var value = _directory.getInteger(FocalLength.TAG_FLASH_BIAS) ?: return null
      var isNegative = false
      if (value > 0xF000) {
        isNegative = true
        value = 0xFFFF - value
        value++
      }
      // this tag is interesting in that the values returned are:
      //  0, 0.375, 0.5, 0.626, 1
      // not
      //  0, 0.33,  0.5, 0.66,  1
      return "${if (isNegative) "-" else ""}${(value / 32f)} EV"
    }

  val afPointUsedDescription: String?
    get() {
      val value = _directory.getInteger(FocalLength.TAG_AF_POINT_USED) ?: return null
      return if (value and 0x7 == 0) {
        "Right"
      } else if (value and 0x7 == 1) {
        "Centre"
      } else if (value and 0x7 == 2) {
        "Left"
      } else {
        "Unknown ($value)"
      }
    }

  val tagAfPointsInFocus: String?
    get() {
      val value = _directory.getInteger(AFInfo.TAG_AF_POINTS_IN_FOCUS) ?: return null
      val sb = StringBuilder()
      for (i in 0..15) {
        if (value and 1 shl i != 0) {
          if (sb.isNotEmpty()) sb.append(',')
          sb.append(i)
        }
      }
      return if (sb.isEmpty()) "None" else sb.toString()
    }

  val whiteBalanceDescription: String?
    get() = getIndexedDescription(
      FocalLength.TAG_WHITE_BALANCE,
      "Auto",
      "Sunny",
      "Cloudy",
      "Tungsten",
      "Florescent",
      "Flash",
      "Custom"
    )

  val focusMode2Description: String?
    get() = getIndexedDescription(CanonMakernoteDirectory.CameraSettings.TAG_FOCUS_MODE_2, "Single", "Continuous")

  val flashDetailsDescription: String?
    get() {
      val value = _directory.getInteger(CanonMakernoteDirectory.CameraSettings.TAG_FLASH_DETAILS) ?: return null
      if (value shr 14 and 1 != 0) {
        return "External E-TTL"
      }
      if (value shr 13 and 1 != 0) {
        return "Internal flash"
      }
      if (value shr 11 and 1 != 0) {
        return "FP sync used"
      }
      return if (value shr 4 and 1 != 0) {
        "FP sync enabled"
      } else "Unknown ($value)"
    }

  val focalUnitsPerMillimetreDescription: String?
    get() {
      val value = _directory.getInteger(CanonMakernoteDirectory.CameraSettings.TAG_FOCAL_UNITS_PER_MM) ?: return null
      return if (value != 0) {
        value.toString()
      } else {
        ""
      }
    }

  val shortFocalLengthDescription: String?
    get() {
      val value = _directory.getInteger(CanonMakernoteDirectory.CameraSettings.TAG_SHORT_FOCAL_LENGTH) ?: return null
      val units = focalUnitsPerMillimetreDescription
      return "$value $units"
    }

  val longFocalLengthDescription: String?
    get() {
      val value = _directory.getInteger(CanonMakernoteDirectory.CameraSettings.TAG_LONG_FOCAL_LENGTH) ?: return null
      val units = focalUnitsPerMillimetreDescription
      return "${Integer.toString(value)} $units"
    }

  val exposureModeDescription: String?
    get() = getIndexedDescription(
      CanonMakernoteDirectory.CameraSettings.TAG_EXPOSURE_MODE,
      "Easy shooting",
      "Program",
      "Tv-priority",
      "Av-priority",
      "Manual",
      "A-DEP"
    )

  val lensTypeDescription: String?
    get() {
      val value = _directory.getInteger(CanonMakernoteDirectory.CameraSettings.TAG_LENS_TYPE) ?: return null
      return if (_lensTypeById.containsKey(value)) _lensTypeById[value] else "Unknown (%d)".format(value)
    }

  val maxApertureDescription: String?
    get() {
      val value = _directory.getInteger(CanonMakernoteDirectory.CameraSettings.TAG_MAX_APERTURE) ?: return null
      return if (value > 512) "Unknown (%d)".format(value) else getFStopDescription(exp(decodeCanonEv(value) * ln(2.0) / 2.0))
    }

  val minApertureDescription: String?
    get() {
      val value = _directory.getInteger(CanonMakernoteDirectory.CameraSettings.TAG_MIN_APERTURE) ?: return null
      return if (value > 512) "Unknown (%d)".format(value) else getFStopDescription(exp(decodeCanonEv(value) * ln(2.0) / 2.0))
    }

  val afPointSelectedDescription: String?
    get() = getIndexedDescription(
      CanonMakernoteDirectory.CameraSettings.TAG_AF_POINT_SELECTED,
      0x3000,
      "None (MF)",
      "Auto selected",
      "Right",
      "Centre",
      "Left"
    )

  val meteringModeDescription: String?
    get() = getIndexedDescription(
      CanonMakernoteDirectory.CameraSettings.TAG_METERING_MODE,
      3,
      "Evaluative",
      "Partial",
      "Centre weighted"
    )

  // Canon PowerShot S3 is special
  val isoDescription: String?
    get() {
      val value = _directory.getInteger(CanonMakernoteDirectory.CameraSettings.TAG_ISO) ?: return null
      // Canon PowerShot S3 is special
      val canonMask = 0x4000
      return if (value and canonMask != 0) "${value and canonMask.inv()}" else when (value) {
        0 -> "Not specified (see ISOSpeedRatings tag)"
        15 -> "Auto"
        16 -> "50"
        17 -> "100"
        18 -> "200"
        19 -> "400"
        else -> "Unknown ($value)"
      }
    }

  val sharpnessDescription: String?
    get() {
      val value = _directory.getInteger(CanonMakernoteDirectory.CameraSettings.TAG_SHARPNESS) ?: return null
      return when (value) {
        0xFFFF -> "Low"
        0x000 -> "Normal"
        0x001 -> "High"
        else -> "Unknown ($value)"
      }
    }

  val saturationDescription: String?
    get() {
      val value = _directory.getInteger(CanonMakernoteDirectory.CameraSettings.TAG_SATURATION) ?: return null
      return when (value) {
        0xFFFF -> "Low"
        0x000 -> "Normal"
        0x001 -> "High"
        else -> "Unknown ($value)"
      }
    }

  val contrastDescription: String?
    get() {
      val value = _directory.getInteger(CanonMakernoteDirectory.CameraSettings.TAG_CONTRAST) ?: return null
      return when (value) {
        0xFFFF -> "Low"
        0x000 -> "Normal"
        0x001 -> "High"
        else -> "Unknown ($value)"
      }
    }

  val easyShootingModeDescription: String?
    get() = getIndexedDescription(
      CanonMakernoteDirectory.CameraSettings.TAG_EASY_SHOOTING_MODE,
      "Full auto",
      "Manual",
      "Landscape",
      "Fast shutter",
      "Slow shutter",
      "Night",
      "B&W",
      "Sepia",
      "Portrait",
      "Sports",
      "Macro / Closeup",
      "Pan focus"
    )

  val imageSizeDescription: String?
    get() = getIndexedDescription(
      CanonMakernoteDirectory.CameraSettings.TAG_IMAGE_SIZE,
      "Large",
      "Medium",
      "Small"
    )

  // TODO should check field 32 here (FOCUS_MODE_2)
  val focusMode1Description: String?
    get() = getIndexedDescription(
      CanonMakernoteDirectory.CameraSettings.TAG_FOCUS_MODE_1,
      "One-shot",
      "AI Servo",
      "AI Focus",
      "Manual Focus",  // TODO should check field 32 here (FOCUS_MODE_2)
      "Single",
      "Continuous",
      "Manual Focus"
    )

  val continuousDriveModeDescription: String?
    get() {
      val value = _directory.getInteger(CanonMakernoteDirectory.CameraSettings.TAG_CONTINUOUS_DRIVE_MODE)
        ?: return null
      when (value) {
        0 -> {
          val delay = _directory.getInteger(CanonMakernoteDirectory.CameraSettings.TAG_SELF_TIMER_DELAY)
          return if (delay != null) if (delay == 0) "Single shot" else "Single shot with self-timer" else "Continuous"
        }
        1 -> return "Continuous"
      }
      return "Unknown ($value)"
    }

  // note: this value not set on Canon D30
  val flashModeDescription: String?
    get() {
      val value = _directory.getInteger(CanonMakernoteDirectory.CameraSettings.TAG_FLASH_MODE) ?: return null
      return when (value) {
        0 -> "No flash fired"
        1 -> "Auto"
        2 -> "On"
        3 -> "Red-eye reduction"
        4 -> "Slow-synchro"
        5 -> "Auto and red-eye reduction"
        6 -> "On and red-eye reduction"
        16 ->  // note: this value not set on Canon D30
          "External flash"
        else -> "Unknown ($value)"
      }
    }

  val selfTimerDelayDescription: String?
    get() {
      val value = _directory.getInteger(CanonMakernoteDirectory.CameraSettings.TAG_SELF_TIMER_DELAY) ?: return null
      return if (value == 0) {
        "Self timer not used"
      } else {
        val format = DecimalFormat("0.##")
        format.format(value.toDouble() * 0.1) + " sec"
      }
    }

  val macroModeDescription: String?
    get() = getIndexedDescription(CanonMakernoteDirectory.CameraSettings.TAG_MACRO_MODE, 1, "Macro", "Normal")

  val qualityDescription: String?
    get() = getIndexedDescription(CanonMakernoteDirectory.CameraSettings.TAG_QUALITY, 2, "Normal", "Fine", null, "Superfine")

  val digitalZoomDescription: String?
    get() = getIndexedDescription(CanonMakernoteDirectory.CameraSettings.TAG_DIGITAL_ZOOM, "No digital zoom", "2x", "4x")

  val recordModeDescription: String?
    get() = getIndexedDescription(CanonMakernoteDirectory.CameraSettings.TAG_RECORD_MODE, 1, "JPEG", "CRW+THM", "AVI+THM", "TIF", "TIF+JPEG", "CR2", "CR2+JPEG", null, "MOV", "MP4")

  val focusTypeDescription: String?
    get() {
      val value = _directory.getInteger(CanonMakernoteDirectory.CameraSettings.TAG_FOCUS_TYPE) ?: return null
      return when (value) {
        0 -> "Manual"
        1 -> "Auto"
        3 -> "Close-up (Macro)"
        8 -> "Locked (Pan Mode)"
        else -> "Unknown ($value)"
      }
    }

  val flashActivityDescription: String?
    get() = getIndexedDescription(CanonMakernoteDirectory.CameraSettings.TAG_FLASH_ACTIVITY, "Flash did not fire", "Flash fired")

  val focusContinuousDescription: String?
    get() = getIndexedDescription(CanonMakernoteDirectory.CameraSettings.TAG_FOCUS_CONTINUOUS, 0,
      "Single", "Continuous", null, null, null, null, null, null, "Manual")

  val aESettingDescription: String?
    get() = getIndexedDescription(CanonMakernoteDirectory.CameraSettings.TAG_AE_SETTING, 0,
      "Normal AE", "Exposure Compensation", "AE Lock", "AE Lock + Exposure Comp.", "No AE")

  val displayApertureDescription: String?
    get() {
      val value = _directory.getInteger(CanonMakernoteDirectory.CameraSettings.TAG_DISPLAY_APERTURE) ?: return null
      return if (value == 0xFFFF) value.toString() else getFStopDescription(value / 10f.toDouble())
    }

  val spotMeteringModeDescription: String?
    get() = getIndexedDescription(CanonMakernoteDirectory.CameraSettings.TAG_SPOT_METERING_MODE, 0,
      "Center", "AF Point")

  val photoEffectDescription: String?
    get() {
      val value = _directory.getInteger(CanonMakernoteDirectory.CameraSettings.TAG_PHOTO_EFFECT) ?: return null
      return when (value) {
        0 -> "Off"
        1 -> "Vivid"
        2 -> "Neutral"
        3 -> "Smooth"
        4 -> "Sepia"
        5 -> "B&W"
        6 -> "Custom"
        100 -> "My Color Data"
        else -> "Unknown ($value)"
      }
    }

  // (EOS models)
  val manualFlashOutputDescription: String?
    get() {
      val value = _directory.getInteger(CanonMakernoteDirectory.CameraSettings.TAG_MANUAL_FLASH_OUTPUT) ?: return null
      return when (value) {
        0 -> "n/a"
        0x500 -> "Full"
        0x502 -> "Medium"
        0x504 -> "Low"
        0x7fff -> "n/a" // (EOS models)
        else -> "Unknown ($value)"
      }
    }

  val colorToneDescription: String?
    get() {
      val value = _directory.getInteger(CanonMakernoteDirectory.CameraSettings.TAG_COLOR_TONE) ?: return null
      return if (value == 0x7fff) "n/a" else value.toString()
    }

  val sRawQualityDescription: String?
    get() = getIndexedDescription(CanonMakernoteDirectory.CameraSettings.TAG_SRAW_QUALITY, 0, "n/a", "sRAW1 (mRAW)", "sRAW2 (sRAW)")

  /**
   * Canon hex-based EV (modulo 0x20) to real number.
   *
   * Converted from Exiftool version 10.10 created by Phil Harvey
   * http://www.sno.phy.queensu.ca/~phil/exiftool/
   * lib\Image\ExifTool\Canon.pm
   *
   * eg) 0x00 -> 0
   * 0x0c -> 0.33333
   * 0x10 -> 0.5
   * 0x14 -> 0.66666
   * 0x20 -> 1   ... etc
   */
  private fun decodeCanonEv(value: Int): Double {
    var value = value
    var sign = 1
    if (value < 0) {
      value = -value
      sign = -1
    }
    var frac = value and 0x1f
    value -= frac
    if (frac == 0x0c) frac = 0x20 / 3 else if (frac == 0x14) frac = 0x40 / 3
    return sign * (value + frac) / 0x20.toDouble()
  }

  companion object {
    /**
     * Map from <see cref="CanonMakernoteDirectory.CameraSettings.TagLensType"></see> to string descriptions.
     *
     * Data sourced from http://www.sno.phy.queensu.ca/~phil/exiftool/TagNames/Canon.html#LensType
     *
     * Note that only Canon lenses are listed. Lenses from other manufacturers may identify themselves to the camera
     * as being from this set, but in fact may be quite different. This limits the usefulness of this data,
     * unfortunately.
     */
    private val _lensTypeById = HashMap<Int, String?>()

    init {
      _lensTypeById[1] = "Canon EF 50mm f/1.8"
      _lensTypeById[2] = "Canon EF 28mm f/2.8"
      _lensTypeById[3] = "Canon EF 135mm f/2.8 Soft"
      _lensTypeById[4] = "Canon EF 35-105mm f/3.5-4.5 or Sigma Lens"
      _lensTypeById[5] = "Canon EF 35-70mm f/3.5-4.5"
      _lensTypeById[6] = "Canon EF 28-70mm f/3.5-4.5 or Sigma or Tokina Lens"
      _lensTypeById[7] = "Canon EF 100-300mm f/5.6L"
      _lensTypeById[8] = "Canon EF 100-300mm f/5.6 or Sigma or Tokina Lens"
      _lensTypeById[9] = "Canon EF 70-210mm f/4"
      _lensTypeById[10] = "Canon EF 50mm f/2.5 Macro or Sigma Lens"
      _lensTypeById[11] = "Canon EF 35mm f/2"
      _lensTypeById[13] = "Canon EF 15mm f/2.8 Fisheye"
      _lensTypeById[14] = "Canon EF 50-200mm f/3.5-4.5L"
      _lensTypeById[15] = "Canon EF 50-200mm f/3.5-4.5"
      _lensTypeById[16] = "Canon EF 35-135mm f/3.5-4.5"
      _lensTypeById[17] = "Canon EF 35-70mm f/3.5-4.5A"
      _lensTypeById[18] = "Canon EF 28-70mm f/3.5-4.5"
      _lensTypeById[20] = "Canon EF 100-200mm f/4.5A"
      _lensTypeById[21] = "Canon EF 80-200mm f/2.8L"
      _lensTypeById[22] = "Canon EF 20-35mm f/2.8L or Tokina Lens"
      _lensTypeById[23] = "Canon EF 35-105mm f/3.5-4.5"
      _lensTypeById[24] = "Canon EF 35-80mm f/4-5.6 Power Zoom"
      _lensTypeById[25] = "Canon EF 35-80mm f/4-5.6 Power Zoom"
      _lensTypeById[26] = "Canon EF 100mm f/2.8 Macro or Other Lens"
      _lensTypeById[27] = "Canon EF 35-80mm f/4-5.6"
      _lensTypeById[28] = "Canon EF 80-200mm f/4.5-5.6 or Tamron Lens"
      _lensTypeById[29] = "Canon EF 50mm f/1.8 II"
      _lensTypeById[30] = "Canon EF 35-105mm f/4.5-5.6"
      _lensTypeById[31] = "Canon EF 75-300mm f/4-5.6 or Tamron Lens"
      _lensTypeById[32] = "Canon EF 24mm f/2.8 or Sigma Lens"
      _lensTypeById[33] = "Voigtlander or Carl Zeiss Lens"
      _lensTypeById[35] = "Canon EF 35-80mm f/4-5.6"
      _lensTypeById[36] = "Canon EF 38-76mm f/4.5-5.6"
      _lensTypeById[37] = "Canon EF 35-80mm f/4-5.6 or Tamron Lens"
      _lensTypeById[38] = "Canon EF 80-200mm f/4.5-5.6"
      _lensTypeById[39] = "Canon EF 75-300mm f/4-5.6"
      _lensTypeById[40] = "Canon EF 28-80mm f/3.5-5.6"
      _lensTypeById[41] = "Canon EF 28-90mm f/4-5.6"
      _lensTypeById[42] = "Canon EF 28-200mm f/3.5-5.6 or Tamron Lens"
      _lensTypeById[43] = "Canon EF 28-105mm f/4-5.6"
      _lensTypeById[44] = "Canon EF 90-300mm f/4.5-5.6"
      _lensTypeById[45] = "Canon EF-S 18-55mm f/3.5-5.6 [II]"
      _lensTypeById[46] = "Canon EF 28-90mm f/4-5.6"
      _lensTypeById[47] = "Zeiss Milvus 35mm f/2 or 50mm f/2"
      _lensTypeById[48] = "Canon EF-S 18-55mm f/3.5-5.6 IS"
      _lensTypeById[49] = "Canon EF-S 55-250mm f/4-5.6 IS"
      _lensTypeById[50] = "Canon EF-S 18-200mm f/3.5-5.6 IS"
      _lensTypeById[51] = "Canon EF-S 18-135mm f/3.5-5.6 IS"
      _lensTypeById[52] = "Canon EF-S 18-55mm f/3.5-5.6 IS II"
      _lensTypeById[53] = "Canon EF-S 18-55mm f/3.5-5.6 III"
      _lensTypeById[54] = "Canon EF-S 55-250mm f/4-5.6 IS II"
      _lensTypeById[94] = "Canon TS-E 17mm f/4L"
      _lensTypeById[95] = "Canon TS-E 24.0mm f/3.5 L II"
      _lensTypeById[124] = "Canon MP-E 65mm f/2.8 1-5x Macro Photo"
      _lensTypeById[125] = "Canon TS-E 24mm f/3.5L"
      _lensTypeById[126] = "Canon TS-E 45mm f/2.8"
      _lensTypeById[127] = "Canon TS-E 90mm f/2.8"
      _lensTypeById[129] = "Canon EF 300mm f/2.8L"
      _lensTypeById[130] = "Canon EF 50mm f/1.0L"
      _lensTypeById[131] = "Canon EF 28-80mm f/2.8-4L or Sigma Lens"
      _lensTypeById[132] = "Canon EF 1200mm f/5.6L"
      _lensTypeById[134] = "Canon EF 600mm f/4L IS"
      _lensTypeById[135] = "Canon EF 200mm f/1.8L"
      _lensTypeById[136] = "Canon EF 300mm f/2.8L"
      _lensTypeById[137] = "Canon EF 85mm f/1.2L or Sigma or Tamron Lens"
      _lensTypeById[138] = "Canon EF 28-80mm f/2.8-4L"
      _lensTypeById[139] = "Canon EF 400mm f/2.8L"
      _lensTypeById[140] = "Canon EF 500mm f/4.5L"
      _lensTypeById[141] = "Canon EF 500mm f/4.5L"
      _lensTypeById[142] = "Canon EF 300mm f/2.8L IS"
      _lensTypeById[143] = "Canon EF 500mm f/4L IS or Sigma Lens"
      _lensTypeById[144] = "Canon EF 35-135mm f/4-5.6 USM"
      _lensTypeById[145] = "Canon EF 100-300mm f/4.5-5.6 USM"
      _lensTypeById[146] = "Canon EF 70-210mm f/3.5-4.5 USM"
      _lensTypeById[147] = "Canon EF 35-135mm f/4-5.6 USM"
      _lensTypeById[148] = "Canon EF 28-80mm f/3.5-5.6 USM"
      _lensTypeById[149] = "Canon EF 100mm f/2 USM"
      _lensTypeById[150] = "Canon EF 14mm f/2.8L or Sigma Lens"
      _lensTypeById[151] = "Canon EF 200mm f/2.8L"
      _lensTypeById[152] = "Canon EF 300mm f/4L IS or Sigma Lens"
      _lensTypeById[153] = "Canon EF 35-350mm f/3.5-5.6L or Sigma or Tamron Lens"
      _lensTypeById[154] = "Canon EF 20mm f/2.8 USM or Zeiss Lens"
      _lensTypeById[155] = "Canon EF 85mm f/1.8 USM"
      _lensTypeById[156] = "Canon EF 28-105mm f/3.5-4.5 USM or Tamron Lens"
      _lensTypeById[160] = "Canon EF 20-35mm f/3.5-4.5 USM or Tamron or Tokina Lens"
      _lensTypeById[161] = "Canon EF 28-70mm f/2.8L or Sigma or Tamron Lens"
      _lensTypeById[162] = "Canon EF 200mm f/2.8L"
      _lensTypeById[163] = "Canon EF 300mm f/4L"
      _lensTypeById[164] = "Canon EF 400mm f/5.6L"
      _lensTypeById[165] = "Canon EF 70-200mm f/2.8 L"
      _lensTypeById[166] = "Canon EF 70-200mm f/2.8 L + 1.4x"
      _lensTypeById[167] = "Canon EF 70-200mm f/2.8 L + 2x"
      _lensTypeById[168] = "Canon EF 28mm f/1.8 USM or Sigma Lens"
      _lensTypeById[169] = "Canon EF 17-35mm f/2.8L or Sigma Lens"
      _lensTypeById[170] = "Canon EF 200mm f/2.8L II"
      _lensTypeById[171] = "Canon EF 300mm f/4L"
      _lensTypeById[172] = "Canon EF 400mm f/5.6L or Sigma Lens"
      _lensTypeById[173] = "Canon EF 180mm Macro f/3.5L or Sigma Lens"
      _lensTypeById[174] = "Canon EF 135mm f/2L or Other Lens"
      _lensTypeById[175] = "Canon EF 400mm f/2.8L"
      _lensTypeById[176] = "Canon EF 24-85mm f/3.5-4.5 USM"
      _lensTypeById[177] = "Canon EF 300mm f/4L IS"
      _lensTypeById[178] = "Canon EF 28-135mm f/3.5-5.6 IS"
      _lensTypeById[179] = "Canon EF 24mm f/1.4L"
      _lensTypeById[180] = "Canon EF 35mm f/1.4L or Other Lens"
      _lensTypeById[181] = "Canon EF 100-400mm f/4.5-5.6L IS + 1.4x or Sigma Lens"
      _lensTypeById[182] = "Canon EF 100-400mm f/4.5-5.6L IS + 2x or Sigma Lens"
      _lensTypeById[183] = "Canon EF 100-400mm f/4.5-5.6L IS or Sigma Lens"
      _lensTypeById[184] = "Canon EF 400mm f/2.8L + 2x"
      _lensTypeById[185] = "Canon EF 600mm f/4L IS"
      _lensTypeById[186] = "Canon EF 70-200mm f/4L"
      _lensTypeById[187] = "Canon EF 70-200mm f/4L + 1.4x"
      _lensTypeById[188] = "Canon EF 70-200mm f/4L + 2x"
      _lensTypeById[189] = "Canon EF 70-200mm f/4L + 2.8x"
      _lensTypeById[190] = "Canon EF 100mm f/2.8 Macro USM"
      _lensTypeById[191] = "Canon EF 400mm f/4 DO IS"
      _lensTypeById[193] = "Canon EF 35-80mm f/4-5.6 USM"
      _lensTypeById[194] = "Canon EF 80-200mm f/4.5-5.6 USM"
      _lensTypeById[195] = "Canon EF 35-105mm f/4.5-5.6 USM"
      _lensTypeById[196] = "Canon EF 75-300mm f/4-5.6 USM"
      _lensTypeById[197] = "Canon EF 75-300mm f/4-5.6 IS USM"
      _lensTypeById[198] = "Canon EF 50mm f/1.4 USM or Zeiss Lens"
      _lensTypeById[199] = "Canon EF 28-80mm f/3.5-5.6 USM"
      _lensTypeById[200] = "Canon EF 75-300mm f/4-5.6 USM"
      _lensTypeById[201] = "Canon EF 28-80mm f/3.5-5.6 USM"
      _lensTypeById[202] = "Canon EF 28-80mm f/3.5-5.6 USM IV"
      _lensTypeById[208] = "Canon EF 22-55mm f/4-5.6 USM"
      _lensTypeById[209] = "Canon EF 55-200mm f/4.5-5.6"
      _lensTypeById[210] = "Canon EF 28-90mm f/4-5.6 USM"
      _lensTypeById[211] = "Canon EF 28-200mm f/3.5-5.6 USM"
      _lensTypeById[212] = "Canon EF 28-105mm f/4-5.6 USM"
      _lensTypeById[213] = "Canon EF 90-300mm f/4.5-5.6 USM or Tamron Lens"
      _lensTypeById[214] = "Canon EF-S 18-55mm f/3.5-5.6 USM"
      _lensTypeById[215] = "Canon EF 55-200mm f/4.5-5.6 II USM"
      _lensTypeById[217] = "Tamron AF 18-270mm f/3.5-6.3 Di II VC PZD"
      _lensTypeById[224] = "Canon EF 70-200mm f/2.8L IS"
      _lensTypeById[225] = "Canon EF 70-200mm f/2.8L IS + 1.4x"
      _lensTypeById[226] = "Canon EF 70-200mm f/2.8L IS + 2x"
      _lensTypeById[227] = "Canon EF 70-200mm f/2.8L IS + 2.8x"
      _lensTypeById[228] = "Canon EF 28-105mm f/3.5-4.5 USM"
      _lensTypeById[229] = "Canon EF 16-35mm f/2.8L"
      _lensTypeById[230] = "Canon EF 24-70mm f/2.8L"
      _lensTypeById[231] = "Canon EF 17-40mm f/4L"
      _lensTypeById[232] = "Canon EF 70-300mm f/4.5-5.6 DO IS USM"
      _lensTypeById[233] = "Canon EF 28-300mm f/3.5-5.6L IS"
      _lensTypeById[234] = "Canon EF-S 17-85mm f/4-5.6 IS USM or Tokina Lens"
      _lensTypeById[235] = "Canon EF-S 10-22mm f/3.5-4.5 USM"
      _lensTypeById[236] = "Canon EF-S 60mm f/2.8 Macro USM"
      _lensTypeById[237] = "Canon EF 24-105mm f/4L IS"
      _lensTypeById[238] = "Canon EF 70-300mm f/4-5.6 IS USM"
      _lensTypeById[239] = "Canon EF 85mm f/1.2L II"
      _lensTypeById[240] = "Canon EF-S 17-55mm f/2.8 IS USM"
      _lensTypeById[241] = "Canon EF 50mm f/1.2L"
      _lensTypeById[242] = "Canon EF 70-200mm f/4L IS"
      _lensTypeById[243] = "Canon EF 70-200mm f/4L IS + 1.4x"
      _lensTypeById[244] = "Canon EF 70-200mm f/4L IS + 2x"
      _lensTypeById[245] = "Canon EF 70-200mm f/4L IS + 2.8x"
      _lensTypeById[246] = "Canon EF 16-35mm f/2.8L II"
      _lensTypeById[247] = "Canon EF 14mm f/2.8L II USM"
      _lensTypeById[248] = "Canon EF 200mm f/2L IS or Sigma Lens"
      _lensTypeById[249] = "Canon EF 800mm f/5.6L IS"
      _lensTypeById[250] = "Canon EF 24mm f/1.4L II or Sigma Lens"
      _lensTypeById[251] = "Canon EF 70-200mm f/2.8L IS II USM"
      _lensTypeById[252] = "Canon EF 70-200mm f/2.8L IS II USM + 1.4x"
      _lensTypeById[253] = "Canon EF 70-200mm f/2.8L IS II USM + 2x"
      _lensTypeById[254] = "Canon EF 100mm f/2.8L Macro IS USM"
      _lensTypeById[255] = "Sigma 24-105mm f/4 DG OS HSM | A or Other Sigma Lens"
      _lensTypeById[488] = "Canon EF-S 15-85mm f/3.5-5.6 IS USM"
      _lensTypeById[489] = "Canon EF 70-300mm f/4-5.6L IS USM"
      _lensTypeById[490] = "Canon EF 8-15mm f/4L Fisheye USM"
      _lensTypeById[491] = "Canon EF 300mm f/2.8L IS II USM"
      _lensTypeById[492] = "Canon EF 400mm f/2.8L IS II USM"
      _lensTypeById[493] = "Canon EF 500mm f/4L IS II USM or EF 24-105mm f4L IS USM"
      _lensTypeById[494] = "Canon EF 600mm f/4.0L IS II USM"
      _lensTypeById[495] = "Canon EF 24-70mm f/2.8L II USM"
      _lensTypeById[496] = "Canon EF 200-400mm f/4L IS USM"
      _lensTypeById[499] = "Canon EF 200-400mm f/4L IS USM + 1.4x"
      _lensTypeById[502] = "Canon EF 28mm f/2.8 IS USM"
      _lensTypeById[503] = "Canon EF 24mm f/2.8 IS USM"
      _lensTypeById[504] = "Canon EF 24-70mm f/4L IS USM"
      _lensTypeById[505] = "Canon EF 35mm f/2 IS USM"
      _lensTypeById[506] = "Canon EF 400mm f/4 DO IS II USM"
      _lensTypeById[507] = "Canon EF 16-35mm f/4L IS USM"
      _lensTypeById[508] = "Canon EF 11-24mm f/4L USM"
      _lensTypeById[747] = "Canon EF 100-400mm f/4.5-5.6L IS II USM"
      _lensTypeById[750] = "Canon EF 35mm f/1.4L II USM"
      _lensTypeById[4142] = "Canon EF-S 18-135mm f/3.5-5.6 IS STM"
      _lensTypeById[4143] = "Canon EF-M 18-55mm f/3.5-5.6 IS STM or Tamron Lens"
      _lensTypeById[4144] = "Canon EF 40mm f/2.8 STM"
      _lensTypeById[4145] = "Canon EF-M 22mm f/2 STM"
      _lensTypeById[4146] = "Canon EF-S 18-55mm f/3.5-5.6 IS STM"
      _lensTypeById[4147] = "Canon EF-M 11-22mm f/4-5.6 IS STM"
      _lensTypeById[4148] = "Canon EF-S 55-250mm f/4-5.6 IS STM"
      _lensTypeById[4149] = "Canon EF-M 55-200mm f/4.5-6.3 IS STM"
      _lensTypeById[4150] = "Canon EF-S 10-18mm f/4.5-5.6 IS STM"
      _lensTypeById[4152] = "Canon EF 24-105mm f/3.5-5.6 IS STM"
      _lensTypeById[4153] = "Canon EF-M 15-45mm f/3.5-6.3 IS STM"
      _lensTypeById[4154] = "Canon EF-S 24mm f/2.8 STM"
      _lensTypeById[4156] = "Canon EF 50mm f/1.8 STM"
      _lensTypeById[36912] = "Canon EF-S 18-135mm f/3.5-5.6 IS USM"
      _lensTypeById[65535] = "N/A"
    }
  }
}
