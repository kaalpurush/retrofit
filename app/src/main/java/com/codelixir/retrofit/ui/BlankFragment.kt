package com.codelixir.retrofit.ui

import androidx.fragment.app.setFragmentResult
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.navArgs
import com.codelixir.retrofit.databinding.FragmentBlankBinding

class BlankFragment : BaseFragment() {
    private lateinit var binding: FragmentBlankBinding
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

        setFragmentResult("RESULT", bundleOf("message" to "Good Bye!"))
    }

}