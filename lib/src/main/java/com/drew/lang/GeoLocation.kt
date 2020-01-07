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

import java.text.DecimalFormat
import kotlin.math.abs
import kotlin.math.truncate

/**
 * Represents a latitude and longitude pair, giving a position on earth in spherical coordinates.
 *
 *
 * Values of latitude and longitude are given in degrees.
 *
 *
 * This type is immutable.
 */
data class GeoLocation
/**
 * Instantiates a new instance of [GeoLocation].
 *
 * @param latitude the latitude, in degrees
 * @param longitude the longitude, in degrees
 */(
  /**
   * @return the latitudinal angle of this location, in degrees.
   */
  val latitude: Double,
  /**
   * @return the longitudinal angle of this location, in degrees.
   */
  val longitude: Double) {

  /**
   * @return true, if both latitude and longitude are equal to zero
   */
  val isZero = latitude == 0.0 && longitude == 0.0

  /**
   * @return a string representation of this location, of format: `1.23, 4.56`
   */
  override fun toString(): String {
    return "$latitude, $longitude"
  }

  /**
   * @return a string representation of this location, of format: `-1° 23' 4.56", 54° 32' 1.92"`
   */
  fun toDMSString(): String {
    return "${decimalToDegreesMinutesSecondsString(latitude)}, ${decimalToDegreesMinutesSecondsString(longitude)}"
  }

  companion object {
    /**
     * Converts a decimal degree angle into its corresponding DMS (degrees-minutes-seconds) representation as a string,
     * of format: `-1° 23' 4.56"`
     */
    @JvmStatic
    fun decimalToDegreesMinutesSecondsString(decimal: Double): String {
      val dms = decimalToDegreesMinutesSeconds(decimal)
      val format = DecimalFormat("0.##")
      return "%s\u00B0 %s' %s\"".format(format.format(dms[0]), format.format(dms[1]), format.format(dms[2]))
    }

    /**
     * Converts a decimal degree angle into its corresponding DMS (degrees-minutes-seconds) component values, as
     * a double array.
     */
    @JvmStatic
    fun decimalToDegreesMinutesSeconds(decimal: Double): DoubleArray {
      val d = decimal.toInt()
      val m = abs((decimal % 1) * 60)
      val s = (m % 1) * 60
      return doubleArrayOf(d.toDouble(), truncate(m), s)
    }

    /**
     * Converts DMS (degrees-minutes-seconds) rational values, as given in [com.drew.metadata.exif.GpsDirectory],
     * into a single value in degrees, as a double.
     */
    @JvmStatic
    fun degreesMinutesSecondsToDecimal(degs: Rational, mins: Rational, secs: Rational, isNegative: Boolean): Double? {
      var decimal: Double = (abs(degs.toDouble())
        + mins.toDouble() / 60.0 + secs.toDouble() / 3600.0)
      if (decimal.isNaN()) return null
      if (isNegative) decimal *= -1.0
      return decimal
    }
  }
}
