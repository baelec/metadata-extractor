/*
 * Copyright 2002-2015 Drew Noakes
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
import java.text.DecimalFormat
import java.util.*
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * Provides human-readable String representations of tag values stored in a [OlympusEquipmentMakernoteDirectory].
 *
 *
 * Some Description functions and the Extender and Lens types lists converted from Exiftool version 10.10 created by Phil Harvey
 * http://www.sno.phy.queensu.ca/~phil/exiftool/
 * lib\Image\ExifTool\Olympus.pm
 *
 * @author Kevin Mott https://github.com/kwhopper
 * @author Drew Noakes https://drewnoakes.com
 */
class OlympusEquipmentMakernoteDescriptor(directory: OlympusEquipmentMakernoteDirectory) : TagDescriptor<OlympusEquipmentMakernoteDirectory>(directory) {
  override fun getDescription(tagType: Int): String? {
    return when (tagType) {
      OlympusEquipmentMakernoteDirectory.TAG_EQUIPMENT_VERSION -> equipmentVersionDescription
      OlympusEquipmentMakernoteDirectory.TAG_CAMERA_TYPE_2 -> cameraType2Description
      OlympusEquipmentMakernoteDirectory.TAG_FOCAL_PLANE_DIAGONAL -> focalPlaneDiagonalDescription
      OlympusEquipmentMakernoteDirectory.TAG_BODY_FIRMWARE_VERSION -> bodyFirmwareVersionDescription
      OlympusEquipmentMakernoteDirectory.TAG_LENS_TYPE -> lensTypeDescription
      OlympusEquipmentMakernoteDirectory.TAG_LENS_FIRMWARE_VERSION -> lensFirmwareVersionDescription
      OlympusEquipmentMakernoteDirectory.TAG_MAX_APERTURE_AT_MIN_FOCAL -> maxApertureAtMinFocalDescription
      OlympusEquipmentMakernoteDirectory.TAG_MAX_APERTURE_AT_MAX_FOCAL -> maxApertureAtMaxFocalDescription
      OlympusEquipmentMakernoteDirectory.TAG_MAX_APERTURE -> maxApertureDescription
      OlympusEquipmentMakernoteDirectory.TAG_LENS_PROPERTIES -> lensPropertiesDescription
      OlympusEquipmentMakernoteDirectory.TAG_EXTENDER -> extenderDescription
      OlympusEquipmentMakernoteDirectory.TAG_FLASH_TYPE -> flashTypeDescription
      OlympusEquipmentMakernoteDirectory.TAG_FLASH_MODEL -> flashModelDescription
      else -> super.getDescription(tagType)
    }
  }

  val equipmentVersionDescription: String?
    get() = getVersionBytesDescription(OlympusEquipmentMakernoteDirectory.TAG_EQUIPMENT_VERSION, 4)

  val cameraType2Description: String?
    get() {
      val cameratype = _directory.getString(OlympusEquipmentMakernoteDirectory.TAG_CAMERA_TYPE_2) ?: return null
      return if (OlympusMakernoteDirectory.OlympusCameraTypes.containsKey(cameratype)) OlympusMakernoteDirectory.OlympusCameraTypes[cameratype] else cameratype
    }

  val focalPlaneDiagonalDescription: String?
    get() = _directory.getString(OlympusEquipmentMakernoteDirectory.TAG_FOCAL_PLANE_DIAGONAL) + " mm"

  val bodyFirmwareVersionDescription: String?
    get() {
      val value = _directory.getInteger(OlympusEquipmentMakernoteDirectory.TAG_BODY_FIRMWARE_VERSION) ?: return null
      val hex = "%04X".format(value)
      return "%s.%s".format(
        hex.substring(0, hex.length - 3),
        hex.substring(hex.length - 3))
    }

  // The String contains six numbers:
  //
  // - Make
  // - Unknown
  // - Model
  // - Sub-model
  // - Unknown
  // - Unknown
  //
  // Only the Make, Model and Sub-model are used to identify the lens type
  val lensTypeDescription: String?
    get() {
      val str = _directory.getString(OlympusEquipmentMakernoteDirectory.TAG_LENS_TYPE) ?: return null
      // The String contains six numbers:
      //
      // - Make
      // - Unknown
      // - Model
      // - Sub-model
      // - Unknown
      // - Unknown
      //
      // Only the Make, Model and Sub-model are used to identify the lens type
      val values = str.split(" ").toTypedArray()
      return if (values.size < 6) null else try {
        val num1 = values[0].toInt()
        val num2 = values[2].toInt()
        val num3 = values[3].toInt()
        _olympusLensTypes["%X %02X %02X".format(num1, num2, num3)]
      } catch (e: NumberFormatException) {
        null
      }
    }

