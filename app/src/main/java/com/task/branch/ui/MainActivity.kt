package com.task.branch.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.task.branch.R
import com.task.branch.databinding.ActivityMainBinding
import com.task.branch.ui.fragment.ChatFragment
import com.task.branch.ui.fragment.HomeFragment
import com.task.branch.ui.fragment.LoginFragment
import com.task.branch.ui.viewmodel.MainViewModel
import com.task.branch.util.Constants
import com.task.branch.util.Constants.AUTH_TOKEN
import com.task.branch.util.Screen
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    @Inject lateinit var sharedPreferences : SharedPreferences

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkIfUserLoggedIn()
        setObserver()
        setupBackButton()
    }

    private fun setObserver() {
        viewModel.getCurrentThread().observe(this) {
            supportFragmentManager.beginTransaction().add(R.id.container, ChatFragment()).addToBackStack(null).commit()
            binding.screenTitle.text = it?.first()?.userId
            binding.backButton.visibility = VISIBLE
        }

        viewModel.getFragmentState().observe(this) {
            if (it is Screen.Home) {
                supportFragmentManager.beginTransaction().replace(R.id.container, HomeFragment()).commit()
                binding.screenTitle.text = Constants.HOME_SCREEN
            }
        }
    }

    private fun checkIfUserLoggedIn() {
        if (sharedPreferences.getString(AUTH_TOKEN, null) != null) {
            supportFragmentManager.beginTransaction().replace(R.id.container, HomeFragment()).commit()
            binding.screenTitle.text = Constants.HOME_SCREEN
        } else {
            supportFragmentManager.beginTransaction().replace(R.id.container, LoginFragment()).commit()
            binding.screenTitle.text = Constants.LOGIN_SCREEN
        }
    }

    private fun setupBackButton() {
        binding.backButton.setOnClickListener {
            binding.backButton.visibility = GONE
            supportFragmentManager.popBackStack()
            binding.screenTitle.text = Constants.HOME_SCREEN
        }
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
            binding.backButton.visibility = GONE
            binding.screenTitle.text = Constants.HOME_SCREEN
        }
        else super.onBackPressed()
    }
}