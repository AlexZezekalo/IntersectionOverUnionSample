package com.zezekalo.iou.domain.validation

import com.zezekalo.iou.domain.model.InputData

interface Validator {

    fun validateInputData(inputData: InputData)
}