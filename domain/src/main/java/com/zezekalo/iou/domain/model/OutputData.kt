package com.zezekalo.iou.domain.model

data class OutputData(
    val inputData: InputData,
    val unionBox: UnionBox,
    val intersectionOverUnion: Float,
)
