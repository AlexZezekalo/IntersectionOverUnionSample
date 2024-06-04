package com.zezekalo.iou.domain.usecase

import com.zezekalo.iou.domain.model.InputData
import com.zezekalo.iou.domain.model.OutputData
import com.zezekalo.iou.domain.model.exception.CustomException
import com.zezekalo.iou.domain.repository.ComputationRepository
import com.zezekalo.iou.domain.validation.Validator

class GetIntersectionOverUnionUseCase(
    private val computationRepository: ComputationRepository,
    private val validator: Validator,
) {

    suspend operator fun invoke(inputData: InputData): Result<OutputData> {
        return try {
            validator.validateInputData(inputData)
            val outputData = computationRepository.computeIntersectionOverUnion(inputData)
            Result.success(outputData)
        } catch (e: CustomException) {
            Result.failure(e)
        }
    }
}