  val lensFirmwareVersionDescription: String?
    get() {
      val value = _directory.getInteger(OlympusEquipmentMakernoteDirectory.TAG_LENS_FIRMWARE_VERSION) ?: return null
      val hex = "%04X".format(value)
      return "%s.%s".format(
        hex.substring(0, hex.length - 3),
        hex.substring(hex.length - 3))
    }

  val maxApertureAtMinFocalDescription: String?
    get() {
      val value = _directory.getInteger(OlympusEquipmentMakernoteDirectory.TAG_MAX_APERTURE_AT_MIN_FOCAL)
        ?: return null
      val format = DecimalFormat("0.#")
      return format.format(CalcMaxAperture(value))
    }

  val maxApertureAtMaxFocalDescription: String?
    get() {
      val value = _directory.getInteger(OlympusEquipmentMakernoteDirectory.TAG_MAX_APERTURE_AT_MAX_FOCAL)
        ?: return null
      val format = DecimalFormat("0.#")
      return format.format(CalcMaxAperture(value))
    }

  val maxApertureDescription: String?
    get() {
      val value = _directory.getInteger(OlympusEquipmentMakernoteDirectory.TAG_MAX_APERTURE) ?: return null
      val format = DecimalFormat("0.#")
      return format.format(CalcMaxAperture(value))
    }

  val lensPropertiesDescription: String?
    get() {
      val value = _directory.getInteger(OlympusEquipmentMakernoteDirectory.TAG_LENS_PROPERTIES) ?: return null
      return String.format("0x%04X", value)
    }

  // The String contains six numbers:
  //
  // - Make
  // - Unknown
  // - Model
  // - Sub-model
  // - Unknown
  // - Unknown
  //
  // Only the Make and Model are used to identify the extender
  val extenderDescription: String?
    get() {
      val str = _directory.getString(OlympusEquipmentMakernoteDirectory.TAG_EXTENDER) ?: return null
      // The String contains six numbers:
      //
      // - Make
      // - Unknown
      // - Model
      // - Sub-model
      // - Unknown
      // - Unknown
      //
      // Only the Make and Model are used to identify the extender
      val values = str.split(" ").toTypedArray()
      return if (values.size < 6) null else try {
        val num1 = values[0].toInt()
        val num2 = values[2].toInt()
        val extenderType = "%X %02X".format(num1, num2)
        _olympusExtenderTypes[extenderType]
      } catch (e: NumberFormatException) {
        null
      }
    }

  val flashTypeDescription: String?
    get() = getIndexedDescription(OlympusEquipmentMakernoteDirectory.TAG_FLASH_TYPE,
      "None", null, "Simple E-System", "E-System")

  val flashModelDescription: String?
    get() = getIndexedDescription(OlympusEquipmentMakernoteDirectory.TAG_FLASH_MODEL,
      "None", "FL-20", "FL-50", "RF-11", "TF-22", "FL-36", "FL-50R", "FL-36R")

