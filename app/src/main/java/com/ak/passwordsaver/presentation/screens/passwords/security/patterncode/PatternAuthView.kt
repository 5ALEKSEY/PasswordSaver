package com.ak.passwordsaver.presentation.screens.passwords.security.patterncode

import android.content.Context
import android.graphics.*
import android.support.annotation.ColorInt
import android.util.AttributeSet
import android.util.SparseArray
import android.view.MotionEvent
import android.widget.RelativeLayout
import collections.forEach
import com.ak.passwordsaver.R
import com.ak.passwordsaver.utils.extensions.dpToPx
import kotlin.math.sqrt

class PatternAuthView(context: Context?, attrs: AttributeSet?) : RelativeLayout(context, attrs) {

    companion object {
        private const val STROKE_WIDTH = 20F
        @ColorInt
        private const val LINE_COLOR = Color.WHITE
        @ColorInt
        private const val BACKGROUND_COLOR = Color.BLACK
        private const val PATTERN_VIEW_SIZE = 256F
        private const val PATTERN_VIEW_NODES_OFFSET = 32F
        private const val NODES_INVOKE_OFFSET_RADIUS = 24F
        /**
         * Only 4, 9, 16, 25 ... For sqrt() correct invoke.
         * [mNodesCodesList] should be changed after [NODES_NUMBER] value changes.
         */
        private const val NODES_NUMBER = 9
        private const val FINISH_RESET_DELAY_IN_MILLIS = 1000L
    }

    lateinit var mOnFinishedAction: (patternResultCode: String) -> Unit
    lateinit var mOnNewNodeSelectedAction: () -> Unit

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
    private val mNodesMap = SparseArray<PatternNodeData>(NODES_NUMBER)
    private val mNodesCodesList = arrayListOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9")
    private val mInvokedNodesNumbers = arrayListOf<Int>()

    private var mIsAuthStarted = false

    private var mX: Float = 0F
    private var mY: Float = 0F

    init {
        setWillNotDraw(false)
        mCanvas = Canvas(mBitmap)
        // init paint
        mPaint.apply {
            isAntiAlias = true
            isDither = true
            color = LINE_COLOR
            style = Paint.Style.STROKE
            strokeJoin = Paint.Join.ROUND
            strokeCap = Paint.Cap.ROUND
            xfermode = null
            alpha = 0xFF
        }
        addNodeViews()
        clearAndReset()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event == null) {
            return false
        }
        val x = event.x
        val y = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> checkNodesAndStart(x, y)
            MotionEvent.ACTION_UP -> return false
            MotionEvent.ACTION_MOVE -> touchMove(x, y)
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
        mCanvas.drawColor(BACKGROUND_COLOR)

        mLinePaths.forEach {
            mPaint.color = it.color
            mPaint.strokeWidth = STROKE_WIDTH
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

                patternNodeView.layoutParams
                addView(patternNodeView, params)
            }
        }
    }

    private fun calculateNodeViewCoordinate(nodesMarginSize: Float, marginsCount: Int) =
        (PATTERN_VIEW_NODES_OFFSET + nodesMarginSize * marginsCount).dpToPx()

    private fun calculateNodesMarginDpSize(marginsCount: Int) =
        (PATTERN_VIEW_SIZE - 2 * PATTERN_VIEW_NODES_OFFSET) / marginsCount

    private fun calculateNodesCountInLine(): Int {
        return sqrt(NODES_NUMBER.toFloat()).toInt()
    }

    private fun calculateNodesMarginsCount(): Int {
        val nodesInLine = calculateNodesCountInLine()
        return nodesInLine - 1
    }

    private fun touchMove(x: Float, y: Float) {
        if (this::mPath.isInitialized && mIsAuthStarted) {
            mPath.reset()
            mPath.moveTo(mX, mY)
            mPath.lineTo(x, y)
            checkNodesAndStart(x, y)
        }
    }

    private fun checkNodesAndStart(x: Float, y: Float) {
        val invokedNodeNumber = getInvokedNodeNumber(x, y) ?: return
        if (mInvokedNodesNumbers.contains(invokedNodeNumber)) return

        startAuthAction(invokedNodeNumber)
    }

    private fun startAuthAction(invokedNodeNumber: Int) {
        val nodeData = mNodesMap[invokedNodeNumber]

        mIsAuthStarted = true
        mInvokedNodesNumbers.add(invokedNodeNumber)
        if (this::mOnNewNodeSelectedAction.isInitialized) {
            mOnNewNodeSelectedAction.invoke()
        }

        touchMove(nodeData.x.toFloat(), nodeData.y.toFloat())
        nodeData.nodeView.setNodeEnableState(true)
        mX = nodeData.x.toFloat()
        mY = nodeData.y.toFloat()

        mPath = Path()
        val linePath = PatternLinePath(LINE_COLOR, mPath)
        mLinePaths.add(linePath)
        mPath.reset()
        mPath.moveTo(mX, mY)

        if (isAuthFinished()) {
            mIsAuthStarted = false
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
        // return result code
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
        mNodesMap.forEach { _, patternNodeData -> patternNodeData.nodeView.setNodeEnableState(false) }
        mIsAuthStarted = false
        invalidate()
    }
}