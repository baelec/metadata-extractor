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
class QuickTimeTimecodeDirectory : QuickTimeDirectory() {
  companion object {
    // Timecode Media Description Atom
    const val TAG_DROP_FRAME = 1
    const val TAG_24_HOUR_MAX = 2
    const val TAG_NEGATIVE_TIMES_OK = 3
    const val TAG_COUNTER = 4
    const val TAG_TEXT_FONT = 5
    const val TAG_TEXT_FACE = 6
    const val TAG_TEXT_SIZE = 7
    const val TAG_TEXT_COLOR = 8
    const val TAG_BACKGROUND_COLOR = 9
    const val TAG_FONT_NAME = 10
    protected val tagNameMap = HashMap<Int, String>()

    init {
      addQuickTimeMediaTags(this.tagNameMap)
      tagNameMap[TAG_DROP_FRAME] = "Drop Frame"
      tagNameMap[TAG_24_HOUR_MAX] = "24 Hour Max"
      tagNameMap[TAG_NEGATIVE_TIMES_OK] = "Negative Times OK"
      tagNameMap[TAG_COUNTER] = "Counter"
      tagNameMap[TAG_TEXT_FONT] = "Text Font"
      tagNameMap[TAG_TEXT_FACE] = "Text Face"
      tagNameMap[TAG_TEXT_SIZE] = "Text Size"
      tagNameMap[TAG_TEXT_COLOR] = "Text Color"
      tagNameMap[TAG_BACKGROUND_COLOR] = "Background Color"
      tagNameMap[TAG_FONT_NAME] = "Font Name"
    }
  }

  override val name: String
    get() = "QuickTime Timecode"

  init {
    setDescriptor(QuickTimeTimecodeDescriptor(this))
  }
}
