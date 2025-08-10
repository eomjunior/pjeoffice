/*     */ package org.apache.hc.client5.http.impl.cookie;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.time.Instant;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import org.apache.hc.client5.http.cookie.SetCookie;
/*     */ import org.apache.hc.client5.http.utils.DateUtils;
/*     */ import org.apache.hc.core5.util.Args;
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
/*     */ public final class BasicClientCookie
/*     */   implements SetCookie, Cloneable, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -3869795591041535538L;
/*     */   private final String name;
/*     */   private Map<String, String> attribs;
/*     */   private String value;
/*     */   private String cookieDomain;
/*     */   private Instant cookieExpiryDate;
/*     */   private String cookiePath;
/*     */   private boolean isSecure;
/*     */   private Instant creationDate;
/*     */   private boolean httpOnly;
/*     */   
/*     */   public BasicClientCookie(String name, String value) {
/*  58 */     Args.notNull(name, "Name");
/*  59 */     this.name = name;
/*  60 */     this.attribs = new HashMap<>();
/*  61 */     this.value = value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/*  71 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getValue() {
/*  81 */     return this.value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setValue(String value) {
/*  91 */     this.value = value;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public Date getExpiryDate() {
/* 109 */     return DateUtils.toDate(this.cookieExpiryDate);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Instant getExpiryInstant() {
/* 117 */     return this.cookieExpiryDate;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void setExpiryDate(Date expiryDate) {
/* 134 */     this.cookieExpiryDate = DateUtils.toInstant(expiryDate);
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
/*     */ 
/*     */ 
/*     */   
/*     */   public void setExpiryDate(Instant expiryInstant) {
/* 149 */     this.cookieExpiryDate = expiryInstant;
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
/*     */   
/*     */   public boolean isPersistent() {
/* 162 */     return (null != this.cookieExpiryDate);
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
/*     */   
/*     */   public String getDomain() {
/* 175 */     return this.cookieDomain;
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
/*     */   public void setDomain(String domain) {
/* 187 */     if (domain != null) {
/* 188 */       this.cookieDomain = domain.toLowerCase(Locale.ROOT);
/*     */     } else {
/* 190 */       this.cookieDomain = null;
/*     */     } 
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
/*     */   
/*     */   public String getPath() {
/* 204 */     return this.cookiePath;
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
/*     */   
/*     */   public void setPath(String path) {
/* 217 */     this.cookiePath = path;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSecure() {
/* 226 */     return this.isSecure;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSecure(boolean secure) {
/* 243 */     this.isSecure = secure;
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
/*     */   
/*     */   public void setHttpOnly(boolean httpOnly) {
/* 256 */     this.httpOnly = httpOnly;
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
/*     */   
/*     */   @Deprecated
/*     */   public boolean isExpired(Date date) {
/* 270 */     Args.notNull(date, "Date");
/* 271 */     return (this.cookieExpiryDate != null && this.cookieExpiryDate
/* 272 */       .compareTo(DateUtils.toInstant(date)) <= 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isExpired(Instant instant) {
/* 283 */     Args.notNull(instant, "Instant");
/* 284 */     return (this.cookieExpiryDate != null && this.cookieExpiryDate
/* 285 */       .compareTo(instant) <= 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public Date getCreationDate() {
/* 296 */     return DateUtils.toDate(this.creationDate);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Instant getCreationInstant() {
/* 304 */     return this.creationDate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isHttpOnly() {
/* 314 */     return this.httpOnly;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void setCreationDate(Date creationDate) {
/* 323 */     this.creationDate = DateUtils.toInstant(creationDate);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCreationDate(Instant creationDate) {
/* 330 */     this.creationDate = creationDate;
/*     */   }
/*     */   
/*     */   public void setAttribute(String name, String value) {
/* 334 */     this.attribs.put(name, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getAttribute(String name) {
/* 339 */     return this.attribs.get(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsAttribute(String name) {
/* 344 */     return this.attribs.containsKey(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean removeAttribute(String name) {
/* 351 */     return (this.attribs.remove(name) != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public Object clone() throws CloneNotSupportedException {
/* 356 */     BasicClientCookie clone = (BasicClientCookie)super.clone();
/* 357 */     clone.attribs = new HashMap<>(this.attribs);
/* 358 */     return clone;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 363 */     StringBuilder buffer = new StringBuilder();
/* 364 */     buffer.append("[name: ");
/* 365 */     buffer.append(this.name);
/* 366 */     buffer.append("; ");
/* 367 */     buffer.append("value: ");
/* 368 */     buffer.append(this.value);
/* 369 */     buffer.append("; ");
/* 370 */     buffer.append("domain: ");
/* 371 */     buffer.append(this.cookieDomain);
/* 372 */     buffer.append("; ");
/* 373 */     buffer.append("path: ");
/* 374 */     buffer.append(this.cookiePath);
/* 375 */     buffer.append("; ");
/* 376 */     buffer.append("expiry: ");
/* 377 */     buffer.append(this.cookieExpiryDate);
/* 378 */     buffer.append("]");
/* 379 */     return buffer.toString();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/cookie/BasicClientCookie.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */