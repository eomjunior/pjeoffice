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
/*     */ import java.util.Arrays;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BarcodeEAN
/*     */   extends Barcode
/*     */ {
/*  71 */   private static final int[] GUARD_EMPTY = new int[0];
/*     */   
/*  73 */   private static final int[] GUARD_UPCA = new int[] { 0, 2, 4, 6, 28, 30, 52, 54, 56, 58 };
/*     */   
/*  75 */   private static final int[] GUARD_EAN13 = new int[] { 0, 2, 28, 30, 56, 58 };
/*     */   
/*  77 */   private static final int[] GUARD_EAN8 = new int[] { 0, 2, 20, 22, 40, 42 };
/*     */   
/*  79 */   private static final int[] GUARD_UPCE = new int[] { 0, 2, 28, 30, 32 };
/*     */   
/*  81 */   private static final float[] TEXTPOS_EAN13 = new float[] { 6.5F, 13.5F, 20.5F, 27.5F, 34.5F, 41.5F, 53.5F, 60.5F, 67.5F, 74.5F, 81.5F, 88.5F };
/*     */   
/*  83 */   private static final float[] TEXTPOS_EAN8 = new float[] { 6.5F, 13.5F, 20.5F, 27.5F, 39.5F, 46.5F, 53.5F, 60.5F };
/*     */   
/*  85 */   private static final byte[][] BARS = new byte[][] { { 3, 2, 1, 1 }, { 2, 2, 2, 1 }, { 2, 1, 2, 2 }, { 1, 4, 1, 1 }, { 1, 1, 3, 2 }, { 1, 2, 3, 1 }, { 1, 1, 1, 4 }, { 1, 3, 1, 2 }, { 1, 2, 1, 3 }, { 3, 1, 1, 2 } };
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int TOTALBARS_EAN13 = 59;
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int TOTALBARS_EAN8 = 43;
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int TOTALBARS_UPCE = 33;
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int TOTALBARS_SUPP2 = 13;
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int TOTALBARS_SUPP5 = 31;
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int ODD = 0;
/*     */ 
/*     */   
/*     */   private static final int EVEN = 1;
/*     */ 
/*     */   
/* 115 */   private static final byte[][] PARITY13 = new byte[][] { { 0, 0, 0, 0, 0, 0 }, { 0, 0, 1, 0, 1, 1 }, { 0, 0, 1, 1, 0, 1 }, { 0, 0, 1, 1, 1, 0 }, { 0, 1, 0, 0, 1, 1 }, { 0, 1, 1, 0, 0, 1 }, { 0, 1, 1, 1, 0, 0 }, { 0, 1, 0, 1, 0, 1 }, { 0, 1, 0, 1, 1, 0 }, { 0, 1, 1, 0, 1, 0 } };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 130 */   private static final byte[][] PARITY2 = new byte[][] { { 0, 0 }, { 0, 1 }, { 1, 0 }, { 1, 1 } };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 139 */   private static final byte[][] PARITY5 = new byte[][] { { 1, 1, 0, 0, 0 }, { 1, 0, 1, 0, 0 }, { 1, 0, 0, 1, 0 }, { 1, 0, 0, 0, 1 }, { 0, 1, 1, 0, 0 }, { 0, 0, 1, 1, 0 }, { 0, 0, 0, 1, 1 }, { 0, 1, 0, 1, 0 }, { 0, 1, 0, 0, 1 }, { 0, 0, 1, 0, 1 } };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 154 */   private static final byte[][] PARITYE = new byte[][] { { 1, 1, 1, 0, 0, 0 }, { 1, 1, 0, 1, 0, 0 }, { 1, 1, 0, 0, 1, 0 }, { 1, 1, 0, 0, 0, 1 }, { 1, 0, 1, 1, 0, 0 }, { 1, 0, 0, 1, 1, 0 }, { 1, 0, 0, 0, 1, 1 }, { 1, 0, 1, 0, 1, 0 }, { 1, 0, 1, 0, 0, 1 }, { 1, 0, 0, 1, 0, 1 } };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BarcodeEAN() {
/*     */     try {
/* 171 */       this.x = 0.8F;
/* 172 */       this.font = BaseFont.createFont("Helvetica", "winansi", false);
/* 173 */       this.size = 8.0F;
/* 174 */       this.baseline = this.size;
/* 175 */       this.barHeight = this.size * 3.0F;
/* 176 */       this.guardBars = true;
/* 177 */       this.codeType = 1;
/* 178 */       this.code = "";
/*     */     }
/* 180 */     catch (Exception e) {
/* 181 */       throw new ExceptionConverter(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int calculateEANParity(String code) {
/* 190 */     int mul = 3;
/* 191 */     int total = 0;
/* 192 */     for (int k = code.length() - 1; k >= 0; k--) {
/* 193 */       int n = code.charAt(k) - 48;
/* 194 */       total += mul * n;
/* 195 */       mul ^= 0x2;
/*     */     } 
/* 197 */     return (10 - total % 10) % 10;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String convertUPCAtoUPCE(String text) {
/* 207 */     if (text.length() != 12 || (!text.startsWith("0") && !text.startsWith("1")))
/* 208 */       return null; 
/* 209 */     if (text.substring(3, 6).equals("000") || text.substring(3, 6).equals("100") || text
/* 210 */       .substring(3, 6).equals("200")) {
/* 211 */       if (text.substring(6, 8).equals("00")) {
/* 212 */         return text.substring(0, 1) + text.substring(1, 3) + text.substring(8, 11) + text.substring(3, 4) + text.substring(11);
/*     */       }
/* 214 */     } else if (text.substring(4, 6).equals("00")) {
/* 215 */       if (text.substring(6, 9).equals("000")) {
/* 216 */         return text.substring(0, 1) + text.substring(1, 4) + text.substring(9, 11) + "3" + text.substring(11);
/*     */       }
/* 218 */     } else if (text.substring(5, 6).equals("0")) {
/* 219 */       if (text.substring(6, 10).equals("0000")) {
/* 220 */         return text.substring(0, 1) + text.substring(1, 5) + text.substring(10, 11) + "4" + text.substring(11);
/*     */       }
/* 222 */     } else if (text.charAt(10) >= '5' && 
/* 223 */       text.substring(6, 10).equals("0000")) {
/* 224 */       return text.substring(0, 1) + text.substring(1, 6) + text.substring(10, 11) + text.substring(11);
/*     */     } 
/* 226 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] getBarsEAN13(String _code) {
/* 234 */     int[] code = new int[_code.length()];
/* 235 */     for (int k = 0; k < code.length; k++)
/* 236 */       code[k] = _code.charAt(k) - 48; 
/* 237 */     byte[] bars = new byte[59];
/* 238 */     int pb = 0;
/* 239 */     bars[pb++] = 1;
/* 240 */     bars[pb++] = 1;
/* 241 */     bars[pb++] = 1;
/* 242 */     byte[] sequence = PARITY13[code[0]]; int i;
/* 243 */     for (i = 0; i < sequence.length; i++) {
/* 244 */       int c = code[i + 1];
/* 245 */       byte[] stripes = BARS[c];
/* 246 */       if (sequence[i] == 0) {
/* 247 */         bars[pb++] = stripes[0];
/* 248 */         bars[pb++] = stripes[1];
/* 249 */         bars[pb++] = stripes[2];
/* 250 */         bars[pb++] = stripes[3];
/*     */       } else {
/*     */         
/* 253 */         bars[pb++] = stripes[3];
/* 254 */         bars[pb++] = stripes[2];
/* 255 */         bars[pb++] = stripes[1];
/* 256 */         bars[pb++] = stripes[0];
/*     */       } 
/*     */     } 
/* 259 */     bars[pb++] = 1;
/* 260 */     bars[pb++] = 1;
/* 261 */     bars[pb++] = 1;
/* 262 */     bars[pb++] = 1;
/* 263 */     bars[pb++] = 1;
/* 264 */     for (i = 7; i < 13; i++) {
/* 265 */       int c = code[i];
/* 266 */       byte[] stripes = BARS[c];
/* 267 */       bars[pb++] = stripes[0];
/* 268 */       bars[pb++] = stripes[1];
/* 269 */       bars[pb++] = stripes[2];
/* 270 */       bars[pb++] = stripes[3];
/*     */     } 
/* 272 */     bars[pb++] = 1;
/* 273 */     bars[pb++] = 1;
/* 274 */     bars[pb++] = 1;
/* 275 */     return bars;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] getBarsEAN8(String _code) {
/* 283 */     int[] code = new int[_code.length()];
/* 284 */     for (int k = 0; k < code.length; k++)
/* 285 */       code[k] = _code.charAt(k) - 48; 
/* 286 */     byte[] bars = new byte[43];
/* 287 */     int pb = 0;
/* 288 */     bars[pb++] = 1;
/* 289 */     bars[pb++] = 1;
/* 290 */     bars[pb++] = 1; int i;
/* 291 */     for (i = 0; i < 4; i++) {
/* 292 */       int c = code[i];
/* 293 */       byte[] stripes = BARS[c];
/* 294 */       bars[pb++] = stripes[0];
/* 295 */       bars[pb++] = stripes[1];
/* 296 */       bars[pb++] = stripes[2];
/* 297 */       bars[pb++] = stripes[3];
/*     */     } 
/* 299 */     bars[pb++] = 1;
/* 300 */     bars[pb++] = 1;
/* 301 */     bars[pb++] = 1;
/* 302 */     bars[pb++] = 1;
/* 303 */     bars[pb++] = 1;
/* 304 */     for (i = 4; i < 8; i++) {
/* 305 */       int c = code[i];
/* 306 */       byte[] stripes = BARS[c];
/* 307 */       bars[pb++] = stripes[0];
/* 308 */       bars[pb++] = stripes[1];
/* 309 */       bars[pb++] = stripes[2];
/* 310 */       bars[pb++] = stripes[3];
/*     */     } 
/* 312 */     bars[pb++] = 1;
/* 313 */     bars[pb++] = 1;
/* 314 */     bars[pb++] = 1;
/* 315 */     return bars;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] getBarsUPCE(String _code) {
/* 323 */     int[] code = new int[_code.length()];
/* 324 */     for (int k = 0; k < code.length; k++)
/* 325 */       code[k] = _code.charAt(k) - 48; 
/* 326 */     byte[] bars = new byte[33];
/* 327 */     boolean flip = (code[0] != 0);
/* 328 */     int pb = 0;
/* 329 */     bars[pb++] = 1;
/* 330 */     bars[pb++] = 1;
/* 331 */     bars[pb++] = 1;
/* 332 */     byte[] sequence = PARITYE[code[code.length - 1]];
/* 333 */     for (int i = 1; i < code.length - 1; i++) {
/* 334 */       int c = code[i];
/* 335 */       byte[] stripes = BARS[c];
/* 336 */       if (sequence[i - 1] == (flip ? 1 : 0)) {
/* 337 */         bars[pb++] = stripes[0];
/* 338 */         bars[pb++] = stripes[1];
/* 339 */         bars[pb++] = stripes[2];
/* 340 */         bars[pb++] = stripes[3];
/*     */       } else {
/*     */         
/* 343 */         bars[pb++] = stripes[3];
/* 344 */         bars[pb++] = stripes[2];
/* 345 */         bars[pb++] = stripes[1];
/* 346 */         bars[pb++] = stripes[0];
/*     */       } 
/*     */     } 
/* 349 */     bars[pb++] = 1;
/* 350 */     bars[pb++] = 1;
/* 351 */     bars[pb++] = 1;
/* 352 */     bars[pb++] = 1;
/* 353 */     bars[pb++] = 1;
/* 354 */     bars[pb++] = 1;
/* 355 */     return bars;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] getBarsSupplemental2(String _code) {
/* 363 */     int[] code = new int[2];
/* 364 */     for (int k = 0; k < code.length; k++)
/* 365 */       code[k] = _code.charAt(k) - 48; 
/* 366 */     byte[] bars = new byte[13];
/* 367 */     int pb = 0;
/* 368 */     int parity = (code[0] * 10 + code[1]) % 4;
/* 369 */     bars[pb++] = 1;
/* 370 */     bars[pb++] = 1;
/* 371 */     bars[pb++] = 2;
/* 372 */     byte[] sequence = PARITY2[parity];
/* 373 */     for (int i = 0; i < sequence.length; i++) {
/* 374 */       if (i == 1) {
/* 375 */         bars[pb++] = 1;
/* 376 */         bars[pb++] = 1;
/*     */       } 
/* 378 */       int c = code[i];
/* 379 */       byte[] stripes = BARS[c];
/* 380 */       if (sequence[i] == 0) {
/* 381 */         bars[pb++] = stripes[0];
/* 382 */         bars[pb++] = stripes[1];
/* 383 */         bars[pb++] = stripes[2];
/* 384 */         bars[pb++] = stripes[3];
/*     */       } else {
/*     */         
/* 387 */         bars[pb++] = stripes[3];
/* 388 */         bars[pb++] = stripes[2];
/* 389 */         bars[pb++] = stripes[1];
/* 390 */         bars[pb++] = stripes[0];
/*     */       } 
/*     */     } 
/* 393 */     return bars;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] getBarsSupplemental5(String _code) {
/* 401 */     int[] code = new int[5];
/* 402 */     for (int k = 0; k < code.length; k++)
/* 403 */       code[k] = _code.charAt(k) - 48; 
/* 404 */     byte[] bars = new byte[31];
/* 405 */     int pb = 0;
/* 406 */     int parity = ((code[0] + code[2] + code[4]) * 3 + (code[1] + code[3]) * 9) % 10;
/* 407 */     bars[pb++] = 1;
/* 408 */     bars[pb++] = 1;
/* 409 */     bars[pb++] = 2;
/* 410 */     byte[] sequence = PARITY5[parity];
/* 411 */     for (int i = 0; i < sequence.length; i++) {
/* 412 */       if (i != 0) {
/* 413 */         bars[pb++] = 1;
/* 414 */         bars[pb++] = 1;
/*     */       } 
/* 416 */       int c = code[i];
/* 417 */       byte[] stripes = BARS[c];
/* 418 */       if (sequence[i] == 0) {
/* 419 */         bars[pb++] = stripes[0];
/* 420 */         bars[pb++] = stripes[1];
/* 421 */         bars[pb++] = stripes[2];
/* 422 */         bars[pb++] = stripes[3];
/*     */       } else {
/*     */         
/* 425 */         bars[pb++] = stripes[3];
/* 426 */         bars[pb++] = stripes[2];
/* 427 */         bars[pb++] = stripes[1];
/* 428 */         bars[pb++] = stripes[0];
/*     */       } 
/*     */     } 
/* 431 */     return bars;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Rectangle getBarcodeSize() {
/* 439 */     float width = 0.0F;
/* 440 */     float height = this.barHeight;
/* 441 */     if (this.font != null)
/* 442 */       if (this.baseline <= 0.0F) {
/* 443 */         height += -this.baseline + this.size;
/*     */       } else {
/* 445 */         height += this.baseline - this.font.getFontDescriptor(3, this.size);
/*     */       }  
/* 447 */     switch (this.codeType) {
/*     */       case 1:
/* 449 */         width = this.x * 95.0F;
/* 450 */         if (this.font != null) {
/* 451 */           width += this.font.getWidthPoint(this.code.charAt(0), this.size);
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 478 */         return new Rectangle(width, height);case 2: width = this.x * 67.0F; return new Rectangle(width, height);case 3: width = this.x * 95.0F; if (this.font != null) width += this.font.getWidthPoint(this.code.charAt(0), this.size) + this.font.getWidthPoint(this.code.charAt(11), this.size);  return new Rectangle(width, height);case 4: width = this.x * 51.0F; if (this.font != null) width += this.font.getWidthPoint(this.code.charAt(0), this.size) + this.font.getWidthPoint(this.code.charAt(7), this.size);  return new Rectangle(width, height);case 5: width = this.x * 20.0F; return new Rectangle(width, height);case 6: width = this.x * 47.0F; return new Rectangle(width, height);
/*     */     } 
/*     */     throw new RuntimeException(MessageLocalization.getComposedMessage("invalid.code.type", new Object[0]));
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
/*     */   public Rectangle placeBarcode(PdfContentByte cb, BaseColor barColor, BaseColor textColor) {
/* 518 */     Rectangle rect = getBarcodeSize();
/* 519 */     float barStartX = 0.0F;
/* 520 */     float barStartY = 0.0F;
/* 521 */     float textStartY = 0.0F;
/* 522 */     if (this.font != null) {
/* 523 */       if (this.baseline <= 0.0F) {
/* 524 */         textStartY = this.barHeight - this.baseline;
/*     */       } else {
/* 526 */         textStartY = -this.font.getFontDescriptor(3, this.size);
/* 527 */         barStartY = textStartY + this.baseline;
/*     */       } 
/*     */     }
/* 530 */     switch (this.codeType) {
/*     */       case 1:
/*     */       case 3:
/*     */       case 4:
/* 534 */         if (this.font != null)
/* 535 */           barStartX += this.font.getWidthPoint(this.code.charAt(0), this.size); 
/*     */         break;
/*     */     } 
/* 538 */     byte[] bars = null;
/* 539 */     int[] guard = GUARD_EMPTY;
/* 540 */     switch (this.codeType) {
/*     */       case 1:
/* 542 */         bars = getBarsEAN13(this.code);
/* 543 */         guard = GUARD_EAN13;
/*     */         break;
/*     */       case 2:
/* 546 */         bars = getBarsEAN8(this.code);
/* 547 */         guard = GUARD_EAN8;
/*     */         break;
/*     */       case 3:
/* 550 */         bars = getBarsEAN13("0" + this.code);
/* 551 */         guard = GUARD_UPCA;
/*     */         break;
/*     */       case 4:
/* 554 */         bars = getBarsUPCE(this.code);
/* 555 */         guard = GUARD_UPCE;
/*     */         break;
/*     */       case 5:
/* 558 */         bars = getBarsSupplemental2(this.code);
/*     */         break;
/*     */       case 6:
/* 561 */         bars = getBarsSupplemental5(this.code);
/*     */         break;
/*     */     } 
/* 564 */     float keepBarX = barStartX;
/* 565 */     boolean print = true;
/* 566 */     float gd = 0.0F;
/* 567 */     if (this.font != null && this.baseline > 0.0F && this.guardBars) {
/* 568 */       gd = this.baseline / 2.0F;
/*     */     }
/* 570 */     if (barColor != null)
/* 571 */       cb.setColorFill(barColor);  int k;
/* 572 */     for (k = 0; k < bars.length; k++) {
/* 573 */       float w = bars[k] * this.x;
/* 574 */       if (print)
/* 575 */         if (Arrays.binarySearch(guard, k) >= 0) {
/* 576 */           cb.rectangle(barStartX, barStartY - gd, w - this.inkSpreading, this.barHeight + gd);
/*     */         } else {
/* 578 */           cb.rectangle(barStartX, barStartY, w - this.inkSpreading, this.barHeight);
/*     */         }  
/* 580 */       print = !print;
/* 581 */       barStartX += w;
/*     */     } 
/* 583 */     cb.fill();
/* 584 */     if (this.font != null) {
/* 585 */       if (textColor != null)
/* 586 */         cb.setColorFill(textColor); 
/* 587 */       cb.beginText();
/* 588 */       cb.setFontAndSize(this.font, this.size);
/* 589 */       switch (this.codeType) {
/*     */         case 1:
/* 591 */           cb.setTextMatrix(0.0F, textStartY);
/* 592 */           cb.showText(this.code.substring(0, 1));
/* 593 */           for (k = 1; k < 13; k++) {
/* 594 */             String c = this.code.substring(k, k + 1);
/* 595 */             float len = this.font.getWidthPoint(c, this.size);
/* 596 */             float pX = keepBarX + TEXTPOS_EAN13[k - 1] * this.x - len / 2.0F;
/* 597 */             cb.setTextMatrix(pX, textStartY);
/* 598 */             cb.showText(c);
/*     */           } 
/*     */           break;
/*     */         case 2:
/* 602 */           for (k = 0; k < 8; k++) {
/* 603 */             String c = this.code.substring(k, k + 1);
/* 604 */             float len = this.font.getWidthPoint(c, this.size);
/* 605 */             float pX = TEXTPOS_EAN8[k] * this.x - len / 2.0F;
/* 606 */             cb.setTextMatrix(pX, textStartY);
/* 607 */             cb.showText(c);
/*     */           } 
/*     */           break;
/*     */         case 3:
/* 611 */           cb.setTextMatrix(0.0F, textStartY);
/* 612 */           cb.showText(this.code.substring(0, 1));
/* 613 */           for (k = 1; k < 11; k++) {
/* 614 */             String c = this.code.substring(k, k + 1);
/* 615 */             float len = this.font.getWidthPoint(c, this.size);
/* 616 */             float pX = keepBarX + TEXTPOS_EAN13[k] * this.x - len / 2.0F;
/* 617 */             cb.setTextMatrix(pX, textStartY);
/* 618 */             cb.showText(c);
/*     */           } 
/* 620 */           cb.setTextMatrix(keepBarX + this.x * 95.0F, textStartY);
/* 621 */           cb.showText(this.code.substring(11, 12));
/*     */           break;
/*     */         case 4:
/* 624 */           cb.setTextMatrix(0.0F, textStartY);
/* 625 */           cb.showText(this.code.substring(0, 1));
/* 626 */           for (k = 1; k < 7; k++) {
/* 627 */             String c = this.code.substring(k, k + 1);
/* 628 */             float len = this.font.getWidthPoint(c, this.size);
/* 629 */             float pX = keepBarX + TEXTPOS_EAN13[k - 1] * this.x - len / 2.0F;
/* 630 */             cb.setTextMatrix(pX, textStartY);
/* 631 */             cb.showText(c);
/*     */           } 
/* 633 */           cb.setTextMatrix(keepBarX + this.x * 51.0F, textStartY);
/* 634 */           cb.showText(this.code.substring(7, 8));
/*     */           break;
/*     */         case 5:
/*     */         case 6:
/* 638 */           for (k = 0; k < this.code.length(); k++) {
/* 639 */             String c = this.code.substring(k, k + 1);
/* 640 */             float len = this.font.getWidthPoint(c, this.size);
/* 641 */             float pX = (7.5F + (9 * k)) * this.x - len / 2.0F;
/* 642 */             cb.setTextMatrix(pX, textStartY);
/* 643 */             cb.showText(c);
/*     */           } 
/*     */           break;
/*     */       } 
/* 647 */       cb.endText();
/*     */     } 
/* 649 */     return rect;
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
/*     */   public Image createAwtImage(Color foreground, Color background) {
/* 661 */     int f = foreground.getRGB();
/* 662 */     int g = background.getRGB();
/* 663 */     Canvas canvas = new Canvas();
/*     */     
/* 665 */     int width = 0;
/* 666 */     byte[] bars = null;
/* 667 */     switch (this.codeType) {
/*     */       case 1:
/* 669 */         bars = getBarsEAN13(this.code);
/* 670 */         width = 95;
/*     */         break;
/*     */       case 2:
/* 673 */         bars = getBarsEAN8(this.code);
/* 674 */         width = 67;
/*     */         break;
/*     */       case 3:
/* 677 */         bars = getBarsEAN13("0" + this.code);
/* 678 */         width = 95;
/*     */         break;
/*     */       case 4:
/* 681 */         bars = getBarsUPCE(this.code);
/* 682 */         width = 51;
/*     */         break;
/*     */       case 5:
/* 685 */         bars = getBarsSupplemental2(this.code);
/* 686 */         width = 20;
/*     */         break;
/*     */       case 6:
/* 689 */         bars = getBarsSupplemental5(this.code);
/* 690 */         width = 47;
/*     */         break;
/*     */       default:
/* 693 */         throw new RuntimeException(MessageLocalization.getComposedMessage("invalid.code.type", new Object[0]));
/*     */     } 
/*     */     
/* 696 */     boolean print = true;
/* 697 */     int ptr = 0;
/* 698 */     int height = (int)this.barHeight;
/* 699 */     int[] pix = new int[width * height]; int k;
/* 700 */     for (k = 0; k < bars.length; k++) {
/* 701 */       int w = bars[k];
/* 702 */       int c = g;
/* 703 */       if (print)
/* 704 */         c = f; 
/* 705 */       print = !print;
/* 706 */       for (int j = 0; j < w; j++)
/* 707 */         pix[ptr++] = c; 
/*     */     } 
/* 709 */     for (k = width; k < pix.length; k += width) {
/* 710 */       System.arraycopy(pix, 0, pix, k, width);
/*     */     }
/* 712 */     Image img = canvas.createImage(new MemoryImageSource(width, height, pix, 0, width));
/*     */     
/* 714 */     return img;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/BarcodeEAN.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */