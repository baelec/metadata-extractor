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

import com.drew.metadata.Directory
import java.util.*

/**
 * @author Drew Noakes https://drewnoakes.com
 * @author Nadahar
 */
class BmpHeaderDirectory : Directory() {
  override val tagNameMap = Companion.tagNameMap
  companion object {
    const val TAG_BITMAP_TYPE = -2
    const val TAG_HEADER_SIZE = -1
    const val TAG_IMAGE_HEIGHT = 1
    const val TAG_IMAGE_WIDTH = 2
    const val TAG_COLOUR_PLANES = 3
    const val TAG_BITS_PER_PIXEL = 4
    const val TAG_COMPRESSION = 5
    const val TAG_X_PIXELS_PER_METER = 6
    const val TAG_Y_PIXELS_PER_METER = 7
    const val TAG_PALETTE_COLOUR_COUNT = 8
    const val TAG_IMPORTANT_COLOUR_COUNT = 9
    const val TAG_RENDERING = 10
    const val TAG_COLOR_ENCODING = 11
    const val TAG_RED_MASK = 12
    const val TAG_GREEN_MASK = 13
    const val TAG_BLUE_MASK = 14
    const val TAG_ALPHA_MASK = 15
    const val TAG_COLOR_SPACE_TYPE = 16
    const val TAG_GAMMA_RED = 17
    const val TAG_GAMMA_GREEN = 18
    const val TAG_GAMMA_BLUE = 19
    const val TAG_INTENT = 20
    const val TAG_LINKED_PROFILE = 21
    private val tagNameMap = HashMap<Int, String>()

    init {
      tagNameMap[TAG_BITMAP_TYPE] = "Bitmap type"
      tagNameMap[TAG_HEADER_SIZE] = "Header Size"
      tagNameMap[TAG_IMAGE_HEIGHT] = "Image Height"
      tagNameMap[TAG_IMAGE_WIDTH] = "Image Width"
      tagNameMap[TAG_COLOUR_PLANES] = "Planes"
      tagNameMap[TAG_BITS_PER_PIXEL] = "Bits Per Pixel"
      tagNameMap[TAG_COMPRESSION] = "Compression"
      tagNameMap[TAG_X_PIXELS_PER_METER] = "X Pixels per Meter"
      tagNameMap[TAG_Y_PIXELS_PER_METER] = "Y Pixels per Meter"
      tagNameMap[TAG_PALETTE_COLOUR_COUNT] = "Palette Colour Count"
      tagNameMap[TAG_IMPORTANT_COLOUR_COUNT] = "Important Colour Count"
      tagNameMap[TAG_RENDERING] = "Rendering"
      tagNameMap[TAG_COLOR_ENCODING] = "Color Encoding"
      tagNameMap[TAG_RED_MASK] = "Red Mask"
      tagNameMap[TAG_GREEN_MASK] = "Green Mask"
      tagNameMap[TAG_BLUE_MASK] = "Blue Mask"
      tagNameMap[TAG_ALPHA_MASK] = "Alpha Mask"
      tagNameMap[TAG_COLOR_SPACE_TYPE] = "Color Space Type"
      tagNameMap[TAG_GAMMA_RED] = "Red Gamma Curve"
      tagNameMap[TAG_GAMMA_GREEN] = "Green Gamma Curve"
      tagNameMap[TAG_GAMMA_BLUE] = "Blue Gamma Curve"
      tagNameMap[TAG_INTENT] = "Rendering Intent"
      tagNameMap[TAG_LINKED_PROFILE] = "Linked Profile File Name"
    }
  }

  val bitmapType: BitmapType?
    get() {
      val value = getInteger(TAG_BITMAP_TYPE)
      return if (value == null) null else BitmapType.typeOf(value.toInt())
    }

  val compression: Compression?
    get() = Compression.typeOf(this)

  val rendering: RenderingHalftoningAlgorithm?
    get() {
      val value = getInteger(TAG_RENDERING)
      return if (value == null) null else RenderingHalftoningAlgorithm.typeOf(value.toInt())
    }

  val colorEncoding: ColorEncoding?
    get() {
      val value = getInteger(TAG_COLOR_ENCODING)
      return if (value == null) null else ColorEncoding.typeOf(value.toInt())
    }

