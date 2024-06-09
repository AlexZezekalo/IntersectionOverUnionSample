package com.zezekalo.iou.presentation.model

import android.os.Parcelable
import com.zezekalo.iou.domain.model.GroundTruthBoundingBox
import com.zezekalo.iou.domain.model.InputData
import com.zezekalo.iou.domain.model.PredictedBoundingBox
import kotlinx.parcelize.Parcelize

@Parcelize
data class InputDataUi(
    val groundTruthBoundingBox: BoundingBoxUi,
    val predictedBoundingBox: BoundingBoxUi,
) : Parcelable {

    companion object {

        val INITIAL = InputDataUi(
            groundTruthBoundingBox = BoundingBoxUi.EMPTY,
            predictedBoundingBox = BoundingBoxUi.EMPTY
        )
    }
}

fun InputDataUi.toDomain(): InputData {
    return InputData(
        groundTruthBoundingBox = groundTruthBoundingBox.toDomain(GroundTruthBoundingBox::class),
        predictedBoundingBox = predictedBoundingBox.toDomain(PredictedBoundingBox::class)
    )
}