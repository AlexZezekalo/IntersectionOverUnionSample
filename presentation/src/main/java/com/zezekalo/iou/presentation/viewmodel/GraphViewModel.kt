package com.zezekalo.iou.presentation.viewmodel

import com.zezekalo.iou.domain.model.OutputData
import com.zezekalo.iou.domain.usecase.GetIntersectionOverUnionUseCase
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

    private val _inputDataUi = MutableStateFlow(InputDataUi.INITIAL)
    val inputData: StateFlow<InputDataUi> = _inputDataUi.asStateFlow()

    fun clearData() {
        setInputData(InputDataUi.INITIAL)
        updateUiState(UiState.Clear)
    }

    fun getInputData(): InputDataUi = inputData.value

    fun setInputData(inputDataUi: InputDataUi) {
        _inputDataUi.value = inputDataUi
        calculateIntersectionOverUnion(inputDataUi)
    }

    private fun calculateIntersectionOverUnion(inputDataUi: InputDataUi) {
        scope.launch {
            val inputData = inputDataUi.toDomain()
            useCase(inputData)
                .onFailure { updateUiState(UiState.Error(it)) }
                .onSuccess { updateUiState(UiState.Success(it)) }
        }
    }

    private fun updateUiState(uiState: UiState) {
        _uiState.update { uiState }
    }
}