/*     */ package com.itextpdf.text.pdf;
/*     */ 
/*     */ import com.itextpdf.text.DocListener;
/*     */ import com.itextpdf.text.Document;
/*     */ import com.itextpdf.text.DocumentException;
/*     */ import com.itextpdf.text.ExceptionConverter;
/*     */ import com.itextpdf.text.error_messages.MessageLocalization;
/*     */ import com.itextpdf.text.exceptions.BadPasswordException;
/*     */ import com.itextpdf.text.log.Counter;
/*     */ import com.itextpdf.text.log.CounterFactory;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
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
/*     */ @Deprecated
/*     */ class PdfCopyFieldsImp
/*     */   extends PdfWriter
/*     */ {
/*  66 */   private static final PdfName iTextTag = new PdfName("_iTextTag_");
/*  67 */   private static final Integer zero = Integer.valueOf(0);
/*  68 */   ArrayList<PdfReader> readers = new ArrayList<PdfReader>();
/*  69 */   HashMap<PdfReader, IntHashtable> readers2intrefs = new HashMap<PdfReader, IntHashtable>();
/*  70 */   HashMap<PdfReader, IntHashtable> pages2intrefs = new HashMap<PdfReader, IntHashtable>();
/*  71 */   HashMap<PdfReader, IntHashtable> visited = new HashMap<PdfReader, IntHashtable>();
/*  72 */   ArrayList<AcroFields> fields = new ArrayList<AcroFields>();
/*     */   RandomAccessFileOrArray file;
/*  74 */   HashMap<String, Object> fieldTree = new HashMap<String, Object>();
/*  75 */   ArrayList<PdfIndirectReference> pageRefs = new ArrayList<PdfIndirectReference>();
/*  76 */   ArrayList<PdfDictionary> pageDics = new ArrayList<PdfDictionary>();
/*  77 */   PdfDictionary resources = new PdfDictionary();
/*     */   PdfDictionary form;
/*     */   boolean closing = false;
/*     */   Document nd;
/*     */   private HashMap<PdfArray, ArrayList<Integer>> tabOrder;
/*  82 */   private ArrayList<String> calculationOrder = new ArrayList<String>();
/*     */   private ArrayList<Object> calculationOrderRefs;
/*     */   private boolean hasSignature;
/*     */   private boolean needAppearances = false;
/*  86 */   private HashSet<Object> mergedRadioButtons = new HashSet();
/*     */ 
/*     */ 
/*     */   
/*  90 */   protected Counter COUNTER = CounterFactory.getCounter(PdfCopyFields.class);
/*     */   protected Counter getCounter() {
/*  92 */     return this.COUNTER;
/*     */   }
/*     */   
/*     */   PdfCopyFieldsImp(OutputStream os) throws DocumentException {
/*  96 */     this(os, false);
/*     */   }
/*     */   
/*     */   PdfCopyFieldsImp(OutputStream os, char pdfVersion) throws DocumentException {
/* 100 */     super(new PdfDocument(), os);
/* 101 */     this.pdf.addWriter(this);
/* 102 */     if (pdfVersion != '\000')
/* 103 */       setPdfVersion(pdfVersion); 
/* 104 */     this.nd = new Document();
/* 105 */     this.nd.addDocListener((DocListener)this.pdf);
/*     */   }
/*     */   
/*     */   void addDocument(PdfReader reader, List<Integer> pagesToKeep) throws DocumentException, IOException {
/* 109 */     if (!this.readers2intrefs.containsKey(reader) && reader.isTampered())
/* 110 */       throw new DocumentException(MessageLocalization.getComposedMessage("the.document.was.reused", new Object[0])); 
/* 111 */     reader = new PdfReader(reader);
/* 112 */     reader.selectPages(pagesToKeep);
/* 113 */     if (reader.getNumberOfPages() == 0)
/*     */       return; 
/* 115 */     reader.setTampered(false);
/* 116 */     addDocument(reader);
/*     */   }
/*     */   
/*     */   void addDocument(PdfReader reader) throws DocumentException, IOException {
/* 120 */     if (!reader.isOpenedWithFullPermissions())
/* 121 */       throw new BadPasswordException(MessageLocalization.getComposedMessage("pdfreader.not.opened.with.owner.password", new Object[0])); 
/* 122 */     openDoc();
/* 123 */     if (this.readers2intrefs.containsKey(reader)) {
/* 124 */       reader = new PdfReader(reader);
/*     */     } else {
/*     */       
/* 127 */       if (reader.isTampered())
/* 128 */         throw new DocumentException(MessageLocalization.getComposedMessage("the.document.was.reused", new Object[0])); 
/* 129 */       reader.consolidateNamedDestinations();
/* 130 */       reader.setTampered(true);
/*     */     } 
/* 132 */     reader.shuffleSubsetNames();
/* 133 */     this.readers2intrefs.put(reader, new IntHashtable());
/* 134 */     this.readers.add(reader);
/* 135 */     int len = reader.getNumberOfPages();
/* 136 */     IntHashtable refs = new IntHashtable();
/* 137 */     for (int p = 1; p <= len; p++) {
/* 138 */       refs.put(reader.getPageOrigRef(p).getNumber(), 1);
/* 139 */       reader.releasePage(p);
/*     */     } 
/* 141 */     this.pages2intrefs.put(reader, refs);
/* 142 */     this.visited.put(reader, new IntHashtable());
/* 143 */     AcroFields acro = reader.getAcroFields();
/*     */ 
/*     */     
/* 146 */     boolean needapp = !acro.isGenerateAppearances();
/* 147 */     if (needapp)
/* 148 */       this.needAppearances = true; 
/* 149 */     this.fields.add(acro);
/* 150 */     updateCalculationOrder(reader);
/*     */   }
/*     */   
/*     */   private static String getCOName(PdfReader reader, PRIndirectReference ref) {
/* 154 */     String name = "";
/* 155 */     while (ref != null) {
/* 156 */       PdfObject obj = PdfReader.getPdfObject(ref);
/* 157 */       if (obj == null || obj.type() != 6)
/*     */         break; 
/* 159 */       PdfDictionary dic = (PdfDictionary)obj;
/* 160 */       PdfString t = dic.getAsString(PdfName.T);
/* 161 */       if (t != null) {
/* 162 */         name = t.toUnicodeString() + "." + name;
/*     */       }
/* 164 */       ref = (PRIndirectReference)dic.get(PdfName.PARENT);
/*     */     } 
/* 166 */     if (name.endsWith("."))
/* 167 */       name = name.substring(0, name.length() - 1); 
/* 168 */     return name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void updateCalculationOrder(PdfReader reader) {
/* 175 */     PdfDictionary catalog = reader.getCatalog();
/* 176 */     PdfDictionary acro = catalog.getAsDict(PdfName.ACROFORM);
/* 177 */     if (acro == null)
/*     */       return; 
/* 179 */     PdfArray co = acro.getAsArray(PdfName.CO);
/* 180 */     if (co == null || co.size() == 0)
/*     */       return; 
/* 182 */     AcroFields af = reader.getAcroFields();
/* 183 */     for (int k = 0; k < co.size(); k++) {
/* 184 */       PdfObject obj = co.getPdfObject(k);
/* 185 */       if (obj != null && obj.isIndirect()) {
/*     */         
/* 187 */         String name = getCOName(reader, (PRIndirectReference)obj);
/* 188 */         if (af.getFieldItem(name) != null) {
/*     */           
/* 190 */           name = "." + name;
/* 191 */           if (!this.calculationOrder.contains(name))
/*     */           {
/* 193 */             this.calculationOrder.add(name); } 
/*     */         } 
/*     */       } 
/*     */     }  } void propagate(PdfObject obj, PdfIndirectReference refo, boolean restricted) throws IOException { PdfDictionary dic;
/*     */     Iterator<PdfObject> it;
/* 198 */     if (obj == null) {
/*     */       return;
/*     */     }
/*     */     
/* 202 */     if (obj instanceof PdfIndirectReference)
/*     */       return; 
/* 204 */     switch (obj.type()) {
/*     */       case 6:
/*     */       case 7:
/* 207 */         dic = (PdfDictionary)obj;
/* 208 */         for (PdfName key : dic.getKeys()) {
/* 209 */           if (restricted && (key.equals(PdfName.PARENT) || key.equals(PdfName.KIDS)))
/*     */             continue; 
/* 211 */           PdfObject ob = dic.get(key);
/* 212 */           if (ob != null && ob.isIndirect()) {
/* 213 */             PRIndirectReference ind = (PRIndirectReference)ob;
/* 214 */             if (!setVisited(ind) && !isPage(ind)) {
/* 215 */               PdfIndirectReference ref = getNewReference(ind);
/* 216 */               propagate(PdfReader.getPdfObjectRelease(ind), ref, restricted);
/*     */             } 
/*     */             continue;
/*     */           } 
/* 220 */           propagate(ob, (PdfIndirectReference)null, restricted);
/*     */         } 
/*     */         break;
/*     */ 
/*     */       
/*     */       case 5:
/* 226 */         for (it = ((PdfArray)obj).listIterator(); it.hasNext(); ) {
/* 227 */           PdfObject ob = it.next();
/* 228 */           if (ob != null && ob.isIndirect()) {
/* 229 */             PRIndirectReference ind = (PRIndirectReference)ob;
/* 230 */             if (!isVisited(ind) && !isPage(ind)) {
/* 231 */               PdfIndirectReference ref = getNewReference(ind);
/* 232 */               propagate(PdfReader.getPdfObjectRelease(ind), ref, restricted);
/*     */             } 
/*     */             continue;
/*     */           } 
/* 236 */           propagate(ob, (PdfIndirectReference)null, restricted);
/*     */         } 
/*     */         break;
/*     */       
/*     */       case 10:
/* 241 */         throw new RuntimeException(MessageLocalization.getComposedMessage("reference.pointing.to.reference", new Object[0]));
/*     */     }  }
/*     */ 
/*     */ 
/*     */   
/*     */   private void adjustTabOrder(PdfArray annots, PdfIndirectReference ind, PdfNumber nn) {
/* 247 */     int v = nn.intValue();
/* 248 */     ArrayList<Integer> t = this.tabOrder.get(annots);
/* 249 */     if (t == null) {
/* 250 */       t = new ArrayList<Integer>();
/* 251 */       int size = annots.size() - 1;
/* 252 */       for (int k = 0; k < size; k++) {
/* 253 */         t.add(zero);
/*     */       }
/* 255 */       t.add(Integer.valueOf(v));
/* 256 */       this.tabOrder.put(annots, t);
/* 257 */       annots.add(ind);
/*     */     } else {
/*     */       
/* 260 */       int size = t.size() - 1;
/* 261 */       for (int k = size; k >= 0; k--) {
/* 262 */         if (((Integer)t.get(k)).intValue() <= v) {
/* 263 */           t.add(k + 1, Integer.valueOf(v));
/* 264 */           annots.add(k + 1, ind);
/* 265 */           size = -2;
/*     */           break;
/*     */         } 
/*     */       } 
/* 269 */       if (size != -2) {
/* 270 */         t.add(0, Integer.valueOf(v));
/* 271 */         annots.add(0, ind);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected PdfArray branchForm(HashMap<String, Object> level, PdfIndirectReference parent, String fname) throws IOException {
/* 278 */     PdfArray arr = new PdfArray();
/* 279 */     for (Map.Entry<String, Object> entry : level.entrySet()) {
/* 280 */       String name = entry.getKey();
/* 281 */       Object obj = entry.getValue();
/* 282 */       PdfIndirectReference ind = getPdfIndirectReference();
/* 283 */       PdfDictionary dic = new PdfDictionary();
/* 284 */       if (parent != null)
/* 285 */         dic.put(PdfName.PARENT, parent); 
/* 286 */       dic.put(PdfName.T, new PdfString(name, "UnicodeBig"));
/* 287 */       String fname2 = fname + "." + name;
/* 288 */       int coidx = this.calculationOrder.indexOf(fname2);
/* 289 */       if (coidx >= 0)
/* 290 */         this.calculationOrderRefs.set(coidx, ind); 
/* 291 */       if (obj instanceof HashMap) {
/* 292 */         dic.put(PdfName.KIDS, branchForm((HashMap<String, Object>)obj, ind, fname2));
/* 293 */         arr.add(ind);
/* 294 */         addToBody(dic, ind);
/*     */         continue;
/*     */       } 
/* 297 */       ArrayList<Object> list = (ArrayList<Object>)obj;
/* 298 */       dic.mergeDifferent((PdfDictionary)list.get(0));
/* 299 */       if (list.size() == 3) {
/* 300 */         dic.mergeDifferent((PdfDictionary)list.get(2));
/* 301 */         int page = ((Integer)list.get(1)).intValue();
/* 302 */         PdfDictionary pageDic = this.pageDics.get(page - 1);
/* 303 */         PdfArray annots = pageDic.getAsArray(PdfName.ANNOTS);
/* 304 */         if (annots == null) {
/* 305 */           annots = new PdfArray();
/* 306 */           pageDic.put(PdfName.ANNOTS, annots);
/*     */         } 
/* 308 */         PdfNumber nn = (PdfNumber)dic.get(iTextTag);
/* 309 */         dic.remove(iTextTag);
/* 310 */         adjustTabOrder(annots, ind, nn);
/*     */       } else {
/*     */         
/* 313 */         PdfDictionary field = (PdfDictionary)list.get(0);
/* 314 */         PdfName v = field.getAsName(PdfName.V);
/* 315 */         PdfArray kids = new PdfArray();
/* 316 */         for (int k = 1; k < list.size(); k += 2) {
/* 317 */           int page = ((Integer)list.get(k)).intValue();
/* 318 */           PdfDictionary pageDic = this.pageDics.get(page - 1);
/* 319 */           PdfArray annots = pageDic.getAsArray(PdfName.ANNOTS);
/* 320 */           if (annots == null) {
/* 321 */             annots = new PdfArray();
/* 322 */             pageDic.put(PdfName.ANNOTS, annots);
/*     */           } 
/* 324 */           PdfDictionary widget = new PdfDictionary();
/* 325 */           widget.merge((PdfDictionary)list.get(k + 1));
/* 326 */           widget.put(PdfName.PARENT, ind);
/* 327 */           PdfNumber nn = (PdfNumber)widget.get(iTextTag);
/* 328 */           widget.remove(iTextTag);
/* 329 */           if (PdfCopy.isCheckButton(field)) {
/* 330 */             PdfName as = widget.getAsName(PdfName.AS);
/* 331 */             if (v != null && as != null)
/* 332 */               widget.put(PdfName.AS, v); 
/* 333 */           } else if (PdfCopy.isRadioButton(field)) {
/* 334 */             PdfName as = widget.getAsName(PdfName.AS);
/* 335 */             if (v != null && as != null && !as.equals(getOffStateName(widget))) {
/* 336 */               if (!this.mergedRadioButtons.contains(list)) {
/* 337 */                 this.mergedRadioButtons.add(list);
/* 338 */                 widget.put(PdfName.AS, v);
/*     */               } else {
/* 340 */                 widget.put(PdfName.AS, getOffStateName(widget));
/*     */               } 
/*     */             }
/*     */           } 
/* 344 */           PdfIndirectReference wref = addToBody(widget).getIndirectReference();
/* 345 */           adjustTabOrder(annots, wref, nn);
/* 346 */           kids.add(wref);
/* 347 */           propagate(widget, (PdfIndirectReference)null, false);
/*     */         } 
/* 349 */         dic.put(PdfName.KIDS, kids);
/*     */       } 
/* 351 */       arr.add(ind);
/* 352 */       addToBody(dic, ind);
/* 353 */       propagate(dic, (PdfIndirectReference)null, false);
/*     */     } 
/*     */     
/* 356 */     return arr;
/*     */   }
/*     */   
/*     */   protected PdfName getOffStateName(PdfDictionary widget) {
/* 360 */     return PdfName.Off;
/*     */   }
/*     */   
/*     */   protected void createAcroForms() throws IOException {
/* 364 */     if (this.fieldTree.isEmpty())
/*     */       return; 
/* 366 */     this.form = new PdfDictionary();
/* 367 */     this.form.put(PdfName.DR, this.resources);
/* 368 */     propagate(this.resources, (PdfIndirectReference)null, false);
/* 369 */     if (this.needAppearances) {
/* 370 */       this.form.put(PdfName.NEEDAPPEARANCES, PdfBoolean.PDFTRUE);
/*     */     }
/* 372 */     this.form.put(PdfName.DA, new PdfString("/Helv 0 Tf 0 g "));
/* 373 */     this.tabOrder = new HashMap<PdfArray, ArrayList<Integer>>();
/* 374 */     this.calculationOrderRefs = new ArrayList(this.calculationOrder);
/* 375 */     this.form.put(PdfName.FIELDS, branchForm(this.fieldTree, (PdfIndirectReference)null, ""));
/* 376 */     if (this.hasSignature)
/* 377 */       this.form.put(PdfName.SIGFLAGS, new PdfNumber(3)); 
/* 378 */     PdfArray co = new PdfArray();
/* 379 */     for (int k = 0; k < this.calculationOrderRefs.size(); k++) {
/* 380 */       Object obj = this.calculationOrderRefs.get(k);
/* 381 */       if (obj instanceof PdfIndirectReference)
/* 382 */         co.add((PdfIndirectReference)obj); 
/*     */     } 
/* 384 */     if (co.size() > 0) {
/* 385 */       this.form.put(PdfName.CO, co);
/*     */     }
/*     */   }
/*     */   
/*     */   public void close() {
/* 390 */     if (this.closing) {
/* 391 */       super.close();
/*     */       return;
/*     */     } 
/* 394 */     this.closing = true;
/*     */     try {
/* 396 */       closeIt();
/*     */     }
/* 398 */     catch (Exception e) {
/* 399 */       throw new ExceptionConverter(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void closeIt() throws IOException {
/* 407 */     for (int k = 0; k < this.readers.size(); k++)
/* 408 */       ((PdfReader)this.readers.get(k)).removeFields(); 
/*     */     int r;
/* 410 */     for (r = 0; r < this.readers.size(); r++) {
/* 411 */       PdfReader reader = this.readers.get(r);
/* 412 */       for (int page = 1; page <= reader.getNumberOfPages(); page++) {
/* 413 */         this.pageRefs.add(getNewReference(reader.getPageOrigRef(page)));
/* 414 */         this.pageDics.add(reader.getPageN(page));
/*     */       } 
/*     */     } 
/* 417 */     mergeFields();
/* 418 */     createAcroForms();
/* 419 */     for (r = 0; r < this.readers.size(); r++) {
/* 420 */       PdfReader reader = this.readers.get(r);
/* 421 */       for (int page = 1; page <= reader.getNumberOfPages(); page++) {
/* 422 */         PdfDictionary dic = reader.getPageN(page);
/* 423 */         PdfIndirectReference pageRef = getNewReference(reader.getPageOrigRef(page));
/* 424 */         PdfIndirectReference parent = this.root.addPageRef(pageRef);
/* 425 */         dic.put(PdfName.PARENT, parent);
/* 426 */         propagate(dic, pageRef, false);
/*     */       } 
/*     */     } 
/* 429 */     for (Map.Entry<PdfReader, IntHashtable> entry : this.readers2intrefs.entrySet()) {
/* 430 */       PdfReader reader = entry.getKey();
/*     */       try {
/* 432 */         this.file = reader.getSafeFile();
/* 433 */         this.file.reOpen();
/* 434 */         IntHashtable t = entry.getValue();
/* 435 */         int[] keys = t.toOrderedKeys();
/* 436 */         for (int i = 0; i < keys.length; i++) {
/* 437 */           PRIndirectReference ref = new PRIndirectReference(reader, keys[i]);
/* 438 */           addToBody(PdfReader.getPdfObjectRelease(ref), t.get(keys[i]));
/*     */         } 
/*     */       } finally {
/*     */         
/*     */         try {
/* 443 */           this.file.close();
/*     */ 
/*     */         
/*     */         }
/* 447 */         catch (Exception exception) {}
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 452 */     this.pdf.close();
/*     */   }
/*     */   
/*     */   void addPageOffsetToField(Map<String, AcroFields.Item> fd, int pageOffset) {
/* 456 */     if (pageOffset == 0)
/*     */       return; 
/* 458 */     for (AcroFields.Item item : fd.values()) {
/* 459 */       for (int k = 0; k < item.size(); k++) {
/* 460 */         int p = item.getPage(k).intValue();
/* 461 */         item.forcePage(k, p + pageOffset);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   void createWidgets(ArrayList<Object> list, AcroFields.Item item) {
/* 467 */     for (int k = 0; k < item.size(); k++) {
/* 468 */       list.add(item.getPage(k));
/* 469 */       PdfDictionary merged = item.getMerged(k);
/* 470 */       PdfObject dr = merged.get(PdfName.DR);
/* 471 */       if (dr != null)
/* 472 */         PdfFormField.mergeResources(this.resources, (PdfDictionary)PdfReader.getPdfObject(dr)); 
/* 473 */       PdfDictionary widget = new PdfDictionary();
/* 474 */       for (PdfName element : merged.getKeys()) {
/* 475 */         PdfName key = element;
/* 476 */         if (widgetKeys.containsKey(key))
/* 477 */           widget.put(key, merged.get(key)); 
/*     */       } 
/* 479 */       widget.put(iTextTag, new PdfNumber(item.getTabOrder(k).intValue() + 1));
/* 480 */       list.add(widget);
/*     */     } 
/*     */   }
/*     */   void mergeField(String name, AcroFields.Item item) {
/*     */     String s;
/*     */     Object<Object, Object> obj;
/* 486 */     HashMap<String, Object> map = this.fieldTree;
/* 487 */     StringTokenizer tk = new StringTokenizer(name, ".");
/* 488 */     if (!tk.hasMoreTokens())
/*     */       return; 
/*     */     while (true) {
/* 491 */       s = tk.nextToken();
/* 492 */       obj = (Object<Object, Object>)map.get(s);
/* 493 */       if (tk.hasMoreTokens()) {
/* 494 */         if (obj == null) {
/* 495 */           obj = (Object<Object, Object>)new HashMap<Object, Object>();
/* 496 */           map.put(s, obj);
/* 497 */           map = (HashMap)obj;
/*     */           continue;
/*     */         } 
/* 500 */         if (obj instanceof HashMap) {
/* 501 */           map = (HashMap)obj; continue;
/*     */         }  return;
/*     */       } 
/*     */       break;
/*     */     } 
/* 506 */     if (obj instanceof HashMap)
/*     */       return; 
/* 508 */     PdfDictionary merged = item.getMerged(0);
/* 509 */     if (obj == null) {
/* 510 */       PdfDictionary field = new PdfDictionary();
/* 511 */       if (PdfName.SIG.equals(merged.get(PdfName.FT)))
/* 512 */         this.hasSignature = true; 
/* 513 */       for (PdfName element : merged.getKeys()) {
/* 514 */         PdfName key = element;
/* 515 */         if (fieldKeys.containsKey(key))
/* 516 */           field.put(key, merged.get(key)); 
/*     */       } 
/* 518 */       ArrayList<Object> list = new ArrayList();
/* 519 */       list.add(field);
/* 520 */       createWidgets(list, item);
/* 521 */       map.put(s, list);
/*     */     } else {
/*     */       
/* 524 */       ArrayList<Object> list = (ArrayList)obj;
/* 525 */       PdfDictionary field = (PdfDictionary)list.get(0);
/* 526 */       PdfName type1 = (PdfName)field.get(PdfName.FT);
/* 527 */       PdfName type2 = (PdfName)merged.get(PdfName.FT);
/* 528 */       if (type1 == null || !type1.equals(type2))
/*     */         return; 
/* 530 */       int flag1 = 0;
/* 531 */       PdfObject f1 = field.get(PdfName.FF);
/* 532 */       if (f1 != null && f1.isNumber())
/* 533 */         flag1 = ((PdfNumber)f1).intValue(); 
/* 534 */       int flag2 = 0;
/* 535 */       PdfObject f2 = merged.get(PdfName.FF);
/* 536 */       if (f2 != null && f2.isNumber())
/* 537 */         flag2 = ((PdfNumber)f2).intValue(); 
/* 538 */       if (type1.equals(PdfName.BTN)) {
/* 539 */         if (((flag1 ^ flag2) & 0x10000) != 0)
/*     */           return; 
/* 541 */         if ((flag1 & 0x10000) == 0 && ((flag1 ^ flag2) & 0x8000) != 0) {
/*     */           return;
/*     */         }
/* 544 */       } else if (type1.equals(PdfName.CH) && ((
/* 545 */         flag1 ^ flag2) & 0x20000) != 0) {
/*     */         return;
/*     */       } 
/* 548 */       createWidgets(list, item);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void mergeWithMaster(Map<String, AcroFields.Item> fd) {
/* 556 */     for (Map.Entry<String, AcroFields.Item> entry : fd.entrySet()) {
/* 557 */       String name = entry.getKey();
/* 558 */       mergeField(name, entry.getValue());
/*     */     } 
/*     */   }
/*     */   
/*     */   void mergeFields() {
/* 563 */     int pageOffset = 0;
/* 564 */     for (int k = 0; k < this.fields.size(); k++) {
/* 565 */       Map<String, AcroFields.Item> fd = ((AcroFields)this.fields.get(k)).getFields();
/* 566 */       addPageOffsetToField(fd, pageOffset);
/* 567 */       mergeWithMaster(fd);
/* 568 */       pageOffset += ((PdfReader)this.readers.get(k)).getNumberOfPages();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public PdfIndirectReference getPageReference(int page) {
/* 574 */     return this.pageRefs.get(page - 1);
/*     */   }
/*     */ 
/*     */   
/*     */   protected PdfDictionary getCatalog(PdfIndirectReference rootObj) {
/*     */     try {
/* 580 */       PdfDictionary cat = this.pdf.getCatalog(rootObj);
/* 581 */       if (this.form != null) {
/* 582 */         PdfIndirectReference ref = addToBody(this.form).getIndirectReference();
/* 583 */         cat.put(PdfName.ACROFORM, ref);
/*     */       } 
/* 585 */       return cat;
/*     */     }
/* 587 */     catch (IOException e) {
/* 588 */       throw new ExceptionConverter(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected PdfIndirectReference getNewReference(PRIndirectReference ref) {
/* 593 */     return new PdfIndirectReference(0, getNewObjectNumber(ref.getReader(), ref.getNumber(), 0));
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getNewObjectNumber(PdfReader reader, int number, int generation) {
/* 598 */     IntHashtable refs = this.readers2intrefs.get(reader);
/* 599 */     int n = refs.get(number);
/* 600 */     if (n == 0) {
/* 601 */       n = getIndirectReferenceNumber();
/* 602 */       refs.put(number, n);
/*     */     } 
/* 604 */     return n;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean setVisited(PRIndirectReference ref) {
/* 614 */     IntHashtable refs = this.visited.get(ref.getReader());
/* 615 */     if (refs != null) {
/* 616 */       return (refs.put(ref.getNumber(), 1) != 0);
/*     */     }
/* 618 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isVisited(PRIndirectReference ref) {
/* 627 */     IntHashtable refs = this.visited.get(ref.getReader());
/* 628 */     if (refs != null) {
/* 629 */       return refs.containsKey(ref.getNumber());
/*     */     }
/* 631 */     return false;
/*     */   }
/*     */   
/*     */   protected boolean isVisited(PdfReader reader, int number, int generation) {
/* 635 */     IntHashtable refs = this.readers2intrefs.get(reader);
/* 636 */     return refs.containsKey(number);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isPage(PRIndirectReference ref) {
/* 645 */     IntHashtable refs = this.pages2intrefs.get(ref.getReader());
/* 646 */     if (refs != null) {
/* 647 */       return refs.containsKey(ref.getNumber());
/*     */     }
/* 649 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   RandomAccessFileOrArray getReaderFile(PdfReader reader) {
/* 654 */     return this.file;
/*     */   }
/*     */   
/*     */   public void openDoc() {
/* 658 */     if (!this.nd.isOpen())
/* 659 */       this.nd.open(); 
/*     */   }
/*     */   
/* 662 */   protected static final HashMap<PdfName, Integer> widgetKeys = new HashMap<PdfName, Integer>();
/* 663 */   protected static final HashMap<PdfName, Integer> fieldKeys = new HashMap<PdfName, Integer>();
/*     */   static {
/* 665 */     Integer one = Integer.valueOf(1);
/* 666 */     widgetKeys.put(PdfName.SUBTYPE, one);
/* 667 */     widgetKeys.put(PdfName.CONTENTS, one);
/* 668 */     widgetKeys.put(PdfName.RECT, one);
/* 669 */     widgetKeys.put(PdfName.NM, one);
/* 670 */     widgetKeys.put(PdfName.M, one);
/* 671 */     widgetKeys.put(PdfName.F, one);
/* 672 */     widgetKeys.put(PdfName.BS, one);
/* 673 */     widgetKeys.put(PdfName.BORDER, one);
/* 674 */     widgetKeys.put(PdfName.AP, one);
/* 675 */     widgetKeys.put(PdfName.AS, one);
/* 676 */     widgetKeys.put(PdfName.C, one);
/* 677 */     widgetKeys.put(PdfName.A, one);
/* 678 */     widgetKeys.put(PdfName.STRUCTPARENT, one);
/* 679 */     widgetKeys.put(PdfName.OC, one);
/* 680 */     widgetKeys.put(PdfName.H, one);
/* 681 */     widgetKeys.put(PdfName.MK, one);
/* 682 */     widgetKeys.put(PdfName.DA, one);
/* 683 */     widgetKeys.put(PdfName.Q, one);
/* 684 */     widgetKeys.put(PdfName.P, one);
/* 685 */     fieldKeys.put(PdfName.AA, one);
/* 686 */     fieldKeys.put(PdfName.FT, one);
/* 687 */     fieldKeys.put(PdfName.TU, one);
/* 688 */     fieldKeys.put(PdfName.TM, one);
/* 689 */     fieldKeys.put(PdfName.FF, one);
/* 690 */     fieldKeys.put(PdfName.V, one);
/* 691 */     fieldKeys.put(PdfName.DV, one);
/* 692 */     fieldKeys.put(PdfName.DS, one);
/* 693 */     fieldKeys.put(PdfName.RV, one);
/* 694 */     fieldKeys.put(PdfName.OPT, one);
/* 695 */     fieldKeys.put(PdfName.MAXLEN, one);
/* 696 */     fieldKeys.put(PdfName.TI, one);
/* 697 */     fieldKeys.put(PdfName.I, one);
/* 698 */     fieldKeys.put(PdfName.LOCK, one);
/* 699 */     fieldKeys.put(PdfName.SV, one);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/PdfCopyFieldsImp.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */