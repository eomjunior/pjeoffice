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
/*     */ public class BarcodeCodabar
/*     */   extends Barcode
/*     */ {
/*  73 */   private static final byte[][] BARS = new byte[][] { { 0, 0, 0, 0, 0, 1, 1 }, { 0, 0, 0, 0, 1, 1, 0 }, { 0, 0, 0, 1, 0, 0, 1 }, { 1, 1, 0, 0, 0, 0, 0 }, { 0, 0, 1, 0, 0, 1, 0 }, { 1, 0, 0, 0, 0, 1, 0 }, { 0, 1, 0, 0, 0, 0, 1 }, { 0, 1, 0, 0, 1, 0, 0 }, { 0, 1, 1, 0, 0, 0, 0 }, { 1, 0, 0, 1, 0, 0, 0 }, { 0, 0, 0, 1, 1, 0, 0 }, { 0, 0, 1, 1, 0, 0, 0 }, { 1, 0, 0, 0, 1, 0, 1 }, { 1, 0, 1, 0, 0, 0, 1 }, { 1, 0, 1, 0, 1, 0, 0 }, { 0, 0, 1, 0, 1, 0, 1 }, { 0, 0, 1, 1, 0, 1, 0 }, { 0, 1, 0, 1, 0, 0, 1 }, { 0, 0, 0, 1, 0, 1, 1 }, { 0, 0, 0, 1, 1, 1, 0 } };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String CHARS = "0123456789-$:/.+ABCD";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int START_STOP_IDX = 16;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BarcodeCodabar() {
/*     */     try {
/* 106 */       this.x = 0.8F;
/* 107 */       this.n = 2.0F;
/* 108 */       this.font = BaseFont.createFont("Helvetica", "winansi", false);
/* 109 */       this.size = 8.0F;
/* 110 */       this.baseline = this.size;
/* 111 */       this.barHeight = this.size * 3.0F;
/* 112 */       this.textAlignment = 1;
/* 113 */       this.generateChecksum = false;
/* 114 */       this.checksumText = false;
/* 115 */       this.startStopText = false;
/* 116 */       this.codeType = 12;
/*     */     }
/* 118 */     catch (Exception e) {
/* 119 */       throw new ExceptionConverter(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] getBarsCodabar(String text) {
/* 128 */     text = text.toUpperCase();
/* 129 */     int len = text.length();
/* 130 */     if (len < 2)
/* 131 */       throw new IllegalArgumentException(MessageLocalization.getComposedMessage("codabar.must.have.at.least.a.start.and.stop.character", new Object[0])); 
/* 132 */     if ("0123456789-$:/.+ABCD".indexOf(text.charAt(0)) < 16 || "0123456789-$:/.+ABCD".indexOf(text.charAt(len - 1)) < 16)
/* 133 */       throw new IllegalArgumentException(MessageLocalization.getComposedMessage("codabar.must.have.one.of.abcd.as.start.stop.character", new Object[0])); 
/* 134 */     byte[] bars = new byte[text.length() * 8 - 1];
/* 135 */     for (int k = 0; k < len; k++) {
/* 136 */       int idx = "0123456789-$:/.+ABCD".indexOf(text.charAt(k));
/* 137 */       if (idx >= 16 && k > 0 && k < len - 1)
/* 138 */         throw new IllegalArgumentException(MessageLocalization.getComposedMessage("in.codabar.start.stop.characters.are.only.allowed.at.the.extremes", new Object[0])); 
/* 139 */       if (idx < 0)
/* 140 */         throw new IllegalArgumentException(MessageLocalization.getComposedMessage("the.character.1.is.illegal.in.codabar", text.charAt(k))); 
/* 141 */       System.arraycopy(BARS[idx], 0, bars, k * 8, 7);
/*     */     } 
/* 143 */     return bars;
/*     */   }
/*     */   
/*     */   public static String calculateChecksum(String code) {
/* 147 */     if (code.length() < 2)
/* 148 */       return code; 
/* 149 */     String text = code.toUpperCase();
/* 150 */     int sum = 0;
/* 151 */     int len = text.length();
/* 152 */     for (int k = 0; k < len; k++)
/* 153 */       sum += "0123456789-$:/.+ABCD".indexOf(text.charAt(k)); 
/* 154 */     sum = (sum + 15) / 16 * 16 - sum;
/* 155 */     return code.substring(0, len - 1) + "0123456789-$:/.+ABCD".charAt(sum) + code.substring(len - 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Rectangle getBarcodeSize() {
/* 163 */     float fontX = 0.0F;
/* 164 */     float fontY = 0.0F;
/* 165 */     String text = this.code;
/* 166 */     if (this.generateChecksum && this.checksumText)
/* 167 */       text = calculateChecksum(this.code); 
/* 168 */     if (!this.startStopText)
/* 169 */       text = text.substring(1, text.length() - 1); 
/* 170 */     if (this.font != null) {
/* 171 */       if (this.baseline > 0.0F) {
/* 172 */         fontY = this.baseline - this.font.getFontDescriptor(3, this.size);
/*     */       } else {
/* 174 */         fontY = -this.baseline + this.size;
/* 175 */       }  fontX = this.font.getWidthPoint((this.altText != null) ? this.altText : text, this.size);
/*     */     } 
/* 177 */     text = this.code;
/* 178 */     if (this.generateChecksum)
/* 179 */       text = calculateChecksum(this.code); 
/* 180 */     byte[] bars = getBarsCodabar(text);
/* 181 */     int wide = 0;
/* 182 */     for (int k = 0; k < bars.length; k++) {
/* 183 */       wide += bars[k];
/*     */     }
/* 185 */     int narrow = bars.length - wide;
/* 186 */     float fullWidth = this.x * (narrow + wide * this.n);
/* 187 */     fullWidth = Math.max(fullWidth, fontX);
/* 188 */     float fullHeight = this.barHeight + fontY;
/* 189 */     return new Rectangle(fullWidth, fullHeight);
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
/* 229 */     String fullCode = this.code;
/* 230 */     if (this.generateChecksum && this.checksumText)
/* 231 */       fullCode = calculateChecksum(this.code); 
/* 232 */     if (!this.startStopText)
/* 233 */       fullCode = fullCode.substring(1, fullCode.length() - 1); 
/* 234 */     float fontX = 0.0F;
/* 235 */     if (this.font != null) {
/* 236 */       fontX = this.font.getWidthPoint(fullCode = (this.altText != null) ? this.altText : fullCode, this.size);
/*     */     }
/* 238 */     byte[] bars = getBarsCodabar(this.generateChecksum ? calculateChecksum(this.code) : this.code);
/* 239 */     int wide = 0;
/* 240 */     for (int k = 0; k < bars.length; k++) {
/* 241 */       wide += bars[k];
/*     */     }
/* 243 */     int narrow = bars.length - wide;
/* 244 */     float fullWidth = this.x * (narrow + wide * this.n);
/* 245 */     float barStartX = 0.0F;
/* 246 */     float textStartX = 0.0F;
/* 247 */     switch (this.textAlignment) {
/*     */       case 0:
/*     */         break;
/*     */       case 2:
/* 251 */         if (fontX > fullWidth) {
/* 252 */           barStartX = fontX - fullWidth; break;
/*     */         } 
/* 254 */         textStartX = fullWidth - fontX;
/*     */         break;
/*     */       default:
/* 257 */         if (fontX > fullWidth) {
/* 258 */           barStartX = (fontX - fullWidth) / 2.0F; break;
/*     */         } 
/* 260 */         textStartX = (fullWidth - fontX) / 2.0F;
/*     */         break;
/*     */     } 
/* 263 */     float barStartY = 0.0F;
/* 264 */     float textStartY = 0.0F;
/* 265 */     if (this.font != null) {
/* 266 */       if (this.baseline <= 0.0F) {
/* 267 */         textStartY = this.barHeight - this.baseline;
/*     */       } else {
/* 269 */         textStartY = -this.font.getFontDescriptor(3, this.size);
/* 270 */         barStartY = textStartY + this.baseline;
/*     */       } 
/*     */     }
/* 273 */     boolean print = true;
/* 274 */     if (barColor != null)
/* 275 */       cb.setColorFill(barColor); 
/* 276 */     for (int i = 0; i < bars.length; i++) {
/* 277 */       float w = (bars[i] == 0) ? this.x : (this.x * this.n);
/* 278 */       if (print)
/* 279 */         cb.rectangle(barStartX, barStartY, w - this.inkSpreading, this.barHeight); 
/* 280 */       print = !print;
/* 281 */       barStartX += w;
/*     */     } 
/* 283 */     cb.fill();
/* 284 */     if (this.font != null) {
/* 285 */       if (textColor != null)
/* 286 */         cb.setColorFill(textColor); 
/* 287 */       cb.beginText();
/* 288 */       cb.setFontAndSize(this.font, this.size);
/* 289 */       cb.setTextMatrix(textStartX, textStartY);
/* 290 */       cb.showText(fullCode);
/* 291 */       cb.endText();
/*     */     } 
/* 293 */     return getBarcodeSize();
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
/* 305 */     int f = foreground.getRGB();
/* 306 */     int g = background.getRGB();
/* 307 */     Canvas canvas = new Canvas();
/*     */     
/* 309 */     String fullCode = this.code;
/* 310 */     if (this.generateChecksum && this.checksumText)
/* 311 */       fullCode = calculateChecksum(this.code); 
/* 312 */     if (!this.startStopText)
/* 313 */       fullCode = fullCode.substring(1, fullCode.length() - 1); 
/* 314 */     byte[] bars = getBarsCodabar(this.generateChecksum ? calculateChecksum(this.code) : this.code);
/* 315 */     int wide = 0;
/* 316 */     for (int k = 0; k < bars.length; k++) {
/* 317 */       wide += bars[k];
/*     */     }
/* 319 */     int narrow = bars.length - wide;
/* 320 */     int fullWidth = narrow + wide * (int)this.n;
/* 321 */     boolean print = true;
/* 322 */     int ptr = 0;
/* 323 */     int height = (int)this.barHeight;
/* 324 */     int[] pix = new int[fullWidth * height]; int i;
/* 325 */     for (i = 0; i < bars.length; i++) {
/* 326 */       int w = (bars[i] == 0) ? 1 : (int)this.n;
/* 327 */       int c = g;
/* 328 */       if (print)
/* 329 */         c = f; 
/* 330 */       print = !print;
/* 331 */       for (int j = 0; j < w; j++)
/* 332 */         pix[ptr++] = c; 
/*     */     } 
/* 334 */     for (i = fullWidth; i < pix.length; i += fullWidth) {
/* 335 */       System.arraycopy(pix, 0, pix, i, fullWidth);
/*     */     }
/* 337 */     Image img = canvas.createImage(new MemoryImageSource(fullWidth, height, pix, 0, fullWidth));
/*     */     
/* 339 */     return img;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/BarcodeCodabar.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */