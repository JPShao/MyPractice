package com.fscloud.lib_base.db

import com.blankj.utilcode.util.GsonUtils
import com.fscloud.lib_base.constant.Mapper
import com.fscloud.lib_base.model.EnterpriseData
import com.fscloud.lib_base.model.FileUrlBean
import com.fscloud.lib_base.model.banner.BannerModel
import com.google.gson.Gson
import java.lang.Exception

/**
 * @ProjectName :
 * @author MrChen
 * @date :2021/1/19 10:46
 * @description:
 */
object Launcher {

    /**
     * 开屏广告数据
     */
    val launcherData: BannerModel? get() = try {
        val info = SpUtils.getInstance().decodeString(Mapper.LAUNCHER_KEY)
        if (info.isNotEmpty()){
            GsonUtils.fromJson(info, BannerModel::class.java)
        }else {
            null
        }
    }catch (e: Exception){
        e.printStackTrace()
        null
    }

    /**
     * 本地是否已经保存了开屏信息
     */
    val isHasLauncherInfo: Boolean get() =
        SpUtils.getInstance().decodeString(Mapper.LAUNCHER_KEY).isNotEmpty()

    /**
     * 保存launcher信息
     */
    fun saveLauncherInfo(info:BannerModel?){
        if(info ==null){
            SpUtils.getInstance().encode(Mapper.LAUNCHER_KEY,"")
        }else{
            SpUtils.getInstance().encode(Mapper.LAUNCHER_KEY,Gson().toJson(info))

        }
    }



}
