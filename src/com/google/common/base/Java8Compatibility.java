/*    */ package com.google.common.base;
/*    */ 
/*    */ import com.google.common.annotations.GwtIncompatible;
/*    */ import com.google.common.annotations.J2ktIncompatible;
/*    */ import java.nio.Buffer;
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
/*    */ @J2ktIncompatible
/*    */ @GwtIncompatible
/*    */ final class Java8Compatibility
/*    */ {
/*    */   static void clear(Buffer b) {
/* 30 */     b.clear();
/*    */   }
/*    */   
/*    */   static void flip(Buffer b) {
/* 34 */     b.flip();
/*    */   }
/*    */   
/*    */   static void limit(Buffer b, int limit) {
/* 38 */     b.limit(limit);
/*    */   }
/*    */   
/*    */   static void position(Buffer b, int position) {
/* 42 */     b.position(position);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/base/Java8Compatibility.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */