package com.zezekalo.iou.presentation.ui.view.listener

import android.view.DragEvent
import android.view.View
import timber.log.Timber

class CoordinatePlateDragListener : View.OnDragListener {
    override fun onDrag(
        view: View?,
        dragEvent: DragEvent,
    ): Boolean {
        view ?: return false
        Timber.d("onDrag: view: x = ${view.x}, y = ${view.y}, width = ${view.width}, height = ${view.height}")
        val draggableItem = dragEvent.localState as View
        when (dragEvent.action) {
            DragEvent.ACTION_DRAG_STARTED -> {
                Timber.i("ACTION_DRAG_STARTED")
            }
            DragEvent.ACTION_DRAG_ENTERED -> {
                Timber.d("ACTION_DRAG_ENTERED")
            }
            DragEvent.ACTION_DRAG_LOCATION -> {
                Timber.d("ACTION_DRAG_LOCATION")
            }
            DragEvent.ACTION_DRAG_EXITED -> {
                Timber.d("ACTION_DRAG_EXITED")
            }
            DragEvent.ACTION_DROP -> {
                Timber.w("ACTION_DROP")
            }
            DragEvent.ACTION_DRAG_ENDED -> {
                Timber.w("ACTION_DRAG_ENDED")
            }
            else -> return false
        }
        return true
    }
}
