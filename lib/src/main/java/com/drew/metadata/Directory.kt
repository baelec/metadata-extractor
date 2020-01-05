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

import com.drew.lang.Rational
import com.drew.lang.annotations.SuppressWarnings
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset
import java.text.DateFormat
import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern
import kotlin.experimental.and

/**
 * Abstract base class for all directory implementations, having methods for getting and setting tag values of various
 * data types.
 *
 * @author Drew Noakes https://drewnoakes.com
 */
abstract class Directory protected constructor() {
  /** Map of values hashed by type identifiers.  */
  protected val _tagMap: MutableMap<Int, Any> = HashMap()
  /**
   * A convenient list holding tag values in the order in which they were stored.
   * This is used for creation of an iterator, and for counting the number of
   * defined tags.
   */
  protected val _definedTagList: MutableCollection<Tag> = ArrayList()
  private val _errorList: MutableCollection<String> = ArrayList(4)
  /** The descriptor used to interpret tag values.  */
  protected var _descriptor: TagDescriptor<*>? = null
  var parent: Directory? = null
    private set
  // ABSTRACT METHODS
  /**
   * Provides the name of the directory, for display purposes.  E.g. `Exif`
   *
   * @return the name of the directory
   */
  abstract val name: String

  /**
   * Provides the map of tag names, hashed by tag type identifier.
   *
   * @return the map of tag names
   */
  protected abstract val tagNameMap: HashMap<Int, String>
  // VARIOUS METHODS
  /**
   * Gets a value indicating whether the directory is empty, meaning it contains no errors and no tag values.
   */
  val isEmpty: Boolean
    get() = _errorList.isEmpty() && _definedTagList.isEmpty()

  /**
   * Indicates whether the specified tag type has been set.
   *
   * @param tagType the tag type to check for
   * @return true if a value exists for the specified tag type, false if not
   */
  fun containsTag(tagType: Int): Boolean {
    return _tagMap.containsKey(Integer.valueOf(tagType))
  }

  /**
   * Returns an Iterator of Tag instances that have been set in this Directory.
   *
   * @return an Iterator of Tag instances
   */
  val tags: Collection<Tag>
    get() = Collections.unmodifiableCollection(_definedTagList)

  /**
   * Returns the number of tags set in this Directory.
   *
   * @return the number of tags set in this Directory
   */
  val tagCount: Int
    get() = _definedTagList.size

  /**
   * Sets the descriptor used to interpret tag values.
   *
   * @param descriptor the descriptor used to interpret tag values
   */
  fun setDescriptor(descriptor: TagDescriptor<*>) {
    _descriptor = descriptor
  }

  /**
   * Registers an error message with this directory.
   *
   * @param message an error message.
   */
  fun addError(message: String) {
    _errorList.add(message)
  }

  /**
   * Gets a value indicating whether this directory has any error messages.
   *
   * @return true if the directory contains errors, otherwise false
   */
  fun hasErrors(): Boolean {
    return _errorList.isNotEmpty()
  }

  /**
   * Used to iterate over any error messages contained in this directory.
   *
   * @return an iterable collection of error message strings.
   */
  val errors: Iterable<String>
    get() = Collections.unmodifiableCollection(_errorList)

  /** Returns the count of error messages in this directory.  */
  val errorCount: Int
    get() = _errorList.size

  fun setParent(parent: Directory) {
    this.parent = parent
  }
  // TAG SETTERS
  /**
   * Sets an `int` value for the specified tag.
   *
   * @param tagType the tag's value as an int
   * @param value   the value for the specified tag as an int
   */
  fun setInt(tagType: Int, value: Int) {
    setObject(tagType, value)
  }

  /**
   * Sets an `int[]` (array) for the specified tag.
   *
   * @param tagType the tag identifier
   * @param ints    the int array to store
   */
  fun setIntArray(tagType: Int, ints: IntArray) {
    setObjectArray(tagType, ints)
  }

  /**
   * Sets a `float` value for the specified tag.
   *
   * @param tagType the tag's value as an int
   * @param value   the value for the specified tag as a float
   */
  fun setFloat(tagType: Int, value: Float) {
    setObject(tagType, value)
  }

  /**
   * Sets a `float[]` (array) for the specified tag.
   *
   * @param tagType the tag identifier
   * @param floats  the float array to store
   */
  fun setFloatArray(tagType: Int, floats: FloatArray) {
    setObjectArray(tagType, floats)
  }

