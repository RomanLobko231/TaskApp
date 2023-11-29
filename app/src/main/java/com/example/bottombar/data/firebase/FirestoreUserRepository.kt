package com.example.bottombar.data.firebase

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class FirestoreUserRepository(firebaseFirestore: FirebaseFirestore) {
    private val usersCollectionRef = firebaseFirestore.collection("users")
    private val user = Firebase.auth.currentUser

    suspend fun saveUser(user: User) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                usersCollectionRef.document(user.id).set(user).await()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getCurrentUser(): FirebaseUser?{
        return user
    }

    suspend fun getUserById(id: String): User? {
        var user: User? = null



        return usersCollectionRef.document(id).get().await().toObject<User>()
    }

    fun getProjectIds(): Flow<List<String>> = callbackFlow {
        val listenerRegistration = usersCollectionRef.document(user!!.uid)
            .addSnapshotListener { querySnapshot, error ->
                error?.let {
                    println(error.message)
                    close(it)
                    return@addSnapshotListener
                }

                querySnapshot?.let {
                    val projectsIds: List<String> = it.get("projectsIds") as List<String>
                    trySend(projectsIds)
                }

            }
        awaitClose {
            listenerRegistration.remove()
        }
    }


    suspend fun addProjectId(id: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                usersCollectionRef.document(user!!.uid)
                    .update("projectsIds", FieldValue.arrayUnion(id)).await()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    suspend fun deleteProjectId(id: String) {
        CoroutineScope(Dispatchers.IO).launch {
            usersCollectionRef.document(user!!.uid).update("projectsIds", FieldValue.arrayRemove(id)).await()
        }
    }
}