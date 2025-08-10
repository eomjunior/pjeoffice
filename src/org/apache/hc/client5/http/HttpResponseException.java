/*    */ package org.apache.hc.client5.http;
/*    */ 
/*    */ import org.apache.hc.core5.util.TextUtils;
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
/*    */ public class HttpResponseException
/*    */   extends ClientProtocolException
/*    */ {
/*    */   private static final long serialVersionUID = -7186627969477257933L;
/*    */   private final int statusCode;
/*    */   private final String reasonPhrase;
/*    */   
/*    */   public HttpResponseException(int statusCode, String reasonPhrase) {
/* 44 */     super(String.format("status code: %d" + (
/* 45 */           TextUtils.isBlank(reasonPhrase) ? "" : ", reason phrase: %s"), new Object[] { Integer.valueOf(statusCode), reasonPhrase }));
/* 46 */     this.statusCode = statusCode;
/* 47 */     this.reasonPhrase = reasonPhrase;
/*    */   }
/*    */   
/*    */   public int getStatusCode() {
/* 51 */     return this.statusCode;
/*    */   }
/*    */   
/*    */   public String getReasonPhrase() {
/* 55 */     return this.reasonPhrase;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/HttpResponseException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */