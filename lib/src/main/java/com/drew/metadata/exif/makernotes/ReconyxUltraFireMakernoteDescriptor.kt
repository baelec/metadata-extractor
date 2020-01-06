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
package com.drew.metadata.exif.makernotes

import com.drew.metadata.TagDescriptor
import java.text.DateFormat
import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat

/**
 * Provides human-readable string representations of tag values stored in a [ReconyxUltraFireMakernoteDirectory].
 *
 * @author Todd West http://cascadescarnivoreproject.blogspot.com
 */
class ReconyxUltraFireMakernoteDescriptor(directory: ReconyxUltraFireMakernoteDirectory) : TagDescriptor<ReconyxUltraFireMakernoteDirectory>(directory) {
  override fun getDescription(tagType: Int): String? {
    return when (tagType) {
      ReconyxUltraFireMakernoteDirectory.TAG_LABEL -> _directory.getString(tagType)
      ReconyxUltraFireMakernoteDirectory.TAG_MAKERNOTE_ID -> "0x%08X".format(_directory.getInteger(tagType))
      ReconyxUltraFireMakernoteDirectory.TAG_MAKERNOTE_SIZE -> "%d".format(_directory.getInteger(tagType))
      ReconyxUltraFireMakernoteDirectory.TAG_MAKERNOTE_PUBLIC_ID -> "0x%08X".format(_directory.getInteger(tagType))
      ReconyxUltraFireMakernoteDirectory.TAG_MAKERNOTE_PUBLIC_SIZE -> "%d".format(_directory.getInteger(tagType))
      ReconyxUltraFireMakernoteDirectory.TAG_CAMERA_VERSION, ReconyxUltraFireMakernoteDirectory.TAG_UIB_VERSION, ReconyxUltraFireMakernoteDirectory.TAG_BTL_VERSION, ReconyxUltraFireMakernoteDirectory.TAG_PEX_VERSION, ReconyxUltraFireMakernoteDirectory.TAG_EVENT_TYPE -> _directory.getString(tagType)
      ReconyxUltraFireMakernoteDirectory.TAG_SEQUENCE -> {
        val sequence = _directory.getIntArray(tagType) ?: return null
        "%d/%d".format(sequence[0], sequence[1])
      }
      ReconyxUltraFireMakernoteDirectory.TAG_EVENT_NUMBER -> "%d".format(_directory.getInteger(tagType))
      ReconyxUltraFireMakernoteDirectory.TAG_DATE_TIME_ORIGINAL -> {
        val date = _directory.getString(tagType)
        return try {
          val parser: DateFormat = SimpleDateFormat("yyyy:MM:dd HH:mm:ss")
          parser.format(parser.parse(date))
        } catch (e: ParseException) {
          null
        }
      }
      ReconyxUltraFireMakernoteDirectory.TAG_MOON_PHASE -> getIndexedDescription(tagType, "New", "Waxing Crescent", "First Quarter", "Waxing Gibbous", "Full", "Waning Gibbous", "Last Quarter", "Waning Crescent")
      ReconyxUltraFireMakernoteDirectory.TAG_AMBIENT_TEMPERATURE_FAHRENHEIT, ReconyxUltraFireMakernoteDirectory.TAG_AMBIENT_TEMPERATURE -> "%d".format(_directory.getInteger(tagType))
      ReconyxUltraFireMakernoteDirectory.TAG_FLASH -> getIndexedDescription(tagType, "Off", "On")
      ReconyxUltraFireMakernoteDirectory.TAG_BATTERY_VOLTAGE -> {
        val value = _directory.getDoubleObject(tagType)
        val formatter = DecimalFormat("0.000")
        if (value == null) null else formatter.format(value)
      }
      ReconyxUltraFireMakernoteDirectory.TAG_SERIAL_NUMBER -> {
        // default is UTF_8
        val svalue = _directory.getStringValue(tagType) ?: return null
        svalue.toString()
      }
      ReconyxUltraFireMakernoteDirectory.TAG_USER_LABEL -> _directory.getString(tagType)
      else -> super.getDescription(tagType)
    }
  }
}
