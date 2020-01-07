@file:JvmName("ImageMetadataReader")

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
package com.drew.imaging

import com.drew.imaging.FileTypeDetector.Companion.detectFileType
import com.drew.imaging.quicktime.QuickTimeMetadataReader
import com.drew.imaging.tiff.TiffMetadataReader
import com.drew.lang.RandomAccessStreamReader
import com.drew.lang.urlEncode
import com.drew.metadata.Metadata
import com.drew.metadata.MetadataException
import com.drew.metadata.exif.ExifDirectoryBase
import com.drew.metadata.exif.ExifIFD0Directory
import com.drew.metadata.file.FileSystemMetadataReader
import com.drew.metadata.file.FileTypeDirectory
import com.drew.metadata.xmp.XmpDirectory
import java.io.BufferedInputStream
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream
import java.io.File
import kotlin.collections.ArrayList
import kotlin.system.exitProcess

/**
 * Reads metadata from any supported file format.
 *
 *
 * This class a lightweight wrapper around other, specific metadata processors.
 * During extraction, the file type is determined from the first few bytes of the file.
 * Parsing is then delegated to one of:
 *
 *
 *  * [AviMetadataReader] for AVI files
 *  * [BmpMetadataReader] for BMP files
 *  * [FileSystemMetadataReader] for metadata from the file system when a [File] is provided
 *  * [GifMetadataReader] for GIF files
 *  * [IcoMetadataReader] for ICO files
 *  * [JpegMetadataReader] for JPEG files
 *  * [Mp4MetadataReader] for MPEG-4 files
 *  * [PcxMetadataReader] for PCX files
 *  * [PngMetadataReader] for PNG files
 *  * [PsdMetadataReader] for Photoshop files
 *  * [QuickTimeMetadataReader] for QuickTime files
 *  * [RafMetadataReader] for RAF files
 *  * [TiffMetadataReader] for TIFF and (most) RAW files
 *  * [WavMetadataReader] for WAV files
 *  * [WebpMetadataReader] for WebP files
 *
 *
 * If you know the file type you're working with, you may use one of the above processors directly.
 * For most scenarios it is simpler, more convenient and more robust to use this class.
 *
 *
 * [FileTypeDetector] is used to determine the provided image's file type, and therefore
 * the appropriate metadata reader to use.
 *
 * @author Drew Noakes https://drewnoakes.com
 */
/**
 * Reads metadata from an [InputStream] of known length.
 *
 * @param inputStream a stream from which the file data may be read.  The stream must be positioned at the
 * beginning of the file's data.
 * @param streamLength the length of the stream, if known, otherwise -1.
 * @return a populated [Metadata] object containing directories of tags with values and any processing errors.
 * @throws ImageProcessingException if the file type is unknown, or for general processing errors.
 */
/**
 * Reads metadata from an [InputStream].
 *
 * @param inputStream a stream from which the file data may be read.  The stream must be positioned at the
 * beginning of the file's data.
 * @return a populated [Metadata] object containing directories of tags with values and any processing errors.
 * @throws ImageProcessingException if the file type is unknown, or for general processing errors.
 */
@JvmOverloads
@Throws(ImageProcessingException::class, IOException::class)
fun readMetadata(inputStream: InputStream, streamLength: Long = -1): Metadata {
  val bufferedInputStream = if (inputStream is BufferedInputStream) inputStream else BufferedInputStream(inputStream)
  val fileType = detectFileType(bufferedInputStream)
  val metadata = readMetadata(bufferedInputStream, streamLength, fileType)
  fileType?.let {
    metadata.addDirectory(FileTypeDirectory(it))
  }

  return metadata
}

/**
 * Reads metadata from an [InputStream] of known length and file type.
 *
 * @param inputStream a stream from which the file data may be read.  The stream must be positioned at the
 * beginning of the file's data.
 * @param streamLength the length of the stream, if known, otherwise -1.
 * @param fileType the file type of the data stream.
 * @return a populated [Metadata] object containing directories of tags with values and any processing errors.
 * @throws ImageProcessingException if the file type is unknown, or for general processing errors.
 */
@Throws(IOException::class, ImageProcessingException::class)
fun readMetadata(inputStream: InputStream, streamLength: Long, fileType: FileType?): Metadata {
  return when (fileType) {
    FileType.Jpeg -> readMetadata(inputStream)
    FileType.Tiff, FileType.Arw, FileType.Cr2, FileType.Nef, FileType.Orf, FileType.Rw2 -> TiffMetadataReader.readMetadata(RandomAccessStreamReader(inputStream, RandomAccessStreamReader.DEFAULT_CHUNK_LENGTH, streamLength))
    FileType.Psd -> readMetadata(inputStream)
    FileType.Png -> readMetadata(inputStream)
    FileType.Bmp -> readMetadata(inputStream)
    FileType.Gif -> readMetadata(inputStream)
    FileType.Ico -> readMetadata(inputStream)
    FileType.Pcx -> readMetadata(inputStream)
    FileType.WebP -> readMetadata(inputStream)
    FileType.Raf -> readMetadata(inputStream)
    FileType.Avi -> readMetadata(inputStream)
    FileType.Wav -> readMetadata(inputStream)
    FileType.Mov -> QuickTimeMetadataReader.readMetadata(inputStream)
    FileType.Mp4 -> readMetadata(inputStream)
    FileType.Mp3 -> readMetadata(inputStream)
    FileType.Eps -> readMetadata(inputStream)
    FileType.Heif -> readMetadata(inputStream)
    FileType.Unknown -> throw ImageProcessingException("File format could not be determined")
    else -> Metadata()
  }
}

