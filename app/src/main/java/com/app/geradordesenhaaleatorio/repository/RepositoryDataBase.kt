package com.app.geradordesenhaaleatorio.repository

import com.app.geradordesenhaaleatorio.model.MyKeys
import com.app.geradordesenhaaleatorio.ultis.UiState

interface RepositoryDataBase {
    suspend fun saveKey(id:String, keys: MyKeys, result: (UiState<String>)-> Unit)
    suspend fun getKey(id: String, keys: MyKeys, result: (UiState<ArrayList<MyKeys>>) -> Unit)
    suspend fun updateKey(id: String, keysId: String, hashMap: HashMap<String, Any>, result: (UiState<String>) -> Unit)
    suspend fun deleteKey(id: String, keys: MyKeys, result: (UiState<String>) -> Unit)
}