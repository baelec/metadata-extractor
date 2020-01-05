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

import com.drew.imaging.jpeg.JpegProcessingException
import com.drew.imaging.jpeg.readMetadata
import com.drew.imaging.tiff.TiffProcessingException
import com.drew.imaging.tiff.TiffReader
import com.drew.lang.*
import com.drew.metadata.Directory
import com.drew.metadata.Metadata
import com.drew.metadata.StringValue
import com.drew.metadata.exif.makernotes.*
import com.drew.metadata.icc.IccReader
import com.drew.metadata.iptc.IptcReader
import com.drew.metadata.photoshop.PhotoshopReader
import com.drew.metadata.tiff.DirectoryTiffHandler
import com.drew.metadata.xmp.XmpReader
import java.io.ByteArrayInputStream
import java.io.IOException

/**
 * Implementation of [com.drew.imaging.tiff.TiffHandler] used for handling TIFF tags according to the Exif
 * standard.
 *
 *
 * Includes support for camera manufacturer makernotes.
 *
 * @author Drew Noakes https://drewnoakes.com
 */
open class ExifTiffHandler(metadata: Metadata, parentDirectory: Directory?) : DirectoryTiffHandler(metadata, parentDirectory) {
  @Throws(TiffProcessingException::class)
  override fun setTiffMarker(marker: Int) {
    val standardTiffMarker = 0x002A
    val olympusRawTiffMarker = 0x4F52 // for ORF files
    val olympusRawTiffMarker2 = 0x5352 // for ORF files
    val panasonicRawTiffMarker = 0x0055 // for RW2 files
    when (marker) {
      standardTiffMarker, olympusRawTiffMarker, olympusRawTiffMarker2 -> pushDirectory(ExifIFD0Directory::class.java)
      panasonicRawTiffMarker -> pushDirectory(PanasonicRawIFD0Directory::class.java)
      else -> throw TiffProcessingException(String.format("Unexpected TIFF marker: 0x%X", marker))
    }
  }

  override fun tryEnterSubIfd(tagId: Int): Boolean {
    if (tagId == ExifDirectoryBase.TAG_SUB_IFD_OFFSET) {
      pushDirectory(ExifSubIFDDirectory::class.java)
      return true
    }
    if (_currentDirectory is ExifIFD0Directory || _currentDirectory is PanasonicRawIFD0Directory) {
      if (tagId == ExifIFD0Directory.TAG_EXIF_SUB_IFD_OFFSET) {
        pushDirectory(ExifSubIFDDirectory::class.java)
        return true
      }
      if (tagId == ExifIFD0Directory.TAG_GPS_INFO_OFFSET) {
        pushDirectory(GpsDirectory::class.java)
        return true
      }
    }
    if (_currentDirectory is ExifSubIFDDirectory) {
      if (tagId == ExifSubIFDDirectory.TAG_INTEROP_OFFSET) {
        pushDirectory(ExifInteropDirectory::class.java)
        return true
      }
    }
    if (_currentDirectory is OlympusMakernoteDirectory) { // Note: these also appear in customProcessTag because some are IFD pointers while others begin immediately
// for the same directories
      when (tagId) {
        OlympusMakernoteDirectory.TAG_EQUIPMENT -> {
          pushDirectory(OlympusEquipmentMakernoteDirectory::class.java)
          return true
        }
        OlympusMakernoteDirectory.TAG_CAMERA_SETTINGS -> {
          pushDirectory(OlympusCameraSettingsMakernoteDirectory::class.java)
          return true
        }
        OlympusMakernoteDirectory.TAG_RAW_DEVELOPMENT -> {
          pushDirectory(OlympusRawDevelopmentMakernoteDirectory::class.java)
          return true
        }
        OlympusMakernoteDirectory.TAG_RAW_DEVELOPMENT_2 -> {
          pushDirectory(OlympusRawDevelopment2MakernoteDirectory::class.java)
          return true
        }
        OlympusMakernoteDirectory.TAG_IMAGE_PROCESSING -> {
          pushDirectory(OlympusImageProcessingMakernoteDirectory::class.java)
          return true
        }
        OlympusMakernoteDirectory.TAG_FOCUS_INFO -> {
          pushDirectory(OlympusFocusInfoMakernoteDirectory::class.java)
          return true
        }
        OlympusMakernoteDirectory.TAG_RAW_INFO -> {
          pushDirectory(OlympusRawInfoMakernoteDirectory::class.java)
          return true
        }
        OlympusMakernoteDirectory.TAG_MAIN_INFO -> {
          pushDirectory(OlympusMakernoteDirectory::class.java)
          return true
        }
      }
    }
    return false
  }

  override fun hasFollowerIfd(): Boolean {
    // In Exif, the only known 'follower' IFD is the thumbnail one, however this may not be the case.
    // UPDATE: In multipage TIFFs, the 'follower' IFD points to the next image in the set
    val currentDirectory = _currentDirectory
    if (currentDirectory is ExifIFD0Directory || currentDirectory is ExifImageDirectory) {
      // If the PageNumber tag is defined, assume this is a multipage TIFF or similar
      // TODO: Find better ways to know which follower Directory should be used
      if (currentDirectory.containsTag(ExifDirectoryBase.TAG_PAGE_NUMBER)) pushDirectory(ExifImageDirectory::class.java) else pushDirectory(ExifThumbnailDirectory::class.java)
      return true
    }
    // The Canon EOS 7D (CR2) has three chained/following thumbnail IFDs
    return currentDirectory is ExifThumbnailDirectory
    // This should not happen, as Exif doesn't use follower IFDs apart from that above.
    // NOTE have seen the CanonMakernoteDirectory IFD have a follower pointer, but it points to invalid data.
  }

  override fun tryCustomProcessFormat(tagId: Int, formatCode: Int, componentCount: Long): Long? {
    if (formatCode == 13) return componentCount * 4
    // an unknown (0) formatCode needs to be potentially handled later as a highly custom directory tag
    return if (formatCode == 0) 0L else null
  }

