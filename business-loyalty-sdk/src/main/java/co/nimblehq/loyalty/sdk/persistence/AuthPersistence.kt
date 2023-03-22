package co.nimblehq.loyalty.sdk.persistence

interface AuthPersistence {
    fun saveAccessToken(accessToken: String)
    fun saveTokenType(tokenType: String)
    fun getAccessToken(): String?
    fun getTokenType(): String?
}

internal class AuthPersistenceImpl constructor(
    private val sharedPrefs: LoyaltySdkEncryptedSharedPrefs
): AuthPersistence {
    override fun saveAccessToken(accessToken: String) {
        sharedPrefs.set(KEY_ACCESS_TOKEN, accessToken)
    }

    override fun saveTokenType(tokenType: String) {
        sharedPrefs.set(KEY_TOKEN_TYPE, tokenType)
    }

    override fun getAccessToken(): String? {
        return sharedPrefs.get<String>(KEY_ACCESS_TOKEN)
    }

    override fun getTokenType(): String? {
        return sharedPrefs.get<String>(KEY_TOKEN_TYPE)
    }

}