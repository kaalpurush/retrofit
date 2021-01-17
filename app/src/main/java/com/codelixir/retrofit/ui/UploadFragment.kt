package com.codelixir.retrofit.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.OpenMultipleDocuments
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import coil.load
import com.codelixir.retrofit.databinding.FragmentUploadBinding
import com.codelixir.retrofit.util.getBitmapAsImageFile
import com.codelixir.retrofit.util.shareFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UploadFragment : BaseFragment() {
    private lateinit var binding: FragmentUploadBinding
    private lateinit var openMultipleDocumentsLauncher: ActivityResultLauncher<Array<String>>
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentUploadBinding.inflate(inflater, container, false).let {
            binding = it
            binding.root
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initReqPermission()
        initOpenDocument()
    }


    private fun initReqPermission() {
        requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) {
                    openMultipleDocumentsLauncher.launch(arrayOf("image/*"))
                } else {
                    Log.i(TAG, "permission denied")
                }
            }
    }

    private fun initOpenDocument() {
        openMultipleDocumentsLauncher = this.registerForActivityResult(
            OpenMultipleDocuments()
        ) { urilist ->
            for (uri in urilist) {
                val filename: String = uri.getLastPathSegment() ?: "invalid"
                Log.d("FILE", filename)
                binding.ivImage.load(uri) { allowHardware(false) }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        binding.btnSelect.setOnClickListener {
            reqPermissionAndOpenDocuments()
        }

        binding.ivImage.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                requireActivity().shareFile(it.getBitmapAsImageFile(), "Share")
            }
        }
    }

    fun reqPermissionAndOpenDocuments() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            openMultipleDocumentsLauncher.launch(arrayOf("image/*"))
        } else {
            requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

}