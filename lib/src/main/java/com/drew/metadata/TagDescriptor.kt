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
package com.drew.metadata

import java.io.UnsupportedEncodingException
import java.math.RoundingMode
import java.nio.charset.Charset
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.exp
import kotlin.math.ln
import kotlin.math.roundToLong

/**
 * Base class for all tag descriptor classes.  Implementations are responsible for
 * providing the human-readable string representation of tag values stored in a directory.
 * The directory is provided to the tag descriptor via its constructor.
 *
 * @author Drew Noakes https://drewnoakes.com
 */
open class TagDescriptor<T : Directory>(@JvmField protected val _directory: T) {
  /**
   * Returns a descriptive value of the specified tag for this image.
   * Where possible, known values will be substituted here in place of the raw
   * tokens actually kept in the metadata segment.  If no substitution is
   * available, the value provided by `getString(tagType)` will be returned.
   *
   * @param tagType the tag to find a description for
   * @return a description of the image's value for the specified tag, or
   * `null` if the tag hasn't been defined.
   */
  open fun getDescription(tagType: Int): String? {
    val obj = _directory.getObject(tagType) ?: return null
    // special presentation for long arrays
    if (obj.javaClass.isArray) {
      val length = java.lang.reflect.Array.getLength(obj)
      if (length > 16) {
        return "[%d values]".format(length)
      }
    }
    return if (obj is Date) { // Produce a date string having a format that includes the offset in form "+00:00"
      SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy")
        .format(obj)
        .replace("([0-9]{2} [^ ]+)$".toRegex(), ":$1")
    } else _directory.getString(tagType)
    // no special handling required, so use default conversion to a string
  }

  protected fun getVersionBytesDescription(tagType: Int, majorDigits: Int): String? {
    val values = _directory.getIntArray(tagType)
    return values?.let { convertBytesToVersionString(it, majorDigits) }
  }

  //TODO: descriptions was marked as "@NotNull but parts of the code pass in null. Check what is happening here.
  protected fun getIndexedDescription(tagType: Int, vararg descriptions: String?): String? {
    return getIndexedDescription(tagType, 0, *descriptions)
  }

  //TODO: descriptions was marked as "@NotNull but parts of the code pass in null. Check what is happening here.
  protected fun getIndexedDescription(tagType: Int, baseIndex: Int, vararg descriptions: String?): String? {
    val index = _directory.getLongObject(tagType) ?: return null
    val arrayIndex = index - baseIndex
    if (arrayIndex >= 0 && arrayIndex < descriptions.size.toLong()) {
      val description = descriptions[arrayIndex.toInt()]
      if (description != null) return description
    }
    return "Unknown ($index)"
  }

  protected fun getByteLengthDescription(tagType: Int): String? {
    val bytes = _directory.getByteArray(tagType) ?: return null
    return "(%d byte%s)".format(bytes.size, if (bytes.size == 1) "" else "s")
  }

  protected fun getSimpleRational(tagType: Int): String? {
    val value = _directory.getRational(tagType) ?: return null
    return value.toSimpleString(true)
  }

  protected fun getDecimalRational(tagType: Int, decimalPlaces: Int): String? {
    val value = _directory.getRational(tagType) ?: return null
    return "%.${decimalPlaces}f".format(value.toDouble())
  }

  protected fun getFormattedInt(tagType: Int, format: String): String? {
    val value = _directory.getInteger(tagType) ?: return null
    return format.format(value)
  }

  protected fun getFormattedFloat(tagType: Int, format: String): String? {
    val value = _directory.getFloatObject(tagType) ?: return null
    return format.format(value)
  }

  protected fun getFormattedString(tagType: Int, format: String): String? {
    val value = _directory.getString(tagType) ?: return null
    return format.format(value)
  }

  protected fun getEpochTimeDescription(tagType: Int): String? { // TODO have observed a byte[8] here which is likely some kind of date (ticks as long?)
    val value = _directory.getLongObject(tagType) ?: return null
    return Date(value).toString()
  }

  /**
   * LSB first. Labels may be null, a String, or a String[2] with (low label,high label) values.
   */
  protected fun getBitFlagDescription(tagType: Int, vararg labels: Any?): String? {
    var value = _directory.getInteger(tagType) ?: return null
    val parts: MutableList<String> = ArrayList()
    var bitIndex = 0
    while (labels.size > bitIndex) {
      val labelObj = labels[bitIndex]
      val isBitSet = value and 1 == 1
      if (labelObj is Array<*>) {
        assert(labelObj.size == 2)
        parts.add(labelObj[if (isBitSet) 1 else 0] as String)
      } else if (isBitSet && labelObj is String) {
        parts.add(labelObj)
      }
      value = value shr 1
      bitIndex++
    }
    return parts.joinToString(", ")
  }

  protected fun get7BitStringFromBytes(tagType: Int): String? {
    val bytes = _directory.getByteArray(tagType) ?: return null
    var length = bytes.size
    for (index in bytes.indices) {
      val i: Int = bytes[index].toInt() and 0xFF
      if (i == 0 || i > 0x7F) {
        length = index
        break
      }
    }
    return String(bytes, 0, length)
  }

  protected fun getStringFromBytes(tag: Int, cs: Charset): String? {
    val values = _directory.getByteArray(tag) ?: return null
    return try {
      String(values, cs).trim { it <= ' ' }
    } catch (e: UnsupportedEncodingException) {
      null
    }
  }

  protected fun getRationalOrDoubleString(tagType: Int): String? {
    val rational = _directory.getRational(tagType)
    if (rational != null) return rational.toSimpleString(true)
    val d = _directory.getDoubleObject(tagType)
    if (d != null) {
      val format = DecimalFormat("0.###")
      return format.format(d)
    }
    return null
  }

  protected fun getLensSpecificationDescription(tag: Int): String? {
    val values = _directory.getRationalArray(tag)
    if (values == null || values.size != 4 || values[0].isZero && values[2].isZero) return null
    val sb = StringBuilder()
    if (values[0].equals(values[1])) sb.append(values[0].toSimpleString(true)).append("mm") else sb.append(values[0].toSimpleString(true)).append('-').append(values[1].toSimpleString(true)).append("mm")
    if (!values[2].isZero) {
      sb.append(' ')
      val format = DecimalFormat("0.0")
      format.roundingMode = RoundingMode.HALF_UP
      if (values[2].equals(values[3])) sb.append(getFStopDescription(values[2].toDouble())) else sb.append("f/").append(format.format(values[2].toDouble())).append('-').append(format.format(values[3].toDouble()))
    }
    return sb.toString()
  }

  protected fun getOrientationDescription(tag: Int): String? {
    return getIndexedDescription(tag, 1,
      "Top, left side (Horizontal / normal)",
      "Top, right side (Mirror horizontal)",
      "Bottom, right side (Rotate 180)",
      "Bottom, left side (Mirror vertical)",
      "Left side, top (Mirror horizontal and rotate 270 CW)",
      "Right side, top (Rotate 90 CW)",
      "Right side, bottom (Mirror horizontal and rotate 90 CW)",
      "Left side, bottom (Rotate 270 CW)")
  }

