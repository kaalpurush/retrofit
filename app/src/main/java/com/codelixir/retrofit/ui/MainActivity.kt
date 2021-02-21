package com.codelixir.retrofit.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
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
import kotlinx.coroutines.launch
import org.funktionale.currying.curried

class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<GitHubViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.setupWithNavController(getNavController())

        val count = Application.getSetting("worker", 0)

        Toast.makeText(this, "Worker Run Count: $count", Toast.LENGTH_SHORT).show()

        val add = { x: Int, y: Int -> {x + y}}.curried()
        add(4)(5)

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

        binding.btnR.setOnClickListener {
            refreshData()
        }

        binding.btnB.setOnClickListener {
            navigateTo(NavGraphDirections.actionToGlobalBlank("Blank from Activity"))

            lifecycleScope.launch { }
        }

        val graph = getNavController().navInflater.inflate(R.navigation.nav_graph)
        getNavController().graph = graph

        binding.rootScrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener {
                v, scrollX, scrollY, oldScrollX, oldScrollY ->
            binding.tv1.text = scrollY.toString()
        })
    }

    private fun refreshData() {
        Toast.makeText(this, "Refreshing...", Toast.LENGTH_SHORT).show()
        viewModel.getSharedRepositories()
            .observe(this, Observer { out ->
                if (out.status == Resource.Status.SUCCESS) {
                    out.data?.let {
                        Toast.makeText(this, "Total items: ${it.size}", Toast.LENGTH_SHORT).show()
                    }
                }
            })
    }

    override fun getNavController() = findNavController(R.id.nav_host_fragment)

}
