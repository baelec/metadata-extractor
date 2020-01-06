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

import com.drew.imaging.jpeg.JpegSegmentMetadataReader
import com.drew.imaging.jpeg.JpegSegmentType
import com.drew.imaging.jpeg.JpegSegmentType.Companion.fromByte
import com.drew.lang.SequentialByteArrayReader
import com.drew.lang.SequentialReader
import com.drew.metadata.Metadata
import com.drew.metadata.jpeg.HuffmanTablesDirectory.HuffmanTable
import com.drew.metadata.jpeg.HuffmanTablesDirectory.HuffmanTable.HuffmanTableClass.Companion.typeOf
import java.io.IOException

/**
 * Reader for JPEG Huffman tables, found in the DHT JPEG segment.
 *
 * @author Nadahar
 */
class JpegDhtReader : JpegSegmentMetadataReader {
  override val segmentTypes: Iterable<JpegSegmentType>
    get() = listOf(JpegSegmentType.DHT)

  override fun readJpegSegments(segments: Iterable<ByteArray>, metadata: Metadata, segmentType: JpegSegmentType) {
    for (segmentBytes in segments) {
      extract(SequentialByteArrayReader(segmentBytes), metadata)
    }
  }

  /**
   * Performs the DHT tables extraction, adding found tables to the specified
   * instance of [Metadata].
   */
  fun extract(reader: SequentialReader, metadata: Metadata) {
    var directory = metadata.getFirstDirectoryOfType(HuffmanTablesDirectory::class.java)
    if (directory == null) {
      directory = HuffmanTablesDirectory()
      metadata.addDirectory<HuffmanTablesDirectory>(directory)
    }
    try {
      while (reader.available() > 0) {
        val header = reader.getByte().toInt()
        val tableClass = typeOf(header and 0xF0 shr 4)
        val tableDestinationId: Int = header and 0xF
        val lBytes = getBytes(reader, 16)
        var vCount = 0
        for (b in lBytes) {
          vCount += b.toInt() and 0xFF
        }
        val vBytes = getBytes(reader, vCount)
        directory.tables.add(HuffmanTable(tableClass, tableDestinationId, lBytes, vBytes))
      }
    } catch (me: IOException) {
      directory.addError(me.message!!)
    }
    directory.setInt(HuffmanTablesDirectory.TAG_NUMBER_OF_TABLES, directory.tables.size)
  }

  @Throws(IOException::class)
  private fun getBytes(reader: SequentialReader, count: Int): ByteArray {
    val bytes = ByteArray(count)
    for (i in 0 until count) {
      val b = reader.getByte()
      if (b.toInt() and 0xFF == 0xFF) {
        val stuffing = reader.getByte()
        if (stuffing.toInt() != 0x00) {
          throw IOException("Marker " + fromByte(stuffing) + " found inside DHT segment")
        }
      }
      bytes[i] = b
    }
    return bytes
  }
}
