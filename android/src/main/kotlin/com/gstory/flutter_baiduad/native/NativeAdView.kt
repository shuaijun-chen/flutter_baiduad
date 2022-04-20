package com.gstory.flutter_baiduad.native

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.baidu.mobads.sdk.api.*
import com.gstory.flutter_baiduad.FlutterBaiduAdConfig
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.platform.PlatformView

import com.gstory.flutter_tencentad.LogUtil


/**
 * @Author: gstory
 * @CreateDate: 2021/11/23 9:56 上午
 * @Description: 描述
 */

class NativeAdView(var activity: Activity,
                   messenger: BinaryMessenger?,
                   id: Int,
                   params: Map<String?, Any?>
) :
        PlatformView, BaiduNativeManager.FeedAdListener {

    private val TAG = "NativeAdView"

    private var mContainer: FrameLayout? = null
    private var channel: MethodChannel

    private var mBaiduNativeManager: BaiduNativeManager? = null

    //广告所需参数
    private var codeId: String = params["androidId"] as String
    private var appSid: String = params["appSid"] as String
    private var isCacheVideo: Boolean = params["isCacheVideo"] as Boolean
    private var timeOut: Int = params["timeOut"] as Int

    init {
        mContainer = FrameLayout(activity)
        mContainer?.layoutParams?.width = ViewGroup.LayoutParams.WRAP_CONTENT
        mContainer?.layoutParams?.height = ViewGroup.LayoutParams.WRAP_CONTENT
        channel = MethodChannel(messenger, FlutterBaiduAdConfig.nativeAdView + "_" + id)
        loadNativeAdView()
    }

    override fun getView(): View {
        return mContainer!!
    }

    /**
     * 将px值转换为dip或dp值，保证尺寸大小不变
     * @param pxValue
     * @return
     */
    fun dip2px(context: Context, dpValue: Float): Int {
        val scale: Float = context.resources.displayMetrics.density //屏幕密度
        return (dpValue * scale + 0.5f).toInt() //加0.5四舍五入
    }

    //px转化为dp
    fun px2dip(context: Context, pxValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }


    private fun loadNativeAdView() {
        mBaiduNativeManager = BaiduNativeManager(activity, codeId, isCacheVideo, timeOut)
        mBaiduNativeManager?.setAppSid(appSid)
        // 构建请求参数
        val requestParameters = RequestParameters.Builder()
                .downloadAppConfirmPolicy(RequestParameters.DOWNLOAD_APP_CONFIRM_ONLY_MOBILE)
                /**
                 * 【信息流传参】传参功能支持的参数见ArticleInfo类，各个参数字段的描述和取值可以参考如下注释
                 * 注意：所有参数的总长度(不包含key值)建议控制在150字符内，避免因超长发生截断，影响信息的上报
                 * 注意：【高】【中】【低】代表参数的优先级，请尽量提供更多高优先级参数
                 */
                // 【高】通用信息：用户性别，取值：0-unknown，1-male，2-female
//                .addExtra(ArticleInfo.USER_SEX, "1") // 【高】最近阅读：小说、文章的名称
//                .addExtra(ArticleInfo.PAGE_TITLE, "测试书名") // 【高】最近阅读：小说、文章的ID
//                .addExtra(ArticleInfo.PAGE_ID, "10930484090") // 【高】书籍信息：小说分类，取值：一级分类和二级分类用'/'分隔
//                .addExtra(ArticleInfo.CONTENT_CATEGORY, "一级分类/二级分类") // 【高】书籍信息：小说、文章的标签，取值：最多10个，且不同标签用'/分隔'
//                .addExtra(ArticleInfo.CONTENT_LABEL, "标签1/标签2/标签3") // 【中】通用信息：收藏的小说ID，取值：最多五个ID，且不同ID用'/分隔'
//                .addExtra(ArticleInfo.FAVORITE_BOOK, "这是小说的名称1/这是小说的名称2/这是小说的名称3") // 【中】最近阅读：一级目录，格式：章节名，章节编号
//                .addExtra(ArticleInfo.FIRST_LEVEL_CONTENTS, "测试一级目录，001") // 【低】书籍信息：章节数，取值：32位整数，默认值0
//                .addExtra(ArticleInfo.CHAPTER_NUM, "12345") // 【低】书籍信息：连载状态，取值：0 表示连载，1 表示完结，默认值0
//                .addExtra(ArticleInfo.PAGE_SERIAL_STATUS, "0") // 【低】书籍信息：作者ID/名称
//                .addExtra(ArticleInfo.PAGE_AUTHOR_ID, "123456") // 【低】最近阅读：二级目录，格式：章节名，章节编号
//                .addExtra(ArticleInfo.SECOND_LEVEL_CONTENTS, "测试二级目录，2000")
                .build()
        // 发起信息流广告请求
        mBaiduNativeManager?.loadFeedAd(requestParameters, this)

    }

    //广告请求成功
    override fun onNativeLoad(p0: MutableList<NativeResponse>?) {
        mContainer?.removeAllViews()
        /// 一个广告只允许展现一次，多次展现、点击只会计入一次
        if (p0 == null && p0?.size == 0) {
            return
        }
        //信息流智能优选 模板类信息流FeedNativeView
        //如果NativeResponse不是智能优选的广告，会导致无法渲染。因此需要配置相应的智能优选广告位ID
        var feedView = FeedNativeView(activity)
        var nativeResponse = p0?.get(0) as XAdNativeResponse?
        nativeResponse?.setAdDislikeListener {
            LogUtil.d("$TAG 信息流广告点击了负反馈渠道")
            channel.invokeMethod("onDisLike", "")
        }
        feedView.setAdData(nativeResponse)
        var width = feedView.adContainerWidth
        var height = feedView.adContainerHeight
        //重新设置容器大小
        mContainer?.layoutParams?.width = width
        mContainer?.layoutParams?.height = height
        mContainer?.addView(feedView)
        mContainer?.setOnClickListener {
//            nativeResponse?.handleClick(view, true)
        }
        nativeResponse?.registerViewForInteraction(mContainer, object : NativeResponse.AdInteractionListener {
            override fun onAdClick() {
                LogUtil.d("$TAG 信息流广告点击")
                channel.invokeMethod("onClick", "")
            }

            override fun onADExposed() {
                LogUtil.d("$TAG 信息流广告曝光成功")
                channel.invokeMethod("onExpose", "")
            }

            override fun onADExposureFailed(p0: Int) {
                //reason参数说明
                //0：默认值；
                //1：广告View不可见或被回收；
                //3：广告View可见区域小于整体大小50%；
                //4：手机息屏；
                //6：广告View过小（长宽不足15px）；
                LogUtil.d("$TAG 信息流广告曝光失败 $p0")
            }

            override fun onADStatusChanged() {
                LogUtil.d("$TAG 信息流广告下载状态回调")
            }

            override fun onAdUnionClick() {
                LogUtil.d("$TAG 信息流广告联盟官网点击回调")
            }
        })
        LogUtil.d("$TAG 信息流显示 $width  $height")
        val map: MutableMap<String, Any?> = mutableMapOf("width" to px2dip(activity, width.toFloat()), "height" to px2dip(activity, height.toFloat()))
        channel.invokeMethod("onShow", map)
    }

    //广告请求失败
    override fun onNativeFail(p0: Int, p1: String?) {
        LogUtil.d("$TAG 信息流广告请求失败  $p0  $p1")
        val map: MutableMap<String, Any?> = mutableMapOf("code" to 0, "message" to p1)
        channel.invokeMethod("onFail", map)
    }

    //无广告返回
    override fun onNoAd(p0: Int, p1: String?) {
        LogUtil.d("$TAG 信息流无广告返回  $p0  $p1")
        val map: MutableMap<String, Any?> = mutableMapOf("code" to 0, "message" to p1)
        channel.invokeMethod("onFail", map)
    }

    //视频物料缓存成功
    override fun onVideoDownloadSuccess() {
        LogUtil.d("$TAG 信息流视频物料缓存成功")
    }

    //视频物料缓存失败
    override fun onVideoDownloadFailed() {
        LogUtil.d("$TAG 信息流视频物料缓存失败")
    }

    //lp页面被关闭（返回键或关闭图标）
    override fun onLpClosed() {
        LogUtil.d("$TAG 信息流lp页面被关闭")
    }

    override fun dispose() {

        mContainer?.removeAllViews()
    }
}