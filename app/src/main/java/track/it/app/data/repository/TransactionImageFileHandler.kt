package track.it.app.data.repository

import android.content.Context
import androidx.core.net.toUri
import track.it.app.domain.repository.FileRepository
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class TransactionImageFileHandler(private val context: Context) : FileRepository {
    override fun copyImage(
        sourceFilePath: String,
        fileName: String
    ): String? {
        try {
            // Create the destination folder if it doesn't exist
            val destinationFolder = File(
                context.filesDir,
                FOLDER_NAME
            )
            if (!destinationFolder.exists()) {
                destinationFolder.mkdir()
            }

            // Create destination file in the app-specific folder
            val destinationFile = File(
                destinationFolder,
                fileName
            )
            // Copy the source file to the destination file
            context.contentResolver.openInputStream(sourceFilePath.toUri())?.use { inputStream ->
                FileOutputStream(destinationFile).use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }

            // Return the new file path
            return destinationFile.absolutePath
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }
    }

    override fun deleteImages(sourceFilePath: List<String>) {
        sourceFilePath.forEach { filePath ->
            try {
                val file = File(filePath)
                if (file.exists()) {
                    file.delete()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    companion object {
        const val FOLDER_NAME = "Bill_images"
    }
}