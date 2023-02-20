package com.antisoftware.popreminder.data

import com.google.firebase.firestore.DocumentId

data class Reminder(
    @DocumentId val id: String = "",
    val msg: String = "",
    val description: String = "",
    val location_x: String = "",
    val location_y: String = "",
    val dueDate: String = "",
    val dueTime: String = "",
    val creation_time: String = "",
    val creatorId: String = "",
    //val check: Boolean = false,
    val completed: Boolean = false
)
