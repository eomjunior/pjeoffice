/*     */ package com.itextpdf.text.pdf;
/*     */ 
/*     */ import com.itextpdf.text.log.Counter;
/*     */ import com.itextpdf.text.log.CounterFactory;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.URL;
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
/*     */ public class FdfReader
/*     */   extends PdfReader
/*     */ {
/*     */   HashMap<String, PdfDictionary> fields;
/*     */   String fileSpec;
/*     */   PdfName encoding;
/*     */   
/*     */   public FdfReader(String filename) throws IOException {
/*  66 */     super(filename);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FdfReader(byte[] pdfIn) throws IOException {
/*  74 */     super(pdfIn);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FdfReader(URL url) throws IOException {
/*  82 */     super(url);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FdfReader(InputStream is) throws IOException {
/*  91 */     super(is);
/*     */   }
/*     */   
/*  94 */   protected static Counter COUNTER = CounterFactory.getCounter(FdfReader.class);
/*     */   protected Counter getCounter() {
/*  96 */     return COUNTER;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readPdf() throws IOException {
/* 101 */     this.fields = new HashMap<String, PdfDictionary>();
/* 102 */     this.tokens.checkFdfHeader();
/* 103 */     rebuildXref();
/* 104 */     readDocObj();
/* 105 */     readFields();
/*     */   }
/*     */   
/*     */   protected void kidNode(PdfDictionary merged, String name) {
/* 109 */     PdfArray kids = merged.getAsArray(PdfName.KIDS);
/* 110 */     if (kids == null || kids.isEmpty()) {
/* 111 */       if (name.length() > 0)
/* 112 */         name = name.substring(1); 
/* 113 */       this.fields.put(name, merged);
/*     */     } else {
/*     */       
/* 116 */       merged.remove(PdfName.KIDS);
/* 117 */       for (int k = 0; k < kids.size(); k++) {
/* 118 */         PdfDictionary dic = new PdfDictionary();
/* 119 */         dic.merge(merged);
/* 120 */         PdfDictionary newDic = kids.getAsDict(k);
/* 121 */         PdfString t = newDic.getAsString(PdfName.T);
/* 122 */         String newName = name;
/* 123 */         if (t != null)
/* 124 */           newName = newName + "." + t.toUnicodeString(); 
/* 125 */         dic.merge(newDic);
/* 126 */         dic.remove(PdfName.T);
/* 127 */         kidNode(dic, newName);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void readFields() {
/* 133 */     this.catalog = this.trailer.getAsDict(PdfName.ROOT);
/* 134 */     PdfDictionary fdf = this.catalog.getAsDict(PdfName.FDF);
/* 135 */     if (fdf == null)
/*     */       return; 
/* 137 */     PdfString fs = fdf.getAsString(PdfName.F);
/* 138 */     if (fs != null)
/* 139 */       this.fileSpec = fs.toUnicodeString(); 
/* 140 */     PdfArray fld = fdf.getAsArray(PdfName.FIELDS);
/* 141 */     if (fld == null)
/*     */       return; 
/* 143 */     this.encoding = fdf.getAsName(PdfName.ENCODING);
/* 144 */     PdfDictionary merged = new PdfDictionary();
/* 145 */     merged.put(PdfName.KIDS, fld);
/* 146 */     kidNode(merged, "");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HashMap<String, PdfDictionary> getFields() {
/* 155 */     return this.fields;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfDictionary getField(String name) {
/* 163 */     return this.fields.get(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getAttachedFile(String name) throws IOException {
/* 174 */     PdfDictionary field = this.fields.get(name);
/* 175 */     if (field != null) {
/* 176 */       PdfIndirectReference ir = (PRIndirectReference)field.get(PdfName.V);
/* 177 */       PdfDictionary filespec = (PdfDictionary)getPdfObject(ir.getNumber());
/* 178 */       PdfDictionary ef = filespec.getAsDict(PdfName.EF);
/* 179 */       ir = (PRIndirectReference)ef.get(PdfName.F);
/* 180 */       PRStream stream = (PRStream)getPdfObject(ir.getNumber());
/* 181 */       return getStreamBytes(stream);
/*     */     } 
/* 183 */     return new byte[0];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFieldValue(String name) {
/* 193 */     PdfDictionary field = this.fields.get(name);
/* 194 */     if (field == null)
/* 195 */       return null; 
/* 196 */     PdfObject v = getPdfObject(field.get(PdfName.V));
/* 197 */     if (v == null)
/* 198 */       return null; 
/* 199 */     if (v.isName())
/* 200 */       return PdfName.decodeName(((PdfName)v).toString()); 
/* 201 */     if (v.isString()) {
/* 202 */       PdfString vs = (PdfString)v;
/* 203 */       if (this.encoding == null || vs.getEncoding() != null)
/* 204 */         return vs.toUnicodeString(); 
/* 205 */       byte[] b = vs.getBytes();
/* 206 */       if (b.length >= 2 && b[0] == -2 && b[1] == -1)
/* 207 */         return vs.toUnicodeString(); 
/*     */       try {
/* 209 */         if (this.encoding.equals(PdfName.SHIFT_JIS))
/* 210 */           return new String(b, "SJIS"); 
/* 211 */         if (this.encoding.equals(PdfName.UHC))
/* 212 */           return new String(b, "MS949"); 
/* 213 */         if (this.encoding.equals(PdfName.GBK))
/* 214 */           return new String(b, "GBK"); 
/* 215 */         if (this.encoding.equals(PdfName.BIGFIVE))
/* 216 */           return new String(b, "Big5"); 
/* 217 */         if (this.encoding.equals(PdfName.UTF_8)) {
/* 218 */           return new String(b, "UTF8");
/*     */         }
/* 220 */       } catch (Exception exception) {}
/*     */       
/* 222 */       return vs.toUnicodeString();
/*     */     } 
/* 224 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFileSpec() {
/* 231 */     return this.fileSpec;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/FdfReader.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */