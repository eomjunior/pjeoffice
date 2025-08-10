/*     */ package com.itextpdf.testutils;
/*     */ 
/*     */ import com.itextpdf.text.log.Logger;
/*     */ import com.itextpdf.text.log.LoggerFactory;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class ITextTest
/*     */ {
/*  54 */   private static final Logger LOGGER = LoggerFactory.getLogger(ITextTest.class.getName());
/*     */   
/*     */   public void runTest() throws Exception {
/*  57 */     LOGGER.info("Starting test.");
/*  58 */     String outPdf = getOutPdf();
/*  59 */     if (outPdf == null || outPdf.length() == 0)
/*  60 */       throw new IOException("outPdf cannot be empty!"); 
/*  61 */     makePdf(outPdf);
/*  62 */     assertPdf(outPdf);
/*  63 */     comparePdf(outPdf, getCmpPdf());
/*  64 */     LOGGER.info("Test complete.");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void makePdf(String paramString) throws Exception;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract String getOutPdf();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void assertPdf(String outPdf) throws Exception {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void comparePdf(String outPdf, String cmpPdf) throws Exception {}
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getCmpPdf() {
/*  90 */     return "";
/*     */   }
/*     */   
/*     */   protected void deleteDirectory(File path) {
/*  94 */     if (path == null)
/*     */       return; 
/*  96 */     if (path.exists()) {
/*  97 */       for (File f : path.listFiles()) {
/*  98 */         if (f.isDirectory()) {
/*  99 */           deleteDirectory(f);
/* 100 */           f.delete();
/*     */         } else {
/* 102 */           f.delete();
/*     */         } 
/*     */       } 
/* 105 */       path.delete();
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void deleteFiles(File path) {
/* 110 */     if (path != null && path.exists())
/* 111 */       for (File f : path.listFiles())
/* 112 */         f.delete();  
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/testutils/ITextTest.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */