package com.zezekalo.iou.domain.model.exception

import com.zezekalo.iou.domain.model.Coordinate

data class ValidationResult(
    val errors: Map<Coordinate, CustomException?>,
)