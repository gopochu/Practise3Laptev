package com.vadlap.practise3.presentation.profile

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.vadlap.practise3.presentation.ViewModelFactory
import java.io.File
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(navController: NavController) {
    val context = LocalContext.current
    val viewModel: ProfileViewModel = viewModel(factory = ViewModelFactory(context))
    val profile by viewModel.profileState.collectAsState()

    var name by remember { mutableStateOf("") }
    var resumeUrl by remember { mutableStateOf("") }
    var avatarUri by remember { mutableStateOf("") }
    var classTime by remember { mutableStateOf("") } // Время пары
    var timeError by remember { mutableStateOf(false) } // Ошибка валидации

    var showAvatarDialog by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    LaunchedEffect(profile) {
        if (name.isEmpty()) name = profile.name
        if (resumeUrl.isEmpty()) resumeUrl = profile.resumeUrl
        if (avatarUri.isEmpty()) avatarUri = profile.avatarUri
        if (classTime.isEmpty()) classTime = profile.classTime
    }

    val tempUri = remember {
        val file = File.createTempFile("avatar_", ".jpg", context.cacheDir)
        FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
    }

    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let { avatarUri = it.toString() }
    }
    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) avatarUri = tempUri.toString()
    }
    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) cameraLauncher.launch(tempUri)
        else Toast.makeText(context, "Нужен доступ к камере", Toast.LENGTH_SHORT).show()
    }
    
    val notificationPermissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { }

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    fun saveAndSchedule() {
        // Валидация времени
        if (!classTime.matches(Regex("^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$"))) {
            timeError = true
            Toast.makeText(context, "Неверный формат времени (HH:mm)", Toast.LENGTH_SHORT).show()
            return
        }
        timeError = false

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            if (!alarmManager.canScheduleExactAlarms()) {
                val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                context.startActivity(intent)
                Toast.makeText(context, "Разрешите установку будильников", Toast.LENGTH_LONG).show()
                return
            }
        }

        scheduleAlarm(context, classTime, name)

        viewModel.saveProfile(name, avatarUri, resumeUrl, classTime)
        navController.popBackStack()
    }

    if (showAvatarDialog) {
        AlertDialog(
            onDismissRequest = { showAvatarDialog = false },
            title = { Text("Выберите фото") },
            text = {
                Column {
                    TextButton(onClick = { galleryLauncher.launch("image/*"); showAvatarDialog = false }) { Text("Из галереи") }
                    TextButton(onClick = { permissionLauncher.launch(Manifest.permission.CAMERA); showAvatarDialog = false }) { Text("Сделать фото") }
                }
            },
            confirmButton = { TextButton(onClick = { showAvatarDialog = false }) { Text("Отмена") } }
        )
    }

    if (showTimePicker) {
        val initialHour = classTime.split(":").getOrNull(0)?.toIntOrNull() ?: 9
        val initialMinute = classTime.split(":").getOrNull(1)?.toIntOrNull() ?: 0
        val timePickerState = rememberTimePickerState(initialHour, initialMinute, true)

        TimePickerDialog(
            onDismissRequest = { showTimePicker = false },
            onConfirm = {
                val hour = timePickerState.hour.toString().padStart(2, '0')
                val minute = timePickerState.minute.toString().padStart(2, '0')
                classTime = "$hour:$minute"
                timeError = false
                showTimePicker = false
            }
        ) {
            TimePicker(state = timePickerState)
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Редактирование профиля") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape)
                    .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                    .clickable { showAvatarDialog = true }
            ) {
                if (avatarUri.isNotEmpty()) {
                    Image(
                        painter = rememberAsyncImagePainter(avatarUri),
                        contentDescription = "Аватар",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(Icons.Default.Person, null, Modifier.size(80.dp), MaterialTheme.colorScheme.secondary)
                }
                Box(Modifier.fillMaxSize(), Alignment.BottomEnd) {
                    Icon(Icons.Default.CameraAlt, "Изменить", Modifier.padding(16.dp))
                }
            }

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("ФИО") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = resumeUrl,
                onValueChange = { resumeUrl = it },
                label = { Text("Ссылка на резюме (PDF)") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = classTime,
                onValueChange = { 
                    classTime = it
                    timeError = !it.matches(Regex("^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$")) && it.isNotEmpty()
                },
                label = { Text("Время любимой пары (HH:mm)") },
                trailingIcon = {
                    Icon(
                        Icons.Default.AccessTime,
                        contentDescription = "Выбрать время",
                        modifier = Modifier.clickable { showTimePicker = true }
                    )
                },
                isError = timeError,
                supportingText = { if (timeError) Text("Введите время в формате HH:mm") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { saveAndSchedule() },
                enabled = !timeError && classTime.isNotEmpty(), // Блокировка кнопки при ошибке
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Готово")
            }
        }
    }
}

fun scheduleAlarm(context: Context, time: String, userName: String) {
    try {
        val parts = time.split(":")
        val hour = parts[0].toInt()
        val minute = parts[1].toInt()

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
        }

        if (calendar.timeInMillis < System.currentTimeMillis()) {

        }

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("USER_NAME", userName)
        }
        
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
        } else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
        }
        
        Toast.makeText(context, "Напоминание установлено на $time", Toast.LENGTH_SHORT).show()
        
    } catch (e: Exception) {
        e.printStackTrace()
        Toast.makeText(context, "Ошибка установки будильника", Toast.LENGTH_SHORT).show()
    }
}
