package com.drew.metadata.heif.boxes

import com.drew.lang.SequentialReader
import com.drew.metadata.heif.HeifDirectory

/**
 * ISO/IEC 23008-12:2017 pg.15
 */
class ImageRotationBox(reader: SequentialReader, box: Box) : Box(box) {
  // First 6 bits are reserved
  var angle: Int = reader.getUInt8().toInt() and 0x03
  fun addMetadata(directory: HeifDirectory) {
    if (!directory.containsTag(HeifDirectory.TAG_IMAGE_ROTATION)) {
      directory.setInt(HeifDirectory.TAG_IMAGE_ROTATION, angle)
    }
  }
}
