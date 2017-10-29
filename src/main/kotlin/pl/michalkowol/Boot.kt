package pl.michalkowol

import com.softwareberg.XmlMapper
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
        staticFiles.location("/public")
        get("pdf", this::pdf)
        post("pdf", this::pdf)

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

    private fun pdf(request: Request, response: Response): ByteArray {
        response.type("application/pdf")
        val body = request.body()
        val stories = XmlJiraLoader(XmlMapper.create()).loadStories(body)
        val pdf = PdfConverter().convert(stories)
        return pdf
    }

}
