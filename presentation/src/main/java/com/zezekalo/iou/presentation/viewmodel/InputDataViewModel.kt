package com.zezekalo.iou.presentation.viewmodel

import com.zezekalo.iou.domain.usecase.GetValidationResultUseCase
import com.zezekalo.iou.presentation.model.BoundingBoxUi
import com.zezekalo.iou.presentation.model.InputDataUi
import com.zezekalo.iou.presentation.viewmodel.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class InputDataViewModel @Inject constructor(
    private val useCase: GetValidationResultUseCase
): BaseViewModel() {

    private val _inputGroundTruthBoundingBox = MutableStateFlow(BoundingBoxUi.EMPTY)
    val inputGroundTruthBoundingBox: StateFlow<BoundingBoxUi> = _inputGroundTruthBoundingBox.asStateFlow()

    private val _inputPredictedBoundingBox = MutableStateFlow(BoundingBoxUi.EMPTY)
    val inputPredictedBoundingBox: StateFlow<BoundingBoxUi> = _inputPredictedBoundingBox.asStateFlow()

    private val _inputValidTruthBoundingBox = MutableStateFlow(BoundingBoxUi.EMPTY)

    init {
        Timber.i("start InputDataViewModel")
    }

    fun setInputData(inputData: InputDataUi) {
        _inputGroundTruthBoundingBox.update { inputData.groundTruthBoundingBox }
        _inputPredictedBoundingBox.update { inputData.predictedBoundingBox }
    }

    fun validateInput(inputData: InputDataUi) {
        scope.launch {
//            useCase.invoke()
        }
    }


}