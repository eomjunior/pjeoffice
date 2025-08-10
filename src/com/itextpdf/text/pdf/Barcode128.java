/*     */ package com.itextpdf.text.pdf;
/*     */ 
/*     */ import com.itextpdf.text.BaseColor;
/*     */ import com.itextpdf.text.ExceptionConverter;
/*     */ import com.itextpdf.text.Rectangle;
/*     */ import com.itextpdf.text.error_messages.MessageLocalization;
/*     */ import java.awt.Canvas;
/*     */ import java.awt.Color;
/*     */ import java.awt.Image;
/*     */ import java.awt.image.MemoryImageSource;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Barcode128
/*     */   extends Barcode
/*     */ {
/*  76 */   private static final byte[][] BARS = new byte[][] { { 2, 1, 2, 2, 2, 2 }, { 2, 2, 2, 1, 2, 2 }, { 2, 2, 2, 2, 2, 1 }, { 1, 2, 1, 2, 2, 3 }, { 1, 2, 1, 3, 2, 2 }, { 1, 3, 1, 2, 2, 2 }, { 1, 2, 2, 2, 1, 3 }, { 1, 2, 2, 3, 1, 2 }, { 1, 3, 2, 2, 1, 2 }, { 2, 2, 1, 2, 1, 3 }, { 2, 2, 1, 3, 1, 2 }, { 2, 3, 1, 2, 1, 2 }, { 1, 1, 2, 2, 3, 2 }, { 1, 2, 2, 1, 3, 2 }, { 1, 2, 2, 2, 3, 1 }, { 1, 1, 3, 2, 2, 2 }, { 1, 2, 3, 1, 2, 2 }, { 1, 2, 3, 2, 2, 1 }, { 2, 2, 3, 2, 1, 1 }, { 2, 2, 1, 1, 3, 2 }, { 2, 2, 1, 2, 3, 1 }, { 2, 1, 3, 2, 1, 2 }, { 2, 2, 3, 1, 1, 2 }, { 3, 1, 2, 1, 3, 1 }, { 3, 1, 1, 2, 2, 2 }, { 3, 2, 1, 1, 2, 2 }, { 3, 2, 1, 2, 2, 1 }, { 3, 1, 2, 2, 1, 2 }, { 3, 2, 2, 1, 1, 2 }, { 3, 2, 2, 2, 1, 1 }, { 2, 1, 2, 1, 2, 3 }, { 2, 1, 2, 3, 2, 1 }, { 2, 3, 2, 1, 2, 1 }, { 1, 1, 1, 3, 2, 3 }, { 1, 3, 1, 1, 2, 3 }, { 1, 3, 1, 3, 2, 1 }, { 1, 1, 2, 3, 1, 3 }, { 1, 3, 2, 1, 1, 3 }, { 1, 3, 2, 3, 1, 1 }, { 2, 1, 1, 3, 1, 3 }, { 2, 3, 1, 1, 1, 3 }, { 2, 3, 1, 3, 1, 1 }, { 1, 1, 2, 1, 3, 3 }, { 1, 1, 2, 3, 3, 1 }, { 1, 3, 2, 1, 3, 1 }, { 1, 1, 3, 1, 2, 3 }, { 1, 1, 3, 3, 2, 1 }, { 1, 3, 3, 1, 2, 1 }, { 3, 1, 3, 1, 2, 1 }, { 2, 1, 1, 3, 3, 1 }, { 2, 3, 1, 1, 3, 1 }, { 2, 1, 3, 1, 1, 3 }, { 2, 1, 3, 3, 1, 1 }, { 2, 1, 3, 1, 3, 1 }, { 3, 1, 1, 1, 2, 3 }, { 3, 1, 1, 3, 2, 1 }, { 3, 3, 1, 1, 2, 1 }, { 3, 1, 2, 1, 1, 3 }, { 3, 1, 2, 3, 1, 1 }, { 3, 3, 2, 1, 1, 1 }, { 3, 1, 4, 1, 1, 1 }, { 2, 2, 1, 4, 1, 1 }, { 4, 3, 1, 1, 1, 1 }, { 1, 1, 1, 2, 2, 4 }, { 1, 1, 1, 4, 2, 2 }, { 1, 2, 1, 1, 2, 4 }, { 1, 2, 1, 4, 2, 1 }, { 1, 4, 1, 1, 2, 2 }, { 1, 4, 1, 2, 2, 1 }, { 1, 1, 2, 2, 1, 4 }, { 1, 1, 2, 4, 1, 2 }, { 1, 2, 2, 1, 1, 4 }, { 1, 2, 2, 4, 1, 1 }, { 1, 4, 2, 1, 1, 2 }, { 1, 4, 2, 2, 1, 1 }, { 2, 4, 1, 2, 1, 1 }, { 2, 2, 1, 1, 1, 4 }, { 4, 1, 3, 1, 1, 1 }, { 2, 4, 1, 1, 1, 2 }, { 1, 3, 4, 1, 1, 1 }, { 1, 1, 1, 2, 4, 2 }, { 1, 2, 1, 1, 4, 2 }, { 1, 2, 1, 2, 4, 1 }, { 1, 1, 4, 2, 1, 2 }, { 1, 2, 4, 1, 1, 2 }, { 1, 2, 4, 2, 1, 1 }, { 4, 1, 1, 2, 1, 2 }, { 4, 2, 1, 1, 1, 2 }, { 4, 2, 1, 2, 1, 1 }, { 2, 1, 2, 1, 4, 1 }, { 2, 1, 4, 1, 2, 1 }, { 4, 1, 2, 1, 2, 1 }, { 1, 1, 1, 1, 4, 3 }, { 1, 1, 1, 3, 4, 1 }, { 1, 3, 1, 1, 4, 1 }, { 1, 1, 4, 1, 1, 3 }, { 1, 1, 4, 3, 1, 1 }, { 4, 1, 1, 1, 1, 3 }, { 4, 1, 1, 3, 1, 1 }, { 1, 1, 3, 1, 4, 1 }, { 1, 1, 4, 1, 3, 1 }, { 3, 1, 1, 1, 4, 1 }, { 4, 1, 1, 1, 3, 1 }, { 2, 1, 1, 4, 1, 2 }, { 2, 1, 1, 2, 1, 4 }, { 2, 1, 1, 2, 3, 2 } };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 188 */   private static final byte[] BARS_STOP = new byte[] { 2, 3, 3, 1, 1, 1, 2 };
/*     */   
/*     */   public static final char CODE_AB_TO_C = 'c';
/*     */   
/*     */   public static final char CODE_AC_TO_B = 'd';
/*     */   
/*     */   public static final char CODE_BC_TO_A = 'e';
/*     */   
/*     */   public static final char FNC1_INDEX = 'f';
/*     */   
/*     */   public static final char START_A = 'g';
/*     */   
/*     */   public static final char START_B = 'h';
/*     */   
/*     */   public static final char START_C = 'i';
/*     */   
/*     */   public static final char FNC1 = 'Ê';
/*     */   
/*     */   public static final char DEL = 'Ã';
/*     */   
/*     */   public static final char FNC3 = 'Ä';
/*     */   
/*     */   public static final char FNC2 = 'Å';
/*     */   
/*     */   public static final char SHIFT = 'Æ';
/*     */   
/*     */   public static final char CODE_C = 'Ç';
/*     */   
/*     */   public static final char CODE_A = 'È';
/*     */   
/*     */   public static final char FNC4 = 'È';
/*     */   
/*     */   public static final char STARTA = 'Ë';
/*     */   public static final char STARTB = 'Ì';
/*     */   public static final char STARTC = 'Í';
/* 223 */   private static final IntHashtable ais = new IntHashtable();
/*     */   
/*     */   public Barcode128() {
/*     */     try {
/* 227 */       this.x = 0.8F;
/* 228 */       this.font = BaseFont.createFont("Helvetica", "winansi", false);
/* 229 */       this.size = 8.0F;
/* 230 */       this.baseline = this.size;
/* 231 */       this.barHeight = this.size * 3.0F;
/* 232 */       this.textAlignment = 1;
/* 233 */       this.codeType = 9;
/*     */     }
/* 235 */     catch (Exception e) {
/* 236 */       throw new ExceptionConverter(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public enum Barcode128CodeSet {
/* 241 */     A,
/* 242 */     B,
/* 243 */     C,
/* 244 */     AUTO;
/*     */     
/*     */     public char getStartSymbol() {
/* 247 */       switch (this) {
/*     */         case A:
/* 249 */           return 'g';
/*     */         case B:
/* 251 */           return 'h';
/*     */         case C:
/* 253 */           return 'i';
/*     */       } 
/* 255 */       return 'h';
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCodeSet(Barcode128CodeSet codeSet) {
/* 261 */     this.codeSet = codeSet;
/*     */   }
/*     */   
/*     */   public Barcode128CodeSet getCodeSet() {
/* 265 */     return this.codeSet;
/*     */   }
/*     */   
/* 268 */   private Barcode128CodeSet codeSet = Barcode128CodeSet.AUTO;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String removeFNC1(String code) {
/* 276 */     int len = code.length();
/* 277 */     StringBuffer buf = new StringBuffer(len);
/* 278 */     for (int k = 0; k < len; k++) {
/* 279 */       char c = code.charAt(k);
/* 280 */       if (c >= ' ' && c <= '~')
/* 281 */         buf.append(c); 
/*     */     } 
/* 283 */     return buf.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getHumanReadableUCCEAN(String code) {
/* 292 */     StringBuffer buf = new StringBuffer();
/* 293 */     String fnc1 = String.valueOf('Ê');
/*     */     try {
/*     */       while (true) {
/* 296 */         while (code.startsWith(fnc1)) {
/* 297 */           code = code.substring(1);
/*     */         }
/*     */         
/* 300 */         int n = 0;
/* 301 */         int idlen = 0;
/* 302 */         for (int k = 2; k < 5 && 
/* 303 */           code.length() >= k; k++) {
/*     */           
/* 305 */           if ((n = ais.get(Integer.parseInt(code.substring(0, k)))) != 0) {
/* 306 */             idlen = k;
/*     */             break;
/*     */           } 
/*     */         } 
/* 310 */         if (idlen == 0)
/*     */           break; 
/* 312 */         buf.append('(').append(code.substring(0, idlen)).append(')');
/* 313 */         code = code.substring(idlen);
/* 314 */         if (n > 0) {
/* 315 */           n -= idlen;
/* 316 */           if (code.length() <= n)
/*     */             break; 
/* 318 */           buf.append(removeFNC1(code.substring(0, n)));
/* 319 */           code = code.substring(n);
/*     */           continue;
/*     */         } 
/* 322 */         int idx = code.indexOf('Ê');
/* 323 */         if (idx < 0)
/*     */           break; 
/* 325 */         buf.append(code.substring(0, idx));
/* 326 */         code = code.substring(idx + 1);
/*     */       }
/*     */     
/*     */     }
/* 330 */     catch (Exception exception) {}
/*     */ 
/*     */     
/* 333 */     buf.append(removeFNC1(code));
/* 334 */     return buf.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static boolean isNextDigits(String text, int textIndex, int numDigits) {
/* 345 */     int len = text.length();
/* 346 */     while (textIndex < len && numDigits > 0) {
/* 347 */       if (text.charAt(textIndex) == 'Ê') {
/* 348 */         textIndex++;
/*     */         continue;
/*     */       } 
/* 351 */       int n = Math.min(2, numDigits);
/* 352 */       if (textIndex + n > len)
/* 353 */         return false; 
/* 354 */       while (n-- > 0) {
/* 355 */         char c = text.charAt(textIndex++);
/* 356 */         if (c < '0' || c > '9')
/* 357 */           return false; 
/* 358 */         numDigits--;
/*     */       } 
/*     */     } 
/* 361 */     return (numDigits == 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static String getPackedRawDigits(String text, int textIndex, int numDigits) {
/* 372 */     StringBuilder out = new StringBuilder("");
/* 373 */     int start = textIndex;
/* 374 */     while (numDigits > 0) {
/* 375 */       if (text.charAt(textIndex) == 'Ê') {
/* 376 */         out.append('f');
/* 377 */         textIndex++;
/*     */         continue;
/*     */       } 
/* 380 */       numDigits -= 2;
/* 381 */       int c1 = text.charAt(textIndex++) - 48;
/* 382 */       int c2 = text.charAt(textIndex++) - 48;
/* 383 */       out.append((char)(c1 * 10 + c2));
/*     */     } 
/* 385 */     return (char)(textIndex - start) + out.toString();
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
/*     */   public static String getRawText(String text, boolean ucc, Barcode128CodeSet codeSet) {
/* 397 */     String out = "";
/* 398 */     int tLen = text.length();
/* 399 */     if (tLen == 0) {
/* 400 */       out = out + codeSet.getStartSymbol();
/* 401 */       if (ucc)
/* 402 */         out = out + 'f'; 
/* 403 */       return out;
/*     */     } 
/* 405 */     int c = 0;
/* 406 */     for (int k = 0; k < tLen; k++) {
/* 407 */       c = text.charAt(k);
/* 408 */       if (c > 127 && c != 202)
/* 409 */         throw new RuntimeException(MessageLocalization.getComposedMessage("there.are.illegal.characters.for.barcode.128.in.1", new Object[] { text })); 
/*     */     } 
/* 411 */     c = text.charAt(0);
/* 412 */     char currentCode = codeSet.getStartSymbol();
/* 413 */     int index = 0;
/* 414 */     if ((codeSet == Barcode128CodeSet.AUTO || codeSet == Barcode128CodeSet.C) && isNextDigits(text, index, 2)) {
/* 415 */       currentCode = 'i';
/* 416 */       out = out + currentCode;
/* 417 */       if (ucc)
/* 418 */         out = out + 'f'; 
/* 419 */       String out2 = getPackedRawDigits(text, index, 2);
/* 420 */       index += out2.charAt(0);
/* 421 */       out = out + out2.substring(1);
/*     */     }
/* 423 */     else if (c < 32) {
/* 424 */       currentCode = 'g';
/* 425 */       out = out + currentCode;
/* 426 */       if (ucc)
/* 427 */         out = out + 'f'; 
/* 428 */       out = out + (char)(c + 64);
/* 429 */       index++;
/*     */     } else {
/*     */       
/* 432 */       out = out + currentCode;
/* 433 */       if (ucc)
/* 434 */         out = out + 'f'; 
/* 435 */       if (c == 202) {
/* 436 */         out = out + 'f';
/*     */       } else {
/* 438 */         out = out + (char)(c - 32);
/* 439 */       }  index++;
/*     */     } 
/* 441 */     if (codeSet != Barcode128CodeSet.AUTO && currentCode != codeSet.getStartSymbol())
/* 442 */       throw new RuntimeException(MessageLocalization.getComposedMessage("there.are.illegal.characters.for.barcode.128.in.1", new Object[] { text })); 
/* 443 */     while (index < tLen) {
/* 444 */       switch (currentCode) {
/*     */         case 'g':
/* 446 */           if (codeSet == Barcode128CodeSet.AUTO && isNextDigits(text, index, 4)) {
/* 447 */             currentCode = 'i';
/* 448 */             out = out + 'c';
/* 449 */             String out2 = getPackedRawDigits(text, index, 4);
/* 450 */             index += out2.charAt(0);
/* 451 */             out = out + out2.substring(1);
/*     */             break;
/*     */           } 
/* 454 */           c = text.charAt(index++);
/* 455 */           if (c == 202) {
/* 456 */             out = out + 'f'; break;
/* 457 */           }  if (c > 95) {
/* 458 */             currentCode = 'h';
/* 459 */             out = out + 'd';
/* 460 */             out = out + (char)(c - 32); break;
/*     */           } 
/* 462 */           if (c < 32) {
/* 463 */             out = out + (char)(c + 64); break;
/*     */           } 
/* 465 */           out = out + (char)(c - 32);
/*     */           break;
/*     */ 
/*     */         
/*     */         case 'h':
/* 470 */           if (codeSet == Barcode128CodeSet.AUTO && isNextDigits(text, index, 4)) {
/* 471 */             currentCode = 'i';
/* 472 */             out = out + 'c';
/* 473 */             String out2 = getPackedRawDigits(text, index, 4);
/* 474 */             index += out2.charAt(0);
/* 475 */             out = out + out2.substring(1);
/*     */             break;
/*     */           } 
/* 478 */           c = text.charAt(index++);
/* 479 */           if (c == 202) {
/* 480 */             out = out + 'f'; break;
/* 481 */           }  if (c < 32) {
/* 482 */             currentCode = 'g';
/* 483 */             out = out + 'e';
/* 484 */             out = out + (char)(c + 64);
/*     */             break;
/*     */           } 
/* 487 */           out = out + (char)(c - 32);
/*     */           break;
/*     */ 
/*     */ 
/*     */         
/*     */         case 'i':
/* 493 */           if (isNextDigits(text, index, 2)) {
/* 494 */             String out2 = getPackedRawDigits(text, index, 2);
/* 495 */             index += out2.charAt(0);
/* 496 */             out = out + out2.substring(1);
/*     */             break;
/*     */           } 
/* 499 */           c = text.charAt(index++);
/* 500 */           if (c == 202) {
/* 501 */             out = out + 'f'; break;
/* 502 */           }  if (c < 32) {
/* 503 */             currentCode = 'g';
/* 504 */             out = out + 'e';
/* 505 */             out = out + (char)(c + 64);
/*     */             break;
/*     */           } 
/* 508 */           currentCode = 'h';
/* 509 */           out = out + 'd';
/* 510 */           out = out + (char)(c - 32);
/*     */           break;
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 516 */       if (codeSet != Barcode128CodeSet.AUTO && currentCode != codeSet.getStartSymbol())
/* 517 */         throw new RuntimeException(MessageLocalization.getComposedMessage("there.are.illegal.characters.for.barcode.128.in.1", new Object[] { text })); 
/*     */     } 
/* 519 */     return out;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getRawText(String text, boolean ucc) {
/* 530 */     return getRawText(text, ucc, Barcode128CodeSet.AUTO);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] getBarsCode128Raw(String text) {
/* 539 */     int idx = text.indexOf('￿');
/* 540 */     if (idx >= 0)
/* 541 */       text = text.substring(0, idx); 
/* 542 */     int chk = text.charAt(0);
/* 543 */     for (int k = 1; k < text.length(); k++)
/* 544 */       chk += k * text.charAt(k); 
/* 545 */     chk %= 103;
/* 546 */     text = text + (char)chk;
/* 547 */     byte[] bars = new byte[(text.length() + 1) * 6 + 7];
/*     */     int i;
/* 549 */     for (i = 0; i < text.length(); i++)
/* 550 */       System.arraycopy(BARS[text.charAt(i)], 0, bars, i * 6, 6); 
/* 551 */     System.arraycopy(BARS_STOP, 0, bars, i * 6, 7);
/* 552 */     return bars;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Rectangle getBarcodeSize() {
/*     */     String fullCode;
/* 560 */     float fontX = 0.0F;
/* 561 */     float fontY = 0.0F;
/*     */     
/* 563 */     if (this.font != null) {
/* 564 */       if (this.baseline > 0.0F) {
/* 565 */         fontY = this.baseline - this.font.getFontDescriptor(3, this.size);
/*     */       } else {
/* 567 */         fontY = -this.baseline + this.size;
/* 568 */       }  if (this.codeType == 11) {
/* 569 */         int idx = this.code.indexOf('￿');
/* 570 */         if (idx < 0) {
/* 571 */           fullCode = "";
/*     */         } else {
/* 573 */           fullCode = this.code.substring(idx + 1);
/*     */         } 
/* 575 */       } else if (this.codeType == 10) {
/* 576 */         fullCode = getHumanReadableUCCEAN(this.code);
/*     */       } else {
/* 578 */         fullCode = removeFNC1(this.code);
/* 579 */       }  fontX = this.font.getWidthPoint((this.altText != null) ? this.altText : fullCode, this.size);
/*     */     } 
/* 581 */     if (this.codeType == 11) {
/* 582 */       int idx = this.code.indexOf('￿');
/* 583 */       if (idx >= 0) {
/* 584 */         fullCode = this.code.substring(0, idx);
/*     */       } else {
/* 586 */         fullCode = this.code;
/*     */       } 
/*     */     } else {
/* 589 */       fullCode = getRawText(this.code, (this.codeType == 10), this.codeSet);
/*     */     } 
/* 591 */     int len = fullCode.length();
/* 592 */     float fullWidth = ((len + 2) * 11) * this.x + 2.0F * this.x;
/* 593 */     fullWidth = Math.max(fullWidth, fontX);
/* 594 */     float fullHeight = this.barHeight + fontY;
/* 595 */     return new Rectangle(fullWidth, fullHeight);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Rectangle placeBarcode(PdfContentByte cb, BaseColor barColor, BaseColor textColor) {
/*     */     String fullCode, bCode;
/* 636 */     if (this.codeType == 11) {
/* 637 */       int idx = this.code.indexOf('￿');
/* 638 */       if (idx < 0) {
/* 639 */         fullCode = "";
/*     */       } else {
/* 641 */         fullCode = this.code.substring(idx + 1);
/*     */       } 
/* 643 */     } else if (this.codeType == 10) {
/* 644 */       fullCode = getHumanReadableUCCEAN(this.code);
/*     */     } else {
/* 646 */       fullCode = removeFNC1(this.code);
/* 647 */     }  float fontX = 0.0F;
/* 648 */     if (this.font != null) {
/* 649 */       fontX = this.font.getWidthPoint(fullCode = (this.altText != null) ? this.altText : fullCode, this.size);
/*     */     }
/*     */     
/* 652 */     if (this.codeType == 11) {
/* 653 */       int idx = this.code.indexOf('￿');
/* 654 */       if (idx >= 0) {
/* 655 */         bCode = this.code.substring(0, idx);
/*     */       } else {
/* 657 */         bCode = this.code;
/*     */       } 
/*     */     } else {
/* 660 */       bCode = getRawText(this.code, (this.codeType == 10), this.codeSet);
/*     */     } 
/* 662 */     int len = bCode.length();
/* 663 */     float fullWidth = ((len + 2) * 11) * this.x + 2.0F * this.x;
/* 664 */     float barStartX = 0.0F;
/* 665 */     float textStartX = 0.0F;
/* 666 */     switch (this.textAlignment) {
/*     */       case 0:
/*     */         break;
/*     */       case 2:
/* 670 */         if (fontX > fullWidth) {
/* 671 */           barStartX = fontX - fullWidth; break;
/*     */         } 
/* 673 */         textStartX = fullWidth - fontX;
/*     */         break;
/*     */       default:
/* 676 */         if (fontX > fullWidth) {
/* 677 */           barStartX = (fontX - fullWidth) / 2.0F; break;
/*     */         } 
/* 679 */         textStartX = (fullWidth - fontX) / 2.0F;
/*     */         break;
/*     */     } 
/* 682 */     float barStartY = 0.0F;
/* 683 */     float textStartY = 0.0F;
/* 684 */     if (this.font != null) {
/* 685 */       if (this.baseline <= 0.0F) {
/* 686 */         textStartY = this.barHeight - this.baseline;
/*     */       } else {
/* 688 */         textStartY = -this.font.getFontDescriptor(3, this.size);
/* 689 */         barStartY = textStartY + this.baseline;
/*     */       } 
/*     */     }
/* 692 */     byte[] bars = getBarsCode128Raw(bCode);
/* 693 */     boolean print = true;
/* 694 */     if (barColor != null)
/* 695 */       cb.setColorFill(barColor); 
/* 696 */     for (int k = 0; k < bars.length; k++) {
/* 697 */       float w = bars[k] * this.x;
/* 698 */       if (print)
/* 699 */         cb.rectangle(barStartX, barStartY, w - this.inkSpreading, this.barHeight); 
/* 700 */       print = !print;
/* 701 */       barStartX += w;
/*     */     } 
/* 703 */     cb.fill();
/* 704 */     if (this.font != null) {
/* 705 */       if (textColor != null)
/* 706 */         cb.setColorFill(textColor); 
/* 707 */       cb.beginText();
/* 708 */       cb.setFontAndSize(this.font, this.size);
/* 709 */       cb.setTextMatrix(textStartX, textStartY);
/* 710 */       cb.showText(fullCode);
/* 711 */       cb.endText();
/*     */     } 
/* 713 */     return getBarcodeSize();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCode(String code) {
/* 724 */     if (getCodeType() == 10 && code.startsWith("(")) {
/* 725 */       int idx = 0;
/* 726 */       StringBuilder ret = new StringBuilder("");
/* 727 */       while (idx >= 0) {
/* 728 */         int end = code.indexOf(')', idx);
/* 729 */         if (end < 0)
/* 730 */           throw new IllegalArgumentException(MessageLocalization.getComposedMessage("badly.formed.ucc.string.1", new Object[] { code })); 
/* 731 */         String sai = code.substring(idx + 1, end);
/* 732 */         if (sai.length() < 2)
/* 733 */           throw new IllegalArgumentException(MessageLocalization.getComposedMessage("ai.too.short.1", new Object[] { sai })); 
/* 734 */         int ai = Integer.parseInt(sai);
/* 735 */         int len = ais.get(ai);
/* 736 */         if (len == 0)
/* 737 */           throw new IllegalArgumentException(MessageLocalization.getComposedMessage("ai.not.found.1", new Object[] { sai })); 
/* 738 */         sai = String.valueOf(ai);
/* 739 */         if (sai.length() == 1)
/* 740 */           sai = "0" + sai; 
/* 741 */         idx = code.indexOf('(', end);
/* 742 */         int next = (idx < 0) ? code.length() : idx;
/* 743 */         ret.append(sai).append(code.substring(end + 1, next));
/* 744 */         if (len < 0) {
/* 745 */           if (idx >= 0)
/* 746 */             ret.append('Ê');  continue;
/*     */         } 
/* 748 */         if (next - end - 1 + sai.length() != len)
/* 749 */           throw new IllegalArgumentException(MessageLocalization.getComposedMessage("invalid.ai.length.1", new Object[] { sai })); 
/*     */       } 
/* 751 */       super.setCode(ret.toString());
/*     */     } else {
/*     */       
/* 754 */       super.setCode(code);
/*     */     } 
/*     */   }
/*     */   static {
/* 758 */     ais.put(0, 20);
/* 759 */     ais.put(1, 16);
/* 760 */     ais.put(2, 16);
/* 761 */     ais.put(10, -1);
/* 762 */     ais.put(11, 9);
/* 763 */     ais.put(12, 8);
/* 764 */     ais.put(13, 8);
/* 765 */     ais.put(15, 8);
/* 766 */     ais.put(17, 8);
/* 767 */     ais.put(20, 4);
/* 768 */     ais.put(21, -1);
/* 769 */     ais.put(22, -1);
/* 770 */     ais.put(23, -1);
/* 771 */     ais.put(240, -1);
/* 772 */     ais.put(241, -1);
/* 773 */     ais.put(250, -1);
/* 774 */     ais.put(251, -1);
/* 775 */     ais.put(252, -1);
/* 776 */     ais.put(30, -1); int k;
/* 777 */     for (k = 3100; k < 3700; k++)
/* 778 */       ais.put(k, 10); 
/* 779 */     ais.put(37, -1);
/* 780 */     for (k = 3900; k < 3940; k++)
/* 781 */       ais.put(k, -1); 
/* 782 */     ais.put(400, -1);
/* 783 */     ais.put(401, -1);
/* 784 */     ais.put(402, 20);
/* 785 */     ais.put(403, -1);
/* 786 */     for (k = 410; k < 416; k++)
/* 787 */       ais.put(k, 16); 
/* 788 */     ais.put(420, -1);
/* 789 */     ais.put(421, -1);
/* 790 */     ais.put(422, 6);
/* 791 */     ais.put(423, -1);
/* 792 */     ais.put(424, 6);
/* 793 */     ais.put(425, 6);
/* 794 */     ais.put(426, 6);
/* 795 */     ais.put(7001, 17);
/* 796 */     ais.put(7002, -1);
/* 797 */     for (k = 7030; k < 7040; k++)
/* 798 */       ais.put(k, -1); 
/* 799 */     ais.put(8001, 18);
/* 800 */     ais.put(8002, -1);
/* 801 */     ais.put(8003, -1);
/* 802 */     ais.put(8004, -1);
/* 803 */     ais.put(8005, 10);
/* 804 */     ais.put(8006, 22);
/* 805 */     ais.put(8007, -1);
/* 806 */     ais.put(8008, -1);
/* 807 */     ais.put(8018, 22);
/* 808 */     ais.put(8020, -1);
/* 809 */     ais.put(8100, 10);
/* 810 */     ais.put(8101, 14);
/* 811 */     ais.put(8102, 6);
/* 812 */     for (k = 90; k < 100; k++) {
/* 813 */       ais.put(k, -1);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Image createAwtImage(Color foreground, Color background) {
/*     */     String bCode;
/* 825 */     int f = foreground.getRGB();
/* 826 */     int g = background.getRGB();
/* 827 */     Canvas canvas = new Canvas();
/*     */     
/* 829 */     if (this.codeType == 11) {
/* 830 */       int idx = this.code.indexOf('￿');
/* 831 */       if (idx >= 0) {
/* 832 */         bCode = this.code.substring(0, idx);
/*     */       } else {
/* 834 */         bCode = this.code;
/*     */       } 
/*     */     } else {
/* 837 */       bCode = getRawText(this.code, (this.codeType == 10));
/*     */     } 
/* 839 */     int len = bCode.length();
/* 840 */     int fullWidth = (len + 2) * 11 + 2;
/* 841 */     byte[] bars = getBarsCode128Raw(bCode);
/*     */     
/* 843 */     boolean print = true;
/* 844 */     int ptr = 0;
/* 845 */     int height = (int)this.barHeight;
/* 846 */     int[] pix = new int[fullWidth * height]; int k;
/* 847 */     for (k = 0; k < bars.length; k++) {
/* 848 */       int w = bars[k];
/* 849 */       int c = g;
/* 850 */       if (print)
/* 851 */         c = f; 
/* 852 */       print = !print;
/* 853 */       for (int j = 0; j < w; j++)
/* 854 */         pix[ptr++] = c; 
/*     */     } 
/* 856 */     for (k = fullWidth; k < pix.length; k += fullWidth) {
/* 857 */       System.arraycopy(pix, 0, pix, k, fullWidth);
/*     */     }
/* 859 */     Image img = canvas.createImage(new MemoryImageSource(fullWidth, height, pix, 0, fullWidth));
/*     */     
/* 861 */     return img;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/Barcode128.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */