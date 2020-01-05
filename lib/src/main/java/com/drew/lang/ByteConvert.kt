@file:JvmName("ByteConvert")

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

/**
 * @author Drew Noakes http://drewnoakes.com
 */
fun toInt32BigEndian(bytes: ByteArray): Int {
  return bytes[0].toInt() shl 24 and -0x1000000 or
    (bytes[1].toInt() shl 16 and 0xFF0000) or
    (bytes[2].toInt() shl 8 and 0xFF00) or
    (bytes[3].toInt() and 0xFF)
}

fun toInt32LittleEndian(bytes: ByteArray): Int {
  return bytes[0].toInt() and 0xFF or
    (bytes[1].toInt() shl 8 and 0xFF00) or
    (bytes[2].toInt() shl 16 and 0xFF0000) or
    (bytes[3].toInt() shl 24 and -0x1000000)
}
