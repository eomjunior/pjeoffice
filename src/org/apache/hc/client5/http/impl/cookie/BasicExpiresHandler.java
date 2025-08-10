/*    */ package org.apache.hc.client5.http.impl.cookie;
/*    */ 
/*    */ import java.time.Instant;
/*    */ import java.time.format.DateTimeFormatter;
/*    */ import java.time.format.DateTimeFormatterBuilder;
/*    */ import org.apache.hc.client5.http.cookie.CommonCookieAttributeHandler;
/*    */ import org.apache.hc.client5.http.cookie.MalformedCookieException;
/*    */ import org.apache.hc.client5.http.cookie.SetCookie;
/*    */ import org.apache.hc.client5.http.utils.DateUtils;
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
/*    */ public class BasicExpiresHandler
/*    */   extends AbstractCookieAttributeHandler
/*    */   implements CommonCookieAttributeHandler
/*    */ {
/*    */   private final DateTimeFormatter[] datePatterns;
/*    */   
/*    */   public BasicExpiresHandler(DateTimeFormatter... datePatterns) {
/* 57 */     this.datePatterns = datePatterns;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Deprecated
/*    */   public BasicExpiresHandler(String[] datePatterns) {
/* 65 */     Args.notNull(datePatterns, "Array of date patterns");
/* 66 */     this.datePatterns = new DateTimeFormatter[datePatterns.length];
/* 67 */     for (int i = 0; i < datePatterns.length; i++) {
/* 68 */       this.datePatterns[i] = (new DateTimeFormatterBuilder())
/* 69 */         .parseLenient()
/* 70 */         .parseCaseInsensitive()
/* 71 */         .appendPattern(datePatterns[i])
/* 72 */         .toFormatter();
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void parse(SetCookie cookie, String value) throws MalformedCookieException {
/* 80 */     Args.notNull(cookie, "Cookie");
/* 81 */     if (value == null) {
/* 82 */       throw new MalformedCookieException("Missing value for 'expires' attribute");
/*    */     }
/* 84 */     Instant expiry = DateUtils.parseDate(value, this.datePatterns);
/* 85 */     if (expiry == null) {
/* 86 */       throw new MalformedCookieException("Invalid 'expires' attribute: " + value);
/*    */     }
/*    */     
/* 89 */     cookie.setExpiryDate(expiry);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getAttributeName() {
/* 94 */     return "expires";
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/cookie/BasicExpiresHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */