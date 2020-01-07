package com.drew.metadata.eps

import com.drew.imaging.tiff.TiffProcessingException
import com.drew.imaging.tiff.TiffReader
import com.drew.lang.*
import com.drew.metadata.Metadata
import com.drew.metadata.icc.IccReader
import com.drew.metadata.photoshop.PhotoshopReader
import com.drew.metadata.photoshop.PhotoshopTiffHandler
import com.drew.metadata.xmp.XmpReader
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream

/**
 * Reads file passed in through SequentialReader and parses encountered data:
 *
 *  * Basic EPS Comments
 *  * EXIF
 *  * Photoshop
 *  * IPTC
 *  * ICC Profile
 *  * XMP
 *
 * EPS comments are retrieved from EPS directory.  Photoshop, ICC Profile, and XMP processing
 * is passed to their respective reader.
 *
 *
 * EPS Constraints (Source: https://www-cdf.fnal.gov/offline/PostScript/5001.PDF pg.18):
 *
 *  * Max line length is 255 characters
 *  * Lines end with a CR(0xD) or LF(0xA) character (or both, in practice)
 *  * ':' separates keywords (considered part of the keyword)
 *  * Whitespace is either a space(0x20) or tab(0x9)
 *  * If there is more than one header, the 1st is truth
 *
 *
 * @author Payton Garland
 */
class EpsReader {
  private var _previousTag = 0
  /**
   * Filter method that determines if file will contain an EPS Header.  If it does, it will read the necessary
   * data and then set the position to the beginning of the PostScript data.  If it does not, the position will not
   * be changed.  After both scenarios, the main extract method is called.
   *
   * @param inputStream InputStream containing file
   * @param metadata Metadata to add directory to and extracted data
   */
  @Throws(IOException::class)
  fun extract(inputStream: InputStream, metadata: Metadata) {
    val reader = RandomAccessStreamReader(inputStream)
    val directory = EpsDirectory()
    metadata.addDirectory(directory)
    when (reader.getInt32(0)) {
      -0x3a2f2c3a -> {
        reader.isMotorolaByteOrder = false
        val postScriptOffset = reader.getInt32(4)
        val postScriptLength = reader.getInt32(8)
        val wmfOffset = reader.getInt32(12)
        val wmfSize = reader.getInt32(16)
        val tifOffset = reader.getInt32(20)
        val tifSize = reader.getInt32(24)
        //int checkSum = reader.getInt32(28);
        // Get Tiff/WMF preview data if applicable
        if (tifSize != 0) {
          directory.setInt(EpsDirectory.TAG_TIFF_PREVIEW_SIZE, tifSize)
          directory.setInt(EpsDirectory.TAG_TIFF_PREVIEW_OFFSET, tifOffset)
          // Get Tiff metadata
          try {
            val byteArrayReader = ByteArrayReader(reader.getBytes(tifOffset, tifSize))
            TiffReader().processTiff(byteArrayReader, PhotoshopTiffHandler(metadata, null), 0)
          } catch (ex: TiffProcessingException) {
            directory.addError("Unable to process TIFF data: " + ex.message)
          }
        } else if (wmfSize != 0) {
          directory.setInt(EpsDirectory.TAG_WMF_PREVIEW_SIZE, wmfSize)
          directory.setInt(EpsDirectory.TAG_WMF_PREVIEW_OFFSET, wmfOffset)
        }
        // TODO avoid allocating byte array here -- read directly from InputStream
        extract(directory, metadata, SequentialByteArrayReader(reader.getBytes(postScriptOffset, postScriptLength)))
      }
      0x25215053 -> {
        inputStream.reset()
        extract(directory, metadata, StreamReader(inputStream))
      }
      else -> directory.addError("File type not supported.")
    }
  }

  /**
   * Main method that parses all comments and then distributes data extraction among other methods that parse the
   * rest of file and store encountered data in metadata (if there exists an entry in EpsDirectory
   * for the found data).  Reads until a begin data/binary comment is found or _reader's estimated
   * available data has run out (or AI09 End Private Data).  Will extract data from normal EPS comments, Photoshop, ICC, and XMP.
   *
   * @param metadata Metadata to add directory to and extracted data
   */
  @Throws(IOException::class)
  private fun extract(directory: EpsDirectory, metadata: Metadata, reader: SequentialReader) {
    val line = StringBuilder()
    while (true) {
      line.setLength(0)
      // Read the next line, excluding any trailing newline character
      // Note that for Windows-style line endings ("\r\n") the outer loop will be run a second time with an empty
      // string, which is fine.
      while (true) {
        val c = reader.getByte().toChar()
        if (c == '\r' || c == '\n') break
        line.append(c)
      }
      // Stop when we hit a line that is not a comment
      if (line.isNotEmpty() && line[0] != '%') break
      var name: String
      // ':' signifies there is an associated keyword (should be put in directory)
      // otherwise, the name could be a marker
      val colonIndex = line.indexOf(":")
      if (colonIndex != -1) {
        name = line.substring(0, colonIndex).trim { it <= ' ' }
        val value = line.substring(colonIndex + 1).trim { it <= ' ' }
        addToDirectory(directory, name, value)
      } else {
        name = line.toString().trim { it <= ' ' }
      }
      // Some comments will both have a value and signify a new block to follow
      when (name) {
        "%BeginPhotoshop" -> {
          extractPhotoshopData(metadata, reader)
        }
        "%%BeginICCProfile" -> {
          extractIccData(metadata, reader)
        }
        "%begin_xml_packet" -> {
          extractXmpData(metadata, reader)
        }
      }
    }
  }

  /**
   * Default case that adds comment with keyword to directory
   *
   * @param directory EpsDirectory to add extracted data to
   * @param name String that holds name of current comment
   * @param value String that holds value of current comment
   */
  @Throws(IOException::class)
  private fun addToDirectory(directory: EpsDirectory, name: String, value: String) {
    val tag: Int = EpsDirectory._tagIntegerMap[name] ?: return
    when (tag) {
      EpsDirectory.TAG_IMAGE_DATA -> extractImageData(directory, value)
      EpsDirectory.TAG_CONTINUE_LINE -> directory.setString(_previousTag, directory.getString(_previousTag) + " " + value)
      else -> _previousTag = if (EpsDirectory._tagNameMap.containsKey(tag) && !directory.containsTag(tag)) {
        directory.setString(tag, value)
        tag
      } else { // Set previous tag to an Integer that doesn't exist in EpsDirectory
        0
      }
    }
    _previousTag = tag
  }

  companion object {
    /**
     * Parses `%ImageData` comment which holds several values including width in px,
     * height in px and color type.
     */
    @Throws(IOException::class)
    private fun extractImageData(directory: EpsDirectory, imageData: String) { // %ImageData: 1000 1000 8 3 1 1000 7 "beginimage"
      directory.setString(EpsDirectory.TAG_IMAGE_DATA, imageData.trim { it <= ' ' })
      val imageDataParts = imageData.split(" ").toTypedArray()
      val width = imageDataParts[0].toInt()
      val height = imageDataParts[1].toInt()
      val colorType = imageDataParts[3].toInt()
      // Only add values that are not already present
      if (!directory.containsTag(EpsDirectory.TAG_IMAGE_WIDTH)) directory.setInt(EpsDirectory.TAG_IMAGE_WIDTH, width)
      if (!directory.containsTag(EpsDirectory.TAG_IMAGE_HEIGHT)) directory.setInt(EpsDirectory.TAG_IMAGE_HEIGHT, height)
      if (!directory.containsTag(EpsDirectory.TAG_COLOR_TYPE)) directory.setInt(EpsDirectory.TAG_COLOR_TYPE, colorType)
      if (!directory.containsTag(EpsDirectory.TAG_RAM_SIZE)) {
        var bytesPerPixel = 0
        if (colorType == 1) bytesPerPixel = 1 // grayscale
        else if (colorType == 2 || colorType == 3) bytesPerPixel = 3 // Lab or RGB
        else if (colorType == 4) bytesPerPixel = 3 // CMYK
        if (bytesPerPixel != 0) directory.setInt(EpsDirectory.TAG_RAM_SIZE, bytesPerPixel * width * height)
      }
    }

    /**
     * Decodes a commented hex section, and uses [PhotoshopReader] to decode the resulting data.
     */
    @Throws(IOException::class)
    private fun extractPhotoshopData(metadata: Metadata, reader: SequentialReader) {
      val buffer = decodeHexCommentBlock(reader)
      if (buffer != null) PhotoshopReader().extract(SequentialByteArrayReader(buffer), buffer.size, metadata)
    }

    /**
     * Decodes a commented hex section, and uses [IccReader] to decode the resulting data.
     */
    @Throws(IOException::class)
    private fun extractIccData(metadata: Metadata, reader: SequentialReader) {
      val buffer = decodeHexCommentBlock(reader)
      if (buffer != null) IccReader().extract(ByteArrayReader(buffer), metadata)
    }

    /**
     * Extracts an XMP xpacket, and uses [XmpReader] to decode the resulting data.
     */
    @Throws(IOException::class)
    private fun extractXmpData(metadata: Metadata, reader: SequentialReader) {
      val bytes = readUntil(reader, "<?xpacket end=\"w\"?>".toByteArray())
      val xmp = String(bytes, UTF_8)
      XmpReader().extract(xmp, metadata)
    }

    /**
     * Reads all bytes until the given sentinel is observed.
     * The sentinel will be included in the returned bytes.
     */
    @Throws(IOException::class)
    private fun readUntil(reader: SequentialReader, sentinel: ByteArray): ByteArray {
      val bytes = ByteArrayOutputStream()
      val length = sentinel.size
      var depth = 0
      while (depth != length) {
        val b = reader.getByte()
        if (b == sentinel[depth]) depth++ else depth = 0
        bytes.write(b.toInt())
      }
      return bytes.toByteArray()
    }

    /**
     * EPS files can contain hexadecimal-encoded ASCII blocks, each prefixed with <c>"% "</c>.
     * This method reads such a block and returns a byte[] of the decoded contents.
     * Reading stops at the first invalid line, which is discarded (it's a terminator anyway).
     *
     *
     * For example:
     * <pre>`
     * %BeginPhotoshop: 9564
     * % 3842494D040400000000005D1C015A00031B25471C0200000200041C02780004
     * % 6E756C6C1C027A00046E756C6C1C025000046E756C6C1C023700083230313630
     * % 3331311C023C000B3131343335362B303030301C023E00083230313630333131
     * % 48000000010000003842494D03FD0000000000080101000000000000
     * %EndPhotoshop
    `</pre> *
     * When calling this method, the reader must be positioned at the start of the first line containing
     * hex data, not at the introductory line.
     *
     * @return The decoded bytes, or `null` if decoding failed.
     */
    @Throws(IOException::class)
    private fun decodeHexCommentBlock(reader: SequentialReader): ByteArray? {
      val bytes = ByteArrayOutputStream()
      // Use a state machine to efficiently parse data in a single traversal
      val AwaitingPercent = 0
      val AwaitingSpace = 1
      val AwaitingHex1 = 2
      val AwaitingHex2 = 3
      var state = AwaitingPercent
      var carry = 0
      var done = false
      var b: Byte = 0
      while (!done) {
        b = reader.getByte()
        when (state) {
          AwaitingPercent -> {
            when (b) {
              '\r'.toByte(), '\n'.toByte(), ' '.toByte() -> {
              }
              '%'.toByte() -> state = AwaitingSpace
              else -> return null
            }
          }
          AwaitingSpace -> {
            when (b) {
              ' '.toByte() -> state = AwaitingHex1
              else -> done = true
            }
          }
          AwaitingHex1 -> {
            val i = tryHexToInt(b)
            if (i != -1) {
              carry = i * 16
              state = AwaitingHex2
            } else if (b == '\r'.toByte() || b == '\n'.toByte()) {
              state = AwaitingPercent
            } else {
              return null
            }
          }
          AwaitingHex2 -> {
            val i = tryHexToInt(b)
            if (i == -1) return null
            bytes.write(carry + i)
            state = AwaitingHex1
          }
        }
      }
      // skip through the remainder of the last line
      while (b != '\n'.toByte()) b = reader.getByte()
      return bytes.toByteArray()
    }

    /**
     * Treats a byte as an ASCII character, and returns it's numerical value in hexadecimal.
     * If conversion is not possible, returns -1.
     */
    private fun tryHexToInt(b: Byte): Int {
      if (b >= '0'.toByte() && b <= '9'.toByte()) return b - '0'.toByte()
      if (b >= 'A'.toByte() && b <= 'F'.toByte()) return b - 'A'.toByte() + 10
      return if (b >= 'a'.toByte() && b <= 'f'.toByte()) b - 'a'.toByte() + 10 else -1
    }
  }
}