  protected fun getShutterSpeedDescription(tag: Int): String? { // I believe this method to now be stable, but am leaving some alternative snippets of
// code in here, to assist anyone who's looking into this (given that I don't have a public CVS).
//        float apexValue = _directory.getFloat(ExifSubIFDDirectory.TAG_SHUTTER_SPEED);
//        int apexPower = (int)Math.pow(2.0, apexValue);
//        return "1/" + apexPower + " sec";
// TODO test this method
// thanks to Mark Edwards for spotting and patching a bug in the calculation of this
// description (spotted bug using a Canon EOS 300D)
// thanks also to Gli Blr for spotting this bug
    val apexValue = _directory.getFloatObject(tag) ?: return null
    return if (apexValue <= 1) {
      val apexPower = (1 / exp(apexValue * ln(2.0))).toFloat()
      val apexPower10 = (apexPower.toDouble() * 10.0).roundToLong()
      val fApexPower = apexPower10.toFloat() / 10.0f
      val format = DecimalFormat("0.##")
      format.roundingMode = RoundingMode.HALF_UP
      format.format(fApexPower.toDouble()) + " sec"
    } else {
      val apexPower = exp(apexValue * ln(2.0)).toInt()
      "1/$apexPower sec"
    }
    /*
        // This alternative implementation offered by Bill Richards
        // TODO determine which is the correct / more-correct implementation
        double apexValue = _directory.getDouble(ExifSubIFDDirectory.TAG_SHUTTER_SPEED);
        double apexPower = Math.pow(2.0, apexValue);

        StringBuffer sb = new StringBuffer();
        if (apexPower > 1)
            apexPower = Math.floor(apexPower);

        if (apexPower < 1) {
            sb.append((int)Math.round(1/apexPower));
        } else {
            sb.append("1/");
            sb.append((int)apexPower);
        }
        sb.append(" sec");
        return sb.toString();
*/
  }

  // EXIF UserComment, GPSProcessingMethod and GPSAreaInformation
  protected fun getEncodedTextDescription(tagType: Int): String? {
    val commentBytes = _directory.getByteArray(tagType) ?: return null
    if (commentBytes.isEmpty()) return ""
    return try {
      if (commentBytes.size >= 10) {
        val firstTenBytesString = String(commentBytes, 0, 10)
        // try each encoding name
        for ((encodingName, charset) in encodingMap) {
          val charset = Charset.forName(charset)
          if (firstTenBytesString.startsWith(encodingName)) { // skip any null or blank characters commonly present after the encoding name, up to a limit of 10 from the start
            for (j in encodingName.length..9) {
              val b = commentBytes[j]
              if (b != '\u0000'.toByte() && b != ' '.toByte()) return String(commentBytes, j, commentBytes.size - j, charset).trim { it <= ' ' }
            }
            return String(commentBytes, 10, commentBytes.size - 10, charset).trim { it <= ' ' }
          }
        }
      }
      // special handling fell through, return a plain string representation
      String(commentBytes, Charset.forName(System.getProperty("file.encoding"))).trim { it <= ' ' }
    } catch (ex: UnsupportedEncodingException) {
      null
    }
  }

  companion object {
    val encodingMap: Map<String, String> by lazy { mapOf("ASCII" to System.getProperty("file.encoding"), "UNICODE" to "UTF-16LE", "JIS" to "Shift-JIS") }

    /**
     * Takes a series of 4 bytes from the specified offset, and converts these to a
     * well-known version number, where possible.
     *
     *
     * Two different formats are processed:
     *
     *  * [30 32 31 30] -&gt; 2.10
     *  * [0 1 0 0] -&gt; 1.00
     *
     *
     * @param components  the four version values
     * @param majorDigits the number of components to be
     * @return the version as a string of form "2.10" or null if the argument cannot be converted
     */
    fun convertBytesToVersionString(components: IntArray?, majorDigits: Int): String? {
      if (components == null) return null
      val version = StringBuilder()
      var i = 0
      while (i < 4 && i < components.size) {
        if (i == majorDigits) version.append('.')
        var c = components[i].toChar()
        if (c < '0') c += '0'.toInt()
        if (i == 0 && c == '0') {
          i++
          continue
        }
        version.append(c)
        i++
      }
      return version.toString()
    }

    @JvmStatic
    protected fun getFStopDescription(fStop: Double): String? {
      val format = DecimalFormat("0.0")
      format.roundingMode = RoundingMode.HALF_UP
      return "f/" + format.format(fStop)
    }

    @JvmStatic
    protected fun getFocalLengthDescription(mm: Double): String? {
      val format = DecimalFormat("0.#")
      format.roundingMode = RoundingMode.HALF_UP
      return format.format(mm) + " mm"
    }
  }
}
