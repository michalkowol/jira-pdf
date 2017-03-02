package pl.michalkowol

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSetter
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.containsSubstring
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.isEmpty
import com.softwareberg.XmlMapper
import org.junit.Test
import pl.michalkowol.TestUtils.readFile

data class Story(val key: String, val summary: String, val type: String, val description: String?, val comments: List<String>, val subtasks: List<String>)

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
    }

    override fun loadStories(input: String): List<Story> {
        val channel = xmlMapper.read<Rss>(input)
        val stories = channel.channel.items.map { item -> Story(item.key, item.summary, item.type, item.description, item.comments.orEmpty().filter(String::isNotBlank), item.subtasks.orEmpty().filter(String::isNotBlank)) }
        return stories
    }
}

class XmlJiraLoaderSpec {

    @Test
    fun `it should read one minmal story from xml`() {
        // given
        val xml = """<rss version="0.92">
    <channel>
        <item>
            <description>As a producer I want...</description>
            <key>WTAI-1052</key>
            <summary>Provider base configuration for ARC data</summary>
            <type>Story</type>
            <comments>
                <comment>comment A</comment>
                <comment>comment B</comment>
            </comments>
            <subtasks>
                <subtask>WTAI-553</subtask>
                <subtask>WTAI-551</subtask>
            </subtasks>
        </item>
    </channel>
</rss>"""
        val xmlMapper = XmlMapper.create()
        val xmlJiraLoader = XmlJiraLoader(xmlMapper)

        // when
        val story = xmlJiraLoader.loadStories(xml)[0]

        // then
        assertThat(story.key, equalTo("WTAI-1052"))
        assertThat(story.summary, equalTo("Provider base configuration for ARC data"))
        assertThat(story.type, equalTo("Story"))
        assertThat(story.comments[0], equalTo("comment A"))
        assertThat(story.comments[1], equalTo("comment B"))
        assertThat(story.subtasks[0], equalTo("WTAI-553"))
        assertThat(story.subtasks[1], equalTo("WTAI-551"))
        assertThat(story.description, equalTo("As a producer I want..."))
    }

    @Test
    fun `it should read stories from xml`() {
        // given
        // project = WTAI and key in (WTAI-1052, WTAI-440, WTAI-552, WTAI-992)
        val xml = readFile("jira/samples/xml/stories.xml")
        val xmlMapper = XmlMapper.create()
        val xmlJiraLoader = XmlJiraLoader(xmlMapper)

        // when
        val stories = xmlJiraLoader.loadStories(xml)

        // then
        val storyA = stories[0]
        val storyB = stories[1]
        val storyC = stories[2]
        val storyD = stories[3]
        assertThat(storyA.key, equalTo("WTAI-1052"))
        assertThat(storyA.summary, equalTo("Provider base configuration for ARC data"))
        assertThat(storyA.type, equalTo("Story"))
        assertThat(storyA.comments[0], containsSubstring("We should use"))
        assertThat(storyA.subtasks, isEmpty)
        assertThat(storyA.description.orEmpty(), containsSubstring("As a producer I want to have provider bas"))
    }
}
