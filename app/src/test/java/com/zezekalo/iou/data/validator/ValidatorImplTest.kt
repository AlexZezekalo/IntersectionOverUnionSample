package com.zezekalo.iou.data.validator

import com.zezekalo.iou.domain.model.Coordinate
import com.zezekalo.iou.domain.model.exception.InvalidBoxCoordinateRangeException
import com.zezekalo.iou.domain.model.exception.RightCoordinateNotValidException
import com.zezekalo.iou.domain.model.exception.ValidationResult
import com.zezekalo.iou.domain.validation.Validator
import kotlinx.coroutines.runBlocking
import org.junit.Test

import org.junit.Assert.*

class ValidatorImplTest {

    private val validator: Validator = ValidatorImpl()

    @Test
    fun `validate incorrect BoundingBox coordinate range`(): Unit = runBlocking {
        val boundingBox = invalidLeftBottomBoundingBox

        val result = validator.validateBoundingBoxCoordinates(boundingBox)

        assertTrue(result.isSuccess)
        val validationResult: ValidationResult? = result.getOrNull()
        assertNotNull(validationResult)
        val errors = validationResult?.errors
        assertNotNull(errors)

        assertNotNull(errors?.get(Coordinate.LEFT))
        assert(errors?.get(Coordinate.LEFT) is InvalidBoxCoordinateRangeException)

        assertNull(errors?.get(Coordinate.TOP))
        assertNull(errors?.get(Coordinate.RIGHT))

        assertNotNull(errors?.get(Coordinate.BOTTOM))
        assert(errors?.get(Coordinate.BOTTOM) is InvalidBoxCoordinateRangeException)
    }

    @Test
    fun `validate correct BoundingBox coordinates`() = runBlocking {
        val boundingBox = validBoundingBox

        val result = validator.validateBoundingBoxCoordinates(boundingBox)

        assertTrue(result.isSuccess)
        val validationResult: ValidationResult? = result.getOrNull()
        assertNotNull(validationResult)
        val errors = validationResult?.errors
        assertNotNull(errors)

        assertTrue(errors?.all { it.value == null } == true)
    }

    @Test
    fun `validate incorrect Right and Bottom range BoundingBox coordinate`() = runBlocking {
        val boundingBox = invalidRightAndBottomRangeBoundingBox

        val result = validator.validateBoundingBoxCoordinates(boundingBox)

        val errors = result.getOrNull()?.errors

        assertNotNull(errors?.get(Coordinate.RIGHT))
        assert(errors?.get(Coordinate.RIGHT) is RightCoordinateNotValidException)

        assertNotNull(errors?.get(Coordinate.BOTTOM))
        assert(errors?.get(Coordinate.BOTTOM) is InvalidBoxCoordinateRangeException)
    }
}