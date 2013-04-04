package rpgboss.model

import rpgboss.lib._
import rpgboss.lib.FileHelper._

import org.json4s.native.Serialization

import scala.collection.JavaConversions._
import java.io._

case class Project(dir: File, data: ProjectData)
{
  def writeMetadata() : Boolean = 
    Project.filename(dir).useWriter { writer =>
      implicit val formats = org.json4s.DefaultFormats
      Serialization.writePretty(data, writer) != null
    } getOrElse false
  
  def rcDir   = dir
}

object Project {
  
  def startingProject(title: String, 
                      dir: File) =
    Project(dir, ProjectData(
        uuid = java.util.UUID.randomUUID().toString(), 
        title = title))
  
  def filename(dir: File) = new File(dir, "rpgproject.json")
  
  def readFromDisk(projDir: File) : Option[Project] =
    filename(projDir).readAsString.map { str =>
      implicit val formats = org.json4s.DefaultFormats
      val pd = Serialization.read[ProjectData](str)
      Project(projDir, pd)
    }
}

