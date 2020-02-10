package com.buscatumoto.utils.mapper

import com.buscatumoto.data.remote.dto.response.MotoEntity
import com.buscatumoto.utils.mapper.ui.MotoUI

object MotoEntityToUiMapper: BaseMapper<List<MotoEntity>, ArrayList<String>?> {
    override fun map(type: List<MotoEntity>?): ArrayList<String>? {
        return type?.map { it.model } as? ArrayList<String>
    }


}