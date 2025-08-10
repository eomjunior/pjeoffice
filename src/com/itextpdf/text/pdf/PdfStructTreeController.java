/*     */ package com.itextpdf.text.pdf;
/*     */ 
/*     */ import com.itextpdf.text.error_messages.MessageLocalization;
/*     */ import java.io.IOException;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PdfStructTreeController
/*     */ {
/*     */   private PdfDictionary structTreeRoot;
/*     */   private PdfCopy writer;
/*     */   private PdfStructureTreeRoot structureTreeRoot;
/*     */   private PdfDictionary parentTree;
/*     */   protected PdfReader reader;
/*  60 */   private PdfDictionary roleMap = null;
/*  61 */   private PdfDictionary sourceRoleMap = null;
/*  62 */   private PdfDictionary sourceClassMap = null;
/*  63 */   private PdfIndirectReference nullReference = null;
/*     */   
/*     */   public enum returnType {
/*  66 */     BELOW, FOUND, ABOVE, NOTFOUND; }
/*     */   
/*     */   protected PdfStructTreeController(PdfReader reader, PdfCopy writer) throws BadPdfFormatException {
/*  69 */     if (!writer.isTagged())
/*  70 */       throw new BadPdfFormatException(MessageLocalization.getComposedMessage("no.structtreeroot.found", new Object[0])); 
/*  71 */     this.writer = writer;
/*  72 */     this.structureTreeRoot = writer.getStructureTreeRoot();
/*  73 */     this.structureTreeRoot.put(PdfName.PARENTTREE, new PdfDictionary(PdfName.STRUCTELEM));
/*  74 */     setReader(reader);
/*     */   }
/*     */   
/*     */   protected void setReader(PdfReader reader) throws BadPdfFormatException {
/*  78 */     this.reader = reader;
/*  79 */     PdfObject obj = reader.getCatalog().get(PdfName.STRUCTTREEROOT);
/*  80 */     obj = getDirectObject(obj);
/*  81 */     if (obj == null || !obj.isDictionary())
/*  82 */       throw new BadPdfFormatException(MessageLocalization.getComposedMessage("no.structtreeroot.found", new Object[0])); 
/*  83 */     this.structTreeRoot = (PdfDictionary)obj;
/*  84 */     obj = getDirectObject(this.structTreeRoot.get(PdfName.PARENTTREE));
/*  85 */     if (obj == null || !obj.isDictionary())
/*  86 */       throw new BadPdfFormatException(MessageLocalization.getComposedMessage("the.document.does.not.contain.parenttree", new Object[0])); 
/*  87 */     this.parentTree = (PdfDictionary)obj;
/*  88 */     this.sourceRoleMap = null;
/*  89 */     this.sourceClassMap = null;
/*  90 */     this.nullReference = null;
/*     */   }
/*     */   
/*     */   public static boolean checkTagged(PdfReader reader) {
/*  94 */     PdfObject obj = reader.getCatalog().get(PdfName.STRUCTTREEROOT);
/*  95 */     obj = getDirectObject(obj);
/*  96 */     if (obj == null || !obj.isDictionary())
/*  97 */       return false; 
/*  98 */     PdfDictionary structTreeRoot = (PdfDictionary)obj;
/*  99 */     obj = getDirectObject(structTreeRoot.get(PdfName.PARENTTREE));
/* 100 */     if (obj == null || !obj.isDictionary())
/* 101 */       return false; 
/* 102 */     return true;
/*     */   }
/*     */   
/*     */   public static PdfObject getDirectObject(PdfObject object) {
/* 106 */     if (object == null)
/* 107 */       return null; 
/* 108 */     while (object.isIndirect())
/* 109 */       object = PdfReader.getPdfObjectRelease(object); 
/* 110 */     return object;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void copyStructTreeForPage(PdfNumber sourceArrayNumber, int newArrayNumber) throws BadPdfFormatException, IOException {
/* 119 */     if (copyPageMarks(this.parentTree, sourceArrayNumber, newArrayNumber) == returnType.NOTFOUND) {
/* 120 */       throw new BadPdfFormatException(MessageLocalization.getComposedMessage("invalid.structparent", new Object[0]));
/*     */     }
/*     */   }
/*     */   
/*     */   private returnType copyPageMarks(PdfDictionary parentTree, PdfNumber arrayNumber, int newArrayNumber) throws BadPdfFormatException, IOException {
/* 125 */     PdfArray pages = (PdfArray)getDirectObject(parentTree.get(PdfName.NUMS));
/* 126 */     if (pages == null) {
/* 127 */       PdfArray kids = (PdfArray)getDirectObject(parentTree.get(PdfName.KIDS));
/* 128 */       if (kids == null)
/* 129 */         return returnType.NOTFOUND; 
/* 130 */       int cur = kids.size() / 2;
/* 131 */       int begin = 0;
/*     */       while (true) {
/* 133 */         PdfDictionary kidTree = (PdfDictionary)getDirectObject(kids.getPdfObject(cur + begin));
/* 134 */         switch (copyPageMarks(kidTree, arrayNumber, newArrayNumber)) {
/*     */           case FOUND:
/* 136 */             return returnType.FOUND;
/*     */           case ABOVE:
/* 138 */             begin += cur;
/* 139 */             cur /= 2;
/* 140 */             if (cur == 0)
/* 141 */               cur = 1; 
/* 142 */             if (cur + begin == kids.size())
/* 143 */               return returnType.ABOVE; 
/*     */             continue;
/*     */           case BELOW:
/* 146 */             if (cur + begin == 0)
/* 147 */               return returnType.BELOW; 
/* 148 */             if (cur == 0)
/* 149 */               return returnType.NOTFOUND; 
/* 150 */             cur /= 2; continue;
/*     */         }  break;
/*     */       } 
/* 153 */       return returnType.NOTFOUND;
/*     */     } 
/*     */ 
/*     */     
/* 157 */     if (pages.size() == 0)
/* 158 */       return returnType.NOTFOUND; 
/* 159 */     return findAndCopyMarks(pages, arrayNumber.intValue(), newArrayNumber);
/*     */   }
/*     */ 
/*     */   
/*     */   private returnType findAndCopyMarks(PdfArray pages, int arrayNumber, int newArrayNumber) throws BadPdfFormatException, IOException {
/* 164 */     if (pages.getAsNumber(0).intValue() > arrayNumber)
/* 165 */       return returnType.BELOW; 
/* 166 */     if (pages.getAsNumber(pages.size() - 2).intValue() < arrayNumber)
/* 167 */       return returnType.ABOVE; 
/* 168 */     int cur = pages.size() / 4;
/* 169 */     int begin = 0;
/*     */ 
/*     */     
/*     */     while (true) {
/* 173 */       int curNumber = pages.getAsNumber((begin + cur) * 2).intValue();
/* 174 */       if (curNumber == arrayNumber) {
/* 175 */         PdfObject obj = pages.getPdfObject((begin + cur) * 2 + 1);
/* 176 */         PdfObject obj1 = obj;
/* 177 */         for (; obj.isIndirect(); obj = PdfReader.getPdfObjectRelease(obj));
/* 178 */         if (obj.isArray()) {
/* 179 */           PdfObject firstNotNullKid = null;
/* 180 */           for (PdfObject numObj : obj) {
/* 181 */             if (numObj.isNull()) {
/* 182 */               if (this.nullReference == null)
/* 183 */                 this.nullReference = this.writer.addToBody(new PdfNull()).getIndirectReference(); 
/* 184 */               this.structureTreeRoot.setPageMark(newArrayNumber, this.nullReference); continue;
/*     */             } 
/* 186 */             PdfObject res = this.writer.copyObject(numObj, true, false);
/* 187 */             if (firstNotNullKid == null) firstNotNullKid = res; 
/* 188 */             this.structureTreeRoot.setPageMark(newArrayNumber, (PdfIndirectReference)res);
/*     */           } 
/*     */           
/* 191 */           attachStructTreeRootKids(firstNotNullKid);
/* 192 */         } else if (obj.isDictionary()) {
/* 193 */           PdfDictionary k = getKDict((PdfDictionary)obj);
/* 194 */           if (k == null)
/* 195 */             return returnType.NOTFOUND; 
/* 196 */           PdfObject res = this.writer.copyObject(obj1, true, false);
/* 197 */           this.structureTreeRoot.setAnnotationMark(newArrayNumber, (PdfIndirectReference)res);
/*     */         } else {
/* 199 */           return returnType.NOTFOUND;
/* 200 */         }  return returnType.FOUND;
/*     */       } 
/* 202 */       if (curNumber < arrayNumber) {
/* 203 */         if (cur == 0)
/* 204 */           return returnType.NOTFOUND; 
/* 205 */         begin += cur;
/* 206 */         if (cur != 1)
/* 207 */           cur /= 2; 
/* 208 */         if (cur + begin == pages.size())
/* 209 */           return returnType.NOTFOUND; 
/*     */         continue;
/*     */       } 
/* 212 */       if (cur + begin == 0)
/* 213 */         return returnType.BELOW; 
/* 214 */       if (cur == 0)
/* 215 */         return returnType.NOTFOUND; 
/* 216 */       cur /= 2;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void attachStructTreeRootKids(PdfObject firstNotNullKid) throws IOException, BadPdfFormatException {
/* 224 */     PdfObject structKids = this.structTreeRoot.get(PdfName.K);
/* 225 */     if (structKids == null || (!structKids.isArray() && !structKids.isIndirect())) {
/*     */       
/* 227 */       addKid(this.structureTreeRoot, firstNotNullKid);
/*     */     }
/* 229 */     else if (structKids.isIndirect()) {
/* 230 */       addKid(structKids);
/*     */     } else {
/* 232 */       for (PdfObject kid : structKids) {
/* 233 */         addKid(kid);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   static PdfDictionary getKDict(PdfDictionary obj) {
/* 239 */     PdfDictionary k = obj.getAsDict(PdfName.K);
/* 240 */     if (k != null) {
/* 241 */       if (PdfName.OBJR.equals(k.getAsName(PdfName.TYPE))) {
/* 242 */         return k;
/*     */       }
/*     */     } else {
/* 245 */       PdfArray k1 = obj.getAsArray(PdfName.K);
/* 246 */       if (k1 == null)
/* 247 */         return null; 
/* 248 */       for (int i = 0; i < k1.size(); i++) {
/* 249 */         k = k1.getAsDict(i);
/* 250 */         if (k != null && 
/* 251 */           PdfName.OBJR.equals(k.getAsName(PdfName.TYPE))) {
/* 252 */           return k;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 257 */     return null;
/*     */   }
/*     */   
/*     */   private void addKid(PdfObject obj) throws IOException, BadPdfFormatException {
/* 261 */     if (!obj.isIndirect())
/* 262 */       return;  PRIndirectReference currRef = (PRIndirectReference)obj;
/* 263 */     RefKey key = new RefKey(currRef);
/* 264 */     if (!this.writer.indirects.containsKey(key)) {
/* 265 */       this.writer.copyIndirect(currRef, true, false);
/*     */     }
/* 267 */     PdfIndirectReference newKid = ((PdfCopy.IndirectReferences)this.writer.indirects.get(key)).getRef();
/*     */     
/* 269 */     if (this.writer.updateRootKids) {
/* 270 */       addKid(this.structureTreeRoot, newKid);
/* 271 */       this.writer.structureTreeRootKidsForReaderImported(this.reader);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static PdfArray getDirectArray(PdfArray in) {
/* 276 */     PdfArray out = new PdfArray();
/* 277 */     for (int i = 0; i < in.size(); i++) {
/* 278 */       PdfObject value = getDirectObject(in.getPdfObject(i));
/* 279 */       if (value != null)
/*     */       {
/* 281 */         if (value.isArray()) {
/* 282 */           out.add(getDirectArray((PdfArray)value));
/* 283 */         } else if (value.isDictionary()) {
/* 284 */           out.add(getDirectDict((PdfDictionary)value));
/*     */         } else {
/* 286 */           out.add(value);
/*     */         }  } 
/*     */     } 
/* 289 */     return out;
/*     */   }
/*     */   
/*     */   private static PdfDictionary getDirectDict(PdfDictionary in) {
/* 293 */     PdfDictionary out = new PdfDictionary();
/* 294 */     for (Map.Entry<PdfName, PdfObject> entry : in.hashMap.entrySet()) {
/* 295 */       PdfObject value = getDirectObject(entry.getValue());
/* 296 */       if (value == null)
/*     */         continue; 
/* 298 */       if (value.isArray()) {
/* 299 */         out.put(entry.getKey(), getDirectArray((PdfArray)value)); continue;
/* 300 */       }  if (value.isDictionary()) {
/* 301 */         out.put(entry.getKey(), getDirectDict((PdfDictionary)value)); continue;
/*     */       } 
/* 303 */       out.put(entry.getKey(), value);
/*     */     } 
/*     */     
/* 306 */     return out;
/*     */   }
/*     */   
/*     */   public static boolean compareObjects(PdfObject value1, PdfObject value2) {
/* 310 */     value2 = getDirectObject(value2);
/* 311 */     if (value2 == null)
/* 312 */       return false; 
/* 313 */     if (value1.type() != value2.type()) {
/* 314 */       return false;
/*     */     }
/* 316 */     if (value1.isBoolean()) {
/* 317 */       if (value1 == value2)
/* 318 */         return true; 
/* 319 */       if (value2 instanceof PdfBoolean) {
/* 320 */         return (((PdfBoolean)value1).booleanValue() == ((PdfBoolean)value2).booleanValue());
/*     */       }
/* 322 */       return false;
/* 323 */     }  if (value1.isName())
/* 324 */       return value1.equals(value2); 
/* 325 */     if (value1.isNumber()) {
/* 326 */       if (value1 == value2)
/* 327 */         return true; 
/* 328 */       if (value2 instanceof PdfNumber) {
/* 329 */         return (((PdfNumber)value1).doubleValue() == ((PdfNumber)value2).doubleValue());
/*     */       }
/* 331 */       return false;
/* 332 */     }  if (value1.isNull()) {
/* 333 */       if (value1 == value2)
/* 334 */         return true; 
/* 335 */       if (value2 instanceof PdfNull)
/* 336 */         return true; 
/* 337 */       return false;
/* 338 */     }  if (value1.isString()) {
/* 339 */       if (value1 == value2)
/* 340 */         return true; 
/* 341 */       if (value2 instanceof PdfString) {
/* 342 */         return ((((PdfString)value2).value == null && ((PdfString)value1).value == null) || (((PdfString)value1).value != null && ((PdfString)value1).value
/* 343 */           .equals(((PdfString)value2).value)));
/*     */       }
/* 345 */       return false;
/*     */     } 
/* 347 */     if (value1.isArray()) {
/* 348 */       PdfArray array1 = (PdfArray)value1;
/* 349 */       PdfArray array2 = (PdfArray)value2;
/* 350 */       if (array1.size() != array2.size())
/* 351 */         return false; 
/* 352 */       for (int i = 0; i < array1.size(); i++) {
/* 353 */         if (!compareObjects(array1.getPdfObject(i), array2.getPdfObject(i)))
/* 354 */           return false; 
/* 355 */       }  return true;
/*     */     } 
/* 357 */     if (value1.isDictionary()) {
/* 358 */       PdfDictionary first = (PdfDictionary)value1;
/* 359 */       PdfDictionary second = (PdfDictionary)value2;
/* 360 */       if (first.size() != second.size())
/* 361 */         return false; 
/* 362 */       for (PdfName name : first.hashMap.keySet()) {
/* 363 */         if (!compareObjects(first.get(name), second.get(name)))
/* 364 */           return false; 
/*     */       } 
/* 366 */       return true;
/*     */     } 
/* 368 */     return false;
/*     */   }
/*     */   
/*     */   protected void addClass(PdfObject object) throws BadPdfFormatException {
/* 372 */     object = getDirectObject(object);
/* 373 */     if (object.isDictionary()) {
/* 374 */       PdfObject curClass = ((PdfDictionary)object).get(PdfName.C);
/* 375 */       if (curClass == null)
/*     */         return; 
/* 377 */       if (curClass.isArray())
/* 378 */       { PdfArray array = (PdfArray)curClass;
/* 379 */         for (int i = 0; i < array.size(); i++) {
/* 380 */           addClass(array.getPdfObject(i));
/*     */         } }
/* 382 */       else if (curClass.isName())
/* 383 */       { addClass(curClass); } 
/* 384 */     } else if (object.isName()) {
/* 385 */       PdfName name = (PdfName)object;
/* 386 */       if (this.sourceClassMap == null) {
/* 387 */         object = getDirectObject(this.structTreeRoot.get(PdfName.CLASSMAP));
/* 388 */         if (object == null || !object.isDictionary()) {
/*     */           return;
/*     */         }
/* 391 */         this.sourceClassMap = (PdfDictionary)object;
/*     */       } 
/* 393 */       object = getDirectObject(this.sourceClassMap.get(name));
/* 394 */       if (object == null) {
/*     */         return;
/*     */       }
/* 397 */       PdfObject put = this.structureTreeRoot.getMappedClass(name);
/* 398 */       if (put != null) {
/* 399 */         if (!compareObjects(put, object)) {
/* 400 */           throw new BadPdfFormatException(MessageLocalization.getComposedMessage("conflict.in.classmap", new Object[] { name }));
/*     */         }
/*     */       }
/* 403 */       else if (object.isDictionary()) {
/* 404 */         this.structureTreeRoot.mapClass(name, getDirectDict((PdfDictionary)object));
/* 405 */       } else if (object.isArray()) {
/* 406 */         this.structureTreeRoot.mapClass(name, getDirectArray((PdfArray)object));
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addRole(PdfName structType) throws BadPdfFormatException {
/* 413 */     if (structType == null) {
/*     */       return;
/*     */     }
/* 416 */     for (PdfName name : this.writer.getStandardStructElems()) {
/* 417 */       if (name.equals(structType))
/*     */         return; 
/*     */     } 
/* 420 */     if (this.sourceRoleMap == null) {
/* 421 */       PdfObject pdfObject = getDirectObject(this.structTreeRoot.get(PdfName.ROLEMAP));
/* 422 */       if (pdfObject == null || !pdfObject.isDictionary()) {
/*     */         return;
/*     */       }
/* 425 */       this.sourceRoleMap = (PdfDictionary)pdfObject;
/*     */     } 
/* 427 */     PdfObject object = this.sourceRoleMap.get(structType);
/* 428 */     if (object == null || !object.isName()) {
/*     */       return;
/*     */     }
/*     */     
/* 432 */     if (this.roleMap == null)
/* 433 */     { this.roleMap = new PdfDictionary();
/* 434 */       this.structureTreeRoot.put(PdfName.ROLEMAP, this.roleMap);
/* 435 */       this.roleMap.put(structType, object); }
/* 436 */     else { PdfObject currentRole; if ((currentRole = this.roleMap.get(structType)) != null) {
/* 437 */         if (!currentRole.equals(object)) {
/* 438 */           throw new BadPdfFormatException(MessageLocalization.getComposedMessage("conflict.in.rolemap", new Object[] { structType }));
/*     */         }
/*     */       } else {
/* 441 */         this.roleMap.put(structType, object);
/*     */       }  }
/*     */   
/*     */   } protected void addKid(PdfDictionary parent, PdfObject kid) {
/*     */     PdfArray kids;
/* 446 */     PdfObject kidObj = parent.get(PdfName.K);
/*     */     
/* 448 */     if (kidObj instanceof PdfArray) {
/* 449 */       kids = (PdfArray)kidObj;
/*     */     } else {
/* 451 */       kids = new PdfArray();
/* 452 */       if (kidObj != null)
/* 453 */         kids.add(kidObj); 
/*     */     } 
/* 455 */     kids.add(kid);
/* 456 */     parent.put(PdfName.K, kids);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/PdfStructTreeController.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */