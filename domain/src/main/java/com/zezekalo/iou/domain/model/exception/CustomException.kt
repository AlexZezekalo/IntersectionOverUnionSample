package com.zezekalo.iou.domain.model.exception

open class CustomException(
    open val msg: String = "Custom Exception message",
) : RuntimeException()

class InvalidBoxCoordinateRangeException(
    override val msg: String = "Invalid box coordinate",
) : CustomException()

class RightCoordinateNotValidException(
    override val msg: String = "Right coordinate not valid",
) : CustomException()

class BottomCoordinateNotValidException(
    override val msg: String = "Bottom coordinate not valid",
) : CustomException()