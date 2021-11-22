/// @Author: gstory
/// @CreateDate: 2021/11/22 4:38 下午
/// @Email gstory0404@gmail.com
/// @Description: dart类作用描述 

///数据类型
class FlutterBaiduAdType {
  static const String adType = "adType";

  ///激励广告
  static const String rewardAd = "rewardAd";

  ///插屏广告
  static const String interactAd = "interactAd";
}

class FlutterBaiduAdMethod {
  ///stream中 广告方法
  static const String onAdMethod = "onAdMethod";

  ///广告加载状态 view使用
  ///显示view
  static const String onShow = "onShow";

  ///加载失败
  static const String onFail = "onFail";

  ///点击
  static const String onClick = "onClick";

  ///倒计时结束
  static const String onFinish = "onFinish";

  ///广告关闭
  static const String onClose = "onClose";

  ///广告奖励校验
  static const String onVerify = "onVerify";

  ///广告预加载完成
  static const String onReady = "onReady";

  ///广告未预加载
  static const String onUnReady = "onUnReady";

  ///跳过
  static const String onSkip = "onSkip";
}


