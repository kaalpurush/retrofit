package com.codelixir.retrofit.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.codelixir.retrofit.data.ServerViewModel
import com.codelixir.retrofit.databinding.FragmentServerBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ServerFragment : BaseFragment<FragmentServerBinding>() {
    private val viewModel by viewModels<ServerViewModel>()

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentServerBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnGet.setOnClickListener {
            viewModel.getList().observe(viewLifecycleOwner, {
                println("Api:viewModel: $it")
            })
        }
    }

}