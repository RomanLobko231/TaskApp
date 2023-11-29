package com.example.bottombar.di

import android.app.Application
import androidx.room.Room
import com.example.bottombar.data.firebase.FirestoreProjectRepository
import com.example.bottombar.data.firebase.FirestoreUserRepository
import com.example.bottombar.data.room.TaskDatabase
import com.example.bottombar.data.room.TaskRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(app: Application): TaskDatabase {
        return Room.databaseBuilder(
            app,
            TaskDatabase::class.java,
            "task_db"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore {
        return Firebase.firestore
    }

    @Provides
    fun provideFirestoreProjectRepository(firebaseFirestore: FirebaseFirestore): FirestoreProjectRepository {
        return FirestoreProjectRepository(firebaseFirestore)
    }

    @Provides
    fun provideFirestoreUserRepository(firebaseFirestore: FirebaseFirestore): FirestoreUserRepository {
        return FirestoreUserRepository(firebaseFirestore)
    }

    @Provides
    @Singleton
    fun provideRepository(taskDb: TaskDatabase): TaskRepository {
        return TaskRepository(taskDb.dao)
    }
}