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
package com.drew.metadata.avi

import com.drew.imaging.riff.RiffHandler
import com.drew.lang.ByteArrayReader
import com.drew.metadata.Metadata
import java.io.IOException
import kotlin.math.pow
import kotlin.math.roundToInt

/**
 * Implementation of [RiffHandler] specialising in AVI support.
 *
 * Extracts data from chunk/list types:
 *
 *
 *  * `"avih"`: width, height, streams
 *  * `"strh"`: frames/second, samples/second, duration, video codec
 *
 *
 * Sources: http://www.alexander-noe.com/video/documentation/avi.pdf
 * https://msdn.microsoft.com/en-us/library/ms899422.aspx
 * https://www.loc.gov/preservation/digital/formats/fdd/fdd000025.shtml
 *
 * @author Payton Garland
 */
class AviRiffHandler(metadata: Metadata) : RiffHandler {
  private val _directory: AviDirectory = AviDirectory()
  override fun shouldAcceptRiffIdentifier(identifier: String): Boolean {
    return identifier == AviDirectory.FORMAT
  }

  override fun shouldAcceptChunk(fourCC: String): Boolean {
    return fourCC == AviDirectory.CHUNK_STREAM_HEADER || fourCC == AviDirectory.CHUNK_MAIN_HEADER || fourCC == AviDirectory.CHUNK_DATETIME_ORIGINAL
  }

  override fun shouldAcceptList(fourCC: String): Boolean {
    // if (fourCC.equals(AviDirectory.LIST_HEADER) || fourCC.equals(AviDirectory.LIST_STREAM_HEADER) || fourCC.equals(AviDirectory.FORMAT)) {
    // _currentList = fourCC;
    // } else {
    // _currentList = "";
    // }
    return fourCC == AviDirectory.LIST_HEADER || fourCC == AviDirectory.LIST_STREAM_HEADER || fourCC == AviDirectory.FORMAT
  }

  override fun processChunk(fourCC: String, payload: ByteArray) {
    try {
      if (fourCC == AviDirectory.CHUNK_STREAM_HEADER) {
        val reader = ByteArrayReader(payload)
        reader.isMotorolaByteOrder = false
        val fccType = String(reader.getBytes(0, 4))
        val fccHandler = String(reader.getBytes(4, 4))
        // int dwFlags = reader.getInt32(8);
        // int wPriority = reader.getInt16(12);
        // int wLanguage = reader.getInt16(14);
        // int dwInitialFrames = reader.getInt32(16);
        val dwScale = reader.getFloat32(20)
        val dwRate = reader.getFloat32(24)
        // int dwStart = reader.getInt32(28);
        val dwLength = reader.getInt32(32)
        // int dwSuggestedBufferSize = reader.getInt32(36);
        // int dwQuality = reader.getInt32(40);
        // int dwSampleSize = reader.getInt32(44);
        // byte[] rcFrame = reader.getBytes(48, 2);
        if (fccType == "vids") {
          if (!_directory.containsTag(AviDirectory.TAG_FRAMES_PER_SECOND)) {
            _directory.setDouble(AviDirectory.TAG_FRAMES_PER_SECOND, (dwRate / dwScale).toDouble())
            val duration = dwLength / (dwRate / dwScale).toDouble()
            val hours = duration.toInt() / 60.0.pow(2.0).toInt()
            val minutes = duration.toInt() / 60.0.pow(1.0).toInt() - hours * 60
            val seconds = (duration / 60.0.pow(0.0) - minutes * 60).roundToInt()
            val time = "%1$02d:%2$02d:%3$02d".format(hours, minutes, seconds)
            _directory.setString(AviDirectory.TAG_DURATION, time)
            _directory.setString(AviDirectory.TAG_VIDEO_CODEC, fccHandler)
          }
        } else if (fccType == "auds") {
          if (!_directory.containsTag(AviDirectory.TAG_SAMPLES_PER_SECOND)) {
            _directory.setDouble(AviDirectory.TAG_SAMPLES_PER_SECOND, (dwRate / dwScale).toDouble())
          }
        }
      } else if (fourCC == AviDirectory.CHUNK_MAIN_HEADER) {
        val reader = ByteArrayReader(payload)
        reader.isMotorolaByteOrder = false
        // int dwMicroSecPerFrame = reader.getInt32(0);
        // int dwMaxBytesPerSec = reader.getInt32(4);
        // int dwPaddingGranularity = reader.getInt32(8);
        // int dwFlags = reader.getInt32(12);
        // int dwTotalFrames = reader.getInt32(16);
        // int dwInitialFrames = reader.getInt32(20);
        val dwStreams = reader.getInt32(24)
        // int dwSuggestedBufferSize = reader.getInt32(28);
        val dwWidth = reader.getInt32(32)
        val dwHeight = reader.getInt32(36)
        //byte[] dwReserved = reader.getBytes(40, 4);
        _directory.setInt(AviDirectory.TAG_WIDTH, dwWidth)
        _directory.setInt(AviDirectory.TAG_HEIGHT, dwHeight)
        _directory.setInt(AviDirectory.TAG_STREAMS, dwStreams)
      } else if (fourCC == AviDirectory.CHUNK_DATETIME_ORIGINAL) {
        val reader = ByteArrayReader(payload)
        var str = reader.getString(0, payload.size, "ASCII")
        if (str.length == 26 && str.endsWith(String(charArrayOf(0x0A.toChar(), 0x00.toChar())))) {
          // ?0A 00? "New Line" + padded to nearest WORD boundary
          str = str.substring(0, 24)
        }
        _directory.setString(AviDirectory.TAG_DATETIME_ORIGINAL, str)
      }
    } catch (ex: IOException) {
      _directory.addError(ex.message!!)
    }
  }

  // @NotNull
  // private String _currentList = "";
  init {
    metadata.addDirectory(_directory)
  }
}