  @Throws(IOException::class)
  override fun customProcessTag(tagOffset: Int,
                                processedIfdOffsets: Set<Int>,
                                tiffHeaderOffset: Int,
                                reader: RandomAccessReader,
                                tagId: Int,
                                byteCount: Int): Boolean {
    assert(_currentDirectory != null)
    val currentDirectory = _currentDirectory ?: return false

    // Some 0x0000 tags have a 0 byteCount. Determine whether it's bad.
    if (tagId == 0) {
      if (currentDirectory.containsTag(tagId)) { // Let it go through for now. Some directories handle it, some don't
        return false
      }
      // Skip over 0x0000 tags that don't have any associated bytes. No idea what it contains in this case, if anything.
      if (byteCount == 0) return true
    }
    // Custom processing for the Makernote tag
    if (tagId == ExifSubIFDDirectory.TAG_MAKERNOTE && currentDirectory is ExifSubIFDDirectory) {
      return processMakernote(tagOffset, processedIfdOffsets, tiffHeaderOffset, reader)
    }
    // Custom processing for embedded IPTC data
    if (tagId == ExifSubIFDDirectory.TAG_IPTC_NAA && currentDirectory is ExifIFD0Directory) { // NOTE Adobe sets type 4 for IPTC instead of 7
      if (reader.getInt8(tagOffset).toInt() == 0x1c) {
        val iptcBytes = reader.getBytes(tagOffset, byteCount)
        IptcReader().extract(SequentialByteArrayReader(iptcBytes), _metadata, iptcBytes.size.toLong(), currentDirectory)
        return true
      }
      return false
    }
    // Custom processing for ICC Profile data
    if (tagId == ExifSubIFDDirectory.TAG_INTER_COLOR_PROFILE) {
      val iccBytes = reader.getBytes(tagOffset, byteCount)
      IccReader().extract(ByteArrayReader(iccBytes), _metadata, currentDirectory)
      return true
    }
    // Custom processing for Photoshop data
    if (tagId == ExifSubIFDDirectory.TAG_PHOTOSHOP_SETTINGS && currentDirectory is ExifIFD0Directory) {
      val photoshopBytes = reader.getBytes(tagOffset, byteCount)
      PhotoshopReader().extract(SequentialByteArrayReader(photoshopBytes), byteCount, _metadata, _currentDirectory)
      return true
    }
    // Custom processing for embedded XMP data
    if (tagId == ExifSubIFDDirectory.TAG_APPLICATION_NOTES && currentDirectory is ExifIFD0Directory) {
      XmpReader().extract(reader.getNullTerminatedBytes(tagOffset, byteCount), _metadata, _currentDirectory)
      return true
    }
    if (handlePrintIM(currentDirectory, tagId)) {
      val printIMDirectory = PrintIMDirectory()
      printIMDirectory.setParent(currentDirectory)
      _metadata.addDirectory(printIMDirectory)
      processPrintIM(printIMDirectory, tagOffset, reader, byteCount)
      return true
    }
    // Note: these also appear in tryEnterSubIfd because some are IFD pointers while others begin immediately
// for the same directories
    if (currentDirectory is OlympusMakernoteDirectory) {
      when (tagId) {
        OlympusMakernoteDirectory.TAG_EQUIPMENT -> {
          pushDirectory(OlympusEquipmentMakernoteDirectory::class.java)
          TiffReader.processIfd(this, reader, processedIfdOffsets, tagOffset, tiffHeaderOffset)
          return true
        }
        OlympusMakernoteDirectory.TAG_CAMERA_SETTINGS -> {
          pushDirectory(OlympusCameraSettingsMakernoteDirectory::class.java)
          TiffReader.processIfd(this, reader, processedIfdOffsets, tagOffset, tiffHeaderOffset)
          return true
        }
        OlympusMakernoteDirectory.TAG_RAW_DEVELOPMENT -> {
          pushDirectory(OlympusRawDevelopmentMakernoteDirectory::class.java)
          TiffReader.processIfd(this, reader, processedIfdOffsets, tagOffset, tiffHeaderOffset)
          return true
        }
        OlympusMakernoteDirectory.TAG_RAW_DEVELOPMENT_2 -> {
          pushDirectory(OlympusRawDevelopment2MakernoteDirectory::class.java)
          TiffReader.processIfd(this, reader, processedIfdOffsets, tagOffset, tiffHeaderOffset)
          return true
        }
        OlympusMakernoteDirectory.TAG_IMAGE_PROCESSING -> {
          pushDirectory(OlympusImageProcessingMakernoteDirectory::class.java)
          TiffReader.processIfd(this, reader, processedIfdOffsets, tagOffset, tiffHeaderOffset)
          return true
        }
        OlympusMakernoteDirectory.TAG_FOCUS_INFO -> {
          pushDirectory(OlympusFocusInfoMakernoteDirectory::class.java)
          TiffReader.processIfd(this, reader, processedIfdOffsets, tagOffset, tiffHeaderOffset)
          return true
        }
        OlympusMakernoteDirectory.TAG_RAW_INFO -> {
          pushDirectory(OlympusRawInfoMakernoteDirectory::class.java)
          TiffReader.processIfd(this, reader, processedIfdOffsets, tagOffset, tiffHeaderOffset)
          return true
        }
        OlympusMakernoteDirectory.TAG_MAIN_INFO -> {
          pushDirectory(OlympusMakernoteDirectory::class.java)
          TiffReader.processIfd(this, reader, processedIfdOffsets, tagOffset, tiffHeaderOffset)
          return true
        }
      }
    }
    if (currentDirectory is PanasonicRawIFD0Directory) { // these contain binary data with specific offsets, and can't be processed as regular ifd's.
      // The binary data is broken into 'fake' tags and there is a pattern.
      when (tagId) {
        PanasonicRawIFD0Directory.TagWbInfo -> {
          val dirWbInfo = PanasonicRawWbInfoDirectory()
          dirWbInfo.setParent(currentDirectory)
          _metadata.addDirectory(dirWbInfo)
          processBinary(dirWbInfo, tagOffset, reader, byteCount, false, 2)
          return true
        }
        PanasonicRawIFD0Directory.TagWbInfo2 -> {
          val dirWbInfo2 = PanasonicRawWbInfo2Directory()
          dirWbInfo2.setParent(currentDirectory)
          _metadata.addDirectory(dirWbInfo2)
          processBinary(dirWbInfo2, tagOffset, reader, byteCount, false, 3)
          return true
        }
        PanasonicRawIFD0Directory.TagDistortionInfo -> {
          val dirDistort = PanasonicRawDistortionDirectory()
          dirDistort.setParent(currentDirectory)
          _metadata.addDirectory(dirDistort)
          processBinary(dirDistort, tagOffset, reader, byteCount, true, 1)
          return true
        }
      }
    }
    // Panasonic RAW sometimes contains an embedded version of the data as a JPG file.
    if (tagId == PanasonicRawIFD0Directory.TagJpgFromRaw && currentDirectory is PanasonicRawIFD0Directory) {
      val jpegrawbytes = reader.getBytes(tagOffset, byteCount)
      // Extract information from embedded image since it is metadata-rich
      val jpegmem = ByteArrayInputStream(jpegrawbytes)
      try {
        val jpegDirectory = readMetadata(jpegmem)
        for (directory in jpegDirectory.directories) {
          directory.setParent(currentDirectory)
          _metadata.addDirectory(directory)
        }
        return true
      } catch (e: JpegProcessingException) {
        currentDirectory.addError("Error processing JpgFromRaw: " + e.message)
      } catch (e: IOException) {
        currentDirectory.addError("Error reading JpgFromRaw: " + e.message)
      }
    }
    return false
  }

