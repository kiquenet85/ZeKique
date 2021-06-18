package com.example.zemogatest.presentation.post.list.ui_model

import android.os.Parcelable
import com.example.zemogatest.presentation.base.Identifier
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PostUI(
    val id: String,
    val title: String,
    val description: String,
    val favorite: Boolean,
    val userId: String,
    val seen: Boolean,
    val showAsNotSeen: Boolean,
    val userUI: UserUI? = null
) : Identifier, Parcelable {
    override fun getIdentifier(): String {
        return id
    }
}

@Parcelize
data class UserUI(
    val name: String,
    val email: String,
    val phone: String,
    val website: String
) : Parcelable