  companion object {
    private fun CalcMaxAperture(value: Int): Double {
      return sqrt(2.00).pow(value / 256.0)
    }

    private val _olympusLensTypes = HashMap<String, String>()
    private val _olympusExtenderTypes = HashMap<String, String>()

    init {
      _olympusLensTypes["0 00 00"] = "None"
      // Olympus lenses (also Kenko Tokina)
      _olympusLensTypes["0 01 00"] = "Olympus Zuiko Digital ED 50mm F2.0 Macro"
      _olympusLensTypes["0 01 01"] = "Olympus Zuiko Digital 40-150mm F3.5-4.5" //8
      _olympusLensTypes["0 01 10"] = "Olympus M.Zuiko Digital ED 14-42mm F3.5-5.6" //PH (E-P1 pre-production)
      _olympusLensTypes["0 02 00"] = "Olympus Zuiko Digital ED 150mm F2.0"
      _olympusLensTypes["0 02 10"] = "Olympus M.Zuiko Digital 17mm F2.8 Pancake" //PH (E-P1 pre-production)
      _olympusLensTypes["0 03 00"] = "Olympus Zuiko Digital ED 300mm F2.8"
      _olympusLensTypes["0 03 10"] = "Olympus M.Zuiko Digital ED 14-150mm F4.0-5.6 [II]" //11 (The second version of this lens seems to have the same lens ID number as the first version #20)
      _olympusLensTypes["0 04 10"] = "Olympus M.Zuiko Digital ED 9-18mm F4.0-5.6" //11
      _olympusLensTypes["0 05 00"] = "Olympus Zuiko Digital 14-54mm F2.8-3.5"
      _olympusLensTypes["0 05 01"] = "Olympus Zuiko Digital Pro ED 90-250mm F2.8" //9
      _olympusLensTypes["0 05 10"] = "Olympus M.Zuiko Digital ED 14-42mm F3.5-5.6 L" //11 (E-PL1)
      _olympusLensTypes["0 06 00"] = "Olympus Zuiko Digital ED 50-200mm F2.8-3.5"
      _olympusLensTypes["0 06 01"] = "Olympus Zuiko Digital ED 8mm F3.5 Fisheye" //9
      _olympusLensTypes["0 06 10"] = "Olympus M.Zuiko Digital ED 40-150mm F4.0-5.6" //PH
      _olympusLensTypes["0 07 00"] = "Olympus Zuiko Digital 11-22mm F2.8-3.5"
      _olympusLensTypes["0 07 01"] = "Olympus Zuiko Digital 18-180mm F3.5-6.3" //6
      _olympusLensTypes["0 07 10"] = "Olympus M.Zuiko Digital ED 12mm F2.0" //PH
      _olympusLensTypes["0 08 01"] = "Olympus Zuiko Digital 70-300mm F4.0-5.6" //7 (seen as release 1 - PH)
      _olympusLensTypes["0 08 10"] = "Olympus M.Zuiko Digital ED 75-300mm F4.8-6.7" //PH
      _olympusLensTypes["0 09 10"] = "Olympus M.Zuiko Digital 14-42mm F3.5-5.6 II" //PH (E-PL2)
      _olympusLensTypes["0 10 01"] = "Kenko Tokina Reflex 300mm F6.3 MF Macro" //20
      _olympusLensTypes["0 10 10"] = "Olympus M.Zuiko Digital ED 12-50mm F3.5-6.3 EZ" //PH
      _olympusLensTypes["0 11 10"] = "Olympus M.Zuiko Digital 45mm F1.8" //17
      _olympusLensTypes["0 12 10"] = "Olympus M.Zuiko Digital ED 60mm F2.8 Macro" //20
      _olympusLensTypes["0 13 10"] = "Olympus M.Zuiko Digital 14-42mm F3.5-5.6 II R" //PH/20
      _olympusLensTypes["0 14 10"] = "Olympus M.Zuiko Digital ED 40-150mm F4.0-5.6 R" //19
      // '0 14 10.1", "Olympus M.Zuiko Digital ED 14-150mm F4.0-5.6 II"); //11 (questionable & unconfirmed -- all samples I can find are '0 3 10' - PH)
      _olympusLensTypes["0 15 00"] = "Olympus Zuiko Digital ED 7-14mm F4.0"
      _olympusLensTypes["0 15 10"] = "Olympus M.Zuiko Digital ED 75mm F1.8" //PH
      _olympusLensTypes["0 16 10"] = "Olympus M.Zuiko Digital 17mm F1.8" //20
      _olympusLensTypes["0 17 00"] = "Olympus Zuiko Digital Pro ED 35-100mm F2.0" //7
      _olympusLensTypes["0 18 00"] = "Olympus Zuiko Digital 14-45mm F3.5-5.6"
      _olympusLensTypes["0 18 10"] = "Olympus M.Zuiko Digital ED 75-300mm F4.8-6.7 II" //20
      _olympusLensTypes["0 19 10"] = "Olympus M.Zuiko Digital ED 12-40mm F2.8 Pro" //PH
      _olympusLensTypes["0 20 00"] = "Olympus Zuiko Digital 35mm F3.5 Macro" //9
      _olympusLensTypes["0 20 10"] = "Olympus M.Zuiko Digital ED 40-150mm F2.8 Pro" //20
      _olympusLensTypes["0 21 10"] = "Olympus M.Zuiko Digital ED 14-42mm F3.5-5.6 EZ" //20
      _olympusLensTypes["0 22 00"] = "Olympus Zuiko Digital 17.5-45mm F3.5-5.6" //9
      _olympusLensTypes["0 22 10"] = "Olympus M.Zuiko Digital 25mm F1.8" //20
      _olympusLensTypes["0 23 00"] = "Olympus Zuiko Digital ED 14-42mm F3.5-5.6" //PH
      _olympusLensTypes["0 23 10"] = "Olympus M.Zuiko Digital ED 7-14mm F2.8 Pro" //20
      _olympusLensTypes["0 24 00"] = "Olympus Zuiko Digital ED 40-150mm F4.0-5.6" //PH
      _olympusLensTypes["0 24 10"] = "Olympus M.Zuiko Digital ED 300mm F4.0 IS Pro" //20
      _olympusLensTypes["0 25 10"] = "Olympus M.Zuiko Digital ED 8mm F1.8 Fisheye Pro" //20
      _olympusLensTypes["0 30 00"] = "Olympus Zuiko Digital ED 50-200mm F2.8-3.5 SWD" //7
      _olympusLensTypes["0 31 00"] = "Olympus Zuiko Digital ED 12-60mm F2.8-4.0 SWD" //7
      _olympusLensTypes["0 32 00"] = "Olympus Zuiko Digital ED 14-35mm F2.0 SWD" //PH
      _olympusLensTypes["0 33 00"] = "Olympus Zuiko Digital 25mm F2.8" //PH
      _olympusLensTypes["0 34 00"] = "Olympus Zuiko Digital ED 9-18mm F4.0-5.6" //7
      _olympusLensTypes["0 35 00"] = "Olympus Zuiko Digital 14-54mm F2.8-3.5 II" //PH
      // Sigma lenses
      _olympusLensTypes["1 01 00"] = "Sigma 18-50mm F3.5-5.6 DC" //8
      _olympusLensTypes["1 01 10"] = "Sigma 30mm F2.8 EX DN" //20
      _olympusLensTypes["1 02 00"] = "Sigma 55-200mm F4.0-5.6 DC"
      _olympusLensTypes["1 02 10"] = "Sigma 19mm F2.8 EX DN" //20
      _olympusLensTypes["1 03 00"] = "Sigma 18-125mm F3.5-5.6 DC"
      _olympusLensTypes["1 03 10"] = "Sigma 30mm F2.8 DN | A" //20
      _olympusLensTypes["1 04 00"] = "Sigma 18-125mm F3.5-5.6 DC" //7
      _olympusLensTypes["1 04 10"] = "Sigma 19mm F2.8 DN | A" //20
      _olympusLensTypes["1 05 00"] = "Sigma 30mm F1.4 EX DC HSM" //10
      _olympusLensTypes["1 05 10"] = "Sigma 60mm F2.8 DN | A" //20
      _olympusLensTypes["1 06 00"] = "Sigma APO 50-500mm F4.0-6.3 EX DG HSM" //6
      _olympusLensTypes["1 07 00"] = "Sigma Macro 105mm F2.8 EX DG" //PH
      _olympusLensTypes["1 08 00"] = "Sigma APO Macro 150mm F2.8 EX DG HSM" //PH
      _olympusLensTypes["1 09 00"] = "Sigma 18-50mm F2.8 EX DC Macro" //20
      _olympusLensTypes["1 10 00"] = "Sigma 24mm F1.8 EX DG Aspherical Macro" //PH
      _olympusLensTypes["1 11 00"] = "Sigma APO 135-400mm F4.5-5.6 DG" //11
      _olympusLensTypes["1 12 00"] = "Sigma APO 300-800mm F5.6 EX DG HSM" //11
      _olympusLensTypes["1 13 00"] = "Sigma 30mm F1.4 EX DC HSM" //11
      _olympusLensTypes["1 14 00"] = "Sigma APO 50-500mm F4.0-6.3 EX DG HSM" //11
      _olympusLensTypes["1 15 00"] = "Sigma 10-20mm F4.0-5.6 EX DC HSM" //11
      _olympusLensTypes["1 16 00"] = "Sigma APO 70-200mm F2.8 II EX DG Macro HSM" //11
      _olympusLensTypes["1 17 00"] = "Sigma 50mm F1.4 EX DG HSM" //11
      // Panasonic/Leica lenses
      _olympusLensTypes["2 01 00"] = "Leica D Vario Elmarit 14-50mm F2.8-3.5 Asph." //11
      _olympusLensTypes["2 01 10"] = "Lumix G Vario 14-45mm F3.5-5.6 Asph. Mega OIS" //16
      _olympusLensTypes["2 02 00"] = "Leica D Summilux 25mm F1.4 Asph." //11
      _olympusLensTypes["2 02 10"] = "Lumix G Vario 45-200mm F4.0-5.6 Mega OIS" //16
      _olympusLensTypes["2 03 00"] = "Leica D Vario Elmar 14-50mm F3.8-5.6 Asph. Mega OIS" //11
      _olympusLensTypes["2 03 01"] = "Leica D Vario Elmar 14-50mm F3.8-5.6 Asph." //14 (L10 kit)
      _olympusLensTypes["2 03 10"] = "Lumix G Vario HD 14-140mm F4.0-5.8 Asph. Mega OIS" //16
      _olympusLensTypes["2 04 00"] = "Leica D Vario Elmar 14-150mm F3.5-5.6" //13
      _olympusLensTypes["2 04 10"] = "Lumix G Vario 7-14mm F4.0 Asph." //PH (E-P1 pre-production)
      _olympusLensTypes["2 05 10"] = "Lumix G 20mm F1.7 Asph." //16
      _olympusLensTypes["2 06 10"] = "Leica DG Macro-Elmarit 45mm F2.8 Asph. Mega OIS" //PH
      _olympusLensTypes["2 07 10"] = "Lumix G Vario 14-42mm F3.5-5.6 Asph. Mega OIS" //20
      _olympusLensTypes["2 08 10"] = "Lumix G Fisheye 8mm F3.5" //PH
      _olympusLensTypes["2 09 10"] = "Lumix G Vario 100-300mm F4.0-5.6 Mega OIS" //11
      _olympusLensTypes["2 10 10"] = "Lumix G 14mm F2.5 Asph." //17
      _olympusLensTypes["2 11 10"] = "Lumix G 12.5mm F12 3D" //20 (H-FT012)
      _olympusLensTypes["2 12 10"] = "Leica DG Summilux 25mm F1.4 Asph." //20
      _olympusLensTypes["2 13 10"] = "Lumix G X Vario PZ 45-175mm F4.0-5.6 Asph. Power OIS" //20
      _olympusLensTypes["2 14 10"] = "Lumix G X Vario PZ 14-42mm F3.5-5.6 Asph. Power OIS" //20
      _olympusLensTypes["2 15 10"] = "Lumix G X Vario 12-35mm F2.8 Asph. Power OIS" //PH
      _olympusLensTypes["2 16 10"] = "Lumix G Vario 45-150mm F4.0-5.6 Asph. Mega OIS" //20
      _olympusLensTypes["2 17 10"] = "Lumix G X Vario 35-100mm F2.8 Power OIS" //PH
      _olympusLensTypes["2 18 10"] = "Lumix G Vario 14-42mm F3.5-5.6 II Asph. Mega OIS" //20
      _olympusLensTypes["2 19 10"] = "Lumix G Vario 14-140mm F3.5-5.6 Asph. Power OIS" //20
      _olympusLensTypes["2 20 10"] = "Lumix G Vario 12-32mm F3.5-5.6 Asph. Mega OIS" //20
      _olympusLensTypes["2 21 10"] = "Leica DG Nocticron 42.5mm F1.2 Asph. Power OIS" //20
      _olympusLensTypes["2 22 10"] = "Leica DG Summilux 15mm F1.7 Asph." //20
      // '2 23 10", "Lumix G Vario 35-100mm F4.0-5.6 Asph. Mega OIS"); //20 (guess)
      _olympusLensTypes["2 24 10"] = "Lumix G Macro 30mm F2.8 Asph. Mega OIS" //20
      _olympusLensTypes["2 25 10"] = "Lumix G 42.5mm F1.7 Asph. Power OIS" //20
      _olympusLensTypes["3 01 00"] = "Leica D Vario Elmarit 14-50mm F2.8-3.5 Asph." //11
      _olympusLensTypes["3 02 00"] = "Leica D Summilux 25mm F1.4 Asph." //11
      // Tamron lenses
      _olympusLensTypes["5 01 10"] = "Tamron 14-150mm F3.5-5.8 Di III" //20 (model C001)
      _olympusExtenderTypes["0 00"] = "None"
      _olympusExtenderTypes["0 04"] = "Olympus Zuiko Digital EC-14 1.4x Teleconverter"
      _olympusExtenderTypes["0 08"] = "Olympus EX-25 Extension Tube"
      _olympusExtenderTypes["0 10"] = "Olympus Zuiko Digital EC-20 2.0x Teleconverter"
    }
  }
}
