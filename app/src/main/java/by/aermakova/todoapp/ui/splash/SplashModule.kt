package by.aermakova.todoapp.ui.splash

import android.app.Activity
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.ViewModel
import by.aermakova.todoapp.data.di.module.ViewModelKey
import by.aermakova.todoapp.ui.app.AppActivity
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
class SplashModule {

    @Provides
    fun provideListener(activity: Activity): StartAppActivity = object : StartAppActivity {
        override fun onStartAppActivity() {
            with(activity) {
                Handler(Looper.getMainLooper())
                    .postDelayed({
                        startActivity(Intent(this, AppActivity::class.java))
                        finish()
                    }, SPLASH_DISPLAY_LENGTH)
                Log.d("A_SplashModule", "onStartAppActivity")
            }
        }
    }

    @Provides
    @IntoMap
    @ViewModelKey(SplashViewModel::class)
    fun provideViewModule(viewModel: SplashViewModel): ViewModel = viewModel
}

private const val SPLASH_DISPLAY_LENGTH = 3000L