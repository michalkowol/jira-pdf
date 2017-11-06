package pl.michalkowol.jira

data class Story(val key: String, val summary: String, val type: String, val description: String?, val storyPoints: Int?, val comments: List<String>, val subtasks: List<String>)
