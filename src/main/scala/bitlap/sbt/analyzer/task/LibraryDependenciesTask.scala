package bitlap.sbt.analyzer.task

import scala.collection.mutable

import bitlap.sbt.analyzer.Constants.*
import bitlap.sbt.analyzer.task.SbtShellOutputAnalysisTask.*

import com.intellij.openapi.project.Project

/** TODO: Process the `sbt libraryDependencies` command, get all user explicitly declared dependencies.
 *  @author
 *    梦境迷离
 *  @version 1.0,2023/8/19
 */
final class LibraryDependenciesTask extends SbtShellOutputAnalysisTask[Map[String, List[LibraryModuleID]]] {

  private def moduleIDArtifact(output: String): Option[LibraryModuleID] = {
    output.trim match
      case libraryDependenciesOutput4(group, artifact, revision, _, _) =>
        Some(LibraryModuleID(group.trim, artifact.trim, revision.trim))
      case libraryDependenciesOutput3(group, artifact, revision, conf, _, _) =>
        Some(LibraryModuleID(group.trim, artifact.trim, revision.trim))
      case libraryDependenciesOutput2(group, artifact, revision, conf) =>
        Some(LibraryModuleID(group.trim, artifact.trim, revision.trim))
      case libraryDependenciesOutput1(group, artifact, revision) =>
        Some(LibraryModuleID(group.trim, artifact.trim, revision.trim))
      case _ =>
        None
  }

  override def executeCommand(project: Project): Map[String, List[LibraryModuleID]] = {
    val outputLines             = getCommandOutputLines(project, "libraryDependencies")
    val moduleIdSbtLibrariesMap = mutable.HashMap[String, String]()
    // single module
    if (outputLines.startsWith("[info] *")) {
      val libraries = outputLines.map {
        case shellOutputStarResultRegex(_, _, _, _, artifact) =>
          moduleIDArtifact(artifact)
        case _ => None
      }.collect { case Some(value) =>
        value
      }
      return Map(SingleSbtModule -> libraries)

    }
    // multi-module
    if (outputLines.size % 2 == 0) {
      for (i <- 0 until outputLines.size - 1 by 2) {
        moduleIdSbtLibrariesMap.put(outputLines(i).trim, outputLines(i + 1).trim)
      }
    }

    moduleIdSbtLibrariesMap.map { (k, v) =>
      val key = k match
        case libraryDependenciesInputRegex(_, _, moduleName, _, _) => moduleName.trim
        case rootLibraryDependenciesInputRegex(_, _)               => RootSbtModule
        case _                                                     => Empty_String

      val value = v match
        case shellOutputResultRegex(_, _, output) =>
          output.trim match
            case libraryDependenciesOutputRegex(modules) =>
              modules.split(",").toList.map(_.trim).map(moduleIDArtifact).collect { case Some(value) =>
                value
              }
            case _ => List.empty
        case _ => List.empty

      key -> value
    }.collect {
      case (k, v) if k != Empty_String => k -> v
    }.toMap

  }

}