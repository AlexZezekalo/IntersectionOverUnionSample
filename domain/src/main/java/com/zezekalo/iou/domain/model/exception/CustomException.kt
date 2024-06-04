package com.zezekalo.iou.domain.model.exception

open class CustomException(
    open val msg: String = "Custom Message",
) : RuntimeException()

class NotValidBoxInputException(
    override val msg: String = "Not valid box input",
) : CustomException()