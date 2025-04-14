package track.it.app.di

import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import track.it.app.ui.plans.details.PlanDetailsViewModel
import track.it.app.ui.transaction.create.AddEditTransactionViewModel
import track.it.app.ui.transaction.details.TransactionDetailsViewModel

@EntryPoint
@InstallIn(ActivityComponent::class)
interface PlanDetailsViewModelFactoryProvider {
    fun planDetailsViewModelFactory(): PlanDetailsViewModel.PlanDetailsViewModelFactory
}

@EntryPoint
@InstallIn(ActivityComponent::class)
interface TransactionDetailsViewModelFactoryProvider {
    fun transactionDetailsViewModelFactory(): TransactionDetailsViewModel.TransactionDetailsViewModelFactory
}

@EntryPoint
@InstallIn(ActivityComponent::class)
interface AddEditTransactionViewModelFactoryProvider {
    fun addEditTransactionViewModelFactory(): AddEditTransactionViewModel.AddEditTransactionViewModelFactory
}