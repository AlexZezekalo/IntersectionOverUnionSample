package com.zezekalo.iou.presentation.ui.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Point
import android.graphics.RectF
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.core.view.children
import com.zezekalo.iou.domain.model.BoundingBox
import com.zezekalo.iou.presentation.R
import com.zezekalo.iou.presentation.model.BoundingBoxUi
import com.zezekalo.iou.presentation.ui.view.listener.CoordinatePlateDragListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class CustomCoordinatePlateView : FrameLayout {
    private var textColorInt: Int? = null
    private var textPaint: Paint? = null

    private var axesColorInt: Int? = null
    private var axesPaint: Paint? = null

    private var graphPaperColorInt: Int? = null
    private var graphPaperPaint: Paint? = null

    private var unionColorInt: Int? = null
    private var unionPaint: Paint? = null

    private var axisTextSize: Int? = null

    private var chunksInRow: Int = 0
    private var viewWidth: Int = 0
    private var viewHeight: Int = 0

    private var chunkX: Float = 0f
    private var chunkY: Float = 0f

    private var isInit: Boolean = false

    private var groundTruthBoundingBox: BoundingBoxUi? = null
    private var predictedBoundingBox: BoundingBoxUi? = null
    private var unionBox: BoundingBoxUi? = null

    private val _groundTruthBoundingBoxFlow = MutableStateFlow<BoundingBoxUi?>(null)
    val groundTruthBoundingBoxFlow = _groundTruthBoundingBoxFlow.asStateFlow()

    private val _predictedBoundingBoxFlow = MutableStateFlow<BoundingBoxUi?>(null)
    val predictedBoundingBoxFlow = _predictedBoundingBoxFlow.asStateFlow()

    private val groundTruthBoundingBoxColor: ColorDrawable = ColorDrawable()
    private val predictedBoundingBoxColor: ColorDrawable = ColorDrawable()

    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(attrs, defStyle)
    }

    fun setBoxes(
        groundTruthBoundingBox: BoundingBoxUi,
        predictedBoundingBox: BoundingBoxUi,
        unionBox: BoundingBoxUi?,
    ) {
        this.groundTruthBoundingBox = groundTruthBoundingBox
        this.predictedBoundingBox = predictedBoundingBox
        this.unionBox = unionBox
        requestLayout()
    }

    private fun init(
        attrs: AttributeSet?,
        defStyle: Int,
    ) {
        val attributes =
            context.obtainStyledAttributes(
                attrs,
                R.styleable.CustomCoordinatePlateView,
                defStyle,
                0,
            )
        try {
            axesColorInt =
                attributes
                    .getColor(
                        R.styleable.CustomCoordinatePlateView_axesColor,
                        ContextCompat.getColor(context, R.color.def_axes_color),
                    ).also {
                        axesPaint =
                            Paint(Paint.ANTI_ALIAS_FLAG).apply {
                                color = it
                                style = Paint.Style.FILL
                                strokeWidth = 8f
                            }
                    }

            graphPaperColorInt =
                attributes
                    .getColor(
                        R.styleable.CustomCoordinatePlateView_graphColor,
                        ContextCompat.getColor(context, R.color.def_graph_color),
                    ).also {
                        graphPaperPaint =
                            Paint(Paint.ANTI_ALIAS_FLAG).apply {
                                color = it
                                style = Paint.Style.FILL_AND_STROKE
                                strokeWidth = 2f
                            }
                    }

            unionColorInt =
                attributes
                    .getColor(
                        R.styleable.CustomCoordinatePlateView_unionColor,
                        ContextCompat.getColor(context, R.color.def_union_color),
                    ).also {
                        unionPaint =
                            Paint(Paint.ANTI_ALIAS_FLAG).apply {
                                color = it
                                style = Paint.Style.FILL
                            }
                    }

            axisTextSize =
                attributes.getDimensionPixelSize(
                    R.styleable.CustomGraphView_axisTextSize,
                    resources.getDimensionPixelSize(R.dimen.def_axis_text_size),
                )

            textColorInt =
                attributes
                    .getColor(
                        R.styleable.CustomCoordinatePlateView_axesTextColor,
                        ContextCompat.getColor(context, R.color.def_text_color),
                    ).also {
                        textPaint =
                            Paint(Paint.ANTI_ALIAS_FLAG).apply {
                                color = it
                                style = Paint.Style.FILL
                                textAlign = Paint.Align.CENTER
                                textSize = axisTextSize!!.toFloat()
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
        for (child in children) {
            measureChild(child, widthMeasureSpec, heightMeasureSpec)
        }

        val dimen = if (widthSize < heightSize) widthSize else heightSize
        setMeasuredDimension(dimen, dimen)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        requireNotNull(axesPaint) { "axesPaint is null" }.also {
            drawAxis(canvas, it)
        }
        requireNotNull(textPaint) { "textPaint is null" }.also {
            drawTextOnXaxis(canvas, it)
            drawTextOnYaxis(canvas, it)
        }
        requireNotNull(unionPaint) { "unionPaint is null" }.also {
            drawBoundingBox(canvas, it, unionBox)
        }
        requireNotNull(graphPaperPaint) { "graphPaint is null" }.also {
            drawGraphPaper(canvas, it)
        }
    }

    private fun drawAxis(
        canvas: Canvas,
        paint: Paint,
    ) {
        // x-axis
        canvas.drawLine(chunkX, chunkY, viewWidth - chunkX, chunkY, paint)
        // y-axis
        canvas.drawLine(chunkX, chunkY, chunkX, viewHeight - chunkY, paint)
    }

    private fun drawGraphPaper(
        canvas: Canvas,
        paint: Paint,
    ) {
        var cx = chunkX * 2f
        var cy = chunkY
        // vertical
        repeat(chunksInRow) {
            canvas.drawLine(cx, cy, cx, viewHeight - cy, paint)
            cx += chunkX
        }
        cy = chunkY * 2f
        cx = chunkX
        // horizontal
        repeat(chunksInRow) {
            canvas.drawLine(cx, cy, viewWidth - cx, cy, paint)
            cy += chunkY
        }
    }

    private fun drawTextOnXaxis(
        canvas: Canvas,
        paint: Paint,
    ) {
        var cx = chunkX
        val cy = chunkY * 0.75f
        // horizontal
        repeat(chunksInRow + 1) {
            canvas.drawText(it.toString(), cx, cy, paint)
            cx += chunkX
        }
    }

    private fun drawTextOnYaxis(
        canvas: Canvas,
        paint: Paint,
    ) {
        val cx = chunkX * 0.5f
        var cy = chunkY * 1.1f
        // vertical
        repeat(chunksInRow + 1) {
            canvas.drawText(it.toString(), cx, cy, paint)
            cy += chunkY
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

    override fun onLayout(
        changed: Boolean,
        left: Int,
        top: Int,
        right: Int,
        bottom: Int,
    ) {
        if (!isInit) init()
        for (child in children) {
            val boxType = (child as CustomBoxView).type
            val box = if (boxType == CustomBoxView.BoxType.GROUND_TRUTH) {
                    groundTruthBoundingBox
                } else {
                    predictedBoundingBox
                }
            if (child.visibility == View.GONE || box == null) continue
            val boxWidth = ((box.right - box.left) * chunkX).toInt()
            val boxHeight = ((box.bottom - box.top) * chunkY).toInt()
            child.measure(
                MeasureSpec.makeMeasureSpec(boxWidth, MeasureSpec.AT_MOST),
                MeasureSpec.makeMeasureSpec(boxHeight, MeasureSpec.AT_MOST),
            )
            child.layout(
                (1 + box.left) * chunkX.toInt(),
                (1 + box.top) * chunkY.toInt(),
                (1 + box.right) * chunkX.toInt(),
                (1 + box.bottom) * chunkY.toInt(),
            )
            child.x = (1 + box.left) * chunkX
            child.y = (1 + box.top) * chunkY
            child.getBoxColor()?.let { color ->
                val boxColor = if (boxType == CustomBoxView.BoxType.GROUND_TRUTH) {
                    groundTruthBoundingBoxColor
                } else {
                    predictedBoundingBoxColor
                }
                child.background = boxColor.also { it.color = color }
            }
        }
    }

    fun updateBoxLocation(type: CustomBoxView.BoxType, leftTopPoint: Point, rightBottom: Point) {
        val boundingBoxUi = getValidBoxCoordinates(leftTopPoint, rightBottom)
        if (type == CustomBoxView.BoxType.GROUND_TRUTH) {
            _groundTruthBoundingBoxFlow.tryEmit(boundingBoxUi)
        } else {
            _predictedBoundingBoxFlow.tryEmit(boundingBoxUi)
        }
    }

    private fun getValidBoxCoordinates(leftTopPoint: Point, rightBottomPoint: Point):
            BoundingBoxUi {
        var left: Int = Math.round(leftTopPoint.x/chunkX - 1)
        var top: Int = Math.round(leftTopPoint.y/chunkY - 1)
        var right: Int = Math.round(rightBottomPoint.x/chunkX - 1)
        var bottom: Int = Math.round(rightBottomPoint.y/chunkY - 1)
        val width = (rightBottomPoint.x - leftTopPoint.x)/chunkX.toInt()
        val height = (rightBottomPoint.y - leftTopPoint.y)/chunkY.toInt()
        if (left < 0) {
            left = 0
            right = width
        }
        if (top < 0) {
            top = 0
            bottom = width
        }
        if (right > DEFAULT_CHUNK_IN_A_ROWS) {
            right = DEFAULT_CHUNK_IN_A_ROWS
            left = DEFAULT_CHUNK_IN_A_ROWS - width
        }
        if (bottom > DEFAULT_CHUNK_IN_A_ROWS) {
            bottom = DEFAULT_CHUNK_IN_A_ROWS
            top = DEFAULT_CHUNK_IN_A_ROWS - height
        }
        return BoundingBoxUi(left, top, right, bottom)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        setOnDragListener(CoordinatePlateDragListener(this))
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        setOnDragListener(null)
    }

    companion object {
        private const val DEFAULT_CHUNK_IN_A_ROWS = 16
    }
}