package com.codelixir.retrofit.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codelixir.retrofit.data.GitHubViewModel
import com.codelixir.retrofit.data.Resource
import com.codelixir.retrofit.databinding.FragmentHomeBinding
import com.codelixir.retrofit.databinding.FragmentListBinding
import com.codelixir.retrofit.util.show

class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    private val viewModel by viewModels<GitHubViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentResultListener("RESULT") { key, bundle ->
            val message = bundle.getString("message")
            Toast.makeText(requireContext(), "Result: $message", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentHomeBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setup()
    }

    private fun setup() {
        binding.btnRefresh.setOnClickListener {
            refreshData()
        }

        binding.btnBlank.setOnClickListener {
            navigateTo(HomeFragmentDirections.actionToBlank("Blank from Fragment"))
        }

        binding.btnUpload.setOnClickListener {
            navigateTo(HomeFragmentDirections.actionToUpload())
        }

        binding.btnList.setOnClickListener {
            navigateTo(HomeFragmentDirections.actionToList())
        }

        binding.btnServer.setOnClickListener {
            navigateTo(HomeFragmentDirections.actionToServer())
        }

        binding.rvList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                binding.textView2.text = newState.toString()
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                binding.textView1.text = dy.toString()
            }
        })

    }

    private fun refreshData() {
        viewModel.getRepositoriesWithFallback()
            .observe(viewLifecycleOwner, { out ->
                binding.progressBar.show(out.status == Resource.Status.LOADING)

                if (out.status == Resource.Status.SUCCESS) {
                    out.data?.let {
                        println("Api:viewModel: $it")
                        //binding.textView1.text = it.size.toString()

                        val adapter = GitHubDataListAdapter()
                        binding.rvList.layoutManager = LinearLayoutManager(context)
                        binding.rvList.adapter = adapter
                        adapter.submitList(it)
                    }
                }
            })
    }

}