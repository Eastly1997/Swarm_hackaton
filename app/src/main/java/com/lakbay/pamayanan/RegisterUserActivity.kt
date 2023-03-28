package com.lakbay.pamayanan

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.widget.doOnTextChanged
import com.lakbay.pamayanan.databinding.ActivityRegisterUserBinding
import com.lakbay.pamayanan.utils.FirebaseUtils
import com.lakbay.pamayanan.viewModels.User

class RegisterUserActivity: AppCompatActivity() {

    private lateinit var binding: ActivityRegisterUserBinding
    private var isValidPhone = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init() {
        binding.phoneNumber.doOnTextChanged { text, _, _, _ ->
            isValidPhone = text?.length == 10 && text[0] == '9'
        }

        binding.terms.setOnCheckedChangeListener{ _, isChecked ->
            binding.register.isClickable = isChecked
            binding.register.isEnabled = isChecked

            val color = if(isChecked) ContextCompat.getColor(this, R.color.primary) else
                ContextCompat.getColor(this, R.color.hint)

            var buttonDrawable: Drawable = binding.register.background
            buttonDrawable = DrawableCompat.wrap(buttonDrawable)
            DrawableCompat.setTint(buttonDrawable, color)


        }

        binding.terms.isChecked = false

        binding.validateInput = View.OnClickListener {
            if(validateInput()) {
                val intent = Intent(this, PhoneAuthenticationActivity::class.java)
                intent.putExtra(User.FIELD_MOBILE_NUMBER, binding.phoneNumber.text.toString())
                intent.putExtra(User.FIELD_USER_NAME, binding.userName.text.toString())
                startActivity(intent)
            }
        }
    }
    private fun validateInput() : Boolean {
        with(binding) {

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