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
package com.drew.imaging.png

import org.junit.Assert
import org.junit.Test

/**
 * @author Drew Noakes https://drewnoakes.com
 */
class PngChunkTypeTest {
  @Test
  @Throws(Exception::class)
  fun testConstructorTooLong() {
    try {
      PngChunkType("TooLong")
      Assert.fail("Expecting exception")
    } catch (ex: PngProcessingException) {
      Assert.assertEquals("PNG chunk type identifier must be four bytes in length", ex.message)
    }
  }

  @Test
  @Throws(Exception::class)
  fun testConstructorTooShort() {
    try {
      PngChunkType("foo")
      Assert.fail("Expecting exception")
    } catch (ex: PngProcessingException) {
      Assert.assertEquals("PNG chunk type identifier must be four bytes in length", ex.message)
    }
  }

  @Test
  @Throws(Exception::class)
  fun testConstructorInvalidBytes() {
    val invalidStrings = arrayOf("ABC1", "1234", "    ", "!£$%")
    for (invalidString in invalidStrings) {
      try {
        PngChunkType(invalidString)
        Assert.fail("Expecting exception")
      } catch (ex: PngProcessingException) {
        Assert.assertEquals("PNG chunk type identifier may only contain alphabet characters", ex.message)
      }
    }
  }

  @Test
  @Throws(Exception::class)
  fun testConstructorValidBytes() {
    val validStrings = arrayOf("ABCD", "abcd", "wxyz", "WXYZ", "lkjh", "LKJH")
    for (validString in validStrings) {
      PngChunkType(validString)
    }
  }

  @Test
  @Throws(Exception::class)
  fun testIsCritical() {
    Assert.assertTrue(PngChunkType("ABCD").isCritical)
    Assert.assertFalse(PngChunkType("aBCD").isCritical)
  }

  @Test
  @Throws(Exception::class)
  fun testIsAncillary() {
    Assert.assertFalse(PngChunkType("ABCD").isAncillary)
    Assert.assertTrue(PngChunkType("aBCD").isAncillary)
  }

  @Test
  @Throws(Exception::class)
  fun testIsPrivate() {
    Assert.assertTrue(PngChunkType("ABCD").isPrivate)
    Assert.assertFalse(PngChunkType("AbCD").isPrivate)
  }

  @Test
  @Throws(Exception::class)
  fun testIsSafeToCopy() {
    Assert.assertFalse(PngChunkType("ABCD").isSafeToCopy)
    Assert.assertTrue(PngChunkType("ABCd").isSafeToCopy)
  }

  @Test
  @Throws(Exception::class)
  fun testAreMultipleAllowed() {
    Assert.assertFalse(PngChunkType("ABCD").areMultipleAllowed())
    Assert.assertFalse(PngChunkType("ABCD", false).areMultipleAllowed())
    Assert.assertTrue(PngChunkType("ABCD", true).areMultipleAllowed())
  }

  @Test
  @Throws(Exception::class)
  fun testEquality() {
    Assert.assertEquals(PngChunkType("ABCD"), PngChunkType("ABCD"))
    Assert.assertEquals(PngChunkType("ABCD", true), PngChunkType("ABCD", true))
    Assert.assertEquals(PngChunkType("ABCD", false), PngChunkType("ABCD", false))
    // NOTE we don't consider the 'allowMultiples' value in the equality test (or hash code)
    Assert.assertEquals(PngChunkType("ABCD", true), PngChunkType("ABCD", false))
    Assert.assertNotEquals(PngChunkType("ABCD"), PngChunkType("abcd"))
  }
}
