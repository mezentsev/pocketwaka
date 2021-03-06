package com.kondenko.pocketwaka.screens.auth

import com.kondenko.pocketwaka.data.auth.model.AccessToken
import com.kondenko.pocketwaka.screens.base.BaseView

interface AuthView : BaseView {

    fun openAuthUrl(url: String)

    fun onGetTokenSuccess(token: AccessToken)

}