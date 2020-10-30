package by.aermakova.todoapp.data.remote.auth.loginManager

import android.content.Intent

interface AppLoginManager {

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean
}