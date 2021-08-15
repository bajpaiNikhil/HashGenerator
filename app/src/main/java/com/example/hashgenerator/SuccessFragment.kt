package com.example.hashgenerator

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.example.hashgenerator.databinding.FragmentSuccessBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SuccessFragment : Fragment() {

    private var _binding : FragmentSuccessBinding? = null
    private val binding get() = _binding!!
    lateinit var hashReturnData : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            hashReturnData = it.getString("hash")!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        Log.d("SuccessFrag" , hashReturnData.replace(", ",""))
        _binding = FragmentSuccessBinding.inflate(inflater, container, false)

        binding.hashTextView.text = hashReturnData.replace(", ","")
        binding.copyButton.setOnClickListener {
            onCopyClicked()
        }
        return binding.root
    }

    private fun onCopyClicked() {
        //copyToClipboard(hashReturnData)
        lifecycleScope.launch {
            copyToClipboard(hashReturnData)
            applyAnimation()
        }
        
    }

    private fun copyToClipboard(hashReturnData : String) {
        val clipboardManager = requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("Encrypted Text" , hashReturnData.replace(", ",""))
        clipboardManager.setPrimaryClip(clipData)
    }

    private suspend fun applyAnimation(){
        binding.include.messageBackground.animate().translationY(80f).duration = 200L
        binding.include.messageTextView.animate().translationY(80f).duration  = 200L
        delay(2000L)
        binding.include.messageBackground.animate().translationY(-80f).duration = 500L
        binding.include.messageTextView.animate().translationY(-80f).duration  = 500L
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}