package pl.michalkowol.jira

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement

@JacksonXmlRootElement(localName = "stories")
data class StoriesWrapper(@JsonProperty("story") @JacksonXmlElementWrapper(useWrapping = false) val stories: List<Story>)
