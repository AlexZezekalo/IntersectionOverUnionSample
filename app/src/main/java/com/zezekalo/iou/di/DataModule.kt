package com.zezekalo.iou.di

import com.zezekalo.iou.data.repository.ComputationRepositoryImpl
import com.zezekalo.iou.data.validator.ValidatorImpl
import com.zezekalo.iou.domain.repository.ComputationRepository
import com.zezekalo.iou.domain.validation.Validator
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
interface DataModule {
    @Binds
    fun bindComputationRepository(computationRepositoryImpl: ComputationRepositoryImpl): ComputationRepository

    @Binds
    fun bindValidator(validator: ValidatorImpl): Validator
}