  /**
   * Sets a `double` value for the specified tag.
   *
   * @param tagType the tag's value as an int
   * @param value   the value for the specified tag as a double
   */
  fun setDouble(tagType: Int, value: Double) {
    setObject(tagType, value)
  }

  /**
   * Sets a `double[]` (array) for the specified tag.
   *
   * @param tagType the tag identifier
   * @param doubles the double array to store
   */
  fun setDoubleArray(tagType: Int, doubles: DoubleArray) {
    setObjectArray(tagType, doubles)
  }

  /**
   * Sets a `StringValue` value for the specified tag.
   *
   * @param tagType the tag's value as an int
   * @param value   the value for the specified tag as a StringValue
   */
  fun setStringValue(tagType: Int, value: StringValue) {
    setObject(tagType, value)
  }

  /**
   * Sets a `String` value for the specified tag.
   *
   * @param tagType the tag's value as an int
   * @param value   the value for the specified tag as a String
   */
  fun setString(tagType: Int, value: String) {
    setObject(tagType, value)
  }

  /**
   * Sets a `String[]` (array) for the specified tag.
   *
   * @param tagType the tag identifier
   * @param strings the String array to store
   */
  fun setStringArray(tagType: Int, strings: Array<String?>) {
    setObjectArray(tagType, strings)
  }

  /**
   * Sets a `StringValue[]` (array) for the specified tag.
   *
   * @param tagType the tag identifier
   * @param strings the StringValue array to store
   */
  fun setStringValueArray(tagType: Int, strings: Array<StringValue?>) {
    setObjectArray(tagType, strings)
  }

  /**
   * Sets a `boolean` value for the specified tag.
   *
   * @param tagType the tag's value as an int
   * @param value   the value for the specified tag as a boolean
   */
  fun setBoolean(tagType: Int, value: Boolean) {
    setObject(tagType, value)
  }

  /**
   * Sets a `long` value for the specified tag.
   *
   * @param tagType the tag's value as an int
   * @param value   the value for the specified tag as a long
   */
  fun setLong(tagType: Int, value: Long) {
    setObject(tagType, value)
  }

  /**
   * Sets a `java.util.Date` value for the specified tag.
   *
   * @param tagType the tag's value as an int
   * @param value   the value for the specified tag as a java.util.Date
   */
  fun setDate(tagType: Int, value: Date) {
    setObject(tagType, value)
  }

  /**
   * Sets a `Rational` value for the specified tag.
   *
   * @param tagType  the tag's value as an int
   * @param rational rational number
   */
  fun setRational(tagType: Int, rational: Rational) {
    setObject(tagType, rational)
  }

  /**
   * Sets a `Rational[]` (array) for the specified tag.
   *
   * @param tagType   the tag identifier
   * @param rationals the Rational array to store
   */
  fun setRationalArray(tagType: Int, rationals: Array<Rational?>) {
    setObjectArray(tagType, rationals)
  }

  /**
   * Sets a `byte[]` (array) for the specified tag.
   *
   * @param tagType the tag identifier
   * @param bytes   the byte array to store
   */
  open fun setByteArray(tagType: Int, bytes: ByteArray) {
    setObjectArray(tagType, bytes)
  }

  /**
   * Sets a `Object` for the specified tag.
   *
   * @param tagType the tag's value as an int
   * @param value   the value for the specified tag
   * @throws NullPointerException if value is `null`
   */
  open fun setObject(tagType: Int, value: Any) {
    if (!_tagMap.containsKey(Integer.valueOf(tagType))) {
      _definedTagList.add(Tag(tagType, this))
    }
    //        else {
//            final Object oldValue = _tagMap.get(tagType);
//            if (!oldValue.equals(value))
//                addError(String.format("Overwritten tag 0x%s (%s).  Old=%s, New=%s", Integer.toHexString(tagType), getTagName(tagType), oldValue, value));
//        }
    _tagMap[tagType] = value
  }

  /**
   * Sets an array `Object` for the specified tag.
   *
   * @param tagType the tag's value as an int
   * @param array   the array of values for the specified tag
   */
  open fun setObjectArray(tagType: Int, array: Any) { // for now, we don't do anything special -- this method might be a candidate for removal once the dust settles
    setObject(tagType, array)
  }
  // TAG GETTERS
  /**
   * Returns the specified tag's value as an int, if possible.  Every attempt to represent the tag's value as an int
   * is taken.  Here is a list of the action taken depending upon the tag's original type:
   *
   *  *  int - Return unchanged.
   *  *  Number - Return an int value (real numbers are truncated).
   *  *  Rational - Truncate any fractional part and returns remaining int.
   *  *  String - Attempt to parse string as an int.  If this fails, convert the char[] to an int (using shifts and OR).
   *  *  Rational[] - Return int value of first item in array.
   *  *  byte[] - Return int value of first item in array.
   *  *  int[] - Return int value of first item in array.
   *
   *
   * @throws MetadataException if no value exists for tagType or if it cannot be converted to an int.
   */
  @Throws(MetadataException::class)
  fun getInt(tagType: Int): Int {
    val integer = getInteger(tagType)
    if (integer != null) return integer
    val o = getObject(tagType)
      ?: throw MetadataException("Tag '" + getTagName(tagType) + "' has not been set -- check using containsTag() first")
    throw MetadataException("Tag '" + tagType + "' cannot be converted to int.  It is of type '" + o.javaClass + "'.")
  }

  /**
   * Returns the specified tag's value as an Integer, if possible.  Every attempt to represent the tag's value as an
   * Integer is taken.  Here is a list of the action taken depending upon the tag's original type:
   *
   *  *  int - Return unchanged
   *  *  Number - Return an int value (real numbers are truncated)
   *  *  Rational - Truncate any fractional part and returns remaining int
   *  *  String - Attempt to parse string as an int.  If this fails, convert the char[] to an int (using shifts and OR)
   *  *  Rational[] - Return int value of first item in array if length &gt; 0
   *  *  byte[] - Return int value of first item in array if length &gt; 0
   *  *  int[] - Return int value of first item in array if length &gt; 0
   *
   *
   * If the value is not found or cannot be converted to int, `null` is returned.
   */
  fun getInteger(tagType: Int): Int? {
    val o = getObject(tagType) ?: return null
    if (o is Number) {
      return o.toInt()
    } else if (o is String || o is StringValue) {
      return try {
        o.toString().toInt()
      } catch (nfe: NumberFormatException) { // convert the char array to an int
        val s = o.toString()
        val bytes = s.toByteArray()
        var value: Long = 0
        for (aByte in bytes) {
          value = value shl 8
          value += (aByte and 0xff.toByte()).toLong()
        }
        value.toInt()
      }
    } else if (o is Array<*>) {
      val rationals = o as Array<Rational>
      if (rationals.size == 1) return rationals[0].toInt()
    } else if (o is ByteArray) {
      if (o.size == 1) return o[0].toInt()
    } else if (o is IntArray) {
      if (o.size == 1) return o[0]
    } else if (o is ShortArray) {
      if (o.size == 1) return o[0].toInt()
    }
    return null
  }

  /**
   * Gets the specified tag's value as a String array, if possible.  Only supported
   * where the tag is set as StringValue[], String[], StringValue, String, int[], byte[] or Rational[].
   *
   * @param tagType the tag identifier
   * @return the tag's value as an array of Strings. If the value is unset or cannot be converted, `null` is returned.
   */
  fun getStringArray(tagType: Int): Array<String>? {
    val o = getObject(tagType) ?: return null
    return when {
      o is String -> arrayOf(o)
      o is StringValue -> arrayOf(o.toString())
      o is Array<*> && o.javaClass.componentType == String::class -> o as Array<String>
      o is Array<*> && o.javaClass.componentType == Rational::class  -> {
        val rationals = o as Array<Rational>
        rationals.indices.map { rationals[it].toSimpleString(false) }.toTypedArray()
      }
      o is Array<*> -> {
        o.indices.map { o[it].toString() }.toTypedArray()
      }
      else -> null
    }
  }

  /**
   * Gets the specified tag's value as a StringValue array, if possible.
   * Only succeeds if the tag is set as StringValue[], or StringValue.
   *
   * @param tagType the tag identifier
   * @return the tag's value as an array of StringValues. If the value is unset or cannot be converted, `null` is returned.
   */
  fun getStringValueArray(tagType: Int): Array<StringValue>? {
    val o = getObject(tagType) ?: return null
    if (o is Array<*>) return o as Array<StringValue>
    return if (o is StringValue) arrayOf(o) else null
  }

  /**
   * Gets the specified tag's value as an int array, if possible.  Only supported
   * where the tag is set as String, Integer, int[], byte[] or Rational[].
   *
   * @param tagType the tag identifier
   * @return the tag's value as an int array
   */
  fun getIntArray(tagType: Int): IntArray? {
    val o = getObject(tagType) ?: return null
    if (o is IntArray) return o
    else if (o is Array<*>) {
      val rationals = o as Array<Rational>
      val ints = IntArray(rationals.size)
      for (i in ints.indices) {
        ints[i] = rationals[i].toInt()
      }
      return ints
    }
    else if (o is ShortArray) {
      val ints = IntArray(o.size)
      for (i in o.indices) {
        ints[i] = o[i].toInt()
      }
      return ints
    }
    else if (o is ByteArray) {
      val ints = IntArray(o.size)
      for (i in o.indices) {
        ints[i] = o[i].toInt()
      }
      return ints
    }
    else if (o is CharSequence) {
      val ints = IntArray(o.length)
      for (i in 0 until o.length) {
        ints[i] = o[i].toInt()
      }
      return ints
    }
    return if (o is Int) intArrayOf(o) else null
  }

  /**
   * Gets the specified tag's value as an byte array, if possible.  Only supported
   * where the tag is set as String, Integer, int[], byte[] or Rational[].
   *
   * @param tagType the tag identifier
   * @return the tag's value as a byte array
   */
  fun getByteArray(tagType: Int): ByteArray? {
    when (val o = getObject(tagType)) {
      null -> {
        return null
      }
      is StringValue -> {
        return o.bytes
      }
      is Array<*> -> {
        val rationals = o as Array<Rational>
        val bytes = ByteArray(rationals.size)
        for (i in bytes.indices) {
          bytes[i] = rationals[i].toByte()
        }
        return bytes
      }
      is ByteArray -> {
        return o
      }
      is IntArray -> {
        val bytes = ByteArray(o.size)
        for (i in o.indices) {
          bytes[i] = o[i].toByte()
        }
        return bytes
      }
      is ShortArray -> {
        val bytes = ByteArray(o.size)
        for (i in o.indices) {
          bytes[i] = o[i].toByte()
        }
        return bytes
      }
      is CharSequence -> {
        val bytes = ByteArray(o.length)
        for (i in 0 until o.length) {
          bytes[i] = o[i].toByte()
        }
        return bytes
      }
      else -> return if (o is Int) byteArrayOf(o.toByte()) else null
    }
  }

  /** Returns the specified tag's value as a double, if possible.  */
  @Throws(MetadataException::class)
  fun getDouble(tagType: Int): Double {
    val value = getDoubleObject(tagType)
    if (value != null) return value
    val o = getObject(tagType)
      ?: throw MetadataException("Tag '" + getTagName(tagType) + "' has not been set -- check using containsTag() first")
    throw MetadataException("Tag '" + tagType + "' cannot be converted to a double.  It is of type '" + o.javaClass + "'.")
  }

  /** Returns the specified tag's value as a Double.  If the tag is not set or cannot be converted, `null` is returned.  */
  fun getDoubleObject(tagType: Int): Double? {
    val o = getObject(tagType) ?: return null
    if (o is String || o is StringValue) {
      return try {
        o.toString().toDouble()
      } catch (nfe: NumberFormatException) {
        null
      }
    }
    return if (o is Number) o.toDouble() else null
  }

  /** Returns the specified tag's value as a float, if possible.  */
  @Throws(MetadataException::class)
  fun getFloat(tagType: Int): Float {
    val value = getFloatObject(tagType)
    if (value != null) return value
    val o = getObject(tagType)
      ?: throw MetadataException("Tag '" + getTagName(tagType) + "' has not been set -- check using containsTag() first")
    throw MetadataException("Tag '" + tagType + "' cannot be converted to a float.  It is of type '" + o.javaClass + "'.")
  }

  /** Returns the specified tag's value as a float.  If the tag is not set or cannot be converted, `null` is returned.  */
  fun getFloatObject(tagType: Int): Float? {
    val o = getObject(tagType) ?: return null
    if (o is String || o is StringValue) {
      return try {
        o.toString().toFloat()
      } catch (nfe: NumberFormatException) {
        null
      }
    }
    return if (o is Number) o.toFloat() else null
  }

  /** Returns the specified tag's value as a long, if possible.  */
  @Throws(MetadataException::class)
  fun getLong(tagType: Int): Long {
    val value = getLongObject(tagType)
    if (value != null) return value
    val o = getObject(tagType)
      ?: throw MetadataException("Tag '" + getTagName(tagType) + "' has not been set -- check using containsTag() first")
    throw MetadataException("Tag '" + tagType + "' cannot be converted to a long.  It is of type '" + o.javaClass + "'.")
  }

  /** Returns the specified tag's value as a long.  If the tag is not set or cannot be converted, `null` is returned.  */
  fun getLongObject(tagType: Int): Long? {
    val o = getObject(tagType) ?: return null
    if (o is Number) return o.toLong()
    if (o is String || o is StringValue) {
      return try {
        o.toString().toLong()
      } catch (nfe: NumberFormatException) {
        null
      }
    } else if (o is Array<*>) {
      val rationals = o as Array<Rational>
      if (rationals.size == 1) return rationals[0].toLong()
    } else if (o is ByteArray) {
      if (o.size == 1) return o[0].toLong()
    } else if (o is IntArray) {
      if (o.size == 1) return o[0].toLong()
    } else if (o is ShortArray) {
      if (o.size == 1) return o[0].toLong()
    }
    return null
  }

  /** Returns the specified tag's value as a boolean, if possible.  */
  @Throws(MetadataException::class)
  fun getBoolean(tagType: Int): Boolean {
    val value = getBooleanObject(tagType)
    if (value != null) return value
    val o = getObject(tagType)
      ?: throw MetadataException("Tag '" + getTagName(tagType) + "' has not been set -- check using containsTag() first")
    throw MetadataException("Tag '" + tagType + "' cannot be converted to a boolean.  It is of type '" + o.javaClass + "'.")
  }

  /** Returns the specified tag's value as a boolean.  If the tag is not set or cannot be converted, `null` is returned.  */
  @SuppressWarnings(value = "NP_BOOLEAN_RETURN_NULL", justification = "keep API interface consistent")
  fun getBooleanObject(tagType: Int): Boolean? {
    val o = getObject(tagType) ?: return null
    if (o is Boolean) return o
    if (o is String || o is StringValue) {
      return try {
        java.lang.Boolean.getBoolean(o.toString())
      } catch (nfe: NumberFormatException) {
        null
      }
    }
    return if (o is Number) o.toDouble() != 0.0 else null
  }

  /**
   * Returns the specified tag's value as a java.util.Date.  If the value is unset or cannot be converted, `null` is returned.
   *
   *
   * If the underlying value is a [String], then attempts will be made to parse the string as though it is in
   * the GMT [TimeZone].  If the [TimeZone] is known, call the overload that accepts one as an argument.
   */
  fun getDate(tagType: Int): Date? {
    return getDate(tagType, null, null)
  }

  /**
   * Returns the specified tag's value as a java.util.Date.  If the value is unset or cannot be converted, `null` is returned.
   *
   *
   * If the underlying value is a [String], then attempts will be made to parse the string as though it is in
   * the [TimeZone] represented by the `timeZone` parameter (if it is non-null).  Note that this parameter
   * is only considered if the underlying value is a string and it has no time zone information, otherwise it has no effect.
   */
  fun getDate(tagType: Int, timeZone: TimeZone?): Date? {
    return getDate(tagType, null, timeZone)
  }

  /**
   * Returns the specified tag's value as a java.util.Date.  If the value is unset or cannot be converted, `null` is returned.
   *
   *
   * If the underlying value is a [String], then attempts will be made to parse the string as though it is in
   * the [TimeZone] represented by the `timeZone` parameter (if it is non-null).  Note that this parameter
   * is only considered if the underlying value is a string and it has no time zone information, otherwise it has no effect.
   * In addition, the `subsecond` parameter, which specifies the number of digits after the decimal point in the seconds,
   * is set to the returned Date. This parameter is only considered if the underlying value is a string and is has
   * no subsecond information, otherwise it has no effect.
   *
   * @param tagType the tag identifier
   * @param subsecond the subsecond value for the Date
   * @param timeZone the time zone to use
   * @return a Date representing the time value
   */
  fun getDate(tagType: Int, subsecond: String?, timeZone: TimeZone?): Date? {
    var subsecond = subsecond
    var timeZone = timeZone
    val o = getObject(tagType)
    if (o is Date) return o
    var date: Date? = null
    if (o is String || o is StringValue) { // This seems to cover all known Exif and Xmp date strings
// Note that "    :  :     :  :  " is a valid date string according to the Exif spec (which means 'unknown date'): http://www.awaresystems.be/imaging/tiff/tifftags/privateifd/exif/datetimeoriginal.html
      val datePatterns = arrayOf(
        "yyyy:MM:dd HH:mm:ss",
        "yyyy:MM:dd HH:mm",
        "yyyy-MM-dd HH:mm:ss",
        "yyyy-MM-dd HH:mm",
        "yyyy.MM.dd HH:mm:ss",
        "yyyy.MM.dd HH:mm",
        "yyyy-MM-dd'T'HH:mm:ss",
        "yyyy-MM-dd'T'HH:mm",
        "yyyy-MM-dd",
        "yyyy-MM",
        "yyyyMMdd",  // as used in IPTC data
        "yyyy")
      var dateString = o.toString()
      // if the date string has subsecond information, it supersedes the subsecond parameter
      val subsecondPattern = Pattern.compile("(\\d\\d:\\d\\d:\\d\\d)(\\.\\d+)")
      val subsecondMatcher = subsecondPattern.matcher(dateString)
      if (subsecondMatcher.find()) {
        subsecond = subsecondMatcher.group(2).substring(1)
        dateString = subsecondMatcher.replaceAll("$1")
      }
      // if the date string has time zone information, it supersedes the timeZone parameter
      val timeZonePattern = Pattern.compile("(Z|[+-]\\d\\d:\\d\\d)$")
      val timeZoneMatcher = timeZonePattern.matcher(dateString)
      if (timeZoneMatcher.find()) {
        timeZone = TimeZone.getTimeZone("GMT" + timeZoneMatcher.group().replace("Z".toRegex(), ""))
        dateString = timeZoneMatcher.replaceAll("")
      }
      for (datePattern in datePatterns) {
        try {
          val parser: DateFormat = SimpleDateFormat(datePattern)
          if (timeZone != null) parser.timeZone = timeZone else parser.timeZone = TimeZone.getTimeZone("GMT") // don't interpret zone time
          date = parser.parse(dateString)
          break
        } catch (ex: ParseException) { // simply try the next pattern
        }
      }
    }
    if (date == null) return null
    return if (subsecond == null) date else try {
      val millisecond = (".$subsecond".toDouble() * 1000).toInt()
      if (millisecond in 0..999) {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar[Calendar.MILLISECOND] = millisecond
        return calendar.time
      }
      date
    } catch (e: NumberFormatException) {
      date
    }
  }

  /** Returns the specified tag's value as a Rational.  If the value is unset or cannot be converted, `null` is returned.  */
  fun getRational(tagType: Int): Rational? {
    val o = getObject(tagType) ?: return null
    if (o is Rational) return o
    if (o is Int) return Rational(o.toLong(), 1)
    return if (o is Long) Rational(o, 1) else null
    // NOTE not doing conversions for real number types
  }

  /** Returns the specified tag's value as an array of Rational.  If the value is unset or cannot be converted, `null` is returned.  */
  fun getRationalArray(tagType: Int): Array<Rational>? {
    val o = getObject(tagType) ?: return null
    return if (o is Array<*>) o as Array<Rational> else null
  }

  /**
   * Returns the specified tag's value as a String.  This value is the 'raw' value.  A more presentable decoding
   * of this value may be obtained from the corresponding Descriptor.
   *
   * @return the String representation of the tag's value, or
   * `null` if the tag hasn't been defined.
   */
  fun getString(tagType: Int): String? {
    val o = getObject(tagType) ?: return null
    if (o is Rational) return o.toSimpleString(true)
    if (o.javaClass.isArray) { // handle arrays of objects and primitives
      val arrayLength = java.lang.reflect.Array.getLength(o)
      val componentType = o.javaClass.componentType
      val string = StringBuilder()
      if (Any::class.java.isAssignableFrom(componentType)) { // object array
        for (i in 0 until arrayLength) {
          if (i != 0) string.append(' ')
          string.append(java.lang.reflect.Array.get(o, i).toString())
        }
      } else if (componentType.name == "int") {
        for (i in 0 until arrayLength) {
          if (i != 0) string.append(' ')
          string.append(java.lang.reflect.Array.getInt(o, i))
        }
      } else if (componentType.name == "short") {
        for (i in 0 until arrayLength) {
          if (i != 0) string.append(' ')
          string.append(java.lang.reflect.Array.getShort(o, i).toInt())
        }
      } else if (componentType.name == "long") {
        for (i in 0 until arrayLength) {
          if (i != 0) string.append(' ')
          string.append(java.lang.reflect.Array.getLong(o, i))
        }
      } else if (componentType.name == "float") {
        val format = DecimalFormat(_floatFormatPattern)
        for (i in 0 until arrayLength) {
          if (i != 0) string.append(' ')
          val s = format.format(java.lang.reflect.Array.getFloat(o, i).toDouble())
          string.append(if (s == "-0") "0" else s)
        }
      } else if (componentType.name == "double") {
        val format = DecimalFormat(_floatFormatPattern)
        for (i in 0 until arrayLength) {
          if (i != 0) string.append(' ')
          val s = format.format(java.lang.reflect.Array.getDouble(o, i))
          string.append(if (s == "-0") "0" else s)
        }
      } else if (componentType.name == "byte") {
        for (i in 0 until arrayLength) {
          if (i != 0) string.append(' ')
          string.append(java.lang.reflect.Array.getByte(o, i) and 0xff.toByte())
        }
      } else {
        addError("Unexpected array component type: " + componentType.name)
      }
      return string.toString()
    }
    if (o is Double) return DecimalFormat(_floatFormatPattern).format(o.toDouble())
    return if (o is Float) DecimalFormat(_floatFormatPattern).format(o.toFloat().toDouble()) else o.toString()
    // Note that several cameras leave trailing spaces (Olympus, Nikon) but this library is intended to show
// the actual data within the file.  It is not inconceivable that whitespace may be significant here, so we
// do not trim.  Also, if support is added for writing data back to files, this may cause issues.
// We leave trimming to the presentation layer.
  }

  fun getString(tagType: Int, charset: String?): String? {
    val bytes = getByteArray(tagType) ?: return null
    return try {
      String(bytes, Charset.forName(charset))
    } catch (e: UnsupportedEncodingException) {
      null
    }
  }

  fun getStringValue(tagType: Int): StringValue? {
    val o = getObject(tagType)
    return if (o is StringValue) o else null
  }

  /**
   * Returns the object hashed for the particular tag type specified, if available.
   *
   * @param tagType the tag type identifier
   * @return the tag's value as an Object if available, else `null`
   */
  fun getObject(tagType: Int): Any? {
    return _tagMap[Integer.valueOf(tagType)]
  }
  // OTHER METHODS
  /**
   * Returns the name of a specified tag as a String.
   *
   * @param tagType the tag type identifier
   * @return the tag's name as a String
   */
  open fun getTagName(tagType: Int): String {
    val nameMap = tagNameMap
    if (!nameMap.containsKey(tagType)) {
      var hex = Integer.toHexString(tagType)
      while (hex.length < 4) {
        hex = "0$hex"
      }
      return "Unknown tag (0x$hex)"
    }
    return nameMap[tagType]!!
  }

  /**
   * Gets whether the specified tag is known by the directory and has a name.
   *
   * @param tagType the tag type identifier
   * @return whether this directory has a name for the specified tag
   */
  open fun hasTagName(tagType: Int): Boolean {
    return tagNameMap.containsKey(tagType)
  }

  /**
   * Provides a description of a tag's value using the descriptor set by
   * `setDescriptor(Descriptor)`.
   *
   * @param tagType the tag type identifier
   * @return the tag value's description as a String
   */
  fun getDescription(tagType: Int): String? {
    assert(_descriptor != null)
    return _descriptor!!.getDescription(tagType)
  }

  override fun toString(): String {
    return String.format("%s Directory (%d %s)",
      name,
      _tagMap.size,
      if (_tagMap.size == 1) "tag" else "tags")
  }

  companion object {
    private const val _floatFormatPattern = "0.###"
  }
}
