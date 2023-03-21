package co.nimblehq.loyalty.sdk.poc

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import co.nimblehq.loyalty.sdk.LoyaltySdk

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<View>(R.id.mainSignInButton).setOnClickListener {
            LoyaltySdk.instance.authenticate(this)
        }
    }
}
