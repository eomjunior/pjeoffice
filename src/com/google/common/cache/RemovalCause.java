/*    */ package com.google.common.cache;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
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
/*    */ public enum RemovalCause
/*    */ {
/* 36 */   EXPLICIT
/*    */   {
/*    */     boolean wasEvicted() {
/* 39 */       return false;
/*    */     }
/*    */   },
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 49 */   REPLACED
/*    */   {
/*    */     boolean wasEvicted() {
/* 52 */       return false;
/*    */     }
/*    */   },
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 61 */   COLLECTED
/*    */   {
/*    */     boolean wasEvicted() {
/* 64 */       return true;
/*    */     }
/*    */   },
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 72 */   EXPIRED
/*    */   {
/*    */     boolean wasEvicted() {
/* 75 */       return true;
/*    */     }
/*    */   },
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 83 */   SIZE
/*    */   {
/*    */     boolean wasEvicted() {
/* 86 */       return true;
/*    */     }
/*    */   };
/*    */   
/*    */   abstract boolean wasEvicted();
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/cache/RemovalCause.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */