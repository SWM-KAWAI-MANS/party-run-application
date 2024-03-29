package online.partyrun.partyrunapplication.core.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import online.partyrun.partyrunapplication.core.data.repository.MatchRepository
import online.partyrun.partyrunapplication.core.data.repository.MatchRepositoryImpl
import online.partyrun.partyrunapplication.core.data.repository.SignInRepositoryImpl
import online.partyrun.partyrunapplication.core.data.repository.SignInRepository
import online.partyrun.partyrunapplication.core.data.repository.AgreementRepository
import online.partyrun.partyrunapplication.core.data.repository.AgreementRepositoryImpl
import online.partyrun.partyrunapplication.core.data.repository.BattleRepository
import online.partyrun.partyrunapplication.core.data.repository.BattleRepositoryImpl
import online.partyrun.partyrunapplication.core.data.repository.GoogleAuthRepository
import online.partyrun.partyrunapplication.core.data.repository.GoogleAuthRepositoryImpl
import online.partyrun.partyrunapplication.core.data.repository.ResultRepository
import online.partyrun.partyrunapplication.core.data.repository.ResultRepositoryImpl
import online.partyrun.partyrunapplication.core.data.repository.TokenRepository
import online.partyrun.partyrunapplication.core.data.repository.TokenRepositoryImpl
import online.partyrun.partyrunapplication.core.data.repository.MemberRepository
import online.partyrun.partyrunapplication.core.data.repository.MemberRepositoryImpl
import online.partyrun.partyrunapplication.core.data.repository.PartyRepository
import online.partyrun.partyrunapplication.core.data.repository.PartyRepositoryImpl
import online.partyrun.partyrunapplication.core.data.repository.SingleRepository
import online.partyrun.partyrunapplication.core.data.repository.SingleRepositoryImpl
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
    fun bindSignInRepository(
        signInRepository: SignInRepositoryImpl
    ): SignInRepository

    @Singleton
    @Binds
    fun bindGoogleAuthRepository(
        googleAuthRepository: GoogleAuthRepositoryImpl
    ): GoogleAuthRepository

    @Singleton
    @Binds
    abstract fun bindMatchRepository(
        matchRepository: MatchRepositoryImpl
    ): MatchRepository

    @Singleton
    @Binds
    fun bindAgreementRepository(
        agreementRepository: AgreementRepositoryImpl
    ): AgreementRepository

    @Singleton
    @Binds
    fun bindTokenRepository(
        tokenRepository: TokenRepositoryImpl
    ): TokenRepository

    @Singleton
    @Binds
    fun bindBattleRepository(
        battleRepository: BattleRepositoryImpl
    ): BattleRepository

    @Singleton
    @Binds
    fun bindSingleRepository(
        singleRepository: SingleRepositoryImpl
    ): SingleRepository

    @Singleton
    @Binds
    fun bindPartyRepository(
        partyRepository: PartyRepositoryImpl
    ): PartyRepository

    @Singleton
    @Binds
    fun bindResultRepository(
        resultRepository: ResultRepositoryImpl
    ): ResultRepository

    @Singleton
    @Binds
    fun bindMemberRepository(
        memberRepository: MemberRepositoryImpl
    ): MemberRepository
}
