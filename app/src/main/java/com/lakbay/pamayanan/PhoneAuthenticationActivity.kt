package com.lakbay.pamayanan

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.lakbay.pamayanan.databinding.ActivityPhoneAuthenticationBinding
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.lakbay.pamayanan.utils.CommonConstants
import com.lakbay.pamayanan.utils.CommonUtils
import com.lakbay.pamayanan.viewModels.User
import java.util.concurrent.TimeUnit

class PhoneAuthenticationActivity : AppCompatActivity() {

    lateinit var binding: ActivityPhoneAuthenticationBinding
    private lateinit var auth: FirebaseAuth
    private var phoneNumber: String? = null
    private var storedVerificationId: String? = ""
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks

    private val db = Firebase.firestore
    private val usersRef = db.collection(CommonConstants.FIREBASE_USER)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhoneAuthenticationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init() {
        binding.resendOtp.text = getString(R.string.verification_sending)
        auth = Firebase.auth
        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                binding.verificationCode.setText(credential.smsCode)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.e("FIREBASE", e.toString())
                binding.commonLoading.visibility = View.GONE

                Toast.makeText(this@PhoneAuthenticationActivity, e.toString(), Toast.LENGTH_LONG).show()

                if (e is FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                } else if (e is FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    binding.resendOtp.text = getString(R.string.verifiation_try_again)
2
                }

                // Show a message and update the UI
            }

            override fun onCodeSent(verificationId: String,
                                    token: PhoneAuthProvider.ForceResendingToken) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.

                // Save verification ID and resending token so we can use them later
                binding.commonLoading.visibility = View.GONE
                storedVerificationId = verificationId
                resendToken = token
                binding.resendOtp.isClickable = false
                object : CountDownTimer(60000, 1000) {

                    override fun onTick(millisUntilFinished: Long) {
                        binding.resendOtp.text = getString(R.string.verification_count_down) + String.format("%02d", millisUntilFinished / 1000)
                    }

                    override fun onFinish() {
                        binding.resendOtp.text = getString(R.string.verification_resend)
                        binding.resendOtp.isClickable = true
                    }
                }.start()
                Log.d("FIREBASE", "Code Sent")

            }
        }
        phoneNumber = intent.getStringExtra("mobile_number");
        if(phoneNumber.isNullOrEmpty()) {
            finish()
            return
        } else {
            binding.phoneNumber.text = "+63 " + phoneNumber!!.substring(0,3)  + " " +
                    phoneNumber!!.substring(3,6) + " " + phoneNumber!!.substring(6,10)
            phoneNumber = "+63$phoneNumber"
        }
        startPhoneNumberVerification(phoneNumber!!)

        binding.verificationCode.doOnTextChanged { text, start, before, count ->
            if(!text.isNullOrEmpty() && text.length == 6) {
                verifyPhoneNumberWithCode(storedVerificationId, text.toString())
            }
        }

        binding.resendOtp.setOnClickListener {
            binding.resendOtp.isClickable = false
            resendVerificationCode()
        }
    }

    private fun startPhoneNumberVerification(phoneNumber: String) {
        // [START start_phone_auth]
        binding.commonLoading.visibility = View.VISIBLE
        Log.d("FIREBASE", "PhoneNumber: $phoneNumber")
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this)                 // Activity (for callback binding)
            .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
        // [END start_phone_auth]
    }

    private fun verifyPhoneNumberWithCode(verificationId: String?, code: String) {
        // [START verify_with_code]
        val credential = PhoneAuthProvider.getCredential(verificationId!!, code)
        // [END verify_with_code]
        signInWithPhoneAuthCredential(credential)
    }

    private fun resendVerificationCode() {
        val optionsBuilder = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber!!)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this)                 // Activity (for callback binding)
            .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
        optionsBuilder.setForceResendingToken(resendToken)
        PhoneAuthProvider.verifyPhoneNumber(optionsBuilder.build())
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = task.result?.user
                    if(user!= null) registerUser(user)
                    else {
                        startActivity(Intent(this, MainActivity ::class.java))
                        finishAffinity()
                    }
                } else {
                    // Sign in failed, display a message and update the UI
                    Log.d("FIREBASE", "Sign in Failed" + task.exception!!.message)

                    Toast.makeText(this, task.exception!!.message, Toast.LENGTH_LONG).show()

                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                    }
                    // Update UI
                }
            }
    }

    private fun registerUser(user: FirebaseUser) {
        Log.d("FIREBASE", "registerUser() $user")

        usersRef.document(user.uid).get().addOnSuccessListener {
            val returnedUser = it.toObject<User>()
            Log.d("FIREBASE", returnedUser.toString())
            if(returnedUser == null) {
                val newUser = User()
                newUser.mobileNumber = phoneNumber!!
                newUser.uid = user.uid
                usersRef.document(user.uid).set(newUser).addOnSuccessListener {
                    val intent = Intent(this@PhoneAuthenticationActivity, RegisterUserActivity::class.java)
                    intent.putExtra(User.FIELD_UID, newUser.uid)
                    startActivity(intent)
                    finish()
                }
            } else {
                val intent = Intent(this@PhoneAuthenticationActivity, RegisterUserActivity::class.java)
                intent.putExtra(User.FIELD_UID, user.uid)
                startActivity(intent)
                finish()
            }
        }
    }

}