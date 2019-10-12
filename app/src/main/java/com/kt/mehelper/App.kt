package com.kt.mehelper

import android.app.Application
import skin.support.SkinCompatManager
import skin.support.app.SkinAppCompatViewInflater
import skin.support.app.SkinCardViewInflater
import skin.support.constraint.app.SkinConstraintViewInflater
import skin.support.design.app.SkinMaterialViewInflater


class App : Application() {

    override fun onCreate() {
        super.onCreate()

        SkinCompatManager.withoutActivity(this)
            .addInflater(SkinAppCompatViewInflater())           // 基础控件换肤初始化
            .addInflater(SkinMaterialViewInflater())            // material design 控件换肤初始化[可选]
            .addInflater(SkinConstraintViewInflater())          // ConstraintLayout 控件换肤初始化[可选]
            .addInflater(SkinCardViewInflater())                // CardView v7 控件换肤初始化[可选]
            .setSkinStatusBarColorEnable(false)                     // 关闭状态栏换肤，默认打开[可选]
            .setSkinWindowBackgroundEnable(false)                   // 关闭windowBackground换肤，默认打开[可选]
            .loadSkin()

    }

}