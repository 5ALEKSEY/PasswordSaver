package com.ak.base.ui.custom.popupmenu

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.Disposable
import java.util.concurrent.TimeUnit
import kotlin.math.abs

@SuppressLint("ClickableViewAccessibility")
fun View.setOnPopupWindowMenuListener(
    menuItems: List<PopupWindowMenuItem>,
    popupMenuItemClickListener: PopupWindowMenuItemClickListener,
    onPopupMenuOpened: (popupmenu: PopupWindowMenu) -> Unit,
    onPopupMenuClosed: () -> Unit,
) {
    fun showPopupMenu(targetView: View, x: Float, y: Float) {
        PopupWindowMenu.create(targetView.context).apply {
            setOnDismissListener {
                onPopupMenuClosed()
            }
            show(
                menuItems = menuItems,
                popupMenuItemClickListener = popupMenuItemClickListener,
                clickedView = targetView,
                clickedX = x,
                clickedY = y,
            )
            onPopupMenuOpened(this)
        }
    }

    setOnTouchListener(
        TouchWithCoordinatesListener(
            { targetView: View, _, _ ->
                targetView.performClick()
            },
            { targetView: View, x: Float, y: Float ->
                showPopupMenu(targetView, x, y)
            },
        )
    )
}

fun View.removeOnPopupWindowMenuListener() {
    setOnTouchListener(
        TouchWithCoordinatesListener(
            { targetView: View, _, _ ->
                targetView.performClick()
            },
            { targetView: View, _, _ ->
                targetView.performLongClick()
            },
        )
    )
}

@SuppressLint("ClickableViewAccessibility")
private class TouchWithCoordinatesListener(
    private val onSingleClick: (targetView: View, x: Float, y: Float) -> Unit,
    private val onLongClick: (targetView: View, x: Float, y: Float) -> Unit,
) : View.OnTouchListener {

    private var eventTimeDisposable: Disposable? = null
    private var wasCancelled = false
    private var downX = -1F
    private var downY = -1F

    private val onSingleClickProxy = { targetView: View, x: Float, y: Float ->
        if (!wasCancelled) onSingleClick(targetView, x, y)
    }
    private val onLongClickProxy = { targetView: View, x: Float, y: Float ->
        if (!wasCancelled) onLongClick(targetView, x, y)
    }

    override fun onTouch(targetView: View?, event: MotionEvent?): Boolean {
        if (targetView == null || event == null) return false

        when (event.action) {
            MotionEvent.ACTION_DOWN -> startEventLogic(targetView, event)
            MotionEvent.ACTION_MOVE -> if (!isMoveDistanceAcceptable(event)) stopEventLogic()
            MotionEvent.ACTION_CANCEL -> {
                wasCancelled = true
                stopEventLogic()
            }
            MotionEvent.ACTION_UP -> stopEventLogic()
        }

        return true
    }

    private fun startEventLogic(targetView: View, event: MotionEvent) {
        wasCancelled = false
        startEventTimer(targetView)
        setDownCoordinates(event)
    }

    private fun stopEventLogic() {
        cancelEventTimer()
        clearDownCoordinates()
    }

    private fun startEventTimer(targetView: View) {
        eventTimeDisposable = Single.timer(LONG_CLICK_HOLD_DELAY, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnDispose { ifCanPerformClick { onSingleClickProxy(targetView, downX, downY) } }
            .subscribe { _ ->
                ifCanPerformClick { onLongClickProxy(targetView, downX, downY) }
                clearDownCoordinates()
            }
    }

    private fun cancelEventTimer() {
        eventTimeDisposable?.dispose()
        eventTimeDisposable = null
    }

    private fun setDownCoordinates(event: MotionEvent) {
        setDownCoordinates(event.x, event.y)
    }

    private fun setDownCoordinates(x: Float, y: Float) {
        downX = x
        downY = y
    }

    private fun clearDownCoordinates() {
        downX = -1F
        downY = -1F
    }

    private fun isMoveDistanceAcceptable(event: MotionEvent): Boolean {
        val xMoveDistance = abs(downX - event.x)
        val yMoveDistance = abs(downY - event.y)

        return xMoveDistance <= MAX_MOVE_DISTANCE_IN_PIXELS && yMoveDistance <= MAX_MOVE_DISTANCE_IN_PIXELS
    }

    private fun ifCanPerformClick(block: () -> Unit) {
        if (downX > 0 && downY > 0) block()
    }

    private companion object {
        const val LONG_CLICK_HOLD_DELAY = 600L
        const val MAX_MOVE_DISTANCE_IN_PIXELS = 75F
    }
}

@SuppressLint("ClickableViewAccessibility")
private class PopUpMenuTouchListener(
    private val timeProvider: () -> Long,
    private val menuItems: List<PopupWindowMenuItem>,
    private val popupMenuItemClickListener: PopupWindowMenuItemClickListener,
    private val onPopupMenuOpened: (popupmenu: PopupWindowMenu) -> Unit,
    private val onPopupMenuClosed: () -> Unit,
) : View.OnTouchListener {

    private var showTimeDisposable: Disposable? = null
    private var lastDownTime = -1L
    private var downX = -1F
    private var downY = -1F

    override fun onTouch(targetView: View?, event: MotionEvent?): Boolean {
        if (targetView == null || event == null) return false

        when (event.action) {
            MotionEvent.ACTION_DOWN -> startPopupMenuLogic(targetView, event)
            MotionEvent.ACTION_MOVE -> if (!isMoveDistanceAcceptable(event)) stopPopupMenuLogic()
            MotionEvent.ACTION_CANCEL,
            MotionEvent.ACTION_UP -> stopPopupMenuLogic()
        }

        return true
    }

    private fun startPopupMenuLogic(targetView: View, event: MotionEvent) {
        startShowPopupMenuTimer(targetView)
        setDownCoordinates(event)
        lastDownTime = timeProvider()
    }

    private fun stopPopupMenuLogic() {
        cancelShowPopupMenuTimer()
        clearDownCoordinates()
        lastDownTime = -1
    }

    private fun startShowPopupMenuTimer(targetView: View) {
        showTimeDisposable = Single.timer(OPEN_POPUP_HOLD_DELAY, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnDispose {
                val a = 1
            }
            .subscribe { _ ->
                if (!canShowPopupMenu()) {
                    clearDownCoordinates()
                    return@subscribe
                }
                showPopupMenu(targetView, downX, downY)
                clearDownCoordinates()
                lastDownTime = -1
            }
    }

    private fun cancelShowPopupMenuTimer() {
        showTimeDisposable?.dispose()
        showTimeDisposable = null
    }

    private fun setDownCoordinates(event: MotionEvent) {
        setDownCoordinates(event.x, event.y)
    }

    private fun setDownCoordinates(x: Float, y: Float) {
        downX = x
        downY = y
    }

    private fun clearDownCoordinates() {
        downX = -1F
        downY = -1F
    }

    private fun isMoveDistanceAcceptable(event: MotionEvent): Boolean {
        val xMoveDistance = abs(downX - event.x)
        val yMoveDistance = abs(downY - event.y)

        return xMoveDistance <= OPEN_POPUP_MOVE_MAX_DISTANCE && yMoveDistance <= OPEN_POPUP_MOVE_MAX_DISTANCE
    }

    private fun canShowPopupMenu(): Boolean {
        if (downX < 0 || downY < 0) {
            return false
        }

        val holdTime = timeProvider() - lastDownTime
        if (holdTime < OPEN_POPUP_HOLD_DELAY) {
            return false
        }

        return true
    }

    private fun showPopupMenu(targetView: View, x: Float, y: Float) {
        PopupWindowMenu.create(targetView.context).apply {
            setOnDismissListener {
                onPopupMenuClosed()
            }
            show(
                menuItems = menuItems,
                popupMenuItemClickListener = popupMenuItemClickListener,
                clickedView = targetView,
                clickedX = x,
                clickedY = y,
            )
            onPopupMenuOpened(this)
        }
    }

    private companion object {
        const val OPEN_POPUP_HOLD_DELAY = 600L
        const val OPEN_POPUP_MOVE_MAX_DISTANCE = 75F
    }
 }