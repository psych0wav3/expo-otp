package expo.modules.otp

import android.content.Context
import android.content.IntentFilter
import android.os.Bundle
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.WritableArray
import com.google.android.gms.auth.api.phone.SmsRetriever
import expo.modules.kotlin.Promise

class ExpoOtpService(private val sendEvent: (body: Bundle) -> Unit) {
  private val intentFilter = IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION)
  
  fun getHash(context: Context): WritableArray {
    val helper = AppSignatureHelper(context)
    val signatures: ArrayList<String> = helper.getAppSignatures()
    val arr: WritableArray = Arguments.createArray()
    for (s in signatures) {
      arr.pushString(s)
    }
    return arr
  }
  
  fun startSmsRetriever(context: Context, promise: Promise) {
    try{
      val client = SmsRetriever.getClient(context)
      val task = client.startSmsRetriever()
      task.addOnSuccessListener { promise.resolve(null) }
      task.addOnFailureListener { e: Exception -> promise.reject("SMS_OTP_START","FAIL", e)}
    } catch (e: Exception) {
      promise.reject("SMS_OTP_START","FAIL", e)
    }
  }
  
  fun registerOtpBroadcastReceiver(context: Context) {
    context.registerReceiver(ExpoOtpBroadcast(sendEvent), intentFilter)
  }
  
  fun unregisterOtpBroadcastReceivers(context: Context) {
    context.unregisterReceiver(ExpoOtpBroadcast(sendEvent))
  }
}