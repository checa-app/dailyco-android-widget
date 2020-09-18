package co.daily.android.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.webkit.CookieManager
import android.webkit.WebView


class DailycoWebView : WebView {
  constructor(context: Context) : super(context) {
    initView(context)
  }

  constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
    initView(context)
  }

  fun setOnEventListener(onEvent: (event: DailycoEvent) -> Unit = {}) {
    webViewClient = ChromeClientHandler({
      onEvent.invoke(DailycoEvent.OnLoadFinish)
    }, {
      onEvent.invoke(DailycoEvent.OnLoadError(it))
    })
    addJavascriptInterface(VideoWebViewInterface(onEvent), "VideoWebView")
  }

  fun enableDebugMode(enable: Boolean) {
    if (enable) {
      setWebContentsDebuggingEnabled(true)
    }
  }

  fun useDefaultTemplate() {
    loadUrl("file:///android_asset/video.html")
  }

  fun setVideoUiFile(videoUiFile: String) {
    loadUrl("file:///android_asset/${videoUiFile}")
  }

  fun loadRoom(room: String) {
    loadUrl("javascript:loadRoom(\"${url}\");")
  }

  fun runFunction(functionName: String) {
    loadUrl("javascript:${functionName};")
  }

  @SuppressLint("SetJavaScriptEnabled")
  private fun initView(context: Context) {
    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    // Enable Javascript
    settings.javaScriptEnabled = true

    // Use WideViewport and Zoom out if there is no viewport defined
    settings.useWideViewPort = true
    settings.loadWithOverviewMode = true

    // Enable pinch to zoom without the zoom buttons
    settings.builtInZoomControls = true

    // Allow use of Local Storage
    settings.domStorageEnabled = true

    // Hide the zoom controls
    settings.displayZoomControls = false

    settings.mediaPlaybackRequiresUserGesture = false

    // AppRTC requires third party cookies to work
    val cookieManager: CookieManager = CookieManager.getInstance()
    cookieManager.setAcceptThirdPartyCookies(this, true)

    webChromeClient = WebChromeClientPermissions()
  }
}

sealed class DailycoEvent {
  object OnLoadFinish : DailycoEvent()
  class OnLoadError(val error: String) : DailycoEvent()
  object OnJoining : DailycoEvent()
  object OnJoined : DailycoEvent()
  object OnLeft : DailycoEvent()
  object OnParticipantJoined : DailycoEvent()
  object OnParticipantUpdated : DailycoEvent()
  object OnParticipantLeft : DailycoEvent()
  object OnError : DailycoEvent()
}