package com.codelixir.retrofit.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.codelixir.retrofit.Application
import com.codelixir.retrofit.NavGraphDirections
import com.codelixir.retrofit.R
import com.codelixir.retrofit.data.GitHubViewModel
import com.codelixir.retrofit.data.Resource
import com.codelixir.retrofit.databinding.ActivityMainBinding
import com.codelixir.retrofit.util.show

class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: GitHubViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.setupWithNavController(getNavController())

        viewModel = ViewModelProvider(this).get(GitHubViewModel::class.java)

        val count = Application.getSetting("worker", 0)

        Toast.makeText(this, "Worker Run Count: $count", Toast.LENGTH_LONG).show()

        binding.btnRefresh.setOnClickListener {
            refreshData()
        }

        binding.btnBlank.setOnClickListener {
            navigateTo(NavGraphDirections.actionToGlobalBlank("Blank from Activity"))
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

        val graph = getNavController().navInflater.inflate(R.navigation.nav_graph)
        getNavController().graph = graph
    }

    private fun refreshData() {
        viewModel.getSharedRepositories()
            .observe(this, Observer { out ->
                if (out.status == Resource.Status.SUCCESS) {
                    out.data?.let {
                        Toast.makeText(this, "Total items: ${it.size}", Toast.LENGTH_LONG).show()
                    }
                }
            })
    }

    override fun getNavController() = findNavController(R.id.nav_host_fragment)

}
