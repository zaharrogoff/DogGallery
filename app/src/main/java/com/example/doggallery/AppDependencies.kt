package com.example.doggallery

import android.content.Context
import androidx.room.Room
import com.example.doggallery.data.BreedLocalDataSource
import com.example.doggallery.data.BreedRemoteDataSource
import com.example.doggallery.data.BreedRepository
import com.example.doggallery.data.local.AppDatabase
import com.example.doggallery.data.remote.DogApi
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object AppDependencies {

    private const val BASE_URL = "https://api.thedogapi.com/v1/"

    /** Необязательный ключ с https://thedogapi.com. Пустая строка: запросы без ключа. */
    private const val API_KEY = ""

    private lateinit var appContext: Context

    fun init(context: Context) {
        appContext = context.applicationContext
    }

    private val database by lazy {
        Room.databaseBuilder(appContext, AppDatabase::class.java, "dog_gallery.db").build()
    }

    private val moshi by lazy { Moshi.Builder().build() }

    private val okHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor { chain ->
                val builder = chain.request().newBuilder()
                if (API_KEY.isNotBlank()) builder.header("x-api-key", API_KEY)
                chain.proceed(builder.build())
            }
            .build()
    }

    private val api by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(DogApi::class.java)
    }

    val repository by lazy {
        BreedRepository(
            local = BreedLocalDataSource(database.breedDao()),
            remote = BreedRemoteDataSource(api)
        )
    }
}