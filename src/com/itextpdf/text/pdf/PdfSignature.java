/*     */ package com.itextpdf.text.pdf;
/*     */ 
/*     */ import com.itextpdf.text.pdf.security.PdfSignatureBuildProperties;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PdfSignature
/*     */   extends PdfDictionary
/*     */ {
/*     */   public PdfSignature(PdfName filter, PdfName subFilter) {
/*  57 */     super(PdfName.SIG);
/*  58 */     put(PdfName.FILTER, filter);
/*  59 */     put(PdfName.SUBFILTER, subFilter);
/*     */   }
/*     */   
/*     */   public void setByteRange(int[] range) {
/*  63 */     PdfArray array = new PdfArray();
/*  64 */     for (int k = 0; k < range.length; k++)
/*  65 */       array.add(new PdfNumber(range[k])); 
/*  66 */     put(PdfName.BYTERANGE, array);
/*     */   }
/*     */   
/*     */   public void setContents(byte[] contents) {
/*  70 */     put(PdfName.CONTENTS, (new PdfString(contents)).setHexWriting(true));
/*     */   }
/*     */   
/*     */   public void setCert(byte[] cert) {
/*  74 */     put(PdfName.CERT, new PdfString(cert));
/*     */   }
/*     */   
/*     */   public void setName(String name) {
/*  78 */     put(PdfName.NAME, new PdfString(name, "UnicodeBig"));
/*     */   }
/*     */   
/*     */   public void setDate(PdfDate date) {
/*  82 */     put(PdfName.M, date);
/*     */   }
/*     */   
/*     */   public void setLocation(String name) {
/*  86 */     put(PdfName.LOCATION, new PdfString(name, "UnicodeBig"));
/*     */   }
/*     */   
/*     */   public void setReason(String name) {
/*  90 */     put(PdfName.REASON, new PdfString(name, "UnicodeBig"));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSignatureCreator(String name) {
/* 100 */     if (name != null) {
/* 101 */       getPdfSignatureBuildProperties().setSignatureCreator(name);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   PdfSignatureBuildProperties getPdfSignatureBuildProperties() {
/* 112 */     PdfSignatureBuildProperties buildPropDic = (PdfSignatureBuildProperties)getAsDict(PdfName.PROP_BUILD);
/* 113 */     if (buildPropDic == null) {
/* 114 */       buildPropDic = new PdfSignatureBuildProperties();
/* 115 */       put(PdfName.PROP_BUILD, (PdfObject)buildPropDic);
/*     */     } 
/* 117 */     return buildPropDic;
/*     */   }
/*     */   
/*     */   public void setContact(String name) {
/* 121 */     put(PdfName.CONTACTINFO, new PdfString(name, "UnicodeBig"));
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/PdfSignature.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */