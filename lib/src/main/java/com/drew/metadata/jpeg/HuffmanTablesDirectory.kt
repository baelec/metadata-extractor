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
package com.drew.metadata.jpeg

import com.drew.metadata.Directory
import com.drew.metadata.MetadataException
import java.util.*

/**
 * Directory of tables for the DHT (Define Huffman Table(s)) segment.
 *
 * @author Nadahar
 */
class HuffmanTablesDirectory : Directory() {
  override val tagNameMap = Companion.tagNameMap
  /**
   * @return The [List] of [HuffmanTable]s in this
   * [Directory].
   */
  @JvmField
  val tables: List<HuffmanTable> = ArrayList(4)

  companion object {
    const val TAG_NUMBER_OF_TABLES = 1
    @JvmField
    val TYPICAL_LUMINANCE_DC_LENGTHS = byteArrayOf(
      0x00.toByte(), 0x01.toByte(), 0x05.toByte(), 0x01.toByte(), 0x01.toByte(), 0x01.toByte(), 0x01.toByte(), 0x01.toByte(),
      0x01.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte()
    )
    @JvmField
    val TYPICAL_LUMINANCE_DC_VALUES = byteArrayOf(
      0x00.toByte(), 0x01.toByte(), 0x02.toByte(), 0x03.toByte(), 0x04.toByte(), 0x05.toByte(), 0x06.toByte(), 0x07.toByte(),
      0x08.toByte(), 0x09.toByte(), 0x0A.toByte(), 0x0B.toByte()
    )
    val TYPICAL_CHROMINANCE_DC_LENGTHS = byteArrayOf(
      0x00.toByte(), 0x03.toByte(), 0x01.toByte(), 0x01.toByte(), 0x01.toByte(), 0x01.toByte(), 0x01.toByte(), 0x01.toByte(),
      0x01.toByte(), 0x01.toByte(), 0x01.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte()
    )
    val TYPICAL_CHROMINANCE_DC_VALUES = byteArrayOf(
      0x00.toByte(), 0x01.toByte(), 0x02.toByte(), 0x03.toByte(), 0x04.toByte(), 0x05.toByte(), 0x06.toByte(), 0x07.toByte(),
      0x08.toByte(), 0x09.toByte(), 0x0A.toByte(), 0x0B.toByte()
    )
    val TYPICAL_LUMINANCE_AC_LENGTHS = byteArrayOf(
      0x00.toByte(), 0x02.toByte(), 0x01.toByte(), 0x03.toByte(), 0x03.toByte(), 0x02.toByte(), 0x04.toByte(), 0x03.toByte(),
      0x05.toByte(), 0x05.toByte(), 0x04.toByte(), 0x04.toByte(), 0x00.toByte(), 0x00.toByte(), 0x01.toByte(), 0x7D.toByte()
    )
    val TYPICAL_LUMINANCE_AC_VALUES = byteArrayOf(
      0x01.toByte(), 0x02.toByte(), 0x03.toByte(), 0x00.toByte(), 0x04.toByte(), 0x11.toByte(), 0x05.toByte(), 0x12.toByte(),
      0x21.toByte(), 0x31.toByte(), 0x41.toByte(), 0x06.toByte(), 0x13.toByte(), 0x51.toByte(), 0x61.toByte(), 0x07.toByte(),
      0x22.toByte(), 0x71.toByte(), 0x14.toByte(), 0x32.toByte(), 0x81.toByte(), 0x91.toByte(), 0xA1.toByte(), 0x08.toByte(),
      0x23.toByte(), 0x42.toByte(), 0xB1.toByte(), 0xC1.toByte(), 0x15.toByte(), 0x52.toByte(), 0xD1.toByte(), 0xF0.toByte(),
      0x24.toByte(), 0x33.toByte(), 0x62.toByte(), 0x72.toByte(), 0x82.toByte(), 0x09.toByte(), 0x0A.toByte(), 0x16.toByte(),
      0x17.toByte(), 0x18.toByte(), 0x19.toByte(), 0x1A.toByte(), 0x25.toByte(), 0x26.toByte(), 0x27.toByte(), 0x28.toByte(),
      0x29.toByte(), 0x2A.toByte(), 0x34.toByte(), 0x35.toByte(), 0x36.toByte(), 0x37.toByte(), 0x38.toByte(), 0x39.toByte(),
      0x3A.toByte(), 0x43.toByte(), 0x44.toByte(), 0x45.toByte(), 0x46.toByte(), 0x47.toByte(), 0x48.toByte(), 0x49.toByte(),
      0x4A.toByte(), 0x53.toByte(), 0x54.toByte(), 0x55.toByte(), 0x56.toByte(), 0x57.toByte(), 0x58.toByte(), 0x59.toByte(),
      0x5A.toByte(), 0x63.toByte(), 0x64.toByte(), 0x65.toByte(), 0x66.toByte(), 0x67.toByte(), 0x68.toByte(), 0x69.toByte(),
      0x6A.toByte(), 0x73.toByte(), 0x74.toByte(), 0x75.toByte(), 0x76.toByte(), 0x77.toByte(), 0x78.toByte(), 0x79.toByte(),
      0x7A.toByte(), 0x83.toByte(), 0x84.toByte(), 0x85.toByte(), 0x86.toByte(), 0x87.toByte(), 0x88.toByte(), 0x89.toByte(),
      0x8A.toByte(), 0x92.toByte(), 0x93.toByte(), 0x94.toByte(), 0x95.toByte(), 0x96.toByte(), 0x97.toByte(), 0x98.toByte(),
      0x99.toByte(), 0x9A.toByte(), 0xA2.toByte(), 0xA3.toByte(), 0xA4.toByte(), 0xA5.toByte(), 0xA6.toByte(), 0xA7.toByte(),
      0xA8.toByte(), 0xA9.toByte(), 0xAA.toByte(), 0xB2.toByte(), 0xB3.toByte(), 0xB4.toByte(), 0xB5.toByte(), 0xB6.toByte(),
      0xB7.toByte(), 0xB8.toByte(), 0xB9.toByte(), 0xBA.toByte(), 0xC2.toByte(), 0xC3.toByte(), 0xC4.toByte(), 0xC5.toByte(),
      0xC6.toByte(), 0xC7.toByte(), 0xC8.toByte(), 0xC9.toByte(), 0xCA.toByte(), 0xD2.toByte(), 0xD3.toByte(), 0xD4.toByte(),
      0xD5.toByte(), 0xD6.toByte(), 0xD7.toByte(), 0xD8.toByte(), 0xD9.toByte(), 0xDA.toByte(), 0xE1.toByte(), 0xE2.toByte(),
      0xE3.toByte(), 0xE4.toByte(), 0xE5.toByte(), 0xE6.toByte(), 0xE7.toByte(), 0xE8.toByte(), 0xE9.toByte(), 0xEA.toByte(),
      0xF1.toByte(), 0xF2.toByte(), 0xF3.toByte(), 0xF4.toByte(), 0xF5.toByte(), 0xF6.toByte(), 0xF7.toByte(), 0xF8.toByte(),
      0xF9.toByte(), 0xFA.toByte()
    )
    @JvmField
    val TYPICAL_CHROMINANCE_AC_LENGTHS = byteArrayOf(
      0x00.toByte(), 0x02.toByte(), 0x01.toByte(), 0x02.toByte(), 0x04.toByte(), 0x04.toByte(), 0x03.toByte(), 0x04.toByte(),
      0x07.toByte(), 0x05.toByte(), 0x04.toByte(), 0x04.toByte(), 0x00.toByte(), 0x01.toByte(), 0x02.toByte(), 0x77.toByte()
    )
    @JvmField
    val TYPICAL_CHROMINANCE_AC_VALUES = byteArrayOf(
      0x00.toByte(), 0x01.toByte(), 0x02.toByte(), 0x03.toByte(), 0x11.toByte(), 0x04.toByte(), 0x05.toByte(), 0x21.toByte(),
      0x31.toByte(), 0x06.toByte(), 0x12.toByte(), 0x41.toByte(), 0x51.toByte(), 0x07.toByte(), 0x61.toByte(), 0x71.toByte(),
      0x13.toByte(), 0x22.toByte(), 0x32.toByte(), 0x81.toByte(), 0x08.toByte(), 0x14.toByte(), 0x42.toByte(), 0x91.toByte(),
      0xA1.toByte(), 0xB1.toByte(), 0xC1.toByte(), 0x09.toByte(), 0x23.toByte(), 0x33.toByte(), 0x52.toByte(), 0xF0.toByte(),
      0x15.toByte(), 0x62.toByte(), 0x72.toByte(), 0xD1.toByte(), 0x0A.toByte(), 0x16.toByte(), 0x24.toByte(), 0x34.toByte(),
      0xE1.toByte(), 0x25.toByte(), 0xF1.toByte(), 0x17.toByte(), 0x18.toByte(), 0x19.toByte(), 0x1A.toByte(), 0x26.toByte(),
      0x27.toByte(), 0x28.toByte(), 0x29.toByte(), 0x2A.toByte(), 0x35.toByte(), 0x36.toByte(), 0x37.toByte(), 0x38.toByte(),
      0x39.toByte(), 0x3A.toByte(), 0x43.toByte(), 0x44.toByte(), 0x45.toByte(), 0x46.toByte(), 0x47.toByte(), 0x48.toByte(),
      0x49.toByte(), 0x4A.toByte(), 0x53.toByte(), 0x54.toByte(), 0x55.toByte(), 0x56.toByte(), 0x57.toByte(), 0x58.toByte(),
      0x59.toByte(), 0x5A.toByte(), 0x63.toByte(), 0x64.toByte(), 0x65.toByte(), 0x66.toByte(), 0x67.toByte(), 0x68.toByte(),
      0x69.toByte(), 0x6A.toByte(), 0x73.toByte(), 0x74.toByte(), 0x75.toByte(), 0x76.toByte(), 0x77.toByte(), 0x78.toByte(),
      0x79.toByte(), 0x7A.toByte(), 0x82.toByte(), 0x83.toByte(), 0x84.toByte(), 0x85.toByte(), 0x86.toByte(), 0x87.toByte(),
      0x88.toByte(), 0x89.toByte(), 0x8A.toByte(), 0x92.toByte(), 0x93.toByte(), 0x94.toByte(), 0x95.toByte(), 0x96.toByte(),
      0x97.toByte(), 0x98.toByte(), 0x99.toByte(), 0x9A.toByte(), 0xA2.toByte(), 0xA3.toByte(), 0xA4.toByte(), 0xA5.toByte(),
      0xA6.toByte(), 0xA7.toByte(), 0xA8.toByte(), 0xA9.toByte(), 0xAA.toByte(), 0xB2.toByte(), 0xB3.toByte(), 0xB4.toByte(),
      0xB5.toByte(), 0xB6.toByte(), 0xB7.toByte(), 0xB8.toByte(), 0xB9.toByte(), 0xBA.toByte(), 0xC2.toByte(), 0xC3.toByte(),
      0xC4.toByte(), 0xC5.toByte(), 0xC6.toByte(), 0xC7.toByte(), 0xC8.toByte(), 0xC9.toByte(), 0xCA.toByte(), 0xD2.toByte(),
      0xD3.toByte(), 0xD4.toByte(), 0xD5.toByte(), 0xD6.toByte(), 0xD7.toByte(), 0xD8.toByte(), 0xD9.toByte(), 0xDA.toByte(),
      0xE2.toByte(), 0xE3.toByte(), 0xE4.toByte(), 0xE5.toByte(), 0xE6.toByte(), 0xE7.toByte(), 0xE8.toByte(), 0xE9.toByte(),
      0xEA.toByte(), 0xF2.toByte(), 0xF3.toByte(), 0xF4.toByte(), 0xF5.toByte(), 0xF6.toByte(), 0xF7.toByte(), 0xF8.toByte(),
      0xF9.toByte(), 0xFA.toByte()
    )
    protected val tagNameMap = HashMap<Int, String>()

    init {
      tagNameMap[TAG_NUMBER_OF_TABLES] = "Number of Tables"
    }
  }

