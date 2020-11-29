package com.codelixir.retrofit.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.codelixir.retrofit.Application
import com.codelixir.retrofit.data.GitHubViewModel
import com.codelixir.retrofit.data.Resource
import com.codelixir.retrofit.databinding.ActivityMainBinding
import com.codelixir.retrofit.util.show

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: GitHubViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(GitHubViewModel::class.java)

        //refreshData()

        val count = Application.getSetting("worker", 0)
        //Application.saveSetting("worker", count + 1)
        Toast.makeText(this, "$count", Toast.LENGTH_LONG).show()

        binding.btnRefresh.setOnClickListener {
            refreshData()
        }

        binding.btnNew.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    MainActivity::class.java
                )
            )
        }

        binding.btnKill.setOnClickListener {
            finish()
            moveTaskToBack(true)
            System.exit(0)
        }
    }

    private fun refreshData() {
        viewModel.getRepositoriesWithFallback()
            .observe(this, Observer { out ->
                binding.progressBar.show(out.status == Resource.Status.LOADING)

                if (out.status == Resource.Status.SUCCESS) {
                    out.data?.let {
                        println("Api:viewModel: $it")
                        binding.textView1.text = it.size.toString()

                        val adapter = GitHubDataListAdapter()
                        binding.rvList.layoutManager = LinearLayoutManager(this)
                        binding.rvList.adapter = adapter
                        adapter.submitList(it)
                    }
                }
            })
    }

}
