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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Contract(threading = ThreadingBehavior.STATELESS)
/*    */ public class BasicPathHandler
/*    */   implements CommonCookieAttributeHandler
/*    */ {
/* 52 */   public static final BasicPathHandler INSTANCE = new BasicPathHandler();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void parse(SetCookie cookie, String value) throws MalformedCookieException {
/* 61 */     Args.notNull(cookie, "Cookie");
/* 62 */     cookie.setPath(!TextUtils.isBlank(value) ? value : "/");
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void validate(Cookie cookie, CookieOrigin origin) throws MalformedCookieException {}
/*    */ 
/*    */   
/*    */   static boolean pathMatch(String uriPath, String cookiePath) {
/* 71 */     String normalizedCookiePath = cookiePath;
/* 72 */     if (normalizedCookiePath == null) {
/* 73 */       normalizedCookiePath = "/";
/*    */     }
/* 75 */     if (normalizedCookiePath.length() > 1 && normalizedCookiePath.endsWith("/")) {
/* 76 */       normalizedCookiePath = normalizedCookiePath.substring(0, normalizedCookiePath.length() - 1);
/*    */     }
/* 78 */     if (uriPath.startsWith(normalizedCookiePath)) {
/* 79 */       if (normalizedCookiePath.equals("/")) {
/* 80 */         return true;
/*    */       }
/* 82 */       if (uriPath.length() == normalizedCookiePath.length()) {
/* 83 */         return true;
/*    */       }
/* 85 */       return (uriPath.charAt(normalizedCookiePath.length()) == '/');
/*    */     } 
/* 87 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean match(Cookie cookie, CookieOrigin origin) {
/* 92 */     Args.notNull(cookie, "Cookie");
/* 93 */     Args.notNull(origin, "Cookie origin");
/* 94 */     return pathMatch(origin.getPath(), cookie.getPath());
/*    */   }
/*    */ 
/*    */   
/*    */   public String getAttributeName() {
/* 99 */     return "path";
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/cookie/BasicPathHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */