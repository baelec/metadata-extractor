package com.drew.imaging.jpeg

import com.drew.metadata.Metadata

/**
 * Defines an object that extracts metadata from in JPEG segments.
 */
interface JpegSegmentMetadataReader {
  /**
   * Gets the set of JPEG segment types that this reader is interested in.
   */
  val segmentTypes: Iterable<JpegSegmentType?>

  /**
   * Extracts metadata from all instances of a particular JPEG segment type.
   *
   * @param segments A sequence of byte arrays from which the metadata should be extracted. These are in the order
   * encountered in the original file.
   * @param metadata The [Metadata] object into which extracted values should be merged.
   * @param segmentType The [JpegSegmentType] being read.
   */
  fun readJpegSegments(segments: Iterable<ByteArray?>, metadata: Metadata, segmentType: JpegSegmentType)
}
