part of 'flutter_baiduad.dart';

/// @Author: gstory
/// @CreateDate: 2021/11/22 4:31 下午
/// @Email gstory0404@gmail.com
/// @Description: dart类作用描述

///显示
typedef BOnShow = void Function();

///点击
typedef BOnClick = void Function();

///关闭
typedef BOnClose = void Function();

///曝光
typedef BOnExpose = void Function();

///点击
typedef BOnFail = void Function(int code, dynamic message);

///播放完成
typedef BOnFinish = void Function();

///跳过
typedef BOnSkip = void Function();

///广告预加载完成
typedef BOnReady = void Function();

///广告预加载未完成
typedef BOnUnReady = void Function();

///广告奖励验证
typedef BOnVerify = void Function(
    bool verify, String rewardName, int rewardAmount);

///不感兴趣
typedef BOnDisLike = void Function();

///激励广告回调
class FlutterBaiduAdRewardCallBack {
  BOnShow? onShow;
  BOnClose? onClose;
  BOnFail? onFail;
  BOnClick? onClick;
  BOnSkip? onSkip;
  BOnReady? onReady;
  BOnUnReady? onUnReady;
  BOnFinish? onFinish;
  BOnVerify? onVerify;

  FlutterBaiduAdRewardCallBack({
    this.onShow,
    this.onClick,
    this.onClose,
    this.onFail,
    this.onSkip,
    this.onReady,
    this.onUnReady,
    this.onFinish,
    this.onVerify,
  });
}

///banner广告回调
class FlutterBaiduAdBannerCallBack {
  BOnShow? onShow;
  BOnFail? onFail;
  BOnClick? onClick;
  BOnClose? onClose;

  FlutterBaiduAdBannerCallBack(
      {this.onShow, this.onFail, this.onClick, this.onClose});
}

///splash广告回调
class FlutterBaiduAdSplashCallBack {
  BOnShow? onShow;
  BOnFail? onFail;
  BOnClick? onClick;
  BOnClose? onClose;

  FlutterBaiduAdSplashCallBack(
      {this.onShow, this.onFail, this.onClick, this.onClose});
}

///native广告回调
class FlutterBaiduAdNativeCallBack {
  BOnShow? onShow;
  BOnFail? onFail;
  BOnClick? onClick;
  BOnClose? onClose;
  BOnExpose? onExpose;
  BOnDisLike? onDisLike;

  FlutterBaiduAdNativeCallBack(
      {this.onShow,
      this.onFail,
      this.onClick,
      this.onClose,
      this.onExpose,
      this.onDisLike});
}

///插屏广告回调
class FlutterBaiduAdInteractionCallBack {
  BOnShow? onShow;
  BOnClick? onClick;
  BOnClose? onClose;
  BOnFail? onFail;
  BOnReady? onReady;
  BOnUnReady? onUnReady;
  BOnExpose? onExpose;

  FlutterBaiduAdInteractionCallBack(
      {this.onShow,
      this.onClick,
      this.onClose,
      this.onFail,
      this.onReady,
      this.onUnReady,
      this.onExpose});
}
