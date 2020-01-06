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

import com.drew.lang.GeoLocation.Companion.decimalToDegreesMinutesSecondsString
import com.drew.lang.GeoLocation.Companion.degreesMinutesSecondsToDecimal
import com.drew.metadata.TagDescriptor
import java.text.DecimalFormat

/**
 * Provides human-readable string representations of tag values stored in a [GpsDirectory].
 *
 * @author Drew Noakes https://drewnoakes.com
 */
class GpsDescriptor(directory: GpsDirectory) : TagDescriptor<GpsDirectory>(directory) {
  override fun getDescription(tagType: Int): String? {
    return when (tagType) {
      GpsDirectory.TAG_VERSION_ID -> gpsVersionIdDescription
      GpsDirectory.TAG_ALTITUDE -> gpsAltitudeDescription
      GpsDirectory.TAG_ALTITUDE_REF -> gpsAltitudeRefDescription
      GpsDirectory.TAG_STATUS -> gpsStatusDescription
      GpsDirectory.TAG_MEASURE_MODE -> gpsMeasureModeDescription
      GpsDirectory.TAG_DOP -> gpsDopDescription
      GpsDirectory.TAG_SPEED_REF -> gpsSpeedRefDescription
      GpsDirectory.TAG_SPEED -> gpsSpeedDescription
      GpsDirectory.TAG_TRACK_REF, GpsDirectory.TAG_IMG_DIRECTION_REF, GpsDirectory.TAG_DEST_BEARING_REF -> getGpsDirectionReferenceDescription(tagType)
      GpsDirectory.TAG_TRACK, GpsDirectory.TAG_IMG_DIRECTION, GpsDirectory.TAG_DEST_BEARING -> getGpsDirectionDescription(tagType)
      GpsDirectory.TAG_DEST_LATITUDE -> gpsDestLatitudeDescription
      GpsDirectory.TAG_DEST_LONGITUDE -> gpsDestLongitudeDescription
      GpsDirectory.TAG_DEST_DISTANCE_REF -> gpsDestinationReferenceDescription
      GpsDirectory.TAG_DEST_DISTANCE -> gpsDestDistanceDescription
      GpsDirectory.TAG_TIME_STAMP -> gpsTimeStampDescription
      GpsDirectory.TAG_LONGITUDE ->  // three rational numbers -- displayed in HH"MM"SS.ss
        gpsLongitudeDescription
      GpsDirectory.TAG_LATITUDE ->  // three rational numbers -- displayed in HH"MM"SS.ss
        gpsLatitudeDescription
      GpsDirectory.TAG_PROCESSING_METHOD -> gpsProcessingMethodDescription
      GpsDirectory.TAG_AREA_INFORMATION -> gpsAreaInformationDescription
      GpsDirectory.TAG_DIFFERENTIAL -> gpsDifferentialDescription
      GpsDirectory.TAG_H_POSITIONING_ERROR -> gpsHPositioningErrorDescription
      else -> super.getDescription(tagType)
    }
  }

  private val gpsVersionIdDescription: String?
    private get() = getVersionBytesDescription(GpsDirectory.TAG_VERSION_ID, 1)

  val gpsLatitudeDescription: String?
    get() {
      val location = _directory.geoLocation
      return if (location == null) null else decimalToDegreesMinutesSecondsString(location.latitude)
    }

  val gpsLongitudeDescription: String?
    get() {
      val location = _directory.geoLocation
      return if (location == null) null else decimalToDegreesMinutesSecondsString(location.longitude)
    }

  // time in hour, min, sec
  val gpsTimeStampDescription: String?
    get() { // time in hour, min, sec
      val timeComponents = _directory.getRationalArray(GpsDirectory.TAG_TIME_STAMP)
      val df = DecimalFormat("00.000")
      return if (timeComponents == null) null else String.format("%02d:%02d:%s UTC",
        timeComponents[0].toInt(),
        timeComponents[1].toInt(),
        df.format(timeComponents[2].toDouble()))
    }

  val gpsDestLatitudeDescription: String?
    get() {
      val latitudes = _directory.getRationalArray(GpsDirectory.TAG_DEST_LATITUDE)
      val latitudeRef = _directory.getString(GpsDirectory.TAG_DEST_LATITUDE_REF)
      if (latitudes == null || latitudes.size != 3 || latitudeRef == null) return null
      val lat = degreesMinutesSecondsToDecimal(
        latitudes[0], latitudes[1], latitudes[2], latitudeRef.equals("S", ignoreCase = true))
      return lat?.let { decimalToDegreesMinutesSecondsString(it) }
    }

  val gpsDestLongitudeDescription: String?
    get() {
      val longitudes = _directory.getRationalArray(GpsDirectory.TAG_LONGITUDE)
      val longitudeRef = _directory.getString(GpsDirectory.TAG_LONGITUDE_REF)
      if (longitudes == null || longitudes.size != 3 || longitudeRef == null) return null
      val lon = degreesMinutesSecondsToDecimal(
        longitudes[0], longitudes[1], longitudes[2], longitudeRef.equals("W", ignoreCase = true))
      return lon?.let { decimalToDegreesMinutesSecondsString(it) }
    }

  val gpsDestinationReferenceDescription: String?
    get() {
      val value = _directory.getString(GpsDirectory.TAG_DEST_DISTANCE_REF) ?: return null
      val distanceRef = value.trim { it <= ' ' }
      return if ("K".equals(distanceRef, ignoreCase = true)) {
        "kilometers"
      } else if ("M".equals(distanceRef, ignoreCase = true)) {
        "miles"
      } else if ("N".equals(distanceRef, ignoreCase = true)) {
        "knots"
      } else {
        "Unknown ($distanceRef)"
      }
    }

  val gpsDestDistanceDescription: String?
    get() {
      val value = _directory.getRational(GpsDirectory.TAG_DEST_DISTANCE) ?: return null
      val unit = gpsDestinationReferenceDescription
      return String.format("%s %s",
        DecimalFormat("0.##").format(value.toDouble()),
        unit?.toLowerCase() ?: "unit")
    }

  fun getGpsDirectionDescription(tagType: Int): String? {
    val angle = _directory.getRational(tagType)
    // provide a decimal version of rational numbers in the description, to avoid strings like "35334/199 degrees"
    val value = if (angle != null) DecimalFormat("0.##").format(angle.toDouble()) else _directory.getString(tagType)
    return if (value == null || value.trim { it <= ' ' }.length == 0) null else value.trim { it <= ' ' } + " degrees"
  }

  fun getGpsDirectionReferenceDescription(tagType: Int): String? {
    val value = _directory.getString(tagType) ?: return null
    val gpsDistRef = value.trim { it <= ' ' }
    return if ("T".equals(gpsDistRef, ignoreCase = true)) {
      "True direction"
    } else if ("M".equals(gpsDistRef, ignoreCase = true)) {
      "Magnetic direction"
    } else {
      "Unknown ($gpsDistRef)"
    }
  }

  val gpsDopDescription: String?
    get() {
      val value = _directory.getRational(GpsDirectory.TAG_DOP)
      return if (value == null) null else DecimalFormat("0.##").format(value.toDouble())
    }

  val gpsSpeedRefDescription: String?
    get() {
      val value = _directory.getString(GpsDirectory.TAG_SPEED_REF) ?: return null
      val gpsSpeedRef = value.trim { it <= ' ' }
      return if ("K".equals(gpsSpeedRef, ignoreCase = true)) {
        "km/h"
      } else if ("M".equals(gpsSpeedRef, ignoreCase = true)) {
        "mph"
      } else if ("N".equals(gpsSpeedRef, ignoreCase = true)) {
        "knots"
      } else {
        "Unknown ($gpsSpeedRef)"
      }
    }

  val gpsSpeedDescription: String?
    get() {
      val value = _directory.getRational(GpsDirectory.TAG_SPEED) ?: return null
      val unit = gpsSpeedRefDescription
      return String.format("%s %s",
        DecimalFormat("0.##").format(value.toDouble()),
        unit?.toLowerCase() ?: "unit")
    }

  val gpsMeasureModeDescription: String?
    get() {
      val value = _directory.getString(GpsDirectory.TAG_MEASURE_MODE) ?: return null
      val gpsSpeedMeasureMode = value.trim { it <= ' ' }
      return if ("2".equals(gpsSpeedMeasureMode, ignoreCase = true)) {
        "2-dimensional measurement"
      } else if ("3".equals(gpsSpeedMeasureMode, ignoreCase = true)) {
        "3-dimensional measurement"
      } else {
        "Unknown ($gpsSpeedMeasureMode)"
      }
    }

  val gpsStatusDescription: String?
    get() {
      val value = _directory.getString(GpsDirectory.TAG_STATUS) ?: return null
      val gpsStatus = value.trim { it <= ' ' }
      return if ("A".equals(gpsStatus, ignoreCase = true)) {
        "Active (Measurement in progress)"
      } else if ("V".equals(gpsStatus, ignoreCase = true)) {
        "Void (Measurement Interoperability)"
      } else {
        "Unknown ($gpsStatus)"
      }
    }

  val gpsAltitudeRefDescription: String?
    get() = getIndexedDescription(GpsDirectory.TAG_ALTITUDE_REF, "Sea level", "Below sea level")

  val gpsAltitudeDescription: String?
    get() {
      val value = _directory.getRational(GpsDirectory.TAG_ALTITUDE)
      return if (value == null) null else DecimalFormat("0.##").format(value.toDouble()) + " metres"
    }

  val gpsProcessingMethodDescription: String?
    get() = getEncodedTextDescription(GpsDirectory.TAG_PROCESSING_METHOD)

  val gpsAreaInformationDescription: String?
    get() = getEncodedTextDescription(GpsDirectory.TAG_AREA_INFORMATION)

  val gpsDifferentialDescription: String?
    get() = getIndexedDescription(GpsDirectory.TAG_DIFFERENTIAL, "No Correction", "Differential Corrected")

  val gpsHPositioningErrorDescription: String?
    get() {
      val value = _directory.getRational(GpsDirectory.TAG_H_POSITIONING_ERROR)
      return if (value == null) null else DecimalFormat("0.##").format(value.toDouble()) + " metres"
    }

  val degreesMinutesSecondsDescription: String?
    get() {
      val location = _directory.geoLocation
      return location?.toDMSString()
    }
}
