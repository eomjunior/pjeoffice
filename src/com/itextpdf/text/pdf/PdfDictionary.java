/*     */ package com.itextpdf.text.pdf;
/*     */ 
/*     */ import com.itextpdf.text.error_messages.MessageLocalization;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PdfDictionary
/*     */   extends PdfObject
/*     */ {
/*  82 */   public static final PdfName FONT = PdfName.FONT;
/*     */ 
/*     */   
/*  85 */   public static final PdfName OUTLINES = PdfName.OUTLINES;
/*     */ 
/*     */   
/*  88 */   public static final PdfName PAGE = PdfName.PAGE;
/*     */ 
/*     */   
/*  91 */   public static final PdfName PAGES = PdfName.PAGES;
/*     */ 
/*     */   
/*  94 */   public static final PdfName CATALOG = PdfName.CATALOG;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  99 */   private PdfName dictionaryType = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected LinkedHashMap<PdfName, PdfObject> hashMap;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfDictionary() {
/* 110 */     super(6);
/* 111 */     this.hashMap = new LinkedHashMap<PdfName, PdfObject>();
/*     */   }
/*     */   
/*     */   public PdfDictionary(int capacity) {
/* 115 */     super(6);
/* 116 */     this.hashMap = new LinkedHashMap<PdfName, PdfObject>(capacity);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfDictionary(PdfName type) {
/* 125 */     this();
/* 126 */     this.dictionaryType = type;
/* 127 */     put(PdfName.TYPE, this.dictionaryType);
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
/*     */   public void toPdf(PdfWriter writer, OutputStream os) throws IOException {
/* 142 */     PdfWriter.checkPdfIsoConformance(writer, 11, this);
/* 143 */     os.write(60);
/* 144 */     os.write(60);
/*     */ 
/*     */     
/* 147 */     int type = 0;
/* 148 */     for (Map.Entry<PdfName, PdfObject> e : this.hashMap.entrySet()) {
/* 149 */       ((PdfName)e.getKey()).toPdf(writer, os);
/* 150 */       PdfObject value = e.getValue();
/* 151 */       type = value.type();
/* 152 */       if (type != 5 && type != 6 && type != 4 && type != 3)
/* 153 */         os.write(32); 
/* 154 */       value.toPdf(writer, os);
/*     */     } 
/* 156 */     os.write(62);
/* 157 */     os.write(62);
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
/* 172 */     if (get(PdfName.TYPE) == null)
/* 173 */       return "Dictionary"; 
/* 174 */     return "Dictionary of type: " + get(PdfName.TYPE);
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
/*     */   public void put(PdfName key, PdfObject object) {
/* 192 */     if (key == null)
/* 193 */       throw new IllegalArgumentException(MessageLocalization.getComposedMessage("key.is.null", new Object[0])); 
/* 194 */     if (object == null || object.isNull()) {
/* 195 */       this.hashMap.remove(key);
/*     */     } else {
/* 197 */       this.hashMap.put(key, object);
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
/*     */ 
/*     */   
/*     */   public void putEx(PdfName key, PdfObject value) {
/* 213 */     if (key == null)
/* 214 */       throw new IllegalArgumentException(MessageLocalization.getComposedMessage("key.is.null", new Object[0])); 
/* 215 */     if (value == null)
/*     */       return; 
/* 217 */     put(key, value);
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
/*     */   public void putAll(PdfDictionary dic) {
/* 231 */     this.hashMap.putAll(dic.hashMap);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void remove(PdfName key) {
/* 241 */     this.hashMap.remove(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/* 250 */     this.hashMap.clear();
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
/*     */   public PdfObject get(PdfName key) {
/* 262 */     return this.hashMap.get(key);
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
/*     */   public PdfObject getDirectObject(PdfName key) {
/* 277 */     return PdfReader.getPdfObject(get(key));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<PdfName> getKeys() {
/* 286 */     return this.hashMap.keySet();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/* 297 */     return this.hashMap.size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean contains(PdfName key) {
/* 307 */     return this.hashMap.containsKey(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFont() {
/* 318 */     return checkType(FONT);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isPage() {
/* 327 */     return checkType(PAGE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isPages() {
/* 336 */     return checkType(PAGES);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isCatalog() {
/* 345 */     return checkType(CATALOG);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isOutlineTree() {
/* 354 */     return checkType(OUTLINES);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean checkType(PdfName type) {
/* 363 */     if (type == null)
/* 364 */       return false; 
/* 365 */     if (this.dictionaryType == null)
/* 366 */       this.dictionaryType = getAsName(PdfName.TYPE); 
/* 367 */     return type.equals(this.dictionaryType);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void merge(PdfDictionary other) {
/* 373 */     this.hashMap.putAll(other.hashMap);
/*     */   }
/*     */   
/*     */   public void mergeDifferent(PdfDictionary other) {
/* 377 */     for (PdfName key : other.hashMap.keySet()) {
/* 378 */       if (!this.hashMap.containsKey(key)) {
/* 379 */         this.hashMap.put(key, other.hashMap.get(key));
/*     */       }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfDictionary getAsDict(PdfName key) {
/* 400 */     PdfDictionary dict = null;
/* 401 */     PdfObject orig = getDirectObject(key);
/* 402 */     if (orig != null && orig.isDictionary())
/* 403 */       dict = (PdfDictionary)orig; 
/* 404 */     return dict;
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
/*     */   public PdfArray getAsArray(PdfName key) {
/* 421 */     PdfArray array = null;
/* 422 */     PdfObject orig = getDirectObject(key);
/* 423 */     if (orig != null && orig.isArray())
/* 424 */       array = (PdfArray)orig; 
/* 425 */     return array;
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
/*     */   public PdfStream getAsStream(PdfName key) {
/* 442 */     PdfStream stream = null;
/* 443 */     PdfObject orig = getDirectObject(key);
/* 444 */     if (orig != null && orig.isStream())
/* 445 */       stream = (PdfStream)orig; 
/* 446 */     return stream;
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
/*     */   public PdfString getAsString(PdfName key) {
/* 463 */     PdfString string = null;
/* 464 */     PdfObject orig = getDirectObject(key);
/* 465 */     if (orig != null && orig.isString())
/* 466 */       string = (PdfString)orig; 
/* 467 */     return string;
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
/*     */   public PdfNumber getAsNumber(PdfName key) {
/* 484 */     PdfNumber number = null;
/* 485 */     PdfObject orig = getDirectObject(key);
/* 486 */     if (orig != null && orig.isNumber())
/* 487 */       number = (PdfNumber)orig; 
/* 488 */     return number;
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
/*     */   public PdfName getAsName(PdfName key) {
/* 505 */     PdfName name = null;
/* 506 */     PdfObject orig = getDirectObject(key);
/* 507 */     if (orig != null && orig.isName())
/* 508 */       name = (PdfName)orig; 
/* 509 */     return name;
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
/*     */   public PdfBoolean getAsBoolean(PdfName key) {
/* 526 */     PdfBoolean bool = null;
/* 527 */     PdfObject orig = getDirectObject(key);
/* 528 */     if (orig != null && orig.isBoolean())
/* 529 */       bool = (PdfBoolean)orig; 
/* 530 */     return bool;
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
/*     */   public PdfIndirectReference getAsIndirectObject(PdfName key) {
/* 545 */     PdfIndirectReference ref = null;
/* 546 */     PdfObject orig = get(key);
/* 547 */     if (orig != null && orig.isIndirect())
/* 548 */       ref = (PdfIndirectReference)orig; 
/* 549 */     return ref;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/PdfDictionary.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */