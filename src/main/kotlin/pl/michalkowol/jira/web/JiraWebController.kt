package pl.michalkowol.jira.web

import pl.michalkowol.jira.xml.JiraXmlLoader
import pl.michalkowol.pdfs.PdfConverter
import pl.michalkowol.web.Controller
import pl.michalkowol.web.errors.BadRequestException
import spark.Request
import spark.Response
import spark.Spark.get
import spark.Spark.post

class JiraWebController(
    private val jiraReader: JiraReader,
    private val jiraXmlLoader: JiraXmlLoader,
    private val pdfConverter: PdfConverter
) : Controller {

    override fun start() {
        get("web", this::pdf)
        post("web", this::pdf)
    }

    private fun pdf(request: Request, response: Response): ByteArray {
        response.type("application/pdf")
        val queryMap = request.queryMap()
        val username = queryMap["username"].value() ?: throw BadRequestException("username")
        val password = queryMap["password"].value() ?: throw BadRequestException("password")
        val jql = queryMap["jql"].value() ?: throw BadRequestException("jql")
        val input = jiraReader.readXml(username, password, jql)
        val stories = jiraXmlLoader.loadStories(input)
        return pdfConverter.convert(stories)
    }

}
