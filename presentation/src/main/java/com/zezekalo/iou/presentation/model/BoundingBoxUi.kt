package com.zezekalo.iou.presentation.model

import android.os.Parcelable
import com.zezekalo.iou.domain.model.BoundingBox
import kotlinx.parcelize.Parcelize
import kotlin.reflect.KClass

@Parcelize
data class BoundingBoxUi(
    override val left: Int,
    override val top: Int,
    override val right: Int,
    override val bottom: Int,
) : BoundingBox,
    Parcelable {
    companion object {
        private const val INIT_VALUE: Int = 0
        val EMPTY = BoundingBoxUi(left = INIT_VALUE, top = INIT_VALUE, right = INIT_VALUE, bottom = INIT_VALUE)
    }
}

fun BoundingBoxUi?.orEmpty(): BoundingBoxUi = this ?: BoundingBoxUi.EMPTY

inline fun <reified T : BoundingBox> BoundingBoxUi.toDomain(clazz: KClass<T>): T =
    clazz.constructors.first().call(left, top, right, bottom)

fun BoundingBox.toUi(clazz: KClass<BoundingBoxUi>): BoundingBoxUi =
    clazz.constructors.first().call(left, top, right, bottom)
