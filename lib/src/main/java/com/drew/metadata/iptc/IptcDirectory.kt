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
package com.drew.metadata.iptc

import com.drew.metadata.Directory
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Describes tags used by the International Press Telecommunications Council (IPTC) metadata format.
 *
 * @author Drew Noakes https://drewnoakes.com
 */
class IptcDirectory : Directory() {
  override val tagNameMap = Companion.tagNameMap
  companion object {
    // IPTC EnvelopeRecord Tags
    const val TAG_ENVELOPE_RECORD_VERSION = 0x0100 // 0 + 0x0100
    const val TAG_DESTINATION = 0x0105 // 5
    const val TAG_FILE_FORMAT = 0x0114 // 20
    const val TAG_FILE_VERSION = 0x0116 // 22
    const val TAG_SERVICE_ID = 0x011E // 30
    const val TAG_ENVELOPE_NUMBER = 0x0128 // 40
    const val TAG_PRODUCT_ID = 0x0132 // 50
    const val TAG_ENVELOPE_PRIORITY = 0x013C // 60
    const val TAG_DATE_SENT = 0x0146 // 70
    const val TAG_TIME_SENT = 0x0150 // 80
    const val TAG_CODED_CHARACTER_SET = 0x015A // 90
    const val TAG_UNIQUE_OBJECT_NAME = 0x0164 // 100
    const val TAG_ARM_IDENTIFIER = 0x0178 // 120
    const val TAG_ARM_VERSION = 0x017a // 122
    // IPTC ApplicationRecord Tags
    const val TAG_APPLICATION_RECORD_VERSION = 0x0200 // 0 + 0x0200
    const val TAG_OBJECT_TYPE_REFERENCE = 0x0203 // 3
    const val TAG_OBJECT_ATTRIBUTE_REFERENCE = 0x0204 // 4
    const val TAG_OBJECT_NAME = 0x0205 // 5
    const val TAG_EDIT_STATUS = 0x0207 // 7
    const val TAG_EDITORIAL_UPDATE = 0x0208 // 8
    const val TAG_URGENCY = 0X020A // 10
    const val TAG_SUBJECT_REFERENCE = 0X020C // 12
    const val TAG_CATEGORY = 0x020F // 15
    const val TAG_SUPPLEMENTAL_CATEGORIES = 0x0214 // 20
    const val TAG_FIXTURE_ID = 0x0216 // 22
    const val TAG_KEYWORDS = 0x0219 // 25
    const val TAG_CONTENT_LOCATION_CODE = 0x021A // 26
    const val TAG_CONTENT_LOCATION_NAME = 0x021B // 27
    const val TAG_RELEASE_DATE = 0X021E // 30
    const val TAG_RELEASE_TIME = 0x0223 // 35
    const val TAG_EXPIRATION_DATE = 0x0225 // 37
    const val TAG_EXPIRATION_TIME = 0x0226 // 38
    const val TAG_SPECIAL_INSTRUCTIONS = 0x0228 // 40
    const val TAG_ACTION_ADVISED = 0x022A // 42
    const val TAG_REFERENCE_SERVICE = 0x022D // 45
    const val TAG_REFERENCE_DATE = 0x022F // 47
    const val TAG_REFERENCE_NUMBER = 0x0232 // 50
    const val TAG_DATE_CREATED = 0x0237 // 55
    const val TAG_TIME_CREATED = 0X023C // 60
    const val TAG_DIGITAL_DATE_CREATED = 0x023E // 62
    const val TAG_DIGITAL_TIME_CREATED = 0x023F // 63
    const val TAG_ORIGINATING_PROGRAM = 0x0241 // 65
    const val TAG_PROGRAM_VERSION = 0x0246 // 70
    const val TAG_OBJECT_CYCLE = 0x024B // 75
    const val TAG_BY_LINE = 0x0250 // 80
    const val TAG_BY_LINE_TITLE = 0x0255 // 85
    const val TAG_CITY = 0x025A // 90
    const val TAG_SUB_LOCATION = 0x025C // 92
    const val TAG_PROVINCE_OR_STATE = 0x025F // 95
    const val TAG_COUNTRY_OR_PRIMARY_LOCATION_CODE = 0x0264 // 100
    const val TAG_COUNTRY_OR_PRIMARY_LOCATION_NAME = 0x0265 // 101
    const val TAG_ORIGINAL_TRANSMISSION_REFERENCE = 0x0267 // 103
    const val TAG_HEADLINE = 0x0269 // 105
    const val TAG_CREDIT = 0x026E // 110
    const val TAG_SOURCE = 0x0273 // 115
    const val TAG_COPYRIGHT_NOTICE = 0x0274 // 116
    const val TAG_CONTACT = 0x0276 // 118
    const val TAG_CAPTION = 0x0278 // 120
    const val TAG_LOCAL_CAPTION = 0x0279 // 121
    const val TAG_CAPTION_WRITER = 0x027A // 122
    const val TAG_RASTERIZED_CAPTION = 0x027D // 125
    const val TAG_IMAGE_TYPE = 0x0282 // 130
    const val TAG_IMAGE_ORIENTATION = 0x0283 // 131
    const val TAG_LANGUAGE_IDENTIFIER = 0x0287 // 135
    const val TAG_AUDIO_TYPE = 0x0296 // 150
    const val TAG_AUDIO_SAMPLING_RATE = 0x0297 // 151
    const val TAG_AUDIO_SAMPLING_RESOLUTION = 0x0298 // 152
    const val TAG_AUDIO_DURATION = 0x0299 // 153
    const val TAG_AUDIO_OUTCUE = 0x029A // 154
    const val TAG_JOB_ID = 0x02B8 // 184
    const val TAG_MASTER_DOCUMENT_ID = 0x02B9 // 185
    const val TAG_SHORT_DOCUMENT_ID = 0x02BA // 186
    const val TAG_UNIQUE_DOCUMENT_ID = 0x02BB // 187
    const val TAG_OWNER_ID = 0x02BC // 188
    const val TAG_OBJECT_PREVIEW_FILE_FORMAT = 0x02C8 // 200
    const val TAG_OBJECT_PREVIEW_FILE_FORMAT_VERSION = 0x02C9 // 201
    const val TAG_OBJECT_PREVIEW_DATA = 0x02CA // 202
    protected val tagNameMap = HashMap<Int, String>()

    init {
      tagNameMap[TAG_ENVELOPE_RECORD_VERSION] = "Enveloped Record Version"
      tagNameMap[TAG_DESTINATION] = "Destination"
      tagNameMap[TAG_FILE_FORMAT] = "File Format"
      tagNameMap[TAG_FILE_VERSION] = "File Version"
      tagNameMap[TAG_SERVICE_ID] = "Service Identifier"
      tagNameMap[TAG_ENVELOPE_NUMBER] = "Envelope Number"
      tagNameMap[TAG_PRODUCT_ID] = "Product Identifier"
      tagNameMap[TAG_ENVELOPE_PRIORITY] = "Envelope Priority"
      tagNameMap[TAG_DATE_SENT] = "Date Sent"
      tagNameMap[TAG_TIME_SENT] = "Time Sent"
      tagNameMap[TAG_CODED_CHARACTER_SET] = "Coded Character Set"
      tagNameMap[TAG_UNIQUE_OBJECT_NAME] = "Unique Object Name"
      tagNameMap[TAG_ARM_IDENTIFIER] = "ARM Identifier"
      tagNameMap[TAG_ARM_VERSION] = "ARM Version"
      tagNameMap[TAG_APPLICATION_RECORD_VERSION] = "Application Record Version"
      tagNameMap[TAG_OBJECT_TYPE_REFERENCE] = "Object Type Reference"
      tagNameMap[TAG_OBJECT_ATTRIBUTE_REFERENCE] = "Object Attribute Reference"
      tagNameMap[TAG_OBJECT_NAME] = "Object Name"
      tagNameMap[TAG_EDIT_STATUS] = "Edit Status"
      tagNameMap[TAG_EDITORIAL_UPDATE] = "Editorial Update"
      tagNameMap[TAG_URGENCY] = "Urgency"
      tagNameMap[TAG_SUBJECT_REFERENCE] = "Subject Reference"
      tagNameMap[TAG_CATEGORY] = "Category"
      tagNameMap[TAG_SUPPLEMENTAL_CATEGORIES] = "Supplemental Category(s)"
      tagNameMap[TAG_FIXTURE_ID] = "Fixture Identifier"
      tagNameMap[TAG_KEYWORDS] = "Keywords"
      tagNameMap[TAG_CONTENT_LOCATION_CODE] = "Content Location Code"
      tagNameMap[TAG_CONTENT_LOCATION_NAME] = "Content Location Name"
      tagNameMap[TAG_RELEASE_DATE] = "Release Date"
      tagNameMap[TAG_RELEASE_TIME] = "Release Time"
      tagNameMap[TAG_EXPIRATION_DATE] = "Expiration Date"
      tagNameMap[TAG_EXPIRATION_TIME] = "Expiration Time"
      tagNameMap[TAG_SPECIAL_INSTRUCTIONS] = "Special Instructions"
      tagNameMap[TAG_ACTION_ADVISED] = "Action Advised"
      tagNameMap[TAG_REFERENCE_SERVICE] = "Reference Service"
      tagNameMap[TAG_REFERENCE_DATE] = "Reference Date"
      tagNameMap[TAG_REFERENCE_NUMBER] = "Reference Number"
      tagNameMap[TAG_DATE_CREATED] = "Date Created"
      tagNameMap[TAG_TIME_CREATED] = "Time Created"
      tagNameMap[TAG_DIGITAL_DATE_CREATED] = "Digital Date Created"
      tagNameMap[TAG_DIGITAL_TIME_CREATED] = "Digital Time Created"
      tagNameMap[TAG_ORIGINATING_PROGRAM] = "Originating Program"
      tagNameMap[TAG_PROGRAM_VERSION] = "Program Version"
      tagNameMap[TAG_OBJECT_CYCLE] = "Object Cycle"
      tagNameMap[TAG_BY_LINE] = "By-line"
      tagNameMap[TAG_BY_LINE_TITLE] = "By-line Title"
      tagNameMap[TAG_CITY] = "City"
      tagNameMap[TAG_SUB_LOCATION] = "Sub-location"
      tagNameMap[TAG_PROVINCE_OR_STATE] = "Province/State"
      tagNameMap[TAG_COUNTRY_OR_PRIMARY_LOCATION_CODE] = "Country/Primary Location Code"
      tagNameMap[TAG_COUNTRY_OR_PRIMARY_LOCATION_NAME] = "Country/Primary Location Name"
      tagNameMap[TAG_ORIGINAL_TRANSMISSION_REFERENCE] = "Original Transmission Reference"
      tagNameMap[TAG_HEADLINE] = "Headline"
      tagNameMap[TAG_CREDIT] = "Credit"
      tagNameMap[TAG_SOURCE] = "Source"
      tagNameMap[TAG_COPYRIGHT_NOTICE] = "Copyright Notice"
      tagNameMap[TAG_CONTACT] = "Contact"
      tagNameMap[TAG_CAPTION] = "Caption/Abstract"
      tagNameMap[TAG_LOCAL_CAPTION] = "Local Caption"
      tagNameMap[TAG_CAPTION_WRITER] = "Caption Writer/Editor"
      tagNameMap[TAG_RASTERIZED_CAPTION] = "Rasterized Caption"
      tagNameMap[TAG_IMAGE_TYPE] = "Image Type"
      tagNameMap[TAG_IMAGE_ORIENTATION] = "Image Orientation"
      tagNameMap[TAG_LANGUAGE_IDENTIFIER] = "Language Identifier"
      tagNameMap[TAG_AUDIO_TYPE] = "Audio Type"
      tagNameMap[TAG_AUDIO_SAMPLING_RATE] = "Audio Sampling Rate"
      tagNameMap[TAG_AUDIO_SAMPLING_RESOLUTION] = "Audio Sampling Resolution"
      tagNameMap[TAG_AUDIO_DURATION] = "Audio Duration"
      tagNameMap[TAG_AUDIO_OUTCUE] = "Audio Outcue"
      tagNameMap[TAG_JOB_ID] = "Job Identifier"
      tagNameMap[TAG_MASTER_DOCUMENT_ID] = "Master Document Identifier"
      tagNameMap[TAG_SHORT_DOCUMENT_ID] = "Short Document Identifier"
      tagNameMap[TAG_UNIQUE_DOCUMENT_ID] = "Unique Document Identifier"
      tagNameMap[TAG_OWNER_ID] = "Owner Identifier"
      tagNameMap[TAG_OBJECT_PREVIEW_FILE_FORMAT] = "Object Data Preview File Format"
      tagNameMap[TAG_OBJECT_PREVIEW_FILE_FORMAT_VERSION] = "Object Data Preview File Format Version"
      tagNameMap[TAG_OBJECT_PREVIEW_DATA] = "Object Data Preview Data"
    }
  }

