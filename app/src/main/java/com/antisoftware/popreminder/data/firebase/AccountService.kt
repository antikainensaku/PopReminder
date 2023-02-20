package com.antisoftware.popreminder.data.firebase

import com.antisoftware.popreminder.data.User
import kotlinx.coroutines.flow.Flow

interface AccountService {
    val currentUserId: String
    val hasUser: Boolean
    val isAnonymous: Boolean

    val currentUser: Flow<User>

    fun getUserEmail() : String
    fun getName() : String
    suspend fun updateDisplayName(name: String)
    suspend fun authenticate(email: String, password: String)
    suspend fun sendRecoveryEmail(email: String)
    // suspend fun createAnonymousAccount()
    suspend fun createAccount(email: String, password: String)
    suspend fun deleteAccount()
    suspend fun signOut()
}
