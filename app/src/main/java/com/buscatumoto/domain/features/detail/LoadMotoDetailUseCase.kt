package com.buscatumoto.domain.features.detail

import com.buscatumoto.data.local.dao.MotoDao
import com.buscatumoto.data.local.entity.MotoEntity
import com.buscatumoto.data.mapper.MotoEntityToMotoDetailUiMapper
import com.buscatumoto.ui.models.MotoDetailUi
import javax.inject.Inject

class LoadMotoDetailUseCase @Inject constructor(private val motoDao: MotoDao) {

    /**
     * Parses moto data stored locally object to a object that ui can handle and interpret.
     */
    suspend fun parseMotoEntity(motoEntity: MotoEntity): MotoDetailUi? =
        MotoEntityToMotoDetailUiMapper.suspenMap(motoEntity)

    /**
     * Retrieves the current motoEntity record.
     */
    fun getMoto(): MotoEntity {
        return motoDao.getMoto()
    }

}