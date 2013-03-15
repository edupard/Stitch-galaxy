//
//  ThreadMaterialCollection.m
//  Cross Stitch Library
//
//  Created by 123 on 27.02.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

#import "CSGThreadsPalette.h"

@interface CSGThreadsPalette ()
{
    NSHashTable* threadMaterials;
    NSMapTable* threadMaterialsToIndexMap;
    NSMapTable* indexToThreadMaterialMap;
    uint32_t freeIndex;
}

@end

@implementation CSGThreadsPalette

- (id) init
{
    if (self = [super init])
    {
        return [self initWithCapacity: 0];
    }
    return self;
}

- (id) initWithCapacity: (NSUInteger) aCapacity
{
  if (self = [super init])
  {
      threadMaterials = [[NSHashTable alloc] initWithOptions:NSPointerFunctionsStrongMemory capacity:aCapacity];
      threadMaterialsToIndexMap = [[NSMapTable alloc] initWithKeyOptions: NSMapTableWeakMemory  valueOptions: NSMapTableWeakMemory capacity:aCapacity];
      indexToThreadMaterialMap = [[NSMapTable alloc] initWithKeyOptions: NSMapTableWeakMemory  valueOptions: NSMapTableWeakMemory capacity:aCapacity];
      freeIndex = 0;
  }
    return self;
}

- (uint32_t) size
{
    return [threadMaterials count];
}

- (CSGThread *) threadMaterialByColor: (UIColor *) color
{
    CSGThread* aThreadMaterial = [[CSGThread alloc] initWithColor:color];
    if ([threadMaterials containsObject:aThreadMaterial])
    {
        for(CSGThread* aThMat in threadMaterials)
        {
            if ([aThMat isEqual:aThreadMaterial])
            {
                return aThMat;
            }
        }
    }
    [self AddThreadMaterial:aThreadMaterial WithIndex:freeIndex];
    
    return aThreadMaterial;
}

- (void) AddThreadMaterial: (CSGThread *) aThreadMaterial WithIndex: (uint32_t) index
{
    [threadMaterials addObject:aThreadMaterial];
    
    NSNumber* nIndex = [NSNumber numberWithUnsignedInteger:index];
    [threadMaterialsToIndexMap setObject: nIndex forKey:aThreadMaterial];
    [indexToThreadMaterialMap setObject:aThreadMaterial forKey:nIndex];
    
    if (index >= freeIndex)
    {
        freeIndex = (index + 1);
    }
}


@end

@implementation CSGThreadsPalette (Serialization)

- (uint32_t) threadIndex: (CSGThread *) aThreadMaterial
{
    NSNumber* nIndex = [threadMaterialsToIndexMap objectForKey:aThreadMaterial];
    if (nIndex)
    {
        return nIndex.unsignedIntegerValue;
    }
    [NSException raise:@"Can not find index by threadMaterial" format:nil];
    return 0;
}

- (CSGThread *) threadAtIndex: (uint32_t) anIndex
{
    NSNumber* nIndex = [NSNumber numberWithUnsignedInteger:anIndex];
    CSGThread* ret = [indexToThreadMaterialMap objectForKey:nIndex];
    if (ret)
    {
        return ret;
    }
    [NSException raise:@"Can not find threadMaterial by index" format:nil];
    return nil;
}

- (size_t) serializedLength
{
    size_t size = sizeof(uint32_t);
    for(CSGThread* aThMat in threadMaterials)
    {
        size += [aThMat serializedLength];
        size += sizeof(uint32_t);
    }
    return size;
}

- (void) serializeWithBinaryEncoder: (CSGBinaryEncoder *) anEncoder
{
    uint32_t *buf = [anEncoder modifyBytes:sizeof(uint32_t)];
    *buf = threadMaterials.count;
    
    for(CSGThread *tMat in threadMaterials)
    {
        uint32_t *indexBuf = [anEncoder modifyBytes:sizeof(uint32_t)];
        *indexBuf = [self threadIndex: tMat];
        
        ++indexBuf;
        void *voidBuf = (void *)indexBuf;
        
        [tMat serializeToBuffer:voidBuf];
        [anEncoder modifyBytes:[tMat serializedLength]];
    }
}

- (id) initWithBinaryDecoder: (CSGBinaryDecoder*) anDecoder
{
    if (self = [super init])
    {
        const uint32_t *buf = [anDecoder readBytes:sizeof(uint32_t)];
        uint32_t length = *buf;
        
        self = [self initWithCapacity:length];
        
        for (uint32_t i = 0; i < length; ++i)
        {
            buf = [anDecoder readBytes:sizeof(uint32_t)];
            uint32_t index = *buf;
            
            ++buf;
            void *voidBuf = (void *) buf;
            CSGThread* tMat = [CSGThread deserializeFromBuffer:voidBuf];
            
            [anDecoder readBytes:[tMat serializedLength]];
            
            [self AddThreadMaterial:tMat WithIndex:index];
        }

    }
    return self;
}

@end