  val colorSpaceType: ColorSpaceType?
    get() {
      val value = getLongObject(TAG_COLOR_SPACE_TYPE)
      return if (value == null) null else ColorSpaceType.typeOf(value.toLong())
    }

  val renderingIntent: RenderingIntent?
    get() {
      val value = getInteger(TAG_INTENT)
      return if (value == null) null else RenderingIntent.typeOf(value.toInt().toLong())
    }

  override val name: String
    get() = "BMP Header"

  enum class BitmapType(val value: Int) {
    /** "BM" - Windows or OS/2 bitmap  */
    BITMAP(0x4D42),
    /** "BA" - OS/2 Bitmap array (multiple bitmaps)  */
    OS2_BITMAP_ARRAY(0x4142),
    /** "IC" - OS/2 Icon  */
    OS2_ICON(0x4349),
    /** "CI" - OS/2 Color icon  */
    OS2_COLOR_ICON(0x4943),
    /** "CP" - OS/2 Color pointer  */
    OS2_COLOR_POINTER(0x5043),
    /** "PT" - OS/2 Pointer  */
    OS2_POINTER(0x5450);

    override fun toString(): String {
      return when (this) {
        BITMAP -> "Standard"
        OS2_BITMAP_ARRAY -> "Bitmap Array"
        OS2_COLOR_ICON -> "Color Icon"
        OS2_COLOR_POINTER -> "Color Pointer"
        OS2_ICON -> "Monochrome Icon"
        OS2_POINTER -> "Monochrome Pointer"
        else -> throw IllegalStateException("Unimplemented bitmap type " + super.toString())
      }
    }

    companion object {
      fun typeOf(value: Int): BitmapType? {
        for (bitmapType in values()) {
          if (bitmapType.value == value) {
            return bitmapType
          }
        }
        return null
      }
    }

  }

  enum class Compression(val value: Int) {
    /** 0 = None  */
    BI_RGB(0),
    /** 1 = RLE 8-bit/pixel  */
    BI_RLE8(1),
    /** 2 = RLE 4-bit/pixel  */
    BI_RLE4(2),
    /** 3 = Bit fields (not OS22XBITMAPHEADER (size 64))  */
    BI_BITFIELDS(3),
    /** 3 = Huffman 1D (if OS22XBITMAPHEADER (size 64))  */
    BI_HUFFMAN_1D(3),
    /** 4 = JPEG (not OS22XBITMAPHEADER (size 64))  */
    BI_JPEG(4),
    /** 4 = RLE 24-bit/pixel (if OS22XBITMAPHEADER (size 64))  */
    BI_RLE24(4),
    /** 5 = PNG  */
    BI_PNG(5),
    /** 6 = RGBA bit fields  */
    BI_ALPHABITFIELDS(6),
    /** 11 = CMYK  */
    BI_CMYK(11),
    /** 12 = CMYK RLE-8  */
    BI_CMYKRLE8(12),
    /** 13 = CMYK RLE-4  */
    BI_CMYKRLE4(13);

    override fun toString(): String {
      return when (this) {
        BI_RGB -> "None"
        BI_RLE8 -> "RLE 8-bit/pixel"
        BI_RLE4 -> "RLE 4-bit/pixel"
        BI_BITFIELDS -> "Bit Fields"
        BI_HUFFMAN_1D -> "Huffman 1D"
        BI_JPEG -> "JPEG"
        BI_RLE24 -> "RLE 24-bit/pixel"
        BI_PNG -> "PNG"
        BI_ALPHABITFIELDS -> "RGBA Bit Fields"
        BI_CMYK -> "CMYK Uncompressed"
        BI_CMYKRLE8 -> "CMYK RLE-8"
        BI_CMYKRLE4 -> "CMYK RLE-4"
      }
    }

    companion object {
      fun typeOf(directory: BmpHeaderDirectory): Compression? {
        val value = directory.getInteger(TAG_COMPRESSION) ?: return null
        val headerSize = directory.getInteger(TAG_HEADER_SIZE) ?: return null
        return typeOf(value, headerSize)
      }

      fun typeOf(value: Int, headerSize: Int): Compression? {
        return when (value) {
          0 -> BI_RGB
          1 -> BI_RLE8
          2 -> BI_RLE4
          3 -> if (headerSize == 64) BI_HUFFMAN_1D else BI_BITFIELDS
          4 -> if (headerSize == 64) BI_RLE24 else BI_JPEG
          5 -> BI_PNG
          6 -> BI_ALPHABITFIELDS
          11 -> BI_CMYK
          12 -> BI_CMYKRLE8
          13 -> BI_CMYKRLE4
          else -> null
        }
      }
    }
  }

  enum class RenderingHalftoningAlgorithm(val value: Int) {
    /** No halftoning algorithm  */
    NONE(0),
    /** Error Diffusion Halftoning  */
    ERROR_DIFFUSION(1),
    /** Processing Algorithm for Noncoded Document Acquisition  */
    PANDA(2),
    /** Super-circle Halftoning  */
    SUPER_CIRCLE(3);

    override fun toString(): String {
      return when (this) {
        NONE -> "No Halftoning Algorithm"
        ERROR_DIFFUSION -> "Error Diffusion Halftoning"
        PANDA -> "Processing Algorithm for Noncoded Document Acquisition"
        SUPER_CIRCLE -> "Super-circle Halftoning"
      }
    }

    companion object {
      fun typeOf(value: Int): RenderingHalftoningAlgorithm? {
        for (renderingHalftoningAlgorithm in values()) {
          if (renderingHalftoningAlgorithm.value == value) {
            return renderingHalftoningAlgorithm
          }
        }
        return null
      }
    }

  }

  enum class ColorEncoding(val value: Int) {
    RGB(0);

    companion object {
      fun typeOf(value: Int): ColorEncoding? {
        return if (value == 0) RGB else null
      }
    }

  }

  enum class ColorSpaceType(val value: Long) {
    /** 0 = Calibrated RGB  */
    LCS_CALIBRATED_RGB(0L),
    /** "sRGB" = sRGB Color Space  */
    LCS_sRGB(0x73524742L),
    /** "Win " = System Default Color Space, sRGB  */
    LCS_WINDOWS_COLOR_SPACE(0x57696E20L),
    /** "LINK" = Linked Profile  */
    PROFILE_LINKED(0x4C494E4BL),
    /** "MBED" = Embedded Profile  */
    PROFILE_EMBEDDED(0x4D424544L);

    override fun toString(): String {
      return when (this) {
        LCS_CALIBRATED_RGB -> "Calibrated RGB"
        LCS_sRGB -> "sRGB Color Space"
        LCS_WINDOWS_COLOR_SPACE -> "System Default Color Space, sRGB"
        PROFILE_LINKED -> "Linked Profile"
        PROFILE_EMBEDDED -> "Embedded Profile"
      }
    }

    companion object {
      fun typeOf(value: Long): ColorSpaceType? {
        for (colorSpaceType in values()) {
          if (colorSpaceType.value == value) {
            return colorSpaceType
          }
        }
        return null
      }
    }

  }

  enum class RenderingIntent(val value: Int) {
    /** Graphic, Saturation  */
    LCS_GM_BUSINESS(1),
    /** Proof, Relative Colorimetric  */
    LCS_GM_GRAPHICS(2),
    /** Picture, Perceptual  */
    LCS_GM_IMAGES(4),
    /** Match, Absolute Colorimetric  */
    LCS_GM_ABS_COLORIMETRIC(8);

    override fun toString(): String {
      return when (this) {
        LCS_GM_BUSINESS -> "Graphic, Saturation"
        LCS_GM_GRAPHICS -> "Proof, Relative Colorimetric"
        LCS_GM_IMAGES -> "Picture, Perceptual"
        LCS_GM_ABS_COLORIMETRIC -> "Match, Absolute Colorimetric"
      }
    }

    companion object {
      fun typeOf(value: Long): RenderingIntent? {
        for (renderingIntent in values()) {
          if (renderingIntent.value.toLong() == value) {
            return renderingIntent
          }
        }
        return null
      }
    }
  }

  init {
    setDescriptor(BmpHeaderDescriptor(this))
  }
}
