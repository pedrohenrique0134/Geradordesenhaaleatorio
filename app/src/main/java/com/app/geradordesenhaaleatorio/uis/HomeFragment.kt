package com.app.geradordesenhaaleatorio.uis

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.app.geradordesenhaaleatorio.R
import com.app.geradordesenhaaleatorio.databinding.FragmentHomeBinding
import com.app.geradordesenhaaleatorio.extention.createDialog
import com.app.geradordesenhaaleatorio.extention.geradorKey
import com.app.geradordesenhaaleatorio.model.MyKeys
import com.app.geradordesenhaaleatorio.ultis.UiState
import com.app.geradordesenhaaleatorio.viewModel.ViewModelDatabase
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.handleCoroutineException
import java.util.Date

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var dialog: Dialog
    private lateinit var btnSim: Button
    private lateinit var btnNao: Button
    private lateinit var senhaDialog: TextView
    private lateinit var oqueE: EditText
    private lateinit var progressDialog: ProgressBar
    private var senhaSalva = ""
    
    private val viewModelDatabase: ViewModelDatabase by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var handler = Handler(Looper.getMainLooper())
        binding = FragmentHomeBinding.inflate(layoutInflater).apply {
            viewLifecycleOwner
        }
        verifiqueRadius()
        observer()
        binding.btnGeradorSenha.setOnClickListener {
            binding.senhaGerada.visibility = View.VISIBLE
        }

        binding.btnSaveSenha.setOnClickListener {
            if (binding.senhaGerada.visibility == View.INVISIBLE){
                Toast.makeText(requireContext(), "Impossivel salvar uma senha que não existe, burro!", Toast.LENGTH_SHORT).show()
            }else{
                dialog = requireContext().createDialog(R.layout.dialog_salvar_senha, true)
                btnSim = dialog.findViewById(R.id.btn_sim)
                btnNao = dialog.findViewById(R.id.btn_nao)
                senhaDialog = dialog.findViewById(R.id.senha_dialog)
                progressDialog = dialog.findViewById(R.id.progress_dialog)
                oqueE = dialog.findViewById(R.id.onde_senha)

                senhaDialog.text = senhaSalva
                
                btnSim.setOnClickListener { 
                    viewModelDatabase.saveKey(
                        FirebaseAuth.getInstance().currentUser?.uid.toString(),
                        MyKeys(
                            title = oqueE.text.toString(),
                            keys = senhaSalva,
                            id = Date().time.toString()
                        )
                    )
                    dialog.dismiss()
                }
                
                btnNao.setOnClickListener { 
                    dialog.dismiss()
                }
                
                dialog.show()
            }
        }
        binding.goToKeySave.setOnClickListener {
            findNavController().navigate(
                R.id.action_homeFragment_to_senhasSalvasFragment
            )
        }

        return binding.root
    }
    
    private fun observer(){
        viewModelDatabase.saveKeyState.observe(viewLifecycleOwner){state->
            when(state){
                is UiState.Failure -> {
                    progressDialog.visibility = View.GONE
                    Toast.makeText(requireContext(),
                        "${state.error}",
                        Toast.LENGTH_SHORT).show()
                }
                UiState.Loading -> {
                    progressDialog.visibility = View.VISIBLE
                }
                is UiState.Success -> {
                    progressDialog.visibility = View.GONE
                    Toast.makeText(requireContext(),
                        "${state.data}",
                        Toast.LENGTH_SHORT).show()

                }
            }
        }
        
    }


    private fun verifiqueRadius() {

        binding.radioGrup.setOnCheckedChangeListener { group, checkedId ->

            when (checkedId) {
                R.id.forte -> {
                    binding.btnSaveSenha.visibility = View.VISIBLE
                    binding.senhaGerada.visibility = View.INVISIBLE
                    binding.btnGeradorSenha.setBackgroundColor(Color.GREEN)
                    geradorKey(18, "forte").apply {
                        binding.senhaGerada.text = "Senha: $this"
                        senhaSalva = this
                        Log.d("TAG", "verifiqueRadius: $senhaSalva")
                    }
                }

                R.id.media -> {
                    binding.btnSaveSenha.visibility = View.VISIBLE
                    binding.senhaGerada.visibility = View.INVISIBLE
                    binding.btnGeradorSenha.setBackgroundColor(Color.YELLOW)
                    binding.btnGeradorSenha.setTextColor(Color.BLACK)
                    geradorKey(12, "media").apply {
                        binding.senhaGerada.text = "Senha: $this"
                        senhaSalva = this
                        Log.d("TAG", "verifiqueRadius: $senhaSalva")

                    }
                }

                R.id.fraca -> {
                    binding.btnSaveSenha.visibility = View.VISIBLE
                    binding.senhaGerada.visibility = View.INVISIBLE
                    binding.btnGeradorSenha.setBackgroundColor(Color.RED)
                    geradorKey(8, "facil").apply {
                        binding.senhaGerada.text = "Senha: $this"
                        senhaSalva = this
                        Log.d("TAG", "verifiqueRadius: $senhaSalva")

                    }
                }
            }

        }

    }
}