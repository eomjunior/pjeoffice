/*     */ package com.itextpdf.text.pdf;
/*     */ 
/*     */ import com.itextpdf.text.DocWriter;
/*     */ import com.itextpdf.text.Document;
/*     */ import com.itextpdf.text.ExceptionConverter;
/*     */ import com.itextpdf.text.error_messages.MessageLocalization;
/*     */ import java.io.ByteArrayOutputStream;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PdfStream
/*     */   extends PdfDictionary
/*     */ {
/*     */   public static final int DEFAULT_COMPRESSION = -1;
/*     */   public static final int NO_COMPRESSION = 0;
/*     */   public static final int BEST_SPEED = 1;
/*     */   public static final int BEST_COMPRESSION = 9;
/*     */   protected boolean compressed = false;
/* 112 */   protected int compressionLevel = 0;
/*     */   
/* 114 */   protected ByteArrayOutputStream streamBytes = null;
/*     */   protected InputStream inputStream;
/*     */   protected PdfIndirectReference ref;
/* 117 */   protected int inputStreamLength = -1;
/*     */   
/*     */   protected PdfWriter writer;
/*     */   protected int rawLength;
/* 121 */   static final byte[] STARTSTREAM = DocWriter.getISOBytes("stream\n");
/* 122 */   static final byte[] ENDSTREAM = DocWriter.getISOBytes("\nendstream");
/* 123 */   static final int SIZESTREAM = STARTSTREAM.length + ENDSTREAM.length;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfStream(byte[] bytes) {
/* 135 */     this.type = 7;
/* 136 */     this.bytes = bytes;
/* 137 */     this.rawLength = bytes.length;
/* 138 */     put(PdfName.LENGTH, new PdfNumber(bytes.length));
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
/*     */   public PdfStream(InputStream inputStream, PdfWriter writer) {
/* 158 */     this.type = 7;
/* 159 */     this.inputStream = inputStream;
/* 160 */     this.writer = writer;
/* 161 */     this.ref = writer.getPdfIndirectReference();
/* 162 */     put(PdfName.LENGTH, this.ref);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected PdfStream() {
/* 171 */     this.type = 7;
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
/*     */   public void writeLength() throws IOException {
/* 183 */     if (this.inputStream == null)
/* 184 */       throw new UnsupportedOperationException(MessageLocalization.getComposedMessage("writelength.can.only.be.called.in.a.contructed.pdfstream.inputstream.pdfwriter", new Object[0])); 
/* 185 */     if (this.inputStreamLength == -1)
/* 186 */       throw new IOException(MessageLocalization.getComposedMessage("writelength.can.only.be.called.after.output.of.the.stream.body", new Object[0])); 
/* 187 */     this.writer.addToBody(new PdfNumber(this.inputStreamLength), this.ref, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getRawLength() {
/* 195 */     return this.rawLength;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void flateCompress() {
/* 202 */     flateCompress(-1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void flateCompress(int compressionLevel) {
/* 211 */     if (!Document.compress) {
/*     */       return;
/*     */     }
/* 214 */     if (this.compressed) {
/*     */       return;
/*     */     }
/* 217 */     this.compressionLevel = compressionLevel;
/* 218 */     if (this.inputStream != null) {
/* 219 */       this.compressed = true;
/*     */       
/*     */       return;
/*     */     } 
/* 223 */     PdfObject filter = PdfReader.getPdfObject(get(PdfName.FILTER));
/* 224 */     if (filter != null) {
/* 225 */       if (filter.isName()) {
/* 226 */         if (PdfName.FLATEDECODE.equals(filter)) {
/*     */           return;
/*     */         }
/* 229 */       } else if (filter.isArray()) {
/* 230 */         if (((PdfArray)filter).contains(PdfName.FLATEDECODE)) {
/*     */           return;
/*     */         }
/*     */       } else {
/* 234 */         throw new RuntimeException(MessageLocalization.getComposedMessage("stream.could.not.be.compressed.filter.is.not.a.name.or.array", new Object[0]));
/*     */       } 
/*     */     }
/*     */     
/*     */     try {
/* 239 */       ByteArrayOutputStream stream = new ByteArrayOutputStream();
/* 240 */       Deflater deflater = new Deflater(compressionLevel);
/* 241 */       DeflaterOutputStream zip = new DeflaterOutputStream(stream, deflater);
/* 242 */       if (this.streamBytes != null) {
/* 243 */         this.streamBytes.writeTo(zip);
/*     */       } else {
/* 245 */         zip.write(this.bytes);
/* 246 */       }  zip.close();
/* 247 */       deflater.end();
/*     */       
/* 249 */       this.streamBytes = stream;
/* 250 */       this.bytes = null;
/* 251 */       put(PdfName.LENGTH, new PdfNumber(this.streamBytes.size()));
/* 252 */       if (filter == null) {
/* 253 */         put(PdfName.FILTER, PdfName.FLATEDECODE);
/*     */       } else {
/*     */         
/* 256 */         PdfArray filters = new PdfArray(filter);
/* 257 */         filters.add(0, PdfName.FLATEDECODE);
/* 258 */         put(PdfName.FILTER, filters);
/*     */       } 
/* 260 */       this.compressed = true;
/*     */     }
/* 262 */     catch (IOException ioe) {
/* 263 */       throw new ExceptionConverter(ioe);
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
/*     */ 
/*     */   
/*     */   protected void superToPdf(PdfWriter writer, OutputStream os) throws IOException {
/* 277 */     super.toPdf(writer, os);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void toPdf(PdfWriter writer, OutputStream os) throws IOException {
/* 284 */     if (this.inputStream != null && this.compressed)
/* 285 */       put(PdfName.FILTER, PdfName.FLATEDECODE); 
/* 286 */     PdfEncryption crypto = null;
/* 287 */     if (writer != null)
/* 288 */       crypto = writer.getEncryption(); 
/* 289 */     if (crypto != null) {
/* 290 */       PdfObject filter = get(PdfName.FILTER);
/* 291 */       if (filter != null)
/* 292 */         if (PdfName.CRYPT.equals(filter)) {
/* 293 */           crypto = null;
/* 294 */         } else if (filter.isArray()) {
/* 295 */           PdfArray a = (PdfArray)filter;
/* 296 */           if (!a.isEmpty() && PdfName.CRYPT.equals(a.getPdfObject(0))) {
/* 297 */             crypto = null;
/*     */           }
/*     */         }  
/*     */     } 
/* 301 */     PdfObject nn = get(PdfName.LENGTH);
/* 302 */     if (crypto != null && nn != null && nn.isNumber()) {
/* 303 */       int sz = ((PdfNumber)nn).intValue();
/* 304 */       put(PdfName.LENGTH, new PdfNumber(crypto.calculateStreamSize(sz)));
/* 305 */       superToPdf(writer, os);
/* 306 */       put(PdfName.LENGTH, nn);
/*     */     } else {
/*     */       
/* 309 */       superToPdf(writer, os);
/* 310 */     }  PdfWriter.checkPdfIsoConformance(writer, 9, this);
/* 311 */     os.write(STARTSTREAM);
/* 312 */     if (this.inputStream != null) {
/* 313 */       this.rawLength = 0;
/* 314 */       DeflaterOutputStream def = null;
/* 315 */       OutputStreamCounter osc = new OutputStreamCounter(os);
/* 316 */       OutputStreamEncryption ose = null;
/* 317 */       OutputStream fout = osc;
/* 318 */       if (crypto != null && !crypto.isEmbeddedFilesOnly())
/* 319 */         fout = ose = crypto.getEncryptionStream(fout); 
/* 320 */       Deflater deflater = null;
/* 321 */       if (this.compressed) {
/* 322 */         deflater = new Deflater(this.compressionLevel);
/* 323 */         fout = def = new DeflaterOutputStream(fout, deflater, 32768);
/*     */       } 
/*     */       
/* 326 */       byte[] buf = new byte[4192];
/*     */       while (true) {
/* 328 */         int n = this.inputStream.read(buf);
/* 329 */         if (n <= 0)
/*     */           break; 
/* 331 */         fout.write(buf, 0, n);
/* 332 */         this.rawLength += n;
/*     */       } 
/* 334 */       if (def != null) {
/* 335 */         def.finish();
/* 336 */         deflater.end();
/*     */       } 
/* 338 */       if (ose != null)
/* 339 */         ose.finish(); 
/* 340 */       this.inputStreamLength = (int)osc.getCounter();
/*     */     
/*     */     }
/* 343 */     else if (crypto != null && !crypto.isEmbeddedFilesOnly()) {
/*     */       byte[] b;
/* 345 */       if (this.streamBytes != null) {
/* 346 */         b = crypto.encryptByteArray(this.streamBytes.toByteArray());
/*     */       } else {
/*     */         
/* 349 */         b = crypto.encryptByteArray(this.bytes);
/*     */       } 
/* 351 */       os.write(b);
/*     */     
/*     */     }
/* 354 */     else if (this.streamBytes != null) {
/* 355 */       this.streamBytes.writeTo(os);
/*     */     } else {
/* 357 */       os.write(this.bytes);
/*     */     } 
/*     */     
/* 360 */     os.write(ENDSTREAM);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void writeContent(OutputStream os) throws IOException {
/* 369 */     if (this.streamBytes != null) {
/* 370 */       this.streamBytes.writeTo(os);
/* 371 */     } else if (this.bytes != null) {
/* 372 */       os.write(this.bytes);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 379 */     if (get(PdfName.TYPE) == null) return "Stream"; 
/* 380 */     return "Stream of type: " + get(PdfName.TYPE);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/PdfStream.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */