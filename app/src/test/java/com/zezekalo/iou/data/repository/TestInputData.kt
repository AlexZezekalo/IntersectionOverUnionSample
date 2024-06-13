package com.zezekalo.iou.data.repository

import com.zezekalo.iou.domain.model.GroundTruthBoundingBox
import com.zezekalo.iou.domain.model.InputData
import com.zezekalo.iou.domain.model.PredictedBoundingBox

val inputDataWithOverlapping: InputData =
    InputData(
        groundTruthBoundingBox =
            GroundTruthBoundingBox(
                left = 3,
                top = 3,
                right = 10,
                bottom = 10,
            ),
        predictedBoundingBox =
            PredictedBoundingBox(
                left = 7,
                top = 7,
                right = 13,
                bottom = 13,
            ),
    )

val congruentInputData: InputData =
    InputData(
        groundTruthBoundingBox =
            GroundTruthBoundingBox(
                left = 3,
                top = 4,
                right = 10,
                bottom = 10,
            ),
        predictedBoundingBox =
            PredictedBoundingBox(
                left = 3,
                top = 4,
                right = 10,
                bottom = 10,
            ),
    )

val nonOverlappingInputData: InputData =
    InputData(
        groundTruthBoundingBox =
            GroundTruthBoundingBox(
                left = 2,
                top = 2,
                right = 6,
                bottom = 6,
            ),
        predictedBoundingBox =
            PredictedBoundingBox(
                left = 10,
                top = 10,
                right = 5,
                bottom = 5,
            ),
    )

val predictedInsideGroundTruthInputData: InputData =
    InputData(
        groundTruthBoundingBox =
            GroundTruthBoundingBox(
                left = 3,
                top = 3,
                right = 10,
                bottom = 10,
            ),
        predictedBoundingBox =
            PredictedBoundingBox(
                left = 4,
                top = 4,
                right = 8,
                bottom = 8,
            ),
    )

val groundTruthInsidePredictedInputData: InputData =
    InputData(
        groundTruthBoundingBox =
            GroundTruthBoundingBox(
                left = 4,
                top = 4,
                right = 8,
                bottom = 8,
            ),
        predictedBoundingBox =
            PredictedBoundingBox(
                left = 3,
                top = 3,
                right = 10,
                bottom = 10,
            ),
    )

val twoLinesAsInputData: InputData =
    InputData(
        groundTruthBoundingBox =
            GroundTruthBoundingBox(
                left = 8,
                top = 4,
                right = 8,
                bottom = 10,
            ),
        predictedBoundingBox =
            PredictedBoundingBox(
                left = 1,
                top = 5,
                right = 10,
                bottom = 5,
            ),
    )

val groundTruthLinePredictedSquareInputData: InputData =
    InputData(
        groundTruthBoundingBox =
            GroundTruthBoundingBox(
                left = 8,
                top = 4,
                right = 8,
                bottom = 10,
            ),
        predictedBoundingBox =
            PredictedBoundingBox(
                left = 1,
                top = 1,
                right = 10,
                bottom = 10,
            ),
    )
