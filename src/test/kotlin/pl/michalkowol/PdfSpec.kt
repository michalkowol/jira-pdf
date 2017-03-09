package pl.michalkowol

import com.itextpdf.kernel.geom.PageSize
import com.itextpdf.kernel.geom.Rectangle
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfPage
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.kernel.pdf.canvas.PdfCanvas
import com.itextpdf.layout.Canvas
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.*
import com.itextpdf.layout.property.UnitValue.createPercentValue
import com.itextpdf.layout.property.VerticalAlignment
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.softwareberg.XmlMapper
import org.junit.Test
import java.io.File


class ItextSpec {

    @Test
    fun `it should create pdf`() {
        // given
        val path = "pdfs/simple.pdf"
        // when
        val pdf = PdfDocument(PdfWriter(path))
        val pageA = pdf.addNewPage(PageSize(400f, 300f))
        val pageB = pdf.addNewPage(PageSize(400f, 300f))
        pdf.close()
        // then
        assertThat(File(path).exists(), equalTo(true))
    }

    @Test
    fun `it should create with rec pdf`() {
        // given
        val path = "pdfs/rect.pdf"
        // when
        val pdf = PdfDocument(PdfWriter(path))
        val page = pdf.addNewPage()
        val pdfCanvas = PdfCanvas(page)
        val rectangle = Rectangle(36f, 650f, 100f, 100f)
        pdfCanvas.rectangle(rectangle)
        pdfCanvas.stroke()
        val canvas = Canvas(pdfCanvas, pdf, rectangle)
//        val font = PdfFontFactory.createFont(StyleConstants.FontConstants.TIMES_ROMAN)
//        val bold = PdfFontFactory.createFont(StyleConstants.FontConstants.TIMES_BOLD)
        val title = Text("The Strange Case of Dr. Jekyll and Mr. Hyde")//.setFont(bold)
        val author = Text("Robert Louis Stevenson")//.setFont(font)
        val p = Paragraph().add(title).add(" by ").add(author)
        canvas.add(p)
        pdf.close()
        // then
        assertThat(File(path).exists(), equalTo(true))
    }

    @Test
    fun `it should create pdf with rect`() {
        // given
        val path = "pdfs/simple-rect.pdf"
        // when
        val pdf = PdfDocument(PdfWriter(path))
        val pageA = pdf.addNewPage(PageSize(400f, 300f))
        rect(pdf, pageA)
        val pageB = pdf.addNewPage(PageSize(400f, 300f))
        rect(pdf, pageB)
        val pageC = pdf.addNewPage(PageSize(400f, 300f))
        rect(pdf, pageC)
        val pageD = pdf.addNewPage(PageSize(400f, 300f))
        rect(pdf, pageD)
        pdf.close()
        // then
        assertThat(File(path).exists(), equalTo(true))
    }

    private fun rect(pdf: PdfDocument, page: PdfPage): Unit {
        val pdfCanvas = PdfCanvas(page)
        val pageSize = page.pageSize
        val descriptionBox = Rectangle(pageSize.width - 10f, pageSize.height - 10f - 40f).setX(5f).setY(5f)
        val titleBox = Rectangle(pageSize.width - 10f, 40f).setX(5f).setY(pageSize.height - 40f - 5f)
        val pointsBox = Rectangle(40f, 40f).setX(pageSize.width - 40f - 5f).setY(pageSize.height - 40f - 5f)

        pdfCanvas.rectangle(descriptionBox)
        pdfCanvas.rectangle(titleBox)
        pdfCanvas.rectangle(pointsBox)
        pdfCanvas.stroke()

        val titleCanvas = Canvas(pdfCanvas, pdf, titleBox)
        val title = Text("The Strange Case of Dr. Jekyll and Mr. Hyde")
        titleCanvas.add(Paragraph().add(title).setMargin(0f))
    }

    @Test
    fun `it should create pdf with table`() {
        // given
        val path = "pdfs/simple-table.pdf"
        // when
        val pdf = PdfDocument(PdfWriter(path))
        val doc = Document(pdf, PageSize(400f, 300f))
        doc.setMargins(10f, 10f, 10f, 10f)
        doc.add(table("1", "Title", "Desc", "3"))
        doc.add(AreaBreak())
        doc.add(table("2", "Title", "Desc", "1"))

        pdf.close()
        // then
        assertThat(File(path).exists(), equalTo(true))
    }

    @Test
    fun `it should create pdf from jira`() {
        // given
        val xml = TestUtils.readFile("jira/samples/xml/stories.xml")
        val xmlMapper = XmlMapper.create()
        val xmlJiraLoader = XmlJiraLoader(xmlMapper)
        val stories = xmlJiraLoader.loadStories(xml)
        val path = "pdfs/jira-table.pdf"
        // when
        val pdf = PdfDocument(PdfWriter(path))
        val doc = Document(pdf, PageSize(400f, 300f))
        doc.setMargins(10f, 10f, 10f, 10f)

        stories.forEach { story ->
            doc.add(table(story.key, story.summary, story.description.orEmpty().replace("<[^>]*>".toRegex(), ""), story.storyPoints?.toString().orEmpty()))
            doc.add(AreaBreak())
        }

        pdf.close()
        // then
        assertThat(File(path).exists(), equalTo(true))
    }
}
