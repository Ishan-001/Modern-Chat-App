package com.task.branch.ui.fragment

import android.content.SharedPreferences
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.google.android.material.snackbar.Snackbar
import com.task.branch.databinding.FragmentLoginBinding
import com.task.branch.ui.viewmodel.MainViewModel
import com.task.branch.util.Resource
import com.task.branch.util.Screen


class LoginFragment : Fragment() {
    companion object {
        const val TAG = "LoginFragment"
    }

    private val viewModel : MainViewModel by activityViewModels()

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            loginButton.setOnClickListener {
                validateLogin()
            }
        }

        setupObserver()
    }

    private fun setupObserver() {
        viewModel.getAuthResponse().observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Error -> {
                    Snackbar.make(binding.root, it.message, Snackbar.LENGTH_SHORT).show()
                    binding.progressBar.visibility = View.GONE
                }
                is Resource.Exception -> {
                    Log.d(TAG, it.exception.stackTraceToString())
                    Snackbar.make(binding.root, "Error", Snackbar.LENGTH_SHORT).show()
                    binding.progressBar.visibility = View.GONE
                }
                Resource.Loading -> binding.progressBar.visibility = VISIBLE
                is Resource.Success -> {
                    viewModel.setFragmentState(Screen.Home)
                    binding.progressBar.visibility = View.GONE
                }
            }
        }
    }

    private fun validateLogin() {
        binding.apply {
            if (username.text.isNullOrEmpty()) usernameContainer.helperText = "Username can't be empty"
            else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(username.text.toString()).matches())
                usernameContainer.helperText = "Invalid username"
            else if (password.text.isNullOrEmpty()) passwordContainer.helperText = "Password can't be empty"
            else if (username.text.toString() != password.text.toString().reversed())
                passwordContainer.helperText = "Password should be be reverse of username"
            else {
                viewModel.login(username.text.toString(), password.text.toString())
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}