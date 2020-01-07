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
package com.drew.imaging

import com.drew.lang.ByteTrie
import java.io.FilterInputStream
import java.io.IOException
import java.util.*

/**
 * Examines the a file's first bytes and estimates the file's type.
 */
class FileTypeDetector private constructor() {
  companion object {
    private val root: ByteTrie<FileType> = ByteTrie()
    private val ftypMap: HashMap<String, FileType>
    /**
     * Examines the file's bytes and estimates the file's type.
     *
     *
     * Requires a [FilterInputStream] in order to mark and reset the stream to the position
     * at which it was provided to this method once completed.
     *
     *
     * Requires the stream to contain at least eight bytes.
     *
     * @throws IOException if an IO error occurred or the input stream ended unexpectedly.
     */
    @JvmStatic
    @Throws(IOException::class)
    fun detectFileType(inputStream: FilterInputStream): FileType? {
      if (!inputStream.markSupported()) throw IOException("Stream must support mark/reset")
      val maxByteCount = 16.coerceAtLeast(root.maxDepth)
      inputStream.mark(maxByteCount)
      val bytes = ByteArray(maxByteCount)
      val bytesRead = inputStream.read(bytes)
      if (bytesRead == -1) throw IOException("Stream ended before file's magic number could be determined.")
      inputStream.reset()
      val fileType = root.find(bytes)
      if (fileType == FileType.Unknown) {
        val eightCC = String(bytes, 4, 8)
        // Test at offset 4 for Base Media Format (i.e. QuickTime, MP4, etc...) identifier "ftyp" plus four identifying characters
        val t = ftypMap[eightCC]
        if (t != null) return t
      } else if (fileType == FileType.Riff) {
        val fourCC = String(bytes, 8, 4)
        if (fourCC == "WAVE") return FileType.Wav
        if (fourCC == "AVI ") return FileType.Avi
        if (fourCC == "WEBP") return FileType.WebP
      }
      return fileType
    }

    init {
      root.setDefaultValue(FileType.Unknown)
      // https://en.wikipedia.org/wiki/List_of_file_signatures
      root.addPath(FileType.Jpeg, byteArrayOf(0xff.toByte(), 0xd8.toByte()))
      root.addPath(FileType.Tiff, "II".toByteArray(), byteArrayOf(0x2a, 0x00))
      root.addPath(FileType.Tiff, "MM".toByteArray(), byteArrayOf(0x00, 0x2a))
      root.addPath(FileType.Psd, "8BPS".toByteArray())
      root.addPath(FileType.Png, byteArrayOf(0x89.toByte(), 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A, 0x00, 0x00, 0x00, 0x0D, 0x49, 0x48, 0x44, 0x52))
      root.addPath(FileType.Bmp, "BM".toByteArray()) // Standard Bitmap Windows and OS/2
      root.addPath(FileType.Bmp, "BA".toByteArray()) // OS/2 Bitmap Array
      root.addPath(FileType.Bmp, "CI".toByteArray()) // OS/2 Color Icon
      root.addPath(FileType.Bmp, "CP".toByteArray()) // OS/2 Color Pointer
      root.addPath(FileType.Bmp, "IC".toByteArray()) // OS/2 Icon
      root.addPath(FileType.Bmp, "PT".toByteArray()) // OS/2 Pointer
      root.addPath(FileType.Gif, "GIF87a".toByteArray())
      root.addPath(FileType.Gif, "GIF89a".toByteArray())
      root.addPath(FileType.Ico, byteArrayOf(0x00, 0x00, 0x01, 0x00))
      root.addPath(FileType.Pcx, byteArrayOf(0x0A, 0x00, 0x01)) // multiple PCX versions, explicitly listed
      root.addPath(FileType.Pcx, byteArrayOf(0x0A, 0x02, 0x01))
      root.addPath(FileType.Pcx, byteArrayOf(0x0A, 0x03, 0x01))
      root.addPath(FileType.Pcx, byteArrayOf(0x0A, 0x05, 0x01))
      root.addPath(FileType.Riff, "RIFF".toByteArray())
      root.addPath(FileType.Arw, "II".toByteArray(), byteArrayOf(0x2a, 0x00, 0x08, 0x00))
      root.addPath(FileType.Crw, "II".toByteArray(), byteArrayOf(0x1a, 0x00, 0x00, 0x00), "HEAPCCDR".toByteArray())
      root.addPath(FileType.Cr2, "II".toByteArray(), byteArrayOf(0x2a, 0x00, 0x10, 0x00, 0x00, 0x00, 0x43, 0x52))
      // NOTE this doesn't work for NEF as it incorrectly flags many other TIFF files as being NEF
      // _root.addPath(FileType.Nef, "MM".getBytes(), new byte[]{0x00, 0x2a, 0x00, 0x00, 0x00, (byte)0x08, 0x00});
      root.addPath(FileType.Orf, "IIRO".toByteArray(), byteArrayOf(0x08.toByte(), 0x00))
      root.addPath(FileType.Orf, "MMOR".toByteArray(), byteArrayOf(0x00.toByte(), 0x00))
      root.addPath(FileType.Orf, "IIRS".toByteArray(), byteArrayOf(0x08.toByte(), 0x00))
      root.addPath(FileType.Raf, "FUJIFILMCCD-RAW".toByteArray())
      root.addPath(FileType.Rw2, "II".toByteArray(), byteArrayOf(0x55, 0x00))
      root.addPath(FileType.Eps, "%!PS".toByteArray())
      root.addPath(FileType.Eps, byteArrayOf(0xC5.toByte(), 0xD0.toByte(), 0xD3.toByte(), 0xC6.toByte()))
      root.addPath(FileType.Mp3, byteArrayOf(0xFF.toByte()))
      ftypMap = HashMap()
      // http://www.ftyps.com
      // QuickTime Mov
      ftypMap["ftypmoov"] = FileType.Mov
      ftypMap["ftypwide"] = FileType.Mov
      ftypMap["ftypmdat"] = FileType.Mov
      ftypMap["ftypfree"] = FileType.Mov
      ftypMap["ftypqt  "] = FileType.Mov
      // MP4
      ftypMap["ftypavc1"] = FileType.Mp4
      ftypMap["ftypiso2"] = FileType.Mp4
      ftypMap["ftypisom"] = FileType.Mp4
      ftypMap["ftypM4A "] = FileType.Mp4
      ftypMap["ftypM4B "] = FileType.Mp4
      ftypMap["ftypM4P "] = FileType.Mp4
      ftypMap["ftypM4V "] = FileType.Mp4
      ftypMap["ftypM4VH"] = FileType.Mp4
      ftypMap["ftypM4VP"] = FileType.Mp4
      ftypMap["ftypmmp4"] = FileType.Mp4
      ftypMap["ftypmp41"] = FileType.Mp4
      ftypMap["ftypmp42"] = FileType.Mp4
      ftypMap["ftypmp71"] = FileType.Mp4
      ftypMap["ftypMSNV"] = FileType.Mp4
      ftypMap["ftypNDAS"] = FileType.Mp4
      ftypMap["ftypNDSC"] = FileType.Mp4
      ftypMap["ftypNDSH"] = FileType.Mp4
      ftypMap["ftypNDSM"] = FileType.Mp4
      ftypMap["ftypNDSP"] = FileType.Mp4
      ftypMap["ftypNDSS"] = FileType.Mp4
      ftypMap["ftypNDXC"] = FileType.Mp4
      ftypMap["ftypNDXH"] = FileType.Mp4
      ftypMap["ftypNDXM"] = FileType.Mp4
      ftypMap["ftypNDXP"] = FileType.Mp4
      ftypMap["ftypNDXS"] = FileType.Mp4
      // HEIF
      ftypMap["ftypmif1"] = FileType.Heif
      ftypMap["ftypmsf1"] = FileType.Heif
      ftypMap["ftypheic"] = FileType.Heif
      ftypMap["ftypheix"] = FileType.Heif
      ftypMap["ftyphevc"] = FileType.Heif
      ftypMap["ftyphevx"] = FileType.Heif
      // Only file detection
      root.addPath(FileType.Aac, byteArrayOf(0xFF.toByte(), 0xF1.toByte()))
      root.addPath(FileType.Aac, byteArrayOf(0xFF.toByte(), 0xF9.toByte()))
      root.addPath(FileType.Asf, byteArrayOf(0x30, 0x26, 0xB2.toByte(), 0x75, 0x8E.toByte(), 0x66, 0xCF.toByte(), 0x11, 0xA6.toByte(), 0xD9.toByte(), 0x00, 0xAA.toByte(), 0x00, 0x62, 0xCE.toByte(), 0x6C))
      root.addPath(FileType.Cfbf, byteArrayOf(0xD0.toByte(), 0xCF.toByte(), 0x11, 0xE0.toByte(), 0xA1.toByte(), 0xB1.toByte(), 0x1A, 0xE1.toByte(), 0x00))
      root.addPath(FileType.Flv, byteArrayOf(0x46, 0x4C, 0x56))
      root.addPath(FileType.Indd, byteArrayOf(0x06, 0x06, 0xED.toByte(), 0xF5.toByte(), 0xD8.toByte(), 0x1D, 0x46, 0xE5.toByte(), 0xBD.toByte(), 0x31, 0xEF.toByte(), 0xE7.toByte(), 0xFE.toByte(), 0x74, 0xB7.toByte(), 0x1D))
      root.addPath(FileType.Mxf, byteArrayOf(0x06, 0x0e, 0x2b, 0x34, 0x02, 0x05, 0x01, 0x01, 0x0d, 0x01, 0x02, 0x01, 0x01, 0x02)) // has offset?
      root.addPath(FileType.Qxp, byteArrayOf(0x00, 0x00, 0x49, 0x49, 0x58, 0x50, 0x52, 0x33)) // "..IIXPR3" (little-endian - intel)
      root.addPath(FileType.Qxp, byteArrayOf(0x00, 0x00, 0x4D, 0x4D, 0x58, 0x50, 0x52, 0x33)) // "..MMXPR3" (big-endian - motorola)
      root.addPath(FileType.Ram, byteArrayOf(0x72, 0x74, 0x73, 0x70, 0x3A, 0x2F, 0x2F))
      root.addPath(FileType.Rtf, byteArrayOf(0x7B, 0x5C, 0x72, 0x74, 0x66, 0x31))
      root.addPath(FileType.Sit, byteArrayOf(0x53, 0x49, 0x54, 0x21, 0x00)) // SIT!);
      root.addPath(FileType.Sit, byteArrayOf(0x53, 0x74, 0x75, 0x66, 0x66, 0x49, 0x74, 0x20, 0x28, 0x63, 0x29, 0x31, 0x39, 0x39, 0x37, 0x2D)) // StuffIt (c)1997-
      root.addPath(FileType.Sitx, byteArrayOf(0x53, 0x74, 0x75, 0x66, 0x66, 0x49, 0x74, 0x21))
      root.addPath(FileType.Swf, "CWS".toByteArray())
      root.addPath(FileType.Swf, "FWS".toByteArray())
      root.addPath(FileType.Swf, "ZWS".toByteArray())
      root.addPath(FileType.Vob, byteArrayOf(0x00, 0x00, 0x01, 0xBA.toByte()))
      root.addPath(FileType.Zip, "PK".toByteArray())
    }
  }

  init {
    throw Exception("Not intended for instantiation")
  }
}
