package com.example.playlistmaker

import android.content.SharedPreferences
import android.provider.Settings.Global.putString

data class SearchHistory(private val sharedPref : SharedPreferences) {

    private fun saveValueToSharedPrefs(key: String, value: String) { //функция для сохранения значений
        with(sharedPref.edit()) {
            putString(key, value)
            apply()
        }
    }

    private fun loadValueFromSharedPrefs(key: String, value : String){
        with(sharedPref.edit()){
            sharedPref.getString(key, value)
            apply()
        }
    }

    //binding.smallSalThu.setOnFocusChangeListener { _, hasFocus ->
      //  if (!hasFocus) {
        //    saveValueToSharedPrefs(resources.getString(R.string.мелкий_чт), binding.smallSalThu.text.toString())
        //}
    //}

}