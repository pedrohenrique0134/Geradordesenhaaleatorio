package com.app.geradordesenhaaleatorio.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.geradordesenhaaleatorio.databinding.ItemBinding
import com.app.geradordesenhaaleatorio.interfaces.ClickDelete
import com.app.geradordesenhaaleatorio.interfaces.ClickEdit
import com.app.geradordesenhaaleatorio.interfaces.CopyKey
import com.app.geradordesenhaaleatorio.model.MyKeys

class AdapterItem(private var list: ArrayList<MyKeys>,
                  val clickEdit: ClickEdit,
                  val clickDelete: ClickDelete,
                  val copyKey: CopyKey
    ):
    RecyclerView.Adapter<AdapterItem.MyViewHolder>() {

    inner class MyViewHolder(
        private val binding:
        ItemBinding
    ):RecyclerView.ViewHolder(binding.root){
        fun bind(myKeys: MyKeys){
            binding.tituloSenhaItem.text = myKeys.title.let {"Titulo: $it"}
            binding.senhaItem.text = myKeys.keys.let {"Senha: $it"}
            binding.editKey.setOnClickListener {
                clickEdit.clickedit(myKeys)
            }
            binding.deleteKey.setOnClickListener {
                clickDelete.clickdelete(myKeys)
            }
            binding.senhaItem.setOnLongClickListener {
                copyKey.copykey(myKeys.keys!!)
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemBinding.inflate(inflater, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val mykeys = list[position]
        holder.bind(mykeys)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}