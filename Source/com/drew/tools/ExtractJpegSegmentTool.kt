@file:JvmName("ExtractJpegSegmentTool")

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
package com.drew.tools

import com.drew.imaging.jpeg.JpegProcessingException
import com.drew.imaging.jpeg.JpegSegmentData
import com.drew.imaging.jpeg.JpegSegmentReader
import com.drew.imaging.jpeg.JpegSegmentType
import com.drew.lang.Iterables
import com.drew.lang.annotations.NotNull
import java.io.File
import java.io.IOException
import java.util.*
import kotlin.system.exitProcess

/**
 * Extracts JPEG segments and writes them to individual files.
 *
 *
 * Extracting only the required segment(s) for use in unit testing has several benefits:
 *
 *  * Helps reduce the repository size. For example a small JPEG image may still be 20kB+ in size, yet its
 * APPD (IPTC) segment may be as small as 200 bytes.
 *  * Makes unit tests run more rapidly.
 *  * Partially anonymises user-contributed data by removing image portions.
 *
 *
 * @author Drew Noakes https://drewnoakes.com
 */
@Throws(IOException::class, JpegProcessingException::class)
fun main(args: Array<String>) {
  if (args.isEmpty()) {
    printUsage()
    exitProcess(1)
  }
  val filePath = args[0]
  if (!File(filePath).exists()) {
    System.err.println("File does not exist")
    printUsage()
    exitProcess(1)
  }
  val segmentTypes: MutableSet<JpegSegmentType> = mutableSetOf()
  for (i in 1 until args.size) {
    val segmentType = JpegSegmentType.valueOf(args[i].toUpperCase())
    if (!segmentType.canContainMetadata) {
      System.err.println("WARNING: Segment type %s cannot contain metadata so it may not be necessary to extract it".format(segmentType))
    }
    segmentTypes.add(segmentType)
  }
  if (segmentTypes.isEmpty()) { // If none specified, use all that could reasonably contain metadata
    segmentTypes.addAll(JpegSegmentType.canContainMetadataTypes)
  }
  println("Reading: $filePath")
  val segmentData = JpegSegmentReader.readSegments(File(filePath), segmentTypes)
  saveSegmentFiles(filePath, segmentData)
}

@Throws(IOException::class)
fun saveSegmentFiles(jpegFilePath: String, segmentData: JpegSegmentData) {
  for (segmentType in segmentData.segmentTypes) {
    val segments = Iterables.toList(segmentData.getSegments(segmentType))
    if (segments.size == 0) {
      continue
    }
    if (segments.size > 1) {
      for (i in segments.indices) {
        val outputFilePath = "%s.%s.%d".format(jpegFilePath, segmentType.toString().toLowerCase(), i)
        println("Writing: $outputFilePath")
        saveBytes(File(outputFilePath), segments[i])
      }
    } else {
      val outputFilePath = "%s.%s".format(jpegFilePath, segmentType.toString().toLowerCase())
      println("Writing: $outputFilePath")
      saveBytes(File(outputFilePath), segments[0])
    }
  }
}

private fun printUsage() {
  println("USAGE:\n")
  println("\tjava com.drew.tools.ExtractJpegSegmentTool <filename> [<segment> ...]\n")
  print("Where <segment> is zero or more of:")
  for (segmentType in JpegSegmentType::class.java.enumConstants) {
    if (segmentType.canContainMetadata) {
      print(" $segmentType")
    }
  }
  println()
}
