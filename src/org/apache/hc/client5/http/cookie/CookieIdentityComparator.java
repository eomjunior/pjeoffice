/*    */ package org.apache.hc.client5.http.cookie;
/*    */ 
/*    */ import java.io.Serializable;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Contract(threading = ThreadingBehavior.STATELESS)
/*    */ public class CookieIdentityComparator
/*    */   implements Serializable, Comparator<Cookie>
/*    */ {
/* 53 */   public static final CookieIdentityComparator INSTANCE = new CookieIdentityComparator();
/*    */   
/*    */   private static final long serialVersionUID = 4466565437490631532L;
/*    */ 
/*    */   
/*    */   public int compare(Cookie c1, Cookie c2) {
/* 59 */     int res = c1.getName().compareTo(c2.getName());
/* 60 */     if (res == 0) {
/*    */       
/* 62 */       String d1 = c1.getDomain();
/* 63 */       if (d1 == null) {
/* 64 */         d1 = "";
/*    */       }
/* 66 */       String d2 = c2.getDomain();
/* 67 */       if (d2 == null) {
/* 68 */         d2 = "";
/*    */       }
/* 70 */       res = d1.compareToIgnoreCase(d2);
/*    */     } 
/* 72 */     if (res == 0) {
/* 73 */       String p1 = c1.getPath();
/* 74 */       if (p1 == null) {
/* 75 */         p1 = "/";
/*    */       }
/* 77 */       String p2 = c2.getPath();
/* 78 */       if (p2 == null) {
/* 79 */         p2 = "/";
/*    */       }
/* 81 */       res = p1.compareTo(p2);
/*    */     } 
/* 83 */     return res;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/cookie/CookieIdentityComparator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */