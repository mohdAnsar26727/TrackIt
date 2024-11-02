package track.it.app.domain.model

import android.text.format.DateUtils

data class Transaction(
    val id: Long,
    val planId: Long,
    val to: String,
    val note: String,
    val amount: Double,
    val status: TransactionStatus, // Combined field for predicted/paid status
    val createdAt: Long,
    val updatedAt: Long
) {
    val formattedTime get() = DateUtils.getRelativeTimeSpanString(createdAt).toString()
}

data class TransactionWithImages(
    val transaction: Transaction,
    val billImages: List<BillImage>
)

data class BillImage(
    val id: Long = 0,
    val transactionId: Long,
    val imageUrl: String
)

enum class TransactionStatus {
    PAID, // Transaction has been paid
    PREDICTED // Transaction is predicted but not paid
}