package com.buscatumoto.domain.features.detail

import com.buscatumoto.data.local.dao.MotoDao
import com.buscatumoto.data.local.entity.MotoEntity
import com.buscatumoto.data.mapper.MotoEntityToMotoDetailUiMapper
import com.buscatumoto.ui.models.MotoDetailUi
import javax.inject.Inject

class LoadMotoDetailUseCase @Inject constructor(private val motoDao: MotoDao) {

//    fun execute(id: String) : LiveData<MotoEntity> =
//        motoDao.getMotoById(id)

    fun executeNoLiveData(id: String): MotoEntity = motoDao.getMotoById(id)

    suspend fun parseMotoEntity(motoEntity: MotoEntity): MotoDetailUi? =
        MotoEntityToMotoDetailUiMapper.suspenMap(motoEntity)

}