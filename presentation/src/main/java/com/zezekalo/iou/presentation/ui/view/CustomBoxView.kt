package com.zezekalo.iou.presentation.ui.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.annotation.StyleableRes
import androidx.core.content.ContextCompat
import com.zezekalo.iou.domain.model.BoundingBox
import com.zezekalo.iou.presentation.R
import timber.log.Timber

class CustomBoxView : View {
    private var boxColorInt: Int? = null
    private var boxPaint: Paint? = null

    private var type: BoxType = BoxType.GROUND_TRUTH

    private var chunksInRow: Int = 0
    private var viewWidth: Int = 0
    private var viewHeight: Int = 0

    private var chunkX: Float = 0f
    private var chunkY: Float = 0f

    private var isInit: Boolean = false

    private var boundingBox: BoundingBox? = null

    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(attrs, defStyle)
    }

    fun setBox(
        boundingBox: BoundingBox,
    ) {
        this.boundingBox = boundingBox
        visibility = VISIBLE
        invalidate()
    }

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
            Timber.i("type: $type")
            @StyleableRes
            val styleableResId: Int = if (type == BoxType.GROUND_TRUTH) {
                R.styleable.CustomBoxView_groundTruthColor
            } else {
                R.styleable.CustomBoxView_predictedColor
            }

            val defColorInt: Int = if (type == BoxType.GROUND_TRUTH) {
                R.color.def_ground_truth_color
            } else {
                R.color.def_predicted_color
            }

            boxColorInt =
                attributes
                    .getColor(
                        styleableResId,
                        ContextCompat.getColor(context, defColorInt),
                    ).also {
                        boxPaint =
                            Paint(Paint.ANTI_ALIAS_FLAG).apply {
                                color = it
                                style = Paint.Style.FILL
                            }
                    }

        } finally {
            attributes.recycle()
        }
    }

    private fun init() {
        viewWidth = width
        viewHeight = height

        chunksInRow = DEFAULT_CHUNK_IN_A_ROWS

        chunkX = viewWidth / (chunksInRow + 2).toFloat()
        chunkY = viewHeight / (chunksInRow + 2).toFloat()

        isInit = true
    }

    override fun onMeasure(
        widthMeasureSpec: Int,
        heightMeasureSpec: Int,
    ) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        val dimen = if (widthSize < heightSize) widthSize else heightSize
        setMeasuredDimension(dimen, dimen)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (!isInit) init()
        requireNotNull(boxPaint) { "boundingBoxPaint is null" }.also {
            drawBoundingBox(canvas, it, boundingBox)
        }
    }

    private fun drawBoundingBox(
        canvas: Canvas,
        paint: Paint,
        box: BoundingBox?,
    ) {
        box?.let {
            val rect =
                RectF(
                    (1 + box.left) * chunkX,
                    (1 + box.top) * chunkY,
                    (1 + box.right) * chunkX,
                    (1 + box.bottom) * chunkY,
                )
            canvas.drawRect(rect, paint)
        }
    }

    enum class BoxType {
        GROUND_TRUTH,
        PREDICTED
    }

    companion object {
        private const val DEFAULT_CHUNK_IN_A_ROWS = 16
    }
}
