package track.it.app.domain.repository

interface FileRepository {
    fun copyImage(
        sourceFilePath: String,
        fileName: String
    ): String?

    fun deleteImages(sourceFilePath: List<String>)
}