package com.antisoftware.popreminder.data

data class User(
    val id: String = "",
    val isAnonymous: Boolean = true,
    val name: String? = "",
    val showAll: Boolean = true
)
