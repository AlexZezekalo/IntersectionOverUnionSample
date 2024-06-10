package com.zezekalo.iou.data.validator

import com.zezekalo.iou.domain.model.Coordinate
import com.zezekalo.iou.domain.model.exception.InvalidBoxCoordinateRangeException
import com.zezekalo.iou.domain.model.exception.RightCoordinateNotValidException
import com.zezekalo.iou.domain.validation.Validator
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test

class ValidatorImplTest {

    private val validator: Validator = ValidatorImpl()

    @Test
    fun `validate incorrect BoundingBox coordinate range`(): Unit = runBlocking {
        val boundingBox = invalidLeftBottomBoundingBox

        val result = validator.validateBoundingBoxCoordinates(boundingBox)

        val errors = result.errors
        assertNotNull(errors)

        assertNotNull(errors[Coordinate.LEFT])
        assert(errors[Coordinate.LEFT] is InvalidBoxCoordinateRangeException)

        assertNull(errors[Coordinate.TOP])
        assertNull(errors[Coordinate.RIGHT])

        assertNotNull(errors[Coordinate.BOTTOM])
        assert(errors[Coordinate.BOTTOM] is InvalidBoxCoordinateRangeException)
    }

    @Test
    fun `validate correct BoundingBox coordinates`() = runBlocking {
        val boundingBox = validBoundingBox

        val result = validator.validateBoundingBoxCoordinates(boundingBox)

        val errors = result.errors
        assertNotNull(errors)

        assertTrue(errors.all { it.value == null })
    }

    @Test
    fun `validate incorrect Right and Bottom range BoundingBox coordinate`() = runBlocking {
        val boundingBox = invalidRightAndBottomRangeBoundingBox

        val result = validator.validateBoundingBoxCoordinates(boundingBox)

        val errors = result.errors

        assertNotNull(errors[Coordinate.RIGHT])
        assert(errors[Coordinate.RIGHT] is RightCoordinateNotValidException)

        assertNotNull(errors[Coordinate.BOTTOM])
        assert(errors[Coordinate.BOTTOM] is InvalidBoxCoordinateRangeException)
    }
}