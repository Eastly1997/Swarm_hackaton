package com.lakbay.pamayanan

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.widget.doOnTextChanged
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.lakbay.pamayanan.databinding.ActivityRegisterUserBinding
import com.lakbay.pamayanan.utils.CommonConstants
import com.lakbay.pamayanan.utils.SharedPrefUtils
import com.lakbay.pamayanan.viewModels.User
import java.lang.StringBuilder

class RegisterUserActivity: AppCompatActivity() {

    private lateinit var binding: ActivityRegisterUserBinding
    private lateinit var uid: String
    private val db = Firebase.firestore
    private val usersRef = db.collection(CommonConstants.FIREBASE_USER)
    private var isValidPhone = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init() {
        binding.phoneNumber.doOnTextChanged { text, start, before, count ->
            isValidPhone = text?.length == 10 && text[0] == '9'
        }

        binding.terms.setOnCheckedChangeListener{ _, isChecked ->
            binding.register.isClickable = isChecked
            binding.register.isEnabled = isChecked

            val color = if(isChecked) resources.getColor(R.color.primary) else
            resources.getColor(R.color.hint)

            var buttonDrawable: Drawable = binding.register.background
            buttonDrawable = DrawableCompat.wrap(buttonDrawable);
            DrawableCompat.setTint(buttonDrawable, color);


        }

        binding.terms.isChecked = false

        binding.validateInput = View.OnClickListener {
            if(validateInput()) {
                binding.loading.visibility = View.VISIBLE;
                usersRef.whereEqualTo(User.FIELD_MOBILE_NUMBER, "+63${binding.phoneNumber.text}")
                    .get().addOnSuccessListener {
                        binding.loading.visibility = View.GONE;
                        if(it.isEmpty) {
                            // proceed to phone verification
                            val intent = Intent(this, PhoneAuthenticationActivity::class.java)
                            intent.putExtra(User.FIELD_MOBILE_NUMBER, binding.phoneNumber.text.toString())
                            intent.putExtra(User.FIELD_USER_NAME, binding.userName.text.toString())
                            startActivity(intent)
                        } else {
                            // alert user already taken
                            Toast.makeText(this,
                                "Mobile number is already in use, would you like to login instead?",
                                Toast.LENGTH_LONG).show()
                        }
                    }
            }
        }
    }
    private fun validateInput() : Boolean {
        with(binding) {
            if(userName.text.toString().isNullOrEmpty() || userName.text.toString().length < 6) {
                warningMessage = getString(R.string.username_error)
                return false;
            } else {
                warningMessage = ""
            }

            if(!isValidPhone) {
                phoneWarning.visibility = View.VISIBLE
                phoneWarning.text = getString(R.string.username_error)
                return false
            } else {
                phoneWarning.visibility = View.GONE
            }

            return true
        }
    }
}