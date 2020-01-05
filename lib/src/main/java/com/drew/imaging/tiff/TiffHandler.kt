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
package com.drew.imaging.tiff

import com.drew.imaging.tiff.TiffProcessingException
import com.drew.lang.RandomAccessReader
import com.drew.lang.Rational
import com.drew.metadata.StringValue
import java.io.IOException

/**
 * Interface of an class capable of handling events raised during the reading of a TIFF file
 * via [TiffReader].
 *
 * @author Drew Noakes https://drewnoakes.com
 */
interface TiffHandler {
  /**
   * Receives the 2-byte marker found in the TIFF header.
   *
   *
   * Implementations are not obligated to use this information for any purpose, though it may be useful for
   * validation or perhaps differentiating the type of mapping to use for observed tags and IFDs.
   *
   * @param marker the 2-byte value found at position 2 of the TIFF header
   */
  @Throws(TiffProcessingException::class)
  fun setTiffMarker(marker: Int)

  fun tryEnterSubIfd(tagId: Int): Boolean
  fun hasFollowerIfd(): Boolean
  fun endingIFD()
  fun tryCustomProcessFormat(tagId: Int, formatCode: Int, componentCount: Long): Long?
  @Throws(IOException::class)
  fun customProcessTag(tagOffset: Int,
                       processedIfdOffsets: Set<Int>,
                       tiffHeaderOffset: Int,
                       reader: RandomAccessReader,
                       tagId: Int,
                       byteCount: Int): Boolean

  fun warn(message: String)
  fun error(message: String)
  fun setByteArray(tagId: Int, bytes: ByteArray)
  fun setString(tagId: Int, string: StringValue)
  fun setRational(tagId: Int, rational: Rational)
  fun setRationalArray(tagId: Int, array: Array<Rational?>)
  fun setFloat(tagId: Int, float32: Float)
  fun setFloatArray(tagId: Int, array: FloatArray)
  fun setDouble(tagId: Int, double64: Double)
  fun setDoubleArray(tagId: Int, array: DoubleArray)
  fun setInt8s(tagId: Int, int8s: Byte)
  fun setInt8sArray(tagId: Int, array: ByteArray)
  fun setInt8u(tagId: Int, int8u: Short)
  fun setInt8uArray(tagId: Int, array: ShortArray)
  fun setInt16s(tagId: Int, int16s: Int)
  fun setInt16sArray(tagId: Int, array: ShortArray)
  fun setInt16u(tagId: Int, int16u: Int)
  fun setInt16uArray(tagId: Int, array: IntArray)
  fun setInt32s(tagId: Int, int32s: Int)
  fun setInt32sArray(tagId: Int, array: IntArray)
  fun setInt32u(tagId: Int, int32u: Long)
  fun setInt32uArray(tagId: Int, array: LongArray)
}
