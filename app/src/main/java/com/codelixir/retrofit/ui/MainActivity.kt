package com.codelixir.retrofit.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.codelixir.retrofit.data.GitHubViewModel
import com.codelixir.retrofit.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: GitHubViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(GitHubViewModel::class.java)

        viewModel.getRepositories(intent.getBooleanExtra("refresh", false))
            .observe(this, Observer {
                println("Api:viewModel: $it")
                binding.textView1.text = it.size.toString()

                val adapter = GitHubDataListAdapter()
                binding.rvList.layoutManager = LinearLayoutManager(this)
                binding.rvList.adapter = adapter
                adapter.submitList(it)
            })

/*        viewModel.getSharedRepositories(intent.getBooleanExtra("refresh", false))
            .observe(this, Observer {
                println("Api:viewModel:sharedData: $it")
                binding.textView2.text = it.size.toString()
            })

        lifecycleScope.launch(Dispatchers.Main) {
            val it = withContext(Dispatchers.IO) {
                GitHubDataRepository.fetchRepositories()
            }
            println("Api:coroutine: $it")
            binding.textView3.text = it?.size.toString()
        }*/

        binding.btnRefresh.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    MainActivity::class.java
                ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("refresh", true)
            )
        }

        binding.btnNoRefresh.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    MainActivity::class.java
                ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("refresh", false)
            )
        }

        binding.btnKill.setOnClickListener {
            finish()
            moveTaskToBack(true)
            System.exit(0)
        }
    }

}
