/*      */ package com.itextpdf.text.pdf;
/*      */ 
/*      */ import com.itextpdf.text.DocListener;
/*      */ import com.itextpdf.text.Document;
/*      */ import com.itextpdf.text.DocumentException;
/*      */ import com.itextpdf.text.ExceptionConverter;
/*      */ import com.itextpdf.text.Rectangle;
/*      */ import com.itextpdf.text.error_messages.MessageLocalization;
/*      */ import com.itextpdf.text.exceptions.BadPasswordException;
/*      */ import com.itextpdf.text.log.Counter;
/*      */ import com.itextpdf.text.log.CounterFactory;
/*      */ import com.itextpdf.text.log.Logger;
/*      */ import com.itextpdf.text.log.LoggerFactory;
/*      */ import java.io.IOException;
/*      */ import java.io.OutputStream;
/*      */ import java.util.ArrayList;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.StringTokenizer;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class PdfCopy
/*      */   extends PdfWriter
/*      */ {
/*   76 */   private static final Logger LOGGER = LoggerFactory.getLogger(PdfCopy.class);
/*      */ 
/*      */   
/*      */   static class IndirectReferences
/*      */   {
/*      */     PdfIndirectReference theRef;
/*      */     
/*      */     boolean hasCopied;
/*      */     
/*      */     IndirectReferences(PdfIndirectReference ref) {
/*   86 */       this.theRef = ref;
/*   87 */       this.hasCopied = false;
/*      */     }
/*   89 */     void setCopied() { this.hasCopied = true; }
/*   90 */     void setNotCopied() { this.hasCopied = false; }
/*   91 */     boolean getCopied() { return this.hasCopied; } PdfIndirectReference getRef() {
/*   92 */       return this.theRef;
/*      */     }
/*      */     
/*      */     public String toString() {
/*   96 */       String ext = "";
/*   97 */       if (this.hasCopied) ext = ext + " Copied"; 
/*   98 */       return getRef() + ext;
/*      */     }
/*      */   }
/*      */   
/*  102 */   protected static Counter COUNTER = CounterFactory.getCounter(PdfCopy.class); protected HashMap<RefKey, IndirectReferences> indirects; protected HashMap<PdfReader, HashMap<RefKey, IndirectReferences>> indirectMap;
/*      */   protected Counter getCounter() {
/*  104 */     return COUNTER;
/*      */   }
/*      */ 
/*      */   
/*      */   protected HashMap<PdfObject, PdfObject> parentObjects;
/*      */   protected HashSet<PdfObject> disableIndirects;
/*      */   protected PdfReader reader;
/*  111 */   protected int[] namePtr = new int[] { 0 };
/*      */   
/*      */   private boolean rotateContents = true;
/*      */   protected PdfArray fieldArray;
/*      */   protected HashSet<PdfTemplate> fieldTemplates;
/*  116 */   private PdfStructTreeController structTreeController = null;
/*  117 */   private int currentStructArrayNumber = 0;
/*      */   
/*      */   protected PRIndirectReference structTreeRootReference;
/*      */   
/*      */   protected LinkedHashMap<RefKey, PdfIndirectObject> indirectObjects;
/*      */   
/*      */   protected ArrayList<PdfIndirectObject> savedObjects;
/*      */   
/*      */   protected ArrayList<ImportedPage> importedPages;
/*      */   
/*      */   protected boolean updateRootKids = false;
/*      */   
/*  129 */   private static final PdfName annotId = new PdfName("iTextAnnotId");
/*  130 */   private static int annotIdCnt = 0;
/*      */   
/*      */   protected boolean mergeFields = false;
/*      */   private boolean needAppearances = false;
/*      */   private boolean hasSignature;
/*      */   private PdfIndirectReference acroForm;
/*      */   private HashMap<PdfArray, ArrayList<Integer>> tabOrder;
/*      */   private ArrayList<Object> calculationOrderRefs;
/*      */   private PdfDictionary resources;
/*      */   protected ArrayList<AcroFields> fields;
/*      */   private ArrayList<String> calculationOrder;
/*      */   private HashMap<String, Object> fieldTree;
/*      */   private HashMap<Integer, PdfIndirectObject> unmergedMap;
/*      */   private HashMap<RefKey, PdfIndirectObject> unmergedIndirectRefsMap;
/*      */   private HashMap<Integer, PdfIndirectObject> mergedMap;
/*      */   private HashSet<PdfIndirectObject> mergedSet;
/*      */   private boolean mergeFieldsInternalCall = false;
/*  147 */   private static final PdfName iTextTag = new PdfName("_iTextTag_");
/*  148 */   private static final Integer zero = Integer.valueOf(0);
/*  149 */   private HashSet<Object> mergedRadioButtons = new HashSet();
/*  150 */   private HashMap<Object, PdfString> mergedTextFields = new HashMap<Object, PdfString>();
/*      */   
/*  152 */   private HashSet<PdfReader> readersWithImportedStructureTreeRootKids = new HashSet<PdfReader>();
/*      */   
/*      */   protected static class ImportedPage {
/*      */     int pageNumber;
/*      */     PdfReader reader;
/*      */     PdfArray mergedFields;
/*      */     PdfIndirectReference annotsIndirectReference;
/*      */     
/*      */     ImportedPage(PdfReader reader, int pageNumber, boolean keepFields) {
/*  161 */       this.pageNumber = pageNumber;
/*  162 */       this.reader = reader;
/*  163 */       if (keepFields) {
/*  164 */         this.mergedFields = new PdfArray();
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(Object o) {
/*  170 */       if (!(o instanceof ImportedPage)) return false; 
/*  171 */       ImportedPage other = (ImportedPage)o;
/*  172 */       return (this.pageNumber == other.pageNumber && this.reader.equals(other.reader));
/*      */     }
/*      */     
/*      */     public String toString() {
/*  176 */       return Integer.toString(this.pageNumber);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PdfCopy(Document document, OutputStream os) throws DocumentException {
/*  186 */     super(new PdfDocument(), os);
/*  187 */     document.addDocListener((DocListener)this.pdf);
/*  188 */     this.pdf.addWriter(this);
/*  189 */     this.indirectMap = new HashMap<PdfReader, HashMap<RefKey, IndirectReferences>>();
/*  190 */     this.parentObjects = new HashMap<PdfObject, PdfObject>();
/*  191 */     this.disableIndirects = new HashSet<PdfObject>();
/*      */     
/*  193 */     this.indirectObjects = new LinkedHashMap<RefKey, PdfIndirectObject>();
/*  194 */     this.savedObjects = new ArrayList<PdfIndirectObject>();
/*  195 */     this.importedPages = new ArrayList<ImportedPage>();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPageEvent(PdfPageEvent event) {
/*  205 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isRotateContents() {
/*  213 */     return this.rotateContents;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setRotateContents(boolean rotateContents) {
/*  221 */     this.rotateContents = rotateContents;
/*      */   }
/*      */   
/*      */   public void setMergeFields() {
/*  225 */     this.mergeFields = true;
/*  226 */     this.resources = new PdfDictionary();
/*  227 */     this.fields = new ArrayList<AcroFields>();
/*  228 */     this.calculationOrder = new ArrayList<String>();
/*  229 */     this.fieldTree = new LinkedHashMap<String, Object>();
/*  230 */     this.unmergedMap = new HashMap<Integer, PdfIndirectObject>();
/*  231 */     this.unmergedIndirectRefsMap = new HashMap<RefKey, PdfIndirectObject>();
/*  232 */     this.mergedMap = new HashMap<Integer, PdfIndirectObject>();
/*  233 */     this.mergedSet = new HashSet<PdfIndirectObject>();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PdfImportedPage getImportedPage(PdfReader reader, int pageNumber) {
/*  244 */     if (this.mergeFields && !this.mergeFieldsInternalCall) {
/*  245 */       throw new IllegalArgumentException(MessageLocalization.getComposedMessage("1.method.cannot.be.used.in.mergeFields.mode.please.use.addDocument", new Object[] { "getImportedPage" }));
/*      */     }
/*  247 */     if (this.mergeFields) {
/*  248 */       ImportedPage newPage = new ImportedPage(reader, pageNumber, this.mergeFields);
/*  249 */       this.importedPages.add(newPage);
/*      */     } 
/*  251 */     if (this.structTreeController != null)
/*  252 */       this.structTreeController.reader = null; 
/*  253 */     this.disableIndirects.clear();
/*  254 */     this.parentObjects.clear();
/*  255 */     return getImportedPageImpl(reader, pageNumber);
/*      */   }
/*      */   
/*      */   public PdfImportedPage getImportedPage(PdfReader reader, int pageNumber, boolean keepTaggedPdfStructure) throws BadPdfFormatException {
/*  259 */     if (this.mergeFields && !this.mergeFieldsInternalCall) {
/*  260 */       throw new IllegalArgumentException(MessageLocalization.getComposedMessage("1.method.cannot.be.used.in.mergeFields.mode.please.use.addDocument", new Object[] { "getImportedPage" }));
/*      */     }
/*  262 */     this.updateRootKids = false;
/*  263 */     if (!keepTaggedPdfStructure) {
/*  264 */       if (this.mergeFields) {
/*  265 */         ImportedPage importedPage = new ImportedPage(reader, pageNumber, this.mergeFields);
/*  266 */         this.importedPages.add(importedPage);
/*      */       } 
/*  268 */       return getImportedPageImpl(reader, pageNumber);
/*      */     } 
/*  270 */     if (this.structTreeController != null) {
/*  271 */       if (reader != this.structTreeController.reader)
/*  272 */         this.structTreeController.setReader(reader); 
/*      */     } else {
/*  274 */       this.structTreeController = new PdfStructTreeController(reader, this);
/*      */     } 
/*  276 */     ImportedPage newPage = new ImportedPage(reader, pageNumber, this.mergeFields);
/*  277 */     switch (checkStructureTreeRootKids(newPage)) {
/*      */       case -1:
/*  279 */         clearIndirects(reader);
/*  280 */         this.updateRootKids = true;
/*      */         break;
/*      */       case 0:
/*  283 */         this.updateRootKids = false;
/*      */         break;
/*      */       case 1:
/*  286 */         this.updateRootKids = true;
/*      */         break;
/*      */     } 
/*  289 */     this.importedPages.add(newPage);
/*      */     
/*  291 */     this.disableIndirects.clear();
/*  292 */     this.parentObjects.clear();
/*  293 */     return getImportedPageImpl(reader, pageNumber);
/*      */   }
/*      */   
/*      */   private void clearIndirects(PdfReader reader) {
/*  297 */     HashMap<RefKey, IndirectReferences> currIndirects = this.indirectMap.get(reader);
/*  298 */     ArrayList<RefKey> forDelete = new ArrayList<RefKey>();
/*  299 */     for (Map.Entry<RefKey, IndirectReferences> entry : currIndirects.entrySet()) {
/*  300 */       PdfIndirectReference iRef = ((IndirectReferences)entry.getValue()).theRef;
/*  301 */       RefKey key = new RefKey(iRef);
/*  302 */       PdfIndirectObject iobj = this.indirectObjects.get(key);
/*  303 */       if (iobj == null) {
/*  304 */         forDelete.add(entry.getKey()); continue;
/*      */       } 
/*  306 */       if (iobj.object.isArray() || iobj.object.isDictionary() || iobj.object.isStream()) {
/*  307 */         forDelete.add(entry.getKey());
/*      */       }
/*      */     } 
/*      */     
/*  311 */     for (RefKey key : forDelete) {
/*  312 */       currIndirects.remove(key);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int checkStructureTreeRootKids(ImportedPage newPage) {
/*  320 */     if (this.importedPages.size() == 0) return 1; 
/*  321 */     boolean readerExist = false;
/*  322 */     for (ImportedPage page : this.importedPages) {
/*  323 */       if (page.reader.equals(newPage.reader)) {
/*  324 */         readerExist = true;
/*      */         
/*      */         break;
/*      */       } 
/*      */     } 
/*      */     
/*  330 */     if (!readerExist) return 1;
/*      */     
/*  332 */     ImportedPage lastPage = this.importedPages.get(this.importedPages.size() - 1);
/*  333 */     boolean equalReader = lastPage.reader.equals(newPage.reader);
/*      */     
/*  335 */     if (equalReader && newPage.pageNumber > lastPage.pageNumber) {
/*  336 */       if (this.readersWithImportedStructureTreeRootKids.contains(newPage.reader)) {
/*  337 */         return 0;
/*      */       }
/*  339 */       return 1;
/*      */     } 
/*      */     
/*  342 */     return -1;
/*      */   }
/*      */   
/*      */   protected void structureTreeRootKidsForReaderImported(PdfReader reader) {
/*  346 */     this.readersWithImportedStructureTreeRootKids.add(reader);
/*      */   }
/*      */   
/*      */   protected void fixStructureTreeRoot(HashSet<RefKey> activeKeys, HashSet<PdfName> activeClassMaps) {
/*  350 */     HashMap<PdfName, PdfObject> newClassMap = new HashMap<PdfName, PdfObject>(activeClassMaps.size());
/*  351 */     for (PdfName key : activeClassMaps) {
/*  352 */       PdfObject cm = this.structureTreeRoot.classes.get(key);
/*  353 */       if (cm != null) newClassMap.put(key, cm);
/*      */     
/*      */     } 
/*  356 */     this.structureTreeRoot.classes = newClassMap;
/*      */     
/*  358 */     PdfArray kids = this.structureTreeRoot.getAsArray(PdfName.K);
/*  359 */     if (kids != null)
/*  360 */       for (int i = 0; i < kids.size(); i++) {
/*  361 */         PdfIndirectReference iref = (PdfIndirectReference)kids.getPdfObject(i);
/*  362 */         RefKey key = new RefKey(iref);
/*  363 */         if (!activeKeys.contains(key)) kids.remove(i--);
/*      */       
/*      */       }  
/*      */   }
/*      */   
/*      */   protected PdfImportedPage getImportedPageImpl(PdfReader reader, int pageNumber) {
/*  369 */     if (this.currentPdfReaderInstance != null) {
/*  370 */       if (this.currentPdfReaderInstance.getReader() != reader)
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  380 */         this.currentPdfReaderInstance = getPdfReaderInstance(reader);
/*      */       }
/*      */     } else {
/*      */       
/*  384 */       this.currentPdfReaderInstance = getPdfReaderInstance(reader);
/*      */     } 
/*      */ 
/*      */     
/*  388 */     return this.currentPdfReaderInstance.getImportedPage(pageNumber);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected PdfIndirectReference copyIndirect(PRIndirectReference in, boolean keepStructure, boolean directRootKids) throws IOException, BadPdfFormatException {
/*      */     PdfIndirectReference theRef;
/*  402 */     RefKey key = new RefKey(in);
/*  403 */     IndirectReferences iRef = this.indirects.get(key);
/*  404 */     PdfObject obj = PdfReader.getPdfObjectRelease(in);
/*  405 */     if (keepStructure && directRootKids && 
/*  406 */       obj instanceof PdfDictionary) {
/*  407 */       PdfDictionary dict = (PdfDictionary)obj;
/*  408 */       if (dict.contains(PdfName.PG)) {
/*  409 */         return null;
/*      */       }
/*      */     } 
/*  412 */     if (iRef != null) {
/*  413 */       theRef = iRef.getRef();
/*  414 */       if (iRef.getCopied()) {
/*  415 */         return theRef;
/*      */       }
/*      */     } else {
/*      */       
/*  419 */       theRef = this.body.getPdfIndirectReference();
/*  420 */       iRef = new IndirectReferences(theRef);
/*  421 */       this.indirects.put(key, iRef);
/*      */     } 
/*      */     
/*  424 */     if (obj != null && obj.isDictionary()) {
/*  425 */       PdfObject type = PdfReader.getPdfObjectRelease(((PdfDictionary)obj).get(PdfName.TYPE));
/*  426 */       if (type != null) {
/*  427 */         if (PdfName.PAGE.equals(type)) {
/*  428 */           return theRef;
/*      */         }
/*  430 */         if (PdfName.CATALOG.equals(type)) {
/*  431 */           LOGGER.warn(MessageLocalization.getComposedMessage("make.copy.of.catalog.dictionary.is.forbidden", new Object[0]));
/*  432 */           return null;
/*      */         } 
/*      */       } 
/*      */     } 
/*  436 */     iRef.setCopied();
/*  437 */     if (obj != null) this.parentObjects.put(obj, in); 
/*  438 */     PdfObject res = copyObject(obj, keepStructure, directRootKids);
/*  439 */     if (this.disableIndirects.contains(obj))
/*  440 */       iRef.setNotCopied(); 
/*  441 */     if (res != null) {
/*      */       
/*  443 */       addToBody(res, theRef);
/*  444 */       return theRef;
/*      */     } 
/*      */     
/*  447 */     this.indirects.remove(key);
/*  448 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected PdfIndirectReference copyIndirect(PRIndirectReference in) throws IOException, BadPdfFormatException {
/*  463 */     return copyIndirect(in, false, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected PdfDictionary copyDictionary(PdfDictionary in, boolean keepStruct, boolean directRootKids) throws IOException, BadPdfFormatException {
/*  472 */     PdfDictionary out = new PdfDictionary(in.size());
/*  473 */     PdfObject type = PdfReader.getPdfObjectRelease(in.get(PdfName.TYPE));
/*      */     
/*  475 */     if (keepStruct) {
/*      */       
/*  477 */       if (directRootKids && in.contains(PdfName.PG)) {
/*      */         
/*  479 */         PdfObject curr = in;
/*  480 */         this.disableIndirects.add(curr);
/*  481 */         while (this.parentObjects.containsKey(curr) && !this.disableIndirects.contains(curr)) {
/*  482 */           curr = this.parentObjects.get(curr);
/*  483 */           this.disableIndirects.add(curr);
/*      */         } 
/*  485 */         return null;
/*      */       } 
/*      */       
/*  488 */       PdfName structType = in.getAsName(PdfName.S);
/*  489 */       this.structTreeController.addRole(structType);
/*  490 */       this.structTreeController.addClass(in);
/*      */     } 
/*  492 */     if (this.structTreeController != null && this.structTreeController.reader != null && (in.contains(PdfName.STRUCTPARENTS) || in.contains(PdfName.STRUCTPARENT))) {
/*  493 */       PdfName key = PdfName.STRUCTPARENT;
/*  494 */       if (in.contains(PdfName.STRUCTPARENTS)) {
/*  495 */         key = PdfName.STRUCTPARENTS;
/*      */       }
/*  497 */       PdfObject value = in.get(key);
/*  498 */       out.put(key, new PdfNumber(this.currentStructArrayNumber));
/*  499 */       this.structTreeController.copyStructTreeForPage((PdfNumber)value, this.currentStructArrayNumber++);
/*      */     } 
/*  501 */     for (PdfName element : in.getKeys()) {
/*  502 */       PdfObject res; PdfName key = element;
/*  503 */       PdfObject value = in.get(key);
/*  504 */       if (this.structTreeController != null && this.structTreeController.reader != null && (key.equals(PdfName.STRUCTPARENTS) || key.equals(PdfName.STRUCTPARENT))) {
/*      */         continue;
/*      */       }
/*  507 */       if (PdfName.PAGE.equals(type)) {
/*  508 */         if (!key.equals(PdfName.B) && !key.equals(PdfName.PARENT)) {
/*  509 */           this.parentObjects.put(value, in);
/*  510 */           res = copyObject(value, keepStruct, directRootKids);
/*  511 */           if (res != null) {
/*  512 */             out.put(key, res);
/*      */           }
/*      */         } 
/*      */         continue;
/*      */       } 
/*  517 */       if (this.tagged && value.isIndirect() && isStructTreeRootReference((PRIndirectReference)value)) {
/*  518 */         res = this.structureTreeRoot.getReference();
/*      */       } else {
/*  520 */         res = copyObject(value, keepStruct, directRootKids);
/*      */       } 
/*  522 */       if (res != null) {
/*  523 */         out.put(key, res);
/*      */       }
/*      */     } 
/*      */     
/*  527 */     return out;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected PdfDictionary copyDictionary(PdfDictionary in) throws IOException, BadPdfFormatException {
/*  536 */     return copyDictionary(in, false, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected PdfStream copyStream(PRStream in) throws IOException, BadPdfFormatException {
/*  543 */     PRStream out = new PRStream(in, null);
/*      */     
/*  545 */     for (PdfName element : in.getKeys()) {
/*  546 */       PdfName key = element;
/*  547 */       PdfObject value = in.get(key);
/*  548 */       this.parentObjects.put(value, in);
/*  549 */       PdfObject res = copyObject(value);
/*  550 */       if (res != null) {
/*  551 */         out.put(key, res);
/*      */       }
/*      */     } 
/*  554 */     return out;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected PdfArray copyArray(PdfArray in, boolean keepStruct, boolean directRootKids) throws IOException, BadPdfFormatException {
/*  562 */     PdfArray out = new PdfArray(in.size());
/*      */     
/*  564 */     for (Iterator<PdfObject> i = in.listIterator(); i.hasNext(); ) {
/*  565 */       PdfObject value = i.next();
/*  566 */       this.parentObjects.put(value, in);
/*  567 */       PdfObject res = copyObject(value, keepStruct, directRootKids);
/*  568 */       if (res != null)
/*  569 */         out.add(res); 
/*      */     } 
/*  571 */     return out;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected PdfArray copyArray(PdfArray in) throws IOException, BadPdfFormatException {
/*  579 */     return copyArray(in, false, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected PdfObject copyObject(PdfObject in, boolean keepStruct, boolean directRootKids) throws IOException, BadPdfFormatException {
/*  586 */     if (in == null)
/*  587 */       return PdfNull.PDFNULL; 
/*  588 */     switch (in.type) {
/*      */       case 6:
/*  590 */         return copyDictionary((PdfDictionary)in, keepStruct, directRootKids);
/*      */       case 10:
/*  592 */         if (!keepStruct && !directRootKids)
/*      */         {
/*  594 */           return copyIndirect((PRIndirectReference)in);
/*      */         }
/*  596 */         return copyIndirect((PRIndirectReference)in, keepStruct, directRootKids);
/*      */       case 5:
/*  598 */         return copyArray((PdfArray)in, keepStruct, directRootKids);
/*      */       case 0:
/*      */       case 1:
/*      */       case 2:
/*      */       case 3:
/*      */       case 4:
/*      */       case 8:
/*  605 */         return in;
/*      */       case 7:
/*  607 */         return copyStream((PRStream)in);
/*      */     } 
/*      */     
/*  610 */     if (in.type < 0) {
/*  611 */       String lit = ((PdfLiteral)in).toString();
/*  612 */       if (lit.equals("true") || lit.equals("false")) {
/*  613 */         return new PdfBoolean(lit);
/*      */       }
/*  615 */       return new PdfLiteral(lit);
/*      */     } 
/*  617 */     System.out.println("CANNOT COPY type " + in.type);
/*  618 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected PdfObject copyObject(PdfObject in) throws IOException, BadPdfFormatException {
/*  626 */     return copyObject(in, false, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected int setFromIPage(PdfImportedPage iPage) {
/*  633 */     int pageNum = iPage.getPageNumber();
/*  634 */     PdfReaderInstance inst = this.currentPdfReaderInstance = iPage.getPdfReaderInstance();
/*  635 */     this.reader = inst.getReader();
/*  636 */     setFromReader(this.reader);
/*  637 */     return pageNum;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void setFromReader(PdfReader reader) {
/*  644 */     this.reader = reader;
/*  645 */     this.indirects = this.indirectMap.get(reader);
/*  646 */     if (this.indirects == null) {
/*  647 */       this.indirects = new HashMap<RefKey, IndirectReferences>();
/*  648 */       this.indirectMap.put(reader, this.indirects);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addPage(PdfImportedPage iPage) throws IOException, BadPdfFormatException {
/*  657 */     if (this.mergeFields && !this.mergeFieldsInternalCall) {
/*  658 */       throw new IllegalArgumentException(MessageLocalization.getComposedMessage("1.method.cannot.be.used.in.mergeFields.mode.please.use.addDocument", new Object[] { "addPage" }));
/*      */     }
/*      */     
/*  661 */     int pageNum = setFromIPage(iPage);
/*  662 */     PdfDictionary thePage = this.reader.getPageN(pageNum);
/*  663 */     PRIndirectReference origRef = this.reader.getPageOrigRef(pageNum);
/*  664 */     this.reader.releasePage(pageNum);
/*  665 */     RefKey key = new RefKey(origRef);
/*      */     
/*  667 */     IndirectReferences iRef = this.indirects.get(key);
/*  668 */     if (iRef != null && !iRef.getCopied()) {
/*  669 */       this.pageReferences.add(iRef.getRef());
/*  670 */       iRef.setCopied();
/*      */     } 
/*  672 */     PdfIndirectReference pageRef = getCurrentPage();
/*  673 */     if (iRef == null) {
/*  674 */       iRef = new IndirectReferences(pageRef);
/*  675 */       this.indirects.put(key, iRef);
/*      */     } 
/*  677 */     iRef.setCopied();
/*  678 */     if (this.tagged)
/*  679 */       this.structTreeRootReference = (PRIndirectReference)this.reader.getCatalog().get(PdfName.STRUCTTREEROOT); 
/*  680 */     PdfDictionary newPage = copyDictionary(thePage);
/*  681 */     if (this.mergeFields) {
/*  682 */       ImportedPage importedPage = this.importedPages.get(this.importedPages.size() - 1);
/*  683 */       importedPage.annotsIndirectReference = this.body.getPdfIndirectReference();
/*  684 */       newPage.put(PdfName.ANNOTS, importedPage.annotsIndirectReference);
/*      */     } 
/*  686 */     this.root.addPage(newPage);
/*  687 */     iPage.setCopied();
/*  688 */     this.currentPageNumber++;
/*  689 */     this.pdf.setPageCount(this.currentPageNumber);
/*  690 */     this.structTreeRootReference = null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addPage(Rectangle rect, int rotation) throws DocumentException {
/*  701 */     if (this.mergeFields && !this.mergeFieldsInternalCall) {
/*  702 */       throw new IllegalArgumentException(MessageLocalization.getComposedMessage("1.method.cannot.be.used.in.mergeFields.mode.please.use.addDocument", new Object[] { "addPage" }));
/*      */     }
/*  704 */     PdfRectangle mediabox = new PdfRectangle(rect, rotation);
/*  705 */     PageResources resources = new PageResources();
/*  706 */     PdfPage page = new PdfPage(mediabox, new HashMap<String, PdfRectangle>(), resources.getResources(), 0);
/*  707 */     page.put(PdfName.TABS, getTabs());
/*  708 */     this.root.addPage(page);
/*  709 */     this.currentPageNumber++;
/*  710 */     this.pdf.setPageCount(this.currentPageNumber);
/*      */   }
/*      */   
/*      */   public void addDocument(PdfReader reader, List<Integer> pagesToKeep) throws DocumentException, IOException {
/*  714 */     if (this.indirectMap.containsKey(reader)) {
/*  715 */       throw new IllegalArgumentException(MessageLocalization.getComposedMessage("document.1.has.already.been.added", new Object[] { reader.toString() }));
/*      */     }
/*  717 */     reader.selectPages(pagesToKeep, false);
/*  718 */     addDocument(reader);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void copyDocumentFields(PdfReader reader) throws DocumentException, IOException {
/*  728 */     if (!this.document.isOpen()) {
/*  729 */       throw new DocumentException(MessageLocalization.getComposedMessage("the.document.is.not.open.yet.you.can.only.add.meta.information", new Object[0]));
/*      */     }
/*      */     
/*  732 */     if (this.indirectMap.containsKey(reader)) {
/*  733 */       throw new IllegalArgumentException(MessageLocalization.getComposedMessage("document.1.has.already.been.added", new Object[] { reader.toString() }));
/*      */     }
/*      */     
/*  736 */     if (!reader.isOpenedWithFullPermissions()) {
/*  737 */       throw new BadPasswordException(MessageLocalization.getComposedMessage("pdfreader.not.opened.with.owner.password", new Object[0]));
/*      */     }
/*  739 */     if (!this.mergeFields) {
/*  740 */       throw new IllegalArgumentException(MessageLocalization.getComposedMessage("1.method.can.be.only.used.in.mergeFields.mode.please.use.addDocument", new Object[] { "copyDocumentFields" }));
/*      */     }
/*  742 */     this.indirects = new HashMap<RefKey, IndirectReferences>();
/*  743 */     this.indirectMap.put(reader, this.indirects);
/*      */     
/*  745 */     reader.consolidateNamedDestinations();
/*  746 */     reader.shuffleSubsetNames();
/*  747 */     if (this.tagged && PdfStructTreeController.checkTagged(reader)) {
/*  748 */       this.structTreeRootReference = (PRIndirectReference)reader.getCatalog().get(PdfName.STRUCTTREEROOT);
/*  749 */       if (this.structTreeController != null) {
/*  750 */         if (reader != this.structTreeController.reader)
/*  751 */           this.structTreeController.setReader(reader); 
/*      */       } else {
/*  753 */         this.structTreeController = new PdfStructTreeController(reader, this);
/*      */       } 
/*      */     } 
/*      */     
/*  757 */     List<PdfObject> annotationsToBeCopied = new ArrayList<PdfObject>();
/*      */     
/*  759 */     for (int i = 1; i <= reader.getNumberOfPages(); i++) {
/*  760 */       PdfDictionary page = reader.getPageNRelease(i);
/*  761 */       if (page != null && page.contains(PdfName.ANNOTS)) {
/*  762 */         PdfArray annots = page.getAsArray(PdfName.ANNOTS);
/*  763 */         if (annots != null && annots.size() > 0) {
/*  764 */           if (this.importedPages.size() < i)
/*  765 */             throw new DocumentException(MessageLocalization.getComposedMessage("there.are.not.enough.imported.pages.for.copied.fields", new Object[0])); 
/*  766 */           ((HashMap<RefKey, IndirectReferences>)this.indirectMap.get(reader)).put(new RefKey(reader.pageRefs.getPageOrigRef(i)), new IndirectReferences(this.pageReferences.get(i - 1)));
/*  767 */           for (int j = 0; j < annots.size(); j++) {
/*  768 */             PdfDictionary annot = annots.getAsDict(j);
/*  769 */             if (annot != null) {
/*  770 */               annot.put(annotId, new PdfNumber(++annotIdCnt));
/*  771 */               annotationsToBeCopied.add(annots.getPdfObject(j));
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/*  778 */     for (PdfObject annot : annotationsToBeCopied) {
/*  779 */       copyObject(annot);
/*      */     }
/*      */     
/*  782 */     if (this.tagged && this.structTreeController != null) {
/*  783 */       this.structTreeController.attachStructTreeRootKids(null);
/*      */     }
/*  785 */     AcroFields acro = reader.getAcroFields();
/*  786 */     boolean needapp = !acro.isGenerateAppearances();
/*  787 */     if (needapp)
/*  788 */       this.needAppearances = true; 
/*  789 */     this.fields.add(acro);
/*  790 */     updateCalculationOrder(reader);
/*  791 */     this.structTreeRootReference = null;
/*      */   }
/*      */   
/*      */   public void addDocument(PdfReader reader) throws DocumentException, IOException {
/*  795 */     if (!this.document.isOpen()) {
/*  796 */       throw new DocumentException(MessageLocalization.getComposedMessage("the.document.is.not.open.yet.you.can.only.add.meta.information", new Object[0]));
/*      */     }
/*  798 */     if (this.indirectMap.containsKey(reader)) {
/*  799 */       throw new IllegalArgumentException(MessageLocalization.getComposedMessage("document.1.has.already.been.added", new Object[] { reader.toString() }));
/*      */     }
/*  801 */     if (!reader.isOpenedWithFullPermissions())
/*  802 */       throw new BadPasswordException(MessageLocalization.getComposedMessage("pdfreader.not.opened.with.owner.password", new Object[0])); 
/*  803 */     if (this.mergeFields) {
/*  804 */       reader.consolidateNamedDestinations();
/*  805 */       reader.shuffleSubsetNames();
/*  806 */       for (int j = 1; j <= reader.getNumberOfPages(); j++) {
/*  807 */         PdfDictionary page = reader.getPageNRelease(j);
/*  808 */         if (page != null && page.contains(PdfName.ANNOTS)) {
/*  809 */           PdfArray annots = page.getAsArray(PdfName.ANNOTS);
/*  810 */           if (annots != null)
/*  811 */             for (int k = 0; k < annots.size(); k++) {
/*  812 */               PdfDictionary annot = annots.getAsDict(k);
/*  813 */               if (annot != null) {
/*  814 */                 annot.put(annotId, new PdfNumber(++annotIdCnt));
/*      */               }
/*      */             }  
/*      */         } 
/*      */       } 
/*  819 */       AcroFields acro = reader.getAcroFields();
/*      */ 
/*      */       
/*  822 */       boolean needapp = !acro.isGenerateAppearances();
/*  823 */       if (needapp)
/*  824 */         this.needAppearances = true; 
/*  825 */       this.fields.add(acro);
/*  826 */       updateCalculationOrder(reader);
/*      */     } 
/*  828 */     boolean tagged = (this.tagged && PdfStructTreeController.checkTagged(reader));
/*  829 */     this.mergeFieldsInternalCall = true;
/*  830 */     for (int i = 1; i <= reader.getNumberOfPages(); i++) {
/*  831 */       addPage(getImportedPage(reader, i, tagged));
/*      */     }
/*  833 */     this.mergeFieldsInternalCall = false;
/*      */   }
/*      */ 
/*      */   
/*      */   public PdfIndirectObject addToBody(PdfObject object, PdfIndirectReference ref) throws IOException {
/*  838 */     return addToBody(object, ref, false);
/*      */   }
/*      */   
/*      */   public PdfIndirectObject addToBody(PdfObject object, PdfIndirectReference ref, boolean formBranching) throws IOException {
/*      */     PdfIndirectObject iobj;
/*  843 */     if (formBranching) {
/*  844 */       updateReferences(object);
/*      */     }
/*      */     
/*  847 */     if ((this.tagged || this.mergeFields) && this.indirectObjects != null && (object.isArray() || object.isDictionary() || object.isStream() || object.isNull())) {
/*  848 */       RefKey key = new RefKey(ref);
/*  849 */       PdfIndirectObject obj = this.indirectObjects.get(key);
/*  850 */       if (obj == null) {
/*  851 */         obj = new PdfIndirectObject(ref, object, this);
/*  852 */         this.indirectObjects.put(key, obj);
/*      */       } 
/*  854 */       iobj = obj;
/*      */     } else {
/*  856 */       iobj = super.addToBody(object, ref);
/*      */     } 
/*  858 */     if (this.mergeFields && object.isDictionary()) {
/*  859 */       PdfNumber annotId = ((PdfDictionary)object).getAsNumber(PdfCopy.annotId);
/*  860 */       if (annotId != null) {
/*  861 */         if (formBranching) {
/*  862 */           this.mergedMap.put(Integer.valueOf(annotId.intValue()), iobj);
/*  863 */           this.mergedSet.add(iobj);
/*      */         } else {
/*  865 */           this.unmergedMap.put(Integer.valueOf(annotId.intValue()), iobj);
/*  866 */           this.unmergedIndirectRefsMap.put(new RefKey(iobj.number, iobj.generation), iobj);
/*      */         } 
/*      */       }
/*      */     } 
/*  870 */     return iobj;
/*      */   }
/*      */ 
/*      */   
/*      */   protected void cacheObject(PdfIndirectObject iobj) {
/*  875 */     if ((this.tagged || this.mergeFields) && this.indirectObjects != null) {
/*  876 */       this.savedObjects.add(iobj);
/*  877 */       RefKey key = new RefKey(iobj.number, iobj.generation);
/*  878 */       if (!this.indirectObjects.containsKey(key)) this.indirectObjects.put(key, iobj);
/*      */     
/*      */     } 
/*      */   }
/*      */   
/*      */   protected void flushTaggedObjects() throws IOException {
/*      */     
/*  885 */     try { fixTaggedStructure(); }
/*  886 */     catch (ClassCastException classCastException) {  }
/*  887 */     finally { flushIndirectObjects(); }
/*      */   
/*      */   }
/*      */   
/*      */   protected void flushAcroFields() throws IOException, BadPdfFormatException {
/*  892 */     if (this.mergeFields) {
/*      */ 
/*      */       
/*  895 */       try { for (ImportedPage page : this.importedPages) {
/*  896 */           PdfDictionary pageDict = page.reader.getPageN(page.pageNumber);
/*  897 */           if (pageDict != null) {
/*  898 */             PdfArray pageFields = pageDict.getAsArray(PdfName.ANNOTS);
/*  899 */             if (pageFields == null || pageFields.size() == 0)
/*      */               continue; 
/*  901 */             for (AcroFields.Item items : page.reader.getAcroFields().getFields().values()) {
/*  902 */               for (PdfIndirectReference ref : items.widget_refs) {
/*  903 */                 pageFields.arrayList.remove(ref);
/*      */               }
/*      */             } 
/*  906 */             this.indirects = this.indirectMap.get(page.reader);
/*  907 */             for (PdfObject ref : pageFields.arrayList) {
/*  908 */               page.mergedFields.add(copyObject(ref));
/*      */             }
/*      */           } 
/*      */         } 
/*  912 */         for (PdfReader reader : this.indirectMap.keySet()) {
/*  913 */           reader.removeFields();
/*      */         }
/*  915 */         mergeFields();
/*  916 */         createAcroForms(); }
/*      */       
/*  918 */       catch (ClassCastException classCastException) {  }
/*      */       finally
/*  920 */       { if (!this.tagged) {
/*  921 */           flushIndirectObjects();
/*      */         } }
/*      */     
/*      */     }
/*      */   }
/*      */   
/*      */   protected void fixTaggedStructure() throws IOException {
/*  928 */     HashMap<Integer, PdfIndirectReference> numTree = this.structureTreeRoot.getNumTree();
/*  929 */     HashSet<RefKey> activeKeys = new HashSet<RefKey>();
/*  930 */     ArrayList<PdfIndirectReference> actives = new ArrayList<PdfIndirectReference>();
/*  931 */     int pageRefIndex = 0;
/*      */     
/*  933 */     if (this.mergeFields && this.acroForm != null) {
/*  934 */       actives.add(this.acroForm);
/*  935 */       activeKeys.add(new RefKey(this.acroForm));
/*      */     } 
/*  937 */     for (PdfIndirectReference page : this.pageReferences) {
/*  938 */       actives.add(page);
/*  939 */       activeKeys.add(new RefKey(page));
/*      */     } 
/*      */ 
/*      */     
/*  943 */     for (int i = numTree.size() - 1; i >= 0; i--) {
/*  944 */       PdfIndirectReference currNum = numTree.get(Integer.valueOf(i));
/*  945 */       if (currNum != null) {
/*      */ 
/*      */         
/*  948 */         RefKey numKey = new RefKey(currNum);
/*  949 */         PdfObject obj = ((PdfIndirectObject)this.indirectObjects.get(numKey)).object;
/*  950 */         if (obj.isDictionary()) {
/*  951 */           boolean addActiveKeys = false;
/*  952 */           if (this.pageReferences.contains(((PdfDictionary)obj).get(PdfName.PG))) {
/*  953 */             addActiveKeys = true;
/*      */           } else {
/*  955 */             PdfDictionary k = PdfStructTreeController.getKDict((PdfDictionary)obj);
/*  956 */             if (k != null && this.pageReferences.contains(k.get(PdfName.PG))) {
/*  957 */               addActiveKeys = true;
/*      */             }
/*      */           } 
/*  960 */           if (addActiveKeys) {
/*  961 */             activeKeys.add(numKey);
/*  962 */             actives.add(currNum);
/*      */           } else {
/*  964 */             numTree.remove(Integer.valueOf(i));
/*      */           } 
/*  966 */         } else if (obj.isArray()) {
/*  967 */           activeKeys.add(numKey);
/*  968 */           actives.add(currNum);
/*  969 */           PdfArray currNums = (PdfArray)obj;
/*  970 */           PdfIndirectReference currPage = this.pageReferences.get(pageRefIndex++);
/*  971 */           actives.add(currPage);
/*  972 */           activeKeys.add(new RefKey(currPage));
/*  973 */           PdfIndirectReference prevKid = null;
/*  974 */           for (int j = 0; j < currNums.size(); j++) {
/*  975 */             PdfIndirectReference currKid = (PdfIndirectReference)currNums.getDirectObject(j);
/*  976 */             if (!currKid.equals(prevKid)) {
/*  977 */               RefKey kidKey = new RefKey(currKid);
/*  978 */               activeKeys.add(kidKey);
/*  979 */               actives.add(currKid);
/*      */               
/*  981 */               PdfIndirectObject iobj = this.indirectObjects.get(kidKey);
/*  982 */               if (iobj.object.isDictionary()) {
/*  983 */                 PdfDictionary dict = (PdfDictionary)iobj.object;
/*  984 */                 PdfIndirectReference pg = (PdfIndirectReference)dict.get(PdfName.PG);
/*      */                 
/*  986 */                 if (pg != null && !this.pageReferences.contains(pg) && !pg.equals(currPage)) {
/*  987 */                   dict.put(PdfName.PG, currPage);
/*  988 */                   PdfArray kids = dict.getAsArray(PdfName.K);
/*  989 */                   if (kids != null) {
/*  990 */                     PdfObject firstKid = kids.getDirectObject(0);
/*  991 */                     if (firstKid.isNumber()) kids.remove(0); 
/*      */                   } 
/*      */                 } 
/*      */               } 
/*  995 */               prevKid = currKid;
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/* 1000 */     }  HashSet<PdfName> activeClassMaps = new HashSet<PdfName>();
/*      */     
/* 1002 */     findActives(actives, activeKeys, activeClassMaps);
/*      */     
/* 1004 */     ArrayList<PdfIndirectReference> newRefs = findActiveParents(activeKeys);
/*      */     
/* 1006 */     fixPgKey(newRefs, activeKeys);
/*      */     
/* 1008 */     fixStructureTreeRoot(activeKeys, activeClassMaps);
/*      */     
/* 1010 */     for (Map.Entry<RefKey, PdfIndirectObject> entry : this.indirectObjects.entrySet()) {
/* 1011 */       if (!activeKeys.contains(entry.getKey())) {
/* 1012 */         entry.setValue(null);
/*      */         continue;
/*      */       } 
/* 1015 */       if (((PdfIndirectObject)entry.getValue()).object.isArray()) {
/* 1016 */         removeInactiveReferences((PdfArray)((PdfIndirectObject)entry.getValue()).object, activeKeys); continue;
/* 1017 */       }  if (((PdfIndirectObject)entry.getValue()).object.isDictionary()) {
/* 1018 */         PdfObject kids = ((PdfDictionary)((PdfIndirectObject)entry.getValue()).object).get(PdfName.K);
/* 1019 */         if (kids != null && kids.isArray()) {
/* 1020 */           removeInactiveReferences((PdfArray)kids, activeKeys);
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private void removeInactiveReferences(PdfArray array, HashSet<RefKey> activeKeys) {
/* 1027 */     for (int i = 0; i < array.size(); i++) {
/* 1028 */       PdfObject obj = array.getPdfObject(i);
/* 1029 */       if ((obj.type() == 0 && !activeKeys.contains(new RefKey((PdfIndirectReference)obj))) || (obj
/* 1030 */         .isDictionary() && containsInactivePg((PdfDictionary)obj, activeKeys)))
/* 1031 */         array.remove(i--); 
/*      */     } 
/*      */   }
/*      */   
/*      */   private boolean containsInactivePg(PdfDictionary dict, HashSet<RefKey> activeKeys) {
/* 1036 */     PdfObject pg = dict.get(PdfName.PG);
/* 1037 */     if (pg != null && !activeKeys.contains(new RefKey((PdfIndirectReference)pg)))
/* 1038 */       return true; 
/* 1039 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private ArrayList<PdfIndirectReference> findActiveParents(HashSet<RefKey> activeKeys) {
/* 1044 */     ArrayList<PdfIndirectReference> newRefs = new ArrayList<PdfIndirectReference>();
/* 1045 */     ArrayList<RefKey> tmpActiveKeys = new ArrayList<RefKey>(activeKeys);
/* 1046 */     for (int i = 0; i < tmpActiveKeys.size(); i++) {
/* 1047 */       PdfIndirectObject iobj = this.indirectObjects.get(tmpActiveKeys.get(i));
/* 1048 */       if (iobj != null && iobj.object.isDictionary()) {
/* 1049 */         PdfObject parent = ((PdfDictionary)iobj.object).get(PdfName.P);
/* 1050 */         if (parent != null && parent.type() == 0) {
/* 1051 */           RefKey key = new RefKey((PdfIndirectReference)parent);
/* 1052 */           if (!activeKeys.contains(key)) {
/* 1053 */             activeKeys.add(key);
/* 1054 */             tmpActiveKeys.add(key);
/* 1055 */             newRefs.add((PdfIndirectReference)parent);
/*      */           } 
/*      */         } 
/*      */       } 
/* 1059 */     }  return newRefs;
/*      */   }
/*      */   
/*      */   private void fixPgKey(ArrayList<PdfIndirectReference> newRefs, HashSet<RefKey> activeKeys) {
/* 1063 */     for (PdfIndirectReference iref : newRefs) {
/* 1064 */       PdfIndirectObject iobj = this.indirectObjects.get(new RefKey(iref));
/* 1065 */       if (iobj == null || !iobj.object.isDictionary())
/* 1066 */         continue;  PdfDictionary dict = (PdfDictionary)iobj.object;
/* 1067 */       PdfObject pg = dict.get(PdfName.PG);
/* 1068 */       if (pg == null || activeKeys.contains(new RefKey((PdfIndirectReference)pg)))
/* 1069 */         continue;  PdfArray kids = dict.getAsArray(PdfName.K);
/* 1070 */       if (kids == null)
/* 1071 */         continue;  for (int i = 0; i < kids.size(); i++) {
/* 1072 */         PdfObject obj = kids.getPdfObject(i);
/* 1073 */         if (obj.type() != 0) {
/* 1074 */           kids.remove(i--);
/*      */         } else {
/* 1076 */           PdfIndirectObject kid = this.indirectObjects.get(new RefKey((PdfIndirectReference)obj));
/* 1077 */           if (kid != null && kid.object.isDictionary()) {
/* 1078 */             PdfObject kidPg = ((PdfDictionary)kid.object).get(PdfName.PG);
/* 1079 */             if (kidPg != null && activeKeys.contains(new RefKey((PdfIndirectReference)kidPg))) {
/* 1080 */               dict.put(PdfName.PG, kidPg);
/*      */               break;
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void findActives(ArrayList<PdfIndirectReference> actives, HashSet<RefKey> activeKeys, HashSet<PdfName> activeClassMaps) {
/* 1091 */     for (int i = 0; i < actives.size(); i++) {
/* 1092 */       RefKey key = new RefKey(actives.get(i));
/* 1093 */       PdfIndirectObject iobj = this.indirectObjects.get(key);
/* 1094 */       if (iobj != null && iobj.object != null)
/* 1095 */         switch (iobj.object.type()) {
/*      */           case 0:
/* 1097 */             findActivesFromReference((PdfIndirectReference)iobj.object, actives, activeKeys);
/*      */             break;
/*      */           case 5:
/* 1100 */             findActivesFromArray((PdfArray)iobj.object, actives, activeKeys, activeClassMaps);
/*      */             break;
/*      */           case 6:
/*      */           case 7:
/* 1104 */             findActivesFromDict((PdfDictionary)iobj.object, actives, activeKeys, activeClassMaps);
/*      */             break;
/*      */         }  
/*      */     } 
/*      */   }
/*      */   
/*      */   private void findActivesFromReference(PdfIndirectReference iref, ArrayList<PdfIndirectReference> actives, HashSet<RefKey> activeKeys) {
/* 1111 */     RefKey key = new RefKey(iref);
/* 1112 */     PdfIndirectObject iobj = this.indirectObjects.get(key);
/* 1113 */     if (iobj != null && iobj.object.isDictionary() && containsInactivePg((PdfDictionary)iobj.object, activeKeys))
/*      */       return; 
/* 1115 */     if (!activeKeys.contains(key)) {
/* 1116 */       activeKeys.add(key);
/* 1117 */       actives.add(iref);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void findActivesFromArray(PdfArray array, ArrayList<PdfIndirectReference> actives, HashSet<RefKey> activeKeys, HashSet<PdfName> activeClassMaps) {
/* 1122 */     for (PdfObject obj : array) {
/* 1123 */       switch (obj.type()) {
/*      */         case 0:
/* 1125 */           findActivesFromReference((PdfIndirectReference)obj, actives, activeKeys);
/*      */         
/*      */         case 5:
/* 1128 */           findActivesFromArray((PdfArray)obj, actives, activeKeys, activeClassMaps);
/*      */         
/*      */         case 6:
/*      */         case 7:
/* 1132 */           findActivesFromDict((PdfDictionary)obj, actives, activeKeys, activeClassMaps);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void findActivesFromDict(PdfDictionary dict, ArrayList<PdfIndirectReference> actives, HashSet<RefKey> activeKeys, HashSet<PdfName> activeClassMaps) {
/* 1139 */     if (containsInactivePg(dict, activeKeys))
/* 1140 */       return;  for (PdfName key : dict.getKeys()) {
/* 1141 */       PdfObject obj = dict.get(key);
/* 1142 */       if (key.equals(PdfName.P))
/* 1143 */         continue;  if (key.equals(PdfName.C)) {
/* 1144 */         if (obj.isArray()) {
/* 1145 */           for (PdfObject cm : obj) {
/* 1146 */             if (cm.isName()) activeClassMaps.add((PdfName)cm); 
/*      */           }  continue;
/*      */         } 
/* 1149 */         if (obj.isName()) activeClassMaps.add((PdfName)obj); 
/*      */         continue;
/*      */       } 
/* 1152 */       switch (obj.type()) {
/*      */         case 0:
/* 1154 */           findActivesFromReference((PdfIndirectReference)obj, actives, activeKeys);
/*      */         
/*      */         case 5:
/* 1157 */           findActivesFromArray((PdfArray)obj, actives, activeKeys, activeClassMaps);
/*      */         
/*      */         case 6:
/*      */         case 7:
/* 1161 */           findActivesFromDict((PdfDictionary)obj, actives, activeKeys, activeClassMaps);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected void flushIndirectObjects() throws IOException {
/* 1168 */     for (PdfIndirectObject iobj : this.savedObjects)
/* 1169 */       this.indirectObjects.remove(new RefKey(iobj.number, iobj.generation)); 
/* 1170 */     HashSet<RefKey> inactives = new HashSet<RefKey>();
/* 1171 */     for (Map.Entry<RefKey, PdfIndirectObject> entry : this.indirectObjects.entrySet()) {
/* 1172 */       if (entry.getValue() != null) {
/* 1173 */         writeObjectToBody(entry.getValue()); continue;
/*      */       } 
/* 1175 */       inactives.add(entry.getKey());
/*      */     } 
/* 1177 */     ArrayList<PdfWriter.PdfBody.PdfCrossReference> pdfCrossReferences = new ArrayList<PdfWriter.PdfBody.PdfCrossReference>(this.body.xrefs);
/* 1178 */     for (PdfWriter.PdfBody.PdfCrossReference cr : pdfCrossReferences) {
/* 1179 */       RefKey key = new RefKey(cr.getRefnum(), 0);
/* 1180 */       if (inactives.contains(key))
/* 1181 */         this.body.xrefs.remove(cr); 
/*      */     } 
/* 1183 */     this.indirectObjects = null;
/*      */   }
/*      */   
/*      */   private void writeObjectToBody(PdfIndirectObject object) throws IOException {
/* 1187 */     boolean skipWriting = false;
/* 1188 */     if (this.mergeFields) {
/* 1189 */       updateAnnotationReferences(object.object);
/* 1190 */       if (object.object.isDictionary() || object.object.isStream()) {
/* 1191 */         PdfDictionary dictionary = (PdfDictionary)object.object;
/* 1192 */         if (this.unmergedIndirectRefsMap.containsKey(new RefKey(object.number, object.generation))) {
/* 1193 */           PdfNumber annotId = dictionary.getAsNumber(PdfCopy.annotId);
/* 1194 */           if (annotId != null && this.mergedMap.containsKey(Integer.valueOf(annotId.intValue())))
/* 1195 */             skipWriting = true; 
/*      */         } 
/* 1197 */         if (this.mergedSet.contains(object)) {
/* 1198 */           PdfNumber annotId = dictionary.getAsNumber(PdfCopy.annotId);
/* 1199 */           if (annotId != null) {
/* 1200 */             PdfIndirectObject unmerged = this.unmergedMap.get(Integer.valueOf(annotId.intValue()));
/* 1201 */             if (unmerged != null && unmerged.object.isDictionary()) {
/* 1202 */               PdfNumber structParent = ((PdfDictionary)unmerged.object).getAsNumber(PdfName.STRUCTPARENT);
/* 1203 */               if (structParent != null) {
/* 1204 */                 dictionary.put(PdfName.STRUCTPARENT, structParent);
/*      */               }
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/* 1211 */     if (!skipWriting) {
/* 1212 */       PdfDictionary dictionary = null;
/* 1213 */       PdfNumber annotId = null;
/* 1214 */       if (this.mergeFields && object.object.isDictionary()) {
/* 1215 */         dictionary = (PdfDictionary)object.object;
/* 1216 */         annotId = dictionary.getAsNumber(PdfCopy.annotId);
/* 1217 */         if (annotId != null)
/* 1218 */           dictionary.remove(PdfCopy.annotId); 
/*      */       } 
/* 1220 */       this.body.add(object.object, object.number, object.generation, true);
/* 1221 */       if (annotId != null) {
/* 1222 */         dictionary.put(PdfCopy.annotId, annotId);
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   private void updateAnnotationReferences(PdfObject obj) {
/* 1228 */     if (obj.isArray()) {
/* 1229 */       PdfArray array = (PdfArray)obj;
/* 1230 */       for (int i = 0; i < array.size(); i++) {
/* 1231 */         PdfObject o = array.getPdfObject(i);
/* 1232 */         if (o != null && o.type() == 0) {
/* 1233 */           PdfIndirectObject entry = this.unmergedIndirectRefsMap.get(new RefKey((PdfIndirectReference)o));
/* 1234 */           if (entry != null && 
/* 1235 */             entry.object.isDictionary()) {
/* 1236 */             PdfNumber annotId = ((PdfDictionary)entry.object).getAsNumber(PdfCopy.annotId);
/* 1237 */             if (annotId != null) {
/* 1238 */               PdfIndirectObject merged = this.mergedMap.get(Integer.valueOf(annotId.intValue()));
/* 1239 */               if (merged != null) {
/* 1240 */                 array.set(i, merged.getIndirectReference());
/*      */               }
/*      */             } 
/*      */           } 
/*      */         } else {
/*      */           
/* 1246 */           updateAnnotationReferences(o);
/*      */         } 
/*      */       } 
/* 1249 */     } else if (obj.isDictionary() || obj.isStream()) {
/* 1250 */       PdfDictionary dictionary = (PdfDictionary)obj;
/* 1251 */       for (PdfName key : dictionary.getKeys()) {
/* 1252 */         PdfObject o = dictionary.get(key);
/* 1253 */         if (o != null && o.type() == 0) {
/* 1254 */           PdfIndirectObject entry = this.unmergedIndirectRefsMap.get(new RefKey((PdfIndirectReference)o));
/* 1255 */           if (entry != null && 
/* 1256 */             entry.object.isDictionary()) {
/* 1257 */             PdfNumber annotId = ((PdfDictionary)entry.object).getAsNumber(PdfCopy.annotId);
/* 1258 */             if (annotId != null) {
/* 1259 */               PdfIndirectObject merged = this.mergedMap.get(Integer.valueOf(annotId.intValue()));
/* 1260 */               if (merged != null) {
/* 1261 */                 dictionary.put(key, merged.getIndirectReference());
/*      */               }
/*      */             } 
/*      */           } 
/*      */           continue;
/*      */         } 
/* 1267 */         updateAnnotationReferences(o);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void updateCalculationOrder(PdfReader reader) {
/* 1274 */     PdfDictionary catalog = reader.getCatalog();
/* 1275 */     PdfDictionary acro = catalog.getAsDict(PdfName.ACROFORM);
/* 1276 */     if (acro == null)
/*      */       return; 
/* 1278 */     PdfArray co = acro.getAsArray(PdfName.CO);
/* 1279 */     if (co == null || co.size() == 0)
/*      */       return; 
/* 1281 */     AcroFields af = reader.getAcroFields();
/* 1282 */     for (int k = 0; k < co.size(); k++) {
/* 1283 */       PdfObject obj = co.getPdfObject(k);
/* 1284 */       if (obj != null && obj.isIndirect()) {
/*      */         
/* 1286 */         String name = getCOName(reader, (PRIndirectReference)obj);
/* 1287 */         if (af.getFieldItem(name) != null) {
/*      */           
/* 1289 */           name = "." + name;
/* 1290 */           if (!this.calculationOrder.contains(name))
/*      */           {
/* 1292 */             this.calculationOrder.add(name); } 
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   } private static String getCOName(PdfReader reader, PRIndirectReference ref) {
/* 1297 */     String name = "";
/* 1298 */     while (ref != null) {
/* 1299 */       PdfObject obj = PdfReader.getPdfObject(ref);
/* 1300 */       if (obj == null || obj.type() != 6)
/*      */         break; 
/* 1302 */       PdfDictionary dic = (PdfDictionary)obj;
/* 1303 */       PdfString t = dic.getAsString(PdfName.T);
/* 1304 */       if (t != null) {
/* 1305 */         name = t.toUnicodeString() + "." + name;
/*      */       }
/* 1307 */       ref = (PRIndirectReference)dic.get(PdfName.PARENT);
/*      */     } 
/* 1309 */     if (name.endsWith("."))
/* 1310 */       name = name.substring(0, name.length() - 2); 
/* 1311 */     return name;
/*      */   }
/*      */   
/*      */   private void mergeFields() {
/* 1315 */     int pageOffset = 0;
/* 1316 */     for (int k = 0; k < this.fields.size(); k++) {
/* 1317 */       AcroFields af = this.fields.get(k);
/* 1318 */       Map<String, AcroFields.Item> fd = af.getFields();
/* 1319 */       if (pageOffset < this.importedPages.size() && ((ImportedPage)this.importedPages.get(pageOffset)).reader == af.reader) {
/* 1320 */         addPageOffsetToField(fd, pageOffset);
/* 1321 */         pageOffset += af.reader.getNumberOfPages();
/*      */       } 
/* 1323 */       mergeWithMaster(fd);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void addPageOffsetToField(Map<String, AcroFields.Item> fd, int pageOffset) {
/* 1328 */     if (pageOffset == 0)
/*      */       return; 
/* 1330 */     for (AcroFields.Item item : fd.values()) {
/* 1331 */       for (int k = 0; k < item.size(); k++) {
/* 1332 */         int p = item.getPage(k).intValue();
/* 1333 */         item.forcePage(k, p + pageOffset);
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private void mergeWithMaster(Map<String, AcroFields.Item> fd) {
/* 1339 */     for (Map.Entry<String, AcroFields.Item> entry : fd.entrySet()) {
/* 1340 */       String name = entry.getKey();
/* 1341 */       mergeField(name, entry.getValue());
/*      */     } 
/*      */   }
/*      */   private void mergeField(String name, AcroFields.Item item) {
/*      */     String s;
/*      */     Object<Object, Object> obj;
/* 1347 */     HashMap<String, Object> map = this.fieldTree;
/* 1348 */     StringTokenizer tk = new StringTokenizer(name, ".");
/* 1349 */     if (!tk.hasMoreTokens())
/*      */       return; 
/*      */     while (true) {
/* 1352 */       s = tk.nextToken();
/* 1353 */       obj = (Object<Object, Object>)map.get(s);
/* 1354 */       if (tk.hasMoreTokens()) {
/* 1355 */         if (obj == null) {
/* 1356 */           obj = (Object<Object, Object>)new LinkedHashMap<Object, Object>();
/* 1357 */           map.put(s, obj);
/* 1358 */           map = (HashMap)obj;
/*      */           continue;
/*      */         } 
/* 1361 */         if (obj instanceof HashMap) {
/* 1362 */           map = (HashMap)obj; continue;
/*      */         }  return;
/*      */       } 
/*      */       break;
/*      */     } 
/* 1367 */     if (obj instanceof HashMap)
/*      */       return; 
/* 1369 */     PdfDictionary merged = item.getMerged(0);
/* 1370 */     if (obj == null) {
/* 1371 */       PdfDictionary field = new PdfDictionary();
/* 1372 */       if (PdfName.SIG.equals(merged.get(PdfName.FT)))
/* 1373 */         this.hasSignature = true; 
/* 1374 */       for (PdfName element : merged.getKeys()) {
/* 1375 */         PdfName key = element;
/* 1376 */         if (fieldKeys.contains(key))
/* 1377 */           field.put(key, merged.get(key)); 
/*      */       } 
/* 1379 */       ArrayList<Object> list = new ArrayList();
/* 1380 */       list.add(field);
/* 1381 */       createWidgets(list, item);
/* 1382 */       map.put(s, list);
/*      */     } else {
/*      */       
/* 1385 */       ArrayList<Object> list = (ArrayList)obj;
/* 1386 */       PdfDictionary field = (PdfDictionary)list.get(0);
/* 1387 */       PdfName type1 = (PdfName)field.get(PdfName.FT);
/* 1388 */       PdfName type2 = (PdfName)merged.get(PdfName.FT);
/* 1389 */       if (type1 == null || !type1.equals(type2))
/*      */         return; 
/* 1391 */       int flag1 = 0;
/* 1392 */       PdfObject f1 = field.get(PdfName.FF);
/* 1393 */       if (f1 != null && f1.isNumber())
/* 1394 */         flag1 = ((PdfNumber)f1).intValue(); 
/* 1395 */       int flag2 = 0;
/* 1396 */       PdfObject f2 = merged.get(PdfName.FF);
/* 1397 */       if (f2 != null && f2.isNumber())
/* 1398 */         flag2 = ((PdfNumber)f2).intValue(); 
/* 1399 */       if (type1.equals(PdfName.BTN)) {
/* 1400 */         if (((flag1 ^ flag2) & 0x10000) != 0)
/*      */           return; 
/* 1402 */         if ((flag1 & 0x10000) == 0 && ((flag1 ^ flag2) & 0x8000) != 0) {
/*      */           return;
/*      */         }
/* 1405 */       } else if (type1.equals(PdfName.CH) && ((
/* 1406 */         flag1 ^ flag2) & 0x20000) != 0) {
/*      */         return;
/*      */       } 
/* 1409 */       createWidgets(list, item);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void createWidgets(ArrayList<Object> list, AcroFields.Item item) {
/* 1417 */     for (int k = 0; k < item.size(); k++) {
/* 1418 */       list.add(item.getPage(k));
/* 1419 */       PdfDictionary merged = item.getMerged(k);
/* 1420 */       PdfObject dr = merged.get(PdfName.DR);
/* 1421 */       if (dr != null)
/* 1422 */         PdfFormField.mergeResources(this.resources, (PdfDictionary)PdfReader.getPdfObject(dr)); 
/* 1423 */       PdfDictionary widget = new PdfDictionary();
/* 1424 */       for (PdfName element : merged.getKeys()) {
/* 1425 */         PdfName key = element;
/* 1426 */         if (widgetKeys.contains(key))
/* 1427 */           widget.put(key, merged.get(key)); 
/*      */       } 
/* 1429 */       widget.put(iTextTag, new PdfNumber(item.getTabOrder(k).intValue() + 1));
/* 1430 */       list.add(widget);
/*      */     } 
/*      */   }
/*      */   
/*      */   private PdfObject propagate(PdfObject obj) throws IOException {
/* 1435 */     if (obj == null)
/* 1436 */       return new PdfNull(); 
/* 1437 */     if (obj.isArray()) {
/* 1438 */       PdfArray a = (PdfArray)obj;
/* 1439 */       for (int i = 0; i < a.size(); i++) {
/* 1440 */         a.set(i, propagate(a.getPdfObject(i)));
/*      */       }
/* 1442 */       return a;
/* 1443 */     }  if (obj.isDictionary() || obj.isStream()) {
/* 1444 */       PdfDictionary d = (PdfDictionary)obj;
/* 1445 */       for (PdfName key : d.getKeys()) {
/* 1446 */         d.put(key, propagate(d.get(key)));
/*      */       }
/* 1448 */       return d;
/* 1449 */     }  if (obj.isIndirect()) {
/* 1450 */       obj = PdfReader.getPdfObject(obj);
/* 1451 */       return addToBody(propagate(obj)).getIndirectReference();
/*      */     } 
/* 1453 */     return obj;
/*      */   }
/*      */   
/*      */   private void createAcroForms() throws IOException, BadPdfFormatException {
/* 1457 */     if (this.fieldTree.isEmpty()) {
/*      */       
/* 1459 */       for (ImportedPage importedPage : this.importedPages) {
/* 1460 */         if (importedPage.mergedFields.size() > 0)
/* 1461 */           addToBody(importedPage.mergedFields, importedPage.annotsIndirectReference); 
/*      */       } 
/*      */       return;
/*      */     } 
/* 1465 */     PdfDictionary form = new PdfDictionary();
/* 1466 */     form.put(PdfName.DR, propagate(this.resources));
/*      */     
/* 1468 */     if (this.needAppearances) {
/* 1469 */       form.put(PdfName.NEEDAPPEARANCES, PdfBoolean.PDFTRUE);
/*      */     }
/* 1471 */     form.put(PdfName.DA, new PdfString("/Helv 0 Tf 0 g "));
/* 1472 */     this.tabOrder = new HashMap<PdfArray, ArrayList<Integer>>();
/* 1473 */     this.calculationOrderRefs = new ArrayList(this.calculationOrder);
/* 1474 */     form.put(PdfName.FIELDS, branchForm(this.fieldTree, (PdfIndirectReference)null, ""));
/* 1475 */     if (this.hasSignature)
/* 1476 */       form.put(PdfName.SIGFLAGS, new PdfNumber(3)); 
/* 1477 */     PdfArray co = new PdfArray();
/* 1478 */     for (int k = 0; k < this.calculationOrderRefs.size(); k++) {
/* 1479 */       Object obj = this.calculationOrderRefs.get(k);
/* 1480 */       if (obj instanceof PdfIndirectReference)
/* 1481 */         co.add((PdfIndirectReference)obj); 
/*      */     } 
/* 1483 */     if (co.size() > 0)
/* 1484 */       form.put(PdfName.CO, co); 
/* 1485 */     this.acroForm = addToBody(form).getIndirectReference();
/* 1486 */     for (ImportedPage importedPage : this.importedPages) {
/* 1487 */       addToBody(importedPage.mergedFields, importedPage.annotsIndirectReference);
/*      */     }
/*      */   }
/*      */   
/*      */   private void updateReferences(PdfObject obj) {
/* 1492 */     if (obj.isDictionary() || obj.isStream()) {
/* 1493 */       PdfDictionary dictionary = (PdfDictionary)obj;
/* 1494 */       for (PdfName key : dictionary.getKeys()) {
/* 1495 */         PdfObject o = dictionary.get(key);
/* 1496 */         if (o.isIndirect()) {
/* 1497 */           PdfReader reader = ((PRIndirectReference)o).getReader();
/* 1498 */           HashMap<RefKey, IndirectReferences> indirects = this.indirectMap.get(reader);
/* 1499 */           IndirectReferences indRef = indirects.get(new RefKey((PRIndirectReference)o));
/* 1500 */           if (indRef != null)
/* 1501 */             dictionary.put(key, indRef.getRef()); 
/*      */           continue;
/*      */         } 
/* 1504 */         updateReferences(o);
/*      */       }
/*      */     
/* 1507 */     } else if (obj.isArray()) {
/* 1508 */       PdfArray array = (PdfArray)obj;
/* 1509 */       for (int i = 0; i < array.size(); i++) {
/* 1510 */         PdfObject o = array.getPdfObject(i);
/* 1511 */         if (o.isIndirect()) {
/* 1512 */           PdfReader reader = ((PRIndirectReference)o).getReader();
/* 1513 */           HashMap<RefKey, IndirectReferences> indirects = this.indirectMap.get(reader);
/* 1514 */           IndirectReferences indRef = indirects.get(new RefKey((PRIndirectReference)o));
/* 1515 */           if (indRef != null) {
/* 1516 */             array.set(i, indRef.getRef());
/*      */           }
/*      */         } else {
/* 1519 */           updateReferences(o);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private PdfArray branchForm(HashMap<String, Object> level, PdfIndirectReference parent, String fname) throws IOException, BadPdfFormatException {
/* 1527 */     PdfArray arr = new PdfArray();
/* 1528 */     for (Map.Entry<String, Object> entry : level.entrySet()) {
/* 1529 */       String name = entry.getKey();
/* 1530 */       Object obj = entry.getValue();
/* 1531 */       PdfIndirectReference ind = getPdfIndirectReference();
/* 1532 */       PdfDictionary dic = new PdfDictionary();
/* 1533 */       if (parent != null)
/* 1534 */         dic.put(PdfName.PARENT, parent); 
/* 1535 */       dic.put(PdfName.T, new PdfString(name, "UnicodeBig"));
/* 1536 */       String fname2 = fname + "." + name;
/* 1537 */       int coidx = this.calculationOrder.indexOf(fname2);
/* 1538 */       if (coidx >= 0)
/* 1539 */         this.calculationOrderRefs.set(coidx, ind); 
/* 1540 */       if (obj instanceof HashMap) {
/* 1541 */         dic.put(PdfName.KIDS, branchForm((HashMap<String, Object>)obj, ind, fname2));
/* 1542 */         arr.add(ind);
/* 1543 */         addToBody(dic, ind, true);
/*      */         continue;
/*      */       } 
/* 1546 */       ArrayList<Object> list = (ArrayList<Object>)obj;
/* 1547 */       dic.mergeDifferent((PdfDictionary)list.get(0));
/* 1548 */       if (list.size() == 3) {
/* 1549 */         dic.mergeDifferent((PdfDictionary)list.get(2));
/* 1550 */         int page = ((Integer)list.get(1)).intValue();
/* 1551 */         PdfArray annots = ((ImportedPage)this.importedPages.get(page - 1)).mergedFields;
/* 1552 */         PdfNumber nn = (PdfNumber)dic.get(iTextTag);
/* 1553 */         dic.remove(iTextTag);
/* 1554 */         dic.put(PdfName.TYPE, PdfName.ANNOT);
/* 1555 */         adjustTabOrder(annots, ind, nn);
/*      */       } else {
/* 1557 */         PdfDictionary field = (PdfDictionary)list.get(0);
/* 1558 */         PdfArray kids = new PdfArray();
/* 1559 */         for (int k = 1; k < list.size(); k += 2) {
/* 1560 */           int page = ((Integer)list.get(k)).intValue();
/* 1561 */           PdfArray annots = ((ImportedPage)this.importedPages.get(page - 1)).mergedFields;
/* 1562 */           PdfDictionary widget = new PdfDictionary();
/* 1563 */           widget.merge((PdfDictionary)list.get(k + 1));
/* 1564 */           widget.put(PdfName.PARENT, ind);
/* 1565 */           PdfNumber nn = (PdfNumber)widget.get(iTextTag);
/* 1566 */           widget.remove(iTextTag);
/* 1567 */           if (isTextField(field)) {
/* 1568 */             PdfString v = field.getAsString(PdfName.V);
/* 1569 */             PdfObject ap = widget.getDirectObject(PdfName.AP);
/* 1570 */             if (v != null && ap != null) {
/* 1571 */               if (!this.mergedTextFields.containsKey(list)) {
/* 1572 */                 this.mergedTextFields.put(list, v);
/*      */               } else {
/*      */                 try {
/* 1575 */                   TextField tx = new TextField(this, null, null);
/* 1576 */                   ((AcroFields)this.fields.get(0)).decodeGenericDictionary(widget, tx);
/* 1577 */                   Rectangle box = PdfReader.getNormalizedRectangle(widget.getAsArray(PdfName.RECT));
/* 1578 */                   if (tx.getRotation() == 90 || tx.getRotation() == 270)
/* 1579 */                     box = box.rotate(); 
/* 1580 */                   tx.setBox(box);
/* 1581 */                   tx.setText(((PdfString)this.mergedTextFields.get(list)).toUnicodeString());
/* 1582 */                   PdfAppearance app = tx.getAppearance();
/* 1583 */                   ((PdfDictionary)ap).put(PdfName.N, app.getIndirectReference());
/* 1584 */                 } catch (DocumentException documentException) {}
/*      */               }
/*      */             
/*      */             }
/*      */           }
/* 1589 */           else if (isCheckButton(field)) {
/* 1590 */             PdfName v = field.getAsName(PdfName.V);
/* 1591 */             PdfName as = widget.getAsName(PdfName.AS);
/* 1592 */             if (v != null && as != null)
/* 1593 */               widget.put(PdfName.AS, v); 
/* 1594 */           } else if (isRadioButton(field)) {
/* 1595 */             PdfName v = field.getAsName(PdfName.V);
/* 1596 */             PdfName as = widget.getAsName(PdfName.AS);
/* 1597 */             if (v != null && as != null && !as.equals(getOffStateName(widget))) {
/* 1598 */               if (!this.mergedRadioButtons.contains(list)) {
/* 1599 */                 this.mergedRadioButtons.add(list);
/* 1600 */                 widget.put(PdfName.AS, v);
/*      */               } else {
/* 1602 */                 widget.put(PdfName.AS, getOffStateName(widget));
/*      */               } 
/*      */             }
/*      */           } 
/* 1606 */           widget.put(PdfName.TYPE, PdfName.ANNOT);
/* 1607 */           PdfIndirectReference wref = addToBody(widget, getPdfIndirectReference(), true).getIndirectReference();
/* 1608 */           adjustTabOrder(annots, wref, nn);
/* 1609 */           kids.add(wref);
/*      */         } 
/* 1611 */         dic.put(PdfName.KIDS, kids);
/*      */       } 
/* 1613 */       arr.add(ind);
/* 1614 */       addToBody(dic, ind, true);
/*      */     } 
/*      */     
/* 1617 */     return arr;
/*      */   }
/*      */   
/*      */   private void adjustTabOrder(PdfArray annots, PdfIndirectReference ind, PdfNumber nn) {
/* 1621 */     int v = nn.intValue();
/* 1622 */     ArrayList<Integer> t = this.tabOrder.get(annots);
/* 1623 */     if (t == null) {
/* 1624 */       t = new ArrayList<Integer>();
/* 1625 */       int size = annots.size() - 1;
/* 1626 */       for (int k = 0; k < size; k++) {
/* 1627 */         t.add(zero);
/*      */       }
/* 1629 */       t.add(Integer.valueOf(v));
/* 1630 */       this.tabOrder.put(annots, t);
/* 1631 */       annots.add(ind);
/*      */     } else {
/*      */       
/* 1634 */       int size = t.size() - 1;
/* 1635 */       for (int k = size; k >= 0; k--) {
/* 1636 */         if (((Integer)t.get(k)).intValue() <= v) {
/* 1637 */           t.add(k + 1, Integer.valueOf(v));
/* 1638 */           annots.add(k + 1, ind);
/* 1639 */           size = -2;
/*      */           break;
/*      */         } 
/*      */       } 
/* 1643 */       if (size != -2) {
/* 1644 */         t.add(0, Integer.valueOf(v));
/* 1645 */         annots.add(0, ind);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected PdfDictionary getCatalog(PdfIndirectReference rootObj) {
/*      */     try {
/* 1657 */       PdfDictionary theCat = this.pdf.getCatalog(rootObj);
/* 1658 */       buildStructTreeRootForTagged(theCat);
/* 1659 */       if (this.fieldArray != null) {
/* 1660 */         addFieldResources(theCat);
/* 1661 */       } else if (this.mergeFields && this.acroForm != null) {
/* 1662 */         theCat.put(PdfName.ACROFORM, this.acroForm);
/*      */       } 
/* 1664 */       return theCat;
/*      */     }
/* 1666 */     catch (IOException e) {
/* 1667 */       throw new ExceptionConverter(e);
/*      */     } 
/*      */   }
/*      */   
/*      */   protected boolean isStructTreeRootReference(PdfIndirectReference prRef) {
/* 1672 */     if (prRef == null || this.structTreeRootReference == null)
/* 1673 */       return false; 
/* 1674 */     return (prRef.number == this.structTreeRootReference.number && prRef.generation == this.structTreeRootReference.generation);
/*      */   }
/*      */   
/*      */   private void addFieldResources(PdfDictionary catalog) throws IOException {
/* 1678 */     if (this.fieldArray == null)
/*      */       return; 
/* 1680 */     PdfDictionary acroForm = new PdfDictionary();
/* 1681 */     catalog.put(PdfName.ACROFORM, acroForm);
/* 1682 */     acroForm.put(PdfName.FIELDS, this.fieldArray);
/* 1683 */     acroForm.put(PdfName.DA, new PdfString("/Helv 0 Tf 0 g "));
/* 1684 */     if (this.fieldTemplates.isEmpty())
/*      */       return; 
/* 1686 */     PdfDictionary dr = new PdfDictionary();
/* 1687 */     acroForm.put(PdfName.DR, dr);
/* 1688 */     for (PdfTemplate template : this.fieldTemplates) {
/* 1689 */       PdfFormField.mergeResources(dr, (PdfDictionary)template.getResources());
/*      */     }
/*      */     
/* 1692 */     PdfDictionary fonts = dr.getAsDict(PdfName.FONT);
/* 1693 */     if (fonts == null) {
/* 1694 */       fonts = new PdfDictionary();
/* 1695 */       dr.put(PdfName.FONT, fonts);
/*      */     } 
/* 1697 */     if (!fonts.contains(PdfName.HELV)) {
/* 1698 */       PdfDictionary dic = new PdfDictionary(PdfName.FONT);
/* 1699 */       dic.put(PdfName.BASEFONT, PdfName.HELVETICA);
/* 1700 */       dic.put(PdfName.ENCODING, PdfName.WIN_ANSI_ENCODING);
/* 1701 */       dic.put(PdfName.NAME, PdfName.HELV);
/* 1702 */       dic.put(PdfName.SUBTYPE, PdfName.TYPE1);
/* 1703 */       fonts.put(PdfName.HELV, addToBody(dic).getIndirectReference());
/*      */     } 
/* 1705 */     if (!fonts.contains(PdfName.ZADB)) {
/* 1706 */       PdfDictionary dic = new PdfDictionary(PdfName.FONT);
/* 1707 */       dic.put(PdfName.BASEFONT, PdfName.ZAPFDINGBATS);
/* 1708 */       dic.put(PdfName.NAME, PdfName.ZADB);
/* 1709 */       dic.put(PdfName.SUBTYPE, PdfName.TYPE1);
/* 1710 */       fonts.put(PdfName.ZADB, addToBody(dic).getIndirectReference());
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void close() {
/* 1726 */     if (this.open) {
/* 1727 */       this.pdf.close();
/* 1728 */       super.close();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PdfIndirectReference add(PdfOutline outline) {
/* 1742 */     return null;
/*      */   }
/*      */   
/*      */   public void addAnnotation(PdfAnnotation annot) {}
/*      */   
/*      */   PdfIndirectReference add(PdfPage page, PdfContents contents) throws PdfException {
/* 1748 */     return null;
/*      */   }
/*      */   
/*      */   public void freeReader(PdfReader reader) throws IOException {
/* 1752 */     if (this.mergeFields)
/* 1753 */       throw new UnsupportedOperationException(MessageLocalization.getComposedMessage("it.is.not.possible.to.free.reader.in.merge.fields.mode", new Object[0])); 
/* 1754 */     PdfArray array = reader.trailer.getAsArray(PdfName.ID);
/* 1755 */     if (array != null)
/* 1756 */       this.originalFileID = array.getAsString(0).getBytes(); 
/* 1757 */     this.indirectMap.remove(reader);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1768 */     this.currentPdfReaderInstance = null;
/*      */ 
/*      */     
/* 1771 */     super.freeReader(reader);
/*      */   }
/*      */   
/*      */   protected PdfName getOffStateName(PdfDictionary widget) {
/* 1775 */     return PdfName.Off;
/*      */   }
/*      */   
/* 1778 */   protected static final HashSet<PdfName> widgetKeys = new HashSet<PdfName>();
/* 1779 */   protected static final HashSet<PdfName> fieldKeys = new HashSet<PdfName>();
/*      */   static {
/* 1781 */     widgetKeys.add(PdfName.SUBTYPE);
/* 1782 */     widgetKeys.add(PdfName.CONTENTS);
/* 1783 */     widgetKeys.add(PdfName.RECT);
/* 1784 */     widgetKeys.add(PdfName.NM);
/* 1785 */     widgetKeys.add(PdfName.M);
/* 1786 */     widgetKeys.add(PdfName.F);
/* 1787 */     widgetKeys.add(PdfName.BS);
/* 1788 */     widgetKeys.add(PdfName.BORDER);
/* 1789 */     widgetKeys.add(PdfName.AP);
/* 1790 */     widgetKeys.add(PdfName.AS);
/* 1791 */     widgetKeys.add(PdfName.C);
/* 1792 */     widgetKeys.add(PdfName.A);
/* 1793 */     widgetKeys.add(PdfName.STRUCTPARENT);
/* 1794 */     widgetKeys.add(PdfName.OC);
/* 1795 */     widgetKeys.add(PdfName.H);
/* 1796 */     widgetKeys.add(PdfName.MK);
/* 1797 */     widgetKeys.add(PdfName.DA);
/* 1798 */     widgetKeys.add(PdfName.Q);
/* 1799 */     widgetKeys.add(PdfName.P);
/* 1800 */     widgetKeys.add(PdfName.TYPE);
/* 1801 */     widgetKeys.add(annotId);
/* 1802 */     fieldKeys.add(PdfName.AA);
/* 1803 */     fieldKeys.add(PdfName.FT);
/* 1804 */     fieldKeys.add(PdfName.TU);
/* 1805 */     fieldKeys.add(PdfName.TM);
/* 1806 */     fieldKeys.add(PdfName.FF);
/* 1807 */     fieldKeys.add(PdfName.V);
/* 1808 */     fieldKeys.add(PdfName.DV);
/* 1809 */     fieldKeys.add(PdfName.DS);
/* 1810 */     fieldKeys.add(PdfName.RV);
/* 1811 */     fieldKeys.add(PdfName.OPT);
/* 1812 */     fieldKeys.add(PdfName.MAXLEN);
/* 1813 */     fieldKeys.add(PdfName.TI);
/* 1814 */     fieldKeys.add(PdfName.I);
/* 1815 */     fieldKeys.add(PdfName.LOCK);
/* 1816 */     fieldKeys.add(PdfName.SV);
/*      */   }
/*      */   
/*      */   static Integer getFlags(PdfDictionary field) {
/* 1820 */     PdfName type = field.getAsName(PdfName.FT);
/* 1821 */     if (!PdfName.BTN.equals(type))
/* 1822 */       return null; 
/* 1823 */     PdfNumber flags = field.getAsNumber(PdfName.FF);
/* 1824 */     if (flags == null)
/* 1825 */       return null; 
/* 1826 */     return Integer.valueOf(flags.intValue());
/*      */   }
/*      */   
/*      */   static boolean isCheckButton(PdfDictionary field) {
/* 1830 */     Integer flags = getFlags(field);
/* 1831 */     return (flags == null || ((flags.intValue() & 0x10000) == 0 && (flags.intValue() & 0x8000) == 0));
/*      */   }
/*      */   
/*      */   static boolean isRadioButton(PdfDictionary field) {
/* 1835 */     Integer flags = getFlags(field);
/* 1836 */     return (flags != null && (flags.intValue() & 0x10000) == 0 && (flags.intValue() & 0x8000) != 0);
/*      */   }
/*      */   
/*      */   static boolean isTextField(PdfDictionary field) {
/* 1840 */     PdfName type = field.getAsName(PdfName.FT);
/* 1841 */     return PdfName.TX.equals(type);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PageStamp createPageStamp(PdfImportedPage iPage) {
/* 1868 */     int pageNum = iPage.getPageNumber();
/* 1869 */     PdfReader reader = iPage.getPdfReaderInstance().getReader();
/* 1870 */     if (isTagged())
/* 1871 */       throw new RuntimeException(MessageLocalization.getComposedMessage("creating.page.stamp.not.allowed.for.tagged.reader", new Object[0])); 
/* 1872 */     PdfDictionary pageN = reader.getPageN(pageNum);
/* 1873 */     return new PageStamp(reader, pageN, this);
/*      */   }
/*      */   
/*      */   public static class PageStamp
/*      */   {
/*      */     PdfDictionary pageN;
/*      */     PdfCopy.StampContent under;
/*      */     PdfCopy.StampContent over;
/*      */     PageResources pageResources;
/*      */     PdfReader reader;
/*      */     PdfCopy cstp;
/*      */     
/*      */     PageStamp(PdfReader reader, PdfDictionary pageN, PdfCopy cstp) {
/* 1886 */       this.pageN = pageN;
/* 1887 */       this.reader = reader;
/* 1888 */       this.cstp = cstp;
/*      */     }
/*      */     
/*      */     public PdfContentByte getUnderContent() {
/* 1892 */       if (this.under == null) {
/* 1893 */         if (this.pageResources == null) {
/* 1894 */           this.pageResources = new PageResources();
/* 1895 */           PdfDictionary resources = this.pageN.getAsDict(PdfName.RESOURCES);
/* 1896 */           this.pageResources.setOriginalResources(resources, this.cstp.namePtr);
/*      */         } 
/* 1898 */         this.under = new PdfCopy.StampContent(this.cstp, this.pageResources);
/*      */       } 
/* 1900 */       return this.under;
/*      */     }
/*      */     
/*      */     public PdfContentByte getOverContent() {
/* 1904 */       if (this.over == null) {
/* 1905 */         if (this.pageResources == null) {
/* 1906 */           this.pageResources = new PageResources();
/* 1907 */           PdfDictionary resources = this.pageN.getAsDict(PdfName.RESOURCES);
/* 1908 */           this.pageResources.setOriginalResources(resources, this.cstp.namePtr);
/*      */         } 
/* 1910 */         this.over = new PdfCopy.StampContent(this.cstp, this.pageResources);
/*      */       } 
/* 1912 */       return this.over;
/*      */     }
/*      */     
/*      */     public void alterContents() throws IOException {
/* 1916 */       if (this.over == null && this.under == null)
/*      */         return; 
/* 1918 */       PdfArray ar = null;
/* 1919 */       PdfObject content = PdfReader.getPdfObject(this.pageN.get(PdfName.CONTENTS), this.pageN);
/* 1920 */       if (content == null) {
/* 1921 */         ar = new PdfArray();
/* 1922 */         this.pageN.put(PdfName.CONTENTS, ar);
/* 1923 */       } else if (content.isArray()) {
/* 1924 */         ar = (PdfArray)content;
/* 1925 */       } else if (content.isStream()) {
/* 1926 */         ar = new PdfArray();
/* 1927 */         ar.add(this.pageN.get(PdfName.CONTENTS));
/* 1928 */         this.pageN.put(PdfName.CONTENTS, ar);
/*      */       } else {
/* 1930 */         ar = new PdfArray();
/* 1931 */         this.pageN.put(PdfName.CONTENTS, ar);
/*      */       } 
/* 1933 */       ByteBuffer out = new ByteBuffer();
/* 1934 */       if (this.under != null) {
/* 1935 */         out.append(PdfContents.SAVESTATE);
/* 1936 */         applyRotation(this.pageN, out);
/* 1937 */         out.append(this.under.getInternalBuffer());
/* 1938 */         out.append(PdfContents.RESTORESTATE);
/*      */       } 
/* 1940 */       if (this.over != null)
/* 1941 */         out.append(PdfContents.SAVESTATE); 
/* 1942 */       PdfStream stream = new PdfStream(out.toByteArray());
/* 1943 */       stream.flateCompress(this.cstp.getCompressionLevel());
/* 1944 */       PdfIndirectReference ref1 = this.cstp.addToBody(stream).getIndirectReference();
/* 1945 */       ar.addFirst(ref1);
/* 1946 */       out.reset();
/* 1947 */       if (this.over != null) {
/* 1948 */         out.append(' ');
/* 1949 */         out.append(PdfContents.RESTORESTATE);
/* 1950 */         out.append(PdfContents.SAVESTATE);
/* 1951 */         applyRotation(this.pageN, out);
/* 1952 */         out.append(this.over.getInternalBuffer());
/* 1953 */         out.append(PdfContents.RESTORESTATE);
/* 1954 */         stream = new PdfStream(out.toByteArray());
/* 1955 */         stream.flateCompress(this.cstp.getCompressionLevel());
/* 1956 */         ar.add(this.cstp.addToBody(stream).getIndirectReference());
/*      */       } 
/* 1958 */       this.pageN.put(PdfName.RESOURCES, this.pageResources.getResources());
/*      */     }
/*      */     
/*      */     void applyRotation(PdfDictionary pageN, ByteBuffer out) {
/* 1962 */       if (!this.cstp.rotateContents)
/*      */         return; 
/* 1964 */       Rectangle page = this.reader.getPageSizeWithRotation(pageN);
/* 1965 */       int rotation = page.getRotation();
/* 1966 */       switch (rotation) {
/*      */         case 90:
/* 1968 */           out.append(PdfContents.ROTATE90);
/* 1969 */           out.append(page.getTop());
/* 1970 */           out.append(' ').append('0').append(PdfContents.ROTATEFINAL);
/*      */           break;
/*      */         case 180:
/* 1973 */           out.append(PdfContents.ROTATE180);
/* 1974 */           out.append(page.getRight());
/* 1975 */           out.append(' ');
/* 1976 */           out.append(page.getTop());
/* 1977 */           out.append(PdfContents.ROTATEFINAL);
/*      */           break;
/*      */         case 270:
/* 1980 */           out.append(PdfContents.ROTATE270);
/* 1981 */           out.append('0').append(' ');
/* 1982 */           out.append(page.getRight());
/* 1983 */           out.append(PdfContents.ROTATEFINAL);
/*      */           break;
/*      */       } 
/*      */     }
/*      */     
/*      */     private void addDocumentField(PdfIndirectReference ref) {
/* 1989 */       if (this.cstp.fieldArray == null)
/* 1990 */         this.cstp.fieldArray = new PdfArray(); 
/* 1991 */       this.cstp.fieldArray.add(ref);
/*      */     }
/*      */     
/*      */     private void expandFields(PdfFormField field, ArrayList<PdfAnnotation> allAnnots) {
/* 1995 */       allAnnots.add(field);
/* 1996 */       ArrayList<PdfFormField> kids = field.getKids();
/* 1997 */       if (kids != null)
/* 1998 */         for (PdfFormField f : kids) {
/* 1999 */           expandFields(f, allAnnots);
/*      */         } 
/*      */     }
/*      */     
/*      */     public void addAnnotation(PdfAnnotation annot) {
/*      */       try {
/* 2005 */         ArrayList<PdfAnnotation> allAnnots = new ArrayList<PdfAnnotation>();
/* 2006 */         if (annot.isForm()) {
/* 2007 */           PdfFormField field = (PdfFormField)annot;
/* 2008 */           if (field.getParent() != null)
/*      */             return; 
/* 2010 */           expandFields(field, allAnnots);
/* 2011 */           if (this.cstp.fieldTemplates == null) {
/* 2012 */             this.cstp.fieldTemplates = new HashSet<PdfTemplate>();
/*      */           }
/*      */         } else {
/* 2015 */           allAnnots.add(annot);
/* 2016 */         }  for (int k = 0; k < allAnnots.size(); k++) {
/* 2017 */           annot = allAnnots.get(k);
/* 2018 */           if (annot.isForm()) {
/* 2019 */             if (!annot.isUsed()) {
/* 2020 */               HashSet<PdfTemplate> templates = annot.getTemplates();
/* 2021 */               if (templates != null)
/* 2022 */                 this.cstp.fieldTemplates.addAll(templates); 
/*      */             } 
/* 2024 */             PdfFormField field = (PdfFormField)annot;
/* 2025 */             if (field.getParent() == null)
/* 2026 */               addDocumentField(field.getIndirectReference()); 
/*      */           } 
/* 2028 */           if (annot.isAnnotation()) {
/* 2029 */             PdfObject pdfobj = PdfReader.getPdfObject(this.pageN.get(PdfName.ANNOTS), this.pageN);
/* 2030 */             PdfArray annots = null;
/* 2031 */             if (pdfobj == null || !pdfobj.isArray()) {
/* 2032 */               annots = new PdfArray();
/* 2033 */               this.pageN.put(PdfName.ANNOTS, annots);
/*      */             } else {
/*      */               
/* 2036 */               annots = (PdfArray)pdfobj;
/* 2037 */             }  annots.add(annot.getIndirectReference());
/* 2038 */             if (!annot.isUsed()) {
/* 2039 */               PdfRectangle rect = (PdfRectangle)annot.get(PdfName.RECT);
/* 2040 */               if (rect != null && (rect.left() != 0.0F || rect.right() != 0.0F || rect.top() != 0.0F || rect.bottom() != 0.0F)) {
/* 2041 */                 int rotation = this.reader.getPageRotation(this.pageN);
/* 2042 */                 Rectangle pageSize = this.reader.getPageSizeWithRotation(this.pageN);
/* 2043 */                 switch (rotation) {
/*      */                   case 90:
/* 2045 */                     annot.put(PdfName.RECT, new PdfRectangle(pageSize
/* 2046 */                           .getTop() - rect.bottom(), rect
/* 2047 */                           .left(), pageSize
/* 2048 */                           .getTop() - rect.top(), rect
/* 2049 */                           .right()));
/*      */                     break;
/*      */                   case 180:
/* 2052 */                     annot.put(PdfName.RECT, new PdfRectangle(pageSize
/* 2053 */                           .getRight() - rect.left(), pageSize
/* 2054 */                           .getTop() - rect.bottom(), pageSize
/* 2055 */                           .getRight() - rect.right(), pageSize
/* 2056 */                           .getTop() - rect.top()));
/*      */                     break;
/*      */                   case 270:
/* 2059 */                     annot.put(PdfName.RECT, new PdfRectangle(rect
/* 2060 */                           .bottom(), pageSize
/* 2061 */                           .getRight() - rect.left(), rect
/* 2062 */                           .top(), pageSize
/* 2063 */                           .getRight() - rect.right()));
/*      */                     break;
/*      */                 } 
/*      */               } 
/*      */             } 
/*      */           } 
/* 2069 */           if (!annot.isUsed()) {
/* 2070 */             annot.setUsed();
/* 2071 */             this.cstp.addToBody(annot, annot.getIndirectReference());
/*      */           }
/*      */         
/*      */         } 
/* 2075 */       } catch (IOException e) {
/* 2076 */         throw new ExceptionConverter(e);
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   public static class StampContent
/*      */     extends PdfContentByte {
/*      */     PageResources pageResources;
/*      */     
/*      */     StampContent(PdfWriter writer, PageResources pageResources) {
/* 2086 */       super(writer);
/* 2087 */       this.pageResources = pageResources;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public PdfContentByte getDuplicate() {
/* 2098 */       return new StampContent(this.writer, this.pageResources);
/*      */     }
/*      */ 
/*      */     
/*      */     PageResources getPageResources() {
/* 2103 */       return this.pageResources;
/*      */     }
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/PdfCopy.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */