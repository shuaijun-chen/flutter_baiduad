import 'dart:async';

import 'package:flutter/services.dart';
import 'package:flutter_baiduad/flutter_baiduad_code.dart';

import 'flutter_baiduad.dart';

/// @Author: gstory
/// @CreateDate: 2021/11/22 4:29 下午
/// @Email gstory0404@gmail.com
/// @Description: dart类作用描述

const EventChannel baiduAdEventEvent =
    EventChannel("com.gstory.flutter_baiduad/adevent");

class FlutterBaiduAdStream {
  ///
  /// # 注册stream监听原生返回的信息
  ///
  /// [flutterBaiduAdRewardCallBack] 激励广告回调
  ///
  /// [interactionAdCallBack] 插屏广告回调
  ///
  static StreamSubscription initAdStream(
      {FlutterBaiduAdRewardCallBack? flutterBaiduAdRewardCallBack,
      FlutterBaiduAdInteractionCallBack? flutterBaiduAdInteractionCallBack}) {
    StreamSubscription _adStream =
        baiduAdEventEvent.receiveBroadcastStream().listen((event) {
          print("激励广告-------》$event");
      switch (event[FlutterBaiduAdType.adType]) {

        ///激励广告
        case FlutterBaiduAdType.rewardAd:
          switch (event[FlutterBaiduAdMethod.onAdMethod]) {
            case FlutterBaiduAdMethod.onShow:
              flutterBaiduAdRewardCallBack?.onShow!();
              break;
            case FlutterBaiduAdMethod.onClose:
              flutterBaiduAdRewardCallBack?.onClose!();
              break;
            case FlutterBaiduAdMethod.onClick:
              flutterBaiduAdRewardCallBack?.onClick!();
              break;
            case FlutterBaiduAdMethod.onFail:
              flutterBaiduAdRewardCallBack?.onFail!(
                  event["code"], event["message"]);
              break;
            case FlutterBaiduAdMethod.onSkip:
              flutterBaiduAdRewardCallBack?.onSkip!();
              break;
            case FlutterBaiduAdMethod.onReady:
              flutterBaiduAdRewardCallBack?.onReady!();
              break;
            case FlutterBaiduAdMethod.onUnReady:
              flutterBaiduAdRewardCallBack?.onUnReady!();
              break;
            case FlutterBaiduAdMethod.onFinish:
              flutterBaiduAdRewardCallBack?.onFinish!();
              break;
            case FlutterBaiduAdMethod.onVerify:
              flutterBaiduAdRewardCallBack?.onVerify!(
                  event["verify"], event["rewardName"], event["rewardAmount"]);
              break;
          }
          break;

        ///激励广告
        case FlutterBaiduAdType.interactAd:
          switch (event[FlutterBaiduAdMethod.onAdMethod]) {
            case FlutterBaiduAdMethod.onShow:
              flutterBaiduAdInteractionCallBack?.onShow!();
              break;
            case FlutterBaiduAdMethod.onClose:
              flutterBaiduAdInteractionCallBack?.onClose!();
              break;
            case FlutterBaiduAdMethod.onClick:
              flutterBaiduAdInteractionCallBack?.onClick!();
              break;
            case FlutterBaiduAdMethod.onFail:
              flutterBaiduAdInteractionCallBack?.onFail!(
                  event["code"], event["message"]);
              break;
            case FlutterBaiduAdMethod.onReady:
              flutterBaiduAdInteractionCallBack?.onReady!();
              break;
            case FlutterBaiduAdMethod.onUnReady:
              flutterBaiduAdInteractionCallBack?.onUnReady!();
              break;
            case FlutterBaiduAdMethod.onExpose:
              flutterBaiduAdInteractionCallBack?.onExpose!();
              break;
          }
          break;
      }
    });
    return _adStream;
  }

  static void deleteAdStream(StreamSubscription stream) {
    stream.cancel();
  }
}
