@file:JvmName("StreamUtil")

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
package com.drew.lang

import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream

/**
 * @author Drew Noakes https://drewnoakes.com
 */
@Throws(IOException::class)
fun readAllBytes(stream: InputStream): ByteArray {
  val outputStream = ByteArrayOutputStream()
  val buffer = ByteArray(1024)
  while (true) {
    val bytesRead = stream.read(buffer)
    if (bytesRead == -1) {
      break
    }
    outputStream.write(buffer, 0, bytesRead)
  }
  return outputStream.toByteArray()
}