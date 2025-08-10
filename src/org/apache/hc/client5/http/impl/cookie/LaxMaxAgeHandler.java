/*    */ package org.apache.hc.client5.http.impl.cookie;
/*    */ 
/*    */ import java.time.Instant;
/*    */ import java.util.regex.Matcher;
/*    */ import java.util.regex.Pattern;
/*    */ import org.apache.hc.client5.http.cookie.CommonCookieAttributeHandler;
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
/*    */ 
/*    */ 
/*    */ @Contract(threading = ThreadingBehavior.STATELESS)
/*    */ public class LaxMaxAgeHandler
/*    */   extends AbstractCookieAttributeHandler
/*    */   implements CommonCookieAttributeHandler
/*    */ {
/* 56 */   public static final LaxMaxAgeHandler INSTANCE = new LaxMaxAgeHandler();
/*    */   
/* 58 */   private static final Pattern MAX_AGE_PATTERN = Pattern.compile("^\\-?[0-9]+$");
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void parse(SetCookie cookie, String value) throws MalformedCookieException {
/* 66 */     Args.notNull(cookie, "Cookie");
/* 67 */     if (TextUtils.isBlank(value)) {
/*    */       return;
/*    */     }
/* 70 */     Matcher matcher = MAX_AGE_PATTERN.matcher(value);
/* 71 */     if (matcher.matches()) {
/*    */       int age;
/*    */       try {
/* 74 */         age = Integer.parseInt(value);
/* 75 */       } catch (NumberFormatException e) {
/*    */         return;
/*    */       } 
/* 78 */       Instant expiryDate = (age >= 0) ? Instant.now().plusSeconds(age) : Instant.EPOCH;
/*    */       
/* 80 */       cookie.setExpiryDate(expiryDate);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public String getAttributeName() {
/* 86 */     return "max-age";
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/cookie/LaxMaxAgeHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */