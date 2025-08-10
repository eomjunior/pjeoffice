/*     */ package com.itextpdf.text.pdf;
/*     */ 
/*     */ import com.itextpdf.text.DocumentException;
/*     */ import com.itextpdf.text.log.LoggerFactory;
/*     */ import java.io.IOException;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TtfUnicodeWriter
/*     */ {
/*  56 */   protected PdfWriter writer = null;
/*     */   
/*     */   public TtfUnicodeWriter(PdfWriter writer) {
/*  59 */     this.writer = writer;
/*     */   }
/*     */   
/*     */   public void writeFont(TrueTypeFontUnicode font, PdfIndirectReference ref, Object[] params, byte[] rotbits) throws DocumentException, IOException {
/*  63 */     HashMap<Integer, int[]> longTag = (HashMap<Integer, int[]>)params[0];
/*  64 */     font.addRangeUni(longTag, true, font.subset);
/*  65 */     int[][] metrics = (int[][])longTag.values().toArray((Object[])new int[0][]);
/*  66 */     Arrays.sort(metrics, font);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  71 */     if (font.cff) {
/*  72 */       byte[] b = font.readCffFont();
/*  73 */       if (font.subset || font.subsetRanges != null) {
/*  74 */         CFFFontSubset cff = new CFFFontSubset(new RandomAccessFileOrArray(b), longTag);
/*     */         try {
/*  76 */           b = cff.Process(cff.getNames()[0]);
/*     */         }
/*  78 */         catch (Exception e) {
/*  79 */           LoggerFactory.getLogger(TtfUnicodeWriter.class).error("Issue in CFF font subsetting.Subsetting was disabled", e);
/*     */           
/*  81 */           font.setSubset(false);
/*  82 */           font.addRangeUni(longTag, true, font.subset);
/*  83 */           metrics = (int[][])longTag.values().toArray((Object[])new int[0][]);
/*  84 */           Arrays.sort(metrics, font);
/*     */         } 
/*     */       } 
/*  87 */       PdfObject pobj = new BaseFont.StreamFont(b, "CIDFontType0C", font.compressionLevel);
/*  88 */       PdfIndirectObject obj = this.writer.addToBody(pobj);
/*  89 */       ind_font = obj.getIndirectReference();
/*     */     } else {
/*     */       byte[] b;
/*  92 */       if (font.subset || font.directoryOffset != 0) {
/*  93 */         synchronized (font.rf) {
/*  94 */           TrueTypeFontSubSet sb = new TrueTypeFontSubSet(font.fileName, new RandomAccessFileOrArray(font.rf), new HashSet<Integer>(longTag.keySet()), font.directoryOffset, true, false);
/*  95 */           b = sb.process();
/*     */         } 
/*     */       } else {
/*     */         
/*  99 */         b = font.getFullFont();
/*     */       } 
/* 101 */       int[] lengths = { b.length };
/* 102 */       PdfObject pobj = new BaseFont.StreamFont(b, lengths, font.compressionLevel);
/* 103 */       PdfIndirectObject obj = this.writer.addToBody(pobj);
/* 104 */       ind_font = obj.getIndirectReference();
/*     */     } 
/* 106 */     String str = "";
/* 107 */     if (font.subset)
/* 108 */       str = TrueTypeFontUnicode.createSubsetPrefix(); 
/* 109 */     PdfDictionary dic = font.getFontDescriptor(ind_font, str, (PdfIndirectReference)null);
/* 110 */     PdfIndirectObject pdfIndirectObject = this.writer.addToBody(dic);
/* 111 */     PdfIndirectReference ind_font = pdfIndirectObject.getIndirectReference();
/*     */     
/* 113 */     PdfDictionary pdfDictionary1 = font.getCIDFontType2(ind_font, str, (Object[])metrics);
/* 114 */     pdfIndirectObject = this.writer.addToBody(pdfDictionary1);
/* 115 */     ind_font = pdfIndirectObject.getIndirectReference();
/*     */     
/* 117 */     pdfDictionary1 = font.getToUnicode((Object[])metrics);
/* 118 */     PdfIndirectReference toUnicodeRef = null;
/*     */     
/* 120 */     if (pdfDictionary1 != null) {
/* 121 */       pdfIndirectObject = this.writer.addToBody(pdfDictionary1);
/* 122 */       toUnicodeRef = pdfIndirectObject.getIndirectReference();
/*     */     } 
/*     */     
/* 125 */     pdfDictionary1 = font.getFontBaseType(ind_font, str, toUnicodeRef);
/* 126 */     this.writer.addToBody(pdfDictionary1, ref);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/TtfUnicodeWriter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */