package com.ovais.gifster.utils

fun interface Provider<T> {
    fun get(): T
}