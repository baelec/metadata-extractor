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

/**
 * Represents an age in years, months, days, hours, minutes and seconds.
 *
 *
 * Used by certain Panasonic cameras which have face recognition features.
 *
 * @author Drew Noakes https://drewnoakes.com
 */
data class Age(val years: Int, val months: Int, val days: Int, val hours: Int, val minutes: Int, val seconds: Int) {

  override fun toString(): String {
    return "%04d:%02d:%02d %02d:%02d:%02d".format(years, months, days, hours, minutes, seconds)
  }

  fun toFriendlyString(): String {
    val result = StringBuilder()
    appendAgePart(result, years, "year")
    appendAgePart(result, months, "month")
    appendAgePart(result, days, "day")
    appendAgePart(result, hours, "hour")
    appendAgePart(result, minutes, "minute")
    appendAgePart(result, seconds, "second")
    return result.toString()
  }

  companion object {
    /**
     * Parses an age object from the string format used by Panasonic cameras:
     * `0031:07:15 00:00:00`
     *
     * @param s The String in format `0031:07:15 00:00:00`.
     * @return The parsed Age object, or null if the value could not be parsed
     */
    @JvmStatic
    fun fromPanasonicString(s: String): Age? {
      return if (s.length != 19 || s.startsWith("9999:99:99")) null else try {
        val years = s.substring(0, 4).toInt()
        val months = s.substring(5, 7).toInt()
        val days = s.substring(8, 10).toInt()
        val hours = s.substring(11, 13).toInt()
        val minutes = s.substring(14, 16).toInt()
        val seconds = s.substring(17, 19).toInt()
        Age(years, months, days, hours, minutes, seconds)
      } catch (ignored: NumberFormatException) {
        null
      }
    }

    private fun appendAgePart(result: StringBuilder, num: Int, singularName: String) {
      if (num == 0) return
      if (result.isNotEmpty()) result.append(' ')
      result.append(num).append(' ').append(singularName)
      if (num != 1) result.append('s')
    }
  }
}
