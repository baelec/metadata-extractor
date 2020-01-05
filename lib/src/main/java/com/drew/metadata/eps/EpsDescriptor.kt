package com.drew.metadata.eps

import com.drew.metadata.TagDescriptor

/**
 * @author Payton Garland
 */
class EpsDescriptor(directory: EpsDirectory) : TagDescriptor<EpsDirectory>(directory) {
  override fun getDescription(tagType: Int): String? {
    return when (tagType) {
      EpsDirectory.TAG_IMAGE_WIDTH, EpsDirectory.TAG_IMAGE_HEIGHT -> getPixelDescription(tagType)
      EpsDirectory.TAG_TIFF_PREVIEW_SIZE, EpsDirectory.TAG_TIFF_PREVIEW_OFFSET -> getByteSizeDescription(tagType)
      EpsDirectory.TAG_COLOR_TYPE -> colorTypeDescription
      else -> _directory.getString(tagType)
    }
  }

  fun getPixelDescription(tagType: Int): String {
    return _directory.getString(tagType) + " pixels"
  }

  fun getByteSizeDescription(tagType: Int): String {
    return _directory.getString(tagType) + " bytes"
  }

  val colorTypeDescription: String?
    get() = getIndexedDescription(EpsDirectory.TAG_COLOR_TYPE, 1,
      "Grayscale", "Lab", "RGB", "CMYK")
}
