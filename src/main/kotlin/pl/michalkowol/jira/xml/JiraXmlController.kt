package pl.michalkowol.jira.xml

import pl.michalkowol.pdfs.PdfConverter
import pl.michalkowol.web.Controller
import spark.Request
import spark.Response
import spark.Spark.get
import spark.Spark.post

class JiraXmlController(
    private val jiraXmlLoader: JiraXmlLoader,
    private val pdfConverter: PdfConverter
) : Controller {

    override fun start() {
        get("xml", this::pdf)
        post("xml", this::pdf)
    }

    private fun pdf(request: Request, response: Response): ByteArray {
        response.type("application/pdf")
        val body = request.body()
        val stories = jiraXmlLoader.loadStories(body)
        return pdfConverter.convert(stories)
    }

}
