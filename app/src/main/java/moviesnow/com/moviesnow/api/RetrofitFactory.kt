package moviesnow.com.moviesnow.api

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.SerializationFeature
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

object RetrofitFactory {

    private const val BASE_URL = "https://api.themoviedb.org/3/"

    fun getRetrofitService(): ApiService =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(JacksonConverterFactory.create(getSnakeCaseMapper()))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .callFactory(getOkHttpClientFactory())
            .build()
            .create(ApiService::class.java)

    private fun getSnakeCaseMapper() =
        ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            .configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true)

    private fun getOkHttpClientFactory() =
        OkHttpClient.Builder().apply {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.valueOf("BODY")
            addInterceptor(logging)
        }.build()

}