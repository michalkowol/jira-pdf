package pl.michalkowol.pdfs.itext

import com.itextpdf.kernel.geom.PageSize
import com.itextpdf.kernel.geom.Rectangle
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfPage
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.kernel.pdf.canvas.PdfCanvas
import com.itextpdf.layout.Canvas
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Text
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import pl.michalkowol.jira.Story
import java.io.File

class ItextSpec {

    @Rule
    @JvmField
    val folder = TemporaryFolder()

    @Test
    fun `it should create pdf`() {
        // given
        val path = "${folder.root.path}/simple.pdf"
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
        val path = "${folder.root.path}/rect.pdf"
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
        val path = "${folder.root.path}/simple-rect.pdf"
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
        val path = "${folder.root.path}/simple-table.pdf"
        val pdf = ITextPdfConverter()
        // when
        val bytes = pdf.convert(listOf(Story("1", "Title", "story", "desc", 1, emptyList(), emptyList())))
        File(path).writeBytes(bytes)
        // when
        assertThat(File(path).exists(), equalTo(true))
    }

}
