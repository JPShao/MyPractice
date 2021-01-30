package com.fscloud.lib_base.ui

import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.transition.Fade
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.ToastUtils
import com.fscloud.lib_base.utils.AndroidBarUtils
import com.fscloud.lib_base.utils.gps.LocationUtils
import com.fscloud.lib_base.view.BaseDialogFragment
import com.fscloud.lib_base.view.LoadingDialogFragment
import com.fscloud.lib_base.view.MultipleStatusView
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import org.greenrobot.eventbus.EventBus
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions


/**
 * desc:BaseActivity基类
 */
abstract class BaseActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {


    //发起网络请求时显示的弹窗
    private var popupView: BasePopupView? = null

    /**
     * 多种状态的 View 的切换
     */
    private val mLoadingDialog: BaseDialogFragment by lazy {
        LoadingDialogFragment()
    }

    private val mLayoutStatusView: MultipleStatusView by lazy {
        MultipleStatusView(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        window?.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
        setContentView(layoutId())
        showLoading()
        initStatusBar()
        initData()
        initView()
        start()
        initListener()
    }

    private fun initStatusBar() {
        AndroidBarUtils.transparencyBar(this)
        AndroidBarUtils.setStatusBarLightMode(this)
    }

    private fun initListener() {
        mLayoutStatusView?.setOnClickListener(mRetryClickListener)
    }

    open val mRetryClickListener: View.OnClickListener = View.OnClickListener {
        start()
    }

    /**
     *  加载布局
     */
    abstract fun layoutId(): Int

    /**
     * 初始化数据
     */
    abstract fun initData()

    /**
     * 初始化 View
     */
    abstract fun initView()

    /**
     * 开始请求
     */
    abstract fun start()

    protected open fun getLoadingDialog(tips: String?): BaseDialogFragment {
        val bundle = Bundle()
        bundle.putString("tips", tips)
        mLoadingDialog.arguments = bundle
        return mLoadingDialog
    }

    open fun showDialogLoading() {
        showDialogLoading("")
    }

    open fun showDialogLoading(tips: String?) {
        getLoadingDialog(tips).show(supportFragmentManager, "dialog_loading")
    }

    open fun hideDialogLoading() {
        mLoadingDialog.dismiss()
    }
    fun showToast(str:String){

        ToastUtils.showShort(str)
    }
    /**
     * 打开软键盘
     */
    fun openKeyBord(mEditText: EditText, mContext: Context) {
        val imm = mContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(mEditText, InputMethodManager.RESULT_SHOWN)
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
    }

    /**
     * 关闭软键盘
     */
    fun closeKeyBord(mEditText: EditText, mContext: Context) {
        val imm = mContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(mEditText.windowToken, 0)
    }

    override fun getResources(): Resources {
        val res = super.getResources()
        val config = Configuration()
        config.setToDefaults()
        res.updateConfiguration(config, res.displayMetrics)
        return res
    }
    /**
     * 重写要申请权限的Activity或者Fragment的onRequestPermissionsResult()方法，
     * 在里面调用EasyPermissions.onRequestPermissionsResult()，实现回调。
     *
     * @param requestCode  权限请求的识别码
     * @param permissions  申请的权限
     * @param grantResults 授权结果
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        @NonNull permissions: Array<String>,
        @NonNull grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    /**
     * 当权限被成功申请的时候执行回调
     *
     * @param requestCode 权限请求的识别码
     * @param perms       申请的权限的名字
     */
    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        Log.i("EasyPermissions", "requestCode = $requestCode,获取成功的权限$perms")
        if(requestCode == LocationUtils.PERMISSIONS_ACCESS_LOCATION){
            EventBus.getDefault().post(LocationUtils.PERMISSIONS_ACCESS_LOCATION_EVENT)
        }

    }

    /**
     * 当权限申请失败的时候执行的回调
     *
     * @param requestCode 权限请求的识别码
     * @param perms       申请的权限的名字
     */
    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        //处理权限名字字符串
        val sb = StringBuffer()
        for (str in perms) {
            sb.append(str)
            sb.append("\n")
        }
        sb.replace(sb.length - 2, sb.length, "")
        //用户点击拒绝并不在询问时候调用
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            Toast.makeText(this, "已拒绝权限" + sb + "并不再询问", Toast.LENGTH_SHORT).show()
            AppSettingsDialog.Builder(this)
                .setRationale("此功能需要" + sb + "权限，否则无法正常使用，是否打开设置")
                .setPositiveButton("好")
                .setNegativeButton("不行")
                .build()
                .show()
        }
    }

    /**
     * 点击软件盘外部，软键盘收起
     */
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev?.getAction() == MotionEvent.ACTION_DOWN) {
            val v: View? = getCurrentFocus()
            if (isShouldHideKeyboard(v, ev)) {
                KeyboardUtils.hideSoftInput(this);
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    private fun isShouldHideKeyboard(v: View?, event: MotionEvent): Boolean {
        if (v != null && (v is EditText)) {
            var l: IntArray = intArrayOf(0, 0)
            v.getLocationInWindow(l)
            var left = l[0]
            var top = l[1]
            var bottom = top + v.getHeight()
            var right = left + v.getWidth()
            return !(event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom)
        }
        return false
    }

    /**
     * 弹出loading动画
     */
    private fun showLoading() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            var fade: Fade = Fade()
            fade.duration = 2000
            window?.exitTransition = fade!!
        }
    }

    fun showLoadingPopup(){
        popupView = XPopup.Builder(this)
            .hasShadowBg(false)
            .dismissOnTouchOutside(false)
            .asLoading()
            .show()
    }

    fun dismissLoadingPopup(){
        popupView?.dismiss()
    }


}


