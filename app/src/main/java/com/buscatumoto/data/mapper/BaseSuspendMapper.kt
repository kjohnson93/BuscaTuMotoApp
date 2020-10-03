package com.buscatumoto.data.mapper

interface BaseSuspendMapper<in A, out B> {

    suspend fun suspenMap(type: A?): B

}