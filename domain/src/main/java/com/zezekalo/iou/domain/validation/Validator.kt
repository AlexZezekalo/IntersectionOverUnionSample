package com.zezekalo.iou.domain.validation

import com.zezekalo.iou.domain.model.BoundingBox
import com.zezekalo.iou.domain.model.exception.ValidationResult

interface Validator {

    suspend fun validateBoundingBoxCoordinates(boundingBox: BoundingBox): ValidationResult
}