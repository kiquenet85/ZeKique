package com.example.zemogatest.presentation.post.detail.ui_model

import android.os.Parcelable
import com.example.zemogatest.presentation.base.Identifier
import com.example.zemogatest.presentation.post.list.ui_model.PostUI
import kotlinx.android.parcel.Parcelize

@Parcelize
class CommentUI(
    val id: String,
    val title: String,
    val body: String,
    val postUI: PostUI? = null
) : Identifier, Parcelable {
    override fun getIdentifier(): String {
        return id
    }
}