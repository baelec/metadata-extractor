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
package com.drew.metadata.photoshop

import com.drew.lang.*
import com.drew.metadata.TagDescriptor
import java.io.IOException
import java.text.DecimalFormat
import java.util.*
import kotlin.math.pow

/**
 * @author Drew Noakes https://drewnoakes.com
 * @author Yuri Binev
 * @author Payton Garland
 */
class PhotoshopDescriptor(directory: PhotoshopDirectory) : TagDescriptor<PhotoshopDirectory>(directory) {
  override fun getDescription(tagType: Int): String? {
    return when (tagType) {
      PhotoshopDirectory.TAG_THUMBNAIL, PhotoshopDirectory.TAG_THUMBNAIL_OLD -> getThumbnailDescription(tagType)
      PhotoshopDirectory.TAG_URL, PhotoshopDirectory.TAG_XML -> getSimpleString(tagType)
      PhotoshopDirectory.TAG_IPTC -> getBinaryDataString(tagType)
      PhotoshopDirectory.TAG_SLICES -> slicesDescription
      PhotoshopDirectory.TAG_VERSION -> versionDescription
      PhotoshopDirectory.TAG_COPYRIGHT -> getBooleanString(tagType)
      PhotoshopDirectory.TAG_RESOLUTION_INFO -> resolutionInfoDescription
      PhotoshopDirectory.TAG_GLOBAL_ANGLE, PhotoshopDirectory.TAG_GLOBAL_ALTITUDE, PhotoshopDirectory.TAG_URL_LIST, PhotoshopDirectory.TAG_SEED_NUMBER -> get32BitNumberString(tagType)
      PhotoshopDirectory.TAG_JPEG_QUALITY -> jpegQualityString
      PhotoshopDirectory.TAG_PRINT_SCALE -> printScaleDescription
      PhotoshopDirectory.TAG_PIXEL_ASPECT_RATIO -> pixelAspectRatioString
      PhotoshopDirectory.TAG_CLIPPING_PATH_NAME -> getClippingPathNameString(tagType)
      else -> {
        if (tagType >= 0x07D0 && tagType <= 0x0BB6) getPathString(tagType) else super.getDescription(tagType)
      }
    }
  }// & 0xFFFF;

  // & 0xFFFF;
  val jpegQualityString: String?
    get() {
      try {
        val b = _directory.getByteArray(PhotoshopDirectory.TAG_JPEG_QUALITY)
          ?: return _directory.getString(PhotoshopDirectory.TAG_JPEG_QUALITY)
        val reader: RandomAccessReader = ByteArrayReader(b)
        val q = reader.getUInt16(0) // & 0xFFFF;
        val f = reader.getUInt16(2) // & 0xFFFF;
        val s = reader.getUInt16(4)
        val q1 = if (q <= 0xFFFF && q >= 0xFFFD) q - 0xFFFC else if (q <= 8) q + 4 else q
        val quality: String
        quality = when (q) {
          0xFFFD, 0xFFFE, 0xFFFF, 0 -> "Low"
          1, 2, 3 -> "Medium"
          4, 5 -> "High"
          6, 7, 8 -> "Maximum"
          else -> "Unknown"
        }
        val format: String
        format = when (f) {
          0x0000 -> "Standard"
          0x0001 -> "Optimised"
          0x0101 -> "Progressive"
          else -> "Unknown 0x%04X".format(f)
        }
        val scans = if (s in 1..3) "%d".format(s + 2) else "Unknown 0x%04X".format(s)
        return "%d (%s), %s format, %s scans".format(q1, quality, format, scans)
      } catch (e: IOException) {
        return null
      }
    }

  val pixelAspectRatioString: String?
    get() {
      try {
        val bytes = _directory.getByteArray(PhotoshopDirectory.TAG_PIXEL_ASPECT_RATIO) ?: return null
        val reader: RandomAccessReader = ByteArrayReader(bytes)
        val d = reader.getDouble64(4)
        return d.toString()
      } catch (e: Exception) {
        return null
      }
    }

