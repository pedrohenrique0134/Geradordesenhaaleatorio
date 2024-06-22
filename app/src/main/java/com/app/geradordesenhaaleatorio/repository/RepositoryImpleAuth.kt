package com.app.geradordesenhaaleatorio.repository

import com.app.geradordesenhaaleatorio.model.User
import com.app.geradordesenhaaleatorio.ultis.UiState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RepositoryImpleAuth(
    private val auth: FirebaseAuth,
    private val database: FirebaseFirestore
): RepositoryAuth {
    override suspend fun createUser(user: User, result: (UiState<String>) -> Unit) {
       auth.createUserWithEmailAndPassword(user.email!!, user.password!!)
           .addOnCompleteListener {
           if (it.isSuccessful){
               user.id = it.result.user?.uid

               saveUser(user){ state ->
                   when(state){
                       is UiState.Failure -> {
                           result.invoke(
                               UiState.Failure(
                                   state.error
                               )
                           )
                       }
                       UiState.Loading -> {}
                       is UiState.Success -> {
                           result.invoke(
                               UiState.Success(
                                   state.data
                               )
                           )
                       }
                   }
               }
           }
       }.addOnFailureListener {
           result.invoke(
               UiState.Failure(
                   it.localizedMessage
               )
           )
           }
    }

    override suspend fun authUser(user: User, result: (UiState<String>) -> Unit) {
        auth.signInWithEmailAndPassword(
            user.email!!,
            user.password!!
        ).addOnSuccessListener {
            result.invoke(
                UiState.Success(
                    "Usuario logado"
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

    override suspend fun sigOut() {
        auth.signOut()
    }

    override fun saveUser(user: User, result: (UiState<String>) -> Unit) {
        val document = database.collection("users").document(user.id!!)

        document.set(user).addOnSuccessListener {
            result.invoke(
                UiState.Success(
                    "Sucesso ao criar usuario"
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