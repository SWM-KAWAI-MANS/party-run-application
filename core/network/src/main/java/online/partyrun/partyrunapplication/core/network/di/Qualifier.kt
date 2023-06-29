package online.partyrun.partyrunapplication.core.network.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class SSEOkHttpClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RESTOkHttpClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class SSERequestBuilder
