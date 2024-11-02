package track.it.app.di

import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import track.it.app.data.repository.PlanRepositoryImpl
import track.it.app.data.repository.TransactionImageFileHandler
import track.it.app.data.repository.TransactionRepositoryImpl
import track.it.app.domain.repository.FileRepository
import track.it.app.domain.repository.PlanRepository
import track.it.app.domain.repository.TransactionRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    // Use @Binds to simplify PlanRepository binding
    @Binds
    @Singleton
    abstract fun bindPlanRepository(
        planRepositoryImpl: PlanRepositoryImpl
    ): PlanRepository

    // Use @Binds to bind FileRepository
    @Binds
    @Singleton
    abstract fun bindFileRepository(
        fileRepositoryImpl: TransactionImageFileHandler
    ): FileRepository

    // Use @Binds for TransactionRepository
    @Binds
    @Singleton
    abstract fun bindTransactionRepository(
        transactionRepositoryImpl: TransactionRepositoryImpl
    ): TransactionRepository

    companion object {
        // Use @Provides where context is required
        @Provides
        @Singleton
        fun provideTransactionImageFileHandler(
            @ApplicationContext context: Context
        ): TransactionImageFileHandler {
            return TransactionImageFileHandler(context)
        }
    }
}


