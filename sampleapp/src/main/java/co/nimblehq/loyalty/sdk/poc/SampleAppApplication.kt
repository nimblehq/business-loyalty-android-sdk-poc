package co.nimblehq.loyalty.sdk.poc

import android.app.Application
import co.nimblehq.loyalty.sdk.LoyaltySdk
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class SampleAppApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        // TODO Add your keys
        LoyaltySdk.Builder
            .withContext(this)
            .withDebugMode(BuildConfig.DEBUG)
            .withClientId("CLIENT_ID")
            .withClientSecret("CLIENT_SECRET")
            .init()
    }
}
