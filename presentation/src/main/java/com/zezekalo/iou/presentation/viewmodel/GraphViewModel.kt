package com.zezekalo.iou.presentation.viewmodel

import com.zezekalo.iou.domain.usecase.GetIntersectionOverUnionUseCase
import com.zezekalo.iou.presentation.viewmodel.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class GraphViewModel @Inject constructor(
    private val useCase: GetIntersectionOverUnionUseCase
) : BaseViewModel() {

    init {
        Timber.i("init; $this")
    }


}