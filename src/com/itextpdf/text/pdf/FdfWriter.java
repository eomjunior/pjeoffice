/*     */ package com.itextpdf.text.pdf;
/*     */ 
/*     */ import com.itextpdf.text.DocWriter;
/*     */ import com.itextpdf.text.ExceptionConverter;
/*     */ import com.itextpdf.text.Image;
/*     */ import com.itextpdf.text.Rectangle;
/*     */ import com.itextpdf.text.log.Counter;
/*     */ import com.itextpdf.text.log.CounterFactory;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.StringTokenizer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FdfWriter
/*     */ {
/*  64 */   private static final byte[] HEADER_FDF = DocWriter.getISOBytes("%FDF-1.4\n%âãÏÓ\n");
/*  65 */   HashMap<String, Object> fields = new HashMap<String, Object>();
/*  66 */   Wrt wrt = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String file;
/*     */ 
/*     */ 
/*     */   
/*     */   private String statusMessage;
/*     */ 
/*     */ 
/*     */   
/*     */   protected Counter COUNTER;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void writeTo(OutputStream os) throws IOException {
/*  85 */     if (this.wrt == null)
/*  86 */       this.wrt = new Wrt(os, this); 
/*  87 */     this.wrt.write();
/*     */   }
/*     */   
/*     */   public void write() throws IOException {
/*  91 */     this.wrt.write();
/*     */   }
/*     */   
/*     */   public String getStatusMessage() {
/*  95 */     return this.statusMessage;
/*     */   }
/*     */   
/*     */   public void setStatusMessage(String statusMessage) {
/*  99 */     this.statusMessage = statusMessage;
/*     */   }
/*     */   boolean setField(String field, PdfObject value) {
/*     */     String s;
/*     */     Object<Object, Object> obj;
/* 104 */     HashMap<String, Object> map = this.fields;
/* 105 */     StringTokenizer tk = new StringTokenizer(field, ".");
/* 106 */     if (!tk.hasMoreTokens())
/* 107 */       return false; 
/*     */     while (true) {
/* 109 */       s = tk.nextToken();
/* 110 */       obj = (Object<Object, Object>)map.get(s);
/* 111 */       if (tk.hasMoreTokens()) {
/* 112 */         if (obj == null) {
/* 113 */           obj = (Object<Object, Object>)new HashMap<Object, Object>();
/* 114 */           map.put(s, obj);
/* 115 */           map = (HashMap)obj;
/*     */           continue;
/*     */         } 
/* 118 */         if (obj instanceof HashMap) {
/* 119 */           map = (HashMap)obj; continue;
/*     */         } 
/* 121 */         return false;
/*     */       }  break;
/*     */     } 
/* 124 */     if (!(obj instanceof HashMap)) {
/* 125 */       map.put(s, value);
/* 126 */       return true;
/*     */     } 
/*     */     
/* 129 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void iterateFields(HashMap<String, Object> values, HashMap<String, Object> map, String name) {
/* 136 */     for (Map.Entry<String, Object> entry : map.entrySet()) {
/* 137 */       String s = entry.getKey();
/* 138 */       Object obj = entry.getValue();
/* 139 */       if (obj instanceof HashMap) {
/* 140 */         iterateFields(values, (HashMap<String, Object>)obj, name + "." + s); continue;
/*     */       } 
/* 142 */       values.put((name + "." + s).substring(1), obj);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean removeField(String field) {
/*     */     Object obj;
/* 153 */     HashMap<String, Object> map = this.fields;
/* 154 */     StringTokenizer tk = new StringTokenizer(field, ".");
/* 155 */     if (!tk.hasMoreTokens())
/* 156 */       return false; 
/* 157 */     ArrayList<Object> hist = new ArrayList();
/*     */     while (true) {
/* 159 */       String s = tk.nextToken();
/* 160 */       obj = map.get(s);
/* 161 */       if (obj == null)
/* 162 */         return false; 
/* 163 */       hist.add(map);
/* 164 */       hist.add(s);
/* 165 */       if (tk.hasMoreTokens()) {
/* 166 */         if (obj instanceof HashMap) {
/* 167 */           map = (HashMap<String, Object>)obj; continue;
/*     */         } 
/* 169 */         return false;
/*     */       }  break;
/*     */     } 
/* 172 */     if (obj instanceof HashMap) {
/* 173 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 178 */     for (int k = hist.size() - 2; k >= 0; k -= 2) {
/* 179 */       map = (HashMap<String, Object>)hist.get(k);
/* 180 */       String s = (String)hist.get(k + 1);
/* 181 */       map.remove(s);
/* 182 */       if (!map.isEmpty())
/*     */         break; 
/*     */     } 
/* 185 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HashMap<String, Object> getFields() {
/* 193 */     HashMap<String, Object> values = new HashMap<String, Object>();
/* 194 */     iterateFields(values, this.fields, "");
/* 195 */     return values;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getField(String field) {
/*     */     Object obj;
/* 204 */     HashMap<String, Object> map = this.fields;
/* 205 */     StringTokenizer tk = new StringTokenizer(field, ".");
/* 206 */     if (!tk.hasMoreTokens())
/* 207 */       return null; 
/*     */     while (true) {
/* 209 */       String s = tk.nextToken();
/* 210 */       obj = map.get(s);
/* 211 */       if (obj == null)
/* 212 */         return null; 
/* 213 */       if (tk.hasMoreTokens()) {
/* 214 */         if (obj instanceof HashMap) {
/* 215 */           map = (HashMap<String, Object>)obj; continue;
/*     */         } 
/* 217 */         return null;
/*     */       }  break;
/*     */     } 
/* 220 */     if (obj instanceof HashMap) {
/* 221 */       return null;
/*     */     }
/* 223 */     if (((PdfObject)obj).isString()) {
/* 224 */       return ((PdfString)obj).toUnicodeString();
/*     */     }
/* 226 */     return PdfName.decodeName(obj.toString());
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
/*     */   public boolean setFieldAsName(String field, String value) {
/* 240 */     return setField(field, new PdfName(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean setFieldAsString(String field, String value) {
/* 251 */     return setField(field, new PdfString(value, "UnicodeBig"));
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
/*     */   public boolean setFieldAsAction(String field, PdfAction action) {
/* 267 */     return setField(field, action);
/*     */   }
/*     */   
/*     */   public boolean setFieldAsTemplate(String field, PdfTemplate template) {
/*     */     try {
/* 272 */       PdfDictionary d = new PdfDictionary();
/* 273 */       if (template instanceof PdfImportedPage) {
/* 274 */         d.put(PdfName.N, template.getIndirectReference());
/*     */       } else {
/* 276 */         PdfStream str = template.getFormXObject(0);
/* 277 */         PdfIndirectReference ref = this.wrt.addToBody(str).getIndirectReference();
/* 278 */         d.put(PdfName.N, ref);
/*     */       } 
/* 280 */       return setField(field, d);
/* 281 */     } catch (Exception e) {
/* 282 */       throw new ExceptionConverter(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean setFieldAsImage(String field, Image image) {
/*     */     try {
/* 288 */       if (Float.isNaN(image.getAbsoluteX()))
/* 289 */         image.setAbsolutePosition(0.0F, image.getAbsoluteY()); 
/* 290 */       if (Float.isNaN(image.getAbsoluteY()))
/* 291 */         image.setAbsolutePosition(image.getAbsoluteY(), 0.0F); 
/* 292 */       PdfTemplate tmpl = PdfTemplate.createTemplate(this.wrt, image.getWidth(), image.getHeight());
/* 293 */       tmpl.addImage(image);
/* 294 */       PdfStream str = tmpl.getFormXObject(0);
/* 295 */       PdfIndirectReference ref = this.wrt.addToBody(str).getIndirectReference();
/* 296 */       PdfDictionary d = new PdfDictionary();
/* 297 */       d.put(PdfName.N, ref);
/* 298 */       return setField(field, d);
/* 299 */     } catch (Exception de) {
/* 300 */       throw new ExceptionConverter(de);
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean setFieldAsJavascript(String field, PdfName jsTrigName, String js) {
/* 305 */     PdfAnnotation dict = this.wrt.createAnnotation((Rectangle)null, (PdfName)null);
/* 306 */     PdfAction javascript = PdfAction.javaScript(js, this.wrt);
/* 307 */     dict.put(jsTrigName, javascript);
/* 308 */     return setField(field, dict);
/*     */   }
/*     */   
/*     */   public PdfImportedPage getImportedPage(PdfReader reader, int pageNumber) {
/* 312 */     return this.wrt.getImportedPage(reader, pageNumber);
/*     */   }
/*     */   
/*     */   public PdfTemplate createTemplate(float width, float height) {
/* 316 */     return PdfTemplate.createTemplate(this.wrt, width, height);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFields(FdfReader fdf) {
/* 323 */     HashMap<String, PdfDictionary> map = fdf.getFields();
/* 324 */     for (Map.Entry<String, PdfDictionary> entry : map.entrySet()) {
/* 325 */       String key = entry.getKey();
/* 326 */       PdfDictionary dic = entry.getValue();
/* 327 */       PdfObject v = dic.get(PdfName.V);
/* 328 */       if (v != null) {
/* 329 */         setField(key, v);
/*     */       }
/* 331 */       v = dic.get(PdfName.A);
/* 332 */       if (v != null) {
/* 333 */         setField(key, v);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFields(PdfReader pdf) {
/* 342 */     setFields(pdf.getAcroFields());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFields(AcroFields af) {
/* 349 */     for (Map.Entry<String, AcroFields.Item> entry : af.getFields().entrySet()) {
/* 350 */       String fn = entry.getKey();
/* 351 */       AcroFields.Item item = entry.getValue();
/* 352 */       PdfDictionary dic = item.getMerged(0);
/* 353 */       PdfObject v = PdfReader.getPdfObjectRelease(dic.get(PdfName.V));
/* 354 */       if (v == null)
/*     */         continue; 
/* 356 */       PdfObject ft = PdfReader.getPdfObjectRelease(dic.get(PdfName.FT));
/* 357 */       if (ft == null || PdfName.SIG.equals(ft))
/*     */         continue; 
/* 359 */       setField(fn, v);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFile() {
/* 367 */     return this.file;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFile(String file) {
/* 375 */     this.file = file;
/*     */   }
/*     */   
/*     */   static class Wrt extends PdfWriter {
/*     */     private FdfWriter fdf;
/*     */     
/*     */     Wrt(OutputStream os, FdfWriter fdf) throws IOException {
/* 382 */       super(new PdfDocument(), os);
/* 383 */       this.fdf = fdf;
/* 384 */       this.os.write(FdfWriter.HEADER_FDF);
/* 385 */       this.body = new PdfWriter.PdfBody(this);
/*     */     }
/*     */     
/*     */     void write() throws IOException {
/* 389 */       for (PdfReaderInstance element : this.readerInstances.values()) {
/* 390 */         this.currentPdfReaderInstance = element;
/* 391 */         this.currentPdfReaderInstance.writeAllPages();
/*     */       } 
/*     */       
/* 394 */       PdfDictionary dic = new PdfDictionary();
/* 395 */       dic.put(PdfName.FIELDS, calculate(this.fdf.fields));
/* 396 */       if (this.fdf.file != null)
/* 397 */         dic.put(PdfName.F, new PdfString(this.fdf.file, "UnicodeBig")); 
/* 398 */       if (this.fdf.statusMessage != null && this.fdf.statusMessage.trim().length() != 0)
/* 399 */         dic.put(PdfName.STATUS, new PdfString(this.fdf.statusMessage)); 
/* 400 */       PdfDictionary fd = new PdfDictionary();
/* 401 */       fd.put(PdfName.FDF, dic);
/* 402 */       PdfIndirectReference ref = addToBody(fd).getIndirectReference();
/* 403 */       this.os.write(getISOBytes("trailer\n"));
/* 404 */       PdfDictionary trailer = new PdfDictionary();
/* 405 */       trailer.put(PdfName.ROOT, ref);
/* 406 */       trailer.toPdf(null, this.os);
/* 407 */       this.os.write(getISOBytes("\n%%EOF\n"));
/* 408 */       this.os.close();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     PdfArray calculate(HashMap<String, Object> map) throws IOException {
/* 414 */       PdfArray ar = new PdfArray();
/* 415 */       for (Map.Entry<String, Object> entry : map.entrySet()) {
/* 416 */         String key = entry.getKey();
/* 417 */         Object v = entry.getValue();
/* 418 */         PdfDictionary dic = new PdfDictionary();
/* 419 */         dic.put(PdfName.T, new PdfString(key, "UnicodeBig"));
/* 420 */         if (v instanceof HashMap) {
/* 421 */           dic.put(PdfName.KIDS, calculate((HashMap<String, Object>)v));
/* 422 */         } else if (v instanceof PdfAction) {
/* 423 */           dic.put(PdfName.A, (PdfAction)v);
/* 424 */         } else if (v instanceof PdfAnnotation) {
/* 425 */           dic.put(PdfName.AA, (PdfAnnotation)v);
/* 426 */         } else if (v instanceof PdfDictionary && ((PdfDictionary)v).size() == 1 && ((PdfDictionary)v).contains(PdfName.N)) {
/* 427 */           dic.put(PdfName.AP, (PdfDictionary)v);
/*     */         } else {
/* 429 */           dic.put(PdfName.V, (PdfObject)v);
/*     */         } 
/* 431 */         ar.add(dic);
/*     */       } 
/* 433 */       return ar;
/*     */     }
/*     */   }
/*     */   
/* 437 */   public FdfWriter() { this.COUNTER = CounterFactory.getCounter(FdfWriter.class); } public FdfWriter(OutputStream os) throws IOException { this.COUNTER = CounterFactory.getCounter(FdfWriter.class);
/*     */     this.wrt = new Wrt(os, this); } protected Counter getCounter() {
/* 439 */     return this.COUNTER;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/FdfWriter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */