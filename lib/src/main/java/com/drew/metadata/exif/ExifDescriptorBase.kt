package com.drew.metadata.exif

import com.drew.imaging.apertureToFStop
import com.drew.lang.ByteArrayReader
import com.drew.lang.UTF_16LE
import com.drew.metadata.Directory
import com.drew.metadata.TagDescriptor
import java.io.IOException
import java.io.UnsupportedEncodingException
import java.text.DecimalFormat
import kotlin.math.min

abstract class ExifDescriptorBase<T : Directory>(directory: T) : TagDescriptor<T>(directory) {
  /**
   * Dictates whether rational values will be represented in decimal format in instances
   * where decimal notation is elegant (such as 1/2 -> 0.5, but not 1/3).
   */
  private val _allowDecimalRepresentationOfRationals = true

  // Note for the potential addition of brightness presentation in eV:
  // Brightness of taken subject. To calculate Exposure(Ev) from BrightnessValue(Bv),
  // you must add SensitivityValue(Sv).
  // Ev=BV+Sv   Sv=log2(ISOSpeedRating/3.125)
  // ISO100:Sv=5, ISO200:Sv=6, ISO400:Sv=7, ISO125:Sv=5.32.

  // Note for the potential addition of brightness presentation in eV:
// Brightness of taken subject. To calculate Exposure(Ev) from BrightnessValue(Bv),
// you must add SensitivityValue(Sv).
// Ev=BV+Sv   Sv=log2(ISOSpeedRating/3.125)
// ISO100:Sv=5, ISO200:Sv=6, ISO400:Sv=7, ISO125:Sv=5.32.


