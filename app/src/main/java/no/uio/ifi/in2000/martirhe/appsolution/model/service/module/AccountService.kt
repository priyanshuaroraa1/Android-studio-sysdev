package no.uio.ifi.in2000.martirhe.appsolution.model.service.module

import kotlinx.coroutines.flow.Flow
import no.uio.ifi.in2000.martirhe.appsolution.model.User

interface AccountService {
    val currentUser: Flow<User?>
    val currentUserId: String
    fun hasUser(): Boolean
    suspend fun signIn(email: String, password: String)
    suspend fun signUp(email: String, password: String)
    suspend fun signOut()
    suspend fun deleteAccount()
}