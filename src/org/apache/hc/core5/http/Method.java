/*     */ package org.apache.hc.core5.http;
/*     */ 
/*     */ import java.util.Locale;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public enum Method
/*     */ {
/*  41 */   GET(true, true),
/*  42 */   HEAD(true, true),
/*  43 */   POST(false, false),
/*  44 */   PUT(false, true),
/*  45 */   DELETE(false, true),
/*  46 */   CONNECT(false, false),
/*  47 */   TRACE(true, true),
/*  48 */   OPTIONS(true, true),
/*  49 */   PATCH(false, false);
/*     */   
/*     */   private final boolean safe;
/*     */   private final boolean idempotent;
/*     */   
/*     */   Method(boolean safe, boolean idempotent) {
/*  55 */     this.safe = safe;
/*  56 */     this.idempotent = idempotent;
/*     */   }
/*     */   
/*     */   public boolean isSafe() {
/*  60 */     return this.safe;
/*     */   }
/*     */   
/*     */   public boolean isIdempotent() {
/*  64 */     return this.idempotent;
/*     */   }
/*     */   
/*     */   public static boolean isSafe(String value) {
/*  68 */     if (value == null) {
/*  69 */       return false;
/*     */     }
/*     */     try {
/*  72 */       return (normalizedValueOf(value)).safe;
/*  73 */     } catch (IllegalArgumentException ex) {
/*  74 */       return false;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static boolean isIdempotent(String value) {
/*  79 */     if (value == null) {
/*  80 */       return false;
/*     */     }
/*     */     try {
/*  83 */       return (normalizedValueOf(value)).idempotent;
/*  84 */     } catch (IllegalArgumentException ex) {
/*  85 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Method normalizedValueOf(String method) {
/*  96 */     return valueOf(((String)Args.notNull(method, "method")).toUpperCase(Locale.ROOT));
/*     */   }
/*     */   
/*     */   public boolean isSame(String value) {
/* 100 */     if (value == null) {
/* 101 */       return false;
/*     */     }
/* 103 */     return name().equalsIgnoreCase(value);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/Method.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */