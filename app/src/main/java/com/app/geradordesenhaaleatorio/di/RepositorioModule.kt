package com.app.geradordesenhaaleatorio.di

import com.app.geradordesenhaaleatorio.repository.RepositoryAuth
import com.app.geradordesenhaaleatorio.repository.RepositoryDataBase
import com.app.geradordesenhaaleatorio.repository.RepositoryImpleAuth
import com.app.geradordesenhaaleatorio.repository.RepositoryImpleDataBase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object RepositorioModule {

    @Provides
    @Singleton
    fun providesRepositorioAuth(
       database: FirebaseFirestore,
       auth: FirebaseAuth
    ): RepositoryAuth{
        return RepositoryImpleAuth(auth,database)
    }

    @Provides
    @Singleton
    fun providesRepositoroDatabase(
        database: FirebaseFirestore,
    ): RepositoryDataBase{
        return RepositoryImpleDataBase(database)
    }

}