  override fun getDescription(tagType: Int): String? {
    return when (tagType) {
      ExifDirectoryBase.TAG_INTEROP_INDEX -> getInteropIndexDescription()
      ExifDirectoryBase.TAG_INTEROP_VERSION -> getInteropVersionDescription()
      ExifDirectoryBase.TAG_NEW_SUBFILE_TYPE -> getNewSubfileTypeDescription()
      ExifDirectoryBase.TAG_SUBFILE_TYPE -> getSubfileTypeDescription()
      ExifDirectoryBase.TAG_IMAGE_WIDTH -> getImageWidthDescription()
      ExifDirectoryBase.TAG_IMAGE_HEIGHT -> getImageHeightDescription()
      ExifDirectoryBase.TAG_BITS_PER_SAMPLE -> getBitsPerSampleDescription()
      ExifDirectoryBase.TAG_COMPRESSION -> getCompressionDescription()
      ExifDirectoryBase.TAG_PHOTOMETRIC_INTERPRETATION -> getPhotometricInterpretationDescription()
      ExifDirectoryBase.TAG_THRESHOLDING -> getThresholdingDescription()
      ExifDirectoryBase.TAG_FILL_ORDER -> getFillOrderDescription()
      ExifDirectoryBase.TAG_ORIENTATION -> getOrientationDescription()
      ExifDirectoryBase.TAG_SAMPLES_PER_PIXEL -> getSamplesPerPixelDescription()
      ExifDirectoryBase.TAG_ROWS_PER_STRIP -> getRowsPerStripDescription()
      ExifDirectoryBase.TAG_STRIP_BYTE_COUNTS -> getStripByteCountsDescription()
      ExifDirectoryBase.TAG_X_RESOLUTION -> getXResolutionDescription()
      ExifDirectoryBase.TAG_Y_RESOLUTION -> getYResolutionDescription()
      ExifDirectoryBase.TAG_PLANAR_CONFIGURATION -> getPlanarConfigurationDescription()
      ExifDirectoryBase.TAG_RESOLUTION_UNIT -> getResolutionDescription()
      ExifDirectoryBase.TAG_JPEG_PROC -> getJpegProcDescription()
      ExifDirectoryBase.TAG_YCBCR_SUBSAMPLING -> getYCbCrSubsamplingDescription()
      ExifDirectoryBase.TAG_YCBCR_POSITIONING -> getYCbCrPositioningDescription()
      ExifDirectoryBase.TAG_REFERENCE_BLACK_WHITE -> getReferenceBlackWhiteDescription()
      ExifDirectoryBase.TAG_CFA_PATTERN_2 -> getCfaPattern2Description()
      ExifDirectoryBase.TAG_EXPOSURE_TIME -> getExposureTimeDescription()
      ExifDirectoryBase.TAG_FNUMBER -> getFNumberDescription()
      ExifDirectoryBase.TAG_EXPOSURE_PROGRAM -> getExposureProgramDescription()
      ExifDirectoryBase.TAG_ISO_EQUIVALENT -> getIsoEquivalentDescription()
      ExifDirectoryBase.TAG_SENSITIVITY_TYPE -> getSensitivityTypeRangeDescription()
      ExifDirectoryBase.TAG_EXIF_VERSION -> getExifVersionDescription()
      ExifDirectoryBase.TAG_COMPONENTS_CONFIGURATION -> getComponentConfigurationDescription()
      ExifDirectoryBase.TAG_COMPRESSED_AVERAGE_BITS_PER_PIXEL -> getCompressedAverageBitsPerPixelDescription()
      ExifDirectoryBase.TAG_SHUTTER_SPEED -> getShutterSpeedDescription()
      ExifDirectoryBase.TAG_APERTURE -> getApertureValueDescription()
      ExifDirectoryBase.TAG_BRIGHTNESS_VALUE -> getBrightnessValueDescription()
      ExifDirectoryBase.TAG_EXPOSURE_BIAS -> getExposureBiasDescription()
      ExifDirectoryBase.TAG_MAX_APERTURE -> getMaxApertureValueDescription()
      ExifDirectoryBase.TAG_SUBJECT_DISTANCE -> getSubjectDistanceDescription()
      ExifDirectoryBase.TAG_METERING_MODE -> getMeteringModeDescription()
      ExifDirectoryBase.TAG_WHITE_BALANCE -> getWhiteBalanceDescription()
      ExifDirectoryBase.TAG_FLASH -> getFlashDescription()
      ExifDirectoryBase.TAG_FOCAL_LENGTH -> getFocalLengthDescription()
      ExifDirectoryBase.TAG_USER_COMMENT -> getUserCommentDescription()
      ExifDirectoryBase.TAG_TEMPERATURE -> getTemperatureDescription()
      ExifDirectoryBase.TAG_HUMIDITY -> getHumidityDescription()
      ExifDirectoryBase.TAG_PRESSURE -> getPressureDescription()
      ExifDirectoryBase.TAG_WATER_DEPTH -> getWaterDepthDescription()
      ExifDirectoryBase.TAG_ACCELERATION -> getAccelerationDescription()
      ExifDirectoryBase.TAG_CAMERA_ELEVATION_ANGLE -> getCameraElevationAngleDescription()
      ExifDirectoryBase.TAG_WIN_TITLE -> getWindowsTitleDescription()
      ExifDirectoryBase.TAG_WIN_COMMENT -> getWindowsCommentDescription()
      ExifDirectoryBase.TAG_WIN_AUTHOR -> getWindowsAuthorDescription()
      ExifDirectoryBase.TAG_WIN_KEYWORDS -> getWindowsKeywordsDescription()
      ExifDirectoryBase.TAG_WIN_SUBJECT -> getWindowsSubjectDescription()
      ExifDirectoryBase.TAG_FLASHPIX_VERSION -> getFlashPixVersionDescription()
      ExifDirectoryBase.TAG_COLOR_SPACE -> getColorSpaceDescription()
      ExifDirectoryBase.TAG_EXIF_IMAGE_WIDTH -> getExifImageWidthDescription()
      ExifDirectoryBase.TAG_EXIF_IMAGE_HEIGHT -> getExifImageHeightDescription()
      ExifDirectoryBase.TAG_FOCAL_PLANE_X_RESOLUTION -> getFocalPlaneXResolutionDescription()
      ExifDirectoryBase.TAG_FOCAL_PLANE_Y_RESOLUTION -> getFocalPlaneYResolutionDescription()
      ExifDirectoryBase.TAG_FOCAL_PLANE_RESOLUTION_UNIT -> getFocalPlaneResolutionUnitDescription()
      ExifDirectoryBase.TAG_SENSING_METHOD -> getSensingMethodDescription()
      ExifDirectoryBase.TAG_FILE_SOURCE -> getFileSourceDescription()
      ExifDirectoryBase.TAG_SCENE_TYPE -> getSceneTypeDescription()
      ExifDirectoryBase.TAG_CFA_PATTERN -> getCfaPatternDescription()
      ExifDirectoryBase.TAG_CUSTOM_RENDERED -> getCustomRenderedDescription()
      ExifDirectoryBase.TAG_EXPOSURE_MODE -> getExposureModeDescription()
      ExifDirectoryBase.TAG_WHITE_BALANCE_MODE -> getWhiteBalanceModeDescription()
      ExifDirectoryBase.TAG_DIGITAL_ZOOM_RATIO -> getDigitalZoomRatioDescription()
      ExifDirectoryBase.TAG_35MM_FILM_EQUIV_FOCAL_LENGTH -> get35mmFilmEquivFocalLengthDescription()
      ExifDirectoryBase.TAG_SCENE_CAPTURE_TYPE -> getSceneCaptureTypeDescription()
      ExifDirectoryBase.TAG_GAIN_CONTROL -> getGainControlDescription()
      ExifDirectoryBase.TAG_CONTRAST -> getContrastDescription()
      ExifDirectoryBase.TAG_SATURATION -> getSaturationDescription()
      ExifDirectoryBase.TAG_SHARPNESS -> getSharpnessDescription()
      ExifDirectoryBase.TAG_SUBJECT_DISTANCE_RANGE -> getSubjectDistanceRangeDescription()
      ExifDirectoryBase.TAG_LENS_SPECIFICATION -> getLensSpecificationDescription()
      else -> super.getDescription(tagType)
    }
  }

  open fun getInteropIndexDescription(): String? {
    val value = _directory.getString(ExifDirectoryBase.TAG_INTEROP_INDEX) ?: return null
    return if ("R98".equals(value.trim { it <= ' ' }, ignoreCase = true)) "Recommended Exif Interoperability Rules (ExifR98)" else "Unknown ($value)"
  }

  open fun getInteropVersionDescription(): String? {
    return getVersionBytesDescription(ExifDirectoryBase.TAG_INTEROP_VERSION, 2)
  }

