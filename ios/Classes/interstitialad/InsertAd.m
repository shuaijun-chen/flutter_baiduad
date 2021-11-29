//
//  InsertAd.m
//  flutter_baiduad
//
//  Created by 郭维佳 on 2021/11/29.
//

#import "InsertAd.h"
#import "BaiduMobAdSDK/BaiduMobAdInterstitial.h"
#import "LogUtil.h"
#import "StringUtls.h"
#import "BaiduAdManager.h"

@interface InsertAd()<BaiduMobAdInterstitialDelegate>
@property(nonatomic,strong) BaiduMobAdInterstitial *bdInsertAd;
@property(nonatomic,strong) NSString *codeId;
@property(nonatomic,strong) NSString *appSid;
@property(nonatomic,assign) BOOL isFullScreen;
@end

@implementation InsertAd

+ (instancetype)sharedInstance{
    static InsertAd *myInstance = nil;
    if(myInstance == nil){
        myInstance = [[InsertAd alloc]init];
    }
    return myInstance;
}

//预加载插屏广告
-(void)initAd:(NSDictionary*)arguments{
    NSDictionary *dic = arguments;
    _codeId = dic[@"iosId"];
    _appSid = dic[@"appSid"];
    _isFullScreen = [dic[@"isFullScreen"] boolValue];
    [self loadInsertAd];
}

//展示广告
-(void)showAd{
}

//加载广告
-(void)loadInsertAd{
    _bdInsertAd = [[BaiduMobAdInterstitial alloc]init];
    _bdInsertAd.AdUnitTag = _codeId;
    _bdInsertAd.delegate = self;
    _bdInsertAd.interstitialType = BaiduMobAdViewTypeInterstitialOther;
    [_bdInsertAd load];
}

#pragma mark - 广告请求BaiduMobAdInterstitialDelegate

/**
 *  appid
 */
- (NSString *)publisherId{
    if([StringUtls isStringEmpty:_appSid]){
        return [BaiduAdManager sharedInstance].getAppId;
    }else{
        return self.appSid;
    }
}


/**
 *  广告预加载成功
 */
- (void)interstitialSuccessToLoadAd:(BaiduMobAdInterstitial *)interstitial{
    GLog(@"插屏广告预加载成功");
    UIWindow *window = [[UIApplication sharedApplication] windows];
    [_bdInsertAd presentFromRootViewController:window.rootViewController];
}

/**
 *  广告预加载失败
 */
- (void)interstitialFailToLoadAd:(BaiduMobAdInterstitial *)interstitial{
    GLog(@"插屏广告预加载失败");
    _bdInsertAd.delegate = nil;
    _bdInsertAd = nil;
}

/**
 *  广告即将展示
 */
- (void)interstitialWillPresentScreen:(BaiduMobAdInterstitial *)interstitial{
    GLog(@"插屏广告即将展示");
}

/**
 *  广告展示成功
 */
- (void)interstitialSuccessPresentScreen:(BaiduMobAdInterstitial *)interstitial{
    GLog(@"插屏");
}

/**
 *  广告展示失败
 */
- (void)interstitialFailPresentScreen:(BaiduMobAdInterstitial *)interstitial withError:(BaiduMobFailReason) reason{
    GLog(@"插屏 广告展示失败");
}

/**
 *  广告展示被用户点击时的回调
 */
- (void)interstitialDidAdClicked:(BaiduMobAdInterstitial *)interstitial{
    GLog(@"插屏广告展示被用户点击时的回调");
}

/**
 *  广告展示结束
 */
- (void)interstitialDidDismissScreen:(BaiduMobAdInterstitial *)interstitial{
    GLog(@"插屏广告展示结束");
}

/**
 *  广告详情页被关闭
 */
- (void)interstitialDidDismissLandingPage:(BaiduMobAdInterstitial *)interstitial{
    GLog(@"插屏 广告详情页被关闭");
}

@end
