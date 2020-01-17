package com.buscatumoto.test

import java.lang.Exception

interface Consumer<T> {

    @Throws(Exception::class)
    fun accept(t: T)
}