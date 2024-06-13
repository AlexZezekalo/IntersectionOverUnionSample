package com.zezekalo.iou.data.repository

import com.zezekalo.iou.data.util.area
import com.zezekalo.iou.domain.model.InputData
import com.zezekalo.iou.domain.model.OutputData
import com.zezekalo.iou.domain.model.UnionBox
import com.zezekalo.iou.domain.repository.ComputationRepository
import javax.inject.Inject
import kotlin.math.max
import kotlin.math.min

class ComputationRepositoryImpl
    @Inject
    constructor() : ComputationRepository {
        override suspend fun computeIntersectionOverUnion(inputData: InputData): OutputData {
            val groundTruthBoundingBox = inputData.groundTruthBoundingBox
            val predictedBoundingBox = inputData.predictedBoundingBox

            val groundTruthArea = groundTruthBoundingBox.area()
            val predictedArea = predictedBoundingBox.area()

            val unionLeft = max(groundTruthBoundingBox.left, predictedBoundingBox.left)
            val unionTop = max(groundTruthBoundingBox.top, predictedBoundingBox.top)
            val unionRight = min(groundTruthBoundingBox.right, predictedBoundingBox.right)
            val unionBottom = min(groundTruthBoundingBox.bottom, predictedBoundingBox.bottom)

            val unionWidth = max(0, unionRight - unionLeft)
            val unionHeight = max(0, unionBottom - unionTop)

            val unionBox =
                if (unionWidth <= 0 || unionHeight <= 0) {
                    UnionBox.NOT_OVERLAP
                } else {
                    UnionBox(
                        left = unionLeft,
                        top = unionTop,
                        right = unionRight,
                        bottom = unionBottom,
                    )
                }
            val summaryArea: Int = (groundTruthArea + predictedArea - unionBox.area())
            return OutputData(
                inputData = inputData,
                unionBox = unionBox,
                intersectionOverUnion = if (summaryArea != 0) unionBox.area() / summaryArea.toFloat() else 0.0f,
            )
        }
    }
