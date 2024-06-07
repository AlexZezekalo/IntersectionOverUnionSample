package com.zezekalo.iou.domain.model

data class InputData(
    val groundTruthBoundingBox: GroundTruthBoundingBox,
    val predictedBoundingBox: PredictedBoundingBox,
) {
    companion object {
        val EMPTY = InputData(
            groundTruthBoundingBox = GroundTruthBoundingBox.INIT,
            predictedBoundingBox = PredictedBoundingBox.INIT,
        )
    }
}
