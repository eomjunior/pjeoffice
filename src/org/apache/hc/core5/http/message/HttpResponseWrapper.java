/*    */ package org.apache.hc.core5.http.message;
/*    */ 
/*    */ import java.util.Locale;
/*    */ import org.apache.hc.core5.http.HttpMessage;
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
/*    */ public class HttpResponseWrapper
/*    */   extends AbstractMessageWrapper
/*    */   implements HttpResponse
/*    */ {
/*    */   private final HttpResponse message;
/*    */   
/*    */   public HttpResponseWrapper(HttpResponse message) {
/* 42 */     super((HttpMessage)message);
/* 43 */     this.message = message;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getCode() {
/* 48 */     return this.message.getCode();
/*    */   }
/*    */ 
/*    */   
/*    */   public void setCode(int code) {
/* 53 */     this.message.setCode(code);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getReasonPhrase() {
/* 58 */     return this.message.getReasonPhrase();
/*    */   }
/*    */ 
/*    */   
/*    */   public void setReasonPhrase(String reason) {
/* 63 */     this.message.setReasonPhrase(reason);
/*    */   }
/*    */ 
/*    */   
/*    */   public Locale getLocale() {
/* 68 */     return this.message.getLocale();
/*    */   }
/*    */ 
/*    */   
/*    */   public void setLocale(Locale loc) {
/* 73 */     this.message.setLocale(loc);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/message/HttpResponseWrapper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */