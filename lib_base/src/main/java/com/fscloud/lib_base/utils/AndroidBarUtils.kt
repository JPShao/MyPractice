package com.fscloud.lib_base.utils

import android.R
import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import android.widget.FrameLayout
import androidx.annotation.ColorRes
import androidx.drawerlayout.widget.DrawerLayout

/**
 * @description StatusBar 和 NavigationBar 的工具类
 */
object AndroidBarUtils {
    private const val STATUS_BAR_HEIGHT_RES_NAME = "status_bar_height"
    private const val NAV_BAR_HEIGHT_RES_NAME = "navigation_bar_height"
    private const val NAV_BAR_WIDTH_RES_NAME = "navigation_bar_width"

    /**
     * 是否有NavigationBar
     *
     * @param activity 上下文
     * @return 是否有NavigationBar
     */
    fun hasNavBar(activity: Activity): Boolean {
        val windowManager = activity.windowManager
        val d = windowManager.defaultDisplay
        val realDisplayMetrics = DisplayMetrics()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            d.getRealMetrics(realDisplayMetrics)
        }
        val realHeight = realDisplayMetrics.heightPixels
        val realWidth = realDisplayMetrics.widthPixels
        val displayMetrics = DisplayMetrics()
        d.getMetrics(displayMetrics)
        val displayHeight = displayMetrics.heightPixels
        val displayWidth = displayMetrics.widthPixels
        return realWidth - displayWidth > 0 || realHeight - displayHeight > 0
    }

    /**
     * 创建Navigation Bar
     *
     * @param activity 上下文
     */
    fun createNavBar(activity: Activity) {
        val navBarHeight = getNavigationBarHeight(activity)
        val navBarWidth = getNavigationBarWidth(activity)
        if (navBarHeight > 0 && navBarWidth > 0) {
            //创建NavigationBar
            val navBar = View(activity)
            val pl: FrameLayout.LayoutParams
            if (activity.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                pl = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, navBarHeight)
                pl.gravity = Gravity.BOTTOM
            } else {
                pl = FrameLayout.LayoutParams(navBarWidth, ViewGroup.LayoutParams.MATCH_PARENT)
                pl.gravity = Gravity.END
            }
            navBar.layoutParams = pl
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                navBar.setBackgroundColor(Color.parseColor("#fffafafa"))
            } else {
                navBar.setBackgroundColor(Color.parseColor("#40000000"))
            }
            //添加到布局当中
            val decorView = activity.window.decorView as ViewGroup
            decorView.addView(navBar)
            //设置主布局PaddingBottom
            val contentView = decorView.findViewById<ViewGroup>(R.id.content)
            if (activity.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                contentView.setPaddingRelative(0, 0, 0, navBarHeight)
            } else {
                contentView.setPaddingRelative(0, 0, navBarWidth, 0)
            }
        }
    }

    /**
     * 设置 DrawerLayout 在4.4版本下透明，不然会出现白边
     *
     * @param drawerLayout DrawerLayout
     */
    fun setTranslucentDrawerLayout(drawerLayout: DrawerLayout?) {
        if (drawerLayout != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawerLayout.fitsSystemWindows = true
            drawerLayout.clipToPadding = false
        }
    }

    /**
     * 修改状态栏为全透明
     *
     * @param activity
     */
    fun transparencyBar(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = activity.window
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val window = activity.window
            window.setFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
            )
        }
    }

    /**
     * 修改状态栏颜色，支持4.4以上版本
     *
     * @param activity
     * @param colorId
     */
    fun setStatusBarColor(activity: Activity, @ColorRes colorId: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = activity.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = activity.resources.getColor(colorId)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //使用SystemBarTint库使4.4版本状态栏变色，需要先将状态栏设置为透明
            transparencyBar(activity)
        }
        // 着色状态栏 需要为系统状态栏预留控件 防止页面上顶
       val rootView = activity.window.decorView.findViewById<ViewGroup>(R.id.content)
        val view = rootView.getChildAt(0)
        view.fitsSystemWindows = true
    }

    /**
     * 修改状态栏颜色，支持4.4以上版本
     *  禁用
     * @param activity
     * @param colorId
     */
    fun setStatusBarColorToFragment(activity: Activity, @ColorRes colorId: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = activity.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = activity.resources.getColor(colorId)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //使用SystemBarTint库使4.4版本状态栏变色，需要先将状态栏设置为透明
            transparencyBar(activity)
        }
        // 着色状态栏 需要为系统状态栏预留控件 防止页面上顶
       // val rootView = activity.window.decorView.findViewById<ViewGroup>(R.id.content)
      //  val view = rootView.getChildAt(0)
      //  view.fitsSystemWindows = true
    }


    /**
     * 设置华为手机 StatusBar
     *
     * @param darkMode 是否是黑色模式
     * @param window   window
     */
    fun setHuaWeiStatusBar(darkMode: Boolean, window: Window) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            try {
                val decorViewClazz = Class.forName("com.android.internal.policy.DecorView")
                val field = decorViewClazz.getDeclaredField("mSemiTransparentStatusBarColor")
                field.isAccessible = darkMode
                field.setInt(window.decorView, Color.TRANSPARENT) //改为透明
            } catch (e: ClassNotFoundException) {
                Log.e("setHuaWeiStatusBar", "HuaWei status bar 模式设置失败")
            } catch (e: IllegalAccessException) {
                Log.e("setHuaWeiStatusBar", "HuaWei status bar 模式设置失败")
            } catch (e: NoSuchFieldException) {
                Log.e("setHuaWeiStatusBar", "HuaWei status bar 模式设置失败")
            }
        }
    }

    /**
     * 获取状态栏高度
     *
     * @param context context
     * @return 状态栏高度
     */
    fun getStatusBarHeight(context: Context): Int {
        // 获得状态栏高度
        return getBarHeight(context, STATUS_BAR_HEIGHT_RES_NAME)
    }

    /**
     * 获取导航栏高度
     *
     * @param activity activity
     * @return 导航栏高度
     */
    fun getNavigationBarHeight(activity: Activity): Int {
        return if (hasNavBar(activity)) {
            // 获得导航栏高度
            getBarHeight(activity, NAV_BAR_HEIGHT_RES_NAME)
        } else {
            0
        }
    }

    /**
     * 获取横屏状态下导航栏的宽度
     *
     * @param activity activity
     * @return 导航栏的宽度
     */
    fun getNavigationBarWidth(activity: Activity): Int {
        return if (hasNavBar(activity)) {
            // 获得导航栏高度
            getBarHeight(activity, NAV_BAR_WIDTH_RES_NAME)
        } else {
            0
        }
    }

    /**
     * 获取Bar高度
     *
     * @param context context
     * @param barName 名称
     * @return Bar高度
     */
    fun getBarHeight(context: Context, barName: String?): Int {
        // 获得状态栏高度
        val resourceId = context.resources.getIdentifier(barName, "dimen", "android")
        return context.resources.getDimensionPixelSize(resourceId)
    }

    /**
     * 状态栏亮色模式，设置状态栏黑色文字、图标，
     * 适配4.4以上版本MIUIV、Flyme和6.0以上版本其他Android
     *
     * @param activity
     * @return 1:MIUUI 2:Flyme 3:android6.0
     */
    fun setStatusBarLightMode(activity: Activity): Int {
        var result = 0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            when {
                setMIUISetStatusBarLightMode(activity, true) -> {
                    result = 1
                }
                setFlymeSetStatusBarLightMode(activity.window, true) -> {
                    result = 2
                }
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
                    activity.window.decorView.systemUiVisibility =
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                    result = 3
                }
            }
        }
        return result
    }

    /**
     * 已知系统类型时，设置状态栏黑色文字、图标。
     * 适配4.4以上版本MIUIV、Flyme和6.0以上版本其他Android
     *
     * @param activity
     * @param type     1:MIUUI 2:Flyme 3:android6.0
     */
    fun setStatusBarLightMode(activity: Activity, type: Int) {
        when (type) {
            1 -> {
                setMIUISetStatusBarLightMode(activity, true)
            }
            2 -> {
                setFlymeSetStatusBarLightMode(activity.window, true)
            }
            3 -> {
                activity.window.decorView.systemUiVisibility = (
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
            }
        }
    }

    /**
     * 设置状态栏白色字体图标，
     * 适配4.4以上版本MIUIV、Flyme和6.0以上版本其他Android
     *
     * @param activity
     * @return 1:MIUUI 2:Flyme 3:android6.0
     */
    fun setStatusBarDarkMode(activity: Activity): Int {
        var result = 0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            when {
                setMIUISetStatusBarLightMode(activity, false) -> {
                    result = 1
                }
                setFlymeSetStatusBarLightMode(activity.window, false) -> {
                    result = 2
                }
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
                    activity.window.decorView.systemUiVisibility =
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    result = 3
                }
            }
        }
        return result
    }

    /**
     * 状态栏暗色模式，清除MIUI、flyme或6.0以上版本状态栏黑色文字、图标
     */
    fun setStatusBarDarkMode(activity: Activity, type: Int) {
        when (type) {
            1 -> {
                setMIUISetStatusBarLightMode(activity, false)
            }
            2 -> {
                setFlymeSetStatusBarLightMode(activity.window, false)
            }
            3 -> {
                activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
            }
        }
    }

    /**
     * 设置状态栏图标为深色和魅族特定的文字风格
     * 可以用来判断是否为Flyme用户
     *
     * @param window 需要设置的窗口
     * @param dark   是否把状态栏文字及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    private fun setFlymeSetStatusBarLightMode(window: Window?, dark: Boolean): Boolean {
        var result = false
        if (window != null) {
            try {
                val lp = window.attributes
                val darkFlag = WindowManager.LayoutParams::class.java
                    .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON")
                val meizuFlags = WindowManager.LayoutParams::class.java
                    .getDeclaredField("meizuFlags")
                darkFlag.isAccessible = true
                meizuFlags.isAccessible = true
                val bit = darkFlag.getInt(null)
                var value = meizuFlags.getInt(lp)
                value = if (dark) {
                    value or bit
                } else {
                    value and bit.inv()
                }
                meizuFlags.setInt(lp, value)
                window.attributes = lp
                result = true
            } catch (e: Exception) {
            }
        }
        return result
    }

    /**
     * 需要MIUIV6以上
     *
     * @param activity
     * @param dark     是否把状态栏文字及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    private fun setMIUISetStatusBarLightMode(activity: Activity, dark: Boolean): Boolean {
        var result = false
        val window = activity.window
        if (window != null) {
            val clazz: Class<*> = window.javaClass
            try {
                var darkModeFlag = 0
                val layoutParams = Class.forName("android.view.MiuiWindowManager\$LayoutParams")
                val field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE")
                darkModeFlag = field.getInt(layoutParams)
                val extraFlagField = clazz.getMethod(
                    "setExtraFlags",
                    Int::class.javaPrimitiveType,
                    Int::class.javaPrimitiveType
                )
                if (dark) {
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag) //状态栏透明且黑色字体
                } else {
                    extraFlagField.invoke(window, 0, darkModeFlag) //清除黑色字体
                }
                result = true
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    //开发版 7.7.13 及以后版本采用了系统API，旧方法无效但不会报错，所以两个方式都要加上
                    if (dark) {
                        activity.window.decorView.systemUiVisibility =
                            (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                    or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
                    } else {
                        activity.window.decorView.systemUiVisibility =
                            (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
                    }
                }
            } catch (e: Exception) {
            }
        }
        return result
    }
}