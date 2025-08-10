/*     */ package com.itextpdf.text.pdf;
/*     */ 
/*     */ import com.itextpdf.text.ExceptionConverter;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PdfFunction
/*     */ {
/*     */   protected PdfWriter writer;
/*     */   protected PdfIndirectReference reference;
/*     */   protected PdfDictionary dictionary;
/*     */   
/*     */   protected PdfFunction(PdfWriter writer) {
/*  63 */     this.writer = writer;
/*     */   }
/*     */   
/*     */   PdfIndirectReference getReference() {
/*     */     try {
/*  68 */       if (this.reference == null) {
/*  69 */         this.reference = this.writer.addToBody(this.dictionary).getIndirectReference();
/*     */       }
/*     */     }
/*  72 */     catch (IOException ioe) {
/*  73 */       throw new ExceptionConverter(ioe);
/*     */     } 
/*  75 */     return this.reference;
/*     */   }
/*     */ 
/*     */   
/*     */   public static PdfFunction type0(PdfWriter writer, float[] domain, float[] range, int[] size, int bitsPerSample, int order, float[] encode, float[] decode, byte[] stream) {
/*  80 */     PdfFunction func = new PdfFunction(writer);
/*  81 */     func.dictionary = new PdfStream(stream);
/*  82 */     ((PdfStream)func.dictionary).flateCompress(writer.getCompressionLevel());
/*  83 */     func.dictionary.put(PdfName.FUNCTIONTYPE, new PdfNumber(0));
/*  84 */     func.dictionary.put(PdfName.DOMAIN, new PdfArray(domain));
/*  85 */     func.dictionary.put(PdfName.RANGE, new PdfArray(range));
/*  86 */     func.dictionary.put(PdfName.SIZE, new PdfArray(size));
/*  87 */     func.dictionary.put(PdfName.BITSPERSAMPLE, new PdfNumber(bitsPerSample));
/*  88 */     if (order != 1)
/*  89 */       func.dictionary.put(PdfName.ORDER, new PdfNumber(order)); 
/*  90 */     if (encode != null)
/*  91 */       func.dictionary.put(PdfName.ENCODE, new PdfArray(encode)); 
/*  92 */     if (decode != null)
/*  93 */       func.dictionary.put(PdfName.DECODE, new PdfArray(decode)); 
/*  94 */     return func;
/*     */   }
/*     */   
/*     */   public static PdfFunction type2(PdfWriter writer, float[] domain, float[] range, float[] c0, float[] c1, float n) {
/*  98 */     PdfFunction func = new PdfFunction(writer);
/*  99 */     func.dictionary = new PdfDictionary();
/* 100 */     func.dictionary.put(PdfName.FUNCTIONTYPE, new PdfNumber(2));
/* 101 */     func.dictionary.put(PdfName.DOMAIN, new PdfArray(domain));
/* 102 */     if (range != null)
/* 103 */       func.dictionary.put(PdfName.RANGE, new PdfArray(range)); 
/* 104 */     if (c0 != null)
/* 105 */       func.dictionary.put(PdfName.C0, new PdfArray(c0)); 
/* 106 */     if (c1 != null)
/* 107 */       func.dictionary.put(PdfName.C1, new PdfArray(c1)); 
/* 108 */     func.dictionary.put(PdfName.N, new PdfNumber(n));
/* 109 */     return func;
/*     */   }
/*     */   
/*     */   public static PdfFunction type3(PdfWriter writer, float[] domain, float[] range, PdfFunction[] functions, float[] bounds, float[] encode) {
/* 113 */     PdfFunction func = new PdfFunction(writer);
/* 114 */     func.dictionary = new PdfDictionary();
/* 115 */     func.dictionary.put(PdfName.FUNCTIONTYPE, new PdfNumber(3));
/* 116 */     func.dictionary.put(PdfName.DOMAIN, new PdfArray(domain));
/* 117 */     if (range != null)
/* 118 */       func.dictionary.put(PdfName.RANGE, new PdfArray(range)); 
/* 119 */     PdfArray array = new PdfArray();
/* 120 */     for (int k = 0; k < functions.length; k++)
/* 121 */       array.add(functions[k].getReference()); 
/* 122 */     func.dictionary.put(PdfName.FUNCTIONS, array);
/* 123 */     func.dictionary.put(PdfName.BOUNDS, new PdfArray(bounds));
/* 124 */     func.dictionary.put(PdfName.ENCODE, new PdfArray(encode));
/* 125 */     return func;
/*     */   }
/*     */   
/*     */   public static PdfFunction type4(PdfWriter writer, float[] domain, float[] range, String postscript) {
/* 129 */     byte[] b = new byte[postscript.length()];
/* 130 */     for (int k = 0; k < b.length; k++)
/* 131 */       b[k] = (byte)postscript.charAt(k); 
/* 132 */     PdfFunction func = new PdfFunction(writer);
/* 133 */     func.dictionary = new PdfStream(b);
/* 134 */     ((PdfStream)func.dictionary).flateCompress(writer.getCompressionLevel());
/* 135 */     func.dictionary.put(PdfName.FUNCTIONTYPE, new PdfNumber(4));
/* 136 */     func.dictionary.put(PdfName.DOMAIN, new PdfArray(domain));
/* 137 */     func.dictionary.put(PdfName.RANGE, new PdfArray(range));
/* 138 */     return func;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/PdfFunction.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */