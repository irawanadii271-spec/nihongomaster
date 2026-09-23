package com.example.nihongomaster.ui.components

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.view.ViewGroup
import android.webkit.*
import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.nihongomaster.util.NetworkUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

const val TARGET_URL = "https://nihongo-master-jlpt.vercel.app/"

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun NihongoWebViewScreen(
    url: String = TARGET_URL,
    onBackPressWhenRoot: () -> Unit = {}
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var webViewInstance by remember { mutableStateOf<WebView?>(null) }
    var canGoBackState by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }
    var isInitialLoad by remember { mutableStateOf(true) }
    var loadProgress by remember { mutableIntStateOf(0) }
    var hasError by remember { mutableStateOf(false) }
    var errorDescription by remember { mutableStateOf<String?>(null) }
    var isRetrying by remember { mutableStateOf(false) }

    // Physical Back Button Handling
    BackHandler(enabled = true) {
        if (webViewInstance != null && webViewInstance!!.canGoBack()) {
            webViewInstance!!.goBack()
        } else {
            onBackPressWhenRoot()
        }
    }

    // Function to reload / retry
    val performRetry = {
        coroutineScope.launch {
            isRetrying = true
            hasError = false
            errorDescription = null

            val isOnline = NetworkUtils.isNetworkAvailable(context)
            if (isOnline) {
                if (webViewInstance != null) {
                    webViewInstance?.reload()
                }
            } else {
                delay(800)
                hasError = true
                errorDescription = "Koneksi internet belum tersedia."
            }
            isRetrying = false
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .imePadding() // Adjusts automatically when virtual keyboard (IME) is visible
    ) {
        // Main WebView
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { ctx ->
                WebView(ctx).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )

                    // WebSettings configuration
                    settings.apply {
                        javaScriptEnabled = true
                        domStorageEnabled = true
                        databaseEnabled = true
                        mediaPlaybackRequiresUserGesture = false // Allow autoplay audio/media
                        allowFileAccess = true
                        allowContentAccess = true
                        loadWithOverviewMode = true
                        useWideViewPort = true
                        builtInZoomControls = true
                        displayZoomControls = false
                        setSupportZoom(true)
                        mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                        cacheMode = WebSettings.LOAD_DEFAULT

                        // Modern Mobile User Agent enhancement
                        val originalUa = userAgentString
                        userAgentString = "$originalUa NihongoMasterApp/1.0.0"
                    }

                    webChromeClient = object : WebChromeClient() {
                        override fun onProgressChanged(view: WebView?, newProgress: Int) {
                            super.onProgressChanged(view, newProgress)
                            loadProgress = newProgress
                            if (newProgress >= 100) {
                                isLoading = false
                                if (isInitialLoad) {
                                    isInitialLoad = false
                                }
                            }
                        }

                        override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
                            return super.onConsoleMessage(consoleMessage)
                        }
                    }

                    webViewClient = object : WebViewClient() {
                        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                            super.onPageStarted(view, url, favicon)
                            isLoading = true
                            canGoBackState = view?.canGoBack() == true
                        }

                        override fun onPageFinished(view: WebView?, url: String?) {
                            super.onPageFinished(view, url)
                            isLoading = false
                            canGoBackState = view?.canGoBack() == true
                            if (isInitialLoad) {
                                isInitialLoad = false
                            }
                        }

                        override fun onReceivedError(
                            view: WebView?,
                            request: WebResourceRequest?,
                            error: WebResourceError?
                        ) {
                            super.onReceivedError(view, request, error)
                            // Only trigger fullscreen error if main frame fails
                            if (request?.isForMainFrame == true) {
                                hasError = true
                                val desc = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    error?.description?.toString()
                                } else {
                                    "Gagal memuat halaman."
                                }
                                errorDescription = desc
                                isLoading = false
                            }
                        }

                        @Deprecated("Deprecated in Java")
                        override fun onReceivedError(
                            view: WebView?,
                            errorCode: Int,
                            description: String?,
                            failingUrl: String?
                        ) {
                            @Suppress("DEPRECATION")
                            super.onReceivedError(view, errorCode, description, failingUrl)
                            if (failingUrl == null || failingUrl == url || failingUrl.startsWith("http")) {
                                hasError = true
                                errorDescription = description
                                isLoading = false
                            }
                        }

                        override fun shouldOverrideUrlLoading(
                            view: WebView?,
                            request: WebResourceRequest?
                        ): Boolean {
                            val uri = request?.url ?: return false
                            val uriString = uri.toString()

                            // If standard http/https inside our target domain or web links
                            if (uriString.startsWith("http://") || uriString.startsWith("https://")) {
                                return false // Let WebView load it
                            }

                            // Handle external intents: tel, mailto, whatsapp, market, etc.
                            return try {
                                val intent = Intent(Intent.ACTION_VIEW, uri)
                                ctx.startActivity(intent)
                                true
                            } catch (_: Exception) {
                                true
                            }
                        }
                    }

                    // Initial load
                    if (!NetworkUtils.isNetworkAvailable(ctx)) {
                        hasError = true
                        errorDescription = "Tidak ada koneksi internet."
                        isLoading = false
                    } else {
                        loadUrl(url)
                    }

                    webViewInstance = this
                }
            },
            update = { webView ->
                webViewInstance = webView
                canGoBackState = webView.canGoBack()
            }
        )

        // Top Linear Progress Bar when navigating (not initial splash)
        if (isLoading && !isInitialLoad && !hasError) {
            LinearProgressIndicator(
                progress = { loadProgress / 100f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.5.dp)
                    .align(Alignment.TopCenter),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
            )
        }

        // Custom Splash Screen on initial open
        AnimatedVisibility(
            visible = isInitialLoad && !hasError,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            SplashScreenOverlay(
                progress = loadProgress,
                modifier = Modifier.fillMaxSize()
            )
        }

        // Custom Network Error Screen
        AnimatedVisibility(
            visible = hasError,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            NetworkErrorScreen(
                errorMessage = errorDescription,
                isRetrying = isRetrying,
                onRetry = { performRetry() },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
