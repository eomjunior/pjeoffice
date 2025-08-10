/*     */ package com.itextpdf.text.pdf;
/*     */ 
/*     */ import com.itextpdf.text.error_messages.MessageLocalization;
/*     */ import com.itextpdf.text.io.StreamUtil;
/*     */ import com.itextpdf.text.pdf.collection.PdfCollectionItem;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.net.URL;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PdfFileSpecification
/*     */   extends PdfDictionary
/*     */ {
/*     */   protected PdfWriter writer;
/*     */   protected PdfIndirectReference ref;
/*     */   
/*     */   public PdfFileSpecification() {
/*  63 */     super(PdfName.FILESPEC);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static PdfFileSpecification url(PdfWriter writer, String url) {
/*  73 */     PdfFileSpecification fs = new PdfFileSpecification();
/*  74 */     fs.writer = writer;
/*  75 */     fs.put(PdfName.FS, PdfName.URL);
/*  76 */     fs.put(PdfName.F, new PdfString(url));
/*  77 */     return fs;
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
/*     */   public static PdfFileSpecification fileEmbedded(PdfWriter writer, String filePath, String fileDisplay, byte[] fileStore) throws IOException {
/*  92 */     return fileEmbedded(writer, filePath, fileDisplay, fileStore, 9);
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
/*     */   public static PdfFileSpecification fileEmbedded(PdfWriter writer, String filePath, String fileDisplay, byte[] fileStore, int compressionLevel) throws IOException {
/* 110 */     return fileEmbedded(writer, filePath, fileDisplay, fileStore, (String)null, (PdfDictionary)null, compressionLevel);
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
/*     */   public static PdfFileSpecification fileEmbedded(PdfWriter writer, String filePath, String fileDisplay, byte[] fileStore, boolean compress) throws IOException {
/* 128 */     return fileEmbedded(writer, filePath, fileDisplay, fileStore, (String)null, (PdfDictionary)null, compress ? 9 : 0);
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
/*     */   public static PdfFileSpecification fileEmbedded(PdfWriter writer, String filePath, String fileDisplay, byte[] fileStore, boolean compress, String mimeType, PdfDictionary fileParameter) throws IOException {
/* 147 */     return fileEmbedded(writer, filePath, fileDisplay, fileStore, mimeType, fileParameter, compress ? 9 : 0);
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
/*     */   public static PdfFileSpecification fileEmbedded(PdfWriter writer, String filePath, String fileDisplay, byte[] fileStore, String mimeType, PdfDictionary fileParameter, int compressionLevel) throws IOException {
/*     */     PdfIndirectReference ref;
/* 166 */     PdfFileSpecification fs = new PdfFileSpecification();
/* 167 */     fs.writer = writer;
/* 168 */     fs.put(PdfName.F, new PdfString(fileDisplay));
/* 169 */     fs.setUnicodeFileName(fileDisplay, false);
/*     */     
/* 171 */     InputStream in = null;
/*     */     
/* 173 */     PdfIndirectReference refFileLength = null; try {
/*     */       PdfEFStream stream;
/* 175 */       if (fileStore == null) {
/* 176 */         refFileLength = writer.getPdfIndirectReference();
/* 177 */         File file = new File(filePath);
/* 178 */         if (file.canRead()) {
/* 179 */           in = new FileInputStream(filePath);
/*     */         
/*     */         }
/* 182 */         else if (filePath.startsWith("file:/") || filePath.startsWith("http://") || filePath.startsWith("https://") || filePath.startsWith("jar:")) {
/* 183 */           in = (new URL(filePath)).openStream();
/*     */         } else {
/*     */           
/* 186 */           in = StreamUtil.getResourceStream(filePath);
/* 187 */           if (in == null) {
/* 188 */             throw new IOException(MessageLocalization.getComposedMessage("1.not.found.as.file.or.resource", new Object[] { filePath }));
/*     */           }
/*     */         } 
/* 191 */         stream = new PdfEFStream(in, writer);
/*     */       } else {
/*     */         
/* 194 */         stream = new PdfEFStream(fileStore);
/*     */       } 
/* 196 */       stream.put(PdfName.TYPE, PdfName.EMBEDDEDFILE);
/* 197 */       stream.flateCompress(compressionLevel);
/* 198 */       PdfDictionary param = new PdfDictionary();
/* 199 */       if (fileParameter != null) {
/* 200 */         param.merge(fileParameter);
/*     */       }
/* 202 */       if (!param.contains(PdfName.MODDATE)) {
/* 203 */         param.put(PdfName.MODDATE, new PdfDate());
/*     */       }
/* 205 */       if (fileStore == null) {
/* 206 */         stream.put(PdfName.PARAMS, refFileLength);
/*     */       } else {
/*     */         
/* 209 */         param.put(PdfName.SIZE, new PdfNumber(stream.getRawLength()));
/* 210 */         stream.put(PdfName.PARAMS, param);
/*     */       } 
/*     */       
/* 213 */       if (mimeType != null) {
/* 214 */         stream.put(PdfName.SUBTYPE, new PdfName(mimeType));
/*     */       }
/* 216 */       ref = writer.addToBody(stream).getIndirectReference();
/* 217 */       if (fileStore == null) {
/* 218 */         stream.writeLength();
/* 219 */         param.put(PdfName.SIZE, new PdfNumber(stream.getRawLength()));
/* 220 */         writer.addToBody(param, refFileLength);
/*     */       } 
/*     */     } finally {
/*     */       
/* 224 */       if (in != null)
/* 225 */         try { in.close(); } catch (Exception exception) {} 
/*     */     } 
/* 227 */     PdfDictionary f = new PdfDictionary();
/* 228 */     f.put(PdfName.F, ref);
/* 229 */     f.put(PdfName.UF, ref);
/* 230 */     fs.put(PdfName.EF, f);
/* 231 */     return fs;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static PdfFileSpecification fileExtern(PdfWriter writer, String filePath) {
/* 241 */     PdfFileSpecification fs = new PdfFileSpecification();
/* 242 */     fs.writer = writer;
/* 243 */     fs.put(PdfName.F, new PdfString(filePath));
/* 244 */     fs.setUnicodeFileName(filePath, false);
/* 245 */     return fs;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfIndirectReference getReference() throws IOException {
/* 255 */     if (this.ref != null)
/* 256 */       return this.ref; 
/* 257 */     this.ref = this.writer.addToBody(this).getIndirectReference();
/* 258 */     return this.ref;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMultiByteFileName(byte[] fileName) {
/* 268 */     put(PdfName.F, (new PdfString(fileName)).setHexWriting(true));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUnicodeFileName(String filename, boolean unicode) {
/* 279 */     put(PdfName.UF, new PdfString(filename, unicode ? "UnicodeBig" : "PDF"));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setVolatile(boolean volatile_file) {
/* 289 */     put(PdfName.V, new PdfBoolean(volatile_file));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addDescription(String description, boolean unicode) {
/* 298 */     put(PdfName.DESC, new PdfString(description, unicode ? "UnicodeBig" : "PDF"));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addCollectionItem(PdfCollectionItem ci) {
/* 305 */     put(PdfName.CI, (PdfObject)ci);
/*     */   }
/*     */ 
/*     */   
/*     */   public void toPdf(PdfWriter writer, OutputStream os) throws IOException {
/* 310 */     PdfWriter.checkPdfIsoConformance(writer, 10, this);
/* 311 */     super.toPdf(writer, os);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/PdfFileSpecification.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */