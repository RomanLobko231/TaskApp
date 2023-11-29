package com.example.bottombar.data.firebase

import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class FirestoreProjectRepository(private val firebaseFirestore: FirebaseFirestore) {

    private val projectsCollectionRef = firebaseFirestore.collection("projects")
    private val firestoreUserRepository = FirestoreUserRepository(firebaseFirestore)

    fun getUserProjects(): Flow<MutableList<Project>> = flow {
        val projectList: MutableList<Project> = mutableListOf()
        CoroutineScope(Dispatchers.IO).launch {
            firestoreUserRepository.getProjectIds().collect {
                for (id in it) {
                    getProjectById(id)?.let { it1 -> projectList.add(it1) }
                }
                println(projectList.toString())
            }
        }
        emit(projectList)
    }

    fun fetchUserProjects(): Flow<List<Project>> = callbackFlow {
        val userId = firestoreUserRepository.getCurrentUser()!!.uid
        val userDocRef = firebaseFirestore.collection("users").document(userId)

        val listener = userDocRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }

            val projectIds = snapshot?.get("projectsIds") as? List<String> ?: emptyList()
            if (projectIds.isNotEmpty()) {
                val projectsCollectionRef = firebaseFirestore.collection("projects")
                    .whereIn(FieldPath.documentId(), projectIds)

                 projectsCollectionRef.addSnapshotListener { projectSnapshot, error1 ->
                    if (error1 != null) {
                        close(error1)
                        return@addSnapshotListener
                    }

                    val projects = projectSnapshot?.documents?.mapNotNull { docSnapshot ->
                        docSnapshot.toObject<Project>()
                    }
                    if (projects != null) {
                        trySend(projects)
                    }
                }
            } else {
                trySend(emptyList())
            }
        }

        awaitClose {
            listener.remove()
        }
    }


    fun getIdForNewProject(): String {
        return projectsCollectionRef.document().id
    }

    suspend fun saveProject(project: Project) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                projectsCollectionRef.document(project.id).set(
                    Project(
                        projectName = project.projectName,
                        deadline = project.deadline,
                        priority = project.priority,
                        timestamp = project.timestamp,
                        id = project.id
                    )
                ).await()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    suspend fun updateProject(project: Project) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                projectsCollectionRef.document(project.id).update(
                    "projectName", project.projectName,
                    "deadline", project.deadline,
                    "priority", project.priority
                ).await()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    suspend fun deleteProjectById(id: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                projectsCollectionRef.document(id).delete().await()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    suspend fun getProjectById(id: String): Project? {
        var project: Project? = null
        try {
            project = projectsCollectionRef.document(id).get().await().toObject<Project>()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return project
    }

    fun getProjects(): Flow<List<Project>> = callbackFlow {
        val listenerRegistration = projectsCollectionRef
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { querySnapshot, error ->
                error?.let {
                    println(error.message)
                    close(it)
                    return@addSnapshotListener
                }

                querySnapshot?.let {
                    val projects = it.documents.mapNotNull { document ->
                        document.toObject<Project>()
                    }
                    trySend(projects)
                }
            }
        awaitClose {
            listenerRegistration.remove()
        }
    }
}