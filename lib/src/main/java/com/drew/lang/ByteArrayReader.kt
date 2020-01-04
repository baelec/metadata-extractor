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

import com.drew.lang.annotations.SuppressWarnings
import java.io.IOException

/**
 * Provides methods to read specific values from a byte array, with a consistent, checked exception structure for
 * issues.
 *
 *
 * By default, the reader operates with Motorola byte order (big endianness).  This can be changed by calling
 * `setMotorolaByteOrder(boolean)`.
 *
 * @author Drew Noakes https://drewnoakes.com
 */
class ByteArrayReader @SuppressWarnings(value = "EI_EXPOSE_REP2", justification = "Design intent") constructor(buffer: ByteArray, baseOffset: Int) : RandomAccessReader() {
  private val _buffer: ByteArray
  private val _baseOffset: Int

  @SuppressWarnings(value = "EI_EXPOSE_REP2", justification = "Design intent")
  constructor(buffer: ByteArray) : this(buffer, 0) {
  }

  override fun toUnshiftedOffset(localOffset: Int): Int {
    return localOffset + _baseOffset
  }

  override val length
    get() = (_buffer.size - _baseOffset).toLong()

  @Throws(IOException::class)
  override fun getByte(index: Int): Byte {
    validateIndex(index, 1)
    return _buffer[index + _baseOffset]
  }

  @Throws(IOException::class)
  override fun validateIndex(index: Int, bytesRequested: Int) {
    if (!isValidIndex(index, bytesRequested)) throw BufferBoundsException(toUnshiftedOffset(index), bytesRequested, _buffer.size.toLong())
  }

  @Throws(IOException::class)
  override fun isValidIndex(index: Int, bytesRequested: Int): Boolean {
    return bytesRequested >= 0 && index >= 0 && index.toLong() + bytesRequested.toLong() - 1L < length
  }

  @Throws(IOException::class)
  override fun getBytes(index: Int, count: Int): ByteArray {
    validateIndex(index, count)
    val bytes = ByteArray(count)
    System.arraycopy(_buffer, index + _baseOffset, bytes, 0, count)
    return bytes
  }

  init {
    require(baseOffset >= 0) { "Must be zero or greater" }
    _buffer = buffer
    _baseOffset = baseOffset
  }
}
