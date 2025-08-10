/*     */ package com.itextpdf.text.pdf;
/*     */ 
/*     */ import com.itextpdf.text.BaseColor;
/*     */ import com.itextpdf.text.Rectangle;
/*     */ import com.itextpdf.text.error_messages.MessageLocalization;
/*     */ import java.awt.Color;
/*     */ import java.awt.Image;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BarcodeEANSUPP
/*     */   extends Barcode
/*     */ {
/*     */   protected Barcode ean;
/*     */   protected Barcode supp;
/*     */   
/*     */   public BarcodeEANSUPP(Barcode ean, Barcode supp) {
/*  76 */     this.n = 8.0F;
/*  77 */     this.ean = ean;
/*  78 */     this.supp = supp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Rectangle getBarcodeSize() {
/*  86 */     Rectangle rect = this.ean.getBarcodeSize();
/*  87 */     rect.setRight(rect.getWidth() + this.supp.getBarcodeSize().getWidth() + this.n);
/*  88 */     return rect;
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
/* 128 */     if (this.supp.getFont() != null) {
/* 129 */       this.supp.setBarHeight(this.ean.getBarHeight() + this.supp.getBaseline() - this.supp.getFont().getFontDescriptor(2, this.supp.getSize()));
/*     */     } else {
/* 131 */       this.supp.setBarHeight(this.ean.getBarHeight());
/* 132 */     }  Rectangle eanR = this.ean.getBarcodeSize();
/* 133 */     cb.saveState();
/* 134 */     this.ean.placeBarcode(cb, barColor, textColor);
/* 135 */     cb.restoreState();
/* 136 */     cb.saveState();
/* 137 */     cb.concatCTM(1.0F, 0.0F, 0.0F, 1.0F, eanR.getWidth() + this.n, eanR.getHeight() - this.ean.getBarHeight());
/* 138 */     this.supp.placeBarcode(cb, barColor, textColor);
/* 139 */     cb.restoreState();
/* 140 */     return getBarcodeSize();
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
/* 152 */     throw new UnsupportedOperationException(MessageLocalization.getComposedMessage("the.two.barcodes.must.be.composed.externally", new Object[0]));
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/BarcodeEANSUPP.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */