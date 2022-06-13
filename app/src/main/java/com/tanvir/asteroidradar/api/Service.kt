package com.tanvir.asteroidradar.api

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.tanvir.asteroidradar.Constants
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface NasaApiService {
    //    ?start_date={start}&end_date={end}&api_key={api}
    @GET("neo/rest/v1/feed")
    suspend fun getNEOAsync(
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String,
        @Query("api_key") apiKey: String
    ): Response<String>

    @GET("planetary/apod")
    suspend fun getPodAsync(
        @Query("api_key") apiKey: String
    ): Response<NetworkPod>
}
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

object NasaApi {
    private val okHttpClient = OkHttpClient().newBuilder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()


    private val retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .baseUrl(Constants.BASE_URL)
        .build()
    val retrofitService: NasaApiService by lazy { retrofit.create(NasaApiService::class.java) }
}