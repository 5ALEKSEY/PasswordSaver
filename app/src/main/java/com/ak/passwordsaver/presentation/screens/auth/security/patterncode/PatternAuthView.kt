package com.ak.passwordsaver.presentation.screens.auth.security.patterncode

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.SparseArray
import android.view.MotionEvent
import android.view.animation.AnimationUtils
import android.widget.RelativeLayout
import collections.forEach
import com.ak.passwordsaver.R
import com.ak.passwordsaver.utils.extensions.dpToPx
import com.ak.passwordsaver.utils.extensions.getColorCompat
import com.ak.passwordsaver.utils.extensions.vibrate
import kotlin.math.abs
import kotlin.math.roundToInt
import kotlin.math.sqrt

class PatternAuthView(context: Context?, attrs: AttributeSet?) : RelativeLayout(context, attrs) {

    companion object DefaultValues {
        private const val LINE_WIDTH = 12F
        private const val PATTERN_VIEW_SIZE = 256F
        private const val PATTERN_VIEW_NODES_OFFSET = PATTERN_VIEW_SIZE * 0.1.toFloat()
        private const val NODES_INVOKE_OFFSET_RADIUS = 20F
        /**
         * Only 4, 9, 16 ... For sqrt() correct invoke.
         * [mNodesCodesList] should be changed after [NODES_NUMBER] value changes.
         */
        private const val NODES_NUMBER = 9
        private const val FINISH_RESET_DELAY_IN_MILLIS = 1000L
        private const val NODE_SELECT_VIBRATION_DELAY_IN_MILLIS = 30L
        private const val FAILED_AUTH_VIBRATION_DELAY_IN_MILLIS = FINISH_RESET_DELAY_IN_MILLIS / 3
        private const val IS_VIBRATION_NEEDS = true
    }

    //-------------------------------------- values for init ---------------------------------------
    private var mLineWidth = 0F
    private var mIsVibrationNeeds = false
    private var mNodesNumber = 0
    //----------------------------------------------------------------------------------------------


    private val mDefaultLineColor by lazy { context!!.getColorCompat(R.color.default_pattern_line_color) }
    private val mFailedLineColor by lazy { context!!.getColorCompat(R.color.failed_pattern_line_color) }
    private val mBackgroundColor by lazy { context!!.getColorCompat(R.color.security_background_color) }

    lateinit var mOnFinishedAction: (patternResultCode: String) -> Unit

    private var mBitmap: Bitmap = Bitmap.createBitmap(
        PATTERN_VIEW_SIZE.dpToPx(),
        PATTERN_VIEW_SIZE.dpToPx(),
        Bitmap.Config.ARGB_8888
    )
    private var mCanvas: Canvas
    private val mPaint = Paint()
    private val mBitmapPaint = Paint(Paint.DITHER_FLAG)
    private lateinit var mPath: Path

    private val mLinePaths = arrayListOf<PatternLinePath>()
    private val mNodesMap = SparseArray<PatternNodeData>(mNodesNumber)
    private val mNodesCodesList =
        arrayListOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f")
    private val mInvokedNodesNumbers = arrayListOf<Int>()

    private var mIsAuthStarted = false
    private var mIsPatternInputLocked = false

    private var mX: Float = 0F
    private var mY: Float = 0F

    init {
        initViewAttributes(attrs)
        setWillNotDraw(false)
        mCanvas = Canvas(mBitmap)
        // init paint
        mPaint.apply {
            isAntiAlias = true
            isDither = true
            color = mDefaultLineColor
            style = Paint.Style.STROKE
            strokeJoin = Paint.Join.ROUND
            strokeCap = Paint.Cap.ROUND
            xfermode = null
            alpha = 0xFF
        }
        addNodeViews()
        clearAndReset()
    }

    private fun initViewAttributes(attrs: AttributeSet?) {
        val typedArray = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.PatternAuthViewStyleable,
            0, 0
        )

        try {
            mIsVibrationNeeds = typedArray.getBoolean(
                R.styleable.PatternAuthViewStyleable_vibration,
                IS_VIBRATION_NEEDS
            )
            mLineWidth = typedArray.getFloat(
                R.styleable.PatternAuthViewStyleable_patternLineWidth,
                LINE_WIDTH
            )
            mNodesNumber = typedArray.getInt(
                R.styleable.PatternAuthViewStyleable_nodesNumber,
                NODES_NUMBER
            )
        } finally {
            typedArray.recycle()
        }
    }

    fun setFailedAuthViewState() {
        startAuthFailedAnimation()
    }

    fun setAuthViewInputLockState(isLocked: Boolean) {
        mIsPatternInputLocked = isLocked
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event == null || mIsPatternInputLocked) {
            return false
        }
        val x = event.x
        val y = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> handleCoordinateForNodeAdd(x, y)
            MotionEvent.ACTION_UP -> {
                if (mIsAuthStarted) {
                    mLinePaths.removeAt(mLinePaths.size - 1)
                    onAuthFinished()
                }
            }
            MotionEvent.ACTION_MOVE -> handleMoveAction(x, y)
        }

        invalidate()
        return true
    }

    override fun onDraw(canvas: Canvas?) {
        if (canvas == null) {
            super.onDraw(canvas)
            return
        }

        canvas.save()
        mCanvas.drawColor(mBackgroundColor)

        mLinePaths.forEach {
            mPaint.color = it.color
            mPaint.strokeWidth = mLineWidth
            mCanvas.drawPath(it.path, mPaint)
        }

        canvas.drawBitmap(mBitmap, 0f, 0f, mBitmapPaint)
        canvas.restore()
    }

    private fun addNodeViews() {
        val marginsCount = calculateNodesMarginsCount()
        val nodesMarginSize = calculateNodesMarginDpSize(marginsCount)
        val nodeViewPxSize = resources.getDimensionPixelSize(R.dimen.pattern_code_node_size)
        // node is circle (offset of x,y coordinates should be like circle radius)
        val nodesCoordinatesPxOffset = nodeViewPxSize / 2

        var nodeNumber = -1
        for (i in 0..marginsCount) {
            for (j in 0..marginsCount) {
                nodeNumber++

                val patternNodeView = PatternCodeNodeView(context, null)
                val params = LayoutParams(nodeViewPxSize, nodeViewPxSize)

                val x = calculateNodeViewCoordinate(nodesMarginSize, i)
                val y = calculateNodeViewCoordinate(nodesMarginSize, j)

                params.leftMargin = x - nodesCoordinatesPxOffset
                params.topMargin = y - nodesCoordinatesPxOffset

                mNodesMap.put(nodeNumber, PatternNodeData(patternNodeView, x, y))

                addView(patternNodeView, params)
            }
        }
    }

    private fun calculateNodeViewCoordinate(nodesMarginSize: Float, marginsCount: Int) =
        (PATTERN_VIEW_NODES_OFFSET + nodesMarginSize * marginsCount).dpToPx()

    private fun calculateNodesMarginDpSize(marginsCount: Int) =
        (PATTERN_VIEW_SIZE - 2 * PATTERN_VIEW_NODES_OFFSET) / marginsCount

    private fun calculateNodesCountInLine(): Int {
        return sqrt(mNodesNumber.toFloat()).toInt()
    }

    private fun calculateNodesMarginsCount(): Int {
        val nodesInLine = calculateNodesCountInLine()
        return nodesInLine - 1
    }

    private fun handleMoveAction(x: Float, y: Float) {
        if (mIsAuthStarted) {
            drawLineTo(x, y)
            // check nodes and path line intersection
            checkNodesOnPathLineAndStart(mX, mY, x, y)
            // check node intersection with current position
            handleCoordinateForNodeAdd(x, y)
        }
    }

    private fun drawLineTo(x: Float, y: Float) {
        if (this::mPath.isInitialized) {
            mPath.reset()
            mPath.moveTo(mX, mY)
            mPath.lineTo(x, y)
        }
    }

    private fun checkNodesOnPathLineAndStart(x1: Float, y1: Float, x2: Float, y2: Float) {
        var dx = x2 - x1
        var dy = y2 - y1

        val dxAbs = abs(dx)
        val dyAbs = abs(dy)
        val step = if (dxAbs >= dyAbs) {
            dxAbs
        } else {
            dyAbs
        }

        dx /= step
        dy /= step

        var x = x1
        var y = y1
        for (i in 1..step.roundToInt()) {
            if (handleCoordinateForNodeAdd(x, y)) {
                return
            }
            x += dx
            y += dy
        }
    }

    // return true if new node invoked, otherwise false
    private fun handleCoordinateForNodeAdd(x: Float, y: Float): Boolean {
        val invokedNodeNumber = getInvokedNodeNumber(x, y) ?: return false
        if (mInvokedNodesNumbers.contains(invokedNodeNumber)) return false

        addNewAuthNode(invokedNodeNumber)
        return true
    }

    private fun addNewAuthNode(invokedNodeNumber: Int) {
        val nodeData = mNodesMap[invokedNodeNumber]

        mIsAuthStarted = true
        mInvokedNodesNumbers.add(invokedNodeNumber)
        vibrate(NODE_SELECT_VIBRATION_DELAY_IN_MILLIS)

        drawLineTo(nodeData.x.toFloat(), nodeData.y.toFloat())
        nodeData.nodeView.setNodeEnableState(true)
        mX = nodeData.x.toFloat()
        mY = nodeData.y.toFloat()

        mPath = Path()
        val linePath = PatternLinePath(mDefaultLineColor, mPath)
        mLinePaths.add(linePath)
        mPath.reset()
        mPath.moveTo(mX, mY)

        if (isAuthFinished()) {
            onAuthFinished()
            return
        }
    }

    private fun getInvokedNodeNumber(x: Float, y: Float): Int? {
        val invokeRadius = NODES_INVOKE_OFFSET_RADIUS.dpToPx()
        mNodesMap.forEach { i, nodeData ->
            if (isInvokedCoordinateValue(x, nodeData.x.toFloat(), invokeRadius)
                && isInvokedCoordinateValue(y, nodeData.y.toFloat(), invokeRadius)
            ) {
                return i
            }
        }
        return null
    }

    private fun isInvokedCoordinateValue(
        coordinateValue: Float,
        nodeCoordinate: Float,
        invokeOffset: Int
    ): Boolean {
        val maxCoordinate = coordinateValue + invokeOffset
        val minCoordinate = coordinateValue - invokeOffset
        return nodeCoordinate in minCoordinate..maxCoordinate
    }

    private fun isAuthFinished() = mNodesMap.size() == mInvokedNodesNumbers.size

    private fun onAuthFinished() {
        if (mInvokedNodesNumbers.size == 1) {
            clearAndReset()
            return
        }

        mIsPatternInputLocked = true
        mIsAuthStarted = false
        val resultStringBuilder = StringBuilder()
        mInvokedNodesNumbers.forEach {
            resultStringBuilder.append(mNodesCodesList[it])
        }
        if (this::mOnFinishedAction.isInitialized) {
            mOnFinishedAction.invoke(resultStringBuilder.toString())
        }
        postDelayed({ clearAndReset() }, FINISH_RESET_DELAY_IN_MILLIS)
    }

    private fun clearAndReset() {
        mLinePaths.clear()
        mInvokedNodesNumbers.clear()
        mNodesMap.forEach { _, patternNodeData ->
            patternNodeData.nodeView.setNodeEnableState(false)
        }
        mIsAuthStarted = false
        mIsPatternInputLocked = false
        invalidate()
    }

    private fun startAuthFailedAnimation() {
        val shakeAnimation = AnimationUtils.loadAnimation(context, R.anim.medium_shake)
        mLinePaths.forEach {
            it.color = mFailedLineColor
        }
        mNodesMap.forEach { _, patternNodeData ->
            patternNodeData.nodeView.apply {
                setNodeFailedState()
                startAnimation(shakeAnimation)
            }
        }
        vibrate(FAILED_AUTH_VIBRATION_DELAY_IN_MILLIS)
    }

    private fun vibrate(duration: Long) {
        if (mIsVibrationNeeds) {
            context?.vibrate(duration)
        }
    }
}