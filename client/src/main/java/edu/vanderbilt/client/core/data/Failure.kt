package com.udacity.shoestore.core.data

sealed class Failure {
    @Suppress("unused") //TODO: for future use
    object NetworkFailure : Failure()
    @Suppress("unused") //TODO: for future use
    object ServerFailure : Failure()
    data class UnexpectedFailure(val t: Throwable) : Failure()
    abstract class FeatureFailure: Failure()
}