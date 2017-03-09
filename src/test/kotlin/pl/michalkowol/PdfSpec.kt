package pl.michalkowol

import com.itextpdf.kernel.geom.Rectangle
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.kernel.pdf.canvas.PdfCanvas
import com.itextpdf.layout.Canvas
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Text
import org.junit.Test


class ItextSpec {

    @Test
    fun `it should create pdf`() {
        val pdf = PdfDocument(PdfWriter("test.pdf"))

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

        //Close document
        pdf.close()
    }
}
