package com.drew.metadata.mov.atoms.canon

import com.drew.imaging.jpeg.*
import com.drew.lang.SequentialReader
import com.drew.lang.StreamReader
import com.drew.metadata.Directory
import com.drew.metadata.Metadata
import com.drew.metadata.exif.ExifDirectoryBase
import com.drew.metadata.exif.ExifIFD0Directory
import com.drew.metadata.exif.ExifReader
import com.drew.metadata.mov.QuickTimeDirectory
import com.drew.metadata.mov.atoms.Atom
import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStream
import java.util.*

/**
 *
 * @author PerB
 */
class CanonThumbnailAtom(reader: SequentialReader) : Atom(reader) {
  private val dateTime: String?
  /**
   * Canon Data Block (Exif/TIFF ThumbnailImage)
   */
  @Throws(IOException::class)
  private fun readCNDA(reader: SequentialReader): String? {
    if (type.compareTo("CNDA") == 0) { // From JpegMetadataReader
      val exifReader: JpegSegmentMetadataReader = ExifReader()
      val exifStream: InputStream = ByteArrayInputStream(reader.getBytes(size.toInt()))
      val segmentTypes: MutableSet<JpegSegmentType> = HashSet()
      for (type in exifReader.segmentTypes) {
        segmentTypes.add(type)
      }
      val segmentData: JpegSegmentData
      segmentData = try {
        readSegments(StreamReader(exifStream), segmentTypes)
      } catch (e: JpegProcessingException) {
        return null
      }
      // TODO should we keep all extracted metadata here?
      val metadata = Metadata()
      for (segmentType in exifReader.segmentTypes) {
        exifReader.readJpegSegments(segmentData.getSegments(segmentType), metadata, segmentType)
      }
      val directory: Directory? = metadata.getFirstDirectoryOfType(ExifIFD0Directory::class.java)
      if (directory != null) {
        for (tag in directory.tags) {
          if (tag.tagType == ExifDirectoryBase.TAG_DATETIME) {
            return tag.description
          }
        }
      }
    }
    return null
  }

  fun addMetadata(directory: QuickTimeDirectory) {
    if (dateTime != null) {
      directory.setString(QuickTimeDirectory.TAG_CANON_THUMBNAIL_DT, dateTime)
    }
  }

  init {
    dateTime = readCNDA(reader)
  }
}
