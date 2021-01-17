package com.codelixir.retrofit.ui

import android.content.Intent
import android.net.Uri
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController

open class BaseFragment : Fragment(), NavigationHost {

    protected val TAG by lazy {
        this::class.java.simpleName
    }

    override fun getNavController(): NavController = findNavController()

    protected fun navigateToDeeplink(deepLink: String) {
        try {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW, Uri.parse(deepLink), requireContext(),
                    BaseActivity::class.java
                )
            )
        } catch (ex: Throwable) {
        }
    }
}