  open fun getNewSubfileTypeDescription(): String? {
    return getIndexedDescription(ExifDirectoryBase.TAG_NEW_SUBFILE_TYPE, 0,
      "Full-resolution image",
      "Reduced-resolution image",
      "Single page of multi-page image",
      "Single page of multi-page reduced-resolution image",
      "Transparency mask",
      "Transparency mask of reduced-resolution image",
      "Transparency mask of multi-page image",
      "Transparency mask of reduced-resolution multi-page image"
    )
  }

  open fun getSubfileTypeDescription(): String? {
    return getIndexedDescription(ExifDirectoryBase.TAG_SUBFILE_TYPE, 1,
      "Full-resolution image",
      "Reduced-resolution image",
      "Single page of multi-page image"
    )
  }

  open fun getImageWidthDescription(): String? {
    val value = _directory.getString(ExifDirectoryBase.TAG_IMAGE_WIDTH)
    return if (value == null) null else "$value pixels"
  }

  open fun getImageHeightDescription(): String? {
    val value = _directory.getString(ExifDirectoryBase.TAG_IMAGE_HEIGHT)
    return if (value == null) null else "$value pixels"
  }

  open fun getBitsPerSampleDescription(): String? {
    val value = _directory.getString(ExifDirectoryBase.TAG_BITS_PER_SAMPLE)
    return if (value == null) null else "$value bits/component/pixel"
  }

  open fun getCompressionDescription(): String? {
    val value = _directory.getInteger(ExifDirectoryBase.TAG_COMPRESSION) ?: return null
    return when (value) {
      1 -> "Uncompressed"
      2 -> "CCITT 1D"
      3 -> "T4/Group 3 Fax"
      4 -> "T6/Group 4 Fax"
      5 -> "LZW"
      6 -> "JPEG (old-style)"
      7 -> "JPEG"
      8 -> "Adobe Deflate"
      9 -> "JBIG B&W"
      10 -> "JBIG Color"
      99 -> "JPEG"
      262 -> "Kodak 262"
      32766 -> "Next"
      32767 -> "Sony ARW Compressed"
      32769 -> "Packed RAW"
      32770 -> "Samsung SRW Compressed"
      32771 -> "CCIRLEW"
      32772 -> "Samsung SRW Compressed 2"
      32773 -> "PackBits"
      32809 -> "Thunderscan"
      32867 -> "Kodak KDC Compressed"
      32895 -> "IT8CTPAD"
      32896 -> "IT8LW"
      32897 -> "IT8MP"
      32898 -> "IT8BL"
      32908 -> "PixarFilm"
      32909 -> "PixarLog"
      32946 -> "Deflate"
      32947 -> "DCS"
      34661 -> "JBIG"
      34676 -> "SGILog"
      34677 -> "SGILog24"
      34712 -> "JPEG 2000"
      34713 -> "Nikon NEF Compressed"
      34715 -> "JBIG2 TIFF FX"
      34718 -> "Microsoft Document Imaging (MDI) Binary Level Codec"
      34719 -> "Microsoft Document Imaging (MDI) Progressive Transform Codec"
      34720 -> "Microsoft Document Imaging (MDI) Vector"
      34892 -> "Lossy JPEG"
      65000 -> "Kodak DCR Compressed"
      65535 -> "Pentax PEF Compressed"
      else -> "Unknown ($value)"
    }
  }

  open fun getPhotometricInterpretationDescription(): String? { // Shows the color space of the image data components
    val value = _directory.getInteger(ExifDirectoryBase.TAG_PHOTOMETRIC_INTERPRETATION) ?: return null
    return when (value) {
      0 -> "WhiteIsZero"
      1 -> "BlackIsZero"
      2 -> "RGB"
      3 -> "RGB Palette"
      4 -> "Transparency Mask"
      5 -> "CMYK"
      6 -> "YCbCr"
      8 -> "CIELab"
      9 -> "ICCLab"
      10 -> "ITULab"
      32803 -> "Color Filter Array"
      32844 -> "Pixar LogL"
      32845 -> "Pixar LogLuv"
      32892 -> "Linear Raw"
      else -> "Unknown colour space"
    }
  }

  open fun getThresholdingDescription(): String? {
    return getIndexedDescription(ExifDirectoryBase.TAG_THRESHOLDING, 1,
      "No dithering or halftoning",
      "Ordered dither or halftone",
      "Randomized dither"
    )
  }

  open fun getFillOrderDescription(): String? {
    return getIndexedDescription(ExifDirectoryBase.TAG_FILL_ORDER, 1,
      "Normal",
      "Reversed"
    )
  }

  open fun getOrientationDescription(): String? {
    return super.getOrientationDescription(ExifDirectoryBase.TAG_ORIENTATION)
  }

  open fun getSamplesPerPixelDescription(): String? {
    val value = _directory.getString(ExifDirectoryBase.TAG_SAMPLES_PER_PIXEL)
    return if (value == null) null else "$value samples/pixel"
  }

  open fun getRowsPerStripDescription(): String? {
    val value = _directory.getString(ExifDirectoryBase.TAG_ROWS_PER_STRIP)
    return if (value == null) null else "$value rows/strip"
  }

