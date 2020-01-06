package com.drew.metadata.heif.boxes

import com.drew.lang.ByteArrayReader
import com.drew.lang.SequentialReader
import com.drew.metadata.Metadata
import com.drew.metadata.heif.HeifDirectory
import com.drew.metadata.icc.IccReader

/**
 * ISO/IEC 14496-12:2015 pg.159
 */
class ColourInformationBox(reader: SequentialReader, box: Box, metadata: Metadata) : Box(box) {
  var colourType: String
  var colourPrimaries = 0
  var transferCharacteristics = 0
  var matrixCoefficients = 0
  var fullRangeFlag = 0
  fun addMetadata(directory: HeifDirectory?) {}

  init {
    colourType = reader.getString(4)
    if (colourType == "nclx") {
      colourPrimaries = reader.getUInt16()
      transferCharacteristics = reader.getUInt16()
      matrixCoefficients = reader.getUInt16()
      // Last 7 bits are reserved
      fullRangeFlag = reader.getUInt8().toInt() and 0x80 shr 7
    } else if (colourType == "rICC") {
      val buffer = reader.getBytes((size - 12).toInt())
      IccReader().extract(ByteArrayReader(buffer), metadata)
    } else if (colourType == "prof") {
      val buffer = reader.getBytes((size - 12).toInt())
      IccReader().extract(ByteArrayReader(buffer), metadata)
    }
  }
}
