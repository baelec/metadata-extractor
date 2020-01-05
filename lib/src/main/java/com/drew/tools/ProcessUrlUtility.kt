@file:JvmName("ProcessUrlUtility")

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

import com.drew.imaging.readMetadata
import com.drew.imaging.ImageProcessingException
import com.drew.imaging.jpeg.JpegProcessingException
import com.drew.metadata.Metadata
import java.io.IOException
import java.net.URL
import kotlin.system.exitProcess

/**
 * Utility that extracts metadata found at a given URL.
 *
 * @author Drew Noakes https://drewnoakes.com
 */
@Throws(IOException::class, JpegProcessingException::class)
fun main(args: Array<String>) {
  if (args.isEmpty()) {
    System.err.println("Expects one or more URLs as arguments.")
    exitProcess(1)
  }
  for (url in args) processUrl(URL(url))
  println("Completed.")
}

@Throws(IOException::class)
private fun processUrl(url: URL) {
  val con = url.openConnection()
  // con.setConnectTimeout(connectTimeout);
  // con.setReadTimeout(readTimeout);
  val inputStream = con.getInputStream()
  // Read metadata
  val metadata: Metadata
  metadata = try {
    readMetadata(inputStream)
  } catch (e: ImageProcessingException) { // this is an error in the Jpeg segment structure.  we're looking for bad handling of
    // metadata segments.  in this case, we didn't even get a segment.
    System.err.println("%s: %s [Error Extracting Metadata]%n\t%s".format(e.javaClass.name, url, e.message))
    return
  } catch (t: Throwable) { // general, uncaught exception during processing of jpeg segments
    System.err.println("%s: %s [Error Extracting Metadata]".format(t.javaClass.name, url))
    t.printStackTrace(System.err)
    return
  }
  if (metadata.hasErrors()) {
    System.err.println(url)
    for (directory in metadata.directories) {
      if (!directory.hasErrors()) {
        continue
      }
      for (error in directory.errors) {
        System.err.println("\t[%s] %s".format(directory.name, error))
      }
    }
  }
  // Iterate through all values
  for (directory in metadata.directories) {
    for (tag in directory.tags) {
      val tagName = tag.tagName
      val directoryName = directory.name
      var description = tag.description
      // truncate the description if it's too long
      if (description != null && description.length > 1024) {
        description = "${description.substring(0, 1024)}..."
      }
      println("[%s] %s = %s".format(directoryName, tagName, description))
    }
  }
  // if (processedCount > 0)
  // System.out.println(String.format("Processed %,d files (%,d bytes) with %,d exceptions and %,d file errors in %s", processedCount, byteCount, exceptionCount, errorCount, path));
}
