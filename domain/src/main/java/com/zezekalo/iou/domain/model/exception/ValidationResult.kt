package com.zezekalo.iou.domain.model.exception

import com.zezekalo.iou.domain.model.Coordinate
import java.util.*
import kotlin.collections.HashMap

data class ValidationResult(
    val errors: Map<Coordinate, CustomException?>,
) {
    companion object {
        fun emptyExceptions(): ValidationResult {
            return ValidationResult(
                errors = HashMap(EnumSet.allOf(Coordinate::class.java).associateWith { null })
            )
        }
    }
}

fun ValidationResult.noError() = errors.all { it.value == null }