/*     */ package com.itextpdf.text.pdf;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.util.zip.Deflater;
/*     */ import java.util.zip.DeflaterOutputStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PdfEFStream
/*     */   extends PdfStream
/*     */ {
/*     */   public PdfEFStream(InputStream in, PdfWriter writer) {
/*  66 */     super(in, writer);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfEFStream(byte[] fileStore) {
/*  74 */     super(fileStore);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void toPdf(PdfWriter writer, OutputStream os) throws IOException {
/*  81 */     if (this.inputStream != null && this.compressed)
/*  82 */       put(PdfName.FILTER, PdfName.FLATEDECODE); 
/*  83 */     PdfEncryption crypto = null;
/*  84 */     if (writer != null)
/*  85 */       crypto = writer.getEncryption(); 
/*  86 */     if (crypto != null) {
/*  87 */       PdfObject filter = get(PdfName.FILTER);
/*  88 */       if (filter != null)
/*  89 */         if (PdfName.CRYPT.equals(filter)) {
/*  90 */           crypto = null;
/*  91 */         } else if (filter.isArray()) {
/*  92 */           PdfArray a = (PdfArray)filter;
/*  93 */           if (!a.isEmpty() && PdfName.CRYPT.equals(a.getPdfObject(0))) {
/*  94 */             crypto = null;
/*     */           }
/*     */         }  
/*     */     } 
/*  98 */     if (crypto != null && crypto.isEmbeddedFilesOnly()) {
/*  99 */       PdfArray filter = new PdfArray();
/* 100 */       PdfArray decodeparms = new PdfArray();
/* 101 */       PdfDictionary crypt = new PdfDictionary();
/* 102 */       crypt.put(PdfName.NAME, PdfName.STDCF);
/* 103 */       filter.add(PdfName.CRYPT);
/* 104 */       decodeparms.add(crypt);
/* 105 */       if (this.compressed) {
/* 106 */         filter.add(PdfName.FLATEDECODE);
/* 107 */         decodeparms.add(new PdfNull());
/*     */       } 
/* 109 */       put(PdfName.FILTER, filter);
/* 110 */       put(PdfName.DECODEPARMS, decodeparms);
/*     */     } 
/* 112 */     PdfObject nn = get(PdfName.LENGTH);
/* 113 */     if (crypto != null && nn != null && nn.isNumber()) {
/* 114 */       int sz = ((PdfNumber)nn).intValue();
/* 115 */       put(PdfName.LENGTH, new PdfNumber(crypto.calculateStreamSize(sz)));
/* 116 */       superToPdf(writer, os);
/* 117 */       put(PdfName.LENGTH, nn);
/*     */     } else {
/*     */       
/* 120 */       superToPdf(writer, os);
/*     */     } 
/* 122 */     os.write(STARTSTREAM);
/* 123 */     if (this.inputStream != null) {
/* 124 */       this.rawLength = 0;
/* 125 */       DeflaterOutputStream def = null;
/* 126 */       OutputStreamCounter osc = new OutputStreamCounter(os);
/* 127 */       OutputStreamEncryption ose = null;
/* 128 */       OutputStream fout = osc;
/* 129 */       if (crypto != null)
/* 130 */         fout = ose = crypto.getEncryptionStream(fout); 
/* 131 */       Deflater deflater = null;
/* 132 */       if (this.compressed) {
/* 133 */         deflater = new Deflater(this.compressionLevel);
/* 134 */         fout = def = new DeflaterOutputStream(fout, deflater, 32768);
/*     */       } 
/*     */       
/* 137 */       byte[] buf = new byte[4192];
/*     */       while (true) {
/* 139 */         int n = this.inputStream.read(buf);
/* 140 */         if (n <= 0)
/*     */           break; 
/* 142 */         fout.write(buf, 0, n);
/* 143 */         this.rawLength += n;
/*     */       } 
/* 145 */       if (def != null) {
/* 146 */         def.finish();
/* 147 */         deflater.end();
/*     */       } 
/* 149 */       if (ose != null)
/* 150 */         ose.finish(); 
/* 151 */       this.inputStreamLength = (int)osc.getCounter();
/*     */     
/*     */     }
/* 154 */     else if (crypto == null) {
/* 155 */       if (this.streamBytes != null) {
/* 156 */         this.streamBytes.writeTo(os);
/*     */       } else {
/* 158 */         os.write(this.bytes);
/*     */       } 
/*     */     } else {
/*     */       byte[] b;
/* 162 */       if (this.streamBytes != null) {
/* 163 */         b = crypto.encryptByteArray(this.streamBytes.toByteArray());
/*     */       } else {
/*     */         
/* 166 */         b = crypto.encryptByteArray(this.bytes);
/*     */       } 
/* 168 */       os.write(b);
/*     */     } 
/*     */     
/* 171 */     os.write(ENDSTREAM);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/PdfEFStream.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */