package com.zezekalo.iou.presentation.ui.util.extensions

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.NavDirections

fun NavController.safeNavigateWithArgs(
    direction: NavDirections,
    bundle: Bundle?,
) {
    currentDestination?.getAction(direction.actionId)?.run {
        navigate(direction.actionId, bundle)
    }
}
