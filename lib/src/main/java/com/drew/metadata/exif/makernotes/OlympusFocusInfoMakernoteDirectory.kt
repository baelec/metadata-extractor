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
 * The Olympus focus info makernote is used by many manufacturers (Epson, Konica, Minolta and Agfa...), and as such contains some tags
 * that appear specific to those manufacturers.
 *
 * @author Kevin Mott https://github.com/kwhopper
 * @author Drew Noakes https://drewnoakes.com
 */
class OlympusFocusInfoMakernoteDirectory : Directory() {
  override val tagNameMap = Companion.tagNameMap

  companion object {
    const val TagFocusInfoVersion = 0x0000
    const val TagAutoFocus = 0x0209
    const val TagSceneDetect = 0x0210
    const val TagSceneArea = 0x0211
    const val TagSceneDetectData = 0x0212
    const val TagZoomStepCount = 0x0300
    const val TagFocusStepCount = 0x0301
    const val TagFocusStepInfinity = 0x0303
    const val TagFocusStepNear = 0x0304
    const val TagFocusDistance = 0x0305
    const val TagAfPoint = 0x0308
    // 0x031a Continuous AF parameters?
    const val TagAfInfo = 0x0328 // ifd
    const val TagExternalFlash = 0x1201
    const val TagExternalFlashGuideNumber = 0x1203
    const val TagExternalFlashBounce = 0x1204
    const val TagExternalFlashZoom = 0x1205
    const val TagInternalFlash = 0x1208
    const val TagManualFlash = 0x1209
    const val TagMacroLed = 0x120A
    const val TagSensorTemperature = 0x1500
    const val TagImageStabilization = 0x1600
    protected val tagNameMap = HashMap<Int, String>()

    init {
      tagNameMap[TagFocusInfoVersion] = "Focus Info Version"
      tagNameMap[TagAutoFocus] = "Auto Focus"
      tagNameMap[TagSceneDetect] = "Scene Detect"
      tagNameMap[TagSceneArea] = "Scene Area"
      tagNameMap[TagSceneDetectData] = "Scene Detect Data"
      tagNameMap[TagZoomStepCount] = "Zoom Step Count"
      tagNameMap[TagFocusStepCount] = "Focus Step Count"
      tagNameMap[TagFocusStepInfinity] = "Focus Step Infinity"
      tagNameMap[TagFocusStepNear] = "Focus Step Near"
      tagNameMap[TagFocusDistance] = "Focus Distance"
      tagNameMap[TagAfPoint] = "AF Point"
      tagNameMap[TagAfInfo] = "AF Info"
      tagNameMap[TagExternalFlash] = "External Flash"
      tagNameMap[TagExternalFlashGuideNumber] = "External Flash Guide Number"
      tagNameMap[TagExternalFlashBounce] = "External Flash Bounce"
      tagNameMap[TagExternalFlashZoom] = "External Flash Zoom"
      tagNameMap[TagInternalFlash] = "Internal Flash"
      tagNameMap[TagManualFlash] = "Manual Flash"
      tagNameMap[TagMacroLed] = "Macro LED"
      tagNameMap[TagSensorTemperature] = "Sensor Temperature"
      tagNameMap[TagImageStabilization] = "Image Stabilization"
    }
  }

  override val name: String
    get() = "Olympus Focus Info"

  init {
    setDescriptor(OlympusFocusInfoMakernoteDescriptor(this))
  }
}