  val printScaleDescription: String?
    get() {
      try {
        val bytes = _directory.getByteArray(PhotoshopDirectory.TAG_PRINT_SCALE) ?: return null
        val reader: RandomAccessReader = ByteArrayReader(bytes)
        val style = reader.getInt32(0)
        val locX = reader.getFloat32(2)
        val locY = reader.getFloat32(6)
        val scale = reader.getFloat32(10)
        return when (style) {
          0 -> "Centered, Scale $scale"
          1 -> "Size to fit"
          2 -> "User defined, X:%s Y:%s, Scale:%s".format(locX, locY, scale)
          else -> "Unknown %04X, X:%s Y:%s, Scale:%s".format(style, locX, locY, scale)
        }
      } catch (e: Exception) {
        return null
      }
    }

  // is this the correct offset? it's only reading 4 bytes each time
  val resolutionInfoDescription: String?
    get(){
      try {
        val bytes = _directory.getByteArray(PhotoshopDirectory.TAG_RESOLUTION_INFO) ?: return null
        val reader: RandomAccessReader = ByteArrayReader(bytes)
        val resX = reader.getS15Fixed16(0)
        val resY = reader.getS15Fixed16(8) // is this the correct offset? it's only reading 4 bytes each time
        val format = DecimalFormat("0.##")
        return "${format.format(resX.toDouble())}x${format.format(resY.toDouble())} DPI"
      } catch (e: Exception) {
        return null
      }
    }

  val versionDescription: String?
    get() {
      try {
        val bytes = _directory.getByteArray(PhotoshopDirectory.TAG_VERSION) ?: return null
        val reader: RandomAccessReader = ByteArrayReader(bytes)
        var pos = 0
        val ver = reader.getInt32(0)
        pos += 4
        pos++
        val readerLength = reader.getInt32(5)
        pos += 4
        val readerStr = reader.getString(9, readerLength * 2, UTF_16)
        pos += readerLength * 2
        val writerLength = reader.getInt32(pos)
        pos += 4
        val writerStr = reader.getString(pos, writerLength * 2, UTF_16)
        pos += writerLength * 2
        val fileVersion = reader.getInt32(pos)
        return "%d (%s, %s) %d".format(ver, readerStr, writerStr, fileVersion)
      } catch (e: IOException) {
        return null
      }
    }

  val slicesDescription: String?
    get() {
      try {
        val bytes = _directory.getByteArray(PhotoshopDirectory.TAG_SLICES) ?: return null
        val reader: RandomAccessReader = ByteArrayReader(bytes)
        val nameLength = reader.getInt32(20)
        val name = reader.getString(24, nameLength * 2, UTF_16)
        val pos = 24 + nameLength * 2
        val sliceCount = reader.getInt32(pos)
        return "%s (%d,%d,%d,%d) %d Slices".format(
          name, reader.getInt32(4), reader.getInt32(8), reader.getInt32(12), reader.getInt32(16), sliceCount)
      } catch (e: IOException) {
          return null
      }
    }

  fun getThumbnailDescription(tagType: Int): String? {
    return try {
      val v = _directory.getByteArray(tagType) ?: return null
      val reader: RandomAccessReader = ByteArrayReader(v)
      val format = reader.getInt32(0)
      val width = reader.getInt32(4)
      val height = reader.getInt32(8)
      //skip WidthBytes
      val totalSize = reader.getInt32(16)
      val compSize = reader.getInt32(20)
      val bpp = reader.getInt32(24)
      //skip Number of planes
      "%s, %dx%d, Decomp %d bytes, %d bpp, %d bytes".format(
        if (format == 1) "JpegRGB" else "RawRGB",
        width, height, totalSize, bpp, compSize)
    } catch (e: IOException) {
      null
    }
  }

  private fun getBooleanString(tag: Int): String? {
    val bytes = _directory.getByteArray(tag)
    if (bytes == null || bytes.isEmpty()) return null
    return if (bytes[0] == 0.toByte()) "No" else "Yes"
  }

  private fun get32BitNumberString(tag: Int): String? {
    val bytes = _directory.getByteArray(tag) ?: return null
    val reader: RandomAccessReader = ByteArrayReader(bytes)
    return try {
      "%d".format(reader.getInt32(0))
    } catch (e: IOException) {
      null
    }
  }

  private fun getSimpleString(tagType: Int): String? {
    val bytes = _directory.getByteArray(tagType) ?: return null
    return String(bytes)
  }

  private fun getBinaryDataString(tagType: Int): String? {
    val bytes = _directory.getByteArray(tagType) ?: return null
    return "%d bytes binary data".format(bytes.size)
  }

  fun getClippingPathNameString(tagType: Int): String? {
    return try {
      val bytes = _directory.getByteArray(tagType) ?: return null
      val reader: RandomAccessReader = ByteArrayReader(bytes)
      val length = reader.getByte(0).toInt()
      String(reader.getBytes(1, length), UTF_8)
    } catch (e: Exception) {
      null
    }
  }

  fun getPathString(tagType: Int): String? {
    return try {
      val bytes = _directory.getByteArray(tagType) ?: return null
      val reader: RandomAccessReader = ByteArrayReader(bytes)
      val length = (reader.length - reader.getByte(reader.length.toInt() - 1) - 1).toInt() / 26
      var fillRecord: String? = null
      // Possible subpaths
      var cSubpath = Subpath()
      var oSubpath = Subpath()
      val paths = ArrayList<Subpath>()
      // Loop through each path resource block segment (26-bytes)
      for (i in 0 until length) { // Spacer takes into account which block is currently being worked on while accessing byte array
        val recordSpacer = 26 * i
        when (val selector = reader.getInt16(recordSpacer).toInt()) {
          0 -> {
            // Insert previous Paths if there are any
            if (cSubpath.size() != 0) {
              paths.add(cSubpath)
            }
            // Make path size accordingly
            cSubpath = Subpath("Closed Subpath")
          }
          1, 2 -> {
            val knot: Knot = if (selector == 1) Knot("Linked") else Knot("Unlinked")
            // Insert each point into cSubpath - points are 32-bit signed, fixed point numbers and have 8-bits before the point
            var j = 0
            while (j < 6) {
              knot.setPoint(j, reader.getInt8(j * 4 + 2 + recordSpacer) + reader.getInt24(j * 4 + 3 + recordSpacer) / 2.0.pow(24.0))
              j++
            }
            cSubpath.add(knot)
          }
          3 -> {
            // Insert previous Paths if there are any
            if (oSubpath.size() != 0) {
              paths.add(oSubpath)
            }
            // Make path size accordingly
            oSubpath = Subpath("Open Subpath")
          }
          4, 5 -> {
            var knot: Knot = if (selector == 4) Knot("Linked") else Knot("Unlinked")
            // Insert each point into oSubpath - points are 32-bit signed, fixed point numbers and have 8-bits before the point
            var j = 0
            while (j < 6) {
              knot.setPoint(j, reader.getInt8(j * 4 + 2 + recordSpacer) + reader.getInt24(j * 4 + 3 + recordSpacer) / 2.0.pow(24.0))
              j++
            }
            oSubpath.add(knot)
          }
          6 -> {
          }
          7 -> {
          }
          8 -> fillRecord = if (reader.getInt16(2 + recordSpacer).toInt() == 1) "with all pixels" else "without all pixels"
        }
      }
      // Add any more paths that were not added already
      if (cSubpath.size() != 0) paths.add(cSubpath)
      if (oSubpath.size() != 0) paths.add(oSubpath)
      // Extract name (previously appended to end of byte array)
      val nameLength = reader.getByte(reader.length.toInt() - 1).toInt()
      val name: String = reader.getString(reader.length.toInt() - nameLength - 1, nameLength, ASCII)
      // Build description
      val str = StringBuilder()
      str.append('"').append(name).append('"')
        .append(" having ")
      if (fillRecord != null) str.append("initial fill rule \"").append(fillRecord).append("\" and ")
      str.append(paths.size).append(if (paths.size == 1) " subpath:" else " subpaths:")
      for (path in paths) {
        str.append("\n- ").append(path.type).append(" with ").append(paths.size).append(if (paths.size == 1) " knot:" else " knots:")
        for (knot in path.knots) {
          str.append("\n  - ").append(knot.type)
          str.append(" (").append(knot.getPoint(0)).append(",").append(knot.getPoint(1)).append(")")
          str.append(" (").append(knot.getPoint(2)).append(",").append(knot.getPoint(3)).append(")")
          str.append(" (").append(knot.getPoint(4)).append(",").append(knot.getPoint(5)).append(")")
        }
      }
      str.toString()
    } catch (e: Exception) {
      null
    }
  }
}
