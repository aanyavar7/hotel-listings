package edu.vanderbilt.client.core.data.logging

import org.koin.core.logger.Level
import org.koin.core.logger.Logger
import org.koin.core.logger.MESSAGE
import timber.log.Timber

class TimberLogger(
    private val showInfo: Boolean = true,
    private val showDebug: Boolean = true,
    private val showError: Boolean = true,
    tag: String? = null
) : Logger() {
    private val tree: Timber.Tree =
        if (tag == null) {
            Timber.asTree()
        } else {
            Timber.tag(tag)
        }

    override fun log(level: Level, msg: MESSAGE) {
        when (level) {
            Level.DEBUG -> if (showDebug) tree.d(msg)
            Level.INFO -> if (showError) tree.i(msg)
            Level.ERROR -> if (showInfo) tree.e(msg)
            Level.NONE -> tree.v(msg)
        }
    }
}