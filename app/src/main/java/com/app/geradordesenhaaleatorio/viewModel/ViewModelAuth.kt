package com.app.geradordesenhaaleatorio.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.geradordesenhaaleatorio.model.User
import com.app.geradordesenhaaleatorio.repository.RepositoryAuth
import com.app.geradordesenhaaleatorio.ultis.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModelAuth @Inject constructor(
    private val repositoryAuth: RepositoryAuth
): ViewModel(){
    private val _authState = MutableLiveData<UiState<String>>()
    val authState: LiveData<UiState<String>>
        get() = _authState

    private val _createState = MutableLiveData<UiState<String>>()
    val createState: LiveData<UiState<String>>
        get() = _createState

    fun createUser(user: User) {
        _createState.value = UiState.Loading
        viewModelScope.launch {
            repositoryAuth.createUser(user) { result ->
                _createState.value = result
            }
        }
    }

    fun authUser(user: User) {
        _authState.value = UiState.Loading
        viewModelScope.launch {
            repositoryAuth.authUser(user) { result ->
                _authState.value = result
            }
        }
    }

    fun sigOut(){
        viewModelScope.launch {
            repositoryAuth.sigOut()
        }
    }
}
