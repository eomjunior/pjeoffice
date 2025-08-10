/*    */ package org.apache.hc.core5.http2.impl.nio.bootstrap;
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
/*    */ final class HandlerEntry<T>
/*    */ {
/*    */   final String hostname;
/*    */   final String uriPattern;
/*    */   final T handler;
/*    */   
/*    */   public HandlerEntry(String hostname, String uriPattern, T handler) {
/* 36 */     this.hostname = hostname;
/* 37 */     this.uriPattern = uriPattern;
/* 38 */     this.handler = handler;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 43 */     StringBuilder builder = new StringBuilder();
/* 44 */     builder.append("HandlerEntry [hostname=");
/* 45 */     builder.append(this.hostname);
/* 46 */     builder.append(", uriPattern=");
/* 47 */     builder.append(this.uriPattern);
/* 48 */     builder.append(", handler=");
/* 49 */     builder.append(this.handler);
/* 50 */     builder.append("]");
/* 51 */     return builder.toString();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http2/impl/nio/bootstrap/HandlerEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */