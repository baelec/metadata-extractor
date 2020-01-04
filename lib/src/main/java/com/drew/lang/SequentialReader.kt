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
 * @author Drew Noakes https://drewnoakes.com
 */
abstract class SequentialReader {
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
  // TODO review whether the masks are needed (in both this and RandomAccessReader)
  var isMotorolaByteOrder = true

  @get:Throws(IOException::class)
  abstract val position: Long

  /**
   * Gets the next byte in the sequence.
   *
   * @return The read byte value
   */
  @Throws(IOException::class)
  abstract fun getByte(): Byte

  /**
   * Returns the required number of bytes from the sequence.
   *
   * @param count The number of bytes to be returned
   * @return The requested bytes
   */
  @Throws(IOException::class)
  abstract fun getBytes(count: Int): ByteArray

  /**
   * Retrieves bytes, writing them into a caller-provided buffer.
   * @param buffer The array to write bytes to.
   * @param offset The starting position within buffer to write to.
   * @param count The number of bytes to be written.
   */
  @Throws(IOException::class)
  abstract fun getBytes(buffer: ByteArray, offset: Int, count: Int)

  /**
   * Skips forward in the sequence. If the sequence ends, an [EOFException] is thrown.
   *
   * @param n the number of byte to skip. Must be zero or greater.
   * @throws EOFException the end of the sequence is reached.
   * @throws IOException an error occurred reading from the underlying source.
   */
  @Throws(IOException::class)
  abstract fun skip(n: Long)

  /**
   * Skips forward in the sequence, returning a boolean indicating whether the skip succeeded, or whether the sequence ended.
   *
   * @param n the number of byte to skip. Must be zero or greater.
   * @return a boolean indicating whether the skip succeeded, or whether the sequence ended.
   * @throws IOException an error occurred reading from the underlying source.
   */
  @Throws(IOException::class)
  abstract fun trySkip(n: Long): Boolean

  /**
   * Returns an estimate of the number of bytes that can be read (or skipped
   * over) from this [SequentialReader] without blocking by the next
   * invocation of a method for this input stream. A single read or skip of
   * this many bytes will not block, but may read or skip fewer bytes.
   *
   *
   * Note that while some implementations of [SequentialReader] like
   * [SequentialByteArrayReader] will return the total remaining number
   * of bytes in the stream, others will not. It is never correct to use the
   * return value of this method to allocate a buffer intended to hold all
   * data in this stream.
   *
   * @return an estimate of the number of bytes that can be read (or skipped
   * over) from this [SequentialReader] without blocking or
   * `0` when it reaches the end of the input stream.
   */
  abstract fun available(): Int

  /**
   * Returns an unsigned 8-bit int calculated from the next byte of the sequence.
   *
   * @return the 8 bit int value, between 0 and 255
   */
  @Throws(IOException::class)
  fun getUInt8(): Short = (getByte().toInt() and 0xFF).toShort()

  /**
   * Returns a signed 8-bit int calculated from the next byte the sequence.
   *
   * @return the 8 bit int value, between 0x00 and 0xFF
   */
  @Throws(IOException::class)
  fun getInt8(): Byte = getByte()// Intel ordering - LSB first// Motorola - MSB first

  /**
   * Returns an unsigned 16-bit int calculated from the next two bytes of the sequence.
   *
   * @return the 16 bit int value, between 0x0000 and 0xFFFF
   */
  @Throws(IOException::class)
  fun getUInt16(): Int {
      return if (isMotorolaByteOrder) { // Motorola - MSB first
        getByte().toInt() shl 8 and 0xFF00 or
          (getByte().toInt() and 0xFF)
      } else { // Intel ordering - LSB first
        getByte().toInt() and 0xFF or
          (getByte().toInt() shl 8 and 0xFF00)
      }// Intel ordering - LSB first// Motorola - MSB first
    }

