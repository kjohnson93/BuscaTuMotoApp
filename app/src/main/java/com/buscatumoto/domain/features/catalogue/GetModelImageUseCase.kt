package com.buscatumoto.domain.features.catalogue


import android.graphics.drawable.Drawable
import coil.Coil
import coil.api.get

import javax.inject.Inject
import javax.inject.Singleton

//Singleton to avoid instantiating in every catalogue list item.
@Singleton
class GetModelImageUseCase @Inject constructor() {

    suspend fun execute(imageUrl: String): Drawable {
        return Coil.get(imageUrl)
    }
}