  @Throws(IOException::class)
  private fun processMakernote(makernoteOffset: Int,
                               processedIfdOffsets: Set<Int?>,
                               tiffHeaderOffset: Int,
                               reader: RandomAccessReader): Boolean {
    assert(_currentDirectory != null)
    val currentDirectory = _currentDirectory ?: return false
    // Determine the camera model and makernote format.
    val ifd0Directory: Directory? = _metadata.getFirstDirectoryOfType(ExifIFD0Directory::class.java)
    val cameraMake = ifd0Directory?.getString(ExifIFD0Directory.TAG_MAKE)
    val firstTwoChars = getReaderString(reader, makernoteOffset, 2)
    val firstThreeChars = getReaderString(reader, makernoteOffset, 3)
    val firstFourChars = getReaderString(reader, makernoteOffset, 4)
    val firstFiveChars = getReaderString(reader, makernoteOffset, 5)
    val firstSixChars = getReaderString(reader, makernoteOffset, 6)
    val firstSevenChars = getReaderString(reader, makernoteOffset, 7)
    val firstEightChars = getReaderString(reader, makernoteOffset, 8)
    val firstNineChars = getReaderString(reader, makernoteOffset, 9)
    val firstTenChars = getReaderString(reader, makernoteOffset, 10)
    val firstTwelveChars = getReaderString(reader, makernoteOffset, 12)
    val byteOrderBefore = reader.isMotorolaByteOrder
    if ("OLYMP\u0000" == firstSixChars || "EPSON" == firstFiveChars || "AGFA" == firstFourChars) { // Olympus Makernote
      // Epson and Agfa use Olympus makernote standard: http://www.ozhiker.com/electronics/pjmt/jpeg_info/
      pushDirectory(OlympusMakernoteDirectory::class.java)
      TiffReader.processIfd(this, reader, processedIfdOffsets, makernoteOffset + 8, tiffHeaderOffset)
    } else if ("OLYMPUS\u0000II" == firstTenChars) { // Olympus Makernote (alternate)
      // Note that data is relative to the beginning of the makernote
      // http://exiv2.org/makernote.html
      pushDirectory(OlympusMakernoteDirectory::class.java)
      TiffReader.processIfd(this, reader, processedIfdOffsets, makernoteOffset + 12, makernoteOffset)
    } else if (cameraMake != null && cameraMake.toUpperCase().startsWith("MINOLTA")) { // Cases seen with the model starting with MINOLTA in capitals seem to have a valid Olympus makernote
      // area that commences immediately.
      pushDirectory(OlympusMakernoteDirectory::class.java)
      TiffReader.processIfd(this, reader, processedIfdOffsets, makernoteOffset, tiffHeaderOffset)
    } else if (cameraMake != null && cameraMake.trim { it <= ' ' }.toUpperCase().startsWith("NIKON")) {
      if ("Nikon" == firstFiveChars) {
        /* There are two scenarios here:
         * Type 1:                  **
         * :0000: 4E 69 6B 6F 6E 00 01 00-05 00 02 00 02 00 06 00 Nikon...........
         * :0010: 00 00 EC 02 00 00 03 00-03 00 01 00 00 00 06 00 ................
         * Type 3:                  **
         * :0000: 4E 69 6B 6F 6E 00 02 00-00 00 4D 4D 00 2A 00 00 Nikon....MM.*...
         * :0010: 00 08 00 1E 00 01 00 07-00 00 00 04 30 32 30 30 ............0200
         */
        when (reader.getUInt8(makernoteOffset + 6)) {
          1.toShort() -> {
            pushDirectory(NikonType1MakernoteDirectory::class.java)
            TiffReader.processIfd(this, reader, processedIfdOffsets, makernoteOffset + 8, tiffHeaderOffset)
          }
          2.toShort() -> {
            pushDirectory(NikonType2MakernoteDirectory::class.java)
            TiffReader.processIfd(this, reader, processedIfdOffsets, makernoteOffset + 18, makernoteOffset + 10)
          }
          else -> currentDirectory.addError("Unsupported Nikon makernote data ignored.")
        }
      } else { // The IFD begins with the first Makernote byte (no ASCII name).  This occurs with CoolPix 775, E990 and D1 models.
        pushDirectory(NikonType2MakernoteDirectory::class.java)
        TiffReader.processIfd(this, reader, processedIfdOffsets, makernoteOffset, tiffHeaderOffset)
      }
    } else if ("SONY CAM" == firstEightChars || "SONY DSC" == firstEightChars) {
      pushDirectory(SonyType1MakernoteDirectory::class.java)
      TiffReader.processIfd(this, reader, processedIfdOffsets, makernoteOffset + 12, tiffHeaderOffset)
      // Do this check LAST after most other Sony checks
    } else if (cameraMake != null && cameraMake.startsWith("SONY") &&
      !reader.getBytes(makernoteOffset, 2).contentEquals(byteArrayOf(0x01, 0x00))) { // The IFD begins with the first Makernote byte (no ASCII name). Used in SR2 and ARW images
      pushDirectory(SonyType1MakernoteDirectory::class.java)
      TiffReader.processIfd(this, reader, processedIfdOffsets, makernoteOffset, tiffHeaderOffset)
    } else if ("SEMC MS\u0000\u0000\u0000\u0000\u0000" == firstTwelveChars) { // force MM for this directory
      reader.isMotorolaByteOrder = true
      // skip 12 byte header + 2 for "MM" + 6
      pushDirectory(SonyType6MakernoteDirectory::class.java)
      TiffReader.processIfd(this, reader, processedIfdOffsets, makernoteOffset + 20, tiffHeaderOffset)
    } else if ("SIGMA\u0000\u0000\u0000" == firstEightChars || "FOVEON\u0000\u0000" == firstEightChars) {
      pushDirectory(SigmaMakernoteDirectory::class.java)
      TiffReader.processIfd(this, reader, processedIfdOffsets, makernoteOffset + 10, tiffHeaderOffset)
    } else if ("KDK" == firstThreeChars) {
      reader.isMotorolaByteOrder = firstSevenChars == "KDK INFO"
      val directory = KodakMakernoteDirectory()
      _metadata.addDirectory(directory)
      processKodakMakernote(directory, makernoteOffset, reader)
    } else if ("Canon".equals(cameraMake, ignoreCase = true)) {
      pushDirectory(CanonMakernoteDirectory::class.java)
      TiffReader.processIfd(this, reader, processedIfdOffsets, makernoteOffset, tiffHeaderOffset)
    } else if (cameraMake != null && cameraMake.toUpperCase().startsWith("CASIO")) {
      if ("QVC\u0000\u0000\u0000" == firstSixChars) {
        pushDirectory(CasioType2MakernoteDirectory::class.java)
        TiffReader.processIfd(this, reader, processedIfdOffsets, makernoteOffset + 6, tiffHeaderOffset)
      } else {
        pushDirectory(CasioType1MakernoteDirectory::class.java)
        TiffReader.processIfd(this, reader, processedIfdOffsets, makernoteOffset, tiffHeaderOffset)
      }
    } else if ("FUJIFILM" == firstEightChars || "Fujifilm".equals(cameraMake, ignoreCase = true)) { // Note that this also applies to certain Leica cameras, such as the Digilux-4.3
      reader.isMotorolaByteOrder = false
      // the 4 bytes after "FUJIFILM" in the makernote point to the start of the makernote
      // IFD, though the offset is relative to the start of the makernote, not the TIFF
      // header (like everywhere else)
      val ifdStart = makernoteOffset + reader.getInt32(makernoteOffset + 8)
      pushDirectory(FujifilmMakernoteDirectory::class.java)
      TiffReader.processIfd(this, reader, processedIfdOffsets, ifdStart, makernoteOffset)
    } else if ("KYOCERA" == firstSevenChars) { // http://www.ozhiker.com/electronics/pjmt/jpeg_info/kyocera_mn.html
      pushDirectory(KyoceraMakernoteDirectory::class.java)
      TiffReader.processIfd(this, reader, processedIfdOffsets, makernoteOffset + 22, tiffHeaderOffset)
    } else if ("LEICA" == firstFiveChars) {
      reader.isMotorolaByteOrder = false
      // used by the X1/X2/X VARIO/T
      // (X1 starts with "LEICA\0\x01\0", Make is "LEICA CAMERA AG")
      // (X2 starts with "LEICA\0\x05\0", Make is "LEICA CAMERA AG")
      // (X VARIO starts with "LEICA\0\x04\0", Make is "LEICA CAMERA AG")
      // (T (Typ 701) starts with "LEICA\0\0x6", Make is "LEICA CAMERA AG")
      // (X (Typ 113) starts with "LEICA\0\0x7", Make is "LEICA CAMERA AG")
      if ("LEICA\u0000\u0001\u0000" == firstEightChars || "LEICA\u0000\u0004\u0000" == firstEightChars || "LEICA\u0000\u0005\u0000" == firstEightChars || "LEICA\u0000\u0006\u0000" == firstEightChars || "LEICA\u0000\u0007\u0000" == firstEightChars) {
        pushDirectory(LeicaType5MakernoteDirectory::class.java)
        TiffReader.processIfd(this, reader, processedIfdOffsets, makernoteOffset + 8, makernoteOffset)
      } else if ("Leica Camera AG" == cameraMake) {
        pushDirectory(LeicaMakernoteDirectory::class.java)
        TiffReader.processIfd(this, reader, processedIfdOffsets, makernoteOffset + 8, tiffHeaderOffset)
      } else if ("LEICA" == cameraMake) { // Some Leica cameras use Panasonic makernote tags
        pushDirectory(PanasonicMakernoteDirectory::class.java)
        TiffReader.processIfd(this, reader, processedIfdOffsets, makernoteOffset + 8, tiffHeaderOffset)
      } else {
        return false
      }
    } else if ("Panasonic\u0000\u0000\u0000" == firstTwelveChars) { // NON-Standard TIFF IFD Data using Panasonic Tags. There is no Next-IFD pointer after the IFD
      // Offsets are relative to the start of the TIFF header at the beginning of the EXIF segment
      // more information here: http://www.ozhiker.com/electronics/pjmt/jpeg_info/panasonic_mn.html
      pushDirectory(PanasonicMakernoteDirectory::class.java)
      TiffReader.processIfd(this, reader, processedIfdOffsets, makernoteOffset + 12, tiffHeaderOffset)
    } else if ("AOC\u0000" == firstFourChars) { // NON-Standard TIFF IFD Data using Casio Type 2 Tags
      // IFD has no Next-IFD pointer at end of IFD, and
      // Offsets are relative to the start of the current IFD tag, not the TIFF header
      // Observed for:
      // - Pentax ist D
      pushDirectory(CasioType2MakernoteDirectory::class.java)
      TiffReader.processIfd(this, reader, processedIfdOffsets, makernoteOffset + 6, makernoteOffset)
    } else if (cameraMake != null && (cameraMake.toUpperCase().startsWith("PENTAX") || cameraMake.toUpperCase().startsWith("ASAHI"))) { // NON-Standard TIFF IFD Data using Pentax Tags
      // IFD has no Next-IFD pointer at end of IFD, and
      // Offsets are relative to the start of the current IFD tag, not the TIFF header
      // Observed for:
      // - PENTAX Optio 330
      // - PENTAX Optio 430
      pushDirectory(PentaxMakernoteDirectory::class.java)
      TiffReader.processIfd(this, reader, processedIfdOffsets, makernoteOffset, makernoteOffset)
      // } else if ("KC".equals(firstTwoChars) || "MINOL".equals(firstFiveChars) || "MLY".equals(firstThreeChars) || "+M+M+M+M".equals(firstEightChars)) {
      // This Konica data is not understood.  Header identified in accordance with information at this site:
      // http://www.ozhiker.com/electronics/pjmt/jpeg_info/minolta_mn.html
      // TODO add support for minolta/konica cameras
      // exifDirectory.addError("Unsupported Konica/Minolta data ignored.");
    } else if ("SANYO\u0000\u0001\u0000" == firstEightChars) {
      pushDirectory(SanyoMakernoteDirectory::class.java)
      TiffReader.processIfd(this, reader, processedIfdOffsets, makernoteOffset + 8, makernoteOffset)
    } else if (cameraMake != null && cameraMake.toLowerCase().startsWith("ricoh")) {
      if (firstTwoChars == "Rv" || firstThreeChars == "Rev") { // This is a textual format, where the makernote bytes look like:
        //   Rv0103;Rg1C;Bg18;Ll0;Ld0;Aj0000;Bn0473800;Fp2E00:������������������������������
        //   Rv0103;Rg1C;Bg18;Ll0;Ld0;Aj0000;Bn0473800;Fp2D05:������������������������������
        //   Rv0207;Sf6C84;Rg76;Bg60;Gg42;Ll0;Ld0;Aj0004;Bn0B02900;Fp10B8;Md6700;Ln116900086D27;Sv263:0000000000000000000000��
        // This format is currently unsupported
        return false
      } else if (firstFiveChars.equals("Ricoh", ignoreCase = true)) { // Always in Motorola byte order
        reader.isMotorolaByteOrder = true
        pushDirectory(RicohMakernoteDirectory::class.java)
        TiffReader.processIfd(this, reader, processedIfdOffsets, makernoteOffset + 8, makernoteOffset)
      }
    } else if (firstTenChars == "Apple iOS\u0000") { // Always in Motorola byte order
      val orderBefore = reader.isMotorolaByteOrder
      reader.isMotorolaByteOrder = true
      pushDirectory(AppleMakernoteDirectory::class.java)
      TiffReader.processIfd(this, reader, processedIfdOffsets, makernoteOffset + 14, makernoteOffset)
      reader.isMotorolaByteOrder = orderBefore
    } else if (reader.getUInt16(makernoteOffset) == ReconyxHyperFireMakernoteDirectory.MAKERNOTE_VERSION) {
      val directory = ReconyxHyperFireMakernoteDirectory()
      _metadata.addDirectory(directory)
      processReconyxHyperFireMakernote(directory, makernoteOffset, reader)
    } else if (firstNineChars.equals("RECONYXUF", ignoreCase = true)) {
      val directory = ReconyxUltraFireMakernoteDirectory()
      _metadata.addDirectory(directory)
      processReconyxUltraFireMakernote(directory, makernoteOffset, reader)
    } else if ("SAMSUNG" == cameraMake) { // Only handles Type2 notes correctly. Others aren't implemented, and it's complex to determine which ones to use
      pushDirectory(SamsungType2MakernoteDirectory::class.java)
      TiffReader.processIfd(this, reader, processedIfdOffsets, makernoteOffset, tiffHeaderOffset)
    } else { // The makernote is not comprehended by this library.
      // If you are reading this and believe a particular camera's image should be processed, get in touch.
      return false
    }
    reader.isMotorolaByteOrder = byteOrderBefore
    return true
  }

