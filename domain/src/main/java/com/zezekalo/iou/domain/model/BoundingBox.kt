package com.zezekalo.iou.domain.model

interface BoundingBox {
    val left: Int
    val top: Int
    val right: Int
    val bottom: Int
}

data class GroundTruthBoundingBox(
    override val left: Int,
    override val top: Int,
    override val right: Int,
    override val bottom: Int,
): BoundingBox

data class PredictedBoundingBox(
    override val left: Int,
    override val top: Int,
    override val right: Int,
    override val bottom: Int,
): BoundingBox

data class UnionBox(
    override val left: Int,
    override val top: Int,
    override val right: Int,
    override val bottom: Int,
): BoundingBox {

    companion object {
        val NOT_OVERLAP = UnionBox(0, 0, 0, 0)
    }
}