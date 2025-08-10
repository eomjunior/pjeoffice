/*      */ package com.itextpdf.text.pdf;
/*      */ 
/*      */ import com.itextpdf.awt.geom.AffineTransform;
/*      */ import com.itextpdf.awt.geom.Point;
/*      */ import com.itextpdf.text.DocumentException;
/*      */ import com.itextpdf.text.ExceptionConverter;
/*      */ import com.itextpdf.text.Image;
/*      */ import com.itextpdf.text.Rectangle;
/*      */ import com.itextpdf.text.Version;
/*      */ import com.itextpdf.text.error_messages.MessageLocalization;
/*      */ import com.itextpdf.text.exceptions.BadPasswordException;
/*      */ import com.itextpdf.text.io.RandomAccessSource;
/*      */ import com.itextpdf.text.io.RandomAccessSourceFactory;
/*      */ import com.itextpdf.text.log.Counter;
/*      */ import com.itextpdf.text.log.CounterFactory;
/*      */ import com.itextpdf.text.log.Logger;
/*      */ import com.itextpdf.text.log.LoggerFactory;
/*      */ import com.itextpdf.text.pdf.collection.PdfCollection;
/*      */ import com.itextpdf.text.pdf.internal.PdfViewerPreferencesImp;
/*      */ import com.itextpdf.text.xml.xmp.PdfProperties;
/*      */ import com.itextpdf.text.xml.xmp.XmpBasicProperties;
/*      */ import com.itextpdf.text.xml.xmp.XmpWriter;
/*      */ import com.itextpdf.xmp.XMPException;
/*      */ import com.itextpdf.xmp.XMPMeta;
/*      */ import com.itextpdf.xmp.XMPMetaFactory;
/*      */ import com.itextpdf.xmp.options.SerializeOptions;
/*      */ import java.io.ByteArrayOutputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.OutputStream;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collections;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
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
/*      */ class PdfStamperImp
/*      */   extends PdfWriter
/*      */ {
/*   87 */   HashMap<PdfReader, IntHashtable> readers2intrefs = new HashMap<PdfReader, IntHashtable>();
/*   88 */   HashMap<PdfReader, RandomAccessFileOrArray> readers2file = new HashMap<PdfReader, RandomAccessFileOrArray>();
/*      */   protected RandomAccessFileOrArray file;
/*      */   PdfReader reader;
/*   91 */   IntHashtable myXref = new IntHashtable();
/*      */ 
/*      */ 
/*      */   
/*   95 */   HashMap<PdfDictionary, PageStamp> pagesToContent = new HashMap<PdfDictionary, PageStamp>();
/*      */   
/*      */   protected boolean closed = false;
/*      */   
/*      */   private boolean rotateContents = true;
/*      */   
/*      */   protected AcroFields acroFields;
/*      */   protected boolean flat = false;
/*      */   protected boolean flatFreeText = false;
/*      */   protected boolean flatannotations = false;
/*  105 */   protected int[] namePtr = new int[] { 0 };
/*  106 */   protected HashSet<String> partialFlattening = new HashSet<String>();
/*      */   protected boolean useVp = false;
/*  108 */   protected PdfViewerPreferencesImp viewerPreferences = new PdfViewerPreferencesImp();
/*  109 */   protected HashSet<PdfTemplate> fieldTemplates = new HashSet<PdfTemplate>();
/*      */   protected boolean fieldsAdded = false;
/*  111 */   protected int sigFlags = 0;
/*      */   protected boolean append;
/*      */   protected IntHashtable marked;
/*      */   protected int initialXrefSize;
/*      */   protected PdfAction openAction;
/*  116 */   protected HashMap<Object, PdfObject> namedDestinations = new HashMap<Object, PdfObject>();
/*      */   
/*  118 */   protected Counter COUNTER = CounterFactory.getCounter(PdfStamper.class);
/*      */   private Logger logger;
/*      */   
/*      */   protected Counter getCounter() {
/*  122 */     return this.COUNTER;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean originalLayersAreRead = false;
/*      */ 
/*      */   
/*  131 */   private HashMap<String, PdfIndirectReference> builtInAnnotationFonts = new HashMap<String, PdfIndirectReference>();
/*  132 */   private static HashMap<String, String> fromShortToFullAnnotationFontNames = new HashMap<String, String>();
/*      */   
/*      */   static {
/*  135 */     fromShortToFullAnnotationFontNames.put("CoBO", "Courier-BoldOblique");
/*  136 */     fromShortToFullAnnotationFontNames.put("CoBo", "Courier-Bold");
/*  137 */     fromShortToFullAnnotationFontNames.put("CoOb", "Courier-Oblique");
/*  138 */     fromShortToFullAnnotationFontNames.put("Cour", "Courier");
/*  139 */     fromShortToFullAnnotationFontNames.put("HeBO", "Helvetica-BoldOblique");
/*  140 */     fromShortToFullAnnotationFontNames.put("HeBo", "Helvetica-Bold");
/*  141 */     fromShortToFullAnnotationFontNames.put("HeOb", "Helvetica-Oblique");
/*  142 */     fromShortToFullAnnotationFontNames.put("Helv", "Helvetica");
/*  143 */     fromShortToFullAnnotationFontNames.put("Symb", "Symbol");
/*  144 */     fromShortToFullAnnotationFontNames.put("TiBI", "Times-BoldItalic");
/*  145 */     fromShortToFullAnnotationFontNames.put("TiBo", "Times-Bold");
/*  146 */     fromShortToFullAnnotationFontNames.put("TiIt", "Times-Italic");
/*  147 */     fromShortToFullAnnotationFontNames.put("TiRo", "Times-Roman");
/*  148 */     fromShortToFullAnnotationFontNames.put("ZaDb", "ZapfDingbats");
/*      */   }
/*      */   
/*  151 */   private double[] DEFAULT_MATRIX = new double[] { 1.0D, 0.0D, 0.0D, 1.0D, 0.0D, 0.0D };
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
/*      */   protected PdfStamperImp(PdfReader reader, OutputStream os, char pdfVersion, boolean append) throws DocumentException, IOException {
/*  165 */     super(new PdfDocument(), os);
/*  166 */     this.logger = LoggerFactory.getLogger(PdfStamperImp.class);
/*  167 */     if (!reader.isOpenedWithFullPermissions())
/*  168 */       throw new BadPasswordException(MessageLocalization.getComposedMessage("pdfreader.not.opened.with.owner.password", new Object[0])); 
/*  169 */     if (reader.isTampered())
/*  170 */       throw new DocumentException(MessageLocalization.getComposedMessage("the.original.document.was.reused.read.it.again.from.file", new Object[0])); 
/*  171 */     reader.setTampered(true);
/*  172 */     this.reader = reader;
/*  173 */     this.file = reader.getSafeFile();
/*  174 */     this.append = append;
/*  175 */     if (reader.isEncrypted() && (append || PdfReader.unethicalreading)) {
/*  176 */       this.crypto = new PdfEncryption(reader.getDecrypt());
/*      */     }
/*  178 */     if (append) {
/*  179 */       if (reader.isRebuilt())
/*  180 */         throw new DocumentException(MessageLocalization.getComposedMessage("append.mode.requires.a.document.without.errors.even.if.recovery.was.possible", new Object[0])); 
/*  181 */       this.pdf_version.setAppendmode(true);
/*  182 */       if (pdfVersion == '\000') {
/*  183 */         this.pdf_version.setPdfVersion(reader.getPdfVersion());
/*      */       } else {
/*  185 */         this.pdf_version.setPdfVersion(pdfVersion);
/*      */       } 
/*  187 */       byte[] buf = new byte[8192];
/*      */       int n;
/*  189 */       while ((n = this.file.read(buf)) > 0)
/*  190 */         this.os.write(buf, 0, n); 
/*  191 */       this.prevxref = reader.getLastXref();
/*  192 */       reader.setAppendable(true);
/*      */     }
/*  194 */     else if (pdfVersion == '\000') {
/*  195 */       setPdfVersion(reader.getPdfVersion());
/*      */     } else {
/*  197 */       setPdfVersion(pdfVersion);
/*      */     } 
/*      */     
/*  200 */     if (reader.isTagged()) {
/*  201 */       setTagged();
/*      */     }
/*      */     
/*  204 */     open();
/*  205 */     this.pdf.addWriter(this);
/*  206 */     if (append) {
/*  207 */       this.body.setRefnum(reader.getXrefSize());
/*  208 */       this.marked = new IntHashtable();
/*  209 */       if (reader.isNewXrefType())
/*  210 */         this.fullCompression = true; 
/*  211 */       if (reader.isHybridXref())
/*  212 */         this.fullCompression = false; 
/*      */     } 
/*  214 */     this.initialXrefSize = reader.getXrefSize();
/*  215 */     readColorProfile();
/*      */   }
/*      */   
/*      */   protected void readColorProfile() {
/*  219 */     PdfObject outputIntents = this.reader.getCatalog().getAsArray(PdfName.OUTPUTINTENTS);
/*  220 */     if (outputIntents != null && ((PdfArray)outputIntents).size() > 0) {
/*  221 */       PdfStream iccProfileStream = null;
/*  222 */       for (int i = 0; i < ((PdfArray)outputIntents).size(); i++) {
/*  223 */         PdfDictionary outputIntentDictionary = ((PdfArray)outputIntents).getAsDict(i);
/*  224 */         if (outputIntentDictionary != null) {
/*  225 */           iccProfileStream = outputIntentDictionary.getAsStream(PdfName.DESTOUTPUTPROFILE);
/*  226 */           if (iccProfileStream != null) {
/*      */             break;
/*      */           }
/*      */         } 
/*      */       } 
/*  231 */       if (iccProfileStream instanceof PRStream) {
/*      */         try {
/*  233 */           this.colorProfile = ICC_Profile.getInstance(PdfReader.getStreamBytes((PRStream)iccProfileStream));
/*  234 */         } catch (IOException exc) {
/*  235 */           throw new ExceptionConverter(exc);
/*      */         } 
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   protected void setViewerPreferences() {
/*  242 */     this.reader.setViewerPreferences(this.viewerPreferences);
/*  243 */     markUsed(this.reader.getTrailer().get(PdfName.ROOT));
/*      */   }
/*      */   
/*      */   protected void close(Map<String, String> moreInfo) throws IOException {
/*  247 */     if (this.closed) {
/*      */       return;
/*      */     }
/*  250 */     if (this.useVp) {
/*  251 */       setViewerPreferences();
/*      */     }
/*  253 */     if (this.flat) {
/*  254 */       flatFields();
/*      */     }
/*  256 */     if (this.flatFreeText) {
/*  257 */       flatFreeTextFields();
/*      */     }
/*  259 */     if (this.flatannotations) {
/*  260 */       flattenAnnotations();
/*      */     }
/*  262 */     addFieldResources();
/*  263 */     PdfDictionary catalog = this.reader.getCatalog();
/*  264 */     getPdfVersion().addToCatalog(catalog);
/*  265 */     PdfDictionary acroForm = (PdfDictionary)PdfReader.getPdfObject(catalog.get(PdfName.ACROFORM), this.reader.getCatalog());
/*  266 */     if (this.acroFields != null && this.acroFields.getXfa().isChanged()) {
/*  267 */       markUsed(acroForm);
/*  268 */       if (!this.flat) {
/*  269 */         this.acroFields.getXfa().setXfa(this);
/*      */       }
/*      */     } 
/*  272 */     if (this.sigFlags != 0 && 
/*  273 */       acroForm != null) {
/*  274 */       acroForm.put(PdfName.SIGFLAGS, new PdfNumber(this.sigFlags));
/*  275 */       markUsed(acroForm);
/*  276 */       markUsed(catalog);
/*      */     } 
/*      */     
/*  279 */     this.closed = true;
/*  280 */     addSharedObjectsToBody();
/*  281 */     setOutlines();
/*  282 */     setJavaScript();
/*  283 */     addFileAttachments();
/*      */     
/*  285 */     if (this.extraCatalog != null) {
/*  286 */       catalog.mergeDifferent(this.extraCatalog);
/*      */     }
/*  288 */     if (this.openAction != null) {
/*  289 */       catalog.put(PdfName.OPENACTION, this.openAction);
/*      */     }
/*  291 */     if (this.pdf.pageLabels != null) {
/*  292 */       catalog.put(PdfName.PAGELABELS, this.pdf.pageLabels.getDictionary(this));
/*      */     }
/*      */     
/*  295 */     if (!this.documentOCG.isEmpty()) {
/*  296 */       fillOCProperties(false);
/*  297 */       PdfDictionary ocdict = catalog.getAsDict(PdfName.OCPROPERTIES);
/*  298 */       if (ocdict == null) {
/*  299 */         this.reader.getCatalog().put(PdfName.OCPROPERTIES, this.OCProperties);
/*      */       } else {
/*  301 */         ocdict.put(PdfName.OCGS, this.OCProperties.get(PdfName.OCGS));
/*  302 */         PdfDictionary ddict = ocdict.getAsDict(PdfName.D);
/*  303 */         if (ddict == null) {
/*  304 */           ddict = new PdfDictionary();
/*  305 */           ocdict.put(PdfName.D, ddict);
/*      */         } 
/*  307 */         ddict.put(PdfName.ORDER, this.OCProperties.getAsDict(PdfName.D).get(PdfName.ORDER));
/*  308 */         ddict.put(PdfName.RBGROUPS, this.OCProperties.getAsDict(PdfName.D).get(PdfName.RBGROUPS));
/*  309 */         ddict.put(PdfName.OFF, this.OCProperties.getAsDict(PdfName.D).get(PdfName.OFF));
/*  310 */         ddict.put(PdfName.AS, this.OCProperties.getAsDict(PdfName.D).get(PdfName.AS));
/*      */       } 
/*  312 */       PdfWriter.checkPdfIsoConformance(this, 7, this.OCProperties);
/*      */     } 
/*      */     
/*  315 */     int skipInfo = -1;
/*  316 */     PdfIndirectReference iInfo = this.reader.getTrailer().getAsIndirectObject(PdfName.INFO);
/*  317 */     if (iInfo != null) {
/*  318 */       skipInfo = iInfo.getNumber();
/*      */     }
/*  320 */     PdfDictionary oldInfo = this.reader.getTrailer().getAsDict(PdfName.INFO);
/*  321 */     String producer = null;
/*  322 */     if (oldInfo != null && oldInfo.get(PdfName.PRODUCER) != null) {
/*  323 */       producer = oldInfo.getAsString(PdfName.PRODUCER).toUnicodeString();
/*      */     }
/*  325 */     Version version = Version.getInstance();
/*  326 */     if (producer == null || version.getVersion().indexOf(version.getProduct()) == -1) {
/*  327 */       producer = version.getVersion();
/*      */     } else {
/*  329 */       StringBuffer buf; int idx = producer.indexOf("; modified using");
/*      */       
/*  331 */       if (idx == -1) {
/*  332 */         buf = new StringBuffer(producer);
/*      */       } else {
/*  334 */         buf = new StringBuffer(producer.substring(0, idx));
/*  335 */       }  buf.append("; modified using ");
/*  336 */       buf.append(version.getVersion());
/*  337 */       producer = buf.toString();
/*      */     } 
/*  339 */     PdfIndirectReference info = null;
/*  340 */     PdfDictionary newInfo = new PdfDictionary();
/*  341 */     if (oldInfo != null) {
/*  342 */       for (PdfName element : oldInfo.getKeys()) {
/*  343 */         PdfName key = element;
/*  344 */         PdfObject value = oldInfo.get(key);
/*  345 */         newInfo.put(key, value);
/*      */       } 
/*      */     }
/*  348 */     if (moreInfo != null) {
/*  349 */       for (Map.Entry<String, String> entry : moreInfo.entrySet()) {
/*  350 */         String key = entry.getKey();
/*  351 */         PdfName keyName = new PdfName(key);
/*  352 */         String value = entry.getValue();
/*  353 */         if (value == null) {
/*  354 */           newInfo.remove(keyName); continue;
/*      */         } 
/*  356 */         newInfo.put(keyName, new PdfString(value, "UnicodeBig"));
/*      */       } 
/*      */     }
/*  359 */     PdfDate date = new PdfDate();
/*  360 */     newInfo.put(PdfName.MODDATE, date);
/*  361 */     newInfo.put(PdfName.PRODUCER, new PdfString(producer, "UnicodeBig"));
/*  362 */     if (this.append) {
/*  363 */       if (iInfo == null) {
/*  364 */         info = addToBody(newInfo, false).getIndirectReference();
/*      */       } else {
/*  366 */         info = addToBody(newInfo, iInfo.getNumber(), false).getIndirectReference();
/*      */       } 
/*      */     } else {
/*  369 */       info = addToBody(newInfo, false).getIndirectReference();
/*      */     } 
/*      */     
/*  372 */     byte[] altMetadata = null;
/*  373 */     PdfObject xmpo = PdfReader.getPdfObject(catalog.get(PdfName.METADATA));
/*  374 */     if (xmpo != null && xmpo.isStream()) {
/*  375 */       altMetadata = PdfReader.getStreamBytesRaw((PRStream)xmpo);
/*  376 */       PdfReader.killIndirect(catalog.get(PdfName.METADATA));
/*      */     } 
/*  378 */     PdfStream xmp = null;
/*  379 */     if (this.xmpMetadata != null) {
/*  380 */       altMetadata = this.xmpMetadata;
/*  381 */     } else if (this.xmpWriter != null) {
/*      */       try {
/*  383 */         ByteArrayOutputStream baos = new ByteArrayOutputStream();
/*  384 */         PdfProperties.setProducer(this.xmpWriter.getXmpMeta(), producer);
/*  385 */         XmpBasicProperties.setModDate(this.xmpWriter.getXmpMeta(), date.getW3CDate());
/*  386 */         XmpBasicProperties.setMetaDataDate(this.xmpWriter.getXmpMeta(), date.getW3CDate());
/*  387 */         this.xmpWriter.serialize(baos);
/*  388 */         this.xmpWriter.close();
/*  389 */         xmp = new PdfStream(baos.toByteArray());
/*  390 */       } catch (XMPException exc) {
/*  391 */         this.xmpWriter = null;
/*      */       } 
/*      */     } 
/*  394 */     if (xmp == null && altMetadata != null) {
/*      */       try {
/*  396 */         ByteArrayOutputStream baos = new ByteArrayOutputStream();
/*  397 */         if (moreInfo == null || this.xmpMetadata != null) {
/*  398 */           XMPMeta xmpMeta = XMPMetaFactory.parseFromBuffer(altMetadata);
/*      */           
/*  400 */           PdfProperties.setProducer(xmpMeta, producer);
/*  401 */           XmpBasicProperties.setModDate(xmpMeta, date.getW3CDate());
/*  402 */           XmpBasicProperties.setMetaDataDate(xmpMeta, date.getW3CDate());
/*      */           
/*  404 */           SerializeOptions serializeOptions = new SerializeOptions();
/*  405 */           serializeOptions.setPadding(2000);
/*  406 */           XMPMetaFactory.serialize(xmpMeta, baos, serializeOptions);
/*      */         } else {
/*  408 */           XmpWriter xmpw = createXmpWriter(baos, newInfo);
/*  409 */           xmpw.close();
/*      */         } 
/*  411 */         xmp = new PdfStream(baos.toByteArray());
/*  412 */       } catch (XMPException e) {
/*  413 */         xmp = new PdfStream(altMetadata);
/*  414 */       } catch (IOException e) {
/*  415 */         xmp = new PdfStream(altMetadata);
/*      */       } 
/*      */     }
/*  418 */     if (xmp != null) {
/*  419 */       xmp.put(PdfName.TYPE, PdfName.METADATA);
/*  420 */       xmp.put(PdfName.SUBTYPE, PdfName.XML);
/*  421 */       if (this.crypto != null && !this.crypto.isMetadataEncrypted()) {
/*  422 */         PdfArray ar = new PdfArray();
/*  423 */         ar.add(PdfName.CRYPT);
/*  424 */         xmp.put(PdfName.FILTER, ar);
/*      */       } 
/*  426 */       if (this.append && xmpo != null) {
/*  427 */         this.body.add(xmp, xmpo.getIndRef());
/*      */       } else {
/*  429 */         catalog.put(PdfName.METADATA, this.body.add(xmp).getIndirectReference());
/*  430 */         markUsed(catalog);
/*      */       } 
/*      */     } 
/*      */     
/*  434 */     if (!this.namedDestinations.isEmpty())
/*  435 */       updateNamedDestinations(); 
/*  436 */     close(info, skipInfo);
/*      */   }
/*      */   
/*      */   protected void close(PdfIndirectReference info, int skipInfo) throws IOException {
/*  440 */     alterContents();
/*  441 */     int rootN = ((PRIndirectReference)this.reader.trailer.get(PdfName.ROOT)).getNumber();
/*  442 */     if (this.append) {
/*  443 */       int[] keys = this.marked.getKeys(); int k;
/*  444 */       for (k = 0; k < keys.length; k++) {
/*  445 */         int j = keys[k];
/*  446 */         PdfObject obj = this.reader.getPdfObjectRelease(j);
/*  447 */         if (obj != null && skipInfo != j && j < this.initialXrefSize) {
/*  448 */           addToBody(obj, obj.getIndRef(), (j != rootN));
/*      */         }
/*      */       } 
/*  451 */       for (k = this.initialXrefSize; k < this.reader.getXrefSize(); k++) {
/*  452 */         PdfObject obj = this.reader.getPdfObject(k);
/*  453 */         if (obj != null) {
/*  454 */           addToBody(obj, getNewObjectNumber(this.reader, k, 0));
/*      */         }
/*      */       } 
/*      */     } else {
/*  458 */       for (int k = 1; k < this.reader.getXrefSize(); k++) {
/*  459 */         PdfObject obj = this.reader.getPdfObjectRelease(k);
/*  460 */         if (obj != null && skipInfo != k) {
/*  461 */           addToBody(obj, getNewObjectNumber(this.reader, k, 0), (k != rootN));
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/*  466 */     PdfIndirectReference encryption = null;
/*  467 */     PdfObject fileID = null;
/*  468 */     if (this.crypto != null) {
/*  469 */       if (this.append) {
/*  470 */         encryption = this.reader.getCryptoRef();
/*      */       } else {
/*  472 */         PdfIndirectObject encryptionObject = addToBody(this.crypto.getEncryptionDictionary(), false);
/*  473 */         encryption = encryptionObject.getIndirectReference();
/*      */       } 
/*  475 */       fileID = this.crypto.getFileID(true);
/*      */     } else {
/*  477 */       PdfArray IDs = this.reader.trailer.getAsArray(PdfName.ID);
/*  478 */       if (IDs != null && IDs.getAsString(0) != null) {
/*  479 */         fileID = PdfEncryption.createInfoId(IDs.getAsString(0).getBytes(), true);
/*      */       } else {
/*  481 */         fileID = PdfEncryption.createInfoId(PdfEncryption.createDocumentId(), true);
/*      */       } 
/*      */     } 
/*  484 */     PRIndirectReference iRoot = (PRIndirectReference)this.reader.trailer.get(PdfName.ROOT);
/*  485 */     PdfIndirectReference root = new PdfIndirectReference(0, getNewObjectNumber(this.reader, iRoot.getNumber(), 0));
/*      */     
/*  487 */     this.body.writeCrossReferenceTable(this.os, root, info, encryption, fileID, this.prevxref);
/*  488 */     if (this.fullCompression) {
/*  489 */       writeKeyInfo(this.os);
/*  490 */       this.os.write(getISOBytes("startxref\n"));
/*  491 */       this.os.write(getISOBytes(String.valueOf(this.body.offset())));
/*  492 */       this.os.write(getISOBytes("\n%%EOF\n"));
/*      */     } else {
/*      */       
/*  495 */       PdfWriter.PdfTrailer trailer = new PdfWriter.PdfTrailer(this.body.size(), this.body.offset(), root, info, encryption, fileID, this.prevxref);
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  500 */       trailer.toPdf(this, this.os);
/*      */     } 
/*  502 */     this.os.flush();
/*  503 */     if (isCloseStream())
/*  504 */       this.os.close(); 
/*  505 */     getCounter().written(this.os.getCounter());
/*      */   }
/*      */   
/*      */   void applyRotation(PdfDictionary pageN, ByteBuffer out) {
/*  509 */     if (!this.rotateContents)
/*      */       return; 
/*  511 */     Rectangle page = this.reader.getPageSizeWithRotation(pageN);
/*  512 */     int rotation = page.getRotation();
/*  513 */     switch (rotation) {
/*      */       case 90:
/*  515 */         out.append(PdfContents.ROTATE90);
/*  516 */         out.append(page.getTop());
/*  517 */         out.append(' ').append('0').append(PdfContents.ROTATEFINAL);
/*      */         break;
/*      */       case 180:
/*  520 */         out.append(PdfContents.ROTATE180);
/*  521 */         out.append(page.getRight());
/*  522 */         out.append(' ');
/*  523 */         out.append(page.getTop());
/*  524 */         out.append(PdfContents.ROTATEFINAL);
/*      */         break;
/*      */       case 270:
/*  527 */         out.append(PdfContents.ROTATE270);
/*  528 */         out.append('0').append(' ');
/*  529 */         out.append(page.getRight());
/*  530 */         out.append(PdfContents.ROTATEFINAL);
/*      */         break;
/*      */     } 
/*      */   }
/*      */   
/*      */   protected void alterContents() throws IOException {
/*  536 */     for (PageStamp element : this.pagesToContent.values()) {
/*  537 */       PageStamp ps = element;
/*  538 */       PdfDictionary pageN = ps.pageN;
/*  539 */       markUsed(pageN);
/*  540 */       PdfArray ar = null;
/*  541 */       PdfObject content = PdfReader.getPdfObject(pageN.get(PdfName.CONTENTS), pageN);
/*  542 */       if (content == null) {
/*  543 */         ar = new PdfArray();
/*  544 */         pageN.put(PdfName.CONTENTS, ar);
/*  545 */       } else if (content.isArray()) {
/*  546 */         ar = new PdfArray((PdfArray)content);
/*  547 */         pageN.put(PdfName.CONTENTS, ar);
/*  548 */       } else if (content.isStream()) {
/*  549 */         ar = new PdfArray();
/*  550 */         ar.add(pageN.get(PdfName.CONTENTS));
/*  551 */         pageN.put(PdfName.CONTENTS, ar);
/*      */       } else {
/*  553 */         ar = new PdfArray();
/*  554 */         pageN.put(PdfName.CONTENTS, ar);
/*      */       } 
/*  556 */       ByteBuffer out = new ByteBuffer();
/*  557 */       if (ps.under != null) {
/*  558 */         out.append(PdfContents.SAVESTATE);
/*  559 */         applyRotation(pageN, out);
/*  560 */         out.append(ps.under.getInternalBuffer());
/*  561 */         out.append(PdfContents.RESTORESTATE);
/*      */       } 
/*  563 */       if (ps.over != null)
/*  564 */         out.append(PdfContents.SAVESTATE); 
/*  565 */       PdfStream stream = new PdfStream(out.toByteArray());
/*  566 */       stream.flateCompress(this.compressionLevel);
/*  567 */       ar.addFirst(addToBody(stream).getIndirectReference());
/*  568 */       out.reset();
/*  569 */       if (ps.over != null) {
/*  570 */         out.append(' ');
/*  571 */         out.append(PdfContents.RESTORESTATE);
/*  572 */         ByteBuffer buf = ps.over.getInternalBuffer();
/*  573 */         out.append(buf.getBuffer(), 0, ps.replacePoint);
/*  574 */         out.append(PdfContents.SAVESTATE);
/*  575 */         applyRotation(pageN, out);
/*  576 */         out.append(buf.getBuffer(), ps.replacePoint, buf.size() - ps.replacePoint);
/*  577 */         out.append(PdfContents.RESTORESTATE);
/*  578 */         stream = new PdfStream(out.toByteArray());
/*  579 */         stream.flateCompress(this.compressionLevel);
/*  580 */         ar.add(addToBody(stream).getIndirectReference());
/*      */       } 
/*  582 */       alterResources(ps);
/*      */     } 
/*      */   }
/*      */   
/*      */   void alterResources(PageStamp ps) {
/*  587 */     ps.pageN.put(PdfName.RESOURCES, ps.pageResources.getResources());
/*      */   }
/*      */ 
/*      */   
/*      */   protected int getNewObjectNumber(PdfReader reader, int number, int generation) {
/*  592 */     IntHashtable ref = this.readers2intrefs.get(reader);
/*  593 */     if (ref != null) {
/*  594 */       int n = ref.get(number);
/*  595 */       if (n == 0) {
/*  596 */         n = getIndirectReferenceNumber();
/*  597 */         ref.put(number, n);
/*      */       } 
/*  599 */       return n;
/*      */     } 
/*  601 */     if (this.currentPdfReaderInstance == null) {
/*  602 */       if (this.append && number < this.initialXrefSize)
/*  603 */         return number; 
/*  604 */       int n = this.myXref.get(number);
/*  605 */       if (n == 0) {
/*  606 */         n = getIndirectReferenceNumber();
/*  607 */         this.myXref.put(number, n);
/*      */       } 
/*  609 */       return n;
/*      */     } 
/*  611 */     return this.currentPdfReaderInstance.getNewObjectNumber(number, generation);
/*      */   }
/*      */ 
/*      */   
/*      */   RandomAccessFileOrArray getReaderFile(PdfReader reader) {
/*  616 */     if (this.readers2intrefs.containsKey(reader)) {
/*  617 */       RandomAccessFileOrArray raf = this.readers2file.get(reader);
/*  618 */       if (raf != null)
/*  619 */         return raf; 
/*  620 */       return reader.getSafeFile();
/*      */     } 
/*  622 */     if (this.currentPdfReaderInstance == null) {
/*  623 */       return this.file;
/*      */     }
/*  625 */     return this.currentPdfReaderInstance.getReaderFile();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void registerReader(PdfReader reader, boolean openFile) throws IOException {
/*  634 */     if (this.readers2intrefs.containsKey(reader))
/*      */       return; 
/*  636 */     this.readers2intrefs.put(reader, new IntHashtable());
/*  637 */     if (openFile) {
/*  638 */       RandomAccessFileOrArray raf = reader.getSafeFile();
/*  639 */       this.readers2file.put(reader, raf);
/*  640 */       raf.reOpen();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void unRegisterReader(PdfReader reader) {
/*  648 */     if (!this.readers2intrefs.containsKey(reader))
/*      */       return; 
/*  650 */     this.readers2intrefs.remove(reader);
/*  651 */     RandomAccessFileOrArray raf = this.readers2file.get(reader);
/*  652 */     if (raf == null)
/*      */       return; 
/*  654 */     this.readers2file.remove(reader);
/*      */     try {
/*  656 */       raf.close();
/*  657 */     } catch (Exception exception) {} } static void findAllObjects(PdfReader reader, PdfObject obj, IntHashtable hits) {
/*      */     PRIndirectReference iref;
/*      */     PdfArray a;
/*      */     int k;
/*      */     PdfDictionary dic;
/*  662 */     if (obj == null)
/*      */       return; 
/*  664 */     switch (obj.type()) {
/*      */       case 10:
/*  666 */         iref = (PRIndirectReference)obj;
/*  667 */         if (reader != iref.getReader())
/*      */           return; 
/*  669 */         if (hits.containsKey(iref.getNumber()))
/*      */           return; 
/*  671 */         hits.put(iref.getNumber(), 1);
/*  672 */         findAllObjects(reader, PdfReader.getPdfObject(obj), hits);
/*      */         return;
/*      */       case 5:
/*  675 */         a = (PdfArray)obj;
/*  676 */         for (k = 0; k < a.size(); k++) {
/*  677 */           findAllObjects(reader, a.getPdfObject(k), hits);
/*      */         }
/*      */         return;
/*      */       case 6:
/*      */       case 7:
/*  682 */         dic = (PdfDictionary)obj;
/*  683 */         for (PdfName element : dic.getKeys()) {
/*  684 */           PdfName name = element;
/*  685 */           findAllObjects(reader, dic.get(name), hits);
/*      */         } 
/*      */         return;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addComments(FdfReader fdf) throws IOException {
/*  696 */     if (this.readers2intrefs.containsKey(fdf))
/*      */       return; 
/*  698 */     PdfDictionary catalog = fdf.getCatalog();
/*  699 */     catalog = catalog.getAsDict(PdfName.FDF);
/*  700 */     if (catalog == null)
/*      */       return; 
/*  702 */     PdfArray annots = catalog.getAsArray(PdfName.ANNOTS);
/*  703 */     if (annots == null || annots.size() == 0)
/*      */       return; 
/*  705 */     registerReader(fdf, false);
/*  706 */     IntHashtable hits = new IntHashtable();
/*  707 */     HashMap<String, PdfObject> irt = new HashMap<String, PdfObject>();
/*  708 */     ArrayList<PdfObject> an = new ArrayList<PdfObject>();
/*  709 */     for (int k = 0; k < annots.size(); k++) {
/*  710 */       PdfObject obj = annots.getPdfObject(k);
/*  711 */       PdfDictionary annot = (PdfDictionary)PdfReader.getPdfObject(obj);
/*  712 */       PdfNumber page = annot.getAsNumber(PdfName.PAGE);
/*  713 */       if (page != null && page.intValue() < this.reader.getNumberOfPages()) {
/*      */         
/*  715 */         findAllObjects(fdf, obj, hits);
/*  716 */         an.add(obj);
/*  717 */         if (obj.type() == 10) {
/*  718 */           PdfObject nm = PdfReader.getPdfObject(annot.get(PdfName.NM));
/*  719 */           if (nm != null && nm.type() == 3)
/*  720 */             irt.put(nm.toString(), obj); 
/*      */         } 
/*      */       } 
/*  723 */     }  int[] arhits = hits.getKeys(); int i;
/*  724 */     for (i = 0; i < arhits.length; i++) {
/*  725 */       int n = arhits[i];
/*  726 */       PdfObject obj = fdf.getPdfObject(n);
/*  727 */       if (obj.type() == 6) {
/*  728 */         PdfObject str = PdfReader.getPdfObject(((PdfDictionary)obj).get(PdfName.IRT));
/*  729 */         if (str != null && str.type() == 3) {
/*  730 */           PdfObject pdfObject = irt.get(str.toString());
/*  731 */           if (pdfObject != null) {
/*  732 */             PdfDictionary dic2 = new PdfDictionary();
/*  733 */             dic2.merge((PdfDictionary)obj);
/*  734 */             dic2.put(PdfName.IRT, pdfObject);
/*  735 */             obj = dic2;
/*      */           } 
/*      */         } 
/*      */       } 
/*  739 */       addToBody(obj, getNewObjectNumber(fdf, n, 0));
/*      */     } 
/*  741 */     for (i = 0; i < an.size(); i++) {
/*  742 */       PdfObject obj = an.get(i);
/*  743 */       PdfDictionary annot = (PdfDictionary)PdfReader.getPdfObject(obj);
/*  744 */       PdfNumber page = annot.getAsNumber(PdfName.PAGE);
/*  745 */       PdfDictionary dic = this.reader.getPageN(page.intValue() + 1);
/*  746 */       PdfArray annotsp = (PdfArray)PdfReader.getPdfObject(dic.get(PdfName.ANNOTS), dic);
/*  747 */       if (annotsp == null) {
/*  748 */         annotsp = new PdfArray();
/*  749 */         dic.put(PdfName.ANNOTS, annotsp);
/*  750 */         markUsed(dic);
/*      */       } 
/*  752 */       markUsed(annotsp);
/*  753 */       annotsp.add(obj);
/*      */     } 
/*      */   }
/*      */   
/*      */   PageStamp getPageStamp(int pageNum) {
/*  758 */     PdfDictionary pageN = this.reader.getPageN(pageNum);
/*  759 */     PageStamp ps = this.pagesToContent.get(pageN);
/*  760 */     if (ps == null) {
/*  761 */       ps = new PageStamp(this, this.reader, pageN);
/*  762 */       this.pagesToContent.put(pageN, ps);
/*      */     } 
/*  764 */     ps.pageN.setIndRef(this.reader.getPageOrigRef(pageNum));
/*  765 */     return ps;
/*      */   }
/*      */   
/*      */   PdfContentByte getUnderContent(int pageNum) {
/*  769 */     if (pageNum < 1 || pageNum > this.reader.getNumberOfPages())
/*  770 */       return null; 
/*  771 */     PageStamp ps = getPageStamp(pageNum);
/*  772 */     if (ps.under == null)
/*  773 */       ps.under = new StampContent(this, ps); 
/*  774 */     return ps.under;
/*      */   }
/*      */   
/*      */   PdfContentByte getOverContent(int pageNum) {
/*  778 */     if (pageNum < 1 || pageNum > this.reader.getNumberOfPages())
/*  779 */       return null; 
/*  780 */     PageStamp ps = getPageStamp(pageNum);
/*  781 */     if (ps.over == null)
/*  782 */       ps.over = new StampContent(this, ps); 
/*  783 */     return ps.over;
/*      */   }
/*      */   
/*      */   void correctAcroFieldPages(int page) {
/*  787 */     if (this.acroFields == null)
/*      */       return; 
/*  789 */     if (page > this.reader.getNumberOfPages())
/*      */       return; 
/*  791 */     Map<String, AcroFields.Item> fields = this.acroFields.getFields();
/*  792 */     for (AcroFields.Item item : fields.values()) {
/*  793 */       for (int k = 0; k < item.size(); k++) {
/*  794 */         int p = item.getPage(k).intValue();
/*  795 */         if (p >= page)
/*  796 */           item.forcePage(k, p + 1); 
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private static void moveRectangle(PdfDictionary dic2, PdfReader r, int pageImported, PdfName key, String name) {
/*  802 */     Rectangle m = r.getBoxSize(pageImported, name);
/*  803 */     if (m == null) {
/*  804 */       dic2.remove(key);
/*      */     } else {
/*  806 */       dic2.put(key, new PdfRectangle(m));
/*      */     } 
/*      */   }
/*      */   void replacePage(PdfReader r, int pageImported, int pageReplaced) {
/*  810 */     PdfDictionary pageN = this.reader.getPageN(pageReplaced);
/*  811 */     if (this.pagesToContent.containsKey(pageN))
/*  812 */       throw new IllegalStateException(MessageLocalization.getComposedMessage("this.page.cannot.be.replaced.new.content.was.already.added", new Object[0])); 
/*  813 */     PdfImportedPage p = getImportedPage(r, pageImported);
/*  814 */     PdfDictionary dic2 = this.reader.getPageNRelease(pageReplaced);
/*  815 */     dic2.remove(PdfName.RESOURCES);
/*  816 */     dic2.remove(PdfName.CONTENTS);
/*  817 */     moveRectangle(dic2, r, pageImported, PdfName.MEDIABOX, "media");
/*  818 */     moveRectangle(dic2, r, pageImported, PdfName.CROPBOX, "crop");
/*  819 */     moveRectangle(dic2, r, pageImported, PdfName.TRIMBOX, "trim");
/*  820 */     moveRectangle(dic2, r, pageImported, PdfName.ARTBOX, "art");
/*  821 */     moveRectangle(dic2, r, pageImported, PdfName.BLEEDBOX, "bleed");
/*  822 */     dic2.put(PdfName.ROTATE, new PdfNumber(r.getPageRotation(pageImported)));
/*  823 */     PdfContentByte cb = getOverContent(pageReplaced);
/*  824 */     cb.addTemplate(p, 0.0F, 0.0F);
/*  825 */     PageStamp ps = this.pagesToContent.get(pageN);
/*  826 */     ps.replacePoint = ps.over.getInternalBuffer().size();
/*      */   } void insertPage(int pageNumber, Rectangle mediabox) {
/*      */     PdfDictionary parent;
/*      */     PRIndirectReference parentRef;
/*  830 */     Rectangle media = new Rectangle(mediabox);
/*  831 */     int rotation = media.getRotation() % 360;
/*  832 */     PdfDictionary page = new PdfDictionary(PdfName.PAGE);
/*  833 */     page.put(PdfName.RESOURCES, new PdfDictionary());
/*  834 */     page.put(PdfName.ROTATE, new PdfNumber(rotation));
/*  835 */     page.put(PdfName.MEDIABOX, new PdfRectangle(media, rotation));
/*  836 */     PRIndirectReference pref = this.reader.addPdfObject(page);
/*      */ 
/*      */     
/*  839 */     if (pageNumber > this.reader.getNumberOfPages()) {
/*  840 */       PdfDictionary lastPage = this.reader.getPageNRelease(this.reader.getNumberOfPages());
/*  841 */       parentRef = (PRIndirectReference)lastPage.get(PdfName.PARENT);
/*  842 */       parentRef = new PRIndirectReference(this.reader, parentRef.getNumber());
/*  843 */       parent = (PdfDictionary)PdfReader.getPdfObject(parentRef);
/*  844 */       PdfArray kids = (PdfArray)PdfReader.getPdfObject(parent.get(PdfName.KIDS), parent);
/*  845 */       kids.add(pref);
/*  846 */       markUsed(kids);
/*  847 */       this.reader.pageRefs.insertPage(pageNumber, pref);
/*      */     } else {
/*  849 */       if (pageNumber < 1)
/*  850 */         pageNumber = 1; 
/*  851 */       PdfDictionary firstPage = this.reader.getPageN(pageNumber);
/*  852 */       PRIndirectReference firstPageRef = this.reader.getPageOrigRef(pageNumber);
/*  853 */       this.reader.releasePage(pageNumber);
/*  854 */       parentRef = (PRIndirectReference)firstPage.get(PdfName.PARENT);
/*  855 */       parentRef = new PRIndirectReference(this.reader, parentRef.getNumber());
/*  856 */       parent = (PdfDictionary)PdfReader.getPdfObject(parentRef);
/*  857 */       PdfArray kids = (PdfArray)PdfReader.getPdfObject(parent.get(PdfName.KIDS), parent);
/*  858 */       int len = kids.size();
/*  859 */       int num = firstPageRef.getNumber();
/*  860 */       for (int k = 0; k < len; k++) {
/*  861 */         PRIndirectReference cur = (PRIndirectReference)kids.getPdfObject(k);
/*  862 */         if (num == cur.getNumber()) {
/*  863 */           kids.add(k, pref);
/*      */           break;
/*      */         } 
/*      */       } 
/*  867 */       if (len == kids.size())
/*  868 */         throw new RuntimeException(MessageLocalization.getComposedMessage("internal.inconsistence", new Object[0])); 
/*  869 */       markUsed(kids);
/*  870 */       this.reader.pageRefs.insertPage(pageNumber, pref);
/*  871 */       correctAcroFieldPages(pageNumber);
/*      */     } 
/*  873 */     page.put(PdfName.PARENT, parentRef);
/*  874 */     while (parent != null) {
/*  875 */       markUsed(parent);
/*  876 */       PdfNumber count = (PdfNumber)PdfReader.getPdfObjectRelease(parent.get(PdfName.COUNT));
/*  877 */       parent.put(PdfName.COUNT, new PdfNumber(count.intValue() + 1));
/*  878 */       parent = parent.getAsDict(PdfName.PARENT);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   boolean isRotateContents() {
/*  888 */     return this.rotateContents;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void setRotateContents(boolean rotateContents) {
/*  897 */     this.rotateContents = rotateContents;
/*      */   }
/*      */   
/*      */   boolean isContentWritten() {
/*  901 */     return (this.body.size() > 1);
/*      */   }
/*      */   
/*      */   AcroFields getAcroFields() {
/*  905 */     if (this.acroFields == null) {
/*  906 */       this.acroFields = new AcroFields(this.reader, this);
/*      */     }
/*  908 */     return this.acroFields;
/*      */   }
/*      */   
/*      */   void setFormFlattening(boolean flat) {
/*  912 */     this.flat = flat;
/*      */   }
/*      */   
/*      */   void setFreeTextFlattening(boolean flat) {
/*  916 */     this.flatFreeText = flat;
/*      */   }
/*      */   
/*      */   boolean partialFormFlattening(String name) {
/*  920 */     getAcroFields();
/*  921 */     if (this.acroFields.getXfa().isXfaPresent())
/*  922 */       throw new UnsupportedOperationException(MessageLocalization.getComposedMessage("partial.form.flattening.is.not.supported.with.xfa.forms", new Object[0])); 
/*  923 */     if (!this.acroFields.getFields().containsKey(name))
/*  924 */       return false; 
/*  925 */     this.partialFlattening.add(name);
/*  926 */     return true;
/*      */   }
/*      */   
/*      */   protected void flatFields() {
/*  930 */     if (this.append)
/*  931 */       throw new IllegalArgumentException(MessageLocalization.getComposedMessage("field.flattening.is.not.supported.in.append.mode", new Object[0])); 
/*  932 */     getAcroFields();
/*  933 */     Map<String, AcroFields.Item> fields = this.acroFields.getFields();
/*  934 */     if (this.fieldsAdded && this.partialFlattening.isEmpty()) {
/*  935 */       for (String s : fields.keySet()) {
/*  936 */         this.partialFlattening.add(s);
/*      */       }
/*      */     }
/*  939 */     PdfDictionary acroForm = this.reader.getCatalog().getAsDict(PdfName.ACROFORM);
/*  940 */     PdfArray acroFds = null;
/*  941 */     if (acroForm != null) {
/*  942 */       acroFds = (PdfArray)PdfReader.getPdfObject(acroForm.get(PdfName.FIELDS), acroForm);
/*      */     }
/*  944 */     for (Map.Entry<String, AcroFields.Item> entry : fields.entrySet()) {
/*  945 */       String name = entry.getKey();
/*  946 */       if (!this.partialFlattening.isEmpty() && !this.partialFlattening.contains(name))
/*      */         continue; 
/*  948 */       AcroFields.Item item = entry.getValue();
/*  949 */       for (int k = 0; k < item.size(); k++) {
/*  950 */         PdfDictionary merged = item.getMerged(k);
/*  951 */         PdfNumber ff = merged.getAsNumber(PdfName.F);
/*  952 */         int flags = 0;
/*  953 */         if (ff != null)
/*  954 */           flags = ff.intValue(); 
/*  955 */         int page = item.getPage(k).intValue();
/*  956 */         if (page >= 1) {
/*      */           
/*  958 */           PdfDictionary appDic = merged.getAsDict(PdfName.AP);
/*  959 */           PdfObject as_n = null;
/*  960 */           if (appDic != null) {
/*  961 */             as_n = appDic.getDirectObject(PdfName.N);
/*  962 */             if (as_n == null)
/*  963 */               as_n = appDic.getAsStream(PdfName.N); 
/*  964 */             if (as_n == null) {
/*  965 */               as_n = appDic.getAsDict(PdfName.N);
/*      */             }
/*      */           } 
/*  968 */           boolean applyRotation = false;
/*  969 */           if (this.acroFields.isGenerateAppearances()) {
/*  970 */             if (appDic == null || as_n == null) {
/*      */               
/*  972 */               try { this.acroFields.regenerateField(name);
/*  973 */                 appDic = this.acroFields.getFieldItem(name).getMerged(k).getAsDict(PdfName.AP);
/*      */                  }
/*      */               
/*  976 */               catch (IOException iOException) {  }
/*  977 */               catch (DocumentException documentException) {}
/*      */             }
/*  979 */             else if (as_n.isStream()) {
/*  980 */               PdfStream stream = (PdfStream)as_n;
/*  981 */               PdfArray bbox = stream.getAsArray(PdfName.BBOX);
/*  982 */               PdfArray rect = merged.getAsArray(PdfName.RECT);
/*  983 */               if (bbox != null && rect != null) {
/*  984 */                 applyRotation = true;
/*  985 */                 float rectWidth = rect.getAsNumber(2).floatValue() - rect.getAsNumber(0).floatValue();
/*  986 */                 float bboxWidth = bbox.getAsNumber(2).floatValue() - bbox.getAsNumber(0).floatValue();
/*  987 */                 float rectHeight = rect.getAsNumber(3).floatValue() - rect.getAsNumber(1).floatValue();
/*  988 */                 float bboxHeight = bbox.getAsNumber(3).floatValue() - bbox.getAsNumber(1).floatValue();
/*      */                 
/*  990 */                 double fieldRotation = 0.0D;
/*  991 */                 if (merged.getAsDict(PdfName.MK) != null && 
/*  992 */                   merged.getAsDict(PdfName.MK).get(PdfName.R) != null) {
/*  993 */                   fieldRotation = merged.getAsDict(PdfName.MK).getAsNumber(PdfName.R).floatValue();
/*      */                 }
/*      */ 
/*      */                 
/*  997 */                 fieldRotation = fieldRotation * Math.PI / 180.0D;
/*      */                 
/*  999 */                 fieldRotation %= 6.283185307179586D;
/*      */                 
/* 1001 */                 if (fieldRotation % Math.PI != 0.0D) {
/* 1002 */                   float temp = rectWidth;
/* 1003 */                   rectWidth = rectHeight;
/* 1004 */                   rectHeight = temp;
/*      */                 } 
/* 1006 */                 float widthCoef = Math.abs((bboxWidth != 0.0F) ? (rectWidth / bboxWidth) : Float.MAX_VALUE);
/* 1007 */                 float heightCoef = Math.abs((bboxHeight != 0.0F) ? (rectHeight / bboxHeight) : Float.MAX_VALUE);
/*      */ 
/*      */                 
/* 1010 */                 NumberArray array = new NumberArray(new float[] { widthCoef, 0.0F, 0.0F, heightCoef, 0.0F, 0.0F });
/* 1011 */                 stream.put(PdfName.MATRIX, array);
/* 1012 */                 markUsed(stream);
/*      */               } 
/*      */             } 
/* 1015 */           } else if (appDic != null && as_n != null && (as_n.isStream() || as_n.isDictionary())) {
/* 1016 */             PdfArray bbox = ((PdfDictionary)as_n).getAsArray(PdfName.BBOX);
/* 1017 */             PdfArray rect = merged.getAsArray(PdfName.RECT);
/* 1018 */             if (bbox != null && rect != null) {
/*      */               
/* 1020 */               float widthDiff = bbox.getAsNumber(2).floatValue() - bbox.getAsNumber(0).floatValue() - rect.getAsNumber(2).floatValue() - rect.getAsNumber(0).floatValue();
/*      */               
/* 1022 */               float heightDiff = bbox.getAsNumber(3).floatValue() - bbox.getAsNumber(1).floatValue() - rect.getAsNumber(3).floatValue() - rect.getAsNumber(1).floatValue();
/* 1023 */               if (Math.abs(widthDiff) > 1.0F || Math.abs(heightDiff) > 1.0F) {
/*      */ 
/*      */                 
/* 1026 */                 try { this.acroFields.setGenerateAppearances(true);
/* 1027 */                   this.acroFields.regenerateField(name);
/* 1028 */                   this.acroFields.setGenerateAppearances(false);
/* 1029 */                   appDic = this.acroFields.getFieldItem(name).getMerged(k).getAsDict(PdfName.AP);
/*      */                    }
/*      */                 
/* 1032 */                 catch (IOException iOException) {  }
/* 1033 */                 catch (DocumentException documentException) {}
/*      */               }
/*      */             } 
/*      */           } 
/*      */ 
/*      */           
/* 1039 */           if (appDic != null && (flags & 0x4) != 0 && (flags & 0x2) == 0) {
/* 1040 */             PdfObject obj = appDic.get(PdfName.N);
/* 1041 */             PdfAppearance app = null;
/* 1042 */             PdfObject objReal = PdfReader.getPdfObject(obj);
/* 1043 */             if (obj != null) {
/* 1044 */               if (obj instanceof PdfIndirectReference && !obj.isIndirect()) {
/* 1045 */                 app = new PdfAppearance((PdfIndirectReference)obj);
/* 1046 */               } else if (objReal instanceof PdfStream) {
/* 1047 */                 ((PdfDictionary)objReal).put(PdfName.SUBTYPE, PdfName.FORM);
/* 1048 */                 app = new PdfAppearance((PdfIndirectReference)obj);
/*      */               }
/* 1050 */               else if (objReal != null && objReal.isDictionary()) {
/* 1051 */                 PdfName as = merged.getAsName(PdfName.AS);
/* 1052 */                 if (as != null) {
/* 1053 */                   PdfIndirectReference iref = (PdfIndirectReference)((PdfDictionary)objReal).get(as);
/* 1054 */                   if (iref != null) {
/* 1055 */                     app = new PdfAppearance(iref);
/* 1056 */                     if (iref.isIndirect()) {
/* 1057 */                       objReal = PdfReader.getPdfObject(iref);
/* 1058 */                       ((PdfDictionary)objReal).put(PdfName.SUBTYPE, PdfName.FORM);
/*      */                     } 
/*      */                   } 
/*      */                 } 
/*      */               } 
/*      */             }
/*      */             
/* 1065 */             if (app != null) {
/* 1066 */               Rectangle box = PdfReader.getNormalizedRectangle(merged.getAsArray(PdfName.RECT));
/* 1067 */               PdfContentByte cb = getOverContent(page);
/* 1068 */               cb.setLiteral("Q ");
/* 1069 */               if (applyRotation) {
/*      */ 
/*      */ 
/*      */                 
/* 1073 */                 AffineTransform tf = new AffineTransform();
/* 1074 */                 double fieldRotation = 0.0D;
/* 1075 */                 if (merged.getAsDict(PdfName.MK) != null && 
/* 1076 */                   merged.getAsDict(PdfName.MK).get(PdfName.R) != null) {
/* 1077 */                   fieldRotation = merged.getAsDict(PdfName.MK).getAsNumber(PdfName.R).floatValue();
/*      */                 }
/*      */ 
/*      */                 
/* 1081 */                 fieldRotation = fieldRotation * Math.PI / 180.0D;
/*      */                 
/* 1083 */                 fieldRotation %= 6.283185307179586D;
/*      */                 
/* 1085 */                 tf = calculateTemplateTransformationMatrix(tf, fieldRotation, box);
/* 1086 */                 cb.addTemplate(app, tf);
/*      */               }
/* 1088 */               else if (objReal instanceof PdfDictionary && ((PdfDictionary)objReal).getAsArray(PdfName.BBOX) != null) {
/* 1089 */                 Rectangle bBox = PdfReader.getNormalizedRectangle(((PdfDictionary)objReal).getAsArray(PdfName.BBOX));
/* 1090 */                 cb.addTemplate(app, box.getWidth() / bBox.getWidth(), 0.0F, 0.0F, box.getHeight() / bBox.getHeight(), box.getLeft(), box.getBottom());
/*      */               } else {
/* 1092 */                 cb.addTemplate(app, box.getLeft(), box.getBottom());
/*      */               } 
/*      */               
/* 1095 */               cb.setLiteral("q ");
/*      */             } 
/*      */           } 
/* 1098 */           if (!this.partialFlattening.isEmpty()) {
/*      */             
/* 1100 */             PdfDictionary pageDic = this.reader.getPageN(page);
/* 1101 */             PdfArray annots = pageDic.getAsArray(PdfName.ANNOTS);
/* 1102 */             if (annots != null)
/*      */             
/* 1104 */             { for (int idx = 0; idx < annots.size(); idx++) {
/* 1105 */                 PdfObject ran = annots.getPdfObject(idx);
/* 1106 */                 if (ran.isIndirect()) {
/*      */                   
/* 1108 */                   PdfObject ran2 = item.getWidgetRef(k);
/* 1109 */                   if (ran2.isIndirect())
/*      */                   {
/* 1111 */                     if (((PRIndirectReference)ran).getNumber() == ((PRIndirectReference)ran2).getNumber()) {
/* 1112 */                       annots.remove(idx--);
/* 1113 */                       PRIndirectReference wdref = (PRIndirectReference)ran2;
/*      */                       while (true) {
/* 1115 */                         PdfDictionary wd = (PdfDictionary)PdfReader.getPdfObject(wdref);
/* 1116 */                         PRIndirectReference parentRef = (PRIndirectReference)wd.get(PdfName.PARENT);
/* 1117 */                         PdfReader.killIndirect(wdref);
/* 1118 */                         if (parentRef == null) {
/* 1119 */                           for (int i = 0; i < acroFds.size(); i++) {
/* 1120 */                             PdfObject h = acroFds.getPdfObject(i);
/* 1121 */                             if (h.isIndirect() && ((PRIndirectReference)h).getNumber() == wdref.getNumber()) {
/* 1122 */                               acroFds.remove(i);
/* 1123 */                               i--;
/*      */                             } 
/*      */                           } 
/*      */                           break;
/*      */                         } 
/* 1128 */                         PdfDictionary parent = (PdfDictionary)PdfReader.getPdfObject(parentRef);
/* 1129 */                         PdfArray kids = parent.getAsArray(PdfName.KIDS);
/* 1130 */                         for (int fr = 0; fr < kids.size(); fr++) {
/* 1131 */                           PdfObject h = kids.getPdfObject(fr);
/* 1132 */                           if (h.isIndirect() && ((PRIndirectReference)h).getNumber() == wdref.getNumber()) {
/* 1133 */                             kids.remove(fr);
/* 1134 */                             fr--;
/*      */                           } 
/*      */                         } 
/* 1137 */                         if (!kids.isEmpty())
/*      */                           break; 
/* 1139 */                         wdref = parentRef;
/*      */                       } 
/*      */                     }  } 
/*      */                 } 
/* 1143 */               }  if (annots.isEmpty())
/* 1144 */               { PdfReader.killIndirect(pageDic.get(PdfName.ANNOTS));
/* 1145 */                 pageDic.remove(PdfName.ANNOTS); }  } 
/*      */           } 
/*      */         } 
/*      */       } 
/* 1149 */     }  if (!this.fieldsAdded && this.partialFlattening.isEmpty()) {
/* 1150 */       for (int page = 1; page <= this.reader.getNumberOfPages(); page++) {
/* 1151 */         PdfDictionary pageDic = this.reader.getPageN(page);
/* 1152 */         PdfArray annots = pageDic.getAsArray(PdfName.ANNOTS);
/* 1153 */         if (annots != null) {
/*      */           
/* 1155 */           for (int idx = 0; idx < annots.size(); idx++) {
/* 1156 */             PdfObject annoto = annots.getDirectObject(idx);
/* 1157 */             if (!(annoto instanceof PdfIndirectReference) || annoto.isIndirect())
/*      */             {
/* 1159 */               if (!annoto.isDictionary() || PdfName.WIDGET.equals(((PdfDictionary)annoto).get(PdfName.SUBTYPE))) {
/* 1160 */                 annots.remove(idx);
/* 1161 */                 idx--;
/*      */               }  } 
/*      */           } 
/* 1164 */           if (annots.isEmpty()) {
/* 1165 */             PdfReader.killIndirect(pageDic.get(PdfName.ANNOTS));
/* 1166 */             pageDic.remove(PdfName.ANNOTS);
/*      */           } 
/*      */         } 
/* 1169 */       }  eliminateAcroformObjects();
/*      */     } 
/*      */   }
/*      */   
/*      */   void eliminateAcroformObjects() {
/* 1174 */     PdfObject acro = this.reader.getCatalog().get(PdfName.ACROFORM);
/* 1175 */     if (acro == null)
/*      */       return; 
/* 1177 */     PdfDictionary acrodic = (PdfDictionary)PdfReader.getPdfObject(acro);
/* 1178 */     this.reader.killXref(acrodic.get(PdfName.XFA));
/* 1179 */     acrodic.remove(PdfName.XFA);
/* 1180 */     PdfObject iFields = acrodic.get(PdfName.FIELDS);
/* 1181 */     if (iFields != null) {
/* 1182 */       PdfDictionary kids = new PdfDictionary();
/* 1183 */       kids.put(PdfName.KIDS, iFields);
/* 1184 */       sweepKids(kids);
/* 1185 */       PdfReader.killIndirect(iFields);
/* 1186 */       acrodic.put(PdfName.FIELDS, new PdfArray());
/*      */     } 
/* 1188 */     acrodic.remove(PdfName.SIGFLAGS);
/* 1189 */     acrodic.remove(PdfName.NEEDAPPEARANCES);
/* 1190 */     acrodic.remove(PdfName.DR);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private AffineTransform calculateTemplateTransformationMatrix(AffineTransform currentMatrix, double fieldRotation, Rectangle box) {
/* 1196 */     AffineTransform templateTransform = new AffineTransform(currentMatrix);
/*      */     
/* 1198 */     double x = box.getLeft();
/* 1199 */     double y = box.getBottom();
/* 1200 */     if (fieldRotation % 1.5707963267948966D == 0.0D && fieldRotation % 4.71238898038469D != 0.0D && fieldRotation != 0.0D) {
/* 1201 */       x += box.getWidth();
/*      */     }
/* 1203 */     if ((fieldRotation % 4.71238898038469D == 0.0D || fieldRotation % Math.PI == 0.0D) && fieldRotation != 0.0D) {
/* 1204 */       y += box.getHeight();
/*      */     }
/* 1206 */     templateTransform.translate(x, y);
/*      */     
/* 1208 */     templateTransform.rotate(fieldRotation);
/* 1209 */     return templateTransform;
/*      */   }
/*      */   
/*      */   void sweepKids(PdfObject obj) {
/* 1213 */     PdfObject oo = PdfReader.killIndirect(obj);
/* 1214 */     if (oo == null || !oo.isDictionary())
/*      */       return; 
/* 1216 */     PdfDictionary dic = (PdfDictionary)oo;
/* 1217 */     PdfArray kids = (PdfArray)PdfReader.killIndirect(dic.get(PdfName.KIDS));
/* 1218 */     if (kids == null)
/*      */       return; 
/* 1220 */     for (int k = 0; k < kids.size(); k++) {
/* 1221 */       sweepKids(kids.getPdfObject(k));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setFlatAnnotations(boolean flatAnnotations) {
/* 1232 */     this.flatannotations = flatAnnotations;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void flattenAnnotations() {
/* 1241 */     flattenAnnotations(false);
/*      */   }
/*      */   
/*      */   private void flattenAnnotations(boolean flattenFreeTextAnnotations) {
/* 1245 */     if (this.append) {
/* 1246 */       if (flattenFreeTextAnnotations) {
/* 1247 */         throw new IllegalArgumentException(MessageLocalization.getComposedMessage("freetext.flattening.is.not.supported.in.append.mode", new Object[0]));
/*      */       }
/* 1249 */       throw new IllegalArgumentException(MessageLocalization.getComposedMessage("annotation.flattening.is.not.supported.in.append.mode", new Object[0]));
/*      */     } 
/*      */ 
/*      */     
/* 1253 */     for (int page = 1; page <= this.reader.getNumberOfPages(); page++) {
/* 1254 */       PdfDictionary pageDic = this.reader.getPageN(page);
/* 1255 */       PdfArray annots = pageDic.getAsArray(PdfName.ANNOTS);
/*      */       
/* 1257 */       if (annots != null) {
/*      */ 
/*      */ 
/*      */         
/* 1261 */         for (int idx = 0; idx < annots.size(); idx++) {
/* 1262 */           PdfObject annoto = annots.getDirectObject(idx);
/* 1263 */           if (!(annoto instanceof PdfIndirectReference) || annoto.isIndirect())
/*      */           {
/* 1265 */             if (annoto instanceof PdfDictionary) {
/*      */               
/* 1267 */               PdfDictionary annDic = (PdfDictionary)annoto;
/* 1268 */               PdfObject subType = annDic.get(PdfName.SUBTYPE);
/* 1269 */               if (flattenFreeTextAnnotations ? 
/* 1270 */                 !PdfName.FREETEXT.equals(subType) : 
/*      */ 
/*      */ 
/*      */                 
/* 1274 */                 PdfName.WIDGET.equals(subType)) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */                 
/* 1280 */                 PdfNumber ff = annDic.getAsNumber(PdfName.F);
/* 1281 */                 int flags = (ff != null) ? ff.intValue() : 0;
/*      */                 
/* 1283 */                 if ((flags & 0x4) != 0 && (flags & 0x2) == 0) {
/* 1284 */                   PdfObject obj1 = annDic.get(PdfName.AP);
/* 1285 */                   if (obj1 != null) {
/*      */ 
/*      */                     
/* 1288 */                     PdfDictionary appDic = (obj1 instanceof PdfIndirectReference) ? (PdfDictionary)PdfReader.getPdfObject(obj1) : (PdfDictionary)obj1;
/* 1289 */                     PdfObject obj = appDic.get(PdfName.N);
/* 1290 */                     PdfDictionary objDict = appDic.getAsStream(PdfName.N);
/* 1291 */                     PdfAppearance app = null;
/* 1292 */                     PdfObject objReal = PdfReader.getPdfObject(obj);
/*      */                     
/* 1294 */                     if (obj instanceof PdfIndirectReference && !obj.isIndirect()) {
/* 1295 */                       app = new PdfAppearance((PdfIndirectReference)obj);
/* 1296 */                     } else if (objReal instanceof PdfStream) {
/* 1297 */                       ((PdfDictionary)objReal).put(PdfName.SUBTYPE, PdfName.FORM);
/* 1298 */                       app = new PdfAppearance((PdfIndirectReference)obj);
/*      */                     }
/* 1300 */                     else if (objReal != null) {
/* 1301 */                       if (objReal.isDictionary()) {
/* 1302 */                         PdfName as_p = appDic.getAsName(PdfName.AS);
/* 1303 */                         if (as_p != null) {
/* 1304 */                           PdfIndirectReference iref = (PdfIndirectReference)((PdfDictionary)objReal).get(as_p);
/* 1305 */                           if (iref != null) {
/* 1306 */                             app = new PdfAppearance(iref);
/* 1307 */                             if (iref.isIndirect()) {
/* 1308 */                               objReal = PdfReader.getPdfObject(iref);
/* 1309 */                               ((PdfDictionary)objReal).put(PdfName.SUBTYPE, PdfName.FORM);
/*      */                             }
/*      */                           
/*      */                           } 
/*      */                         } 
/*      */                       } 
/* 1315 */                     } else if (PdfName.FREETEXT.equals(subType)) {
/* 1316 */                       PdfString defaultAppearancePdfString = annDic.getAsString(PdfName.DA);
/* 1317 */                       if (defaultAppearancePdfString != null) {
/* 1318 */                         PdfString freeTextContent = annDic.getAsString(PdfName.CONTENTS);
/* 1319 */                         String defaultAppearanceString = defaultAppearancePdfString.toString();
/*      */ 
/*      */ 
/*      */                         
/* 1323 */                         PdfIndirectReference fontReference = null;
/* 1324 */                         PdfName pdfFontName = null;
/*      */                         try {
/* 1326 */                           RandomAccessSource source = (new RandomAccessSourceFactory()).createSource(defaultAppearancePdfString.getBytes());
/* 1327 */                           PdfContentParser ps = new PdfContentParser(new PRTokeniser(new RandomAccessFileOrArray(source)));
/* 1328 */                           ArrayList<PdfObject> operands = new ArrayList<PdfObject>();
/* 1329 */                           while (ps.parse(operands).size() > 0) {
/* 1330 */                             PdfLiteral operator = (PdfLiteral)operands.get(operands.size() - 1);
/* 1331 */                             if (operator.toString().equals("Tf")) {
/* 1332 */                               pdfFontName = (PdfName)operands.get(0);
/* 1333 */                               String fontName = pdfFontName.toString().substring(1);
/* 1334 */                               String fullName = fromShortToFullAnnotationFontNames.get(fontName);
/* 1335 */                               if (fullName == null) {
/* 1336 */                                 fullName = fontName;
/*      */                               }
/* 1338 */                               fontReference = this.builtInAnnotationFonts.get(fullName);
/* 1339 */                               if (fontReference == null) {
/* 1340 */                                 PdfDictionary dic = BaseFont.createBuiltInFontDictionary(fullName);
/* 1341 */                                 if (dic != null) {
/* 1342 */                                   fontReference = addToBody(dic).getIndirectReference();
/* 1343 */                                   this.builtInAnnotationFonts.put(fullName, fontReference);
/*      */                                 } 
/*      */                               } 
/*      */                             } 
/*      */                           } 
/* 1348 */                         } catch (Exception any) {
/* 1349 */                           this.logger.warn(MessageLocalization.getComposedMessage("error.resolving.freetext.font", new Object[0]));
/*      */                           
/*      */                           break;
/*      */                         } 
/* 1353 */                         app = new PdfAppearance(this);
/*      */ 
/*      */                         
/* 1356 */                         if (fontReference != null) {
/* 1357 */                           app.getPageResources().addFont(pdfFontName, fontReference);
/*      */                         }
/* 1359 */                         app.saveState();
/* 1360 */                         app.beginText();
/* 1361 */                         app.setLiteral(defaultAppearanceString);
/* 1362 */                         app.setLiteral("(" + freeTextContent.toString() + ") Tj\n");
/* 1363 */                         app.endText();
/* 1364 */                         app.restoreState();
/*      */                       }
/*      */                       else {
/*      */                         
/* 1368 */                         this.logger.warn(MessageLocalization.getComposedMessage("freetext.annotation.doesnt.contain.da", new Object[0]));
/*      */                       } 
/*      */                     } else {
/* 1371 */                       this.logger.warn(MessageLocalization.getComposedMessage("annotation.type.not.supported.flattening", new Object[0]));
/*      */                     } 
/*      */ 
/*      */                     
/* 1375 */                     if (app != null)
/* 1376 */                     { Rectangle rect = PdfReader.getNormalizedRectangle(annDic.getAsArray(PdfName.RECT));
/* 1377 */                       Rectangle bBox = null;
/*      */                       
/* 1379 */                       if (objDict != null) {
/* 1380 */                         bBox = PdfReader.getNormalizedRectangle(objDict.getAsArray(PdfName.BBOX));
/*      */                       } else {
/* 1382 */                         bBox = new Rectangle(0.0F, 0.0F, rect.getWidth(), rect.getHeight());
/* 1383 */                         app.setBoundingBox(bBox);
/*      */                       } 
/*      */                       
/* 1386 */                       PdfContentByte cb = getOverContent(page);
/* 1387 */                       cb.setLiteral("Q ");
/* 1388 */                       if (objDict != null && objDict.getAsArray(PdfName.MATRIX) != null && 
/* 1389 */                         !Arrays.equals(this.DEFAULT_MATRIX, objDict.getAsArray(PdfName.MATRIX).asDoubleArray())) {
/* 1390 */                         double[] matrix = objDict.getAsArray(PdfName.MATRIX).asDoubleArray();
/* 1391 */                         Rectangle transformBBox = transformBBoxByMatrix(bBox, matrix);
/* 1392 */                         cb.addTemplate(app, rect.getWidth() / transformBBox.getWidth(), 0.0F, 0.0F, rect.getHeight() / transformBBox.getHeight(), rect.getLeft(), rect.getBottom());
/*      */                       } else {
/*      */                         
/* 1395 */                         float heightCorrection = -bBox.getBottom();
/* 1396 */                         float widthCorrection = -bBox.getLeft();
/*      */ 
/*      */                         
/* 1399 */                         cb.addTemplate(app, rect.getWidth() / bBox.getWidth(), 0.0F, 0.0F, rect.getHeight() / bBox.getHeight(), rect.getLeft() + widthCorrection, rect.getBottom() + heightCorrection);
/*      */                       } 
/* 1401 */                       cb.setLiteral("q ");
/*      */                       
/* 1403 */                       annots.remove(idx);
/* 1404 */                       idx--; } 
/*      */                   } 
/*      */                 } 
/*      */               } 
/*      */             }  } 
/* 1409 */         }  if (annots.isEmpty()) {
/* 1410 */           PdfReader.killIndirect(pageDic.get(PdfName.ANNOTS));
/* 1411 */           pageDic.remove(PdfName.ANNOTS);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Rectangle transformBBoxByMatrix(Rectangle bBox, double[] matrix) {
/* 1422 */     List<Double> xArr = new ArrayList();
/* 1423 */     List<Double> yArr = new ArrayList();
/* 1424 */     Point p1 = transformPoint(bBox.getLeft(), bBox.getBottom(), matrix);
/* 1425 */     xArr.add(Double.valueOf(p1.x));
/* 1426 */     yArr.add(Double.valueOf(p1.y));
/* 1427 */     Point p2 = transformPoint(bBox.getRight(), bBox.getTop(), matrix);
/* 1428 */     xArr.add(Double.valueOf(p2.x));
/* 1429 */     yArr.add(Double.valueOf(p2.y));
/* 1430 */     Point p3 = transformPoint(bBox.getLeft(), bBox.getTop(), matrix);
/* 1431 */     xArr.add(Double.valueOf(p3.x));
/* 1432 */     yArr.add(Double.valueOf(p3.y));
/* 1433 */     Point p4 = transformPoint(bBox.getRight(), bBox.getBottom(), matrix);
/* 1434 */     xArr.add(Double.valueOf(p4.x));
/* 1435 */     yArr.add(Double.valueOf(p4.y));
/*      */     
/* 1437 */     return new Rectangle(((Double)Collections.<Double>min(xArr)).floatValue(), (
/* 1438 */         (Double)Collections.<Double>min(yArr)).floatValue(), (
/* 1439 */         (Double)Collections.<Double>max(xArr)).floatValue(), (
/* 1440 */         (Double)Collections.<Double>max(yArr)).floatValue());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Point transformPoint(double x, double y, double[] matrix) {
/* 1450 */     Point point = new Point();
/* 1451 */     point.x = matrix[0] * x + matrix[2] * y + matrix[4];
/* 1452 */     point.y = matrix[1] * x + matrix[3] * y + matrix[5];
/* 1453 */     return point;
/*      */   }
/*      */   
/*      */   protected void flatFreeTextFields() {
/* 1457 */     flattenAnnotations(true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PdfIndirectReference getPageReference(int page) {
/* 1465 */     PdfIndirectReference ref = this.reader.getPageOrigRef(page);
/* 1466 */     if (ref == null)
/* 1467 */       throw new IllegalArgumentException(MessageLocalization.getComposedMessage("invalid.page.number.1", page)); 
/* 1468 */     return ref;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addAnnotation(PdfAnnotation annot) {
/* 1476 */     throw new RuntimeException(MessageLocalization.getComposedMessage("unsupported.in.this.context.use.pdfstamper.addannotation", new Object[0]));
/*      */   }
/*      */   
/*      */   void addDocumentField(PdfIndirectReference ref) {
/* 1480 */     PdfDictionary catalog = this.reader.getCatalog();
/* 1481 */     PdfDictionary acroForm = (PdfDictionary)PdfReader.getPdfObject(catalog.get(PdfName.ACROFORM), catalog);
/* 1482 */     if (acroForm == null) {
/* 1483 */       acroForm = new PdfDictionary();
/* 1484 */       catalog.put(PdfName.ACROFORM, acroForm);
/* 1485 */       markUsed(catalog);
/*      */     } 
/* 1487 */     PdfArray fields = (PdfArray)PdfReader.getPdfObject(acroForm.get(PdfName.FIELDS), acroForm);
/* 1488 */     if (fields == null) {
/* 1489 */       fields = new PdfArray();
/* 1490 */       acroForm.put(PdfName.FIELDS, fields);
/* 1491 */       markUsed(acroForm);
/*      */     } 
/* 1493 */     if (!acroForm.contains(PdfName.DA)) {
/* 1494 */       acroForm.put(PdfName.DA, new PdfString("/Helv 0 Tf 0 g "));
/* 1495 */       markUsed(acroForm);
/*      */     } 
/* 1497 */     fields.add(ref);
/* 1498 */     markUsed(fields);
/*      */   }
/*      */   
/*      */   protected void addFieldResources() throws IOException {
/* 1502 */     if (this.fieldTemplates.isEmpty())
/*      */       return; 
/* 1504 */     PdfDictionary catalog = this.reader.getCatalog();
/* 1505 */     PdfDictionary acroForm = (PdfDictionary)PdfReader.getPdfObject(catalog.get(PdfName.ACROFORM), catalog);
/* 1506 */     if (acroForm == null) {
/* 1507 */       acroForm = new PdfDictionary();
/* 1508 */       catalog.put(PdfName.ACROFORM, acroForm);
/* 1509 */       markUsed(catalog);
/*      */     } 
/* 1511 */     PdfDictionary dr = (PdfDictionary)PdfReader.getPdfObject(acroForm.get(PdfName.DR), acroForm);
/* 1512 */     if (dr == null) {
/* 1513 */       dr = new PdfDictionary();
/* 1514 */       acroForm.put(PdfName.DR, dr);
/* 1515 */       markUsed(acroForm);
/*      */     } 
/* 1517 */     markUsed(dr);
/* 1518 */     for (PdfTemplate template : this.fieldTemplates) {
/* 1519 */       PdfFormField.mergeResources(dr, (PdfDictionary)template.getResources(), this);
/*      */     }
/*      */     
/* 1522 */     PdfDictionary fonts = dr.getAsDict(PdfName.FONT);
/* 1523 */     if (fonts == null) {
/* 1524 */       fonts = new PdfDictionary();
/* 1525 */       dr.put(PdfName.FONT, fonts);
/*      */     } 
/* 1527 */     if (!fonts.contains(PdfName.HELV)) {
/* 1528 */       PdfDictionary dic = new PdfDictionary(PdfName.FONT);
/* 1529 */       dic.put(PdfName.BASEFONT, PdfName.HELVETICA);
/* 1530 */       dic.put(PdfName.ENCODING, PdfName.WIN_ANSI_ENCODING);
/* 1531 */       dic.put(PdfName.NAME, PdfName.HELV);
/* 1532 */       dic.put(PdfName.SUBTYPE, PdfName.TYPE1);
/* 1533 */       fonts.put(PdfName.HELV, addToBody(dic).getIndirectReference());
/*      */     } 
/* 1535 */     if (!fonts.contains(PdfName.ZADB)) {
/* 1536 */       PdfDictionary dic = new PdfDictionary(PdfName.FONT);
/* 1537 */       dic.put(PdfName.BASEFONT, PdfName.ZAPFDINGBATS);
/* 1538 */       dic.put(PdfName.NAME, PdfName.ZADB);
/* 1539 */       dic.put(PdfName.SUBTYPE, PdfName.TYPE1);
/* 1540 */       fonts.put(PdfName.ZADB, addToBody(dic).getIndirectReference());
/*      */     } 
/* 1542 */     if (acroForm.get(PdfName.DA) == null) {
/* 1543 */       acroForm.put(PdfName.DA, new PdfString("/Helv 0 Tf 0 g "));
/* 1544 */       markUsed(acroForm);
/*      */     } 
/*      */   }
/*      */   
/*      */   void expandFields(PdfFormField field, ArrayList<PdfAnnotation> allAnnots) {
/* 1549 */     allAnnots.add(field);
/* 1550 */     ArrayList<PdfFormField> kids = field.getKids();
/* 1551 */     if (kids != null)
/* 1552 */       for (int k = 0; k < kids.size(); k++) {
/* 1553 */         expandFields(kids.get(k), allAnnots);
/*      */       } 
/*      */   }
/*      */   
/*      */   void addAnnotation(PdfAnnotation annot, PdfDictionary pageN) {
/*      */     try {
/* 1559 */       ArrayList<PdfAnnotation> allAnnots = new ArrayList<PdfAnnotation>();
/* 1560 */       if (annot.isForm()) {
/* 1561 */         this.fieldsAdded = true;
/* 1562 */         getAcroFields();
/* 1563 */         PdfFormField field = (PdfFormField)annot;
/* 1564 */         if (field.getParent() != null)
/*      */           return; 
/* 1566 */         expandFields(field, allAnnots);
/*      */       } else {
/* 1568 */         allAnnots.add(annot);
/* 1569 */       }  for (int k = 0; k < allAnnots.size(); k++) {
/* 1570 */         annot = allAnnots.get(k);
/* 1571 */         if (annot.getPlaceInPage() > 0)
/* 1572 */           pageN = this.reader.getPageN(annot.getPlaceInPage()); 
/* 1573 */         if (annot.isForm()) {
/* 1574 */           if (!annot.isUsed()) {
/* 1575 */             HashSet<PdfTemplate> templates = annot.getTemplates();
/* 1576 */             if (templates != null)
/* 1577 */               this.fieldTemplates.addAll(templates); 
/*      */           } 
/* 1579 */           PdfFormField field = (PdfFormField)annot;
/* 1580 */           if (field.getParent() == null)
/* 1581 */             addDocumentField(field.getIndirectReference()); 
/*      */         } 
/* 1583 */         if (annot.isAnnotation()) {
/* 1584 */           PdfObject pdfobj = PdfReader.getPdfObject(pageN.get(PdfName.ANNOTS), pageN);
/* 1585 */           PdfArray annots = null;
/* 1586 */           if (pdfobj == null || !pdfobj.isArray()) {
/* 1587 */             annots = new PdfArray();
/* 1588 */             pageN.put(PdfName.ANNOTS, annots);
/* 1589 */             markUsed(pageN);
/*      */           } else {
/* 1591 */             annots = (PdfArray)pdfobj;
/* 1592 */           }  annots.add(annot.getIndirectReference());
/* 1593 */           markUsed(annots);
/* 1594 */           if (!annot.isUsed()) {
/* 1595 */             PdfRectangle rect = (PdfRectangle)annot.get(PdfName.RECT);
/* 1596 */             if (rect != null && (rect.left() != 0.0F || rect.right() != 0.0F || rect.top() != 0.0F || rect.bottom() != 0.0F)) {
/* 1597 */               int rotation = this.reader.getPageRotation(pageN);
/* 1598 */               Rectangle pageSize = this.reader.getPageSizeWithRotation(pageN);
/* 1599 */               switch (rotation) {
/*      */                 case 90:
/* 1601 */                   annot.put(PdfName.RECT, new PdfRectangle(pageSize
/* 1602 */                         .getTop() - rect.top(), rect
/* 1603 */                         .right(), pageSize
/* 1604 */                         .getTop() - rect.bottom(), rect
/* 1605 */                         .left()));
/*      */                   break;
/*      */                 case 180:
/* 1608 */                   annot.put(PdfName.RECT, new PdfRectangle(pageSize
/* 1609 */                         .getRight() - rect.left(), pageSize
/* 1610 */                         .getTop() - rect.bottom(), pageSize
/* 1611 */                         .getRight() - rect.right(), pageSize
/* 1612 */                         .getTop() - rect.top()));
/*      */                   break;
/*      */                 case 270:
/* 1615 */                   annot.put(PdfName.RECT, new PdfRectangle(rect
/* 1616 */                         .bottom(), pageSize
/* 1617 */                         .getRight() - rect.left(), rect
/* 1618 */                         .top(), pageSize
/* 1619 */                         .getRight() - rect.right()));
/*      */                   break;
/*      */               } 
/*      */             } 
/*      */           } 
/*      */         } 
/* 1625 */         if (!annot.isUsed()) {
/* 1626 */           annot.setUsed();
/* 1627 */           addToBody(annot, annot.getIndirectReference());
/*      */         } 
/*      */       } 
/* 1630 */     } catch (IOException e) {
/* 1631 */       throw new ExceptionConverter(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   void addAnnotation(PdfAnnotation annot, int page) {
/* 1637 */     if (annot.isAnnotation())
/* 1638 */       annot.setPage(page); 
/* 1639 */     addAnnotation(annot, this.reader.getPageN(page));
/*      */   }
/*      */   
/*      */   private void outlineTravel(PRIndirectReference outline) {
/* 1643 */     while (outline != null) {
/* 1644 */       PdfDictionary outlineR = (PdfDictionary)PdfReader.getPdfObjectRelease(outline);
/* 1645 */       PRIndirectReference first = (PRIndirectReference)outlineR.get(PdfName.FIRST);
/* 1646 */       if (first != null) {
/* 1647 */         outlineTravel(first);
/*      */       }
/* 1649 */       PdfReader.killIndirect(outlineR.get(PdfName.DEST));
/* 1650 */       PdfReader.killIndirect(outlineR.get(PdfName.A));
/* 1651 */       PdfReader.killIndirect(outline);
/* 1652 */       outline = (PRIndirectReference)outlineR.get(PdfName.NEXT);
/*      */     } 
/*      */   }
/*      */   
/*      */   void deleteOutlines() {
/* 1657 */     PdfDictionary catalog = this.reader.getCatalog();
/* 1658 */     PdfObject obj = catalog.get(PdfName.OUTLINES);
/* 1659 */     if (obj == null)
/*      */       return; 
/* 1661 */     if (obj instanceof PRIndirectReference) {
/* 1662 */       PRIndirectReference outlines = (PRIndirectReference)obj;
/* 1663 */       outlineTravel(outlines);
/* 1664 */       PdfReader.killIndirect(outlines);
/*      */     } 
/* 1666 */     catalog.remove(PdfName.OUTLINES);
/* 1667 */     markUsed(catalog);
/*      */   }
/*      */   
/*      */   protected void setJavaScript() throws IOException {
/* 1671 */     HashMap<String, PdfObject> djs = this.pdf.getDocumentLevelJS();
/* 1672 */     if (djs.isEmpty())
/*      */       return; 
/* 1674 */     PdfDictionary catalog = this.reader.getCatalog();
/* 1675 */     PdfDictionary names = (PdfDictionary)PdfReader.getPdfObject(catalog.get(PdfName.NAMES), catalog);
/* 1676 */     if (names == null) {
/* 1677 */       names = new PdfDictionary();
/* 1678 */       catalog.put(PdfName.NAMES, names);
/* 1679 */       markUsed(catalog);
/*      */     } 
/* 1681 */     markUsed(names);
/* 1682 */     PdfDictionary tree = PdfNameTree.writeTree(djs, this);
/* 1683 */     names.put(PdfName.JAVASCRIPT, addToBody(tree).getIndirectReference());
/*      */   }
/*      */   
/*      */   protected void addFileAttachments() throws IOException {
/* 1687 */     HashMap<String, PdfObject> fs = this.pdf.getDocumentFileAttachment();
/* 1688 */     if (fs.isEmpty())
/*      */       return; 
/* 1690 */     PdfDictionary catalog = this.reader.getCatalog();
/* 1691 */     PdfDictionary names = (PdfDictionary)PdfReader.getPdfObject(catalog.get(PdfName.NAMES), catalog);
/* 1692 */     if (names == null) {
/* 1693 */       names = new PdfDictionary();
/* 1694 */       catalog.put(PdfName.NAMES, names);
/* 1695 */       markUsed(catalog);
/*      */     } 
/* 1697 */     markUsed(names);
/* 1698 */     HashMap<String, PdfObject> old = PdfNameTree.readTree((PdfDictionary)PdfReader.getPdfObjectRelease(names.get(PdfName.EMBEDDEDFILES)));
/* 1699 */     for (Map.Entry<String, PdfObject> entry : fs.entrySet()) {
/* 1700 */       String name = entry.getKey();
/* 1701 */       int k = 0;
/* 1702 */       StringBuilder nn = new StringBuilder(name);
/* 1703 */       while (old.containsKey(nn.toString())) {
/* 1704 */         k++;
/* 1705 */         nn.append(" ").append(k);
/*      */       } 
/* 1707 */       old.put(nn.toString(), entry.getValue());
/*      */     } 
/* 1709 */     PdfDictionary tree = PdfNameTree.writeTree(old, this);
/*      */     
/* 1711 */     PdfObject oldEmbeddedFiles = names.get(PdfName.EMBEDDEDFILES);
/* 1712 */     if (oldEmbeddedFiles != null) {
/* 1713 */       PdfReader.killIndirect(oldEmbeddedFiles);
/*      */     }
/*      */ 
/*      */     
/* 1717 */     names.put(PdfName.EMBEDDEDFILES, addToBody(tree).getIndirectReference());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void makePackage(PdfCollection collection) {
/* 1726 */     PdfDictionary catalog = this.reader.getCatalog();
/* 1727 */     catalog.put(PdfName.COLLECTION, (PdfObject)collection);
/*      */   }
/*      */   
/*      */   protected void setOutlines() throws IOException {
/* 1731 */     if (this.newBookmarks == null)
/*      */       return; 
/* 1733 */     deleteOutlines();
/* 1734 */     if (this.newBookmarks.isEmpty())
/*      */       return; 
/* 1736 */     PdfDictionary catalog = this.reader.getCatalog();
/* 1737 */     boolean namedAsNames = (catalog.get(PdfName.DESTS) != null);
/* 1738 */     writeOutlines(catalog, namedAsNames);
/* 1739 */     markUsed(catalog);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setViewerPreferences(int preferences) {
/* 1750 */     this.useVp = true;
/* 1751 */     this.viewerPreferences.setViewerPreferences(preferences);
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
/*      */   public void addViewerPreference(PdfName key, PdfObject value) {
/* 1763 */     this.useVp = true;
/* 1764 */     this.viewerPreferences.addViewerPreference(key, value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setSigFlags(int f) {
/* 1774 */     this.sigFlags |= f;
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
/*      */   public void setPageAction(PdfName actionType, PdfAction action) throws PdfException {
/* 1787 */     throw new UnsupportedOperationException(MessageLocalization.getComposedMessage("use.setpageaction.pdfname.actiontype.pdfaction.action.int.page", new Object[0]));
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
/*      */   void setPageAction(PdfName actionType, PdfAction action, int page) throws PdfException {
/* 1800 */     if (!actionType.equals(PAGE_OPEN) && !actionType.equals(PAGE_CLOSE))
/* 1801 */       throw new PdfException(MessageLocalization.getComposedMessage("invalid.page.additional.action.type.1", new Object[] { actionType.toString() })); 
/* 1802 */     PdfDictionary pg = this.reader.getPageN(page);
/* 1803 */     PdfDictionary aa = (PdfDictionary)PdfReader.getPdfObject(pg.get(PdfName.AA), pg);
/* 1804 */     if (aa == null) {
/* 1805 */       aa = new PdfDictionary();
/* 1806 */       pg.put(PdfName.AA, aa);
/* 1807 */       markUsed(pg);
/*      */     } 
/* 1809 */     aa.put(actionType, action);
/* 1810 */     markUsed(aa);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDuration(int seconds) {
/* 1820 */     throw new UnsupportedOperationException(MessageLocalization.getComposedMessage("use.setpageaction.pdfname.actiontype.pdfaction.action.int.page", new Object[0]));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setTransition(PdfTransition transition) {
/* 1830 */     throw new UnsupportedOperationException(MessageLocalization.getComposedMessage("use.setpageaction.pdfname.actiontype.pdfaction.action.int.page", new Object[0]));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void setDuration(int seconds, int page) {
/* 1840 */     PdfDictionary pg = this.reader.getPageN(page);
/* 1841 */     if (seconds < 0) {
/* 1842 */       pg.remove(PdfName.DUR);
/*      */     } else {
/* 1844 */       pg.put(PdfName.DUR, new PdfNumber(seconds));
/* 1845 */     }  markUsed(pg);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void setTransition(PdfTransition transition, int page) {
/* 1855 */     PdfDictionary pg = this.reader.getPageN(page);
/* 1856 */     if (transition == null) {
/* 1857 */       pg.remove(PdfName.TRANS);
/*      */     } else {
/* 1859 */       pg.put(PdfName.TRANS, transition.getTransitionDictionary());
/* 1860 */     }  markUsed(pg);
/*      */   }
/*      */   
/*      */   protected void markUsed(PdfObject obj) {
/* 1864 */     if (this.append && obj != null) {
/* 1865 */       PRIndirectReference ref = null;
/* 1866 */       if (obj.type() == 10) {
/* 1867 */         ref = (PRIndirectReference)obj;
/*      */       } else {
/* 1869 */         ref = obj.getIndRef();
/* 1870 */       }  if (ref != null)
/* 1871 */         this.marked.put(ref.getNumber(), 1); 
/*      */     } 
/*      */   }
/*      */   
/*      */   protected void markUsed(int num) {
/* 1876 */     if (this.append) {
/* 1877 */       this.marked.put(num, 1);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   boolean isAppend() {
/* 1886 */     return this.append;
/*      */   }
/*      */   
/*      */   public PdfReader getPdfReader() {
/* 1890 */     return this.reader;
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
/*      */   public void setAdditionalAction(PdfName actionType, PdfAction action) throws PdfException {
/* 1906 */     if (!actionType.equals(DOCUMENT_CLOSE) && 
/* 1907 */       !actionType.equals(WILL_SAVE) && 
/* 1908 */       !actionType.equals(DID_SAVE) && 
/* 1909 */       !actionType.equals(WILL_PRINT) && 
/* 1910 */       !actionType.equals(DID_PRINT)) {
/* 1911 */       throw new PdfException(MessageLocalization.getComposedMessage("invalid.additional.action.type.1", new Object[] { actionType.toString() }));
/*      */     }
/* 1913 */     PdfDictionary aa = this.reader.getCatalog().getAsDict(PdfName.AA);
/* 1914 */     if (aa == null) {
/* 1915 */       if (action == null)
/*      */         return; 
/* 1917 */       aa = new PdfDictionary();
/* 1918 */       this.reader.getCatalog().put(PdfName.AA, aa);
/*      */     } 
/* 1920 */     markUsed(aa);
/* 1921 */     if (action == null) {
/* 1922 */       aa.remove(actionType);
/*      */     } else {
/* 1924 */       aa.put(actionType, action);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setOpenAction(PdfAction action) {
/* 1932 */     this.openAction = action;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setOpenAction(String name) {
/* 1940 */     throw new UnsupportedOperationException(MessageLocalization.getComposedMessage("open.actions.by.name.are.not.supported", new Object[0]));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setThumbnail(Image image) {
/* 1948 */     throw new UnsupportedOperationException(MessageLocalization.getComposedMessage("use.pdfstamper.setthumbnail", new Object[0]));
/*      */   }
/*      */   
/*      */   void setThumbnail(Image image, int page) throws PdfException, DocumentException {
/* 1952 */     PdfIndirectReference thumb = getImageReference(addDirectImageSimple(image));
/* 1953 */     this.reader.resetReleasePage();
/* 1954 */     PdfDictionary dic = this.reader.getPageN(page);
/* 1955 */     dic.put(PdfName.THUMB, thumb);
/* 1956 */     this.reader.resetReleasePage();
/*      */   }
/*      */ 
/*      */   
/*      */   public PdfContentByte getDirectContentUnder() {
/* 1961 */     throw new UnsupportedOperationException(MessageLocalization.getComposedMessage("use.pdfstamper.getundercontent.or.pdfstamper.getovercontent", new Object[0]));
/*      */   }
/*      */ 
/*      */   
/*      */   public PdfContentByte getDirectContent() {
/* 1966 */     throw new UnsupportedOperationException(MessageLocalization.getComposedMessage("use.pdfstamper.getundercontent.or.pdfstamper.getovercontent", new Object[0]));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void readOCProperties() {
/* 1977 */     if (!this.documentOCG.isEmpty()) {
/*      */       return;
/*      */     }
/* 1980 */     PdfDictionary dict = this.reader.getCatalog().getAsDict(PdfName.OCPROPERTIES);
/* 1981 */     if (dict == null) {
/*      */       return;
/*      */     }
/* 1984 */     PdfArray ocgs = dict.getAsArray(PdfName.OCGS);
/* 1985 */     if (ocgs == null) {
/* 1986 */       ocgs = new PdfArray();
/* 1987 */       dict.put(PdfName.OCGS, ocgs);
/*      */     } 
/*      */ 
/*      */     
/* 1991 */     HashMap<String, PdfLayer> ocgmap = new HashMap<String, PdfLayer>();
/* 1992 */     for (Iterator<PdfObject> i = ocgs.listIterator(); i.hasNext(); ) {
/* 1993 */       PdfIndirectReference ref = (PdfIndirectReference)i.next();
/* 1994 */       PdfLayer layer = new PdfLayer(null);
/* 1995 */       layer.setRef(ref);
/* 1996 */       layer.setOnPanel(false);
/* 1997 */       layer.merge((PdfDictionary)PdfReader.getPdfObject(ref));
/* 1998 */       ocgmap.put(ref.toString(), layer);
/*      */     } 
/* 2000 */     PdfDictionary d = dict.getAsDict(PdfName.D);
/* 2001 */     PdfArray off = d.getAsArray(PdfName.OFF);
/* 2002 */     if (off != null) {
/* 2003 */       for (Iterator<PdfObject> iterator = off.listIterator(); iterator.hasNext(); ) {
/* 2004 */         PdfIndirectReference ref = (PdfIndirectReference)iterator.next();
/* 2005 */         PdfLayer layer = ocgmap.get(ref.toString());
/* 2006 */         layer.setOn(false);
/*      */       } 
/*      */     }
/* 2009 */     PdfArray order = d.getAsArray(PdfName.ORDER);
/* 2010 */     if (order != null) {
/* 2011 */       addOrder((PdfLayer)null, order, ocgmap);
/*      */     }
/* 2013 */     this.documentOCG.addAll(ocgmap.values());
/* 2014 */     this.OCGRadioGroup = d.getAsArray(PdfName.RBGROUPS);
/* 2015 */     if (this.OCGRadioGroup == null)
/* 2016 */       this.OCGRadioGroup = new PdfArray(); 
/* 2017 */     this.OCGLocked = d.getAsArray(PdfName.LOCKED);
/* 2018 */     if (this.OCGLocked == null) {
/* 2019 */       this.OCGLocked = new PdfArray();
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
/*      */   private void addOrder(PdfLayer parent, PdfArray arr, Map<String, PdfLayer> ocgmap) {
/* 2033 */     for (int i = 0; i < arr.size(); i++) {
/* 2034 */       PdfObject obj = arr.getPdfObject(i);
/* 2035 */       if (obj.isIndirect()) {
/* 2036 */         PdfLayer layer = ocgmap.get(obj.toString());
/* 2037 */         if (layer != null) {
/* 2038 */           layer.setOnPanel(true);
/* 2039 */           registerLayer(layer);
/* 2040 */           if (parent != null) {
/* 2041 */             parent.addChild(layer);
/*      */           }
/* 2043 */           if (arr.size() > i + 1 && arr.getPdfObject(i + 1).isArray()) {
/* 2044 */             i++;
/* 2045 */             addOrder(layer, (PdfArray)arr.getPdfObject(i), ocgmap);
/*      */           } 
/*      */         } 
/* 2048 */       } else if (obj.isArray()) {
/* 2049 */         PdfArray sub = (PdfArray)obj;
/* 2050 */         if (sub.isEmpty())
/* 2051 */           return;  obj = sub.getPdfObject(0);
/* 2052 */         if (obj.isString()) {
/* 2053 */           PdfLayer layer = new PdfLayer(obj.toString());
/* 2054 */           layer.setOnPanel(true);
/* 2055 */           registerLayer(layer);
/* 2056 */           if (parent != null) {
/* 2057 */             parent.addChild(layer);
/*      */           }
/* 2059 */           PdfArray array = new PdfArray();
/* 2060 */           for (Iterator<PdfObject> j = sub.listIterator(); j.hasNext();) {
/* 2061 */             array.add(j.next());
/*      */           }
/* 2063 */           addOrder(layer, array, ocgmap);
/*      */         } else {
/* 2065 */           addOrder(parent, (PdfArray)obj, ocgmap);
/*      */         } 
/*      */       } 
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
/*      */   public Map<String, PdfLayer> getPdfLayers() {
/* 2079 */     if (!this.originalLayersAreRead) {
/* 2080 */       this.originalLayersAreRead = true;
/* 2081 */       readOCProperties();
/*      */     } 
/* 2083 */     HashMap<String, PdfLayer> map = new HashMap<String, PdfLayer>();
/*      */ 
/*      */     
/* 2086 */     for (PdfOCG pdfOCG : this.documentOCG) {
/* 2087 */       String key; PdfLayer layer = (PdfLayer)pdfOCG;
/* 2088 */       if (layer.getTitle() == null) {
/* 2089 */         key = layer.getAsString(PdfName.NAME).toString();
/*      */       } else {
/* 2091 */         key = layer.getTitle();
/*      */       } 
/* 2093 */       if (map.containsKey(key)) {
/* 2094 */         int seq = 2;
/* 2095 */         String tmp = key + "(" + seq + ")";
/* 2096 */         while (map.containsKey(tmp)) {
/* 2097 */           seq++;
/* 2098 */           tmp = key + "(" + seq + ")";
/*      */         } 
/* 2100 */         key = tmp;
/*      */       } 
/* 2102 */       map.put(key, layer);
/*      */     } 
/* 2104 */     return map;
/*      */   }
/*      */ 
/*      */   
/*      */   void registerLayer(PdfOCG layer) {
/* 2109 */     if (!this.originalLayersAreRead) {
/* 2110 */       this.originalLayersAreRead = true;
/* 2111 */       readOCProperties();
/*      */     } 
/* 2113 */     super.registerLayer(layer);
/*      */   }
/*      */   
/*      */   public void createXmpMetadata() {
/*      */     try {
/* 2118 */       this.xmpWriter = createXmpWriter((ByteArrayOutputStream)null, this.reader.getInfo());
/* 2119 */       this.xmpMetadata = null;
/* 2120 */     } catch (IOException ioe) {
/* 2121 */       ioe.printStackTrace();
/*      */     } 
/*      */   }
/*      */   
/*      */   protected HashMap<Object, PdfObject> getNamedDestinations() {
/* 2126 */     return this.namedDestinations;
/*      */   }
/*      */ 
/*      */   
/*      */   protected void updateNamedDestinations() throws IOException {
/* 2131 */     PdfDictionary dic = this.reader.getCatalog().getAsDict(PdfName.NAMES);
/* 2132 */     if (dic != null)
/* 2133 */       dic = dic.getAsDict(PdfName.DESTS); 
/* 2134 */     if (dic == null) {
/* 2135 */       dic = this.reader.getCatalog().getAsDict(PdfName.DESTS);
/*      */     }
/* 2137 */     if (dic == null) {
/* 2138 */       dic = new PdfDictionary();
/* 2139 */       PdfDictionary dests = new PdfDictionary();
/* 2140 */       dic.put(PdfName.NAMES, new PdfArray());
/* 2141 */       dests.put(PdfName.DESTS, dic);
/* 2142 */       this.reader.getCatalog().put(PdfName.NAMES, dests);
/*      */     } 
/*      */     
/* 2145 */     PdfArray names = getLastChildInNameTree(dic);
/*      */     
/* 2147 */     for (Object name : this.namedDestinations.keySet()) {
/* 2148 */       names.add(new PdfString(name.toString()));
/* 2149 */       names.add(addToBody(this.namedDestinations.get(name), getPdfIndirectReference()).getIndirectReference());
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private PdfArray getLastChildInNameTree(PdfDictionary dic) {
/* 2157 */     PdfArray names, childNode = dic.getAsArray(PdfName.KIDS);
/* 2158 */     if (childNode != null) {
/* 2159 */       PdfDictionary lastKid = childNode.getAsDict(childNode.size() - 1);
/* 2160 */       names = getLastChildInNameTree(lastKid);
/*      */     } else {
/* 2162 */       names = dic.getAsArray(PdfName.NAMES);
/*      */     } 
/*      */     
/* 2165 */     return names;
/*      */   }
/*      */   
/*      */   static class PageStamp
/*      */   {
/*      */     PdfDictionary pageN;
/*      */     StampContent under;
/*      */     StampContent over;
/*      */     PageResources pageResources;
/* 2174 */     int replacePoint = 0;
/*      */     
/*      */     PageStamp(PdfStamperImp stamper, PdfReader reader, PdfDictionary pageN) {
/* 2177 */       this.pageN = pageN;
/* 2178 */       this.pageResources = new PageResources();
/* 2179 */       PdfDictionary resources = pageN.getAsDict(PdfName.RESOURCES);
/* 2180 */       this.pageResources.setOriginalResources(resources, stamper.namePtr);
/*      */     }
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/PdfStamperImp.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */