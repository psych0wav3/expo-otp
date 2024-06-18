package expo.modules.otp

import android.content.Context
import android.content.ContextWrapper
import android.content.pm.PackageManager
import android.util.Base64
import android.util.Log
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*

class AppSignatureHelper(context: Context) : ContextWrapper(context) {
  companion object {
    const val TAG = "AppSignatureHelper"
    private const val HASH_TYPE = "SHA-256"
    const val NUM_HASHED_BYTES = 9
    const val NUM_BASE64_CHAR = 11
  }
  
  fun getAppSignatures(): ArrayList<String> {
    val appCodes: ArrayList<String> = ArrayList()
    try {
      val packageName = packageName
      val packageManager: PackageManager = packageManager
      val signatures = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES).signatures
      
      for (signature in signatures) {
        val hash = hash(packageName, signature.toCharsString())
        hash?.let {
          appCodes.add(String.format("%s", it))
        }
      }
    } catch (e: PackageManager.NameNotFoundException) {
      Log.e(TAG, "Unable to find package to obtain hash.", e)
    }
    return appCodes
  }

  private fun hash(packageName: String, signature: String): String? {
    val appInfo = "$packageName $signature"
    return try {
      val messageDigest: MessageDigest = MessageDigest.getInstance(HASH_TYPE)
        messageDigest.update(appInfo.toByteArray(StandardCharsets.UTF_8))

      var hashSignature: ByteArray = messageDigest.digest()
      
      hashSignature = hashSignature.copyOfRange(0, NUM_HASHED_BYTES)

      var base64Hash: String = Base64.encodeToString(hashSignature, Base64.NO_PADDING or Base64.NO_WRAP)
      base64Hash = base64Hash.substring(0, NUM_BASE64_CHAR)

      Log.d(TAG, String.format("pkg: %s -- hash: %s", packageName, base64Hash))
      base64Hash
    } catch (e: NoSuchAlgorithmException) {
      Log.e(TAG, "hash:NoSuchAlgorithm", e)
      null
    }
  }
}