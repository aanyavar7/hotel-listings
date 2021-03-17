package edu.vanderbilt.client

import android.app.Application
import edu.vanderbilt.client.core.di.appModule
import edu.vanderbilt.client.core.data.logging.TimberLogger
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.core.context.startKoin
import timber.log.Timber

@Suppress("unused")
class App : Application() {
    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())

        startKoin {
            androidContext(this@App)
            androidFileProperties()
            modules(appModule)
            logger(TimberLogger(showInfo = true, showDebug = true, showError = true, tag = "Koin"))
        }
    }
}