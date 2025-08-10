/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.common.base.Preconditions;
/*    */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @ElementTypesAreNonnullByDefault
/*    */ @GwtCompatible
/*    */ final class CollectPreconditions
/*    */ {
/*    */   static void checkEntryNotNull(Object key, Object value) {
/* 30 */     if (key == null)
/* 31 */       throw new NullPointerException("null key in entry: null=" + value); 
/* 32 */     if (value == null) {
/* 33 */       throw new NullPointerException("null value in entry: " + key + "=null");
/*    */     }
/*    */   }
/*    */   
/*    */   @CanIgnoreReturnValue
/*    */   static int checkNonnegative(int value, String name) {
/* 39 */     if (value < 0) {
/* 40 */       throw new IllegalArgumentException(name + " cannot be negative but was: " + value);
/*    */     }
/* 42 */     return value;
/*    */   }
/*    */   
/*    */   @CanIgnoreReturnValue
/*    */   static long checkNonnegative(long value, String name) {
/* 47 */     if (value < 0L) {
/* 48 */       throw new IllegalArgumentException(name + " cannot be negative but was: " + value);
/*    */     }
/* 50 */     return value;
/*    */   }
/*    */   
/*    */   static void checkPositive(int value, String name) {
/* 54 */     if (value <= 0) {
/* 55 */       throw new IllegalArgumentException(name + " must be positive but was: " + value);
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static void checkRemove(boolean canRemove) {
/* 64 */     Preconditions.checkState(canRemove, "no calls to next() since the last call to remove()");
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/CollectPreconditions.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */