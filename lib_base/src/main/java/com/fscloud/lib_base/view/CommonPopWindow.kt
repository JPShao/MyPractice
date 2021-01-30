package com.fscloud.lib_base.view

import android.app.Activity
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.*
import android.widget.PopupWindow
import androidx.annotation.FloatRange
import androidx.annotation.LayoutRes
import androidx.annotation.StyleRes

class CommonPopWindow private constructor() {
    interface ViewClickListener {
        fun getChildView(mPopupWindow: PopupWindow?, view: View?, mLayoutResId: Int)
    }

    /**
     * 获取PopupWindow宽度
     *
     * @return
     */
    val width: Int
        get() = if (mPopupWindow != null) {
            mContentView!!.measuredWidth
        } else 0

    /**
     * 获取PopupWindow高度
     *
     * @return
     */
    val height: Int
        get() = if (mPopupWindow != null) {
            mContentView!!.measuredHeight
        } else 0

    /**
     * 显示在控件的下方
     */
    fun showDownPop(parent: View): CommonPopWindow {
        if (parent.visibility == View.GONE) {
            mPopupWindow!!.showAtLocation(parent, Gravity.NO_GRAVITY, 0, 0)
        } else {
            val location = IntArray(2)
            parent.getLocationOnScreen(location)
            if (mPopupWindow != null) {
                mPopupWindow!!.showAtLocation(
                    parent,
                    Gravity.NO_GRAVITY,
                    location[0],
                    location[1] + parent.height
                )
            }
        }
        return this
    }

    /**
     * 显示在控件的上方
     */
    fun showAsUp(view: View): CommonPopWindow {
        if (view.visibility == View.GONE) {
            mPopupWindow!!.showAtLocation(view, Gravity.NO_GRAVITY, 0, 0)
        } else {
            val location = IntArray(2)
            view.getLocationOnScreen(location)
            if (mPopupWindow != null) {
                mPopupWindow!!.showAtLocation(
                    view,
                    Gravity.NO_GRAVITY,
                    location[0],
                    location[1] - view.height
                )
                //方式二
//                mPopupWindow.showAsDropDown(view, 0, -(getHeight() + view.getMeasuredHeight()));
            }
        }
        return this
    }

    /**
     * 显示在控件的左边
     */
    fun showAsLeft(view: View): CommonPopWindow {
        if (view.visibility == View.GONE) {
            mPopupWindow!!.showAtLocation(view, Gravity.NO_GRAVITY, 0, 0)
        } else {
            val location = IntArray(2)
            view.getLocationOnScreen(location)
            if (mPopupWindow != null) {
                mPopupWindow!!.showAtLocation(
                    view,
                    Gravity.NO_GRAVITY,
                    location[0] - width,
                    location[1]
                )
            }
        }
        return this
    }

    /**
     * 显示在控件右边
     */
    fun showAsRight(view: View): CommonPopWindow {
        if (view.visibility == View.GONE) {
            mPopupWindow!!.showAtLocation(view, Gravity.NO_GRAVITY, 0, 0)
        } else {
            val location = IntArray(2)
            view.getLocationOnScreen(location)
            if (mPopupWindow != null) {
                mPopupWindow!!.showAtLocation(
                    view,
                    Gravity.NO_GRAVITY,
                    location[0] + view.width,
                    location[1]
                )
            }
        }
        return this
    }

    /**
     * 显示控件下方
     *
     * @param view
     * @return
     */
    fun showAsDown(view: View?): CommonPopWindow {
        if (mPopupWindow != null) {
            mPopupWindow!!.showAsDropDown(view)
        }
        return this
    }

    /**
     * 全屏弹出
     */
    fun showAsBottom(view: View): CommonPopWindow {
        if (view.visibility == View.GONE) {
            mPopupWindow!!.showAtLocation(view, Gravity.NO_GRAVITY, 0, 0)
        } else {
            val location = IntArray(2)
            view.getLocationOnScreen(location)
            if (mPopupWindow != null) {
                mPopupWindow!!.showAtLocation(view, Gravity.BOTTOM, 0, 0)
            }
        }
        return this
    }

    fun showAtLocation(anchor: View?, gravity: Int, x: Int, y: Int): CommonPopWindow {
        if (mPopupWindow != null) {
            mPopupWindow!!.showAtLocation(anchor, gravity, x, y)
        }
        return this
    }

    fun dismiss() {
        if (mPopupWindow != null) {
            mPopupWindow!!.dismiss()
        }
    }

    /*
     * ---------------------Builder-------------------------
     */
    class Builder : PopupWindow.OnDismissListener {
        private var mContext: Context? = null
        private var mLayoutResId //布局ID
                = 0
        private var mWidth = 0
        private var mHeight //弹窗宽高
                = 0
        private var mAnimationStyle //动画样式
                = 0
        private var mListener //子View监听回调
                : ViewClickListener? = null
        private var mDrawable //背景Drawable
                : Drawable? = null
        private var mTouchable = true //是否相应touch事件
        private var mFocusable = true //是否获取焦点
        private var mOutsideTouchable = true //设置外部是否点击
        private var mBackgroundDarkEnable = false //是否背景窗体变暗
        private var mDarkAlpha = 1.0f //透明值
        fun build(context: Context?): CommonPopWindow {
            mContext = context
            val popWindow = CommonPopWindow()
            apply()
            if (mListener != null && mLayoutResId != 0) {
                mListener!!.getChildView(mPopupWindow, mContentView, mLayoutResId)
            }
            return popWindow
        }

        private fun apply() {
            if (mLayoutResId != 0) {
                mContentView = LayoutInflater.from(mContext).inflate(mLayoutResId, null)
            }
            if (mWidth != 0 && mHeight != 0) {
                mPopupWindow = PopupWindow(mContentView, mWidth, mHeight)
            } else {
                mPopupWindow = PopupWindow(
                    mContentView,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
            mPopupWindow!!.isTouchable = mTouchable
            mPopupWindow!!.isFocusable = mFocusable
            mPopupWindow!!.isOutsideTouchable = mOutsideTouchable
            if (mDrawable != null) {
                mPopupWindow!!.setBackgroundDrawable(mDrawable)
            } else {
                mPopupWindow!!.setBackgroundDrawable(ColorDrawable())
            }
            if (mAnimationStyle != -1) {
                mPopupWindow!!.animationStyle = mAnimationStyle
            }
            if (mWidth == 0 || mHeight == 0) {
                measureWidthAndHeight(mContentView)
                //如果没有设置高度的情况下，设置宽高并赋值
                mWidth = mPopupWindow!!.contentView.measuredWidth
                mHeight = mPopupWindow!!.contentView.measuredHeight
            }
            val activity = mContext as Activity?
            if (activity != null && mBackgroundDarkEnable) {
                val alpha = if (mDarkAlpha >= 0f || mDarkAlpha <= 1f) mDarkAlpha else 0.7f
                mWindow = activity.window
                val params = mWindow?.attributes
                params?.alpha = alpha
                mWindow?.attributes = params
            }
            mPopupWindow!!.setOnDismissListener(this)
            mPopupWindow!!.update()
        }

        override fun onDismiss() {
            dismiss()
        }

        /**
         * 测量View的宽高
         *
         * @param mContentView
         */
        private fun measureWidthAndHeight(mContentView: View?) {
            val widthMeasureSpec =
                View.MeasureSpec.makeMeasureSpec((1 shl 30) - 1, View.MeasureSpec.AT_MOST)
            val heightMeasureSpec =
                View.MeasureSpec.makeMeasureSpec((1 shl 30) - 1, View.MeasureSpec.AT_MOST)
            mContentView!!.measure(widthMeasureSpec, heightMeasureSpec)
        }

        /**
         * 设置布局ID
         *
         * @param layoutResId
         * @return
         */
        fun setView(@LayoutRes layoutResId: Int): Builder {
            mContentView = null
            mLayoutResId = layoutResId
            return this
        }

        /**
         * 设置宽高
         *
         * @param width
         * @param height
         * @return
         */
        fun setSize(width: Int, height: Int): Builder {
            mWidth = width
            mHeight = height
            return this
        }

        /**
         * 设置背景
         *
         * @param drawable
         * @return
         */
        fun setBackgroundDrawable(drawable: Drawable?): Builder {
            mDrawable = drawable
            return this
        }

        /**
         * 设置背景是否变暗
         *
         * @param darkEnable
         * @return
         */
        fun setBackgroundDarkEnable(darkEnable: Boolean): Builder {
            mBackgroundDarkEnable = darkEnable
            return this
        }

        /**
         * 设置背景透明度
         *
         * @param dackAlpha
         * @return
         */
        fun setBackgroundAlpha(@FloatRange(from = 0.0, to = 1.0) dackAlpha: Float): Builder {
            mDarkAlpha = dackAlpha
            return this
        }

        /**
         * 是否点击Outside消失
         *
         * @param touchable
         * @return
         */
        fun setOutsideTouchable(touchable: Boolean): Builder {
            mOutsideTouchable = touchable
            return this
        }

        /**
         * 是否设置Touch事件
         *
         * @param touchable
         * @return
         */
        fun setTouchable(touchable: Boolean): Builder {
            mTouchable = touchable
            return this
        }

        /**
         * 设置动画
         *
         * @param animationStyle
         * @return
         */
        fun setAnimationStyle(@StyleRes animationStyle: Int): Builder {
            mAnimationStyle = animationStyle
            return this
        }

        /**
         * 是否设置获取焦点
         *
         * @param focusable
         * @return
         */
        fun setFocusable(focusable: Boolean): Builder {
            mFocusable = focusable
            return this
        }

        /**
         * 设置子View点击事件回调
         *
         * @param listener
         * @return
         */
        fun setViewOnClickListener(listener: ViewClickListener?): Builder {
            mListener = listener
            return this
        }
    }

    companion object {
        private var mPopupWindow: PopupWindow? = null
        private var mBuilder: Builder? = null
        private var mContentView: View? = null
        private var mWindow: Window? = null
        fun newBuilder(): Builder {
            if (mBuilder == null) {
                mBuilder = Builder()
            }
            return mBuilder!!
        }

        /**
         * 取消
         */
        fun dismiss() {
            if (mWindow != null) {
                val params = mWindow!!.attributes
                params.alpha = 1.0f
                mWindow!!.attributes = params
            }
            if (mPopupWindow != null && mPopupWindow!!.isShowing) mPopupWindow!!.dismiss()
        }
    }

    init {
        mBuilder = Builder()
    }
}