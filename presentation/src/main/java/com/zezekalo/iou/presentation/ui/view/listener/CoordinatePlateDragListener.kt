package com.zezekalo.iou.presentation.ui.view.listener

import android.graphics.Point
import android.view.DragEvent
import android.view.View
import com.zezekalo.iou.presentation.ui.view.CustomBoxView
import com.zezekalo.iou.presentation.ui.view.CustomCoordinatePlateView
import timber.log.Timber

class CoordinatePlateDragListener(
    private val coordinatePlate: CustomCoordinatePlateView
) : View.OnDragListener {
    override fun onDrag(
        view: View?,
        dragEvent: DragEvent,
    ): Boolean {
        view ?: return false
        Timber.d("onDrag: view: ${view.javaClass.simpleName} x = ${view.x}, y = ${view.y}, width = ${view
            .width}, height = ${view.height}")
        Timber.i("onDrag: dragEvent: x = ${dragEvent.x}, y = ${dragEvent.y}, result = ${dragEvent
            .result}")
        val draggableItem = dragEvent.localState as CustomBoxView
        Timber.e("draggableItem: ${draggableItem.javaClass.simpleName}; type: ${draggableItem.type}")
        when (dragEvent.action) {
            DragEvent.ACTION_DRAG_STARTED -> {
                Timber.i("ACTION_DRAG_STARTED")
            }
            DragEvent.ACTION_DRAG_ENTERED -> {
                Timber.d("ACTION_DRAG_ENTERED")
            }
            DragEvent.ACTION_DRAG_LOCATION -> {
                Timber.d("ACTION_DRAG_LOCATION")
                draggableItem.visibility = View.INVISIBLE
            }
            DragEvent.ACTION_DRAG_EXITED -> {
                Timber.d("ACTION_DRAG_EXITED")
            }
            DragEvent.ACTION_DROP -> {
                Timber.w("ACTION_DROP: draggableItem: x: ${draggableItem.x}; y: ${draggableItem
                    .y}; width = ${draggableItem.width}; height = ${draggableItem.height}; left =" +
                        " ${draggableItem.left}; top = ${draggableItem.top}; right = ${draggableItem.right}; bottom = ${draggableItem.bottom}")
                val left = draggableItem.x.toInt()
                val top = draggableItem.y.toInt()
                val right = left + draggableItem.width
                val bottom = top + draggableItem.height
                Timber.w("ACTION_DROP: left = $left; top = $top; right = $right; bottom = $bottom")

                draggableItem.x = dragEvent.x - draggableItem.width/2
                draggableItem.y = dragEvent.y - draggableItem.height/2
                if (draggableItem.type == CustomBoxView.BoxType.GROUND_TRUTH) {
                    (draggableItem.parent as CustomCoordinatePlateView).apply {
                        removeViewAt(0)
                        addView(draggableItem, 0)
                    }
                } else {
                    (draggableItem.parent as CustomCoordinatePlateView).apply {
                        removeViewAt(1)
                        addView(draggableItem,1)
                    }
                }
                coordinatePlate.updateBoxLocation(draggableItem.type, Point(left, top), Point
                    (right, bottom))
            }
            DragEvent.ACTION_DRAG_ENDED -> {
                Timber.w("ACTION_DRAG_ENDED")
                draggableItem.visibility = View.VISIBLE
                view.invalidate()
            }
            else -> return false
        }
        return true
    }
}
