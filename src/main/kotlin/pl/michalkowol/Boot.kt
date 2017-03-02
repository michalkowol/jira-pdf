package pl.michalkowol

import org.slf4j.LoggerFactory
import spark.Request
import spark.Response
import spark.Spark.*
import java.io.PrintWriter
import java.io.StringWriter
import java.net.HttpURLConnection.HTTP_INTERNAL_ERROR

fun main(args: Array<String>) {
    Boot().start()
}

class Boot {

    private val log = LoggerFactory.getLogger(Boot::class.java)

    fun start() {
        port(8080)

        get("/echo/:id", this::echo)

        exception(Exception::class.java, { e, request, response ->
            log.error(request.url(), e)
            val errorMsgWriter = StringWriter()
            e.printStackTrace(PrintWriter(errorMsgWriter))
            val errorMsg = errorMsgWriter.toString()
            response.type("text/plain")
            response.status(HTTP_INTERNAL_ERROR)
            response.body(errorMsg)
        })
    }

    private fun echo(request: Request, response: Response): String {
        response.type("application/json")
        val id = request.params("id")
        return """{"id": "$id"}"""
    }
}
