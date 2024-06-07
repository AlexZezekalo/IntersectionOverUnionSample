package com.zezekalo.iou.presentation.ui.util.mapper

import android.content.res.Resources
import com.zezekalo.iou.domain.model.exception.BottomCoordinateNotValidException
import com.zezekalo.iou.domain.model.exception.CustomException
import com.zezekalo.iou.domain.model.exception.InvalidBoxCoordinateException
import com.zezekalo.iou.domain.model.exception.RightCoordinateNotValidException
import com.zezekalo.iou.presentation.R

class ThrowableToErrorMessageMapper(val resources: Resources) {

    fun map(throwable: Throwable): String {
        return when (throwable) {
            is CustomException -> getFromCustomException(throwable)
            else -> resources.getString(R.string.generic_error_message)
        }
    }

    private fun getFromCustomException(exception: CustomException): String {
        return when (exception) {
            is InvalidBoxCoordinateException -> resources.getString(R.string.invalid_box_coordinate_error_message)
            is RightCoordinateNotValidException -> resources.getString(R.string.right_coordinate_invalid_error_message)
            is BottomCoordinateNotValidException -> resources.getString(R.string.bottom_coordinate_invalid_error_message)
            else -> resources.getString(R.string.generic_error_message)
        }
    }
}