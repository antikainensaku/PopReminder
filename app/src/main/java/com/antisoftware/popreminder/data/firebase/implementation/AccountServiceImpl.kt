package com.antisoftware.popreminder.data.firebase.implementation

import androidx.compose.runtime.snapshots.SnapshotApplyResult
import com.antisoftware.popreminder.data.User
import com.antisoftware.popreminder.data.firebase.AccountService
import com.antisoftware.popreminder.data.firebase.trace
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import javax.inject.Inject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class AccountServiceImpl @Inject constructor(private val auth: FirebaseAuth) : AccountService {

    override val currentUserId: String
        get() = auth.currentUser?.uid.orEmpty()

    override val hasUser: Boolean
        get() = auth.currentUser != null

    override val isAnonymous: Boolean
        get() = auth.currentUser!!.isAnonymous

    override val currentUser: Flow<User>
        get() = callbackFlow {
            val listener =
                FirebaseAuth.AuthStateListener { auth ->
                    this.trySend(auth.currentUser?.let { User(it.uid, it.isAnonymous, it.displayName) } ?: User())
                }
            auth.addAuthStateListener(listener)
            awaitClose { auth.removeAuthStateListener(listener) }
        }



    override fun getUserEmail() : String {
        return auth.currentUser?.email ?: "Not available"
    }

    override fun getName() : String {
        return auth.currentUser?.displayName ?: "Not available"
    }

    override suspend fun updateDisplayName(name: String) {
        auth.currentUser!!.updateProfile(userProfileChangeRequest {displayName = name}).await()
    }

    override suspend fun authenticate(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).await()
    }

    override suspend fun sendRecoveryEmail(email: String) {
        auth.sendPasswordResetEmail(email).await()
    }

    /* *** Disabled anonymous accounts for now ***
    override suspend fun createAnonymousAccount() {
        auth.signInAnonymously().await()
    }*/

    override suspend fun createAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).await()
    }

    override suspend fun deleteAccount() {
        auth.currentUser!!.delete().await()
    }

    override suspend fun signOut() {
        if (auth.currentUser!!.isAnonymous) {
            auth.currentUser!!.delete().await()
        }
        auth.signOut()

        // Sign the user back in anonymously.
        // createAnonymousAccount()
    }
}
