package hr.fika.budgeapp.common.sharedprefs

import android.content.Context
import android.content.SharedPreferences

object SharedPrefsManager {
    private lateinit var sharedPreferences: SharedPreferences

    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
    }

    fun getString(key: PreferenceKeys) =
        sharedPreferences.getString(key.name, "")

    fun setString(key: PreferenceKeys, value: String) {
        sharedPreferences.edit().putString(key.name, value).apply()
    }

}