package com.lightbend.coursegentools

/**
  * Copyright © 2014, 2015, 2016 Lightbend, Inc. All rights reserved. [http://www.lightbend.com]
  */

object Studentify {

  def main(args: Array[String]): Unit = {

    import Helpers._
    import java.io.File

    val cmdOptions = StudentifyCmdLineOptParse.parse(args)
    if (cmdOptions.isEmpty) System.exit(-1)
    val StudentifyCmdOptions(masterRepo, targetFolder, multiJVM) = cmdOptions.get

    val projectName = masterRepo.getName
    val tmpDir = cleanMasterViaGit(masterRepo, projectName)
    val cleanMasterRepo = new File(tmpDir, projectName)
    val exercises: Seq[String] = getExerciseNames(cleanMasterRepo)
    val targetCourseFolder = new File(targetFolder, projectName)
    stageFirstExercise(exercises.head, cleanMasterRepo, targetCourseFolder)
    copyMaster(cleanMasterRepo, targetCourseFolder)
    val solutionPaths = hideExerciseSolutions(targetCourseFolder)
    createBookmarkFile(exercises, targetCourseFolder)
    createBuildFile(targetCourseFolder, multiJVM)
    cleanUp(List(".git", ".gitignore"), targetCourseFolder)
    sbt.IO.delete(tmpDir)

  }

}