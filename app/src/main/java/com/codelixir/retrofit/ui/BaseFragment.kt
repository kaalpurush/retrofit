package com.codelixir.retrofit.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.codelixir.retrofit.data.BaseViewModel

abstract class BaseFragment<T : ViewBinding> : Fragment(), NavigationHost {

    protected val baseViewModel: BaseViewModel by activityViewModels()

    protected val TAG by lazy {
        this::class.java.simpleName
    }

    fun setProgress(progress: Boolean) {
        baseViewModel.progress.postValue(progress)
    }

    protected lateinit var binding: T

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = getViewBinding(inflater, container)
        return binding.root
    }

    abstract fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): T

    override fun getNavController(): NavController = findNavController()

    protected fun navigateToDeeplink(deepLink: String) {
        try {
            startActivity(
                Intent().let { intent ->
                    intent.data = Uri.parse(deepLink)
                    intent.action = Intent.ACTION_VIEW
                    intent
                }
            )
        } catch (ex: Throwable) {
        }
    }
}