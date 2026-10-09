package com.pemmob.digimonexplorer.network

import com.pemmob.digimonexplorer.data.model.DigimonDetailDto
import com.pemmob.digimonexplorer.data.model.DigimonListResponse
import com.pemmob.digimonexplorer.util.DigimonConstants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {
    @GET("api/v1/digimon")
    suspend fun getDigimonList(
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int
    ): DigimonListResponse

    @GET("api/v1/digimon/{id}")
    suspend fun getDigimonDetail(@Path("id") id: Int): DigimonDetailDto
}

object ApiClient {
    val instance: ApiInterface by lazy {
        Retrofit.Builder()
            .baseUrl(DigimonConstants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiInterface::class.java)
    }
}
