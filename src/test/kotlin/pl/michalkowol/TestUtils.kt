package pl.michalkowol

import com.google.common.io.Resources
import java.nio.charset.StandardCharsets.UTF_8

object TestUtils {

    fun readFile(filename: String): String {
        val resource = Resources.getResource(filename)
        return Resources.toString(resource, UTF_8)
    }

}

