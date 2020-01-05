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
package com.drew.metadata.iptc

import java.nio.ByteBuffer
import java.nio.charset.CharacterCodingException
import java.nio.charset.Charset

object Iso2022Converter {
  private const val ISO_8859_1 = "ISO-8859-1"
  private const val UTF_8 = "UTF-8"
  private const val LATIN_CAPITAL_A: Byte = 0x41
  private const val DOT = 0xe280a2
  private const val LATIN_CAPITAL_G: Byte = 0x47
  private const val PERCENT_SIGN: Byte = 0x25
  private const val DOT_SIGN: Byte = 0x2E
  private const val ESC: Byte = 0x1B
  /**
   * Converts the given ISO2022 char set to a Java charset name.
   * A reference of valid charsets can be found here: http://nozer0.github.io/en/technology/system/character-encoding/#ISO/IEC%202022
   *
   * @param bytes string data encoded using ISO2022
   * @return the Java charset name as a string, or `null` if the conversion was not possible
   */
  @JvmStatic
  fun convertISO2022CharsetToJavaCharset(bytes: ByteArray): String? {
    if (bytes.size > 2 && bytes[0] == ESC && bytes[1] == PERCENT_SIGN && bytes[2] == LATIN_CAPITAL_G) return UTF_8
    if (bytes.size > 2 && bytes[0] == ESC && bytes[1] == DOT_SIGN && bytes[2] == LATIN_CAPITAL_A) return ISO_8859_1
    return if (bytes.size > 3 && bytes[0] == ESC && bytes[3].toInt() and 0xFF or (bytes[2].toInt() and 0xFF shl 8) or (bytes[1].toInt() and 0xFF shl 16) == DOT && bytes[4] == LATIN_CAPITAL_A) ISO_8859_1 else null
  }

  /**
   * Attempts to guess the [Charset] of a string provided as a byte array.
   *
   *
   * Charsets trialled are, in order:
   *
   *  * UTF-8
   *  * `System.getProperty("file.encoding")`
   *  * ISO-8859-1
   *
   *
   *
   * Its only purpose is to guess the Charset if and only if IPTC tag coded character set is not set. If the
   * encoding is not UTF-8, the tag should be set. Otherwise it is bad practice. This method tries to
   * workaround this issue since some metadata manipulating tools do not prevent such bad practice.
   *
   *
   * About the reliability of this method: The check if some bytes are UTF-8 or not has a very high reliability.
   * The two other checks are less reliable.
   *
   * @param bytes some text as bytes
   * @return the name of the encoding or null if none could be guessed
   */
  fun guessCharSet(bytes: ByteArray): Charset? {
    val encodings = arrayOf(UTF_8, System.getProperty("file.encoding"), ISO_8859_1)
    for (encoding in encodings) {
      val charset = Charset.forName(encoding)
      val cs = charset.newDecoder()
      try {
        cs.decode(ByteBuffer.wrap(bytes))
        return charset
      } catch (e: CharacterCodingException) {
        // fall through...
      }
    }
    // No encodings succeeded. Return null.
    return null
  }
}
