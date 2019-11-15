package com.kt.app_alpha

import android.content.Intent
import android.os.Bundle
import com.kt.mehelper.base.BaseActivity
import com.kt.mehelper.ui.MainActivity

class ALPMainActivity : BaseActivity() {
    override val layoutId: Int = R.layout.alp_activity_main

    override fun initView(savedInstanceState: Bundle?) {

        getRootView().setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}
