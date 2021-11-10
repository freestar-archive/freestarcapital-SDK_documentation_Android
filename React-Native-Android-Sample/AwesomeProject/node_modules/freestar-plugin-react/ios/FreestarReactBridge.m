//
//  FreestarReactBridge.m
//  FreestarPlatformReactNativePlugin
//
//  Created by Lev Trubov on 6/25/20.
//  Copyright © 2020 Freestar. All rights reserved.
//

#import "FreestarReactBridge.h"
@import FreestarAds;

@interface FreestarReactBridge () <FreestarInterstitialDelegate, FreestarRewardedDelegate>

@property FreestarInterstitialAd *interstitial;
@property FreestarRewardedAd *reward;

@end

@implementation FreestarReactBridge

RCT_EXPORT_MODULE();

@synthesize bridge = _bridge;

+ (BOOL)requiresMainQueueSetup
{
  return YES;  // only do this if your module initialization relies on calling UIKit!
}

- (dispatch_queue_t)methodQueue
{
  return dispatch_get_main_queue();
}

#pragma mark - init

RCT_EXPORT_METHOD(initWithAdUnitID:(NSString *)apiKey) {
    [Freestar initWithAdUnitID:apiKey];
}

#pragma mark - targeting

RCT_EXPORT_METHOD(setDemographics:(NSInteger)age
                  birthday:(NSDate *)birthday
                  gender:(NSString *)gender
                  maritalStatus:(NSString *)maritalStatus
                  ethnicity:(NSString *)ethnicity
                  ) {
    FreestarDemographics *dem = [Freestar demographics];
    [dem setAge:age];
    unsigned unitFlags = NSCalendarUnitDay | NSCalendarUnitMonth |  NSCalendarUnitYear;
    NSDateComponents *comps = [[NSCalendar currentCalendar] components:unitFlags fromDate:birthday];
    [dem setBirthdayYear:comps.year month:comps.month day:comps.day];
    
    [self processGender:gender.lowercaseString forDemographics:dem];
    [self processMaritalStatus:maritalStatus.lowercaseString forDemographics:dem];
    [dem setEthnicity:ethnicity];
}

-(void)processGender:(NSString *)gender forDemographics:(FreestarDemographics *)dem {
    if ([gender isEqualToString:@"m"] ||
        [gender isEqualToString:@"male"]) {
        [dem setGender:FreestarGenderMale];
    } else if ([gender isEqualToString:@"f"] ||
               [gender isEqualToString:@"female"]) {
        [dem setGender:FreestarGenderFemale];
    } else if ([gender isEqualToString:@"o"] ||
               [gender isEqualToString:@"other"]) {
        [dem setGender:FreestarGenderOther];
    }
}

-(void)processMaritalStatus:(NSString *)ms forDemographics:(FreestarDemographics *)dem {
    if([ms containsString:@"single"]) {
        [dem setMaritalStatus:FreestarMaritalStatusSingle];
    }else if([ms containsString:@"married"]) {
        [dem setMaritalStatus:FreestarMaritalStatusMarried];
    }else if([ms containsString:@"divorced"]) {
        [dem setMaritalStatus:FreestarMaritalStatusDivorced];
    }else if([ms containsString:@"widowed"]) {
        [dem setMaritalStatus:FreestarMaritalStatusWidowed];
    }else if([ms containsString:@"separated"]) {
        [dem setMaritalStatus:FreestarMaritalStatusSeparated];
    }else if([ms containsString:@"other"]) {
        [dem setMaritalStatus:FreestarMaritalStatusOther];
    }
}

RCT_EXPORT_METHOD(setLocation:(NSString *)dmaCode
                  postalCode:(NSString *)postalCode
                  currPostal:(NSString *)currPostal
                  latitide:(NSString *)latitude
                  longitude:(NSString *)longitude
                  ) {
    FreestarLocation *loc = [Freestar location];
    loc.dmacode = dmaCode;
    loc.postalcode = postalCode;
    loc.currpostal = currPostal;
    loc.location = [[CLLocation alloc] initWithLatitude:[latitude doubleValue] longitude:[longitude doubleValue]];

}



#pragma mark - privacy

RCT_EXPORT_METHOD(subjectToGDPR:(BOOL)gdprApplies withConsent:(NSString *)gdprConsentString) {
    FreestarPrivacySettings *priv = [Freestar privacySettings];
    [priv subjectToGDPR:gdprApplies withConsent:gdprConsentString];
}

#pragma mark - events

- (NSArray<NSString *> *)supportedEvents{
    return @[@"onInterstitialLoaded",
             @"onInterstitialClicked",
             @"onInterstitialShown",
             @"onInterstitialFailed",
             @"onInterstitialDismissed",
             @"onRewardedLoaded",
             @"onRewardedFailed",
             @"onRewardedShowFailed",
             @"onRewardedCompleted",
             @"onRewardedShown",
             @"onRewardedDismissed"];
}

#pragma mark - launching interstitial

RCT_EXPORT_METHOD(loadInterstitialAd:(NSString *)placement) {
  self.interstitial = [[FreestarInterstitialAd alloc] initWithDelegate:self];
  [self.interstitial loadPlacement:placement];
}

RCT_EXPORT_METHOD(showInterstitialAd) {
    [self.interstitial showFrom:[self visibleViewController:[UIApplication sharedApplication].keyWindow.rootViewController]];
}

#pragma mark - Interstitial delegate

