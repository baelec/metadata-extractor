@file:JvmName("EpsMetadataReader")

package com.drew.imaging.eps

import com.drew.metadata.Metadata
import com.drew.metadata.eps.EpsReader
import com.drew.metadata.file.FileSystemMetadataReader
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream

/**
 * Obtains metadata from EPS files.
 *
 * @author Payton Garland
 */
@Throws(IOException::class)
fun readMetadata(file: File): Metadata {
  val metadata = Metadata()
  val stream = FileInputStream(file)
  stream.use { stream ->
    EpsReader().extract(stream, metadata)
  }
  FileSystemMetadataReader().read(file, metadata)
  return metadata
}

@Throws(IOException::class)
fun readMetadata(inputStream: InputStream): Metadata {
  val metadata = Metadata()
  EpsReader().extract(inputStream, metadata)
  return metadata
}
