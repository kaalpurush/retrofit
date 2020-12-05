package com.codelixir.retrofit.ui

import android.content.Intent
import android.net.Uri
import android.webkit.URLUtil
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController

abstract class BaseActivity : AppCompatActivity(), NavigationHost {

    protected val TAG by lazy {
        this::class.java.simpleName
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.dataString?.let { navigateUsingDeepLink(it) }
    }

    private fun navigateUsingDeepLink(deepLink: String) {
        try {
            with(Uri.parse(deepLink)) {
                if (getNavController().graph.hasDeepLink(this)) {
                    getNavController().navigate(this)
                } else if (URLUtil.isValidUrl(deepLink)) {
                    Intent.makeMainSelectorActivity(Intent.ACTION_MAIN, Intent.CATEGORY_APP_BROWSER)
                        .let {
                            it.data = this
                            startActivity(it)
                        }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    abstract override fun getNavController(): NavController

/*    override fun onSupportNavigateUp(): Boolean {
        return getNavController().navigateUp() || super.onSupportNavigateUp()
    }*/
}