//
//  FreestarBannerAdViewManager.m
//  CocoaAsyncSocket
//
//  Created by Vdopia Developer on 6/30/20.
//

#import "FreestarBannerAdViewManager.h"
@import FreestarAds;

//@interface FreestarBannerAd (ReactBridge)
//-(void)setRequestOptions:(NSDictionary *)options;
//@end

static NSString* EVENT_AD_LOADED = @"onBannerAdLoaded";
static NSString* EVENT_AD_FAILED_TO_LOAD = @"onBannerAdFailedToLoad";
static NSString* EVENT_AD_CLICKED = @"onBannerAdClicked";

@interface FreestarBannerAdViewManager () <FreestarBannerAdDelegate>

@property FreestarBannerAd *ad;
@property RCTBubblingEventBlock loadedCallback;
@property RCTBubblingEventBlock loadFailedCallback;
@property RCTBubblingEventBlock clickedCallback;

@end

@implementation FreestarBannerAd (ReactBridge)

-(void)setRequestOptions:(NSDictionary *)options {
    NSString *sizeKey = options[@"size"];
    if(sizeKey && ![[sizeKey uppercaseString] isEqualToString:@"MREC"]) {
        if([[sizeKey lowercaseString] isEqualToString:@"banner"]) {
            self.size = FreestarBanner320x50;
        } else if([[sizeKey lowercaseString] isEqualToString:@"leaderboard"]) {
            self.size = FreestarBanner728x90;
        }
        [self resetToSize];
    }
    
    NSDictionary *targetingParams = options[@"targetingParams"];
    if(targetingParams && targetingParams.count > 0) {
        [targetingParams enumerateKeysAndObjectsUsingBlock:^(NSString *key, NSString* obj, BOOL * _Nonnull stop) {
            [self addCustomTargeting:key as:obj];
        }];
    }
    
    [self loadPlacement:nil];
}

-(void)resetToSize {
    CGRect newFrame;
    switch(self.size){
        case FreestarBanner320x50:
            newFrame = CGRectMake(0, 0, 320, 50);
            break;
        case FreestarBanner728x90:
            newFrame = CGRectMake(0, 0, 728, 90);
            break;
        case FreestarBanner300x250:
            newFrame = CGRectMake(0, 0, 300, 250);
            break;
        case FreestarBanner320x480:
            return;
            //newFrame = self.frame; //no banners of this size
    }
    
    CGPoint oldCenter = self.center;
    self.frame = newFrame;
    self.center = oldCenter;
        
    [self setValue:@(self.size) forKeyPath:@"ad.size"];
}

-(void)setOnBannerAdLoaded:(RCTBubblingEventBlock)loadedBlock {
    ((FreestarBannerAdViewManager *)self.delegate).loadedCallback = loadedBlock;
}

-(void)setOnBannerAdFailedToLoad:(RCTBubblingEventBlock)failedBlock {
    ((FreestarBannerAdViewManager *)self.delegate).loadFailedCallback = failedBlock;
}

-(void)setOnBannerAdClicked:(RCTBubblingEventBlock)clickedBlock {
    ((FreestarBannerAdViewManager *)self.delegate).clickedCallback = clickedBlock;
}

@end



@implementation FreestarBannerAdViewManager

RCT_EXPORT_MODULE(BannerAd);

RCT_EXPORT_VIEW_PROPERTY(requestOptions, NSDictionary);

RCT_EXPORT_VIEW_PROPERTY(onBannerAdLoaded, RCTBubblingEventBlock);


- (UIView *)view {
    self.ad = [[FreestarBannerAd alloc] initWithDelegate:self andSize:FreestarBanner300x250];
    return self.ad;
}

- (dispatch_queue_t)methodQueue {
  return dispatch_get_main_queue();
}

- (void)sendEvent:(RCTBubblingEventBlock)callback type:(NSString *)type payload:(NSDictionary *_Nullable)payload {
  if (!callback) {
    return;
  }

  NSMutableDictionary *event = [@{
      @"type": type,
  } mutableCopy];

  if (payload != nil) {
    [event addEntriesFromDictionary:payload];
  }

  callback(event);
}

#pragma mark - FreestarBannerAdDelegate



-(void)freestarBannerLoaded:(FreestarBannerAd *)ad {
    [self sendEvent:self.loadedCallback type:EVENT_AD_LOADED payload:@{}];
}

-(void)freestarBannerFailed:(FreestarBannerAd *)ad because:(FreestarNoAdReason)reason {
    [self sendEvent:self.loadFailedCallback type:EVENT_AD_FAILED_TO_LOAD payload:@{
        @"errorCode" : @(reason)
    }];
}

-(void)freestarBannerShown:(FreestarBannerAd *)ad {
    
}

-(void)freestarBannerClicked:(FreestarBannerAd *)ad {
    [self sendEvent:self.clickedCallback type:EVENT_AD_CLICKED payload:@{}];
}

-(void)freestarBannerClosed:(FreestarBannerAd *)ad {
    
}

@end