  open fun getStripByteCountsDescription(): String? {
    val value = _directory.getString(ExifDirectoryBase.TAG_STRIP_BYTE_COUNTS)
    return if (value == null) null else "$value bytes"
  }

  open fun getXResolutionDescription(): String? {
    val value = _directory.getRational(ExifDirectoryBase.TAG_X_RESOLUTION) ?: return null
    val unit = getResolutionDescription()
    return String.format("%s dots per %s",
      value.toSimpleString(_allowDecimalRepresentationOfRationals),
      unit?.toLowerCase() ?: "unit")
  }

  open fun getYResolutionDescription(): String? {
    val value = _directory.getRational(ExifDirectoryBase.TAG_Y_RESOLUTION) ?: return null
    val unit = getResolutionDescription()
    return String.format("%s dots per %s",
      value.toSimpleString(_allowDecimalRepresentationOfRationals),
      unit?.toLowerCase() ?: "unit")
  }

  open fun getPlanarConfigurationDescription(): String? { // When image format is no compression YCbCr, this value shows byte aligns of YCbCr
// data. If value is '1', Y/Cb/Cr value is chunky format, contiguous for each subsampling
// pixel. If value is '2', Y/Cb/Cr value is separated and stored to Y plane/Cb plane/Cr
// plane format.
    return getIndexedDescription(ExifDirectoryBase.TAG_PLANAR_CONFIGURATION,
      1,
      "Chunky (contiguous for each subsampling pixel)",
      "Separate (Y-plane/Cb-plane/Cr-plane format)"
    )
  }

  open fun getResolutionDescription(): String? { // '1' means no-unit, '2' means inch, '3' means centimeter. Default value is '2'(inch)
    return getIndexedDescription(ExifDirectoryBase.TAG_RESOLUTION_UNIT, 1, "(No unit)", "Inch", "cm")
  }

  open fun getJpegProcDescription(): String? {
    val value = _directory.getInteger(ExifDirectoryBase.TAG_JPEG_PROC) ?: return null
    return when (value) {
      1 -> "Baseline"
      14 -> "Lossless"
      else -> "Unknown ($value)"
    }
  }

  open fun getYCbCrSubsamplingDescription(): String? {
    val positions = _directory.getIntArray(ExifDirectoryBase.TAG_YCBCR_SUBSAMPLING)
    if (positions == null || positions.size < 2) return null
    return if (positions[0] == 2 && positions[1] == 1) {
      "YCbCr4:2:2"
    } else if (positions[0] == 2 && positions[1] == 2) {
      "YCbCr4:2:0"
    } else {
      "(Unknown)"
    }
  }

  open fun getYCbCrPositioningDescription(): String? {
    return getIndexedDescription(ExifDirectoryBase.TAG_YCBCR_POSITIONING, 1, "Center of pixel array", "Datum point")
  }

  open fun getReferenceBlackWhiteDescription(): String? { // For some reason, sometimes this is read as a long[] and
// getIntArray isn't able to deal with it
    var ints = _directory.getIntArray(ExifDirectoryBase.TAG_REFERENCE_BLACK_WHITE)
    if (ints == null || ints.size < 6) {
      val o = _directory.getObject(ExifDirectoryBase.TAG_REFERENCE_BLACK_WHITE)
      if (o != null && o is LongArray) {
        if (o.size < 6) return null
        ints = IntArray(o.size)
        for (i in o.indices) ints[i] = o[i].toInt()
      } else return null
    }
    val blackR = ints[0]
    val whiteR = ints[1]
    val blackG = ints[2]
    val whiteG = ints[3]
    val blackB = ints[4]
    val whiteB = ints[5]
    return String.format("[%d,%d,%d] [%d,%d,%d]", blackR, blackG, blackB, whiteR, whiteG, whiteB)
  }

  /// <summary>
/// String description of CFA Pattern
/// </summary>
/// <remarks>
/// Indicates the color filter array (CFA) geometric pattern of the image sensor when a one-chip color area sensor is used.
/// It does not apply to all sensing methods.
///
/// ExifDirectoryBase.TAG_CFA_PATTERN_2 holds only the pixel pattern. ExifDirectoryBase.TAG_CFA_REPEAT_PATTERN_DIM is expected to exist and pass
/// some conditional tests.
/// </remarks>
  open fun getCfaPattern2Description(): String? {
    val values = _directory.getByteArray(ExifDirectoryBase.TAG_CFA_PATTERN_2) ?: return null
    val repeatPattern = _directory.getIntArray(ExifDirectoryBase.TAG_CFA_REPEAT_PATTERN_DIM)
      ?: return String.format("Repeat Pattern not found for CFAPattern (%s)", super.getDescription(ExifDirectoryBase.TAG_CFA_PATTERN_2))
    if (repeatPattern.size == 2 && values.size == repeatPattern[0] * repeatPattern[1]) {
      val intpattern = IntArray(2 + values.size)
      intpattern[0] = repeatPattern[0]
      intpattern[1] = repeatPattern[1]
      for (i in values.indices) intpattern[i + 2] = values[i].toInt() and 0xFF // convert the values[i] byte to unsigned
      return formatCFAPattern(intpattern)
    }
    return String.format("Unknown Pattern (%s)", super.getDescription(ExifDirectoryBase.TAG_CFA_PATTERN_2))
  }

  open fun getExposureTimeDescription(): String? {
    val value = _directory.getString(ExifDirectoryBase.TAG_EXPOSURE_TIME)
    return if (value == null) null else "$value sec"
  }

  open fun getFNumberDescription(): String? {
    val value = _directory.getRational(ExifDirectoryBase.TAG_FNUMBER) ?: return null
    return getFStopDescription(value.toDouble())
  }

  open fun getExposureProgramDescription(): String? {
    return getIndexedDescription(ExifDirectoryBase.TAG_EXPOSURE_PROGRAM,
      1,
      "Manual control",
      "Program normal",
      "Aperture priority",
      "Shutter priority",
      "Program creative (slow program)",
      "Program action (high-speed program)",
      "Portrait mode",
      "Landscape mode"
    )
  }

  open fun getIsoEquivalentDescription(): String? {
    // Have seen an exception here from files produced by ACDSEE that stored an int[] here with two values
    val isoEquiv = _directory.getInteger(ExifDirectoryBase.TAG_ISO_EQUIVALENT)
    // There used to be a check here that multiplied ISO values < 50 by 200.
    // Issue 36 shows a smart-phone image from a Samsung Galaxy S2 with ISO-40.
    return isoEquiv?.toString()
  }

  open fun getSensitivityTypeRangeDescription(): String? {
    return getIndexedDescription(ExifDirectoryBase.TAG_SENSITIVITY_TYPE,
      "Unknown",
      "Standard Output Sensitivity",
      "Recommended Exposure Index",
      "ISO Speed",
      "Standard Output Sensitivity and Recommended Exposure Index",
      "Standard Output Sensitivity and ISO Speed",
      "Recommended Exposure Index and ISO Speed",
      "Standard Output Sensitivity, Recommended Exposure Index and ISO Speed"
    )
  }

  open fun getExifVersionDescription(): String? {
    return getVersionBytesDescription(ExifDirectoryBase.TAG_EXIF_VERSION, 2)
  }

  open fun getComponentConfigurationDescription(): String? {
    val components = _directory.getIntArray(ExifDirectoryBase.TAG_COMPONENTS_CONFIGURATION) ?: return null
    val componentStrings = arrayOf("", "Y", "Cb", "Cr", "R", "G", "B")
    val componentConfig = StringBuilder()
    for (i in 0 until min(4, components.size)) {
      val j = components[i]
      if (j > 0 && j < componentStrings.size) {
        componentConfig.append(componentStrings[j])
      }
    }
    return componentConfig.toString()
  }

  open fun getCompressedAverageBitsPerPixelDescription(): String? {
    val value = _directory.getRational(ExifDirectoryBase.TAG_COMPRESSED_AVERAGE_BITS_PER_PIXEL) ?: return null
    val ratio = value.toSimpleString(_allowDecimalRepresentationOfRationals)
    return if (value.isInteger && value.toInt() == 1) "$ratio bit/pixel" else "$ratio bits/pixel"
  }

  open fun getShutterSpeedDescription(): String? {
    return super.getShutterSpeedDescription(ExifDirectoryBase.TAG_SHUTTER_SPEED)
  }

  open fun getApertureValueDescription(): String? {
    val aperture = _directory.getDoubleObject(ExifDirectoryBase.TAG_APERTURE) ?: return null
    val fStop = apertureToFStop(aperture)
    return getFStopDescription(fStop)
  }

  open fun getBrightnessValueDescription(): String? {
    val value = _directory.getRational(ExifDirectoryBase.TAG_BRIGHTNESS_VALUE) ?: return null
    if (value.numerator == 0xFFFFFFFFL) return "Unknown"
    val formatter = DecimalFormat("0.0##")
    return formatter.format(value.toDouble())
  }

  open fun getExposureBiasDescription(): String? {
    val value = _directory.getRational(ExifDirectoryBase.TAG_EXPOSURE_BIAS) ?: return null
    return value.toSimpleString(true) + " EV"
  }

  open fun getMaxApertureValueDescription(): String? {
    val aperture = _directory.getDoubleObject(ExifDirectoryBase.TAG_MAX_APERTURE) ?: return null
    val fStop = apertureToFStop(aperture)
    return getFStopDescription(fStop)
  }

  open fun getSubjectDistanceDescription(): String? {
    val value = _directory.getRational(ExifDirectoryBase.TAG_SUBJECT_DISTANCE) ?: return null
    if (value.numerator == 0xFFFFFFFFL) return "Infinity"
    if (value.numerator == 0L) return "Unknown"
    val formatter = DecimalFormat("0.0##")
    return formatter.format(value.toDouble()) + " metres"
  }

  open fun getMeteringModeDescription(): String? { // '0' means unknown, '1' average, '2' center weighted average, '3' spot
// '4' multi-spot, '5' multi-segment, '6' partial, '255' other
    val value = _directory.getInteger(ExifDirectoryBase.TAG_METERING_MODE) ?: return null
    return when (value) {
      0 -> "Unknown"
      1 -> "Average"
      2 -> "Center weighted average"
      3 -> "Spot"
      4 -> "Multi-spot"
      5 -> "Multi-segment"
      6 -> "Partial"
      255 -> "(Other)"
      else -> "Unknown ($value)"
    }
  }

