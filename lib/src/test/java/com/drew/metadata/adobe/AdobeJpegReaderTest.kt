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
package com.drew.metadata.adobe

import com.drew.imaging.jpeg.JpegSegmentType
import com.drew.lang.SequentialByteArrayReader
import com.drew.metadata.Metadata
import com.drew.tools.readBytes
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Test
import java.io.IOException

/**
 * @author Drew Noakes https://drewnoakes.com
 */
class AdobeJpegReaderTest {
  @Test
  @Throws(Exception::class)
  fun testSegmentTypes() {
    val reader = AdobeJpegReader()
    assertEquals(1, reader.segmentTypes.toList().size)
    assertEquals(JpegSegmentType.APPE, reader.segmentTypes.toList()[0])
  }

  @Test
  @Throws(Exception::class)
  fun testReadAdobeJpegMetadata1() {
    val directory = processBytes("src/test/resources/adobeJpeg1.jpg.appe")
    assertFalse(directory.errors.toString(), directory.hasErrors())
    assertEquals(4, directory.tagCount.toLong())
    assertEquals(1, directory.getInt(AdobeJpegDirectory.TAG_COLOR_TRANSFORM).toLong())
    assertEquals(25600, directory.getInt(AdobeJpegDirectory.TAG_DCT_ENCODE_VERSION).toLong())
    assertEquals(128, directory.getInt(AdobeJpegDirectory.TAG_APP14_FLAGS0).toLong())
    assertEquals(0, directory.getInt(AdobeJpegDirectory.TAG_APP14_FLAGS1).toLong())
  }

  companion object {
    @Throws(IOException::class)
    fun processBytes(filePath: String): AdobeJpegDirectory {
      val metadata = Metadata()
      AdobeJpegReader().extract(SequentialByteArrayReader(readBytes(filePath)), metadata)
      val directory = metadata.getFirstDirectoryOfType(AdobeJpegDirectory::class.java)
      assertNotNull(directory)
      return directory!!
    }
  }
}
