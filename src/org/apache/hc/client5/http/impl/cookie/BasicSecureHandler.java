/*    */ package org.apache.hc.client5.http.impl.cookie;
/*    */ 
/*    */ import org.apache.hc.client5.http.cookie.CommonCookieAttributeHandler;
/*    */ import org.apache.hc.client5.http.cookie.Cookie;
/*    */ import org.apache.hc.client5.http.cookie.CookieOrigin;
/*    */ import org.apache.hc.client5.http.cookie.MalformedCookieException;
/*    */ import org.apache.hc.client5.http.cookie.SetCookie;
/*    */ import org.apache.hc.core5.annotation.Contract;
/*    */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*    */ import org.apache.hc.core5.util.Args;
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
/*    */ @Contract(threading = ThreadingBehavior.STATELESS)
/*    */ public class BasicSecureHandler
/*    */   extends AbstractCookieAttributeHandler
/*    */   implements CommonCookieAttributeHandler
/*    */ {
/* 51 */   public static final BasicSecureHandler INSTANCE = new BasicSecureHandler();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void parse(SetCookie cookie, String value) throws MalformedCookieException {
/* 60 */     Args.notNull(cookie, "Cookie");
/* 61 */     cookie.setSecure(true);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean match(Cookie cookie, CookieOrigin origin) {
/* 66 */     Args.notNull(cookie, "Cookie");
/* 67 */     Args.notNull(origin, "Cookie origin");
/* 68 */     return (!cookie.isSecure() || origin.isSecure());
/*    */   }
/*    */ 
/*    */   
/*    */   public String getAttributeName() {
/* 73 */     return "secure";
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/cookie/BasicSecureHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */