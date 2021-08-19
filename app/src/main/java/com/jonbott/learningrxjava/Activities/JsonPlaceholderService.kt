package com.jonbott.learningrxjava.Activities

import com.jonbott.learningrxjava.ModelLayer.Entities.Posting
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

public interface JsonPlaceholderService {

    @GET("posts/{id}")
    fun getPosts(@Path("id") id: String): Call<Posting>
}