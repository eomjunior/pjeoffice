/*    */ package org.apache.hc.client5.http.cookie;
/*    */ 
/*    */ import java.time.Instant;
/*    */ import java.util.Comparator;
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
/*    */ @Contract(threading = ThreadingBehavior.STATELESS)
/*    */ public class CookiePriorityComparator
/*    */   implements Comparator<Cookie>
/*    */ {
/* 46 */   public static final CookiePriorityComparator INSTANCE = new CookiePriorityComparator();
/*    */   
/*    */   private int getPathLength(Cookie cookie) {
/* 49 */     String path = cookie.getPath();
/* 50 */     return (path != null) ? path.length() : 1;
/*    */   }
/*    */ 
/*    */   
/*    */   public int compare(Cookie c1, Cookie c2) {
/* 55 */     int l1 = getPathLength(c1);
/* 56 */     int l2 = getPathLength(c2);
/* 57 */     int result = l2 - l1;
/* 58 */     if (result == 0) {
/* 59 */       Instant d1 = c1.getCreationInstant();
/* 60 */       Instant d2 = c2.getCreationInstant();
/* 61 */       if (d1 != null && d2 != null) {
/* 62 */         return d1.compareTo(d2);
/*    */       }
/*    */     } 
/* 65 */     return result;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/cookie/CookiePriorityComparator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */