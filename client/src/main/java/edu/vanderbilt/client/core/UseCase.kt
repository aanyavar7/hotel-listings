package edu.vanderbilt.client.core

import com.udacity.shoestore.core.data.Failure
import com.udacity.shoestore.core.data.Either
import kotlinx.coroutines.*

abstract class UseCase<out Type, in Params> where Type : Any {
    abstract suspend fun run(params: Params): Either<Failure, Type>

    open operator fun invoke(
        scope: CoroutineScope,
        params: Params,
        onResult: (Either<Failure, Type>) -> Unit = {}
    ) {
        val job = scope.async { run(params) }
        scope.launch { onResult(job.await()) }
    }
}