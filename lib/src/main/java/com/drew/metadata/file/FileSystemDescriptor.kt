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

import com.drew.metadata.TagDescriptor

/**
 * @author Drew Noakes https://drewnoakes.com
 */
class FileSystemDescriptor(directory: FileSystemDirectory) : TagDescriptor<FileSystemDirectory>(directory) {
  override fun getDescription(tagType: Int): String? {
    return when (tagType) {
      FileSystemDirectory.TAG_FILE_SIZE -> fileSizeDescription
      else -> super.getDescription(tagType)
    }
  }

  private val fileSizeDescription: String?
    get() {
      val size = _directory.getLongObject(FileSystemDirectory.TAG_FILE_SIZE) ?: return null
      return "$size bytes"
    }
}
