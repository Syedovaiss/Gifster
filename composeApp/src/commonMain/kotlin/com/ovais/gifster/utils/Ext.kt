package com.ovais.gifster.utils

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

@OptIn(ExperimentalContracts::class)
inline fun <C : CharSequence> C?.ifNullOrEmpty(defaultValue: () -> C): C {
    contract {
        callsInPlace(defaultValue, InvocationKind.AT_MOST_ONCE)
    }
    return if (this.isNullOrEmpty()) defaultValue() else this
}