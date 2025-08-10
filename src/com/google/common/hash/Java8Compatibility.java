/*    */ package com.google.common.hash;
/*    */ 
/*    */ import com.google.common.annotations.GwtIncompatible;
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
/*    */ @GwtIncompatible
/*    */ final class Java8Compatibility
/*    */ {
/*    */   static void clear(Buffer b) {
/* 28 */     b.clear();
/*    */   }
/*    */   
/*    */   static void flip(Buffer b) {
/* 32 */     b.flip();
/*    */   }
/*    */   
/*    */   static void limit(Buffer b, int limit) {
/* 36 */     b.limit(limit);
/*    */   }
/*    */   
/*    */   static void position(Buffer b, int position) {
/* 40 */     b.position(position);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/hash/Java8Compatibility.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */