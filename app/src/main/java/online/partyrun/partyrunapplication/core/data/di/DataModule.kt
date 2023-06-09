package online.partyrun.partyrunapplication.core.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import online.partyrun.partyrunapplication.core.data.repository.SignInRepositoryImpl
import online.partyrun.partyrunapplication.core.data.repository.TestRepositoryImpl
import online.partyrun.partyrunapplication.core.data.repository.SignInRepository
import online.partyrun.partyrunapplication.core.data.repository.TestRepository
import javax.inject.Singleton

/*
    interface인 Repository를 주입하기 위한
    DataModule 작성
*/

@Module
@InstallIn(SingletonComponent::class)
internal interface DataModule {

    @Singleton
    @Binds
    fun bindTestRepository (
        testRepository: TestRepositoryImpl
    ): TestRepository

    @Singleton
    @Binds
    fun bindSignInRepository (
        signInRepository: SignInRepositoryImpl
    ): SignInRepository

}
