package com.codelixir.retrofit.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codelixir.retrofit.R
import com.codelixir.retrofit.data.GitHubEntity
import com.codelixir.retrofit.data.GitHubViewModel
import com.codelixir.retrofit.data.Resource
import com.codelixir.retrofit.databinding.FragmentHomeBinding
import com.codelixir.retrofit.util.show
import com.codelixir.retrofit.util.toast
import com.rabbitmq.client.Channel
import com.rabbitmq.client.Connection
import com.rabbitmq.client.ConnectionFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.lang.RuntimeException
import java.util.concurrent.TimeoutException

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

        binding.btnRabbitMQ.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                val factory = ConnectionFactory()
                try {
                    factory.host = "192.168.0.140"
                    factory.username = "guest"
                    factory.password = "guest"
                    val channel: Channel
                    val connection: Connection = factory.newConnection()
                    channel = connection.createChannel()
                    channel.exchangeDeclare("logs", "fanout", false, false, null)
                    val message = "Example3"
                    channel.basicPublish("logs", "", null, message.toByteArray())
                    println(" [x] Sent '$message'")
                    channel.close()
                    connection.close()
                } catch (e: IOException) {
                    println("Rabbitmq problem:"+ e.message)
                } catch (e: TimeoutException) {
                    println("Rabbitmq problem"+ e.message)
                }
            }

        }

    }

    private fun refreshData() {
        viewModel.getRepositoriesWithFallback()
            .observe(viewLifecycleOwner, { out ->
                baseViewModel.progress.postValue(out.status == Resource.Status.LOADING)

                if (out.status == Resource.Status.SUCCESS) {
                    val data = out.data
                    println("Api:viewModel: $data")

                    binding.rvList.layoutManager = LinearLayoutManager(context)
                    binding.rvList.addItemDecoration(
                        DividerItemDecoration(
                            requireContext(),
                            R.drawable.item_divider
                        )
                    )
                    binding.rvList.adapter = object : GenericListAdapter<GitHubEntity>(
                        R.layout.row_github_data,
                        bind = { item, holder, itemCount ->
                            with(holder.itemView) {
                                this.findViewById<TextView>(R.id.tvName).text = item.name
                                this.findViewById<TextView>(R.id.tvUrl).text = item.url
                                this.findViewById<TextView>(R.id.tvOwner).text =
                                    item.owner?.login
                            }
                            Log.d(TAG, item.name)
                        },
                        clickListener = { item, isLongClick -> toast(requireContext(), item.name) }
                    ) {

                    }.apply {
                        submitList(data)
                    }
                }
            })
    }

}