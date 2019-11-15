package com.kt.mehelper.ui

import android.os.Bundle
import com.kt.mehelper.R
import com.kt.mehelper.base.BaseActivityUp

class MainActivity : BaseActivityUp() {

    override fun initView(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_main)
        convertActivityToTranslucent(this)
    }

    override fun initListener(savedInstanceState: Bundle?) {

    }

    override fun initData(savedInstanceState: Bundle?) {
    }

}