  /**
   * Returns a signed 16-bit int calculated from two bytes of data (MSB, LSB).
   *
   * @return the 16 bit int value, between 0x0000 and 0xFFFF
   * @throws IOException the buffer does not contain enough bytes to service the request
   */
  @Throws(IOException::class)
  fun getInt16(): Short {
      return if (isMotorolaByteOrder) { // Motorola - MSB first
        (getByte().toInt() shl 8 and 0xFF00 or (getByte().toInt() and 0xFF)).toShort()
      } else { // Intel ordering - LSB first
        (getByte().toInt() and 0xFF or
          (getByte().toInt() shl 8 and 0xFF00)).toShort()
      }// Intel ordering - LSB first (little endian)// Motorola - MSB first (big endian)
    }

  /**
   * Get a 32-bit unsigned integer from the buffer, returning it as a long.
   *
   * @return the unsigned 32-bit int value as a long, between 0x00000000 and 0xFFFFFFFF
   * @throws IOException the buffer does not contain enough bytes to service the request
   */
  @Throws(IOException::class)
  fun getUInt32(): Long {
    return if (isMotorolaByteOrder) { // Motorola - MSB first (big endian)
      getByte().toLong() shl 24 and 0xFF000000L or
        (getByte().toLong() shl 16 and 0xFF0000L) or
        (getByte().toLong() shl 8 and 0xFF00L) or
        (getByte().toLong() and 0xFFL)
    } else { // Intel ordering - LSB first (little endian)
      getByte().toLong() and 0xFFL or
        (getByte().toLong() shl 8 and 0xFF00L) or
        (getByte().toLong() shl 16 and 0xFF0000L) or
        (getByte().toLong() shl 24 and 0xFF000000L)
    }// Intel ordering - LSB first (little endian)// Motorola - MSB first (big endian)
  }

  /**
   * Returns a signed 32-bit integer from four bytes of data.
   *
   * @return the signed 32 bit int value, between 0x00000000 and 0xFFFFFFFF
   * @throws IOException the buffer does not contain enough bytes to service the request
   */
  @Throws(IOException::class)
  fun getInt32(): Int {
      return if (isMotorolaByteOrder) { // Motorola - MSB first (big endian)
        getByte().toInt() shl 24 and -0x1000000 or
          (getByte().toInt() shl 16 and 0xFF0000) or
          (getByte().toInt() shl 8 and 0xFF00) or
          (getByte().toInt() and 0xFF)
      } else { // Intel ordering - LSB first (little endian)
        getByte().toInt() and 0xFF or
          (getByte().toInt() shl 8 and 0xFF00) or
          (getByte().toInt() shl 16 and 0xFF0000) or
          (getByte().toInt() shl 24 and -0x1000000)
      }// Intel ordering - LSB first// Motorola - MSB first
    }

  /**
   * Get a signed 64-bit integer from the buffer.
   *
   * @return the 64 bit int value, between 0x0000000000000000 and 0xFFFFFFFFFFFFFFFF
   * @throws IOException the buffer does not contain enough bytes to service the request
   */
  @Throws(IOException::class)
  fun getInt64(): Long {
    return if (isMotorolaByteOrder) { // Motorola - MSB first
      getByte().toLong() shl 56 and -0x100000000000000L or
        (getByte().toLong() shl 48 and 0xFF000000000000L) or
        (getByte().toLong() shl 40 and 0xFF0000000000L) or
        (getByte().toLong() shl 32 and 0xFF00000000L) or
        (getByte().toLong() shl 24 and 0xFF000000L) or
        (getByte().toLong() shl 16 and 0xFF0000L) or
        (getByte().toLong() shl 8 and 0xFF00L) or
        (getByte().toLong() and 0xFFL)
    } else { // Intel ordering - LSB first
      getByte().toLong() and 0xFFL or
        (getByte().toLong() shl 8 and 0xFF00L) or
        (getByte().toLong() shl 16 and 0xFF0000L) or
        (getByte().toLong() shl 24 and 0xFF000000L) or
        (getByte().toLong() shl 32 and 0xFF00000000L) or
        (getByte().toLong() shl 40 and 0xFF0000000000L) or
        (getByte().toLong() shl 48 and 0xFF000000000000L) or
        (getByte().toLong() shl 56 and -0x100000000000000L)
    }// this particular branch is untested
  }

