package com.zezekalo.iou.presentation.viewmodel

import com.zezekalo.iou.domain.model.GroundTruthBoundingBox
import com.zezekalo.iou.domain.model.InputData
import com.zezekalo.iou.domain.model.OutputData
import com.zezekalo.iou.domain.model.PredictedBoundingBox
import com.zezekalo.iou.domain.usecase.GetIntersectionOverUnionUseCase
import com.zezekalo.iou.presentation.viewmodel.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class UiState {
    data object Clear : UiState()
    data class Error(val throwable: Throwable) : UiState()
    data class Success(val data: OutputData) : UiState()
}

@HiltViewModel
class GraphViewModel @Inject constructor(
    private val useCase: GetIntersectionOverUnionUseCase
) : BaseViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Clear)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _inputData = MutableStateFlow(InputData.EMPTY)
    private val inputData = _inputData.asStateFlow()

    val groundTruthBoundingBox = inputData.map { it.groundTruthBoundingBox }
    val predictedBoundingBox = inputData.map { it.predictedBoundingBox }

    fun clearData() {
        updateInputData(InputData.EMPTY)
        updateUiState(UiState.Clear)
    }

    //TODO only for test purposes, remove later
    fun startTestBoxes() {
        updateInputData(inputDataWithOverlapping)
        calculateIntersectionOverUnion(inputDataWithOverlapping.groundTruthBoundingBox, inputDataWithOverlapping.predictedBoundingBox)
    }

    fun calculateIntersectionOverUnion(
        groundTruthBoundingBox: GroundTruthBoundingBox,
        predictedBoundingBox: PredictedBoundingBox
    ) {
        scope.launch {
            val inputData = InputData(
                groundTruthBoundingBox = groundTruthBoundingBox,
                predictedBoundingBox = predictedBoundingBox
            )
            useCase(inputData)
                .onFailure { updateUiState(UiState.Error(it)) }
                .onSuccess { updateUiState(UiState.Success(it)) }
        }
    }

    private fun updateUiState(uiState: UiState) {
        _uiState.update { uiState }
    }

    private fun updateInputData(inputData: InputData) {
        _inputData.update { inputData }
    }
}

//TODO only for test purposes, remove later
val inputDataWithOverlapping: InputData = InputData(
    groundTruthBoundingBox = GroundTruthBoundingBox(
        left = 3,
        top = 3,
        right = 10,
        bottom = 10
    ),
    predictedBoundingBox = PredictedBoundingBox(
        left = 7,
        top = 7,
        right = 13,
        bottom = 13
    )
)