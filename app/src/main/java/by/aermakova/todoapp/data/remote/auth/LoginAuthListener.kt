package by.aermakova.todoapp.data.remote.auth

import by.aermakova.todoapp.ui.login.LoginNavigation


class LoginAuthListener(private val loginNavigation: LoginNavigation) :
    AuthListener {

    override fun isSignIn() {
        loginNavigation.navigateAfterLoginSuccess()
    }

    override fun notSignIn() {
    }
}