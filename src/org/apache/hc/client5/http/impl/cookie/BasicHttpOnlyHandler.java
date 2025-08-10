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
/*    */ 
/*    */ @Contract(threading = ThreadingBehavior.STATELESS)
/*    */ public class BasicHttpOnlyHandler
/*    */   implements CommonCookieAttributeHandler
/*    */ {
/* 51 */   public static final BasicHttpOnlyHandler INSTANCE = new BasicHttpOnlyHandler();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void parse(SetCookie cookie, String value) throws MalformedCookieException {
/* 60 */     Args.notNull(cookie, "Cookie");
/* 61 */     cookie.setHttpOnly(true);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void validate(Cookie cookie, CookieOrigin origin) throws MalformedCookieException {}
/*    */ 
/*    */   
/*    */   public boolean match(Cookie cookie, CookieOrigin origin) {
/* 70 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getAttributeName() {
/* 75 */     return "httpOnly";
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/cookie/BasicHttpOnlyHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */