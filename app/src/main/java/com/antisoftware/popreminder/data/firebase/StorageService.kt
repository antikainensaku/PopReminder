package com.antisoftware.popreminder.data.firebase

import com.antisoftware.popreminder.data.Reminder
import com.antisoftware.popreminder.data.User
import kotlinx.coroutines.flow.Flow

interface StorageService {
    val reminders: Flow<List<Reminder>>

    suspend fun getReminder(reminderId: String): Reminder?
    suspend fun save(reminder: Reminder): String
    suspend fun update(reminder: Reminder)
    suspend fun delete(reminderId: String)
    suspend fun deleteAllForUser(userId: String)
}
