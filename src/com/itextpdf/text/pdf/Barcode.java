/*     */ package com.itextpdf.text.pdf;
/*     */ 
/*     */ import com.itextpdf.text.BaseColor;
/*     */ import com.itextpdf.text.ExceptionConverter;
/*     */ import com.itextpdf.text.Image;
/*     */ import com.itextpdf.text.Rectangle;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class Barcode
/*     */ {
/*     */   public static final int EAN13 = 1;
/*     */   public static final int EAN8 = 2;
/*     */   public static final int UPCA = 3;
/*     */   public static final int UPCE = 4;
/*     */   public static final int SUPP2 = 5;
/*     */   public static final int SUPP5 = 6;
/*     */   public static final int POSTNET = 7;
/*     */   public static final int PLANET = 8;
/*     */   public static final int CODE128 = 9;
/*     */   public static final int CODE128_UCC = 10;
/*     */   public static final int CODE128_RAW = 11;
/*     */   public static final int CODABAR = 12;
/*     */   protected float x;
/*     */   protected float n;
/*     */   protected BaseFont font;
/*     */   protected float size;
/*     */   protected float baseline;
/*     */   protected float barHeight;
/*     */   protected int textAlignment;
/*     */   protected boolean generateChecksum;
/*     */   protected boolean checksumText;
/*     */   protected boolean startStopText;
/*     */   protected boolean extended;
/* 132 */   protected String code = "";
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean guardBars;
/*     */ 
/*     */ 
/*     */   
/*     */   protected int codeType;
/*     */ 
/*     */   
/* 143 */   protected float inkSpreading = 0.0F;
/*     */   
/*     */   protected String altText;
/*     */ 
/*     */   
/*     */   public float getX() {
/* 149 */     return this.x;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setX(float x) {
/* 156 */     this.x = x;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getN() {
/* 163 */     return this.n;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setN(float n) {
/* 170 */     this.n = n;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BaseFont getFont() {
/* 177 */     return this.font;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFont(BaseFont font) {
/* 184 */     this.font = font;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getSize() {
/* 191 */     return this.size;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSize(float size) {
/* 198 */     this.size = size;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getBaseline() {
/* 207 */     return this.baseline;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBaseline(float baseline) {
/* 216 */     this.baseline = baseline;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getBarHeight() {
/* 223 */     return this.barHeight;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBarHeight(float barHeight) {
/* 230 */     this.barHeight = barHeight;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getTextAlignment() {
/* 238 */     return this.textAlignment;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTextAlignment(int textAlignment) {
/* 246 */     this.textAlignment = textAlignment;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isGenerateChecksum() {
/* 253 */     return this.generateChecksum;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setGenerateChecksum(boolean generateChecksum) {
/* 260 */     this.generateChecksum = generateChecksum;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isChecksumText() {
/* 267 */     return this.checksumText;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setChecksumText(boolean checksumText) {
/* 274 */     this.checksumText = checksumText;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isStartStopText() {
/* 282 */     return this.startStopText;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setStartStopText(boolean startStopText) {
/* 290 */     this.startStopText = startStopText;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isExtended() {
/* 297 */     return this.extended;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setExtended(boolean extended) {
/* 304 */     this.extended = extended;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getCode() {
/* 311 */     return this.code;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCode(String code) {
/* 318 */     this.code = code;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isGuardBars() {
/* 325 */     return this.guardBars;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setGuardBars(boolean guardBars) {
/* 332 */     this.guardBars = guardBars;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getCodeType() {
/* 339 */     return this.codeType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCodeType(int codeType) {
/* 346 */     this.codeType = codeType;
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
/*     */   public abstract Rectangle getBarcodeSize();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract Rectangle placeBarcode(PdfContentByte paramPdfContentByte, BaseColor paramBaseColor1, BaseColor paramBaseColor2);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfTemplate createTemplateWithBarcode(PdfContentByte cb, BaseColor barColor, BaseColor textColor) {
/* 402 */     PdfTemplate tp = cb.createTemplate(0.0F, 0.0F);
/* 403 */     Rectangle rect = placeBarcode(tp, barColor, textColor);
/* 404 */     tp.setBoundingBox(rect);
/* 405 */     return tp;
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
/*     */   public Image createImageWithBarcode(PdfContentByte cb, BaseColor barColor, BaseColor textColor) {
/*     */     try {
/* 418 */       return Image.getInstance(createTemplateWithBarcode(cb, barColor, textColor));
/*     */     }
/* 420 */     catch (Exception e) {
/* 421 */       throw new ExceptionConverter(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getInkSpreading() {
/* 430 */     return this.inkSpreading;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setInkSpreading(float inkSpreading) {
/* 440 */     this.inkSpreading = inkSpreading;
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
/*     */   public String getAltText() {
/* 453 */     return this.altText;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAltText(String altText) {
/* 462 */     this.altText = altText;
/*     */   }
/*     */   
/*     */   public abstract Image createAwtImage(Color paramColor1, Color paramColor2);
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/Barcode.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */