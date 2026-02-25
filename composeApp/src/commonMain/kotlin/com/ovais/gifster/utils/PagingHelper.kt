package com.ovais.gifster.utils


fun calculateOffset(
    limit: Int = 25,
    pageNumber: Int = 1
): Int {
    return limit * (pageNumber - 1)
}