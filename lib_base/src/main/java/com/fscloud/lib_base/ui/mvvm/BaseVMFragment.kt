package com.fscloud.lib_base.ui.mvvm

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.annotation.NonNull
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.fscloud.lib_base.ui.BaseUMFragment
import com.fscloud.lib_base.view.BaseDialogFragment
import com.fscloud.lib_base.view.LoadingDialogFragment
import com.fscloud.lib_base.view.MultipleStatusView
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

/**
 * desc:
 */

abstract class BaseVMFragment<V : ViewDataBinding, VM : ViewModel> : BaseUMFragment(),
    EasyPermissions.PermissionCallbacks {
    protected var binding: V? = null
    protected var viewModel: VM? = null
    private var viewModelId: Int = 0

    /**
     * 视图是否加载完毕
     */
    private var isViewPrepare = false

    /**
     * 数据是否加载过了
     */
    private var hasLoadData = false

    /**
     * 多种状态的 View 的切换
     */
    protected var mLayoutStatusView: MultipleStatusView? = null

    private val mLoadingDialog: BaseDialogFragment by lazy {
        LoadingDialogFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate<V>(inflater, getLayoutId(), container, false)
        return binding?.root
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            lazyLoadDataIfPrepared()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModelBinding()
        isViewPrepare = true
        initView()
        lazyLoadDataIfPrepared()
        //多种状态切换的view 重试点击事件
        mLayoutStatusView?.setOnClickListener(mRetryClickListener)
    }

    private fun initViewModelBinding() {
        viewModelId = initVariableId()
        if (viewModel == null) {
            viewModel = createViewModel()
        }
        //关联ViewModel
        binding?.setVariable(viewModelId, viewModel)
        binding?.lifecycleOwner = this
    }

    protected abstract fun initVariableId(): Int

    protected abstract fun createViewModel(): VM

    private fun lazyLoadDataIfPrepared() {
        if (userVisibleHint && isViewPrepare && !hasLoadData) {
            lazyLoad()
            hasLoadData = true
        }
    }

    open val mRetryClickListener: View.OnClickListener = View.OnClickListener {
        lazyLoad()
    }


    /**
     * 加载布局
     */
    @LayoutRes
    abstract fun getLayoutId(): Int

    /**
     * 初始化 ViewI
     */
    abstract fun initView()

    /**
     * 懒加载
     */
    abstract fun lazyLoad()

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
        getLoadingDialog(tips).show(childFragmentManager, "dialog_loading")
    }

    open fun hideDialogLoading() {
        mLoadingDialog.dismiss()
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
        Log.i("EasyPermissions", "获取成功的权限$perms")
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
            Toast.makeText(activity, "已拒绝权限" + sb + "并不再询问", Toast.LENGTH_SHORT).show()
            AppSettingsDialog.Builder(this)
                .setRationale("此功能需要" + sb + "权限，否则无法正常使用，是否打开设置")
                .setPositiveButton("好")
                .setNegativeButton("不行")
                .build()
                .show()
        }
    }
}
