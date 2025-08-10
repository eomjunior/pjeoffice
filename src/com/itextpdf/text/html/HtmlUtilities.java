/*     */ package com.itextpdf.text.html;
/*     */ 
/*     */ import com.itextpdf.text.BaseColor;
/*     */ import java.util.HashMap;
/*     */ import java.util.Properties;
/*     */ import java.util.StringTokenizer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Deprecated
/*     */ public class HtmlUtilities
/*     */ {
/*     */   public static final float DEFAULT_FONT_SIZE = 12.0F;
/*  66 */   private static HashMap<String, Float> sizes = new HashMap<String, Float>();
/*     */   static {
/*  68 */     sizes.put("xx-small", new Float(4.0F));
/*  69 */     sizes.put("x-small", new Float(6.0F));
/*  70 */     sizes.put("small", new Float(8.0F));
/*  71 */     sizes.put("medium", new Float(10.0F));
/*  72 */     sizes.put("large", new Float(13.0F));
/*  73 */     sizes.put("x-large", new Float(18.0F));
/*  74 */     sizes.put("xx-large", new Float(26.0F));
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
/*     */   public static float parseLength(String string) {
/*  87 */     return parseLength(string, 12.0F);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static float parseLength(String string, float actualFontSize) {
/*  96 */     if (string == null)
/*  97 */       return 0.0F; 
/*  98 */     Float fl = sizes.get(string.toLowerCase());
/*  99 */     if (fl != null)
/* 100 */       return fl.floatValue(); 
/* 101 */     int pos = 0;
/* 102 */     int length = string.length();
/* 103 */     boolean ok = true;
/* 104 */     while (ok && pos < length) {
/* 105 */       switch (string.charAt(pos)) {
/*     */         case '+':
/*     */         case '-':
/*     */         case '.':
/*     */         case '0':
/*     */         case '1':
/*     */         case '2':
/*     */         case '3':
/*     */         case '4':
/*     */         case '5':
/*     */         case '6':
/*     */         case '7':
/*     */         case '8':
/*     */         case '9':
/* 119 */           pos++;
/*     */           continue;
/*     */       } 
/* 122 */       ok = false;
/*     */     } 
/*     */     
/* 125 */     if (pos == 0)
/* 126 */       return 0.0F; 
/* 127 */     if (pos == length)
/* 128 */       return Float.parseFloat(string + "f"); 
/* 129 */     float f = Float.parseFloat(string.substring(0, pos) + "f");
/* 130 */     string = string.substring(pos);
/*     */     
/* 132 */     if (string.startsWith("in")) {
/* 133 */       return f * 72.0F;
/*     */     }
/*     */     
/* 136 */     if (string.startsWith("cm")) {
/* 137 */       return f / 2.54F * 72.0F;
/*     */     }
/*     */     
/* 140 */     if (string.startsWith("mm")) {
/* 141 */       return f / 25.4F * 72.0F;
/*     */     }
/*     */     
/* 144 */     if (string.startsWith("pc")) {
/* 145 */       return f * 12.0F;
/*     */     }
/*     */     
/* 148 */     if (string.startsWith("em")) {
/* 149 */       return f * actualFontSize;
/*     */     }
/*     */ 
/*     */     
/* 153 */     if (string.startsWith("ex")) {
/* 154 */       return f * actualFontSize / 2.0F;
/*     */     }
/*     */     
/* 157 */     return f;
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
/*     */   public static BaseColor decodeColor(String s) {
/* 170 */     if (s == null)
/* 171 */       return null; 
/* 172 */     s = s.toLowerCase().trim();
/*     */     try {
/* 174 */       return WebColors.getRGBColor(s);
/*     */     }
/* 176 */     catch (IllegalArgumentException iae) {
/* 177 */       return null;
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
/*     */   public static Properties parseAttributes(String string) {
/* 191 */     Properties result = new Properties();
/* 192 */     if (string == null)
/* 193 */       return result; 
/* 194 */     StringTokenizer keyValuePairs = new StringTokenizer(string, ";");
/*     */ 
/*     */ 
/*     */     
/* 198 */     while (keyValuePairs.hasMoreTokens()) {
/* 199 */       StringTokenizer keyValuePair = new StringTokenizer(keyValuePairs.nextToken(), ":");
/* 200 */       if (keyValuePair.hasMoreTokens()) {
/* 201 */         String key = keyValuePair.nextToken().trim();
/*     */ 
/*     */         
/* 204 */         if (keyValuePair.hasMoreTokens())
/* 205 */         { String value = keyValuePair.nextToken().trim();
/*     */ 
/*     */           
/* 208 */           if (value.startsWith("\""))
/* 209 */             value = value.substring(1); 
/* 210 */           if (value.endsWith("\""))
/* 211 */             value = value.substring(0, value.length() - 1); 
/* 212 */           result.setProperty(key.toLowerCase(), value); } 
/*     */       } 
/* 214 */     }  return result;
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
/*     */   public static String removeComment(String string, String startComment, String endComment) {
/* 230 */     StringBuffer result = new StringBuffer();
/* 231 */     int pos = 0;
/* 232 */     int end = endComment.length();
/* 233 */     int start = string.indexOf(startComment, pos);
/* 234 */     while (start > -1) {
/* 235 */       result.append(string.substring(pos, start));
/* 236 */       pos = string.indexOf(endComment, start) + end;
/* 237 */       start = string.indexOf(startComment, pos);
/*     */     } 
/* 239 */     result.append(string.substring(pos));
/* 240 */     return result.toString();
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
/*     */   public static String eliminateWhiteSpace(String content) {
/* 252 */     StringBuffer buf = new StringBuffer();
/* 253 */     int len = content.length();
/*     */     
/* 255 */     boolean newline = false;
/* 256 */     for (int i = 0; i < len; i++) {
/* 257 */       char character; switch (character = content.charAt(i)) {
/*     */         case ' ':
/* 259 */           if (!newline) {
/* 260 */             buf.append(character);
/*     */           }
/*     */           break;
/*     */         case '\n':
/* 264 */           if (i > 0) {
/* 265 */             newline = true;
/* 266 */             buf.append(' ');
/*     */           } 
/*     */           break;
/*     */         
/*     */         case '\r':
/*     */         case '\t':
/*     */           break;
/*     */         default:
/* 274 */           newline = false;
/* 275 */           buf.append(character); break;
/*     */       } 
/*     */     } 
/* 278 */     return buf.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 285 */   public static final int[] FONTSIZES = new int[] { 8, 10, 12, 14, 18, 24, 36 };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getIndexedFontSize(String value, String previous) {
/* 295 */     int sIndex = 0;
/*     */     
/* 297 */     if (value.startsWith("+") || value.startsWith("-")) {
/*     */       
/* 299 */       if (previous == null)
/* 300 */         previous = "12"; 
/* 301 */       int c = (int)Float.parseFloat(previous);
/*     */       
/* 303 */       for (int k = FONTSIZES.length - 1; k >= 0; k--) {
/* 304 */         if (c >= FONTSIZES[k]) {
/* 305 */           sIndex = k;
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/*     */       
/* 311 */       int diff = Integer.parseInt(value.startsWith("+") ? value
/* 312 */           .substring(1) : value);
/*     */       
/* 314 */       sIndex += diff;
/*     */     } else {
/*     */ 
/*     */       
/*     */       try {
/* 319 */         sIndex = Integer.parseInt(value) - 1;
/* 320 */       } catch (NumberFormatException nfe) {
/* 321 */         sIndex = 0;
/*     */       } 
/*     */     } 
/* 324 */     if (sIndex < 0) {
/* 325 */       sIndex = 0;
/* 326 */     } else if (sIndex >= FONTSIZES.length) {
/* 327 */       sIndex = FONTSIZES.length - 1;
/* 328 */     }  return FONTSIZES[sIndex];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int alignmentValue(String alignment) {
/* 338 */     if (alignment == null) return -1; 
/* 339 */     if ("center".equalsIgnoreCase(alignment)) {
/* 340 */       return 1;
/*     */     }
/* 342 */     if ("left".equalsIgnoreCase(alignment)) {
/* 343 */       return 0;
/*     */     }
/* 345 */     if ("right".equalsIgnoreCase(alignment)) {
/* 346 */       return 2;
/*     */     }
/* 348 */     if ("justify".equalsIgnoreCase(alignment)) {
/* 349 */       return 3;
/*     */     }
/* 351 */     if ("JustifyAll".equalsIgnoreCase(alignment)) {
/* 352 */       return 8;
/*     */     }
/* 354 */     if ("top".equalsIgnoreCase(alignment)) {
/* 355 */       return 4;
/*     */     }
/* 357 */     if ("middle".equalsIgnoreCase(alignment)) {
/* 358 */       return 5;
/*     */     }
/* 360 */     if ("bottom".equalsIgnoreCase(alignment)) {
/* 361 */       return 6;
/*     */     }
/* 363 */     if ("baseline".equalsIgnoreCase(alignment)) {
/* 364 */       return 7;
/*     */     }
/* 366 */     return -1;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/html/HtmlUtilities.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */