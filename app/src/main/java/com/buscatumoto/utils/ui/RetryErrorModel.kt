package com.buscatumoto.utils.ui

/**
 * This class helps view models to identify a network request that needs to retry.
 */
data class RetryErrorModel(val errorMessage: Int?, val requestType: Int) {
    companion object {
        const val FILTER_ERROR = 0
        const val SEARCH_ERROR = 1
    }
}