-(void)freestarInterstitialLoaded:(FreestarInterstitialAd *)ad {
  [self sendEventWithName:@"onInterstitialLoaded" body:nil];
}

-(void)freestarInterstitialFailed:(FreestarInterstitialAd *)ad because:(FreestarNoAdReason)reason {
  [self sendEventWithName:@"onInterstitialFailed" body:nil];
}

-(void)freestarInterstitialShown:(FreestarInterstitialAd *)ad {
  [self sendEventWithName:@"onInterstitialShown" body:nil];
}

-(void)freestarInterstitialClicked:(FreestarInterstitialAd *)ad {
  [self sendEventWithName:@"onInterstitialClicked" body:nil];
}

-(void)freestarInterstitialClosed:(FreestarInterstitialAd *)ad {
  [self sendEventWithName:@"onInterstitialDismissed" body:nil];
}


#pragma mark - launching reward

RCT_EXPORT_METHOD(loadRewardAd:(NSString *)placement) {
  
  self.reward = [[FreestarRewardedAd alloc] initWithDelegate:self andReward:[FreestarReward blankReward]];
  [self.reward loadPlacement:placement];
}

RCT_EXPORT_METHOD(showRewardAd:(NSString *)rewardName
                  amount:(NSInteger)rewardAmount
                  userID:(NSString *)userID
                  secretKey:(NSString *)secretKey) {
  FreestarReward *rew = [FreestarReward blankReward];

  rew.rewardName = rewardName;
  rew.rewardAmount = rewardAmount;
  rew.userID = userID;
  rew.secretKey = secretKey;
  self.reward.reward = rew;
  [self.reward showFrom:[self visibleViewController:[UIApplication sharedApplication].keyWindow.rootViewController]];
}

#pragma mark - Reward delegate

-(void)freestarRewardedLoaded:(FreestarRewardedAd *)ad {
  [self sendEventWithName:@"onRewardedLoaded" body:nil];
}

-(void)freestarRewardedFailed:(FreestarRewardedAd *)ad because:(FreestarNoAdReason)reason {
  [self sendEventWithName:@"onRewardedFailed" body:nil];
}

-(void)freestarRewardedShown:(FreestarRewardedAd *)ad {
  [self sendEventWithName:@"onRewardedShown" body:nil];
}

-(void)freestarRewardedClosed:(FreestarRewardedAd *)ad {
  [self sendEventWithName:@"onRewardedDismissed" body:nil];
}

-(void)freestarRewardedFailedToStart:(FreestarRewardedAd *)ad because:(FreestarNoAdReason)reason {
  [self sendEventWithName:@"onRewardedShowFailed" body:nil];
}

-(void)freestarRewardedAd:(FreestarRewardedAd *)ad received:(NSString *)rewardName amount:(NSInteger)rewardAmount {
  [self sendEventWithName:@"onRewardedCompleted"
  body:@{@"rewardName" : rewardName,
         @"rewardAmount" : @(rewardAmount)
         }];
}

#pragma mark - support

- (UIViewController *)visibleViewController:(UIViewController *)rootViewController
{
    if (rootViewController.presentedViewController == nil)
    {
        return rootViewController;
    }
    if ([rootViewController.presentedViewController isKindOfClass:[UINavigationController class]])
    {
        UINavigationController *navigationController = (UINavigationController *)rootViewController.presentedViewController;
        UIViewController *lastViewController = [[navigationController viewControllers] lastObject];
        
        return [self visibleViewController:lastViewController];
    }
    if ([rootViewController.presentedViewController isKindOfClass:[UITabBarController class]])
    {
        UITabBarController *tabBarController = (UITabBarController *)rootViewController.presentedViewController;
        UIViewController *selectedViewController = tabBarController.selectedViewController;
        
        return [self visibleViewController:selectedViewController];
    }
    
    UIViewController *presentedViewController = (UIViewController *)rootViewController.presentedViewController;
    
    return [self visibleViewController:presentedViewController];
}

#pragma mark - custom segment properties

RCT_EXPORT_METHOD(setCustomSegmentProperty:(NSString *)key with:(NSString *)value) {
    [FreestarCustomSegmentProperties setCustomSegmentProperty:key with:value];
}

RCT_EXPORT_METHOD(getCustomSegmentProperty:(NSString *)key callback:(RCTResponseSenderBlock)callback) {
    callback(@[[NSNull null], [FreestarCustomSegmentProperties getCustomSegmentProperty:key]]);
}

RCT_EXPORT_METHOD(getAllCustomSegmentProperties:(RCTResponseSenderBlock)callback) {
    callback(@[[NSNull null], [FreestarCustomSegmentProperties getAllCustomSegmentProperties]]);
}

RCT_EXPORT_METHOD(deleteCustomSegmentProperty:(NSString *)key) {
    [FreestarCustomSegmentProperties deleteCustomSegmentProperty:key];
}

RCT_EXPORT_METHOD(deleteAllCustomSegmentProperties) {
    [FreestarCustomSegmentProperties deleteAllCustomSegmentProperties];
}

#pragma mark - deprecated

RCT_EXPORT_METHOD(setCoppaStatus) {}

RCT_EXPORT_METHOD(setAppInfo:(NSString *)appName
                  publisher:(NSString *)publisher
                  appDomain:(NSString *)appDomain
                  publisherDomain:(NSString *)publisherDomain
                  storeURL:(NSString *)storeURL
                  category:(NSString *)iabCategory
                  ) {
    
}
@end
