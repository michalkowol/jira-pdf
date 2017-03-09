package pl.michalkowol

import com.itextpdf.kernel.geom.PageSize
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.*
import com.itextpdf.layout.property.UnitValue
import com.itextpdf.layout.property.VerticalAlignment
import java.io.ByteArrayOutputStream

class PdfConverter {

    fun convert(stories: List<Story>): ByteArray {
        return ByteArrayOutputStream().use { outputStream ->
            PdfDocument(PdfWriter(outputStream)).use { pdf ->
                Document(pdf, PageSize(400f, 300f)).use { doc ->
                    addPagePerStory(doc, stories)
                    pdf.close()
                    outputStream.toByteArray()
                }
            }
        }
    }

    private fun addPagePerStory(doc: Document, stories: List<Story>) {
        doc.setMargins(10f, 10f, 10f, 10f)
        val tables = stories.map { (key, summary, _, description, storyPoints) ->
            val descriptionWithoutHtmlTags = if (description != null) removeHtmlTags(description) else ""
            table(key, summary, descriptionWithoutHtmlTags, storyPoints?.toString().orEmpty())
        }
        tables.withIndex().map { (index, table) ->
            doc.add(table)
            if (index < tables.size - 1) {
                doc.add(AreaBreak())
            }
        }
    }

    private fun removeHtmlTags(html: String): String {
        return html.replace("<[^>]*>".toRegex(), "")
    }

    private fun table(key: String, title: String, description: String, points: String): Table {
        val table = Table(arrayOf(UnitValue.createPercentValue(93f), UnitValue.createPercentValue(7f))).setHeight(300f - 20f)
        val titleCell = Cell().add(Paragraph(Text(key).setBold()).add(" ").add(title)).setVerticalAlignment(VerticalAlignment.MIDDLE).setMinHeight(20f).setMaxHeight(60f)
        val pointsCell = Cell().add(Paragraph(points)).setVerticalAlignment(VerticalAlignment.MIDDLE).setMinHeight(20f).setMaxHeight(60f)
        val descriptionCell = Cell(1, 2).add(Paragraph(description).setFontSize(8f)).setHeight(300f - 20f - 60f - 5f)
        table.addCell(titleCell).addCell(pointsCell).addCell(descriptionCell)
        return table
    }
}
