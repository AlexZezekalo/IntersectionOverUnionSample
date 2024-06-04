package com.zezekalo.iou.domain.repository

import com.zezekalo.iou.domain.model.InputData
import com.zezekalo.iou.domain.model.OutputData


interface ComputationRepository {

    suspend fun computeIntersectionOverUnion(inputData: InputData): OutputData
}