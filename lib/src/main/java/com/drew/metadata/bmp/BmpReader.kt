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
package com.drew.metadata.bmp

import com.drew.lang.ByteArrayReader
import com.drew.lang.SequentialReader
import com.drew.lang.WINDOWS_1252
import com.drew.metadata.Directory
import com.drew.metadata.ErrorDirectory
import com.drew.metadata.Metadata
import com.drew.metadata.MetadataException
import com.drew.metadata.icc.IccReader
import java.io.IOException

/**
 * Reader for Windows and OS/2 bitmap files.
 *
 *
 * References:
 *
 *  * [Fileformats Wiki BMP overview](https://web.archive.org/web/20170302205626/http://fileformats.archiveteam.org/wiki/BMP)
 *  * [Graphics File Formats encyclopedia Windows bitmap description](http://web.archive.org/web/20170303000822/http://netghost.narod.ru/gff/graphics/summary/micbmp.htm)
 *  * [Graphics File Formats encyclopedia OS/2 bitmap description](https://web.archive.org/web/20170302205330/http://netghost.narod.ru/gff/graphics/summary/os2bmp.htm)
 *  * [OS/2 bitmap specification](https://web.archive.org/web/20170302205457/http://www.fileformat.info/format/os2bmp/spec/902d5c253f2a43ada39c2b81034f27fd/view.htm)
 *  * [Microsoft Bitmap Structures](https://msdn.microsoft.com/en-us/library/dd183392(v=vs.85).aspx)
 *
 *
 * @author Drew Noakes https://drewnoakes.com
 * @author Nadahar
 */
class BmpReader {
  fun extract(reader: SequentialReader, metadata: Metadata) {
    reader.isMotorolaByteOrder = false
    // BITMAP INFORMATION HEADER
    //
    // The first four bytes of the header give the size, which is a discriminator of the actual header format.
    // See this for more information http://en.wikipedia.org/wiki/BMP_file_format
    readFileHeader(reader, metadata, true)
  }

  protected fun readFileHeader(reader: SequentialReader, metadata: Metadata, allowArray: Boolean) { /*
         * There are two possible headers a file can start with. If the magic
         * number is OS/2 Bitmap Array (0x4142) the OS/2 Bitmap Array Header
         * will follow. In all other cases the file header will follow, which
         * is identical for Windows and OS/2.
         *
         * File header:
         *
         *    WORD   FileType;      - File type identifier
         *    DWORD  FileSize;      - Size of the file in bytes
         *    WORD   XHotSpot;      - X coordinate of hotspot for pointers
         *    WORD   YHotSpot;      - Y coordinate of hotspot for pointers
         *    DWORD  BitmapOffset;  - Starting position of image data in bytes
         *
         * OS/2 Bitmap Array Header:
         *
         *     WORD  Type;          - Header type, always 4142h ("BA")
         *     DWORD Size;          - Size of this header
         *     DWORD OffsetToNext;  - Offset to next OS2BMPARRAYFILEHEADER
         *     WORD  ScreenWidth;   - Width of the image display in pixels
         *     WORD  ScreenHeight;  - Height of the image display in pixels
         *
         */
    val magicNumber: Int
    magicNumber = try {
      reader.getUInt16()
    } catch (e: IOException) {
      metadata.addDirectory(ErrorDirectory("Couldn't determine bitmap type: " + e.message))
      return
    }
    var directory: Directory? = null
    try {
      when (magicNumber) {
        OS2_BITMAP_ARRAY -> {
          if (!allowArray) {
            addError("Invalid bitmap file - nested arrays not allowed", metadata)
            return
          }
          reader.skip(4) // Size
          val nextHeaderOffset = reader.getUInt32()
          reader.skip(2 + 2.toLong()) // Screen Resolution
          readFileHeader(reader, metadata, false)
          if (nextHeaderOffset == 0L) {
            return  // No more bitmaps
          }
          if (reader.position > nextHeaderOffset) {
            addError("Invalid next header offset", metadata)
            return
          }
          reader.skip(nextHeaderOffset - reader.position)
          readFileHeader(reader, metadata, true)
        }
        BITMAP, OS2_ICON, OS2_COLOR_ICON, OS2_COLOR_POINTER, OS2_POINTER -> {
          directory = BmpHeaderDirectory()
          metadata.addDirectory<Directory>(directory)
          directory.setInt(BmpHeaderDirectory.TAG_BITMAP_TYPE, magicNumber)
          // skip past the rest of the file header
          reader.skip(4 + 2 + 2 + 4.toLong())
          readBitmapHeader(reader, directory, metadata)
        }
        else -> {
          metadata.addDirectory(ErrorDirectory("Invalid BMP magic number 0x" + Integer.toHexString(magicNumber)))
          return
        }
      }
    } catch (e: IOException) {
      if (directory == null) {
        addError("Unable to read BMP file header", metadata)
      } else {
        directory.addError("Unable to read BMP file header")
      }
    }
  }

