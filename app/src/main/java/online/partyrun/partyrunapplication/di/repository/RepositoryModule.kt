package online.partyrun.partyrunapplication.di.repository

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import online.partyrun.partyrunapplication.data.repository.SignInRepositoryImpl
import online.partyrun.partyrunapplication.data.repository.TestRepositoryImpl
import online.partyrun.partyrunapplication.domain.repository.SignInRepository
import online.partyrun.partyrunapplication.domain.repository.TestRepository
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

    @Singleton
    @Binds
    abstract fun bindSignInRepository (
        signInRepositoryImpl: SignInRepositoryImpl
    ): SignInRepository

}
