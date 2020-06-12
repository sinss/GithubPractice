package com.example.githubproject.manager

import com.example.githubproject.data.GithubUser
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RequestInterface {
    @GET("users")
    fun getUsers(
        @Query("since") since: Int,
        @Query("per_page") perPage: Int
    ): Single<List<GithubUser>>


    @GET("users/{userId}")
    fun getUser(@Path("userId") userId: String): Single<GithubUser>
}

enum class Status {
    RUNNING,
    SUCCESS,
    FAILED
}

@Suppress("DataClassPrivateConstructor")
data class NetworkState private constructor(
    val status: Status,
    val msg: String? = null) {
    companion object {
        val LOADED =
            NetworkState(Status.SUCCESS)
        val LOADING =
            NetworkState(Status.RUNNING)
        fun error(msg: String?) = NetworkState(
            Status.FAILED,
            msg
        )
    }
}