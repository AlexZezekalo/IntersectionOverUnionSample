package com.zezekalo.iou.log

import com.zezekalo.iou.IouApp.Companion.GLOBAL_TAG
import timber.log.Timber

class CustomDebugTree : Timber.DebugTree() {

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        super.log(priority, "$GLOBAL_TAG: $tag", message, t)
    }

    override fun createStackElementTag(element: StackTraceElement): String {
        return String.format("%s: %s:", super.createStackElementTag(element), element.methodName)
    }
}