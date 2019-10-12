package com.kt.mehelper.base

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.SkinAppCompatDelegateImpl

abstract class BaseActivity : AppCompatActivity(){

    abstract val layoutId: Int
    lateinit var mContext : BaseActivity

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

        setContentView(layoutId)
        mContext = this
        try {
            initView(savedInstanceState)
        }catch (e : Throwable){}

    }

    abstract fun initView(savedInstanceState: Bundle?)

    override fun getDelegate(): AppCompatDelegate {
        return SkinAppCompatDelegateImpl.get(this, this)
    }

}
