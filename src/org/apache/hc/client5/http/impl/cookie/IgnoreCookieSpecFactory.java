/*    */ package org.apache.hc.client5.http.impl.cookie;
/*    */ 
/*    */ import org.apache.hc.client5.http.cookie.CookieSpec;
/*    */ import org.apache.hc.client5.http.cookie.CookieSpecFactory;
/*    */ import org.apache.hc.core5.annotation.Contract;
/*    */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*    */ import org.apache.hc.core5.http.protocol.HttpContext;
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
/*    */ @Contract(threading = ThreadingBehavior.SAFE)
/*    */ public class IgnoreCookieSpecFactory
/*    */   implements CookieSpecFactory
/*    */ {
/*    */   private volatile CookieSpec cookieSpec;
/*    */   
/*    */   public CookieSpec create(HttpContext context) {
/* 52 */     if (this.cookieSpec == null) {
/* 53 */       synchronized (this) {
/* 54 */         if (this.cookieSpec == null) {
/* 55 */           this.cookieSpec = IgnoreSpecSpec.INSTANCE;
/*    */         }
/*    */       } 
/*    */     }
/* 59 */     return this.cookieSpec;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/cookie/IgnoreCookieSpecFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */