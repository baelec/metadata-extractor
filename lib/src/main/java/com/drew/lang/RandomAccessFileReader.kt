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
import java.io.RandomAccessFile

/**
 * Provides methods to read specific values from a [RandomAccessFile], with a consistent, checked exception structure for
 * issues.
 *
 * @author Drew Noakes https://drewnoakes.com
 */
class RandomAccessFileReader @SuppressWarnings(value = "EI_EXPOSE_REP2", justification = "Design intent") constructor(file: RandomAccessFile, baseOffset: Int) : RandomAccessReader() {
  private val _file: RandomAccessFile
  override val length: Long
  private var _currentIndex = 0
  private val _baseOffset: Int

  @SuppressWarnings(value = "EI_EXPOSE_REP2", justification = "Design intent")
  constructor(file: RandomAccessFile) : this(file, 0) {
  }

  override fun toUnshiftedOffset(localOffset: Int): Int {
    return localOffset + _baseOffset
  }

  @Throws(IOException::class)
  override fun getByte(index: Int): Byte {
    if (index != _currentIndex) seek(index)
    val b = _file.read()
    if (b < 0) throw BufferBoundsException("Unexpected end of file encountered.")
    assert(b <= 0xff)
    _currentIndex++
    return b.toByte()
  }

  @Throws(IOException::class)
  override fun getBytes(index: Int, count: Int): ByteArray {
    validateIndex(index, count)
    if (index != _currentIndex) seek(index)
    val bytes = ByteArray(count)
    val bytesRead = _file.read(bytes)
    _currentIndex += bytesRead
    if (bytesRead != count) throw BufferBoundsException("Unexpected end of file encountered.")
    return bytes
  }

  @Throws(IOException::class)
  private fun seek(index: Int) {
    if (index == _currentIndex) return
    _file.seek(index.toLong())
    _currentIndex = index
  }

  @Throws(IOException::class)
  override fun isValidIndex(index: Int, bytesRequested: Int): Boolean {
    return bytesRequested >= 0 && index >= 0 && index.toLong() + bytesRequested.toLong() - 1L < length
  }

  @Throws(IOException::class)
  override fun validateIndex(index: Int, bytesRequested: Int) {
    if (!isValidIndex(index, bytesRequested)) throw BufferBoundsException(index, bytesRequested, length)
  }

  init {
    if (file == null) throw NullPointerException()
    _file = file
    _baseOffset = baseOffset
    length = _file.length()
  }
}
