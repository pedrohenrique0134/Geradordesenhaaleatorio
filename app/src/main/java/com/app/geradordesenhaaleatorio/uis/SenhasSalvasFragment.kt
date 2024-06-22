package com.app.geradordesenhaaleatorio.uis

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.compose.runtime.key
import androidx.databinding.adapters.AdapterViewBindingAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.app.geradordesenhaaleatorio.R
import com.app.geradordesenhaaleatorio.adapter.AdapterItem
import com.app.geradordesenhaaleatorio.databinding.FragmentSenhasSalvasBinding
import com.app.geradordesenhaaleatorio.extention.copyText
import com.app.geradordesenhaaleatorio.extention.createDialog
import com.app.geradordesenhaaleatorio.interfaces.ClickDelete
import com.app.geradordesenhaaleatorio.interfaces.ClickEdit
import com.app.geradordesenhaaleatorio.interfaces.CopyKey
import com.app.geradordesenhaaleatorio.model.MyKeys
import com.app.geradordesenhaaleatorio.ultis.UiState
import com.app.geradordesenhaaleatorio.viewModel.ViewModelDatabase
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SenhasSalvasFragment : Fragment(), ClickEdit, ClickDelete, CopyKey {

    private lateinit var binding: FragmentSenhasSalvasBinding
    private var list: ArrayList<MyKeys> = arrayListOf()
    private val adapter by lazy {
        AdapterItem(list, this,this, this)

    }
    private val viewModelDatabase: ViewModelDatabase by viewModels()

    private lateinit var dialogEdit: Dialog
    private lateinit var dialogDelete: Dialog

    private lateinit var editTextTitulo: EditText
    private lateinit var editTextKey: EditText
    private lateinit var btnSalvar: Button
    private lateinit var btnCancelarEdit: Button

    private lateinit var progressEdit: ProgressBar
    private lateinit var progressDelete: ProgressBar
    private lateinit var btnCancelarDelete: Button
    private lateinit var btnDelete: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSenhasSalvasBinding.inflate(layoutInflater).apply {
            viewLifecycleOwner
        }
        init()
        observer()
        return binding.root
    }
    private  fun observer(){
        viewModelDatabase.getKeyState.observe(viewLifecycleOwner){state ->
            when(state){
                is UiState.Failure -> {
                    list.clear()
                    binding.progressCircular.visibility = View.GONE
                    binding.llError.visibility = View.VISIBLE
                    binding.erroAparente.text = state.error
                }
                UiState.Loading -> {
                    list.clear()
                    binding.llError.visibility = View.GONE
                    binding.recyclerView.visibility = View.GONE
                    binding.progressCircular.visibility = View.VISIBLE
                }
                is UiState.Success -> {
                    list.clear()
                    binding.progressCircular.visibility = View.GONE
                    binding.recyclerView.visibility = View.VISIBLE
                    list.addAll(state.data)
                    binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
                    binding.recyclerView.adapter = adapter

                }
            }
        }

        viewModelDatabase.updateKeyState.observe(viewLifecycleOwner){state->
            when(state){
                is UiState.Failure -> {
                    progressEdit.visibility = View.GONE
                    Toast.makeText(requireContext(), state.error, Toast.LENGTH_SHORT).show()
                }
                UiState.Loading -> {
                   progressEdit.visibility = View.VISIBLE
                }
                is UiState.Success -> {
                    progressEdit.visibility = View.GONE
                    dialogEdit.dismiss()
                    Toast.makeText(requireContext(), state.data, Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModelDatabase.deleteKeyState.observe(viewLifecycleOwner){state->
            when(state){
                is UiState.Failure ->  {
                    progressDelete.visibility = View.GONE
                    Toast.makeText(requireContext(), state.error, Toast.LENGTH_SHORT).show()
                }
                UiState.Loading -> {
                    progressDelete.visibility = View.VISIBLE
                }
                is UiState.Success -> {
                    progressDelete.visibility = View.GONE
                    dialogDelete.dismiss()
                }
            }
        }
    }
    private fun init(){
        viewModelDatabase.getKey(FirebaseAuth.getInstance().currentUser?.uid!!)

        binding.btnBackHome.setOnClickListener {
            findNavController().navigate(
                R.id.action_senhasSalvasFragment_to_homeFragment
            )
        }

        binding.btnRegarregar.setOnClickListener {
            viewModelDatabase.getKey(FirebaseAuth.getInstance().currentUser?.uid!!)
        }
    }

    override fun clickedit(myKeys: MyKeys) {
        dialogEdit = requireContext().createDialog(R.layout.edti_key, false)

        editTextTitulo = dialogEdit.findViewById(R.id.titulo_dialog)
        editTextKey = dialogEdit.findViewById(R.id.new_key)
        progressEdit = dialogEdit.findViewById(R.id.progress_edit)
        btnSalvar = dialogEdit.findViewById(R.id.salvar_key_dialog)
        btnCancelarEdit = dialogEdit.findViewById(R.id.cancelar_dialog_edit)

        editTextTitulo.setText(myKeys.title)
        editTextKey.setText(myKeys.keys)



        btnSalvar.setOnClickListener {
            val hashMap = hashMapOf<String, Any>(
                "title" to editTextTitulo.text.toString(),
                "keys" to editTextKey.text.toString(),
                "id" to myKeys.id!!
            )

            viewModelDatabase.updateKey(
                FirebaseAuth.getInstance().currentUser?.uid!!,
                myKeys.id.toString(),
                hashMap
            )
        }

        btnCancelarEdit.setOnClickListener {
            dialogEdit.dismiss()
        }

        dialogEdit.show()
    }

    override fun clickdelete(myKeys: MyKeys) {
       dialogDelete = requireContext().createDialog(R.layout.delete_key, false)
        btnDelete = dialogDelete.findViewById(R.id.excluir_key_dialog)
        btnCancelarDelete = dialogDelete.findViewById(R.id.no_excluir_key_dialog)
        progressDelete = dialogDelete.findViewById(R.id.progress_delete)
        btnDelete.setOnClickListener {
            viewModelDatabase.deleteKey(
                FirebaseAuth.getInstance().currentUser?.uid!!,
                myKeys
            )
        }.apply {
            binding.recyclerView.visibility = View.GONE
        }
        btnCancelarDelete.setOnClickListener {
            dialogDelete.dismiss()
        }
        dialogDelete.show()
    }

    override fun copykey(key: String) {
        copyText(key, requireContext()).apply {
        Toast.makeText(requireContext(), "senha copiada: $key", Toast.LENGTH_SHORT).show()}

    }

}