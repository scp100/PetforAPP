package com.example.core.persistence

import androidx.room.* 
import com.example.core.model.Task
import com.example.core.model.Pet

@Dao interface TaskDao {
  @Query("SELECT * FROM tasks") fun getAll(): List<TaskEntity>
  @Insert suspend fun insert(t: TaskEntity)
  @Update suspend fun update(t: TaskEntity)
  @Delete suspend fun delete(t: TaskEntity)
}

@Entity("tasks")
data class TaskEntity(
   @PrimaryKey val id: Int,
   val title: String,
   val description: String,
   val priority: Int,
   val dueDate: String,
   val createdAt: String,
   val completedAt: String?,
)

@Database(entities=[TaskEntity::class], version=1)
abstract class AppDatabase : RoomDatabase() {
  abstract fun taskDao(): TaskDao
}

// PetData & PetDao 類似省略…

interface PersistenceService {
  suspend fun loadTasks(): List<Task>
  suspend fun saveTask(task: Task)
  // load/save Pet…
}
