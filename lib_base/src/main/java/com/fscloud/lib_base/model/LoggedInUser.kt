package com.fscloud.lib_base.model

import android.os.Parcel
import android.os.Parcelable

/**
 * 用户信息
 */
data class LoggedInUser(
    //用户id
    val userId: String? = null,
    //用户名
    val username: String? = null,
    //创建时间
    val createTime: String? = null,
    //最后一次登录时间
    val lastLoginTime: String? = null,
    //用户机构id
    val orgId: String? = null,
    //用户机构名
    val orgName: String? = null,
    //手机号
    val phone: String? = null,
    //角色id
    val roleId: String? = null,
    //角色名称
    val roleName: String? = null,
    //状态1:正差 0：冻结
    val status: Int = 1,
    //租户id
    val tenantId: String? = null,
    //租户名称
    val tenantName: String? = null,
    //用户实名信息
    val userInfoVO: Auth? = null,
    //所属区域
    val areaList: List<Area>? = null,
    //是否为会员
    val isVip: Boolean = false,
    //会员到期时间
    val endTime: String? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readParcelable(Auth::class.java.classLoader),
        parcel.createTypedArrayList(Area),
        parcel.readByte() != 0.toByte(),
        parcel.readString(),
    ) {
    }

    data class Area(
        //编号
        val id: String? = null,
        //名称
        val fullName: String? = null,
        //层级
        val level: String? = null,
        //名称
        val name: String? = null,
        //上级
        val parent: String? = null,
        //状态1:正常 0:冻结
        val status: Int = 1
    ) : Parcelable {
        constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt()
        ) {
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeString(id)
            parcel.writeString(fullName)
            parcel.writeString(level)
            parcel.writeString(name)
            parcel.writeString(parent)
            parcel.writeInt(status)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<Area> {
            override fun createFromParcel(parcel: Parcel): Area {
                return Area(parcel)
            }

            override fun newArray(size: Int): Array<Area?> {
                return arrayOfNulls(size)
            }
        }

    }

    data class Auth(
        val id: String? = null,
        //历史头像
        val avatars: String = "",
        //姓名
        val fullname: String = "",
        //身份证号
        val idcard: String = "",
        //身份证人像面
        val idcardBack: String? = null,
        //国徽面
        val idcardFront: String? = null,
        //性别
        val sex: String? = null
    ) : Parcelable {
        constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString(),
            parcel.readString(),
            parcel.readString()
        ) {
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeString(id)
            parcel.writeString(avatars)
            parcel.writeString(fullname)
            parcel.writeString(idcard)
            parcel.writeString(idcardBack)
            parcel.writeString(idcardFront)
            parcel.writeString(sex)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<Auth> {
            override fun createFromParcel(parcel: Parcel): Auth {
                return Auth(parcel)
            }

            override fun newArray(size: Int): Array<Auth?> {
                return arrayOfNulls(size)
            }
        }

    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(userId)
        parcel.writeString(username)
        parcel.writeString(createTime)
        parcel.writeString(lastLoginTime)
        parcel.writeString(orgId)
        parcel.writeString(orgName)
        parcel.writeString(phone)
        parcel.writeString(roleId)
        parcel.writeString(roleName)
        parcel.writeInt(status)
        parcel.writeString(tenantId)
        parcel.writeString(tenantName)
        parcel.writeParcelable(userInfoVO, flags)
        parcel.writeTypedList(areaList)
        parcel.writeByte(if (isVip) 1 else 0)
        parcel.writeString(endTime)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<LoggedInUser> {
        override fun createFromParcel(parcel: Parcel): LoggedInUser {
            return LoggedInUser(parcel)
        }

        override fun newArray(size: Int): Array<LoggedInUser?> {
            return arrayOfNulls(size)
        }
    }


}