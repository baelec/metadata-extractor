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
package com.drew.metadata.gif

import com.drew.metadata.Directory
import java.util.*

/**
 * @author Drew Noakes https://drewnoakes.com
 * @author Kevin Mott https://github.com/kwhopper
 */
class GifControlDirectory : Directory() {
  override val tagNameMap = Companion.tagNameMap

  companion object {
    const val TAG_DELAY = 1
    const val TAG_DISPOSAL_METHOD = 2
    const val TAG_USER_INPUT_FLAG = 3
    const val TAG_TRANSPARENT_COLOR_FLAG = 4
    const val TAG_TRANSPARENT_COLOR_INDEX = 5
    protected val tagNameMap = HashMap<Int, String>()

    init {
      tagNameMap[TAG_DELAY] = "Delay"
      tagNameMap[TAG_DISPOSAL_METHOD] = "Disposal Method"
      tagNameMap[TAG_USER_INPUT_FLAG] = "User Input Flag"
      tagNameMap[TAG_TRANSPARENT_COLOR_FLAG] = "Transparent Color Flag"
      tagNameMap[TAG_TRANSPARENT_COLOR_INDEX] = "Transparent Color Index"
    }
  }

  override val name: String
    get() = "GIF Control"

  /**
   * @return The [DisposalMethod].
   */
  val disposalMethod: DisposalMethod
    get() = getObject(TAG_DISPOSAL_METHOD) as DisposalMethod

  /**
   * @return Whether the GIF has transparency.
   */
  val isTransparent: Boolean
    get() {
      val transparent = getBooleanObject(TAG_TRANSPARENT_COLOR_FLAG)
      return transparent != null && transparent
    }

  /**
   * Disposal method indicates the way in which the graphic is to be treated
   * after being displayed.
   */
  enum class DisposalMethod {
    NOT_SPECIFIED, DO_NOT_DISPOSE, RESTORE_TO_BACKGROUND_COLOR, RESTORE_TO_PREVIOUS, TO_BE_DEFINED, INVALID;

    override fun toString(): String {
      return when (this) {
        DO_NOT_DISPOSE -> "Don't Dispose"
        INVALID -> "Invalid value"
        NOT_SPECIFIED -> "Not Specified"
        RESTORE_TO_BACKGROUND_COLOR -> "Restore to Background Color"
        RESTORE_TO_PREVIOUS -> "Restore to Previous"
        TO_BE_DEFINED -> "To Be Defined"
      }
    }

    companion object {
      @JvmStatic
      fun typeOf(value: Int): DisposalMethod {
        return when (value) {
          0 -> NOT_SPECIFIED
          1 -> DO_NOT_DISPOSE
          2 -> RESTORE_TO_BACKGROUND_COLOR
          3 -> RESTORE_TO_PREVIOUS
          4, 5, 6, 7 -> TO_BE_DEFINED
          else -> INVALID
        }
      }
    }
  }

  init {
    setDescriptor(GifControlDescriptor(this))
  }
}
