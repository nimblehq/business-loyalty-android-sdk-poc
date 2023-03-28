package co.nimblehq.loyalty.sdk.persistence

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

private const val LOYALTY_SECRET_SHARED_PREFS = "LOYALTY_SECRET_SHARED_PREFS"

internal const val KEY_ACCESS_TOKEN = "KEY_ACCESS_TOKEN"
internal const val KEY_TOKEN_TYPE = "KEY_TOKEN_TYPE"

internal class LoyaltySdkEncryptedSharedPrefs constructor(
    applicationContext: Context
) : BaseSharedPreferences() {

    companion object {
        private lateinit var instance: LoyaltySdkEncryptedSharedPrefs

        fun getInstance(applicationContext: Context): LoyaltySdkEncryptedSharedPrefs {
            if (!::instance.isInitialized) {
                instance = LoyaltySdkEncryptedSharedPrefs(applicationContext)
            }
            return instance
        }
    }

    init {
        val masterKey = MasterKey.Builder(applicationContext)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        sharedPreferences = EncryptedSharedPreferences.create(
            applicationContext,
            LOYALTY_SECRET_SHARED_PREFS,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }
}