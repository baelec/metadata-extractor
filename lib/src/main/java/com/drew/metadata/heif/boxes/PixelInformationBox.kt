package com.drew.metadata.heif.boxes

import com.drew.lang.SequentialReader
import com.drew.metadata.heif.HeifDirectory

/**
 * ISO/IEC 23008-12:2017 pg.13
 */
class PixelInformationBox(reader: SequentialReader, box: Box) : FullBox(reader, box) {
  var numChannels: Int = reader.getUInt8().toInt()
  var channels: IntArray
  fun addMetadata(directory: HeifDirectory) {
    if (!directory.containsTag(HeifDirectory.TAG_BITS_PER_CHANNEL)) {
      directory.setIntArray(HeifDirectory.TAG_BITS_PER_CHANNEL, channels)
    }
  }

  init {
    channels = IntArray(numChannels)
    for (i in channels.indices) {
      channels[i] = reader.getUInt8().toInt()
    }
  }
}