  open fun getWhiteBalanceDescription(): String? {
    val value = _directory.getInteger(ExifDirectoryBase.TAG_WHITE_BALANCE) ?: return null
    return getWhiteBalanceDescription(value)
  }

  open fun getFlashDescription(): String? { /*
         * This is a bit mask.
         * 0 = flash fired
         * 1 = return detected
         * 2 = return able to be detected
         * 3 = unknown
         * 4 = auto used
         * 5 = unknown
         * 6 = red eye reduction used
         */
    val value = _directory.getInteger(ExifDirectoryBase.TAG_FLASH) ?: return null
    val sb = java.lang.StringBuilder()
    if (value and 0x1 != 0) sb.append("Flash fired") else sb.append("Flash did not fire")
    // check if we're able to detect a return, before we mention it
    if (value and 0x4 != 0) {
      if (value and 0x2 != 0) sb.append(", return detected") else sb.append(", return not detected")
    }
    // If 0x10 is set and the lowest byte is not zero - then flash is Auto
    if (value and 0x10 != 0 && value and 0x0F != 0) sb.append(", auto")
    if (value and 0x40 != 0) sb.append(", red-eye reduction")
    return sb.toString()
  }

  open fun getFocalLengthDescription(): String? {
    val value = _directory.getRational(ExifDirectoryBase.TAG_FOCAL_LENGTH)
    return if (value == null) null else getFocalLengthDescription(value.toDouble())
  }

  open fun getUserCommentDescription(): String? {
    return getEncodedTextDescription(ExifDirectoryBase.TAG_USER_COMMENT)
  }

  open fun getTemperatureDescription(): String? {
    val value = _directory.getRational(ExifDirectoryBase.TAG_TEMPERATURE) ?: return null
    if (value.denominator == 0xFFFFFFFFL) return "Unknown"
    val formatter = DecimalFormat("0.0")
    return formatter.format(value.toDouble()) + " °C"
  }

  open fun getHumidityDescription(): String? {
    val value = _directory.getRational(ExifDirectoryBase.TAG_HUMIDITY) ?: return null
    if (value.denominator == 0xFFFFFFFFL) return "Unknown"
    val formatter = DecimalFormat("0.0")
    return formatter.format(value.toDouble()) + " %"
  }

  open fun getPressureDescription(): String? {
    val value = _directory.getRational(ExifDirectoryBase.TAG_PRESSURE) ?: return null
    if (value.denominator == 0xFFFFFFFFL) return "Unknown"
    val formatter = DecimalFormat("0.0")
    return formatter.format(value.toDouble()) + " hPa"
  }

  open fun getWaterDepthDescription(): String? {
    val value = _directory.getRational(ExifDirectoryBase.TAG_WATER_DEPTH) ?: return null
    if (value.denominator == 0xFFFFFFFFL) return "Unknown"
    val formatter = DecimalFormat("0.0##")
    return formatter.format(value.toDouble()) + " metres"
  }

  open fun getAccelerationDescription(): String? {
    val value = _directory.getRational(ExifDirectoryBase.TAG_ACCELERATION) ?: return null
    if (value.denominator == 0xFFFFFFFFL) return "Unknown"
    val formatter = DecimalFormat("0.0##")
    return formatter.format(value.toDouble()) + " mGal"
  }

  open fun getCameraElevationAngleDescription(): String? {
    val value = _directory.getRational(ExifDirectoryBase.TAG_CAMERA_ELEVATION_ANGLE) ?: return null
    if (value.denominator == 0xFFFFFFFFL) return "Unknown"
    val formatter = DecimalFormat("0.##")
    return formatter.format(value.toDouble()) + " degrees"
  }

  /** The Windows specific tags uses plain Unicode.  */
  private fun getUnicodeDescription(tag: Int): String? {
    val bytes = _directory.getByteArray(tag) ?: return null
    return try { // Decode the unicode string and trim the unicode zero "\0" from the end.
      String(bytes, UTF_16LE).trim { it <= ' ' }
    } catch (ex: UnsupportedEncodingException) {
      null
    }
  }

  open fun getWindowsTitleDescription(): String? {
    return getUnicodeDescription(ExifDirectoryBase.TAG_WIN_TITLE)
  }

  open fun getWindowsCommentDescription(): String? {
    return getUnicodeDescription(ExifDirectoryBase.TAG_WIN_COMMENT)
  }

  open fun getWindowsAuthorDescription(): String? {
    return getUnicodeDescription(ExifDirectoryBase.TAG_WIN_AUTHOR)
  }

  open fun getWindowsKeywordsDescription(): String? {
    return getUnicodeDescription(ExifDirectoryBase.TAG_WIN_KEYWORDS)
  }

  open fun getWindowsSubjectDescription(): String? {
    return getUnicodeDescription(ExifDirectoryBase.TAG_WIN_SUBJECT)
  }

  open fun getFlashPixVersionDescription(): String? {
    return getVersionBytesDescription(ExifDirectoryBase.TAG_FLASHPIX_VERSION, 2)
  }

  open fun getColorSpaceDescription(): String? {
    val value = _directory.getInteger(ExifDirectoryBase.TAG_COLOR_SPACE) ?: return null
    if (value == 1) return "sRGB"
    return if (value == 65535) "Undefined" else "Unknown ($value)"
  }

  open fun getExifImageWidthDescription(): String? {
    val value = _directory.getInteger(ExifDirectoryBase.TAG_EXIF_IMAGE_WIDTH)
    return if (value == null) null else "$value pixels"
  }

  open fun getExifImageHeightDescription(): String? {
    val value = _directory.getInteger(ExifDirectoryBase.TAG_EXIF_IMAGE_HEIGHT)
    return if (value == null) null else "$value pixels"
  }

  open fun getFocalPlaneXResolutionDescription(): String? {
    val rational = _directory.getRational(ExifDirectoryBase.TAG_FOCAL_PLANE_X_RESOLUTION) ?: return null
    val unit = getFocalPlaneResolutionUnitDescription()
    return (rational.reciprocal.toSimpleString(_allowDecimalRepresentationOfRationals)
      + if (unit == null) "" else " " + unit.toLowerCase())
  }

  open fun getFocalPlaneYResolutionDescription(): String? {
    val rational = _directory.getRational(ExifDirectoryBase.TAG_FOCAL_PLANE_Y_RESOLUTION) ?: return null
    val unit = getFocalPlaneResolutionUnitDescription()
    return (rational.reciprocal.toSimpleString(_allowDecimalRepresentationOfRationals)
      + if (unit == null) "" else " " + unit.toLowerCase())
  }

  open fun getFocalPlaneResolutionUnitDescription(): String? { // Unit of FocalPlaneXResolution/FocalPlaneYResolution.
// '1' means no-unit, '2' inch, '3' centimeter.
    return getIndexedDescription(ExifDirectoryBase.TAG_FOCAL_PLANE_RESOLUTION_UNIT,
      1,
      "(No unit)",
      "Inches",
      "cm"
    )
  }

  open fun getSensingMethodDescription(): String? { // '1' Not defined, '2' One-chip color area sensor, '3' Two-chip color area sensor
// '4' Three-chip color area sensor, '5' Color sequential area sensor
// '7' Trilinear sensor '8' Color sequential linear sensor,  'Other' reserved
    return getIndexedDescription(ExifDirectoryBase.TAG_SENSING_METHOD,
      1,
      "(Not defined)",
      "One-chip color area sensor",
      "Two-chip color area sensor",
      "Three-chip color area sensor",
      "Color sequential area sensor",
      null,
      "Trilinear sensor",
      "Color sequential linear sensor"
    )
  }

  open fun getFileSourceDescription(): String? {
    return getIndexedDescription(ExifDirectoryBase.TAG_FILE_SOURCE,
      1,
      "Film Scanner",
      "Reflection Print Scanner",
      "Digital Still Camera (DSC)"
    )
  }

  open fun getSceneTypeDescription(): String? {
    return getIndexedDescription(ExifDirectoryBase.TAG_SCENE_TYPE,
      1,
      "Directly photographed image"
    )
  }

  /// <summary>
/// String description of CFA Pattern
/// </summary>
/// <remarks>
/// Converted from Exiftool version 10.33 created by Phil Harvey
/// http://www.sno.phy.queensu.ca/~phil/exiftool/
/// lib\Image\ExifTool\Exif.pm
///
/// Indicates the color filter array (CFA) geometric pattern of the image sensor when a one-chip color area sensor is used.
/// It does not apply to all sensing methods.
/// </remarks>
  open fun getCfaPatternDescription(): String? {
    return formatCFAPattern(decodeCfaPattern(ExifDirectoryBase.TAG_CFA_PATTERN))
  }

  /// <summary>
  /// Decode raw CFAPattern value
  /// </summary>
  /// <remarks>
  /// Converted from Exiftool version 10.33 created by Phil Harvey
  /// http://www.sno.phy.queensu.ca/~phil/exiftool/
  /// lib\Image\ExifTool\Exif.pm
  ///
  /// The value consists of:
  /// - Two short, being the grid width and height of the repeated pattern.
  /// - Next, for every pixel in that pattern, an identification code.
  /// </remarks>
  private fun decodeCfaPattern(tagType: Int): IntArray? {
    val ret: IntArray
    val values = _directory.getByteArray(tagType) ?: return null
    if (values.size < 4) {
      ret = IntArray(values.size)
      for (i in values.indices) ret[i] = values[i].toInt()
      return ret
    }
    ret = IntArray(values.size - 2)
    try {
      val reader = ByteArrayReader(values)
      // first two values should be read as 16-bits (2 bytes)
      var item0 = reader.getInt16(0)
      var item1 = reader.getInt16(2)
      var copyArray = false
      val end = 2 + item0 * item1
      if (end > values.size) // sanity check in case of byte order problems; calculated 'end' should be <= length of the values
      { // try swapping byte order (I have seen this order different than in EXIF)
        reader.isMotorolaByteOrder = !reader.isMotorolaByteOrder
        item0 = reader.getInt16(0)
        item1 = reader.getInt16(2)
        if (values.size >= 2 + item0 * item1) copyArray = true
      } else copyArray = true
      if (copyArray) {
        ret[0] = item0.toInt()
        ret[1] = item1.toInt()
        for (i in 4 until values.size) ret[i - 2] = reader.getInt8(i).toInt()
      }
    } catch (ex: IOException) {
      _directory.addError("IO exception processing data: ${ex.message}")
    }
    return ret
  }

