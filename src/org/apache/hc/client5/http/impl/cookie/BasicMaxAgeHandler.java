/*    */ package org.apache.hc.client5.http.impl.cookie;
/*    */ 
/*    */ import java.time.Instant;
/*    */ import org.apache.hc.client5.http.cookie.CommonCookieAttributeHandler;
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
/*    */ 
/*    */ @Contract(threading = ThreadingBehavior.STATELESS)
/*    */ public class BasicMaxAgeHandler
/*    */   extends AbstractCookieAttributeHandler
/*    */   implements CommonCookieAttributeHandler
/*    */ {
/* 52 */   public static final BasicMaxAgeHandler INSTANCE = new BasicMaxAgeHandler();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void parse(SetCookie cookie, String value) throws MalformedCookieException {
/*    */     int age;
/* 61 */     Args.notNull(cookie, "Cookie");
/* 62 */     if (value == null) {
/* 63 */       throw new MalformedCookieException("Missing value for 'max-age' attribute");
/*    */     }
/*    */     
/*    */     try {
/* 67 */       age = Integer.parseInt(value);
/* 68 */     } catch (NumberFormatException e) {
/* 69 */       throw new MalformedCookieException("Invalid 'max-age' attribute: " + value);
/*    */     } 
/*    */     
/* 72 */     if (age < 0) {
/* 73 */       throw new MalformedCookieException("Negative 'max-age' attribute: " + value);
/*    */     }
/*    */     
/* 76 */     cookie.setExpiryDate(Instant.now().plusSeconds(age));
/*    */   }
/*    */ 
/*    */   
/*    */   public String getAttributeName() {
/* 81 */     return "max-age";
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/cookie/BasicMaxAgeHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */