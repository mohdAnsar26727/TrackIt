package track.it.app.domain.usecases.transaction

import track.it.app.domain.repository.FileRepository

class DeleteBillImageFilesUseCase(
    private val fileHandler: FileRepository
) {
    operator fun invoke(copiedImages: List<String>) = fileHandler.deleteImages(copiedImages)
}