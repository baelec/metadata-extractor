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
 * Describes tags specific to certain Leica cameras.
 *
 *
 * Tag reference from: http://www.sno.phy.queensu.ca/~phil/exiftool/TagNames/Panasonic.html
 *
 * @author Kevin Mott https://github.com/kwhopper
 * @author Drew Noakes https://drewnoakes.com
 */
class LeicaType5MakernoteDirectory : Directory() {
  override val tagNameMap = Companion.tagNameMap

  companion object {
    const val TagLensModel = 0x0303
    const val TagOriginalFileName = 0x0407
    const val TagOriginalDirectory = 0x0408
    const val TagExposureMode = 0x040d
    const val TagShotInfo = 0x0410
    const val TagFilmMode = 0x0412
    const val TagWbRgbLevels = 0x0413
    protected val tagNameMap = HashMap<Int, String>()

    init {
      tagNameMap[TagLensModel] = "Lens Model"
      tagNameMap[TagOriginalFileName] = "Original File Name"
      tagNameMap[TagOriginalDirectory] = "Original Directory"
      tagNameMap[TagExposureMode] = "Exposure Mode"
      tagNameMap[TagShotInfo] = "Shot Info"
      tagNameMap[TagFilmMode] = "Film Mode"
      tagNameMap[TagWbRgbLevels] = "WB RGB Levels"
    }
  }

  override val name: String
    get() = "Leica Makernote"

  init {
    setDescriptor(LeicaType5MakernoteDescriptor(this))
  }
}
