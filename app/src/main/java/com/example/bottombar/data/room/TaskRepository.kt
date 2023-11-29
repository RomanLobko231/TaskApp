package com.example.bottombar.data.room

import kotlinx.coroutines.flow.Flow

class TaskRepository(private val dao: TaskDao) {

    suspend fun insertTask(task: Task){
        dao.insertTask(task)
    }

    suspend fun deleteTask(task: Task){
        dao.deleteTask(task)
    }

    suspend fun getTaskById(id: Int): Task?{
       return dao.getTaskById(id)
    }

    fun getActiveTasks(): Flow<List<Task>>{
        return dao.getActiveTasks()
    }

    fun getDoneTasks(): Flow<List<Task>>{
        return dao.getDoneTasks()
    }
}