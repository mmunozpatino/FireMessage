package com.mercedes.firemessage

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import kotlinx.android.synthetic.main.activity_sing_in.*
import org.jetbrains.anko.*
import org.jetbrains.anko.design.longSnackbar

class SingInActivity : AppCompatActivity() {

    private val RC_SING_IN = 1
    private val singInProviders = listOf(AuthUI.IdpConfig.EmailBuilder().setAllowNewAccounts(true)
            .setRequireName(true).build())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sing_in)

        account_sign_in.setOnClickListener{
            val intent = AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(singInProviders)
                    .setLogo(R.drawable.ic_ic_fire_emoji)
                    .build()
            startActivityForResult(intent,1)
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 1){
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK){
                val processDialog = indeterminateProgressDialog ("Setting up your account" )
                //TODO: inicializar usuario en firebase
                //lo de task es para no volver al login una vez que ya está hecho
                startActivity(intentFor<MainActivity>().newTask().clearTask())
                processDialog.dismiss()
            }
            else if(resultCode == Activity.RESULT_CANCELED){
                if(response == null) return
                when(response.error?.errorCode){
                    ErrorCodes.NO_NETWORK -> {
                        longSnackbar(constraint_layout, "No network")
                    }
                    ErrorCodes.UNKNOWN_ERROR -> {
                        longSnackbar(constraint_layout, "Unknow Error")
                    }
                }
            }
        }
    }
}
