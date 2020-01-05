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
package com.drew.imaging.jpeg

import com.drew.imaging.jpeg.JpegSegmentType.Companion.fromByte
import java.util.*

/**
 * Holds a collection of JPEG data segments.  This need not necessarily be all segments
 * within the JPEG. For example, it may be convenient to store only the non-image
 * segments when analysing metadata.
 *
 *
 * Segments are keyed via their [JpegSegmentType]. Where multiple segments use the
 * same segment type, they will all be stored and available.
 *
 *
 * Each segment type may contain multiple entries. Conceptually the model is:
 * `Map<JpegSegmentType, Collection<byte[]>>`. This class provides
 * convenience methods around that structure.
 *
 * @author Drew Noakes https://drewnoakes.com
 */
class JpegSegmentData {
  // TODO key this on JpegSegmentType rather than Byte, and hopefully lose much of the use of 'byte' with this class
  private val _segmentDataMap = HashMap<Byte, MutableList<ByteArray>>(10)

  /**
   * Adds segment bytes to the collection.
   *
   * @param segmentType  the type of the segment being added
   * @param segmentBytes the byte array holding data for the segment being added
   */
  fun addSegment(segmentType: Byte, segmentBytes: ByteArray) {
    getOrCreateSegmentList(segmentType).add(segmentBytes)
  }

  /**
   * Gets the set of JPEG segment type identifiers.
   */
  val segmentTypes: Iterable<JpegSegmentType>
    get() {
      val segmentTypes: MutableSet<JpegSegmentType> = HashSet()
      for (segmentTypeByte in _segmentDataMap.keys) {
        val segmentType = fromByte(segmentTypeByte)
          ?: throw IllegalStateException("Should not have a segmentTypeByte that is not in the enum: " + Integer.toHexString(segmentTypeByte.toInt()))
        segmentTypes.add(segmentType)
      }
      return segmentTypes
    }

  /**
   * Gets the first JPEG segment data for the specified type.
   *
   * @param segmentType the JpegSegmentType for the desired segment
   * @return a byte[] containing segment data or null if no data exists for that segment
   */
  fun getSegment(segmentType: Byte): ByteArray? {
    return getSegment(segmentType, 0)
  }

  /**
   * Gets the first JPEG segment data for the specified type.
   *
   * @param segmentType the JpegSegmentType for the desired segment
   * @return a byte[] containing segment data or null if no data exists for that segment
   */
  fun getSegment(segmentType: JpegSegmentType): ByteArray? {
    return getSegment(segmentType.byteValue, 0)
  }

  /**
   * Gets segment data for a specific occurrence and type.  Use this method when more than one occurrence
   * of segment data for a given type exists.
   *
   * @param segmentType identifies the required segment
   * @param occurrence  the zero-based index of the occurrence
   * @return the segment data as a byte[], or null if no segment exists for the type &amp; occurrence
   */
  fun getSegment(segmentType: JpegSegmentType, occurrence: Int): ByteArray? {
    return getSegment(segmentType.byteValue, occurrence)
  }

  /**
   * Gets segment data for a specific occurrence and type.  Use this method when more than one occurrence
   * of segment data for a given type exists.
   *
   * @param segmentType identifies the required segment
   * @param occurrence  the zero-based index of the occurrence
   * @return the segment data as a byte[], or null if no segment exists for the type &amp; occurrence
   */
  fun getSegment(segmentType: Byte, occurrence: Int): ByteArray? {
    val segmentList = getSegmentList(segmentType)
    return if (segmentList != null && segmentList.size > occurrence) segmentList[occurrence] else null
  }

  /**
   * Returns all instances of a given JPEG segment.  If no instances exist, an empty sequence is returned.
   *
   * @param segmentType a number which identifies the type of JPEG segment being queried
   * @return zero or more byte arrays, each holding the data of a JPEG segment
   */
  fun getSegments(segmentType: JpegSegmentType): Iterable<ByteArray> {
    return getSegments(segmentType.byteValue)
  }

  /**
   * Returns all instances of a given JPEG segment.  If no instances exist, an empty sequence is returned.
   *
   * @param segmentType a number which identifies the type of JPEG segment being queried
   * @return zero or more byte arrays, each holding the data of a JPEG segment
   */
  fun getSegments(segmentType: Byte): Iterable<ByteArray> {
    val segmentList = getSegmentList(segmentType)
    return segmentList ?: ArrayList()
  }

  private fun getSegmentList(segmentType: Byte): List<ByteArray>? {
    return _segmentDataMap[segmentType]
  }

  private fun getOrCreateSegmentList(segmentType: Byte): MutableList<ByteArray> {
    return _segmentDataMap.getOrPut(segmentType, { ArrayList() })
  }

  /**
   * Returns the count of segment data byte arrays stored for a given segment type.
   *
   * @param segmentType identifies the required segment
   * @return the segment count (zero if no segments exist).
   */
  fun getSegmentCount(segmentType: JpegSegmentType): Int {
    return getSegmentCount(segmentType.byteValue)
  }

  /**
   * Returns the count of segment data byte arrays stored for a given segment type.
   *
   * @param segmentType identifies the required segment
   * @return the segment count (zero if no segments exist).
   */
  fun getSegmentCount(segmentType: Byte): Int {
    val segmentList = getSegmentList(segmentType)
    return segmentList?.size ?: 0
  }

  /**
   * Removes a specified instance of a segment's data from the collection.  Use this method when more than one
   * occurrence of segment data exists for a given type exists.
   *
   * @param segmentType identifies the required segment
   * @param occurrence  the zero-based index of the segment occurrence to remove.
   */
  fun removeSegmentOccurrence(segmentType: JpegSegmentType, occurrence: Int) {
    removeSegmentOccurrence(segmentType.byteValue, occurrence)
  }

  /**
   * Removes a specified instance of a segment's data from the collection.  Use this method when more than one
   * occurrence of segment data exists for a given type exists.
   *
   * @param segmentType identifies the required segment
   * @param occurrence  the zero-based index of the segment occurrence to remove.
   */
  fun removeSegmentOccurrence(segmentType: Byte, occurrence: Int) {
    val segmentList = _segmentDataMap[segmentType]
    segmentList?.removeAt(occurrence)
  }

  /**
   * Removes all segments from the collection having the specified type.
   *
   * @param segmentType identifies the required segment
   */
  fun removeSegment(segmentType: JpegSegmentType) {
    removeSegment(segmentType.byteValue)
  }

  /**
   * Removes all segments from the collection having the specified type.
   *
   * @param segmentType identifies the required segment
   */
  fun removeSegment(segmentType: Byte) {
    _segmentDataMap.remove(segmentType)
  }

  /**
   * Determines whether data is present for a given segment type.
   *
   * @param segmentType identifies the required segment
   * @return true if data exists, otherwise false
   */
  fun containsSegment(segmentType: JpegSegmentType): Boolean {
    return containsSegment(segmentType.byteValue)
  }

  /**
   * Determines whether data is present for a given segment type.
   *
   * @param segmentType identifies the required segment
   * @return true if data exists, otherwise false
   */
  fun containsSegment(segmentType: Byte): Boolean {
    return _segmentDataMap.containsKey(segmentType)
  }
}
