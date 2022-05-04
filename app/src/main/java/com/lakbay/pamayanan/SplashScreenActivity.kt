package com.lakbay.pamayanan

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.lakbay.pamayanan.utils.CommonConstants
import com.lakbay.pamayanan.utils.CommonUtils
import com.lakbay.pamayanan.utils.SharedPrefUtils


class SplashScreenActivity : AppCompatActivity (){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val user = Firebase.auth.currentUser;
        val storageEnv = SharedPrefUtils.getStringData(this, CommonConstants.ENVIRONMENT)
        val currentEnv = CommonConstants.ENVIRONMENT_STAGING
        if(storageEnv != currentEnv) {
            Firebase.auth.signOut();
            intent = Intent(this@SplashScreenActivity, ProvisioningActivity ::class.java)
            SharedPrefUtils.clearData(this)
            SharedPrefUtils.saveData(this, CommonConstants.ENVIRONMENT, currentEnv)
        } else {
            intent = if (user != null) {
                Intent(this@SplashScreenActivity, MainActivity ::class.java)
            } else {
                Intent(this@SplashScreenActivity, ProvisioningActivity ::class.java)
            }
        }

        startActivity(intent)
        finish()
    }
}