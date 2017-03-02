package pl.michalkowol

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.containsSubstring
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.hasSize
import com.natpryce.hamkrest.isEmpty
import com.softwareberg.XmlMapper
import org.junit.Test
import pl.michalkowol.TestUtils.readFile

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
        assertThat(stories, hasSize(equalTo(4)))
        val storyA = stories[0]
        assertThat(storyA.key, equalTo("WTAI-1052"))
        assertThat(storyA.summary, equalTo("Provider base configuration for ARC data"))
        assertThat(storyA.type, equalTo("Story"))
        assertThat(storyA.comments[0], containsSubstring("We should use"))
        assertThat(storyA.subtasks, isEmpty)
        assertThat(storyA.description.orEmpty(), containsSubstring("As a producer I want to have provider bas"))
    }
}
