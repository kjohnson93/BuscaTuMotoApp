package com.buscatumoto.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import com.buscatumoto.BuscaTuMotoApplication
import com.buscatumoto.utils.global.LANGUAGE_CA
import com.buscatumoto.utils.global.LANGUAGE_ENG
import com.buscatumoto.utils.global.LANGUAGE_ES
import javax.inject.Inject

class LanguagePickerViewModel @Inject constructor(): BaseViewModel() {

    //this mutable sets the current language on the system and sets value so observers can notice it.
    val languageSelectedMutable = MutableLiveData<String> ()
    val catalanSelectedMutable = MutableLiveData<Boolean> ()
    val englishSelectedMutable = MutableLiveData<Boolean> ()
    val spanishSelectedMutable = MutableLiveData<Boolean> ()

    init {
        loadSelectedLanguage()
    }

    private fun loadSelectedLanguage() {

        when (BuscaTuMotoApplication.getInstance().getDefaultLanguage().language) {
            LANGUAGE_CA -> {
                catalanSelectedMutable.value = true
                spanishSelectedMutable.value = false
                englishSelectedMutable.value = false
            }
            LANGUAGE_ES -> {
                catalanSelectedMutable.value = false
                spanishSelectedMutable.value = true
                englishSelectedMutable.value = false
            }
            LANGUAGE_ENG ->{
                catalanSelectedMutable.value = false
                spanishSelectedMutable.value = false
                englishSelectedMutable.value = true
            }
        }
    }

    fun setLanguage(languageLocale: String) {
        languageSelectedMutable.value = languageLocale
    }
}