package com.buscatumoto.data.mapper

import com.buscatumoto.data.local.entity.MotoEntity

object MotoEntityToUiMapper: BaseMapper<List<MotoEntity>, ArrayList<String>?> {
    override fun map(type: List<MotoEntity>?): ArrayList<String>? {
        return type?.map { it.model } as? ArrayList<String>
    }


}