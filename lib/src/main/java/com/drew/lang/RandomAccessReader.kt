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

import com.drew.metadata.StringValue
import java.io.IOException
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset

/**
 * Base class for random access data reading operations of common data types.
 *
 *
 * By default, the reader operates with Motorola byte order (big endianness).  This can be changed by calling
 * [com.drew.lang.RandomAccessReader.setMotorolaByteOrder].
 *
 *
 * Concrete implementations include:
 *
 *  * [ByteArrayReader]
 *  * [RandomAccessStreamReader]
 *
 *
 * @author Drew Noakes https://drewnoakes.com
 */
abstract class RandomAccessReader {
  /**
   * Gets the endianness of this reader.
   *
   *  * `true` for Motorola (or big) endianness (also known as network byte order), with MSB before LSB.
   *  * `false` for Intel (or little) endianness, with LSB before MSB.
   *
   */
  /**
   * Sets the endianness of this reader.
   *
   *  * `true` for Motorola (or big) endianness (also known as network byte order), with MSB before LSB.
   *  * `false` for Intel (or little) endianness, with LSB before MSB.
   *
   *
   * @param motorolaByteOrder `true` for Motorola/big endian, `false` for Intel/little endian
   */
  var isMotorolaByteOrder = true

  abstract fun toUnshiftedOffset(localOffset: Int): Int
  /**
   * Gets the byte value at the specified byte `index`.
   *
   *
   * Implementations should not perform any bounds checking in this method. That should be performed
   * in `validateIndex` and `isValidIndex`.
   *
   * @param index The index from which to read the byte
   * @return The read byte value
   * @throws IllegalArgumentException `index` is negative
   * @throws BufferBoundsException if the requested byte is beyond the end of the underlying data source
   * @throws IOException if the byte is unable to be read
   */
  @Throws(IOException::class)
  abstract fun getByte(index: Int): Byte

  /**
   * Returns the required number of bytes from the specified index from the underlying source.
   *
   * @param index The index from which the bytes begins in the underlying source
   * @param count The number of bytes to be returned
   * @return The requested bytes
   * @throws IllegalArgumentException `index` or `count` are negative
   * @throws BufferBoundsException if the requested bytes extend beyond the end of the underlying data source
   * @throws IOException if the byte is unable to be read
   */
  @Throws(IOException::class)
  abstract fun getBytes(index: Int, count: Int): ByteArray

  /**
   * Ensures that the buffered bytes extend to cover the specified index. If not, an attempt is made
   * to read to that point.
   *
   *
   * If the stream ends before the point is reached, a [BufferBoundsException] is raised.
   *
   * @param index the index from which the required bytes start
   * @param bytesRequested the number of bytes which are required
   * @throws IOException if the stream ends before the required number of bytes are acquired
   */
  @Throws(IOException::class)
  protected abstract fun validateIndex(index: Int, bytesRequested: Int)

  @Throws(IOException::class)
  protected abstract fun isValidIndex(index: Int, bytesRequested: Int): Boolean

  /**
   * Returns the length of the data source in bytes.
   *
   *
   * This is a simple operation for implementations (such as [RandomAccessFileReader] and
   * [ByteArrayReader]) that have the entire data source available.
   *
   *
   * Users of this method must be aware that sequentially accessed implementations such as
   * [RandomAccessStreamReader] will have to read and buffer the entire data source in
   * order to determine the length.
   *
   * @return the length of the data source, in bytes.
   */
  @get:Throws(IOException::class)
  abstract val length: Long

  /**
   * Gets whether a bit at a specific index is set or not.
   *
   * @param index the number of bits at which to test
   * @return true if the bit is set, otherwise false
   * @throws IOException the buffer does not contain enough bytes to service the request, or index is negative
   */
  @Throws(IOException::class)
  fun getBit(index: Int): Boolean {
    val byteIndex = index / 8
    val bitIndex = index % 8
    validateIndex(byteIndex, 1)
    val b = getByte(byteIndex)
    return b.toInt() shr bitIndex and 1 == 1
  }

  /**
   * Returns an unsigned 8-bit int calculated from one byte of data at the specified index.
   *
   * @param index position within the data buffer to read byte
   * @return the 8 bit int value, between 0 and 255
   * @throws IOException the buffer does not contain enough bytes to service the request, or index is negative
   */
  @Throws(IOException::class)
  fun getUInt8(index: Int): Short {
    validateIndex(index, 1)
    return (getByte(index).toInt() and 0xFF).toShort()
  }

  /**
   * Returns a signed 8-bit int calculated from one byte of data at the specified index.
   *
   * @param index position within the data buffer to read byte
   * @return the 8 bit int value, between 0x00 and 0xFF
   * @throws IOException the buffer does not contain enough bytes to service the request, or index is negative
   */
  @Throws(IOException::class)
  fun getInt8(index: Int): Byte {
    validateIndex(index, 1)
    return getByte(index)
  }

  /**
   * Returns an unsigned 16-bit int calculated from two bytes of data at the specified index.
   *
   * @param index position within the data buffer to read first byte
   * @return the 16 bit int value, between 0x0000 and 0xFFFF
   * @throws IOException the buffer does not contain enough bytes to service the request, or index is negative
   */
  @Throws(IOException::class)
  fun getUInt16(index: Int): Int {
    validateIndex(index, 2)
    return if (isMotorolaByteOrder) { // Motorola - MSB first
      getByte(index).toInt() shl 8 and 0xFF00 or
        (getByte(index + 1).toInt() and 0xFF)
    } else { // Intel ordering - LSB first
      getByte(index + 1) .toInt()shl 8 and 0xFF00 or
        (getByte(index).toInt() and 0xFF)
    }
  }

  /**
   * Returns a signed 16-bit int calculated from two bytes of data at the specified index (MSB, LSB).
   *
   * @param index position within the data buffer to read first byte
   * @return the 16 bit int value, between 0x0000 and 0xFFFF
   * @throws IOException the buffer does not contain enough bytes to service the request, or index is negative
   */
  @Throws(IOException::class)
  fun getInt16(index: Int): Short {
    validateIndex(index, 2)
    return (if (isMotorolaByteOrder) { // Motorola - MSB first
      (getByte(index).toInt() shl 8 and 0xFF00 or
        (getByte(index + 1).toInt() and 0xFF))
    } else { // Intel ordering - LSB first
      (getByte(index + 1).toInt() shl 8 and 0xFF00 or
        (getByte(index).toInt() and 0xFF))
    }).toShort()
  }

  /**
   * Get a 24-bit unsigned integer from the buffer, returning it as an int.
   *
   * @param index position within the data buffer to read first byte
   * @return the unsigned 24-bit int value as a long, between 0x00000000 and 0x00FFFFFF
   * @throws IOException the buffer does not contain enough bytes to service the request, or index is negative
   */
  @Throws(IOException::class)
  fun getInt24(index: Int): Int {
    validateIndex(index, 3)
    return if (isMotorolaByteOrder) { // Motorola - MSB first (big endian)
      getByte(index).toInt() shl 16 and 0xFF0000 or
        (getByte(index + 1).toInt() shl 8 and 0xFF00) or
        (getByte(index + 2).toInt() and 0xFF)
    } else { // Intel ordering - LSB first (little endian)
      getByte(index + 2).toInt() shl 16 and 0xFF0000 or
        (getByte(index + 1).toInt() shl 8 and 0xFF00) or
        (getByte(index).toInt() and 0xFF)
    }
  }

  /**
   * Get a 32-bit unsigned integer from the buffer, returning it as a long.
   *
   * @param index position within the data buffer to read first byte
   * @return the unsigned 32-bit int value as a long, between 0x00000000 and 0xFFFFFFFF
   * @throws IOException the buffer does not contain enough bytes to service the request, or index is negative
   */
  @Throws(IOException::class)
  fun getUInt32(index: Int): Long {
    validateIndex(index, 4)
    return if (isMotorolaByteOrder) { // Motorola - MSB first (big endian)
      getByte(index).toLong() shl 24 and 0xFF000000L or
        (getByte(index + 1).toLong() shl 16 and 0xFF0000L) or
        (getByte(index + 2).toLong() shl 8 and 0xFF00L) or
        (getByte(index + 3).toLong() and 0xFFL)
    } else { // Intel ordering - LSB first (little endian)
      getByte(index + 3).toLong() shl 24 and 0xFF000000L or
        (getByte(index + 2).toLong() shl 16 and 0xFF0000L) or
        (getByte(index + 1).toLong() shl 8 and 0xFF00L) or
        (getByte(index).toLong() and 0xFFL)
    }
  }

  /**
   * Returns a signed 32-bit integer from four bytes of data at the specified index the buffer.
   *
   * @param index position within the data buffer to read first byte
   * @return the signed 32 bit int value, between 0x00000000 and 0xFFFFFFFF
   * @throws IOException the buffer does not contain enough bytes to service the request, or index is negative
   */
  @Throws(IOException::class)
  fun getInt32(index: Int): Int {
    validateIndex(index, 4)
    return if (isMotorolaByteOrder) { // Motorola - MSB first (big endian)
      getByte(index).toInt() shl 24 and -0x1000000 or
        (getByte(index + 1).toInt() shl 16 and 0xFF0000) or
        (getByte(index + 2).toInt() shl 8 and 0xFF00) or
        (getByte(index + 3).toInt() and 0xFF)
    } else { // Intel ordering - LSB first (little endian)
      getByte(index + 3).toInt() shl 24 and -0x1000000 or
        (getByte(index + 2).toInt() shl 16 and 0xFF0000) or
        (getByte(index + 1).toInt() shl 8 and 0xFF00) or
        (getByte(index).toInt() and 0xFF)
    }
  }

  /**
   * Get a signed 64-bit integer from the buffer.
   *
   * @param index position within the data buffer to read first byte
   * @return the 64 bit int value, between 0x0000000000000000 and 0xFFFFFFFFFFFFFFFF
   * @throws IOException the buffer does not contain enough bytes to service the request, or index is negative
   */
  @Throws(IOException::class)
  fun getInt64(index: Int): Long {
    validateIndex(index, 8)
    return if (isMotorolaByteOrder) { // Motorola - MSB first
      getByte(index).toLong() shl 56 and -0x100000000000000L or
        (getByte(index + 1).toLong() shl 48 and 0xFF000000000000L) or
        (getByte(index + 2).toLong() shl 40 and 0xFF0000000000L) or
        (getByte(index + 3).toLong() shl 32 and 0xFF00000000L) or
        (getByte(index + 4).toLong() shl 24 and 0xFF000000L) or
        (getByte(index + 5).toLong() shl 16 and 0xFF0000L) or
        (getByte(index + 6).toLong() shl 8 and 0xFF00L) or
        (getByte(index + 7).toLong() and 0xFFL)
    } else { // Intel ordering - LSB first
      getByte(index + 7).toLong() shl 56 and -0x100000000000000L or
        (getByte(index + 6).toLong() shl 48 and 0xFF000000000000L) or
        (getByte(index + 5).toLong() shl 40 and 0xFF0000000000L) or
        (getByte(index + 4).toLong() shl 32 and 0xFF00000000L) or
        (getByte(index + 3).toLong() shl 24 and 0xFF000000L) or
        (getByte(index + 2).toLong() shl 16 and 0xFF0000L) or
        (getByte(index + 1).toLong() shl 8 and 0xFF00L) or
        (getByte(index).toLong() and 0xFFL)
    }
  }