  companion object {
    @Throws(IOException::class)
    private fun processBinary(directory: Directory, tagValueOffset: Int, reader: RandomAccessReader, byteCount: Int, isSigned: Boolean, arrayLength: Int) { // expects signed/unsigned int16 (for now)
      //int byteSize = isSigned ? sizeof(short) : sizeof(ushort);
      val byteSize = 2
      // 'directory' is assumed to contain tags that correspond to the byte position unless it's a set of bytes
      var i = 0
      while (i < byteCount) {
        if (directory.hasTagName(i)) { // only process this tag if the 'next' integral tag exists. Otherwise, it's a set of bytes
          if (i < byteCount - 1 && directory.hasTagName(i + 1)) {
            if (isSigned) directory.setObject(i, reader.getInt16(tagValueOffset + i * byteSize)) else directory.setObject(i, reader.getUInt16(tagValueOffset + i * byteSize))
          } else { // the next arrayLength bytes are a multi-byte value
            if (isSigned) {
              val `val` = ShortArray(arrayLength)
              for (j in `val`.indices) `val`[j] = reader.getInt16(tagValueOffset + (i + j) * byteSize)
              directory.setObjectArray(i, `val`)
            } else {
              val value = IntArray(arrayLength)
              for (j in value.indices) value[j] = reader.getUInt16(tagValueOffset + (i + j) * byteSize)
              directory.setObjectArray(i, value)
            }
            i += arrayLength - 1
          }
        }
        i++
      }
    }

    /** Read a given number of bytes from the stream
     *
     * This method is employed to "suppress" attempts to read beyond end of the
     * file as may happen at the beginning of processMakernote when we read
     * increasingly longer camera makes.
     *
     * Instead of failing altogether in this context we return an empty string
     * which will fail all sensible attempts to compare to makes while avoiding
     * a full-on failure.
     */
    @Throws(IOException::class)
    private fun getReaderString(reader: RandomAccessReader, makernoteOffset: Int, bytesRequested: Int): String {
      return try {
        reader.getString(makernoteOffset, bytesRequested, UTF_8)
      } catch (e: BufferBoundsException) {
        ""
      }
    }

    private fun handlePrintIM(directory: Directory, tagId: Int): Boolean {
      if (tagId == ExifDirectoryBase.TAG_PRINT_IMAGE_MATCHING_INFO) return true
      if (tagId == 0x0E00) { // Tempting to say every tagid of 0x0E00 is a PIM tag, but can't be 100% sure
        if (directory is CasioType2MakernoteDirectory ||
          directory is KyoceraMakernoteDirectory ||
          directory is NikonType2MakernoteDirectory ||
          directory is OlympusMakernoteDirectory ||
          directory is PanasonicMakernoteDirectory ||
          directory is PentaxMakernoteDirectory ||
          directory is RicohMakernoteDirectory ||
          directory is SanyoMakernoteDirectory ||
          directory is SonyType1MakernoteDirectory) return true
      }
      return false
    }

    /// <summary>
    /// Process PrintIM IFD
    /// </summary>
    /// <remarks>
    /// Converted from Exiftool version 10.33 created by Phil Harvey
    /// http://www.sno.phy.queensu.ca/~phil/exiftool/
    /// lib\Image\ExifTool\PrintIM.pm
    /// </remarks>
    @Throws(IOException::class)
    private fun processPrintIM(directory: PrintIMDirectory, tagValueOffset: Int, reader: RandomAccessReader, byteCount: Int) {
      var resetByteOrder: Boolean? = null
      if (byteCount == 0) {
        directory.addError("Empty PrintIM data")
        return
      }
      if (byteCount <= 15) {
        directory.addError("Bad PrintIM data")
        return
      }
      val header: String = reader.getString(tagValueOffset, 12, UTF_8)
      if (!header.startsWith("PrintIM")) {
        directory.addError("Invalid PrintIM header")
        return
      }
      // check size of PrintIM block
      var num = reader.getUInt16(tagValueOffset + 14)
      if (byteCount < 16 + num * 6) { // size is too big, maybe byte ordering is wrong
        resetByteOrder = reader.isMotorolaByteOrder
        reader.isMotorolaByteOrder = !reader.isMotorolaByteOrder
        num = reader.getUInt16(tagValueOffset + 14)
        if (byteCount < 16 + num * 6) {
          directory.addError("Bad PrintIM size")
          return
        }
      }
      directory.setObject(PrintIMDirectory.TagPrintImVersion, header.substring(8, 12))
      for (n in 0 until num) {
        val pos = tagValueOffset + 16 + n * 6
        val tag = reader.getUInt16(pos)
        val `val` = reader.getUInt32(pos + 2)
        directory.setObject(tag, `val`)
      }
      if (resetByteOrder != null) reader.isMotorolaByteOrder = resetByteOrder
    }

    private fun processKodakMakernote(directory: KodakMakernoteDirectory, tagValueOffset: Int, reader: RandomAccessReader) { // Kodak's makernote is not in IFD format. It has values at fixed offsets.
      val dataOffset = tagValueOffset + 8
      try {
        directory.setStringValue(KodakMakernoteDirectory.TAG_KODAK_MODEL, reader.getStringValue(dataOffset, 8, UTF_8))
        directory.setInt(KodakMakernoteDirectory.TAG_QUALITY, reader.getUInt8(dataOffset + 9).toInt())
        directory.setInt(KodakMakernoteDirectory.TAG_BURST_MODE, reader.getUInt8(dataOffset + 10).toInt())
        directory.setInt(KodakMakernoteDirectory.TAG_IMAGE_WIDTH, reader.getUInt16(dataOffset + 12))
        directory.setInt(KodakMakernoteDirectory.TAG_IMAGE_HEIGHT, reader.getUInt16(dataOffset + 14))
        directory.setInt(KodakMakernoteDirectory.TAG_YEAR_CREATED, reader.getUInt16(dataOffset + 16))
        directory.setByteArray(KodakMakernoteDirectory.TAG_MONTH_DAY_CREATED, reader.getBytes(dataOffset + 18, 2))
        directory.setByteArray(KodakMakernoteDirectory.TAG_TIME_CREATED, reader.getBytes(dataOffset + 20, 4))
        directory.setInt(KodakMakernoteDirectory.TAG_BURST_MODE_2, reader.getUInt16(dataOffset + 24))
        directory.setInt(KodakMakernoteDirectory.TAG_SHUTTER_MODE, reader.getUInt8(dataOffset + 27).toInt())
        directory.setInt(KodakMakernoteDirectory.TAG_METERING_MODE, reader.getUInt8(dataOffset + 28).toInt())
        directory.setInt(KodakMakernoteDirectory.TAG_SEQUENCE_NUMBER, reader.getUInt8(dataOffset + 29).toInt())
        directory.setInt(KodakMakernoteDirectory.TAG_F_NUMBER, reader.getUInt16(dataOffset + 30))
        directory.setLong(KodakMakernoteDirectory.TAG_EXPOSURE_TIME, reader.getUInt32(dataOffset + 32))
        directory.setInt(KodakMakernoteDirectory.TAG_EXPOSURE_COMPENSATION, reader.getInt16(dataOffset + 36).toInt())
        directory.setInt(KodakMakernoteDirectory.TAG_FOCUS_MODE, reader.getUInt8(dataOffset + 56).toInt())
        directory.setInt(KodakMakernoteDirectory.TAG_WHITE_BALANCE, reader.getUInt8(dataOffset + 64).toInt())
        directory.setInt(KodakMakernoteDirectory.TAG_FLASH_MODE, reader.getUInt8(dataOffset + 92).toInt())
        directory.setInt(KodakMakernoteDirectory.TAG_FLASH_FIRED, reader.getUInt8(dataOffset + 93).toInt())
        directory.setInt(KodakMakernoteDirectory.TAG_ISO_SETTING, reader.getUInt16(dataOffset + 94))
        directory.setInt(KodakMakernoteDirectory.TAG_ISO, reader.getUInt16(dataOffset + 96))
        directory.setInt(KodakMakernoteDirectory.TAG_TOTAL_ZOOM, reader.getUInt16(dataOffset + 98))
        directory.setInt(KodakMakernoteDirectory.TAG_DATE_TIME_STAMP, reader.getUInt16(dataOffset + 100))
        directory.setInt(KodakMakernoteDirectory.TAG_COLOR_MODE, reader.getUInt16(dataOffset + 102))
        directory.setInt(KodakMakernoteDirectory.TAG_DIGITAL_ZOOM, reader.getUInt16(dataOffset + 104))
        directory.setInt(KodakMakernoteDirectory.TAG_SHARPNESS, reader.getInt8(dataOffset + 107).toInt())
      } catch (ex: IOException) {
        directory.addError("Error processing Kodak makernote data: " + ex.message)
      }
    }

    @Throws(IOException::class)
    private fun processReconyxHyperFireMakernote(directory: ReconyxHyperFireMakernoteDirectory, makernoteOffset: Int, reader: RandomAccessReader) {
      directory.setObject(ReconyxHyperFireMakernoteDirectory.TAG_MAKERNOTE_VERSION, reader.getUInt16(makernoteOffset))
      val major = reader.getUInt16(makernoteOffset + ReconyxHyperFireMakernoteDirectory.TAG_FIRMWARE_VERSION)
      val minor = reader.getUInt16(makernoteOffset + ReconyxHyperFireMakernoteDirectory.TAG_FIRMWARE_VERSION + 2)
      val revision = reader.getUInt16(makernoteOffset + ReconyxHyperFireMakernoteDirectory.TAG_FIRMWARE_VERSION + 4)
      val buildYear = String.format("%04X", reader.getUInt16(makernoteOffset + ReconyxHyperFireMakernoteDirectory.TAG_FIRMWARE_VERSION + 6))
      val buildDate = String.format("%04X", reader.getUInt16(makernoteOffset + ReconyxHyperFireMakernoteDirectory.TAG_FIRMWARE_VERSION + 8))
      val buildYearAndDate = buildYear + buildDate
      val build: Int?
      build = try {
        buildYearAndDate.toInt()
      } catch (e: NumberFormatException) {
        null
      }
      if (build != null) {
        directory.setString(ReconyxHyperFireMakernoteDirectory.TAG_FIRMWARE_VERSION, String.format("%d.%d.%d.%s", major, minor, revision, build))
      } else {
        directory.setString(ReconyxHyperFireMakernoteDirectory.TAG_FIRMWARE_VERSION, String.format("%d.%d.%d", major, minor, revision))
        directory.addError("Error processing Reconyx HyperFire makernote data: build '$buildYearAndDate' is not in the expected format and will be omitted from Firmware Version.")
      }
      directory.setString(ReconyxHyperFireMakernoteDirectory.TAG_TRIGGER_MODE, reader.getUInt16(makernoteOffset + ReconyxHyperFireMakernoteDirectory.TAG_TRIGGER_MODE).toChar().toString())
      directory.setIntArray(ReconyxHyperFireMakernoteDirectory.TAG_SEQUENCE, intArrayOf(
        reader.getUInt16(makernoteOffset + ReconyxHyperFireMakernoteDirectory.TAG_SEQUENCE),
        reader.getUInt16(makernoteOffset + ReconyxHyperFireMakernoteDirectory.TAG_SEQUENCE + 2)
      ))
      val eventNumberHigh = reader.getUInt16(makernoteOffset + ReconyxHyperFireMakernoteDirectory.TAG_EVENT_NUMBER)
      val eventNumberLow = reader.getUInt16(makernoteOffset + ReconyxHyperFireMakernoteDirectory.TAG_EVENT_NUMBER + 2)
      directory.setInt(ReconyxHyperFireMakernoteDirectory.TAG_EVENT_NUMBER, (eventNumberHigh shl 16) + eventNumberLow)
      val seconds = reader.getUInt16(makernoteOffset + ReconyxHyperFireMakernoteDirectory.TAG_DATE_TIME_ORIGINAL)
      val minutes = reader.getUInt16(makernoteOffset + ReconyxHyperFireMakernoteDirectory.TAG_DATE_TIME_ORIGINAL + 2)
      val hour = reader.getUInt16(makernoteOffset + ReconyxHyperFireMakernoteDirectory.TAG_DATE_TIME_ORIGINAL + 4)
      val month = reader.getUInt16(makernoteOffset + ReconyxHyperFireMakernoteDirectory.TAG_DATE_TIME_ORIGINAL + 6)
      val day = reader.getUInt16(makernoteOffset + ReconyxHyperFireMakernoteDirectory.TAG_DATE_TIME_ORIGINAL + 8)
      val year = reader.getUInt16(makernoteOffset + ReconyxHyperFireMakernoteDirectory.TAG_DATE_TIME_ORIGINAL + 10)
      if (seconds in 0..59 &&
        minutes >= 0 && minutes < 60 &&
        hour >= 0 && hour < 24 &&
        month >= 1 && month < 13 &&
        day >= 1 && day < 32 &&
        year >= 1 && year <= 9999) {
        directory.setString(ReconyxHyperFireMakernoteDirectory.TAG_DATE_TIME_ORIGINAL, String.format("%4d:%2d:%2d %2d:%2d:%2d", year, month, day, hour, minutes, seconds))
      } else {
        directory.addError("Error processing Reconyx HyperFire makernote data: Date/Time Original $year-$month-$day $hour:$minutes:$seconds is not a valid date/time.")
      }
      directory.setInt(ReconyxHyperFireMakernoteDirectory.TAG_MOON_PHASE, reader.getUInt16(makernoteOffset + ReconyxHyperFireMakernoteDirectory.TAG_MOON_PHASE))
      directory.setInt(ReconyxHyperFireMakernoteDirectory.TAG_AMBIENT_TEMPERATURE_FAHRENHEIT, reader.getInt16(makernoteOffset + ReconyxHyperFireMakernoteDirectory.TAG_AMBIENT_TEMPERATURE_FAHRENHEIT).toInt())
      directory.setInt(ReconyxHyperFireMakernoteDirectory.TAG_AMBIENT_TEMPERATURE, reader.getInt16(makernoteOffset + ReconyxHyperFireMakernoteDirectory.TAG_AMBIENT_TEMPERATURE).toInt())
      //directory.setByteArray(ReconyxHyperFireMakernoteDirectory.TAG_SERIAL_NUMBER, reader.getBytes(makernoteOffset + ReconyxHyperFireMakernoteDirectory.TAG_SERIAL_NUMBER, 28));
      directory.setStringValue(ReconyxHyperFireMakernoteDirectory.TAG_SERIAL_NUMBER, StringValue(reader.getBytes(makernoteOffset + ReconyxHyperFireMakernoteDirectory.TAG_SERIAL_NUMBER, 28), UTF_16LE))
      // two unread bytes: the serial number's terminating null
      directory.setInt(ReconyxHyperFireMakernoteDirectory.TAG_CONTRAST, reader.getUInt16(makernoteOffset + ReconyxHyperFireMakernoteDirectory.TAG_CONTRAST))
      directory.setInt(ReconyxHyperFireMakernoteDirectory.TAG_BRIGHTNESS, reader.getUInt16(makernoteOffset + ReconyxHyperFireMakernoteDirectory.TAG_BRIGHTNESS))
      directory.setInt(ReconyxHyperFireMakernoteDirectory.TAG_SHARPNESS, reader.getUInt16(makernoteOffset + ReconyxHyperFireMakernoteDirectory.TAG_SHARPNESS))
      directory.setInt(ReconyxHyperFireMakernoteDirectory.TAG_SATURATION, reader.getUInt16(makernoteOffset + ReconyxHyperFireMakernoteDirectory.TAG_SATURATION))
      directory.setInt(ReconyxHyperFireMakernoteDirectory.TAG_INFRARED_ILLUMINATOR, reader.getUInt16(makernoteOffset + ReconyxHyperFireMakernoteDirectory.TAG_INFRARED_ILLUMINATOR))
      directory.setInt(ReconyxHyperFireMakernoteDirectory.TAG_MOTION_SENSITIVITY, reader.getUInt16(makernoteOffset + ReconyxHyperFireMakernoteDirectory.TAG_MOTION_SENSITIVITY))
      directory.setDouble(ReconyxHyperFireMakernoteDirectory.TAG_BATTERY_VOLTAGE, reader.getUInt16(makernoteOffset + ReconyxHyperFireMakernoteDirectory.TAG_BATTERY_VOLTAGE) / 1000.0)
      directory.setString(ReconyxHyperFireMakernoteDirectory.TAG_USER_LABEL, reader.getNullTerminatedString(makernoteOffset + ReconyxHyperFireMakernoteDirectory.TAG_USER_LABEL, 44, UTF_8))
    }

    @Throws(IOException::class)
    private fun processReconyxUltraFireMakernote(directory: ReconyxUltraFireMakernoteDirectory, makernoteOffset: Int, reader: RandomAccessReader) {
      directory.setString(ReconyxUltraFireMakernoteDirectory.TAG_LABEL, reader.getString(makernoteOffset, 9, UTF_8))
      /*uint makernoteID = ByteConvert.FromBigEndianToNative(reader.GetUInt32(makernoteOffset + ReconyxUltraFireMakernoteDirectory.TagMakernoteID));
        directory.Set(ReconyxUltraFireMakernoteDirectory.TagMakernoteID, makernoteID);
        if (makernoteID != ReconyxUltraFireMakernoteDirectory.MAKERNOTE_ID) {
            directory.addError("Error processing Reconyx UltraFire makernote data: unknown Makernote ID 0x" + makernoteID.ToString("x8"));
            return;
        }
        directory.Set(ReconyxUltraFireMakernoteDirectory.TagMakernoteSize, ByteConvert.FromBigEndianToNative(reader.GetUInt32(makernoteOffset + ReconyxUltraFireMakernoteDirectory.TagMakernoteSize)));
        uint makernotePublicID = ByteConvert.FromBigEndianToNative(reader.GetUInt32(makernoteOffset + ReconyxUltraFireMakernoteDirectory.TagMakernotePublicID));
        directory.Set(ReconyxUltraFireMakernoteDirectory.TagMakernotePublicID, makernotePublicID);
        if (makernotePublicID != ReconyxUltraFireMakernoteDirectory.MAKERNOTE_PUBLIC_ID) {
            directory.addError("Error processing Reconyx UltraFire makernote data: unknown Makernote Public ID 0x" + makernotePublicID.ToString("x8"));
            return;
        }*/
      //directory.Set(ReconyxUltraFireMakernoteDirectory.TagMakernotePublicSize, ByteConvert.FromBigEndianToNative(reader.GetUInt16(makernoteOffset + ReconyxUltraFireMakernoteDirectory.TagMakernotePublicSize)));
      //directory.Set(ReconyxUltraFireMakernoteDirectory.TagCameraVersion, ProcessReconyxUltraFireVersion(makernoteOffset + ReconyxUltraFireMakernoteDirectory.TagCameraVersion, reader));
      //directory.Set(ReconyxUltraFireMakernoteDirectory.TagUibVersion, ProcessReconyxUltraFireVersion(makernoteOffset + ReconyxUltraFireMakernoteDirectory.TagUibVersion, reader));
      //directory.Set(ReconyxUltraFireMakernoteDirectory.TagBtlVersion, ProcessReconyxUltraFireVersion(makernoteOffset + ReconyxUltraFireMakernoteDirectory.TagBtlVersion, reader));
      //directory.Set(ReconyxUltraFireMakernoteDirectory.TagPexVersion, ProcessReconyxUltraFireVersion(makernoteOffset + ReconyxUltraFireMakernoteDirectory.TagPexVersion, reader));
      directory.setString(ReconyxUltraFireMakernoteDirectory.TAG_EVENT_TYPE, reader.getString(makernoteOffset + ReconyxUltraFireMakernoteDirectory.TAG_EVENT_TYPE, 1, UTF_8))
      directory.setIntArray(ReconyxUltraFireMakernoteDirectory.TAG_SEQUENCE, intArrayOf(
        reader.getByte(makernoteOffset + ReconyxUltraFireMakernoteDirectory.TAG_SEQUENCE).toInt(),
        reader.getByte(makernoteOffset + ReconyxUltraFireMakernoteDirectory.TAG_SEQUENCE + 1)
          .toInt()))
      //directory.Set(ReconyxUltraFireMakernoteDirectory.TagEventNumber, ByteConvert.FromBigEndianToNative(reader.GetUInt32(makernoteOffset + ReconyxUltraFireMakernoteDirectory.TagEventNumber)));
      val seconds = reader.getByte(makernoteOffset + ReconyxUltraFireMakernoteDirectory.TAG_DATE_TIME_ORIGINAL)
      val minutes = reader.getByte(makernoteOffset + ReconyxUltraFireMakernoteDirectory.TAG_DATE_TIME_ORIGINAL + 1)
      val hour = reader.getByte(makernoteOffset + ReconyxUltraFireMakernoteDirectory.TAG_DATE_TIME_ORIGINAL + 2)
      val day = reader.getByte(makernoteOffset + ReconyxUltraFireMakernoteDirectory.TAG_DATE_TIME_ORIGINAL + 3)
      val month = reader.getByte(makernoteOffset + ReconyxUltraFireMakernoteDirectory.TAG_DATE_TIME_ORIGINAL + 4)
      /*ushort year = ByteConvert.FromBigEndianToNative(reader.GetUInt16(makernoteOffset + ReconyxUltraFireMakernoteDirectory.TagDateTimeOriginal + 5));
        if ((seconds >= 0 && seconds < 60) &&
            (minutes >= 0 && minutes < 60) &&
            (hour >= 0 && hour < 24) &&
            (month >= 1 && month < 13) &&
            (day >= 1 && day < 32) &&
            (year >= 1 && year <= 9999)) {
            directory.Set(ReconyxUltraFireMakernoteDirectory.TAG_DATE_TIME_ORIGINAL, new DateTime(year, month, day, hour, minutes, seconds, DateTimeKind.Unspecified));
        } else {
            directory.addError("Error processing Reconyx UltraFire makernote data: Date/Time Original " + year + "-" + month + "-" + day + " " + hour + ":" + minutes + ":" + seconds + " is not a valid date/time.");
        }*/
      //directory.Set(ReconyxUltraFireMakernoteDirectory.TagDayOfWeek, reader.GetByte(makernoteOffset + ReconyxUltraFireMakernoteDirectory.TagDayOfWeek));
      directory.setInt(ReconyxUltraFireMakernoteDirectory.TAG_MOON_PHASE, reader.getByte(makernoteOffset + ReconyxUltraFireMakernoteDirectory.TAG_MOON_PHASE).toInt())
      //directory.Set(ReconyxUltraFireMakernoteDirectory.TagAmbientTemperatureFahrenheit, ByteConvert.FromBigEndianToNative(reader.GetInt16(makernoteOffset + ReconyxUltraFireMakernoteDirectory.TagAmbientTemperatureFahrenheit)));
      //directory.Set(ReconyxUltraFireMakernoteDirectory.TagAmbientTemperature, ByteConvert.FromBigEndianToNative(reader.GetInt16(makernoteOffset + ReconyxUltraFireMakernoteDirectory.TagAmbientTemperature)));
      directory.setInt(ReconyxUltraFireMakernoteDirectory.TAG_FLASH, reader.getByte(makernoteOffset + ReconyxUltraFireMakernoteDirectory.TAG_FLASH).toInt())
      //directory.Set(ReconyxUltraFireMakernoteDirectory.TagBatteryVoltage, ByteConvert.FromBigEndianToNative(reader.GetUInt16(makernoteOffset + ReconyxUltraFireMakernoteDirectory.TagBatteryVoltage)) / 1000.0);
      directory.setStringValue(ReconyxUltraFireMakernoteDirectory.TAG_SERIAL_NUMBER, StringValue(reader.getBytes(makernoteOffset + ReconyxUltraFireMakernoteDirectory.TAG_SERIAL_NUMBER, 14), UTF_8))
      // unread byte: the serial number's terminating null
      directory.setString(ReconyxUltraFireMakernoteDirectory.TAG_USER_LABEL, reader.getNullTerminatedString(makernoteOffset + ReconyxUltraFireMakernoteDirectory.TAG_USER_LABEL, 20, UTF_8))
    }
  }
}
