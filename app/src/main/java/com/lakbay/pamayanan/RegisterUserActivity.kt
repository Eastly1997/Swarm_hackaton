package com.lakbay.pamayanan

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.lakbay.pamayanan.databinding.ActivityRegisterUserBinding

class RegisterUserActivity: AppCompatActivity() {

    private lateinit var binding: ActivityRegisterUserBinding
    private lateinit var uid: String
    private val db = Firebase.firestore
    private val usersRef = db.collection("users")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        if(intent.extras == null) {
            startActivity(Intent(this, ProvisioningActivity ::class.java))
            finish()
        }

        uid = intent.getStringExtra("user_id").toString()

        binding.warningMessage = ""
        binding.checkUser = View.OnClickListener {
            updateUser()
        }
    }

    private fun updateUser() {

        usersRef.whereEqualTo("userName", binding.userName).get().addOnSuccessListener {
            if(it.isEmpty) {
                usersRef.document(uid).update("userName", binding.userName).addOnSuccessListener {
                    startActivity(Intent(this, MainActivity ::class.java))
                    finishAffinity()
                }
            } else {
                binding.warningMessage = "That hero is already helping"
            }
        }
    }
}