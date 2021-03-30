package com.codelixir.retrofit.util

import android.app.Activity
import android.app.PendingIntent
import android.content.*
import android.content.Intent.EXTRA_CHOSEN_COMPONENT
import android.content.pm.ComponentInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Address
import android.location.Geocoder
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.text.TextUtils
import android.text.format.DateUtils
import android.view.*
import android.webkit.MimeTypeMap
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.core.app.ShareCompat
import androidx.core.content.FileProvider
import androidx.core.text.HtmlCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.codelixir.retrofit.Application
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.reflect.KClass


fun View.hide() {
    this.visibility = View.GONE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.show(visible: Boolean = true) {
    this.visibility = if (visible) View.VISIBLE else View.GONE
}

fun View.isVisible() = visibility == View.VISIBLE
fun View.isInvisible() = visibility == View.INVISIBLE
fun View.isGone() = visibility == View.GONE

/**
 * Shows an array of passed views
 */
fun <T : View> List<T>.show() = forEach { view -> view.show() }

/**
 * Hides an array of passed views
 */
fun <T : View> List<T>.hide() = forEach { view -> view.hide() }

fun <T : Any> T.getClass(): KClass<T> {
    return javaClass.kotlin
}

operator fun ViewGroup.get(index: Int): View? = getChildAt(index)

operator fun ViewGroup.plusAssign(child: View) = addView(child)

operator fun ViewGroup.minusAssign(child: View) = removeView(child)

operator fun ViewGroup.contains(child: View) = indexOfChild(child) != -1

inline fun <T : View> T.afterMeasured(crossinline block: T.() -> Unit) {
    viewTreeObserver.addOnGlobalLayoutListener(object :
        ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            if (measuredWidth > 0 && measuredHeight > 0) {
                viewTreeObserver.removeOnGlobalLayoutListener(this)
                block()
            }
        }
    })
}


/*@JvmOverloads
fun ImageView.load(url: String?, @DrawableRes placeHolder: Int = 0) {
    GlideApp.with(this)
        .load(url)
        .placeholder(placeHolder)
        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
        .into(this)
}*/


fun Int.dpToPx(context: Context): Float = this.times(context.resources.displayMetrics.density)

fun Float.pxToDp(context: Context): Int =
    this.div(context.resources.displayMetrics.density).toInt()

fun setHtml(textView: TextView, text: String) {
    textView.text = HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_MODE_LEGACY)
}

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}

fun View.setMargin(top: Int? = null, bottom: Int? = null, left: Int? = null, right: Int? = null) =
    (layoutParams as? ViewGroup.MarginLayoutParams)?.let { lp ->
        lp.setMargins(
            left?.dpToPx(context)?.toInt() ?: lp.leftMargin,
            top?.dpToPx(context)?.toInt() ?: lp.topMargin,
            right?.dpToPx(context)?.toInt() ?: lp.rightMargin,
            bottom?.dpToPx(context)?.toInt() ?: lp.bottomMargin
        )
        layoutParams = lp
    }

fun View.getBitmap(): Bitmap {
    return Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888).apply {
        this@getBitmap.draw(Canvas(this))
    }
}

fun getImageFileFromBitmap(bitmap: Bitmap): File =
    File.createTempFile(
        UUID.randomUUID().toString(),
        ".jpg",
        Application.context.externalCacheDir
    )
        .apply {
            FileOutputStream(this).apply {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, this)
                flush()
                close()
            }
        }.apply { deleteOnExit() }

suspend fun View.getBitmapAsImageFile(): File {
    return getImageFileFromBitmap(
        getBitmap()
    )
}

fun Activity.shareFile(file: File, shareTitle: String) {
    val type = try {
        MimeTypeMap.getSingleton().getMimeTypeFromExtension(file.extension)
    } catch (e: Exception) {
        "image/*"
    }

    val uri = FileProvider.getUriForFile(
        applicationContext,
        applicationContext.packageName + ".provider",
        file
    )

    val intent = ShareCompat.IntentBuilder.from(this)
        .setStream(uri)
        .setType(type)
        .setText(uri.toString())
        .setEmailTo(arrayOf("mail@codelixir.com"))
        .setSubject(shareTitle)
        .intent
        .apply {
            clipData = ClipData.newRawUri(null, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) //FLAG_GRANT_PERSISTABLE_URI_PERMISSION
            putExtra(Intent.EXTRA_TITLE, shareTitle)
        }

/*    val resInfoList =
        packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
    for (resolveInfo in resInfoList) {
        val packageName = resolveInfo.activityInfo.packageName
        grantUriPermission(
            packageName,
            uri,
            Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
        )
    }*/

    val br = object : BroadcastReceiver() {
        override fun onReceive(contxt: Context?, intent: Intent?) {
            val componentInfo: ComponentName =
                intent?.getParcelableExtra(EXTRA_CHOSEN_COMPONENT) as ComponentName
            toast(
                this@shareFile,
                "Received by: " +
                        getAppNameFromPackageName(
                            componentInfo.packageName
                        ) +
                        " (" + componentInfo.packageName + ")"
            )
        }
    }

    val pi = PendingIntent.getBroadcast(
        this, 0,
        Intent("SHARE_CHOSEN_COMPONENT"),
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    this.registerReceiver(br, IntentFilter("SHARE_CHOSEN_COMPONENT"))

    val chooserIntent = Intent.createChooser(intent, "Share", pi.intentSender)
    startActivity(chooserIntent)
}


fun Context.getAppNameFromPackageName(packageName: String): String? {
    return try {
        val packageManager = packageManager
        val info =
            packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
        packageManager.getApplicationLabel(info) as String
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
        ""
    }
}

suspend fun coroutineContext(): CoroutineContext = suspendCoroutine { it.resume(it.context) }

suspend inline fun <reified T> runSuspended(crossinline task: () -> T): T {
    coroutineContext().let {
        return withContext(it) {
            return@withContext async(Dispatchers.IO) { task() }.await()
        }
    }
}

fun String.formatDate(inputFormat: String, outputFormat: String): String? {
    val inputFormatter = SimpleDateFormat(inputFormat)
    val date: Date = inputFormatter.parse(this)
    val outputFormatter = SimpleDateFormat(outputFormat)
    return outputFormatter.format(date)
}

fun TextView.setTextOrHide(text: String?) {
    if (!TextUtils.isEmpty(text)) {
        this.text = text
        show()
    } else {
        hide()
    }
}

fun toast(context: Context, text: String?) =
    Toast.makeText(context, "$text", Toast.LENGTH_SHORT).show()

fun Date.calculateAge(): String =
    ((Date().time - this.time) / DateUtils.DAY_IN_MILLIS / 365).toString()

fun Long.elapsedTime(): Long {
    return (System.currentTimeMillis() / 1000) - this
}

fun Any.toJson(): String = Gson().toJson(this)

inline fun <reified T> fromJson(json: String): T? = Gson().fromJson(json, T::class.java)

fun handlerCompat(ellapsedTime: Long, callback: () -> Unit) {
    val handler = android.os.Handler()
    handler.postDelayed(callback, ellapsedTime)
}

fun Context.openUrl(context: Context, url: String) {
    val i = Intent(Intent.ACTION_VIEW)
    i.data = Uri.parse(url)
    context.startActivity(i)
}

val Context.windowManager
    get() = getSystemService(Context.WINDOW_SERVICE) as WindowManager

val Context.connectivityManager
    get() = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

fun <T : Context> Context.startActivity(type: Class<T>) {
    val intent = Intent(this, type)
    startActivity(intent)
}

inline fun <reified T : Activity> Activity.startActivity() {
    val intent = Intent()
    intent.setClass(this, T::class.java)
    startActivity(intent)
}

fun getAddressFromLatLong(context: Context, lat: Double, long: Double): Address? {
    val addresses: List<Address>
    val geocoder = Geocoder(context, Locale.getDefault())

    try {
        addresses = geocoder.getFromLocation(
            lat,
            long,
            1
        ) // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        return addresses.firstOrNull()
    } catch (e: Exception) {
        e.printStackTrace()
        return null
    }
}

fun Any?.isNull() = this == null
fun Any?.isNotNull() = isNull().not()

fun String.isNumeric(): Boolean = this matches "-?\\d+(\\.\\d+)?".toRegex()
fun String.removeWhitespaces(): String {
    return this.replace("[\\s-]*".toRegex(), "")
}

/*fun String.getJaroWinklerDistance(str2: String): Int {
    val decimalFormat = DecimalFormat("0.00")
    decimalFormat.roundingMode = RoundingMode.UP

    val distance = JaroWinklerDistance.getDistance(toByteArray(), str2.toByteArray())
    val formatted = decimalFormat.format(distance)

    //we will round up the distance value by 2 decimal points for getting a better result
    return (NumberFormat.getInstance().parse(formatted).toFloat() * 100).roundToInt()
}*/

inline fun <reified T> runIfConnected(context: Context, task: () -> T) {
    if (hasInternet(context))
        task.invoke()
}

fun hasInternet(context: Context): Boolean {
    var result = false
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val networkCapabilities = connectivityManager.activeNetwork ?: return false
        val actNw =
            connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
        result = when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    } else {
        connectivityManager.run {
            connectivityManager.activeNetworkInfo?.run {
                result = when (type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }

            }
        }
    }
    return result
}

fun Int.addMask(mask: Int): Int = this or mask
fun Int.clearMask(mask: Int): Int = this xor mask
fun Int.hasMask(mask: Int): Boolean = (this and mask) == mask

fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
    observe(lifecycleOwner, object : Observer<T> {
        override fun onChanged(t: T?) {
            observer.onChanged(t)
            removeObserver(this)
        }
    })
}

fun Lifecycle.isInResume() = currentState.isAtLeast(Lifecycle.State.RESUMED)
fun Lifecycle.isNotInResume() = isInResume().not()
