package com.zezekalo.iou.domain.model

interface BoundingBox {
    val left: Int
    val top: Int
    val right: Int
    val bottom: Int

    companion object {
        const val INIT_VALUE = 0
    }
}

data class GroundTruthBoundingBox(
    override val left: Int,
    override val top: Int,
    override val right: Int,
    override val bottom: Int,
) : BoundingBox {
    companion object {
        val INIT =
            GroundTruthBoundingBox(
                left = BoundingBox.INIT_VALUE,
                top = BoundingBox.INIT_VALUE,
                right = BoundingBox.INIT_VALUE,
                bottom = BoundingBox.INIT_VALUE,
            )
    }
}

data class PredictedBoundingBox(
    override val left: Int,
    override val top: Int,
    override val right: Int,
    override val bottom: Int,
) : BoundingBox {
    companion object {
        val INIT =
            PredictedBoundingBox(
                left = BoundingBox.INIT_VALUE,
                top = BoundingBox.INIT_VALUE,
                right = BoundingBox.INIT_VALUE,
                bottom = BoundingBox.INIT_VALUE,
            )
    }
}

data class UnionBox(
    override val left: Int,
    override val top: Int,
    override val right: Int,
    override val bottom: Int,
) : BoundingBox {
    companion object {
        val NOT_OVERLAP = UnionBox(0, 0, 0, 0)
    }
}
