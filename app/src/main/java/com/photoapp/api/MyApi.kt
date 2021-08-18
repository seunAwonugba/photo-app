package com.photoapp.api

import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.Part


interface MyApi {
    /**
     * Create function that handles uploading image to server
     */
    @Multipart
    @POST("api/v1/upload")
    fun uploadImage(
        @Part image: MultipartBody.Part,
        @Part("desc") desc: RequestBody
    ):Call<UploadResponse>

    /**
     * Using companion object to hold the retrofit builder
     */

    companion object{
        operator fun invoke():MyApi{
            return Retrofit.Builder()
                .baseUrl("https://darot-image-upload-service.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MyApi::class.java)
        }
    }
}