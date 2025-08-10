/*     */ package com.itextpdf.text.pdf.security;
/*     */ 
/*     */ import com.itextpdf.text.DocumentException;
/*     */ import com.itextpdf.text.Rectangle;
/*     */ import com.itextpdf.text.pdf.PdfDeveloperExtension;
/*     */ import com.itextpdf.text.pdf.PdfDictionary;
/*     */ import com.itextpdf.text.pdf.PdfName;
/*     */ import com.itextpdf.text.pdf.PdfObject;
/*     */ import com.itextpdf.text.pdf.PdfSignature;
/*     */ import com.itextpdf.text.pdf.PdfSignatureAppearance;
/*     */ import com.itextpdf.text.pdf.PdfString;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.security.GeneralSecurityException;
/*     */ import java.security.MessageDigest;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LtvTimestamp
/*     */ {
/*     */   public static void timestamp(PdfSignatureAppearance sap, TSAClient tsa, String signatureName) throws IOException, DocumentException, GeneralSecurityException {
/*     */     byte[] tsToken;
/*  77 */     int contentEstimated = tsa.getTokenSizeEstimate();
/*  78 */     sap.addDeveloperExtension(PdfDeveloperExtension.ESIC_1_7_EXTENSIONLEVEL5);
/*  79 */     sap.setVisibleSignature(new Rectangle(0.0F, 0.0F, 0.0F, 0.0F), 1, signatureName);
/*     */     
/*  81 */     PdfSignature dic = new PdfSignature(PdfName.ADOBE_PPKLITE, PdfName.ETSI_RFC3161);
/*  82 */     dic.put(PdfName.TYPE, (PdfObject)PdfName.DOCTIMESTAMP);
/*  83 */     sap.setCryptoDictionary((PdfDictionary)dic);
/*     */     
/*  85 */     HashMap<PdfName, Integer> exc = new HashMap<PdfName, Integer>();
/*  86 */     exc.put(PdfName.CONTENTS, new Integer(contentEstimated * 2 + 2));
/*  87 */     sap.preClose(exc);
/*  88 */     InputStream data = sap.getRangeStream();
/*  89 */     MessageDigest messageDigest = tsa.getMessageDigest();
/*  90 */     byte[] buf = new byte[4096];
/*     */     int n;
/*  92 */     while ((n = data.read(buf)) > 0) {
/*  93 */       messageDigest.update(buf, 0, n);
/*     */     }
/*  95 */     byte[] tsImprint = messageDigest.digest();
/*     */     
/*     */     try {
/*  98 */       tsToken = tsa.getTimeStampToken(tsImprint);
/*     */     }
/* 100 */     catch (Exception e) {
/* 101 */       throw new GeneralSecurityException(e);
/*     */     } 
/*     */     
/* 104 */     if (contentEstimated + 2 < tsToken.length) {
/* 105 */       throw new IOException("Not enough space");
/*     */     }
/* 107 */     byte[] paddedSig = new byte[contentEstimated];
/* 108 */     System.arraycopy(tsToken, 0, paddedSig, 0, tsToken.length);
/*     */     
/* 110 */     PdfDictionary dic2 = new PdfDictionary();
/* 111 */     dic2.put(PdfName.CONTENTS, (PdfObject)(new PdfString(paddedSig)).setHexWriting(true));
/* 112 */     sap.close(dic2);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/security/LtvTimestamp.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */