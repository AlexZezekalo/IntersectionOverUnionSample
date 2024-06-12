package com.zezekalo.iou.di

import android.content.Context
import com.zezekalo.iou.presentation.ui.util.MinMaxInputFilter
import com.zezekalo.iou.presentation.ui.util.mapper.ThrowableToErrorMessageMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext

@InstallIn(ActivityComponent::class)
@Module
class PresentationModule {

    @Provides
    fun provideThrowableToErrorMessageMapper(@ActivityContext context: Context): ThrowableToErrorMessageMapper =
        ThrowableToErrorMessageMapper(context.resources)

    @Provides
    fun provideMinMaxInputFilter(): MinMaxInputFilter = MinMaxInputFilter(MIN_INPUT_VALUE, MAX_INPUT_VALUE)

    companion object {
        const val MIN_INPUT_VALUE: Int = 0
        const val MAX_INPUT_VALUE: Int = 16
    }
}