/*     */ package org.apache.hc.client5.http.impl.cookie;
/*     */ 
/*     */ import java.time.Instant;
/*     */ import java.time.Month;
/*     */ import java.time.ZoneId;
/*     */ import java.time.ZonedDateTime;
/*     */ import java.util.BitSet;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.apache.hc.client5.http.cookie.CommonCookieAttributeHandler;
/*     */ import org.apache.hc.client5.http.cookie.MalformedCookieException;
/*     */ import org.apache.hc.client5.http.cookie.SetCookie;
/*     */ import org.apache.hc.core5.annotation.Contract;
/*     */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*     */ import org.apache.hc.core5.util.Args;
/*     */ import org.apache.hc.core5.util.TextUtils;
/*     */ import org.apache.hc.core5.util.Tokenizer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Contract(threading = ThreadingBehavior.STATELESS)
/*     */ public class LaxExpiresHandler
/*     */   extends AbstractCookieAttributeHandler
/*     */   implements CommonCookieAttributeHandler
/*     */ {
/*  64 */   public static final LaxExpiresHandler INSTANCE = new LaxExpiresHandler();
/*     */   private static final BitSet DELIMS;
/*     */   
/*     */   static {
/*  68 */     BitSet bitSet = new BitSet();
/*  69 */     bitSet.set(9); int b;
/*  70 */     for (b = 32; b <= 47; b++) {
/*  71 */       bitSet.set(b);
/*     */     }
/*  73 */     for (b = 59; b <= 64; b++) {
/*  74 */       bitSet.set(b);
/*     */     }
/*  76 */     for (b = 91; b <= 96; b++) {
/*  77 */       bitSet.set(b);
/*     */     }
/*  79 */     for (b = 123; b <= 126; b++) {
/*  80 */       bitSet.set(b);
/*     */     }
/*  82 */     DELIMS = bitSet;
/*     */ 
/*     */ 
/*     */     
/*  86 */     ConcurrentHashMap<String, Month> map = new ConcurrentHashMap<>(12);
/*  87 */     map.put("jan", Month.JANUARY);
/*  88 */     map.put("feb", Month.FEBRUARY);
/*  89 */     map.put("mar", Month.MARCH);
/*  90 */     map.put("apr", Month.APRIL);
/*  91 */     map.put("may", Month.MAY);
/*  92 */     map.put("jun", Month.JUNE);
/*  93 */     map.put("jul", Month.JULY);
/*  94 */     map.put("aug", Month.AUGUST);
/*  95 */     map.put("sep", Month.SEPTEMBER);
/*  96 */     map.put("oct", Month.OCTOBER);
/*  97 */     map.put("nov", Month.NOVEMBER);
/*  98 */     map.put("dec", Month.DECEMBER);
/*  99 */     MONTHS = map;
/*     */   }
/*     */   private static final Map<String, Month> MONTHS;
/* 102 */   private static final Pattern TIME_PATTERN = Pattern.compile("^([0-9]{1,2}):([0-9]{1,2}):([0-9]{1,2})([^0-9].*)?$");
/*     */   
/* 104 */   private static final Pattern DAY_OF_MONTH_PATTERN = Pattern.compile("^([0-9]{1,2})([^0-9].*)?$");
/*     */   
/* 106 */   private static final Pattern MONTH_PATTERN = Pattern.compile("^(jan|feb|mar|apr|may|jun|jul|aug|sep|oct|nov|dec)(.*)?$", 2);
/*     */   
/* 108 */   private static final Pattern YEAR_PATTERN = Pattern.compile("^([0-9]{2,4})([^0-9].*)?$");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void parse(SetCookie cookie, String value) throws MalformedCookieException {
/* 117 */     Args.notNull(cookie, "Cookie");
/* 118 */     if (TextUtils.isBlank(value)) {
/*     */       return;
/*     */     }
/* 121 */     Tokenizer.Cursor cursor = new Tokenizer.Cursor(0, value.length());
/* 122 */     StringBuilder content = new StringBuilder();
/*     */     
/* 124 */     int second = 0, minute = 0, hour = 0, day = 0, year = 0;
/* 125 */     Month month = Month.JANUARY;
/* 126 */     boolean foundTime = false, foundDayOfMonth = false, foundMonth = false, foundYear = false;
/*     */     try {
/* 128 */       while (!cursor.atEnd()) {
/* 129 */         skipDelims(value, cursor);
/* 130 */         content.setLength(0);
/* 131 */         copyContent(value, cursor, content);
/*     */         
/* 133 */         if (content.length() == 0) {
/*     */           break;
/*     */         }
/* 136 */         if (!foundTime) {
/* 137 */           Matcher matcher = TIME_PATTERN.matcher(content);
/* 138 */           if (matcher.matches()) {
/* 139 */             foundTime = true;
/* 140 */             hour = Integer.parseInt(matcher.group(1));
/* 141 */             minute = Integer.parseInt(matcher.group(2));
/* 142 */             second = Integer.parseInt(matcher.group(3));
/*     */             continue;
/*     */           } 
/*     */         } 
/* 146 */         if (!foundDayOfMonth) {
/* 147 */           Matcher matcher = DAY_OF_MONTH_PATTERN.matcher(content);
/* 148 */           if (matcher.matches()) {
/* 149 */             foundDayOfMonth = true;
/* 150 */             day = Integer.parseInt(matcher.group(1));
/*     */             continue;
/*     */           } 
/*     */         } 
/* 154 */         if (!foundMonth) {
/* 155 */           Matcher matcher = MONTH_PATTERN.matcher(content);
/* 156 */           if (matcher.matches()) {
/* 157 */             foundMonth = true;
/* 158 */             month = MONTHS.get(matcher.group(1).toLowerCase(Locale.ROOT));
/*     */             continue;
/*     */           } 
/*     */         } 
/* 162 */         if (!foundYear) {
/* 163 */           Matcher matcher = YEAR_PATTERN.matcher(content);
/* 164 */           if (matcher.matches()) {
/* 165 */             foundYear = true;
/* 166 */             year = Integer.parseInt(matcher.group(1));
/*     */           }
/*     */         
/*     */         } 
/*     */       } 
/* 171 */     } catch (NumberFormatException ignore) {
/* 172 */       throw new MalformedCookieException("Invalid 'expires' attribute: " + value);
/*     */     } 
/* 174 */     if (!foundTime || !foundDayOfMonth || !foundMonth || !foundYear) {
/* 175 */       throw new MalformedCookieException("Invalid 'expires' attribute: " + value);
/*     */     }
/* 177 */     if (year >= 70 && year <= 99) {
/* 178 */       year = 1900 + year;
/*     */     }
/* 180 */     if (year >= 0 && year <= 69) {
/* 181 */       year = 2000 + year;
/*     */     }
/* 183 */     if (day < 1 || day > 31 || year < 1601 || hour > 23 || minute > 59 || second > 59) {
/* 184 */       throw new MalformedCookieException("Invalid 'expires' attribute: " + value);
/*     */     }
/*     */ 
/*     */     
/* 188 */     Instant expiryDate = ZonedDateTime.of(year, month.getValue(), day, hour, minute, second, 0, ZoneId.of("UTC")).toInstant();
/* 189 */     cookie.setExpiryDate(expiryDate);
/*     */   }
/*     */   
/*     */   private void skipDelims(CharSequence buf, Tokenizer.Cursor cursor) {
/* 193 */     int pos = cursor.getPos();
/* 194 */     int indexFrom = cursor.getPos();
/* 195 */     int indexTo = cursor.getUpperBound();
/* 196 */     for (int i = indexFrom; i < indexTo; ) {
/* 197 */       char current = buf.charAt(i);
/* 198 */       if (DELIMS.get(current)) {
/* 199 */         pos++;
/*     */         
/*     */         i++;
/*     */       } 
/*     */     } 
/* 204 */     cursor.updatePos(pos);
/*     */   }
/*     */   
/*     */   private void copyContent(CharSequence buf, Tokenizer.Cursor cursor, StringBuilder dst) {
/* 208 */     int pos = cursor.getPos();
/* 209 */     int indexFrom = cursor.getPos();
/* 210 */     int indexTo = cursor.getUpperBound();
/* 211 */     for (int i = indexFrom; i < indexTo; i++) {
/* 212 */       char current = buf.charAt(i);
/* 213 */       if (DELIMS.get(current)) {
/*     */         break;
/*     */       }
/* 216 */       pos++;
/* 217 */       dst.append(current);
/*     */     } 
/* 219 */     cursor.updatePos(pos);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getAttributeName() {
/* 224 */     return "expires";
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/cookie/LaxExpiresHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */