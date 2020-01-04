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

import com.drew.metadata.TagDescriptor

/**
 * Provides human-readable string representations of tag values stored in a [IptcDirectory].
 *
 *
 * As the IPTC directory already stores values as strings, this class simply returns the tag's value.
 *
 * @author Drew Noakes https://drewnoakes.com
 */
class IptcDescriptor(directory: IptcDirectory) : TagDescriptor<IptcDirectory>(directory) {
  override fun getDescription(tagType: Int): String? {
    return when (tagType) {
      IptcDirectory.TAG_DATE_CREATED -> dateCreatedDescription
      IptcDirectory.TAG_DIGITAL_DATE_CREATED -> digitalDateCreatedDescription
      IptcDirectory.TAG_DATE_SENT -> dateSentDescription
      IptcDirectory.TAG_EXPIRATION_DATE -> expirationDateDescription
      IptcDirectory.TAG_EXPIRATION_TIME -> expirationTimeDescription
      IptcDirectory.TAG_FILE_FORMAT -> fileFormatDescription
      IptcDirectory.TAG_KEYWORDS -> keywordsDescription
      IptcDirectory.TAG_REFERENCE_DATE -> referenceDateDescription
      IptcDirectory.TAG_RELEASE_DATE -> releaseDateDescription
      IptcDirectory.TAG_RELEASE_TIME -> releaseTimeDescription
      IptcDirectory.TAG_TIME_CREATED -> timeCreatedDescription
      IptcDirectory.TAG_DIGITAL_TIME_CREATED -> digitalTimeCreatedDescription
      IptcDirectory.TAG_TIME_SENT -> timeSentDescription
      else -> super.getDescription(tagType)
    }
  }

  fun getDateDescription(tagType: Int): String? {
    val s = _directory.getString(tagType) ?: return null
    return if (s.length == 8) s.substring(0, 4) + ':' + s.substring(4, 6) + ':' + s.substring(6) else s
  }

  fun getTimeDescription(tagType: Int): String? {
    val s = _directory.getString(tagType) ?: return null
    return if (s.length == 6 || s.length == 11) s.substring(0, 2) + ':' + s.substring(2, 4) + ':' + s.substring(4) else s
  }

  val fileFormatDescription: String?
    get() {
      val value = _directory.getInteger(IptcDirectory.TAG_FILE_FORMAT) ?: return null
      when (value) {
        0 -> return "No ObjectData"
        1 -> return "IPTC-NAA Digital Newsphoto Parameter Record"
        2 -> return "IPTC7901 Recommended Message Format"
        3 -> return "Tagged Image File Format (Adobe/Aldus Image data)"
        4 -> return "Illustrator (Adobe Graphics data)"
        5 -> return "AppleSingle (Apple Computer Inc)"
        6 -> return "NAA 89-3 (ANPA 1312)"
        7 -> return "MacBinary II"
        8 -> return "IPTC Unstructured Character Oriented File Format (UCOFF)"
        9 -> return "United Press International ANPA 1312 variant"
        10 -> return "United Press International Down-Load Message"
        11 -> return "JPEG File Interchange (JFIF)"
        12 -> return "Photo-CD Image-Pac (Eastman Kodak)"
        13 -> return "Bit Mapped Graphics File [.BMP] (Microsoft)"
        14 -> return "Digital Audio File [.WAV] (Microsoft & Creative Labs)"
        15 -> return "Audio plus Moving Video [.AVI] (Microsoft)"
        16 -> return "PC DOS/Windows Executable Files [.COM][.EXE]"
        17 -> return "Compressed Binary File [.ZIP] (PKWare Inc)"
        18 -> return "Audio Interchange File Format AIFF (Apple Computer Inc)"
        19 -> return "RIFF Wave (Microsoft Corporation)"
        20 -> return "Freehand (Macromedia/Aldus)"
        21 -> return "Hypertext Markup Language [.HTML] (The Internet Society)"
        22 -> return "MPEG 2 Audio Layer 2 (Musicom), ISO/IEC"
        23 -> return "MPEG 2 Audio Layer 3, ISO/IEC"
        24 -> return "Portable Document File [.PDF] Adobe"
        25 -> return "News Industry Text Format (NITF)"
        26 -> return "Tape Archive [.TAR]"
        27 -> return "Tidningarnas Telegrambyra NITF version (TTNITF DTD)"
        28 -> return "Ritzaus Bureau NITF version (RBNITF DTD)"
        29 -> return "Corel Draw [.CDR]"
      }
      return String.format("Unknown (%d)", value)
    }

