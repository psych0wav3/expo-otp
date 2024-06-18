package expo.modules.otp

import expo.modules.kotlin.modules.Module
import expo.modules.kotlin.modules.ModuleDefinition
import android.os.Bundle
import android.content.Context
import expo.modules.kotlin.exception.Exceptions
import expo.modules.kotlin.Promise

internal const val OTP_EVENT_SMS = "OptEventSms"

class ExpoOtpModule : Module() {

  private val emitEvent = { body: Bundle ->
    try {
      this@ExpoOtpModule.sendEvent(OTP_EVENT_SMS, body)
    } catch (_: Throwable) {
    }
  }

  private val context: Context
    get() = appContext.reactContext ?: throw Exceptions.ReactContextLost()

  private val otpService = ExpoOtpService(emitEvent)
  override fun definition() = ModuleDefinition {
    Name("ExpoOtp")

    Events(OTP_EVENT_SMS)

    OnCreate {
      otpService.registerOtpBroadcastReceiver(context)
    }

    OnDestroy {
      otpService.unregisterOtpBroadcastReceivers(context)
    }

    AsyncFunction("startSmsRetrieverAsync") { promise: Promise ->
      otpService.startSmsRetriever(context, promise)
    }

    Function("getHash") {
      return@Function otpService.getHash(context)
    }

  }
}
