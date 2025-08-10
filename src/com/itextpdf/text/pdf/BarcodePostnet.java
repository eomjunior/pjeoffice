/*     */ package com.itextpdf.text.pdf;
/*     */ 
/*     */ import com.itextpdf.text.BaseColor;
/*     */ import com.itextpdf.text.Rectangle;
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
/*     */ public class BarcodePostnet
/*     */   extends Barcode
/*     */ {
/*  64 */   private static final byte[][] BARS = new byte[][] { { 1, 1, 0, 0, 0 }, { 0, 0, 0, 1, 1 }, { 0, 0, 1, 0, 1 }, { 0, 0, 1, 1, 0 }, { 0, 1, 0, 0, 1 }, { 0, 1, 0, 1, 0 }, { 0, 1, 1, 0, 0 }, { 1, 0, 0, 0, 1 }, { 1, 0, 0, 1, 0 }, { 1, 0, 1, 0, 0 } };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] getBarsPostnet(String text) {
/*  92 */     int total = 0;
/*  93 */     for (int k = text.length() - 1; k >= 0; k--) {
/*  94 */       int n = text.charAt(k) - 48;
/*  95 */       total += n;
/*     */     } 
/*  97 */     text = text + (char)((10 - total % 10) % 10 + 48);
/*  98 */     byte[] bars = new byte[text.length() * 5 + 2];
/*  99 */     bars[0] = 1;
/* 100 */     bars[bars.length - 1] = 1;
/* 101 */     for (int i = 0; i < text.length(); i++) {
/* 102 */       int c = text.charAt(i) - 48;
/* 103 */       System.arraycopy(BARS[c], 0, bars, i * 5 + 1, 5);
/*     */     } 
/* 105 */     return bars;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Rectangle getBarcodeSize() {
/* 113 */     float width = ((this.code.length() + 1) * 5 + 1) * this.n + this.x;
/* 114 */     return new Rectangle(width, this.barHeight);
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
/* 154 */     if (barColor != null)
/* 155 */       cb.setColorFill(barColor); 
/* 156 */     byte[] bars = getBarsPostnet(this.code);
/* 157 */     byte flip = 1;
/* 158 */     if (this.codeType == 8) {
/* 159 */       flip = 0;
/* 160 */       bars[0] = 0;
/* 161 */       bars[bars.length - 1] = 0;
/*     */     } 
/* 163 */     float startX = 0.0F;
/* 164 */     for (int k = 0; k < bars.length; k++) {
/* 165 */       cb.rectangle(startX, 0.0F, this.x - this.inkSpreading, (bars[k] == flip) ? this.barHeight : this.size);
/* 166 */       startX += this.n;
/*     */     } 
/* 168 */     cb.fill();
/* 169 */     return getBarcodeSize();
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
/*     */   public Image createAwtImage(Color foreground, Color background) {
/* 182 */     int f = foreground.getRGB();
/* 183 */     int g = background.getRGB();
/* 184 */     Canvas canvas = new Canvas();
/* 185 */     int barWidth = (int)this.x;
/* 186 */     if (barWidth <= 0)
/* 187 */       barWidth = 1; 
/* 188 */     int barDistance = (int)this.n;
/* 189 */     if (barDistance <= barWidth)
/* 190 */       barDistance = barWidth + 1; 
/* 191 */     int barShort = (int)this.size;
/* 192 */     if (barShort <= 0)
/* 193 */       barShort = 1; 
/* 194 */     int barTall = (int)this.barHeight;
/* 195 */     if (barTall <= barShort)
/* 196 */       barTall = barShort + 1; 
/* 197 */     int width = ((this.code.length() + 1) * 5 + 1) * barDistance + barWidth;
/* 198 */     int[] pix = new int[width * barTall];
/* 199 */     byte[] bars = getBarsPostnet(this.code);
/* 200 */     byte flip = 1;
/* 201 */     if (this.codeType == 8) {
/* 202 */       flip = 0;
/* 203 */       bars[0] = 0;
/* 204 */       bars[bars.length - 1] = 0;
/*     */     } 
/* 206 */     int idx = 0;
/* 207 */     for (int k = 0; k < bars.length; k++) {
/* 208 */       boolean dot = (bars[k] == flip);
/* 209 */       for (int j = 0; j < barDistance; j++) {
/* 210 */         pix[idx + j] = (dot && j < barWidth) ? f : g;
/*     */       }
/* 212 */       idx += barDistance;
/*     */     } 
/* 214 */     int limit = width * (barTall - barShort); int i;
/* 215 */     for (i = width; i < limit; i += width)
/* 216 */       System.arraycopy(pix, 0, pix, i, width); 
/* 217 */     idx = limit;
/* 218 */     for (i = 0; i < bars.length; i++) {
/* 219 */       for (int j = 0; j < barDistance; j++) {
/* 220 */         pix[idx + j] = (j < barWidth) ? f : g;
/*     */       }
/* 222 */       idx += barDistance;
/*     */     } 
/* 224 */     for (i = limit + width; i < pix.length; i += width)
/* 225 */       System.arraycopy(pix, limit, pix, i, width); 
/* 226 */     Image img = canvas.createImage(new MemoryImageSource(width, barTall, pix, 0, width));
/*     */     
/* 228 */     return img;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/BarcodePostnet.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */