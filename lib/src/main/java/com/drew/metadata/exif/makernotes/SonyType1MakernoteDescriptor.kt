package com.drew.metadata.exif.makernotes

import com.drew.metadata.TagDescriptor

class SonyType1MakernoteDescriptor(directory: SonyType1MakernoteDirectory) : TagDescriptor<SonyType1MakernoteDirectory>(directory) {

  override fun getDescription(tagType: Int): String? {
    return when (tagType) {
      SonyType1MakernoteDirectory.TAG_IMAGE_QUALITY -> getImageQualityDescription()
      SonyType1MakernoteDirectory.TAG_FLASH_EXPOSURE_COMP -> getFlashExposureCompensationDescription()
      SonyType1MakernoteDirectory.TAG_TELECONVERTER -> getTeleconverterDescription()
      SonyType1MakernoteDirectory.TAG_WHITE_BALANCE -> getWhiteBalanceDescription()
      SonyType1MakernoteDirectory.TAG_COLOR_TEMPERATURE -> getColorTemperatureDescription()
      SonyType1MakernoteDirectory.TAG_SCENE_MODE -> getSceneModeDescription()
      SonyType1MakernoteDirectory.TAG_ZONE_MATCHING -> getZoneMatchingDescription()
      SonyType1MakernoteDirectory.TAG_DYNAMIC_RANGE_OPTIMISER -> getDynamicRangeOptimizerDescription()
      SonyType1MakernoteDirectory.TAG_IMAGE_STABILISATION -> getImageStabilizationDescription()
      SonyType1MakernoteDirectory.TAG_COLOR_MODE -> getColorModeDescription()
      SonyType1MakernoteDirectory.TAG_MACRO -> getMacroDescription()
      SonyType1MakernoteDirectory.TAG_EXPOSURE_MODE -> getExposureModeDescription()
      SonyType1MakernoteDirectory.TAG_JPEG_QUALITY -> getJpegQualityDescription()
      SonyType1MakernoteDirectory.TAG_ANTI_BLUR -> getAntiBlurDescription()
      SonyType1MakernoteDirectory.TAG_LONG_EXPOSURE_NOISE_REDUCTION_OR_FOCUS_MODE -> getLongExposureNoiseReductionDescription()
      SonyType1MakernoteDirectory.TAG_HIGH_ISO_NOISE_REDUCTION -> getHighIsoNoiseReductionDescription()
      SonyType1MakernoteDirectory.TAG_PICTURE_EFFECT -> getPictureEffectDescription()
      SonyType1MakernoteDirectory.TAG_SOFT_SKIN_EFFECT -> getSoftSkinEffectDescription()
      SonyType1MakernoteDirectory.TAG_VIGNETTING_CORRECTION -> getVignettingCorrectionDescription()
      SonyType1MakernoteDirectory.TAG_LATERAL_CHROMATIC_ABERRATION -> getLateralChromaticAberrationDescription()
      SonyType1MakernoteDirectory.TAG_DISTORTION_CORRECTION -> getDistortionCorrectionDescription()
      SonyType1MakernoteDirectory.TAG_AUTO_PORTRAIT_FRAMED -> getAutoPortraitFramedDescription()
      SonyType1MakernoteDirectory.TAG_FOCUS_MODE -> getFocusModeDescription()
      SonyType1MakernoteDirectory.TAG_AF_POINT_SELECTED -> getAFPointSelectedDescription()
      SonyType1MakernoteDirectory.TAG_SONY_MODEL_ID -> getSonyModelIdDescription()
      SonyType1MakernoteDirectory.TAG_AF_MODE -> getAFModeDescription()
      SonyType1MakernoteDirectory.TAG_AF_ILLUMINATOR -> getAFIlluminatorDescription()
      SonyType1MakernoteDirectory.TAG_FLASH_LEVEL -> getFlashLevelDescription()
      SonyType1MakernoteDirectory.TAG_RELEASE_MODE -> getReleaseModeDescription()
      SonyType1MakernoteDirectory.TAG_SEQUENCE_NUMBER -> getSequenceNumberDescription()
      else -> super.getDescription(tagType)
    }
  }

  fun getImageQualityDescription(): String? {
    return getIndexedDescription(SonyType1MakernoteDirectory.TAG_IMAGE_QUALITY,
      "RAW",
      "Super Fine",
      "Fine",
      "Standard",
      "Economy",
      "Extra Fine",
      "RAW + JPEG",
      "Compressed RAW",
      "Compressed RAW + JPEG")
  }

  fun getFlashExposureCompensationDescription(): String? {
    return getFormattedInt(SonyType1MakernoteDirectory.TAG_FLASH_EXPOSURE_COMP, "%d EV")
  }

  fun getTeleconverterDescription(): String? {
    val value = _directory.getInteger(SonyType1MakernoteDirectory.TAG_TELECONVERTER) ?: return null
    return when (value) {
      0x00 -> "None"
      0x48 -> "Minolta/Sony AF 2x APO (D)"
      0x50 -> "Minolta AF 2x APO II"
      0x60 -> "Minolta AF 2x APO"
      0x88 -> "Minolta/Sony AF 1.4x APO (D)"
      0x90 -> "Minolta AF 1.4x APO II"
      0xa0 -> "Minolta AF 1.4x APO"
      else -> "Unknown ($value)"
    }
  }

  fun getWhiteBalanceDescription(): String? {
    val value = _directory.getInteger(SonyType1MakernoteDirectory.TAG_WHITE_BALANCE) ?: return null
    return when (value) {
      0x00 -> "Auto"
      0x01 -> "Color Temperature/Color Filter"
      0x10 -> "Daylight"
      0x20 -> "Cloudy"
      0x30 -> "Shade"
      0x40 -> "Tungsten"
      0x50 -> "Flash"
      0x60 -> "Fluorescent"
      0x70 -> "Custom"
      else -> "Unknown ($value)"
    }
  }

  fun getColorTemperatureDescription(): String? {
    val value = _directory.getInteger(SonyType1MakernoteDirectory.TAG_COLOR_TEMPERATURE) ?: return null
    if (value == 0) return "Auto"
    val kelvin = value and 0x00FF0000 shr 8 or (value and -0x1000000 shr 24)
    return "%d K".format(kelvin)
  }

  fun getZoneMatchingDescription(): String? {
    return getIndexedDescription(SonyType1MakernoteDirectory.TAG_ZONE_MATCHING,
      "ISO Setting Used", "High Key", "Low Key")
  }

  fun getDynamicRangeOptimizerDescription(): String? {
    val value = _directory.getInteger(SonyType1MakernoteDirectory.TAG_DYNAMIC_RANGE_OPTIMISER) ?: return null
    return when (value) {
      0 -> "Off"
      1 -> "Standard"
      2 -> "Advanced Auto"
      3 -> "Auto"
      8 -> "Advanced LV1"
      9 -> "Advanced LV2"
      10 -> "Advanced LV3"
      11 -> "Advanced LV4"
      12 -> "Advanced LV5"
      16 -> "LV1"
      17 -> "LV2"
      18 -> "LV3"
      19 -> "LV4"
      20 -> "LV5"
      else -> "Unknown (%d)".format(value)
    }
  }

  fun getImageStabilizationDescription(): String? {
    val value = _directory.getInteger(SonyType1MakernoteDirectory.TAG_IMAGE_STABILISATION) ?: return null
    return when (value) {
      0 -> "Off"
      1 -> "On"
      else -> "N/A"
    }
  }

  fun getColorModeDescription(): String? {
    val value = _directory.getInteger(SonyType1MakernoteDirectory.TAG_COLOR_MODE) ?: return null
    return when (value) {
      0 -> "Standard"
      1 -> "Vivid"
      2 -> "Portrait"
      3 -> "Landscape"
      4 -> "Sunset"
      5 -> "Night Portrait"
      6 -> "Black & White"
      7 -> "Adobe RGB"
      12, 100 -> "Neutral"
      13, 101 -> "Clear"
      14, 102 -> "Deep"
      15, 103 -> "Light"
      16 -> "Autumn"
      17 -> "Sepia"
      104 -> "Night View"
      105 -> "Autumn Leaves"
      else -> "Unknown (%d)".format(value)
    }
  }

  fun getMacroDescription(): String? {
    val value = _directory.getInteger(SonyType1MakernoteDirectory.TAG_MACRO) ?: return null
    return when (value) {
      0 -> "Off"
      1 -> "On"
      2 -> "Magnifying Glass/Super Macro"
      0xFFFF -> "N/A"
      else -> "Unknown (%d)".format(value)
    }
  }

  fun getExposureModeDescription(): String? {
    val value = _directory.getInteger(SonyType1MakernoteDirectory.TAG_EXPOSURE_MODE) ?: return null
    return when (value) {
      0 -> "Program"
      1 -> "Portrait"
      2 -> "Beach"
      3 -> "Sports"
      4 -> "Snow"
      5 -> "Landscape"
      6 -> "Auto"
      7 -> "Aperture Priority"
      8 -> "Shutter Priority"
      9 -> "Night Scene / Twilight"
      10 -> "Hi-Speed Shutter"
      11 -> "Twilight Portrait"
      12 -> "Soft Snap/Portrait"
      13 -> "Fireworks"
      14 -> "Smile Shutter"
      15 -> "Manual"
      18 -> "High Sensitivity"
      19 -> "Macro"
      20 -> "Advanced Sports Shooting"
      29 -> "Underwater"
      33 -> "Food"
      34 -> "Panorama"
      35 -> "Handheld Night Shot"
      36 -> "Anti Motion Blur"
      37 -> "Pet"
      38 -> "Backlight Correction HDR"
      39 -> "Superior Auto"
      40 -> "Background Defocus"
      41 -> "Soft Skin"
      42 -> "3D Image"
      0xFFFF -> "N/A"
      else -> "Unknown (%d)".format(value)
    }
  }

  fun getJpegQualityDescription(): String? {
    val value = _directory.getInteger(SonyType1MakernoteDirectory.TAG_JPEG_QUALITY) ?: return null
    return when (value) {
      0 -> "Normal"
      1 -> "Fine"
      2 -> "Extra Fine"
      0xFFFF -> "N/A"
      else -> "Unknown (%d)".format(value)
    }
  }

  fun getAntiBlurDescription(): String? {
    val value = _directory.getInteger(SonyType1MakernoteDirectory.TAG_ANTI_BLUR) ?: return null
    return when (value) {
      0 -> "Off"
      1 -> "On (Continuous)"
      2 -> "On (Shooting)"
      0xFFFF -> "N/A"
      else -> "Unknown (%d)".format(value)
    }
  }

  fun getLongExposureNoiseReductionDescription(): String? {
    val value = _directory.getInteger(SonyType1MakernoteDirectory.TAG_LONG_EXPOSURE_NOISE_REDUCTION_OR_FOCUS_MODE)
      ?: return null
    return when (value) {
      0 -> "Off"
      1 -> "On"
      0xFFFF -> "N/A"
      else -> "Unknown (%d)".format(value)
    }
  }

  fun getHighIsoNoiseReductionDescription(): String? {
    val value = _directory.getInteger(SonyType1MakernoteDirectory.TAG_HIGH_ISO_NOISE_REDUCTION) ?: return null
    return when (value) {
      0 -> "Off"
      1 -> "On"
      2 -> "Normal"
      3 -> "High"
      0x100 -> "Auto"
      0xffff -> "N/A"
      else -> "Unknown (%d)".format(value)
    }
  }

  fun getPictureEffectDescription(): String? {
    val value = _directory.getInteger(SonyType1MakernoteDirectory.TAG_PICTURE_EFFECT) ?: return null
    return when (value) {
      0 -> "Off"
      1 -> "Toy Camera"
      2 -> "Pop Color"
      3 -> "Posterization"
      4 -> "Posterization B/W"
      5 -> "Retro Photo"
      6 -> "Soft High Key"
      7 -> "Partial Color (red)"
      8 -> "Partial Color (green)"
      9 -> "Partial Color (blue)"
      10 -> "Partial Color (yellow)"
      13 -> "High Contrast Monochrome"
      16 -> "Toy Camera (normal)"
      17 -> "Toy Camera (cool)"
      18 -> "Toy Camera (warm)"
      19 -> "Toy Camera (green)"
      20 -> "Toy Camera (magenta)"
      32 -> "Soft Focus (low)"
      33 -> "Soft Focus"
      34 -> "Soft Focus (high)"
      48 -> "Miniature (auto)"
      49 -> "Miniature (top)"
      50 -> "Miniature (middle horizontal)"
      51 -> "Miniature (bottom)"
      52 -> "Miniature (left)"
      53 -> "Miniature (middle vertical)"
      54 -> "Miniature (right)"
      64 -> "HDR Painting (low)"
      65 -> "HDR Painting"
      66 -> "HDR Painting (high)"
      80 -> "Rich-tone Monochrome"
      97 -> "Water Color"
      98 -> "Water Color 2"
      112 -> "Illustration (low)"
      113 -> "Illustration"
      114 -> "Illustration (high)"
      else -> "Unknown (%d)".format(value)
    }
  }

  fun getSoftSkinEffectDescription(): String? {
    return getIndexedDescription(SonyType1MakernoteDirectory.TAG_SOFT_SKIN_EFFECT, "Off", "Low", "Mid", "High")
  }

  fun getVignettingCorrectionDescription(): String? {
    val value = _directory.getInteger(SonyType1MakernoteDirectory.TAG_VIGNETTING_CORRECTION) ?: return null
    return when (value) {
      0 -> "Off"
      2 -> "Auto"
      -0x1 -> "N/A"
      else -> "Unknown (%d)".format(value)
    }
  }

  fun getLateralChromaticAberrationDescription(): String? {
    val value = _directory.getInteger(SonyType1MakernoteDirectory.TAG_LATERAL_CHROMATIC_ABERRATION) ?: return null
    return when (value) {
      0 -> "Off"
      2 -> "Auto"
      -0x1 -> "N/A"
      else -> "Unknown (%d)".format(value)
    }
  }

  fun getDistortionCorrectionDescription(): String? {
    val value = _directory.getInteger(SonyType1MakernoteDirectory.TAG_DISTORTION_CORRECTION) ?: return null
    return when (value) {
      0 -> "Off"
      2 -> "Auto"
      -0x1 -> "N/A"
      else -> "Unknown (%d)".format(value)
    }
  }

  fun getAutoPortraitFramedDescription(): String? {
    return getIndexedDescription(SonyType1MakernoteDirectory.TAG_AUTO_PORTRAIT_FRAMED, "No", "Yes")
  }

  fun getFocusModeDescription(): String? {
    return getIndexedDescription(SonyType1MakernoteDirectory.TAG_FOCUS_MODE,
      "Manual", null, "AF-A", "AF-C", "AF-S", null, "DMF", "AF-D")
  }

  fun getAFPointSelectedDescription(): String? {
    return getIndexedDescription(SonyType1MakernoteDirectory.TAG_AF_POINT_SELECTED,
      "Auto",  // 0
      "Center",  // 1
      "Top",  // 2
      "Upper-right",  // 3
      "Right",  // 4
      "Lower-right",  // 5
      "Bottom",  // 6
      "Lower-left",  // 7
      "Left",  // 8
      "Upper-left	  	",  // 9
      "Far Right",  // 10
      "Far Left",  // 11
      "Upper-middle",  // 12
      "Near Right",  // 13
      "Lower-middle",  // 14
      "Near Left",  // 15
      "Upper Far Right",  // 16
      "Lower Far Right",  // 17
      "Lower Far Left",  // 18
      "Upper Far Left" // 19
    )
  }

