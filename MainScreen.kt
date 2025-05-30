package com.example.pethabittracker.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.core.model.Task
import com.example.core.model.Pet

@Composable
fun MainScreen(viewModel: MainViewModel = rememberMainViewModel()) {
  val tasks by viewModel.tasks.collectAsState()
  val pet   by viewModel.pet.collectAsState()

  Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
    // 寵物面板
    PetPanel(pet)

    Spacer(Modifier.height(16.dp))

    // 今日進度 & 月度進度
    DailyMonthlyProgress(
      dailyDone = viewModel.dailyDone,
      dailyGoal = viewModel.dailyGoal,
      monthlyDone = viewModel.monthlyDone,
      monthlyGoal = viewModel.monthlyGoal
    )

    Spacer(Modifier.height(16.dp))

    // Task 列表
    TaskList(tasks,
      onAdd = { viewModel.addTask(it) },
      onComplete = { viewModel.completeTask(it) }
    )
  }
}
