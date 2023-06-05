package online.partyrun.partyrunapplication.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import online.partyrun.partyrunapplication.network.api.mockTest.TestApiService
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteModule {

    /*
    @Singleton
    @Provides
    fun provideAuthAPIService(retrofit: Retrofit.Builder): AuthApiService =
        retrofit
            .build()
            .create(AuthApiService::class.java)
     */

    @Provides
    @Singleton
    fun provideTestApiService(okHttpClient: OkHttpClient, retrofit: Retrofit.Builder): TestApiService =
        retrofit
            .client(okHttpClient)
            .build()
            .create(TestApiService::class.java)
}