  fun getSonyModelIdDescription(): String? {
    val value = _directory.getInteger(SonyType1MakernoteDirectory.TAG_SONY_MODEL_ID) ?: return null
    return when (value) {
      2 -> "DSC-R1"
      256 -> "DSLR-A100"
      257 -> "DSLR-A900"
      258 -> "DSLR-A700"
      259 -> "DSLR-A200"
      260 -> "DSLR-A350"
      261 -> "DSLR-A300"
      262 -> "DSLR-A900 (APS-C mode)"
      263 -> "DSLR-A380/A390"
      264 -> "DSLR-A330"
      265 -> "DSLR-A230"
      266 -> "DSLR-A290"
      269 -> "DSLR-A850"
      270 -> "DSLR-A850 (APS-C mode)"
      273 -> "DSLR-A550"
      274 -> "DSLR-A500"
      275 -> "DSLR-A450"
      278 -> "NEX-5"
      279 -> "NEX-3"
      280 -> "SLT-A33"
      281 -> "SLT-A55V"
      282 -> "DSLR-A560"
      283 -> "DSLR-A580"
      284 -> "NEX-C3"
      285 -> "SLT-A35"
      286 -> "SLT-A65V"
      287 -> "SLT-A77V"
      288 -> "NEX-5N"
      289 -> "NEX-7"
      290 -> "NEX-VG20E"
      291 -> "SLT-A37"
      292 -> "SLT-A57"
      293 -> "NEX-F3"
      294 -> "SLT-A99V"
      295 -> "NEX-6"
      296 -> "NEX-5R"
      297 -> "DSC-RX100"
      298 -> "DSC-RX1"
      else -> "Unknown ($value)"
    }
  }

  fun getSceneModeDescription(): String? {
    val value = _directory.getInteger(SonyType1MakernoteDirectory.TAG_SCENE_MODE) ?: return null
    return when (value) {
      0 -> "Standard"
      1 -> "Portrait"
      2 -> "Text"
      3 -> "Night Scene"
      4 -> "Sunset"
      5 -> "Sports"
      6 -> "Landscape"
      7 -> "Night Portrait"
      8 -> "Macro"
      9 -> "Super Macro"
      16 -> "Auto"
      17 -> "Night View/Portrait"
      18 -> "Sweep Panorama"
      19 -> "Handheld Night Shot"
      20 -> "Anti Motion Blur"
      21 -> "Cont. Priority AE"
      22 -> "Auto+"
      23 -> "3D Sweep Panorama"
      24 -> "Superior Auto"
      25 -> "High Sensitivity"
      26 -> "Fireworks"
      27 -> "Food"
      28 -> "Pet"
      else -> "Unknown ($value)"
    }
  }

  fun getAFModeDescription(): String? {
    val value = _directory.getInteger(SonyType1MakernoteDirectory.TAG_AF_MODE) ?: return null
    return when (value) {
      0 -> "Default"
      1 -> "Multi"
      2 -> "Center"
      3 -> "Spot"
      4 -> "Flexible Spot"
      6 -> "Touch"
      14 -> "Manual Focus"
      15 -> "Face Detected"
      0xffff -> "n/a"
      else -> "Unknown ($value)"
    }
  }

  fun getAFIlluminatorDescription(): String? {
    val value = _directory.getInteger(SonyType1MakernoteDirectory.TAG_AF_ILLUMINATOR) ?: return null
    return when (value) {
      0 -> "Off"
      1 -> "Auto"
      0xffff -> "n/a"
      else -> "Unknown ($value)"
    }
  }

  fun getFlashLevelDescription(): String? {
    val value = _directory.getInteger(SonyType1MakernoteDirectory.TAG_FLASH_LEVEL) ?: return null
    return when (value) {
      -32768 -> "Low"
      -3 -> "-3/3"
      -2 -> "-2/3"
      -1 -> "-1/3"
      0 -> "Normal"
      1 -> "+1/3"
      2 -> "+2/3"
      3 -> "+3/3"
      128 -> "n/a"
      32767 -> "High"
      else -> "Unknown ($value)"
    }
  }

  fun getReleaseModeDescription(): String? {
    val value = _directory.getInteger(SonyType1MakernoteDirectory.TAG_RELEASE_MODE) ?: return null
    return when (value) {
      0 -> "Normal"
      2 -> "Continuous"
      5 -> "Exposure Bracketing"
      6 -> "White Balance Bracketing"
      65535 -> "n/a"
      else -> "Unknown ($value)"
    }
  }

  fun getSequenceNumberDescription(): String? {
    val value = _directory.getInteger(SonyType1MakernoteDirectory.TAG_RELEASE_MODE) ?: return null
    return when (value) {
      0 -> "Single"
      65535 -> "n/a"
      else -> value.toString()
    }
  }

}