/*     */ package com.itextpdf.text.pdf.languages;
/*     */ 
/*     */ import com.itextpdf.text.pdf.BidiLine;
/*     */ import com.itextpdf.text.pdf.BidiOrder;
/*     */ import java.util.HashMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ArabicLigaturizer
/*     */   implements LanguageProcessor
/*     */ {
/*  60 */   private static final HashMap<Character, char[]> maptable = (HashMap)new HashMap<Character, char>(); private static final char ALEF = 'ا'; private static final char ALEFHAMZA = 'أ';
/*     */   private static final char ALEFHAMZABELOW = 'إ';
/*     */   private static final char ALEFMADDA = 'آ';
/*     */   private static final char LAM = 'ل';
/*     */   private static final char HAMZA = 'ء';
/*  65 */   private static final HashMap<Character, Character> reverseLigatureMapTable = new HashMap<Character, Character>(); private static final char TATWEEL = 'ـ'; private static final char ZWJ = '‍'; private static final char HAMZAABOVE = 'ٔ'; private static final char HAMZABELOW = 'ٕ'; private static final char WAWHAMZA = 'ؤ'; private static final char YEHHAMZA = 'ئ';
/*     */   
/*     */   static boolean isVowel(char s) {
/*  68 */     return ((s >= 'ً' && s <= 'ٕ') || s == 'ٰ');
/*     */   }
/*     */   private static final char WAW = 'و'; private static final char ALEFMAKSURA = 'ى'; private static final char YEH = 'ي'; private static final char FARSIYEH = 'ی'; private static final char SHADDA = 'ّ';
/*     */   private static final char KASRA = 'ِ';
/*     */   
/*     */   static char charshape(char s, int which) {
/*  74 */     if (s >= 'ء' && s <= 'ۓ') {
/*  75 */       char[] c = maptable.get(Character.valueOf(s));
/*  76 */       if (c != null) {
/*  77 */         return c[which + 1];
/*     */       }
/*  79 */     } else if (s >= 'ﻵ' && s <= 'ﻻ') {
/*  80 */       return (char)(s + which);
/*  81 */     }  return s;
/*     */   }
/*     */   private static final char FATHA = 'َ'; private static final char DAMMA = 'ُ'; private static final char MADDA = 'ٓ'; private static final char LAM_ALEF = 'ﻻ'; private static final char LAM_ALEFHAMZA = 'ﻷ'; private static final char LAM_ALEFHAMZABELOW = 'ﻹ'; private static final char LAM_ALEFMADDA = 'ﻵ';
/*     */   static int shapecount(char s) {
/*  85 */     if (s >= 'ء' && s <= 'ۓ' && !isVowel(s)) {
/*  86 */       char[] c = maptable.get(Character.valueOf(s));
/*  87 */       if (c != null) {
/*  88 */         return c.length - 1;
/*     */       }
/*  90 */     } else if (s == '‍') {
/*  91 */       return 4;
/*     */     } 
/*  93 */     return 1;
/*     */   }
/*     */ 
/*     */   
/*     */   static int ligature(char newchar, charstruct oldchar) {
/*  98 */     int retval = 0;
/*     */     
/* 100 */     if (oldchar.basechar == '\000')
/* 101 */       return 0; 
/* 102 */     if (isVowel(newchar)) {
/* 103 */       retval = 1;
/* 104 */       if (oldchar.vowel != '\000' && newchar != 'ّ') {
/* 105 */         retval = 2;
/*     */       }
/* 107 */       switch (newchar) {
/*     */         case 'ّ':
/* 109 */           if (oldchar.mark1 == '\000') {
/* 110 */             oldchar.mark1 = 'ّ';
/*     */             break;
/*     */           } 
/* 113 */           return 0;
/*     */ 
/*     */         
/*     */         case 'ٕ':
/* 117 */           switch (oldchar.basechar) {
/*     */             case 'ا':
/* 119 */               oldchar.basechar = 'إ';
/* 120 */               retval = 2;
/*     */               break;
/*     */             case 'ﻻ':
/* 123 */               oldchar.basechar = 'ﻹ';
/* 124 */               retval = 2;
/*     */               break;
/*     */           } 
/* 127 */           oldchar.mark1 = 'ٕ';
/*     */           break;
/*     */ 
/*     */         
/*     */         case 'ٔ':
/* 132 */           switch (oldchar.basechar) {
/*     */             case 'ا':
/* 134 */               oldchar.basechar = 'أ';
/* 135 */               retval = 2;
/*     */               break;
/*     */             case 'ﻻ':
/* 138 */               oldchar.basechar = 'ﻷ';
/* 139 */               retval = 2;
/*     */               break;
/*     */             case 'و':
/* 142 */               oldchar.basechar = 'ؤ';
/* 143 */               retval = 2;
/*     */               break;
/*     */             case 'ى':
/*     */             case 'ي':
/*     */             case 'ی':
/* 148 */               oldchar.basechar = 'ئ';
/* 149 */               retval = 2;
/*     */               break;
/*     */           } 
/* 152 */           oldchar.mark1 = 'ٔ';
/*     */           break;
/*     */ 
/*     */         
/*     */         case 'ٓ':
/* 157 */           switch (oldchar.basechar) {
/*     */             case 'ا':
/* 159 */               oldchar.basechar = 'آ';
/* 160 */               retval = 2;
/*     */               break;
/*     */           } 
/*     */           break;
/*     */         default:
/* 165 */           oldchar.vowel = newchar;
/*     */           break;
/*     */       } 
/* 168 */       if (retval == 1) {
/* 169 */         oldchar.lignum++;
/*     */       }
/* 171 */       return retval;
/*     */     } 
/* 173 */     if (oldchar.vowel != '\000') {
/* 174 */       return 0;
/*     */     }
/*     */     
/* 177 */     switch (oldchar.basechar) {
/*     */       case 'ل':
/* 179 */         switch (newchar) {
/*     */           case 'ا':
/* 181 */             oldchar.basechar = 'ﻻ';
/* 182 */             oldchar.numshapes = 2;
/* 183 */             retval = 3;
/*     */             break;
/*     */           case 'أ':
/* 186 */             oldchar.basechar = 'ﻷ';
/* 187 */             oldchar.numshapes = 2;
/* 188 */             retval = 3;
/*     */             break;
/*     */           case 'إ':
/* 191 */             oldchar.basechar = 'ﻹ';
/* 192 */             oldchar.numshapes = 2;
/* 193 */             retval = 3;
/*     */             break;
/*     */           case 'آ':
/* 196 */             oldchar.basechar = 'ﻵ';
/* 197 */             oldchar.numshapes = 2;
/* 198 */             retval = 3;
/*     */             break;
/*     */         } 
/*     */         break;
/*     */       case '\000':
/* 203 */         oldchar.basechar = newchar;
/* 204 */         oldchar.numshapes = shapecount(newchar);
/* 205 */         retval = 1;
/*     */         break;
/*     */     } 
/* 208 */     return retval;
/*     */   }
/*     */ 
/*     */   
/*     */   static void copycstostring(StringBuffer string, charstruct s, int level) {
/* 213 */     if (s.basechar == '\000') {
/*     */       return;
/*     */     }
/* 216 */     string.append(s.basechar);
/* 217 */     s.lignum--;
/* 218 */     if (s.mark1 != '\000') {
/* 219 */       if ((level & 0x1) == 0) {
/* 220 */         string.append(s.mark1);
/* 221 */         s.lignum--;
/*     */       } else {
/*     */         
/* 224 */         s.lignum--;
/*     */       } 
/*     */     }
/* 227 */     if (s.vowel != '\000') {
/* 228 */       if ((level & 0x1) == 0) {
/* 229 */         string.append(s.vowel);
/* 230 */         s.lignum--;
/*     */       } else {
/*     */         
/* 233 */         s.lignum--;
/*     */       } 
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
/*     */   
/*     */   static void doublelig(StringBuffer string, int level) {
/* 249 */     int len = string.length(), olen = len;
/* 250 */     int j = 0, si = 1;
/*     */ 
/*     */     
/* 253 */     while (si < olen) {
/* 254 */       char lapresult = Character.MIN_VALUE;
/* 255 */       if ((level & 0x4) != 0) {
/* 256 */         switch (string.charAt(j)) {
/*     */           case 'ّ':
/* 258 */             switch (string.charAt(si)) {
/*     */               case 'ِ':
/* 260 */                 lapresult = 'ﱢ';
/*     */                 break;
/*     */               case 'َ':
/* 263 */                 lapresult = 'ﱠ';
/*     */                 break;
/*     */               case 'ُ':
/* 266 */                 lapresult = 'ﱡ';
/*     */                 break;
/*     */               case 'ٌ':
/* 269 */                 lapresult = 'ﱞ';
/*     */                 break;
/*     */               case 'ٍ':
/* 272 */                 lapresult = 'ﱟ';
/*     */                 break;
/*     */             } 
/*     */             break;
/*     */           case 'ِ':
/* 277 */             if (string.charAt(si) == 'ّ')
/* 278 */               lapresult = 'ﱢ'; 
/*     */             break;
/*     */           case 'َ':
/* 281 */             if (string.charAt(si) == 'ّ')
/* 282 */               lapresult = 'ﱠ'; 
/*     */             break;
/*     */           case 'ُ':
/* 285 */             if (string.charAt(si) == 'ّ') {
/* 286 */               lapresult = 'ﱡ';
/*     */             }
/*     */             break;
/*     */         } 
/*     */       }
/* 291 */       if ((level & 0x8) != 0) {
/* 292 */         switch (string.charAt(j)) {
/*     */           case 'ﻟ':
/* 294 */             switch (string.charAt(si)) {
/*     */               case 'ﺞ':
/* 296 */                 lapresult = 'ﰿ';
/*     */                 break;
/*     */               case 'ﺠ':
/* 299 */                 lapresult = 'ﳉ';
/*     */                 break;
/*     */               case 'ﺢ':
/* 302 */                 lapresult = 'ﱀ';
/*     */                 break;
/*     */               case 'ﺤ':
/* 305 */                 lapresult = 'ﳊ';
/*     */                 break;
/*     */               case 'ﺦ':
/* 308 */                 lapresult = 'ﱁ';
/*     */                 break;
/*     */               case 'ﺨ':
/* 311 */                 lapresult = 'ﳋ';
/*     */                 break;
/*     */               case 'ﻢ':
/* 314 */                 lapresult = 'ﱂ';
/*     */                 break;
/*     */               case 'ﻤ':
/* 317 */                 lapresult = 'ﳌ';
/*     */                 break;
/*     */             } 
/*     */             break;
/*     */           case 'ﺗ':
/* 322 */             switch (string.charAt(si)) {
/*     */               case 'ﺠ':
/* 324 */                 lapresult = 'ﲡ';
/*     */                 break;
/*     */               case 'ﺤ':
/* 327 */                 lapresult = 'ﲢ';
/*     */                 break;
/*     */               case 'ﺨ':
/* 330 */                 lapresult = 'ﲣ';
/*     */                 break;
/*     */             } 
/*     */             break;
/*     */           case 'ﺑ':
/* 335 */             switch (string.charAt(si)) {
/*     */               case 'ﺠ':
/* 337 */                 lapresult = 'ﲜ';
/*     */                 break;
/*     */               case 'ﺤ':
/* 340 */                 lapresult = 'ﲝ';
/*     */                 break;
/*     */               case 'ﺨ':
/* 343 */                 lapresult = 'ﲞ';
/*     */                 break;
/*     */             } 
/*     */             break;
/*     */           case 'ﻧ':
/* 348 */             switch (string.charAt(si)) {
/*     */               case 'ﺠ':
/* 350 */                 lapresult = 'ﳒ';
/*     */                 break;
/*     */               case 'ﺤ':
/* 353 */                 lapresult = 'ﳓ';
/*     */                 break;
/*     */               case 'ﺨ':
/* 356 */                 lapresult = 'ﳔ';
/*     */                 break;
/*     */             } 
/*     */             
/*     */             break;
/*     */           case 'ﻨ':
/* 362 */             switch (string.charAt(si)) {
/*     */               case 'ﺮ':
/* 364 */                 lapresult = 'ﲊ';
/*     */                 break;
/*     */               case 'ﺰ':
/* 367 */                 lapresult = 'ﲋ';
/*     */                 break;
/*     */             } 
/*     */             break;
/*     */           case 'ﻣ':
/* 372 */             switch (string.charAt(si)) {
/*     */               case 'ﺠ':
/* 374 */                 lapresult = 'ﳎ';
/*     */                 break;
/*     */               case 'ﺤ':
/* 377 */                 lapresult = 'ﳏ';
/*     */                 break;
/*     */               case 'ﺨ':
/* 380 */                 lapresult = 'ﳐ';
/*     */                 break;
/*     */               case 'ﻤ':
/* 383 */                 lapresult = 'ﳑ';
/*     */                 break;
/*     */             } 
/*     */             
/*     */             break;
/*     */           case 'ﻓ':
/* 389 */             switch (string.charAt(si)) {
/*     */               case 'ﻲ':
/* 391 */                 lapresult = 'ﰲ';
/*     */                 break;
/*     */             } 
/*     */ 
/*     */             
/*     */             break;
/*     */         } 
/*     */       
/*     */       }
/* 400 */       if (lapresult != '\000') {
/* 401 */         string.setCharAt(j, lapresult);
/* 402 */         len--;
/* 403 */         si++;
/*     */         
/*     */         continue;
/*     */       } 
/* 407 */       j++;
/* 408 */       string.setCharAt(j, string.charAt(si));
/* 409 */       si++;
/*     */     } 
/*     */     
/* 412 */     string.setLength(len);
/*     */   }
/*     */   
/*     */   static boolean connects_to_left(charstruct a) {
/* 416 */     return (a.numshapes > 2);
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
/*     */   static void shape(char[] text, StringBuffer string, int level) {
/* 431 */     int which, p = 0;
/* 432 */     charstruct oldchar = new charstruct();
/* 433 */     charstruct curchar = new charstruct();
/* 434 */     while (p < text.length) {
/* 435 */       char nextletter = text[p++];
/*     */ 
/*     */       
/* 438 */       int join = ligature(nextletter, curchar);
/* 439 */       if (join == 0) {
/* 440 */         int nc = shapecount(nextletter);
/*     */         
/* 442 */         if (nc == 1) {
/* 443 */           which = 0;
/*     */         } else {
/*     */           
/* 446 */           which = 2;
/*     */         } 
/* 448 */         if (connects_to_left(oldchar)) {
/* 449 */           which++;
/*     */         }
/*     */         
/* 452 */         which %= curchar.numshapes;
/* 453 */         curchar.basechar = charshape(curchar.basechar, which);
/*     */ 
/*     */         
/* 456 */         copycstostring(string, oldchar, level);
/* 457 */         oldchar = curchar;
/*     */ 
/*     */         
/* 460 */         curchar = new charstruct();
/* 461 */         curchar.basechar = nextletter;
/* 462 */         curchar.numshapes = nc;
/* 463 */         curchar.lignum++;
/*     */         continue;
/*     */       } 
/* 466 */       if (join == 1);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 476 */     if (connects_to_left(oldchar)) {
/* 477 */       which = 1;
/*     */     } else {
/* 479 */       which = 0;
/* 480 */     }  which %= curchar.numshapes;
/* 481 */     curchar.basechar = charshape(curchar.basechar, which);
/*     */ 
/*     */     
/* 484 */     copycstostring(string, oldchar, level);
/* 485 */     copycstostring(string, curchar, level);
/*     */   }
/*     */   
/*     */   public static int arabic_shape(char[] src, int srcoffset, int srclength, char[] dest, int destoffset, int destlength, int level) {
/* 489 */     char[] str = new char[srclength];
/* 490 */     for (int k = srclength + srcoffset - 1; k >= srcoffset; k--)
/* 491 */       str[k - srcoffset] = src[k]; 
/* 492 */     StringBuffer string = new StringBuffer(srclength);
/* 493 */     shape(str, string, level);
/* 494 */     if ((level & 0xC) != 0) {
/* 495 */       doublelig(string, level);
/*     */     }
/* 497 */     System.arraycopy(string.toString().toCharArray(), 0, dest, destoffset, string.length());
/* 498 */     return string.length();
/*     */   }
/*     */   
/*     */   public static void processNumbers(char[] text, int offset, int length, int options) {
/* 502 */     int limit = offset + length;
/* 503 */     if ((options & 0xE0) != 0) {
/* 504 */       int digitDelta; char digitTop; int i, j, k; char digitBase = '0';
/* 505 */       switch (options & 0x100) {
/*     */         case 0:
/* 507 */           digitBase = '٠';
/*     */           break;
/*     */         
/*     */         case 256:
/* 511 */           digitBase = '۰';
/*     */           break;
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 518 */       switch (options & 0xE0) {
/*     */         case 32:
/* 520 */           digitDelta = digitBase - 48;
/* 521 */           for (i = offset; i < limit; i++) {
/* 522 */             char ch = text[i];
/* 523 */             if (ch <= '9' && ch >= '0') {
/* 524 */               text[i] = (char)(text[i] + digitDelta);
/*     */             }
/*     */           } 
/*     */           break;
/*     */ 
/*     */         
/*     */         case 64:
/* 531 */           digitTop = (char)(digitBase + 9);
/* 532 */           j = 48 - digitBase;
/* 533 */           for (k = offset; k < limit; k++) {
/* 534 */             char ch = text[k];
/* 535 */             if (ch <= digitTop && ch >= digitBase) {
/* 536 */               text[k] = (char)(text[k] + j);
/*     */             }
/*     */           } 
/*     */           break;
/*     */ 
/*     */         
/*     */         case 96:
/* 543 */           shapeToArabicDigitsWithContext(text, 0, length, digitBase, false);
/*     */           break;
/*     */         
/*     */         case 128:
/* 547 */           shapeToArabicDigitsWithContext(text, 0, length, digitBase, true);
/*     */           break;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void shapeToArabicDigitsWithContext(char[] dest, int start, int length, char digitBase, boolean lastStrongWasAL) {
/* 557 */     digitBase = (char)(digitBase - 48);
/*     */     
/* 559 */     int limit = start + length;
/* 560 */     for (int i = start; i < limit; i++) {
/* 561 */       char ch = dest[i];
/* 562 */       switch (BidiOrder.getDirection(ch)) {
/*     */         case 0:
/*     */         case 3:
/* 565 */           lastStrongWasAL = false;
/*     */           break;
/*     */         case 4:
/* 568 */           lastStrongWasAL = true;
/*     */           break;
/*     */         case 8:
/* 571 */           if (lastStrongWasAL && ch <= '9') {
/* 572 */             dest[i] = (char)(ch + digitBase);
/*     */           }
/*     */           break;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static Character getReverseMapping(char c) {
/* 582 */     return reverseLigatureMapTable.get(Character.valueOf(c));
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
/*     */ 
/*     */ 
/*     */   
/* 615 */   private static final char[][] chartable = new char[][] { { 'ء', 'ﺀ' }, { 'آ', 'ﺁ', 'ﺂ' }, { 'أ', 'ﺃ', 'ﺄ' }, { 'ؤ', 'ﺅ', 'ﺆ' }, { 'إ', 'ﺇ', 'ﺈ' }, { 'ئ', 'ﺉ', 'ﺊ', 'ﺋ', 'ﺌ' }, { 'ا', 'ﺍ', 'ﺎ' }, { 'ب', 'ﺏ', 'ﺐ', 'ﺑ', 'ﺒ' }, { 'ة', 'ﺓ', 'ﺔ' }, { 'ت', 'ﺕ', 'ﺖ', 'ﺗ', 'ﺘ' }, { 'ث', 'ﺙ', 'ﺚ', 'ﺛ', 'ﺜ' }, { 'ج', 'ﺝ', 'ﺞ', 'ﺟ', 'ﺠ' }, { 'ح', 'ﺡ', 'ﺢ', 'ﺣ', 'ﺤ' }, { 'خ', 'ﺥ', 'ﺦ', 'ﺧ', 'ﺨ' }, { 'د', 'ﺩ', 'ﺪ' }, { 'ذ', 'ﺫ', 'ﺬ' }, { 'ر', 'ﺭ', 'ﺮ' }, { 'ز', 'ﺯ', 'ﺰ' }, { 'س', 'ﺱ', 'ﺲ', 'ﺳ', 'ﺴ' }, { 'ش', 'ﺵ', 'ﺶ', 'ﺷ', 'ﺸ' }, { 'ص', 'ﺹ', 'ﺺ', 'ﺻ', 'ﺼ' }, { 'ض', 'ﺽ', 'ﺾ', 'ﺿ', 'ﻀ' }, { 'ط', 'ﻁ', 'ﻂ', 'ﻃ', 'ﻄ' }, { 'ظ', 'ﻅ', 'ﻆ', 'ﻇ', 'ﻈ' }, { 'ع', 'ﻉ', 'ﻊ', 'ﻋ', 'ﻌ' }, { 'غ', 'ﻍ', 'ﻎ', 'ﻏ', 'ﻐ' }, { 'ـ', 'ـ', 'ـ', 'ـ', 'ـ' }, { 'ف', 'ﻑ', 'ﻒ', 'ﻓ', 'ﻔ' }, { 'ق', 'ﻕ', 'ﻖ', 'ﻗ', 'ﻘ' }, { 'ك', 'ﻙ', 'ﻚ', 'ﻛ', 'ﻜ' }, { 'ل', 'ﻝ', 'ﻞ', 'ﻟ', 'ﻠ' }, { 'م', 'ﻡ', 'ﻢ', 'ﻣ', 'ﻤ' }, { 'ن', 'ﻥ', 'ﻦ', 'ﻧ', 'ﻨ' }, { 'ه', 'ﻩ', 'ﻪ', 'ﻫ', 'ﻬ' }, { 'و', 'ﻭ', 'ﻮ' }, { 'ى', 'ﻯ', 'ﻰ', 'ﯨ', 'ﯩ' }, { 'ي', 'ﻱ', 'ﻲ', 'ﻳ', 'ﻴ' }, { 'ٱ', 'ﭐ', 'ﭑ' }, { 'ٹ', 'ﭦ', 'ﭧ', 'ﭨ', 'ﭩ' }, { 'ٺ', 'ﭞ', 'ﭟ', 'ﭠ', 'ﭡ' }, { 'ٻ', 'ﭒ', 'ﭓ', 'ﭔ', 'ﭕ' }, { 'پ', 'ﭖ', 'ﭗ', 'ﭘ', 'ﭙ' }, { 'ٿ', 'ﭢ', 'ﭣ', 'ﭤ', 'ﭥ' }, { 'ڀ', 'ﭚ', 'ﭛ', 'ﭜ', 'ﭝ' }, { 'ڃ', 'ﭶ', 'ﭷ', 'ﭸ', 'ﭹ' }, { 'ڄ', 'ﭲ', 'ﭳ', 'ﭴ', 'ﭵ' }, { 'چ', 'ﭺ', 'ﭻ', 'ﭼ', 'ﭽ' }, { 'ڇ', 'ﭾ', 'ﭿ', 'ﮀ', 'ﮁ' }, { 'ڈ', 'ﮈ', 'ﮉ' }, { 'ڌ', 'ﮄ', 'ﮅ' }, { 'ڍ', 'ﮂ', 'ﮃ' }, { 'ڎ', 'ﮆ', 'ﮇ' }, { 'ڑ', 'ﮌ', 'ﮍ' }, { 'ژ', 'ﮊ', 'ﮋ' }, { 'ڤ', 'ﭪ', 'ﭫ', 'ﭬ', 'ﭭ' }, { 'ڦ', 'ﭮ', 'ﭯ', 'ﭰ', 'ﭱ' }, { 'ک', 'ﮎ', 'ﮏ', 'ﮐ', 'ﮑ' }, { 'ڭ', 'ﯓ', 'ﯔ', 'ﯕ', 'ﯖ' }, { 'گ', 'ﮒ', 'ﮓ', 'ﮔ', 'ﮕ' }, { 'ڱ', 'ﮚ', 'ﮛ', 'ﮜ', 'ﮝ' }, { 'ڳ', 'ﮖ', 'ﮗ', 'ﮘ', 'ﮙ' }, { 'ں', 'ﮞ', 'ﮟ' }, { 'ڻ', 'ﮠ', 'ﮡ', 'ﮢ', 'ﮣ' }, { 'ھ', 'ﮪ', 'ﮫ', 'ﮬ', 'ﮭ' }, { 'ۀ', 'ﮤ', 'ﮥ' }, { 'ہ', 'ﮦ', 'ﮧ', 'ﮨ', 'ﮩ' }, { 'ۅ', 'ﯠ', 'ﯡ' }, { 'ۆ', 'ﯙ', 'ﯚ' }, { 'ۇ', 'ﯗ', 'ﯘ' }, { 'ۈ', 'ﯛ', 'ﯜ' }, { 'ۉ', 'ﯢ', 'ﯣ' }, { 'ۋ', 'ﯞ', 'ﯟ' }, { 'ی', 'ﯼ', 'ﯽ', 'ﯾ', 'ﯿ' }, { 'ې', 'ﯤ', 'ﯥ', 'ﯦ', 'ﯧ' }, { 'ے', 'ﮮ', 'ﮯ' }, { 'ۓ', 'ﮰ', 'ﮱ' } };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int ar_nothing = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int ar_novowel = 1;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int ar_composedtashkeel = 4;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int ar_lig = 8;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int DIGITS_EN2AN = 32;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int DIGITS_AN2EN = 64;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int DIGITS_EN2AN_INIT_LR = 96;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int DIGITS_EN2AN_INIT_AL = 128;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int DIGITS_RESERVED = 160;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int DIGITS_MASK = 224;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int DIGIT_TYPE_AN = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int DIGIT_TYPE_AN_EXTENDED = 256;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int DIGIT_TYPE_MASK = 256;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static class charstruct
/*     */   {
/*     */     char basechar;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     char mark1;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     char vowel;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     int lignum;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 758 */     int numshapes = 1;
/*     */   }
/*     */ 
/*     */   
/* 762 */   protected int options = 0;
/* 763 */   protected int runDirection = 3;
/*     */ 
/*     */   
/*     */   public ArabicLigaturizer() {}
/*     */   
/*     */   public ArabicLigaturizer(int runDirection, int options) {
/* 769 */     this.runDirection = runDirection;
/* 770 */     this.options = options;
/*     */   }
/*     */   
/*     */   public String process(String s) {
/* 774 */     return BidiLine.processLTR(s, this.runDirection, this.options);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isRTL() {
/* 783 */     return true;
/*     */   }
/*     */   
/*     */   static {
/* 787 */     for (char[] c : chartable) {
/* 788 */       maptable.put(Character.valueOf(c[0]), c);
/* 789 */       switch (c.length) {
/*     */         
/*     */         case 5:
/* 792 */           reverseLigatureMapTable.put(Character.valueOf(c[4]), Character.valueOf(c[3]));
/*     */         case 3:
/* 794 */           reverseLigatureMapTable.put(Character.valueOf(c[2]), Character.valueOf(c[1]));
/* 795 */           reverseLigatureMapTable.put(Character.valueOf(c[1]), Character.valueOf(c[0]));
/*     */           break;
/*     */       } 
/* 798 */       if (c[0] == 'ط' || c[0] == 'ظ') {
/* 799 */         reverseLigatureMapTable.put(Character.valueOf(c[4]), Character.valueOf(c[1]));
/* 800 */         reverseLigatureMapTable.put(Character.valueOf(c[3]), Character.valueOf(c[1]));
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/languages/ArabicLigaturizer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */