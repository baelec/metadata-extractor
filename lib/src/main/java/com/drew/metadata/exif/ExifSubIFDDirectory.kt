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
package com.drew.metadata.exif

import java.util.*

/**
 * Describes Exif tags from the SubIFD directory.
 *
 * @author Drew Noakes https://drewnoakes.com
 */
class ExifSubIFDDirectory : ExifDirectoryBase() {
  override val tagNameMap = Companion.tagNameMap

  companion object {
    /** This tag is a pointer to the Exif Interop IFD.  */
    const val TAG_INTEROP_OFFSET = 0xA005
    protected val tagNameMap = HashMap<Int, String>()

    init {
      addExifTagNames(tagNameMap)
    }
  }

  override val name: String
    get() = "Exif SubIFD"

  /**
   * Parses the date/time tag, the subsecond tag and the time offset tag to obtain a single Date
   * object with milliseconds representing the date and time when this image was modified.  If
   * the time offset tag does not exist, attempts will be made to parse the values as though it is
   * in the GMT [TimeZone].
   *
   * @return A Date object representing when this image was modified, if possible, otherwise null
   */
  val dateModified: Date?
    get() = getDateModified(null)

  /**
   * Parses the date/time tag, the subsecond tag and the time offset tag to obtain a single Date
   * object with milliseconds representing the date and time when this image was modified.  If
   * the time offset tag does not exist, attempts will be made to parse the values as though it is
   * in the [TimeZone] represented by the `timeZone` parameter (if it is non-null).
   *
   * @param timeZone the time zone to use
   * @return A Date object representing when this image was modified, if possible, otherwise null
   */
  fun getDateModified(timeZone: TimeZone?): Date? {
    val parent = parent!!
    return if (parent is ExifIFD0Directory) {
      val timeZoneModified = getTimeZone(TAG_OFFSET_TIME)
      parent.getDate(TAG_DATETIME, getString(TAG_SUBSECOND_TIME),
        timeZoneModified ?: timeZone)
    } else {
      null
    }
  }

  /**
   * Parses the date/time tag, the subsecond tag and the time offset tag to obtain a single Date
   * object with milliseconds representing the date and time when this image was captured.  If
   * the time offset tag does not exist, attempts will be made to parse the values as though it is
   * in the GMT [TimeZone].
   *
   * @return A Date object representing when this image was captured, if possible, otherwise null
   */
  val dateOriginal: Date?
    get() = getDateOriginal(null)

  /**
   * Parses the date/time tag, the subsecond tag and the time offset tag to obtain a single Date
   * object with milliseconds representing the date and time when this image was captured.  If
   * the time offset tag does not exist, attempts will be made to parse the values as though it is
   * in the [TimeZone] represented by the `timeZone` parameter (if it is non-null).
   *
   * @param timeZone the time zone to use
   * @return A Date object representing when this image was captured, if possible, otherwise null
   */
  fun getDateOriginal(timeZone: TimeZone?): Date? {
    val timeZoneOriginal = getTimeZone(TAG_OFFSET_TIME_ORIGINAL)
    return getDate(TAG_DATETIME_ORIGINAL, getString(TAG_SUBSECOND_TIME_ORIGINAL),
      timeZoneOriginal ?: timeZone)
  }

  /**
   * Parses the date/time tag, the subsecond tag and the time offset tag to obtain a single Date
   * object with milliseconds representing the date and time when this image was digitized.  If
   * the time offset tag does not exist, attempts will be made to parse the values as though it is
   * in the GMT [TimeZone].
   *
   * @return A Date object representing when this image was digitized, if possible, otherwise null
   */
  val dateDigitized: Date?
    get() = getDateDigitized(null)

  /**
   * Parses the date/time tag, the subsecond tag and the time offset tag to obtain a single Date
   * object with milliseconds representing the date and time when this image was digitized.  If
   * the time offset tag does not exist, attempts will be made to parse the values as though it is
   * in the [TimeZone] represented by the `timeZone` parameter (if it is non-null).
   *
   * @param timeZone the time zone to use
   * @return A Date object representing when this image was digitized, if possible, otherwise null
   */
  fun getDateDigitized(timeZone: TimeZone?): Date? {
    val timeZoneDigitized = getTimeZone(TAG_OFFSET_TIME_DIGITIZED)
    return getDate(TAG_DATETIME_DIGITIZED, getString(TAG_SUBSECOND_TIME_DIGITIZED),
      timeZoneDigitized ?: timeZone)
  }

  private fun getTimeZone(tagType: Int): TimeZone? {
    val timeOffset = getString(tagType)
    return if (timeOffset != null && timeOffset.matches(Regex("[+\\-]\\d\\d:\\d\\d"))) {
      TimeZone.getTimeZone("GMT$timeOffset")
    } else {
      null
    }
  }

  init {
    setDescriptor(ExifSubIFDDescriptor(this))
  }
}
