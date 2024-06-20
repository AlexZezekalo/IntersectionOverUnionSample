package com.zezekalo.iou.presentation.ui.view

import android.content.ClipData
import android.content.ClipDescription
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.view.View.OnLongClickListener
import androidx.annotation.StyleableRes
import androidx.core.content.ContextCompat
import com.zezekalo.iou.presentation.R

class CustomBoxView : View {
    private var boxColorInt: Int? = null
    private var boxPaint: Paint? = null

    var type: BoxType = BoxType.GROUND_TRUTH

    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(attrs, defStyle)
    }

    fun getBoxColor(): Int? = boxColorInt

    private fun init(
        attrs: AttributeSet?,
        defStyle: Int,
    ) {
        val attributes =
            context.obtainStyledAttributes(
                attrs,
                R.styleable.CustomBoxView,
                defStyle,
                0,
            )
        try {
            type = BoxType.entries.toTypedArray()[attributes.getInt(R.styleable.CustomBoxView_type, 0)]
            @StyleableRes
            val styleableResId: Int = when (type) {
                BoxType.GROUND_TRUTH -> R.styleable.CustomBoxView_groundTruthColor
                BoxType.PREDICTED -> R.styleable.CustomBoxView_predictedColor
                BoxType.UNION_BOX -> R.styleable.CustomBoxView_unionColor
            }
            val defColorInt: Int = when (type) {
                BoxType.GROUND_TRUTH -> R.color.def_ground_truth_color
                BoxType.PREDICTED -> R.color.def_predicted_color
                BoxType.UNION_BOX -> R.color.def_union_color
            }
            boxColorInt =
                attributes.getColor(styleableResId, ContextCompat.getColor(context, defColorInt))
                    .also { boxPaint = Paint(Paint.ANTI_ALIAS_FLAG)
                        .apply {
                            color = it
                            style = Paint.Style.FILL
                        }
                    }
        } finally {
            attributes.recycle()
        }
        boxColorInt?.let { background = ColorDrawable(it) }
        if (type != BoxType.UNION_BOX) {
            isLongClickable = true
            setOnLongClickListener(onLongClickListener)
        }
    }

    enum class BoxType {
        GROUND_TRUTH,
        PREDICTED,
        UNION_BOX,
    }

    private val onLongClickListener =
        OnLongClickListener { boxView ->
            val boxTypeName = type.name
            val clipDataItem = ClipData.Item(boxTypeName)
            val clipData = ClipData(boxTypeName, arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN), clipDataItem)

            val dragShadowBuilder = BoxDragShadowBuilder(boxView)
            boxView.startDragAndDrop(clipData, dragShadowBuilder, boxView, 0)
            true
        }

    inner class BoxDragShadowBuilder(view: View) : DragShadowBuilder(view) {

        private val shadow: Drawable? by lazy {
            (view as CustomBoxView).getBoxColor()?.let {
                ColorDrawable(it).also { it.alpha = 0xCC }
            }
        }

        override fun onProvideShadowMetrics(size: Point?, touch: Point?) {
            val width = view.width
            val height = view.height

            shadow?.setBounds(0, 0, width, height)

            size?.set(width, height)

            touch?.set(width / 2, height / 2)
        }

        override fun onDrawShadow(canvas: Canvas) {
            shadow?.draw(canvas)
        }
    }
}