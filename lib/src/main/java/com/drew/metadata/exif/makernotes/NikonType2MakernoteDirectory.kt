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
 * Describes tags specific to Nikon (type 2) cameras.  Type-2 applies to the E990 and D-series cameras such as the E990, D1,
 * D70 and D100.
 *
 *
 * Thanks to Fabrizio Giudici for publishing his reverse-engineering of the D100 makernote data.
 * http://www.timelesswanderings.net/equipment/D100/NEF.html
 *
 *
 * Note that the camera implements image protection (locking images) via the file's 'readonly' attribute.  Similarly
 * image hiding uses the 'hidden' attribute (observed on the D70).  Consequently, these values are not available here.
 *
 *
 * Additional sample images have been observed, and their tag values recorded in javadoc comments for each tag's field.
 * New tags have subsequently been added since Fabrizio's observations.
 *
 *
 * In earlier models (such as the E990 and D1), this directory begins at the first byte of the makernote IFD.  In
 * later models, the IFD was given the standard prefix to indicate the camera models (most other manufacturers also
 * provide this prefix to aid in software decoding).
 *
 * @author Drew Noakes https://drewnoakes.com
 */
class NikonType2MakernoteDirectory : Directory() {
  override val tagNameMap = Companion.tagNameMap

  companion object {
    /**
     * Values observed
     * - 0200 (D70)
     * - 0200 (D1X)
     */
    const val TAG_FIRMWARE_VERSION = 0x0001
    /**
     * Values observed
     * - 0 250
     * - 0 400
     */
    const val TAG_ISO_1 = 0x0002
    /**
     * The camera's color mode, as an uppercase string.  Examples include:
     *
     *  * `B & W`
     *  * `COLOR`
     *  * `COOL`
     *  * `SEPIA`
     *  * `VIVID`
     *
     */
    const val TAG_COLOR_MODE = 0x0003
    /**
     * The camera's quality setting, as an uppercase string.  Examples include:
     *
     *  * `BASIC`
     *  * `FINE`
     *  * `NORMAL`
     *  * `RAW`
     *  * `RAW2.7M`
     *
     */
    const val TAG_QUALITY_AND_FILE_FORMAT = 0x0004
    /**
     * The camera's white balance setting, as an uppercase string.  Examples include:
     *
     *
     *  * `AUTO`
     *  * `CLOUDY`
     *  * `FLASH`
     *  * `FLUORESCENT`
     *  * `INCANDESCENT`
     *  * `PRESET`
     *  * `PRESET0`
     *  * `PRESET1`
     *  * `PRESET3`
     *  * `SUNNY`
     *  * `WHITE PRESET`
     *  * `4350K`
     *  * `5000K`
     *  * `DAY WHITE FL`
     *  * `SHADE`
     *
     */
    const val TAG_CAMERA_WHITE_BALANCE = 0x0005
    /**
     * The camera's sharpening setting, as an uppercase string.  Examples include:
     *
     *
     *  * `AUTO`
     *  * `HIGH`
     *  * `LOW`
     *  * `NONE`
     *  * `NORMAL`
     *  * `MED.H`
     *  * `MED.L`
     *
     */
    const val TAG_CAMERA_SHARPENING = 0x0006
    /**
     * The camera's auto-focus mode, as an uppercase string.  Examples include:
     *
     *
     *  * `AF-C`
     *  * `AF-S`
     *  * `MANUAL`
     *  * `AF-A`
     *
     */
    const val TAG_AF_TYPE = 0x0007
    /**
     * The camera's flash setting, as an uppercase string.  Examples include:
     *
     *
     *  * ``
     *  * `NORMAL`
     *  * `RED-EYE`
     *  * `SLOW`
     *  * `NEW_TTL`
     *  * `REAR`
     *  * `REAR SLOW`
     *
     * Note: when TAG_AUTO_FLASH_MODE is blank (whitespace), Nikon Browser displays "Flash Sync Mode: Not Attached"
     */
    const val TAG_FLASH_SYNC_MODE = 0x0008
    /**
     * The type of flash used in the photograph, as a string.  Examples include:
     *
     *
     *  * ``
     *  * `Built-in,TTL`
     *  * `NEW_TTL` Nikon Browser interprets as "D-TTL"
     *  * `Built-in,M`
     *  * `Optional,TTL` with speedlight SB800, flash sync mode as "NORMAL"
     *
     */
    const val TAG_AUTO_FLASH_MODE = 0x0009
    /**
     * An unknown tag, as a rational.  Several values given here:
     * http://gvsoft.homedns.org/exif/makernote-nikon-type2.html#0x000b
     */
    const val TAG_UNKNOWN_34 = 0x000A
    /**
     * The camera's white balance bias setting, as an uint16 array having either one or two elements.
     *
     *
     *  * `0`
     *  * `1`
     *  * `-3`
     *  * `-2`
     *  * `-1`
     *  * `0,0`
     *  * `1,0`
     *  * `5,-5`
     *
     */
    const val TAG_CAMERA_WHITE_BALANCE_FINE = 0x000B
    /**
     * The first two numbers are coefficients to multiply red and blue channels according to white balance as set in the
     * camera. The meaning of the third and the fourth numbers is unknown.
     *
     * Values observed
     * - 2.25882352 1.76078431 0.0 0.0
     * - 10242/1 34305/1 0/1 0/1
     * - 234765625/100000000 1140625/1000000 1/1 1/1
     */
    const val TAG_CAMERA_WHITE_BALANCE_RB_COEFF = 0x000C
    /**
     * The camera's program shift setting, as an array of four integers.
     * The value, in EV, is calculated as `a*b/c`.
     *
     *
     *  * `0,1,3,0` = 0 EV
     *  * `1,1,3,0` = 0.33 EV
     *  * `-3,1,3,0` = -1 EV
     *  * `1,1,2,0` = 0.5 EV
     *  * `2,1,6,0` = 0.33 EV
     *
     */
    const val TAG_PROGRAM_SHIFT = 0x000D
    /**
     * The exposure difference, as an array of four integers.
     * The value, in EV, is calculated as `a*b/c`.
     *
     *
     *  * `-105,1,12,0` = -8.75 EV
     *  * `-72,1,12,0` = -6.00 EV
     *  * `-11,1,12,0` = -0.92 EV
     *
     */
    const val TAG_EXPOSURE_DIFFERENCE = 0x000E
    /**
     * The camera's ISO mode, as an uppercase string.
     *
     *
     *  * `AUTO`
     *  * `MANUAL`
     *
     */
    const val TAG_ISO_MODE = 0x000F
    /**
     * Added during merge of Type2 &amp; Type3.  May apply to earlier models, such as E990 and D1.
     */
    const val TAG_DATA_DUMP = 0x0010
    /**
     * Preview to another IFD (?)
     *
     *
     * Details here: http://gvsoft.homedns.org/exif/makernote-nikon-2-tag0x0011.html
     * // TODO if this is another IFD, decode it
     */
    const val TAG_PREVIEW_IFD = 0x0011
    /**
     * The flash compensation, as an array of four integers.
     * The value, in EV, is calculated as `a*b/c`.
     *
     *
     *  * `-18,1,6,0` = -3 EV
     *  * `4,1,6,0` = 0.67 EV
     *  * `6,1,6,0` = 1 EV
     *
     */
    const val TAG_AUTO_FLASH_COMPENSATION = 0x0012
    /**
     * The requested ISO value, as an array of two integers.
     *
     *
     *  * `0,0`
     *  * `0,125`
     *  * `1,2500`
     *
     */
    const val TAG_ISO_REQUESTED = 0x0013
    /**
     * Defines the photo corner coordinates, in 8 bytes.  Treated as four 16-bit integers, they
     * decode as: top-left (x,y); bot-right (x,y)
     * - 0 0 49163 53255
     * - 0 0 3008 2000 (the image dimensions were 3008x2000) (D70)
     *
     *  * `0,0,4288,2848` The max resolution of the D300 camera
     *  * `0,0,3008,2000` The max resolution of the D70 camera
     *  * `0,0,4256,2832` The max resolution of the D3 camera
     *
     */
    const val TAG_IMAGE_BOUNDARY = 0x0016
    /**
     * The flash exposure compensation, as an array of four integers.
     * The value, in EV, is calculated as `a*b/c`.
     *
     *
     *  * `0,0,0,0` = 0 EV
     *  * `0,1,6,0` = 0 EV
     *  * `4,1,6,0` = 0.67 EV
     *
     */
    const val TAG_FLASH_EXPOSURE_COMPENSATION = 0x0017
    /**
     * The flash bracket compensation, as an array of four integers.
     * The value, in EV, is calculated as `a*b/c`.
     *
     *
     *  * `0,0,0,0` = 0 EV
     *  * `0,1,6,0` = 0 EV
     *  * `4,1,6,0` = 0.67 EV
     *
     */
    const val TAG_FLASH_BRACKET_COMPENSATION = 0x0018
    /**
     * The AE bracket compensation, as a rational number.
     *
     *
     *  * `0/0`
     *  * `0/1`
     *  * `0/6`
     *  * `4/6`
     *  * `6/6`
     *
     */
    const val TAG_AE_BRACKET_COMPENSATION = 0x0019
    /**
     * Flash mode, as a string.
     *
     *
     *  * ``
     *  * `Red Eye Reduction`
     *  * `D-Lighting`
     *  * `Distortion control`
     *
     */
    const val TAG_FLASH_MODE = 0x001a
    const val TAG_CROP_HIGH_SPEED = 0x001b
    const val TAG_EXPOSURE_TUNING = 0x001c
    /**
     * The camera's serial number, as a string.
     * Note that D200 is always blank, and D50 is always `"D50"`.
     */
    const val TAG_CAMERA_SERIAL_NUMBER = 0x001d
    /**
     * The camera's color space setting.
     *
     *
     *  * `1` sRGB
     *  * `2` Adobe RGB
     *
     */
    const val TAG_COLOR_SPACE = 0x001e
    const val TAG_VR_INFO = 0x001f
    const val TAG_IMAGE_AUTHENTICATION = 0x0020
    const val TAG_UNKNOWN_35 = 0x0021
    /**
     * The active D-Lighting setting.
     *
     *
     *  * `0` Off
     *  * `1` Low
     *  * `3` Normal
     *  * `5` High
     *  * `7` Extra High
     *  * `65535` Auto
     *
     */
    const val TAG_ACTIVE_D_LIGHTING = 0x0022
    const val TAG_PICTURE_CONTROL = 0x0023
    const val TAG_WORLD_TIME = 0x0024
    const val TAG_ISO_INFO = 0x0025
    const val TAG_UNKNOWN_36 = 0x0026
    const val TAG_UNKNOWN_37 = 0x0027
    const val TAG_UNKNOWN_38 = 0x0028
    const val TAG_UNKNOWN_39 = 0x0029
    /**
     * The camera's vignette control setting.
     *
     *
     *  * `0` Off
     *  * `1` Low
     *  * `3` Normal
     *  * `5` High
     *
     */
    const val TAG_VIGNETTE_CONTROL = 0x002a
    const val TAG_UNKNOWN_40 = 0x002b
    const val TAG_UNKNOWN_41 = 0x002c
    const val TAG_UNKNOWN_42 = 0x002d
    const val TAG_UNKNOWN_43 = 0x002e
    const val TAG_UNKNOWN_44 = 0x002f
    const val TAG_UNKNOWN_45 = 0x0030
    const val TAG_UNKNOWN_46 = 0x0031
    /**
     * The camera's image adjustment setting, as a string.
     *
     *
     *  * `AUTO`
     *  * `CONTRAST(+)`
     *  * `CONTRAST(-)`
     *  * `NORMAL`
     *  * `B & W`
     *  * `BRIGHTNESS(+)`
     *  * `BRIGHTNESS(-)`
     *  * `SEPIA`
     *
     */
    const val TAG_IMAGE_ADJUSTMENT = 0x0080
    /**
     * The camera's tone compensation setting, as a string.
     *
     *
     *  * `NORMAL`
     *  * `LOW`
     *  * `MED.L`
     *  * `MED.H`
     *  * `HIGH`
     *  * `AUTO`
     *
     */
    const val TAG_CAMERA_TONE_COMPENSATION = 0x0081
    /**
     * A description of any auxiliary lens, as a string.
     *
     *
     *  * `OFF`
     *  * `FISHEYE 1`
     *  * `FISHEYE 2`
     *  * `TELEPHOTO 2`
     *  * `WIDE ADAPTER`
     *
     */
    const val TAG_ADAPTER = 0x0082
    /**
     * The type of lens used, as a byte.
     *
     *
     *  * `0x00` AF
     *  * `0x01` MF
     *  * `0x02` D
     *  * `0x06` G, D
     *  * `0x08` VR
     *  * `0x0a` VR, D
     *  * `0x0e` VR, G, D
     *
     */
    const val TAG_LENS_TYPE = 0x0083
    /**
     * A pair of focal/max-fstop values that describe the lens used.
     *
     * Values observed
     * - 180.0,180.0,2.8,2.8 (D100)
     * - 240/10 850/10 35/10 45/10
     * - 18-70mm f/3.5-4.5 (D70)
     * - 17-35mm f/2.8-2.8 (D1X)
     * - 70-200mm f/2.8-2.8 (D70)
     *
     * Nikon Browser identifies the lens as "18-70mm F/3.5-4.5 G" which
     * is identical to metadata extractor, except for the "G".  This must
     * be coming from another tag...
     */
    const val TAG_LENS = 0x0084
    /**
     * Added during merge of Type2 &amp; Type3.  May apply to earlier models, such as E990 and D1.
     */
    const val TAG_MANUAL_FOCUS_DISTANCE = 0x0085
    /**
     * The amount of digital zoom used.
     */
    const val TAG_DIGITAL_ZOOM = 0x0086
    /**
     * Whether the flash was used in this image.
     *
     *
     *  * `0` Flash Not Used
     *  * `1` Manual Flash
     *  * `3` Flash Not Ready
     *  * `7` External Flash
     *  * `8` Fired, Commander Mode
     *  * `9` Fired, TTL Mode
     *
     */
    const val TAG_FLASH_USED = 0x0087
    /**
     * The position of the autofocus target.
     */
    const val TAG_AF_FOCUS_POSITION = 0x0088
    /**
     * The camera's shooting mode.
     *
     *
     * A bit-array with:
     *
     *  * `0` Single Frame
     *  * `1` Continuous
     *  * `2` Delay
     *  * `8` PC Control
     *  * `16` Exposure Bracketing
     *  * `32` Auto ISO
     *  * `64` White-Balance Bracketing
     *  * `128` IR Control
     *
     */
    const val TAG_SHOOTING_MODE = 0x0089
    const val TAG_UNKNOWN_20 = 0x008A
    /**
     * Lens stops, as an array of four integers.
     * The value, in EV, is calculated as `a*b/c`.
     *
     *
     *  * `64,1,12,0` = 5.33 EV
     *  * `72,1,12,0` = 6 EV
     *
     */
    const val TAG_LENS_STOPS = 0x008B
    const val TAG_CONTRAST_CURVE = 0x008C
    /**
     * The color space as set in the camera, as a string.
     *
     *
     *  * `MODE1` = Mode 1 (sRGB)
     *  * `MODE1a` = Mode 1 (sRGB)
     *  * `MODE2` = Mode 2 (Adobe RGB)
     *  * `MODE3` = Mode 2 (sRGB): Higher Saturation
     *  * `MODE3a` = Mode 2 (sRGB): Higher Saturation
     *  * `B & W` = B &amp; W
     *
     */
    const val TAG_CAMERA_COLOR_MODE = 0x008D
    const val TAG_UNKNOWN_47 = 0x008E
    /**
     * The camera's scene mode, as a string.  Examples include:
     *
     *  * `BEACH/SNOW`
     *  * `CLOSE UP`
     *  * `NIGHT PORTRAIT`
     *  * `PORTRAIT`
     *  * `ANTI-SHAKE`
     *  * `BACK LIGHT`
     *  * `BEST FACE`
     *  * `BEST`
     *  * `COPY`
     *  * `DAWN/DUSK`
     *  * `FACE-PRIORITY`
     *  * `FIREWORKS`
     *  * `FOOD`
     *  * `HIGH SENS.`
     *  * `LAND SCAPE`
     *  * `MUSEUM`
     *  * `PANORAMA ASSIST`
     *  * `PARTY/INDOOR`
     *  * `SCENE AUTO`
     *  * `SMILE`
     *  * `SPORT`
     *  * `SPORT CONT.`
     *  * `SUNSET`
     *
     */
    const val TAG_SCENE_MODE = 0x008F
    /**
     * The lighting type, as a string.  Examples include:
     *
     *  * ``
     *  * `NATURAL`
     *  * `SPEEDLIGHT`
     *  * `COLORED`
     *  * `MIXED`
     *  * `NORMAL`
     *
     */
    const val TAG_LIGHT_SOURCE = 0x0090
    /**
     * Advertised as ASCII, but actually isn't.  A variable number of bytes (eg. 18 to 533).  Actual number of bytes
     * appears fixed for a given camera model.
     */
    const val TAG_SHOT_INFO = 0x0091
    /**
     * The hue adjustment as set in the camera.  Values observed are either 0 or 3.
     */
    const val TAG_CAMERA_HUE_ADJUSTMENT = 0x0092
    /**
     * The NEF (RAW) compression.  Examples include:
     *
     *  * `1` Lossy (Type 1)
     *  * `2` Uncompressed
     *  * `3` Lossless
     *  * `4` Lossy (Type 2)
     *
     */
    const val TAG_NEF_COMPRESSION = 0x0093
    /**
     * The saturation level, as a signed integer.  Examples include:
     *
     *  * `+3`
     *  * `+2`
     *  * `+1`
     *  * `0` Normal
     *  * `-1`
     *  * `-2`
     *  * `-3` (B&amp;W)
     *
     */
    const val TAG_SATURATION = 0x0094
    /**
     * The type of noise reduction, as a string.  Examples include:
     *
     *  * `OFF`
     *  * `FPNR`
     *
     */
    const val TAG_NOISE_REDUCTION = 0x0095
    const val TAG_LINEARIZATION_TABLE = 0x0096
    const val TAG_COLOR_BALANCE = 0x0097
    const val TAG_LENS_DATA = 0x0098
    /** The NEF (RAW) thumbnail size, as an integer array with two items representing [width,height].  */
    const val TAG_NEF_THUMBNAIL_SIZE = 0x0099
    /** The sensor pixel size, as a pair of rational numbers.  */
    const val TAG_SENSOR_PIXEL_SIZE = 0x009A
    const val TAG_UNKNOWN_10 = 0x009B
    const val TAG_SCENE_ASSIST = 0x009C
    const val TAG_UNKNOWN_11 = 0x009D
    const val TAG_RETOUCH_HISTORY = 0x009E
    const val TAG_UNKNOWN_12 = 0x009F
    /**
     * The camera serial number, as a string.
     *
     *  * `NO= 00002539`
     *  * `NO= -1000d71`
     *  * `PKG597230621263`
     *  * `PKG5995671330625116`
     *  * `PKG49981281631130677`
     *  * `BU672230725063`
     *  * `NO= 200332c7`
     *  * `NO= 30045efe`
     *
     */
    const val TAG_CAMERA_SERIAL_NUMBER_2 = 0x00A0
    const val TAG_IMAGE_DATA_SIZE = 0x00A2
    const val TAG_UNKNOWN_27 = 0x00A3
    const val TAG_UNKNOWN_28 = 0x00A4
    const val TAG_IMAGE_COUNT = 0x00A5
    const val TAG_DELETED_IMAGE_COUNT = 0x00A6
    /** The number of total shutter releases.  This value increments for each exposure (observed on D70).  */
    const val TAG_EXPOSURE_SEQUENCE_NUMBER = 0x00A7
    const val TAG_FLASH_INFO = 0x00A8
    /**
     * The camera's image optimisation, as a string.
     *
     *  * ``
     *  * `NORMAL`
     *  * `CUSTOM`
     *  * `BLACK AND WHITE`
     *  * `LAND SCAPE`
     *  * `MORE VIVID`
     *  * `PORTRAIT`
     *  * `SOFT`
     *  * `VIVID`
     *
     */
    const val TAG_IMAGE_OPTIMISATION = 0x00A9
    /**
     * The camera's saturation level, as a string.
     *
     *  * ``
     *  * `NORMAL`
     *  * `AUTO`
     *  * `ENHANCED`
     *  * `MODERATE`
     *
     */
    const val TAG_SATURATION_2 = 0x00AA
    /**
     * The camera's digital vari-program setting, as a string.
     *
     *  * ``
     *  * `AUTO`
     *  * `AUTO(FLASH OFF)`
     *  * `CLOSE UP`
     *  * `LANDSCAPE`
     *  * `NIGHT PORTRAIT`
     *  * `PORTRAIT`
     *  * `SPORT`
     *
     */
    const val TAG_DIGITAL_VARI_PROGRAM = 0x00AB
    /**
     * The camera's digital vari-program setting, as a string.
     *
     *  * ``
     *  * `VR-ON`
     *  * `VR-OFF`
     *  * `VR-HYBRID`
     *  * `VR-ACTIVE`
     *
     */
    const val TAG_IMAGE_STABILISATION = 0x00AC
    /**
     * The camera's digital vari-program setting, as a string.
     *
     *  * ``
     *  * `HYBRID`
     *  * `STANDARD`
     *
     */
    const val TAG_AF_RESPONSE = 0x00AD
    const val TAG_UNKNOWN_29 = 0x00AE
    const val TAG_UNKNOWN_30 = 0x00AF
    const val TAG_MULTI_EXPOSURE = 0x00B0
    /**
     * The camera's high ISO noise reduction setting, as an integer.
     *
     *  * `0` Off
     *  * `1` Minimal
     *  * `2` Low
     *  * `4` Normal
     *  * `6` High
     *
     */
    const val TAG_HIGH_ISO_NOISE_REDUCTION = 0x00B1
    const val TAG_UNKNOWN_31 = 0x00B2
    const val TAG_UNKNOWN_32 = 0x00B3
    const val TAG_UNKNOWN_33 = 0x00B4
    const val TAG_UNKNOWN_48 = 0x00B5
    const val TAG_POWER_UP_TIME = 0x00B6
    const val TAG_AF_INFO_2 = 0x00B7
    const val TAG_FILE_INFO = 0x00B8
    const val TAG_AF_TUNE = 0x00B9
    const val TAG_UNKNOWN_49 = 0x00BB
    const val TAG_UNKNOWN_50 = 0x00BD
    const val TAG_UNKNOWN_51 = 0x0103
    const val TAG_PRINT_IMAGE_MATCHING_INFO = 0x0E00
    /**
     * Data about changes set by Nikon Capture Editor.
     *
     * Values observed
     */
    const val TAG_NIKON_CAPTURE_DATA = 0x0E01
    const val TAG_UNKNOWN_52 = 0x0E05
    const val TAG_UNKNOWN_53 = 0x0E08
    const val TAG_NIKON_CAPTURE_VERSION = 0x0E09
    const val TAG_NIKON_CAPTURE_OFFSETS = 0x0E0E
    const val TAG_NIKON_SCAN = 0x0E10
    const val TAG_UNKNOWN_54 = 0x0E19
    const val TAG_NEF_BIT_DEPTH = 0x0E22
    const val TAG_UNKNOWN_55 = 0x0E23
    protected val tagNameMap = HashMap<Int, String>()
    /** Nikon decryption tables used in exiftool  */
    private val _decTable1 = intArrayOf(0xc1, 0xbf, 0x6d, 0x0d, 0x59, 0xc5, 0x13, 0x9d, 0x83, 0x61, 0x6b, 0x4f, 0xc7, 0x7f, 0x3d, 0x3d,
      0x53, 0x59, 0xe3, 0xc7, 0xe9, 0x2f, 0x95, 0xa7, 0x95, 0x1f, 0xdf, 0x7f, 0x2b, 0x29, 0xc7, 0x0d,
      0xdf, 0x07, 0xef, 0x71, 0x89, 0x3d, 0x13, 0x3d, 0x3b, 0x13, 0xfb, 0x0d, 0x89, 0xc1, 0x65, 0x1f,
      0xb3, 0x0d, 0x6b, 0x29, 0xe3, 0xfb, 0xef, 0xa3, 0x6b, 0x47, 0x7f, 0x95, 0x35, 0xa7, 0x47, 0x4f,
      0xc7, 0xf1, 0x59, 0x95, 0x35, 0x11, 0x29, 0x61, 0xf1, 0x3d, 0xb3, 0x2b, 0x0d, 0x43, 0x89, 0xc1,
      0x9d, 0x9d, 0x89, 0x65, 0xf1, 0xe9, 0xdf, 0xbf, 0x3d, 0x7f, 0x53, 0x97, 0xe5, 0xe9, 0x95, 0x17,
      0x1d, 0x3d, 0x8b, 0xfb, 0xc7, 0xe3, 0x67, 0xa7, 0x07, 0xf1, 0x71, 0xa7, 0x53, 0xb5, 0x29, 0x89,
      0xe5, 0x2b, 0xa7, 0x17, 0x29, 0xe9, 0x4f, 0xc5, 0x65, 0x6d, 0x6b, 0xef, 0x0d, 0x89, 0x49, 0x2f,
      0xb3, 0x43, 0x53, 0x65, 0x1d, 0x49, 0xa3, 0x13, 0x89, 0x59, 0xef, 0x6b, 0xef, 0x65, 0x1d, 0x0b,
      0x59, 0x13, 0xe3, 0x4f, 0x9d, 0xb3, 0x29, 0x43, 0x2b, 0x07, 0x1d, 0x95, 0x59, 0x59, 0x47, 0xfb,
      0xe5, 0xe9, 0x61, 0x47, 0x2f, 0x35, 0x7f, 0x17, 0x7f, 0xef, 0x7f, 0x95, 0x95, 0x71, 0xd3, 0xa3,
      0x0b, 0x71, 0xa3, 0xad, 0x0b, 0x3b, 0xb5, 0xfb, 0xa3, 0xbf, 0x4f, 0x83, 0x1d, 0xad, 0xe9, 0x2f,
      0x71, 0x65, 0xa3, 0xe5, 0x07, 0x35, 0x3d, 0x0d, 0xb5, 0xe9, 0xe5, 0x47, 0x3b, 0x9d, 0xef, 0x35,
      0xa3, 0xbf, 0xb3, 0xdf, 0x53, 0xd3, 0x97, 0x53, 0x49, 0x71, 0x07, 0x35, 0x61, 0x71, 0x2f, 0x43,
      0x2f, 0x11, 0xdf, 0x17, 0x97, 0xfb, 0x95, 0x3b, 0x7f, 0x6b, 0xd3, 0x25, 0xbf, 0xad, 0xc7, 0xc5,
      0xc5, 0xb5, 0x8b, 0xef, 0x2f, 0xd3, 0x07, 0x6b, 0x25, 0x49, 0x95, 0x25, 0x49, 0x6d, 0x71, 0xc7)
    private val _decTable2 = intArrayOf(0xa7, 0xbc, 0xc9, 0xad, 0x91, 0xdf, 0x85, 0xe5, 0xd4, 0x78, 0xd5, 0x17, 0x46, 0x7c, 0x29, 0x4c,
      0x4d, 0x03, 0xe9, 0x25, 0x68, 0x11, 0x86, 0xb3, 0xbd, 0xf7, 0x6f, 0x61, 0x22, 0xa2, 0x26, 0x34,
      0x2a, 0xbe, 0x1e, 0x46, 0x14, 0x68, 0x9d, 0x44, 0x18, 0xc2, 0x40, 0xf4, 0x7e, 0x5f, 0x1b, 0xad,
      0x0b, 0x94, 0xb6, 0x67, 0xb4, 0x0b, 0xe1, 0xea, 0x95, 0x9c, 0x66, 0xdc, 0xe7, 0x5d, 0x6c, 0x05,
      0xda, 0xd5, 0xdf, 0x7a, 0xef, 0xf6, 0xdb, 0x1f, 0x82, 0x4c, 0xc0, 0x68, 0x47, 0xa1, 0xbd, 0xee,
      0x39, 0x50, 0x56, 0x4a, 0xdd, 0xdf, 0xa5, 0xf8, 0xc6, 0xda, 0xca, 0x90, 0xca, 0x01, 0x42, 0x9d,
      0x8b, 0x0c, 0x73, 0x43, 0x75, 0x05, 0x94, 0xde, 0x24, 0xb3, 0x80, 0x34, 0xe5, 0x2c, 0xdc, 0x9b,
      0x3f, 0xca, 0x33, 0x45, 0xd0, 0xdb, 0x5f, 0xf5, 0x52, 0xc3, 0x21, 0xda, 0xe2, 0x22, 0x72, 0x6b,
      0x3e, 0xd0, 0x5b, 0xa8, 0x87, 0x8c, 0x06, 0x5d, 0x0f, 0xdd, 0x09, 0x19, 0x93, 0xd0, 0xb9, 0xfc,
      0x8b, 0x0f, 0x84, 0x60, 0x33, 0x1c, 0x9b, 0x45, 0xf1, 0xf0, 0xa3, 0x94, 0x3a, 0x12, 0x77, 0x33,
      0x4d, 0x44, 0x78, 0x28, 0x3c, 0x9e, 0xfd, 0x65, 0x57, 0x16, 0x94, 0x6b, 0xfb, 0x59, 0xd0, 0xc8,
      0x22, 0x36, 0xdb, 0xd2, 0x63, 0x98, 0x43, 0xa1, 0x04, 0x87, 0x86, 0xf7, 0xa6, 0x26, 0xbb, 0xd6,
      0x59, 0x4d, 0xbf, 0x6a, 0x2e, 0xaa, 0x2b, 0xef, 0xe6, 0x78, 0xb6, 0x4e, 0xe0, 0x2f, 0xdc, 0x7c,
      0xbe, 0x57, 0x19, 0x32, 0x7e, 0x2a, 0xd0, 0xb8, 0xba, 0x29, 0x00, 0x3c, 0x52, 0x7d, 0xa8, 0x49,
      0x3b, 0x2d, 0xeb, 0x25, 0x49, 0xfa, 0xa3, 0xaa, 0x39, 0xa7, 0xc5, 0xa7, 0x50, 0x11, 0x36, 0xfb,
      0xc6, 0x67, 0x4a, 0xf5, 0xa5, 0x12, 0x65, 0x7e, 0xb0, 0xdf, 0xaf, 0x4e, 0xb3, 0x61, 0x7f, 0x2f)

    init {
      tagNameMap[TAG_FIRMWARE_VERSION] = "Firmware Version"
      tagNameMap[TAG_ISO_1] = "ISO"
      tagNameMap[TAG_QUALITY_AND_FILE_FORMAT] = "Quality & File Format"
      tagNameMap[TAG_CAMERA_WHITE_BALANCE] = "White Balance"
      tagNameMap[TAG_CAMERA_SHARPENING] = "Sharpening"
      tagNameMap[TAG_AF_TYPE] = "AF Type"
      tagNameMap[TAG_CAMERA_WHITE_BALANCE_FINE] = "White Balance Fine"
      tagNameMap[TAG_CAMERA_WHITE_BALANCE_RB_COEFF] = "White Balance RB Coefficients"
      tagNameMap[TAG_ISO_REQUESTED] = "ISO"
      tagNameMap[TAG_ISO_MODE] = "ISO Mode"
      tagNameMap[TAG_DATA_DUMP] = "Data Dump"
      tagNameMap[TAG_PROGRAM_SHIFT] = "Program Shift"
      tagNameMap[TAG_EXPOSURE_DIFFERENCE] = "Exposure Difference"
      tagNameMap[TAG_PREVIEW_IFD] = "Preview IFD"
      tagNameMap[TAG_LENS_TYPE] = "Lens Type"
      tagNameMap[TAG_FLASH_USED] = "Flash Used"
      tagNameMap[TAG_AF_FOCUS_POSITION] = "AF Focus Position"
      tagNameMap[TAG_SHOOTING_MODE] = "Shooting Mode"
      tagNameMap[TAG_LENS_STOPS] = "Lens Stops"
      tagNameMap[TAG_CONTRAST_CURVE] = "Contrast Curve"
      tagNameMap[TAG_LIGHT_SOURCE] = "Light source"
      tagNameMap[TAG_SHOT_INFO] = "Shot Info"
      tagNameMap[TAG_COLOR_BALANCE] = "Color Balance"
      tagNameMap[TAG_LENS_DATA] = "Lens Data"
      tagNameMap[TAG_NEF_THUMBNAIL_SIZE] = "NEF Thumbnail Size"
      tagNameMap[TAG_SENSOR_PIXEL_SIZE] = "Sensor Pixel Size"
      tagNameMap[TAG_UNKNOWN_10] = "Unknown 10"
      tagNameMap[TAG_SCENE_ASSIST] = "Scene Assist"
      tagNameMap[TAG_UNKNOWN_11] = "Unknown 11"
      tagNameMap[TAG_RETOUCH_HISTORY] = "Retouch History"
      tagNameMap[TAG_UNKNOWN_12] = "Unknown 12"
      tagNameMap[TAG_FLASH_SYNC_MODE] = "Flash Sync Mode"
      tagNameMap[TAG_AUTO_FLASH_MODE] = "Auto Flash Mode"
      tagNameMap[TAG_AUTO_FLASH_COMPENSATION] = "Auto Flash Compensation"
      tagNameMap[TAG_EXPOSURE_SEQUENCE_NUMBER] = "Exposure Sequence Number"
      tagNameMap[TAG_COLOR_MODE] = "Color Mode"
      tagNameMap[TAG_UNKNOWN_20] = "Unknown 20"
      tagNameMap[TAG_IMAGE_BOUNDARY] = "Image Boundary"
      tagNameMap[TAG_FLASH_EXPOSURE_COMPENSATION] = "Flash Exposure Compensation"
      tagNameMap[TAG_FLASH_BRACKET_COMPENSATION] = "Flash Bracket Compensation"
      tagNameMap[TAG_AE_BRACKET_COMPENSATION] = "AE Bracket Compensation"
      tagNameMap[TAG_FLASH_MODE] = "Flash Mode"
      tagNameMap[TAG_CROP_HIGH_SPEED] = "Crop High Speed"
      tagNameMap[TAG_EXPOSURE_TUNING] = "Exposure Tuning"
      tagNameMap[TAG_CAMERA_SERIAL_NUMBER] = "Camera Serial Number"
      tagNameMap[TAG_COLOR_SPACE] = "Color Space"
      tagNameMap[TAG_VR_INFO] = "VR Info"
      tagNameMap[TAG_IMAGE_AUTHENTICATION] = "Image Authentication"
      tagNameMap[TAG_UNKNOWN_35] = "Unknown 35"
      tagNameMap[TAG_ACTIVE_D_LIGHTING] = "Active D-Lighting"
      tagNameMap[TAG_PICTURE_CONTROL] = "Picture Control"
      tagNameMap[TAG_WORLD_TIME] = "World Time"
      tagNameMap[TAG_ISO_INFO] = "ISO Info"
      tagNameMap[TAG_UNKNOWN_36] = "Unknown 36"
      tagNameMap[TAG_UNKNOWN_37] = "Unknown 37"
      tagNameMap[TAG_UNKNOWN_38] = "Unknown 38"
      tagNameMap[TAG_UNKNOWN_39] = "Unknown 39"
      tagNameMap[TAG_VIGNETTE_CONTROL] = "Vignette Control"
      tagNameMap[TAG_UNKNOWN_40] = "Unknown 40"
      tagNameMap[TAG_UNKNOWN_41] = "Unknown 41"
      tagNameMap[TAG_UNKNOWN_42] = "Unknown 42"
      tagNameMap[TAG_UNKNOWN_43] = "Unknown 43"
      tagNameMap[TAG_UNKNOWN_44] = "Unknown 44"
      tagNameMap[TAG_UNKNOWN_45] = "Unknown 45"
      tagNameMap[TAG_UNKNOWN_46] = "Unknown 46"
      tagNameMap[TAG_UNKNOWN_47] = "Unknown 47"
      tagNameMap[TAG_SCENE_MODE] = "Scene Mode"
      tagNameMap[TAG_CAMERA_SERIAL_NUMBER_2] = "Camera Serial Number"
      tagNameMap[TAG_IMAGE_DATA_SIZE] = "Image Data Size"
      tagNameMap[TAG_UNKNOWN_27] = "Unknown 27"
      tagNameMap[TAG_UNKNOWN_28] = "Unknown 28"
      tagNameMap[TAG_IMAGE_COUNT] = "Image Count"
      tagNameMap[TAG_DELETED_IMAGE_COUNT] = "Deleted Image Count"
      tagNameMap[TAG_SATURATION_2] = "Saturation"
      tagNameMap[TAG_DIGITAL_VARI_PROGRAM] = "Digital Vari Program"
      tagNameMap[TAG_IMAGE_STABILISATION] = "Image Stabilisation"
      tagNameMap[TAG_AF_RESPONSE] = "AF Response"
      tagNameMap[TAG_UNKNOWN_29] = "Unknown 29"
      tagNameMap[TAG_UNKNOWN_30] = "Unknown 30"
      tagNameMap[TAG_MULTI_EXPOSURE] = "Multi Exposure"
      tagNameMap[TAG_HIGH_ISO_NOISE_REDUCTION] = "High ISO Noise Reduction"
      tagNameMap[TAG_UNKNOWN_31] = "Unknown 31"
      tagNameMap[TAG_UNKNOWN_32] = "Unknown 32"
      tagNameMap[TAG_UNKNOWN_33] = "Unknown 33"
      tagNameMap[TAG_UNKNOWN_48] = "Unknown 48"
      tagNameMap[TAG_POWER_UP_TIME] = "Power Up Time"
      tagNameMap[TAG_AF_INFO_2] = "AF Info 2"
      tagNameMap[TAG_FILE_INFO] = "File Info"
      tagNameMap[TAG_AF_TUNE] = "AF Tune"
      tagNameMap[TAG_FLASH_INFO] = "Flash Info"
      tagNameMap[TAG_IMAGE_OPTIMISATION] = "Image Optimisation"
      tagNameMap[TAG_IMAGE_ADJUSTMENT] = "Image Adjustment"
      tagNameMap[TAG_CAMERA_TONE_COMPENSATION] = "Tone Compensation"
      tagNameMap[TAG_ADAPTER] = "Adapter"
      tagNameMap[TAG_LENS] = "Lens"
      tagNameMap[TAG_MANUAL_FOCUS_DISTANCE] = "Manual Focus Distance"
      tagNameMap[TAG_DIGITAL_ZOOM] = "Digital Zoom"
      tagNameMap[TAG_CAMERA_COLOR_MODE] = "Colour Mode"
      tagNameMap[TAG_CAMERA_HUE_ADJUSTMENT] = "Camera Hue Adjustment"
      tagNameMap[TAG_NEF_COMPRESSION] = "NEF Compression"
      tagNameMap[TAG_SATURATION] = "Saturation"
      tagNameMap[TAG_NOISE_REDUCTION] = "Noise Reduction"
      tagNameMap[TAG_LINEARIZATION_TABLE] = "Linearization Table"
      tagNameMap[TAG_NIKON_CAPTURE_DATA] = "Nikon Capture Data"
      tagNameMap[TAG_UNKNOWN_49] = "Unknown 49"
      tagNameMap[TAG_UNKNOWN_50] = "Unknown 50"
      tagNameMap[TAG_UNKNOWN_51] = "Unknown 51"
      tagNameMap[TAG_PRINT_IMAGE_MATCHING_INFO] = "Print IM"
      tagNameMap[TAG_UNKNOWN_52] = "Unknown 52"
      tagNameMap[TAG_UNKNOWN_53] = "Unknown 53"
      tagNameMap[TAG_NIKON_CAPTURE_VERSION] = "Nikon Capture Version"
      tagNameMap[TAG_NIKON_CAPTURE_OFFSETS] = "Nikon Capture Offsets"
      tagNameMap[TAG_NIKON_SCAN] = "Nikon Scan"
      tagNameMap[TAG_UNKNOWN_54] = "Unknown 54"
      tagNameMap[TAG_NEF_BIT_DEPTH] = "NEF Bit Depth"
      tagNameMap[TAG_UNKNOWN_55] = "Unknown 55"
    }
  }

  override val name: String
    get() = "Nikon Makernote"

  /** decryption algorithm adapted from exiftool  */
  fun getDecryptedIntArray(tagType: Int): IntArray? {
    val data = getIntArray(tagType)
    val serial = getInteger(TAG_CAMERA_SERIAL_NUMBER)
    val count = getInteger(TAG_EXPOSURE_SEQUENCE_NUMBER)
    if (data == null || serial == null || count == null) return null
    var key = 0
    for (i in 0..3) key = key xor (count shr i * 8) and 0xff
    val ci = _decTable1[serial and 0xff]
    var cj = _decTable2[key]
    var ck = 0x60
    for (i in 4 until data.size) {
      cj = cj + ci * ck and 0xff
      ck = ck + 1 and 0xff
      data[i] = data[i] xor cj
    }
    return data
  }

  init {
    setDescriptor(NikonType2MakernoteDescriptor(this))
  }
}
