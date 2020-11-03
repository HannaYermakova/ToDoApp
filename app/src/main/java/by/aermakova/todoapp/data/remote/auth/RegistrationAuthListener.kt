package by.aermakova.todoapp.data.remote.auth

import by.aermakova.todoapp.ui.auth.LoginNavigation

class RegistrationAuthListener(private val loginNavigation: LoginNavigation) :
    AuthListener {

    override fun isSignIn() {
        loginNavigation.navigateAfterRegisterSuccess()
    }

    override fun notSignIn() {
    }
}