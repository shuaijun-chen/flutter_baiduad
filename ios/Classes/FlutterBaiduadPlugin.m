#import "FlutterBaiduadPlugin.h"
#import <BaiduMobAdSDK/BaiduMobAdCommonConfig.h>
#import <BaiduMobAdSDK/BaiduMobAdSetting.h>
#import "BannerView.h"
#import "RewardAd.h"
#import "FlutterBaiduadEvent.h"
#import "BaiduAdManager.h"
#import "SplashAd.h"
#import "InsertAd.h"

@implementation FlutterBaiduadPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
    FlutterMethodChannel* channel = [FlutterMethodChannel
                                     methodChannelWithName:@"flutter_baiduad"
                                     binaryMessenger:[registrar messenger]];
    FlutterBaiduadPlugin* instance = [[FlutterBaiduadPlugin alloc] init];
    [registrar addMethodCallDelegate:instance channel:channel];
    //注册event
    [[FlutterBaiduadEvent sharedInstance]  initEvent:registrar];

    //注册banner
    [registrar registerViewFactory:[[BannerViewFactory alloc] initWithMessenger:registrar.messenger] withId:@"com.gstory.flutter_baiduad/BannerAdView"];
    //注册splash
    [registrar registerViewFactory:[[SplashAdFactory alloc] initWithMessenger:registrar.messenger] withId:@"com.gstory.flutter_baiduad/SplashAdView"];
}

- (void)handleMethodCall:(FlutterMethodCall*)call result:(FlutterResult)result {
    if ([@"register" isEqualToString:call.method]) {
        NSString *appId = call.arguments[@"iosAppId"];
        [[BaiduAdManager sharedInstance] initAppId:appId];
        result(@YES);
    }else if([@"getSDKVersion" isEqualToString:call.method]){
        result(SDK_VERSION_IN_MSSP);
    }else if([@"privacy" isEqualToString:call.method]){
        BOOL bDPermission = [call.arguments[@"bDPermission"] boolValue];
        BOOL personalAds = [call.arguments[@"personalAds"] boolValue];
        [[BaiduMobAdSetting sharedInstance] setBDPermissionEnable:bDPermission];
        [[BaiduMobAdSetting sharedInstance] setLimitBaiduPersonalAds:personalAds];
        result(@YES);
    }else if([@"loadRewardAd" isEqualToString:call.method]){
        RewardAd *rewardAd = [RewardAd sharedInstance];
        [rewardAd initAd:call.arguments];
    }else if([@"showRewardAd" isEqualToString:call.method]){
        [[RewardAd sharedInstance] showAd];
        //加载插屏广告
    }else if([@"loadInterstitialAd" isEqualToString:call.method]){
        [[InsertAd sharedInstance] initAd:call.arguments];
        //展示插屏广告
    }else if([@"showInterstitialAd" isEqualToString:call.method]){
        [[InsertAd sharedInstance] showInsertAd];
    } else {
        result(FlutterMethodNotImplemented);
    }
}

@end
