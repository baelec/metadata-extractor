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

import com.drew.metadata.TagDescriptor

/**
 * @author Drew Noakes https://drewnoakes.com
 * @author Kevin Mott https://github.com/kwhopper
 */
class GifAnimationDescriptor(directory: GifAnimationDirectory) : TagDescriptor<GifAnimationDirectory>(directory) {
  override fun getDescription(tagType: Int): String? {
    return when (tagType) {
      GifAnimationDirectory.TAG_ITERATION_COUNT -> iterationCountDescription
      else -> super.getDescription(tagType)
    }
  }

  val iterationCountDescription: String?
    get() {
      val count = _directory.getInteger(GifAnimationDirectory.TAG_ITERATION_COUNT) ?: return null
      return if (count == 0) "Infinite" else if (count == 1) "Once" else if (count == 2) "Twice" else "$count times"
    }
}
