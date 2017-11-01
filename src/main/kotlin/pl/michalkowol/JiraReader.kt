package pl.michalkowol

import com.softwareberg.HttpClient
import com.softwareberg.HttpHeader
import com.softwareberg.HttpMethod.GET
import com.softwareberg.HttpRequest
import com.softwareberg.UsernameWithPassword as RincaUsernameWithPassword

class JiraReader(private val xmlJiraLoader: XmlJiraLoader, private val httpClient: HttpClient) {

    fun readJiraStories(username: String, password: String, url: String): List<Story> {
        val jiraData = readJiraData(username, password, url)
        return xmlJiraLoader.loadStories(jiraData)
    }

    private fun readJiraData(username: String, password: String, url: String): String {
        val basicAuth = HttpHeader.basicAuth(username, password)
        val httpRequest = HttpRequest(GET, url, listOf(basicAuth))
        val body = httpClient.execute(httpRequest).join().body
        return body ?: ""
    }

}

