/*     */ package com.itextpdf.text.pdf;
/*     */ 
/*     */ import java.util.Calendar;
/*     */ import java.util.GregorianCalendar;
/*     */ import java.util.SimpleTimeZone;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PdfDate
/*     */   extends PdfString
/*     */ {
/*  68 */   private static final int[] DATE_SPACE = new int[] { 1, 4, 0, 2, 2, -1, 5, 2, 0, 11, 2, 0, 12, 2, 0, 13, 2, 0 };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfDate(Calendar d) {
/*  81 */     StringBuffer date = new StringBuffer("D:");
/*  82 */     date.append(setLength(d.get(1), 4));
/*  83 */     date.append(setLength(d.get(2) + 1, 2));
/*  84 */     date.append(setLength(d.get(5), 2));
/*  85 */     date.append(setLength(d.get(11), 2));
/*  86 */     date.append(setLength(d.get(12), 2));
/*  87 */     date.append(setLength(d.get(13), 2));
/*  88 */     int timezone = (d.get(15) + d.get(16)) / 3600000;
/*  89 */     if (timezone == 0) {
/*  90 */       date.append('Z');
/*     */     }
/*  92 */     else if (timezone < 0) {
/*  93 */       date.append('-');
/*  94 */       timezone = -timezone;
/*     */     } else {
/*     */       
/*  97 */       date.append('+');
/*     */     } 
/*  99 */     if (timezone != 0) {
/* 100 */       date.append(setLength(timezone, 2)).append('\'');
/* 101 */       int zone = Math.abs((d.get(15) + d.get(16)) / 60000) - timezone * 60;
/* 102 */       date.append(setLength(zone, 2)).append('\'');
/*     */     } 
/* 104 */     this.value = date.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfDate() {
/* 112 */     this(new GregorianCalendar());
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
/*     */   private String setLength(int i, int length) {
/* 125 */     StringBuffer tmp = new StringBuffer();
/* 126 */     tmp.append(i);
/* 127 */     while (tmp.length() < length) {
/* 128 */       tmp.insert(0, "0");
/*     */     }
/* 130 */     tmp.setLength(length);
/* 131 */     return tmp.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getW3CDate() {
/* 139 */     return getW3CDate(this.value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getW3CDate(String d) {
/* 148 */     if (d.startsWith("D:"))
/* 149 */       d = d.substring(2); 
/* 150 */     StringBuffer sb = new StringBuffer();
/* 151 */     if (d.length() < 4)
/* 152 */       return "0000"; 
/* 153 */     sb.append(d.substring(0, 4));
/* 154 */     d = d.substring(4);
/* 155 */     if (d.length() < 2)
/* 156 */       return sb.toString(); 
/* 157 */     sb.append('-').append(d.substring(0, 2));
/* 158 */     d = d.substring(2);
/* 159 */     if (d.length() < 2)
/* 160 */       return sb.toString(); 
/* 161 */     sb.append('-').append(d.substring(0, 2));
/* 162 */     d = d.substring(2);
/* 163 */     if (d.length() < 2)
/* 164 */       return sb.toString(); 
/* 165 */     sb.append('T').append(d.substring(0, 2));
/* 166 */     d = d.substring(2);
/* 167 */     if (d.length() < 2) {
/* 168 */       sb.append(":00Z");
/* 169 */       return sb.toString();
/*     */     } 
/* 171 */     sb.append(':').append(d.substring(0, 2));
/* 172 */     d = d.substring(2);
/* 173 */     if (d.length() < 2) {
/* 174 */       sb.append('Z');
/* 175 */       return sb.toString();
/*     */     } 
/* 177 */     sb.append(':').append(d.substring(0, 2));
/* 178 */     d = d.substring(2);
/* 179 */     if (d.startsWith("-") || d.startsWith("+")) {
/* 180 */       String sign = d.substring(0, 1);
/* 181 */       d = d.substring(1);
/* 182 */       String h = "00";
/* 183 */       String m = "00";
/* 184 */       if (d.length() >= 2) {
/* 185 */         h = d.substring(0, 2);
/* 186 */         if (d.length() > 2) {
/* 187 */           d = d.substring(3);
/* 188 */           if (d.length() >= 2)
/* 189 */             m = d.substring(0, 2); 
/*     */         } 
/* 191 */         sb.append(sign).append(h).append(':').append(m);
/* 192 */         return sb.toString();
/*     */       } 
/*     */     } 
/* 195 */     sb.append('Z');
/* 196 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Calendar decode(String s) {
/*     */     try {
/*     */       GregorianCalendar calendar;
/* 207 */       if (s.startsWith("D:")) {
/* 208 */         s = s.substring(2);
/*     */       }
/* 210 */       int slen = s.length();
/* 211 */       int idx = s.indexOf('Z');
/* 212 */       if (idx >= 0) {
/* 213 */         slen = idx;
/* 214 */         calendar = new GregorianCalendar(new SimpleTimeZone(0, "ZPDF"));
/*     */       } else {
/*     */         
/* 217 */         int sign = 1;
/* 218 */         idx = s.indexOf('+');
/* 219 */         if (idx < 0) {
/* 220 */           idx = s.indexOf('-');
/* 221 */           if (idx >= 0)
/* 222 */             sign = -1; 
/*     */         } 
/* 224 */         if (idx < 0) {
/* 225 */           calendar = new GregorianCalendar();
/*     */         } else {
/* 227 */           int offset = Integer.parseInt(s.substring(idx + 1, idx + 3)) * 60;
/* 228 */           if (idx + 5 < s.length())
/* 229 */             offset += Integer.parseInt(s.substring(idx + 4, idx + 6)); 
/* 230 */           calendar = new GregorianCalendar(new SimpleTimeZone(offset * sign * 60000, "ZPDF"));
/* 231 */           slen = idx;
/*     */         } 
/*     */       } 
/* 234 */       calendar.clear();
/* 235 */       idx = 0;
/* 236 */       for (int k = 0; k < DATE_SPACE.length && 
/* 237 */         idx < slen; k += 3) {
/*     */         
/* 239 */         calendar.set(DATE_SPACE[k], Integer.parseInt(s.substring(idx, idx + DATE_SPACE[k + 1])) + DATE_SPACE[k + 2]);
/* 240 */         idx += DATE_SPACE[k + 1];
/*     */       } 
/* 242 */       return calendar;
/*     */     }
/* 244 */     catch (Exception e) {
/* 245 */       return null;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/PdfDate.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */