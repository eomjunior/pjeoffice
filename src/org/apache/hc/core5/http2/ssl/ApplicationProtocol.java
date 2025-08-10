/*    */ package org.apache.hc.core5.http2.ssl;
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
/*    */ public enum ApplicationProtocol
/*    */ {
/* 37 */   HTTP_2("h2"), HTTP_1_1("http/1.1");
/*    */   
/*    */   public final String id;
/*    */   
/*    */   ApplicationProtocol(String id) {
/* 42 */     this.id = id;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 47 */     return this.id;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http2/ssl/ApplicationProtocol.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */