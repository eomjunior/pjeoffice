/*     */ package com.itextpdf.xmp.impl;
/*     */ 
/*     */ import com.itextpdf.xmp.XMPConst;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Utils
/*     */   implements XMPConst
/*     */ {
/*     */   public static final int UUID_SEGMENT_COUNT = 4;
/*     */   public static final int UUID_LENGTH = 36;
/*     */   private static boolean[] xmlNameStartChars;
/*     */   private static boolean[] xmlNameChars;
/*     */   
/*     */   static {
/*  55 */     initCharTables();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String normalizeLangValue(String value) {
/*  86 */     if ("x-default".equals(value))
/*     */     {
/*  88 */       return value;
/*     */     }
/*     */     
/*  91 */     int subTag = 1;
/*  92 */     StringBuffer buffer = new StringBuffer();
/*     */     
/*  94 */     for (int i = 0; i < value.length(); i++) {
/*     */       
/*  96 */       switch (value.charAt(i)) {
/*     */ 
/*     */         
/*     */         case '-':
/*     */         case '_':
/* 101 */           buffer.append('-');
/* 102 */           subTag++;
/*     */           break;
/*     */         
/*     */         case ' ':
/*     */           break;
/*     */         
/*     */         default:
/* 109 */           if (subTag != 2) {
/*     */             
/* 111 */             buffer.append(Character.toLowerCase(value.charAt(i)));
/*     */             
/*     */             break;
/*     */           } 
/* 115 */           buffer.append(Character.toUpperCase(value.charAt(i)));
/*     */           break;
/*     */       } 
/*     */     
/*     */     } 
/* 120 */     return buffer.toString();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static String[] splitNameAndValue(String selector) {
/* 145 */     int eq = selector.indexOf('=');
/* 146 */     int pos = 1;
/* 147 */     if (selector.charAt(pos) == '?')
/*     */     {
/* 149 */       pos++;
/*     */     }
/* 151 */     String name = selector.substring(pos, eq);
/*     */ 
/*     */     
/* 154 */     pos = eq + 1;
/* 155 */     char quote = selector.charAt(pos);
/* 156 */     pos++;
/* 157 */     int end = selector.length() - 2;
/* 158 */     StringBuffer value = new StringBuffer(end - eq);
/* 159 */     while (pos < end) {
/*     */       
/* 161 */       value.append(selector.charAt(pos));
/* 162 */       pos++;
/* 163 */       if (selector.charAt(pos) == quote)
/*     */       {
/*     */         
/* 166 */         pos++;
/*     */       }
/*     */     } 
/* 169 */     return new String[] { name, value.toString() };
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
/*     */   static boolean isInternalProperty(String schema, String prop) {
/* 184 */     boolean isInternal = false;
/*     */     
/* 186 */     if ("http://purl.org/dc/elements/1.1/".equals(schema)) {
/*     */       
/* 188 */       if ("dc:format".equals(prop) || "dc:language".equals(prop))
/*     */       {
/* 190 */         isInternal = true;
/*     */       }
/*     */     }
/* 193 */     else if ("http://ns.adobe.com/xap/1.0/".equals(schema)) {
/*     */       
/* 195 */       if ("xmp:BaseURL".equals(prop) || "xmp:CreatorTool".equals(prop) || "xmp:Format"
/* 196 */         .equals(prop) || "xmp:Locale".equals(prop) || "xmp:MetadataDate"
/* 197 */         .equals(prop) || "xmp:ModifyDate".equals(prop))
/*     */       {
/* 199 */         isInternal = true;
/*     */       }
/*     */     }
/* 202 */     else if ("http://ns.adobe.com/pdf/1.3/".equals(schema)) {
/*     */       
/* 204 */       if ("pdf:BaseURL".equals(prop) || "pdf:Creator".equals(prop) || "pdf:ModDate"
/* 205 */         .equals(prop) || "pdf:PDFVersion".equals(prop) || "pdf:Producer"
/* 206 */         .equals(prop))
/*     */       {
/* 208 */         isInternal = true;
/*     */       }
/*     */     }
/* 211 */     else if ("http://ns.adobe.com/tiff/1.0/".equals(schema)) {
/*     */       
/* 213 */       isInternal = true;
/* 214 */       if ("tiff:ImageDescription".equals(prop) || "tiff:Artist".equals(prop) || "tiff:Copyright"
/* 215 */         .equals(prop))
/*     */       {
/* 217 */         isInternal = false;
/*     */       }
/*     */     }
/* 220 */     else if ("http://ns.adobe.com/exif/1.0/".equals(schema)) {
/*     */       
/* 222 */       isInternal = true;
/* 223 */       if ("exif:UserComment".equals(prop))
/*     */       {
/* 225 */         isInternal = false;
/*     */       }
/*     */     }
/* 228 */     else if ("http://ns.adobe.com/exif/1.0/aux/".equals(schema)) {
/*     */       
/* 230 */       isInternal = true;
/*     */     }
/* 232 */     else if ("http://ns.adobe.com/photoshop/1.0/".equals(schema)) {
/*     */       
/* 234 */       if ("photoshop:ICCProfile".equals(prop))
/*     */       {
/* 236 */         isInternal = true;
/*     */       }
/*     */     }
/* 239 */     else if ("http://ns.adobe.com/camera-raw-settings/1.0/".equals(schema)) {
/*     */       
/* 241 */       if ("crs:Version".equals(prop) || "crs:RawFileName".equals(prop) || "crs:ToneCurveName"
/* 242 */         .equals(prop))
/*     */       {
/* 244 */         isInternal = true;
/*     */       }
/*     */     }
/* 247 */     else if ("http://ns.adobe.com/StockPhoto/1.0/".equals(schema)) {
/*     */       
/* 249 */       isInternal = true;
/*     */     }
/* 251 */     else if ("http://ns.adobe.com/xap/1.0/mm/".equals(schema)) {
/*     */       
/* 253 */       isInternal = true;
/*     */     }
/* 255 */     else if ("http://ns.adobe.com/xap/1.0/t/".equals(schema)) {
/*     */       
/* 257 */       isInternal = true;
/*     */     }
/* 259 */     else if ("http://ns.adobe.com/xap/1.0/t/pg/".equals(schema)) {
/*     */       
/* 261 */       isInternal = true;
/*     */     }
/* 263 */     else if ("http://ns.adobe.com/xap/1.0/g/".equals(schema)) {
/*     */       
/* 265 */       isInternal = true;
/*     */     }
/* 267 */     else if ("http://ns.adobe.com/xap/1.0/g/img/".equals(schema)) {
/*     */       
/* 269 */       isInternal = true;
/*     */     }
/* 271 */     else if ("http://ns.adobe.com/xap/1.0/sType/Font#".equals(schema)) {
/*     */       
/* 273 */       isInternal = true;
/*     */     } 
/*     */     
/* 276 */     return isInternal;
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
/*     */ 
/*     */   
/*     */   static boolean checkUUIDFormat(String uuid) {
/* 295 */     boolean result = true;
/* 296 */     int delimCnt = 0;
/* 297 */     int delimPos = 0;
/*     */     
/* 299 */     if (uuid == null)
/*     */     {
/* 301 */       return false;
/*     */     }
/*     */     
/* 304 */     for (delimPos = 0; delimPos < uuid.length(); delimPos++) {
/*     */       
/* 306 */       if (uuid.charAt(delimPos) == '-') {
/*     */         
/* 308 */         delimCnt++;
/* 309 */         result = (result && (delimPos == 8 || delimPos == 13 || delimPos == 18 || delimPos == 23));
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 314 */     return (result && 4 == delimCnt && 36 == delimPos);
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
/*     */   public static boolean isXMLName(String name) {
/* 329 */     if (name.length() > 0 && !isNameStartChar(name.charAt(0)))
/*     */     {
/* 331 */       return false;
/*     */     }
/* 333 */     for (int i = 1; i < name.length(); i++) {
/*     */       
/* 335 */       if (!isNameChar(name.charAt(i)))
/*     */       {
/* 337 */         return false;
/*     */       }
/*     */     } 
/* 340 */     return true;
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
/*     */   public static boolean isXMLNameNS(String name) {
/* 353 */     if (name.length() > 0 && (!isNameStartChar(name.charAt(0)) || name.charAt(0) == ':'))
/*     */     {
/* 355 */       return false;
/*     */     }
/* 357 */     for (int i = 1; i < name.length(); i++) {
/*     */       
/* 359 */       if (!isNameChar(name.charAt(i)) || name.charAt(i) == ':')
/*     */       {
/* 361 */         return false;
/*     */       }
/*     */     } 
/* 364 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static boolean isControlChar(char c) {
/* 374 */     return ((c <= '\037' || c == '') && c != '\t' && c != '\n' && c != '\r');
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
/*     */ 
/*     */ 
/*     */   
/*     */   public static String escapeXML(String value, boolean forAttribute, boolean escapeWhitespaces) {
/* 394 */     boolean needsEscaping = false;
/* 395 */     for (int i = 0; i < value.length(); i++) {
/*     */       
/* 397 */       char c = value.charAt(i);
/* 398 */       if (c == '<' || c == '>' || c == '&' || (escapeWhitespaces && (c == '\t' || c == '\n' || c == '\r')) || (forAttribute && c == '"')) {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 403 */         needsEscaping = true;
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/* 408 */     if (!needsEscaping)
/*     */     {
/*     */       
/* 411 */       return value;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 416 */     StringBuffer buffer = new StringBuffer(value.length() * 4 / 3);
/* 417 */     for (int j = 0; j < value.length(); j++) {
/*     */       
/* 419 */       char c = value.charAt(j);
/* 420 */       if (!escapeWhitespaces || (c != '\t' && c != '\n' && c != '\r')) {
/*     */         
/* 422 */         switch (c) {
/*     */ 
/*     */           
/*     */           case '<':
/* 426 */             buffer.append("&lt;"); break;
/* 427 */           case '>': buffer.append("&gt;"); break;
/* 428 */           case '&': buffer.append("&amp;"); break;
/* 429 */           case '"': buffer.append(forAttribute ? "&quot;" : "\""); break;
/* 430 */           default: buffer.append(c);
/*     */             break;
/*     */         } 
/*     */ 
/*     */ 
/*     */       
/*     */       } else {
/* 437 */         buffer.append("&#x");
/* 438 */         buffer.append(Integer.toHexString(c).toUpperCase());
/* 439 */         buffer.append(';');
/*     */       } 
/*     */     } 
/* 442 */     return buffer.toString();
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
/*     */   static String removeControlChars(String value) {
/* 456 */     StringBuffer buffer = new StringBuffer(value);
/* 457 */     for (int i = 0; i < buffer.length(); i++) {
/*     */       
/* 459 */       if (isControlChar(buffer.charAt(i)))
/*     */       {
/* 461 */         buffer.setCharAt(i, ' ');
/*     */       }
/*     */     } 
/* 464 */     return buffer.toString();
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
/*     */   private static boolean isNameStartChar(char ch) {
/* 478 */     return ((ch <= 'ÿ' && xmlNameStartChars[ch]) || (ch >= 'Ā' && ch <= '˿') || (ch >= 'Ͱ' && ch <= 'ͽ') || (ch >= 'Ϳ' && ch <= '῿') || (ch >= '‌' && ch <= '‍') || (ch >= '⁰' && ch <= '↏') || (ch >= 'Ⰰ' && ch <= '⿯') || (ch >= '、' && ch <= '퟿') || (ch >= '豈' && ch <= '﷏') || (ch >= 'ﷰ' && ch <= '�') || (ch >= 65536 && ch <= 983039));
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isNameChar(char ch) {
/* 503 */     return ((ch <= 'ÿ' && xmlNameChars[ch]) || 
/*     */       
/* 505 */       isNameStartChar(ch) || (ch >= '̀' && ch <= 'ͯ') || (ch >= '‿' && ch <= '⁀'));
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
/*     */   private static void initCharTables() {
/* 518 */     xmlNameChars = new boolean[256];
/* 519 */     xmlNameStartChars = new boolean[256];
/*     */     
/* 521 */     for (char ch = Character.MIN_VALUE; ch < xmlNameChars.length; ch = (char)(ch + 1)) {
/*     */       
/* 523 */       xmlNameStartChars[ch] = (ch == ':' || ('A' <= ch && ch <= 'Z') || ch == '_' || ('a' <= ch && ch <= 'z') || ('À' <= ch && ch <= 'Ö') || ('Ø' <= ch && ch <= 'ö') || ('ø' <= ch && ch <= 'ÿ'));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 532 */       xmlNameChars[ch] = (xmlNameStartChars[ch] || ch == '-' || ch == '.' || ('0' <= ch && ch <= '9') || ch == '·');
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/xmp/impl/Utils.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */