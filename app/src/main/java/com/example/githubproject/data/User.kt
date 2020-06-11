package com.example.githubproject.data

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id")
    var id: Int,

    @SerializedName("login")
    var login: String,

    @SerializedName("avatar_url")
    var avatarUrl: String,

    @SerializedName("site_admin")
    var site_admin: Boolean,

    @SerializedName("location")
    var location: String = "",

    @SerializedName("bio")
    var bio: String = "",

    @SerializedName("name")
    var name: String = "",

    @SerializedName("blog")
    var blog: String = ""
)