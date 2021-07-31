package com.codelixir.retrofit.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.codelixir.retrofit.R
import com.codelixir.retrofit.data.Resource
import com.codelixir.retrofit.data.ServerEntity
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
            viewModel.getList().observe(viewLifecycleOwner, { out ->
                setProgress(out.status == Resource.Status.LOADING)

                if (out.status == Resource.Status.SUCCESS) {
                    println("Api:viewModel: $it")

                    binding.rvList.layoutManager = LinearLayoutManager(context)
                    binding.rvList.addItemDecoration(
                        DividerItemDecoration(
                            requireContext(),
                            R.drawable.item_divider
                        )
                    )
                    binding.rvList.adapter = object : GenericListAdapter<ServerEntity>(
                        R.layout.row_github_data,
                        bind = { item, holder, itemCount ->
                            with(holder.itemView) {
                                this.findViewById<TextView>(R.id.tvName).text = item.name
                            }

                        }
                    ) {}.apply {
                        submitList(out.data)
                    }

                }
            })
        }
    }

}