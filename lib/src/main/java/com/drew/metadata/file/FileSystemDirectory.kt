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
package com.drew.metadata.file

import com.drew.metadata.Directory
import java.util.*

/**
 * @author Drew Noakes https://drewnoakes.com
 */
class FileSystemDirectory : Directory() {
  override val tagNameMap = Companion.tagNameMap

  companion object {
    const val TAG_FILE_NAME = 1
    const val TAG_FILE_SIZE = 2
    const val TAG_FILE_MODIFIED_DATE = 3
    protected val tagNameMap = HashMap<Int, String>()

    init {
      tagNameMap[TAG_FILE_NAME] = "File Name"
      tagNameMap[TAG_FILE_SIZE] = "File Size"
      tagNameMap[TAG_FILE_MODIFIED_DATE] = "File Modified Date"
    }
  }

  override val name: String
    get() = "File"

  init {
    setDescriptor(FileSystemDescriptor(this))
  }
}
