//
//  CSGDecodec.h
//  Cross Stitch Library
//
//  Created by 123 on 28.04.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

#import <Foundation/Foundation.h>

@class CSGThread;
@class CSGObjectsRegistry;
@class CSGThreadInBlend;
@class CSGThreadsBlend;
@class CSGDesignCell;

@interface CSGDecodec : NSObject

//initialization
-(id) initWithData: (NSData*) data;

//deserialization
- (CSGThread*) deserializeThread;
- (CSGThreadInBlend*) deserializeThreadInBlend;
- (CSGThreadsBlend*) deserializeThreadsBlend;
- (CSGDesignCell*) deserializeDesignCell;

@end