  /**
   * Gets a s15.16 fixed point float from the buffer.
   *
   *
   * This particular fixed point encoding has one sign bit, 15 numerator bits and 16 denominator bits.
   *
   * @return the floating point value
   * @throws IOException the buffer does not contain enough bytes to service the request, or index is negative
   */
  @Throws(IOException::class)
  fun getS15Fixed16(index: Int): Float {
    validateIndex(index, 4)
    return if (isMotorolaByteOrder) {
      val res: Float = (getByte(index).toInt() and 0xFF shl 8 or
        (getByte(index + 1).toInt() and 0xFF)).toFloat()
      val d: Int = getByte(index + 2).toInt() and 0xFF shl 8 or
        (getByte(index + 3).toInt() and 0xFF)
      (res + d / 65536.0).toFloat()
    } else { // this particular branch is untested
      val res: Float = (getByte(index + 3).toInt() and 0xFF shl 8 or
        (getByte(index + 2).toInt() and 0xFF)).toFloat()
      val d: Int = getByte(index + 1).toInt() and 0xFF shl 8 or
        (getByte(index).toInt() and 0xFF)
      (res + d / 65536.0).toFloat()
    }
  }

  @Throws(IOException::class)
  fun getFloat32(index: Int): Float {
    return java.lang.Float.intBitsToFloat(getInt32(index))
  }

  @Throws(IOException::class)
  fun getDouble64(index: Int): Double {
    return java.lang.Double.longBitsToDouble(getInt64(index))
  }

  @Throws(IOException::class)
  fun getStringValue(index: Int, bytesRequested: Int, charset: Charset?): StringValue {
    return StringValue(getBytes(index, bytesRequested), charset)
  }

  @Throws(IOException::class)
  fun getString(index: Int, bytesRequested: Int, charset: Charset): String {
    return String(getBytes(index, bytesRequested), charset)
  }

  @Throws(IOException::class)
  fun getString(index: Int, bytesRequested: Int, charset: String): String {
    val bytes = getBytes(index, bytesRequested)
    return try {
      String(bytes, Charset.forName(charset))
    } catch (e: UnsupportedEncodingException) {
      String(bytes)
    }
  }

  /**
   * Creates a String from the _data buffer starting at the specified index,
   * and ending where `byte=='\0'` or where `length==maxLength`.
   *
   * @param index          The index within the buffer at which to start reading the string.
   * @param maxLengthBytes The maximum number of bytes to read.  If a zero-byte is not reached within this limit,
   * reading will stop and the string will be truncated to this length.
   * @return The read string.
   * @throws IOException The buffer does not contain enough bytes to satisfy this request.
   */
  @Throws(IOException::class)
  fun getNullTerminatedString(index: Int, maxLengthBytes: Int, charset: Charset): String {
    return String(getNullTerminatedBytes(index, maxLengthBytes), charset)
  }

  @Throws(IOException::class)
  fun getNullTerminatedStringValue(index: Int, maxLengthBytes: Int, charset: Charset?): StringValue {
    val bytes = getNullTerminatedBytes(index, maxLengthBytes)
    return StringValue(bytes, charset)
  }

  /**
   * Returns the sequence of bytes punctuated by a `\0` value.
   *
   * @param index The index within the buffer at which to start reading the string.
   * @param maxLengthBytes The maximum number of bytes to read. If a `\0` byte is not reached within this limit,
   * the returned array will be `maxLengthBytes` long.
   * @return The read byte array, excluding the null terminator.
   * @throws IOException The buffer does not contain enough bytes to satisfy this request.
   */
  @Throws(IOException::class)
  fun getNullTerminatedBytes(index: Int, maxLengthBytes: Int): ByteArray {
    val buffer = getBytes(index, maxLengthBytes)
    // Count the number of non-null bytes
    var length = 0
    while (length < buffer.size && buffer[length] != 0.toByte()) {
      length++
    }
    if (length == maxLengthBytes) return buffer
    val bytes = ByteArray(length)
    if (length > 0) System.arraycopy(buffer, 0, bytes, 0, length)
    return bytes
  }
}
