package by.aermakova.todoapp.data.remote.auth.loginManager

import android.content.Intent

interface AppLoginManager {

    val errorMessage: String?

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean
}