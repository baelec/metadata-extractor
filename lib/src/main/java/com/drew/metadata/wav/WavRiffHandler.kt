package com.drew.metadata.wav

import com.drew.imaging.riff.RiffHandler
import com.drew.lang.ByteArrayReader
import com.drew.metadata.Metadata
import com.drew.metadata.MetadataException
import java.io.IOException
import kotlin.math.pow
import kotlin.math.roundToInt

/**
 * Implementation of [RiffHandler] specialising in Wav support.
 *
 * Extracts data from chunk/list types:
 *
 *
 *  * `"INFO"`: artist, title, product, track number, date created, genre, comments, copyright, software, duration
 *  * `"fmt "`: format, channels, samples/second, bytes/second, block alignment, bits/sample
 *  * `"data"`: duration
 *
 *
 * Sources: http://www.neurophys.wisc.edu/auditory/riff-format.txt
 * http://www-mmsp.ece.mcgill.ca/Documents/AudioFormats/WAVE/WAVE.html
 * http://wiki.audacityteam.org/wiki/WAV
 *
 * @author Payton Garland
 */
class WavRiffHandler(metadata: Metadata) : RiffHandler {
  private val _directory: WavDirectory = WavDirectory()
  private var _currentList = ""
  override fun shouldAcceptRiffIdentifier(identifier: String): Boolean {
    return identifier == WavDirectory.FORMAT
  }

  override fun shouldAcceptChunk(fourCC: String): Boolean {
    return (fourCC == WavDirectory.CHUNK_FORMAT || _currentList == WavDirectory.LIST_INFO && WavDirectory._tagIntegerMap.containsKey(fourCC)
      || fourCC == WavDirectory.CHUNK_DATA)
  }

  override fun shouldAcceptList(fourCC: String): Boolean {
    return if (fourCC == WavDirectory.LIST_INFO) {
      _currentList = WavDirectory.LIST_INFO
      true
    } else {
      _currentList = ""
      false
    }
  }

  override fun processChunk(fourCC: String, payload: ByteArray) {
    try {
      when {
        fourCC == WavDirectory.CHUNK_FORMAT -> {
          val reader = ByteArrayReader(payload)
          reader.isMotorolaByteOrder = false
          val wFormatTag = reader.getInt16(0).toInt()
          val wChannels = reader.getInt16(2).toInt()
          val dwSamplesPerSec = reader.getInt32(4)
          val dwAvgBytesPerSec = reader.getInt32(8)
          val wBlockAlign = reader.getInt16(12).toInt()
          when (wFormatTag) {
            0x0001 -> {
              val wBitsPerSample = reader.getInt16(14).toInt()
              _directory.setInt(WavDirectory.TAG_BITS_PER_SAMPLE, wBitsPerSample)
              _directory.setString(WavDirectory.TAG_FORMAT, WavDirectory._audioEncodingMap[wFormatTag]!!)
            }
            else -> if (WavDirectory._audioEncodingMap.containsKey(wFormatTag)) {
              _directory.setString(WavDirectory.TAG_FORMAT, WavDirectory._audioEncodingMap[wFormatTag]!!)
            } else {
              _directory.setString(WavDirectory.TAG_FORMAT, "Unknown")
            }
          }
          _directory.setInt(WavDirectory.TAG_CHANNELS, wChannels)
          _directory.setInt(WavDirectory.TAG_SAMPLES_PER_SEC, dwSamplesPerSec)
          _directory.setInt(WavDirectory.TAG_BYTES_PER_SEC, dwAvgBytesPerSec)
          _directory.setInt(WavDirectory.TAG_BLOCK_ALIGNMENT, wBlockAlign)
        }
        fourCC == WavDirectory.CHUNK_DATA -> {
          try {
            if (_directory.containsTag(WavDirectory.TAG_BYTES_PER_SEC)) {
              val duration = payload.size.toDouble() / _directory.getDouble(WavDirectory.TAG_BYTES_PER_SEC)
              val hours = duration.toInt() / 60.0.pow(2.0).toInt()
              val minutes = duration.toInt() / 60.0.pow(1.0).toInt() - hours * 60
              val seconds = (duration / 60.0.pow(0.0) - minutes * 60).roundToInt()
              val time = "%1$02d:%2$02d:%3$02d".format(hours, minutes, seconds)
              _directory.setString(WavDirectory.TAG_DURATION, time)
            }
          } catch (ex: MetadataException) {
            _directory.addError("Error calculating duration: bytes per second not found")
          }
        }
        WavDirectory._tagIntegerMap.containsKey(fourCC) -> {
          _directory.setString(WavDirectory._tagIntegerMap[fourCC]!!, String(payload).substring(0, payload.size - 1))
        }
      }
    } catch (ex: IOException) {
      ex.message?.let {
        _directory.addError(it)
      }
    }
  }

  init {
    metadata.addDirectory(_directory)
  }
}
