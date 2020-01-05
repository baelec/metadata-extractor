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

import com.drew.lang.GeoLocation
import com.drew.lang.GeoLocation.Companion.degreesMinutesSecondsToDecimal
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Describes Exif tags that contain Global Positioning System (GPS) data.
 *
 * @author Drew Noakes https://drewnoakes.com
 */
class GpsDirectory : ExifDirectoryBase() {
  override val tagNameMap = Companion.tagNameMap

  companion object {
    /** GPS tag version GPSVersionID 0 0 BYTE 4  */
    const val TAG_VERSION_ID = 0x0000
    /** North or South Latitude GPSLatitudeRef 1 1 ASCII 2  */
    const val TAG_LATITUDE_REF = 0x0001
    /** Latitude GPSLatitude 2 2 RATIONAL 3  */
    const val TAG_LATITUDE = 0x0002
    /** East or West Longitude GPSLongitudeRef 3 3 ASCII 2  */
    const val TAG_LONGITUDE_REF = 0x0003
    /** Longitude GPSLongitude 4 4 RATIONAL 3  */
    const val TAG_LONGITUDE = 0x0004
    /** Altitude reference GPSAltitudeRef 5 5 BYTE 1  */
    const val TAG_ALTITUDE_REF = 0x0005
    /** Altitude GPSAltitude 6 6 RATIONAL 1  */
    const val TAG_ALTITUDE = 0x0006
    /** GPS time (atomic clock) GPSTimeStamp 7 7 RATIONAL 3  */
    const val TAG_TIME_STAMP = 0x0007
    /** GPS satellites used for measurement GPSSatellites 8 8 ASCII Any  */
    const val TAG_SATELLITES = 0x0008
    /** GPS receiver status GPSStatus 9 9 ASCII 2  */
    const val TAG_STATUS = 0x0009
    /** GPS measurement mode GPSMeasureMode 10 A ASCII 2  */
    const val TAG_MEASURE_MODE = 0x000A
    /** Measurement precision GPSDOP 11 B RATIONAL 1  */
    const val TAG_DOP = 0x000B
    /** Speed unit GPSSpeedRef 12 C ASCII 2  */
    const val TAG_SPEED_REF = 0x000C
    /** Speed of GPS receiver GPSSpeed 13 D RATIONAL 1  */
    const val TAG_SPEED = 0x000D
    /** Reference for direction of movement GPSTrackRef 14 E ASCII 2  */
    const val TAG_TRACK_REF = 0x000E
    /** Direction of movement GPSTrack 15 F RATIONAL 1  */
    const val TAG_TRACK = 0x000F
    /** Reference for direction of image GPSImgDirectionRef 16 10 ASCII 2  */
    const val TAG_IMG_DIRECTION_REF = 0x0010
    /** Direction of image GPSImgDirection 17 11 RATIONAL 1  */
    const val TAG_IMG_DIRECTION = 0x0011
    /** Geodetic survey data used GPSMapDatum 18 12 ASCII Any  */
    const val TAG_MAP_DATUM = 0x0012
    /** Reference for latitude of destination GPSDestLatitudeRef 19 13 ASCII 2  */
    const val TAG_DEST_LATITUDE_REF = 0x0013
    /** Latitude of destination GPSDestLatitude 20 14 RATIONAL 3  */
    const val TAG_DEST_LATITUDE = 0x0014
    /** Reference for longitude of destination GPSDestLongitudeRef 21 15 ASCII 2  */
    const val TAG_DEST_LONGITUDE_REF = 0x0015
    /** Longitude of destination GPSDestLongitude 22 16 RATIONAL 3  */
    const val TAG_DEST_LONGITUDE = 0x0016
    /** Reference for bearing of destination GPSDestBearingRef 23 17 ASCII 2  */
    const val TAG_DEST_BEARING_REF = 0x0017
    /** Bearing of destination GPSDestBearing 24 18 RATIONAL 1  */
    const val TAG_DEST_BEARING = 0x0018
    /** Reference for distance to destination GPSDestDistanceRef 25 19 ASCII 2  */
    const val TAG_DEST_DISTANCE_REF = 0x0019
    /** Distance to destination GPSDestDistance 26 1A RATIONAL 1  */
    const val TAG_DEST_DISTANCE = 0x001A
    /** Name of the method used for location finding GPSProcessingMethod 27 1B UNDEFINED Any  */
    const val TAG_PROCESSING_METHOD = 0x001B
    /** Name of the GPS area GPSAreaInformation 28 1C UNDEFINED Any  */
    const val TAG_AREA_INFORMATION = 0x001C
    /** Date and time GPSDateStamp 29 1D ASCII 11  */
    const val TAG_DATE_STAMP = 0x001D
    /** Whether differential correction is applied GPSDifferential 30 1E SHORT 1  */
    const val TAG_DIFFERENTIAL = 0x001E
    /** Horizontal positioning errors GPSHPositioningError 31 1F RATIONAL 1  */
    const val TAG_H_POSITIONING_ERROR = 0x001F
    protected val tagNameMap = HashMap<Int, String>()

    init {
      addExifTagNames(tagNameMap)
      tagNameMap[TAG_VERSION_ID] = "GPS Version ID"
      tagNameMap[TAG_LATITUDE_REF] = "GPS Latitude Ref"
      tagNameMap[TAG_LATITUDE] = "GPS Latitude"
      tagNameMap[TAG_LONGITUDE_REF] = "GPS Longitude Ref"
      tagNameMap[TAG_LONGITUDE] = "GPS Longitude"
      tagNameMap[TAG_ALTITUDE_REF] = "GPS Altitude Ref"
      tagNameMap[TAG_ALTITUDE] = "GPS Altitude"
      tagNameMap[TAG_TIME_STAMP] = "GPS Time-Stamp"
      tagNameMap[TAG_SATELLITES] = "GPS Satellites"
      tagNameMap[TAG_STATUS] = "GPS Status"
      tagNameMap[TAG_MEASURE_MODE] = "GPS Measure Mode"
      tagNameMap[TAG_DOP] = "GPS DOP"
      tagNameMap[TAG_SPEED_REF] = "GPS Speed Ref"
      tagNameMap[TAG_SPEED] = "GPS Speed"
      tagNameMap[TAG_TRACK_REF] = "GPS Track Ref"
      tagNameMap[TAG_TRACK] = "GPS Track"
      tagNameMap[TAG_IMG_DIRECTION_REF] = "GPS Img Direction Ref"
      tagNameMap[TAG_IMG_DIRECTION] = "GPS Img Direction"
      tagNameMap[TAG_MAP_DATUM] = "GPS Map Datum"
      tagNameMap[TAG_DEST_LATITUDE_REF] = "GPS Dest Latitude Ref"
      tagNameMap[TAG_DEST_LATITUDE] = "GPS Dest Latitude"
      tagNameMap[TAG_DEST_LONGITUDE_REF] = "GPS Dest Longitude Ref"
      tagNameMap[TAG_DEST_LONGITUDE] = "GPS Dest Longitude"
      tagNameMap[TAG_DEST_BEARING_REF] = "GPS Dest Bearing Ref"
      tagNameMap[TAG_DEST_BEARING] = "GPS Dest Bearing"
      tagNameMap[TAG_DEST_DISTANCE_REF] = "GPS Dest Distance Ref"
      tagNameMap[TAG_DEST_DISTANCE] = "GPS Dest Distance"
      tagNameMap[TAG_PROCESSING_METHOD] = "GPS Processing Method"
      tagNameMap[TAG_AREA_INFORMATION] = "GPS Area Information"
      tagNameMap[TAG_DATE_STAMP] = "GPS Date Stamp"
      tagNameMap[TAG_DIFFERENTIAL] = "GPS Differential"
      tagNameMap[TAG_H_POSITIONING_ERROR] = "GPS H Positioning Error"
    }
  }

