package com.jvm.arcbackgroundlayout

import android.content.Context
import android.content.res.Configuration
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.widget.FrameLayout

class ArcBackgroundLayout(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
    FrameLayout(context, attrs, defStyleAttr) {
    private val paint = Paint()
    private val path = Path()

    private val defaultCanvasColor = Color.parseColor("#32c5f3")
    private val defaultDrawColor = Color.parseColor("#FFFFFF")

    private var canvasColor = defaultCanvasColor
    private var drawColor = defaultDrawColor
    private var drawArcPointPortrait = 0.4f
    private var drawArcPointLandscape = 0.6f
    private var curveControlPointYPortrait = 3f
    private var curveControlPointYLandscape = 2f

    init {
        paint.isAntiAlias = true
        getAttrs(attrs, defStyleAttr)
    }

    internal constructor(context: Context) : this(context, attrs = null, defStyleAttr = 0)
    internal constructor(context: Context, attrs: AttributeSet?) : this(
        context,
        attrs,
        defStyleAttr = 0
    )

    /**
     * use this carefully
     * only works in onCreate
     * **/
    fun setDrawArcPointPortrait(point : Float){
        drawArcPointPortrait = point
        this.invalidate()
    }

    fun setCurveControlPointYPortrait(point : Float){
        curveControlPointYPortrait = point
        this.invalidate()
    }

    private fun getAttrs(attrs: AttributeSet?, defStyle: Int) {
        val typedArray =
            context.obtainStyledAttributes(attrs, R.styleable.ArcBackgroundLayout, defStyle, 0)
        setTypeArray(typedArray)
    }

    private fun setTypeArray(typedArray: TypedArray) {
        canvasColor =
            typedArray.getColor(R.styleable.ArcBackgroundLayout_canvas_Color, defaultCanvasColor)
        drawColor =
            typedArray.getColor(R.styleable.ArcBackgroundLayout_draw_Color, defaultDrawColor)
        drawArcPointPortrait =
            typedArray.getFloat(R.styleable.ArcBackgroundLayout_drawArcPoint_Portrait, 0.4f)
        drawArcPointLandscape =
            typedArray.getFloat(R.styleable.ArcBackgroundLayout_drawArcPoint_Landscape, 0.6f)
        curveControlPointYPortrait =
            typedArray.getFloat(R.styleable.ArcBackgroundLayout_curveControlPointY_Portrait, 3f)
        curveControlPointYLandscape =
            typedArray.getFloat(R.styleable.ArcBackgroundLayout_curveControlPointY_Landscape, 2f)

        typedArray.recycle()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas == null) {
            return
        }
        // Draw background color
        canvas.drawColor(canvasColor)

        // Draw curve portion of background
        // Setup color
        paint.color = drawColor

        var curveStartAndEndY =
            drawArcPointPortrait * measuredHeight // <------ You can change this value based on your requirement
        // Set curve's control point's Y position (downwards bulge of curve) based on orientation
        var curveControlPointY =
            measuredHeight / curveControlPointYPortrait  // <------ You can change this value based on your requirement
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // Since in landscape mode, the curve will have greater arc length,
            // we need to give the curve more downwards bulge as compared to that in portrait mode.
            curveStartAndEndY =
                drawArcPointLandscape * measuredHeight
            curveControlPointY =
                measuredHeight / curveControlPointYLandscape // <------ You can change this value based on your requirement
        }
        // Now we draw the entire path for curve
        path.moveTo(0f, curveStartAndEndY)
        path.quadTo(
            measuredWidth / 2f,
            curveControlPointY,
            measuredWidth.toFloat(),
            curveStartAndEndY
        )
        path.lineTo(measuredWidth.toFloat(), 0f)
        path.lineTo(0f, 0f)
        path.lineTo(0f, curveStartAndEndY)
        canvas.drawPath(path, paint)
    }
}