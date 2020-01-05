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
package com.drew.metadata.icc

import com.drew.lang.ASCII
import com.drew.lang.ByteArrayReader
import com.drew.lang.RandomAccessReader
import com.drew.lang.UTF_16BE
import com.drew.metadata.TagDescriptor
import java.io.IOException
import java.io.UnsupportedEncodingException
import java.text.DecimalFormat
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.roundToLong

/**
 * @author Yuri Binev
 * @author Drew Noakes https://drewnoakes.com
 */
class IccDescriptor(directory: IccDirectory) : TagDescriptor<IccDirectory>(directory) {
  override fun getDescription(tagType: Int): String? {
    when (tagType) {
      IccDirectory.TAG_PROFILE_VERSION -> return profileVersionDescription
      IccDirectory.TAG_PROFILE_CLASS -> return profileClassDescription
      IccDirectory.TAG_PLATFORM -> return platformDescription
      IccDirectory.TAG_RENDERING_INTENT -> return renderingIntentDescription
    }
    return if (tagType in 0x20202021..0x7A7A7A79) getTagDataString(tagType) else super.getDescription(tagType)
  }

  private fun getTagDataString(tagType: Int): String? {
    return try {
      val bytes = _directory.getByteArray(tagType) ?: return _directory.getString(tagType)
      val reader: RandomAccessReader = ByteArrayReader(bytes)
      when (val iccTagType = reader.getInt32(0)) {
        ICC_TAG_TYPE_TEXT -> {
          return try {
            String(bytes, 8, bytes.size - 8 - 1, ASCII)
          } catch (ex: UnsupportedEncodingException) {
            String(bytes, 8, bytes.size - 8 - 1)
          }
        }
        ICC_TAG_TYPE_DESC -> {
          val stringLength = reader.getInt32(8)
          String(bytes, 12, stringLength - 1)
        }
        ICC_TAG_TYPE_SIG -> IccReader.getStringFromInt32(reader.getInt32(8))
        ICC_TAG_TYPE_MEAS -> {
          val observerType = reader.getInt32(8)
          val x = reader.getS15Fixed16(12)
          val y = reader.getS15Fixed16(16)
          val z = reader.getS15Fixed16(20)
          val geometryType = reader.getInt32(24)
          val flare = reader.getS15Fixed16(28)
          val illuminantType = reader.getInt32(32)
          val observerString: String
          observerString = when (observerType) {
            0 -> "Unknown"
            1 -> "1931 2\u00B0"
            2 -> "1964 10\u00B0"
            else -> String.format("Unknown %d", observerType)
          }
          val geometryString: String
          geometryString = when (geometryType) {
            0 -> "Unknown"
            1 -> "0/45 or 45/0"
            2 -> "0/d or d/0"
            else -> String.format("Unknown %d", observerType)
          }
          val illuminantString: String
          illuminantString = when (illuminantType) {
            0 -> "unknown"
            1 -> "D50"
            2 -> "D65"
            3 -> "D93"
            4 -> "F2"
            5 -> "D55"
            6 -> "A"
            7 -> "Equi-Power (E)"
            8 -> "F8"
            else -> String.format("Unknown %d", illuminantType)
          }
          val format = DecimalFormat("0.###")
          String.format("%s Observer, Backing (%s, %s, %s), Geometry %s, Flare %d%%, Illuminant %s",
            observerString, format.format(x.toDouble()), format.format(y.toDouble()), format.format(z.toDouble()), geometryString, (flare * 100).roundToInt(), illuminantString)
        }
        ICC_TAG_TYPE_XYZ_ARRAY -> {
          val res = StringBuilder()
          val format = DecimalFormat("0.####")
          val count = (bytes.size - 8) / 12
          var i = 0
          while (i < count) {
            val x = reader.getS15Fixed16(8 + i * 12)
            val y = reader.getS15Fixed16(8 + i * 12 + 4)
            val z = reader.getS15Fixed16(8 + i * 12 + 8)
            if (i > 0) res.append(", ")
            res.append("(").append(format.format(x.toDouble())).append(", ").append(format.format(y.toDouble())).append(", ").append(format.format(z.toDouble())).append(")")
            i++
          }
          res.toString()
        }
        ICC_TAG_TYPE_MLUC -> {
          val int1 = reader.getInt32(8)
          val res = StringBuilder()
          res.append(int1)
          //int int2 = reader.getInt32(12);
          //System.err.format("int1: %d, int2: %d\n", int1, int2);
          var i = 0
          while (i < int1) {
            val str: String = IccReader.getStringFromInt32(reader.getInt32(16 + i * 12))
            val len = reader.getInt32(16 + i * 12 + 4)
            val ofs = reader.getInt32(16 + i * 12 + 8)
            var name: String
            name = try {
              String(bytes, ofs, len, UTF_16BE)
            } catch (ex: UnsupportedEncodingException) {
              String(bytes, ofs, len)
            }
            res.append(" ").append(str).append("(").append(name).append(")")
            i++
          }
          res.toString()
        }
        ICC_TAG_TYPE_CURV -> {
          val num = reader.getInt32(8)
          val res = StringBuilder()
          var i = 0
          while (i < num) {
            if (i != 0) res.append(", ")
            res.append(formatDoubleAsString(reader.getUInt16(12 + i * 2).toFloat() / 65535.0, 7, false))
            i++
          }
          res.toString()
        }
        else -> String.format("%s (0x%08X): %d bytes", IccReader.getStringFromInt32(iccTagType), iccTagType, bytes.size)
      }
    } catch (e: IOException) { // TODO decode these values during IccReader.extract so we can report any errors at that time
      // It is convention to return null if a description cannot be formulated.
      // If an error is to be reported, it should be done during the extraction process.
      null
    }
  }

  private val renderingIntentDescription: String?
    get() = getIndexedDescription(IccDirectory.TAG_RENDERING_INTENT,
      "Perceptual",
      "Media-Relative Colorimetric",
      "Saturation",
      "ICC-Absolute Colorimetric")

  // Because Java doesn't allow switching on string values, create an integer from the first four chars
// and switch on that instead.
  private val platformDescription: String?
    get() {
      val str = _directory.getString(IccDirectory.TAG_PLATFORM) ?: return null
      // Because Java doesn't allow switching on string values, create an integer from the first four chars
// and switch on that instead.
      val i: Int
      i = try {
        getInt32FromString(str)
      } catch (e: IOException) {
        return str
      }
      return when (i) {
        0x4150504C -> "Apple Computer, Inc."
        0x4D534654 -> "Microsoft Corporation"
        0x53474920 -> "Silicon Graphics, Inc."
        0x53554E57 -> "Sun Microsystems, Inc."
        0x54474E54 -> "Taligent, Inc."
        else -> String.format("Unknown (%s)", str)
      }
    }

  // Because Java doesn't allow switching on string values, create an integer from the first four chars
  // and switch on that instead.
  private val profileClassDescription: String?
    get() {
      val str = _directory.getString(IccDirectory.TAG_PROFILE_CLASS) ?: return null
      // Because Java doesn't allow switching on string values, create an integer from the first four chars
      // and switch on that instead.
      val i: Int
      i = try {
        getInt32FromString(str)
      } catch (e: IOException) {
        return str
      }
      return when (i) {
        0x73636E72 -> "Input Device"
        0x6D6E7472 -> "Display Device"
        0x70727472 -> "Output Device"
        0x6C696E6B -> "DeviceLink"
        0x73706163 -> "ColorSpace Conversion"
        0x61627374 -> "Abstract"
        0x6E6D636C -> "Named Color"
        else -> String.format("Unknown (%s)", str)
      }
    }

  private val profileVersionDescription: String?
    get() {
      val value = _directory.getInteger(IccDirectory.TAG_PROFILE_VERSION) ?: return null
      val m = value and -0x1000000 shr 24
      val r = value and 0x00F00000 shr 20
      val R = value and 0x000F0000 shr 16
      return "%d.%d.%d".format(m, r, R)
    }

  companion object {
    private const val ICC_TAG_TYPE_TEXT = 0x74657874
    private const val ICC_TAG_TYPE_DESC = 0x64657363
    private const val ICC_TAG_TYPE_SIG = 0x73696720
    private const val ICC_TAG_TYPE_MEAS = 0x6D656173
    private const val ICC_TAG_TYPE_XYZ_ARRAY = 0x58595A20
    private const val ICC_TAG_TYPE_MLUC = 0x6d6c7563
    private const val ICC_TAG_TYPE_CURV = 0x63757276
    fun formatDoubleAsString(value: Double, precision: Int, zeroes: Boolean): String {
      if (precision < 1) {
        return "${value.roundToInt()}"
      }
      var intPart = abs(value.toLong())
      var rest: Long = ((abs(value) - intPart) * 10.0.pow(precision.toDouble())).roundToLong()
      val restKept = rest
      var res = ""
      var cour: Byte
      for (i in precision downTo 1) {
        cour = abs(rest % 10).toByte()
        rest /= 10
        if (res.isNotEmpty() || zeroes || cour.toInt() != 0 || i == 1) {
          res = "$cour$res"
        }
      }
      intPart += rest
      val isNegative = value < 0 && (intPart != 0L || restKept != 0L)
      return "${if (isNegative) "-" else ""}$intPart.$res"
    }

    @Throws(IOException::class)
    private fun getInt32FromString(string: String): Int {
      val bytes = string.toByteArray()
      return ByteArrayReader(bytes).getInt32(0)
    }
  }
}
