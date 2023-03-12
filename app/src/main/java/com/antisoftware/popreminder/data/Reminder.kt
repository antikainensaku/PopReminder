package com.antisoftware.popreminder.data

import com.google.firebase.firestore.DocumentId

data class Reminder(
    @DocumentId val id: String = "",
    val msg: String = "",
    val description: String = "",
    val latitude: Double? = null,
    val longitude: Double? = null,
    val dueDate: String = "",
    val dueDateInMillis: Long = 0,
    val dueTime: String = "",
    val dueTimeInMillis: Long = 0,
    val dueMillis: Long = 0,
    val dueDateGone: Boolean = false,
    val creation_time: String = "",
    val creatorId: String = "",
    //val check: Boolean = false,
    val completed: Boolean = false
)
