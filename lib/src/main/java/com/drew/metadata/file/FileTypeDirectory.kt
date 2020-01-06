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

import com.drew.imaging.FileType
import com.drew.metadata.Directory
import java.util.*

/**
 * @author Payton Garland https://github.com/PaytonGarland
 */
class FileTypeDirectory(fileType: FileType) : Directory() {
  override val tagNameMap = Companion.tagNameMap

  companion object {
    const val TAG_DETECTED_FILE_TYPE_NAME = 1
    const val TAG_DETECTED_FILE_TYPE_LONG_NAME = 2
    const val TAG_DETECTED_FILE_MIME_TYPE = 3
    const val TAG_EXPECTED_FILE_NAME_EXTENSION = 4
    protected val tagNameMap = HashMap<Int, String>()

    init {
      tagNameMap[TAG_DETECTED_FILE_TYPE_NAME] = "Detected File Type Name"
      tagNameMap[TAG_DETECTED_FILE_TYPE_LONG_NAME] = "Detected File Type Long Name"
      tagNameMap[TAG_DETECTED_FILE_MIME_TYPE] = "Detected MIME Type"
      tagNameMap[TAG_EXPECTED_FILE_NAME_EXTENSION] = "Expected File Name Extension"
    }
  }

  override val name: String
    get() = "File Type"

  init {
    setDescriptor(FileTypeDescriptor(this))
    setString(TAG_DETECTED_FILE_TYPE_NAME, fileType.getName())
    setString(TAG_DETECTED_FILE_TYPE_LONG_NAME, fileType.longName)
    if (fileType.mimeType != null) setString(TAG_DETECTED_FILE_MIME_TYPE, fileType.mimeType!!)
    if (fileType.commonExtension != null) setString(TAG_EXPECTED_FILE_NAME_EXTENSION, fileType.commonExtension!!)
  }
}
