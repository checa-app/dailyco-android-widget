package co.daily.android.widget

import android.webkit.JavascriptInterface
import co.daily.android.widget.DailycoEvent


class VideoWebViewInterface(private val onEvent: (event: DailycoEvent) -> Unit) {

  @JavascriptInterface
  fun onEvent(event: String) {
    when (event) {
      "joining" -> onEvent.invoke(DailycoEvent.OnJoining)
      "joined" -> onEvent.invoke(DailycoEvent.OnJoined)
      "left" -> onEvent.invoke(DailycoEvent.OnLeft)
      "participant-joined" -> onEvent.invoke(DailycoEvent.OnParticipantJoined)
      "participant-updated" -> onEvent.invoke(DailycoEvent.OnParticipantUpdated)
      "participant-left" -> onEvent.invoke(DailycoEvent.OnParticipantLeft)
      "error" -> onEvent.invoke(DailycoEvent.OnError)
    }
  }
}
