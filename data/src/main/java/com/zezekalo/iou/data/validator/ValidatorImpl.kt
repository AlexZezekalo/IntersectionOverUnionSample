package com.zezekalo.iou.data.validator

import com.zezekalo.iou.domain.model.BoundingBox
import com.zezekalo.iou.domain.model.Coordinate
import com.zezekalo.iou.domain.model.exception.*
import com.zezekalo.iou.domain.validation.Validator
import java.util.*
import javax.inject.Inject

class ValidatorImpl @Inject constructor() : Validator {

    override suspend fun validateBoundingBoxCoordinates(boundingBox: BoundingBox): ValidationResult {
        val errors: HashMap<Coordinate, CustomException?> = HashMap(EnumSet.allOf(Coordinate::class.java).associateWith { null })

        validateCoordinateRange(Coordinate.LEFT, boundingBox.left, errors)
        validateCoordinateRange(Coordinate.TOP, boundingBox.top, errors)
        validateCoordinateRange(Coordinate.RIGHT, boundingBox.right, errors)
        validateCoordinateRange(Coordinate.BOTTOM, boundingBox.bottom, errors)

        if (errors[Coordinate.RIGHT] == null) {
            validateRightCoordinate(boundingBox, errors)
        }
        if (errors[Coordinate.BOTTOM] == null) {
            validateBottomCoordinate(boundingBox, errors)
        }
        return ValidationResult(errors)
    }

    private fun validateCoordinateRange(
        coordinate: Coordinate,
        value: Int,
        errors: HashMap<Coordinate, CustomException?>
    ) {
        if (value !in VALID_RANGE) {
            errors[coordinate] = InvalidBoxCoordinateRangeException()
      }
    }

    private fun validateRightCoordinate(boundingBox: BoundingBox, errors: HashMap<Coordinate, CustomException?>) {
        if (boundingBox.right < boundingBox.left) {
            errors[Coordinate.RIGHT] = RightCoordinateNotValidException()
        }
    }

    private fun validateBottomCoordinate(boundingBox: BoundingBox, errors: HashMap<Coordinate, CustomException?>) {
        if (boundingBox.bottom < boundingBox.top) {
            errors[Coordinate.BOTTOM] = BottomCoordinateNotValidException()
        }
    }

    companion object {

        val VALID_RANGE = 0..16
    }
}