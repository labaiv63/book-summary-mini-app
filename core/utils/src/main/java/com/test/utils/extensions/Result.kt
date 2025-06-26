package com.test.utils.extensions

fun <T> Result<T>.finally(block: () -> Unit) {
    block()
}