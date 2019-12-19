package com.kt.mehelper.utils;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Resources;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.FitWindowsLinearLayout;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;

import com.wbxm.icartoon.utils.screen.ScreenAdaptUtil;

import java.lang.reflect.Method;

/**
 * Created by jianyang on 2016/12/5.
 */

public class NavigationBarUtils {



    public static boolean hasNavBar(Context context) {
        try{
            if (Build.VERSION.SDK_INT >= 14) {
                Resources res = context.getResources();
                int resourceId = res.getIdentifier("config_showNavigationBar", "bool", "android");
                if (resourceId != 0) {
                    boolean hasNav = res.getBoolean(resourceId);
                    // check override flag
                    String sNavBarOverride = getNavBarOverride();
                    if ("1".equals(sNavBarOverride)) {
                        hasNav = false;
                    } else if ("0".equals(sNavBarOverride)) {
                        hasNav = true;
                    }
                    return hasNav;
                } else { // fallback
                    return !ViewConfiguration.get(context).hasPermanentMenuKey();
                }

            }
        }catch (Throwable e){
            e.printStackTrace();
        }

        return false;

    }



    /**
     * 判断虚拟按键栏是否重写
     * @return
     */
    private static String getNavBarOverride() {
        String sNavBarOverride = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                Class c = Class.forName("android.os.SystemProperties");
                Method m = c.getDeclaredMethod("get", String.class);
                m.setAccessible(true);
                sNavBarOverride = (String) m.invoke(null, "qemu.hw.mainkeys");
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        return sNavBarOverride;
    }



    private static int navigationBarHeight;
    /**
     * 获取虚拟按键栏高度
     * @param context
     * @return
     */
    public static int getNavigationBarHeight(Context context) {
        if(navigationBarHeight>0){
           return navigationBarHeight;
        }
        int result = 0;
        float scalePercent  = 0;
        try{
            if (hasNavBar(context)){
                Resources res = context.getResources();
                int resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android");
                if (resourceId > 0) {
                    result = res.getDimensionPixelSize(resourceId);
                }
                scalePercent = (ScreenAdaptUtil.getsNonCompatDensity() / res.getDisplayMetrics().density);
            }
        }catch (Throwable e){
            e.printStackTrace();
        }
        navigationBarHeight = (int) (result*scalePercent);
        return navigationBarHeight;
    }


    /**
     * @return false 关闭了NavgationBar ,true 开启了
     */
    public static boolean navigationBarIsOpen(Context context) {
        try {
            if (!hasNavBar(context)) return false;
            ViewGroup rootLinearLayout = findRootLinearLayout(context);
            int navigationBarHeight = 0;

            if (rootLinearLayout != null) {
                ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) rootLinearLayout.getLayoutParams();
                navigationBarHeight = layoutParams.bottomMargin;
            }
            return navigationBarHeight != 0;
        }catch (Throwable e){
            return false;
        }
    }

    /**
     * 从R.id.content从上遍历，拿到 DecorView 下的唯一子布局LinearLayout
     * 获取对应的bottomMargin 即可得到对应导航栏的高度，0为关闭了或没有导航栏
     */
    private static ViewGroup findRootLinearLayout(Context context) {
        ViewGroup onlyLinearLayout = null;
        try {
            Window window = getWindow(context);
            if (window != null) {
                ViewGroup decorView = (ViewGroup) getWindow(context).getDecorView();
                Activity activity = getActivity(context);
                View contentView = activity.findViewById(android.R.id.content);
                if (contentView != null) {
                    View tempView = contentView;
                    while (tempView.getParent() != decorView) {
                        ViewGroup parent = (ViewGroup) tempView.getParent();
                        if (parent instanceof LinearLayout) {
                            //如果style设置了不带toolbar,mContentView上层布局会由原来的 ActionBarOverlayLayout->FitWindowsLinearLayout)
                            if (parent instanceof FitWindowsLinearLayout) {
                                tempView = parent;
                                continue;
                            } else {
                                onlyLinearLayout = parent;
                                break;
                            }
                        } else {
                            tempView = parent;
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return onlyLinearLayout;
    }

    private static Window getWindow(Context context) {
        if (getAppCompActivity(context) != null) {
            return getAppCompActivity(context).getWindow();
        } else {
            return scanForActivity(context).getWindow();
        }
    }

    private static Activity getActivity(Context context) {
        if (getAppCompActivity(context) != null) {
            return getAppCompActivity(context);
        } else {
            return scanForActivity(context);
        }
    }


    private static AppCompatActivity getAppCompActivity(Context context) {
        if (context == null) return null;
        if (context instanceof AppCompatActivity) {
            return (AppCompatActivity) context;
        } else if (context instanceof ContextThemeWrapper) {
            return getAppCompActivity(((ContextThemeWrapper) context).getBaseContext());
        }
        return null;
    }

    private static Activity scanForActivity(Context context) {
        if (context == null) return null;

        if (context instanceof Activity) {
            return (Activity) context;
        } else if (context instanceof ContextWrapper) {
            return scanForActivity(((ContextWrapper) context).getBaseContext());
        }
        return null;
    }

}
