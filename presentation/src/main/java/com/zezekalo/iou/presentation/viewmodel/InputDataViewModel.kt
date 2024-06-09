package com.zezekalo.iou.presentation.viewmodel

import android.text.Editable
import com.zezekalo.iou.domain.model.Coordinate
import com.zezekalo.iou.domain.model.exception.CustomException
import com.zezekalo.iou.domain.model.exception.InvalidBoxCoordinateRangeException
import com.zezekalo.iou.domain.model.exception.ValidationResult
import com.zezekalo.iou.domain.model.exception.noError
import com.zezekalo.iou.domain.usecase.GetValidationResultUseCase
import com.zezekalo.iou.presentation.model.BoundingBoxUi
import com.zezekalo.iou.presentation.model.InputDataUi
import com.zezekalo.iou.presentation.ui.util.extensions.toIntOrZero
import com.zezekalo.iou.presentation.viewmodel.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.*
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

    private val _groundTruthLeftError = MutableStateFlow<CustomException?>(null)
    val groundTruthLeftError = _groundTruthLeftError.asStateFlow()
    private val _groundTruthTopError = MutableStateFlow<CustomException?>(null)
    val groundTruthTopError = _groundTruthTopError.asStateFlow()
    private val _groundTruthRightError = MutableStateFlow<CustomException?>(null)
    val groundTruthRightError = _groundTruthRightError.asStateFlow()
    private val _groundTruthBottomError = MutableStateFlow<CustomException?>(null)
    val groundTruthBottomError = _groundTruthBottomError.asStateFlow()
    private val _predictedLeftError = MutableStateFlow<CustomException?>(null)
    val predictedLeftError = _predictedLeftError.asStateFlow()
    private val _predictedTopError = MutableStateFlow<CustomException?>(null)
    val predictedTopError = _predictedTopError.asStateFlow()
    private val _predictedRightError = MutableStateFlow<CustomException?>(null)
    val predictedRightError = _predictedRightError.asStateFlow()
    private val _predictedBottomError = MutableStateFlow<CustomException?>(null)
    val predictedBottomError = _predictedBottomError.asStateFlow()

    private val groundTruthErrorMap: MutableMap<Coordinate, MutableStateFlow<CustomException?>> = mutableMapOf(
        Coordinate.LEFT to _groundTruthLeftError, Coordinate.TOP to _groundTruthTopError,
        Coordinate.RIGHT to _groundTruthRightError, Coordinate.BOTTOM to _groundTruthBottomError
    )
    private val summaryGroundTruthError = combine(
        groundTruthLeftError, groundTruthTopError, groundTruthRightError, groundTruthBottomError) {
            leftError, topError, rightError, bottomError ->
        leftError != null || topError != null || rightError!= null || bottomError!= null
    }
    private val predictedErrorMap: MutableMap<Coordinate, MutableStateFlow<CustomException?>> = mutableMapOf(
        Coordinate.LEFT to _predictedLeftError, Coordinate.TOP to _predictedTopError,
        Coordinate.RIGHT to _predictedRightError, Coordinate.BOTTOM to _predictedBottomError
    )
    private val summaryPredictedError = combine(
        predictedLeftError, predictedTopError, predictedRightError, predictedBottomError) {
            leftError, topError, rightError, bottomError ->
        leftError != null || topError != null || rightError != null || bottomError != null
    }

    val inputError = combine(summaryGroundTruthError, summaryPredictedError) {
        groundTruthError, predictedError -> groundTruthError || predictedError
    }

    fun setInputData(inputData: InputDataUi) {
        _inputGroundTruthBoundingBox.update { inputData.groundTruthBoundingBox }
        _inputPredictedBoundingBox.update { inputData.predictedBoundingBox }
    }

    fun validateInput() {
        scope.launch {
            val groundTruthBoundingBox = inputGroundTruthBoundingBox.value
            val predictedBoundingBox = inputPredictedBoundingBox.value
            val groundTruthValidationResult = async { useCase(groundTruthBoundingBox) }.await()
            val predictedValidationResult = async { useCase(predictedBoundingBox) }.await()

            parseValidationResult(groundTruthValidationResult, groundTruthErrorMap)
            parseValidationResult(predictedValidationResult, predictedErrorMap)

            if (groundTruthValidationResult.noError() && predictedValidationResult.noError()) {
                Timber.i("No errors found in input data!")
            }
        }
    }

    private fun parseValidationResult(
        validationResult: ValidationResult, errorMap: MutableMap<Coordinate, MutableStateFlow<CustomException?>>
    ) {
        validationResult.errors.forEach { (coordinate, exception) -> errorMap[coordinate]?.update { exception }}
    }

    private fun checkForGeneralError(inputText: Editable?): CustomException? {
        val hasGeneralInputError = inputText.isNullOrBlank()
        return if (hasGeneralInputError) InvalidBoxCoordinateRangeException() else null
    }

    fun processGroundTruthLeftInput(inputText: Editable?) {
        _groundTruthLeftError.update { checkForGeneralError(inputText) }
        _inputGroundTruthBoundingBox.update { _inputGroundTruthBoundingBox.value.copy(left = inputText.toIntOrZero()) }
    }
    fun processGroundTruthTopInput(inputText: Editable?) {
        _groundTruthTopError.update { checkForGeneralError(inputText) }
        _inputGroundTruthBoundingBox.update { _inputGroundTruthBoundingBox.value.copy(top = inputText.toIntOrZero()) }
    }
    fun processGroundTruthRightInput(inputText: Editable?) {
        _groundTruthRightError.update { checkForGeneralError(inputText) }
        _inputGroundTruthBoundingBox.update { _inputGroundTruthBoundingBox.value.copy(right = inputText.toIntOrZero()) }
    }
    fun processGroundTruthBottomInput(inputText: Editable?) {
        _groundTruthBottomError.update { checkForGeneralError(inputText) }
        _inputGroundTruthBoundingBox.update { _inputGroundTruthBoundingBox.value.copy(bottom = inputText.toIntOrZero()) }
    }
    fun processPredictedLeftInput(inputText: Editable?) {
        _predictedLeftError.update { checkForGeneralError(inputText) }
        _inputPredictedBoundingBox.update { _inputPredictedBoundingBox.value.copy(left = inputText.toIntOrZero()) }
    }
    fun processPredictedTopInput(inputText: Editable?) {
        _predictedTopError.update { checkForGeneralError(inputText) }
        _inputPredictedBoundingBox.update { _inputPredictedBoundingBox.value.copy(top = inputText.toIntOrZero()) }
    }
    fun processPredictedRightInput(inputText: Editable?) {
        _predictedRightError.update { checkForGeneralError(inputText) }
        _inputPredictedBoundingBox.update { _inputPredictedBoundingBox.value.copy(right = inputText.toIntOrZero()) }
    }
    fun processPredictedBottomInput(inputText: Editable?) {
        _predictedBottomError.update { checkForGeneralError(inputText) }
        _inputPredictedBoundingBox.update { _inputPredictedBoundingBox.value.copy(bottom = inputText.toIntOrZero()) }
    }
}