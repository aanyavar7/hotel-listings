package edu.vanderbilt.client.features

import android.app.Application
import android.content.Context
import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

/**
 * Provides an observable progress LiveData property that can be
 * invoked by wrapping LiveData updates in a call to [runWithProgress].
 */
open class BaseViewModel(app: Application) : AndroidViewModel(app), CoroutineScope {
    private val _progress = MutableLiveData(false)
    val progress: LiveData<Boolean>
        get() = _progress

    private val compositeJob = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + compositeJob

    protected val context: Context
        get() = getApplication()

    override fun onCleared() {
        compositeJob.cancel()
        super.onCleared()
    }

    fun runWithProgress(block: suspend () -> Any) {
        viewModelScope.launch(Dispatchers.IO) {
            _progress.postValue(true)

            try {
                block()
            } finally {
                _progress.postValue(false)
            }
        }
    }
}