package com.app.geradordesenhaaleatorio.uis

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.app.geradordesenhaaleatorio.R
import com.app.geradordesenhaaleatorio.databinding.FragmentLoginBinding
import com.app.geradordesenhaaleatorio.model.User
import com.app.geradordesenhaaleatorio.ultis.UiState
import com.app.geradordesenhaaleatorio.viewModel.ViewModelAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private val viewModelAuth: ViewModelAuth by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(layoutInflater).apply {
            viewLifecycleOwner
        }
        observer()
        creatAuth()
        return binding.root
    }

    private fun creatAuth() {
        binding.BtnLogin.setOnClickListener {
            if (checkUser()) {
                viewModelAuth.authUser(
                    objectAuth()
                )
            }
        }
    }

    private fun objectAuth(): User {
        return User(
            email = binding.loginEmail.text.toString(),
            password = binding.loginSenha.text.toString()
        )
    }

    private fun observer() {
        viewModelAuth.authState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Failure -> {
                    binding.loginProgress.visibility = View.GONE
                    Toast.makeText(requireContext(), state.error, Toast.LENGTH_SHORT).show()
                }

                UiState.Loading -> {
                    binding.loginProgress.visibility = View.VISIBLE

                }

                is UiState.Success -> {
                    binding.loginProgress.visibility = View.GONE
                    findNavController().navigate(
                        R.id.action_loginFragment_to_homeFragment
                    )
                }
            }
        }
    }

    private fun checkUser(): Boolean {
        var check = false
        if (
            binding.loginEmail.text.toString().isBlank() ||
            binding.loginSenha.text.toString().isBlank()
        ) {
            check = false
            Toast.makeText(
                requireContext(),
                "Campos em branco",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            check = true
        }
        return check
    }
}