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

import java.io.EOFException
import java.io.IOException
import java.io.InputStream

/**
 *
 * @author Drew Noakes https://drewnoakes.com
 */
class StreamReader(private val _stream: InputStream) : SequentialReader() {
  override var position: Long = 0
    private set

  @Throws(IOException::class)
  override fun getByte(): Byte {
    val value = _stream.read()
    if (value == -1) throw EOFException("End of data reached.")
    position++
    return value.toByte()
  }

  @Throws(IOException::class)
  override fun getBytes(count: Int): ByteArray {
    val bytes = ByteArray(count)
    getBytes(bytes, 0, count)
    return bytes
  }

  @Throws(IOException::class)
  override fun getBytes(buffer: ByteArray, offset: Int, count: Int) {
    var totalBytesRead = 0
    while (totalBytesRead != count) {
      val bytesRead = _stream.read(buffer, offset + totalBytesRead, count - totalBytesRead)
      if (bytesRead == -1) throw EOFException("End of data reached.")
      totalBytesRead += bytesRead
      assert(totalBytesRead <= count)
    }
    position += totalBytesRead.toLong()
  }

  @Throws(IOException::class)
  override fun skip(n: Long) {
    require(n >= 0) { "n must be zero or greater." }
    val skippedCount = skipInternal(n)
    if (skippedCount != n) throw EOFException(String.format("Unable to skip. Requested %d bytes but skipped %d.", n, skippedCount))
  }

  @Throws(IOException::class)
  override fun trySkip(n: Long): Boolean {
    require(n >= 0) { "n must be zero or greater." }
    return skipInternal(n) == n
  }

  override fun available(): Int {
    return try {
      _stream.available()
    } catch (e: IOException) {
      0
    }
  }

  @Throws(IOException::class)
  private fun skipInternal(n: Long): Long { // It seems that for some streams, such as BufferedInputStream, that skip can return
// some smaller number than was requested. So loop until we either skip enough, or
// InputStream.skip returns zero.
//
// See http://stackoverflow.com/questions/14057720/robust-skipping-of-data-in-a-java-io-inputstream-and-its-subtypes
//
    var skippedTotal: Long = 0
    while (skippedTotal != n) {
      val skipped = _stream.skip(n - skippedTotal)
      skippedTotal += skipped
      if (skipped == 0L) break
    }
    position += skippedTotal
    return skippedTotal
  }
}
