package com.codelixir.retrofit.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView.SmoothScroller
import com.codelixir.retrofit.data.GitHubEntity
import com.codelixir.retrofit.data.GitHubViewModel
import com.codelixir.retrofit.data.Resource
import com.codelixir.retrofit.databinding.FragmentListBinding
import com.codelixir.retrofit.util.toast
import java.util.*


class ListFragment : BaseFragment() {
    private lateinit var binding: FragmentListBinding

    private val viewModel by viewModels<GitHubViewModel>()

    private var mSectionedRecyclerAdapter: BaseSectionedDataListAdapter? = null

    private var list: ArrayList<GitHubEntity> = arrayListOf()

    private var pos = 0

    private val smoothScroller: SmoothScroller by lazy {
        object : LinearSmoothScroller(context) {
            override fun getVerticalSnapPreference(): Int {
                return SNAP_TO_START
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentListBinding.inflate(inflater, container, false).let {
            binding = it
            binding.root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        val movieComparator =
            { o1: GitHubEntity, o2: GitHubEntity ->
                o1.programing_language.compareTo(o2.programing_language)
            }
        Collections.sort(list, movieComparator)
        mSectionedRecyclerAdapter = GitHubSectionedDataListAdapter(list)
        binding.recyclerView.adapter = mSectionedRecyclerAdapter

        refreshData()

        binding.btnScroll.setOnClickListener {
            scrollToSectionHeader(pos++)
        }
    }

    private fun refreshData() {
        viewModel.getRepositoriesWithFallback()
            .observe(viewLifecycleOwner, { out ->
                if (out.status == Resource.Status.SUCCESS) {
                    toast(requireContext(), "size:" + out.data?.size)
                    out.data?.let {
                        list.clear()
                        list.addAll(it)
                        val movieComparator =
                            { o1: GitHubEntity, o2: GitHubEntity ->
                                o1.programing_language.compareTo(o2.programing_language)
                            }
                        Collections.sort(list, movieComparator)
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