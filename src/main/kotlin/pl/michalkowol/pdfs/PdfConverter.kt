package pl.michalkowol.pdfs

import pl.michalkowol.jira.Story

interface PdfConverter {

    fun convert(stories: List<Story>): ByteArray

}
