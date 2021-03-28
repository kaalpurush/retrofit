package com.codelixir.retrofit.ui

import androidx.fragment.app.setFragmentResult
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.navArgs
import com.codelixir.retrofit.databinding.FragmentBlankBinding
import com.codelixir.retrofit.databinding.FragmentHomeBinding
import kotlin.random.Random

class BlankFragment : BaseFragment<FragmentBlankBinding>() {
    val args: BlankFragmentArgs by navArgs()

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentBlankBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.textView1.text = args.message ?: "Blank"

        setFragmentResult(
            "RESULT",
            bundleOf("message" to "Good Bye! Random #${Random.nextInt(1, 10)}")
        )
    }

}