  protected fun readBitmapHeader(reader: SequentialReader, directory: BmpHeaderDirectory, metadata: Metadata) { /*
         * BITMAPCOREHEADER (12 bytes):
         *
         *    DWORD Size              - Size of this header in bytes
         *    SHORT Width             - Image width in pixels
         *    SHORT Height            - Image height in pixels
         *    WORD  Planes            - Number of color planes
         *    WORD  BitsPerPixel      - Number of bits per pixel
         *
         * OS21XBITMAPHEADER (12 bytes):
         *
         *    DWORD  Size             - Size of this structure in bytes
         *    WORD   Width            - Bitmap width in pixels
         *    WORD   Height           - Bitmap height in pixel
         *      WORD   NumPlanes        - Number of bit planes (color depth)
         *    WORD   BitsPerPixel     - Number of bits per pixel per plane
         *
         * OS22XBITMAPHEADER (16/64 bytes):
         *
         *    DWORD  Size             - Size of this structure in bytes
         *    DWORD  Width            - Bitmap width in pixels
         *    DWORD  Height           - Bitmap height in pixel
         *      WORD   NumPlanes        - Number of bit planes (color depth)
         *    WORD   BitsPerPixel     - Number of bits per pixel per plane
         *
         *    - Short version ends here -
         *
         *    DWORD  Compression      - Bitmap compression scheme
         *    DWORD  ImageDataSize    - Size of bitmap data in bytes
         *    DWORD  XResolution      - X resolution of display device
         *    DWORD  YResolution      - Y resolution of display device
         *    DWORD  ColorsUsed       - Number of color table indices used
         *    DWORD  ColorsImportant  - Number of important color indices
         *    WORD   Units            - Type of units used to measure resolution
         *    WORD   Reserved         - Pad structure to 4-byte boundary
         *    WORD   Recording        - Recording algorithm
         *    WORD   Rendering        - Halftoning algorithm used
         *    DWORD  Size1            - Reserved for halftoning algorithm use
         *    DWORD  Size2            - Reserved for halftoning algorithm use
         *    DWORD  ColorEncoding    - Color model used in bitmap
         *    DWORD  Identifier       - Reserved for application use
         *
         * BITMAPINFOHEADER (40 bytes), BITMAPV2INFOHEADER (52 bytes), BITMAPV3INFOHEADER (56 bytes),
         * BITMAPV4HEADER (108 bytes) and BITMAPV5HEADER (124 bytes):
         *
         *    DWORD Size              - Size of this header in bytes
         *    LONG  Width             - Image width in pixels
         *    LONG  Height            - Image height in pixels
         *    WORD  Planes            - Number of color planes
         *    WORD  BitsPerPixel      - Number of bits per pixel
         *    DWORD Compression       - Compression methods used
         *    DWORD SizeOfBitmap      - Size of bitmap in bytes
         *    LONG  HorzResolution    - Horizontal resolution in pixels per meter
         *    LONG  VertResolution    - Vertical resolution in pixels per meter
         *    DWORD ColorsUsed        - Number of colors in the image
         *    DWORD ColorsImportant   - Minimum number of important colors
         *
         *    - BITMAPINFOHEADER ends here -
         *
         *    DWORD RedMask           - Mask identifying bits of red component
         *    DWORD GreenMask         - Mask identifying bits of green component
         *    DWORD BlueMask          - Mask identifying bits of blue component
         *
         *    - BITMAPV2INFOHEADER ends here -
         *
         *    DWORD AlphaMask         - Mask identifying bits of alpha component
         *
         *    - BITMAPV3INFOHEADER ends here -
         *
         *    DWORD CSType            - Color space type
         *    LONG  RedX              - X coordinate of red endpoint
         *    LONG  RedY              - Y coordinate of red endpoint
         *    LONG  RedZ              - Z coordinate of red endpoint
         *    LONG  GreenX            - X coordinate of green endpoint
         *    LONG  GreenY            - Y coordinate of green endpoint
         *    LONG  GreenZ            - Z coordinate of green endpoint
         *    LONG  BlueX             - X coordinate of blue endpoint
         *    LONG  BlueY             - Y coordinate of blue endpoint
         *    LONG  BlueZ             - Z coordinate of blue endpoint
         *    DWORD GammaRed          - Gamma red coordinate scale value
         *    DWORD GammaGreen        - Gamma green coordinate scale value
         *    DWORD GammaBlue         - Gamma blue coordinate scale value
         *
         *    - BITMAPV4HEADER ends here -
         *
         *    DWORD Intent            - Rendering intent for bitmap
         *    DWORD ProfileData       - Offset of the profile data relative to BITMAPV5HEADER
         *    DWORD ProfileSize       - Size, in bytes, of embedded profile data
         *    DWORD Reserved          - Shall be zero
         *
         */
    try {
      val bitmapType = directory.getInt(BmpHeaderDirectory.TAG_BITMAP_TYPE)
      val headerOffset = reader.position
      val headerSize = reader.getInt32()
      directory.setInt(BmpHeaderDirectory.TAG_HEADER_SIZE, headerSize)
      /*
             * Known header type sizes:
             *
             *  12 - BITMAPCOREHEADER or OS21XBITMAPHEADER
             *  16 - OS22XBITMAPHEADER (short)
             *  40 - BITMAPINFOHEADER
             *  52 - BITMAPV2INFOHEADER
             *  56 - BITMAPV3INFOHEADER
             *  64 - OS22XBITMAPHEADER (full)
             * 108 - BITMAPV4HEADER
             * 124 - BITMAPV5HEADER
             *
             */if (headerSize == 12 && bitmapType == BITMAP) { //BITMAPCOREHEADER
/*
                 * There's no way to tell BITMAPCOREHEADER and OS21XBITMAPHEADER
                 * apart for the "standard" bitmap type. The difference is only
                 * that BITMAPCOREHEADER has signed width and height while
                 * in OS21XBITMAPHEADER they are unsigned. Since BITMAPCOREHEADER,
                 * the Windows version, is most common, read them as signed.
                 */
        directory.setInt(BmpHeaderDirectory.TAG_IMAGE_WIDTH, reader.getInt16().toInt())
        directory.setInt(BmpHeaderDirectory.TAG_IMAGE_HEIGHT, reader.getInt16().toInt())
        directory.setInt(BmpHeaderDirectory.TAG_COLOUR_PLANES, reader.getUInt16())
        directory.setInt(BmpHeaderDirectory.TAG_BITS_PER_PIXEL, reader.getUInt16())
      } else if (headerSize == 12) {
        // OS21XBITMAPHEADER
        directory.setInt(BmpHeaderDirectory.TAG_IMAGE_WIDTH, reader.getUInt16())
        directory.setInt(BmpHeaderDirectory.TAG_IMAGE_HEIGHT, reader.getUInt16())
        directory.setInt(BmpHeaderDirectory.TAG_COLOUR_PLANES, reader.getUInt16())
        directory.setInt(BmpHeaderDirectory.TAG_BITS_PER_PIXEL, reader.getUInt16())
      } else if (headerSize == 16 || headerSize == 64) {
        // OS22XBITMAPHEADER
        directory.setInt(BmpHeaderDirectory.TAG_IMAGE_WIDTH, reader.getInt32())
        directory.setInt(BmpHeaderDirectory.TAG_IMAGE_HEIGHT, reader.getInt32())
        directory.setInt(BmpHeaderDirectory.TAG_COLOUR_PLANES, reader.getUInt16())
        directory.setInt(BmpHeaderDirectory.TAG_BITS_PER_PIXEL, reader.getUInt16())
        if (headerSize > 16) {
          directory.setInt(BmpHeaderDirectory.TAG_COMPRESSION, reader.getInt32())
          reader.skip(4) // skip the pixel data length
          directory.setInt(BmpHeaderDirectory.TAG_X_PIXELS_PER_METER, reader.getInt32())
          directory.setInt(BmpHeaderDirectory.TAG_Y_PIXELS_PER_METER, reader.getInt32())
          directory.setInt(BmpHeaderDirectory.TAG_PALETTE_COLOUR_COUNT, reader.getInt32())
          directory.setInt(BmpHeaderDirectory.TAG_IMPORTANT_COLOUR_COUNT, reader.getInt32())
          reader.skip(
            (2 +  // Skip Units, can only be 0 (pixels per meter)
              2 +  // Skip padding
              2) // Skip Recording, can only be 0 (left to right, bottom to top)
              .toLong())
          directory.setInt(BmpHeaderDirectory.TAG_RENDERING, reader.getUInt16())
          reader.skip(4 + 4.toLong()) // Skip Size1 and Size2
          directory.setInt(BmpHeaderDirectory.TAG_COLOR_ENCODING, reader.getInt32())
          reader.skip(4) // Skip Identifier
        }
      } else if (headerSize == 40 || headerSize == 52 || headerSize == 56 || headerSize == 108 || headerSize == 124) {
        // BITMAPINFOHEADER V1-5
        directory.setInt(BmpHeaderDirectory.TAG_IMAGE_WIDTH, reader.getInt32())
        directory.setInt(BmpHeaderDirectory.TAG_IMAGE_HEIGHT, reader.getInt32())
        directory.setInt(BmpHeaderDirectory.TAG_COLOUR_PLANES, reader.getUInt16())
        directory.setInt(BmpHeaderDirectory.TAG_BITS_PER_PIXEL, reader.getUInt16())
        directory.setInt(BmpHeaderDirectory.TAG_COMPRESSION, reader.getInt32())
        // skip the pixel data length
        reader.skip(4)
        directory.setInt(BmpHeaderDirectory.TAG_X_PIXELS_PER_METER, reader.getInt32())
        directory.setInt(BmpHeaderDirectory.TAG_Y_PIXELS_PER_METER, reader.getInt32())
        directory.setInt(BmpHeaderDirectory.TAG_PALETTE_COLOUR_COUNT, reader.getInt32())
        directory.setInt(BmpHeaderDirectory.TAG_IMPORTANT_COLOUR_COUNT, reader.getInt32())
        if (headerSize == 40) {
          // BITMAPINFOHEADER end
          return
        }
        directory.setLong(BmpHeaderDirectory.TAG_RED_MASK, reader.getUInt32())
        directory.setLong(BmpHeaderDirectory.TAG_GREEN_MASK, reader.getUInt32())
        directory.setLong(BmpHeaderDirectory.TAG_BLUE_MASK, reader.getUInt32())
        if (headerSize == 52) {
          // BITMAPV2INFOHEADER end
          return
        }
        directory.setLong(BmpHeaderDirectory.TAG_ALPHA_MASK, reader.getUInt32())
        if (headerSize == 56) {
          // BITMAPV3INFOHEADER end
          return
        }
        val csType = reader.getUInt32()
        directory.setLong(BmpHeaderDirectory.TAG_COLOR_SPACE_TYPE, csType)
        reader.skip(36) // Skip color endpoint coordinates
        directory.setLong(BmpHeaderDirectory.TAG_GAMMA_RED, reader.getUInt32())
        directory.setLong(BmpHeaderDirectory.TAG_GAMMA_GREEN, reader.getUInt32())
        directory.setLong(BmpHeaderDirectory.TAG_GAMMA_BLUE, reader.getUInt32())
        if (headerSize == 108) {
          // BITMAPV4HEADER end
          return
        }
        directory.setInt(BmpHeaderDirectory.TAG_INTENT, reader.getInt32())
        if (csType == BmpHeaderDirectory.ColorSpaceType.PROFILE_EMBEDDED.value || csType == BmpHeaderDirectory.ColorSpaceType.PROFILE_LINKED.value) {
          val profileOffset = reader.getUInt32()
          val profileSize = reader.getInt32()
          if (reader.position > headerOffset + profileOffset) {
            directory.addError("Invalid profile data offset 0x" + java.lang.Long.toHexString(headerOffset + profileOffset))
            return
          }
          reader.skip(headerOffset + profileOffset - reader.position)
          if (csType == BmpHeaderDirectory.ColorSpaceType.PROFILE_LINKED.value) {
            directory.setString(BmpHeaderDirectory.TAG_LINKED_PROFILE, reader.getNullTerminatedString(profileSize, WINDOWS_1252))
          } else {
            val randomAccessReader = ByteArrayReader(reader.getBytes(profileSize))
            IccReader().extract(randomAccessReader, metadata, directory)
          }
        } else {
          reader.skip(
            (4 +  // Skip ProfileData offset
              4 +  // Skip ProfileSize
              4) // Skip Reserved
              .toLong())
        }
      } else {
        directory.addError("Unexpected DIB header size: $headerSize")
      }
    } catch (e: IOException) {
      directory.addError("Unable to read BMP header")
    } catch (e: MetadataException) {
      directory.addError("Internal error")
    }
  }

  protected fun addError(errorMessage: String, metadata: Metadata) {
    val directory = metadata.getFirstDirectoryOfType(ErrorDirectory::class.java)
    if (directory == null) {
      metadata.addDirectory(ErrorDirectory(errorMessage))
    } else {
      directory.addError(errorMessage)
    }
  }

  companion object {
    // Possible "magic bytes" indicating bitmap type:
    /**
     * "BM" - Windows or OS/2 bitmap
     */
    const val BITMAP = 0x4D42
    /**
     * "BA" - OS/2 Bitmap array (multiple bitmaps)
     */
    const val OS2_BITMAP_ARRAY = 0x4142
    /**
     * "IC" - OS/2 Icon
     */
    const val OS2_ICON = 0x4349
    /**
     * "CI" - OS/2 Color icon
     */
    const val OS2_COLOR_ICON = 0x4943
    /**
     * "CP" - OS/2 Color pointer
     */
    const val OS2_COLOR_POINTER = 0x5043
    /**
     * "PT" - OS/2 Pointer
     */
    const val OS2_POINTER = 0x5450
  }
}
