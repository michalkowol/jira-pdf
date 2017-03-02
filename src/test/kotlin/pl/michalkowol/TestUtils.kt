package pl.michalkowol

import java.io.FileNotFoundException

object TestUtils {
    fun readFile(filename: String): String {
        val inputStream = TestUtils::class.java.classLoader.getResourceAsStream(filename) ?: throw FileNotFoundException("$filename not found")
        return inputStream.bufferedReader().use { it.readText() }
    }
}

