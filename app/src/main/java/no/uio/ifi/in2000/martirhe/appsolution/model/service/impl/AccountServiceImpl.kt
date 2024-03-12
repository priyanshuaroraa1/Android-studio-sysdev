package no.uio.ifi.in2000.martirhe.appsolution.model.service.impl

import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import no.uio.ifi.in2000.martirhe.appsolution.model.User
import no.uio.ifi.in2000.martirhe.appsolution.model.service.module.AccountService


class AccountServiceImpl : AccountService {

    override val currentUser: Flow<User?>
        get() = callbackFlow {
            // TODO
        }

    override val currentUserId: String
        get() = Firebase.auth.currentUser?.uid.orEmpty()

    override fun hasUser(): Boolean {
        return false
    }

    override suspend fun signIn(email: String, password: String) {
        TODO("Not yet implemented")
    }

    override suspend fun signUp(email: String, password: String) {
        TODO("Not yet implemented")
    }

    override suspend fun signOut() {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAccount() {
        TODO("Not yet implemented")
    }

}