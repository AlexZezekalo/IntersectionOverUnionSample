package com.zezekalo.iou.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.zezekalo.iou.domain.model.GroundTruthBoundingBox
import com.zezekalo.iou.domain.model.OutputData
import com.zezekalo.iou.domain.model.PredictedBoundingBox
import com.zezekalo.iou.domain.model.exception.CustomException
import com.zezekalo.iou.domain.usecase.GetIntersectionOverUnionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

sealed class UiState {
    data object Idle : UiState()
    data object Loading : UiState()
    data class Error(val customException: CustomException) : UiState()
    data class Success(val data: OutputData) : UiState()
}

@HiltViewModel
class MainViewModel @Inject constructor(
    private val useCase: GetIntersectionOverUnionUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState

    fun calculateIntersectionOverUnion(
        groundTruthBoundingBox: GroundTruthBoundingBox,
        predictedBoundingBox: PredictedBoundingBox
    ) {
        //TODO
    }

}