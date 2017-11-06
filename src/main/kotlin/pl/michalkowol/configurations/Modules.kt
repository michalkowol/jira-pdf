package pl.michalkowol.configurations

import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.softwareberg.HttpClient
import com.softwareberg.JsonMapper
import com.softwareberg.SimpleHttpClient
import com.softwareberg.XmlMapper
import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import pl.michalkowol.jira.web.JiraReader
import pl.michalkowol.jira.web.JiraWebController
import pl.michalkowol.jira.xml.JiraXmlController
import pl.michalkowol.jira.xml.JiraXmlLoader
import pl.michalkowol.pdfs.PdfConverter
import pl.michalkowol.pdfs.fop.FopPdfConverter
import pl.michalkowol.web.HttpServer
import pl.michalkowol.web.StaticFilesController
import pl.michalkowol.web.errors.ErrorsController
import javax.inject.Singleton

class Modules : AbstractModule() {

    override fun configure() {}

    @Singleton
    @Provides
    fun provideXmlMapper(): XmlMapper {
        return XmlMapper.create()
    }

    @Singleton
    @Provides
    fun provideXmlJiraLoader(xmlMapper: XmlMapper): JiraXmlLoader {
        return JiraXmlLoader(xmlMapper)
    }

    @Singleton
    @Provides
    private fun provideJiraReader(httpClient: HttpClient): JiraReader {
        return JiraReader(httpClient)
    }

    @Singleton
    @Provides
    private fun provideConfig(): Config {
        return ConfigFactory.load()
    }

    @Singleton
    @Provides
    private fun provideServerConfiguration(config: Config): ServerConfiguration {
        val port = config.getInt("server.port")
        return ServerConfiguration(port)
    }

    @Singleton
    @Provides
    private fun provideHttpClient(): HttpClient {
        return SimpleHttpClient.create()
    }

    @Singleton
    @Provides
    private fun providePdfConverter(xmlMapper: XmlMapper): PdfConverter {
        return FopPdfConverter(xmlMapper)
    }

    @Singleton
    @Provides
    private fun provideJsonMapper(): JsonMapper {
        return JsonMapper.create()
    }

    @Singleton
    @Provides
    private fun provideErrorsController(jsonMapper: JsonMapper): ErrorsController {
        return ErrorsController(jsonMapper)
    }

    @Singleton
    @Provides
    private fun provideJiraWebController(
        jiraReader: JiraReader,
        jiraXmlLoader: JiraXmlLoader,
        pdfConverter: PdfConverter
    ): JiraWebController {
        return JiraWebController(jiraReader, jiraXmlLoader, pdfConverter)
    }

    @Singleton
    @Provides
    private fun provideJiraXmlController(
        jiraXmlLoader: JiraXmlLoader,
        pdfConverter: PdfConverter
    ): JiraXmlController {
        return JiraXmlController(jiraXmlLoader, pdfConverter)
    }


    @Singleton
    @Provides
    private fun provideHttpServer(
        serverConfiguration: ServerConfiguration,
        errorsController: ErrorsController,
        staticFilesController: StaticFilesController,
        jiraXmlController: JiraXmlController,
        jiraWebController: JiraWebController
    ): HttpServer {
        return HttpServer(
            serverConfiguration,
            errorsController,
            staticFilesController,
            jiraXmlController,
            jiraWebController
        )
    }

}
