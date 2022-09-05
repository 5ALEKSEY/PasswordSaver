package com.ak.base.utils

import android.util.SparseArray
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver

typealias PostponedAction = () -> Unit

class LifecyclePostponedEventsManager(
    private val lifecycle: Lifecycle,
) {

    private val postponedActionsMap = SparseArray<List<PostponedAction>>()
    private val lifecycleEventObserver = LifecycleEventObserver { _, event ->
        triggerPostponedForLifecycleEvent(event)
        if (event == Lifecycle.Event.ON_DESTROY) {
            destroy()
        }
    }

    init {
        init()
    }

    fun postponeOrInvokeFor(lifecycleEvent: Lifecycle.Event, postponedAction: PostponedAction) {
        if (lifecycle.currentState.isAtLeast(lifecycleEvent.targetState)) {
            postponedAction.invoke()
        } else {
            postponeActionForEvent(lifecycleEvent, postponedAction)
        }
    }

    private fun init() {
        lifecycle.addObserver(lifecycleEventObserver)
    }

    private fun destroy() {
        lifecycle.removeObserver(lifecycleEventObserver)
        postponedActionsMap.clear()
    }

    private fun postponeActionForEvent(lifecycleEvent: Lifecycle.Event, postponedAction: PostponedAction) {
        val oldPostponedActions = postponedActionsMap.get(lifecycleEvent.toPostponedKey(), emptyList())
        val newPostponedActions = oldPostponedActions + listOf(postponedAction)

        postponedActionsMap.put(lifecycleEvent.toPostponedKey(), newPostponedActions)
    }

    private fun triggerPostponedForLifecycleEvent(lifecycleEvent: Lifecycle.Event) {
        postponedActionsMap.get(lifecycleEvent.toPostponedKey())?.forEach { it.invoke() }
        postponedActionsMap.remove(lifecycleEvent.toPostponedKey())
    }

    private fun Lifecycle.Event.toPostponedKey() = ordinal
}