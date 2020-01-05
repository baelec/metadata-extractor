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
package com.drew.metadata.mp3

import com.drew.imaging.ImageProcessingException
import com.drew.lang.SequentialReader
import com.drew.lang.StreamReader
import com.drew.metadata.Metadata
import java.io.IOException
import java.io.InputStream

/**
 * Sources: http://id3.org/mp3Frame
 * https://www.loc.gov/preservation/digital/formats/fdd/fdd000105.shtml
 *
 * @author Payton Garland
 */
class Mp3Reader {
  fun extract(inputStream: InputStream, metadata: Metadata) {
    val directory = Mp3Directory()
    metadata.addDirectory(directory)
    try {
      inputStream.reset()
      val reader: SequentialReader = StreamReader(inputStream)
      val header = reader.getInt32()
      // ID: MPEG-2.5, MPEG-2, or MPEG-1
      var id = 0.0
      when (header and 0x000180000 shr 19) {
        0 -> {
          directory.setString(Mp3Directory.TAG_ID, "MPEG-2.5")
          id = 2.5
          throw ImageProcessingException("MPEG-2.5 not supported.")
        }
        2 -> {
          directory.setString(Mp3Directory.TAG_ID, "MPEG-2")
          id = 2.0
        }
        3 -> {
          directory.setString(Mp3Directory.TAG_ID, "MPEG-1")
          id = 1.0
        }
        else -> {
        }
      }
      // Layer Type: 1, 2, 3, or not defined
      val layer = header and 0x00060000 shr 17
      when (layer) {
        0 -> directory.setString(Mp3Directory.TAG_LAYER, "Not defined")
        1 -> directory.setString(Mp3Directory.TAG_LAYER, "Layer III")
        2 -> directory.setString(Mp3Directory.TAG_LAYER, "Layer II")
        3 -> directory.setString(Mp3Directory.TAG_LAYER, "Layer I")
        else -> {
        }
      }
      val protectionBit = header and 0x00010000 shr 16
      // Bitrate: depends on ID and Layer
      val bitrate = header and 0x0000F000 shr 12
      if (bitrate != 0 && bitrate != 15) directory.setInt(Mp3Directory.TAG_BITRATE, setBitrate(bitrate, layer, id))
      // Frequency: depends on ID
      var frequency = header and 0x00000C00 shr 10
      val frequencyMapping = Array(2) { IntArray(3) }
      frequencyMapping[0] = intArrayOf(44100, 48000, 32000)
      frequencyMapping[1] = intArrayOf(22050, 24000, 16000)
      if (id == 2.0) {
        directory.setInt(Mp3Directory.TAG_FREQUENCY, frequencyMapping[1][frequency])
        frequency = frequencyMapping[1][frequency]
      } else if (id == 1.0) {
        directory.setInt(Mp3Directory.TAG_FREQUENCY, frequencyMapping[0][frequency])
        frequency = frequencyMapping[0][frequency]
      }
      val paddingBit = header and 0x00000200 shr 9
      // Encoding type: Stereo, Joint Stereo, Dual Channel, or Mono
      when (header and 0x000000C0 shr 6) {
        0 -> directory.setString(Mp3Directory.TAG_MODE, "Stereo")
        1 -> directory.setString(Mp3Directory.TAG_MODE, "Joint stereo")
        2 -> directory.setString(Mp3Directory.TAG_MODE, "Dual channel")
        3 -> directory.setString(Mp3Directory.TAG_MODE, "Mono")
        else -> {}
      }
      // Copyright boolean
      when (header and 0x00000008 shr 3) {
        0 -> directory.setString(Mp3Directory.TAG_COPYRIGHT, "False")
        1 -> directory.setString(Mp3Directory.TAG_COPYRIGHT, "True")
        else -> {
        }
      }
      when (header and 0x00000003) {
        0 -> directory.setString(Mp3Directory.TAG_EMPHASIS, "none")
        1 -> directory.setString(Mp3Directory.TAG_EMPHASIS, "50/15ms")
        3 -> directory.setString(Mp3Directory.TAG_EMPHASIS, "CCITT j.17")
        else -> {
        }
      }
      val frameSize = setBitrate(bitrate, layer, id) * 1000 * 144 / frequency
      directory.setString(Mp3Directory.TAG_FRAME_SIZE, "$frameSize bytes")
    } catch (e: IOException) {
      e.printStackTrace()
    } catch (e: ImageProcessingException) {
      e.printStackTrace()
    }
  }

  fun setBitrate(bitrate: Int, layer: Int, id: Double): Int {
    val bitrateMapping = Array(14) { IntArray(6) }
    bitrateMapping[0] = intArrayOf(32, 32, 32, 32, 32, 8)
    bitrateMapping[1] = intArrayOf(64, 48, 40, 64, 48, 16)
    bitrateMapping[2] = intArrayOf(96, 56, 48, 96, 56, 24)
    bitrateMapping[3] = intArrayOf(128, 64, 56, 128, 64, 32)
    bitrateMapping[4] = intArrayOf(160, 80, 64, 160, 80, 64)
    bitrateMapping[5] = intArrayOf(192, 96, 80, 192, 96, 80)
    bitrateMapping[6] = intArrayOf(224, 112, 96, 224, 112, 56)
    bitrateMapping[7] = intArrayOf(256, 128, 112, 256, 128, 64)
    bitrateMapping[8] = intArrayOf(288, 160, 128, 28, 160, 128)
    bitrateMapping[9] = intArrayOf(320, 192, 160, 320, 192, 160)
    bitrateMapping[10] = intArrayOf(352, 224, 192, 352, 224, 112)
    bitrateMapping[11] = intArrayOf(384, 256, 224, 384, 256, 128)
    bitrateMapping[12] = intArrayOf(416, 320, 256, 416, 320, 256)
    bitrateMapping[13] = intArrayOf(448, 384, 320, 448, 384, 320)
    var xPos = 0
    val yPos = bitrate - 1
    if (id == 2.0) { // MPEG-2
      when (layer) {
        1 -> xPos = 5
        2 -> xPos = 4
        3 -> xPos = 3
        else -> {
        }
      }
    } else if (id == 1.0) { // MPEG-1
      when (layer) {
        1 -> xPos = 2
        2 -> xPos = 1
        3 -> xPos = 0
        else -> {
        }
      }
    }
    return bitrateMapping[yPos][xPos]
  }

  companion object {
    /**
     * https://phoxis.org/2010/05/08/synch-safe/
     */
    fun getSyncSafeSize(decode: Int): Int {
      val a = decode and 0xFF
      val b = decode shr 8 and 0xFF
      val c = decode shr 16 and 0xFF
      val d = decode shr 24 and 0xFF
      var decoded = 0x0
      decoded = decoded or a
      decoded = decoded or (b shl 7)
      decoded = decoded or (c shl 14)
      decoded = decoded or (d shl 21)
      return decoded
    }
  }
}
