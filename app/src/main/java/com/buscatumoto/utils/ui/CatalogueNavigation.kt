package com.buscatumoto.utils.ui

import com.buscatumoto.ui.models.MotoDetailUi

sealed class CatalogueUiOp {
    class LoadDetailActivity(val id: String): CatalogueUiOp()
    class LoadFragmentPageContent(val id: String, val motoDetailUi: MotoDetailUi): CatalogueUiOp()
}
