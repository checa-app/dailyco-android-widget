package co.daily.android.widget

import android.annotation.TargetApi
import android.net.http.SslError
import android.os.Build
import android.webkit.SslErrorHandler
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient


class ChromeClientHandler(
  private val onFinish: () -> Unit = {},
  private val onError: (description: String) -> Unit = {}
) : WebViewClient() {

  @Deprecated("")
  override fun onReceivedError(
    view: WebView?,
    errorCode: Int,
    description: String,
    failingUrl: String?
  ) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
      handleLoadError(description)
    }
  }

  @TargetApi(Build.VERSION_CODES.M)
  override fun onReceivedError(
    view: WebView,
    request: WebResourceRequest,
    error: WebResourceError
  ) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      val url = request.url.toString()
      if (view.url == url) {
        handleLoadError(error.description.toString())
      }
    }
  }

  override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
    return false
  }

  override fun onReceivedSslError(
    view: WebView?,
    handler: SslErrorHandler?,
    error: SslError
  ) {
    var message: String? = null
    when (error.primaryError) {
      SslError.SSL_UNTRUSTED -> message = "The certificate authority is not trusted."
      SslError.SSL_EXPIRED -> message = "The certificate has expired."
      SslError.SSL_IDMISMATCH -> message = "The certificate Hostname mismatch."
      SslError.SSL_INVALID -> message = "SSL connection is invalid."
    }
  }

  override fun onPageFinished(view: WebView?, url: String?) {
    super.onPageFinished(view, url)
    onFinish.invoke()
  }

  private fun handleLoadError(description: String) {
    onError.invoke(description)
  }
}