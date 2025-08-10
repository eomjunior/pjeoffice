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
/*     */ public class BarcodeInter25
/*     */   extends Barcode
/*     */ {
/*  74 */   private static final byte[][] BARS = new byte[][] { { 0, 0, 1, 1, 0 }, { 1, 0, 0, 0, 1 }, { 0, 1, 0, 0, 1 }, { 1, 1, 0, 0, 0 }, { 0, 0, 1, 0, 1 }, { 1, 0, 1, 0, 0 }, { 0, 1, 1, 0, 0 }, { 0, 0, 0, 1, 1 }, { 1, 0, 0, 1, 0 }, { 0, 1, 0, 1, 0 } };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BarcodeInter25() {
/*     */     try {
/*  91 */       this.x = 0.8F;
/*  92 */       this.n = 2.0F;
/*  93 */       this.font = BaseFont.createFont("Helvetica", "winansi", false);
/*  94 */       this.size = 8.0F;
/*  95 */       this.baseline = this.size;
/*  96 */       this.barHeight = this.size * 3.0F;
/*  97 */       this.textAlignment = 1;
/*  98 */       this.generateChecksum = false;
/*  99 */       this.checksumText = false;
/*     */     }
/* 101 */     catch (Exception e) {
/* 102 */       throw new ExceptionConverter(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String keepNumbers(String text) {
/* 111 */     StringBuffer sb = new StringBuffer();
/* 112 */     for (int k = 0; k < text.length(); k++) {
/* 113 */       char c = text.charAt(k);
/* 114 */       if (c >= '0' && c <= '9')
/* 115 */         sb.append(c); 
/*     */     } 
/* 117 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static char getChecksum(String text) {
/* 125 */     int mul = 3;
/* 126 */     int total = 0;
/* 127 */     for (int k = text.length() - 1; k >= 0; k--) {
/* 128 */       int n = text.charAt(k) - 48;
/* 129 */       total += mul * n;
/* 130 */       mul ^= 0x2;
/*     */     } 
/* 132 */     return (char)((10 - total % 10) % 10 + 48);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] getBarsInter25(String text) {
/* 140 */     text = keepNumbers(text);
/* 141 */     if ((text.length() & 0x1) != 0)
/* 142 */       throw new IllegalArgumentException(MessageLocalization.getComposedMessage("the.text.length.must.be.even", new Object[0])); 
/* 143 */     byte[] bars = new byte[text.length() * 5 + 7];
/* 144 */     int pb = 0;
/* 145 */     bars[pb++] = 0;
/* 146 */     bars[pb++] = 0;
/* 147 */     bars[pb++] = 0;
/* 148 */     bars[pb++] = 0;
/* 149 */     int len = text.length() / 2;
/* 150 */     for (int k = 0; k < len; k++) {
/* 151 */       int c1 = text.charAt(k * 2) - 48;
/* 152 */       int c2 = text.charAt(k * 2 + 1) - 48;
/* 153 */       byte[] b1 = BARS[c1];
/* 154 */       byte[] b2 = BARS[c2];
/* 155 */       for (int j = 0; j < 5; j++) {
/* 156 */         bars[pb++] = b1[j];
/* 157 */         bars[pb++] = b2[j];
/*     */       } 
/*     */     } 
/* 160 */     bars[pb++] = 1;
/* 161 */     bars[pb++] = 0;
/* 162 */     bars[pb++] = 0;
/* 163 */     return bars;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Rectangle getBarcodeSize() {
/* 171 */     float fontX = 0.0F;
/* 172 */     float fontY = 0.0F;
/* 173 */     if (this.font != null) {
/* 174 */       if (this.baseline > 0.0F) {
/* 175 */         fontY = this.baseline - this.font.getFontDescriptor(3, this.size);
/*     */       } else {
/* 177 */         fontY = -this.baseline + this.size;
/* 178 */       }  String str = this.code;
/* 179 */       if (this.generateChecksum && this.checksumText)
/* 180 */         str = str + getChecksum(str); 
/* 181 */       fontX = this.font.getWidthPoint((this.altText != null) ? this.altText : str, this.size);
/*     */     } 
/* 183 */     String fullCode = keepNumbers(this.code);
/* 184 */     int len = fullCode.length();
/* 185 */     if (this.generateChecksum)
/* 186 */       len++; 
/* 187 */     float fullWidth = len * (3.0F * this.x + 2.0F * this.x * this.n) + (6.0F + this.n) * this.x;
/* 188 */     fullWidth = Math.max(fullWidth, fontX);
/* 189 */     float fullHeight = this.barHeight + fontY;
/* 190 */     return new Rectangle(fullWidth, fullHeight);
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
/* 230 */     String fullCode = this.code;
/* 231 */     float fontX = 0.0F;
/* 232 */     if (this.font != null) {
/* 233 */       if (this.generateChecksum && this.checksumText)
/* 234 */         fullCode = fullCode + getChecksum(fullCode); 
/* 235 */       fontX = this.font.getWidthPoint(fullCode = (this.altText != null) ? this.altText : fullCode, this.size);
/*     */     } 
/* 237 */     String bCode = keepNumbers(this.code);
/* 238 */     if (this.generateChecksum)
/* 239 */       bCode = bCode + getChecksum(bCode); 
/* 240 */     int len = bCode.length();
/* 241 */     float fullWidth = len * (3.0F * this.x + 2.0F * this.x * this.n) + (6.0F + this.n) * this.x;
/* 242 */     float barStartX = 0.0F;
/* 243 */     float textStartX = 0.0F;
/* 244 */     switch (this.textAlignment) {
/*     */       case 0:
/*     */         break;
/*     */       case 2:
/* 248 */         if (fontX > fullWidth) {
/* 249 */           barStartX = fontX - fullWidth; break;
/*     */         } 
/* 251 */         textStartX = fullWidth - fontX;
/*     */         break;
/*     */       default:
/* 254 */         if (fontX > fullWidth) {
/* 255 */           barStartX = (fontX - fullWidth) / 2.0F; break;
/*     */         } 
/* 257 */         textStartX = (fullWidth - fontX) / 2.0F;
/*     */         break;
/*     */     } 
/* 260 */     float barStartY = 0.0F;
/* 261 */     float textStartY = 0.0F;
/* 262 */     if (this.font != null) {
/* 263 */       if (this.baseline <= 0.0F) {
/* 264 */         textStartY = this.barHeight - this.baseline;
/*     */       } else {
/* 266 */         textStartY = -this.font.getFontDescriptor(3, this.size);
/* 267 */         barStartY = textStartY + this.baseline;
/*     */       } 
/*     */     }
/* 270 */     byte[] bars = getBarsInter25(bCode);
/* 271 */     boolean print = true;
/* 272 */     if (barColor != null)
/* 273 */       cb.setColorFill(barColor); 
/* 274 */     for (int k = 0; k < bars.length; k++) {
/* 275 */       float w = (bars[k] == 0) ? this.x : (this.x * this.n);
/* 276 */       if (print)
/* 277 */         cb.rectangle(barStartX, barStartY, w - this.inkSpreading, this.barHeight); 
/* 278 */       print = !print;
/* 279 */       barStartX += w;
/*     */     } 
/* 281 */     cb.fill();
/* 282 */     if (this.font != null) {
/* 283 */       if (textColor != null)
/* 284 */         cb.setColorFill(textColor); 
/* 285 */       cb.beginText();
/* 286 */       cb.setFontAndSize(this.font, this.size);
/* 287 */       cb.setTextMatrix(textStartX, textStartY);
/* 288 */       cb.showText(fullCode);
/* 289 */       cb.endText();
/*     */     } 
/* 291 */     return getBarcodeSize();
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
/* 303 */     int f = foreground.getRGB();
/* 304 */     int g = background.getRGB();
/* 305 */     Canvas canvas = new Canvas();
/*     */     
/* 307 */     String bCode = keepNumbers(this.code);
/* 308 */     if (this.generateChecksum)
/* 309 */       bCode = bCode + getChecksum(bCode); 
/* 310 */     int len = bCode.length();
/* 311 */     int nn = (int)this.n;
/* 312 */     int fullWidth = len * (3 + 2 * nn) + 6 + nn;
/* 313 */     byte[] bars = getBarsInter25(bCode);
/* 314 */     boolean print = true;
/* 315 */     int ptr = 0;
/* 316 */     int height = (int)this.barHeight;
/* 317 */     int[] pix = new int[fullWidth * height]; int k;
/* 318 */     for (k = 0; k < bars.length; k++) {
/* 319 */       int w = (bars[k] == 0) ? 1 : nn;
/* 320 */       int c = g;
/* 321 */       if (print)
/* 322 */         c = f; 
/* 323 */       print = !print;
/* 324 */       for (int j = 0; j < w; j++)
/* 325 */         pix[ptr++] = c; 
/*     */     } 
/* 327 */     for (k = fullWidth; k < pix.length; k += fullWidth) {
/* 328 */       System.arraycopy(pix, 0, pix, k, fullWidth);
/*     */     }
/* 330 */     Image img = canvas.createImage(new MemoryImageSource(fullWidth, height, pix, 0, fullWidth));
/*     */     
/* 332 */     return img;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/BarcodeInter25.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */