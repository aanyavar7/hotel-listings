package edu.vanderbilt.client.common

import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

/**
 * Source: https://gist.github.com/JoseAlcerreca/5b661f1800e1e654f07cc54fe87441af#gistcomment-3609212
 * Supports global single events or per observer single events (using scope string).
 */
class Event<out T>(private val content: T) {
    private val consumedScopes = HashSet<String>()

    @Suppress("MemberVisibilityCanBePrivate")
    fun isConsumed(scope: String = "") = consumedScopes.contains(scope)

    @MainThread
    fun consume(scope: String = ""): T? {
        return if (isConsumed(scope)) {
            null
        } else {
            consumedScopes.add(scope)
            content
        }
    }

    fun peek(): T = content
}

fun <T> LiveData<Event<T>>.observeEvent(
    lifecycleOwner: LifecycleOwner,
    scope: String = "",
    observer: Observer<T>
) {
    observe(lifecycleOwner) { event ->
        event?.consume(scope)?.let { observer.onChanged(it) }
    }
}