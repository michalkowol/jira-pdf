package pl.michalkowol.jira.web

import com.softwareberg.HttpClient
import com.softwareberg.HttpHeader
import com.softwareberg.HttpMethod.GET
import com.softwareberg.HttpRequest
import pl.michalkowol.web.errors.BadRequestException
import com.softwareberg.UsernameWithPassword as RincaUsernameWithPassword

class JiraReader(private val httpClient: HttpClient) {

    fun readXml(username: String, password: String, jql: String): String {
        val url = "https://jira.mtvi.com/sr/jira.issueviews:searchrequest-xml/temp/SearchRequest.xml?jqlQuery=$jql"
        val basicAuth = HttpHeader.basicAuth(username, password)
        val httpRequest = HttpRequest(GET, url, listOf(basicAuth))
        val body = httpClient.execute(httpRequest).join().body
        return body ?: throw BadRequestException("")
    }

}

