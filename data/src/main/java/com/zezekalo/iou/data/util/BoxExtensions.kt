package com.zezekalo.iou.data.util

import com.zezekalo.iou.domain.model.BoundingBox

fun BoundingBox.area(): Int = (right - left) * (bottom - top)
