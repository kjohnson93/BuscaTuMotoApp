package com.buscatumoto.utils.mapper

interface BaseMapper<in A, out B> {

    fun map(type: A?): B
}