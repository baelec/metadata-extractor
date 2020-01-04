@file:JvmName("StringUtil")

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

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

/**
 * @author Drew Noakes https://drewnoakes.com
 */
fun join(strings: Iterable<CharSequence>, delimiter: String): String {
  var capacity = 0
  val delimLength = delimiter.length
  var iter = strings.iterator()
  if (iter.hasNext()) capacity += iter.next().length + delimLength
  val buffer = StringBuilder(capacity)
  iter = strings.iterator()
  if (iter.hasNext()) {
    buffer.append(iter.next())
    while (iter.hasNext()) {
      buffer.append(delimiter)
      buffer.append(iter.next())
    }
  }
  return buffer.toString()
}

fun <T : CharSequence?> join(strings: Array<T>, delimiter: String): String {
  var capacity = 0
  val delimLength = delimiter.length
  for (value in strings) capacity += value!!.length + delimLength
  val buffer = StringBuilder(capacity)
  var first = true
  for (value in strings) {
    if (!first) {
      buffer.append(delimiter)
    } else {
      first = false
    }
    buffer.append(value)
  }
  return buffer.toString()
}

@Throws(IOException::class)
fun fromStream(stream: InputStream): String {
  val reader = BufferedReader(InputStreamReader(stream))
  val sb = StringBuilder()
  var line: String?
  while (reader.readLine().also { line = it } != null) {
    sb.append(line)
  }
  return sb.toString()
}

fun compare(s1: String?, s2: String?): Int {
  val null1 = s1 == null
  val null2 = s2 == null
  return if (null1 && null2) {
    0
  } else if (null1) {
    -1
  } else if (null2) {
    1
  } else {
    s1!!.compareTo(s2!!)
  }
}

fun urlEncode(name: String): String { // Sufficient for now, it seems
  return name.replace(" ", "%20")
}
