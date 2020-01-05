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
package com.drew.metadata

import java.io.UnsupportedEncodingException
import java.nio.charset.Charset

/**
 * @author Drew Noakes https://drewnoakes.com
 */
class StringValue(val bytes: ByteArray, val charset: Charset?) {
  override fun toString(): String {
    return toString(charset)
  }

  fun toString(charset: Charset?): String {
    if (charset != null) {
      try {
        return String(bytes, charset)
      } catch (ex: UnsupportedEncodingException) {
        // fall through
      }
    }
    return String(bytes)
  }
}
