package com.buscatumoto.test

public interface ObservableOnSubscribe<T> {
    fun subscribe(emitter: Emitter<T>)
}