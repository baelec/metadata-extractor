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
package com.drew.metadata.mov.metadata

import com.drew.metadata.mov.QuickTimeDirectory
import java.util.*

/**
 * @author Payton Garland
 */
class QuickTimeMetadataDirectory : QuickTimeDirectory() {
  companion object {
    // User Data Types Holder (0x0400 - 0x04FF)
    // https://sno.phy.queensu.ca/~phil/exiftool/TagNames/QuickTime.html#Meta
// User Metadata Types Holder (0x0500 - 0x05FF)
// https://developer.apple.com/library/content/documentation/QuickTime/QTFF/Metadata/Metadata.html#//apple_ref/doc/uid/TP40000939-CH1-SW43
// https://sno.phy.queensu.ca/~phil/exiftool/TagNames/QuickTime.html#Meta
    const val TAG_ALBUM = 0x0500
    const val TAG_ARTIST = 0x0501
    const val TAG_ARTWORK = 0x0502
    const val TAG_AUTHOR = 0x0503
    const val TAG_COMMENT = 0x0504
    const val TAG_COPYRIGHT = 0x0505
    const val TAG_CREATION_DATE = 0x0506
    const val TAG_DESCRIPTION = 0x0507
    const val TAG_DIRECTOR = 0x0508
    const val TAG_TITLE = 0x0509
    const val TAG_GENRE = 0x050A
    const val TAG_INFORMATION = 0x050B
    const val TAG_KEYWORDS = 0x050C
    const val TAG_LOCATION_ISO6709 = 0x050D
    const val TAG_PRODUCER = 0x050E
    const val TAG_PUBLISHER = 0x050F
    const val TAG_SOFTWARE = 0x0510
    const val TAG_YEAR = 0x0511
    const val TAG_COLLECTION_USER = 0x0512
    const val TAG_RATING_USER = 0x0513
    const val TAG_LOCATION_NAME = 0x0514
    const val TAG_LOCATION_BODY = 0x0515
    const val TAG_LOCATION_NOTE = 0x0516
    const val TAG_LOCATION_ROLE = 0x0517
    const val TAG_LOCATION_DATE = 0x0518
    const val TAG_DIRECTION_FACING = 0x0519
    const val TAG_DIRECTION_MOTION = 0x051A
    const val TAG_DISPLAY_NAME = 0x051B
    const val TAG_CONTENT_IDENTIFIER = 0x051C
    const val TAG_MAKE = 0x051D
    const val TAG_MODEL = 0x051E
    const val TAG_ORIGINATING_SIGNATURE = 0x051F
    const val TAG_PIXEL_DENSITY = 0x0520
    protected val tagNameMap = HashMap<Int, String>()
    val _tagIntegerMap = HashMap<String, Int>()

    init {
      _tagIntegerMap["com.apple.quicktime.album"] = TAG_ALBUM
      _tagIntegerMap["com.apple.quicktime.artist"] = TAG_ARTIST
      _tagIntegerMap["com.apple.quicktime.artwork"] = TAG_ARTWORK
      _tagIntegerMap["com.apple.quicktime.author"] = TAG_AUTHOR
      _tagIntegerMap["com.apple.quicktime.comment"] = TAG_COMMENT
      _tagIntegerMap["com.apple.quicktime.copyright"] = TAG_COPYRIGHT
      _tagIntegerMap["com.apple.quicktime.creationdate"] = TAG_CREATION_DATE
      _tagIntegerMap["com.apple.quicktime.description"] = TAG_DESCRIPTION
      _tagIntegerMap["com.apple.quicktime.director"] = TAG_DIRECTOR
      _tagIntegerMap["com.apple.quicktime.title"] = TAG_TITLE
      _tagIntegerMap["com.apple.quicktime.genre"] = TAG_GENRE
      _tagIntegerMap["com.apple.quicktime.information"] = TAG_INFORMATION
      _tagIntegerMap["com.apple.quicktime.keywords"] = TAG_KEYWORDS
      _tagIntegerMap["com.apple.quicktime.location.ISO6709"] = TAG_LOCATION_ISO6709
      _tagIntegerMap["com.apple.quicktime.producer"] = TAG_PRODUCER
      _tagIntegerMap["com.apple.quicktime.publisher"] = TAG_PUBLISHER
      _tagIntegerMap["com.apple.quicktime.software"] = TAG_SOFTWARE
      _tagIntegerMap["com.apple.quicktime.year"] = TAG_YEAR
      _tagIntegerMap["com.apple.quicktime.collection.user"] = TAG_COLLECTION_USER
      _tagIntegerMap["com.apple.quicktime.rating.user"] = TAG_RATING_USER
      _tagIntegerMap["com.apple.quicktime.location.name"] = TAG_LOCATION_NAME
      _tagIntegerMap["com.apple.quicktime.location.body"] = TAG_LOCATION_BODY
      _tagIntegerMap["com.apple.quicktime.location.note"] = TAG_LOCATION_NOTE
      _tagIntegerMap["com.apple.quicktime.location.role"] = TAG_LOCATION_ROLE
      _tagIntegerMap["com.apple.quicktime.location.date"] = TAG_LOCATION_DATE
      _tagIntegerMap["com.apple.quicktime.direction.facing"] = TAG_DIRECTION_FACING
      _tagIntegerMap["com.apple.quicktime.direction.motion"] = TAG_DIRECTION_MOTION
      _tagIntegerMap["com.apple.quicktime.displayname"] = TAG_DISPLAY_NAME
      _tagIntegerMap["com.apple.quicktime.content.identifier"] = TAG_CONTENT_IDENTIFIER
      _tagIntegerMap["com.apple.quicktime.make"] = TAG_MAKE
      _tagIntegerMap["com.apple.quicktime.model"] = TAG_MODEL
      _tagIntegerMap["com.apple.photos.originating.signature"] = TAG_ORIGINATING_SIGNATURE
      _tagIntegerMap["com.apple.quicktime.pixeldensity"] = TAG_PIXEL_DENSITY
      _tagIntegerMap["----"] = 0x0400
      _tagIntegerMap["@PST"] = 0x0401
      _tagIntegerMap["@ppi"] = 0x0402
      _tagIntegerMap["@pti"] = 0x0403
      _tagIntegerMap["@sti"] = 0x0404
      _tagIntegerMap["AACR"] = 0x0405
      _tagIntegerMap["CDEK"] = 0x0406
      _tagIntegerMap["CDET"] = 0x0407
      _tagIntegerMap["GUID"] = 0x0408
      _tagIntegerMap["VERS"] = 0x0409
      _tagIntegerMap["aART"] = 0x040A
      _tagIntegerMap["akID"] = 0x040B
      _tagIntegerMap["albm"] = 0x040C
      _tagIntegerMap["apID"] = 0x040D
      _tagIntegerMap["atID"] = 0x040E
      _tagIntegerMap["auth"] = 0x040F
      _tagIntegerMap["catg"] = 0x0410
      _tagIntegerMap["cnID"] = 0x0411
      _tagIntegerMap["covr"] = 0x0412
      _tagIntegerMap["cpil"] = 0x0413
      _tagIntegerMap["cprt"] = 0x0414
      _tagIntegerMap["desc"] = 0x0415
      _tagIntegerMap["disk"] = 0x0416
      _tagIntegerMap["dscp"] = 0x0417
      _tagIntegerMap["egid"] = 0x0418
      _tagIntegerMap["geID"] = 0x0419
      _tagIntegerMap["gnre"] = 0x041A
      _tagIntegerMap["grup"] = 0x041B
      _tagIntegerMap["gshh"] = 0x041C
      _tagIntegerMap["gspm"] = 0x041D
      _tagIntegerMap["gspu"] = 0x041E
      _tagIntegerMap["gssd"] = 0x041F
      _tagIntegerMap["gsst"] = 0x0420
      _tagIntegerMap["gstd"] = 0x0421
      _tagIntegerMap["hdvd"] = 0x0422
      _tagIntegerMap["itnu"] = 0x0423
      _tagIntegerMap["keyw"] = 0x0424
      _tagIntegerMap["ldes"] = 0x0425
      _tagIntegerMap["pcst"] = 0x0426
      _tagIntegerMap["perf"] = 0x0427
      _tagIntegerMap["pgap"] = 0x0428
      _tagIntegerMap["plID"] = 0x0429
      _tagIntegerMap["prID"] = 0x042A
      _tagIntegerMap["purd"] = 0x042B
      _tagIntegerMap["purl"] = 0x042C
      _tagIntegerMap["rate"] = 0x042D
      _tagIntegerMap["rldt"] = 0x042E
      _tagIntegerMap["rtng"] = 0x042F
      _tagIntegerMap["sfID"] = 0x0430
      _tagIntegerMap["soaa"] = 0x0431
      _tagIntegerMap["soal"] = 0x0432
      _tagIntegerMap["soar"] = 0x0433
      _tagIntegerMap["soco"] = 0x0434
      _tagIntegerMap["sonm"] = 0x0435
      _tagIntegerMap["sosn"] = 0x0436
      _tagIntegerMap["stik"] = 0x0437
      _tagIntegerMap["titl"] = 0x0438
      _tagIntegerMap["tmpo"] = 0x0439
      _tagIntegerMap["trkn"] = 0x043A
      _tagIntegerMap["tven"] = 0x043B
      _tagIntegerMap["tves"] = 0x043C
      _tagIntegerMap["tvnn"] = 0x043D
      _tagIntegerMap["tvsh"] = 0x043E
      _tagIntegerMap["tvsn"] = 0x043F
      _tagIntegerMap["yrrc"] = 0x0440
      _tagIntegerMap["�ART"] = 0x0441
      _tagIntegerMap["�alb"] = 0x0442
      _tagIntegerMap["�cmt"] = 0x0443
      _tagIntegerMap["�com"] = 0x0444
      _tagIntegerMap["�cpy"] = 0x0445
      _tagIntegerMap["�day"] = 0x0446
      _tagIntegerMap["�des"] = 0x0447
      _tagIntegerMap["�enc"] = 0x0448
      _tagIntegerMap["�gen"] = 0x0449
      _tagIntegerMap["�grp"] = 0x044A
      _tagIntegerMap["�lyr"] = 0x044B
      _tagIntegerMap["�nam"] = 0x044C
      _tagIntegerMap["�nrt"] = 0x044D
      _tagIntegerMap["�pub"] = 0x044E
      _tagIntegerMap["�too"] = 0x044F
      _tagIntegerMap["�trk"] = 0x0450
      _tagIntegerMap["�wrt"] = 0x0451
      tagNameMap[TAG_ALBUM] = "Album"
      tagNameMap[TAG_ARTIST] = "Artist"
      tagNameMap[TAG_ARTWORK] = "Artwork"
      tagNameMap[TAG_AUTHOR] = "Author"
      tagNameMap[TAG_COMMENT] = "Comment"
      tagNameMap[TAG_COPYRIGHT] = "Copyright"
      tagNameMap[TAG_CREATION_DATE] = "Creation Date"
      tagNameMap[TAG_DESCRIPTION] = "Description"
      tagNameMap[TAG_DIRECTOR] = "Director"
      tagNameMap[TAG_TITLE] = "Title"
      tagNameMap[TAG_GENRE] = "Genre"
      tagNameMap[TAG_INFORMATION] = "Information"
      tagNameMap[TAG_KEYWORDS] = "Keywords"
      tagNameMap[TAG_LOCATION_ISO6709] = "ISO 6709"
      tagNameMap[TAG_PRODUCER] = "Producer"
      tagNameMap[TAG_PUBLISHER] = "Publisher"
      tagNameMap[TAG_SOFTWARE] = "Software"
      tagNameMap[TAG_YEAR] = "Year"
      tagNameMap[TAG_COLLECTION_USER] = "Collection User"
      tagNameMap[TAG_RATING_USER] = "Rating User"
      tagNameMap[TAG_LOCATION_NAME] = "Location Name"
      tagNameMap[TAG_LOCATION_BODY] = "Location Body"
      tagNameMap[TAG_LOCATION_NOTE] = "Location Note"
      tagNameMap[TAG_LOCATION_ROLE] = "Location Role"
      tagNameMap[TAG_LOCATION_DATE] = "Location Date"
      tagNameMap[TAG_DIRECTION_FACING] = "Direction Facing"
      tagNameMap[TAG_DIRECTION_MOTION] = "Direction Motion"
      tagNameMap[TAG_DISPLAY_NAME] = "Display Name"
      tagNameMap[TAG_CONTENT_IDENTIFIER] = "Content Identifier"
      tagNameMap[TAG_MAKE] = "Make"
      tagNameMap[TAG_MODEL] = "Model"
      tagNameMap[TAG_ORIGINATING_SIGNATURE] = "Originating Signature"
      tagNameMap[TAG_PIXEL_DENSITY] = "Pixel Density"
      tagNameMap[0x0400] = "iTunes Info"
      tagNameMap[0x0401] = "Parent Short Title"
      tagNameMap[0x0402] = "Parent Product ID"
      tagNameMap[0x0403] = "Parent Title"
      tagNameMap[0x0404] = "Short Title"
      tagNameMap[0x0405] = "Unknown_AACR?"
      tagNameMap[0x0406] = "Unknown_CDEK?"
      tagNameMap[0x0407] = "Unknown_CDET?"
      tagNameMap[0x0408] = "GUID"
      tagNameMap[0x0409] = "Product Version"
      tagNameMap[0x040A] = "Album Artist"
      tagNameMap[0x040B] = "Apple Store Account Type"
      tagNameMap[0x040C] = "Album"
      tagNameMap[0x040D] = "Apple Store Account"
      tagNameMap[0x040E] = "Album Title ID"
      tagNameMap[0x040F] = "Author"
      tagNameMap[0x0410] = "Category"
      tagNameMap[0x0411] = "Apple Store Catalog ID"
      tagNameMap[0x0412] = "Cover Art"
      tagNameMap[0x0413] = "Compilation"
      tagNameMap[0x0414] = "Copyright"
      tagNameMap[0x0415] = "Description"
      tagNameMap[0x0416] = "Disk Number"
      tagNameMap[0x0417] = "Description"
      tagNameMap[0x0418] = "Episode Global Unique ID"
      tagNameMap[0x0419] = "Genre ID"
      tagNameMap[0x041A] = "Genre"
      tagNameMap[0x041B] = "Grouping"
      tagNameMap[0x041C] = "Google Host Header"
      tagNameMap[0x041D] = "Google Ping Message"
      tagNameMap[0x041E] = "Google Ping URL"
      tagNameMap[0x041F] = "Google Source Data"
      tagNameMap[0x0420] = "Google Start Time"
      tagNameMap[0x0421] = "Google Track Duration"
      tagNameMap[0x0422] = "HD Video"
      tagNameMap[0x0423] = "iTunes U"
      tagNameMap[0x0424] = "Keyword"
      tagNameMap[0x0425] = "Long Description"
      tagNameMap[0x0426] = "Podcast"
      tagNameMap[0x0427] = "Performer"
      tagNameMap[0x0428] = "Play Gap"
      tagNameMap[0x0429] = "Play List ID"
      tagNameMap[0x042A] = "Product ID"
      tagNameMap[0x042B] = "Purchase Date"
      tagNameMap[0x042C] = "Podcast URL"
      tagNameMap[0x042D] = "Rating Percent"
      tagNameMap[0x042E] = "Release Date"
      tagNameMap[0x042F] = "Rating"
      tagNameMap[0x0430] = "Apple Store Country"
      tagNameMap[0x0431] = "Sort Album Artist"
      tagNameMap[0x0432] = "Sort Album"
      tagNameMap[0x0433] = "Sort Artist"
      tagNameMap[0x0434] = "Sort Composer"
      tagNameMap[0x0435] = "Sort Name"
      tagNameMap[0x0436] = "Sort Show"
      tagNameMap[0x0437] = "Media Type"
      tagNameMap[0x0438] = "Title"
      tagNameMap[0x0439] = "Beats Per Minute"
      tagNameMap[0x043A] = "Track Number"
      tagNameMap[0x043B] = "TV Episode ID"
      tagNameMap[0x043C] = "TV Episode"
      tagNameMap[0x043D] = "TV Network Name"
      tagNameMap[0x043E] = "TV Show"
      tagNameMap[0x043F] = "TV Season"
      tagNameMap[0x0440] = "Year"
      tagNameMap[0x0441] = "Artist"
      tagNameMap[0x0442] = "Album"
      tagNameMap[0x0443] = "Comment"
      tagNameMap[0x0444] = "Composer"
      tagNameMap[0x0445] = "Copyright"
      tagNameMap[0x0446] = "Content Create Date"
      tagNameMap[0x0447] = "Description"
      tagNameMap[0x0448] = "Encoded By"
      tagNameMap[0x0449] = "Genre"
      tagNameMap[0x044A] = "Grouping"
      tagNameMap[0x044B] = "Lyrics"
      tagNameMap[0x044C] = "Title"
      tagNameMap[0x044D] = "Narrator"
      tagNameMap[0x044E] = "Publisher"
      tagNameMap[0x044F] = "Encoder"
      tagNameMap[0x0450] = "Track"
      tagNameMap[0x0451] = "Composer"
    }
  }

  override val name: String
    get() = "QuickTime Metadata"

  init {
    setDescriptor(QuickTimeMetadataDescriptor(this))
  }
}
