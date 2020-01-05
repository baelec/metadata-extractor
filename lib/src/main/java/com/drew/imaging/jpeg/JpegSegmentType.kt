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
package com.drew.imaging.jpeg

/**
 * An enumeration of the known segment types found in JPEG files.
 *
 *
 *  * http://www.ozhiker.com/electronics/pjmt/jpeg_info/app_segments.html
 *  * http://www.sno.phy.queensu.ca/~phil/exiftool/TagNames/JPEG.html
 *
 *
 * @author Drew Noakes https://drewnoakes.com
 */
enum class JpegSegmentType(val byteValue: Byte, val canContainMetadata: Boolean) {
  /** APP0 JPEG segment identifier. Commonly contains JFIF, JFXX.  */
  APP0(0xE0.toByte(), true),
  /** APP1 JPEG segment identifier. Commonly contains Exif. XMP data is also kept in here, though usually in a second instance.  */
  APP1(0xE1.toByte(), true),
  /** APP2 JPEG segment identifier. Commonly contains ICC.  */
  APP2(0xE2.toByte(), true),
  /** APP3 JPEG segment identifier.  */
  APP3(0xE3.toByte(), true),
  /** APP4 JPEG segment identifier.  */
  APP4(0xE4.toByte(), true),
  /** APP5 JPEG segment identifier.  */
  APP5(0xE5.toByte(), true),
  /** APP6 JPEG segment identifier.  */
  APP6(0xE6.toByte(), true),
  /** APP7 JPEG segment identifier.  */
  APP7(0xE7.toByte(), true),
  /** APP8 JPEG segment identifier.  */
  APP8(0xE8.toByte(), true),
  /** APP9 JPEG segment identifier.  */
  APP9(0xE9.toByte(), true),
  /** APPA (App10) JPEG segment identifier. Can contain Unicode comments, though [JpegSegmentType.COM] is more commonly used for comments.  */
  APPA(0xEA.toByte(), true),
  /** APPB (App11) JPEG segment identifier.  */
  APPB(0xEB.toByte(), true),
  /** APPC (App12) JPEG segment identifier.  */
  APPC(0xEC.toByte(), true),
  /** APPD (App13) JPEG segment identifier. Commonly contains IPTC, Photoshop data.  */
  APPD(0xED.toByte(), true),
  /** APPE (App14) JPEG segment identifier. Commonly contains Adobe data.  */
  APPE(0xEE.toByte(), true),
  /** APPF (App15) JPEG segment identifier.  */
  APPF(0xEF.toByte(), true),
  /** Start Of Image segment identifier.  */
  SOI(0xD8.toByte(), false),
  /** Define Quantization Table segment identifier.  */
  DQT(0xDB.toByte(), false),
  /** Define Number of Lines segment identifier.  */
  DNL(0xDC.toByte(), false),
  /** Define Restart Interval segment identifier.  */
  DRI(0xDD.toByte(), false),
  /** Define Hierarchical Progression segment identifier.  */
  DHP(0xDE.toByte(), false),
  /** EXPand reference component(s) segment identifier.  */
  EXP(0xDF.toByte(), false),
  /** Define Huffman Table segment identifier.  */
  DHT(0xC4.toByte(), false),
  /** Define Arithmetic Coding conditioning segment identifier.  */
  DAC(0xCC.toByte(), false),
  /** Start-of-Frame (0) segment identifier for Baseline DCT.  */
  SOF0(0xC0.toByte(), true),
  /** Start-of-Frame (1) segment identifier for Extended sequential DCT.  */
  SOF1(0xC1.toByte(), true),
  /** Start-of-Frame (2) segment identifier for Progressive DCT.  */
  SOF2(0xC2.toByte(), true),
  /** Start-of-Frame (3) segment identifier for Lossless (sequential).  */
  SOF3(0xC3.toByte(), true),  //    /** Start-of-Frame (4) segment identifier. */
//    SOF4((byte)0xC4, true),
  /** Start-of-Frame (5) segment identifier for Differential sequential DCT.  */
  SOF5(0xC5.toByte(), true),
  /** Start-of-Frame (6) segment identifier for Differential progressive DCT.  */
  SOF6(0xC6.toByte(), true),
  /** Start-of-Frame (7) segment identifier for Differential lossless (sequential).  */
  SOF7(0xC7.toByte(), true),
  /** Reserved for JPEG extensions.  */
  JPG(0xC8.toByte(), true),
  /** Start-of-Frame (9) segment identifier for Extended sequential DCT.  */
  SOF9(0xC9.toByte(), true),
  /** Start-of-Frame (10) segment identifier for Progressive DCT.  */
  SOF10(0xCA.toByte(), true),
  /** Start-of-Frame (11) segment identifier for Lossless (sequential).  */
  SOF11(0xCB.toByte(), true),  //    /** Start-of-Frame (12) segment identifier. */
//    SOF12((byte)0xCC, true),
  /** Start-of-Frame (13) segment identifier for Differential sequential DCT.  */
  SOF13(0xCD.toByte(), true),
  /** Start-of-Frame (14) segment identifier for Differential progressive DCT.  */
  SOF14(0xCE.toByte(), true),
  /** Start-of-Frame (15) segment identifier for Differential lossless (sequential).  */
  SOF15(0xCF.toByte(), true),
  /** JPEG comment segment identifier for comments.  */
  COM(0xFE.toByte(), true);

  companion object {
    val canContainMetadataTypes: Collection<JpegSegmentType> by lazy {
      val segmentTypes = ArrayList<JpegSegmentType>()
      for (segmentType in JpegSegmentType::class.java.enumConstants) {
        if (segmentType.canContainMetadata) {
          segmentTypes.add(segmentType)
        }
      }
      segmentTypes
    }
    @JvmStatic
    fun fromByte(segmentTypeByte: Byte): JpegSegmentType? {
      for (segmentType in JpegSegmentType::class.java.enumConstants) {
        if (segmentType.byteValue == segmentTypeByte) return segmentType
      }
      return null
    }
  }
}