  override val name: String
    get() = "Huffman"

  /**
   * @param tableNumber The zero-based index of the table. This number is normally between 0 and 3.
   * Use [.getNumberOfTables] for bounds-checking.
   * @return The [HuffmanTable] having the specified number.
   */
  fun getTable(tableNumber: Int): HuffmanTable {
    return tables[tableNumber]
  }

  /**
   * @return The number of Huffman tables held by this [HuffmanTablesDirectory] instance.
   */
  @get:Throws(MetadataException::class)
  val numberOfTables: Int
    get() = getInt(TAG_NUMBER_OF_TABLES)

  /**
   * Evaluates whether all the tables in this [HuffmanTablesDirectory]
   * are "typical" Huffman tables.
   *
   *
   * "Typical" has a special meaning in this context as the JPEG standard
   * (ISO/IEC 10918 or ITU-T T.81) defines 4 Huffman tables that has been
   * developed from the average statistics of a large set of images with 8-bit
   * precision. Using these instead of calculating the optimal Huffman tables
   * for a given image is faster, and is preferred by many hardware encoders
   * and some hardware decoders.
   *
   *
   * Even though the JPEG standard doesn't define these as "standard tables"
   * and requires a decoder to be able to read any valid Huffman tables, some
   * are in reality limited decoding images using these "typical" tables.
   * Standards like DCF (Design rule for Camera File system) and DLNA (Digital
   * Living Network Alliance) actually requires any compliant JPEG to use only
   * the "typical" Huffman tables.
   *
   *
   * This is also related to the term "optimized" JPEG. An "optimized" JPEG is
   * a JPEG that doesn't use the "typical" Huffman tables.
   *
   * @return Whether or not all the tables in this
   * [HuffmanTablesDirectory] are the predefined "typical"
   * Huffman tables.
   */
  val isTypical: Boolean
    get() {
      if (tables.isEmpty()) {
        return false
      }
      for (table in tables) {
        if (!table.isTypical) {
          return false
        }
      }
      return true
    }

