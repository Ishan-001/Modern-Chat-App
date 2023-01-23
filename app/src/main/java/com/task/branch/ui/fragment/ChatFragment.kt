package com.task.branch.ui.fragment

import android.os.Build
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.task.branch.databinding.FragmentChatBinding
import com.task.branch.databinding.FragmentHomeBinding
import com.task.branch.model.Message
import com.task.branch.ui.adapter.ChatsItemAdapter
import com.task.branch.ui.adapter.MessageItemAdapter
import com.task.branch.ui.viewmodel.MainViewModel
import com.task.branch.util.Resource
import kotlinx.coroutines.*


class ChatFragment : Fragment() {
    companion object {
        const val TAG = "HomeFragment"
    }

    private val viewModel : MainViewModel by activityViewModels()
    private lateinit var chatAdapter: MessageItemAdapter
    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getChatHistory()
        setupObserver()
        setupChatInput()
    }

    private fun getChatHistory() = viewModel.getCurrentThread().value?.let { setUpChatsAdapter(it as ArrayList) }

    private fun setupChatInput() {
        binding.apply {
            sendMessageButton.setOnClickListener {
                if (!chatInput.text.isNullOrEmpty()) {
                    viewModel.sendMessage(
                        viewModel.getCurrentThread().value?.last()?.threadId!!,
                        chatInput.text.toString()
                    )
                    chatInput.text = null
                }
            }
        }
    }

    private fun setupObserver() {
        viewModel.getMessageResponse().observe(viewLifecycleOwner) {
            binding.apply {
                when (it) {
                    is Resource.Error -> {
                        Snackbar.make(binding.root, it.message, Snackbar.LENGTH_SHORT).show()
                        chatInput.isEnabled = true
                        sendMessageButton.visibility = VISIBLE
                        progressBar.visibility = GONE
                    }
                    is Resource.Exception -> {
                        Log.d(LoginFragment.TAG, it.exception.stackTraceToString())
                        Snackbar.make(binding.root, "Error", Snackbar.LENGTH_SHORT).show()
                        chatInput.isEnabled = true
                        sendMessageButton.visibility = VISIBLE
                        progressBar.visibility = GONE
                    }
                    Resource.Loading -> {
                        binding.chatInput.isEnabled = false
                        sendMessageButton.visibility = GONE
                        progressBar.visibility = VISIBLE
                    }
                    is Resource.Success -> {
                        chatInput.isEnabled = true
                        sendMessageButton.visibility = VISIBLE
                        progressBar.visibility = GONE
                        chatAdapter.addMessage(it.data!!)
                        viewModel.getAllMessages()
                    }
                }
            }
        }
    }

    private fun setUpChatsAdapter(data : ArrayList<Message>) {
        chatAdapter = MessageItemAdapter(data)
        binding.chatRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = chatAdapter
            scrollToPosition(data.size - 1)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}