  override val name: String
    get() = "GPS"// Make sure we have the required values
  // This can return null, in cases where the conversion was not possible

  /**
   * Parses various tags in an attempt to obtain a single object representing the latitude and longitude
   * at which this image was captured.
   *
   * @return The geographical location of this image, if possible, otherwise null
   */
  val geoLocation: GeoLocation?
    get() {
      val latitudes = getRationalArray(TAG_LATITUDE)
      val longitudes = getRationalArray(TAG_LONGITUDE)
      val latitudeRef = getString(TAG_LATITUDE_REF)
      val longitudeRef = getString(TAG_LONGITUDE_REF)
      // Make sure we have the required values
      if (latitudes == null || latitudes.size != 3) return null
      if (longitudes == null || longitudes.size != 3) return null
      if (latitudeRef == null || longitudeRef == null) return null
      val lat = degreesMinutesSecondsToDecimal(latitudes[0], latitudes[1], latitudes[2], latitudeRef.equals("S", ignoreCase = true))
      val lon = degreesMinutesSecondsToDecimal(longitudes[0], longitudes[1], longitudes[2], longitudeRef.equals("W", ignoreCase = true))
      // This can return null, in cases where the conversion was not possible
      return if (lat == null || lon == null) null else GeoLocation(lat, lon)
    }// Make sure we have the required values

  /**
   * Parses the date stamp tag and the time stamp tag to obtain a single Date object representing the
   * date and time when this image was captured.
   *
   * @return A Date object representing when this image was captured, if possible, otherwise null
   */
  val gpsDate: Date?
    get() {
      val date = getString(TAG_DATE_STAMP)
      val timeComponents = getRationalArray(TAG_TIME_STAMP)
      // Make sure we have the required values
      if (date == null) return null
      if (timeComponents == null || timeComponents.size != 3) return null
      val dateTime = String.format(Locale.US, "%s %02d:%02d:%02.3f UTC",
        date, timeComponents[0].toInt(), timeComponents[1].toInt(), timeComponents[2].toDouble())
      return try {
        val parser: DateFormat = SimpleDateFormat("yyyy:MM:dd HH:mm:ss.S z")
        parser.parse(dateTime)
      } catch (e: ParseException) {
        null
      }
    }

  init {
    setDescriptor(GpsDescriptor(this))
  }
}
