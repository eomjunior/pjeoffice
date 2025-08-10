/*    */ package org.apache.hc.core5.annotation;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum ThreadingBehavior
/*    */ {
/* 38 */   IMMUTABLE,
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 45 */   IMMUTABLE_CONDITIONAL,
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 51 */   STATELESS,
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 56 */   SAFE,
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 62 */   SAFE_CONDITIONAL,
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 67 */   UNSAFE;
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/annotation/ThreadingBehavior.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */