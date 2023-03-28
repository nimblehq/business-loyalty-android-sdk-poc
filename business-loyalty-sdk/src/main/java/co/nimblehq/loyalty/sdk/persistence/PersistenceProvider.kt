package co.nimblehq.loyalty.sdk.persistence

import android.content.Context

internal object PersistenceProvider {

    fun getAuthPersistence(context: Context): AuthPersistence {
        return AuthPersistenceImpl(
            LoyaltySdkEncryptedSharedPrefs.getInstance(context)
        )
    }
}
