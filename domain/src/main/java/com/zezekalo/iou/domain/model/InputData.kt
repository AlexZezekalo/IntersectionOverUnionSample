package com.zezekalo.iou.domain.model

data class InputData(
    val groundTruthBoundingBox: GroundTruthBoundingBox,
    val predictedBoundingBox: PredictedBoundingBox,
)