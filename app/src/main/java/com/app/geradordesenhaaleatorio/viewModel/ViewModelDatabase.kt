package com.app.geradordesenhaaleatorio.viewModel

import android.provider.ContactsContract.RawContacts.Data
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.geradordesenhaaleatorio.model.MyKeys
import com.app.geradordesenhaaleatorio.repository.RepositoryDataBase
import com.app.geradordesenhaaleatorio.ultis.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ViewModelDatabase @Inject constructor(
    private val repositoryDataBase: RepositoryDataBase
): ViewModel(){
    private val _saveKeyState = MutableLiveData<UiState<String>>()
    val saveKeyState: LiveData<UiState<String>> get() = _saveKeyState

    private val _getKeyState = MutableLiveData<UiState<List<MyKeys>>>()
    val getKeyState: LiveData<UiState<List<MyKeys>>> get() = _getKeyState

    private val _updateKeyState = MutableLiveData<UiState<String>>()
    val updateKeyState: LiveData<UiState<String>>
        get() = _updateKeyState

    private val _deleteKeyState = MutableLiveData<UiState<String>>()
    val deleteKeyState: LiveData<UiState<String>> get() = _deleteKeyState

    fun saveKey(id: String, keys: MyKeys) {
        _saveKeyState.value = UiState.Loading
        viewModelScope.launch {
            repositoryDataBase.saveKey(id, keys) { result ->
                _saveKeyState.value = result
            }
        }
    }

    fun getKey(id: String) {
        _getKeyState.value = UiState.Loading
        viewModelScope.launch {
            repositoryDataBase.getKey(id, MyKeys()) { result ->
                _getKeyState.value = result
            }
        }
    }

    fun updateKey(id: String, keysId: String, hashMap: HashMap<String, Any>) {
        _updateKeyState.value = UiState.Loading
        viewModelScope.launch {
            repositoryDataBase.updateKey(id, keysId, hashMap) { result ->
                _updateKeyState.value = result
            }
        }
    }

    fun deleteKey(id: String, keys: MyKeys) {
        _deleteKeyState.value = UiState.Loading
        viewModelScope.launch {
            repositoryDataBase.deleteKey(id, keys) { result ->
                _deleteKeyState.value = result
            }
        }
    }
}
