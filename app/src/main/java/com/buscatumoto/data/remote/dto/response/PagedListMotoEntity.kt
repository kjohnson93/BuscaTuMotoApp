package com.buscatumoto.data.remote.dto.response

import com.buscatumoto.data.local.entity.MotoEntity

data class PagedListMotoEntity(val list: List<MotoEntity>, val totalElements: Int)