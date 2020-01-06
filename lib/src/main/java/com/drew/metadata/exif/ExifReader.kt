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
package com.drew.metadata.exif

import com.drew.imaging.jpeg.JpegSegmentMetadataReader
import com.drew.imaging.jpeg.JpegSegmentType
import com.drew.imaging.tiff.TiffProcessingException
import com.drew.imaging.tiff.TiffReader
import com.drew.lang.ByteArrayReader
import com.drew.lang.RandomAccessReader
import com.drew.metadata.Directory
import com.drew.metadata.Metadata
import java.io.IOException

/**
 * Decodes Exif binary data, populating a [Metadata] object with tag values in [ExifSubIFDDirectory],
 * [ExifThumbnailDirectory], [ExifInteropDirectory], [GpsDirectory] and one of the many camera
 * makernote directories.
 *
 * @author Drew Noakes https://drewnoakes.com
 */
class ExifReader : JpegSegmentMetadataReader {
  override val segmentTypes: Iterable<JpegSegmentType>
    get() = listOf(JpegSegmentType.APP1)

  override fun readJpegSegments(segments: Iterable<ByteArray>, metadata: Metadata, segmentType: JpegSegmentType) {
    assert(segmentType === JpegSegmentType.APP1)
    for (segmentBytes in segments) { // Filter any segments containing unexpected preambles
      if (segmentBytes.size < JPEG_SEGMENT_PREAMBLE.length || String(segmentBytes, 0, JPEG_SEGMENT_PREAMBLE.length) != JPEG_SEGMENT_PREAMBLE) continue
      extract(ByteArrayReader(segmentBytes), metadata, JPEG_SEGMENT_PREAMBLE.length)
    }
  }
  /** Reads TIFF formatted Exif data at a specified offset within a [RandomAccessReader].  */
  /** Reads TIFF formatted Exif data a specified offset within a [RandomAccessReader].  */
  /** Reads TIFF formatted Exif data from start of the specified [RandomAccessReader].  */
  @JvmOverloads
  fun extract(reader: RandomAccessReader, metadata: Metadata, readerOffset: Int = 0, parentDirectory: Directory? = null) {
    val exifTiffHandler = ExifTiffHandler(metadata, parentDirectory)
    try { // Read the TIFF-formatted Exif data
      TiffReader().processTiff(
        reader,
        exifTiffHandler,
        readerOffset
      )
    } catch (e: TiffProcessingException) {
      exifTiffHandler.error("Exception processing TIFF data: ${e.message}")
      // TODO what do to with this error state?
      e.printStackTrace(System.err)
    } catch (e: IOException) {
      exifTiffHandler.error("Exception processing TIFF data: ${e.message}")
      // TODO what do to with this error state?
      e.printStackTrace(System.err)
    }
  }

  companion object {
    /** Exif data stored in JPEG files' APP1 segment are preceded by this six character preamble.  */
    const val JPEG_SEGMENT_PREAMBLE = "Exif\u0000\u0000"
  }
}