/**
 * Reads [Metadata] from a [File] object.
 *
 * @param file a file from which the image data may be read.
 * @return a populated [Metadata] object containing directories of tags with values and any processing errors.
 * @throws ImageProcessingException for general processing errors.
 */
@Throws(ImageProcessingException::class, IOException::class)
fun readMetadata(file: File): Metadata {
  val metadata = FileInputStream(file).use {
    readMetadata(it, file.length())
  }
  FileSystemMetadataReader().read(file, metadata)
  return metadata
}

/**
 * An application entry point.  Takes the name of one or more files as arguments and prints the contents of all
 * metadata directories to `System.out`.
 *
 *
 * If `-thumb` is passed, then any thumbnail data will be written to a file with name of the
 * input file having `.thumb.jpg` appended.
 *
 *
 * If `-markdown` is passed, then output will be in markdown format.
 *
 *
 * If `-hex` is passed, then the ID of each tag will be displayed in hexadecimal.
 *
 * @param args the command line arguments
 */
@Throws(MetadataException::class, IOException::class)
fun main(args: Array<String>) {
  val argList: MutableCollection<String> = ArrayList(listOf(*args))
  val markdownFormat = argList.remove("-markdown")
  val showHex = argList.remove("-hex")
  if (argList.isEmpty()) {
    val version = Companion.getVersion()
    println("metadata-extractor version $version")
    println()
    println("Usage: java -jar metadata-extractor-${version
      ?: "a.b.c"}.jar <filename> [<filename>] [-thumb] [-markdown] [-hex]")
    exitProcess(1)
  }
  for (filePath in argList) {
    val startTime = System.nanoTime()
    val file = File(filePath)
    if (!markdownFormat && argList.size > 1) println("\n***** PROCESSING: $filePath\n")
    val metadata = try {
      readMetadata(file)
    } catch (e: Exception) {
      e.printStackTrace(System.err)
      exitProcess(1)
    }
    val took = System.nanoTime() - startTime
    if (!markdownFormat) {
      print("Processed %.3f MB file in %.2f ms%n%n".format(file.length() / (1024.0 * 1024), took / 1000000.0))
    }
    if (markdownFormat) {
      val fileName = file.name
      val urlName = urlEncode(filePath)
      val exifIFD0Directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory::class.java)
      val make = if (exifIFD0Directory == null) "" else exifIFD0Directory.getString(ExifDirectoryBase.TAG_MAKE)
      val model = if (exifIFD0Directory == null) "" else exifIFD0Directory.getString(ExifDirectoryBase.TAG_MODEL)
      println()
      println("---")
      println()
      println("# $make - $model")
      println("""<a href="https://raw.githubusercontent.com/drewnoakes/metadata-extractor-images/master/$urlName">""")
      println("""<img src="https://raw.githubusercontent.com/drewnoakes/metadata-extractor-images/master/$urlName" width="300"/><br/>""")
      println(fileName)
      println("</a>")
      println()
      println("Directory | Tag Id | Tag Name | Extracted Value")
      println(":--------:|-------:|----------|----------------")
    }
    // iterate over the metadata and print to System.out
    for (directory in metadata.directories) {
      val directoryName = directory.name
      for (tag in directory.tags) {
        val tagName = tag.tagName
        var description = tag.description
        // truncate the description if it's too long
        if (description != null && description.length > 1024) {
          description = "${description.substring(0, 1024)}..."
        }
        if (markdownFormat) {
          println("$directoryName|0x${Integer.toHexString(tag.tagType)}|$tagName|$description")
        } else {
          // simple formatting
          if (showHex) {
            println("[$directoryName - ${tag.tagTypeHex}] $tagName = $description")
          } else {
            println("[$directoryName] $tagName = $description")
          }
        }
      }
      if (directory is XmpDirectory) {
        val xmpProperties = directory.xmpProperties
        for (property in xmpProperties.entries) {
          val key = property.key
          var value = property.value
          if (value.length > 1024) {
            value = "${value.substring(0, 1024)}..."
          }
          if (markdownFormat) {
            println("$directoryName||$key|$value")
          } else {
            println("[$directoryName] $key = $value")
          }
        }
      }
      // print out any errors
      for (error in directory.errors) System.err.println("ERROR: $error")
    }
  }
}

private val Companion = object {
  fun getVersion(): String {
    return javaClass.getPackage().implementationVersion
  }
}
