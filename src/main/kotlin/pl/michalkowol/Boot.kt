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
        val body = """<rss version="0.92">
    <channel>
        <item>
            <description>As a producer I want...</description>
            <key>WTAI-1052</key>
            <summary>Provider base configuration for ARC data</summary>
            <type>Story</type>
            <comments>
                <comment>comment A</comment>
                <comment>comment B</comment>
            </comments>
            <subtasks>
                <subtask>WTAI-553</subtask>
                <subtask>WTAI-551</subtask>
            </subtasks>
            <customfields>
                <customfield>
                    <customfieldname>Story Points</customfieldname>
                    <customfieldvalues>
                        <customfieldvalue>1.0</customfieldvalue>
                    </customfieldvalues>
                </customfield>
            </customfields>
        </item>
    </channel>
</rss>"""
        val stories = XmlJiraLoader(XmlMapper.create()).loadStories(body)
        val pdf = PdfConverter().convert(stories)
        return pdf
    }
}
