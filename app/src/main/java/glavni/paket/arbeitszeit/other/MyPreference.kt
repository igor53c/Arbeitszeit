package glavni.paket.arbeitszeit.other

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import glavni.paket.arbeitszeit.other.Constants.SHARED_PREFERENCES_KEY_BREAK_6_AND_9
import glavni.paket.arbeitszeit.other.Constants.SHARED_PREFERENCES_KEY_BREAK_BELOW_6
import glavni.paket.arbeitszeit.other.Constants.SHARED_PREFERENCES_KEY_BREAK_OVER_9
import glavni.paket.arbeitszeit.other.Constants.SHARED_PREFERENCES_KEY_HOURS_PER_WEEK
import glavni.paket.arbeitszeit.other.Constants.SHARED_PREFERENCES_KEY_LANGUAGES
import glavni.paket.arbeitszeit.other.Constants.SHARED_PREFERENCES_KEY_LAST_LOGIN
import glavni.paket.arbeitszeit.other.Constants.SHARED_PREFERENCES_KEY_LOG_IN
import glavni.paket.arbeitszeit.other.Constants.SHARED_PREFERENCES_KEY_ROUNDING
import glavni.paket.arbeitszeit.other.Constants.SHARED_PREFERENCES_PREFIX
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MyPreference @Inject constructor(@ApplicationContext context : Context){

    private val sharedPref = context.getSharedPreferences(SHARED_PREFERENCES_PREFIX, Context.MODE_PRIVATE)

    fun getLogIn(): Boolean {
        return sharedPref.getBoolean(SHARED_PREFERENCES_KEY_LOG_IN, true)
    }
    fun setLogIn(state: Boolean) {
        sharedPref.edit().putBoolean(SHARED_PREFERENCES_KEY_LOG_IN, state).apply()
    }
    fun getBreakBelow6(): Int {
        return sharedPref.getInt(SHARED_PREFERENCES_KEY_BREAK_BELOW_6, 0)
    }
    fun setBreakBelow6(state: Int) {
        sharedPref.edit().putInt(SHARED_PREFERENCES_KEY_BREAK_BELOW_6, state).apply()
    }
    fun getBreak6And9(): Int {
        return sharedPref.getInt(SHARED_PREFERENCES_KEY_BREAK_6_AND_9, 30)
    }
    fun setBreak6And9(state: Int) {
        sharedPref.edit().putInt(SHARED_PREFERENCES_KEY_BREAK_6_AND_9, state).apply()
    }
    fun getBreakOver9(): Int {
        return sharedPref.getInt(SHARED_PREFERENCES_KEY_BREAK_OVER_9, 45)
    }
    fun setBreakOver9(state: Int) {
        sharedPref.edit().putInt(SHARED_PREFERENCES_KEY_BREAK_OVER_9, state).apply()
    }
    fun getHoursPerWeek(): Float {
        return sharedPref.getFloat(SHARED_PREFERENCES_KEY_HOURS_PER_WEEK, 40.0F)
    }
    fun setHoursPerWeek(state: Float) {
        sharedPref.edit().putFloat(SHARED_PREFERENCES_KEY_HOURS_PER_WEEK, state).apply()
    }
    fun getLanguages(): String? {
        return sharedPref.getString(SHARED_PREFERENCES_KEY_LANGUAGES, "English")
    }
    fun setLanguages(state: String?) {
        sharedPref.edit().putString(SHARED_PREFERENCES_KEY_LANGUAGES, state).apply()
    }
    fun getRounding(): Boolean {
        return sharedPref.getBoolean(SHARED_PREFERENCES_KEY_ROUNDING, false)
    }
    fun setRounding(state: Boolean) {
        sharedPref.edit().putBoolean(SHARED_PREFERENCES_KEY_ROUNDING, state).apply()
    }
    fun getLastLogIn(): Long {
        return sharedPref.getLong(SHARED_PREFERENCES_KEY_LAST_LOGIN, 0)
    }
    fun setLastLogIn(state: Long) {
        sharedPref.edit().putLong(SHARED_PREFERENCES_KEY_LAST_LOGIN, state).apply()
    }
}