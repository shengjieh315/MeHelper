package com.kt.mehelper.base

import android.app.Activity
import android.app.ActivityOptions
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.SkinAppCompatDelegateImpl

abstract class BaseActivity : AppCompatActivity() {

    abstract val layoutId: Int
    lateinit var mContext: BaseActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId)
        mContext = this
        try {
            initView(savedInstanceState)
        } catch (e: Throwable) {
        }
    }

    abstract fun initView(savedInstanceState: Bundle?)

    override fun getDelegate(): AppCompatDelegate {
        return SkinAppCompatDelegateImpl.get(this, this)
    }

    /**
     * 得到根view
     *
     * @return View
     */
    fun getRootView(): View {
        return (mContext.findViewById(android.R.id.content) as ViewGroup)
            .getChildAt(0)
    }

    /**
     * Convert a translucent themed Activity
     * [android.R.attr.windowIsTranslucent] back from opaque to
     * translucent following a call to
     * [.convertActivityFromTranslucent] .
     *
     *
     * Calling this allows the Activity behind this one to be seen again. Once
     * all such Activities have been redrawn
     *
     *
     * This call has no effect on non-translucent activities or on activities
     * with the [android.R.attr.windowIsFloating] attribute.
     */
    fun convertActivityToTranslucent(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            convertActivityToTranslucentAfterL(activity)
        } else {
            convertActivityToTranslucentBeforeL(activity)
        }
    }

    /**
     * Calling the convertToTranslucent method on platforms before Android 5.0
     */
    private fun convertActivityToTranslucentBeforeL(activity: Activity) {
        try {
            val classes = Activity::class.java.declaredClasses
            var translucentConversionListenerClazz: Class<*>? = null
            for (clazz in classes) {
                if (clazz.simpleName.contains("TranslucentConversionListener")) {
                    translucentConversionListenerClazz = clazz
                }
            }
            val method = Activity::class.java.getDeclaredMethod(
                "convertToTranslucent",
                translucentConversionListenerClazz!!
            )
            method.isAccessible = true
            method.invoke(activity, *arrayOf<Any>())
        } catch (t: Throwable) {
        }


    }

    /**
     * Calling the convertToTranslucent method on platforms after Android 5.0
     */
    private fun convertActivityToTranslucentAfterL(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            try {
                val getActivityOptions =
                    Activity::class.java.getDeclaredMethod("getActivityOptions")
                getActivityOptions.isAccessible = true
                val options = getActivityOptions.invoke(activity)

                val classes = Activity::class.java.declaredClasses
                var translucentConversionListenerClazz: Class<*>? = null
                for (clazz in classes) {
                    if (clazz.simpleName.contains("TranslucentConversionListener")) {
                        translucentConversionListenerClazz = clazz
                    }
                }
                val convertToTranslucent = Activity::class.java.getDeclaredMethod(
                    "convertToTranslucent",
                    translucentConversionListenerClazz, ActivityOptions::class.java
                )
                convertToTranslucent.isAccessible = true
                convertToTranslucent.invoke(activity, null, options)
            } catch (t: Throwable) {
            }

        }

    }

}
