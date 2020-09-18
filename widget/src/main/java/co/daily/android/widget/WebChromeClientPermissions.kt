package co.daily.android.widget

import android.webkit.PermissionRequest
import android.webkit.WebChromeClient

class WebChromeClientPermissions : WebChromeClient() {

  override fun onPermissionRequest(request: PermissionRequest) {
    request.grant(request.resources)
  }
}