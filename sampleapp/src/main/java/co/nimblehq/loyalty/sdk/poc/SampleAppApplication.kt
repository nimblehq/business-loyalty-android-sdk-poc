package co.nimblehq.loyalty.sdk.poc

import android.app.Application
import co.nimblehq.loyalty.sdk.LoyaltySdk
import co.nimblehq.loyalty.sdk.model.Credentials
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class SampleAppApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        // TODO Add your keys
        LoyaltySdk.init(
            this,
            Credentials(
                clientId = "CLIENT_ID",
                clientSecret = "CLIENT_SECRET"
            ),
            BuildConfig.DEBUG
        )
    }
}
