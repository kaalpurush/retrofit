package com.codelixir.retrofit.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.codelixir.retrofit.data.GitHubViewModel
import com.codelixir.retrofit.data.Resource
import com.codelixir.retrofit.databinding.FragmentBlankBinding
import com.codelixir.retrofit.databinding.FragmentHomeBinding
import com.codelixir.retrofit.util.show

class BlankFragment : BaseFragment() {
    private lateinit var binding: FragmentBlankBinding
    private val viewModel by viewModels<GitHubViewModel>()
    val args: BlankFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentBlankBinding.inflate(inflater, container, false).let {
            binding = it
            binding.root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.textView1.text = args.message ?: "Blank"
    }

}