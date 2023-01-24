package com.lakbay.pamayanan

import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import com.lakbay.pamayanan.utils.CommonConstants.Companion.FONT_SCALE_LARGE

abstract class BaseActivity : AppCompatActivity() {
    override fun attachBaseContext(newBase: Context?) {
        val newOverride = Configuration(newBase?.resources?.configuration)
        if(newOverride.fontScale > FONT_SCALE_LARGE) {
            newOverride.fontScale = FONT_SCALE_LARGE
            applyOverrideConfiguration(newOverride)
        }
        super.attachBaseContext(newBase)
    }
}