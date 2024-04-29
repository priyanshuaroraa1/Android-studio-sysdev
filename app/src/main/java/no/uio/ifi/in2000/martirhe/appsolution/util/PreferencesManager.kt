package no.uio.ifi.in2000.martirhe.appsolution.util
import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class PreferencesManager @Inject constructor(private val preferences: SharedPreferences) {

    companion object {
        private const val ONBOARDING_SHOWN = "onboarding_shown"
        private const val SEARCH_HISTORY = "search_history"
    }

    var isOnboardingShown: Boolean
        get() = preferences.getBoolean(ONBOARDING_SHOWN, false)
        set(value) = preferences.edit().putBoolean(ONBOARDING_SHOWN, value).apply()

    var searchHistory: String
        get() = preferences.getString(SEARCH_HISTORY, "") ?: ""
        set(value) = preferences.edit().putString(SEARCH_HISTORY, value).apply()

    fun addToSearchHistory(newEntry: String) {
        val newEntryClean = newEntry.replace(";", "")
        val items = getSearchHistory().toMutableList()

        if (items.contains(newEntryClean)) {
            items.remove(newEntryClean)
        } else if (items.size >= 10) {
            items.removeAt(0)
        }
        items.add(newEntryClean)
        searchHistory = items.joinToString(";")
    }

    fun getSearchHistory(): List<String> {
        return if (searchHistory == "") {
            emptyList()
        } else {
            searchHistory.split(";").reversed()
        }

    }
}