/*     */ package com.itextpdf.text.pdf;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PdfArray
/*     */   extends PdfObject
/*     */   implements Iterable<PdfObject>
/*     */ {
/*     */   protected ArrayList<PdfObject> arrayList;
/*     */   
/*     */   public PdfArray() {
/*  80 */     super(5);
/*  81 */     this.arrayList = new ArrayList<PdfObject>();
/*     */   }
/*     */   
/*     */   public PdfArray(int capacity) {
/*  85 */     super(5);
/*  86 */     this.arrayList = new ArrayList<PdfObject>(capacity);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfArray(PdfObject object) {
/*  96 */     super(5);
/*  97 */     this.arrayList = new ArrayList<PdfObject>();
/*  98 */     this.arrayList.add(object);
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
/*     */   public PdfArray(float[] values) {
/* 111 */     super(5);
/* 112 */     this.arrayList = new ArrayList<PdfObject>();
/* 113 */     add(values);
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
/*     */   public PdfArray(int[] values) {
/* 126 */     super(5);
/* 127 */     this.arrayList = new ArrayList<PdfObject>();
/* 128 */     add(values);
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
/*     */   public PdfArray(List<PdfObject> l) {
/* 142 */     this();
/* 143 */     for (PdfObject element : l) {
/* 144 */       add(element);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfArray(PdfArray array) {
/* 154 */     super(5);
/* 155 */     this.arrayList = new ArrayList<PdfObject>(array.arrayList);
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
/*     */   public void toPdf(PdfWriter writer, OutputStream os) throws IOException {
/* 169 */     PdfWriter.checkPdfIsoConformance(writer, 11, this);
/* 170 */     os.write(91);
/*     */     
/* 172 */     Iterator<PdfObject> i = this.arrayList.iterator();
/*     */     
/* 174 */     int type = 0;
/* 175 */     if (i.hasNext()) {
/* 176 */       PdfObject object = i.next();
/* 177 */       if (object == null)
/* 178 */         object = PdfNull.PDFNULL; 
/* 179 */       object.toPdf(writer, os);
/*     */     } 
/* 181 */     while (i.hasNext()) {
/* 182 */       PdfObject object = i.next();
/* 183 */       if (object == null)
/* 184 */         object = PdfNull.PDFNULL; 
/* 185 */       type = object.type();
/* 186 */       if (type != 5 && type != 6 && type != 4 && type != 3)
/* 187 */         os.write(32); 
/* 188 */       object.toPdf(writer, os);
/*     */     } 
/* 190 */     os.write(93);
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
/*     */   public String toString() {
/* 205 */     return this.arrayList.toString();
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
/*     */   public PdfObject set(int idx, PdfObject obj) {
/* 221 */     return this.arrayList.set(idx, obj);
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
/*     */   public PdfObject remove(int idx) {
/* 235 */     return this.arrayList.remove(idx);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public ArrayList<PdfObject> getArrayList() {
/* 246 */     return this.arrayList;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/* 255 */     return this.arrayList.size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 265 */     return this.arrayList.isEmpty();
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
/*     */   public boolean add(PdfObject object) {
/* 277 */     return this.arrayList.add(object);
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
/*     */   public boolean add(float[] values) {
/* 292 */     for (int k = 0; k < values.length; k++)
/* 293 */       this.arrayList.add(new PdfNumber(values[k])); 
/* 294 */     return true;
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
/*     */   public boolean add(int[] values) {
/* 308 */     for (int k = 0; k < values.length; k++)
/* 309 */       this.arrayList.add(new PdfNumber(values[k])); 
/* 310 */     return true;
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
/*     */   public void add(int index, PdfObject element) {
/* 326 */     this.arrayList.add(index, element);
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
/*     */   public void addFirst(PdfObject object) {
/* 339 */     this.arrayList.add(0, object);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean contains(PdfObject object) {
/* 350 */     return this.arrayList.contains(object);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ListIterator<PdfObject> listIterator() {
/* 359 */     return this.arrayList.listIterator();
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
/*     */   public PdfObject getPdfObject(int idx) {
/* 374 */     return this.arrayList.get(idx);
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
/*     */   public PdfObject getDirectObject(int idx) {
/* 388 */     return PdfReader.getPdfObject(getPdfObject(idx));
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
/*     */   public PdfDictionary getAsDict(int idx) {
/* 408 */     PdfDictionary dict = null;
/* 409 */     PdfObject orig = getDirectObject(idx);
/* 410 */     if (orig != null && orig.isDictionary())
/* 411 */       dict = (PdfDictionary)orig; 
/* 412 */     return dict;
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
/*     */   public PdfArray getAsArray(int idx) {
/* 429 */     PdfArray array = null;
/* 430 */     PdfObject orig = getDirectObject(idx);
/* 431 */     if (orig != null && orig.isArray())
/* 432 */       array = (PdfArray)orig; 
/* 433 */     return array;
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
/*     */   public PdfStream getAsStream(int idx) {
/* 450 */     PdfStream stream = null;
/* 451 */     PdfObject orig = getDirectObject(idx);
/* 452 */     if (orig != null && orig.isStream())
/* 453 */       stream = (PdfStream)orig; 
/* 454 */     return stream;
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
/*     */   public PdfString getAsString(int idx) {
/* 471 */     PdfString string = null;
/* 472 */     PdfObject orig = getDirectObject(idx);
/* 473 */     if (orig != null && orig.isString())
/* 474 */       string = (PdfString)orig; 
/* 475 */     return string;
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
/*     */   public PdfNumber getAsNumber(int idx) {
/* 492 */     PdfNumber number = null;
/* 493 */     PdfObject orig = getDirectObject(idx);
/* 494 */     if (orig != null && orig.isNumber())
/* 495 */       number = (PdfNumber)orig; 
/* 496 */     return number;
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
/*     */   public PdfName getAsName(int idx) {
/* 513 */     PdfName name = null;
/* 514 */     PdfObject orig = getDirectObject(idx);
/* 515 */     if (orig != null && orig.isName())
/* 516 */       name = (PdfName)orig; 
/* 517 */     return name;
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
/*     */   public PdfBoolean getAsBoolean(int idx) {
/* 534 */     PdfBoolean bool = null;
/* 535 */     PdfObject orig = getDirectObject(idx);
/* 536 */     if (orig != null && orig.isBoolean())
/* 537 */       bool = (PdfBoolean)orig; 
/* 538 */     return bool;
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
/*     */   public PdfIndirectReference getAsIndirectObject(int idx) {
/* 553 */     PdfIndirectReference ref = null;
/* 554 */     PdfObject orig = getPdfObject(idx);
/* 555 */     if (orig instanceof PdfIndirectReference)
/* 556 */       ref = (PdfIndirectReference)orig; 
/* 557 */     return ref;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Iterator<PdfObject> iterator() {
/* 564 */     return this.arrayList.iterator();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long[] asLongArray() {
/* 573 */     long[] rslt = new long[size()];
/* 574 */     for (int k = 0; k < rslt.length; k++) {
/* 575 */       rslt[k] = getAsNumber(k).longValue();
/*     */     }
/* 577 */     return rslt;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double[] asDoubleArray() {
/* 586 */     double[] rslt = new double[size()];
/* 587 */     for (int k = 0; k < rslt.length; k++) {
/* 588 */       rslt[k] = getAsNumber(k).doubleValue();
/*     */     }
/* 590 */     return rslt;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/PdfArray.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */