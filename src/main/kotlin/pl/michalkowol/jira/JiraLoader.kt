package pl.michalkowol.jira

import pl.michalkowol.jira.web.Story

interface JiraLoader {

    fun loadStories(input: String): List<Story>

}

