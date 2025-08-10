/*     */ package com.itextpdf.text.pdf.codec;
/*     */ 
/*     */ import com.itextpdf.text.ExceptionConverter;
/*     */ import com.itextpdf.text.Image;
/*     */ import com.itextpdf.text.ImgJBIG2;
/*     */ import com.itextpdf.text.error_messages.MessageLocalization;
/*     */ import com.itextpdf.text.pdf.RandomAccessFileOrArray;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JBIG2Image
/*     */ {
/*     */   public static byte[] getGlobalSegment(RandomAccessFileOrArray ra) {
/*     */     try {
/*  68 */       JBIG2SegmentReader sr = new JBIG2SegmentReader(ra);
/*  69 */       sr.read();
/*  70 */       return sr.getGlobal(true);
/*  71 */     } catch (Exception e) {
/*  72 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Image getJbig2Image(RandomAccessFileOrArray ra, int page) {
/*  83 */     if (page < 1) {
/*  84 */       throw new IllegalArgumentException(MessageLocalization.getComposedMessage("the.page.number.must.be.gt.eq.1", new Object[0]));
/*     */     }
/*     */     try {
/*  87 */       JBIG2SegmentReader sr = new JBIG2SegmentReader(ra);
/*  88 */       sr.read();
/*  89 */       JBIG2SegmentReader.JBIG2Page p = sr.getPage(page);
/*  90 */       return (Image)new ImgJBIG2(p.pageBitmapWidth, p.pageBitmapHeight, p.getData(true), sr.getGlobal(true));
/*     */     }
/*  92 */     catch (Exception e) {
/*  93 */       throw new ExceptionConverter(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getNumberOfPages(RandomAccessFileOrArray ra) {
/*     */     try {
/* 104 */       JBIG2SegmentReader sr = new JBIG2SegmentReader(ra);
/* 105 */       sr.read();
/* 106 */       return sr.numberOfPages();
/* 107 */     } catch (Exception e) {
/* 108 */       throw new ExceptionConverter(e);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/codec/JBIG2Image.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */