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
import com.drew.metadata.Age
import com.drew.metadata.Age.Companion.fromPanasonicString
import com.drew.metadata.Directory
import com.drew.metadata.Face
import java.io.IOException
import java.util.*

/**
 * Describes tags specific to Panasonic and Leica cameras.
 *
 * @author Drew Noakes https://drewnoakes.com
 * @author Philipp Sandhaus
 */
class PanasonicMakernoteDirectory : Directory() {
  override val tagNameMap = Companion.tagNameMap

  companion object {
    /**
     * <br></br>
     * 2 = High            <br></br>
     * 3 = Normal          <br></br>
     * 6 = Very High       <br></br>
     * 7 = Raw             <br></br>
     * 9 = Motion Picture  <br></br>
     */
    const val TAG_QUALITY_MODE = 0x0001
    const val TAG_FIRMWARE_VERSION = 0x0002
    /**
     * <br></br>
     * 1 = Auto            <br></br>
     * 2 = Daylight        <br></br>
     * 3 = Cloudy          <br></br>
     * 4 = Incandescent    <br></br>
     * 5 = Manual          <br></br>
     * 8 = Flash           <br></br>
     * 10 = Black &amp; White  <br></br>
     * 11 = Manual         <br></br>
     * 12 = Shade          <br></br>
     */
    const val TAG_WHITE_BALANCE = 0x0003
    /**
     * <br></br>
     * 1 = Auto                <br></br>
     * 2 = Manual              <br></br>
     * 4 =  Auto, Focus Button <br></br>
     * 5 = Auto, Continuous    <br></br>
     */
    const val TAG_FOCUS_MODE = 0x0007
    /**
     * <br></br>
     * 2 bytes                         <br></br>
     * (DMC-FZ10)                      <br></br>
     * '0 1' = Spot Mode On            <br></br>
     * '0 16' = Spot Mode Off          <br></br>
     * '(other models)                 <br></br>
     * 16 = Normal?                    <br></br>
     * '0 1' = 9-area                  <br></br>
     * '0 16' = 3-area (high speed)    <br></br>
     * '1 0' = Spot Focusing           <br></br>
     * '1 1' = 5-area                  <br></br>
     * '16 0' = 1-area                 <br></br>
     * '16 16' = 1-area (high speed)   <br></br>
     * '32 0' = Auto or Face Detect    <br></br>
     * '32 1' = 3-area (left)?         <br></br>
     * '32 2' = 3-area (center)?       <br></br>
     * '32 3' = 3-area (right)?        <br></br>
     * '64 0' = Face Detect            <br></br>
     */
    const val TAG_AF_AREA_MODE = 0x000f
    /**
     * <br></br>
     * 2 = On, Mode 1   <br></br>
     * 3 = Off          <br></br>
     * 4 = On, Mode 2   <br></br>
     */
    const val TAG_IMAGE_STABILIZATION = 0x001a
    /**
     * <br></br>
     * 1 = On    <br></br>
     * 2 = Off   <br></br>
     */
    const val TAG_MACRO_MODE = 0x001C
    /**
     * <br></br>
     * 1 = Normal                            <br></br>
     * 2 = Portrait                          <br></br>
     * 3 = Scenery                           <br></br>
     * 4 = Sports                            <br></br>
     * 5 = Night Portrait                    <br></br>
     * 6 = Program                           <br></br>
     * 7 = Aperture Priority                 <br></br>
     * 8 = Shutter Priority                  <br></br>
     * 9 = Macro                             <br></br>
     * 10= Spot                              <br></br>
     * 11= Manual                            <br></br>
     * 12= Movie Preview                     <br></br>
     * 13= Panning                           <br></br>
     * 14= Simple                            <br></br>
     * 15= Color Effects                     <br></br>
     * 16= Self Portrait                     <br></br>
     * 17= Economy                           <br></br>
     * 18= Fireworks                         <br></br>
     * 19= Party                             <br></br>
     * 20= Snow                              <br></br>
     * 21= Night Scenery                     <br></br>
     * 22= Food                              <br></br>
     * 23= Baby                              <br></br>
     * 24= Soft Skin                         <br></br>
     * 25= Candlelight                       <br></br>
     * 26= Starry Night                      <br></br>
     * 27= High Sensitivity                  <br></br>
     * 28= Panorama Assist                   <br></br>
     * 29= Underwater                        <br></br>
     * 30= Beach                             <br></br>
     * 31= Aerial Photo                      <br></br>
     * 32= Sunset                            <br></br>
     * 33= Pet                               <br></br>
     * 34= Intelligent ISO                   <br></br>
     * 35= Clipboard                         <br></br>
     * 36= High Speed Continuous Shooting    <br></br>
     * 37= Intelligent Auto                  <br></br>
     * 39= Multi-aspect                      <br></br>
     * 41= Transform                         <br></br>
     * 42= Flash Burst                       <br></br>
     * 43= Pin Hole                          <br></br>
     * 44= Film Grain                        <br></br>
     * 45= My Color                          <br></br>
     * 46= Photo Frame                       <br></br>
     * 51= HDR                               <br></br>
     */
    const val TAG_RECORD_MODE = 0x001F
    /**
     * 1 = Yes <br></br>
     * 2 = No  <br></br>
     */
    const val TAG_AUDIO = 0x0020
    /**
     * No idea, what this is
     */
    const val TAG_UNKNOWN_DATA_DUMP = 0x0021
    const val TAG_EASY_MODE = 0x0022
    const val TAG_WHITE_BALANCE_BIAS = 0x0023
    const val TAG_FLASH_BIAS = 0x0024
    /**
     * this number is unique, and contains the date of manufacture,
     * but is not the same as the number printed on the camera body
     */
    const val TAG_INTERNAL_SERIAL_NUMBER = 0x0025
    /**
     * Panasonic Exif Version
     */
    const val TAG_EXIF_VERSION = 0x0026
    /**
     * 1 = Off           <br></br>
     * 2 = Warm          <br></br>
     * 3 = Cool          <br></br>
     * 4 = Black &amp; White <br></br>
     * 5 = Sepia         <br></br>
     */
    const val TAG_COLOR_EFFECT = 0x0028
    /**
     * 4 Bytes <br></br>
     * Time in 1/100 s from when the camera was powered on to when the
     * image is written to memory card
     */
    const val TAG_UPTIME = 0x0029
    /**
     * 0 = Off        <br></br>
     * 1 = On         <br></br>
     * 2 = Infinite   <br></br>
     * 4 = Unlimited  <br></br>
     */
    const val TAG_BURST_MODE = 0x002a
    const val TAG_SEQUENCE_NUMBER = 0x002b
    /**
     * (this decoding seems to work for some models such as the LC1, LX2, FZ7, FZ8, FZ18 and FZ50, but may not be correct for other models such as the FX10, G1, L1, L10 and LC80) <br></br>
     * 0x0 = Normal                                            <br></br>
     * 0x1 = Low                                               <br></br>
     * 0x2 = High                                              <br></br>
     * 0x6 = Medium Low                                        <br></br>
     * 0x7 = Medium High                                       <br></br>
     * 0x100 = Low                                             <br></br>
     * 0x110 = Normal                                          <br></br>
     * 0x120 = High                                            <br></br>
     * (these values are used by the GF1)                      <br></br>
     * 0 = -2                                                  <br></br>
     * 1 = -1                                                  <br></br>
     * 2 = Normal                                              <br></br>
     * 3 = +1                                                  <br></br>
     * 4 = +2                                                  <br></br>
     * 7 = Nature (Color Film)                                 <br></br>
     * 12 = Smooth (Color Film) or Pure (My Color)             <br></br>
     * 17 = Dynamic (B&amp;W Film)                                 <br></br>
     * 22 = Smooth (B&amp;W Film)                                  <br></br>
     * 27 = Dynamic (Color Film)                               <br></br>
     * 32 = Vibrant (Color Film) or Expressive (My Color)      <br></br>
     * 33 = Elegant (My Color)                                 <br></br>
     * 37 = Nostalgic (Color Film)                             <br></br>
     * 41 = Dynamic Art (My Color)                             <br></br>
     * 42 = Retro (My Color)                                   <br></br>
     */
    const val TAG_CONTRAST_MODE = 0x002c
    /**
     * 0 = Standard      <br></br>
     * 1 = Low (-1)      <br></br>
     * 2 = High (+1)     <br></br>
     * 3 = Lowest (-2)   <br></br>
     * 4 = Highest (+2)  <br></br>
     */
    const val TAG_NOISE_REDUCTION = 0x002d
    /**
     * 1 = Off   <br></br>
     * 2 = 10 s  <br></br>
     * 3 = 2 s   <br></br>
     */
    const val TAG_SELF_TIMER = 0x002e
    /**
     * 1 = 0 DG    <br></br>
     * 3 = 180 DG  <br></br>
     * 6 =  90 DG  <br></br>
     * 8 = 270 DG  <br></br>
     */
    const val TAG_ROTATION = 0x0030
    /**
     * 1 = Fired <br></br>
     * 2 = Enabled nut not used <br></br>
     * 3 = Disabled but required <br></br>
     * 4 = Disabled and not required
     */
    const val TAG_AF_ASSIST_LAMP = 0x0031
    /**
     * 0 = Normal <br></br>
     * 1 = Natural<br></br>
     * 2 = Vivid
     *
     */
    const val TAG_COLOR_MODE = 0x0032
    const val TAG_BABY_AGE = 0x0033
    /**
     * 1 = Standard <br></br>
     * 2 = Extended
     */
    const val TAG_OPTICAL_ZOOM_MODE = 0x0034
    /**
     * 1 = Off <br></br>
     * 2 = Wide <br></br>
     * 3 = Telephoto <br></br>
     * 4 = Macro
     */
    const val TAG_CONVERSION_LENS = 0x0035
    const val TAG_TRAVEL_DAY = 0x0036
    /**
     * 0 = Normal
     */
    const val TAG_CONTRAST = 0x0039
    /**
     * <br></br>
     * 1 = Home <br></br>
     * 2 = Destination
     */
    const val TAG_WORLD_TIME_LOCATION = 0x003a
    /**
     * 1 = Off   <br></br>
     * 2 = On
     */
    const val TAG_TEXT_STAMP = 0x003b
    const val TAG_PROGRAM_ISO = 0x003c
    /**
     * <br></br>
     * 1 = Normal                               <br></br>
     * 2 = Outdoor/Illuminations/Flower/HDR Art <br></br>
     * 3 = Indoor/Architecture/Objects/HDR B&amp;W  <br></br>
     * 4 = Creative                             <br></br>
     * 5 = Auto                                 <br></br>
     * 7 = Expressive                           <br></br>
     * 8 = Retro                                <br></br>
     * 9 = Pure                                 <br></br>
     * 10 = Elegant                             <br></br>
     * 12 = Monochrome                          <br></br>
     * 13 = Dynamic Art                         <br></br>
     * 14 = Silhouette                          <br></br>
     */
    const val TAG_ADVANCED_SCENE_MODE = 0x003d
    /**
     * 1 = Off   <br></br>
     * 2 = On
     */
    const val TAG_TEXT_STAMP_1 = 0x003e
    const val TAG_FACES_DETECTED = 0x003f
    const val TAG_SATURATION = 0x0040
    const val TAG_SHARPNESS = 0x0041
    const val TAG_FILM_MODE = 0x0042
    const val TAG_COLOR_TEMP_KELVIN = 0x0044
    const val TAG_BRACKET_SETTINGS = 0x0045
    /**
     * WB adjust AB. Positive is a shift toward blue.
     */
    const val TAG_WB_ADJUST_AB = 0x0046
    /**
     * WB adjust GM. Positive is a shift toward green.
     */
    const val TAG_WB_ADJUST_GM = 0x0047
    const val TAG_FLASH_CURTAIN = 0x0048
    const val TAG_LONG_EXPOSURE_NOISE_REDUCTION = 0x0049
    const val TAG_PANASONIC_IMAGE_WIDTH = 0x004b
    const val TAG_PANASONIC_IMAGE_HEIGHT = 0x004c
    const val TAG_AF_POINT_POSITION = 0x004d
    /**
     * <br></br>
     * Integer (16Bit) Indexes:                                             <br></br>
     * 0  Number Face Positions (maybe less than Faces Detected)            <br></br>
     * 1-4 Face Position 1                                                  <br></br>
     * 5-8 Face Position 2                                                  <br></br>
     * and so on                                                            <br></br>
     * <br></br>
     * The four Integers are interpreted as follows:                        <br></br>
     * (XYWH)  X,Y Center of Face,  (W,H) Width and Height                  <br></br>
     * All values are in respect to double the size of the thumbnail image  <br></br>
     *
     */
    const val TAG_FACE_DETECTION_INFO = 0x004e
    const val TAG_LENS_TYPE = 0x0051
    const val TAG_LENS_SERIAL_NUMBER = 0x0052
    const val TAG_ACCESSORY_TYPE = 0x0053
    const val TAG_ACCESSORY_SERIAL_NUMBER = 0x0054
    /**
     * (decoded as two 16-bit signed integers)
     * '-1 1' = Slim Low
     * '-3 2' = Slim High
     * '0 0' = Off
     * '1 1' = Stretch Low
     * '3 2' = Stretch High
     */
    const val TAG_TRANSFORM = 0x0059
    /**
     * 0 = Off <br></br>
     * 1 = Low <br></br>
     * 2 = Standard <br></br>
     * 3 = High
     */
    const val TAG_INTELLIGENT_EXPOSURE = 0x005d
    const val TAG_LENS_FIRMWARE_VERSION = 0x0060
    const val TAG_BURST_SPEED = 0x0077
    const val TAG_INTELLIGENT_D_RANGE = 0x0079
    const val TAG_CLEAR_RETOUCH = 0x007c
    const val TAG_CITY2 = 0x0080
    const val TAG_PHOTO_STYLE = 0x0089
    const val TAG_SHADING_COMPENSATION = 0x008a
    const val TAG_ACCELEROMETER_Z = 0x008c
    const val TAG_ACCELEROMETER_X = 0x008d
    const val TAG_ACCELEROMETER_Y = 0x008e
    const val TAG_CAMERA_ORIENTATION = 0x008f
    const val TAG_ROLL_ANGLE = 0x0090
    const val TAG_PITCH_ANGLE = 0x0091
    const val TAG_SWEEP_PANORAMA_DIRECTION = 0x0093
    const val TAG_SWEEP_PANORAMA_FIELD_OF_VIEW = 0x0094
    const val TAG_TIMER_RECORDING = 0x0096
    const val TAG_INTERNAL_ND_FILTER = 0x009d
    const val TAG_HDR = 0x009e
    const val TAG_SHUTTER_TYPE = 0x009f
    const val TAG_CLEAR_RETOUCH_VALUE = 0x00a3
    const val TAG_TOUCH_AE = 0x00ab
    /**
     * Info at http://www.ozhiker.com/electronics/pjmt/jpeg_info/pim.html
     */
    const val TAG_PRINT_IMAGE_MATCHING_INFO = 0x0E00
    /**
     * Byte Indexes:                                                                       <br></br>
     * 0    Int (2  Byte) Number of Recognized Faces                                      <br></br>
     * 4    String(20 Byte)    Recognized Face 1 Name                                     <br></br>
     * 24    4 Int (8 Byte)     Recognized Face 1 Position  (Same Format as Face Detection)  <br></br>
     * 32    String(20 Byte)    Recognized Face 1 Age                                      <br></br>
     * 52    String(20 Byte)    Recognized Face 2 Name                                     <br></br>
     * 72    4 Int (8 Byte)     Recognized Face 2 Position  (Same Format as Face Detection)  <br></br>
     * 80    String(20 Byte)    Recognized Face 2 Age                                      <br></br>
     * <br></br>
     * And so on                                                                           <br></br>
     * <br></br>
     * The four Integers are interpreted as follows:                                       <br></br>
     * (XYWH)  X,Y Center of Face,  (W,H) Width and Height                                 <br></br>
     * All values are in respect to double the size of the thumbnail image                 <br></br>
     *
     */
    const val TAG_FACE_RECOGNITION_INFO = 0x0061
    /**
     * 0 = No <br></br>
     * 1 = Yes
     */
    const val TAG_FLASH_WARNING = 0x0062
    const val TAG_RECOGNIZED_FACE_FLAGS = 0x0063
    const val TAG_TITLE = 0x0065
    const val TAG_BABY_NAME = 0x0066
    const val TAG_LOCATION = 0x0067
    const val TAG_COUNTRY = 0x0069
    const val TAG_STATE = 0x006b
    const val TAG_CITY = 0x006d
    const val TAG_LANDMARK = 0x006f
    /**
     * 0 = Off <br></br>
     * 2 = Auto <br></br>
     * 3 = On
     */
    const val TAG_INTELLIGENT_RESOLUTION = 0x0070
    const val TAG_MAKERNOTE_VERSION = 0x8000
    const val TAG_SCENE_MODE = 0x8001
    const val TAG_WB_RED_LEVEL = 0x8004
    const val TAG_WB_GREEN_LEVEL = 0x8005
    const val TAG_WB_BLUE_LEVEL = 0x8006
    const val TAG_FLASH_FIRED = 0x8007
    const val TAG_TEXT_STAMP_2 = 0x8008
    const val TAG_TEXT_STAMP_3 = 0x8009
    const val TAG_BABY_AGE_1 = 0x8010
    /**
     * (decoded as two 16-bit signed integers)
     * '-1 1' = Slim Low
     * '-3 2' = Slim High
     * '0 0' = Off
     * '1 1' = Stretch Low
     * '3 2' = Stretch High
     */
    const val TAG_TRANSFORM_1 = 0x8012
    protected val tagNameMap = HashMap<Int, String>()

    init {
      tagNameMap[TAG_QUALITY_MODE] = "Quality Mode"
      tagNameMap[TAG_FIRMWARE_VERSION] = "Version"
      tagNameMap[TAG_WHITE_BALANCE] = "White Balance"
      tagNameMap[TAG_FOCUS_MODE] = "Focus Mode"
      tagNameMap[TAG_AF_AREA_MODE] = "AF Area Mode"
      tagNameMap[TAG_IMAGE_STABILIZATION] = "Image Stabilization"
      tagNameMap[TAG_MACRO_MODE] = "Macro Mode"
      tagNameMap[TAG_RECORD_MODE] = "Record Mode"
      tagNameMap[TAG_AUDIO] = "Audio"
      tagNameMap[TAG_INTERNAL_SERIAL_NUMBER] = "Internal Serial Number"
      tagNameMap[TAG_UNKNOWN_DATA_DUMP] = "Unknown Data Dump"
      tagNameMap[TAG_EASY_MODE] = "Easy Mode"
      tagNameMap[TAG_WHITE_BALANCE_BIAS] = "White Balance Bias"
      tagNameMap[TAG_FLASH_BIAS] = "Flash Bias"
      tagNameMap[TAG_EXIF_VERSION] = "Exif Version"
      tagNameMap[TAG_COLOR_EFFECT] = "Color Effect"
      tagNameMap[TAG_UPTIME] = "Camera Uptime"
      tagNameMap[TAG_BURST_MODE] = "Burst Mode"
      tagNameMap[TAG_SEQUENCE_NUMBER] = "Sequence Number"
      tagNameMap[TAG_CONTRAST_MODE] = "Contrast Mode"
      tagNameMap[TAG_NOISE_REDUCTION] = "Noise Reduction"
      tagNameMap[TAG_SELF_TIMER] = "Self Timer"
      tagNameMap[TAG_ROTATION] = "Rotation"
      tagNameMap[TAG_AF_ASSIST_LAMP] = "AF Assist Lamp"
      tagNameMap[TAG_COLOR_MODE] = "Color Mode"
      tagNameMap[TAG_BABY_AGE] = "Baby Age"
      tagNameMap[TAG_OPTICAL_ZOOM_MODE] = "Optical Zoom Mode"
      tagNameMap[TAG_CONVERSION_LENS] = "Conversion Lens"
      tagNameMap[TAG_TRAVEL_DAY] = "Travel Day"
      tagNameMap[TAG_CONTRAST] = "Contrast"
      tagNameMap[TAG_WORLD_TIME_LOCATION] = "World Time Location"
      tagNameMap[TAG_TEXT_STAMP] = "Text Stamp"
      tagNameMap[TAG_PROGRAM_ISO] = "Program ISO"
      tagNameMap[TAG_ADVANCED_SCENE_MODE] = "Advanced Scene Mode"
      tagNameMap[TAG_PRINT_IMAGE_MATCHING_INFO] = "Print Image Matching (PIM) Info"
      tagNameMap[TAG_FACES_DETECTED] = "Number of Detected Faces"
      tagNameMap[TAG_SATURATION] = "Saturation"
      tagNameMap[TAG_SHARPNESS] = "Sharpness"
      tagNameMap[TAG_FILM_MODE] = "Film Mode"
      tagNameMap[TAG_COLOR_TEMP_KELVIN] = "Color Temp Kelvin"
      tagNameMap[TAG_BRACKET_SETTINGS] = "Bracket Settings"
      tagNameMap[TAG_WB_ADJUST_AB] = "White Balance Adjust (AB)"
      tagNameMap[TAG_WB_ADJUST_GM] = "White Balance Adjust (GM)"
      tagNameMap[TAG_FLASH_CURTAIN] = "Flash Curtain"
      tagNameMap[TAG_LONG_EXPOSURE_NOISE_REDUCTION] = "Long Exposure Noise Reduction"
      tagNameMap[TAG_PANASONIC_IMAGE_WIDTH] = "Panasonic Image Width"
      tagNameMap[TAG_PANASONIC_IMAGE_HEIGHT] = "Panasonic Image Height"
      tagNameMap[TAG_AF_POINT_POSITION] = "Af Point Position"
      tagNameMap[TAG_FACE_DETECTION_INFO] = "Face Detection Info"
      tagNameMap[TAG_LENS_TYPE] = "Lens Type"
      tagNameMap[TAG_LENS_SERIAL_NUMBER] = "Lens Serial Number"
      tagNameMap[TAG_ACCESSORY_TYPE] = "Accessory Type"
      tagNameMap[TAG_ACCESSORY_SERIAL_NUMBER] = "Accessory Serial Number"
      tagNameMap[TAG_TRANSFORM] = "Transform"
      tagNameMap[TAG_INTELLIGENT_EXPOSURE] = "Intelligent Exposure"
      tagNameMap[TAG_LENS_FIRMWARE_VERSION] = "Lens Firmware Version"
      tagNameMap[TAG_FACE_RECOGNITION_INFO] = "Face Recognition Info"
      tagNameMap[TAG_FLASH_WARNING] = "Flash Warning"
      tagNameMap[TAG_RECOGNIZED_FACE_FLAGS] = "Recognized Face Flags"
      tagNameMap[TAG_TITLE] = "Title"
      tagNameMap[TAG_BABY_NAME] = "Baby Name"
      tagNameMap[TAG_LOCATION] = "Location"
      tagNameMap[TAG_COUNTRY] = "Country"
      tagNameMap[TAG_STATE] = "State"
      tagNameMap[TAG_CITY] = "City"
      tagNameMap[TAG_LANDMARK] = "Landmark"
      tagNameMap[TAG_INTELLIGENT_RESOLUTION] = "Intelligent Resolution"
      tagNameMap[TAG_BURST_SPEED] = "Burst Speed"
      tagNameMap[TAG_INTELLIGENT_D_RANGE] = "Intelligent D-Range"
      tagNameMap[TAG_CLEAR_RETOUCH] = "Clear Retouch"
      tagNameMap[TAG_CITY2] = "City 2"
      tagNameMap[TAG_PHOTO_STYLE] = "Photo Style"
      tagNameMap[TAG_SHADING_COMPENSATION] = "Shading Compensation"
      tagNameMap[TAG_ACCELEROMETER_Z] = "Accelerometer Z"
      tagNameMap[TAG_ACCELEROMETER_X] = "Accelerometer X"
      tagNameMap[TAG_ACCELEROMETER_Y] = "Accelerometer Y"
      tagNameMap[TAG_CAMERA_ORIENTATION] = "Camera Orientation"
      tagNameMap[TAG_ROLL_ANGLE] = "Roll Angle"
      tagNameMap[TAG_PITCH_ANGLE] = "Pitch Angle"
      tagNameMap[TAG_SWEEP_PANORAMA_DIRECTION] = "Sweep Panorama Direction"
      tagNameMap[TAG_SWEEP_PANORAMA_FIELD_OF_VIEW] = "Sweep Panorama Field Of View"
      tagNameMap[TAG_TIMER_RECORDING] = "Timer Recording"
      tagNameMap[TAG_INTERNAL_ND_FILTER] = "Internal ND Filter"
      tagNameMap[TAG_HDR] = "HDR"
      tagNameMap[TAG_SHUTTER_TYPE] = "Shutter Type"
      tagNameMap[TAG_CLEAR_RETOUCH_VALUE] = "Clear Retouch Value"
      tagNameMap[TAG_TOUCH_AE] = "Touch AE"
      tagNameMap[TAG_MAKERNOTE_VERSION] = "Makernote Version"
      tagNameMap[TAG_SCENE_MODE] = "Scene Mode"
      tagNameMap[TAG_WB_RED_LEVEL] = "White Balance (Red)"
      tagNameMap[TAG_WB_GREEN_LEVEL] = "White Balance (Green)"
      tagNameMap[TAG_WB_BLUE_LEVEL] = "White Balance (Blue)"
      tagNameMap[TAG_FLASH_FIRED] = "Flash Fired"
      tagNameMap[TAG_TEXT_STAMP_1] = "Text Stamp 1"
      tagNameMap[TAG_TEXT_STAMP_2] = "Text Stamp 2"
      tagNameMap[TAG_TEXT_STAMP_3] = "Text Stamp 3"
      tagNameMap[TAG_BABY_AGE_1] = "Baby Age 1"
      tagNameMap[TAG_TRANSFORM_1] = "Transform 1"
    }
  }

