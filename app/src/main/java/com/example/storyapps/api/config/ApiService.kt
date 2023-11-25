package com.example.storyapps.api.config

import com.example.storyapps.api.AddStoryResponse
import com.example.storyapps.api.DetailStoryResponse
import com.example.storyapps.api.GetAllStoryResponse
import com.example.storyapps.api.LoginResponse
import com.example.storyapps.api.RegisterResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<RegisterResponse>

    @GET("stories")
    suspend fun getAllStory(
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): GetAllStoryResponse

    @GET("stories/{id}")
    suspend fun getDetailStory(
        @Path("id") id: String
    ): DetailStoryResponse

    @Multipart
    @POST("stories")
    suspend fun uploadStory(
        @Part("description") description: RequestBody,
        @Part file: MultipartBody.Part
    ): AddStoryResponse

    @GET("stories")
    suspend fun getMaps(
        @Query("location") location: Int = 1,
    ): Response<GetAllStoryResponse>
}