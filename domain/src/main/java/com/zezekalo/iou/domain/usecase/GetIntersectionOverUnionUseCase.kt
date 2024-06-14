package com.zezekalo.iou.domain.usecase

import com.zezekalo.iou.domain.model.InputData
import com.zezekalo.iou.domain.model.OutputData
import com.zezekalo.iou.domain.model.exception.CustomException
import com.zezekalo.iou.domain.repository.ComputationRepository
import javax.inject.Inject

class GetIntersectionOverUnionUseCase
    @Inject
    constructor(
        private val computationRepository: ComputationRepository,
    ) {
        suspend operator fun invoke(inputData: InputData): Result<OutputData> =
            try {
                val outputData = computationRepository.computeIntersectionOverUnion(inputData)
                Result.success(outputData)
            } catch (e: CustomException) {
                Result.failure(e)
            }
    }
