/*     */ package org.apache.log4j.helpers;
/*     */ 
/*     */ import org.apache.log4j.spi.LoggingEvent;
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
/*     */ public abstract class PatternConverter
/*     */ {
/*     */   public PatternConverter next;
/*  40 */   int min = -1;
/*  41 */   int max = Integer.MAX_VALUE;
/*     */   
/*     */   boolean leftAlign = false;
/*     */   
/*     */   protected PatternConverter() {}
/*     */   
/*     */   protected PatternConverter(FormattingInfo fi) {
/*  48 */     this.min = fi.min;
/*  49 */     this.max = fi.max;
/*  50 */     this.leftAlign = fi.leftAlign;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract String convert(LoggingEvent paramLoggingEvent);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void format(StringBuffer sbuf, LoggingEvent e) {
/*  63 */     String s = convert(e);
/*     */     
/*  65 */     if (s == null) {
/*  66 */       if (0 < this.min) {
/*  67 */         spacePad(sbuf, this.min);
/*     */       }
/*     */       return;
/*     */     } 
/*  71 */     int len = s.length();
/*     */     
/*  73 */     if (len > this.max) {
/*  74 */       sbuf.append(s.substring(len - this.max));
/*  75 */     } else if (len < this.min) {
/*  76 */       if (this.leftAlign) {
/*  77 */         sbuf.append(s);
/*  78 */         spacePad(sbuf, this.min - len);
/*     */       } else {
/*  80 */         spacePad(sbuf, this.min - len);
/*  81 */         sbuf.append(s);
/*     */       } 
/*     */     } else {
/*  84 */       sbuf.append(s);
/*     */     } 
/*     */   }
/*  87 */   static String[] SPACES = new String[] { " ", "  ", "    ", "        ", "                ", "                                " };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void spacePad(StringBuffer sbuf, int length) {
/*  95 */     while (length >= 32) {
/*  96 */       sbuf.append(SPACES[5]);
/*  97 */       length -= 32;
/*     */     } 
/*     */     
/* 100 */     for (int i = 4; i >= 0; i--) {
/* 101 */       if ((length & 1 << i) != 0)
/* 102 */         sbuf.append(SPACES[i]); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/helpers/PatternConverter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */