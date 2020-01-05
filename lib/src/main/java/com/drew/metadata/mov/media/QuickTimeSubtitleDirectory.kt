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
package com.drew.metadata.mov.media

import com.drew.metadata.mov.QuickTimeDirectory
import com.drew.metadata.mov.media.QuickTimeMediaDirectory.Companion.addQuickTimeMediaTags
import java.util.*

/**
 * @author Payton Garland
 */
class QuickTimeSubtitleDirectory : QuickTimeDirectory() {
  companion object {
    const val TAG_VERTICAL_PLACEMENT = 1
    const val TAG_SOME_SAMPLES_FORCED = 2
    const val TAG_ALL_SAMPLES_FORCED = 3
    const val TAG_DEFAULT_TEXT_BOX = 4
    const val TAG_FONT_IDENTIFIER = 5
    const val TAG_FONT_FACE = 6
    const val TAG_FONT_SIZE = 7
    const val TAG_FOREGROUND_COLOR = 8
    private val tagNameMap = HashMap<Int, String>()

    init {
      addQuickTimeMediaTags(this.tagNameMap)
      tagNameMap[TAG_VERTICAL_PLACEMENT] = "Vertical Placement"
      tagNameMap[TAG_SOME_SAMPLES_FORCED] = "Some Samples Forced"
      tagNameMap[TAG_ALL_SAMPLES_FORCED] = "All Samples Forced"
      tagNameMap[TAG_DEFAULT_TEXT_BOX] = "Default Text Box"
      tagNameMap[TAG_FONT_IDENTIFIER] = "Font Identifier"
      tagNameMap[TAG_FONT_FACE] = "Font Face"
      tagNameMap[TAG_FONT_SIZE] = "Font Size"
      tagNameMap[TAG_FOREGROUND_COLOR] = "Foreground Color"
    }
  }

  override val name: String
    get() = "QuickTime Subtitle"

  init {
    setDescriptor(QuickTimeSubtitleDescriptor(this))
  }
}
