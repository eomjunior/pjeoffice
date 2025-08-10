/*    */ package org.apache.hc.client5.http.cookie;
/*    */ 
/*    */ import java.time.Instant;
/*    */ import java.util.Date;
/*    */ import java.util.List;
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
/*    */ public interface CookieStore
/*    */ {
/*    */   void addCookie(Cookie paramCookie);
/*    */   
/*    */   List<Cookie> getCookies();
/*    */   
/*    */   @Deprecated
/*    */   boolean clearExpired(Date paramDate);
/*    */   
/*    */   default boolean clearExpired(Instant date) {
/* 75 */     return clearExpired((date != null) ? new Date(date.toEpochMilli()) : null);
/*    */   }
/*    */   
/*    */   void clear();
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/cookie/CookieStore.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */