package co.nimblehq.loyalty.sdk.ui.authenticate

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import co.nimblehq.common.extensions.gone
import co.nimblehq.common.extensions.visible
import co.nimblehq.loyalty.sdk.BuildConfig
import co.nimblehq.loyalty.sdk.LoyaltySdk
import co.nimblehq.loyalty.sdk.R
import co.nimblehq.loyalty.sdk.databinding.ActivityAuthenticationBinding
import co.nimblehq.loyalty.sdk.persistence.AuthPersistence
import co.nimblehq.loyalty.sdk.persistence.PersistenceProvider
import co.nimblehq.loyalty.sdk.repository.AuthenticationRepository
import kotlinx.coroutines.*
import java.net.URL

private const val CLIENT_ID = "client_id"
private const val REDIRECT_URI = "redirect_uri"
private const val RESPONSE_TYPE = "response_type"
private const val RESPONSE_TYPE_CODE = "code"
private const val SCOPE = "scope"
private const val SCOPE_READ = "read"

private val TAG = AuthenticationActivity::class.simpleName

class AuthenticationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthenticationBinding

    private val authenticationRepository: AuthenticationRepository by lazy {
        LoyaltySdk.getInstance().authenticationRepository
    }

    private val authPersistence: AuthPersistence by lazy {
        PersistenceProvider.getAuthPersistence(this.applicationContext)
    }

    private val loginWebViewClient by lazy {
        object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                request?.url?.let {
                    if (it.toString().startsWith(getRedirectUrl())) {
                        // Parse the redirect URL query parameters to get the code
                        it.getQueryParameter(RESPONSE_TYPE_CODE)?.let { code ->
                            requestToken(getRedirectUrl(), code)
                        } ?: run {
                            // TODO Users cancelled the login flow
                        }
                    }
                }
                return super.shouldOverrideUrlLoading(view, request)
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                showLoading()
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                hideLoading()
            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                super.onReceivedError(view, request, error)
                view?.gone()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAuthenticationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onResume() {
        super.onResume()

        with(binding.webViewAuthentication) {
            webViewClient = loginWebViewClient
            loadUrl(getLoadingUrl())
        }
    }

    private fun showLoading() {
        binding.pbAuthentication.visible()
    }

    private fun hideLoading() {
        binding.pbAuthentication.gone()
    }

    private fun getLoadingUrl(): String {
        val uri = Uri.parse("${BuildConfig.AUTHENTICATION_API_URL}authorize")
            .buildUpon()
            .appendQueryParameter(CLIENT_ID, LoyaltySdk.getInstance().clientId)
            .appendQueryParameter(REDIRECT_URI, getRedirectUrl())
            .appendQueryParameter(RESPONSE_TYPE, RESPONSE_TYPE_CODE)
            .appendQueryParameter(SCOPE, SCOPE_READ)
            .build()
        return URL(uri.toString()).toString().also {
            Log.d(TAG, "Loading url >>> $it")
        }
    }

    private fun getRedirectUrl() = "${getString(R.string.login_scheme)}://" +
            "${getString(R.string.login_host)}${getString(R.string.login_path)}"

    @OptIn(DelicateCoroutinesApi::class)
    private fun requestToken(redirectUrl: String, code: String) {
        GlobalScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                showLoading()
            }
            val token = authenticationRepository.requestAccessToken(
                clientId = LoyaltySdk.getInstance().clientId,
                clientSecret = LoyaltySdk.getInstance().clientSecret,
                code = code,
                redirectUri = redirectUrl
            )
            authPersistence.saveAccessToken(token.accessToken.orEmpty())
            authPersistence.saveTokenType(token.tokenType.orEmpty())
            withContext(Dispatchers.Main) {
                hideLoading()
                finish()
            }
        }
    }
}