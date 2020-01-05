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
package com.drew.metadata.exif

import com.drew.metadata.Directory
import java.util.*

/**
 * Base class for several Exif format tag directories.
 *
 * @author Drew Noakes https://drewnoakes.com
 */
abstract class ExifDirectoryBase : Directory() {
  companion object {
    const val TAG_INTEROP_INDEX = 0x0001
    const val TAG_INTEROP_VERSION = 0x0002
    /**
     * The new subfile type tag.
     * 0 = Full-resolution Image
     * 1 = Reduced-resolution image
     * 2 = Single page of multi-page image
     * 3 = Single page of multi-page reduced-resolution image
     * 4 = Transparency mask
     * 5 = Transparency mask of reduced-resolution image
     * 6 = Transparency mask of multi-page image
     * 7 = Transparency mask of reduced-resolution multi-page image
     */
    const val TAG_NEW_SUBFILE_TYPE = 0x00FE
    /**
     * The old subfile type tag.
     * 1 = Full-resolution image (Main image)
     * 2 = Reduced-resolution image (Thumbnail)
     * 3 = Single page of multi-page image
     */
    const val TAG_SUBFILE_TYPE = 0x00FF
    const val TAG_IMAGE_WIDTH = 0x0100
    const val TAG_IMAGE_HEIGHT = 0x0101
    /**
     * When image format is no compression, this value shows the number of bits
     * per component for each pixel. Usually this value is '8,8,8'.
     */
    const val TAG_BITS_PER_SAMPLE = 0x0102
    const val TAG_COMPRESSION = 0x0103
    /**
     * Shows the color space of the image data components.
     * 0 = WhiteIsZero
     * 1 = BlackIsZero
     * 2 = RGB
     * 3 = RGB Palette
     * 4 = Transparency Mask
     * 5 = CMYK
     * 6 = YCbCr
     * 8 = CIELab
     * 9 = ICCLab
     * 10 = ITULab
     * 32803 = Color Filter Array
     * 32844 = Pixar LogL
     * 32845 = Pixar LogLuv
     * 34892 = Linear Raw
     */
    const val TAG_PHOTOMETRIC_INTERPRETATION = 0x0106
    /**
     * 1 = No dithering or halftoning
     * 2 = Ordered dither or halftone
     * 3 = Randomized dither
     */
    const val TAG_THRESHOLDING = 0x0107
    /**
     * 1 = Normal
     * 2 = Reversed
     */
    const val TAG_FILL_ORDER = 0x010A
    const val TAG_DOCUMENT_NAME = 0x010D
    const val TAG_IMAGE_DESCRIPTION = 0x010E
    const val TAG_MAKE = 0x010F
    const val TAG_MODEL = 0x0110
    /** The position in the file of raster data.  */
    const val TAG_STRIP_OFFSETS = 0x0111
    const val TAG_ORIENTATION = 0x0112
    /** Each pixel is composed of this many samples.  */
    const val TAG_SAMPLES_PER_PIXEL = 0x0115
    /** The raster is codified by a single block of data holding this many rows.  */
    const val TAG_ROWS_PER_STRIP = 0x0116
    /** The size of the raster data in bytes.  */
    const val TAG_STRIP_BYTE_COUNTS = 0x0117
    const val TAG_MIN_SAMPLE_VALUE = 0x0118
    const val TAG_MAX_SAMPLE_VALUE = 0x0119
    const val TAG_X_RESOLUTION = 0x011A
    const val TAG_Y_RESOLUTION = 0x011B
    /**
     * When image format is no compression YCbCr, this value shows byte aligns of
     * YCbCr data. If value is '1', Y/Cb/Cr value is chunky format, contiguous for
     * each subsampling pixel. If value is '2', Y/Cb/Cr value is separated and
     * stored to Y plane/Cb plane/Cr plane format.
     */
    const val TAG_PLANAR_CONFIGURATION = 0x011C
    const val TAG_PAGE_NAME = 0x011D
    const val TAG_RESOLUTION_UNIT = 0x0128
    const val TAG_PAGE_NUMBER = 0x0129
    const val TAG_TRANSFER_FUNCTION = 0x012D
    const val TAG_SOFTWARE = 0x0131
    const val TAG_DATETIME = 0x0132
    const val TAG_ARTIST = 0x013B
    const val TAG_HOST_COMPUTER = 0x013C
    const val TAG_PREDICTOR = 0x013D
    const val TAG_WHITE_POINT = 0x013E
    const val TAG_PRIMARY_CHROMATICITIES = 0x013F
    const val TAG_TILE_WIDTH = 0x0142
    const val TAG_TILE_LENGTH = 0x0143
    const val TAG_TILE_OFFSETS = 0x0144
    const val TAG_TILE_BYTE_COUNTS = 0x0145
    /**
     * Tag is a pointer to one or more sub-IFDs.
     * + Seems to be used exclusively by raw formats, referencing one or two IFDs.
     */
    const val TAG_SUB_IFD_OFFSET = 0x014a
    const val TAG_TRANSFER_RANGE = 0x0156
    const val TAG_JPEG_TABLES = 0x015B
    const val TAG_JPEG_PROC = 0x0200
    // 0x0201 can have all kinds of descriptions for thumbnail starting index
    // 0x0202 can have all kinds of descriptions for thumbnail length
    const val TAG_JPEG_RESTART_INTERVAL = 0x0203
    const val TAG_JPEG_LOSSLESS_PREDICTORS = 0x0205
    const val TAG_JPEG_POINT_TRANSFORMS = 0x0206
    const val TAG_JPEG_Q_TABLES = 0x0207
    const val TAG_JPEG_DC_TABLES = 0x0208
    const val TAG_JPEG_AC_TABLES = 0x0209
    const val TAG_YCBCR_COEFFICIENTS = 0x0211
    const val TAG_YCBCR_SUBSAMPLING = 0x0212
    const val TAG_YCBCR_POSITIONING = 0x0213
    const val TAG_REFERENCE_BLACK_WHITE = 0x0214
    const val TAG_STRIP_ROW_COUNTS = 0x022f
    const val TAG_APPLICATION_NOTES = 0x02bc
    const val TAG_RELATED_IMAGE_FILE_FORMAT = 0x1000
    const val TAG_RELATED_IMAGE_WIDTH = 0x1001
    const val TAG_RELATED_IMAGE_HEIGHT = 0x1002
    const val TAG_RATING = 0x4746
    const val TAG_CFA_REPEAT_PATTERN_DIM = 0x828D
    /** There are two definitions for CFA pattern, I don't know the difference...  */
    const val TAG_CFA_PATTERN_2 = 0x828E
    const val TAG_BATTERY_LEVEL = 0x828F
    const val TAG_COPYRIGHT = 0x8298
    /**
     * Exposure time (reciprocal of shutter speed). Unit is second.
     */
    const val TAG_EXPOSURE_TIME = 0x829A
    /**
     * The actual F-number(F-stop) of lens when the image was taken.
     */
    const val TAG_FNUMBER = 0x829D
    const val TAG_IPTC_NAA = 0x83BB
    const val TAG_PHOTOSHOP_SETTINGS = 0x8649
    const val TAG_INTER_COLOR_PROFILE = 0x8773
    /**
     * Exposure program that the camera used when image was taken. '1' means
     * manual control, '2' program normal, '3' aperture priority, '4' shutter
     * priority, '5' program creative (slow program), '6' program action
     * (high-speed program), '7' portrait mode, '8' landscape mode.
     */
    const val TAG_EXPOSURE_PROGRAM = 0x8822
    const val TAG_SPECTRAL_SENSITIVITY = 0x8824
    const val TAG_ISO_EQUIVALENT = 0x8827
    /**
     * Indicates the Opto-Electric Conversion Function (OECF) specified in ISO 14524.
     *
     *
     * OECF is the relationship between the camera optical input and the image values.
     *
     *
     * The values are:
     *
     *  * Two shorts, indicating respectively number of columns, and number of rows.
     *  * For each column, the column name in a null-terminated ASCII string.
     *  * For each cell, an SRATIONAL value.
     *
     */
    const val TAG_OPTO_ELECTRIC_CONVERSION_FUNCTION = 0x8828
    const val TAG_INTERLACE = 0x8829
    const val TAG_TIME_ZONE_OFFSET_TIFF_EP = 0x882A
    const val TAG_SELF_TIMER_MODE_TIFF_EP = 0x882B
    /**
     * Applies to ISO tag.
     *
     * 0 = Unknown
     * 1 = Standard Output Sensitivity
     * 2 = Recommended Exposure Index
     * 3 = ISO Speed
     * 4 = Standard Output Sensitivity and Recommended Exposure Index
     * 5 = Standard Output Sensitivity and ISO Speed
     * 6 = Recommended Exposure Index and ISO Speed
     * 7 = Standard Output Sensitivity, Recommended Exposure Index and ISO Speed
     */
    const val TAG_SENSITIVITY_TYPE = 0x8830
    const val TAG_STANDARD_OUTPUT_SENSITIVITY = 0x8831
    const val TAG_RECOMMENDED_EXPOSURE_INDEX = 0x8832
    const val TAG_ISO_SPEED = 0x8833
    const val TAG_ISO_SPEED_LATITUDE_YYY = 0x8834
    const val TAG_ISO_SPEED_LATITUDE_ZZZ = 0x8835
    const val TAG_EXIF_VERSION = 0x9000
    const val TAG_DATETIME_ORIGINAL = 0x9003
    const val TAG_DATETIME_DIGITIZED = 0x9004
    const val TAG_OFFSET_TIME = 0x9010
    const val TAG_OFFSET_TIME_ORIGINAL = 0x9011
    const val TAG_OFFSET_TIME_DIGITIZED = 0x9012
    const val TAG_COMPONENTS_CONFIGURATION = 0x9101
    /**
     * Average (rough estimate) compression level in JPEG bits per pixel.
     */
    const val TAG_COMPRESSED_AVERAGE_BITS_PER_PIXEL = 0x9102
    /**
     * Shutter speed by APEX value. To convert this value to ordinary 'Shutter Speed';
     * calculate this value's power of 2, then reciprocal. For example, if the
     * ShutterSpeedValue is '4', shutter speed is 1/(24)=1/16 second.
     */
    const val TAG_SHUTTER_SPEED = 0x9201
    /**
     * The actual aperture value of lens when the image was taken. Unit is APEX.
     * To convert this value to ordinary F-number (F-stop), calculate this value's
     * power of root 2 (=1.4142). For example, if the ApertureValue is '5',
     * F-number is 1.4142^5 = F5.6.
     */
    const val TAG_APERTURE = 0x9202
    const val TAG_BRIGHTNESS_VALUE = 0x9203
    const val TAG_EXPOSURE_BIAS = 0x9204
    /**
     * Maximum aperture value of lens. You can convert to F-number by calculating
     * power of root 2 (same process of ApertureValue:0x9202).
     * The actual aperture value of lens when the image was taken. To convert this
     * value to ordinary f-number(f-stop), calculate the value's power of root 2
     * (=1.4142). For example, if the ApertureValue is '5', f-number is 1.41425^5 = F5.6.
     */
    const val TAG_MAX_APERTURE = 0x9205
    /**
     * Indicates the distance the autofocus camera is focused to.  Tends to be less accurate as distance increases.
     */
    const val TAG_SUBJECT_DISTANCE = 0x9206
    /**
     * Exposure metering method. '0' means unknown, '1' average, '2' center
     * weighted average, '3' spot, '4' multi-spot, '5' multi-segment, '6' partial,
     * '255' other.
     */
    const val TAG_METERING_MODE = 0x9207

    @Deprecated("use {@link com.drew.metadata.exif.ExifDirectoryBase#TAG_WHITE_BALANCE} instead.")
    val TAG_LIGHT_SOURCE = 0x9208
    /**
     * White balance (aka light source). '0' means unknown, '1' daylight,
     * '2' fluorescent, '3' tungsten, '10' flash, '17' standard light A,
     * '18' standard light B, '19' standard light C, '20' D55, '21' D65,
     * '22' D75, '255' other.
     */
    const val TAG_WHITE_BALANCE = 0x9208
    /**
     * 0x0  = 0000000 = No Flash
     * 0x1  = 0000001 = Fired
     * 0x5  = 0000101 = Fired, Return not detected
     * 0x7  = 0000111 = Fired, Return detected
     * 0x9  = 0001001 = On
     * 0xd  = 0001101 = On, Return not detected
     * 0xf  = 0001111 = On, Return detected
     * 0x10 = 0010000 = Off
     * 0x18 = 0011000 = Auto, Did not fire
     * 0x19 = 0011001 = Auto, Fired
     * 0x1d = 0011101 = Auto, Fired, Return not detected
     * 0x1f = 0011111 = Auto, Fired, Return detected
     * 0x20 = 0100000 = No flash function
     * 0x41 = 1000001 = Fired, Red-eye reduction
     * 0x45 = 1000101 = Fired, Red-eye reduction, Return not detected
     * 0x47 = 1000111 = Fired, Red-eye reduction, Return detected
     * 0x49 = 1001001 = On, Red-eye reduction
     * 0x4d = 1001101 = On, Red-eye reduction, Return not detected
     * 0x4f = 1001111 = On, Red-eye reduction, Return detected
     * 0x59 = 1011001 = Auto, Fired, Red-eye reduction
     * 0x5d = 1011101 = Auto, Fired, Red-eye reduction, Return not detected
     * 0x5f = 1011111 = Auto, Fired, Red-eye reduction, Return detected
     * 6543210 (positions)
     *
     * This is a bitmask.
     * 0 = flash fired
     * 1 = return detected
     * 2 = return able to be detected
     * 3 = unknown
     * 4 = auto used
     * 5 = unknown
     * 6 = red eye reduction used
     */
    const val TAG_FLASH = 0x9209
    /**
     * Focal length of lens used to take image.  Unit is millimeter.
     * Nice digital cameras actually save the focal length as a function of how far they are zoomed in.
     */
    const val TAG_FOCAL_LENGTH = 0x920A
    const val TAG_FLASH_ENERGY_TIFF_EP = 0x920B
    const val TAG_SPATIAL_FREQ_RESPONSE_TIFF_EP = 0x920C
    const val TAG_NOISE = 0x920D
    const val TAG_FOCAL_PLANE_X_RESOLUTION_TIFF_EP = 0x920E
    const val TAG_FOCAL_PLANE_Y_RESOLUTION_TIFF_EP = 0x920F
    const val TAG_IMAGE_NUMBER = 0x9211
    const val TAG_SECURITY_CLASSIFICATION = 0x9212
    const val TAG_IMAGE_HISTORY = 0x9213
    const val TAG_SUBJECT_LOCATION_TIFF_EP = 0x9214
    const val TAG_EXPOSURE_INDEX_TIFF_EP = 0x9215
    const val TAG_STANDARD_ID_TIFF_EP = 0x9216
    /**
     * This tag holds the Exif Makernote. Makernotes are free to be in any format, though they are often IFDs.
     * To determine the format, we consider the starting bytes of the makernote itself and sometimes the
     * camera model and make.
     *
     *
     * The component count for this tag includes all of the bytes needed for the makernote.
     */
    const val TAG_MAKERNOTE = 0x927C
    const val TAG_USER_COMMENT = 0x9286
    const val TAG_SUBSECOND_TIME = 0x9290
    const val TAG_SUBSECOND_TIME_ORIGINAL = 0x9291
    const val TAG_SUBSECOND_TIME_DIGITIZED = 0x9292
    const val TAG_TEMPERATURE = 0x9400
    const val TAG_HUMIDITY = 0x9401
    const val TAG_PRESSURE = 0x9402
    const val TAG_WATER_DEPTH = 0x9403
    const val TAG_ACCELERATION = 0x9404
    const val TAG_CAMERA_ELEVATION_ANGLE = 0x9405
    /** The image title, as used by Windows XP.  */
    const val TAG_WIN_TITLE = 0x9C9B
    /** The image comment, as used by Windows XP.  */
    const val TAG_WIN_COMMENT = 0x9C9C
    /** The image author, as used by Windows XP (called Artist in the Windows shell).  */
    const val TAG_WIN_AUTHOR = 0x9C9D
    /** The image keywords, as used by Windows XP.  */
    const val TAG_WIN_KEYWORDS = 0x9C9E
    /** The image subject, as used by Windows XP.  */
    const val TAG_WIN_SUBJECT = 0x9C9F
    const val TAG_FLASHPIX_VERSION = 0xA000
    /**
     * Defines Color Space. DCF image must use sRGB color space so value is
     * always '1'. If the picture uses the other color space, value is
     * '65535':Uncalibrated.
     */
    const val TAG_COLOR_SPACE = 0xA001
    const val TAG_EXIF_IMAGE_WIDTH = 0xA002
    const val TAG_EXIF_IMAGE_HEIGHT = 0xA003
    const val TAG_RELATED_SOUND_FILE = 0xA004
    const val TAG_FLASH_ENERGY = 0xA20B
    const val TAG_SPATIAL_FREQ_RESPONSE = 0xA20C
    const val TAG_FOCAL_PLANE_X_RESOLUTION = 0xA20E
    const val TAG_FOCAL_PLANE_Y_RESOLUTION = 0xA20F
    /**
     * Unit of FocalPlaneXResolution/FocalPlaneYResolution. '1' means no-unit,
     * '2' inch, '3' centimeter.
     *
     * Note: Some of Fujifilm's digicam(e.g.FX2700,FX2900,Finepix4700Z/40i etc)
     * uses value '3' so it must be 'centimeter', but it seems that they use a
     * '8.3mm?'(1/3in.?) to their ResolutionUnit. Fuji's BUG? Finepix4900Z has
     * been changed to use value '2' but it doesn't match to actual value also.
     */
    const val TAG_FOCAL_PLANE_RESOLUTION_UNIT = 0xA210
    const val TAG_SUBJECT_LOCATION = 0xA214
    const val TAG_EXPOSURE_INDEX = 0xA215
    const val TAG_SENSING_METHOD = 0xA217
    const val TAG_FILE_SOURCE = 0xA300
    const val TAG_SCENE_TYPE = 0xA301
    const val TAG_CFA_PATTERN = 0xA302
    /**
     * This tag indicates the use of special processing on image data, such as rendering
     * geared to output. When special processing is performed, the reader is expected to
     * disable or minimize any further processing.
     * Tag = 41985 (A401.H)
     * Type = SHORT
     * Count = 1
     * Default = 0
     * 0 = Normal process
     * 1 = Custom process
     * Other = reserved
     */
    const val TAG_CUSTOM_RENDERED = 0xA401
    /**
     * This tag indicates the exposure mode set when the image was shot. In auto-bracketing
     * mode, the camera shoots a series of frames of the same scene at different exposure settings.
     * Tag = 41986 (A402.H)
     * Type = SHORT
     * Count = 1
     * Default = none
     * 0 = Auto exposure
     * 1 = Manual exposure
     * 2 = Auto bracket
     * Other = reserved
     */
    const val TAG_EXPOSURE_MODE = 0xA402
    /**
     * This tag indicates the white balance mode set when the image was shot.
     * Tag = 41987 (A403.H)
     * Type = SHORT
     * Count = 1
     * Default = none
     * 0 = Auto white balance
     * 1 = Manual white balance
     * Other = reserved
     */
    const val TAG_WHITE_BALANCE_MODE = 0xA403
    /**
     * This tag indicates the digital zoom ratio when the image was shot. If the
     * numerator of the recorded value is 0, this indicates that digital zoom was
     * not used.
     * Tag = 41988 (A404.H)
     * Type = RATIONAL
     * Count = 1
     * Default = none
     */
    const val TAG_DIGITAL_ZOOM_RATIO = 0xA404
    /**
     * This tag indicates the equivalent focal length assuming a 35mm film camera,
     * in mm. A value of 0 means the focal length is unknown. Note that this tag
     * differs from the FocalLength tag.
     * Tag = 41989 (A405.H)
     * Type = SHORT
     * Count = 1
     * Default = none
     */
    const val TAG_35MM_FILM_EQUIV_FOCAL_LENGTH = 0xA405
    /**
     * This tag indicates the type of scene that was shot. It can also be used to
     * record the mode in which the image was shot. Note that this differs from
     * the scene type (SceneType) tag.
     * Tag = 41990 (A406.H)
     * Type = SHORT
     * Count = 1
     * Default = 0
     * 0 = Standard
     * 1 = Landscape
     * 2 = Portrait
     * 3 = Night scene
     * Other = reserved
     */
    const val TAG_SCENE_CAPTURE_TYPE = 0xA406
    /**
     * This tag indicates the degree of overall image gain adjustment.
     * Tag = 41991 (A407.H)
     * Type = SHORT
     * Count = 1
     * Default = none
     * 0 = None
     * 1 = Low gain up
     * 2 = High gain up
     * 3 = Low gain down
     * 4 = High gain down
     * Other = reserved
     */
    const val TAG_GAIN_CONTROL = 0xA407
    /**
     * This tag indicates the direction of contrast processing applied by the camera
     * when the image was shot.
     * Tag = 41992 (A408.H)
     * Type = SHORT
     * Count = 1
     * Default = 0
     * 0 = Normal
     * 1 = Soft
     * 2 = Hard
     * Other = reserved
     */
    const val TAG_CONTRAST = 0xA408
    /**
     * This tag indicates the direction of saturation processing applied by the camera
     * when the image was shot.
     * Tag = 41993 (A409.H)
     * Type = SHORT
     * Count = 1
     * Default = 0
     * 0 = Normal
     * 1 = Low saturation
     * 2 = High saturation
     * Other = reserved
     */
    const val TAG_SATURATION = 0xA409
    /**
     * This tag indicates the direction of sharpness processing applied by the camera
     * when the image was shot.
     * Tag = 41994 (A40A.H)
     * Type = SHORT
     * Count = 1
     * Default = 0
     * 0 = Normal
     * 1 = Soft
     * 2 = Hard
     * Other = reserved
     */
    const val TAG_SHARPNESS = 0xA40A
    /**
     * This tag indicates information on the picture-taking conditions of a particular
     * camera model. The tag is used only to indicate the picture-taking conditions in
     * the reader.
     * Tag = 41995 (A40B.H)
     * Type = UNDEFINED
     * Count = Any
     * Default = none
     *
     * The information is recorded in the format shown below. The data is recorded
     * in Unicode using SHORT type for the number of display rows and columns and
     * UNDEFINED type for the camera settings. The Unicode (UCS-2) string including
     * Signature is NULL terminated. The specifics of the Unicode string are as given
     * in ISO/IEC 10464-1.
     *
     * Length  Type        Meaning
     * ------+-----------+------------------
     * 2       SHORT       Display columns
     * 2       SHORT       Display rows
     * Any     UNDEFINED   Camera setting-1
     * Any     UNDEFINED   Camera setting-2
     * :       :           :
     * Any     UNDEFINED   Camera setting-n
     */
    const val TAG_DEVICE_SETTING_DESCRIPTION = 0xA40B
    /**
     * This tag indicates the distance to the subject.
     * Tag = 41996 (A40C.H)
     * Type = SHORT
     * Count = 1
     * Default = none
     * 0 = unknown
     * 1 = Macro
     * 2 = Close view
     * 3 = Distant view
     * Other = reserved
     */
    const val TAG_SUBJECT_DISTANCE_RANGE = 0xA40C
    /**
     * This tag indicates an identifier assigned uniquely to each image. It is
     * recorded as an ASCII string equivalent to hexadecimal notation and 128-bit
     * fixed length.
     * Tag = 42016 (A420.H)
     * Type = ASCII
     * Count = 33
     * Default = none
     */
    const val TAG_IMAGE_UNIQUE_ID = 0xA420
    /** String.  */
    const val TAG_CAMERA_OWNER_NAME = 0xA430
    /** String.  */
    const val TAG_BODY_SERIAL_NUMBER = 0xA431
    /** An array of four Rational64u numbers giving focal and aperture ranges.  */
    const val TAG_LENS_SPECIFICATION = 0xA432
    /** String.  */
    const val TAG_LENS_MAKE = 0xA433
    /** String.  */
    const val TAG_LENS_MODEL = 0xA434
    /** String.  */
    const val TAG_LENS_SERIAL_NUMBER = 0xA435
    /** Rational64u.  */
    const val TAG_GAMMA = 0xA500
    const val TAG_PRINT_IMAGE_MATCHING_INFO = 0xC4A5
    const val TAG_PANASONIC_TITLE = 0xC6D2
    const val TAG_PANASONIC_TITLE_2 = 0xC6D3
    const val TAG_PADDING = 0xEA1C
    const val TAG_LENS = 0xFDEA
    @JvmStatic
    protected fun addExifTagNames(map: HashMap<Int, String>) {
      map[TAG_INTEROP_INDEX] = "Interoperability Index"
      map[TAG_INTEROP_VERSION] = "Interoperability Version"
      map[TAG_NEW_SUBFILE_TYPE] = "New Subfile Type"
      map[TAG_SUBFILE_TYPE] = "Subfile Type"
      map[TAG_IMAGE_WIDTH] = "Image Width"
      map[TAG_IMAGE_HEIGHT] = "Image Height"
      map[TAG_BITS_PER_SAMPLE] = "Bits Per Sample"
      map[TAG_COMPRESSION] = "Compression"
      map[TAG_PHOTOMETRIC_INTERPRETATION] = "Photometric Interpretation"
      map[TAG_THRESHOLDING] = "Thresholding"
      map[TAG_FILL_ORDER] = "Fill Order"
      map[TAG_DOCUMENT_NAME] = "Document Name"
      map[TAG_IMAGE_DESCRIPTION] = "Image Description"
      map[TAG_MAKE] = "Make"
      map[TAG_MODEL] = "Model"
      map[TAG_STRIP_OFFSETS] = "Strip Offsets"
      map[TAG_ORIENTATION] = "Orientation"
      map[TAG_SAMPLES_PER_PIXEL] = "Samples Per Pixel"
      map[TAG_ROWS_PER_STRIP] = "Rows Per Strip"
      map[TAG_STRIP_BYTE_COUNTS] = "Strip Byte Counts"
      map[TAG_MIN_SAMPLE_VALUE] = "Minimum Sample Value"
      map[TAG_MAX_SAMPLE_VALUE] = "Maximum Sample Value"
      map[TAG_X_RESOLUTION] = "X Resolution"
      map[TAG_Y_RESOLUTION] = "Y Resolution"
      map[TAG_PLANAR_CONFIGURATION] = "Planar Configuration"
      map[TAG_PAGE_NAME] = "Page Name"
      map[TAG_RESOLUTION_UNIT] = "Resolution Unit"
      map[TAG_PAGE_NUMBER] = "Page Number"
      map[TAG_TRANSFER_FUNCTION] = "Transfer Function"
      map[TAG_SOFTWARE] = "Software"
      map[TAG_DATETIME] = "Date/Time"
      map[TAG_ARTIST] = "Artist"
      map[TAG_PREDICTOR] = "Predictor"
      map[TAG_HOST_COMPUTER] = "Host Computer"
      map[TAG_WHITE_POINT] = "White Point"
      map[TAG_PRIMARY_CHROMATICITIES] = "Primary Chromaticities"
      map[TAG_TILE_WIDTH] = "Tile Width"
      map[TAG_TILE_LENGTH] = "Tile Length"
      map[TAG_TILE_OFFSETS] = "Tile Offsets"
      map[TAG_TILE_BYTE_COUNTS] = "Tile Byte Counts"
      map[TAG_SUB_IFD_OFFSET] = "Sub IFD Pointer(s)"
      map[TAG_TRANSFER_RANGE] = "Transfer Range"
      map[TAG_JPEG_TABLES] = "JPEG Tables"
      map[TAG_JPEG_PROC] = "JPEG Proc"
      map[TAG_JPEG_RESTART_INTERVAL] = "JPEG Restart Interval"
      map[TAG_JPEG_LOSSLESS_PREDICTORS] = "JPEG Lossless Predictors"
      map[TAG_JPEG_POINT_TRANSFORMS] = "JPEG Point Transforms"
      map[TAG_JPEG_Q_TABLES] = "JPEGQ Tables"
      map[TAG_JPEG_DC_TABLES] = "JPEGDC Tables"
      map[TAG_JPEG_AC_TABLES] = "JPEGAC Tables"
      map[TAG_YCBCR_COEFFICIENTS] = "YCbCr Coefficients"
      map[TAG_YCBCR_SUBSAMPLING] = "YCbCr Sub-Sampling"
      map[TAG_YCBCR_POSITIONING] = "YCbCr Positioning"
      map[TAG_REFERENCE_BLACK_WHITE] = "Reference Black/White"
      map[TAG_STRIP_ROW_COUNTS] = "Strip Row Counts"
      map[TAG_APPLICATION_NOTES] = "Application Notes"
      map[TAG_RELATED_IMAGE_FILE_FORMAT] = "Related Image File Format"
      map[TAG_RELATED_IMAGE_WIDTH] = "Related Image Width"
      map[TAG_RELATED_IMAGE_HEIGHT] = "Related Image Height"
      map[TAG_RATING] = "Rating"
      map[TAG_CFA_REPEAT_PATTERN_DIM] = "CFA Repeat Pattern Dim"
      map[TAG_CFA_PATTERN_2] = "CFA Pattern"
      map[TAG_BATTERY_LEVEL] = "Battery Level"
      map[TAG_COPYRIGHT] = "Copyright"
      map[TAG_EXPOSURE_TIME] = "Exposure Time"
      map[TAG_FNUMBER] = "F-Number"
      map[TAG_IPTC_NAA] = "IPTC/NAA"
      map[TAG_PHOTOSHOP_SETTINGS] = "Photoshop Settings"
      map[TAG_INTER_COLOR_PROFILE] = "Inter Color Profile"
      map[TAG_EXPOSURE_PROGRAM] = "Exposure Program"
      map[TAG_SPECTRAL_SENSITIVITY] = "Spectral Sensitivity"
      map[TAG_ISO_EQUIVALENT] = "ISO Speed Ratings"
      map[TAG_OPTO_ELECTRIC_CONVERSION_FUNCTION] = "Opto-electric Conversion Function (OECF)"
      map[TAG_INTERLACE] = "Interlace"
      map[TAG_TIME_ZONE_OFFSET_TIFF_EP] = "Time Zone Offset"
      map[TAG_SELF_TIMER_MODE_TIFF_EP] = "Self Timer Mode"
      map[TAG_SENSITIVITY_TYPE] = "Sensitivity Type"
      map[TAG_STANDARD_OUTPUT_SENSITIVITY] = "Standard Output Sensitivity"
      map[TAG_RECOMMENDED_EXPOSURE_INDEX] = "Recommended Exposure Index"
      map[TAG_ISO_SPEED] = "ISO Speed"
      map[TAG_ISO_SPEED_LATITUDE_YYY] = "ISO Speed Latitude yyy"
      map[TAG_ISO_SPEED_LATITUDE_ZZZ] = "ISO Speed Latitude zzz"
      map[TAG_EXIF_VERSION] = "Exif Version"
      map[TAG_DATETIME_ORIGINAL] = "Date/Time Original"
      map[TAG_DATETIME_DIGITIZED] = "Date/Time Digitized"
      map[TAG_OFFSET_TIME] = "Offset Time"
      map[TAG_OFFSET_TIME_ORIGINAL] = "Offset Time Original"
      map[TAG_OFFSET_TIME_DIGITIZED] = "Offset Time Digitized"
      map[TAG_COMPONENTS_CONFIGURATION] = "Components Configuration"
      map[TAG_COMPRESSED_AVERAGE_BITS_PER_PIXEL] = "Compressed Bits Per Pixel"
      map[TAG_SHUTTER_SPEED] = "Shutter Speed Value"
      map[TAG_APERTURE] = "Aperture Value"
      map[TAG_BRIGHTNESS_VALUE] = "Brightness Value"
      map[TAG_EXPOSURE_BIAS] = "Exposure Bias Value"
      map[TAG_MAX_APERTURE] = "Max Aperture Value"
      map[TAG_SUBJECT_DISTANCE] = "Subject Distance"
      map[TAG_METERING_MODE] = "Metering Mode"
      map[TAG_WHITE_BALANCE] = "White Balance"
      map[TAG_FLASH] = "Flash"
      map[TAG_FOCAL_LENGTH] = "Focal Length"
      map[TAG_FLASH_ENERGY_TIFF_EP] = "Flash Energy"
      map[TAG_SPATIAL_FREQ_RESPONSE_TIFF_EP] = "Spatial Frequency Response"
      map[TAG_NOISE] = "Noise"
      map[TAG_FOCAL_PLANE_X_RESOLUTION_TIFF_EP] = "Focal Plane X Resolution"
      map[TAG_FOCAL_PLANE_Y_RESOLUTION_TIFF_EP] = "Focal Plane Y Resolution"
      map[TAG_IMAGE_NUMBER] = "Image Number"
      map[TAG_SECURITY_CLASSIFICATION] = "Security Classification"
      map[TAG_IMAGE_HISTORY] = "Image History"
      map[TAG_SUBJECT_LOCATION_TIFF_EP] = "Subject Location"
      map[TAG_EXPOSURE_INDEX_TIFF_EP] = "Exposure Index"
      map[TAG_STANDARD_ID_TIFF_EP] = "TIFF/EP Standard ID"
      map[TAG_MAKERNOTE] = "Makernote"
      map[TAG_USER_COMMENT] = "User Comment"
      map[TAG_SUBSECOND_TIME] = "Sub-Sec Time"
      map[TAG_SUBSECOND_TIME_ORIGINAL] = "Sub-Sec Time Original"
      map[TAG_SUBSECOND_TIME_DIGITIZED] = "Sub-Sec Time Digitized"
      map[TAG_TEMPERATURE] = "Temperature"
      map[TAG_HUMIDITY] = "Humidity"
      map[TAG_PRESSURE] = "Pressure"
      map[TAG_WATER_DEPTH] = "Water Depth"
      map[TAG_ACCELERATION] = "Acceleration"
      map[TAG_CAMERA_ELEVATION_ANGLE] = "Camera Elevation Angle"
      map[TAG_WIN_TITLE] = "Windows XP Title"
      map[TAG_WIN_COMMENT] = "Windows XP Comment"
      map[TAG_WIN_AUTHOR] = "Windows XP Author"
      map[TAG_WIN_KEYWORDS] = "Windows XP Keywords"
      map[TAG_WIN_SUBJECT] = "Windows XP Subject"
      map[TAG_FLASHPIX_VERSION] = "FlashPix Version"
      map[TAG_COLOR_SPACE] = "Color Space"
      map[TAG_EXIF_IMAGE_WIDTH] = "Exif Image Width"
      map[TAG_EXIF_IMAGE_HEIGHT] = "Exif Image Height"
      map[TAG_RELATED_SOUND_FILE] = "Related Sound File"
      map[TAG_FLASH_ENERGY] = "Flash Energy"
      map[TAG_SPATIAL_FREQ_RESPONSE] = "Spatial Frequency Response"
      map[TAG_FOCAL_PLANE_X_RESOLUTION] = "Focal Plane X Resolution"
      map[TAG_FOCAL_PLANE_Y_RESOLUTION] = "Focal Plane Y Resolution"
      map[TAG_FOCAL_PLANE_RESOLUTION_UNIT] = "Focal Plane Resolution Unit"
      map[TAG_SUBJECT_LOCATION] = "Subject Location"
      map[TAG_EXPOSURE_INDEX] = "Exposure Index"
      map[TAG_SENSING_METHOD] = "Sensing Method"
      map[TAG_FILE_SOURCE] = "File Source"
      map[TAG_SCENE_TYPE] = "Scene Type"
      map[TAG_CFA_PATTERN] = "CFA Pattern"
      map[TAG_CUSTOM_RENDERED] = "Custom Rendered"
      map[TAG_EXPOSURE_MODE] = "Exposure Mode"
      map[TAG_WHITE_BALANCE_MODE] = "White Balance Mode"
      map[TAG_DIGITAL_ZOOM_RATIO] = "Digital Zoom Ratio"
      map[TAG_35MM_FILM_EQUIV_FOCAL_LENGTH] = "Focal Length 35"
      map[TAG_SCENE_CAPTURE_TYPE] = "Scene Capture Type"
      map[TAG_GAIN_CONTROL] = "Gain Control"
      map[TAG_CONTRAST] = "Contrast"
      map[TAG_SATURATION] = "Saturation"
      map[TAG_SHARPNESS] = "Sharpness"
      map[TAG_DEVICE_SETTING_DESCRIPTION] = "Device Setting Description"
      map[TAG_SUBJECT_DISTANCE_RANGE] = "Subject Distance Range"
      map[TAG_IMAGE_UNIQUE_ID] = "Unique Image ID"
      map[TAG_CAMERA_OWNER_NAME] = "Camera Owner Name"
      map[TAG_BODY_SERIAL_NUMBER] = "Body Serial Number"
      map[TAG_LENS_SPECIFICATION] = "Lens Specification"
      map[TAG_LENS_MAKE] = "Lens Make"
      map[TAG_LENS_MODEL] = "Lens Model"
      map[TAG_LENS_SERIAL_NUMBER] = "Lens Serial Number"
      map[TAG_GAMMA] = "Gamma"
      map[TAG_PRINT_IMAGE_MATCHING_INFO] = "Print Image Matching (PIM) Info"
      map[TAG_PANASONIC_TITLE] = "Panasonic Title"
      map[TAG_PANASONIC_TITLE_2] = "Panasonic Title (2)"
      map[TAG_PADDING] = "Padding"
      map[TAG_LENS] = "Lens"
    }
  }
}
