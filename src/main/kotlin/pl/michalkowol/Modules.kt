package pl.michalkowol

import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.softwareberg.HttpClient
import com.softwareberg.SimpleHttpClient
import com.softwareberg.XmlMapper
import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
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
    fun provideXmlJiraLoader(xmlMapper: XmlMapper): XmlJiraLoader {
        return XmlJiraLoader(xmlMapper)
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
    private fun provideJiraReader(xmlJiraLoader: XmlJiraLoader, httpClient: HttpClient): JiraReader {
        return JiraReader(xmlJiraLoader, httpClient)
    }

    @Singleton
    @Provides
    private fun provideHttpServer(
        serverConfiguration: ServerConfiguration,
        xmlJiraLoader: XmlJiraLoader,
        pdfConverter: PdfConverter,
        jiraReader: JiraReader
    ): HttpServer {
        return HttpServer(serverConfiguration, xmlJiraLoader, pdfConverter, jiraReader)
    }

}
