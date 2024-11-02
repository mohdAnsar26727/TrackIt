package track.it.app.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import track.it.app.data.mappers.BillImageMapper
import track.it.app.data.mappers.TransactionMapper
import track.it.app.data.mappers.TransactionWithImagesMapper
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataMapperModule {

    @Provides
    @Singleton
    fun provideTransactionMapper(): TransactionMapper {
        return TransactionMapper()
    }

    @Provides
    @Singleton
    fun provideBillImagesMapper(): BillImageMapper {
        return BillImageMapper()
    }

    @Provides
    @Singleton
    fun provideTransactionWithImagesMapper(
        transactionMapper: TransactionMapper,
        billImageMapper: BillImageMapper
    ): TransactionWithImagesMapper {
        return TransactionWithImagesMapper(
            transactionMapper,
            billImageMapper
        )
    }
}