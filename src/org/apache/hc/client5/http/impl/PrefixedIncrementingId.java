/*     */ package org.apache.hc.client5.http.impl;
/*     */ 
/*     */ import java.util.concurrent.atomic.AtomicLong;
/*     */ import org.apache.hc.core5.annotation.Internal;
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
/*     */ @Internal
/*     */ public final class PrefixedIncrementingId
/*     */ {
/*  41 */   private final AtomicLong count = new AtomicLong(0L);
/*     */   
/*     */   private final String prefix0;
/*     */   
/*     */   private final String prefix1;
/*     */   
/*     */   private final String prefix2;
/*     */   
/*     */   private final String prefix3;
/*     */   private final String prefix4;
/*     */   private final String prefix5;
/*     */   private final String prefix6;
/*     */   private final String prefix7;
/*     */   private final String prefix8;
/*     */   private final String prefix9;
/*     */   
/*     */   public PrefixedIncrementingId(String prefix) {
/*  58 */     this.prefix0 = (String)Args.notNull(prefix, "prefix");
/*  59 */     this.prefix1 = this.prefix0 + '0';
/*  60 */     this.prefix2 = this.prefix1 + '0';
/*  61 */     this.prefix3 = this.prefix2 + '0';
/*  62 */     this.prefix4 = this.prefix3 + '0';
/*  63 */     this.prefix5 = this.prefix4 + '0';
/*  64 */     this.prefix6 = this.prefix5 + '0';
/*  65 */     this.prefix7 = this.prefix6 + '0';
/*  66 */     this.prefix8 = this.prefix7 + '0';
/*  67 */     this.prefix9 = this.prefix8 + '0';
/*     */   }
/*     */   
/*     */   public long getNextNumber() {
/*  71 */     return this.count.incrementAndGet();
/*     */   }
/*     */   
/*     */   public String getNextId() {
/*  75 */     return createId(this.count.incrementAndGet());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   String createId(long value) {
/*  85 */     String longString = Long.toString(value);
/*  86 */     switch (longString.length()) {
/*     */       case 1:
/*  88 */         return this.prefix9 + longString;
/*     */       case 2:
/*  90 */         return this.prefix8 + longString;
/*     */       case 3:
/*  92 */         return this.prefix7 + longString;
/*     */       case 4:
/*  94 */         return this.prefix6 + longString;
/*     */       case 5:
/*  96 */         return this.prefix5 + longString;
/*     */       case 6:
/*  98 */         return this.prefix4 + longString;
/*     */       case 7:
/* 100 */         return this.prefix3 + longString;
/*     */       case 8:
/* 102 */         return this.prefix2 + longString;
/*     */       case 9:
/* 104 */         return this.prefix1 + longString;
/*     */     } 
/* 106 */     return this.prefix0 + longString;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/PrefixedIncrementingId.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */