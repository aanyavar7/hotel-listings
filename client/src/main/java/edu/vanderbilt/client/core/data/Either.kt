package com.udacity.shoestore.core.data

/**
 * A generic class that holds either a data value or an error value.
 * @param <T>
 */
sealed class Either<out E : Any, out T : Any> {
    data class Left<out E : Any>(val error: E) : Either<E, Nothing>()
    data class Right<out T : Any>(val value: T) : Either<Nothing, T>()

    override fun toString(): String {
        return when (this) {
            is Left -> "Error[$error]"
            is Right -> "Success[$value]"
        }
    }

    /**
     * This is basically a fold operation that has been named
     * "either" for readability.
     */
    inline fun <R> either(onError: (error: E) -> R, onSuccess: (value: T) -> R): R =
        when (this) {
            is Right -> onSuccess(value)
            is Left -> onError(error)
        }
}

// Maps Left and Right accessors to Error and Success to improve readability.
typealias Error<E> = Either.Left<E>
typealias Success<T> = Either.Right<T>