  /**
   * The opposite of [.isTypical].
   *
   * @return Whether or not the tables in this [HuffmanTablesDirectory]
   * are "optimized" - which means that at least one of them aren't
   * one of the "typical" Huffman tables.
   */
  val isOptimized: Boolean
    get() = !isTypical

  /**
   * An instance of this class holds a JPEG Huffman table.
   */
  class HuffmanTable(
    tableClass: HuffmanTableClass,
    tableDestinationId: Int,
    lengthBytes: ByteArray,
    valueBytes: ByteArray) {
    /**
     * @return The table length in bytes.
     */
    val tableLength: Int
    /**
     * @return The [HuffmanTableClass] of this table.
     */
    val tableClass: HuffmanTableClass
    /**
     * @return the the destination identifier for this table.
     */
    val tableDestinationId: Int
    private val _lengthBytes: ByteArray
    private val _valueBytes: ByteArray

    /**
     * @return A byte array with the L values for this table.
     */
    val lengthBytes: ByteArray
      get() {
        val result = ByteArray(_lengthBytes.size)
        System.arraycopy(_lengthBytes, 0, result, 0, _lengthBytes.size)
        return result
      }

    /**
     * @return A byte array with the V values for this table.
     */
    val valueBytes: ByteArray
      get() {
        val result = ByteArray(_valueBytes.size)
        System.arraycopy(_valueBytes, 0, result, 0, _valueBytes.size)
        return result
      }

    /**
     * Evaluates whether this table is a "typical" Huffman table.
     *
     *
     * "Typical" has a special meaning in this context as the JPEG standard
     * (ISO/IEC 10918 or ITU-T T.81) defines 4 Huffman tables that has been
     * developed from the average statistics of a large set of images with
     * 8-bit precision. Using these instead of calculating the optimal
     * Huffman tables for a given image is faster, and is preferred by many
     * hardware encoders and some hardware decoders.
     *
     *
     * Even though the JPEG standard doesn't define these as
     * "standard tables" and requires a decoder to be able to read any valid
     * Huffman tables, some are in reality limited decoding images using
     * these "typical" tables. Standards like DCF (Design rule for Camera
     * File system) and DLNA (Digital Living Network Alliance) actually
     * requires any compliant JPEG to use only the "typical" Huffman tables.
     *
     *
     * This is also related to the term "optimized" JPEG. An "optimized"
     * JPEG is a JPEG that doesn't use the "typical" Huffman tables.
     *
     * @return Whether or not this table is one of the predefined "typical"
     * Huffman tables.
     */
    val isTypical: Boolean
      get() {
        if (tableClass == HuffmanTableClass.DC) {
          return _lengthBytes.contentEquals(TYPICAL_LUMINANCE_DC_LENGTHS) &&
            _valueBytes.contentEquals(TYPICAL_LUMINANCE_DC_VALUES) ||
            _lengthBytes.contentEquals(TYPICAL_CHROMINANCE_DC_LENGTHS) &&
            _valueBytes.contentEquals(TYPICAL_CHROMINANCE_DC_VALUES)
        } else if (tableClass == HuffmanTableClass.AC) {
          return _lengthBytes.contentEquals(TYPICAL_LUMINANCE_AC_LENGTHS) &&
            _valueBytes.contentEquals(TYPICAL_LUMINANCE_AC_VALUES) ||
            _lengthBytes.contentEquals(TYPICAL_CHROMINANCE_AC_LENGTHS) &&
            _valueBytes.contentEquals(TYPICAL_CHROMINANCE_AC_VALUES)
        }
        return false
      }

    /**
     * The opposite of [.isTypical].
     *
     * @return Whether or not this table is "optimized" - which means that
     * it isn't one of the "typical" Huffman tables.
     */
    val isOptimized: Boolean
      get() = !isTypical

    enum class HuffmanTableClass {
      DC, AC, UNKNOWN;

      companion object {
        @JvmStatic
        fun typeOf(value: Int): HuffmanTableClass {
          return when (value) {
            0 -> DC
            1 -> AC
            else -> UNKNOWN
          }
        }
      }
    }

    init {
      this.tableClass = tableClass
      this.tableDestinationId = tableDestinationId
      _lengthBytes = lengthBytes
      _valueBytes = valueBytes
      tableLength = _valueBytes.size + 17
    }
  }

  init {
    setDescriptor(HuffmanTablesDescriptor(this))
  }
}
