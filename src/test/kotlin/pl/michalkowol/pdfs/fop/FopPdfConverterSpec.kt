package pl.michalkowol.pdfs.fop

import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.FileOutputStream


class FopPdfConverterSpec {

    @Rule
    @JvmField
    val folder = TemporaryFolder()

    @Test
    fun `it should create pdf`() {
        // given
        val converter = FopPdfConverter()
        // when
        val bytes = converter.convert(emptyList())
        save(bytes)

        // then
    }

    private fun save(bytes: ByteArray) {
        FileOutputStream("pdf.pdf").use { fos ->
            fos.write(bytes)
        }
    }

}
