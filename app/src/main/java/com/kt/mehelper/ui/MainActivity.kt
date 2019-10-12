package com.kt.mehelper.ui

import android.os.Bundle
import com.kt.mehelper.R
import com.kt.mehelper.base.BaseActivity
import skin.support.SkinCompatManager

class MainActivity : BaseActivity() {

    override val layoutId: Int = R.layout.activity_main

    override fun initView(savedInstanceState: Bundle?) {

        // 恢复应用默认皮肤
        SkinCompatManager.getInstance().restoreDefaultTheme()

    }
}
