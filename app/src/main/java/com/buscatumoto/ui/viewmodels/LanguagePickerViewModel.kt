package com.buscatumoto.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import javax.inject.Inject

class LanguagePickerViewModel @Inject constructor(): BaseViewModel() {

    //this mutable sets the current language on the system and sets value so observers can notice it.
    val languageSelectedMutable = MutableLiveData<String> ()

    fun setLanguage(languageLocale: String) {
        languageSelectedMutable.value = languageLocale
    }
}