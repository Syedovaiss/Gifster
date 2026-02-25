package com.ovais.gifster.utils


fun interface SuspendUseCase<out Result> {
    suspend operator fun invoke(): Result
}

fun interface SuspendParameterizeUseCase<in Params, out Result> {
    suspend operator fun invoke(params: Params): Result
}