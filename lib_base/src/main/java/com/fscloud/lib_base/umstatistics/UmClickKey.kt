package com.fscloud.lib_base.umstatistics

import com.lxj.xpopup.XPopup
import com.lxj.xpopup.util.XPopupUtils

/**
 * @ProjectName :
 * @author MrChen
 * @date :2021/1/25 14:44
 * @description: 友盟自定义事件  key 类
 */
object UmClickKey {

    /**
     * 首页模块
     */
    const val HOME = "indexClick"  //首页点击
    const val BUSINESS = "yyzzClick"  //营业执照点击
    const val INDIVIDUAL_HANDLING = "personalClick"  //个人办理点击
    const val ENTERPRISE_HANDLING = "enterpriseClick"  //企业办理点击
    const val FOOD_PERMIT = "spxkClick"  //食品许可点击
    const val FOOD_BUSINESS_CARD = "spjyxkClick"  //食品经营许可登记点击
    const val FOOD_BUSINESS_SHOP = "spxjydbaClick"  // 食品小经营店备案点击

    const val ADD_RECORD = "addRecordClick"  // 新增备案点击
    const val ELECTRONIC_LICENSE = "certificationClick"  // 电子证照点击
    const val APPLY_LICENSE = "applyCertificationClick"  // 申领证照点击
    const val TIANFU_MORE = "tftbMoreClick"  // 天府通办更多点击
    const val FOOD_SAFETY_MORE = "saglMoreClick"  // 食安管理更多点击
    const val DAILY_REPORT = "dailyReportClick"  // 日常上报点击
    const val DAILY_REPORT_ADD = "addReportClick"  // 上报按钮点击
    const val VERIFICATION_RECORD = "verificationListClick"  // 核查记录点击
    const val VERIFICATION_RECORD_DETAIL = "verificationDetailsClick"  // 核查记录详情点击
    const val EMPLOYEES_LIST = "employeesListClick"  // 员工管理点击  跳转员工列表
    const val ADD_EMPLOYEES = "addemployeesClick"  // 新增员工点击
    const val EMPLOYEES_DETAIL = "employeesDetailClick"  // 员工详情点击
    const val BANNER_BY_ID = "bannerClick"  // banner点击

    /**
     * 视频模块
     */
    const val COLLEGE_MODULE = "collegeClick"  // 学院模块点击
    const val VIDEO_BY_ID = "shortVideoClick"  // 短视频点击
    const val VIDEO_LIST = "videoListClick"  // 短视频目录点击

    /**
     * 商城模块
     */
    const val MALL_MODULE = "mallClick"  // 商城模块点击
    const val LEAVE_ONE_MALL_BY_NAME = "level1ClassClick"  // 一级分类点击
    const val LEAVE_TWO_MALL_BY_NAME = "level2ClassClick"  // 二级分类点击
    const val COMMODITY_DETAIL_TO_ID = "goodsDetailClick"  // 商品详情点击
    const val COMMODITY_BUY_TO_ID = "goodsBuyClick"  // 商品立即购买按钮点击
    const val COMMODITY_LOOK_VIP_BY_ID = "goodsOrderViewMemberClick"  // 商品订单查看会员详情点击
    const val COMMODITY_PAY_BY_ID = "goodsOrderPayClick"  // 商品订单立即支付点击

    /**
     * 我的模块
     */
    const val MINE_MODULE = "mineClick"  // 我的模块点击
    const val SWITCH_SHOP = "switchShopClick"  // 切换店铺点击
    const val NAME_AUTHENTICATION = "nameAuthenticationClick"  // 实名认证点击
    const val MEMBER_DETAIL = "memberDetailClick"  // 会员服务点击
    const val MEMBER_RENEWAL = "memberRenewalClick"  // 会员立即续费点击
    const val MEMBER_PAY = "memberPayClick"  // 购买会员点击

    /**
     * 订单模块
     */
    const val MY_ORDER = "myOrderClick"  // 我的订单点击
    const val MY_ORDER_LIST = "orderListClick"  // 全部订单列表点击
    const val MY_ORDER_WAIT_LIST = "orderWaitClick"  // 待付款列表点击
    const val ORDER_CANCEL_BY_ID = "orderCancelClick"  // 取消订单点击
    const val ORDER_PAY_BY_ID = "orderPayClick"  // 付款点击
    const val ORDER_DETAIL_BY_ID = "orderDetailClick"  // 订单详情点击
    const val ORDER_DETAIL_PAY_BY_ID = "orderDetailPayClick"  // 订单详情支付点击
    const val ORDER_DETAIL_CANCEL_BY_ID = "orderDetailCancelClick"  // 订单详情取消点击

    /**
     * 经营统计
     */
    const val OPERATING_STATISTICS = "businessClick"  // 经营统计点击

    /**
     * 账户安全
     */
    const val ACCOUNT_SECURITY = "accountSecurityClick"  // 账户安全点击
    const val PASSWORD_CHANGE = "passwordChangeClick"  // 修改密码点击
    const val PHONE_NUMBER_CHANGE = "phonenumberChangeClick"  // 修改手机号码点击
    const val EXIT_LOGIN_APP = "quitClick"  // 退出登录

    /**
     * 关于分享
     */
    const val ABOUT_MINE = "aboutClick"  // 关于我们点击
    const val SHAPE_APP = "shareAboutClick"  // 关于分享APP按钮点击
    const val SAVE_SHARE_APP_PHOTO = "savePicClick"  // 保存图片点击
    const val SHAPE_WX = "wxShareClick"  // 微信分享点击
    const val SHAPE_QQ = "qqShareClick"  // QQ分享点击





}