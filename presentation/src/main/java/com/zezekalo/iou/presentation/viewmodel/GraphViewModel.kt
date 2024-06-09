package com.zezekalo.iou.presentation.viewmodel

import com.zezekalo.iou.domain.model.OutputData
import com.zezekalo.iou.domain.usecase.GetIntersectionOverUnionUseCase
import com.zezekalo.iou.presentation.model.BoundingBoxUi
import com.zezekalo.iou.presentation.model.InputDataUi
import com.zezekalo.iou.presentation.model.toDomain
import com.zezekalo.iou.presentation.viewmodel.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
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

    private val _inputGroundTruthBoundingBox = MutableStateFlow(BoundingBoxUi.EMPTY)
    val inputGroundTruthBoundingBox: StateFlow<BoundingBoxUi> = _inputGroundTruthBoundingBox.asStateFlow()

    private val _inputPredictedBoundingBox = MutableStateFlow(BoundingBoxUi.EMPTY)
    val inputPredictedBoundingBox: StateFlow<BoundingBoxUi> = _inputPredictedBoundingBox.asStateFlow()

    fun clearData() {
        setInputData(InputDataUi.INITIAL)
        updateUiState(UiState.Clear)
    }

    fun getInputData(): InputDataUi =
        InputDataUi(
            groundTruthBoundingBox = inputGroundTruthBoundingBox.value,
            predictedBoundingBox = inputPredictedBoundingBox.value
        )

    fun setInputData(inputData: InputDataUi) {
        _inputGroundTruthBoundingBox.update { inputData.groundTruthBoundingBox }
        _inputPredictedBoundingBox.update { inputData.predictedBoundingBox }
    }

    fun calculateIntersectionOverUnion() {
        scope.launch {
            val inputData = getInputData().toDomain()
            useCase(inputData)
                .onFailure { updateUiState(UiState.Error(it)) }
                .onSuccess { updateUiState(UiState.Success(it)) }
        }
    }

    private fun updateUiState(uiState: UiState) {
        _uiState.update { uiState }
    }
}