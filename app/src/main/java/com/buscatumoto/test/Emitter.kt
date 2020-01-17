package com.buscatumoto.test

interface Emitter<T> {
    fun onNext(value: T)
    fun onError(error: Throwable)
    fun onComplete()
}