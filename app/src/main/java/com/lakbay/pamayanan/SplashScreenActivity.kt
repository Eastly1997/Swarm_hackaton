package com.lakbay.pamayanan

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class SplashScreenActivity : AppCompatActivity (){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val user = Firebase.auth.currentUser;
        var intent: Intent
        if (user != null) {
            intent = Intent(this@SplashScreenActivity, MainActivity ::class.java)
        } else {
            intent = Intent(this@SplashScreenActivity, ProvisioningActivity ::class.java)
        }
        startActivity(intent)
        finish()
    }
}