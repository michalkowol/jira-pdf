package pl.michalkowol.pdfs.fop

import com.google.common.io.Resources
import org.apache.fop.apps.FopFactory
import org.apache.fop.apps.MimeConstants.MIME_PDF
import pl.michalkowol.jira.web.Story
import pl.michalkowol.pdfs.PdfConverter
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.net.URI
import javax.xml.transform.TransformerFactory
import javax.xml.transform.sax.SAXResult
import javax.xml.transform.stream.StreamSource
import org.apache.fop.apps.FOUserAgent



class FopPdfConverter : PdfConverter {

    override fun convert(stories: List<Story>): ByteArray {
        return convert()
    }

    private fun convert(): ByteArray {
        val configuration = Resources.getResource("fop/configuration.xconf").toURI()
        val template = Resources.getResource("fop/template.xsl").openStream()
        val data = Resources.getResource("fop/data.xml").openStream()
        return convert(configuration, template, data)
    }

    private fun convert(configuration: URI, template: InputStream, data: InputStream): ByteArray {
        val fopFactory = FopFactory.newInstance(configuration)
        return ByteArrayOutputStream().use { out ->
            val foUserAgent = fopFactory.newFOUserAgent()
            val fop = fopFactory.newFop(MIME_PDF, foUserAgent, out)
            val factory = TransformerFactory.newInstance()
            val transformer = factory.newTransformer(StreamSource(template))
            val res = SAXResult(fop.defaultHandler)
            transformer.transform(StreamSource(data), res)
            out.toByteArray()
        }
    }

}
