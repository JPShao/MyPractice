package com.fscloud.lib_base.utils.gps

import android.app.Activity
import com.blankj.utilcode.util.LogUtils
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

/**
 * @ProjectName :
 * @author MrChen
 * @date :2021/1/20 15:45
 * @description:
 */
object  LocationUtils {
    var gps:GPS? = null
    const val PERMISSIONS_ACCESS_LOCATION = 500
    const val PERMISSIONS_ACCESS_LOCATION_EVENT = "PERMISSIONS_ACCESS_LOCATION_EVENT"
    private var activity: Activity? = null
    private var listener: GPS.LocationSuccessListener? = null
    val perms = arrayOf<String>(
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.ACCESS_COARSE_LOCATION

    )



    fun startLocation(act: Activity, listenerLocation: GPS.LocationSuccessListener) {
        activity = act
        this.listener = listenerLocation
        if (EasyPermissions.hasPermissions(act, *perms)) {
            //有定位权限  定位
            gps = GPS(act)
            gps?.startLocation(listenerLocation)

        } else {

            EasyPermissions.requestPermissions(
                act,
                "需要定位权限",
                PERMISSIONS_ACCESS_LOCATION,
                *perms
            )

        //    listenerLocation.getLocationErr("需要定位权限")
        }


    }


    fun isHasLocation(act: Activity) = EasyPermissions.hasPermissions(act, *perms)



//
//
//        override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
////        if (activity != null)
////            EasyPermissions.onRequestPermissionsResult(
////                requestCode,
////                permissions,
////                grantResults,
////                activity
////            )
//    }
//
//    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
//        //有定位权限  定位
//        if (requestCode ==PERMISSIONS_ACCESS_LOCATION){
//            LogUtils.e("EasyPermissions","允许定位重新定位")
//            val gps = GPS(activity)
//            gps.startLocation(this.listener)
//        }
//
//    }
//
//
//    //
//    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
//        if (activity != null) {
//            if (EasyPermissions.somePermissionPermanentlyDenied(activity!!, perms)) {
//                AppSettingsDialog.Builder(activity!!)
//                    .setTitle("申请权限")
//                    .setRationale("应用需要这个权限")
//                    .build()
//                    .show()
//            }
//        }
//    }



}