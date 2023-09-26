package online.partyrun.partyrunapplication.core.common.network

interface TokenExpirationNotifier {
    fun notifyRefreshTokenExpired()
}
