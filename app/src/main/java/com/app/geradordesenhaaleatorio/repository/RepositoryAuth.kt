package com.app.geradordesenhaaleatorio.repository

import com.app.geradordesenhaaleatorio.model.User
import com.app.geradordesenhaaleatorio.ultis.UiState

interface RepositoryAuth {
    suspend fun createUser(user: User, result: (UiState<String>) -> Unit)
    suspend fun authUser(user: User, result: (UiState<String>) -> Unit)
    fun saveUser(user: User, result: (UiState<String>) -> Unit)
    suspend fun sigOut()
}