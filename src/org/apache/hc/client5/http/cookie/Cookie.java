/*     */ package org.apache.hc.client5.http.cookie;
/*     */ 
/*     */ import java.time.Instant;
/*     */ import java.util.Date;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface Cookie
/*     */ {
/*     */   public static final String PATH_ATTR = "path";
/*     */   public static final String DOMAIN_ATTR = "domain";
/*     */   public static final String MAX_AGE_ATTR = "max-age";
/*     */   public static final String SECURE_ATTR = "secure";
/*     */   public static final String EXPIRES_ATTR = "expires";
/*     */   public static final String HTTP_ONLY_ATTR = "httpOnly";
/*     */   
/*     */   String getAttribute(String paramString);
/*     */   
/*     */   boolean containsAttribute(String paramString);
/*     */   
/*     */   String getName();
/*     */   
/*     */   String getValue();
/*     */   
/*     */   @Deprecated
/*     */   Date getExpiryDate();
/*     */   
/*     */   default Instant getExpiryInstant() {
/*  97 */     Date date = getExpiryDate();
/*  98 */     return (date != null) ? Instant.ofEpochMilli(date.getTime()) : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean isPersistent();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   String getDomain();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   String getPath();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean isSecure();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   boolean isExpired(Date paramDate);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default boolean isExpired(Instant date) {
/* 154 */     return isExpired((date != null) ? new Date(date.toEpochMilli()) : null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   Date getCreationDate();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default Instant getCreationInstant() {
/* 167 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default boolean isHttpOnly() {
/* 179 */     return false;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/cookie/Cookie.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */