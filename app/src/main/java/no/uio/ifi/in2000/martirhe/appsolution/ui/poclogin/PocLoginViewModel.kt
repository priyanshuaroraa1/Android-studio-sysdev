package no.uio.ifi.in2000.martirhe.appsolution.ui.poclogin

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class PocLoginViewModel: ViewModel() {

    val email = MutableStateFlow("")
    val password = MutableStateFlow("")

    fun updateEmail(newEmail: String) {
        email.value = newEmail
    }

    fun updatePassword(newPassword: String) {
        password.value = newPassword
    }


}