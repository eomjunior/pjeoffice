/*     */ package com.itextpdf.text.pdf;
/*     */ 
/*     */ import com.itextpdf.text.BadElementException;
/*     */ import com.itextpdf.text.BaseColor;
/*     */ import com.itextpdf.text.ExceptionConverter;
/*     */ import com.itextpdf.text.Image;
/*     */ import com.itextpdf.text.Rectangle;
/*     */ import com.itextpdf.text.pdf.codec.CCITTG4Encoder;
/*     */ import com.itextpdf.text.pdf.qrcode.ByteMatrix;
/*     */ import com.itextpdf.text.pdf.qrcode.EncodeHintType;
/*     */ import com.itextpdf.text.pdf.qrcode.QRCodeWriter;
/*     */ import com.itextpdf.text.pdf.qrcode.WriterException;
/*     */ import java.awt.Canvas;
/*     */ import java.awt.Color;
/*     */ import java.awt.Image;
/*     */ import java.awt.image.MemoryImageSource;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BarcodeQRCode
/*     */ {
/*     */   ByteMatrix bm;
/*     */   
/*     */   public BarcodeQRCode(String content, int width, int height, Map<EncodeHintType, Object> hints) {
/*     */     try {
/*  77 */       QRCodeWriter qc = new QRCodeWriter();
/*  78 */       this.bm = qc.encode(content, width, height, hints);
/*     */     }
/*  80 */     catch (WriterException ex) {
/*  81 */       throw new ExceptionConverter(ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   private byte[] getBitMatrix() {
/*  86 */     int width = this.bm.getWidth();
/*  87 */     int height = this.bm.getHeight();
/*  88 */     int stride = (width + 7) / 8;
/*  89 */     byte[] b = new byte[stride * height];
/*  90 */     byte[][] mt = this.bm.getArray();
/*  91 */     for (int y = 0; y < height; y++) {
/*  92 */       byte[] line = mt[y];
/*  93 */       for (int x = 0; x < width; x++) {
/*  94 */         if (line[x] != 0) {
/*  95 */           int offset = stride * y + x / 8;
/*  96 */           b[offset] = (byte)(b[offset] | (byte)(128 >> x % 8));
/*     */         } 
/*     */       } 
/*     */     } 
/* 100 */     return b;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Image getImage() throws BadElementException {
/* 108 */     byte[] b = getBitMatrix();
/* 109 */     byte[] g4 = CCITTG4Encoder.compress(b, this.bm.getWidth(), this.bm.getHeight());
/* 110 */     return Image.getInstance(this.bm.getWidth(), this.bm.getHeight(), false, 256, 1, g4, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Image createAwtImage(Color foreground, Color background) {
/* 121 */     int f = foreground.getRGB();
/* 122 */     int g = background.getRGB();
/* 123 */     Canvas canvas = new Canvas();
/*     */     
/* 125 */     int width = this.bm.getWidth();
/* 126 */     int height = this.bm.getHeight();
/* 127 */     int[] pix = new int[width * height];
/* 128 */     byte[][] mt = this.bm.getArray();
/* 129 */     for (int y = 0; y < height; y++) {
/* 130 */       byte[] line = mt[y];
/* 131 */       for (int x = 0; x < width; x++) {
/* 132 */         pix[y * width + x] = (line[x] == 0) ? f : g;
/*     */       }
/*     */     } 
/*     */     
/* 136 */     Image img = canvas.createImage(new MemoryImageSource(width, height, pix, 0, width));
/* 137 */     return img;
/*     */   }
/*     */   
/*     */   public void placeBarcode(PdfContentByte cb, BaseColor foreground, float moduleSide) {
/* 141 */     int width = this.bm.getWidth();
/* 142 */     int height = this.bm.getHeight();
/* 143 */     byte[][] mt = this.bm.getArray();
/*     */     
/* 145 */     cb.setColorFill(foreground);
/*     */     
/* 147 */     for (int y = 0; y < height; y++) {
/* 148 */       byte[] line = mt[y];
/* 149 */       for (int x = 0; x < width; x++) {
/* 150 */         if (line[x] == 0) {
/* 151 */           cb.rectangle(x * moduleSide, (height - y - 1) * moduleSide, moduleSide, moduleSide);
/*     */         }
/*     */       } 
/*     */     } 
/* 155 */     cb.fill();
/*     */   }
/*     */ 
/*     */   
/*     */   public Rectangle getBarcodeSize() {
/* 160 */     return new Rectangle(0.0F, 0.0F, this.bm.getWidth(), this.bm.getHeight());
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/BarcodeQRCode.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */