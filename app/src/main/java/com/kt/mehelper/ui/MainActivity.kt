package com.kt.mehelper.ui

import android.os.Bundle
import com.kt.mehelper.R
import com.kt.mehelper.base.BaseActivity
import permissions.dispatcher.RuntimePermissions

@RuntimePermissions
class MainActivity : BaseActivity() {

    override val layoutId: Int = R.layout.activity_main

    override fun initView(savedInstanceState: Bundle?) {
//        convertActivityToTranslucent(this)
    }



}
