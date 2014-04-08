package ccn.ccnlite

import ccnliteinterface.CCNLiteInterface
import ccn.packet._
import java.io.{FileOutputStream, File}

object CCNLite {
  val ccnIf = new CCNLiteInterface()

  def ccnbToXml(ccnbData: Array[Byte]): String = {
    ccnIf.ccnbToXml(ccnbData)
  }

  def mkBinaryContent(content: Content): Array[Byte] = {
    val name = content.name.toArray
    val data = content.data
    ccnIf.mkBinaryContent(name, data)
  }

  def mkBinaryContent(name: Array[String], data: Array[Byte]): Array[Byte] = {
    ccnIf.mkBinaryContent(name, data)
  }

  def mkBinaryInterest(interest: Interest): Array[Byte] = {
    ccnIf.mkBinaryInterest(interest.name.toArray)
  }

  def mkBinaryPacket(packet: CCNPacket): Array[Byte] = {
    packet match {
      case i: Interest => mkBinaryInterest(i)
      case c: Content => mkBinaryContent(c)
    }
  }

  def mkBinaryInterest(name: Array[String]): Array[Byte] = {
    ccnIf.mkBinaryInterest(name)
  }


  def mkAddToCacheInterest(content: Content): Array[Byte] = {
    val binaryContent = mkBinaryContent(content)

    val filename = s"./service-library/test${System.nanoTime}.ccnb"
    val file = new File(filename)

    // Just to be sure, if the file already exists, wait quickly and try again
    if (file.exists) {
      Thread.sleep(1)
      mkAddToCacheInterest(content)
    } else {
      file.createNewFile
      val out = new FileOutputStream(file)
      try {
        out.write(binaryContent)
      } finally {
        if (out != null) out.close
      }
      val absoluteFilename = file.getCanonicalPath
      val binaryInterest = ccnIf.mkAddToCacheInterest(absoluteFilename)
      file.delete
      binaryInterest
    }
  }

  private def mkAddToCacheInterest(ccnbAbsoluteFilename: String): Array[Byte] = {
    ccnIf.mkAddToCacheInterest(ccnbAbsoluteFilename)
  }
}
