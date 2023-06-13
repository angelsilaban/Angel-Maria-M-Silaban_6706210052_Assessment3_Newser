package org.d3if0052.newser.network

import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://github.com/angelsilaban/berita.json/blob/main/"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

interface BeritaApiService {
    @GET("berita.json")
    suspend fun getBerita(): String
}
object BeritaApi {
    val service: BeritaApiService by lazy {
        retrofit.create(BeritaApiService::class.java)
    }
}