/*    */ package org.apache.hc.client5.http.impl.cookie;
/*    */ 
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import org.apache.hc.client5.http.cookie.Cookie;
/*    */ import org.apache.hc.client5.http.cookie.CookieOrigin;
/*    */ import org.apache.hc.client5.http.cookie.MalformedCookieException;
/*    */ import org.apache.hc.core5.annotation.Contract;
/*    */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*    */ import org.apache.hc.core5.http.Header;
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
/*    */ 
/*    */ 
/*    */ @Contract(threading = ThreadingBehavior.STATELESS)
/*    */ public class IgnoreSpecSpec
/*    */   extends CookieSpecBase
/*    */ {
/* 53 */   public static final IgnoreSpecSpec INSTANCE = new IgnoreSpecSpec();
/*    */ 
/*    */ 
/*    */   
/*    */   public List<Cookie> parse(Header header, CookieOrigin origin) throws MalformedCookieException {
/* 58 */     return Collections.emptyList();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean match(Cookie cookie, CookieOrigin origin) {
/* 63 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public List<Header> formatCookies(List<Cookie> cookies) {
/* 68 */     return Collections.emptyList();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/cookie/IgnoreSpecSpec.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */