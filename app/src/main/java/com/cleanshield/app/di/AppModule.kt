package com.cleanshield.app.di

import android.content.Context
import androidx.room.Room
import com.cleanshield.app.data.local.CleanShieldDatabase
import com.cleanshield.app.data.local.dao.ChallengeDao
import com.cleanshield.app.data.local.dao.JournalDao
import com.cleanshield.app.data.local.dao.TimeSafeDao
import com.cleanshield.app.data.remote.GroqApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): CleanShieldDatabase {
        return Room.databaseBuilder(
            context,
            CleanShieldDatabase::class.java,
            "cleanshield_db"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideJournalDao(db: CleanShieldDatabase): JournalDao = db.journalDao()

    @Provides
    fun provideChallengeDao(db: CleanShieldDatabase): ChallengeDao = db.challengeDao()

    @Provides
    fun provideTimeSafeDao(db: CleanShieldDatabase): TimeSafeDao = db.timeSafeDao()

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    @Provides
    @Singleton
    fun provideGroqApiService(client: OkHttpClient): GroqApiService {
        return Retrofit.Builder()
            .baseUrl("https://api.groq.com/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GroqApiService::class.java)
    }
}
