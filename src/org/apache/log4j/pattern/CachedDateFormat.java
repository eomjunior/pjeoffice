/*     */ package org.apache.log4j.pattern;
/*     */ 
/*     */ import java.text.DateFormat;
/*     */ import java.text.FieldPosition;
/*     */ import java.text.NumberFormat;
/*     */ import java.text.ParsePosition;
/*     */ import java.util.Date;
/*     */ import java.util.TimeZone;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class CachedDateFormat
/*     */   extends DateFormat
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   public static final int NO_MILLISECONDS = -2;
/*     */   private static final String DIGITS = "0123456789";
/*     */   public static final int UNRECOGNIZED_MILLISECONDS = -1;
/*     */   private static final int MAGIC1 = 654;
/*     */   private static final String MAGICSTRING1 = "654";
/*     */   private static final int MAGIC2 = 987;
/*     */   private static final String MAGICSTRING2 = "987";
/*     */   private static final String ZERO_STRING = "000";
/*     */   private final DateFormat formatter;
/*     */   private int millisecondStart;
/*     */   private long slotBegin;
/* 101 */   private StringBuffer cache = new StringBuffer(50);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final int expiration;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private long previousTime;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 118 */   private final Date tmpDate = new Date(0L);
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
/*     */   public CachedDateFormat(DateFormat dateFormat, int expiration) {
/* 130 */     if (dateFormat == null) {
/* 131 */       throw new IllegalArgumentException("dateFormat cannot be null");
/*     */     }
/*     */     
/* 134 */     if (expiration < 0) {
/* 135 */       throw new IllegalArgumentException("expiration must be non-negative");
/*     */     }
/*     */     
/* 138 */     this.formatter = dateFormat;
/* 139 */     this.expiration = expiration;
/* 140 */     this.millisecondStart = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 145 */     this.previousTime = Long.MIN_VALUE;
/* 146 */     this.slotBegin = Long.MIN_VALUE;
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
/*     */   public static int findMillisecondStart(long time, String formatted, DateFormat formatter) {
/* 160 */     long slotBegin = time / 1000L * 1000L;
/*     */     
/* 162 */     if (slotBegin > time) {
/* 163 */       slotBegin -= 1000L;
/*     */     }
/*     */     
/* 166 */     int millis = (int)(time - slotBegin);
/*     */     
/* 168 */     int magic = 654;
/* 169 */     String magicString = "654";
/*     */     
/* 171 */     if (millis == 654) {
/* 172 */       magic = 987;
/* 173 */       magicString = "987";
/*     */     } 
/*     */     
/* 176 */     String plusMagic = formatter.format(new Date(slotBegin + magic));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 182 */     if (plusMagic.length() != formatted.length()) {
/* 183 */       return -1;
/*     */     }
/*     */     
/* 186 */     for (int i = 0; i < formatted.length(); i++) {
/* 187 */       if (formatted.charAt(i) != plusMagic.charAt(i)) {
/*     */ 
/*     */         
/* 190 */         StringBuffer formattedMillis = new StringBuffer("ABC");
/* 191 */         millisecondFormat(millis, formattedMillis, 0);
/*     */         
/* 193 */         String plusZero = formatter.format(new Date(slotBegin));
/*     */ 
/*     */ 
/*     */         
/* 197 */         if (plusZero.length() == formatted.length() && magicString
/* 198 */           .regionMatches(0, plusMagic, i, magicString.length()) && formattedMillis
/* 199 */           .toString().regionMatches(0, formatted, i, magicString.length()) && "000"
/* 200 */           .regionMatches(0, plusZero, i, "000".length())) {
/* 201 */           return i;
/*     */         }
/* 203 */         return -1;
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 209 */     return -2;
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
/*     */   public StringBuffer format(Date date, StringBuffer sbuf, FieldPosition fieldPosition) {
/* 221 */     format(date.getTime(), sbuf);
/*     */     
/* 223 */     return sbuf;
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
/*     */   public StringBuffer format(long now, StringBuffer buf) {
/* 238 */     if (now == this.previousTime) {
/* 239 */       buf.append(this.cache);
/*     */       
/* 241 */       return buf;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 248 */     if (this.millisecondStart != -1 && now < this.slotBegin + this.expiration && now >= this.slotBegin && now < this.slotBegin + 1000L) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 256 */       if (this.millisecondStart >= 0) {
/* 257 */         millisecondFormat((int)(now - this.slotBegin), this.cache, this.millisecondStart);
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 263 */       this.previousTime = now;
/* 264 */       buf.append(this.cache);
/*     */       
/* 266 */       return buf;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 272 */     this.cache.setLength(0);
/* 273 */     this.tmpDate.setTime(now);
/* 274 */     this.cache.append(this.formatter.format(this.tmpDate));
/* 275 */     buf.append(this.cache);
/* 276 */     this.previousTime = now;
/* 277 */     this.slotBegin = this.previousTime / 1000L * 1000L;
/*     */     
/* 279 */     if (this.slotBegin > this.previousTime) {
/* 280 */       this.slotBegin -= 1000L;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 287 */     if (this.millisecondStart >= 0) {
/* 288 */       this.millisecondStart = findMillisecondStart(now, this.cache.toString(), this.formatter);
/*     */     }
/*     */     
/* 291 */     return buf;
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
/*     */   private static void millisecondFormat(int millis, StringBuffer buf, int offset) {
/* 303 */     buf.setCharAt(offset, "0123456789".charAt(millis / 100));
/* 304 */     buf.setCharAt(offset + 1, "0123456789".charAt(millis / 10 % 10));
/* 305 */     buf.setCharAt(offset + 2, "0123456789".charAt(millis % 10));
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
/*     */   public void setTimeZone(TimeZone timeZone) {
/* 317 */     this.formatter.setTimeZone(timeZone);
/* 318 */     this.previousTime = Long.MIN_VALUE;
/* 319 */     this.slotBegin = Long.MIN_VALUE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Date parse(String s, ParsePosition pos) {
/* 330 */     return this.formatter.parse(s, pos);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NumberFormat getNumberFormat() {
/* 339 */     return this.formatter.getNumberFormat();
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
/*     */   public static int getMaximumCacheValidity(String pattern) {
/* 356 */     int firstS = pattern.indexOf('S');
/*     */     
/* 358 */     if (firstS >= 0 && firstS != pattern.lastIndexOf("SSS")) {
/* 359 */       return 1;
/*     */     }
/*     */     
/* 362 */     return 1000;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/pattern/CachedDateFormat.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */