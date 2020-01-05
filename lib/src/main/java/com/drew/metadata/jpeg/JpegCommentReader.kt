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
package com.drew.metadata.jpeg

import com.drew.imaging.jpeg.JpegSegmentMetadataReader
import com.drew.imaging.jpeg.JpegSegmentType
import com.drew.metadata.Metadata
import com.drew.metadata.StringValue

/**
 * Decodes the comment stored within JPEG files, populating a [Metadata] object with tag values in a
 * [JpegCommentDirectory].
 *
 * @author Drew Noakes https://drewnoakes.com
 */
class JpegCommentReader : JpegSegmentMetadataReader {
  override val segmentTypes: Iterable<JpegSegmentType>
    get() = listOf(JpegSegmentType.COM)

  override fun readJpegSegments(segments: Iterable<ByteArray>, metadata: Metadata, segmentType: JpegSegmentType) {
    for (segmentBytes in segments) {
      val directory = JpegCommentDirectory()
      metadata.addDirectory(directory)
      // The entire contents of the directory are the comment
      directory.setStringValue(JpegCommentDirectory.TAG_COMMENT, StringValue(segmentBytes, null))
    }
  }
}
