package co.nimblehq.loyalty.sdk.poc

import android.app.Application
import co.nimblehq.loyalty.sdk.LoyaltySdk

class SampleAppApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        LoyaltySdk.Builder
            .withContext(this)
            .withDebugMode(BuildConfig.DEBUG)
            .withClientId("MRC6bwFASOm0sJYPYwVEt2KD51bkAtdgS7ltqeDYUf4")
            .withClientSecret("wNcVKqTlLFGkGUOYgpqDREzwiOm_8Y3MQxPzYYzzd1o")
            .init()
    }
}
