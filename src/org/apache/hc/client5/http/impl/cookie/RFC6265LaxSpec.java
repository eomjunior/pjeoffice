/*    */ package org.apache.hc.client5.http.impl.cookie;
/*    */ 
/*    */ import org.apache.hc.client5.http.cookie.CommonCookieAttributeHandler;
/*    */ import org.apache.hc.core5.annotation.Contract;
/*    */ import org.apache.hc.core5.annotation.ThreadingBehavior;
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
/*    */ 
/*    */ @Contract(threading = ThreadingBehavior.SAFE)
/*    */ public class RFC6265LaxSpec
/*    */   extends RFC6265CookieSpecBase
/*    */ {
/*    */   public RFC6265LaxSpec() {
/* 46 */     super(new CommonCookieAttributeHandler[] { BasicPathHandler.INSTANCE, BasicDomainHandler.INSTANCE, LaxMaxAgeHandler.INSTANCE, BasicSecureHandler.INSTANCE, BasicHttpOnlyHandler.INSTANCE, LaxExpiresHandler.INSTANCE });
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   RFC6265LaxSpec(CommonCookieAttributeHandler... handlers) {
/* 55 */     super(handlers);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 60 */     return "rfc6265-lax";
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/cookie/RFC6265LaxSpec.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */