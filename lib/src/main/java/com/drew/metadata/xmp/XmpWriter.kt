package com.drew.metadata.xmp

import com.adobe.internal.xmp.XMPException
import com.adobe.internal.xmp.XMPMetaFactory
import com.adobe.internal.xmp.options.SerializeOptions
import com.drew.metadata.Metadata
import java.io.OutputStream

object XmpWriter {
  /**
   * Serializes the XmpDirectory component of `Metadata` into an `OutputStream`
   * @param os Destination for the xmp data
   * @param data populated metadata
   * @return serialize success
   */
  fun write(os: OutputStream?, data: Metadata): Boolean {
    val dir = data.getFirstDirectoryOfType(XmpDirectory::class.java) ?: return false
    val meta = dir.xmpMeta
    try {
      val so = SerializeOptions().setOmitPacketWrapper(true)
      XMPMetaFactory.serialize(meta, os, so)
    } catch (e: XMPException) {
      e.printStackTrace()
      return false
    }
    return true
  }
}
