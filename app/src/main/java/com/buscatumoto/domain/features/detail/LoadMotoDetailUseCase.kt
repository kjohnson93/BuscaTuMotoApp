package com.buscatumoto.domain.features.detail

import androidx.lifecycle.LiveData
import com.buscatumoto.data.local.dao.MotoDao
import com.buscatumoto.data.local.entity.MotoEntity
import com.buscatumoto.data.mapper.MotoEntityToMotoDetailUiMapper
import com.buscatumoto.ui.models.MotoDetailUi
import com.buscatumoto.ui.viewmodels.MotoDetailViewModel
import javax.inject.Inject

class LoadMotoDetailUseCase @Inject constructor(private val motoDao: MotoDao) {

    fun execute(id: String) : LiveData<MotoEntity> =
        motoDao.getMotoById(id)

    fun executeNoLiveData(id: String): MotoEntity = motoDao.getMotoByIdNoLiveData(id)

    suspend fun parseMotoEntity(motoEntity: MotoEntity): MotoDetailUi? =
        MotoEntityToMotoDetailUiMapper.suspenMap(motoEntity)

}