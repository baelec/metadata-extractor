@file:JvmName("ByteUtil")

package com.drew.lang

fun getInt16(buffer: ByteArray, offset: Int, bigEndian: Boolean): Int {
  return if (bigEndian) {
    buffer[offset].toInt() and 0xFF shl 8 or
      (buffer[offset + 1].toInt() and 0xFF)
  } else {
    buffer[offset].toInt() and 0xFF or
      (buffer[offset + 1].toInt() and 0xFF shl 8)
  }
}

fun getInt32(buffer: ByteArray, offset: Int, bigEndian: Boolean): Int {
  return if (bigEndian) {
    buffer[offset].toInt() and 0xFF shl 24 or
      (buffer[offset + 1].toInt() and 0xFF shl 16) or
      (buffer[offset + 2].toInt() and 0xFF shl 8) or
      (buffer[offset + 3].toInt() and 0xFF)
  } else {
    buffer[offset].toInt() and 0xFF or
      (buffer[offset + 1].toInt() and 0xFF shl 8) or
      (buffer[offset + 2].toInt() and 0xFF shl 16) or
      (buffer[offset + 3].toInt() and 0xFF shl 24)
  }
}

fun getLong64(buffer: ByteArray, offset: Int, bigEndian: Boolean): Long {
  return if (bigEndian) {
    (buffer[offset].toLong() and 0xFF) shl 56 or
      ((buffer[offset + 1].toLong() and 0xFF) shl 48) or
      ((buffer[offset + 2].toLong() and 0xFF) shl 40) or
      ((buffer[offset + 3].toLong() and 0xFF) shl 32) or
      ((buffer[offset + 4].toLong() and 0xFF) shl 24) or
      (buffer[offset + 5].toLong() and 0xFF shl 16) or
      (buffer[offset + 6].toLong() and 0xFF shl 8) or
      (buffer[offset + 7].toLong() and 0xFF)
  } else {
    buffer[offset].toLong() and 0xFF or
      (buffer[offset + 1].toLong() and 0xFF shl 8) or
      (buffer[offset + 2].toLong() and 0xFF shl 16) or
      ((buffer[offset + 3].toLong() and 0xFF) shl 24) or
      ((buffer[offset + 4].toLong() and 0xFF) shl 32) or
      ((buffer[offset + 5].toLong() and 0xFF) shl 40) or
      ((buffer[offset + 6].toLong() and 0xFF) shl 48) or
      ((buffer[offset + 7].toLong() and 0xFF) shl 56)
  }
}
