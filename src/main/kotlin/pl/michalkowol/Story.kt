package pl.michalkowol

data class Story(val key: String, val summary: String, val type: String, val description: String?, val comments: List<String>, val subtasks: List<String>)
