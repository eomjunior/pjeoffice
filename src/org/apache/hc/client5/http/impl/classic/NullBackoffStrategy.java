/*    */ package org.apache.hc.client5.http.impl.classic;
/*    */ 
/*    */ import org.apache.hc.client5.http.classic.ConnectionBackoffStrategy;
/*    */ import org.apache.hc.core5.http.HttpResponse;
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
/*    */ public class NullBackoffStrategy
/*    */   implements ConnectionBackoffStrategy
/*    */ {
/*    */   public boolean shouldBackoff(Throwable t) {
/* 42 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean shouldBackoff(HttpResponse response) {
/* 47 */     return false;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/classic/NullBackoffStrategy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */