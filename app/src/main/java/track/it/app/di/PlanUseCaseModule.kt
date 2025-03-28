package track.it.app.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import track.it.app.data.local.database.AppDatabase
import track.it.app.domain.repository.PlanRepository
import track.it.app.domain.repository.TransactionRepository
import track.it.app.domain.usecases.plan.DeletePlanUseCase
import track.it.app.domain.usecases.plan.GetPlanUseCase
import track.it.app.domain.usecases.plan.GetPlansUseCase
import track.it.app.domain.usecases.plan.UpsertPlanUseCase
import track.it.app.domain.usecases.plan.ValidatePlanFormUseCase
import track.it.app.domain.validator.BudgetValidator
import track.it.app.domain.validator.PlanNameValidator
import track.it.app.domain.validator.PlanNoteValidator
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PlanUseCaseModule {

    @Provides
    @Singleton
    fun provideCreatePlanUseCase(planRepository: PlanRepository): UpsertPlanUseCase {
        return UpsertPlanUseCase(planRepository)
    }

    @Provides
    @Singleton
    fun provideDeletePlanUseCase(
        database: AppDatabase,
        planRepository: PlanRepository,
        transactionRepository: TransactionRepository
    ): DeletePlanUseCase {
        return DeletePlanUseCase(database, planRepository, transactionRepository)
    }

    @Provides
    @Singleton
    fun provideGetPlansUseCase(planRepository: PlanRepository): GetPlansUseCase {
        return GetPlansUseCase(planRepository)
    }

    @Provides
    @Singleton
    fun provideGetPlanUseCase(planRepository: PlanRepository): GetPlanUseCase {
        return GetPlanUseCase(planRepository)
    }

    @Provides
    @Singleton
    fun provideValidatePlanFormUseCase(@ApplicationContext context: Context): ValidatePlanFormUseCase {
        return ValidatePlanFormUseCase(
            planNameValidator = PlanNameValidator(context),
            budgetValidator = BudgetValidator(context),
            planNoteValidator = PlanNoteValidator(context),
        )
    }
}