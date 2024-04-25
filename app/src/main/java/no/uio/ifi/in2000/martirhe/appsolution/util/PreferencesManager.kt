package no.uio.ifi.in2000.martirhe.appsolution.util
import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class PreferencesManager @Inject constructor(private val preferences: SharedPreferences) {

    companion object {
        private const val ONBOARDING_SHOWN = "onboarding_shown"
    }

    var isOnboardingShown: Boolean
        get() = preferences.getBoolean(ONBOARDING_SHOWN, false)
        set(value) = preferences.edit().putBoolean(ONBOARDING_SHOWN, value).apply()
}