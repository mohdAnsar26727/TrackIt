package track.it.app.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import track.it.app.domain.repository.PlanRepository
import track.it.app.domain.usecases.plan.CreatePlanUseCase
import track.it.app.domain.usecases.plan.DeletePlanUseCase
import track.it.app.domain.usecases.plan.GetPlanUseCase
import track.it.app.domain.usecases.plan.GetPlansUseCase
import track.it.app.domain.usecases.plan.UpdatePlanUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PlanUseCaseModule {

    @Provides
    @Singleton
    fun provideCreatePlanUseCase(planRepository: PlanRepository): CreatePlanUseCase {
        return CreatePlanUseCase(planRepository)
    }

    @Provides
    @Singleton
    fun provideDeletePlanUseCase(planRepository: PlanRepository): DeletePlanUseCase {
        return DeletePlanUseCase(planRepository)
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
    fun provideUpdatePlanUseCase(planRepository: PlanRepository): UpdatePlanUseCase {
        return UpdatePlanUseCase(planRepository)
    }
}