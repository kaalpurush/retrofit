import android.annotation.SuppressLint
import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.startActivity
import com.codelixir.retrofit.R
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader


object RomUtils {
    private const val TAG = "RomUtils"
    const val IGNORE_BATTERY_OPTIMIZATIONS_REQUEST_CODE = 101

    fun Context.hasBatteryOptimization(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            (this.getSystemService(Context.POWER_SERVICE) as? PowerManager)?.let { pm ->
                return !pm.isIgnoringBatteryOptimizations(packageName)
            }
        }
        return false
    }

    @SuppressLint("BatteryLife")
    fun Activity.askIgnoreBatteryOptimization(): Boolean {
        Intent().let { intent ->
            intent.action = Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
            intent.data = Uri.fromParts("package", packageName, null)
            return try {
                startActivityForResult(
                    intent,
                    IGNORE_BATTERY_OPTIMIZATIONS_REQUEST_CODE
                )
                true
            } catch (ex: Throwable) {
                false
            }
        }
    }

    @SuppressLint("BatteryLife")
    fun Activity.askIgnoreBatteryOptimization(startForResult: ActivityResultLauncher<Intent>? = null): Boolean {
        Intent().let { intent ->
            intent.action = Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
            intent.data = Uri.fromParts("package", packageName, null)
            return try {
                startForResult?.let {
                    it.launch(intent)
                } ?: run {
                    startActivityForResult(intent, IGNORE_BATTERY_OPTIMIZATIONS_REQUEST_CODE)
                }
                true
            } catch (ex: Throwable) {
                false
            }
        }
    }

    fun Activity.askMiuiIgnoreBatteryOptimization(startForResult: ActivityResultLauncher<Intent>? = null): Boolean {
        return try {
            val intent = Intent("miui.intent.action.HIDDEN_APPS_CONFIG_ACTIVITY")
            //            intent.setComponent(new ComponentName("com.miui.powerkeeper",
            //                    "com.miui.powerkeeper.ui.HiddenAppsConfigActivity"));
            intent.putExtra("package_name", packageName)
            intent.putExtra("package_label", getString(R.string.app_name))
            startForResult?.let {
                it.launch(intent)
            } ?: run {
                startActivityForResult(intent, IGNORE_BATTERY_OPTIMIZATIONS_REQUEST_CODE)
            }
            true
        } catch (ex: Exception) {
            false
        }
    }

    fun isMiui(): Boolean {
        return !TextUtils.isEmpty(getSystemProperty("ro.miui.ui.version.name"))
    }

    fun getMiuiVersion(): Int? {
        val version: String? = getSystemProperty("ro.miui.ui.version.name")
        try {
            return version?.substring(1)?.toInt()
        } catch (e: Exception) {

        }
        return null
    }

    fun getSystemProperty(propName: String): String? {
        val line: String
        var input: BufferedReader? = null
        try {
            val p = Runtime.getRuntime().exec("getprop $propName")
            input = BufferedReader(InputStreamReader(p.inputStream), 1024)
            line = input.readLine()
            input.close()
        } catch (ex: IOException) {
            Log.e(TAG, "Unable to read sysprop $propName", ex)
            return null
        } finally {
            if (input != null) {
                try {
                    input.close()
                } catch (e: IOException) {
                    Log.e(TAG, "Exception while closing InputStream", e)
                }
            }
        }
        return line
    }

    fun isMeizu(): Boolean {
        return Build.MANUFACTURER.contains("Meizu")
    }

    fun Context.askAutoRestart() {
        val manufacturer = Build.MANUFACTURER
        try {
            val intent = Intent()
            if ("xiaomi".equals(manufacturer, ignoreCase = true)) {
                intent.component = ComponentName(
                    "com.miui.securitycenter",
                    "com.miui.permcenter.autostart.AutoStartManagementActivity"
                )
            } else if ("oppo".equals(manufacturer, ignoreCase = true)) {
                intent.component = ComponentName(
                    "com.coloros.safecenter",
                    "com.coloros.safecenter.permission.startup.StartupAppListActivity"
                )
            } else if ("vivo".equals(manufacturer, ignoreCase = true)) {
                intent.component = ComponentName(
                    "com.vivo.permissionmanager",
                    "com.vivo.permissionmanager.activity.BgStartUpManagerActivity"
                )
            } else if ("Letv".equals(manufacturer, ignoreCase = true)) {
                intent.component = ComponentName(
                    "com.letv.android.letvsafe",
                    "com.letv.android.letvsafe.AutobootManageActivity"
                )
            } else if ("Honor".equals(manufacturer, ignoreCase = true)) {
                intent.component = ComponentName(
                    "com.huawei.systemmanager",
                    "com.huawei.systemmanager.optimize.process.ProtectActivity"
                )
            } else if ("meizu".equals(manufacturer, ignoreCase = true)) {
                intent.setAction("com.meizu.safe.security.SHOW_APPSEC")
                intent.addCategory(Intent.CATEGORY_DEFAULT)
                intent.putExtra("packageName", packageName)
            }
            val list: List<ResolveInfo> =
                getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
            if (list.size > 0) {
                startActivity(intent)
            }
        } catch (e: Exception) {

        }
    }
}