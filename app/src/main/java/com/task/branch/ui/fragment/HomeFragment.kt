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
import com.task.branch.databinding.FragmentHomeBinding
import com.task.branch.model.Message
import com.task.branch.ui.adapter.ChatsItemAdapter
import com.task.branch.ui.viewmodel.MainViewModel
import com.task.branch.util.Resource
import kotlinx.coroutines.*


class HomeFragment : Fragment() {
    companion object {
        const val TAG = "HomeFragment"
    }

    private val viewModel : MainViewModel by activityViewModels()

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getAllChats()
        setupObserver()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getAllChats() = viewModel.getAllMessages()

    private fun setupObserver() {
        viewModel.getChatsResponse().observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Error -> {
                    Snackbar.make(binding.root, it.message, Snackbar.LENGTH_SHORT).show()
                    binding.progressBar.visibility = GONE
                }
                is Resource.Exception -> {
                    Log.d(LoginFragment.TAG, it.exception.stackTraceToString())
                    Snackbar.make(binding.root, "Error", Snackbar.LENGTH_SHORT).show()
                    binding.progressBar.visibility = GONE
                }
                Resource.Loading -> binding.progressBar.visibility = VISIBLE
                is Resource.Success -> {
                    setUpChatsAdapter(it.data)
                    binding.progressBar.visibility = GONE
                }
            }
        }
    }

    private fun setUpChatsAdapter(data: List<ArrayList<Message>>?) {
        binding.chatsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = ChatsItemAdapter(data!!, chatClickListener)
        }
    }

    private val chatClickListener = object : ChatsItemAdapter.ChatClickListener {
        override fun onChatClick(chat: List<Message>) {
            viewModel.setCurrentThread(chat)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}