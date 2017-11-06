package pl.michalkowol.pdfs.fop

import com.natpryce.hamkrest.assertion.assert
import com.natpryce.hamkrest.greaterThan
import com.natpryce.hamkrest.hasSize
import com.softwareberg.XmlMapper
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import pl.michalkowol.jira.Story
import java.io.FileOutputStream


class FopPdfConverterSpec {

    @Rule
    @JvmField
    val folder = TemporaryFolder()

    @Test
    fun `it should create pdf`() {
        // given
        val stories = listOf(
            Story("1", "Title 1", "bug", "Desc 1", 2, emptyList(), emptyList()),
            Story("2", "Title 2", "bug", "Desc 2", 3, emptyList(), emptyList()),
            Story("4", "Title 3", "bug", "", 3, emptyList(), emptyList())
        )
        val xmlMapper = XmlMapper.create()
        val converter = FopPdfConverter(xmlMapper)
        // when
        val bytes = converter.convert(stories)
        // then
        save(bytes)
        assert.that(bytes.asList(), hasSize(greaterThan(5_000)))
    }

    private fun save(bytes: ByteArray) {
        FileOutputStream("${folder.root.path}/pdf.pdf").use { fos ->
            fos.write(bytes)
        }
    }

}
