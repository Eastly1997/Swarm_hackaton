package com.lakbay.pamayanan

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.lakbay.pamayanan.databinding.ActivityRegisterUserBinding
import com.lakbay.pamayanan.utils.CommonUtils
import com.lakbay.pamayanan.utils.SharedPrefUtils
import com.lakbay.pamayanan.viewModels.User

class RegisterUserActivity: AppCompatActivity() {

    private lateinit var binding: ActivityRegisterUserBinding
    private lateinit var uid: String
    private val db = Firebase.firestore
    private val usersRef = db.collection(CommonUtils.FIREBASE_USER)

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

        uid = intent.getStringExtra(User.FIELD_UID).toString()

        binding.warningMessage = ""
        binding.checkUser = View.OnClickListener {
            updateUser()
        }
    }

    private fun updateUser() {

        usersRef.whereEqualTo("userName", binding.userName).get().addOnSuccessListener {
            if(it.isEmpty) {
                usersRef.document(uid).update(User.FIELD_USER_NAME, binding.userName).addOnSuccessListener {
                    SharedPrefUtils.saveData(this, User.FIELD_USER_NAME, binding.userName.toString())
                    startActivity(Intent(this, MainActivity ::class.java))
                    finishAffinity()
                }
            } else {
                binding.warningMessage = "That hero is already helping"
            }
        }
    }
}