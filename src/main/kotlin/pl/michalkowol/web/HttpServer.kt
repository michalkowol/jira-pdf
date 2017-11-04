package pl.michalkowol.web

import pl.michalkowol.configurations.ServerConfiguration
import pl.michalkowol.jira.web.JiraWebController
import pl.michalkowol.jira.xml.JiraXmlController
import pl.michalkowol.web.errors.ErrorsController
import spark.Spark.port

class HttpServer(
    private val serverConfiguration: ServerConfiguration,
    private val errorsController: ErrorsController,
    private val staticFilesController: StaticFilesController,
    private val jiraXmlController: JiraXmlController,
    private val jiraWebController: JiraWebController
) {

    fun start() {
        port(serverConfiguration.port)
        errorsController.start()
        staticFilesController.start()
        jiraXmlController.start()
        jiraWebController.start()
    }

}
