package com.app.geradordesenhaaleatorio.repository

import com.app.geradordesenhaaleatorio.model.MyKeys
import com.app.geradordesenhaaleatorio.ultis.UiState
import com.google.firebase.firestore.FirebaseFirestore

class RepositoryImpleDataBase(
    private val dataBase: FirebaseFirestore
): RepositoryDataBase{

    override suspend fun saveKey(id: String, keys: MyKeys, result: (UiState<String>) -> Unit) {
        val document = dataBase.collection("keys")
            .document(id)
            .collection("MyKeys").document(keys.id!!)

        document.set(keys).addOnSuccessListener {
            result.invoke(
                UiState.Success(
                    "Sucesso"
                )
            )
        }.addOnFailureListener {
            result.invoke(
                UiState.Failure(
                    it.localizedMessage
                )
            )
        }
    }

    override suspend fun getKey(id: String, keys: MyKeys, result: (UiState<ArrayList<MyKeys>>) -> Unit) {
        val document = dataBase.collection("keys")
            .document(id).collection("MyKeys")

        document.addSnapshotListener { value, error ->
            if (value != null){
                val list: ArrayList<MyKeys> = arrayListOf()
                val data = value.toObjects(MyKeys::class.java)

                if (data.isNotEmpty()){
                    list.addAll(data)
                    result.invoke(
                        UiState.Success(
                            list
                        )
                    )
                }else{
                    result.invoke(
                        UiState.Failure(
                            "lista vazia"
                        )
                    )
                }
            }else{
                result.invoke(
                    UiState.Failure(
                        "Valor null"
                    )
                )
            }
        }
    }

    override suspend fun updateKey(
        id: String,
        keysId: String,
        hashMap: HashMap<String, Any>,
        result: (UiState<String>) -> Unit
    ) {
        val document = dataBase.collection("keys")
            .document(id).collection("MyKeys").document(keysId)
        document.update(hashMap).addOnSuccessListener {
            result.invoke(
                UiState.Success(
                    "Sucesso"
                )
            )
        }.addOnFailureListener {
            result.invoke(
                UiState.Failure(
                    "Error ao atualizar"
                )
            )
        }
    }

    override suspend fun deleteKey(id: String, keys: MyKeys, result: (UiState<String>) -> Unit) {
        val document = dataBase.collection("keys")
            .document(id).collection("MyKeys").document(keys.id!!)

        document.delete().addOnSuccessListener {
            result.invoke(
                UiState.Success(
                    "sucesso ao deletar"
                )
            )
        }.addOnFailureListener {
            result.invoke(
                UiState.Failure(
                    it.localizedMessage
                )
            )
        }
    }

}