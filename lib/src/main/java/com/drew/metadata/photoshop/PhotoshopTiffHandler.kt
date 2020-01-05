package com.drew.metadata.photoshop

import com.drew.lang.ByteArrayReader
import com.drew.lang.RandomAccessReader
import com.drew.lang.SequentialByteArrayReader
import com.drew.metadata.Directory
import com.drew.metadata.Metadata
import com.drew.metadata.exif.ExifTiffHandler
import com.drew.metadata.icc.IccReader
import com.drew.metadata.xmp.XmpReader
import java.io.IOException

/**
 * @author Payton Garland
 */
class PhotoshopTiffHandler(metadata: Metadata, parentDirectory: Directory?) : ExifTiffHandler(metadata, parentDirectory) {
  @Throws(IOException::class)
  override fun customProcessTag(tagOffset: Int,
                                processedIfdOffsets: Set<Int>,
                                tiffHeaderOffset: Int,
                                reader: RandomAccessReader,
                                tagId: Int,
                                byteCount: Int): Boolean {
    when (tagId) {
      TAG_XMP -> {
        XmpReader().extract(reader.getBytes(tagOffset, byteCount), _metadata)
        return true
      }
      TAG_PHOTOSHOP_IMAGE_RESOURCES -> {
        PhotoshopReader().extract(SequentialByteArrayReader(reader.getBytes(tagOffset, byteCount)), byteCount, _metadata)
        return true
      }
      TAG_ICC_PROFILES -> {
        IccReader().extract(ByteArrayReader(reader.getBytes(tagOffset, byteCount)), _metadata)
        return true
      }
    }
    return super.customProcessTag(tagOffset, processedIfdOffsets, tiffHeaderOffset, reader, tagId, byteCount)
  }

  companion object {
    // Photoshop-specific Tiff Tags
    // http://www.adobe.com/devnet-apps/photoshop/fileformatashtml/#50577413_pgfId-1039502
    private const val TAG_PAGE_MAKER_EXTENSION = 0x014A
    private const val TAG_JPEG_TABLES = 0X01B5
    private const val TAG_XMP = 0x02BC
    private const val TAG_FILE_INFORMATION = 0x83BB
    private const val TAG_PHOTOSHOP_IMAGE_RESOURCES = 0x8649
    private const val TAG_EXIF_IFD_POINTER = 0x8769
    private const val TAG_ICC_PROFILES = 0x8773
    private const val TAG_EXIF_GPS = 0x8825
    private const val TAG_T_IMAGE_SOURCE_DATA = 0x935C
    private const val TAG_T_ANNOTATIONS = 0xC44F
  }
}
