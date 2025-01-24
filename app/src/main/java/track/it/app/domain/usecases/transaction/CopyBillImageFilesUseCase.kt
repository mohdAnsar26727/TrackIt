package track.it.app.domain.usecases.transaction

import track.it.app.domain.repository.FileRepository
import track.it.app.util.DateFormat
import track.it.app.util.formattedDate

class CopyBillImageFilesUseCase(
    private val fileHandler: FileRepository
) {
    operator fun invoke(
        originalImages: List<String>,
        transactionTo: String
    ) = kotlin.runCatching {
        originalImages.mapIndexedNotNull { index, url ->
            fileHandler.copyImage(
                url,
                "${index}_${transactionTo}_${
                    System.currentTimeMillis().formattedDate(DateFormat.DD_MM_YYYY_DOT)
                }.jpg"
            )
        }
    }
}