  override val name: String
    get() = "Panasonic Makernote"

  val detectedFaces: Array<Face?>?
    get() {
      val bytes = getByteArray(TAG_FACE_DETECTION_INFO) ?: return null
      val reader: RandomAccessReader = ByteArrayReader(bytes)
      reader.isMotorolaByteOrder = false
      return try {
        val faceCount = reader.getUInt16(0)
        if (faceCount == 0) return null
        val faces = arrayOfNulls<Face>(faceCount)
        for (i in 0 until faceCount) {
          val offset = 2 + i * 8
          faces[i] = Face(
            reader.getUInt16(offset),
            reader.getUInt16(offset + 2),
            reader.getUInt16(offset + 4),
            reader.getUInt16(offset + 6)
            , null, null)
        }
        faces
      } catch (e: IOException) {
        null
      }
    }

  val recognizedFaces: Array<Face?>?
    get() {
      val bytes = getByteArray(TAG_FACE_RECOGNITION_INFO) ?: return null
      val reader: RandomAccessReader = ByteArrayReader(bytes)
      reader.isMotorolaByteOrder = false
      return try {
        val faceCount = reader.getUInt16(0)
        if (faceCount == 0) return null
        val faces = arrayOfNulls<Face>(faceCount)
        for (i in 0 until faceCount) {
          val offset = 4 + i * 44
          val name = reader.getString(offset, 20, "ASCII").trim { it <= ' ' }
          val age = reader.getString(offset + 28, 20, "ASCII").trim { it <= ' ' }
          faces[i] = Face(
            reader.getUInt16(offset + 20),
            reader.getUInt16(offset + 22),
            reader.getUInt16(offset + 24),
            reader.getUInt16(offset + 26),
            name,
            fromPanasonicString(age))
        }
        faces
      } catch (e: IOException) {
        null
      }
    }

  /**
   * Attempts to convert the underlying string value (as stored in the directory) into an Age object.
   * @param tag The tag identifier.
   * @return The parsed Age object, or null if the tag was empty of the value unable to be parsed.
   */
  fun getAge(tag: Int): Age? {
    val ageString = getString(tag) ?: return null
    return fromPanasonicString(ageString)
  }

  init {
    setDescriptor(PanasonicMakernoteDescriptor(this))
  }
}
