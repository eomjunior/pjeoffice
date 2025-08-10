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
/*     */ public class Barcode39
/*     */   extends Barcode
/*     */ {
/*  74 */   private static final byte[][] BARS = new byte[][] { { 0, 0, 0, 1, 1, 0, 1, 0, 0 }, { 1, 0, 0, 1, 0, 0, 0, 0, 1 }, { 0, 0, 1, 1, 0, 0, 0, 0, 1 }, { 1, 0, 1, 1, 0, 0, 0, 0, 0 }, { 0, 0, 0, 1, 1, 0, 0, 0, 1 }, { 1, 0, 0, 1, 1, 0, 0, 0, 0 }, { 0, 0, 1, 1, 1, 0, 0, 0, 0 }, { 0, 0, 0, 1, 0, 0, 1, 0, 1 }, { 1, 0, 0, 1, 0, 0, 1, 0, 0 }, { 0, 0, 1, 1, 0, 0, 1, 0, 0 }, { 1, 0, 0, 0, 0, 1, 0, 0, 1 }, { 0, 0, 1, 0, 0, 1, 0, 0, 1 }, { 1, 0, 1, 0, 0, 1, 0, 0, 0 }, { 0, 0, 0, 0, 1, 1, 0, 0, 1 }, { 1, 0, 0, 0, 1, 1, 0, 0, 0 }, { 0, 0, 1, 0, 1, 1, 0, 0, 0 }, { 0, 0, 0, 0, 0, 1, 1, 0, 1 }, { 1, 0, 0, 0, 0, 1, 1, 0, 0 }, { 0, 0, 1, 0, 0, 1, 1, 0, 0 }, { 0, 0, 0, 0, 1, 1, 1, 0, 0 }, { 1, 0, 0, 0, 0, 0, 0, 1, 1 }, { 0, 0, 1, 0, 0, 0, 0, 1, 1 }, { 1, 0, 1, 0, 0, 0, 0, 1, 0 }, { 0, 0, 0, 0, 1, 0, 0, 1, 1 }, { 1, 0, 0, 0, 1, 0, 0, 1, 0 }, { 0, 0, 1, 0, 1, 0, 0, 1, 0 }, { 0, 0, 0, 0, 0, 0, 1, 1, 1 }, { 1, 0, 0, 0, 0, 0, 1, 1, 0 }, { 0, 0, 1, 0, 0, 0, 1, 1, 0 }, { 0, 0, 0, 0, 1, 0, 1, 1, 0 }, { 1, 1, 0, 0, 0, 0, 0, 0, 1 }, { 0, 1, 1, 0, 0, 0, 0, 0, 1 }, { 1, 1, 1, 0, 0, 0, 0, 0, 0 }, { 0, 1, 0, 0, 1, 0, 0, 0, 1 }, { 1, 1, 0, 0, 1, 0, 0, 0, 0 }, { 0, 1, 1, 0, 1, 0, 0, 0, 0 }, { 0, 1, 0, 0, 0, 0, 1, 0, 1 }, { 1, 1, 0, 0, 0, 0, 1, 0, 0 }, { 0, 1, 1, 0, 0, 0, 1, 0, 0 }, { 0, 1, 0, 1, 0, 1, 0, 0, 0 }, { 0, 1, 0, 1, 0, 0, 0, 1, 0 }, { 0, 1, 0, 0, 0, 1, 0, 1, 0 }, { 0, 0, 0, 1, 0, 1, 0, 1, 0 }, { 0, 1, 0, 0, 1, 0, 1, 0, 0 } };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ-. $/+%*";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String EXTENDED = "%U$A$B$C$D$E$F$G$H$I$J$K$L$M$N$O$P$Q$R$S$T$U$V$W$X$Y$Z%A%B%C%D%E  /A/B/C/D/E/F/G/H/I/J/K/L - ./O 0 1 2 3 4 5 6 7 8 9/Z%F%G%H%I%J%V A B C D E F G H I J K L M N O P Q R S T U V W X Y Z%K%L%M%N%O%W+A+B+C+D+E+F+G+H+I+J+K+L+M+N+O+P+Q+R+S+T+U+V+W+X+Y+Z%P%Q%R%S%T";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Barcode39() {
/*     */     try {
/* 141 */       this.x = 0.8F;
/* 142 */       this.n = 2.0F;
/* 143 */       this.font = BaseFont.createFont("Helvetica", "winansi", false);
/* 144 */       this.size = 8.0F;
/* 145 */       this.baseline = this.size;
/* 146 */       this.barHeight = this.size * 3.0F;
/* 147 */       this.textAlignment = 1;
/* 148 */       this.generateChecksum = false;
/* 149 */       this.checksumText = false;
/* 150 */       this.startStopText = true;
/* 151 */       this.extended = false;
/*     */     }
/* 153 */     catch (Exception e) {
/* 154 */       throw new ExceptionConverter(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] getBarsCode39(String text) {
/* 164 */     text = "*" + text + "*";
/* 165 */     byte[] bars = new byte[text.length() * 10 - 1];
/* 166 */     for (int k = 0; k < text.length(); k++) {
/* 167 */       int idx = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ-. $/+%*".indexOf(text.charAt(k));
/* 168 */       if (idx < 0)
/* 169 */         throw new IllegalArgumentException(MessageLocalization.getComposedMessage("the.character.1.is.illegal.in.code.39", text.charAt(k))); 
/* 170 */       System.arraycopy(BARS[idx], 0, bars, k * 10, 9);
/*     */     } 
/* 172 */     return bars;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getCode39Ex(String text) {
/* 181 */     StringBuilder out = new StringBuilder("");
/* 182 */     for (int k = 0; k < text.length(); k++) {
/* 183 */       char c = text.charAt(k);
/* 184 */       if (c > '')
/* 185 */         throw new IllegalArgumentException(MessageLocalization.getComposedMessage("the.character.1.is.illegal.in.code.39.extended", c)); 
/* 186 */       char c1 = "%U$A$B$C$D$E$F$G$H$I$J$K$L$M$N$O$P$Q$R$S$T$U$V$W$X$Y$Z%A%B%C%D%E  /A/B/C/D/E/F/G/H/I/J/K/L - ./O 0 1 2 3 4 5 6 7 8 9/Z%F%G%H%I%J%V A B C D E F G H I J K L M N O P Q R S T U V W X Y Z%K%L%M%N%O%W+A+B+C+D+E+F+G+H+I+J+K+L+M+N+O+P+Q+R+S+T+U+V+W+X+Y+Z%P%Q%R%S%T".charAt(c * 2);
/* 187 */       char c2 = "%U$A$B$C$D$E$F$G$H$I$J$K$L$M$N$O$P$Q$R$S$T$U$V$W$X$Y$Z%A%B%C%D%E  /A/B/C/D/E/F/G/H/I/J/K/L - ./O 0 1 2 3 4 5 6 7 8 9/Z%F%G%H%I%J%V A B C D E F G H I J K L M N O P Q R S T U V W X Y Z%K%L%M%N%O%W+A+B+C+D+E+F+G+H+I+J+K+L+M+N+O+P+Q+R+S+T+U+V+W+X+Y+Z%P%Q%R%S%T".charAt(c * 2 + 1);
/* 188 */       if (c1 != ' ')
/* 189 */         out.append(c1); 
/* 190 */       out.append(c2);
/*     */     } 
/* 192 */     return out.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static char getChecksum(String text) {
/* 200 */     int chk = 0;
/* 201 */     for (int k = 0; k < text.length(); k++) {
/* 202 */       int idx = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ-. $/+%*".indexOf(text.charAt(k));
/* 203 */       if (idx < 0)
/* 204 */         throw new IllegalArgumentException(MessageLocalization.getComposedMessage("the.character.1.is.illegal.in.code.39", text.charAt(k))); 
/* 205 */       chk += idx;
/*     */     } 
/* 207 */     return "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ-. $/+%*".charAt(chk % 43);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Rectangle getBarcodeSize() {
/* 215 */     float fontX = 0.0F;
/* 216 */     float fontY = 0.0F;
/* 217 */     String fCode = this.code;
/* 218 */     if (this.extended)
/* 219 */       fCode = getCode39Ex(this.code); 
/* 220 */     if (this.font != null) {
/* 221 */       if (this.baseline > 0.0F) {
/* 222 */         fontY = this.baseline - this.font.getFontDescriptor(3, this.size);
/*     */       } else {
/* 224 */         fontY = -this.baseline + this.size;
/* 225 */       }  String fullCode = this.code;
/* 226 */       if (this.generateChecksum && this.checksumText)
/* 227 */         fullCode = fullCode + getChecksum(fCode); 
/* 228 */       if (this.startStopText)
/* 229 */         fullCode = "*" + fullCode + "*"; 
/* 230 */       fontX = this.font.getWidthPoint((this.altText != null) ? this.altText : fullCode, this.size);
/*     */     } 
/* 232 */     int len = fCode.length() + 2;
/* 233 */     if (this.generateChecksum)
/* 234 */       len++; 
/* 235 */     float fullWidth = len * (6.0F * this.x + 3.0F * this.x * this.n) + (len - 1) * this.x;
/* 236 */     fullWidth = Math.max(fullWidth, fontX);
/* 237 */     float fullHeight = this.barHeight + fontY;
/* 238 */     return new Rectangle(fullWidth, fullHeight);
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
/* 278 */     String fullCode = this.code;
/* 279 */     float fontX = 0.0F;
/* 280 */     String bCode = this.code;
/* 281 */     if (this.extended)
/* 282 */       bCode = getCode39Ex(this.code); 
/* 283 */     if (this.font != null) {
/* 284 */       if (this.generateChecksum && this.checksumText)
/* 285 */         fullCode = fullCode + getChecksum(bCode); 
/* 286 */       if (this.startStopText)
/* 287 */         fullCode = "*" + fullCode + "*"; 
/* 288 */       fontX = this.font.getWidthPoint(fullCode = (this.altText != null) ? this.altText : fullCode, this.size);
/*     */     } 
/* 290 */     if (this.generateChecksum)
/* 291 */       bCode = bCode + getChecksum(bCode); 
/* 292 */     int len = bCode.length() + 2;
/* 293 */     float fullWidth = len * (6.0F * this.x + 3.0F * this.x * this.n) + (len - 1) * this.x;
/* 294 */     float barStartX = 0.0F;
/* 295 */     float textStartX = 0.0F;
/* 296 */     switch (this.textAlignment) {
/*     */       case 0:
/*     */         break;
/*     */       case 2:
/* 300 */         if (fontX > fullWidth) {
/* 301 */           barStartX = fontX - fullWidth; break;
/*     */         } 
/* 303 */         textStartX = fullWidth - fontX;
/*     */         break;
/*     */       default:
/* 306 */         if (fontX > fullWidth) {
/* 307 */           barStartX = (fontX - fullWidth) / 2.0F; break;
/*     */         } 
/* 309 */         textStartX = (fullWidth - fontX) / 2.0F;
/*     */         break;
/*     */     } 
/* 312 */     float barStartY = 0.0F;
/* 313 */     float textStartY = 0.0F;
/* 314 */     if (this.font != null) {
/* 315 */       if (this.baseline <= 0.0F) {
/* 316 */         textStartY = this.barHeight - this.baseline;
/*     */       } else {
/* 318 */         textStartY = -this.font.getFontDescriptor(3, this.size);
/* 319 */         barStartY = textStartY + this.baseline;
/*     */       } 
/*     */     }
/* 322 */     byte[] bars = getBarsCode39(bCode);
/* 323 */     boolean print = true;
/* 324 */     if (barColor != null)
/* 325 */       cb.setColorFill(barColor); 
/* 326 */     for (int k = 0; k < bars.length; k++) {
/* 327 */       float w = (bars[k] == 0) ? this.x : (this.x * this.n);
/* 328 */       if (print)
/* 329 */         cb.rectangle(barStartX, barStartY, w - this.inkSpreading, this.barHeight); 
/* 330 */       print = !print;
/* 331 */       barStartX += w;
/*     */     } 
/* 333 */     cb.fill();
/* 334 */     if (this.font != null) {
/* 335 */       if (textColor != null)
/* 336 */         cb.setColorFill(textColor); 
/* 337 */       cb.beginText();
/* 338 */       cb.setFontAndSize(this.font, this.size);
/* 339 */       cb.setTextMatrix(textStartX, textStartY);
/* 340 */       cb.showText(fullCode);
/* 341 */       cb.endText();
/*     */     } 
/* 343 */     return getBarcodeSize();
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
/* 355 */     int f = foreground.getRGB();
/* 356 */     int g = background.getRGB();
/* 357 */     Canvas canvas = new Canvas();
/*     */     
/* 359 */     String bCode = this.code;
/* 360 */     if (this.extended)
/* 361 */       bCode = getCode39Ex(this.code); 
/* 362 */     if (this.generateChecksum)
/* 363 */       bCode = bCode + getChecksum(bCode); 
/* 364 */     int len = bCode.length() + 2;
/* 365 */     int nn = (int)this.n;
/* 366 */     int fullWidth = len * (6 + 3 * nn) + len - 1;
/* 367 */     byte[] bars = getBarsCode39(bCode);
/* 368 */     boolean print = true;
/* 369 */     int ptr = 0;
/* 370 */     int height = (int)this.barHeight;
/* 371 */     int[] pix = new int[fullWidth * height]; int k;
/* 372 */     for (k = 0; k < bars.length; k++) {
/* 373 */       int w = (bars[k] == 0) ? 1 : nn;
/* 374 */       int c = g;
/* 375 */       if (print)
/* 376 */         c = f; 
/* 377 */       print = !print;
/* 378 */       for (int j = 0; j < w; j++)
/* 379 */         pix[ptr++] = c; 
/*     */     } 
/* 381 */     for (k = fullWidth; k < pix.length; k += fullWidth) {
/* 382 */       System.arraycopy(pix, 0, pix, k, fullWidth);
/*     */     }
/* 384 */     Image img = canvas.createImage(new MemoryImageSource(fullWidth, height, pix, 0, fullWidth));
/*     */     
/* 386 */     return img;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/Barcode39.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */