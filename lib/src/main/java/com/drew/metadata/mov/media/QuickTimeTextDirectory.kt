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

/**
 * @author Payton Garland
 */
class QuickTimeTextDirectory : QuickTimeDirectory() {
  companion object {
    // Text Media Description Atom
    const val TAG_AUTO_SCALE = 1
    const val TAG_MOVIE_BACKGROUND_COLOR = 2
    const val TAG_SCROLL_IN = 3
    const val TAG_SCROLL_OUT = 4
    const val TAG_HORIZONTAL_SCROLL = 5
    const val TAG_REVERSE_SCROLL = 6
    const val TAG_CONTINUOUS_SCROLL = 7
    const val TAG_DROP_SHADOW = 8
    const val TAG_ANTI_ALIAS = 9
    const val TAG_KEY_TEXT = 10
    const val TAG_JUSTIFICATION = 11
    const val TAG_BACKGROUND_COLOR = 12
    const val TAG_DEFAULT_TEXT_BOX = 13
    const val TAG_FONT_NUMBER = 14
    const val TAG_FONT_FACE = 15
    const val TAG_FOREGROUND_COLOR = 16
    const val TAG_NAME = 17
    protected val tagNameMap = HashMap<Int, String>()

    init {
      addQuickTimeMediaTags(this.tagNameMap)
      tagNameMap[TAG_AUTO_SCALE] = "Auto Scale"
      tagNameMap[TAG_MOVIE_BACKGROUND_COLOR] = "Use Background Color"
      tagNameMap[TAG_SCROLL_IN] = "Scroll In"
      tagNameMap[TAG_SCROLL_OUT] = "Scroll Out"
      tagNameMap[TAG_HORIZONTAL_SCROLL] = "Scroll Orientation"
      tagNameMap[TAG_REVERSE_SCROLL] = "Scroll Direction"
      tagNameMap[TAG_CONTINUOUS_SCROLL] = "Continuous Scroll"
      tagNameMap[TAG_DROP_SHADOW] = "Drop Shadow"
      tagNameMap[TAG_ANTI_ALIAS] = "Anti-aliasing"
      tagNameMap[TAG_KEY_TEXT] = "Display Text Background Color"
      tagNameMap[TAG_JUSTIFICATION] = "Alignment"
      tagNameMap[TAG_BACKGROUND_COLOR] = "Background Color"
      tagNameMap[TAG_DEFAULT_TEXT_BOX] = "Default Text Box"
      tagNameMap[TAG_FONT_NUMBER] = "Font Number"
      tagNameMap[TAG_FONT_FACE] = "Font Face"
      tagNameMap[TAG_FOREGROUND_COLOR] = "Foreground Color"
      tagNameMap[TAG_NAME] = "Font Name"
    }
  }

  override val name: String
    get() = "QuickTime Text"

  init {
    setDescriptor(QuickTimeTextDescriptor(this))
  }
}
