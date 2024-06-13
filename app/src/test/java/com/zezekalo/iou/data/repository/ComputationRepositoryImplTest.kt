package com.zezekalo.iou.data.repository

import com.zezekalo.iou.domain.repository.ComputationRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class ComputationRepositoryImplTest {
    private val computationRepository: ComputationRepository = ComputationRepositoryImpl()

    @Test
    fun `compute IntersectionOverUnion with overlap`() {
        val inputData = inputDataWithOverlapping

        val outputData =
            runBlocking {
                computationRepository.computeIntersectionOverUnion(inputData)
            }
        assertEquals(7, outputData.unionBox.left)
        assertEquals(7, outputData.unionBox.top)
        assertEquals(10, outputData.unionBox.right)
        assertEquals(10, outputData.unionBox.bottom)
        assertEquals(0.1184f, outputData.intersectionOverUnion, 0.0001f)
    }

    @Test
    fun `compute IntersectionOverUnion for congruent data`() {
        val inputData = congruentInputData

        val outputData =
            runBlocking {
                computationRepository.computeIntersectionOverUnion(inputData)
            }

        assertEquals(3, outputData.unionBox.left)
        assertEquals(4, outputData.unionBox.top)
        assertEquals(10, outputData.unionBox.right)
        assertEquals(10, outputData.unionBox.bottom)
        assertEquals(1.0f, outputData.intersectionOverUnion, 0.0001f)
    }

    @Test
    fun `compute IntersectionOverUnion with no overlap`() {
        val inputData = nonOverlappingInputData

        val outputData =
            runBlocking {
                computationRepository.computeIntersectionOverUnion(inputData)
            }
        assertEquals(0, outputData.unionBox.left)
        assertEquals(0, outputData.unionBox.top)
        assertEquals(0, outputData.unionBox.right)
        assertEquals(0, outputData.unionBox.bottom)
        assertEquals(0.0f, outputData.intersectionOverUnion, 0.0001f)
    }

    @Test
    fun `compute IntersectionOverUnion when Predicted inside GroundTruth`() {
        val inputData = predictedInsideGroundTruthInputData

        val outputData =
            runBlocking {
                computationRepository.computeIntersectionOverUnion(inputData)
            }
        assertEquals(4, outputData.unionBox.left)
        assertEquals(4, outputData.unionBox.top)
        assertEquals(8, outputData.unionBox.right)
        assertEquals(8, outputData.unionBox.bottom)
        assertEquals(0.3265f, outputData.intersectionOverUnion, 0.0001f)
    }

    @Test
    fun `compute IntersectionOverUnion when GroundTruth inside Predicted`() {
        val inputData = groundTruthInsidePredictedInputData

        val outputData =
            runBlocking {
                computationRepository.computeIntersectionOverUnion(inputData)
            }
        assertEquals(4, outputData.unionBox.left)
        assertEquals(4, outputData.unionBox.top)
        assertEquals(8, outputData.unionBox.right)
        assertEquals(8, outputData.unionBox.bottom)
        assertEquals(0.3265f, outputData.intersectionOverUnion, 0.0001f)
    }

    @Test
    fun `compute IntersectionOverUnion when GroundTruth and Predicted are lines`() {
        val inputData = twoLinesAsInputData

        val outputData =
            runBlocking {
                computationRepository.computeIntersectionOverUnion(inputData)
            }
        assertEquals(0, outputData.unionBox.left)
        assertEquals(0, outputData.unionBox.top)
        assertEquals(0, outputData.unionBox.right)
        assertEquals(0, outputData.unionBox.bottom)
        assertEquals(0.0f, outputData.intersectionOverUnion, 0.0001f)
    }

    @Test
    fun `compute IntersectionOverUnion when GroundTruth is line and Predicted is square`() {
        val inputData = groundTruthLinePredictedSquareInputData

        val outputData =
            runBlocking {
                computationRepository.computeIntersectionOverUnion(inputData)
            }
        assertEquals(0, outputData.unionBox.left)
        assertEquals(0, outputData.unionBox.top)
        assertEquals(0, outputData.unionBox.right)
        assertEquals(0, outputData.unionBox.bottom)
        assertEquals(0.0f, outputData.intersectionOverUnion, 0.0001f)
    }
}
