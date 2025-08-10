/*     */ package com.itextpdf.text.pdf;
/*     */ 
/*     */ import com.itextpdf.text.DocumentException;
/*     */ import com.itextpdf.text.error_messages.MessageLocalization;
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
/*     */ 
/*     */ 
/*     */ public class PdfPage
/*     */   extends PdfDictionary
/*     */ {
/*  62 */   private static final String[] boxStrings = new String[] { "crop", "trim", "art", "bleed" };
/*  63 */   private static final PdfName[] boxNames = new PdfName[] { PdfName.CROPBOX, PdfName.TRIMBOX, PdfName.ARTBOX, PdfName.BLEEDBOX };
/*     */ 
/*     */ 
/*     */   
/*  67 */   public static final PdfNumber PORTRAIT = new PdfNumber(0);
/*     */ 
/*     */   
/*  70 */   public static final PdfNumber LANDSCAPE = new PdfNumber(90);
/*     */ 
/*     */   
/*  73 */   public static final PdfNumber INVERTEDPORTRAIT = new PdfNumber(180);
/*     */ 
/*     */   
/*  76 */   public static final PdfNumber SEASCAPE = new PdfNumber(270);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   PdfRectangle mediaBox;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   PdfPage(PdfRectangle mediaBox, HashMap<String, PdfRectangle> boxSize, PdfDictionary resources, int rotate) throws DocumentException {
/*  93 */     super(PAGE);
/*  94 */     this.mediaBox = mediaBox;
/*  95 */     if (mediaBox != null && (mediaBox.width() > 14400.0F || mediaBox.height() > 14400.0F)) {
/*  96 */       throw new DocumentException(MessageLocalization.getComposedMessage("the.page.size.must.be.smaller.than.14400.by.14400.its.1.by.2", new Object[] { Float.valueOf(mediaBox.width()), Float.valueOf(mediaBox.height()) }));
/*     */     }
/*  98 */     put(PdfName.MEDIABOX, mediaBox);
/*  99 */     put(PdfName.RESOURCES, resources);
/* 100 */     if (rotate != 0) {
/* 101 */       put(PdfName.ROTATE, new PdfNumber(rotate));
/*     */     }
/* 103 */     for (int k = 0; k < boxStrings.length; k++) {
/* 104 */       PdfObject rect = boxSize.get(boxStrings[k]);
/* 105 */       if (rect != null) {
/* 106 */         put(boxNames[k], rect);
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
/*     */   PdfPage(PdfRectangle mediaBox, HashMap<String, PdfRectangle> boxSize, PdfDictionary resources) throws DocumentException {
/* 119 */     this(mediaBox, boxSize, resources, 0);
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
/*     */   public boolean isParent() {
/* 131 */     return false;
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
/*     */   void add(PdfIndirectReference contents) {
/* 143 */     put(PdfName.CONTENTS, contents);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   PdfRectangle rotateMediaBox() {
/* 153 */     this.mediaBox = this.mediaBox.rotate();
/* 154 */     put(PdfName.MEDIABOX, this.mediaBox);
/* 155 */     return this.mediaBox;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   PdfRectangle getMediaBox() {
/* 165 */     return this.mediaBox;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/PdfPage.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */