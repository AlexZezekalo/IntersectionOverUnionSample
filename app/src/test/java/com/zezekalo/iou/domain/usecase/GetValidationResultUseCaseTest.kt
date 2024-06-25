package com.zezekalo.iou.domain.usecase

import com.zezekalo.iou.domain.model.Coordinate
import com.zezekalo.iou.domain.model.GroundTruthBoundingBox
import com.zezekalo.iou.domain.model.exception.BottomCoordinateNotValidException
import com.zezekalo.iou.domain.model.exception.CustomException
import com.zezekalo.iou.domain.model.exception.ValidationResult
import com.zezekalo.iou.domain.model.exception.noError
import com.zezekalo.iou.domain.validation.Validator
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.Rule
import org.junit.Test
import java.util.*

class GetValidationResultUseCaseTest {
    @get:Rule
    val mockkRule = MockKRule(this)

    @RelaxedMockK
    lateinit var validator: Validator

    private lateinit var useCase: GetValidationResultUseCase


    @Test
    fun `test valid inputData usecase response`() = runTest {
        coEvery { validator.validateBoundingBoxCoordinates(any()) } returns ValidationResult.emptyExceptions()

        useCase = GetValidationResultUseCase(validator)
        val validationResult = useCase(validBoundingBox)

        coVerify { validator.validateBoundingBoxCoordinates(validBoundingBox) }
        confirmVerified(validator)

        assertThat(validationResult.noError(), equalTo(true))
    }

    @Test
    fun `test invalid inputData usecase response`() = runTest {
        val validationResultWithException = getValidationResultWithException()

        coEvery { validator.validateBoundingBoxCoordinates(any()) } returns validationResultWithException

        useCase = GetValidationResultUseCase(validator)
        val validationResult = useCase(validBoundingBox)

        assertThat(validationResult.errors.any { it.value != null }, equalTo(true))
    }

    private fun getValidationResultWithException(): ValidationResult {
        val errors: MutableMap<Coordinate, CustomException?> = EnumMap(Coordinate::class.java)
        errors[Coordinate.LEFT] = null
        errors[Coordinate.TOP] = null
        errors[Coordinate.RIGHT] = null
        errors[Coordinate.BOTTOM] = BottomCoordinateNotValidException()
        return ValidationResult(errors)
    }

}

private val validBoundingBox = GroundTruthBoundingBox.INIT
