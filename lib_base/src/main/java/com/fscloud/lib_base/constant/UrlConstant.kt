package com.fscloud.lib_base.constant

object UrlConstant {

    /**
     *  1正式环境， 2预生产环境， 3测试环境， 4本地环境
     */
    const val environmentType = 3

    /**
     * 正式环境：http://gateway.shianyunlian.cn/ , 新域名：http://gateway.g-mos.cn
     * 预生产环境：http://pre.gateway.shianyunlian.cn/， http://pre.gateway.g-mos.cn
     * 测试环境：http://47.108.53.178:9010/  , http://test.gateway.g-mos.cn
     * 本地环境：http://192.168.3.179:9010/
     * 刘强环境 ：http://192.168.3.241:9003/
     *
     */
    val BASE_URL: String by lazy {
        when (environmentType) {
            1 -> {
                "https://gateway.g-mos.cn"
            }
            2 -> {
                "https://pre.gateway.g-mos.cn"
            }
            3 -> {
                "http://test.gateway.g-mos.cn"
            }
            4 -> {
                "http://192.168.3.241:9003/"
            }
            else -> {
                "https://gateway.g-mos.cn"
            }
        }
    }

    /**
     * h5基础路由
     * 正式环境：http://shop.app.h5.shianyunlian.cn/#/ , 新域名：http://shop.app.h5.g-mos.cn/#/
     * 预生产环境：http://pre.shop.app.h5.shianyunlian.cn/#/, http://pre.shop.app.h5.g-mos.cn/#/
     * 测试环境：http://test.shop.app.h5.shianyunlian.cn/#/ ，http://test.shop.app.h5.g-mos.cn/#/
     */
    private val H5_BASE_URL: String by lazy {
        when (environmentType) {
            1 -> {
                "https://shop.app.h5.g-mos.cn/#/"
            }
            2 -> {
                "https://pre.app.h5.g-mos.cn/#/"
            }
            3 -> {
                "http://test.shop.app.h5.g-mos.cn/#/"
            }
            else -> {
                "http://shop.app.h5.g-mos.cn/#/"
            }
        }
    }

    //电子证照申领须知
    val LICENSE_APP: String = "${H5_BASE_URL}agreement/electronicLicenseApplication"
    //会员协议
    val VIP_AGREEMENT_URL: String = "${H5_BASE_URL}member/agreement"
    //用户协议地址9004
    val USER_AGREEMENT_URL: String = "${H5_BASE_URL}login/userAgreement"
    //隐私政策h5地址
    val USER_PRIVACY_URL: String = "${H5_BASE_URL}login/privacyPolicy"
    //经营统计
    val BUSINESS: String = "${H5_BASE_URL}statistics/business"
    //行业分析
    val INDUSTRY_ANALYSIS:String = "${H5_BASE_URL}statistics/industryAnalysis"
    //外卖统计
    val TAKE_AWAY:String = "${H5_BASE_URL}statistics/takeaway"
    //店铺诊断
    val STORE_DIAGNOSE = "${H5_BASE_URL}statistics/shopDiagnosis"
    //消费者分析
    val CONSUMER_ANALYSIS = "${H5_BASE_URL}statistics/consumerAnalysis"
    //堂食统计
    val EAT_IN = "${H5_BASE_URL}statistics/houseFood"
    //饿了么开店H5
    const val ELM_OPEN_SHOP = "https://kaidian.ele.me/?origin=say"
}