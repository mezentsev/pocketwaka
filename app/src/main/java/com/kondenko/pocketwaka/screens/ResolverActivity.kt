package com.kondenko.pocketwaka.screens

import android.app.Activity
import android.content.Intent
import com.kondenko.pocketwaka.api.oauth.AccessTokenUtils
import com.kondenko.pocketwaka.screens.login.LoginActivity
import com.kondenko.pocketwaka.screens.main.MainActivity

/**
 * Checks if the user is logged in and proceeds to the appropriate screen
 */
class ResolverActivity : Activity() {
    override fun onResume() {
        super.onResume()
        val activityClass = if (AccessTokenUtils.isTokenSaved(this)) MainActivity::class.java else LoginActivity::class.java
        this.finish()
        startActivity(Intent(this, activityClass))
    }
}
