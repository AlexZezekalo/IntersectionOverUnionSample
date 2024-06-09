package com.zezekalo.iou.domain.usecase

import com.zezekalo.iou.domain.model.BoundingBox
import com.zezekalo.iou.domain.model.exception.ValidationResult
import com.zezekalo.iou.domain.validation.Validator
import javax.inject.Inject

class GetValidationResultUseCase @Inject constructor(
    private val validator: Validator,
) {

    suspend operator fun invoke(boundingBox: BoundingBox): ValidationResult =
        validator.validateBoundingBoxCoordinates(boundingBox)
}