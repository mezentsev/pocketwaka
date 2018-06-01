package com.kondenko.pocketwaka.screens.base

import android.support.annotation.StringRes

interface BaseView {

    fun showError(throwable: Throwable?, @StringRes messageStringRes: Int? = null)

}