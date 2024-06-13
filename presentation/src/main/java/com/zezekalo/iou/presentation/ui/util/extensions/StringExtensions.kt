package com.zezekalo.iou.presentation.ui.util.extensions

import android.text.Editable

val String.Companion.EMPTY: String
    get() = ""

fun Editable?.toIntOrZero(): Int = this?.toString()?.toIntOrNull() ?: 0
