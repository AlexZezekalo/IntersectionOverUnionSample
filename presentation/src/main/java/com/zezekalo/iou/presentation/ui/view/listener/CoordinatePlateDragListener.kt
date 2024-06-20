package com.zezekalo.iou.presentation.ui.view.listener

import android.graphics.Point
import android.view.DragEvent
import android.view.View
import com.zezekalo.iou.presentation.ui.view.CustomBoxView
import com.zezekalo.iou.presentation.ui.view.CustomCoordinatePlateView

class CoordinatePlateDragListener(
    private val coordinatePlate: CustomCoordinatePlateView
) : View.OnDragListener {

    override fun onDrag(view: View?, dragEvent: DragEvent): Boolean {
        view ?: return false
        val draggableItem = dragEvent.localState as CustomBoxView

        when (dragEvent.action) {
            DragEvent.ACTION_DRAG_STARTED -> {
                draggableItem.visibility = View.INVISIBLE
            }
            DragEvent.ACTION_DRAG_ENTERED -> { }
            DragEvent.ACTION_DRAG_LOCATION -> { }
            DragEvent.ACTION_DRAG_EXITED -> { }
            DragEvent.ACTION_DROP -> {
                val left = dragEvent.x - draggableItem.width/2
                val top = dragEvent.y - draggableItem.height/2
                val right = left + draggableItem.width
                val bottom = top + draggableItem.height

                coordinatePlate.updateBoxLocation(draggableItem.type, Point(left.toInt(), top.toInt()),
                    Point(right.toInt(), bottom.toInt()))
            }
            DragEvent.ACTION_DRAG_ENDED -> {
                draggableItem.visibility = View.VISIBLE
                view.invalidate()
            }
            else -> return false
        }
        return true
    }
}
