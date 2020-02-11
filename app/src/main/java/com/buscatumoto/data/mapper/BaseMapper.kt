package com.buscatumoto.data.mapper

interface BaseMapper<in A, out B> {

    fun map(type: A?): B
}