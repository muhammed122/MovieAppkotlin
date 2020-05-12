package com.example.movieappinkotlin.data.api

import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


const val API_KEY = "4f74f29e8398828045eff21d601b0723"
const val BASE_URL = "https://api.themoviedb.org/3/"
const val IMAGE_BASE_URl = "https://image.tmdb.org/t/p/w342"

const val FIRST_PAGE=1
const val MOVIES_PER_PAGE=20


object ApiManager {

    fun getService(): MovieServices {
        val requestInterceptor = Interceptor { chain ->
            val url: HttpUrl = chain.request().url().newBuilder()
                .addQueryParameter("api_key", API_KEY)
                .build()

            val request =chain.request()
                .newBuilder()
                .url(url)
                .build()

            return@Interceptor chain.proceed(request)

        }

        val okHttpClient =OkHttpClient.Builder()
            .addInterceptor(requestInterceptor)
            .connectTimeout(60,TimeUnit.SECONDS)
            .build()


        val retrofit =Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .build()



        return  retrofit.create(MovieServices::class.java)


    }

}