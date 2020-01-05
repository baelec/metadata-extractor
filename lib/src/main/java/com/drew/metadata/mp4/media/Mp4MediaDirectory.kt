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
package com.drew.metadata.mp4.media

import com.drew.metadata.mp4.Mp4Directory
import java.util.*

abstract class Mp4MediaDirectory : Mp4Directory() {
  companion object {
    const val TAG_CREATION_TIME = 101
    const val TAG_MODIFICATION_TIME = 102
    const val TAG_DURATION = 103
    const val TAG_LANGUAGE_CODE = 104
    @JvmStatic
    protected fun addMp4MediaTags(map: HashMap<Int, String>) {
      map[TAG_CREATION_TIME] = "Creation Time"
      map[TAG_MODIFICATION_TIME] = "Modification Time"
      map[TAG_DURATION] = "Duration"
      map[TAG_LANGUAGE_CODE] = "ISO 639-2 Language Code"
    }
  }
}
