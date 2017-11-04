package pl.michalkowol.pdfs

import pl.michalkowol.jira.web.Story

interface PdfConverter {

    fun convert(stories: List<Story>): ByteArray

}
