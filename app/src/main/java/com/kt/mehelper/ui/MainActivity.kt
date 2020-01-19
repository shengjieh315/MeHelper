package com.kt.mehelper.ui

import android.os.Bundle
import com.kt.mehelper.R
import com.kt.mehelper.base.BaseActivity
import com.kt.mehelper.utils.merefresh.MeRefreshLayout
import kotlinx.android.synthetic.main.activity_main.*
import permissions.dispatcher.RuntimePermissions

@RuntimePermissions
class MainActivity : BaseActivity(), MeRefreshLayout.OnRefreshListener {


    override val layoutId: Int = R.layout.activity_main

    override fun initView(savedInstanceState: Bundle?) {
        refresh.setOnRefreshListener(this)
    }

    override fun onRefresh() {
        refresh.postDelayed({
            refresh.refreshComplete()
        },500)
    }

}
