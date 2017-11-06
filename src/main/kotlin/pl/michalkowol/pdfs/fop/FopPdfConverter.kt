package pl.michalkowol.pdfs.fop

import com.google.common.io.Resources
import com.softwareberg.XmlMapper
import org.apache.fop.apps.FopFactory
import org.apache.fop.apps.MimeConstants.MIME_PDF
import pl.michalkowol.jira.StoriesWrapper
import pl.michalkowol.jira.Story
import pl.michalkowol.pdfs.PdfConverter
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.net.URI
import javax.xml.transform.TransformerFactory
import javax.xml.transform.sax.SAXResult
import javax.xml.transform.stream.StreamSource


class FopPdfConverter(private val xmlMapper: XmlMapper) : PdfConverter {

    override fun convert(stories: List<Story>): ByteArray {
        val storiesWithoutHtmlTags = stories.map { removeHtmlFromDescription(it) }
        val storiesWrapper = StoriesWrapper(storiesWithoutHtmlTags)
        val xml = xmlMapper.write(storiesWrapper)
        return convert(xml)
    }

    private fun removeHtmlFromDescription(story: Story): Story {
        fun removeHtmlTags(html: String): String {
            return html.replace("<[^>]*>".toRegex(), "")
        }

        val descriptionWithoutHtmlTags = removeHtmlTags(story.description ?: "")
        return story.copy(description = descriptionWithoutHtmlTags)
    }

    private fun convert(xml: String): ByteArray {
        val configuration = Resources.getResource("fop/configuration.xconf").toURI()
        val template = Resources.getResource("fop/template.xsl").openStream()
        val data = xml.byteInputStream()
        return convert(configuration, template, data)
    }

    private fun convert(configuration: URI, template: InputStream, data: InputStream): ByteArray {
        val fopFactory = FopFactory.newInstance(configuration)
        return ByteArrayOutputStream().use { out ->
            val fop = fopFactory.newFop(MIME_PDF, out)
            val factory = TransformerFactory.newInstance()
            val transformer = factory.newTransformer(StreamSource(template))
            val res = SAXResult(fop.defaultHandler)
            transformer.transform(StreamSource(data), res)
            out.toByteArray()
        }
    }

}
