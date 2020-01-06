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

import com.drew.metadata.Metadata
import java.io.File
import java.io.IOException
import java.util.*

class FileSystemMetadataReader {
  @Throws(IOException::class)
  fun read(file: File, metadata: Metadata) {
    if (!file.isFile) throw IOException("File object must reference a file")
    if (!file.exists()) throw IOException("File does not exist")
    if (!file.canRead()) throw IOException("File is not readable")
    var directory = metadata.getFirstDirectoryOfType(FileSystemDirectory::class.java)
    if (directory == null) {
      directory = FileSystemDirectory()
      metadata.addDirectory<FileSystemDirectory>(directory)
    }
    directory.setString(FileSystemDirectory.TAG_FILE_NAME, file.name)
    directory.setLong(FileSystemDirectory.TAG_FILE_SIZE, file.length())
    directory.setDate(FileSystemDirectory.TAG_FILE_MODIFIED_DATE, Date(file.lastModified()))
  }
}
