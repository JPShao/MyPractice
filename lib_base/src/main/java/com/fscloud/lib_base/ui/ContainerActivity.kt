package com.fscloud.lib_base.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.FragmentUtils
import com.fscloud.lib_base.R
import com.fscloud.lib_base.utils.AndroidBarUtils
import kotlinx.android.synthetic.main.activity_container.*

class ContainerActivity : BaseActivity(), View.OnClickListener {
    private var baseFragment: Fragment? = null
    override fun layoutId(): Int = R.layout.activity_container

    override fun initData() {

    }

    override fun initView() {
        iv_back.setOnClickListener(this)
        baseFragment = supportFragmentManager.fragmentFactory.instantiate(
            classLoader, intent.getStringExtra(
                "fragment"
            )!!
        )
        baseFragment?.arguments = intent.extras
        FragmentUtils.add(supportFragmentManager, baseFragment!!, R.id.container)

        if (!TextUtils.isEmpty(intent.getStringExtra("title"))) {
            tv_title.text = intent.getStringExtra("title")
            AndroidBarUtils.setStatusBarColor(this, R.color.white)
        }else{
            AndroidBarUtils.transparencyBar(this)
            toolbar.visibility = View.GONE
        }
    }

    override fun start() {

    }

    companion object {
        fun start(context: Context, bundle: Bundle, fragmentName: String) {
            context.startActivity(
                Intent(
                    context,
                    ContainerActivity::class.java
                ).putExtras(bundle)
                    .putExtra("fragment", fragmentName)
            )
        }

        fun start(
            context: Context,
            title: String,
            bundle: Bundle,
            fragmentName: String
        ) {
            context.startActivity(
                Intent(
                    context,
                    ContainerActivity::class.java
                ).putExtras(bundle)
                    .putExtra("title", title)
                    .putExtra("fragment", fragmentName)
            )
        }
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.iv_back -> {
                finish()
            }
        }
    }
}