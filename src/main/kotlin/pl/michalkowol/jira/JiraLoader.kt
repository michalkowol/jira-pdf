package pl.michalkowol.jira

interface JiraLoader {

    fun loadStories(input: String): List<Story>

}

