package com.kondenko.pocketwaka.utils

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.os.Build
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v4.view.ViewCompat
import android.view.View
import com.crashlytics.android.Crashlytics
import io.reactivex.Single
import timber.log.Timber

fun isConnectionAvailable(context: Context): Boolean {
    val service = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    return service.activeNetworkInfo?.isConnectedOrConnecting?:false
}

fun <T> T?.singleOrErrorIfNull(exception: Throwable): Single<T> = this?.let { Single.just(it) } ?: Single.error(exception)

/**
 * Extension functions.
 */

inline fun SharedPreferences.edit(crossinline action: SharedPreferences.Editor.() -> Unit) {
    val editor = edit()
    editor.action()
    editor.apply()
}

inline fun FragmentManager.transaction(crossinline action: FragmentTransaction.() -> FragmentTransaction) {
    this.beginTransaction().action().commit()
}

fun View.elevation(elevation: Float) {
    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) ViewCompat.setElevation(this, elevation)
    else this.elevation = elevation
}

/**
 * Prints log output and sends a report to crashlyics about the given exception
 */
fun Throwable.report(message: String? = null) {
    Timber.e(this, message?:this.message)
    Crashlytics.logException(this)
}