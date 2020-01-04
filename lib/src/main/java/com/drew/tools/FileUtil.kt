@file:JvmName("FileUtil")

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
package com.drew.tools

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException

/**
 * A series of utility methods for working with the file system. The methods herein are used in unit testing.
 * Use them in production code at your own risk!
 *
 * @author Drew Noakes https://drewnoakes.com
 */
/**
 * Saves the contents of a `byte[]` to the specified [File].
 */
@Throws(IOException::class)
fun saveBytes(file: File, bytes: ByteArray) {
  FileOutputStream(file).use {
    it.write(bytes)
  }
}

/**
 * Reads the contents of a [File] into a `byte[]`. This relies upon [File.length]
 * returning the correct value, which may not be the case when using a network file system. However this method is
 * intended for unit test support, in which case the files should be on the local volume.
 */
@Throws(IOException::class)
fun readBytes(file: File): ByteArray {
  val length = file.length().toInt()
  assert(length != 0)
  val bytes = ByteArray(length)
  var totalBytesRead = 0
  FileInputStream(file).use {
    while (totalBytesRead != length) {
      val bytesRead = it.read(bytes, totalBytesRead, length - totalBytesRead)
      if (bytesRead == -1) {
        break
      }
      totalBytesRead += bytesRead
    }
  }
  return bytes
}

/**
 * Reads the contents of a [File] into a `byte[]`. This relies upon `File.length()`
 * returning the correct value, which may not be the case when using a network file system. However this method is
 * intended for unit test support, in which case the files should be on the local volume.
 */
@Throws(IOException::class)
fun readBytes(filePath: String): ByteArray {
  return readBytes(File(filePath))
}
