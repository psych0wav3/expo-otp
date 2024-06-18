package expo.modules.otp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status

class ExpoOtpBroadcast(private val sendEvent: (body: Bundle) -> Unit) : BroadcastReceiver() {
  
  override fun onReceive(context: Context, intent: Intent?) {
    if (intent?.action == SmsRetriever.SMS_RETRIEVED_ACTION) {
      val extras = intent.extras ?: return
      val status = extras[SmsRetriever.EXTRA_STATUS] as? Status? ?: return
      when (status.statusCode) {
        CommonStatusCodes.SUCCESS -> {
          val message: String? = extras.getString(SmsRetriever.EXTRA_SMS_MESSAGE)
          if (message != null) {
            val eventData = Bundle().apply {
              putString("message", message)
            }
            sendEvent(eventData)
          } else {
            val error = Bundle().apply {
              putString("error", "ups")
            }
            sendEvent(error)
          }
        }
        CommonStatusCodes.TIMEOUT -> {
          val eventData = Bundle().apply {
            putString("error", "Timeout to get SMS.")
          }
          sendEvent(eventData)
        }
      }
    }
  }
} 