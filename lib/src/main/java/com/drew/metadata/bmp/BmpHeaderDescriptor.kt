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

import com.drew.metadata.TagDescriptor
import java.text.DecimalFormat

/**
 * @author Drew Noakes https://drewnoakes.com
 * @author Nadahar
 */
class BmpHeaderDescriptor(directory: BmpHeaderDirectory) : TagDescriptor<BmpHeaderDirectory>(directory) {
  override fun getDescription(tagType: Int): String? {
    return when (tagType) {
      BmpHeaderDirectory.TAG_BITMAP_TYPE -> bitmapTypeDescription
      BmpHeaderDirectory.TAG_COMPRESSION -> compressionDescription
      BmpHeaderDirectory.TAG_RENDERING -> renderingDescription
      BmpHeaderDirectory.TAG_COLOR_ENCODING -> colorEncodingDescription
      BmpHeaderDirectory.TAG_RED_MASK, BmpHeaderDirectory.TAG_GREEN_MASK, BmpHeaderDirectory.TAG_BLUE_MASK, BmpHeaderDirectory.TAG_ALPHA_MASK -> formatHex(_directory.getLongObject(tagType), 8)
      BmpHeaderDirectory.TAG_COLOR_SPACE_TYPE -> colorSpaceTypeDescription
      BmpHeaderDirectory.TAG_GAMMA_RED, BmpHeaderDirectory.TAG_GAMMA_GREEN, BmpHeaderDirectory.TAG_GAMMA_BLUE -> formatFixed1616(_directory.getLongObject(tagType))
      BmpHeaderDirectory.TAG_INTENT -> renderingIntentDescription
      else -> super.getDescription(tagType)
    }
  }

  val bitmapTypeDescription: String?
    get() {
      val bitmapType = _directory.bitmapType
      return bitmapType?.toString()
    }

  //  0 = None
  //  1 = RLE 8-bit/pixel
  //  2 = RLE 4-bit/pixel
  //  3 = Bit fields (or Huffman 1D if OS22XBITMAPHEADER (size 64))
  //  4 = JPEG (or RLE 24-bit/pixel if OS22XBITMAPHEADER (size 64))
  //  5 = PNG
  // 11 = CMYK
  // 12 = CMYK RLE-8
  // 13 = CMYK RLE-4
  val compressionDescription: String?
    get() { //  0 = None
    //  1 = RLE 8-bit/pixel
    //  2 = RLE 4-bit/pixel
    //  3 = Bit fields (or Huffman 1D if OS22XBITMAPHEADER (size 64))
    //  4 = JPEG (or RLE 24-bit/pixel if OS22XBITMAPHEADER (size 64))
    //  5 = PNG
    // 11 = CMYK
    // 12 = CMYK RLE-8
    // 13 = CMYK RLE-4
      val compression = _directory.compression
      if (compression != null) {
        return compression.toString()
      }
      val value = _directory.getInteger(BmpHeaderDirectory.TAG_COMPRESSION)
      return if (value == null) null else "Illegal value 0x" + Integer.toHexString(value.toInt())
    }

  val renderingDescription: String?
    get() {
      val renderingHalftoningAlgorithm = _directory.rendering
      return renderingHalftoningAlgorithm?.toString()
    }

  val colorEncodingDescription: String?
    get() {
      val colorEncoding = _directory.colorEncoding
      return colorEncoding?.toString()
    }

  val colorSpaceTypeDescription: String?
    get() {
      val colorSpaceType = _directory.colorSpaceType
      return colorSpaceType?.toString()
    }

  val renderingIntentDescription: String?
    get() {
      val renderingIntent = _directory.renderingIntent
      return renderingIntent?.toString()
    }

  companion object {
    fun formatHex(value: Int?, digits: Int): String? {
      return if (value == null) null else formatHex(value.toLong() and 0xFFFFFFFFL, digits)
    }

    fun formatHex(value: Int, digits: Int): String {
      return formatHex(value.toLong() and 0xFFFFFFFFL, digits)
    }

    fun formatHex(value: Long?, digits: Int): String? {
      return if (value == null) null else formatHex(value.toLong(), digits)
    }

    fun formatHex(value: Long, digits: Int): String {
      return String.format("0x%0" + digits + "X", value)
    }

    fun formatFixed1616(value: Int?): String? {
      return if (value == null) null else formatFixed1616(value.toLong() and 0xFFFFFFFFL)
    }

    fun formatFixed1616(value: Int): String {
      return formatFixed1616(value.toLong() and 0xFFFFFFFFL)
    }

    fun formatFixed1616(value: Long?): String? {
      return if (value == null) null else formatFixed1616(value.toLong())
    }

    fun formatFixed1616(value: Long): String {
      val d = value.toDouble() / 0x10000
      val format = DecimalFormat("0.###")
      return format.format(d)
    }
  }
}
