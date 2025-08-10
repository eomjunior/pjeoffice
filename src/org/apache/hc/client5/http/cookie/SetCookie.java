/*    */ package org.apache.hc.client5.http.cookie;
/*    */ 
/*    */ import java.time.Instant;
/*    */ import java.util.Date;
/*    */ import org.apache.hc.client5.http.utils.DateUtils;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface SetCookie
/*    */   extends Cookie
/*    */ {
/*    */   void setValue(String paramString);
/*    */   
/*    */   @Deprecated
/*    */   void setExpiryDate(Date paramDate);
/*    */   
/*    */   default void setExpiryDate(Instant expiryDate) {
/* 71 */     setExpiryDate(DateUtils.toDate(expiryDate));
/*    */   }
/*    */   
/*    */   void setDomain(String paramString);
/*    */   
/*    */   void setPath(String paramString);
/*    */   
/*    */   void setSecure(boolean paramBoolean);
/*    */   
/*    */   default void setHttpOnly(boolean httpOnly) {}
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/cookie/SetCookie.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */