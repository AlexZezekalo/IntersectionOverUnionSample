package com.zezekalo.iou.presentation.ui.util.extensions

fun Int.showBoxCoordinate(description: String): String {
    val value = if (this < 0) "< >" else this.toString()
    return "$description $value"
}

fun Float?.showIoU(description: String): String = if (this != null) "$description ${"""%.3f""".format(this)}" else description
