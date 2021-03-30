package com.codelixir.retrofit.ui

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView.SmoothScroller
import com.codelixir.retrofit.data.GitHubEntity
import com.codelixir.retrofit.data.GitHubViewModel
import com.codelixir.retrofit.data.Resource
import com.codelixir.retrofit.databinding.FragmentListBinding
import com.codelixir.retrofit.util.isInResume
import com.codelixir.retrofit.util.isNotInResume
import com.codelixir.retrofit.util.toast
import java.util.*


class ListFragment : BaseFragment<FragmentListBinding>() {
    private val viewModel by viewModels<GitHubViewModel>()

    private var mSectionedRecyclerAdapter: GitHubSectionedDataListAdapter? = null

    private var list: ArrayList<GitHubEntity> = arrayListOf()

    private var pos = 0

    private val smoothScroller: SmoothScroller by lazy {
        object : LinearSmoothScroller(context) {
            override fun getVerticalSnapPreference(): Int {
                return SNAP_TO_START
            }
        }
    }

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentListBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        mSectionedRecyclerAdapter = GitHubSectionedDataListAdapter(list)
        binding.recyclerView.adapter = mSectionedRecyclerAdapter

        refreshData()

        binding.btnScroll.setOnClickListener {
            scrollToSectionHeader(pos++)
        }

        binding.edtSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            val mHandler = Handler()
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                mHandler.removeCallbacksAndMessages(null)

                mHandler.postDelayed(Runnable {
                    if (lifecycle.isNotInResume()) return@Runnable
                    mSectionedRecyclerAdapter?.filter?.filter(newText)
                }, 500)
                return true
            }
        })
    }

    private fun refreshData() {
        viewModel.getRepositoriesWithFallback()
            .observe(viewLifecycleOwner, { out ->
                if (out.status == Resource.Status.SUCCESS) {
                    toast(requireContext(), "size:" + out.data?.size)
                    out.data?.let {
                        list.clear()

                        val comparator =
                            { o1: GitHubEntity, o2: GitHubEntity ->
                                o1.programing_language.compareTo(o2.programing_language)
                            }
                        list.addAll(it.sortedWith(comparator))

//                        val sort = arrayListOf<String>("Java", "C++", "TypeScript")
//                        list.addAll(it.sortedBy { sort.indexOf(it.language) })

                        mSectionedRecyclerAdapter?.notifyDataChanged()
                    }
                }
            })
    }

    private fun scrollToSectionHeader(scrollSection: Int) {
        mSectionedRecyclerAdapter?.let {
            if (it.sectionsCount > scrollSection) {
                val section =
                    mSectionedRecyclerAdapter?.getSectionSubheaderPosition(
                        scrollSection
                    )
                if (section != null) {
                    smoothScroller.targetPosition = section
                    binding.recyclerView.layoutManager?.startSmoothScroll(smoothScroller)
                }
            } else pos = 0
        }
    }
}