  val byLineDescription: String?
    get() = _directory.getString(IptcDirectory.TAG_BY_LINE)

  val byLineTitleDescription: String?
    get() = _directory.getString(IptcDirectory.TAG_BY_LINE_TITLE)

  val captionDescription: String?
    get() = _directory.getString(IptcDirectory.TAG_CAPTION)

  val categoryDescription: String?
    get() = _directory.getString(IptcDirectory.TAG_CATEGORY)

  val cityDescription: String?
    get() = _directory.getString(IptcDirectory.TAG_CITY)

  val copyrightNoticeDescription: String?
    get() = _directory.getString(IptcDirectory.TAG_COPYRIGHT_NOTICE)

  val countryOrPrimaryLocationDescription: String?
    get() = _directory.getString(IptcDirectory.TAG_COUNTRY_OR_PRIMARY_LOCATION_NAME)

  val creditDescription: String?
    get() = _directory.getString(IptcDirectory.TAG_CREDIT)

  val dateCreatedDescription: String?
    get() = getDateDescription(IptcDirectory.TAG_DATE_CREATED)

  val digitalDateCreatedDescription: String?
    get() = getDateDescription(IptcDirectory.TAG_DIGITAL_DATE_CREATED)

  val dateSentDescription: String?
    get() = getDateDescription(IptcDirectory.TAG_DATE_SENT)

  val expirationDateDescription: String?
    get() = getDateDescription(IptcDirectory.TAG_EXPIRATION_DATE)

  val expirationTimeDescription: String?
    get() = getTimeDescription(IptcDirectory.TAG_EXPIRATION_TIME)

  val headlineDescription: String?
    get() = _directory.getString(IptcDirectory.TAG_HEADLINE)

  val keywordsDescription: String?
    get() {
      val keywords = _directory.getStringArray(IptcDirectory.TAG_KEYWORDS) ?: return null
      return keywords.joinToString(";")
    }

  val objectNameDescription: String?
    get() = _directory.getString(IptcDirectory.TAG_OBJECT_NAME)

  val originalTransmissionReferenceDescription: String?
    get() = _directory.getString(IptcDirectory.TAG_ORIGINAL_TRANSMISSION_REFERENCE)

  val originatingProgramDescription: String?
    get() = _directory.getString(IptcDirectory.TAG_ORIGINATING_PROGRAM)

  val provinceOrStateDescription: String?
    get() = _directory.getString(IptcDirectory.TAG_PROVINCE_OR_STATE)

  val recordVersionDescription: String?
    get() = _directory.getString(IptcDirectory.TAG_APPLICATION_RECORD_VERSION)

  val referenceDateDescription: String?
    get() = getDateDescription(IptcDirectory.TAG_REFERENCE_DATE)

  val releaseDateDescription: String?
    get() = getDateDescription(IptcDirectory.TAG_RELEASE_DATE)

  val releaseTimeDescription: String?
    get() = getTimeDescription(IptcDirectory.TAG_RELEASE_TIME)

  val sourceDescription: String?
    get() = _directory.getString(IptcDirectory.TAG_SOURCE)

  val specialInstructionsDescription: String?
    get() = _directory.getString(IptcDirectory.TAG_SPECIAL_INSTRUCTIONS)

  val supplementalCategoriesDescription: String?
    get() = _directory.getString(IptcDirectory.TAG_SUPPLEMENTAL_CATEGORIES)

  val timeCreatedDescription: String?
    get() = getTimeDescription(IptcDirectory.TAG_TIME_CREATED)

  private val digitalTimeCreatedDescription: String?
    get() = getTimeDescription(IptcDirectory.TAG_DIGITAL_TIME_CREATED)

  val timeSentDescription: String?
    get() = getTimeDescription(IptcDirectory.TAG_TIME_SENT)

  val urgencyDescription: String?
    get() = _directory.getString(IptcDirectory.TAG_URGENCY)

  val writerDescription: String?
    get() = _directory.getString(IptcDirectory.TAG_CAPTION_WRITER)
}
