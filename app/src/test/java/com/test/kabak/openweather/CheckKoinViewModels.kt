/**
 * Created by Eugeny Kabak on 15.05.2022
 */
package com.test.kabak.openweather

import org.junit.Assert
import org.junit.Test
import org.junit.experimental.categories.Category
import org.koin.test.category.CheckModuleTest
import java.io.File

@Category(CheckModuleTest::class)
class CheckKoinViewModels {
    @Test
    fun checkViewModels() {
        val projectRoot = File("").absoluteFile.parentFile!!

        val viewModels = projectRoot
            .walk()
            .excludeBuildFiles()
            .filter { it.extension == "kt" }
            .mapNotNull(::isViewModelFile)
            .toList()

        val moduleFiles = projectRoot
            .walk()
            .excludeBuildFiles()
            .filter { it.extension == "kt" && it.nameWithoutExtension.endsWith("Module") }
            .toList()

        val notAddedToDiViewModels =
            getNotAddedToDiViewModels(moduleFiles = moduleFiles, viewModels = viewModels)

        if (notAddedToDiViewModels.isNotEmpty()) {
            val message = notAddedToDiViewModels.joinToString(separator = ", ")
            Assert.fail("Please add this viewModels to DI: $message")
        }
    }

    private fun isViewModelFile(file: File): String? {
        val fileName = file.nameWithoutExtension

        return if (!exceptions.contains(fileName) && fileName.endsWith("ViewModel")) {
            fileName
        } else null
    }

    private fun getNotAddedToDiViewModels(
        viewModels: List<String>,
        moduleFiles: List<File>,
    ): List<String> {
        return viewModels
            .mapNotNull { viewModelName ->
                val added = moduleFiles
                    .map { fileContainsViewModelDiDeclaration(it, viewModelName) }
                    .all { it }

                if (added) return@mapNotNull null else viewModelName
            }
    }

    private fun fileContainsViewModelDiDeclaration(
        currentFile: File,
        viewModelName: String,
    ): Boolean {
        val fileText = currentFile.readText()

        val result = fileText.lines().any { line ->
            line.contains("::$viewModelName") && !line.startsWith("//")
        }

        return result
    }

    /**
     * Исключить файлы с директории "build"
     */
    private fun Sequence<File>.excludeBuildFiles(): Sequence<File> {
        return this.filter { !it.path.contains("build") }
    }

    private companion object {
        val exceptions = listOf(
            "BaseViewModel"
        )
    }
}