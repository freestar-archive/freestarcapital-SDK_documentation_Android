//
//  FreestarReactBridge.h
//  FreestarPlatformReactNativePlugin
//
//  Created by Lev Trubov on 6/25/20.
//  Copyright © 2020 Freestar. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <React-Core/React/RCTBridgeModule.h>
#import <React-Core/React/RCTEventEmitter.h>

@interface FreestarReactBridge : RCTEventEmitter <RCTBridgeModule>

@end
