package com.antisoftware.popreminder.data.firebase.implementation

import com.antisoftware.popreminder.data.Reminder
import com.antisoftware.popreminder.data.firebase.AccountService
import com.antisoftware.popreminder.data.firebase.StorageService
import com.antisoftware.popreminder.data.firebase.trace
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.snapshots
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.asDeferred
import kotlinx.coroutines.tasks.await

class StorageServiceImpl
@Inject
constructor(private val firestore: FirebaseFirestore, private val auth: AccountService) :
    StorageService {

    @OptIn(ExperimentalCoroutinesApi::class)
    override val reminders: Flow<List<Reminder>>
        get() =
            auth.currentUser.flatMapLatest { user ->
                currentCollection(user.id).snapshots().map { snapshot -> snapshot.toObjects() }
            }

    override suspend fun getReminder(reminderId: String): Reminder? =
        currentCollection(auth.currentUserId).document(reminderId).get().await().toObject()

    override suspend fun save(reminder: Reminder): String =
        trace(SAVE_REMINDER_TRACE) { currentCollection(auth.currentUserId).add(reminder).await().id }

    override suspend fun update(reminder: Reminder): Unit =
        trace(UPDATE_REMINDER_TRACE) {
            currentCollection(auth.currentUserId).document(reminder.id).set(reminder).await()
        }

    override suspend fun delete(reminderId: String) {
        currentCollection(auth.currentUserId).document(reminderId).delete().await()
    }
    // TODO: It's not recommended to delete on the client:
    // https://firebase.google.com/docs/firestore/manage-data/delete-data#kotlin+ktx_2
    override suspend fun deleteAllForUser(userId: String) {
        val matchingReminders = currentCollection(userId).get().await()
        matchingReminders.map { it.reference.delete().asDeferred() }.awaitAll()
    }

    private fun currentCollection(uid: String): CollectionReference =
        firestore.collection(USER_COLLECTION).document(uid).collection(REMINDER_COLLECTION)

    companion object {
        private const val USER_COLLECTION = "users"
        private const val REMINDER_COLLECTION = "reminders"
        private const val SAVE_REMINDER_TRACE = "saveReminder"
        private const val UPDATE_REMINDER_TRACE = "updateReminder"
    }
}
