package com.zezekalo.iou.data.validator

import com.zezekalo.iou.domain.model.GroundTruthBoundingBox

val invalidLeftBottomBoundingBox =
    GroundTruthBoundingBox(
        left = -1,
        right = 0,
        top = 16,
        bottom = 17,
    )

val validBoundingBox =
    GroundTruthBoundingBox(
        left = 0,
        right = 0,
        top = 16,
        bottom = 16,
    )

val invalidRightAndBottomRangeBoundingBox =
    GroundTruthBoundingBox(
        left = 4,
        right = 3,
        top = 10,
        bottom = -1,
    )
