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
class PanasonicRawWbInfo2Directory : Directory() {
  override val tagNameMap = Companion.tagNameMap

  companion object {
    const val TagNumWbEntries = 0
    const val TagWbType1 = 1
    const val TagWbRgbLevels1 = 2
    const val TagWbType2 = 5
    const val TagWbRgbLevels2 = 6
    const val TagWbType3 = 9
    const val TagWbRgbLevels3 = 10
    const val TagWbType4 = 13
    const val TagWbRgbLevels4 = 14
    const val TagWbType5 = 17
    const val TagWbRgbLevels5 = 18
    const val TagWbType6 = 21
    const val TagWbRgbLevels6 = 22
    const val TagWbType7 = 25
    const val TagWbRgbLevels7 = 26
    protected val tagNameMap = HashMap<Int, String>()

    init {
      tagNameMap[TagNumWbEntries] = "Num WB Entries"
      tagNameMap[TagNumWbEntries] = "Num WB Entries"
      tagNameMap[TagWbType1] = "WB Type 1"
      tagNameMap[TagWbRgbLevels1] = "WB RGB Levels 1"
      tagNameMap[TagWbType2] = "WB Type 2"
      tagNameMap[TagWbRgbLevels2] = "WB RGB Levels 2"
      tagNameMap[TagWbType3] = "WB Type 3"
      tagNameMap[TagWbRgbLevels3] = "WB RGB Levels 3"
      tagNameMap[TagWbType4] = "WB Type 4"
      tagNameMap[TagWbRgbLevels4] = "WB RGB Levels 4"
      tagNameMap[TagWbType5] = "WB Type 5"
      tagNameMap[TagWbRgbLevels5] = "WB RGB Levels 5"
      tagNameMap[TagWbType6] = "WB Type 6"
      tagNameMap[TagWbRgbLevels6] = "WB RGB Levels 6"
      tagNameMap[TagWbType7] = "WB Type 7"
      tagNameMap[TagWbRgbLevels7] = "WB RGB Levels 7"
    }
  }

  override val name: String
    get() = "PanasonicRaw WbInfo2"

  init {
    setDescriptor(PanasonicRawWbInfo2Descriptor(this))
  }
}
