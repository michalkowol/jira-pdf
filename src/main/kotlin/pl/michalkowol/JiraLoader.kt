package pl.michalkowol

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSetter
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import com.softwareberg.XmlMapper

interface JiraLoader {
    fun loadStories(input: String): List<Story>
}

class XmlJiraLoader(private val xmlMapper: XmlMapper) : JiraLoader {

    @JacksonXmlRootElement(localName = "rss")
    private class Rss {
        lateinit var channel: Channel
    }

    private class Channel {
        @JacksonXmlElementWrapper(useWrapping = false)
        @JsonProperty("item")
        var items = mutableListOf<Item>()

        @JsonSetter("item")
        fun addField(item: Item) {
            this.items.add(item)
        }
    }

    class Item {
        lateinit var key: String

        lateinit var summary: String

        lateinit var type: String

        var description: String? = null

        @JsonFormat(with = arrayOf(JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY))
        var comments = mutableListOf<String>()

        @JsonFormat(with = arrayOf(JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY))
        var subtasks = mutableListOf<String>()

        @JacksonXmlElementWrapper(localName = "customfields")
        @JsonProperty("customfield")
        var customFields = mutableListOf<CustomField>()
    }

    class CustomField {
        @JsonProperty("customfieldname")
        lateinit var name: String

        @JsonFormat(with = arrayOf(JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY))
        @JsonProperty("customfieldvalues")
        var values = mutableListOf<String>()
    }

    override fun loadStories(input: String): List<Story> {
        val channel = xmlMapper.read<Rss>(input)
        val stories = channel.channel.items.map { item ->
            val storyPoints = item.customFields.find { it.name == "Story Points" }?.values?.first()?.toDoubleOrNull()?.toInt()
            Story(item.key, item.summary, item.type, item.description, storyPoints, item.comments.orEmpty().filter(String::isNotBlank), item.subtasks.orEmpty().filter(String::isNotBlank))
        }
        return stories
    }
}