  override val name: String
    get() = "IPTC"

  /**
   * Returns any keywords contained in the IPTC data.  This value may be `null`.
   */
  val keywords: List<String>?
    get() {
      val array = getStringArray(TAG_KEYWORDS) ?: return null
      return Arrays.asList(*array)
    }

  /**
   * Parses the Date Sent tag and the Time Sent tag to obtain a single Date object representing the
   * date and time when the service sent this image.
   * @return A Date object representing when the service sent this image, if possible, otherwise null
   */
  val dateSent: Date?
    get() = getDate(TAG_DATE_SENT, TAG_TIME_SENT)

  /**
   * Parses the Release Date tag and the Release Time tag to obtain a single Date object representing the
   * date and time when this image was released.
   * @return A Date object representing when this image was released, if possible, otherwise null
   */
  val releaseDate: Date?
    get() = getDate(TAG_RELEASE_DATE, TAG_RELEASE_TIME)

  /**
   * Parses the Expiration Date tag and the Expiration Time tag to obtain a single Date object representing
   * that this image should not used after this date and time.
   * @return A Date object representing when this image was released, if possible, otherwise null
   */
  val expirationDate: Date?
    get() = getDate(TAG_EXPIRATION_DATE, TAG_EXPIRATION_TIME)

  /**
   * Parses the Date Created tag and the Time Created tag to obtain a single Date object representing the
   * date and time when this image was captured.
   * @return A Date object representing when this image was captured, if possible, otherwise null
   */
  val dateCreated: Date?
    get() = getDate(TAG_DATE_CREATED, TAG_TIME_CREATED)

  /**
   * Parses the Digital Date Created tag and the Digital Time Created tag to obtain a single Date object
   * representing the date and time when the digital representation of this image was created.
   * @return A Date object representing when the digital representation of this image was created,
   * if possible, otherwise null
   */
  val digitalDateCreated: Date?
    get() = getDate(TAG_DIGITAL_DATE_CREATED, TAG_DIGITAL_TIME_CREATED)

  private fun getDate(dateTagType: Int, timeTagType: Int): Date? {
    val date = getString(dateTagType)
    val time = getString(timeTagType)
    if (date == null) return null
    return if (time == null) null else try {
      val parser: DateFormat = SimpleDateFormat("yyyyMMddHHmmssZ")
      parser.parse(date + time)
    } catch (e: ParseException) {
      null
    }
  }

  init {
    setDescriptor(IptcDescriptor(this))
  }
}