  open fun getCustomRenderedDescription(): String? {
    return getIndexedDescription(ExifDirectoryBase.TAG_CUSTOM_RENDERED,
      "Normal process",
      "Custom process"
    )
  }

  open fun getExposureModeDescription(): String? {
    return getIndexedDescription(ExifDirectoryBase.TAG_EXPOSURE_MODE,
      "Auto exposure",
      "Manual exposure",
      "Auto bracket"
    )
  }

  open fun getWhiteBalanceModeDescription(): String? {
    return getIndexedDescription(ExifDirectoryBase.TAG_WHITE_BALANCE_MODE,
      "Auto white balance",
      "Manual white balance"
    )
  }

  open fun getDigitalZoomRatioDescription(): String? {
    val value = _directory.getRational(ExifDirectoryBase.TAG_DIGITAL_ZOOM_RATIO)
    return if (value == null) null else if (value.numerator == 0L) "Digital zoom not used" else DecimalFormat("0.#").format(value.toDouble())
  }

  open fun get35mmFilmEquivFocalLengthDescription(): String? {
    val value = _directory.getInteger(ExifDirectoryBase.TAG_35MM_FILM_EQUIV_FOCAL_LENGTH)
    return if (value == null) null else if (value == 0) "Unknown" else getFocalLengthDescription(value.toDouble())
  }

  open fun getSceneCaptureTypeDescription(): String? {
    return getIndexedDescription(ExifDirectoryBase.TAG_SCENE_CAPTURE_TYPE,
      "Standard",
      "Landscape",
      "Portrait",
      "Night scene"
    )
  }

  open fun getGainControlDescription(): String? {
    return getIndexedDescription(ExifDirectoryBase.TAG_GAIN_CONTROL,
      "None",
      "Low gain up",
      "Low gain down",
      "High gain up",
      "High gain down"
    )
  }

  open fun getContrastDescription(): String? {
    return getIndexedDescription(ExifDirectoryBase.TAG_CONTRAST,
      "None",
      "Soft",
      "Hard"
    )
  }

  open fun getSaturationDescription(): String? {
    return getIndexedDescription(ExifDirectoryBase.TAG_SATURATION,
      "None",
      "Low saturation",
      "High saturation"
    )
  }

  open fun getSharpnessDescription(): String? {
    return getIndexedDescription(ExifDirectoryBase.TAG_SHARPNESS,
      "None",
      "Low",
      "Hard"
    )
  }

  open fun getSubjectDistanceRangeDescription(): String? {
    return getIndexedDescription(ExifDirectoryBase.TAG_SUBJECT_DISTANCE_RANGE,
      "Unknown",
      "Macro",
      "Close view",
      "Distant view"
    )
  }

  open fun getLensSpecificationDescription(): String? {
    return getLensSpecificationDescription(ExifDirectoryBase.TAG_LENS_SPECIFICATION)
  }

  companion object {
    @JvmStatic
    private fun formatCFAPattern(pattern: IntArray?): String? {
      if (pattern == null) return null
      if (pattern.size < 2) return "<truncated data>"
      if (pattern[0] == 0 && pattern[1] == 0) return "<zero pattern size>"
      val end = 2 + pattern[0] * pattern[1]
      if (end > pattern.size) return "<invalid pattern size>"
      val cfaColors = arrayOf("Red", "Green", "Blue", "Cyan", "Magenta", "Yellow", "White")
      val ret = StringBuilder()
      ret.append("[")
      for (pos in 2 until end) {
        if (pattern[pos] <= cfaColors.size - 1) ret.append(cfaColors[pattern[pos]]) else ret.append("Unknown") // indicated pattern position is outside the array bounds
        if ((pos - 2) % pattern[1] == 0) ret.append(",") else if (pos != end - 1) ret.append("][")
      }
      ret.append("]")
      return ret.toString()
    }

    @JvmStatic
    fun getWhiteBalanceDescription(value: Int): String { // See http://web.archive.org/web/20131018091152/http://exif.org/Exif2-2.PDF page 35
      return when (value) {
        0 -> "Unknown"
        1 -> "Daylight"
        2 -> "Florescent"
        3 -> "Tungsten (Incandescent)"
        4 -> "Flash"
        9 -> "Fine Weather"
        10 -> "Cloudy"
        11 -> "Shade"
        12 -> "Daylight Fluorescent" // (D 5700 - 7100K)
        13 -> "Day White Fluorescent" // (N 4600 - 5500K)
        14 -> "Cool White Fluorescent" // (W 3800 - 4500K)
        15 -> "White Fluorescent" // (WW 3250 - 3800K)
        16 -> "Warm White Fluorescent" // (L 2600 - 3250K)
        17 -> "Standard light A"
        18 -> "Standard light B"
        19 -> "Standard light C"
        20 -> "D55"
        21 -> "D65"
        22 -> "D75"
        23 -> "D50"
        24 -> "ISO Studio Tungsten"
        255 -> "Other"
        else -> "Unknown ($value)"
      }
    }
  }
}