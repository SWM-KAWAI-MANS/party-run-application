package online.partyrun.partyrunapplication.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import online.partyrun.partyrunapplication.data.mockTest.TestRepositoryImpl
import online.partyrun.partyrunapplication.domain.mockTest.TestRepository
import javax.inject.Singleton

/*
    interface인 Repository를 주입하기 위한
    Repository Module 작성
*/
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindTestRepository (
        testRepositoryImpl: TestRepositoryImpl
    ): TestRepository

    /*
    @Singleton
    @Binds
    abstract fun bindAuthRepository (
        AuthRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository
     */
}
