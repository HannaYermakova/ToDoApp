package by.aermakova.todoapp.ui.splash

import androidx.lifecycle.ViewModel
import javax.inject.Inject

class SplashViewModel @Inject constructor(
    startAppActivity: StartAppActivity
) : ViewModel() {

    init {
        startAppActivity.onStartAppActivity()
    }
}

interface StartAppActivity {

    fun onStartAppActivity()

}