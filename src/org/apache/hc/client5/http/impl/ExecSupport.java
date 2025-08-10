/*    */ package org.apache.hc.client5.http.impl;
/*    */ 
/*    */ import org.apache.hc.core5.annotation.Internal;
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
/*    */ @Internal
/*    */ public final class ExecSupport
/*    */ {
/* 39 */   private static final PrefixedIncrementingId INCREMENTING_ID = new PrefixedIncrementingId("ex-");
/*    */   
/*    */   public static long getNextExecNumber() {
/* 42 */     return INCREMENTING_ID.getNextNumber();
/*    */   }
/*    */   
/*    */   public static String getNextExchangeId() {
/* 46 */     return INCREMENTING_ID.getNextId();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/ExecSupport.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */