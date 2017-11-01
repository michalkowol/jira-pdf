package pl.michalkowol

import org.slf4j.LoggerFactory
import spark.Request
import spark.Response
import spark.Spark.exception
import spark.Spark.get
import spark.Spark.port
import spark.Spark.post
import spark.Spark.staticFiles
import java.io.PrintWriter
import java.io.StringWriter
import java.net.HttpURLConnection

class HttpServer(
    private val serverConfiguration: ServerConfiguration,
    private val xmlJiraLoader: XmlJiraLoader,
    private val pdfConverter: PdfConverter,
    private val jiraReader: JiraReader
) {

    private val log = LoggerFactory.getLogger(HttpServer::class.java)

    fun start() {
        port(serverConfiguration.port)

        staticFiles.location("/public")
        get("pdf", this::pdf)
        post("pdf", this::pdf)

        exception(Exception::class.java, { e, request, response ->
            log.error(request.url(), e)
            val errorMsgWriter = StringWriter()
            e.printStackTrace(PrintWriter(errorMsgWriter))
            val errorMsg = errorMsgWriter.toString()
            response.type("text/plain")
            response.status(HttpURLConnection.HTTP_INTERNAL_ERROR)
            response.body(errorMsg)
        })
    }

    private fun pdf(request: Request, response: Response): ByteArray {
        response.type("application/pdf")
        val body = request.body()
        val stories = xmlJiraLoader.loadStories(body)
        return pdfConverter.convert(stories)
    }

    private fun pdfFromJira(request: Request, response: Response): ByteArray {
        response.type("application/pdf")
        val username = "kowolm"
        val password = "password"
        val url = ""
        val stories = jiraReader.readJiraStories(username, password, url)
        return pdfConverter.convert(stories)
    }

}
