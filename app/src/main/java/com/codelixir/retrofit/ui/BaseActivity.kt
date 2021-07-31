package com.codelixir.retrofit.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.webkit.URLUtil
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.viewbinding.ViewBinding
import com.codelixir.retrofit.R
import com.codelixir.retrofit.data.BaseViewModel
import com.codelixir.retrofit.data.GitHubViewModel

abstract class BaseActivity<T : ViewBinding> : AppCompatActivity(), NavigationHost {

    protected val TAG by lazy {
        this::class.java.simpleName
    }

    protected lateinit var binding: T

    private val navListener: NavController.OnDestinationChangedListener by lazy {
        NavController.OnDestinationChangedListener(this::onNavDestinationChanged)
    }

    open fun onNavDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        getToolbarTitle()?.text = getToolbar()?.title
        getToolbar()?.navigationIcon =
            if (getToolbar()?.navigationIcon != null) ContextCompat.getDrawable(
                this,
                R.drawable.ic_back
            ) else ContextCompat.getDrawable(this, android.R.color.transparent)
    }

    open fun getToolbar(): Toolbar? = null
    open fun getToolbarTitle(): TextView? = null
    abstract fun getViewBinding(inflater: LayoutInflater): T

    open fun getProgressView(): View? = null

    private val baseViewModel: BaseViewModel by viewModels()

    fun setProgress(progress: Boolean) {
        baseViewModel.progress.postValue(progress)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getViewBinding(layoutInflater)
        setContentView(binding.root)

        baseViewModel.progress.observe(this, {
            getProgressView()?.isVisible = it
        })
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

    override fun onNavigateUp(): Boolean {
        return getNavController().navigateUp() || onBackPressed().run { true }
    }

    override fun onResume() {
        super.onResume()
        getNavController().addOnDestinationChangedListener(navListener)
    }

    override fun onPause() {
        getNavController().removeOnDestinationChangedListener(navListener)
        super.onPause()
    }
}