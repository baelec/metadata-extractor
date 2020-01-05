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
 * These tags can be found in Panasonic/Leica RAW, RW2 and RWL images. The index values are 'fake' but
 * chosen specifically to make processing easier
 *
 * @author Kevin Mott https://github.com/kwhopper
 * @author Drew Noakes https://drewnoakes.com
 */
class PanasonicRawDistortionDirectory : Directory() {
  override val tagNameMap = Companion.tagNameMap

  companion object {
    // 0 and 1 are checksums
    const val TagDistortionParam02 = 2
    const val TagDistortionParam04 = 4
    const val TagDistortionScale = 5
    const val TagDistortionCorrection = 7
    const val TagDistortionParam08 = 8
    const val TagDistortionParam09 = 9
    const val TagDistortionParam11 = 11
    const val TagDistortionN = 12
    protected val tagNameMap = HashMap<Int, String>()

    init {
      tagNameMap[TagDistortionParam02] = "Distortion Param 2"
      tagNameMap[TagDistortionParam04] = "Distortion Param 4"
      tagNameMap[TagDistortionScale] = "Distortion Scale"
      tagNameMap[TagDistortionCorrection] = "Distortion Correction"
      tagNameMap[TagDistortionParam08] = "Distortion Param 8"
      tagNameMap[TagDistortionParam09] = "Distortion Param 9"
      tagNameMap[TagDistortionParam11] = "Distortion Param 11"
      tagNameMap[TagDistortionN] = "Distortion N"
    }
  }

  override val name: String
    get() = "PanasonicRaw DistortionInfo"

  init {
    setDescriptor(PanasonicRawDistortionDescriptor(this))
  }
}
