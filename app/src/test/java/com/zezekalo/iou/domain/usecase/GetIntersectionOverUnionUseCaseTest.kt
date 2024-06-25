package com.zezekalo.iou.domain.usecase

import com.zezekalo.iou.domain.model.GroundTruthBoundingBox
import com.zezekalo.iou.domain.model.InputData
import com.zezekalo.iou.domain.model.OutputData
import com.zezekalo.iou.domain.model.PredictedBoundingBox
import com.zezekalo.iou.domain.model.UnionBox
import com.zezekalo.iou.domain.model.exception.RightCoordinateNotValidException
import com.zezekalo.iou.domain.repository.ComputationRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.instanceOf
import org.junit.Rule
import org.junit.Test

class GetIntersectionOverUnionUseCaseTest {
    @get:Rule
    val mockkRule = MockKRule(this)

    @RelaxedMockK
    lateinit var computationRepository: ComputationRepository

    private lateinit var useCase: GetIntersectionOverUnionUseCase

    @Test
    fun `test valid inputData usecase response`() = runTest {
        val inputData = validTestInputData
        coEvery { computationRepository.computeIntersectionOverUnion(inputData) } returns validOutputData

        useCase = GetIntersectionOverUnionUseCase(computationRepository)
        val result = useCase(validTestInputData)

        coVerify { computationRepository.computeIntersectionOverUnion(inputData) }
        confirmVerified(computationRepository)

        assertThat(result.isSuccess, equalTo(true))
        assertThat(result.getOrNull(), equalTo(validOutputData))
    }

    @Test
    fun `test invalid inputData usecase response`() = runTest {
        coEvery {
            computationRepository.computeIntersectionOverUnion(any())
        } coAnswers  {
            throw RightCoordinateNotValidException()
        }
        useCase = GetIntersectionOverUnionUseCase(computationRepository)
        val result = useCase(invalidTestInputData)

        coVerify { computationRepository.computeIntersectionOverUnion(invalidTestInputData) }
        confirmVerified(computationRepository)

        assertThat(result.isFailure, equalTo(true))
        assertThat(result.exceptionOrNull(), instanceOf(RightCoordinateNotValidException::class.java))
    }
}

private val validTestInputData = InputData(
    groundTruthBoundingBox = GroundTruthBoundingBox(3,3,10,10),
    predictedBoundingBox = PredictedBoundingBox(7,7, 13, 13)
)

private val validOutputData = OutputData (
    inputData = validTestInputData,
    unionBox = UnionBox(7,7, 10, 10),
    intersectionOverUnion = 0.1184f
)

private val invalidTestInputData = InputData(
    groundTruthBoundingBox = GroundTruthBoundingBox(3,3,2,4),
    predictedBoundingBox = PredictedBoundingBox(7,7, 5, 10)
)