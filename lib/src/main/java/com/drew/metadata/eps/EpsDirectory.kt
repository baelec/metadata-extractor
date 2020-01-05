package com.drew.metadata.eps

import com.drew.metadata.Directory
import java.util.*

/**
 * @author Payton Garland
 */
class EpsDirectory : Directory() {
  override val tagNameMap = Companion._tagNameMap
  companion object {
    /**
     * Sources: https://www-cdf.fnal.gov/offline/PostScript/5001.PDF
     * http://www.sno.phy.queensu.ca/~phil/exiftool/TagNames/PostScript.html
     */
    const val TAG_DSC_VERSION = 1
    const val TAG_AUTHOR = 2
    const val TAG_BOUNDING_BOX = 3
    const val TAG_COPYRIGHT = 4
    const val TAG_CREATION_DATE = 5
    const val TAG_CREATOR = 6
    const val TAG_FOR = 7
    const val TAG_IMAGE_DATA = 8
    const val TAG_KEYWORDS = 9
    const val TAG_MODIFY_DATE = 10
    const val TAG_PAGES = 11
    const val TAG_ROUTING = 12
    const val TAG_SUBJECT = 13
    const val TAG_TITLE = 14
    const val TAG_VERSION = 15
    const val TAG_DOCUMENT_DATA = 16
    const val TAG_EMULATION = 17
    const val TAG_EXTENSIONS = 18
    const val TAG_LANGUAGE_LEVEL = 19
    const val TAG_ORIENTATION = 20
    const val TAG_PAGE_ORDER = 21
    const val TAG_OPERATOR_INTERNVENTION = 22
    const val TAG_OPERATOR_MESSAGE = 23
    const val TAG_PROOF_MODE = 24
    const val TAG_REQUIREMENTS = 25
    const val TAG_VM_LOCATION = 26
    const val TAG_VM_USAGE = 27
    const val TAG_IMAGE_WIDTH = 28
    const val TAG_IMAGE_HEIGHT = 29
    const val TAG_COLOR_TYPE = 30
    const val TAG_RAM_SIZE = 31
    const val TAG_TIFF_PREVIEW_SIZE = 32
    const val TAG_TIFF_PREVIEW_OFFSET = 33
    const val TAG_WMF_PREVIEW_SIZE = 34
    const val TAG_WMF_PREVIEW_OFFSET = 35
    const val TAG_CONTINUE_LINE = 36
    // Section Markers
    //    public static final int TAG_BEGIN_ICC                                   = 37;
    //    public static final int TAG_BEGIN_PHOTOSHOP                             = 38;
    //    public static final int TAG_BEGIN_XML_PACKET                            = 39;
    //    public static final int TAG_BEGIN_BINARY                                = 40;
    //    public static final int TAG_BEGIN_DATA                                  = 41;
    //    public static final int TAG_AI9_END_PRIVATE_DATA                        = 42;
    val _tagNameMap = HashMap<Int, String>()
    val _tagIntegerMap = HashMap<String, Int>()

    init {
      _tagIntegerMap["%!PS-Adobe-"] = TAG_DSC_VERSION
      _tagIntegerMap["%%Author"] = TAG_AUTHOR
      _tagIntegerMap["%%BoundingBox"] = TAG_BOUNDING_BOX
      _tagIntegerMap["%%Copyright"] = TAG_COPYRIGHT
      _tagIntegerMap["%%CreationDate"] = TAG_CREATION_DATE
      _tagIntegerMap["%%Creator"] = TAG_CREATOR
      _tagIntegerMap["%%For"] = TAG_FOR
      _tagIntegerMap["%ImageData"] = TAG_IMAGE_DATA
      _tagIntegerMap["%%Keywords"] = TAG_KEYWORDS
      _tagIntegerMap["%%ModDate"] = TAG_MODIFY_DATE
      _tagIntegerMap["%%Pages"] = TAG_PAGES
      _tagIntegerMap["%%Routing"] = TAG_ROUTING
      _tagIntegerMap["%%Subject"] = TAG_SUBJECT
      _tagIntegerMap["%%Title"] = TAG_TITLE
      _tagIntegerMap["%%Version"] = TAG_VERSION
      _tagIntegerMap["%%DocumentData"] = TAG_DOCUMENT_DATA
      _tagIntegerMap["%%Emulation"] = TAG_EMULATION
      _tagIntegerMap["%%Extensions"] = TAG_EXTENSIONS
      _tagIntegerMap["%%LanguageLevel"] = TAG_LANGUAGE_LEVEL
      _tagIntegerMap["%%Orientation"] = TAG_ORIENTATION
      _tagIntegerMap["%%PageOrder"] = TAG_PAGE_ORDER
      _tagIntegerMap["%%OperatorIntervention"] = TAG_OPERATOR_INTERNVENTION
      _tagIntegerMap["%%OperatorMessage"] = TAG_OPERATOR_MESSAGE
      _tagIntegerMap["%%ProofMode"] = TAG_PROOF_MODE
      _tagIntegerMap["%%Requirements"] = TAG_REQUIREMENTS
      _tagIntegerMap["%%VMlocation"] = TAG_VM_LOCATION
      _tagIntegerMap["%%VMusage"] = TAG_VM_USAGE
      _tagIntegerMap["Image Width"] = TAG_IMAGE_WIDTH
      _tagIntegerMap["Image Height"] = TAG_IMAGE_HEIGHT
      _tagIntegerMap["Color Type"] = TAG_COLOR_TYPE
      _tagIntegerMap["Ram Size"] = TAG_RAM_SIZE
      _tagIntegerMap["TIFFPreview"] = TAG_TIFF_PREVIEW_SIZE
      _tagIntegerMap["TIFFPreviewOffset"] = TAG_TIFF_PREVIEW_OFFSET
      _tagIntegerMap["WMFPreview"] = TAG_WMF_PREVIEW_SIZE
      _tagIntegerMap["WMFPreviewOffset"] = TAG_WMF_PREVIEW_OFFSET
      _tagIntegerMap["%%+"] = TAG_CONTINUE_LINE
      _tagNameMap[TAG_CONTINUE_LINE] = "Line Continuation"
      _tagNameMap[TAG_BOUNDING_BOX] = "Bounding Box"
      _tagNameMap[TAG_COPYRIGHT] = "Copyright"
      _tagNameMap[TAG_DOCUMENT_DATA] = "Document Data"
      _tagNameMap[TAG_EMULATION] = "Emulation"
      _tagNameMap[TAG_EXTENSIONS] = "Extensions"
      _tagNameMap[TAG_LANGUAGE_LEVEL] = "Language Level"
      _tagNameMap[TAG_ORIENTATION] = "Orientation"
      _tagNameMap[TAG_PAGE_ORDER] = "Page Order"
      _tagNameMap[TAG_VERSION] = "Version"
      _tagNameMap[TAG_IMAGE_DATA] = "Image Data"
      _tagNameMap[TAG_IMAGE_WIDTH] = "Image Width"
      _tagNameMap[TAG_IMAGE_HEIGHT] = "Image Height"
      _tagNameMap[TAG_COLOR_TYPE] = "Color Type"
      _tagNameMap[TAG_RAM_SIZE] = "Ram Size"
      _tagNameMap[TAG_CREATOR] = "Creator"
      _tagNameMap[TAG_CREATION_DATE] = "Creation Date"
      _tagNameMap[TAG_FOR] = "For"
      _tagNameMap[TAG_REQUIREMENTS] = "Requirements"
      _tagNameMap[TAG_ROUTING] = "Routing"
      _tagNameMap[TAG_TITLE] = "Title"
      _tagNameMap[TAG_DSC_VERSION] = "DSC Version"
      _tagNameMap[TAG_PAGES] = "Pages"
      _tagNameMap[TAG_OPERATOR_INTERNVENTION] = "Operator Intervention"
      _tagNameMap[TAG_OPERATOR_MESSAGE] = "Operator Message"
      _tagNameMap[TAG_PROOF_MODE] = "Proof Mode"
      _tagNameMap[TAG_VM_LOCATION] = "VM Location"
      _tagNameMap[TAG_VM_USAGE] = "VM Usage"
      _tagNameMap[TAG_AUTHOR] = "Author"
      _tagNameMap[TAG_KEYWORDS] = "Keywords"
      _tagNameMap[TAG_MODIFY_DATE] = "Modify Date"
      _tagNameMap[TAG_SUBJECT] = "Subject"
      _tagNameMap[TAG_TIFF_PREVIEW_SIZE] = "TIFF Preview Size"
      _tagNameMap[TAG_TIFF_PREVIEW_OFFSET] = "TIFF Preview Offset"
      _tagNameMap[TAG_WMF_PREVIEW_SIZE] = "WMF Preview Size"
      _tagNameMap[TAG_WMF_PREVIEW_OFFSET] = "WMF Preview Offset"
    }
  }

  override val name: String
    get() = "EPS"

  init {
    setDescriptor(EpsDescriptor(this))
  }
}
