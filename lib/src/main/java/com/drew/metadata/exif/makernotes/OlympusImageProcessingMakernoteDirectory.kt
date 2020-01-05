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

import com.drew.metadata.Directory
import java.util.*

/**
 * The Olympus image processing makernote is used by many manufacturers (Epson, Konica, Minolta and Agfa...), and as such contains some tags
 * that appear specific to those manufacturers.
 *
 * @author Kevin Mott https://github.com/kwhopper
 * @author Drew Noakes https://drewnoakes.com
 */
class OlympusImageProcessingMakernoteDirectory : Directory() {
  override val tagNameMap = Companion.tagNameMap

  companion object {
    const val TagImageProcessingVersion = 0x0000
    const val TagWbRbLevels = 0x0100
    // 0x0101 - in-camera AutoWB unless it is all 0's or all 256's (ref IB)
    const val TagWbRbLevels3000K = 0x0102
    const val TagWbRbLevels3300K = 0x0103
    const val TagWbRbLevels3600K = 0x0104
    const val TagWbRbLevels3900K = 0x0105
    const val TagWbRbLevels4000K = 0x0106
    const val TagWbRbLevels4300K = 0x0107
    const val TagWbRbLevels4500K = 0x0108
    const val TagWbRbLevels4800K = 0x0109
    const val TagWbRbLevels5300K = 0x010a
    const val TagWbRbLevels6000K = 0x010b
    const val TagWbRbLevels6600K = 0x010c
    const val TagWbRbLevels7500K = 0x010d
    const val TagWbRbLevelsCwB1 = 0x010e
    const val TagWbRbLevelsCwB2 = 0x010f
    const val TagWbRbLevelsCwB3 = 0x0110
    const val TagWbRbLevelsCwB4 = 0x0111
    const val TagWbGLevel3000K = 0x0113
    const val TagWbGLevel3300K = 0x0114
    const val TagWbGLevel3600K = 0x0115
    const val TagWbGLevel3900K = 0x0116
    const val TagWbGLevel4000K = 0x0117
    const val TagWbGLevel4300K = 0x0118
    const val TagWbGLevel4500K = 0x0119
    const val TagWbGLevel4800K = 0x011a
    const val TagWbGLevel5300K = 0x011b
    const val TagWbGLevel6000K = 0x011c
    const val TagWbGLevel6600K = 0x011d
    const val TagWbGLevel7500K = 0x011e
    const val TagWbGLevel = 0x011f
    // 0x0121 = WB preset for flash (about 6000K) (ref IB)
// 0x0125 = WB preset for underwater (ref IB)
    const val TagColorMatrix = 0x0200
    // color matrices (ref 11):
// 0x0201-0x020d are sRGB color matrices
// 0x020e-0x021a are Adobe RGB color matrices
// 0x021b-0x0227 are ProPhoto RGB color matrices
// 0x0228 and 0x0229 are ColorMatrix for E-330
// 0x0250-0x0252 are sRGB color matrices
// 0x0253-0x0255 are Adobe RGB color matrices
// 0x0256-0x0258 are ProPhoto RGB color matrices
    const val TagEnhancer = 0x0300
    const val TagEnhancerValues = 0x0301
    const val TagCoringFilter = 0x0310
    const val TagCoringValues = 0x0311
    const val TagBlackLevel2 = 0x0600
    const val TagGainBase = 0x0610
    const val TagValidBits = 0x0611
    const val TagCropLeft = 0x0612
    const val TagCropTop = 0x0613
    const val TagCropWidth = 0x0614
    const val TagCropHeight = 0x0615
    const val TagUnknownBlock1 = 0x0635
    const val TagUnknownBlock2 = 0x0636
    // 0x0800 LensDistortionParams, float[9] (ref 11)
// 0x0801 LensShadingParams, int16u[16] (ref 11)
    const val TagSensorCalibration = 0x0805
    const val TagNoiseReduction2 = 0x1010
    const val TagDistortionCorrection2 = 0x1011
    const val TagShadingCompensation2 = 0x1012
    const val TagMultipleExposureMode = 0x101c
    const val TagUnknownBlock3 = 0x1103
    const val TagUnknownBlock4 = 0x1104
    const val TagAspectRatio = 0x1112
    const val TagAspectFrame = 0x1113
    const val TagFacesDetected = 0x1200
    const val TagFaceDetectArea = 0x1201
    const val TagMaxFaces = 0x1202
    const val TagFaceDetectFrameSize = 0x1203
    const val TagFaceDetectFrameCrop = 0x1207
    const val TagCameraTemperature = 0x1306
    const val TagKeystoneCompensation = 0x1900
    const val TagKeystoneDirection = 0x1901
    // 0x1905 - focal length (PH, E-M1)
    const val TagKeystoneValue = 0x1906
    protected val tagNameMap = HashMap<Int, String>()

    init {
      tagNameMap[TagImageProcessingVersion] = "Image Processing Version"
      tagNameMap[TagWbRbLevels] = "WB RB Levels"
      tagNameMap[TagWbRbLevels3000K] = "WB RB Levels 3000K"
      tagNameMap[TagWbRbLevels3300K] = "WB RB Levels 3300K"
      tagNameMap[TagWbRbLevels3600K] = "WB RB Levels 3600K"
      tagNameMap[TagWbRbLevels3900K] = "WB RB Levels 3900K"
      tagNameMap[TagWbRbLevels4000K] = "WB RB Levels 4000K"
      tagNameMap[TagWbRbLevels4300K] = "WB RB Levels 4300K"
      tagNameMap[TagWbRbLevels4500K] = "WB RB Levels 4500K"
      tagNameMap[TagWbRbLevels4800K] = "WB RB Levels 4800K"
      tagNameMap[TagWbRbLevels5300K] = "WB RB Levels 5300K"
      tagNameMap[TagWbRbLevels6000K] = "WB RB Levels 6000K"
      tagNameMap[TagWbRbLevels6600K] = "WB RB Levels 6600K"
      tagNameMap[TagWbRbLevels7500K] = "WB RB Levels 7500K"
      tagNameMap[TagWbRbLevelsCwB1] = "WB RB Levels CWB1"
      tagNameMap[TagWbRbLevelsCwB2] = "WB RB Levels CWB2"
      tagNameMap[TagWbRbLevelsCwB3] = "WB RB Levels CWB3"
      tagNameMap[TagWbRbLevelsCwB4] = "WB RB Levels CWB4"
      tagNameMap[TagWbGLevel3000K] = "WB G Level 3000K"
      tagNameMap[TagWbGLevel3300K] = "WB G Level 3300K"
      tagNameMap[TagWbGLevel3600K] = "WB G Level 3600K"
      tagNameMap[TagWbGLevel3900K] = "WB G Level 3900K"
      tagNameMap[TagWbGLevel4000K] = "WB G Level 4000K"
      tagNameMap[TagWbGLevel4300K] = "WB G Level 4300K"
      tagNameMap[TagWbGLevel4500K] = "WB G Level 4500K"
      tagNameMap[TagWbGLevel4800K] = "WB G Level 4800K"
      tagNameMap[TagWbGLevel5300K] = "WB G Level 5300K"
      tagNameMap[TagWbGLevel6000K] = "WB G Level 6000K"
      tagNameMap[TagWbGLevel6600K] = "WB G Level 6600K"
      tagNameMap[TagWbGLevel7500K] = "WB G Level 7500K"
      tagNameMap[TagWbGLevel] = "WB G Level"
      tagNameMap[TagColorMatrix] = "Color Matrix"
      tagNameMap[TagEnhancer] = "Enhancer"
      tagNameMap[TagEnhancerValues] = "Enhancer Values"
      tagNameMap[TagCoringFilter] = "Coring Filter"
      tagNameMap[TagCoringValues] = "Coring Values"
      tagNameMap[TagBlackLevel2] = "Black Level 2"
      tagNameMap[TagGainBase] = "Gain Base"
      tagNameMap[TagValidBits] = "Valid Bits"
      tagNameMap[TagCropLeft] = "Crop Left"
      tagNameMap[TagCropTop] = "Crop Top"
      tagNameMap[TagCropWidth] = "Crop Width"
      tagNameMap[TagCropHeight] = "Crop Height"
      tagNameMap[TagUnknownBlock1] = "Unknown Block 1"
      tagNameMap[TagUnknownBlock2] = "Unknown Block 2"
      tagNameMap[TagSensorCalibration] = "Sensor Calibration"
      tagNameMap[TagNoiseReduction2] = "Noise Reduction 2"
      tagNameMap[TagDistortionCorrection2] = "Distortion Correction 2"
      tagNameMap[TagShadingCompensation2] = "Shading Compensation 2"
      tagNameMap[TagMultipleExposureMode] = "Multiple Exposure Mode"
      tagNameMap[TagUnknownBlock3] = "Unknown Block 3"
      tagNameMap[TagUnknownBlock4] = "Unknown Block 4"
      tagNameMap[TagAspectRatio] = "Aspect Ratio"
      tagNameMap[TagAspectFrame] = "Aspect Frame"
      tagNameMap[TagFacesDetected] = "Faces Detected"
      tagNameMap[TagFaceDetectArea] = "Face Detect Area"
      tagNameMap[TagMaxFaces] = "Max Faces"
      tagNameMap[TagFaceDetectFrameSize] = "Face Detect Frame Size"
      tagNameMap[TagFaceDetectFrameCrop] = "Face Detect Frame Crop"
      tagNameMap[TagCameraTemperature] = "Camera Temperature"
      tagNameMap[TagKeystoneCompensation] = "Keystone Compensation"
      tagNameMap[TagKeystoneDirection] = "Keystone Direction"
      tagNameMap[TagKeystoneValue] = "Keystone Value"
    }
  }

  override val name: String
    get() = "Olympus Image Processing"

  init {
    setDescriptor(OlympusImageProcessingMakernoteDescriptor(this))
  }
}
