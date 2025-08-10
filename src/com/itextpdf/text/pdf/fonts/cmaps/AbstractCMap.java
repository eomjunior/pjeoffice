/*     */ package com.itextpdf.text.pdf.fonts.cmaps;
/*     */ 
/*     */ import com.itextpdf.text.pdf.PdfArray;
/*     */ import com.itextpdf.text.pdf.PdfEncodings;
/*     */ import com.itextpdf.text.pdf.PdfNumber;
/*     */ import com.itextpdf.text.pdf.PdfObject;
/*     */ import com.itextpdf.text.pdf.PdfString;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractCMap
/*     */ {
/*     */   private String cmapName;
/*     */   private String registry;
/*     */   private String ordering;
/*     */   private int supplement;
/*     */   
/*     */   public String getName() {
/*  64 */     return this.cmapName;
/*     */   }
/*     */   
/*     */   void setName(String cmapName) {
/*  68 */     this.cmapName = cmapName;
/*     */   }
/*     */   
/*     */   public String getOrdering() {
/*  72 */     return this.ordering;
/*     */   }
/*     */   
/*     */   void setOrdering(String ordering) {
/*  76 */     this.ordering = ordering;
/*     */   }
/*     */   
/*     */   public String getRegistry() {
/*  80 */     return this.registry;
/*     */   }
/*     */   
/*     */   void setRegistry(String registry) {
/*  84 */     this.registry = registry;
/*     */   }
/*     */   
/*     */   public int getSupplement() {
/*  88 */     return this.supplement;
/*     */   }
/*     */   
/*     */   void setSupplement(int supplement) {
/*  92 */     this.supplement = supplement;
/*     */   }
/*     */   
/*     */   abstract void addChar(PdfString paramPdfString, PdfObject paramPdfObject);
/*     */   
/*     */   void addRange(PdfString from, PdfString to, PdfObject code) {
/*  98 */     byte[] a1 = decodeStringToByte(from);
/*  99 */     byte[] a2 = decodeStringToByte(to);
/* 100 */     if (a1.length != a2.length || a1.length == 0)
/* 101 */       throw new IllegalArgumentException("Invalid map."); 
/* 102 */     byte[] sout = null;
/* 103 */     if (code instanceof PdfString)
/* 104 */       sout = decodeStringToByte((PdfString)code); 
/* 105 */     int start = byteArrayToInt(a1);
/* 106 */     int end = byteArrayToInt(a2);
/* 107 */     for (int k = start; k <= end; k++) {
/* 108 */       intToByteArray(k, a1);
/* 109 */       PdfString s = new PdfString(a1);
/* 110 */       s.setHexWriting(true);
/* 111 */       if (code instanceof PdfArray) {
/* 112 */         addChar(s, ((PdfArray)code).getPdfObject(k - start));
/*     */       }
/* 114 */       else if (code instanceof PdfNumber) {
/* 115 */         int nn = ((PdfNumber)code).intValue() + k - start;
/* 116 */         addChar(s, (PdfObject)new PdfNumber(nn));
/*     */       }
/* 118 */       else if (code instanceof PdfString) {
/* 119 */         PdfString s1 = new PdfString(sout);
/* 120 */         s1.setHexWriting(true);
/* 121 */         sout[sout.length - 1] = (byte)(sout[sout.length - 1] + 1);
/* 122 */         addChar(s, (PdfObject)s1);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void intToByteArray(int v, byte[] b) {
/* 128 */     for (int k = b.length - 1; k >= 0; k--) {
/* 129 */       b[k] = (byte)v;
/* 130 */       v >>>= 8;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static int byteArrayToInt(byte[] b) {
/* 135 */     int v = 0;
/* 136 */     for (int k = 0; k < b.length; k++) {
/* 137 */       v <<= 8;
/* 138 */       v |= b[k] & 0xFF;
/*     */     } 
/* 140 */     return v;
/*     */   }
/*     */   
/*     */   public static byte[] decodeStringToByte(PdfString s) {
/* 144 */     byte[] b = s.getBytes();
/* 145 */     byte[] br = new byte[b.length];
/* 146 */     System.arraycopy(b, 0, br, 0, b.length);
/* 147 */     return br;
/*     */   }
/*     */   
/*     */   public String decodeStringToUnicode(PdfString ps) {
/* 151 */     if (ps.isHexWriting()) {
/* 152 */       return PdfEncodings.convertToString(ps.getBytes(), "UnicodeBigUnmarked");
/*     */     }
/* 154 */     return ps.toUnicodeString();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/fonts/cmaps/AbstractCMap.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */