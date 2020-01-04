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

import java.io.IOException
import java.io.InputStream
import java.util.*

/**
 * @author Drew Noakes https://drewnoakes.com
 */
class RandomAccessStreamReader @JvmOverloads constructor(stream: InputStream, chunkLength: Int = DEFAULT_CHUNK_LENGTH, streamLength: Long = -1) : RandomAccessReader() {
  private val _stream: InputStream
  private val _chunkLength: Int
  private val _chunks = ArrayList<ByteArray>()
  private var _isStreamFinished = false
  private var _streamLength: Long
  /**
   * Reads to the end of the stream, in order to determine the total number of bytes.
   * In general, this is not a good idea for this implementation of [RandomAccessReader].
   *
   * @return the length of the data source, in bytes.
   */
  @get:Throws(IOException::class)
  override val length: Long
    get() {
      if (_streamLength != -1L) {
        return _streamLength
      }
      isValidIndex(Int.MAX_VALUE, 1)
      assert(_isStreamFinished)
      return _streamLength
    }

  /**
   * Ensures that the buffered bytes extend to cover the specified index. If not, an attempt is made
   * to read to that point.
   *
   *
   * If the stream ends before the point is reached, a [BufferBoundsException] is raised.
   *
   * @param index the index from which the required bytes start
   * @param bytesRequested the number of bytes which are required
   * @throws BufferBoundsException if the stream ends before the required number of bytes are acquired
   */
  @Throws(IOException::class)
  override fun validateIndex(index: Int, bytesRequested: Int) {
    if (index < 0) {
      throw BufferBoundsException(String.format("Attempt to read from buffer using a negative index (%d)", index))
    } else if (bytesRequested < 0) {
      throw BufferBoundsException("Number of requested bytes must be zero or greater")
    } else if (index.toLong() + bytesRequested - 1 > Int.MAX_VALUE) {
      throw BufferBoundsException(String.format("Number of requested bytes summed with starting index exceed maximum range of signed 32 bit integers (requested index: %d, requested count: %d)", index, bytesRequested))
    }
    if (!isValidIndex(index, bytesRequested)) {
      assert(_isStreamFinished)
      throw BufferBoundsException(index, bytesRequested, _streamLength)
    }
  }

  @Throws(IOException::class)
  override fun isValidIndex(index: Int, bytesRequested: Int): Boolean {
    if (index < 0 || bytesRequested < 0) {
      return false
    }
    val endIndexLong = index.toLong() + bytesRequested - 1
    if (endIndexLong > Int.MAX_VALUE) {
      return false
    }
    val endIndex = endIndexLong.toInt()
    if (_isStreamFinished) {
      return endIndex < _streamLength
    }
    val chunkIndex = endIndex / _chunkLength
    // TODO test loading several chunks for a single request
    while (chunkIndex >= _chunks.size) {
      assert(!_isStreamFinished)
      val chunk = ByteArray(_chunkLength)
      var totalBytesRead = 0
      while (!_isStreamFinished && totalBytesRead != _chunkLength) {
        val bytesRead = _stream.read(chunk, totalBytesRead, _chunkLength - totalBytesRead)
        if (bytesRead == -1) { // the stream has ended, which may be ok
          _isStreamFinished = true
          val observedStreamLength = _chunks.size * _chunkLength + totalBytesRead
          if (_streamLength == -1L) {
            _streamLength = observedStreamLength.toLong()
          } else if (_streamLength != observedStreamLength.toLong()) {
            assert(false)
          }
          // check we have enough bytes for the requested index
          if (endIndex >= _streamLength) {
            _chunks.add(chunk)
            return false
          }
        } else {
          totalBytesRead += bytesRead
        }
      }
      _chunks.add(chunk)
    }
    return true
  }

  override fun toUnshiftedOffset(localOffset: Int): Int {
    return localOffset
  }

  @Throws(IOException::class)
  override fun getByte(index: Int): Byte {
    assert(index >= 0)
    val chunkIndex = index / _chunkLength
    val innerIndex = index % _chunkLength
    val chunk = _chunks[chunkIndex]
    return chunk[innerIndex]
  }

  @Throws(IOException::class)
  override fun getBytes(index: Int, count: Int): ByteArray {
    validateIndex(index, count)
    val bytes = ByteArray(count)
    var remaining = count
    var fromIndex = index
    var toIndex = 0
    while (remaining != 0) {
      val fromChunkIndex = fromIndex / _chunkLength
      val fromInnerIndex = fromIndex % _chunkLength
      val length = Math.min(remaining, _chunkLength - fromInnerIndex)
      val chunk = _chunks[fromChunkIndex]
      System.arraycopy(chunk, fromInnerIndex, bytes, toIndex, length)
      remaining -= length
      fromIndex += length
      toIndex += length
    }
    return bytes
  }

  companion object {
    const val DEFAULT_CHUNK_LENGTH = 2 * 1024
  }

  init {
    if (stream == null) throw NullPointerException()
    require(chunkLength > 0) { "chunkLength must be greater than zero" }
    _chunkLength = chunkLength
    _stream = stream
    _streamLength = streamLength
  }
}
