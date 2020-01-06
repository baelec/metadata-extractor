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

/**
 * Provides human-readable string representations of tag values stored in a [SamsungType2MakernoteDirectory].
 *
 *
 * Tag reference from: http://www.sno.phy.queensu.ca/~phil/exiftool/TagNames/Samsung.html
 *
 * @author Kevin Mott https://github.com/kwhopper
 * @author Drew Noakes https://drewnoakes.com
 */
class SamsungType2MakernoteDescriptor(directory: SamsungType2MakernoteDirectory) : TagDescriptor<SamsungType2MakernoteDirectory>(directory) {
  override fun getDescription(tagType: Int): String? {
    return when (tagType) {
      SamsungType2MakernoteDirectory.TagMakerNoteVersion -> makernoteVersionDescription
      SamsungType2MakernoteDirectory.TagDeviceType -> deviceTypeDescription
      SamsungType2MakernoteDirectory.TagSamsungModelId -> samsungModelIdDescription
      SamsungType2MakernoteDirectory.TagCameraTemperature -> cameraTemperatureDescription
      SamsungType2MakernoteDirectory.TagFaceDetect -> faceDetectDescription
      SamsungType2MakernoteDirectory.TagFaceRecognition -> faceRecognitionDescription
      else -> super.getDescription(tagType)
    }
  }

  val makernoteVersionDescription: String?
    get() = getVersionBytesDescription(SamsungType2MakernoteDirectory.TagMakerNoteVersion, 2)

  val deviceTypeDescription: String?
    get() {
      val value = _directory.getInteger(SamsungType2MakernoteDirectory.TagDeviceType) ?: return null
      return when (value) {
        0x1000 -> "Compact Digital Camera"
        0x2000 -> "High-end NX Camera"
        0x3000 -> "HXM Video Camera"
        0x12000 -> "Cell Phone"
        0x300000 -> "SMX Video Camera"
        else -> "Unknown (%d)".format(value)
      }
    }

  val samsungModelIdDescription: String?
    get() {
      val value = _directory.getInteger(SamsungType2MakernoteDirectory.TagSamsungModelId) ?: return null
      return when (value) {
        0x100101c -> "NX10"
        0x1001226 -> "HMX-S15BP"
        0x1001233 -> "HMX-Q10"
        0x1001234 -> "HMX-H304"
        0x100130c -> "NX100"
        0x1001327 -> "NX11"
        0x170104e -> "ES70, ES71 / VLUU ES70, ES71 / SL600"
        0x1701052 -> "ES73 / VLUU ES73 / SL605"
        0x1701300 -> "ES28 / VLUU ES28"
        0x1701303 -> "ES74,ES75,ES78 / VLUU ES75,ES78"
        0x2001046 -> "PL150 / VLUU PL150 / TL210 / PL151"
        0x2001311 -> "PL120,PL121 / VLUU PL120,PL121"
        0x2001315 -> "PL170,PL171 / VLUUPL170,PL171"
        0x200131e -> "PL210, PL211 / VLUU PL210, PL211"
        0x2701317 -> "PL20,PL21 / VLUU PL20,PL21"
        0x2a0001b -> "WP10 / VLUU WP10 / AQ100"
        0x3000000 -> "Various Models (0x3000000)"
        0x3a00018 -> "Various Models (0x3a00018)"
        0x400101f -> "ST1000 / ST1100 / VLUU ST1000 / CL65"
        0x4001022 -> "ST550 / VLUU ST550 / TL225"
        0x4001025 -> "Various Models (0x4001025)"
        0x400103e -> "VLUU ST5500, ST5500, CL80"
        0x4001041 -> "VLUU ST5000, ST5000, TL240"
        0x4001043 -> "ST70 / VLUU ST70 / ST71"
        0x400130a -> "Various Models (0x400130a)"
        0x400130e -> "ST90,ST91 / VLUU ST90,ST91"
        0x4001313 -> "VLUU ST95, ST95"
        0x4a00015 -> "VLUU ST60"
        0x4a0135b -> "ST30, ST65 / VLUU ST65 / ST67"
        0x5000000 -> "Various Models (0x5000000)"
        0x5001038 -> "Various Models (0x5001038)"
        0x500103a -> "WB650 / VLUU WB650 / WB660"
        0x500103c -> "WB600 / VLUU WB600 / WB610"
        0x500133e -> "WB150 / WB150F / WB152 / WB152F / WB151"
        0x5a0000f -> "WB5000 / HZ25W"
        0x6001036 -> "EX1"
        0x700131c -> "VLUU SH100, SH100"
        0x27127002 -> "SMX - C20N"
        else -> String.format("Unknown (%d)", value)
      }
    }

  private val cameraTemperatureDescription: String?
    private get() = getFormattedInt(SamsungType2MakernoteDirectory.TagCameraTemperature, "%d C")

  val faceDetectDescription: String?
    get() = getIndexedDescription(SamsungType2MakernoteDirectory.TagFaceDetect,
      "Off", "On")

  val faceRecognitionDescription: String?
    get() = getIndexedDescription(SamsungType2MakernoteDirectory.TagFaceRecognition,
      "Off", "On")
}