  /**
   * Gets a s15.16 fixed point float from the buffer.
   *
   *
   * This particular fixed point encoding has one sign bit, 15 numerator bits and 16 denominator bits.
   *
   * @return the floating point value
   * @throws IOException the buffer does not contain enough bytes to service the request
   */
  @Throws(IOException::class)
  fun getS15Fixed16(): Float {
      return if (isMotorolaByteOrder) {
        val res = getByte().toInt() and 0xFF shl 8 or
          (getByte().toInt() and 0xFF)
        val d: Int = getByte().toInt() and 0xFF shl 8 or
          (getByte().toInt() and 0xFF)
        (res + d / 65536.0).toFloat()
      } else { // this particular branch is untested
        val d: Int = getByte().toInt() and 0xFF or
          (getByte().toInt() and 0xFF) shl 8
        val res = getByte().toInt() and 0xFF or
          (getByte().toInt() and 0xFF) shl 8
        (res + d / 65536.0).toFloat()
      }
    }

  @Throws(IOException::class)
  fun getFloat32(): Float = java.lang.Float.intBitsToFloat(getInt32())

  @Throws(IOException::class)
  fun getDouble64(): Double = java.lang.Double.longBitsToDouble(getInt64())

  @Throws(IOException::class)
  fun getString(bytesRequested: Int): String {
    return String(getBytes(bytesRequested))
  }

  @Throws(IOException::class)
  fun getString(bytesRequested: Int, charset: String?): String {
    val bytes = getBytes(bytesRequested)
    return try {
      String(bytes, Charset.forName(charset))
    } catch (e: UnsupportedEncodingException) {
      String(bytes)
    }
  }

  @Throws(IOException::class)
  fun getString(bytesRequested: Int, charset: Charset): String {
    val bytes = getBytes(bytesRequested)
    return String(bytes, charset)
  }

  @Throws(IOException::class)
  fun getStringValue(bytesRequested: Int, charset: Charset?): StringValue {
    return StringValue(getBytes(bytesRequested), charset)
  }

  /**
   * Creates a String from the stream, ending where `byte=='\0'` or where `length==maxLength`.
   *
   * @param maxLengthBytes The maximum number of bytes to read.  If a zero-byte is not reached within this limit,
   * reading will stop and the string will be truncated to this length.
   * @return The read string.
   * @throws IOException The buffer does not contain enough bytes to satisfy this request.
   */
  @Throws(IOException::class)
  fun getNullTerminatedString(maxLengthBytes: Int, charset: Charset?): String {
    return getNullTerminatedStringValue(maxLengthBytes, charset).toString()
  }

  /**
   * Creates a String from the stream, ending where `byte=='\0'` or where `length==maxLength`.
   *
   * @param maxLengthBytes The maximum number of bytes to read.  If a `\0` byte is not reached within this limit,
   * reading will stop and the string will be truncated to this length.
   * @param charset The `Charset` to register with the returned `StringValue`, or `null` if the encoding
   * is unknown
   * @return The read string.
   * @throws IOException The buffer does not contain enough bytes to satisfy this request.
   */
  @Throws(IOException::class)
  fun getNullTerminatedStringValue(maxLengthBytes: Int, charset: Charset?): StringValue {
    val bytes = getNullTerminatedBytes(maxLengthBytes)
    return StringValue(bytes, charset)
  }

  /**
   * Returns the sequence of bytes punctuated by a `\0` value.
   *
   * @param maxLengthBytes The maximum number of bytes to read. If a `\0` byte is not reached within this limit,
   * the returned array will be `maxLengthBytes` long.
   * @return The read byte array, excluding the null terminator.
   * @throws IOException The buffer does not contain enough bytes to satisfy this request.
   */
  @Throws(IOException::class)
  fun getNullTerminatedBytes(maxLengthBytes: Int): ByteArray {
    val buffer = ByteArray(maxLengthBytes)
    // Count the number of non-null bytes
    var length = 0
    while (length < buffer.size && getByte().also { buffer[length] = it } != 0.toByte()) length++
    if (length == maxLengthBytes) return buffer
    val bytes = ByteArray(length)
    if (length > 0) System.arraycopy(buffer, 0, bytes, 0, length)
    return bytes
  }
}
