package com.buscatumoto.utils.ui

import android.graphics.drawable.Drawable

sealed class CatalogueUiOp {
    class NavigateToDetail(val id: String): CatalogueUiOp()
}
