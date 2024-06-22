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
import com.app.geradordesenhaaleatorio.databinding.FragmentCadastroBinding
import com.app.geradordesenhaaleatorio.model.User
import com.app.geradordesenhaaleatorio.ultis.UiState
import com.app.geradordesenhaaleatorio.viewModel.ViewModelAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CadastroFragment : Fragment() {
    private lateinit var binding: FragmentCadastroBinding
    private val viewModelAuth: ViewModelAuth by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCadastroBinding.inflate(layoutInflater, container, false)

        observer()
        cadastarCliente()
        binding.goLogin.setOnClickListener {
            findNavController().navigate(
                R.id.action_cadastroFragment_to_loginFragment
            )
        }
        return binding.root
    }

    private fun observer(){
        viewModelAuth.createState.observe(viewLifecycleOwner){state->
            when(state){
                is UiState.Failure -> {
                    binding.progress.visibility = View.GONE
                    Toast.makeText(requireContext(),
                        state.error,
                        Toast.LENGTH_SHORT).show()
                }
                UiState.Loading -> {
                    binding.progress.visibility = View.VISIBLE
                }
                is UiState.Success -> {
                    binding.progress.visibility = View.GONE
                    findNavController().navigate(
                        R.id.action_cadastroFragment_to_homeFragment
                    )
                }
            }
        }
    }

    private fun cadastarCliente() {
        binding.Btn.setOnClickListener {
            if (checkUser()) {
                viewModelAuth.createUser(
                   clienteObj()
                )
            }
        }
    }

    private fun clienteObj(): User {
        return User(
            nome = binding.nome.text.toString(),
            email = binding.cadastroEmail.text.toString(),
            password = binding.cadastroSenha.text.toString()
        )
    }

    private fun checkUser(): Boolean{
        var check = false
        if (
            binding.nome.text.toString().isBlank() ||
            binding.cadastroEmail.text.toString().isBlank() ||
            binding.cadastroSenha.text.toString().isBlank()
        ){
            check = false
            Toast.makeText(requireContext(),
                "Campos em branco",
                Toast.LENGTH_SHORT).show()
        }else{
            check = true
        }
        return check
    }
}