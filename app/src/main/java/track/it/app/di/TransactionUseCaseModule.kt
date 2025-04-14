package track.it.app.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import track.it.app.domain.repository.FileRepository
import track.it.app.domain.repository.TransactionRepository
import track.it.app.domain.usecases.transaction.AddTransactionImagesUseCase
import track.it.app.domain.usecases.transaction.CheckTransactionConstraintsUseCase
import track.it.app.domain.usecases.transaction.CopyBillImageFilesUseCase
import track.it.app.domain.usecases.transaction.CreateTransactionUseCase
import track.it.app.domain.usecases.transaction.DeleteBillImageFilesUseCase
import track.it.app.domain.usecases.transaction.DeleteTransactionUseCase
import track.it.app.domain.usecases.transaction.GetTransactionUseCase
import track.it.app.domain.usecases.transaction.GetTransactionsUseCase
import track.it.app.domain.usecases.transaction.UpdateTransactionUseCase
import track.it.app.domain.usecases.transaction.ValidateTransactionFormUseCase
import track.it.app.domain.validator.TransactionAmountConstraintsValidator
import track.it.app.domain.validator.TransactionAmountValidator
import track.it.app.domain.validator.TransactionImagesValidator
import track.it.app.domain.validator.TransactionNoteValidator
import track.it.app.domain.validator.TransactionReceiverNameValidator
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TransactionUseCaseModule {

    @Provides
    @Singleton
    fun provideAddTransactionUseCase(transactionRepository: TransactionRepository): AddTransactionImagesUseCase {
        return AddTransactionImagesUseCase(transactionRepository)
    }

    @Provides
    @Singleton
    fun provideUpdateTransactionUseCase(transactionRepository: TransactionRepository): UpdateTransactionUseCase {
        return UpdateTransactionUseCase(transactionRepository)
    }

    @Provides
    @Singleton
    fun provideGetTransactionsUseCase(transactionRepository: TransactionRepository): GetTransactionsUseCase {
        return GetTransactionsUseCase(transactionRepository)
    }

    @Provides
    @Singleton
    fun provideGetTransactionUseCase(transactionRepository: TransactionRepository): GetTransactionUseCase {
        return GetTransactionUseCase(transactionRepository)
    }

    @Provides
    @Singleton
    fun provideCreateTransactionUseCase(
        transactionRepository: TransactionRepository,
        copyBillImageFilesUseCase: CopyBillImageFilesUseCase,
        addTransactionImagesUseCase: AddTransactionImagesUseCase,
        deleteBillImageFilesUseCase: DeleteBillImageFilesUseCase
    ): CreateTransactionUseCase {
        return CreateTransactionUseCase(
            transactionRepository,
            copyBillImageFilesUseCase,
            addTransactionImagesUseCase,
            deleteBillImageFilesUseCase
        )
    }

    @Provides
    @Singleton
    fun provideDeleteTransactionUseCase(transactionRepository: TransactionRepository): DeleteTransactionUseCase {
        return DeleteTransactionUseCase(transactionRepository)
    }

    @Provides
    @Singleton
    fun provideCopyBillImagesUseCase(repository: FileRepository): CopyBillImageFilesUseCase {
        return CopyBillImageFilesUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideDeleteBillImageFilesUseCase(repository: FileRepository): DeleteBillImageFilesUseCase {
        return DeleteBillImageFilesUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideValidateTransactionFormUseCase(@ApplicationContext context: Context): ValidateTransactionFormUseCase {
        return ValidateTransactionFormUseCase(
            transactionReceiverNameValidator = TransactionReceiverNameValidator(context),
            transactionAmountValidator = TransactionAmountValidator(context),
            transactionNoteValidator = TransactionNoteValidator(context),
            transactionImagesValidator = TransactionImagesValidator(context)
        )
    }

    @Provides
    @Singleton
    fun provideValidateTransactionConstraintsUseCase(
        @ApplicationContext context: Context
    ): CheckTransactionConstraintsUseCase {
        return CheckTransactionConstraintsUseCase(
            TransactionAmountConstraintsValidator(context)
        )
    }
}