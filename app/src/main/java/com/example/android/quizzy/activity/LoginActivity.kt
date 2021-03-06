package com.example.android.quizzy.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import com.example.android.quizzy.R
import com.example.android.quizzy.fragment.LoginFragment

class LoginActivity : AppCompatActivity(), LoginFragment.LoginTransitionInterface {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        openFragment(LoginFragment())
    }

    override fun openFragment(fragment: Fragment) {
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.login_frame_layout, fragment)
                .commit()
    }

    override fun onBackPressed() {
        //Nothing
    }

}
