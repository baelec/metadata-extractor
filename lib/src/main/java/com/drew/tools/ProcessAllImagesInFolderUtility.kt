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
@file:JvmName("ProcessAllImagesInFolderUtility")

package com.drew.tools

import com.adobe.internal.xmp.XMPException
import com.adobe.internal.xmp.options.IteratorOptions
import com.adobe.internal.xmp.properties.XMPPropertyInfo
import com.drew.imaging.FileTypeDetector
import com.drew.imaging.readMetadata
import com.drew.lang.urlEncode
import com.drew.metadata.Directory
import com.drew.metadata.Metadata
import com.drew.metadata.exif.ExifIFD0Directory
import com.drew.metadata.exif.ExifSubIFDDirectory
import com.drew.metadata.exif.ExifThumbnailDirectory
import com.drew.metadata.file.FileSystemDirectory
import com.drew.metadata.xmp.XmpDirectory
import java.io.*
import java.util.*
import kotlin.system.exitProcess

/**
 * @author Drew Noakes https://drewnoakes.com
 */
@Throws(IOException::class)
fun main(args: Array<String>) {
  val directories: MutableList<String> = ArrayList()
  var handler: FileHandler? = null
  var log = System.out
  var i = 0
  while (i < args.size) {
    val arg = args[i]
    when {
      arg.equals("--text", ignoreCase = true) -> { // If "--text" is specified, write the discovered metadata into a sub-folder relative to the image
        handler = TextFileOutputHandler()
      }
      arg.equals("--markdown", ignoreCase = true) -> { // If "--markdown" is specified, write a summary table in markdown format to standard out
        handler = MarkdownTableOutputHandler()
      }
      arg.equals("--unknown", ignoreCase = true) -> { // If "--unknown" is specified, write CSV tallying unknown tag counts
        handler = UnknownTagHandler()
      }
      arg.equals("--log-file", ignoreCase = true) -> {
        if (i == args.size - 1) {
          printUsage()
          exitProcess(1)
        }
        log = PrintStream(FileOutputStream(args[++i], false), true)
      }
      else -> { // Treat this argument as a directory
        directories.add(arg)
      }
    }
    i++
  }
  if (directories.isEmpty()) {
    System.err.println("Expects one or more directories as arguments.")
    printUsage()
    exitProcess(1)
  }
  if (handler == null) {
    handler = BasicFileHandler()
  }
  val start = System.nanoTime()
  for (directory in directories) {
    processDirectory(File(directory), handler, "", log)
  }
  handler.onScanCompleted(log)
  println(String.format("Completed in %d ms", (System.nanoTime() - start) / 1000000))
  if (log !== System.out) {
    log.close()
  }
}

private fun printUsage() {
  println("Usage:")
  println()
  println("  java com.drew.tools.ProcessAllImagesInFolderUtility [--text|--markdown|--unknown] [--log-file <file-name>]")
}

private fun processDirectory(path: File, handler: FileHandler, relativePath: String, log: PrintStream) {
  handler.onStartingDirectory(path)
  val pathItems = path.list() ?: return
  // Order alphabetically so that output is stable across invocations
  Arrays.sort(pathItems)
  for (pathItem in pathItems) {
    val file = File(path, pathItem)
    if (file.isDirectory) {
      processDirectory(file, handler, if (relativePath.isEmpty()) pathItem else "$relativePath/$pathItem", log)
    } else if (handler.shouldProcess(file)) {
      handler.onBeforeExtraction(file, log, relativePath)
      // Read metadata
      val metadata: Metadata
      metadata = try {
        readMetadata(file)
      } catch (t: Throwable) {
        handler.onExtractionError(file, t, log)
        continue
      }
      handler.onExtractionSuccess(file, metadata, relativePath, log)
    }
  }
}

internal interface FileHandler {
  /** Called when the scan is about to start processing files in directory `path`.  */
  fun onStartingDirectory(directoryPath: File)

  /** Called to determine whether the implementation should process `filePath`.  */
  fun shouldProcess(file: File): Boolean

  /** Called before extraction is performed on `filePath`.  */
  fun onBeforeExtraction(file: File, log: PrintStream, relativePath: String)

  /** Called when extraction on `filePath` completed without an exception.  */
  fun onExtractionSuccess(file: File, metadata: Metadata, relativePath: String, log: PrintStream)

  /** Called when extraction on `filePath` resulted in an exception.  */
  fun onExtractionError(file: File, throwable: Throwable, log: PrintStream)

  /** Called when all files have been processed.  */
  fun onScanCompleted(log: PrintStream)
}

internal abstract class FileHandlerBase : FileHandler {
  // TODO obtain these from FileType enum directly
  private val _supportedExtensions =
    setOf(
      "jpg", "jpeg", "png", "gif", "bmp", "heic", "heif", "ico", "webp", "pcx", "ai", "eps",
      "nef", "crw", "cr2", "orf", "arw", "raf", "srw", "x3f", "rw2", "rwl", "dcr",
      "tif", "tiff", "psd", "dng",
      "mp3",
      "j2c", "jp2", "jpf", "jpm", "mj2",
      "3g2", "3gp", "m4v", "mov", "mp4", "m2v", "mts",
      "pbm", "pnm", "pgm", "ppm")
  private var _processedFileCount = 0
  private var _exceptionCount = 0
  private var _errorCount = 0
  private var _processedByteCount: Long = 0
  override fun onStartingDirectory(directoryPath: File) {}
  override fun shouldProcess(file: File): Boolean {
    val extension = getExtension(file)
    return extension != null && _supportedExtensions.contains(extension.toLowerCase())
  }

  override fun onBeforeExtraction(file: File, log: PrintStream, relativePath: String) {
    _processedFileCount++
    _processedByteCount += file.length()
  }

  override fun onExtractionError(file: File, throwable: Throwable, log: PrintStream) {
    _exceptionCount++
    log.print("\t[${throwable.javaClass.name}] ${throwable.message}\n")
  }

  override fun onExtractionSuccess(file: File, metadata: Metadata, relativePath: String, log: PrintStream) {
    if (metadata.hasErrors()) {
      log.print(file)
      log.print('\n')
      for (directory in metadata.directories) {
        if (!directory.hasErrors()) continue
        for (error in directory.errors) {
          log.print("\t[$directory.name]$error\n")
          _errorCount++
        }
      }
    }
  }

  override fun onScanCompleted(log: PrintStream) {
    if (_processedFileCount > 0) {
      log.print(String.format(
        "Processed %,d files (%,d bytes) with %,d exceptions and %,d file errors\n",
        _processedFileCount, _processedByteCount, _exceptionCount, _errorCount
      ))
    }
  }

  protected fun getExtension(file: File): String? {
    val fileName = file.name
    val i = fileName.lastIndexOf('.')
    if (i == -1) return null
    return if (i == fileName.length - 1) null else fileName.substring(i + 1)
  }
}

/**
 * Writes a text file containing the extracted metadata for each input file.
 */
internal class TextFileOutputHandler : FileHandlerBase() {
  override fun onStartingDirectory(directoryPath: File) {
    super.onStartingDirectory(directoryPath)
    // Delete any existing 'metadata' folder
    val metadataDirectory = File("$directoryPath/metadata/java")
    if (metadataDirectory.exists()) deleteRecursively(metadataDirectory)
  }

  override fun onBeforeExtraction(file: File, log: PrintStream, relativePath: String) {
    super.onBeforeExtraction(file, log, relativePath)
    log.print(file.absoluteFile)
    log.print(NEW_LINE)
  }

  override fun onExtractionSuccess(file: File, metadata: Metadata, relativePath: String, log: PrintStream) {
    super.onExtractionSuccess(file, metadata, relativePath, log)
    try {
      var writer: PrintWriter? = null
      try {
        writer = openWriter(file)
        // Write any errors
        if (metadata.hasErrors()) {
          for (directory in metadata.directories) {
            if (!directory.hasErrors()) continue
            for (error in directory.errors) writer.write("[ERROR: ${directory.name}] $error$NEW_LINE")
          }
          writer.write(NEW_LINE)
        }
        // Write tag values for each directory
        for (directory in metadata.directories) {
          val directoryName = directory.name
          // Write the directory's tags
          for (tag in directory.tags) {
            val tagName = tag.tagName
            var description: String?
            description = try {
              tag.description
            } catch (ex: Exception) {
              "ERROR: " + ex.message
            }
            if (description == null) description = ""
            // Skip the file write-time as this changes based on the time at which the regression test image repository was cloned
            if (directory is FileSystemDirectory && tag.tagType == FileSystemDirectory.TAG_FILE_MODIFIED_DATE) description = "<omitted for regression testing as checkout dependent>"
            writer.write("[$directoryName - ${tag.tagTypeHex}] $tagName = $description$NEW_LINE")
          }
          if (directory.tagCount != 0) writer.write(NEW_LINE)
          // Special handling for XMP directory data
          if (directory is XmpDirectory) {
            var wrote = false
            val xmpMeta = directory.xmpMeta
            try {
              val options = IteratorOptions().setJustLeafnodes(true)
              val iterator = xmpMeta.iterator(options)
              while (iterator.hasNext()) {
                val prop = iterator.next() as XMPPropertyInfo
                var ns = prop.namespace
                val path = prop.path
                var value = prop.value
                if (path == null) continue
                if (ns == null) ns = ""
                val MAX_XMP_VALUE_LENGTH = 512
                if (value == null) value = "" else if (value.length > MAX_XMP_VALUE_LENGTH) value = "${value.substring(0, MAX_XMP_VALUE_LENGTH)} <truncated from ${value.length} characters>"
                writer.write("[XMPMeta - $ns] $path = $value$NEW_LINE")
                wrote = true
              }
            } catch (e: XMPException) {
              e.printStackTrace()
            }
            if (wrote) writer.write(NEW_LINE)
          }
        }
        // Write file structure
        writeHierarchyLevel(metadata, writer, null, 0)
        writer.write(NEW_LINE)
      } finally {
        closeWriter(writer)
      }
    } catch (e: IOException) {
      e.printStackTrace()
    }
  }

  override fun onExtractionError(file: File, throwable: Throwable, log: PrintStream) {
    super.onExtractionError(file, throwable, log)
    try {
      var writer: PrintWriter? = null
      try {
        writer = openWriter(file)
        writer.write("EXCEPTION: ${throwable.message}$NEW_LINE")
        writer.write(NEW_LINE)
      } finally {
        closeWriter(writer)
      }
    } catch (e: IOException) {
      log.print("IO exception writing metadata file: ${e.message}$NEW_LINE")
    }
  }

  companion object {
    /** Standardise line ending so that generated files can be more easily diffed.  */
    private const val NEW_LINE = "\n"

    private fun deleteRecursively(directory: File) {
      require(directory.isDirectory) { "Must be a directory." }
      if (directory.exists()) {
        val list = directory.list()
        if (list != null) {
          for (item in list) {
            val file = File(item)
            if (file.isDirectory) deleteRecursively(file) else file.delete()
          }
        }
      }
      directory.delete()
    }

    private fun writeHierarchyLevel(metadata: Metadata, writer: PrintWriter, parent: Directory?, level: Int) {
      val indent = 4
      for (child in metadata.directories) {
        if (parent == null) {
          if (child.parent != null) continue
        } else if (parent != child.parent) {
          continue
        }
        for (i in 0 until level * indent) {
          writer.write(' '.toInt())
        }
        writer.write("- ")
        writer.write(child.name)
        writer.write(NEW_LINE)
        writeHierarchyLevel(metadata, writer, child, level + 1)
      }
    }

    @Throws(IOException::class)
    private fun openWriter(file: File): PrintWriter { // Create the output directory if it doesn't exist
      val metadataDir = File("${file.parent}/metadata")
      if (!metadataDir.exists()) metadataDir.mkdir()
      val javaDir = File("${file.parent}/metadata/java")
      if (!javaDir.exists()) javaDir.mkdir()
      val outputPath = "${file.parent}/metadata/java/${file.name}.txt"
      val writer: Writer = OutputStreamWriter(
        FileOutputStream(outputPath),
        "UTF-8"
      )
      writer.write("FILE: " + file.name + NEW_LINE)
      // Detect file type
      var stream: BufferedInputStream? = null
      try {
        stream = BufferedInputStream(FileInputStream(file))
        val fileType = FileTypeDetector.detectFileType(stream)
        writer.write("TYPE: ${fileType.toString().toUpperCase()}$NEW_LINE")
        writer.write(NEW_LINE)
      } finally {
        stream?.close()
      }
      return PrintWriter(writer)
    }

    @Throws(IOException::class)
    private fun closeWriter(writer: Writer?) {
      if (writer != null) {
        writer.write("Generated using metadata-extractor$NEW_LINE")
        writer.write("https://drewnoakes.com/code/exif/$NEW_LINE")
        writer.flush()
        writer.close()
      }
    }
  }
}

/**
 * Creates a table describing sample images using Wiki markdown.
 */
internal class MarkdownTableOutputHandler : FileHandlerBase() {
  private val _extensionEquivalence: MutableMap<String, String> = HashMap()
  private val _rowListByExtension: MutableMap<String, MutableList<Row>> = HashMap()

  internal class Row(val file: File, val metadata: Metadata, val relativePath: String) {
    var manufacturer: String? = null
    var model: String? = null
    var exifVersion: String? = null
    var thumbnail: String? = null
    var makernote: String? = null

    init {
      val ifd0Dir = metadata.getFirstDirectoryOfType(ExifIFD0Directory::class.java)
      val subIfdDir = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory::class.java)
      val thumbDir = metadata.getFirstDirectoryOfType(ExifThumbnailDirectory::class.java)
      if (ifd0Dir != null) {
        manufacturer = ifd0Dir.getDescription(ExifIFD0Directory.TAG_MAKE)
        model = ifd0Dir.getDescription(ExifIFD0Directory.TAG_MODEL)
      }
      var hasMakernoteData = false
      if (subIfdDir != null) {
        exifVersion = subIfdDir.getDescription(ExifSubIFDDirectory.TAG_EXIF_VERSION)
        hasMakernoteData = subIfdDir.containsTag(ExifSubIFDDirectory.TAG_MAKERNOTE)
      }
      if (thumbDir != null) {
        val width = thumbDir.getInteger(ExifThumbnailDirectory.TAG_IMAGE_WIDTH)
        val height = thumbDir.getInteger(ExifThumbnailDirectory.TAG_IMAGE_HEIGHT)
        thumbnail = if (width != null && height != null) "Yes ($width x $height)" else "Yes"
      }
      for (directory in metadata.directories) {
        if (directory.javaClass.name.contains("Makernote")) {
          makernote = directory.name.replace("Makernote", "").trim { it <= ' ' }
          break
        }
      }
      if (makernote == null) {
        makernote = if (hasMakernoteData) "(Unknown)" else "N/A"
      }
    }
  }

  override fun onExtractionSuccess(file: File, metadata: Metadata, relativePath: String, log: PrintStream) {
    super.onExtractionSuccess(file, metadata, relativePath, log)
    var extension = getExtension(file) ?: return
    // Sanitise the extension
    extension = extension.toLowerCase()
    extension = _extensionEquivalence.getOrDefault(extension, extension)
    val list = _rowListByExtension.getOrPut(extension, { ArrayList() })
    list.add(Row(file, metadata, relativePath))
  }

  override fun onScanCompleted(log: PrintStream) {
    super.onScanCompleted(log)
    var outputStream: OutputStream? = null
    var stream: PrintStream? = null
    try {
      outputStream = FileOutputStream("../wiki/ImageDatabaseSummary.md", false)
      stream = PrintStream(outputStream, false)
      writeOutput(stream)
      stream.flush()
    } catch (e: IOException) {
      e.printStackTrace()
    } finally {
      stream?.close()
      if (outputStream != null) try {
        outputStream.close()
      } catch (e: IOException) {
        e.printStackTrace()
      }
    }
  }

  @Throws(IOException::class)
  private fun writeOutput(stream: PrintStream) {
    val writer: Writer = OutputStreamWriter(stream)
    writer.write("# Image Database Summary\n\n")
    for ((extension, rows) in _rowListByExtension) {
      writer.write("## " + extension.toUpperCase() + " Files\n\n")
      writer.write("File|Manufacturer|Model|Dir Count|Exif?|Makernote|Thumbnail|All Data\n")
      writer.write("----|------------|-----|---------|-----|---------|---------|--------\n")
      // Order by manufacturer, then model
      rows.sortWith(Comparator { o1, o2 ->
        val c1 = compareValues(o1.manufacturer, o2.manufacturer)
        if (c1 != 0) c1 else compareValues(o1.model, o2.model)
      })
      val baseUrl = "https://raw.githubusercontent.com/drewnoakes/metadata-extractor-images/master"
      for (row in rows) {
        row.apply {
          writer.write("" +
            arrayOf("[${file.name}](${baseUrl}/$relativePath/${urlEncode(file.name)})",
              manufacturer ?: "",
              model ?: "",
              metadata.directoryCount,
              exifVersion ?: "",
              makernote ?: "",
              thumbnail ?: "",
              "[metadata]($baseUrl/$relativePath/metadata/${urlEncode(file.name).toLowerCase()}.txt)\n"
            ).joinToString("|"))
        }
      }
      writer.write('\n'.toInt())
    }
    writer.flush()
  }

  init {
    _extensionEquivalence["jpeg"] = "jpg"
  }
}

/**
 * Keeps track of unknown tags.
 */
internal class UnknownTagHandler : FileHandlerBase() {
  private val _occurrenceCountByTagByDirectory = HashMap<String, HashMap<Int, Int>>()
  override fun onExtractionSuccess(file: File, metadata: Metadata, relativePath: String, log: PrintStream) {
    super.onExtractionSuccess(file, metadata, relativePath, log)
    for (directory in metadata.directories) {
      for (tag in directory.tags) { // Only interested in unknown tags (those without names)
        if (tag.hasTagName()) continue
        var occurrenceCountByTag = _occurrenceCountByTagByDirectory[directory.name]
        if (occurrenceCountByTag == null) {
          occurrenceCountByTag = HashMap()
          _occurrenceCountByTagByDirectory[directory.name] = occurrenceCountByTag
        }
        var count = occurrenceCountByTag[tag.tagType]
        if (count == null) {
          count = 0
          occurrenceCountByTag[tag.tagType] = 0
        }
        occurrenceCountByTag[tag.tagType] = count + 1
      }
    }
  }

  override fun onScanCompleted(log: PrintStream) {
    super.onScanCompleted(log)
    for ((directoryName, value) in _occurrenceCountByTagByDirectory) {
      val counts: List<Map.Entry<Int, Int>> = ArrayList<Map.Entry<Int, Int>>(value.entries)
      Collections.sort(counts) { o1, o2 -> o2.value.compareTo(o1.value) }
      for ((tagType, count) in counts) {
        log.format("%s, 0x%04X, %d\n", directoryName, tagType, count)
      }
    }
  }
}

/**
 * Does nothing with the output except enumerate it in memory and format descriptions. This is useful in order to
 * flush out any potential exceptions raised during the formatting of extracted value descriptions.
 */
internal class BasicFileHandler : FileHandlerBase() {
  override fun onExtractionSuccess(file: File, metadata: Metadata, relativePath: String, log: PrintStream) {
    super.onExtractionSuccess(file, metadata, relativePath, log)
    // Iterate through all values, calling toString to flush out any formatting exceptions
    for (directory in metadata.directories) {
      directory.name
      for (tag in directory.tags) {
        tag.tagName
        tag.description
      }
    }
  }
}
