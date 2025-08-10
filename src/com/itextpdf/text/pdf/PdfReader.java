/*      */ package com.itextpdf.text.pdf;
/*      */ 
/*      */ import com.itextpdf.text.DocWriter;
/*      */ import com.itextpdf.text.Document;
/*      */ import com.itextpdf.text.ExceptionConverter;
/*      */ import com.itextpdf.text.PageSize;
/*      */ import com.itextpdf.text.Rectangle;
/*      */ import com.itextpdf.text.error_messages.MessageLocalization;
/*      */ import com.itextpdf.text.exceptions.BadPasswordException;
/*      */ import com.itextpdf.text.exceptions.InvalidPdfException;
/*      */ import com.itextpdf.text.exceptions.UnsupportedPdfException;
/*      */ import com.itextpdf.text.io.RandomAccessSource;
/*      */ import com.itextpdf.text.io.RandomAccessSourceFactory;
/*      */ import com.itextpdf.text.io.WindowRandomAccessSource;
/*      */ import com.itextpdf.text.log.Counter;
/*      */ import com.itextpdf.text.log.CounterFactory;
/*      */ import com.itextpdf.text.log.Level;
/*      */ import com.itextpdf.text.log.Logger;
/*      */ import com.itextpdf.text.log.LoggerFactory;
/*      */ import com.itextpdf.text.pdf.interfaces.PdfViewerPreferences;
/*      */ import com.itextpdf.text.pdf.internal.PdfViewerPreferencesImp;
/*      */ import com.itextpdf.text.pdf.security.ExternalDecryptionProcess;
/*      */ import java.io.ByteArrayInputStream;
/*      */ import java.io.ByteArrayOutputStream;
/*      */ import java.io.DataInputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.net.URL;
/*      */ import java.security.Key;
/*      */ import java.security.MessageDigest;
/*      */ import java.security.PrivateKey;
/*      */ import java.security.cert.Certificate;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collections;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import java.util.Stack;
/*      */ import java.util.zip.InflaterInputStream;
/*      */ import org.bouncycastle.cert.X509CertificateHolder;
/*      */ import org.bouncycastle.cms.CMSEnvelopedData;
/*      */ import org.bouncycastle.cms.RecipientInformation;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class PdfReader
/*      */   implements PdfViewerPreferences
/*      */ {
/*      */   public static boolean unethicalreading = false;
/*      */   public static boolean debugmode = false;
/*  107 */   private static final Logger LOGGER = LoggerFactory.getLogger(PdfReader.class);
/*      */   
/*  109 */   static final PdfName[] pageInhCandidates = new PdfName[] { PdfName.MEDIABOX, PdfName.ROTATE, PdfName.RESOURCES, PdfName.CROPBOX };
/*      */ 
/*      */ 
/*      */   
/*  113 */   static final byte[] endstream = PdfEncodings.convertToBytes("endstream", (String)null);
/*  114 */   static final byte[] endobj = PdfEncodings.convertToBytes("endobj", (String)null);
/*      */   
/*      */   protected PRTokeniser tokens;
/*      */   
/*      */   protected long[] xref;
/*      */   
/*      */   protected HashMap<Integer, IntHashtable> objStmMark;
/*      */   
/*      */   protected LongHashtable objStmToOffset;
/*      */   protected boolean newXrefType;
/*      */   protected ArrayList<PdfObject> xrefObj;
/*      */   PdfDictionary rootPages;
/*      */   protected PdfDictionary trailer;
/*      */   protected PdfDictionary catalog;
/*      */   protected PageRefs pageRefs;
/*  129 */   protected PRAcroForm acroForm = null;
/*      */   protected boolean acroFormParsed = false;
/*      */   protected boolean encrypted = false;
/*      */   protected boolean rebuilt = false;
/*      */   protected int freeXref;
/*      */   protected boolean tampered = false;
/*      */   protected long lastXref;
/*      */   protected long eofPos;
/*      */   protected char pdfVersion;
/*      */   protected PdfEncryption decrypt;
/*  139 */   protected byte[] password = null;
/*  140 */   protected Key certificateKey = null;
/*  141 */   protected Certificate certificate = null;
/*  142 */   protected String certificateKeyProvider = null;
/*  143 */   protected ExternalDecryptionProcess externalDecryptionProcess = null;
/*      */   private boolean ownerPasswordUsed;
/*  145 */   protected ArrayList<PdfString> strings = new ArrayList<PdfString>();
/*      */   protected boolean sharedStreams = true;
/*      */   protected boolean consolidateNamedDestinations = false;
/*      */   protected boolean remoteToLocalNamedDestinations = false;
/*      */   protected int rValue;
/*      */   protected long pValue;
/*      */   private int objNum;
/*      */   private int objGen;
/*      */   private long fileLength;
/*      */   private boolean hybridXref;
/*  155 */   private int lastXrefPartial = -1;
/*      */   
/*      */   private boolean partial;
/*      */   private PRIndirectReference cryptoRef;
/*  159 */   private final PdfViewerPreferencesImp viewerPreferences = new PdfViewerPreferencesImp();
/*      */ 
/*      */   
/*      */   private boolean encryptionError;
/*      */ 
/*      */   
/*  165 */   MemoryLimitsAwareHandler memoryLimitsAwareHandler = null;
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean appendable;
/*      */ 
/*      */   
/*  172 */   protected static Counter COUNTER = CounterFactory.getCounter(PdfReader.class); private int readDepth;
/*      */   protected Counter getCounter() {
/*  174 */     return COUNTER;
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
/*      */   private PdfReader(RandomAccessSource byteSource, boolean partialRead, byte[] ownerPassword, Certificate certificate, Key certificateKey, String certificateKeyProvider, ExternalDecryptionProcess externalDecryptionProcess, boolean closeSourceOnConstructorError) throws IOException {
/*  189 */     this(byteSource, (new ReaderProperties()).setCertificate(certificate).setCertificateKey(certificateKey).setCertificateKeyProvider(certificateKeyProvider).setExternalDecryptionProcess(externalDecryptionProcess)
/*  190 */         .setOwnerPassword(ownerPassword).setPartialRead(partialRead).setCloseSourceOnconstructorError(closeSourceOnConstructorError));
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
/*      */   public PdfReader(String filename) throws IOException {
/*  230 */     this(filename, (byte[])null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PdfReader(ReaderProperties properties, String filename) throws IOException {
/*  240 */     this((new RandomAccessSourceFactory())
/*  241 */         .setForceRead(false)
/*  242 */         .setUsePlainRandomAccess(Document.plainRandomAccess)
/*  243 */         .createBestSource(filename), properties);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PdfReader(String filename, byte[] ownerPassword) throws IOException {
/*  254 */     this((new ReaderProperties()).setOwnerPassword(ownerPassword), filename);
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
/*      */   public PdfReader(String filename, byte[] ownerPassword, boolean partial) throws IOException {
/*  266 */     this((new RandomAccessSourceFactory())
/*  267 */         .setForceRead(false)
/*  268 */         .setUsePlainRandomAccess(Document.plainRandomAccess)
/*  269 */         .createBestSource(filename), (new ReaderProperties()).setOwnerPassword(ownerPassword).setPartialRead(partial));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PdfReader(byte[] pdfIn) throws IOException {
/*  278 */     this((new RandomAccessSourceFactory()).createSource(pdfIn), new ReaderProperties());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PdfReader(byte[] pdfIn, byte[] ownerPassword) throws IOException {
/*  288 */     this((new RandomAccessSourceFactory()).createSource(pdfIn), (new ReaderProperties()).setOwnerPassword(ownerPassword));
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
/*      */   public PdfReader(String filename, Certificate certificate, Key certificateKey, String certificateKeyProvider) throws IOException {
/*  300 */     this((new RandomAccessSourceFactory())
/*  301 */         .setForceRead(false)
/*  302 */         .setUsePlainRandomAccess(Document.plainRandomAccess)
/*  303 */         .createBestSource(filename), (new ReaderProperties())
/*  304 */         .setCertificate(certificate).setCertificateKey(certificateKey).setCertificateKeyProvider(certificateKeyProvider));
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
/*      */   public PdfReader(String filename, Certificate certificate, ExternalDecryptionProcess externalDecryptionProcess) throws IOException {
/*  316 */     this((new RandomAccessSourceFactory())
/*  317 */         .setForceRead(false)
/*  318 */         .setUsePlainRandomAccess(Document.plainRandomAccess)
/*  319 */         .createBestSource(filename), (new ReaderProperties())
/*  320 */         .setCertificate(certificate).setExternalDecryptionProcess(externalDecryptionProcess));
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
/*      */   public PdfReader(byte[] pdfIn, Certificate certificate, ExternalDecryptionProcess externalDecryptionProcess) throws IOException {
/*  332 */     this((new RandomAccessSourceFactory())
/*  333 */         .setForceRead(false)
/*  334 */         .setUsePlainRandomAccess(Document.plainRandomAccess)
/*  335 */         .createSource(pdfIn), (new ReaderProperties())
/*  336 */         .setCertificate(certificate).setExternalDecryptionProcess(externalDecryptionProcess));
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
/*      */   public PdfReader(InputStream inputStream, Certificate certificate, ExternalDecryptionProcess externalDecryptionProcess) throws IOException {
/*  348 */     this((new RandomAccessSourceFactory()).setForceRead(false).setUsePlainRandomAccess(Document.plainRandomAccess).createSource(inputStream), (new ReaderProperties())
/*  349 */         .setCertificate(certificate).setExternalDecryptionProcess(externalDecryptionProcess));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PdfReader(URL url) throws IOException {
/*  358 */     this((new RandomAccessSourceFactory()).createSource(url), new ReaderProperties());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PdfReader(URL url, byte[] ownerPassword) throws IOException {
/*  368 */     this((new RandomAccessSourceFactory()).createSource(url), (new ReaderProperties())
/*  369 */         .setOwnerPassword(ownerPassword));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PdfReader(InputStream is, byte[] ownerPassword) throws IOException {
/*  380 */     this((new RandomAccessSourceFactory()).createSource(is), (new ReaderProperties())
/*  381 */         .setOwnerPassword(ownerPassword).setCloseSourceOnconstructorError(false));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PdfReader(InputStream is) throws IOException {
/*  392 */     this((new RandomAccessSourceFactory()).createSource(is), (new ReaderProperties()).setCloseSourceOnconstructorError(false));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PdfReader(ReaderProperties properties, InputStream is) throws IOException {
/*  403 */     this((new RandomAccessSourceFactory()).createSource(is), properties);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PdfReader(ReaderProperties properties, RandomAccessFileOrArray raf) throws IOException {
/*  413 */     this(raf.getByteSource(), properties);
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
/*      */   public PdfReader(RandomAccessFileOrArray raf, byte[] ownerPassword) throws IOException {
/*  426 */     this((new ReaderProperties()).setOwnerPassword(ownerPassword).setPartialRead(true).setCloseSourceOnconstructorError(false), raf);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PdfReader(RandomAccessFileOrArray raf, byte[] ownerPassword, boolean partial) throws IOException {
/*  437 */     this(raf.getByteSource(), (new ReaderProperties()).setPartialRead(partial).setOwnerPassword(ownerPassword).setCloseSourceOnconstructorError(false));
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
/*      */   private static PRTokeniser getOffsetTokeniser(RandomAccessSource byteSource) throws IOException {
/*  486 */     PRTokeniser tok = new PRTokeniser(new RandomAccessFileOrArray(byteSource));
/*  487 */     int offset = tok.getHeaderOffset();
/*  488 */     if (offset != 0) {
/*  489 */       WindowRandomAccessSource windowRandomAccessSource = new WindowRandomAccessSource(byteSource, offset);
/*  490 */       tok = new PRTokeniser(new RandomAccessFileOrArray((RandomAccessSource)windowRandomAccessSource));
/*      */     } 
/*  492 */     return tok;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public RandomAccessFileOrArray getSafeFile() {
/*  500 */     return this.tokens.getSafeFile();
/*      */   }
/*      */   
/*      */   protected PdfReaderInstance getPdfReaderInstance(PdfWriter writer) {
/*  504 */     return new PdfReaderInstance(this, writer);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getNumberOfPages() {
/*  513 */     return this.pageRefs.size();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PdfDictionary getCatalog() {
/*  522 */     return this.catalog;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PRAcroForm getAcroForm() {
/*  530 */     if (!this.acroFormParsed) {
/*  531 */       this.acroFormParsed = true;
/*  532 */       PdfObject form = this.catalog.get(PdfName.ACROFORM);
/*  533 */       if (form != null) {
/*      */         try {
/*  535 */           this.acroForm = new PRAcroForm(this);
/*  536 */           this.acroForm.readAcroForm((PdfDictionary)getPdfObject(form));
/*      */         }
/*  538 */         catch (Exception e) {
/*  539 */           this.acroForm = null;
/*      */         } 
/*      */       }
/*      */     } 
/*  543 */     return this.acroForm;
/*      */   }
/*      */   
/*      */   MemoryLimitsAwareHandler getMemoryLimitsAwareHandler() {
/*  547 */     return this.memoryLimitsAwareHandler;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getPageRotation(int index) {
/*  556 */     return getPageRotation(this.pageRefs.getPageNRelease(index));
/*      */   }
/*      */   
/*      */   int getPageRotation(PdfDictionary page) {
/*  560 */     PdfNumber rotate = page.getAsNumber(PdfName.ROTATE);
/*  561 */     if (rotate == null) {
/*  562 */       return 0;
/*      */     }
/*  564 */     int n = rotate.intValue();
/*  565 */     n %= 360;
/*  566 */     return (n < 0) ? (n + 360) : n;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Rectangle getPageSizeWithRotation(int index) {
/*  575 */     return getPageSizeWithRotation(this.pageRefs.getPageNRelease(index));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Rectangle getPageSizeWithRotation(PdfDictionary page) {
/*  584 */     Rectangle rect = getPageSize(page);
/*  585 */     int rotation = getPageRotation(page);
/*  586 */     while (rotation > 0) {
/*  587 */       rect = rect.rotate();
/*  588 */       rotation -= 90;
/*      */     } 
/*  590 */     return rect;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Rectangle getPageSize(int index) {
/*  599 */     return getPageSize(this.pageRefs.getPageNRelease(index));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Rectangle getPageSize(PdfDictionary page) {
/*  608 */     PdfArray mediaBox = page.getAsArray(PdfName.MEDIABOX);
/*  609 */     return getNormalizedRectangle(mediaBox);
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
/*      */   public Rectangle getCropBox(int index) {
/*  621 */     PdfDictionary page = this.pageRefs.getPageNRelease(index);
/*  622 */     PdfArray cropBox = (PdfArray)getPdfObjectRelease(page.get(PdfName.CROPBOX));
/*  623 */     if (cropBox == null)
/*  624 */       return getPageSize(page); 
/*  625 */     return getNormalizedRectangle(cropBox);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Rectangle getBoxSize(int index, String boxName) {
/*  634 */     PdfDictionary page = this.pageRefs.getPageNRelease(index);
/*  635 */     PdfArray box = null;
/*  636 */     if (boxName.equals("trim")) {
/*  637 */       box = (PdfArray)getPdfObjectRelease(page.get(PdfName.TRIMBOX));
/*  638 */     } else if (boxName.equals("art")) {
/*  639 */       box = (PdfArray)getPdfObjectRelease(page.get(PdfName.ARTBOX));
/*  640 */     } else if (boxName.equals("bleed")) {
/*  641 */       box = (PdfArray)getPdfObjectRelease(page.get(PdfName.BLEEDBOX));
/*  642 */     } else if (boxName.equals("crop")) {
/*  643 */       box = (PdfArray)getPdfObjectRelease(page.get(PdfName.CROPBOX));
/*  644 */     } else if (boxName.equals("media")) {
/*  645 */       box = (PdfArray)getPdfObjectRelease(page.get(PdfName.MEDIABOX));
/*  646 */     }  if (box == null)
/*  647 */       return null; 
/*  648 */     return getNormalizedRectangle(box);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public HashMap<String, String> getInfo() {
/*  657 */     HashMap<String, String> map = new HashMap<String, String>();
/*  658 */     PdfDictionary info = this.trailer.getAsDict(PdfName.INFO);
/*  659 */     if (info == null)
/*  660 */       return map; 
/*  661 */     for (PdfName element : info.getKeys()) {
/*  662 */       PdfName key = element;
/*  663 */       PdfObject obj = getPdfObject(info.get(key));
/*  664 */       if (obj == null)
/*      */         continue; 
/*  666 */       String value = obj.toString();
/*  667 */       switch (obj.type()) {
/*      */         case 3:
/*  669 */           value = ((PdfString)obj).toUnicodeString();
/*      */           break;
/*      */         
/*      */         case 4:
/*  673 */           value = PdfName.decodeName(value);
/*      */           break;
/*      */       } 
/*      */       
/*  677 */       map.put(PdfName.decodeName(key.toString()), value);
/*      */     } 
/*  679 */     return map;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Rectangle getNormalizedRectangle(PdfArray box) {
/*  687 */     float llx = ((PdfNumber)getPdfObjectRelease(box.getPdfObject(0))).floatValue();
/*  688 */     float lly = ((PdfNumber)getPdfObjectRelease(box.getPdfObject(1))).floatValue();
/*  689 */     float urx = ((PdfNumber)getPdfObjectRelease(box.getPdfObject(2))).floatValue();
/*  690 */     float ury = ((PdfNumber)getPdfObjectRelease(box.getPdfObject(3))).floatValue();
/*  691 */     return new Rectangle(Math.min(llx, urx), Math.min(lly, ury), 
/*  692 */         Math.max(llx, urx), Math.max(lly, ury));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isTagged() {
/*  699 */     PdfDictionary markInfo = this.catalog.getAsDict(PdfName.MARKINFO);
/*  700 */     if (markInfo == null)
/*  701 */       return false; 
/*  702 */     if (PdfBoolean.PDFTRUE.equals(markInfo.getAsBoolean(PdfName.MARKED))) {
/*  703 */       return (this.catalog.getAsDict(PdfName.STRUCTTREEROOT) != null);
/*      */     }
/*  705 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void readPdf() throws IOException {
/*  713 */     this.fileLength = this.tokens.getFile().length();
/*  714 */     this.pdfVersion = this.tokens.checkPdfHeader();
/*  715 */     if (null == this.memoryLimitsAwareHandler) {
/*  716 */       this.memoryLimitsAwareHandler = new MemoryLimitsAwareHandler(this.fileLength);
/*      */     }
/*      */     try {
/*  719 */       readXref();
/*      */     }
/*  721 */     catch (Exception e) {
/*      */       try {
/*  723 */         this.rebuilt = true;
/*  724 */         rebuildXref();
/*  725 */         this.lastXref = -1L;
/*      */       }
/*  727 */       catch (Exception ne) {
/*  728 */         throw new InvalidPdfException(MessageLocalization.getComposedMessage("rebuild.failed.1.original.message.2", new Object[] { ne.getMessage(), e.getMessage() }));
/*      */       } 
/*      */     } 
/*      */     try {
/*  732 */       readDocObj();
/*      */     }
/*  734 */     catch (Exception e) {
/*  735 */       if (e instanceof BadPasswordException)
/*  736 */         throw new BadPasswordException(e.getMessage()); 
/*  737 */       if (this.rebuilt || this.encryptionError)
/*  738 */         throw new InvalidPdfException(e.getMessage()); 
/*  739 */       this.rebuilt = true;
/*  740 */       this.encrypted = false;
/*      */       try {
/*  742 */         rebuildXref();
/*  743 */         this.lastXref = -1L;
/*  744 */         readDocObj();
/*  745 */       } catch (Exception ne) {
/*  746 */         throw new InvalidPdfException(MessageLocalization.getComposedMessage("rebuild.failed.1.original.message.2", new Object[] { ne.getMessage(), e.getMessage() }));
/*      */       } 
/*      */     } 
/*  749 */     this.strings.clear();
/*  750 */     readPages();
/*      */     
/*  752 */     removeUnusedObjects();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void readPdfPartial() throws IOException {
/*  761 */     this.fileLength = this.tokens.getFile().length();
/*  762 */     this.pdfVersion = this.tokens.checkPdfHeader();
/*  763 */     if (null == this.memoryLimitsAwareHandler) {
/*  764 */       this.memoryLimitsAwareHandler = new MemoryLimitsAwareHandler(this.fileLength);
/*      */     }
/*      */     try {
/*  767 */       readXref();
/*      */     }
/*  769 */     catch (Exception e) {
/*      */       try {
/*  771 */         this.rebuilt = true;
/*  772 */         rebuildXref();
/*  773 */         this.lastXref = -1L;
/*  774 */       } catch (Exception ne) {
/*  775 */         throw new InvalidPdfException(
/*  776 */             MessageLocalization.getComposedMessage("rebuild.failed.1.original.message.2", new Object[] {
/*      */                 
/*  778 */                 ne.getMessage(), e.getMessage() }), ne);
/*      */       } 
/*      */     } 
/*  781 */     readDocObjPartial();
/*  782 */     readPages();
/*      */   }
/*      */   
/*      */   private boolean equalsArray(byte[] ar1, byte[] ar2, int size) {
/*  786 */     for (int k = 0; k < size; k++) {
/*  787 */       if (ar1[k] != ar2[k])
/*  788 */         return false; 
/*      */     } 
/*  790 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void readDecryptedDocObj() throws IOException {
/*  798 */     if (this.encrypted)
/*      */       return; 
/*  800 */     PdfObject encDic = this.trailer.get(PdfName.ENCRYPT);
/*  801 */     if (encDic == null || encDic.toString().equals("null"))
/*      */       return; 
/*  803 */     this.encryptionError = true;
/*  804 */     byte[] encryptionKey = null;
/*  805 */     this.encrypted = true;
/*  806 */     PdfDictionary enc = (PdfDictionary)getPdfObject(encDic);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  811 */     PdfDictionary cfDict = enc.getAsDict(PdfName.CF);
/*  812 */     if (cfDict != null) {
/*  813 */       PdfDictionary stdCFDict = cfDict.getAsDict(PdfName.STDCF);
/*  814 */       if (stdCFDict != null) {
/*  815 */         PdfName authEvent = stdCFDict.getAsName(PdfName.AUTHEVENT);
/*  816 */         if (authEvent != null)
/*      */         {
/*      */           
/*  819 */           if (authEvent.compareTo(PdfName.EFOPEN) == 0 && !this.ownerPasswordUsed) {
/*      */             return;
/*      */           }
/*      */         }
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  827 */     PdfArray documentIDs = this.trailer.getAsArray(PdfName.ID);
/*  828 */     byte[] documentID = null;
/*  829 */     if (documentIDs != null) {
/*  830 */       PdfObject o = documentIDs.getPdfObject(0);
/*  831 */       this.strings.remove(o);
/*  832 */       String s = o.toString();
/*  833 */       documentID = DocWriter.getISOBytes(s);
/*  834 */       if (documentIDs.size() > 1) {
/*  835 */         this.strings.remove(documentIDs.getPdfObject(1));
/*      */       }
/*      */     } 
/*  838 */     if (documentID == null)
/*  839 */       documentID = new byte[0]; 
/*  840 */     byte[] uValue = null;
/*  841 */     byte[] oValue = null;
/*  842 */     int cryptoMode = 0;
/*  843 */     int lengthValue = 0;
/*      */     
/*  845 */     PdfObject filter = getPdfObjectRelease(enc.get(PdfName.FILTER));
/*      */     
/*  847 */     if (filter.equals(PdfName.STANDARD)) {
/*  848 */       PdfDictionary dic; PdfObject em, em5; String s = enc.get(PdfName.U).toString();
/*  849 */       this.strings.remove(enc.get(PdfName.U));
/*  850 */       uValue = DocWriter.getISOBytes(s);
/*  851 */       s = enc.get(PdfName.O).toString();
/*  852 */       this.strings.remove(enc.get(PdfName.O));
/*  853 */       oValue = DocWriter.getISOBytes(s);
/*  854 */       if (enc.contains(PdfName.OE))
/*  855 */         this.strings.remove(enc.get(PdfName.OE)); 
/*  856 */       if (enc.contains(PdfName.UE))
/*  857 */         this.strings.remove(enc.get(PdfName.UE)); 
/*  858 */       if (enc.contains(PdfName.PERMS)) {
/*  859 */         this.strings.remove(enc.get(PdfName.PERMS));
/*      */       }
/*  861 */       PdfObject o = enc.get(PdfName.P);
/*  862 */       if (!o.isNumber())
/*  863 */         throw new InvalidPdfException(MessageLocalization.getComposedMessage("illegal.p.value", new Object[0])); 
/*  864 */       this.pValue = ((PdfNumber)o).longValue();
/*      */       
/*  866 */       o = enc.get(PdfName.R);
/*  867 */       if (!o.isNumber())
/*  868 */         throw new InvalidPdfException(MessageLocalization.getComposedMessage("illegal.r.value", new Object[0])); 
/*  869 */       this.rValue = ((PdfNumber)o).intValue();
/*      */       
/*  871 */       switch (this.rValue) {
/*      */         case 2:
/*  873 */           cryptoMode = 0;
/*      */           break;
/*      */         case 3:
/*  876 */           o = enc.get(PdfName.LENGTH);
/*  877 */           if (!o.isNumber())
/*  878 */             throw new InvalidPdfException(MessageLocalization.getComposedMessage("illegal.length.value", new Object[0])); 
/*  879 */           lengthValue = ((PdfNumber)o).intValue();
/*  880 */           if (lengthValue > 128 || lengthValue < 40 || lengthValue % 8 != 0)
/*  881 */             throw new InvalidPdfException(MessageLocalization.getComposedMessage("illegal.length.value", new Object[0])); 
/*  882 */           cryptoMode = 1;
/*      */           break;
/*      */         case 4:
/*  885 */           dic = (PdfDictionary)enc.get(PdfName.CF);
/*  886 */           if (dic == null)
/*  887 */             throw new InvalidPdfException(MessageLocalization.getComposedMessage("cf.not.found.encryption", new Object[0])); 
/*  888 */           dic = (PdfDictionary)dic.get(PdfName.STDCF);
/*  889 */           if (dic == null)
/*  890 */             throw new InvalidPdfException(MessageLocalization.getComposedMessage("stdcf.not.found.encryption", new Object[0])); 
/*  891 */           if (PdfName.V2.equals(dic.get(PdfName.CFM))) {
/*  892 */             cryptoMode = 1;
/*  893 */           } else if (PdfName.AESV2.equals(dic.get(PdfName.CFM))) {
/*  894 */             cryptoMode = 2;
/*      */           } else {
/*  896 */             throw new UnsupportedPdfException(MessageLocalization.getComposedMessage("no.compatible.encryption.found", new Object[0]));
/*  897 */           }  em = enc.get(PdfName.ENCRYPTMETADATA);
/*  898 */           if (em != null && em.toString().equals("false"))
/*  899 */             cryptoMode |= 0x8; 
/*      */           break;
/*      */         case 5:
/*  902 */           cryptoMode = 3;
/*  903 */           em5 = enc.get(PdfName.ENCRYPTMETADATA);
/*  904 */           if (em5 != null && em5.toString().equals("false"))
/*  905 */             cryptoMode |= 0x8; 
/*      */           break;
/*      */         default:
/*  908 */           throw new UnsupportedPdfException(MessageLocalization.getComposedMessage("unknown.encryption.type.r.eq.1", this.rValue));
/*      */       } 
/*      */     
/*  911 */     } else if (filter.equals(PdfName.PUBSEC)) {
/*  912 */       PdfDictionary dic; X509CertificateHolder certHolder; PdfObject em; boolean foundRecipient = false;
/*  913 */       byte[] envelopedData = null;
/*  914 */       PdfArray recipients = null;
/*      */       
/*  916 */       PdfObject o = enc.get(PdfName.V);
/*  917 */       if (!o.isNumber())
/*  918 */         throw new InvalidPdfException(MessageLocalization.getComposedMessage("illegal.v.value", new Object[0])); 
/*  919 */       int vValue = ((PdfNumber)o).intValue();
/*  920 */       switch (vValue) {
/*      */         case 1:
/*  922 */           cryptoMode = 0;
/*  923 */           lengthValue = 40;
/*  924 */           recipients = (PdfArray)enc.get(PdfName.RECIPIENTS);
/*      */           break;
/*      */         case 2:
/*  927 */           o = enc.get(PdfName.LENGTH);
/*  928 */           if (!o.isNumber())
/*  929 */             throw new InvalidPdfException(MessageLocalization.getComposedMessage("illegal.length.value", new Object[0])); 
/*  930 */           lengthValue = ((PdfNumber)o).intValue();
/*  931 */           if (lengthValue > 128 || lengthValue < 40 || lengthValue % 8 != 0)
/*  932 */             throw new InvalidPdfException(MessageLocalization.getComposedMessage("illegal.length.value", new Object[0])); 
/*  933 */           cryptoMode = 1;
/*  934 */           recipients = (PdfArray)enc.get(PdfName.RECIPIENTS);
/*      */           break;
/*      */         case 4:
/*      */         case 5:
/*  938 */           dic = (PdfDictionary)enc.get(PdfName.CF);
/*  939 */           if (dic == null)
/*  940 */             throw new InvalidPdfException(MessageLocalization.getComposedMessage("cf.not.found.encryption", new Object[0])); 
/*  941 */           dic = (PdfDictionary)dic.get(PdfName.DEFAULTCRYPTFILTER);
/*  942 */           if (dic == null)
/*  943 */             throw new InvalidPdfException(MessageLocalization.getComposedMessage("defaultcryptfilter.not.found.encryption", new Object[0])); 
/*  944 */           if (PdfName.V2.equals(dic.get(PdfName.CFM))) {
/*  945 */             cryptoMode = 1;
/*  946 */             lengthValue = 128;
/*      */           }
/*  948 */           else if (PdfName.AESV2.equals(dic.get(PdfName.CFM))) {
/*  949 */             cryptoMode = 2;
/*  950 */             lengthValue = 128;
/*      */           }
/*  952 */           else if (PdfName.AESV3.equals(dic.get(PdfName.CFM))) {
/*  953 */             cryptoMode = 3;
/*  954 */             lengthValue = 256;
/*      */           } else {
/*      */             
/*  957 */             throw new UnsupportedPdfException(MessageLocalization.getComposedMessage("no.compatible.encryption.found", new Object[0]));
/*  958 */           }  em = dic.get(PdfName.ENCRYPTMETADATA);
/*  959 */           if (em != null && em.toString().equals("false")) {
/*  960 */             cryptoMode |= 0x8;
/*      */           }
/*  962 */           recipients = (PdfArray)dic.get(PdfName.RECIPIENTS);
/*      */           break;
/*      */         default:
/*  965 */           throw new UnsupportedPdfException(MessageLocalization.getComposedMessage("unknown.encryption.type.v.eq.1", vValue));
/*      */       } 
/*      */       
/*      */       try {
/*  969 */         certHolder = new X509CertificateHolder(this.certificate.getEncoded());
/*      */       }
/*  971 */       catch (Exception f) {
/*  972 */         throw new ExceptionConverter(f);
/*      */       } 
/*  974 */       if (this.externalDecryptionProcess == null) {
/*  975 */         for (int i = 0; i < recipients.size(); i++) {
/*  976 */           PdfObject recipient = recipients.getPdfObject(i);
/*  977 */           this.strings.remove(recipient);
/*      */           
/*  979 */           CMSEnvelopedData data = null;
/*      */           try {
/*  981 */             data = new CMSEnvelopedData(recipient.getBytes());
/*      */             
/*  983 */             Iterator<RecipientInformation> recipientCertificatesIt = data.getRecipientInfos().getRecipients().iterator();
/*      */             
/*  985 */             while (recipientCertificatesIt.hasNext()) {
/*  986 */               RecipientInformation recipientInfo = recipientCertificatesIt.next();
/*      */               
/*  988 */               if (recipientInfo.getRID().match(certHolder) && !foundRecipient) {
/*  989 */                 envelopedData = PdfEncryptor.getContent(recipientInfo, (PrivateKey)this.certificateKey, this.certificateKeyProvider);
/*  990 */                 foundRecipient = true;
/*      */               }
/*      */             
/*      */             } 
/*  994 */           } catch (Exception f) {
/*  995 */             throw new ExceptionConverter(f);
/*      */           } 
/*      */         } 
/*      */       } else {
/*  999 */         for (int i = 0; i < recipients.size(); i++) {
/* 1000 */           PdfObject recipient = recipients.getPdfObject(i);
/* 1001 */           this.strings.remove(recipient);
/*      */           
/* 1003 */           CMSEnvelopedData data = null;
/*      */           try {
/* 1005 */             data = new CMSEnvelopedData(recipient.getBytes());
/*      */ 
/*      */             
/* 1008 */             RecipientInformation recipientInfo = data.getRecipientInfos().get(this.externalDecryptionProcess.getCmsRecipientId());
/*      */             
/* 1010 */             if (recipientInfo != null) {
/*      */               
/* 1012 */               envelopedData = recipientInfo.getContent(this.externalDecryptionProcess.getCmsRecipient());
/* 1013 */               foundRecipient = true;
/*      */             } 
/* 1015 */           } catch (Exception f) {
/* 1016 */             throw new ExceptionConverter(f);
/*      */           } 
/*      */         } 
/*      */       } 
/*      */       
/* 1021 */       if (!foundRecipient || envelopedData == null) {
/* 1022 */         throw new UnsupportedPdfException(MessageLocalization.getComposedMessage("bad.certificate.and.key", new Object[0]));
/*      */       }
/*      */       
/* 1025 */       MessageDigest md = null;
/*      */       
/*      */       try {
/* 1028 */         if ((cryptoMode & 0x7) == 3) {
/* 1029 */           md = MessageDigest.getInstance("SHA-256");
/*      */         } else {
/* 1031 */           md = MessageDigest.getInstance("SHA-1");
/* 1032 */         }  md.update(envelopedData, 0, 20);
/* 1033 */         for (int i = 0; i < recipients.size(); i++) {
/* 1034 */           byte[] encodedRecipient = recipients.getPdfObject(i).getBytes();
/* 1035 */           md.update(encodedRecipient);
/*      */         } 
/* 1037 */         if ((cryptoMode & 0x8) != 0)
/* 1038 */           md.update(new byte[] { -1, -1, -1, -1 }); 
/* 1039 */         encryptionKey = md.digest();
/*      */       }
/* 1041 */       catch (Exception f) {
/* 1042 */         throw new ExceptionConverter(f);
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 1047 */     this.decrypt = new PdfEncryption();
/* 1048 */     this.decrypt.setCryptoMode(cryptoMode, lengthValue);
/*      */     
/* 1050 */     if (filter.equals(PdfName.STANDARD)) {
/* 1051 */       if (this.rValue == 5) {
/* 1052 */         this.ownerPasswordUsed = this.decrypt.readKey(enc, this.password);
/* 1053 */         this.decrypt.documentID = documentID;
/* 1054 */         this.pValue = this.decrypt.getPermissions();
/*      */       }
/*      */       else {
/*      */         
/* 1058 */         this.decrypt.setupByOwnerPassword(documentID, this.password, uValue, oValue, this.pValue);
/* 1059 */         if (!equalsArray(uValue, this.decrypt.userKey, (this.rValue == 3 || this.rValue == 4) ? 16 : 32)) {
/*      */           
/* 1061 */           this.decrypt.setupByUserPassword(documentID, this.password, oValue, this.pValue);
/* 1062 */           if (!equalsArray(uValue, this.decrypt.userKey, (this.rValue == 3 || this.rValue == 4) ? 16 : 32)) {
/* 1063 */             throw new BadPasswordException(MessageLocalization.getComposedMessage("bad.user.password", new Object[0]));
/*      */           }
/*      */         } else {
/*      */           
/* 1067 */           this.ownerPasswordUsed = true;
/*      */         } 
/*      */       } 
/* 1070 */     } else if (filter.equals(PdfName.PUBSEC)) {
/* 1071 */       this.decrypt.documentID = documentID;
/* 1072 */       if ((cryptoMode & 0x7) == 3) {
/* 1073 */         this.decrypt.setKey(encryptionKey);
/*      */       } else {
/* 1075 */         this.decrypt.setupByEncryptionKey(encryptionKey, lengthValue);
/* 1076 */       }  this.ownerPasswordUsed = true;
/*      */     } 
/*      */     
/* 1079 */     for (int k = 0; k < this.strings.size(); k++) {
/* 1080 */       PdfString str = this.strings.get(k);
/* 1081 */       str.decrypt(this);
/*      */     } 
/*      */     
/* 1084 */     if (encDic.isIndirect()) {
/* 1085 */       this.cryptoRef = (PRIndirectReference)encDic;
/* 1086 */       this.xrefObj.set(this.cryptoRef.getNumber(), null);
/*      */     } 
/* 1088 */     this.encryptionError = false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static PdfObject getPdfObjectRelease(PdfObject obj) {
/* 1096 */     PdfObject obj2 = getPdfObject(obj);
/* 1097 */     releaseLastXrefPartial(obj);
/* 1098 */     return obj2;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static PdfObject getPdfObject(PdfObject obj) {
/* 1109 */     if (obj == null)
/* 1110 */       return null; 
/* 1111 */     if (!obj.isIndirect())
/* 1112 */       return obj; 
/*      */     try {
/* 1114 */       PRIndirectReference ref = (PRIndirectReference)obj;
/* 1115 */       int idx = ref.getNumber();
/* 1116 */       boolean appendable = (ref.getReader()).appendable;
/* 1117 */       obj = ref.getReader().getPdfObject(idx);
/* 1118 */       if (obj == null) {
/* 1119 */         return null;
/*      */       }
/*      */       
/* 1122 */       if (appendable) {
/* 1123 */         switch (obj.type()) {
/*      */           case 8:
/* 1125 */             obj = new PdfNull();
/*      */             break;
/*      */           case 1:
/* 1128 */             obj = new PdfBoolean(((PdfBoolean)obj).booleanValue());
/*      */             break;
/*      */           case 4:
/* 1131 */             obj = new PdfName(obj.getBytes());
/*      */             break;
/*      */         } 
/* 1134 */         obj.setIndRef(ref);
/*      */       } 
/* 1136 */       return obj;
/*      */     
/*      */     }
/* 1139 */     catch (Exception e) {
/* 1140 */       throw new ExceptionConverter(e);
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
/*      */   public static PdfObject getPdfObjectRelease(PdfObject obj, PdfObject parent) {
/* 1153 */     PdfObject obj2 = getPdfObject(obj, parent);
/* 1154 */     releaseLastXrefPartial(obj);
/* 1155 */     return obj2;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static PdfObject getPdfObject(PdfObject obj, PdfObject parent) {
/* 1164 */     if (obj == null)
/* 1165 */       return null; 
/* 1166 */     if (!obj.isIndirect()) {
/* 1167 */       PRIndirectReference ref = null;
/* 1168 */       if (parent != null && (ref = parent.getIndRef()) != null && ref.getReader().isAppendable()) {
/* 1169 */         switch (obj.type()) {
/*      */           case 8:
/* 1171 */             obj = new PdfNull();
/*      */             break;
/*      */           case 1:
/* 1174 */             obj = new PdfBoolean(((PdfBoolean)obj).booleanValue());
/*      */             break;
/*      */           case 4:
/* 1177 */             obj = new PdfName(obj.getBytes());
/*      */             break;
/*      */         } 
/* 1180 */         obj.setIndRef(ref);
/*      */       } 
/* 1182 */       return obj;
/*      */     } 
/* 1184 */     return getPdfObject(obj);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PdfObject getPdfObjectRelease(int idx) {
/* 1192 */     PdfObject obj = getPdfObject(idx);
/* 1193 */     releaseLastXrefPartial();
/* 1194 */     return obj;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PdfObject getPdfObject(int idx) {
/*      */     try {
/* 1203 */       this.lastXrefPartial = -1;
/* 1204 */       if (idx < 0 || idx >= this.xrefObj.size())
/* 1205 */         return null; 
/* 1206 */       PdfObject obj = this.xrefObj.get(idx);
/* 1207 */       if (!this.partial || obj != null)
/* 1208 */         return obj; 
/* 1209 */       if (idx * 2 >= this.xref.length)
/* 1210 */         return null; 
/* 1211 */       obj = readSingleObject(idx);
/* 1212 */       this.lastXrefPartial = -1;
/* 1213 */       if (obj != null)
/* 1214 */         this.lastXrefPartial = idx; 
/* 1215 */       return obj;
/*      */     }
/* 1217 */     catch (Exception e) {
/* 1218 */       throw new ExceptionConverter(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void resetLastXrefPartial() {
/* 1226 */     this.lastXrefPartial = -1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void releaseLastXrefPartial() {
/* 1233 */     if (this.partial && this.lastXrefPartial != -1) {
/* 1234 */       this.xrefObj.set(this.lastXrefPartial, null);
/* 1235 */       this.lastXrefPartial = -1;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void releaseLastXrefPartial(PdfObject obj) {
/* 1243 */     if (obj == null)
/*      */       return; 
/* 1245 */     if (!obj.isIndirect())
/*      */       return; 
/* 1247 */     if (!(obj instanceof PRIndirectReference)) {
/*      */       return;
/*      */     }
/* 1250 */     PRIndirectReference ref = (PRIndirectReference)obj;
/* 1251 */     PdfReader reader = ref.getReader();
/* 1252 */     if (reader.partial && reader.lastXrefPartial != -1 && reader.lastXrefPartial == ref.getNumber()) {
/* 1253 */       reader.xrefObj.set(reader.lastXrefPartial, null);
/*      */     }
/* 1255 */     reader.lastXrefPartial = -1;
/*      */   }
/*      */   
/*      */   private void setXrefPartialObject(int idx, PdfObject obj) {
/* 1259 */     if (!this.partial || idx < 0)
/*      */       return; 
/* 1261 */     this.xrefObj.set(idx, obj);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PRIndirectReference addPdfObject(PdfObject obj) {
/* 1269 */     this.xrefObj.add(obj);
/* 1270 */     return new PRIndirectReference(this, this.xrefObj.size() - 1);
/*      */   }
/*      */   
/*      */   protected void readPages() throws IOException {
/* 1274 */     this.catalog = this.trailer.getAsDict(PdfName.ROOT);
/* 1275 */     if (this.catalog == null) {
/* 1276 */       throw new InvalidPdfException(MessageLocalization.getComposedMessage("the.document.has.no.catalog.object", new Object[0]));
/*      */     }
/* 1278 */     this.rootPages = this.catalog.getAsDict(PdfName.PAGES);
/* 1279 */     if (this.rootPages == null || (!PdfName.PAGES.equals(this.rootPages.get(PdfName.TYPE)) && !PdfName.PAGES.equals(this.rootPages.get(new PdfName("Types"))))) {
/* 1280 */       if (debugmode) {
/* 1281 */         if (LOGGER.isLogging(Level.ERROR)) {
/* 1282 */           LOGGER.error(MessageLocalization.getComposedMessage("the.document.has.no.page.root", new Object[0]));
/*      */         }
/*      */       } else {
/*      */         
/* 1286 */         throw new InvalidPdfException(MessageLocalization.getComposedMessage("the.document.has.no.page.root", new Object[0]));
/*      */       } 
/*      */     }
/* 1289 */     this.pageRefs = new PageRefs(this);
/*      */   }
/*      */   
/*      */   protected void readDocObjPartial() throws IOException {
/* 1293 */     this.xrefObj = new ArrayList<PdfObject>(this.xref.length / 2);
/* 1294 */     this.xrefObj.addAll(Collections.nCopies(this.xref.length / 2, null));
/* 1295 */     readDecryptedDocObj();
/* 1296 */     if (this.objStmToOffset != null) {
/* 1297 */       long[] keys = this.objStmToOffset.getKeys();
/* 1298 */       for (int k = 0; k < keys.length; k++) {
/* 1299 */         long n = keys[k];
/* 1300 */         this.objStmToOffset.put(n, this.xref[(int)(n * 2L)]);
/* 1301 */         this.xref[(int)(n * 2L)] = -1L;
/*      */       } 
/*      */     } 
/*      */   }
/*      */   protected PdfObject readSingleObject(int k) throws IOException {
/*      */     PdfObject obj;
/* 1307 */     this.strings.clear();
/* 1308 */     int k2 = k * 2;
/* 1309 */     long pos = this.xref[k2];
/* 1310 */     if (pos < 0L)
/* 1311 */       return null; 
/* 1312 */     if (this.xref[k2 + 1] > 0L)
/* 1313 */       pos = this.objStmToOffset.get(this.xref[k2 + 1]); 
/* 1314 */     if (pos == 0L)
/* 1315 */       return null; 
/* 1316 */     this.tokens.seek(pos);
/* 1317 */     this.tokens.nextValidToken();
/* 1318 */     if (this.tokens.getTokenType() != PRTokeniser.TokenType.NUMBER)
/* 1319 */       this.tokens.throwError(MessageLocalization.getComposedMessage("invalid.object.number", new Object[0])); 
/* 1320 */     this.objNum = this.tokens.intValue();
/* 1321 */     this.tokens.nextValidToken();
/* 1322 */     if (this.tokens.getTokenType() != PRTokeniser.TokenType.NUMBER)
/* 1323 */       this.tokens.throwError(MessageLocalization.getComposedMessage("invalid.generation.number", new Object[0])); 
/* 1324 */     this.objGen = this.tokens.intValue();
/* 1325 */     this.tokens.nextValidToken();
/* 1326 */     if (!this.tokens.getStringValue().equals("obj")) {
/* 1327 */       this.tokens.throwError(MessageLocalization.getComposedMessage("token.obj.expected", new Object[0]));
/*      */     }
/*      */     try {
/* 1330 */       obj = readPRObject();
/* 1331 */       for (int j = 0; j < this.strings.size(); j++) {
/* 1332 */         PdfString str = this.strings.get(j);
/* 1333 */         str.decrypt(this);
/*      */       } 
/* 1335 */       if (obj.isStream()) {
/* 1336 */         checkPRStreamLength((PRStream)obj);
/*      */       }
/*      */     }
/* 1339 */     catch (IOException e) {
/* 1340 */       if (debugmode) {
/* 1341 */         if (LOGGER.isLogging(Level.ERROR))
/* 1342 */           LOGGER.error(e.getMessage(), e); 
/* 1343 */         obj = null;
/*      */       } else {
/*      */         
/* 1346 */         throw e;
/*      */       } 
/* 1348 */     }  if (this.xref[k2 + 1] > 0L) {
/* 1349 */       obj = readOneObjStm((PRStream)obj, (int)this.xref[k2]);
/*      */     }
/* 1351 */     this.xrefObj.set(k, obj);
/* 1352 */     return obj;
/*      */   }
/*      */   
/*      */   protected PdfObject readOneObjStm(PRStream stream, int idx) throws IOException {
/* 1356 */     int first = stream.getAsNumber(PdfName.FIRST).intValue();
/* 1357 */     byte[] b = getStreamBytes(stream, this.tokens.getFile());
/* 1358 */     PRTokeniser saveTokens = this.tokens;
/* 1359 */     this.tokens = new PRTokeniser(new RandomAccessFileOrArray((new RandomAccessSourceFactory()).createSource(b))); try {
/*      */       PdfObject obj;
/* 1361 */       int address = 0;
/* 1362 */       boolean ok = true;
/* 1363 */       idx++;
/* 1364 */       for (int k = 0; k < idx; k++) {
/* 1365 */         ok = this.tokens.nextToken();
/* 1366 */         if (!ok)
/*      */           break; 
/* 1368 */         if (this.tokens.getTokenType() != PRTokeniser.TokenType.NUMBER) {
/* 1369 */           ok = false;
/*      */           break;
/*      */         } 
/* 1372 */         ok = this.tokens.nextToken();
/* 1373 */         if (!ok)
/*      */           break; 
/* 1375 */         if (this.tokens.getTokenType() != PRTokeniser.TokenType.NUMBER) {
/* 1376 */           ok = false;
/*      */           break;
/*      */         } 
/* 1379 */         address = this.tokens.intValue() + first;
/*      */       } 
/* 1381 */       if (!ok)
/* 1382 */         throw new InvalidPdfException(MessageLocalization.getComposedMessage("error.reading.objstm", new Object[0])); 
/* 1383 */       this.tokens.seek(address);
/* 1384 */       this.tokens.nextToken();
/*      */       
/* 1386 */       if (this.tokens.getTokenType() == PRTokeniser.TokenType.NUMBER) {
/* 1387 */         obj = new PdfNumber(this.tokens.getStringValue());
/*      */       } else {
/*      */         
/* 1390 */         this.tokens.seek(address);
/* 1391 */         obj = readPRObject();
/*      */       } 
/* 1393 */       return obj;
/*      */     }
/*      */     finally {
/*      */       
/* 1397 */       this.tokens = saveTokens;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public double dumpPerc() {
/* 1405 */     int total = 0;
/* 1406 */     for (int k = 0; k < this.xrefObj.size(); k++) {
/* 1407 */       if (this.xrefObj.get(k) != null)
/* 1408 */         total++; 
/*      */     } 
/* 1410 */     return total * 100.0D / this.xrefObj.size();
/*      */   }
/*      */   
/*      */   protected void readDocObj() throws IOException {
/* 1414 */     ArrayList<PRStream> streams = new ArrayList<PRStream>();
/* 1415 */     this.xrefObj = new ArrayList<PdfObject>(this.xref.length / 2);
/* 1416 */     this.xrefObj.addAll(Collections.nCopies(this.xref.length / 2, null)); int k;
/* 1417 */     for (k = 2; k < this.xref.length; k += 2) {
/* 1418 */       long pos = this.xref[k];
/* 1419 */       if (pos > 0L && this.xref[k + 1] <= 0L) {
/*      */         PdfObject obj;
/* 1421 */         this.tokens.seek(pos);
/* 1422 */         this.tokens.nextValidToken();
/* 1423 */         if (this.tokens.getTokenType() != PRTokeniser.TokenType.NUMBER)
/* 1424 */           this.tokens.throwError(MessageLocalization.getComposedMessage("invalid.object.number", new Object[0])); 
/* 1425 */         this.objNum = this.tokens.intValue();
/* 1426 */         this.tokens.nextValidToken();
/* 1427 */         if (this.tokens.getTokenType() != PRTokeniser.TokenType.NUMBER)
/* 1428 */           this.tokens.throwError(MessageLocalization.getComposedMessage("invalid.generation.number", new Object[0])); 
/* 1429 */         this.objGen = this.tokens.intValue();
/* 1430 */         this.tokens.nextValidToken();
/* 1431 */         if (!this.tokens.getStringValue().equals("obj")) {
/* 1432 */           this.tokens.throwError(MessageLocalization.getComposedMessage("token.obj.expected", new Object[0]));
/*      */         }
/*      */         try {
/* 1435 */           obj = readPRObject();
/* 1436 */           if (obj.isStream()) {
/* 1437 */             streams.add((PRStream)obj);
/*      */           }
/*      */         }
/* 1440 */         catch (IOException e) {
/* 1441 */           if (debugmode) {
/* 1442 */             if (LOGGER.isLogging(Level.ERROR))
/* 1443 */               LOGGER.error(e.getMessage(), e); 
/* 1444 */             obj = null;
/*      */           } else {
/*      */             
/* 1447 */             throw e;
/*      */           } 
/* 1449 */         }  this.xrefObj.set(k / 2, obj);
/*      */       } 
/* 1451 */     }  for (k = 0; k < streams.size(); k++) {
/* 1452 */       checkPRStreamLength(streams.get(k));
/*      */     }
/* 1454 */     readDecryptedDocObj();
/* 1455 */     if (this.objStmMark != null) {
/* 1456 */       for (Map.Entry<Integer, IntHashtable> entry : this.objStmMark.entrySet()) {
/* 1457 */         int n = ((Integer)entry.getKey()).intValue();
/* 1458 */         IntHashtable h = entry.getValue();
/* 1459 */         readObjStm((PRStream)this.xrefObj.get(n), h);
/* 1460 */         this.xrefObj.set(n, null);
/*      */       } 
/* 1462 */       this.objStmMark = null;
/*      */     } 
/* 1464 */     this.xref = null;
/*      */   }
/*      */   
/*      */   private void checkPRStreamLength(PRStream stream) throws IOException {
/* 1468 */     long fileLength = this.tokens.length();
/* 1469 */     long start = stream.getOffset();
/* 1470 */     boolean calc = false;
/* 1471 */     long streamLength = 0L;
/* 1472 */     PdfObject obj = getPdfObjectRelease(stream.get(PdfName.LENGTH));
/* 1473 */     if (obj != null && obj.type() == 2) {
/* 1474 */       streamLength = ((PdfNumber)obj).intValue();
/* 1475 */       if (streamLength + start > fileLength - 20L) {
/* 1476 */         calc = true;
/*      */       } else {
/* 1478 */         this.tokens.seek(start + streamLength);
/* 1479 */         String line = this.tokens.readString(20);
/* 1480 */         if (!line.startsWith("\nendstream") && 
/* 1481 */           !line.startsWith("\r\nendstream") && 
/* 1482 */           !line.startsWith("\rendstream") && 
/* 1483 */           !line.startsWith("endstream")) {
/* 1484 */           calc = true;
/*      */         }
/*      */       } 
/*      */     } else {
/* 1488 */       calc = true;
/* 1489 */     }  if (calc) {
/* 1490 */       long pos; byte[] tline = new byte[16];
/* 1491 */       this.tokens.seek(start);
/*      */       
/*      */       while (true) {
/* 1494 */         pos = this.tokens.getFilePointer();
/* 1495 */         if (!this.tokens.readLineSegment(tline, false))
/*      */           break; 
/* 1497 */         if (equalsn(tline, endstream)) {
/* 1498 */           streamLength = pos - start;
/*      */           break;
/*      */         } 
/* 1501 */         if (equalsn(tline, endobj)) {
/* 1502 */           this.tokens.seek(pos - 16L);
/* 1503 */           String s = this.tokens.readString(16);
/* 1504 */           int index = s.indexOf("endstream");
/* 1505 */           if (index >= 0)
/* 1506 */             pos = pos - 16L + index; 
/* 1507 */           streamLength = pos - start;
/*      */           break;
/*      */         } 
/*      */       } 
/* 1511 */       this.tokens.seek(pos - 2L);
/* 1512 */       if (this.tokens.read() == 13)
/* 1513 */         streamLength--; 
/* 1514 */       this.tokens.seek(pos - 1L);
/* 1515 */       if (this.tokens.read() == 10) {
/* 1516 */         streamLength--;
/*      */       }
/* 1518 */       if (streamLength < 0L) {
/* 1519 */         streamLength = 0L;
/*      */       }
/*      */     } 
/* 1522 */     stream.setLength((int)streamLength);
/*      */   }
/*      */   
/*      */   protected void readObjStm(PRStream stream, IntHashtable map) throws IOException {
/* 1526 */     if (stream == null)
/* 1527 */       return;  int first = stream.getAsNumber(PdfName.FIRST).intValue();
/* 1528 */     int n = stream.getAsNumber(PdfName.N).intValue();
/* 1529 */     byte[] b = getStreamBytes(stream, this.tokens.getFile());
/* 1530 */     PRTokeniser saveTokens = this.tokens;
/* 1531 */     this.tokens = new PRTokeniser(new RandomAccessFileOrArray((new RandomAccessSourceFactory()).createSource(b)));
/*      */     try {
/* 1533 */       int[] address = new int[n];
/* 1534 */       int[] objNumber = new int[n];
/* 1535 */       boolean ok = true; int k;
/* 1536 */       for (k = 0; k < n; k++) {
/* 1537 */         ok = this.tokens.nextToken();
/* 1538 */         if (!ok)
/*      */           break; 
/* 1540 */         if (this.tokens.getTokenType() != PRTokeniser.TokenType.NUMBER) {
/* 1541 */           ok = false;
/*      */           break;
/*      */         } 
/* 1544 */         objNumber[k] = this.tokens.intValue();
/* 1545 */         ok = this.tokens.nextToken();
/* 1546 */         if (!ok)
/*      */           break; 
/* 1548 */         if (this.tokens.getTokenType() != PRTokeniser.TokenType.NUMBER) {
/* 1549 */           ok = false;
/*      */           break;
/*      */         } 
/* 1552 */         address[k] = this.tokens.intValue() + first;
/*      */       } 
/* 1554 */       if (!ok)
/* 1555 */         throw new InvalidPdfException(MessageLocalization.getComposedMessage("error.reading.objstm", new Object[0])); 
/* 1556 */       for (k = 0; k < n; k++) {
/* 1557 */         if (map.containsKey(k)) {
/* 1558 */           PdfObject obj; this.tokens.seek(address[k]);
/* 1559 */           this.tokens.nextToken();
/*      */           
/* 1561 */           if (this.tokens.getTokenType() == PRTokeniser.TokenType.NUMBER) {
/* 1562 */             obj = new PdfNumber(this.tokens.getStringValue());
/*      */           } else {
/*      */             
/* 1565 */             this.tokens.seek(address[k]);
/* 1566 */             obj = readPRObject();
/*      */           } 
/* 1568 */           this.xrefObj.set(objNumber[k], obj);
/*      */         } 
/*      */       } 
/*      */     } finally {
/*      */       
/* 1573 */       this.tokens = saveTokens;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static PdfObject killIndirect(PdfObject obj) {
/* 1584 */     if (obj == null || obj.isNull())
/* 1585 */       return null; 
/* 1586 */     PdfObject ret = getPdfObjectRelease(obj);
/* 1587 */     if (obj.isIndirect()) {
/* 1588 */       PRIndirectReference ref = (PRIndirectReference)obj;
/* 1589 */       PdfReader reader = ref.getReader();
/* 1590 */       int n = ref.getNumber();
/* 1591 */       reader.xrefObj.set(n, null);
/* 1592 */       if (reader.partial)
/* 1593 */         reader.xref[n * 2] = -1L; 
/*      */     } 
/* 1595 */     return ret;
/*      */   }
/*      */   
/*      */   private void ensureXrefSize(int size) {
/* 1599 */     if (size == 0)
/*      */       return; 
/* 1601 */     if (this.xref == null) {
/* 1602 */       this.xref = new long[size];
/*      */     }
/* 1604 */     else if (this.xref.length < size) {
/* 1605 */       long[] xref2 = new long[size];
/* 1606 */       System.arraycopy(this.xref, 0, xref2, 0, this.xref.length);
/* 1607 */       this.xref = xref2;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected void readXref() throws IOException {
/* 1613 */     this.hybridXref = false;
/* 1614 */     this.newXrefType = false;
/* 1615 */     this.tokens.seek(this.tokens.getStartxref());
/* 1616 */     this.tokens.nextToken();
/* 1617 */     if (!this.tokens.getStringValue().equals("startxref"))
/* 1618 */       throw new InvalidPdfException(MessageLocalization.getComposedMessage("startxref.not.found", new Object[0])); 
/* 1619 */     this.tokens.nextToken();
/* 1620 */     if (this.tokens.getTokenType() != PRTokeniser.TokenType.NUMBER)
/* 1621 */       throw new InvalidPdfException(MessageLocalization.getComposedMessage("startxref.is.not.followed.by.a.number", new Object[0])); 
/* 1622 */     long startxref = this.tokens.longValue();
/* 1623 */     this.lastXref = startxref;
/* 1624 */     this.eofPos = this.tokens.getFilePointer();
/*      */     try {
/* 1626 */       if (readXRefStream(startxref)) {
/* 1627 */         this.newXrefType = true;
/*      */         
/*      */         return;
/*      */       } 
/* 1631 */     } catch (Exception exception) {}
/* 1632 */     this.xref = null;
/* 1633 */     this.tokens.seek(startxref);
/* 1634 */     this.trailer = readXrefSection();
/* 1635 */     PdfDictionary trailer2 = this.trailer;
/*      */     while (true) {
/* 1637 */       PdfNumber prev = (PdfNumber)trailer2.get(PdfName.PREV);
/* 1638 */       if (prev == null)
/*      */         break; 
/* 1640 */       if (prev.longValue() == startxref)
/* 1641 */         throw new InvalidPdfException(MessageLocalization.getComposedMessage("trailer.prev.entry.points.to.its.own.cross.reference.section", new Object[0])); 
/* 1642 */       startxref = prev.longValue();
/* 1643 */       this.tokens.seek(startxref);
/* 1644 */       trailer2 = readXrefSection();
/*      */     } 
/*      */   }
/*      */   
/*      */   protected PdfDictionary readXrefSection() throws IOException {
/* 1649 */     this.tokens.nextValidToken();
/* 1650 */     if (!this.tokens.getStringValue().equals("xref"))
/* 1651 */       this.tokens.throwError(MessageLocalization.getComposedMessage("xref.subsection.not.found", new Object[0])); 
/* 1652 */     int start = 0;
/* 1653 */     int end = 0;
/* 1654 */     long pos = 0L;
/* 1655 */     int gen = 0;
/*      */     while (true) {
/* 1657 */       this.tokens.nextValidToken();
/* 1658 */       if (this.tokens.getStringValue().equals("trailer"))
/*      */         break; 
/* 1660 */       if (this.tokens.getTokenType() != PRTokeniser.TokenType.NUMBER)
/* 1661 */         this.tokens.throwError(MessageLocalization.getComposedMessage("object.number.of.the.first.object.in.this.xref.subsection.not.found", new Object[0])); 
/* 1662 */       start = this.tokens.intValue();
/* 1663 */       this.tokens.nextValidToken();
/* 1664 */       if (this.tokens.getTokenType() != PRTokeniser.TokenType.NUMBER)
/* 1665 */         this.tokens.throwError(MessageLocalization.getComposedMessage("number.of.entries.in.this.xref.subsection.not.found", new Object[0])); 
/* 1666 */       end = this.tokens.intValue() + start;
/* 1667 */       if (start == 1) {
/* 1668 */         long back = this.tokens.getFilePointer();
/* 1669 */         this.tokens.nextValidToken();
/* 1670 */         pos = this.tokens.longValue();
/* 1671 */         this.tokens.nextValidToken();
/* 1672 */         gen = this.tokens.intValue();
/* 1673 */         if (pos == 0L && gen == 65535) {
/* 1674 */           start--;
/* 1675 */           end--;
/*      */         } 
/* 1677 */         this.tokens.seek(back);
/*      */       } 
/* 1679 */       ensureXrefSize(end * 2);
/* 1680 */       for (int k = start; k < end; k++) {
/* 1681 */         this.tokens.nextValidToken();
/* 1682 */         pos = this.tokens.longValue();
/* 1683 */         this.tokens.nextValidToken();
/* 1684 */         gen = this.tokens.intValue();
/* 1685 */         this.tokens.nextValidToken();
/* 1686 */         int p = k * 2;
/* 1687 */         if (this.tokens.getStringValue().equals("n")) {
/* 1688 */           if (this.xref[p] == 0L && this.xref[p + 1] == 0L)
/*      */           {
/*      */             
/* 1691 */             this.xref[p] = pos;
/*      */           }
/*      */         }
/* 1694 */         else if (this.tokens.getStringValue().equals("f")) {
/* 1695 */           if (this.xref[p] == 0L && this.xref[p + 1] == 0L) {
/* 1696 */             this.xref[p] = -1L;
/*      */           }
/*      */         } else {
/* 1699 */           this.tokens.throwError(MessageLocalization.getComposedMessage("invalid.cross.reference.entry.in.this.xref.subsection", new Object[0]));
/*      */         } 
/*      */       } 
/* 1702 */     }  PdfDictionary trailer = (PdfDictionary)readPRObject();
/* 1703 */     PdfNumber xrefSize = (PdfNumber)trailer.get(PdfName.SIZE);
/* 1704 */     ensureXrefSize(xrefSize.intValue() * 2);
/* 1705 */     PdfObject xrs = trailer.get(PdfName.XREFSTM);
/* 1706 */     if (xrs != null && xrs.isNumber()) {
/* 1707 */       int loc = ((PdfNumber)xrs).intValue();
/*      */       try {
/* 1709 */         readXRefStream(loc);
/* 1710 */         this.newXrefType = true;
/* 1711 */         this.hybridXref = true;
/*      */       }
/* 1713 */       catch (IOException e) {
/* 1714 */         this.xref = null;
/* 1715 */         throw e;
/*      */       } 
/*      */     } 
/* 1718 */     return trailer;
/*      */   }
/*      */   protected boolean readXRefStream(long ptr) throws IOException {
/*      */     PdfArray index;
/* 1722 */     this.tokens.seek(ptr);
/* 1723 */     int thisStream = 0;
/* 1724 */     if (!this.tokens.nextToken())
/* 1725 */       return false; 
/* 1726 */     if (this.tokens.getTokenType() != PRTokeniser.TokenType.NUMBER)
/* 1727 */       return false; 
/* 1728 */     thisStream = this.tokens.intValue();
/* 1729 */     if (!this.tokens.nextToken() || this.tokens.getTokenType() != PRTokeniser.TokenType.NUMBER)
/* 1730 */       return false; 
/* 1731 */     if (!this.tokens.nextToken() || !this.tokens.getStringValue().equals("obj"))
/* 1732 */       return false; 
/* 1733 */     PdfObject object = readPRObject();
/* 1734 */     PRStream stm = null;
/* 1735 */     if (object.isStream()) {
/* 1736 */       stm = (PRStream)object;
/* 1737 */       if (!PdfName.XREF.equals(stm.get(PdfName.TYPE))) {
/* 1738 */         return false;
/*      */       }
/*      */     } else {
/* 1741 */       return false;
/* 1742 */     }  if (this.trailer == null) {
/* 1743 */       this.trailer = new PdfDictionary();
/* 1744 */       this.trailer.putAll(stm);
/*      */     } 
/* 1746 */     stm.setLength(((PdfNumber)stm.get(PdfName.LENGTH)).intValue());
/* 1747 */     int size = ((PdfNumber)stm.get(PdfName.SIZE)).intValue();
/*      */     
/* 1749 */     PdfObject obj = stm.get(PdfName.INDEX);
/* 1750 */     if (obj == null) {
/* 1751 */       index = new PdfArray();
/* 1752 */       index.add(new int[] { 0, size });
/*      */     } else {
/*      */       
/* 1755 */       index = (PdfArray)obj;
/* 1756 */     }  PdfArray w = (PdfArray)stm.get(PdfName.W);
/* 1757 */     long prev = -1L;
/* 1758 */     obj = stm.get(PdfName.PREV);
/* 1759 */     if (obj != null) {
/* 1760 */       prev = ((PdfNumber)obj).longValue();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1765 */     ensureXrefSize(size * 2);
/* 1766 */     if (this.objStmMark == null && !this.partial)
/* 1767 */       this.objStmMark = new HashMap<Integer, IntHashtable>(); 
/* 1768 */     if (this.objStmToOffset == null && this.partial)
/* 1769 */       this.objStmToOffset = new LongHashtable(); 
/* 1770 */     byte[] b = getStreamBytes(stm, this.tokens.getFile());
/* 1771 */     int bptr = 0;
/* 1772 */     int[] wc = new int[3];
/* 1773 */     for (int k = 0; k < 3; k++)
/* 1774 */       wc[k] = w.getAsNumber(k).intValue(); 
/* 1775 */     for (int idx = 0; idx < index.size(); idx += 2) {
/* 1776 */       int start = index.getAsNumber(idx).intValue();
/* 1777 */       int length = index.getAsNumber(idx + 1).intValue();
/* 1778 */       ensureXrefSize((start + length) * 2);
/* 1779 */       while (length-- > 0) {
/* 1780 */         int type = 1;
/* 1781 */         if (wc[0] > 0) {
/* 1782 */           type = 0;
/* 1783 */           for (int m = 0; m < wc[0]; m++)
/* 1784 */             type = (type << 8) + (b[bptr++] & 0xFF); 
/*      */         } 
/* 1786 */         long field2 = 0L;
/* 1787 */         for (int i = 0; i < wc[1]; i++)
/* 1788 */           field2 = (field2 << 8L) + (b[bptr++] & 0xFF); 
/* 1789 */         int field3 = 0;
/* 1790 */         for (int j = 0; j < wc[2]; j++)
/* 1791 */           field3 = (field3 << 8) + (b[bptr++] & 0xFF); 
/* 1792 */         int base = start * 2;
/* 1793 */         if (this.xref[base] == 0L && this.xref[base + 1] == 0L) {
/* 1794 */           Integer on; IntHashtable seq; switch (type) {
/*      */             case 0:
/* 1796 */               this.xref[base] = -1L;
/*      */               break;
/*      */             case 1:
/* 1799 */               this.xref[base] = field2;
/*      */               break;
/*      */             case 2:
/* 1802 */               this.xref[base] = field3;
/* 1803 */               this.xref[base + 1] = field2;
/* 1804 */               if (this.partial) {
/* 1805 */                 this.objStmToOffset.put(field2, 0L);
/*      */                 break;
/*      */               } 
/* 1808 */               on = Integer.valueOf((int)field2);
/* 1809 */               seq = this.objStmMark.get(on);
/* 1810 */               if (seq == null) {
/* 1811 */                 seq = new IntHashtable();
/* 1812 */                 seq.put(field3, 1);
/* 1813 */                 this.objStmMark.put(on, seq);
/*      */                 break;
/*      */               } 
/* 1816 */               seq.put(field3, 1);
/*      */               break;
/*      */           } 
/*      */         
/*      */         } 
/* 1821 */         start++;
/*      */       } 
/*      */     } 
/* 1824 */     thisStream *= 2;
/* 1825 */     if (thisStream + 1 < this.xref.length && this.xref[thisStream] == 0L && this.xref[thisStream + 1] == 0L) {
/* 1826 */       this.xref[thisStream] = -1L;
/*      */     }
/* 1828 */     if (prev == -1L)
/* 1829 */       return true; 
/* 1830 */     return readXRefStream(prev);
/*      */   }
/*      */   
/*      */   protected void rebuildXref() throws IOException {
/* 1834 */     this.hybridXref = false;
/* 1835 */     this.newXrefType = false;
/* 1836 */     this.tokens.seek(0L);
/* 1837 */     long[][] xr = new long[1024][];
/* 1838 */     long top = 0L;
/* 1839 */     this.trailer = null;
/* 1840 */     byte[] line = new byte[64];
/*      */     while (true) {
/* 1842 */       long pos = this.tokens.getFilePointer();
/* 1843 */       if (!this.tokens.readLineSegment(line, true))
/*      */         break; 
/* 1845 */       if (line[0] == 116) {
/* 1846 */         if (!PdfEncodings.convertToString(line, null).startsWith("trailer"))
/*      */           continue; 
/* 1848 */         this.tokens.seek(pos);
/* 1849 */         this.tokens.nextToken();
/* 1850 */         pos = this.tokens.getFilePointer();
/*      */         try {
/* 1852 */           PdfDictionary dic = (PdfDictionary)readPRObject();
/* 1853 */           if (dic.get(PdfName.ROOT) != null) {
/* 1854 */             this.trailer = dic; continue;
/*      */           } 
/* 1856 */           this.tokens.seek(pos);
/*      */         }
/* 1858 */         catch (Exception e) {
/* 1859 */           this.tokens.seek(pos);
/*      */         }  continue;
/*      */       } 
/* 1862 */       if (line[0] >= 48 && line[0] <= 57) {
/* 1863 */         long[] obj = PRTokeniser.checkObjectStart(line);
/* 1864 */         if (obj == null)
/*      */           continue; 
/* 1866 */         long num = obj[0];
/* 1867 */         long gen = obj[1];
/* 1868 */         if (num >= xr.length) {
/* 1869 */           long newLength = num * 2L;
/* 1870 */           long[][] xr2 = new long[(int)newLength][];
/* 1871 */           System.arraycopy(xr, 0, xr2, 0, (int)top);
/* 1872 */           xr = xr2;
/*      */         } 
/* 1874 */         if (num >= top)
/* 1875 */           top = num + 1L; 
/* 1876 */         if (xr[(int)num] == null || gen >= xr[(int)num][1]) {
/* 1877 */           obj[0] = pos;
/* 1878 */           xr[(int)num] = obj;
/*      */         } 
/*      */       } 
/*      */     } 
/* 1882 */     if (this.trailer == null)
/* 1883 */       throw new InvalidPdfException(MessageLocalization.getComposedMessage("trailer.not.found", new Object[0])); 
/* 1884 */     this.xref = new long[(int)(top * 2L)];
/* 1885 */     for (int k = 0; k < top; k++) {
/* 1886 */       long[] obj = xr[k];
/* 1887 */       if (obj != null)
/* 1888 */         this.xref[k * 2] = obj[0]; 
/*      */     } 
/*      */   }
/*      */   
/*      */   protected PdfDictionary readDictionary() throws IOException {
/* 1893 */     PdfDictionary dic = new PdfDictionary();
/*      */     while (true) {
/* 1895 */       this.tokens.nextValidToken();
/* 1896 */       if (this.tokens.getTokenType() == PRTokeniser.TokenType.END_DIC)
/*      */         break; 
/* 1898 */       if (this.tokens.getTokenType() != PRTokeniser.TokenType.NAME)
/* 1899 */         this.tokens.throwError(MessageLocalization.getComposedMessage("dictionary.key.1.is.not.a.name", new Object[] { this.tokens.getStringValue() })); 
/* 1900 */       PdfName name = new PdfName(this.tokens.getStringValue(), false);
/* 1901 */       PdfObject obj = readPRObject();
/* 1902 */       int type = obj.type();
/* 1903 */       if (-type == PRTokeniser.TokenType.END_DIC.ordinal())
/* 1904 */         this.tokens.throwError(MessageLocalization.getComposedMessage("unexpected.gt.gt", new Object[0])); 
/* 1905 */       if (-type == PRTokeniser.TokenType.END_ARRAY.ordinal())
/* 1906 */         this.tokens.throwError(MessageLocalization.getComposedMessage("unexpected.close.bracket", new Object[0])); 
/* 1907 */       dic.put(name, obj);
/*      */     } 
/* 1909 */     return dic;
/*      */   }
/*      */   
/*      */   protected PdfArray readArray() throws IOException {
/* 1913 */     PdfArray array = new PdfArray();
/*      */     while (true) {
/* 1915 */       PdfObject obj = readPRObject();
/* 1916 */       int type = obj.type();
/* 1917 */       if (-type == PRTokeniser.TokenType.END_ARRAY.ordinal())
/*      */         break; 
/* 1919 */       if (-type == PRTokeniser.TokenType.END_DIC.ordinal())
/* 1920 */         this.tokens.throwError(MessageLocalization.getComposedMessage("unexpected.gt.gt", new Object[0])); 
/* 1921 */       array.add(obj);
/*      */     } 
/* 1923 */     return array;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private PdfReader(RandomAccessSource byteSource, ReaderProperties properties) throws IOException {
/* 1929 */     this.readDepth = 0; this.certificate = properties.certificate; this.certificateKey = properties.certificateKey; this.certificateKeyProvider = properties.certificateKeyProvider; this.externalDecryptionProcess = properties.externalDecryptionProcess; this.password = properties.ownerPassword; this.partial = properties.partialRead; this.memoryLimitsAwareHandler = properties.memoryLimitsAwareHandler; try { this.tokens = getOffsetTokeniser(byteSource); if (this.partial) { readPdfPartial(); } else { readPdf(); }  } catch (IOException e) { if (properties.closeSourceOnconstructorError) byteSource.close();  throw e; }  getCounter().read(this.fileLength); } public PdfReader(PdfReader reader) { this.readDepth = 0; this.appendable = reader.appendable; this.consolidateNamedDestinations = reader.consolidateNamedDestinations; this.encrypted = reader.encrypted; this.rebuilt = reader.rebuilt; this.sharedStreams = reader.sharedStreams; this.tampered = reader.tampered; this.password = reader.password; this.pdfVersion = reader.pdfVersion; this.eofPos = reader.eofPos; this.freeXref = reader.freeXref; this.lastXref = reader.lastXref; this.newXrefType = reader.newXrefType; this.tokens = new PRTokeniser(reader.tokens.getSafeFile()); if (reader.decrypt != null)
/*      */       this.decrypt = new PdfEncryption(reader.decrypt);  this.pValue = reader.pValue; this.rValue = reader.rValue; this.xrefObj = new ArrayList<PdfObject>(reader.xrefObj); for (int k = 0; k < reader.xrefObj.size(); k++)
/*      */       this.xrefObj.set(k, duplicatePdfObject(reader.xrefObj.get(k), this));  this.pageRefs = new PageRefs(reader.pageRefs, this); this.trailer = (PdfDictionary)duplicatePdfObject(reader.trailer, this); this.catalog = this.trailer.getAsDict(PdfName.ROOT); this.rootPages = this.catalog.getAsDict(PdfName.PAGES); this.fileLength = reader.fileLength; this.partial = reader.partial; this.hybridXref = reader.hybridXref; this.objStmToOffset = reader.objStmToOffset; this.xref = reader.xref; this.cryptoRef = (PRIndirectReference)duplicatePdfObject(reader.cryptoRef, this); this.ownerPasswordUsed = reader.ownerPasswordUsed; } protected PdfObject readPRObject() throws IOException { PdfDictionary dic; PdfArray arr; PdfString str; long pos; PdfName cachedName; int num; boolean hasNext;
/* 1932 */     this.tokens.nextValidToken();
/* 1933 */     PRTokeniser.TokenType type = this.tokens.getTokenType();
/* 1934 */     switch (type) {
/*      */       case START_DIC:
/* 1936 */         this.readDepth++;
/* 1937 */         dic = readDictionary();
/* 1938 */         this.readDepth--;
/* 1939 */         pos = this.tokens.getFilePointer();
/*      */ 
/*      */         
/*      */         do {
/* 1943 */           hasNext = this.tokens.nextToken();
/* 1944 */         } while (hasNext && this.tokens.getTokenType() == PRTokeniser.TokenType.COMMENT);
/*      */         
/* 1946 */         if (hasNext && this.tokens.getStringValue().equals("stream"))
/*      */         {
/*      */           while (true) {
/*      */             
/* 1950 */             int ch = this.tokens.read();
/* 1951 */             if (ch != 32 && ch != 9 && ch != 0 && ch != 12) {
/* 1952 */               if (ch != 10)
/* 1953 */                 ch = this.tokens.read(); 
/* 1954 */               if (ch != 10)
/* 1955 */                 this.tokens.backOnePosition(ch); 
/* 1956 */               PRStream stream = new PRStream(this, this.tokens.getFilePointer());
/* 1957 */               stream.putAll(dic);
/*      */               
/* 1959 */               stream.setObjNum(this.objNum, this.objGen);
/*      */               
/* 1961 */               return stream;
/*      */             } 
/*      */           }  } 
/* 1964 */         this.tokens.seek(pos);
/* 1965 */         return dic;
/*      */ 
/*      */       
/*      */       case START_ARRAY:
/* 1969 */         this.readDepth++;
/* 1970 */         arr = readArray();
/* 1971 */         this.readDepth--;
/* 1972 */         return arr;
/*      */       
/*      */       case NUMBER:
/* 1975 */         return new PdfNumber(this.tokens.getStringValue());
/*      */       case STRING:
/* 1977 */         str = (new PdfString(this.tokens.getStringValue(), null)).setHexWriting(this.tokens.isHexString());
/*      */         
/* 1979 */         str.setObjNum(this.objNum, this.objGen);
/* 1980 */         if (this.strings != null) {
/* 1981 */           this.strings.add(str);
/*      */         }
/* 1983 */         return str;
/*      */       case NAME:
/* 1985 */         cachedName = PdfName.staticNames.get(this.tokens.getStringValue());
/* 1986 */         if (this.readDepth > 0 && cachedName != null) {
/* 1987 */           return cachedName;
/*      */         }
/*      */         
/* 1990 */         return new PdfName(this.tokens.getStringValue(), false);
/*      */ 
/*      */       
/*      */       case REF:
/* 1994 */         num = this.tokens.getReference();
/* 1995 */         if (num >= 0) {
/* 1996 */           return new PRIndirectReference(this, num, this.tokens.getGeneration());
/*      */         }
/* 1998 */         if (LOGGER.isLogging(Level.ERROR)) {
/* 1999 */           LOGGER.error(MessageLocalization.getComposedMessage("invalid.reference.number.skip", new Object[0]));
/*      */         }
/* 2001 */         return PdfNull.PDFNULL;
/*      */ 
/*      */       
/*      */       case ENDOFFILE:
/* 2005 */         throw new IOException(MessageLocalization.getComposedMessage("unexpected.end.of.file", new Object[0]));
/*      */     } 
/* 2007 */     String sv = this.tokens.getStringValue();
/* 2008 */     if ("null".equals(sv)) {
/* 2009 */       if (this.readDepth == 0) {
/* 2010 */         return new PdfNull();
/*      */       }
/* 2012 */       return PdfNull.PDFNULL;
/*      */     } 
/* 2014 */     if ("true".equals(sv)) {
/* 2015 */       if (this.readDepth == 0) {
/* 2016 */         return new PdfBoolean(true);
/*      */       }
/* 2018 */       return PdfBoolean.PDFTRUE;
/*      */     } 
/* 2020 */     if ("false".equals(sv)) {
/* 2021 */       if (this.readDepth == 0) {
/* 2022 */         return new PdfBoolean(false);
/*      */       }
/* 2024 */       return PdfBoolean.PDFFALSE;
/*      */     } 
/* 2026 */     return new PdfLiteral(-type.ordinal(), this.tokens.getStringValue()); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte[] FlateDecode(byte[] in) {
/* 2035 */     byte[] b = FlateDecode(in, true);
/* 2036 */     if (b == null)
/* 2037 */       return FlateDecode(in, false); 
/* 2038 */     return b;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static byte[] FlateDecode(byte[] in, ByteArrayOutputStream out) {
/* 2046 */     byte[] b = FlateDecode(in, true, out);
/* 2047 */     if (b == null)
/* 2048 */       return FlateDecode(in, false, out); 
/* 2049 */     return b;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte[] decodePredictor(byte[] in, PdfObject dicPar) {
/* 2058 */     if (dicPar == null || !dicPar.isDictionary())
/* 2059 */       return in; 
/* 2060 */     PdfDictionary dic = (PdfDictionary)dicPar;
/* 2061 */     PdfObject obj = getPdfObject(dic.get(PdfName.PREDICTOR));
/* 2062 */     if (obj == null || !obj.isNumber())
/* 2063 */       return in; 
/* 2064 */     int predictor = ((PdfNumber)obj).intValue();
/* 2065 */     if (predictor < 10 && predictor != 2)
/* 2066 */       return in; 
/* 2067 */     int width = 1;
/* 2068 */     obj = getPdfObject(dic.get(PdfName.COLUMNS));
/* 2069 */     if (obj != null && obj.isNumber())
/* 2070 */       width = ((PdfNumber)obj).intValue(); 
/* 2071 */     int colors = 1;
/* 2072 */     obj = getPdfObject(dic.get(PdfName.COLORS));
/* 2073 */     if (obj != null && obj.isNumber())
/* 2074 */       colors = ((PdfNumber)obj).intValue(); 
/* 2075 */     int bpc = 8;
/* 2076 */     obj = getPdfObject(dic.get(PdfName.BITSPERCOMPONENT));
/* 2077 */     if (obj != null && obj.isNumber())
/* 2078 */       bpc = ((PdfNumber)obj).intValue(); 
/* 2079 */     DataInputStream dataStream = new DataInputStream(new ByteArrayInputStream(in));
/* 2080 */     ByteArrayOutputStream fout = new ByteArrayOutputStream(in.length);
/* 2081 */     int bytesPerPixel = colors * bpc / 8;
/* 2082 */     int bytesPerRow = (colors * width * bpc + 7) / 8;
/* 2083 */     byte[] curr = new byte[bytesPerRow];
/* 2084 */     byte[] prior = new byte[bytesPerRow];
/* 2085 */     if (predictor == 2) {
/* 2086 */       if (bpc == 8) {
/* 2087 */         int numRows = in.length / bytesPerRow;
/* 2088 */         for (int row = 0; row < numRows; row++) {
/* 2089 */           int rowStart = row * bytesPerRow;
/* 2090 */           for (int col = 0 + bytesPerPixel; col < bytesPerRow; col++) {
/* 2091 */             in[rowStart + col] = (byte)(in[rowStart + col] + in[rowStart + col - bytesPerPixel]);
/*      */           }
/*      */         } 
/*      */       } 
/* 2095 */       return in;
/*      */     } 
/*      */ 
/*      */     
/*      */     while (true) {
/* 2100 */       int i, filter = 0;
/*      */       try {
/* 2102 */         filter = dataStream.read();
/* 2103 */         if (filter < 0) {
/* 2104 */           return fout.toByteArray();
/*      */         }
/* 2106 */         dataStream.readFully(curr, 0, bytesPerRow);
/* 2107 */       } catch (Exception e) {
/* 2108 */         return fout.toByteArray();
/*      */       } 
/*      */       
/* 2111 */       switch (filter) {
/*      */         case 0:
/*      */           break;
/*      */         case 1:
/* 2115 */           for (i = bytesPerPixel; i < bytesPerRow; i++) {
/* 2116 */             curr[i] = (byte)(curr[i] + curr[i - bytesPerPixel]);
/*      */           }
/*      */           break;
/*      */         case 2:
/* 2120 */           for (i = 0; i < bytesPerRow; i++) {
/* 2121 */             curr[i] = (byte)(curr[i] + prior[i]);
/*      */           }
/*      */           break;
/*      */         case 3:
/* 2125 */           for (i = 0; i < bytesPerPixel; i++) {
/* 2126 */             curr[i] = (byte)(curr[i] + prior[i] / 2);
/*      */           }
/* 2128 */           for (i = bytesPerPixel; i < bytesPerRow; i++) {
/* 2129 */             curr[i] = (byte)(curr[i] + ((curr[i - bytesPerPixel] & 0xFF) + (prior[i] & 0xFF)) / 2);
/*      */           }
/*      */           break;
/*      */         case 4:
/* 2133 */           for (i = 0; i < bytesPerPixel; i++) {
/* 2134 */             curr[i] = (byte)(curr[i] + prior[i]);
/*      */           }
/*      */           
/* 2137 */           for (i = bytesPerPixel; i < bytesPerRow; i++) {
/* 2138 */             int ret, a = curr[i - bytesPerPixel] & 0xFF;
/* 2139 */             int b = prior[i] & 0xFF;
/* 2140 */             int c = prior[i - bytesPerPixel] & 0xFF;
/*      */             
/* 2142 */             int p = a + b - c;
/* 2143 */             int pa = Math.abs(p - a);
/* 2144 */             int pb = Math.abs(p - b);
/* 2145 */             int pc = Math.abs(p - c);
/*      */ 
/*      */ 
/*      */             
/* 2149 */             if (pa <= pb && pa <= pc) {
/* 2150 */               ret = a;
/* 2151 */             } else if (pb <= pc) {
/* 2152 */               ret = b;
/*      */             } else {
/* 2154 */               ret = c;
/*      */             } 
/* 2156 */             curr[i] = (byte)(curr[i] + (byte)ret);
/*      */           } 
/*      */           break;
/*      */         
/*      */         default:
/* 2161 */           throw new RuntimeException(MessageLocalization.getComposedMessage("png.filter.unknown", new Object[0]));
/*      */       } 
/*      */       try {
/* 2164 */         fout.write(curr);
/*      */       }
/* 2166 */       catch (IOException iOException) {}
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2171 */       byte[] tmp = prior;
/* 2172 */       prior = curr;
/* 2173 */       curr = tmp;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte[] FlateDecode(byte[] in, boolean strict) {
/* 2184 */     return FlateDecode(in, strict, new ByteArrayOutputStream());
/*      */   }
/*      */   
/*      */   private static byte[] FlateDecode(byte[] in, boolean strict, ByteArrayOutputStream out) {
/* 2188 */     ByteArrayInputStream stream = new ByteArrayInputStream(in);
/* 2189 */     InflaterInputStream zip = new InflaterInputStream(stream);
/* 2190 */     byte[] b = new byte[strict ? 4092 : 1];
/*      */     try {
/*      */       int n;
/* 2193 */       while ((n = zip.read(b)) >= 0) {
/* 2194 */         out.write(b, 0, n);
/*      */       }
/* 2196 */       zip.close();
/* 2197 */       out.close();
/* 2198 */       return out.toByteArray();
/* 2199 */     } catch (MemoryLimitsAwareException e) {
/* 2200 */       throw e;
/* 2201 */     } catch (Exception e) {
/* 2202 */       if (strict)
/* 2203 */         return null; 
/* 2204 */       return out.toByteArray();
/*      */     } finally {
/*      */       
/*      */       try {
/* 2208 */         zip.close();
/* 2209 */       } catch (IOException iOException) {}
/*      */       
/*      */       try {
/* 2212 */         out.close();
/* 2213 */       } catch (IOException iOException) {}
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte[] ASCIIHexDecode(byte[] in) {
/* 2223 */     return ASCIIHexDecode(in, new ByteArrayOutputStream());
/*      */   }
/*      */   
/*      */   static byte[] ASCIIHexDecode(byte[] in, ByteArrayOutputStream out) {
/* 2227 */     boolean first = true;
/* 2228 */     int n1 = 0;
/* 2229 */     for (int k = 0; k < in.length; k++) {
/* 2230 */       int ch = in[k] & 0xFF;
/* 2231 */       if (ch == 62)
/*      */         break; 
/* 2233 */       if (!PRTokeniser.isWhitespace(ch)) {
/*      */         
/* 2235 */         int n = PRTokeniser.getHex(ch);
/* 2236 */         if (n == -1)
/* 2237 */           throw new RuntimeException(MessageLocalization.getComposedMessage("illegal.character.in.asciihexdecode", new Object[0])); 
/* 2238 */         if (first) {
/* 2239 */           n1 = n;
/*      */         } else {
/* 2241 */           out.write((byte)((n1 << 4) + n));
/* 2242 */         }  first = !first;
/*      */       } 
/* 2244 */     }  if (!first)
/* 2245 */       out.write((byte)(n1 << 4)); 
/* 2246 */     return out.toByteArray();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte[] ASCII85Decode(byte[] in) {
/* 2254 */     return ASCII85Decode(in, new ByteArrayOutputStream());
/*      */   }
/*      */   
/*      */   static byte[] ASCII85Decode(byte[] in, ByteArrayOutputStream out) {
/* 2258 */     int state = 0;
/* 2259 */     int[] chn = new int[5];
/* 2260 */     for (int k = 0; k < in.length; k++) {
/* 2261 */       int ch = in[k] & 0xFF;
/* 2262 */       if (ch == 126)
/*      */         break; 
/* 2264 */       if (!PRTokeniser.isWhitespace(ch))
/*      */       {
/* 2266 */         if (ch == 122 && state == 0) {
/* 2267 */           out.write(0);
/* 2268 */           out.write(0);
/* 2269 */           out.write(0);
/* 2270 */           out.write(0);
/*      */         } else {
/*      */           
/* 2273 */           if (ch < 33 || ch > 117)
/* 2274 */             throw new RuntimeException(MessageLocalization.getComposedMessage("illegal.character.in.ascii85decode", new Object[0])); 
/* 2275 */           chn[state] = ch - 33;
/* 2276 */           state++;
/* 2277 */           if (state == 5) {
/* 2278 */             state = 0;
/* 2279 */             int i = 0;
/* 2280 */             for (int j = 0; j < 5; j++)
/* 2281 */               i = i * 85 + chn[j]; 
/* 2282 */             out.write((byte)(i >> 24));
/* 2283 */             out.write((byte)(i >> 16));
/* 2284 */             out.write((byte)(i >> 8));
/* 2285 */             out.write((byte)i);
/*      */           } 
/*      */         }  } 
/* 2288 */     }  int r = 0;
/*      */ 
/*      */ 
/*      */     
/* 2292 */     if (state == 2) {
/* 2293 */       r = chn[0] * 85 * 85 * 85 * 85 + chn[1] * 85 * 85 * 85 + 614125 + 7225 + 85;
/* 2294 */       out.write((byte)(r >> 24));
/*      */     }
/* 2296 */     else if (state == 3) {
/* 2297 */       r = chn[0] * 85 * 85 * 85 * 85 + chn[1] * 85 * 85 * 85 + chn[2] * 85 * 85 + 7225 + 85;
/* 2298 */       out.write((byte)(r >> 24));
/* 2299 */       out.write((byte)(r >> 16));
/*      */     }
/* 2301 */     else if (state == 4) {
/* 2302 */       r = chn[0] * 85 * 85 * 85 * 85 + chn[1] * 85 * 85 * 85 + chn[2] * 85 * 85 + chn[3] * 85 + 85;
/* 2303 */       out.write((byte)(r >> 24));
/* 2304 */       out.write((byte)(r >> 16));
/* 2305 */       out.write((byte)(r >> 8));
/*      */     } 
/* 2307 */     return out.toByteArray();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte[] LZWDecode(byte[] in) {
/* 2315 */     return LZWDecode(in, new ByteArrayOutputStream());
/*      */   }
/*      */   
/*      */   static byte[] LZWDecode(byte[] in, ByteArrayOutputStream out) {
/* 2319 */     LZWDecoder lzw = new LZWDecoder();
/* 2320 */     lzw.decode(in, out);
/* 2321 */     return out.toByteArray();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isRebuilt() {
/* 2329 */     return this.rebuilt;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PdfDictionary getPageN(int pageNum) {
/* 2337 */     PdfDictionary dic = this.pageRefs.getPageN(pageNum);
/* 2338 */     if (dic == null)
/* 2339 */       return null; 
/* 2340 */     if (this.appendable)
/* 2341 */       dic.setIndRef(this.pageRefs.getPageOrigRef(pageNum)); 
/* 2342 */     return dic;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PdfDictionary getPageNRelease(int pageNum) {
/* 2350 */     PdfDictionary dic = getPageN(pageNum);
/* 2351 */     this.pageRefs.releasePage(pageNum);
/* 2352 */     return dic;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void releasePage(int pageNum) {
/* 2359 */     this.pageRefs.releasePage(pageNum);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void resetReleasePage() {
/* 2366 */     this.pageRefs.resetReleasePage();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PRIndirectReference getPageOrigRef(int pageNum) {
/* 2374 */     return this.pageRefs.getPageOrigRef(pageNum);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public byte[] getPageContent(int pageNum, RandomAccessFileOrArray file) throws IOException {
/* 2384 */     PdfDictionary page = getPageNRelease(pageNum);
/* 2385 */     if (page == null)
/* 2386 */       return null; 
/* 2387 */     PdfObject contents = getPdfObjectRelease(page.get(PdfName.CONTENTS));
/* 2388 */     if (contents == null)
/* 2389 */       return new byte[0]; 
/* 2390 */     MemoryLimitsAwareHandler handler = this.memoryLimitsAwareHandler;
/* 2391 */     long usedMemory = (null == handler) ? -1L : handler.getAllMemoryUsedForDecompression();
/*      */     
/* 2393 */     if (contents.isStream()) {
/* 2394 */       return getStreamBytes((PRStream)contents, file);
/*      */     }
/* 2396 */     if (contents.isArray()) {
/* 2397 */       PdfArray array = (PdfArray)contents;
/* 2398 */       MemoryLimitsAwareOutputStream bout = new MemoryLimitsAwareOutputStream();
/* 2399 */       for (int k = 0; k < array.size(); k++) {
/* 2400 */         PdfObject item = getPdfObjectRelease(array.getPdfObject(k));
/* 2401 */         if (item != null && item.isStream()) {
/*      */           
/* 2403 */           byte[] b = getStreamBytes((PRStream)item, file);
/*      */           
/* 2405 */           if (null != handler && usedMemory < handler.getAllMemoryUsedForDecompression()) {
/* 2406 */             bout.setMaxStreamSize(handler.getMaxSizeOfSingleDecompressedPdfStream());
/*      */           }
/* 2408 */           bout.write(b);
/* 2409 */           if (k != array.size() - 1)
/* 2410 */             bout.write(10); 
/*      */         } 
/* 2412 */       }  return bout.toByteArray();
/*      */     } 
/*      */     
/* 2415 */     return new byte[0];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte[] getPageContent(PdfDictionary page) throws IOException {
/* 2425 */     if (page == null)
/* 2426 */       return null; 
/* 2427 */     RandomAccessFileOrArray rf = null;
/*      */     try {
/* 2429 */       PdfObject contents = getPdfObjectRelease(page.get(PdfName.CONTENTS));
/* 2430 */       if (contents == null)
/* 2431 */         return new byte[0]; 
/* 2432 */       if (contents.isStream()) {
/* 2433 */         if (rf == null) {
/* 2434 */           rf = ((PRStream)contents).getReader().getSafeFile();
/* 2435 */           rf.reOpen();
/*      */         } 
/* 2437 */         return getStreamBytes((PRStream)contents, rf);
/*      */       } 
/* 2439 */       if (contents.isArray()) {
/* 2440 */         PdfArray array = (PdfArray)contents;
/* 2441 */         ByteArrayOutputStream bout = new ByteArrayOutputStream();
/* 2442 */         for (int k = 0; k < array.size(); k++) {
/* 2443 */           PdfObject item = getPdfObjectRelease(array.getPdfObject(k));
/* 2444 */           if (item != null && item.isStream()) {
/*      */             
/* 2446 */             if (rf == null) {
/* 2447 */               rf = ((PRStream)item).getReader().getSafeFile();
/* 2448 */               rf.reOpen();
/*      */             } 
/* 2450 */             byte[] b = getStreamBytes((PRStream)item, rf);
/* 2451 */             bout.write(b);
/* 2452 */             if (k != array.size() - 1)
/* 2453 */               bout.write(10); 
/*      */           } 
/* 2455 */         }  return bout.toByteArray();
/*      */       } 
/*      */       
/* 2458 */       return new byte[0];
/*      */     } finally {
/*      */       
/*      */       try {
/* 2462 */         if (rf != null)
/* 2463 */           rf.close(); 
/* 2464 */       } catch (Exception exception) {}
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PdfDictionary getPageResources(int pageNum) {
/* 2475 */     return getPageResources(getPageN(pageNum));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PdfDictionary getPageResources(PdfDictionary pageDict) {
/* 2485 */     return pageDict.getAsDict(PdfName.RESOURCES);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public byte[] getPageContent(int pageNum) throws IOException {
/* 2494 */     RandomAccessFileOrArray rf = getSafeFile();
/*      */     try {
/* 2496 */       rf.reOpen();
/* 2497 */       return getPageContent(pageNum, rf);
/*      */     } finally {
/*      */       
/* 2500 */       try { rf.close(); } catch (Exception exception) {}
/*      */     }  } protected void killXref(PdfObject obj) { int xr;
/*      */     PdfArray t;
/*      */     PdfDictionary dic;
/*      */     int i;
/* 2505 */     if (obj == null)
/*      */       return; 
/* 2507 */     if (obj instanceof PdfIndirectReference && !obj.isIndirect())
/*      */       return; 
/* 2509 */     switch (obj.type()) {
/*      */       case 10:
/* 2511 */         xr = ((PRIndirectReference)obj).getNumber();
/* 2512 */         obj = this.xrefObj.get(xr);
/* 2513 */         this.xrefObj.set(xr, null);
/* 2514 */         this.freeXref = xr;
/* 2515 */         killXref(obj);
/*      */         break;
/*      */       
/*      */       case 5:
/* 2519 */         t = (PdfArray)obj;
/* 2520 */         for (i = 0; i < t.size(); i++) {
/* 2521 */           killXref(t.getPdfObject(i));
/*      */         }
/*      */         break;
/*      */       case 6:
/*      */       case 7:
/* 2526 */         dic = (PdfDictionary)obj;
/* 2527 */         for (PdfName element : dic.getKeys()) {
/* 2528 */           killXref(dic.get(element));
/*      */         }
/*      */         break;
/*      */     }  }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPageContent(int pageNum, byte[] content) {
/* 2540 */     setPageContent(pageNum, content, -1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPageContent(int pageNum, byte[] content, int compressionLevel) {
/* 2550 */     setPageContent(pageNum, content, compressionLevel, false);
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
/*      */   public void setPageContent(int pageNum, byte[] content, int compressionLevel, boolean killOldXRefRecursively) {
/* 2563 */     PdfDictionary page = getPageN(pageNum);
/* 2564 */     if (page == null)
/*      */       return; 
/* 2566 */     PdfObject contents = page.get(PdfName.CONTENTS);
/* 2567 */     this.freeXref = -1;
/* 2568 */     if (killOldXRefRecursively) {
/* 2569 */       killXref(contents);
/*      */     }
/* 2571 */     if (this.freeXref == -1) {
/* 2572 */       this.xrefObj.add(null);
/* 2573 */       this.freeXref = this.xrefObj.size() - 1;
/*      */     } 
/* 2575 */     page.put(PdfName.CONTENTS, new PRIndirectReference(this, this.freeXref));
/* 2576 */     this.xrefObj.set(this.freeXref, new PRStream(this, content, compressionLevel));
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
/*      */   public static byte[] decodeBytes(byte[] b, PdfDictionary streamDictionary) throws IOException {
/* 2588 */     return decodeBytes(b, streamDictionary, FilterHandlers.getDefaultFilterHandlers());
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
/*      */   public static byte[] decodeBytes(byte[] b, PdfDictionary streamDictionary, Map<PdfName, FilterHandlers.FilterHandler> filterHandlers) throws IOException {
/* 2601 */     PdfObject filter = getPdfObjectRelease(streamDictionary.get(PdfName.FILTER));
/*      */     
/* 2603 */     ArrayList<PdfObject> filters = new ArrayList<PdfObject>();
/* 2604 */     if (filter != null) {
/* 2605 */       if (filter.isName()) {
/* 2606 */         filters.add(filter);
/* 2607 */       } else if (filter.isArray()) {
/* 2608 */         filters = ((PdfArray)filter).getArrayList();
/*      */       } 
/*      */     }
/* 2611 */     MemoryLimitsAwareHandler memoryLimitsAwareHandler = null;
/* 2612 */     if (streamDictionary instanceof PRStream && null != ((PRStream)streamDictionary).getReader()) {
/* 2613 */       memoryLimitsAwareHandler = ((PRStream)streamDictionary).getReader().getMemoryLimitsAwareHandler();
/*      */     }
/* 2615 */     if (null != memoryLimitsAwareHandler) {
/* 2616 */       HashSet<PdfName> filterSet = new HashSet<PdfName>();
/*      */       int index;
/* 2618 */       for (index = 0; index < filters.size(); index++) {
/* 2619 */         PdfName filterName = (PdfName)filters.get(index);
/* 2620 */         if (!filterSet.add(filterName)) {
/* 2621 */           memoryLimitsAwareHandler.beginDecompressedPdfStreamProcessing();
/*      */           break;
/*      */         } 
/*      */       } 
/* 2625 */       if (index == filters.size()) {
/* 2626 */         memoryLimitsAwareHandler = null;
/*      */       }
/*      */     } 
/*      */     
/* 2630 */     ArrayList<PdfObject> dp = new ArrayList<PdfObject>();
/* 2631 */     PdfObject dpo = getPdfObjectRelease(streamDictionary.get(PdfName.DECODEPARMS));
/* 2632 */     if (dpo == null || (!dpo.isDictionary() && !dpo.isArray()))
/* 2633 */       dpo = getPdfObjectRelease(streamDictionary.get(PdfName.DP)); 
/* 2634 */     if (dpo != null)
/* 2635 */       if (dpo.isDictionary()) {
/* 2636 */         dp.add(dpo);
/* 2637 */       } else if (dpo.isArray()) {
/* 2638 */         dp = ((PdfArray)dpo).getArrayList();
/*      */       }  
/* 2640 */     for (int j = 0; j < filters.size(); j++) {
/* 2641 */       PdfDictionary decodeParams; PdfName filterName = (PdfName)filters.get(j);
/* 2642 */       FilterHandlers.FilterHandler filterHandler = filterHandlers.get(filterName);
/* 2643 */       if (filterHandler == null) {
/* 2644 */         throw new UnsupportedPdfException(MessageLocalization.getComposedMessage("the.filter.1.is.not.supported", new Object[] { filterName }));
/*      */       }
/*      */       
/* 2647 */       if (j < dp.size()) {
/* 2648 */         PdfObject dpEntry = getPdfObject(dp.get(j));
/* 2649 */         if (dpEntry instanceof PdfDictionary) {
/* 2650 */           decodeParams = (PdfDictionary)dpEntry;
/* 2651 */         } else if (dpEntry == null || dpEntry instanceof PdfNull || (dpEntry instanceof PdfLiteral && 
/* 2652 */           Arrays.equals("null".getBytes(), ((PdfLiteral)dpEntry).getBytes()))) {
/* 2653 */           decodeParams = null;
/*      */         } else {
/* 2655 */           throw new UnsupportedPdfException(MessageLocalization.getComposedMessage("the.decode.parameter.type.1.is.not.supported", new Object[] { dpEntry.getClass().toString() }));
/*      */         } 
/*      */       } else {
/*      */         
/* 2659 */         decodeParams = null;
/*      */       } 
/* 2661 */       b = filterHandler.decode(b, filterName, decodeParams, streamDictionary);
/* 2662 */       if (null != memoryLimitsAwareHandler) {
/* 2663 */         memoryLimitsAwareHandler.considerBytesOccupiedByDecompressedPdfStream(b.length);
/*      */       }
/*      */     } 
/* 2666 */     if (null != memoryLimitsAwareHandler) {
/* 2667 */       memoryLimitsAwareHandler.endDecompressedPdfStreamProcessing();
/*      */     }
/* 2669 */     return b;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte[] getStreamBytes(PRStream stream, RandomAccessFileOrArray file) throws IOException {
/* 2679 */     byte[] b = getStreamBytesRaw(stream, file);
/* 2680 */     return decodeBytes(b, stream);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte[] getStreamBytes(PRStream stream) throws IOException {
/* 2689 */     RandomAccessFileOrArray rf = stream.getReader().getSafeFile();
/*      */     try {
/* 2691 */       rf.reOpen();
/* 2692 */       return getStreamBytes(stream, rf);
/*      */     } finally {
/*      */       
/* 2695 */       try { rf.close(); } catch (Exception exception) {}
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte[] getStreamBytesRaw(PRStream stream, RandomAccessFileOrArray file) throws IOException {
/*      */     byte[] b;
/* 2706 */     PdfReader reader = stream.getReader();
/*      */     
/* 2708 */     if (stream.getOffset() < 0L) {
/* 2709 */       b = stream.getBytes();
/*      */     } else {
/* 2711 */       b = new byte[stream.getLength()];
/* 2712 */       file.seek(stream.getOffset());
/* 2713 */       file.readFully(b);
/* 2714 */       PdfEncryption decrypt = reader.getDecrypt();
/* 2715 */       if (decrypt != null) {
/* 2716 */         PdfObject filter = getPdfObjectRelease(stream.get(PdfName.FILTER));
/* 2717 */         ArrayList<PdfObject> filters = new ArrayList<PdfObject>();
/* 2718 */         if (filter != null)
/* 2719 */           if (filter.isName()) {
/* 2720 */             filters.add(filter);
/* 2721 */           } else if (filter.isArray()) {
/* 2722 */             filters = ((PdfArray)filter).getArrayList();
/*      */           }  
/* 2724 */         boolean skip = false;
/* 2725 */         for (int k = 0; k < filters.size(); k++) {
/* 2726 */           PdfObject obj = getPdfObjectRelease(filters.get(k));
/* 2727 */           if (obj != null && obj.toString().equals("/Crypt")) {
/* 2728 */             skip = true;
/*      */             break;
/*      */           } 
/*      */         } 
/* 2732 */         if (!skip) {
/* 2733 */           decrypt.setHashKey(stream.getObjNum(), stream.getObjGen());
/* 2734 */           b = decrypt.decryptByteArray(b);
/*      */         } 
/*      */       } 
/*      */     } 
/* 2738 */     return b;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte[] getStreamBytesRaw(PRStream stream) throws IOException {
/* 2747 */     RandomAccessFileOrArray rf = stream.getReader().getSafeFile();
/*      */     try {
/* 2749 */       rf.reOpen();
/* 2750 */       return getStreamBytesRaw(stream, rf);
/*      */     } finally {
/*      */       
/* 2753 */       try { rf.close(); } catch (Exception exception) {}
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void eliminateSharedStreams() {
/* 2759 */     if (!this.sharedStreams)
/*      */       return; 
/* 2761 */     this.sharedStreams = false;
/* 2762 */     if (this.pageRefs.size() == 1)
/*      */       return; 
/* 2764 */     ArrayList<PRIndirectReference> newRefs = new ArrayList<PRIndirectReference>();
/* 2765 */     ArrayList<PRStream> newStreams = new ArrayList<PRStream>();
/* 2766 */     IntHashtable visited = new IntHashtable(); int k;
/* 2767 */     for (k = 1; k <= this.pageRefs.size(); k++) {
/* 2768 */       PdfDictionary page = this.pageRefs.getPageN(k);
/* 2769 */       if (page != null) {
/*      */         
/* 2771 */         PdfObject contents = getPdfObject(page.get(PdfName.CONTENTS));
/* 2772 */         if (contents != null)
/*      */         {
/* 2774 */           if (contents.isStream()) {
/* 2775 */             PRIndirectReference ref = (PRIndirectReference)page.get(PdfName.CONTENTS);
/* 2776 */             if (visited.containsKey(ref.getNumber())) {
/*      */               
/* 2778 */               newRefs.add(ref);
/* 2779 */               newStreams.add(new PRStream((PRStream)contents, null));
/*      */             } else {
/*      */               
/* 2782 */               visited.put(ref.getNumber(), 1);
/*      */             } 
/* 2784 */           } else if (contents.isArray()) {
/* 2785 */             PdfArray array = (PdfArray)contents;
/* 2786 */             for (int j = 0; j < array.size(); j++) {
/* 2787 */               PRIndirectReference ref = (PRIndirectReference)array.getPdfObject(j);
/* 2788 */               if (visited.containsKey(ref.getNumber()))
/*      */               
/* 2790 */               { newRefs.add(ref);
/* 2791 */                 newStreams.add(new PRStream((PRStream)getPdfObject(ref), null)); }
/*      */               else
/*      */               
/* 2794 */               { visited.put(ref.getNumber(), 1); } 
/*      */             } 
/*      */           }  } 
/*      */       } 
/* 2798 */     }  if (newStreams.isEmpty())
/*      */       return; 
/* 2800 */     for (k = 0; k < newStreams.size(); k++) {
/* 2801 */       this.xrefObj.add(newStreams.get(k));
/* 2802 */       PRIndirectReference ref = newRefs.get(k);
/* 2803 */       ref.setNumber(this.xrefObj.size() - 1, 0);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isTampered() {
/* 2812 */     return this.tampered;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setTampered(boolean tampered) {
/* 2820 */     this.tampered = tampered;
/* 2821 */     this.pageRefs.keepPages();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public byte[] getMetadata() throws IOException {
/* 2829 */     PdfObject obj = getPdfObject(this.catalog.get(PdfName.METADATA));
/* 2830 */     if (!(obj instanceof PRStream))
/* 2831 */       return null; 
/* 2832 */     RandomAccessFileOrArray rf = getSafeFile();
/* 2833 */     byte[] b = null;
/*      */     try {
/* 2835 */       rf.reOpen();
/* 2836 */       b = getStreamBytes((PRStream)obj, rf);
/*      */     } finally {
/*      */       
/*      */       try {
/* 2840 */         rf.close();
/*      */       }
/* 2842 */       catch (Exception exception) {}
/*      */     } 
/*      */ 
/*      */     
/* 2846 */     return b;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getLastXref() {
/* 2854 */     return this.lastXref;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getXrefSize() {
/* 2862 */     return this.xrefObj.size();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getEofPos() {
/* 2870 */     return this.eofPos;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public char getPdfVersion() {
/* 2879 */     return this.pdfVersion;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isEncrypted() {
/* 2887 */     return this.encrypted;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getPermissions() {
/* 2896 */     return this.pValue;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean is128Key() {
/* 2904 */     return (this.rValue == 3);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PdfDictionary getTrailer() {
/* 2912 */     return this.trailer;
/*      */   }
/*      */   
/*      */   PdfEncryption getDecrypt() {
/* 2916 */     return this.decrypt;
/*      */   }
/*      */   
/*      */   static boolean equalsn(byte[] a1, byte[] a2) {
/* 2920 */     int length = a2.length;
/* 2921 */     for (int k = 0; k < length; k++) {
/* 2922 */       if (a1[k] != a2[k])
/* 2923 */         return false; 
/*      */     } 
/* 2925 */     return true;
/*      */   }
/*      */   
/*      */   static boolean existsName(PdfDictionary dic, PdfName key, PdfName value) {
/* 2929 */     PdfObject type = getPdfObjectRelease(dic.get(key));
/* 2930 */     if (type == null || !type.isName())
/* 2931 */       return false; 
/* 2932 */     PdfName name = (PdfName)type;
/* 2933 */     return name.equals(value);
/*      */   }
/*      */   
/*      */   static String getFontName(PdfDictionary dic) {
/* 2937 */     if (dic == null)
/* 2938 */       return null; 
/* 2939 */     PdfObject type = getPdfObjectRelease(dic.get(PdfName.BASEFONT));
/* 2940 */     if (type == null || !type.isName())
/* 2941 */       return null; 
/* 2942 */     return PdfName.decodeName(type.toString());
/*      */   }
/*      */   
/*      */   static String getSubsetPrefix(PdfDictionary dic) {
/* 2946 */     if (dic == null)
/* 2947 */       return null; 
/* 2948 */     String s = getFontName(dic);
/* 2949 */     if (s == null)
/* 2950 */       return null; 
/* 2951 */     if (s.length() < 8 || s.charAt(6) != '+')
/* 2952 */       return null; 
/* 2953 */     for (int k = 0; k < 6; k++) {
/* 2954 */       char c = s.charAt(k);
/* 2955 */       if (c < 'A' || c > 'Z')
/* 2956 */         return null; 
/*      */     } 
/* 2958 */     return s;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int shuffleSubsetNames() {
/* 2966 */     int total = 0;
/* 2967 */     for (int k = 1; k < this.xrefObj.size(); k++) {
/* 2968 */       PdfObject obj = getPdfObjectRelease(k);
/* 2969 */       if (obj != null && obj.isDictionary()) {
/*      */         
/* 2971 */         PdfDictionary dic = (PdfDictionary)obj;
/* 2972 */         if (existsName(dic, PdfName.TYPE, PdfName.FONT))
/*      */         {
/* 2974 */           if (existsName(dic, PdfName.SUBTYPE, PdfName.TYPE1) || 
/* 2975 */             existsName(dic, PdfName.SUBTYPE, PdfName.MMTYPE1) || 
/* 2976 */             existsName(dic, PdfName.SUBTYPE, PdfName.TRUETYPE)) {
/* 2977 */             String s = getSubsetPrefix(dic);
/* 2978 */             if (s != null) {
/*      */               
/* 2980 */               String ns = BaseFont.createSubsetPrefix() + s.substring(7);
/* 2981 */               PdfName newName = new PdfName(ns);
/* 2982 */               dic.put(PdfName.BASEFONT, newName);
/* 2983 */               setXrefPartialObject(k, dic);
/* 2984 */               total++;
/* 2985 */               PdfDictionary fd = dic.getAsDict(PdfName.FONTDESCRIPTOR);
/* 2986 */               if (fd != null)
/*      */               {
/* 2988 */                 fd.put(PdfName.FONTNAME, newName); } 
/*      */             } 
/* 2990 */           } else if (existsName(dic, PdfName.SUBTYPE, PdfName.TYPE0)) {
/* 2991 */             String s = getSubsetPrefix(dic);
/* 2992 */             PdfArray arr = dic.getAsArray(PdfName.DESCENDANTFONTS);
/* 2993 */             if (arr != null)
/*      */             {
/* 2995 */               if (!arr.isEmpty())
/*      */               
/* 2997 */               { PdfDictionary desc = arr.getAsDict(0);
/* 2998 */                 String sde = getSubsetPrefix(desc);
/* 2999 */                 if (sde != null)
/*      */                 
/* 3001 */                 { String ns = BaseFont.createSubsetPrefix();
/* 3002 */                   if (s != null)
/* 3003 */                     dic.put(PdfName.BASEFONT, new PdfName(ns + s.substring(7))); 
/* 3004 */                   setXrefPartialObject(k, dic);
/* 3005 */                   PdfName newName = new PdfName(ns + sde.substring(7));
/* 3006 */                   desc.put(PdfName.BASEFONT, newName);
/* 3007 */                   total++;
/* 3008 */                   PdfDictionary fd = desc.getAsDict(PdfName.FONTDESCRIPTOR);
/* 3009 */                   if (fd != null)
/*      */                   {
/* 3011 */                     fd.put(PdfName.FONTNAME, newName); }  }  }  } 
/*      */           }  } 
/*      */       } 
/* 3014 */     }  return total;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int createFakeFontSubsets() {
/* 3021 */     int total = 0;
/* 3022 */     for (int k = 1; k < this.xrefObj.size(); k++) {
/* 3023 */       PdfObject obj = getPdfObjectRelease(k);
/* 3024 */       if (obj != null && obj.isDictionary()) {
/*      */         
/* 3026 */         PdfDictionary dic = (PdfDictionary)obj;
/* 3027 */         if (existsName(dic, PdfName.TYPE, PdfName.FONT))
/*      */         {
/* 3029 */           if (existsName(dic, PdfName.SUBTYPE, PdfName.TYPE1) || 
/* 3030 */             existsName(dic, PdfName.SUBTYPE, PdfName.MMTYPE1) || 
/* 3031 */             existsName(dic, PdfName.SUBTYPE, PdfName.TRUETYPE)) {
/* 3032 */             String s = getSubsetPrefix(dic);
/* 3033 */             if (s == null)
/*      */             
/* 3035 */             { s = getFontName(dic);
/* 3036 */               if (s != null)
/*      */               
/* 3038 */               { String ns = BaseFont.createSubsetPrefix() + s;
/* 3039 */                 PdfDictionary fd = (PdfDictionary)getPdfObjectRelease(dic.get(PdfName.FONTDESCRIPTOR));
/* 3040 */                 if (fd != null)
/*      */                 {
/* 3042 */                   if (fd.get(PdfName.FONTFILE) != null || fd.get(PdfName.FONTFILE2) != null || fd
/* 3043 */                     .get(PdfName.FONTFILE3) != null)
/*      */                   
/* 3045 */                   { fd = dic.getAsDict(PdfName.FONTDESCRIPTOR);
/* 3046 */                     PdfName newName = new PdfName(ns);
/* 3047 */                     dic.put(PdfName.BASEFONT, newName);
/* 3048 */                     fd.put(PdfName.FONTNAME, newName);
/* 3049 */                     setXrefPartialObject(k, dic);
/* 3050 */                     total++; }  }  }  } 
/*      */           }  } 
/*      */       } 
/* 3053 */     }  return total;
/*      */   }
/*      */   
/*      */   private static PdfArray getNameArray(PdfObject obj) {
/* 3057 */     if (obj == null)
/* 3058 */       return null; 
/* 3059 */     obj = getPdfObjectRelease(obj);
/* 3060 */     if (obj == null)
/* 3061 */       return null; 
/* 3062 */     if (obj.isArray())
/* 3063 */       return (PdfArray)obj; 
/* 3064 */     if (obj.isDictionary()) {
/* 3065 */       PdfObject arr2 = getPdfObjectRelease(((PdfDictionary)obj).get(PdfName.D));
/* 3066 */       if (arr2 != null && arr2.isArray())
/* 3067 */         return (PdfArray)arr2; 
/*      */     } 
/* 3069 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public HashMap<Object, PdfObject> getNamedDestination() {
/* 3078 */     return getNamedDestination(false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public HashMap<Object, PdfObject> getNamedDestination(boolean keepNames) {
/* 3089 */     HashMap<Object, PdfObject> names = getNamedDestinationFromNames(keepNames);
/* 3090 */     names.putAll(getNamedDestinationFromStrings());
/* 3091 */     return names;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public HashMap<String, PdfObject> getNamedDestinationFromNames() {
/* 3102 */     return (HashMap)new HashMap<Object, PdfObject>(getNamedDestinationFromNames(false));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public HashMap<Object, PdfObject> getNamedDestinationFromNames(boolean keepNames) {
/* 3113 */     HashMap<Object, PdfObject> names = new HashMap<Object, PdfObject>();
/* 3114 */     if (this.catalog.get(PdfName.DESTS) != null) {
/* 3115 */       PdfDictionary dic = (PdfDictionary)getPdfObjectRelease(this.catalog.get(PdfName.DESTS));
/* 3116 */       if (dic == null)
/* 3117 */         return names; 
/* 3118 */       Set<PdfName> keys = dic.getKeys();
/* 3119 */       for (PdfName key : keys) {
/* 3120 */         PdfArray arr = getNameArray(dic.get(key));
/* 3121 */         if (arr == null)
/*      */           continue; 
/* 3123 */         if (keepNames) {
/* 3124 */           names.put(key, arr);
/*      */           continue;
/*      */         } 
/* 3127 */         String name = PdfName.decodeName(key.toString());
/* 3128 */         names.put(name, arr);
/*      */       } 
/*      */     } 
/*      */     
/* 3132 */     return names;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public HashMap<String, PdfObject> getNamedDestinationFromStrings() {
/* 3141 */     if (this.catalog.get(PdfName.NAMES) != null) {
/* 3142 */       PdfDictionary dic = (PdfDictionary)getPdfObjectRelease(this.catalog.get(PdfName.NAMES));
/* 3143 */       if (dic != null) {
/* 3144 */         dic = (PdfDictionary)getPdfObjectRelease(dic.get(PdfName.DESTS));
/* 3145 */         if (dic != null) {
/* 3146 */           HashMap<String, PdfObject> names = PdfNameTree.readTree(dic);
/* 3147 */           for (Iterator<Map.Entry<String, PdfObject>> it = names.entrySet().iterator(); it.hasNext(); ) {
/* 3148 */             Map.Entry<String, PdfObject> entry = it.next();
/* 3149 */             PdfArray arr = getNameArray(entry.getValue());
/* 3150 */             if (arr != null) {
/* 3151 */               entry.setValue(arr); continue;
/*      */             } 
/* 3153 */             it.remove();
/*      */           } 
/* 3155 */           return names;
/*      */         } 
/*      */       } 
/*      */     } 
/* 3159 */     return new HashMap<String, PdfObject>();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void removeFields() {
/* 3166 */     this.pageRefs.resetReleasePage();
/* 3167 */     for (int k = 1; k <= this.pageRefs.size(); k++) {
/* 3168 */       PdfDictionary page = this.pageRefs.getPageN(k);
/* 3169 */       PdfArray annots = page.getAsArray(PdfName.ANNOTS);
/* 3170 */       if (annots == null) {
/* 3171 */         this.pageRefs.releasePage(k);
/*      */       } else {
/*      */         
/* 3174 */         for (int j = 0; j < annots.size(); j++) {
/* 3175 */           PdfObject obj = getPdfObjectRelease(annots.getPdfObject(j));
/* 3176 */           if (obj != null && obj.isDictionary()) {
/*      */             
/* 3178 */             PdfDictionary annot = (PdfDictionary)obj;
/* 3179 */             if (PdfName.WIDGET.equals(annot.get(PdfName.SUBTYPE)))
/* 3180 */               annots.remove(j--); 
/*      */           } 
/* 3182 */         }  if (annots.isEmpty())
/* 3183 */         { page.remove(PdfName.ANNOTS); }
/*      */         else
/* 3185 */         { this.pageRefs.releasePage(k); } 
/*      */       } 
/* 3187 */     }  this.catalog.remove(PdfName.ACROFORM);
/* 3188 */     this.pageRefs.resetReleasePage();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void removeAnnotations() {
/* 3195 */     this.pageRefs.resetReleasePage();
/* 3196 */     for (int k = 1; k <= this.pageRefs.size(); k++) {
/* 3197 */       PdfDictionary page = this.pageRefs.getPageN(k);
/* 3198 */       if (page.get(PdfName.ANNOTS) == null) {
/* 3199 */         this.pageRefs.releasePage(k);
/*      */       } else {
/* 3201 */         page.remove(PdfName.ANNOTS);
/*      */       } 
/* 3203 */     }  this.catalog.remove(PdfName.ACROFORM);
/* 3204 */     this.pageRefs.resetReleasePage();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ArrayList<PdfAnnotation.PdfImportedLink> getLinks(int page) {
/* 3213 */     this.pageRefs.resetReleasePage();
/* 3214 */     ArrayList<PdfAnnotation.PdfImportedLink> result = new ArrayList<PdfAnnotation.PdfImportedLink>();
/* 3215 */     PdfDictionary pageDic = this.pageRefs.getPageN(page);
/* 3216 */     if (pageDic.get(PdfName.ANNOTS) != null) {
/* 3217 */       PdfArray annots = pageDic.getAsArray(PdfName.ANNOTS);
/* 3218 */       for (int j = 0; j < annots.size(); j++) {
/* 3219 */         PdfDictionary annot = (PdfDictionary)getPdfObjectRelease(annots.getPdfObject(j));
/*      */         
/* 3221 */         if (PdfName.LINK.equals(annot.get(PdfName.SUBTYPE))) {
/* 3222 */           result.add(new PdfAnnotation.PdfImportedLink(annot));
/*      */         }
/*      */       } 
/*      */     } 
/* 3226 */     this.pageRefs.releasePage(page);
/* 3227 */     this.pageRefs.resetReleasePage();
/* 3228 */     return result;
/*      */   }
/*      */   
/*      */   private void iterateBookmarks(PdfObject outlineRef, HashMap<Object, PdfObject> names) {
/* 3232 */     while (outlineRef != null) {
/* 3233 */       replaceNamedDestination(outlineRef, names);
/* 3234 */       PdfDictionary outline = (PdfDictionary)getPdfObjectRelease(outlineRef);
/* 3235 */       PdfObject first = outline.get(PdfName.FIRST);
/* 3236 */       if (first != null) {
/* 3237 */         iterateBookmarks(first, names);
/*      */       }
/* 3239 */       outlineRef = outline.get(PdfName.NEXT);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void makeRemoteNamedDestinationsLocal() {
/* 3248 */     if (this.remoteToLocalNamedDestinations)
/*      */       return; 
/* 3250 */     this.remoteToLocalNamedDestinations = true;
/* 3251 */     HashMap<Object, PdfObject> names = getNamedDestination(true);
/* 3252 */     if (names.isEmpty())
/*      */       return; 
/* 3254 */     for (int k = 1; k <= this.pageRefs.size(); k++) {
/* 3255 */       PdfDictionary page = this.pageRefs.getPageN(k);
/*      */       PdfObject annotsRef;
/* 3257 */       PdfArray annots = (PdfArray)getPdfObject(annotsRef = page.get(PdfName.ANNOTS));
/* 3258 */       int annotIdx = this.lastXrefPartial;
/* 3259 */       releaseLastXrefPartial();
/* 3260 */       if (annots == null) {
/* 3261 */         this.pageRefs.releasePage(k);
/*      */       } else {
/*      */         
/* 3264 */         boolean commitAnnots = false;
/* 3265 */         for (int an = 0; an < annots.size(); an++) {
/* 3266 */           PdfObject objRef = annots.getPdfObject(an);
/* 3267 */           if (convertNamedDestination(objRef, names) && !objRef.isIndirect())
/* 3268 */             commitAnnots = true; 
/*      */         } 
/* 3270 */         if (commitAnnots)
/* 3271 */           setXrefPartialObject(annotIdx, annots); 
/* 3272 */         if (!commitAnnots || annotsRef.isIndirect()) {
/* 3273 */           this.pageRefs.releasePage(k);
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
/*      */   private boolean convertNamedDestination(PdfObject obj, HashMap<Object, PdfObject> names) {
/* 3285 */     obj = getPdfObject(obj);
/* 3286 */     int objIdx = this.lastXrefPartial;
/* 3287 */     releaseLastXrefPartial();
/* 3288 */     if (obj != null && obj.isDictionary()) {
/* 3289 */       PdfObject ob2 = getPdfObject(((PdfDictionary)obj).get(PdfName.A));
/* 3290 */       if (ob2 != null) {
/* 3291 */         int obj2Idx = this.lastXrefPartial;
/* 3292 */         releaseLastXrefPartial();
/* 3293 */         PdfDictionary dic = (PdfDictionary)ob2;
/* 3294 */         PdfName type = (PdfName)getPdfObjectRelease(dic.get(PdfName.S));
/* 3295 */         if (PdfName.GOTOR.equals(type)) {
/* 3296 */           PdfObject ob3 = getPdfObjectRelease(dic.get(PdfName.D));
/* 3297 */           Object name = null;
/* 3298 */           if (ob3 != null) {
/* 3299 */             if (ob3.isName()) {
/* 3300 */               name = ob3;
/* 3301 */             } else if (ob3.isString()) {
/* 3302 */               name = ob3.toString();
/* 3303 */             }  PdfArray dest = (PdfArray)names.get(name);
/* 3304 */             if (dest != null) {
/* 3305 */               dic.remove(PdfName.F);
/* 3306 */               dic.remove(PdfName.NEWWINDOW);
/* 3307 */               dic.put(PdfName.S, PdfName.GOTO);
/* 3308 */               setXrefPartialObject(obj2Idx, ob2);
/* 3309 */               setXrefPartialObject(objIdx, obj);
/* 3310 */               return true;
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/* 3316 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public void consolidateNamedDestinations() {
/* 3321 */     if (this.consolidateNamedDestinations)
/*      */       return; 
/* 3323 */     this.consolidateNamedDestinations = true;
/* 3324 */     HashMap<Object, PdfObject> names = getNamedDestination(true);
/* 3325 */     if (names.isEmpty())
/*      */       return; 
/* 3327 */     for (int k = 1; k <= this.pageRefs.size(); k++) {
/* 3328 */       PdfDictionary page = this.pageRefs.getPageN(k);
/*      */       PdfObject annotsRef;
/* 3330 */       PdfArray annots = (PdfArray)getPdfObject(annotsRef = page.get(PdfName.ANNOTS));
/* 3331 */       int annotIdx = this.lastXrefPartial;
/* 3332 */       releaseLastXrefPartial();
/* 3333 */       if (annots == null) {
/* 3334 */         this.pageRefs.releasePage(k);
/*      */       } else {
/*      */         
/* 3337 */         boolean commitAnnots = false;
/* 3338 */         for (int an = 0; an < annots.size(); an++) {
/* 3339 */           PdfObject objRef = annots.getPdfObject(an);
/* 3340 */           if (replaceNamedDestination(objRef, names) && !objRef.isIndirect())
/* 3341 */             commitAnnots = true; 
/*      */         } 
/* 3343 */         if (commitAnnots)
/* 3344 */           setXrefPartialObject(annotIdx, annots); 
/* 3345 */         if (!commitAnnots || annotsRef.isIndirect())
/* 3346 */           this.pageRefs.releasePage(k); 
/*      */       } 
/* 3348 */     }  PdfDictionary outlines = (PdfDictionary)getPdfObjectRelease(this.catalog.get(PdfName.OUTLINES));
/* 3349 */     if (outlines == null)
/*      */       return; 
/* 3351 */     iterateBookmarks(outlines.get(PdfName.FIRST), names);
/*      */   }
/*      */   
/*      */   private boolean replaceNamedDestination(PdfObject obj, HashMap<Object, PdfObject> names) {
/* 3355 */     obj = getPdfObject(obj);
/* 3356 */     int objIdx = this.lastXrefPartial;
/* 3357 */     releaseLastXrefPartial();
/* 3358 */     if (obj != null && obj.isDictionary()) {
/* 3359 */       PdfObject ob2 = getPdfObjectRelease(((PdfDictionary)obj).get(PdfName.DEST));
/* 3360 */       Object name = null;
/* 3361 */       if (ob2 != null) {
/* 3362 */         if (ob2.isName()) {
/* 3363 */           name = ob2;
/* 3364 */         } else if (ob2.isString()) {
/* 3365 */           name = ob2.toString();
/* 3366 */         }  PdfArray dest = (PdfArray)names.get(name);
/* 3367 */         if (dest != null) {
/* 3368 */           ((PdfDictionary)obj).put(PdfName.DEST, dest);
/* 3369 */           setXrefPartialObject(objIdx, obj);
/* 3370 */           return true;
/*      */         }
/*      */       
/* 3373 */       } else if ((ob2 = getPdfObject(((PdfDictionary)obj).get(PdfName.A))) != null) {
/* 3374 */         int obj2Idx = this.lastXrefPartial;
/* 3375 */         releaseLastXrefPartial();
/* 3376 */         PdfDictionary dic = (PdfDictionary)ob2;
/* 3377 */         PdfName type = (PdfName)getPdfObjectRelease(dic.get(PdfName.S));
/* 3378 */         if (PdfName.GOTO.equals(type)) {
/* 3379 */           PdfObject ob3 = getPdfObjectRelease(dic.get(PdfName.D));
/* 3380 */           if (ob3 != null)
/* 3381 */             if (ob3.isName()) {
/* 3382 */               name = ob3;
/* 3383 */             } else if (ob3.isString()) {
/* 3384 */               name = ob3.toString();
/*      */             }  
/* 3386 */           PdfArray dest = (PdfArray)names.get(name);
/* 3387 */           if (dest != null) {
/* 3388 */             dic.put(PdfName.D, dest);
/* 3389 */             setXrefPartialObject(obj2Idx, ob2);
/* 3390 */             setXrefPartialObject(objIdx, obj);
/* 3391 */             return true;
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/* 3396 */     return false;
/*      */   }
/*      */   
/*      */   protected static PdfDictionary duplicatePdfDictionary(PdfDictionary original, PdfDictionary copy, PdfReader newReader) {
/* 3400 */     if (copy == null)
/* 3401 */       copy = new PdfDictionary(original.size()); 
/* 3402 */     for (PdfName element : original.getKeys()) {
/* 3403 */       PdfName key = element;
/* 3404 */       copy.put(key, duplicatePdfObject(original.get(key), newReader));
/*      */     } 
/* 3406 */     return copy; } protected static PdfObject duplicatePdfObject(PdfObject original, PdfReader newReader) { PRStream pRStream1; PdfArray originalArray; PRIndirectReference org;
/*      */     PRStream stream;
/*      */     PdfArray arr;
/*      */     Iterator<PdfObject> it;
/* 3410 */     if (original == null)
/* 3411 */       return null; 
/* 3412 */     switch (original.type()) {
/*      */       case 6:
/* 3414 */         return duplicatePdfDictionary((PdfDictionary)original, null, newReader);
/*      */       
/*      */       case 7:
/* 3417 */         pRStream1 = (PRStream)original;
/* 3418 */         stream = new PRStream(pRStream1, null, newReader);
/* 3419 */         duplicatePdfDictionary(pRStream1, stream, newReader);
/* 3420 */         return stream;
/*      */       
/*      */       case 5:
/* 3423 */         originalArray = (PdfArray)original;
/* 3424 */         arr = new PdfArray(originalArray.size());
/* 3425 */         for (it = originalArray.listIterator(); it.hasNext();) {
/* 3426 */           arr.add(duplicatePdfObject(it.next(), newReader));
/*      */         }
/* 3428 */         return arr;
/*      */       
/*      */       case 10:
/* 3431 */         org = (PRIndirectReference)original;
/* 3432 */         return new PRIndirectReference(newReader, org.getNumber(), org.getGeneration());
/*      */     } 
/*      */     
/* 3435 */     return original; }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void close() {
/*      */     try {
/* 3444 */       this.tokens.close();
/*      */     }
/* 3446 */     catch (IOException e) {
/* 3447 */       throw new ExceptionConverter(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected void removeUnusedNode(PdfObject obj, boolean[] hits) {
/* 3453 */     Stack<Object> state = new Stack();
/* 3454 */     state.push(obj);
/* 3455 */     while (!state.empty()) {
/* 3456 */       Object current = state.pop();
/* 3457 */       if (current == null)
/*      */         continue; 
/* 3459 */       ArrayList<PdfObject> ar = null;
/* 3460 */       PdfDictionary dic = null;
/* 3461 */       PdfName[] keys = null;
/* 3462 */       Object[] objs = null;
/* 3463 */       int idx = 0;
/* 3464 */       if (current instanceof PdfObject) {
/* 3465 */         PRIndirectReference ref; int num; obj = (PdfObject)current;
/* 3466 */         switch (obj.type()) {
/*      */           case 6:
/*      */           case 7:
/* 3469 */             dic = (PdfDictionary)obj;
/* 3470 */             keys = new PdfName[dic.size()];
/* 3471 */             dic.getKeys().toArray(keys);
/*      */             break;
/*      */           case 5:
/* 3474 */             ar = ((PdfArray)obj).getArrayList();
/*      */             break;
/*      */           case 10:
/* 3477 */             ref = (PRIndirectReference)obj;
/* 3478 */             num = ref.getNumber();
/* 3479 */             if (!hits[num]) {
/* 3480 */               hits[num] = true;
/* 3481 */               state.push(getPdfObjectRelease(ref));
/*      */             } 
/*      */             continue;
/*      */           
/*      */           default:
/*      */             continue;
/*      */         } 
/*      */       } else {
/* 3489 */         objs = (Object[])current;
/* 3490 */         if (objs[0] instanceof ArrayList) {
/* 3491 */           ar = (ArrayList<PdfObject>)objs[0];
/* 3492 */           idx = ((Integer)objs[1]).intValue();
/*      */         } else {
/*      */           
/* 3495 */           keys = (PdfName[])objs[0];
/* 3496 */           dic = (PdfDictionary)objs[1];
/* 3497 */           idx = ((Integer)objs[2]).intValue();
/*      */         } 
/*      */       } 
/* 3500 */       if (ar != null) {
/* 3501 */         for (int i = idx; i < ar.size(); i++) {
/* 3502 */           PdfObject v = ar.get(i);
/* 3503 */           if (v.isIndirect()) {
/* 3504 */             int num = ((PRIndirectReference)v).getNumber();
/* 3505 */             if (num >= this.xrefObj.size() || (!this.partial && this.xrefObj.get(num) == null)) {
/* 3506 */               ar.set(i, PdfNull.PDFNULL);
/*      */               continue;
/*      */             } 
/*      */           } 
/* 3510 */           if (objs == null) {
/* 3511 */             state.push(new Object[] { ar, Integer.valueOf(i + 1) });
/*      */           } else {
/* 3513 */             objs[1] = Integer.valueOf(i + 1);
/* 3514 */             state.push(objs);
/*      */           } 
/* 3516 */           state.push(v);
/*      */         } 
/*      */         
/*      */         continue;
/*      */       } 
/* 3521 */       for (int k = idx; k < keys.length; k++) {
/* 3522 */         PdfName key = keys[k];
/* 3523 */         PdfObject v = dic.get(key);
/* 3524 */         if (v.isIndirect()) {
/* 3525 */           int num = ((PRIndirectReference)v).getNumber();
/* 3526 */           if (num < 0 || num >= this.xrefObj.size() || (!this.partial && this.xrefObj.get(num) == null)) {
/* 3527 */             dic.put(key, PdfNull.PDFNULL);
/*      */             continue;
/*      */           } 
/*      */         } 
/* 3531 */         if (objs == null) {
/* 3532 */           state.push(new Object[] { keys, dic, Integer.valueOf(k + 1) });
/*      */         } else {
/* 3534 */           objs[2] = Integer.valueOf(k + 1);
/* 3535 */           state.push(objs);
/*      */         } 
/* 3537 */         state.push(v);
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
/*      */   public int removeUnusedObjects() {
/* 3549 */     boolean[] hits = new boolean[this.xrefObj.size()];
/* 3550 */     removeUnusedNode(this.trailer, hits);
/* 3551 */     int total = 0;
/* 3552 */     if (this.partial) {
/* 3553 */       for (int k = 1; k < hits.length; k++) {
/* 3554 */         if (!hits[k]) {
/* 3555 */           this.xref[k * 2] = -1L;
/* 3556 */           this.xref[k * 2 + 1] = 0L;
/* 3557 */           this.xrefObj.set(k, null);
/* 3558 */           total++;
/*      */         } 
/*      */       } 
/*      */     } else {
/*      */       
/* 3563 */       for (int k = 1; k < hits.length; k++) {
/* 3564 */         if (!hits[k]) {
/* 3565 */           this.xrefObj.set(k, null);
/* 3566 */           total++;
/*      */         } 
/*      */       } 
/*      */     } 
/* 3570 */     return total;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public AcroFields getAcroFields() {
/* 3577 */     return new AcroFields(this, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getJavaScript(RandomAccessFileOrArray file) throws IOException {
/* 3587 */     PdfDictionary names = (PdfDictionary)getPdfObjectRelease(this.catalog.get(PdfName.NAMES));
/* 3588 */     if (names == null)
/* 3589 */       return null; 
/* 3590 */     PdfDictionary js = (PdfDictionary)getPdfObjectRelease(names.get(PdfName.JAVASCRIPT));
/* 3591 */     if (js == null)
/* 3592 */       return null; 
/* 3593 */     HashMap<String, PdfObject> jscript = PdfNameTree.readTree(js);
/* 3594 */     String[] sortedNames = new String[jscript.size()];
/* 3595 */     sortedNames = (String[])jscript.keySet().toArray((Object[])sortedNames);
/* 3596 */     Arrays.sort((Object[])sortedNames);
/* 3597 */     StringBuffer buf = new StringBuffer();
/* 3598 */     for (int k = 0; k < sortedNames.length; k++) {
/* 3599 */       PdfDictionary j = (PdfDictionary)getPdfObjectRelease(jscript.get(sortedNames[k]));
/* 3600 */       if (j != null) {
/*      */         
/* 3602 */         PdfObject obj = getPdfObjectRelease(j.get(PdfName.JS));
/* 3603 */         if (obj != null)
/* 3604 */           if (obj.isString()) {
/* 3605 */             buf.append(((PdfString)obj).toUnicodeString()).append('\n');
/* 3606 */           } else if (obj.isStream()) {
/* 3607 */             byte[] bytes = getStreamBytes((PRStream)obj, file);
/* 3608 */             if (bytes.length >= 2 && bytes[0] == -2 && bytes[1] == -1) {
/* 3609 */               buf.append(PdfEncodings.convertToString(bytes, "UnicodeBig"));
/*      */             } else {
/* 3611 */               buf.append(PdfEncodings.convertToString(bytes, "PDF"));
/* 3612 */             }  buf.append('\n');
/*      */           }  
/*      */       } 
/*      */     } 
/* 3616 */     return buf.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getJavaScript() throws IOException {
/* 3625 */     RandomAccessFileOrArray rf = getSafeFile();
/*      */     try {
/* 3627 */       rf.reOpen();
/* 3628 */       return getJavaScript(rf);
/*      */     } finally {
/*      */       
/* 3631 */       try { rf.close(); } catch (Exception exception) {}
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void selectPages(String ranges) {
/* 3642 */     selectPages(SequenceList.expand(ranges, getNumberOfPages()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void selectPages(List<Integer> pagesToKeep) {
/* 3652 */     selectPages(pagesToKeep, true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void selectPages(List<Integer> pagesToKeep, boolean removeUnused) {
/* 3663 */     this.pageRefs.selectPages(pagesToKeep);
/* 3664 */     if (removeUnused) removeUnusedObjects();
/*      */   
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setViewerPreferences(int preferences) {
/* 3672 */     this.viewerPreferences.setViewerPreferences(preferences);
/* 3673 */     setViewerPreferences(this.viewerPreferences);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addViewerPreference(PdfName key, PdfObject value) {
/* 3682 */     this.viewerPreferences.addViewerPreference(key, value);
/* 3683 */     setViewerPreferences(this.viewerPreferences);
/*      */   }
/*      */   
/*      */   public void setViewerPreferences(PdfViewerPreferencesImp vp) {
/* 3687 */     vp.addToCatalog(this.catalog);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getSimpleViewerPreferences() {
/* 3696 */     return PdfViewerPreferencesImp.getViewerPreferences(this.catalog).getPageLayoutAndMode();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isAppendable() {
/* 3704 */     return this.appendable;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAppendable(boolean appendable) {
/* 3712 */     this.appendable = appendable;
/* 3713 */     if (appendable) {
/* 3714 */       getPdfObject(this.trailer.get(PdfName.ROOT));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isNewXrefType() {
/* 3722 */     return this.newXrefType;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getFileLength() {
/* 3730 */     return this.fileLength;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isHybridXref() {
/* 3738 */     return this.hybridXref;
/*      */   }
/*      */ 
/*      */   
/*      */   static class PageRefs
/*      */   {
/*      */     private final PdfReader reader;
/*      */     
/*      */     private ArrayList<PRIndirectReference> refsn;
/*      */     
/*      */     private int sizep;
/*      */     private IntHashtable refsp;
/* 3750 */     private int lastPageRead = -1;
/*      */ 
/*      */     
/*      */     private ArrayList<PdfDictionary> pageInh;
/*      */     
/*      */     private boolean keepPages;
/*      */     
/* 3757 */     private Set<PdfObject> pagesNodes = new HashSet<PdfObject>();
/*      */     
/*      */     private PageRefs(PdfReader reader) throws IOException {
/* 3760 */       this.reader = reader;
/* 3761 */       if (reader.partial) {
/* 3762 */         this.refsp = new IntHashtable();
/* 3763 */         PdfNumber npages = (PdfNumber)PdfReader.getPdfObjectRelease(reader.rootPages.get(PdfName.COUNT));
/* 3764 */         this.sizep = npages.intValue();
/*      */       } else {
/*      */         
/* 3767 */         readPages();
/*      */       } 
/*      */     }
/*      */     
/*      */     PageRefs(PageRefs other, PdfReader reader) {
/* 3772 */       this.reader = reader;
/* 3773 */       this.sizep = other.sizep;
/* 3774 */       if (other.refsn != null) {
/* 3775 */         this.refsn = new ArrayList<PRIndirectReference>(other.refsn);
/* 3776 */         for (int k = 0; k < this.refsn.size(); k++) {
/* 3777 */           this.refsn.set(k, (PRIndirectReference)PdfReader.duplicatePdfObject(this.refsn.get(k), reader));
/*      */         }
/*      */       } else {
/*      */         
/* 3781 */         this.refsp = (IntHashtable)other.refsp.clone();
/*      */       } 
/*      */     }
/*      */     int size() {
/* 3785 */       if (this.refsn != null) {
/* 3786 */         return this.refsn.size();
/*      */       }
/* 3788 */       return this.sizep;
/*      */     }
/*      */     
/*      */     void readPages() throws IOException {
/* 3792 */       if (this.refsn != null)
/*      */         return; 
/* 3794 */       this.refsp = null;
/* 3795 */       this.refsn = new ArrayList<PRIndirectReference>();
/* 3796 */       this.pageInh = new ArrayList<PdfDictionary>();
/* 3797 */       iteratePages((PRIndirectReference)this.reader.catalog.get(PdfName.PAGES));
/* 3798 */       this.pageInh = null;
/* 3799 */       this.reader.rootPages.put(PdfName.COUNT, new PdfNumber(this.refsn.size()));
/*      */     }
/*      */     
/*      */     void reReadPages() throws IOException {
/* 3803 */       this.refsn = null;
/* 3804 */       readPages();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public PdfDictionary getPageN(int pageNum) {
/* 3812 */       PRIndirectReference ref = getPageOrigRef(pageNum);
/* 3813 */       return (PdfDictionary)PdfReader.getPdfObject(ref);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public PdfDictionary getPageNRelease(int pageNum) {
/* 3821 */       PdfDictionary page = getPageN(pageNum);
/* 3822 */       releasePage(pageNum);
/* 3823 */       return page;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public PRIndirectReference getPageOrigRefRelease(int pageNum) {
/* 3831 */       PRIndirectReference ref = getPageOrigRef(pageNum);
/* 3832 */       releasePage(pageNum);
/* 3833 */       return ref;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public PRIndirectReference getPageOrigRef(int pageNum) {
/*      */       try {
/* 3843 */         pageNum--;
/* 3844 */         if (pageNum < 0 || pageNum >= size())
/* 3845 */           return null; 
/* 3846 */         if (this.refsn != null) {
/* 3847 */           return this.refsn.get(pageNum);
/*      */         }
/* 3849 */         int n = this.refsp.get(pageNum);
/* 3850 */         if (n == 0) {
/* 3851 */           PRIndirectReference ref = getSinglePage(pageNum);
/* 3852 */           if (this.reader.lastXrefPartial == -1) {
/* 3853 */             this.lastPageRead = -1;
/*      */           } else {
/* 3855 */             this.lastPageRead = pageNum;
/* 3856 */           }  this.reader.lastXrefPartial = -1;
/* 3857 */           this.refsp.put(pageNum, ref.getNumber());
/* 3858 */           if (this.keepPages)
/* 3859 */             this.lastPageRead = -1; 
/* 3860 */           return ref;
/*      */         } 
/*      */         
/* 3863 */         if (this.lastPageRead != pageNum)
/* 3864 */           this.lastPageRead = -1; 
/* 3865 */         if (this.keepPages)
/* 3866 */           this.lastPageRead = -1; 
/* 3867 */         return new PRIndirectReference(this.reader, n);
/*      */ 
/*      */       
/*      */       }
/* 3871 */       catch (Exception e) {
/* 3872 */         throw new ExceptionConverter(e);
/*      */       } 
/*      */     }
/*      */     
/*      */     void keepPages() {
/* 3877 */       if (this.refsp == null || this.keepPages)
/*      */         return; 
/* 3879 */       this.keepPages = true;
/* 3880 */       this.refsp.clear();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void releasePage(int pageNum) {
/* 3887 */       if (this.refsp == null)
/*      */         return; 
/* 3889 */       pageNum--;
/* 3890 */       if (pageNum < 0 || pageNum >= size())
/*      */         return; 
/* 3892 */       if (pageNum != this.lastPageRead)
/*      */         return; 
/* 3894 */       this.lastPageRead = -1;
/* 3895 */       this.reader.lastXrefPartial = this.refsp.get(pageNum);
/* 3896 */       this.reader.releaseLastXrefPartial();
/* 3897 */       this.refsp.remove(pageNum);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void resetReleasePage() {
/* 3904 */       if (this.refsp == null)
/*      */         return; 
/* 3906 */       this.lastPageRead = -1;
/*      */     }
/*      */     
/*      */     void insertPage(int pageNum, PRIndirectReference ref) {
/* 3910 */       pageNum--;
/* 3911 */       if (this.refsn != null) {
/* 3912 */         if (pageNum >= this.refsn.size()) {
/* 3913 */           this.refsn.add(ref);
/*      */         } else {
/* 3915 */           this.refsn.add(pageNum, ref);
/*      */         } 
/*      */       } else {
/* 3918 */         this.sizep++;
/* 3919 */         this.lastPageRead = -1;
/* 3920 */         if (pageNum >= size()) {
/* 3921 */           this.refsp.put(size(), ref.getNumber());
/*      */         } else {
/*      */           
/* 3924 */           IntHashtable refs2 = new IntHashtable((this.refsp.size() + 1) * 2);
/* 3925 */           for (Iterator<IntHashtable.Entry> it = this.refsp.getEntryIterator(); it.hasNext(); ) {
/* 3926 */             IntHashtable.Entry entry = it.next();
/* 3927 */             int p = entry.getKey();
/* 3928 */             refs2.put((p >= pageNum) ? (p + 1) : p, entry.getValue());
/*      */           } 
/* 3930 */           refs2.put(pageNum, ref.getNumber());
/* 3931 */           this.refsp = refs2;
/*      */         } 
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private void pushPageAttributes(PdfDictionary nodePages) {
/* 3941 */       PdfDictionary dic = new PdfDictionary();
/* 3942 */       if (!this.pageInh.isEmpty()) {
/* 3943 */         dic.putAll(this.pageInh.get(this.pageInh.size() - 1));
/*      */       }
/* 3945 */       for (int k = 0; k < PdfReader.pageInhCandidates.length; k++) {
/* 3946 */         PdfObject obj = nodePages.get(PdfReader.pageInhCandidates[k]);
/* 3947 */         if (obj != null)
/* 3948 */           dic.put(PdfReader.pageInhCandidates[k], obj); 
/*      */       } 
/* 3950 */       this.pageInh.add(dic);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private void popPageAttributes() {
/* 3957 */       this.pageInh.remove(this.pageInh.size() - 1);
/*      */     }
/*      */     
/*      */     private void iteratePages(PRIndirectReference rpage) throws IOException {
/* 3961 */       PdfDictionary page = (PdfDictionary)PdfReader.getPdfObject(rpage);
/* 3962 */       if (page == null)
/*      */         return; 
/* 3964 */       if (!this.pagesNodes.add(PdfReader.getPdfObject(rpage)))
/* 3965 */         throw new InvalidPdfException(MessageLocalization.getComposedMessage("illegal.pages.tree", new Object[0])); 
/* 3966 */       PdfArray kidsPR = page.getAsArray(PdfName.KIDS);
/*      */       
/* 3968 */       if (kidsPR == null) {
/* 3969 */         page.put(PdfName.TYPE, PdfName.PAGE);
/* 3970 */         PdfDictionary dic = this.pageInh.get(this.pageInh.size() - 1);
/*      */         
/* 3972 */         for (PdfName element : dic.getKeys()) {
/* 3973 */           PdfName key = element;
/* 3974 */           if (page.get(key) == null)
/* 3975 */             page.put(key, dic.get(key)); 
/*      */         } 
/* 3977 */         if (page.get(PdfName.MEDIABOX) == null) {
/* 3978 */           PdfArray arr = new PdfArray(new float[] { 0.0F, 0.0F, PageSize.LETTER.getRight(), PageSize.LETTER.getTop() });
/* 3979 */           page.put(PdfName.MEDIABOX, arr);
/*      */         } 
/* 3981 */         this.refsn.add(rpage);
/*      */       }
/*      */       else {
/*      */         
/* 3985 */         page.put(PdfName.TYPE, PdfName.PAGES);
/* 3986 */         pushPageAttributes(page);
/* 3987 */         for (int k = 0; k < kidsPR.size(); k++) {
/* 3988 */           PdfObject obj = kidsPR.getPdfObject(k);
/* 3989 */           if (!obj.isIndirect()) {
/* 3990 */             while (k < kidsPR.size())
/* 3991 */               kidsPR.remove(k); 
/*      */             break;
/*      */           } 
/* 3994 */           iteratePages((PRIndirectReference)obj);
/*      */         } 
/* 3996 */         popPageAttributes();
/*      */       } 
/*      */     }
/*      */     
/*      */     protected PRIndirectReference getSinglePage(int n) {
/* 4001 */       PdfDictionary acc = new PdfDictionary();
/* 4002 */       PdfDictionary top = this.reader.rootPages;
/* 4003 */       int base = 0;
/*      */       while (true) {
/* 4005 */         for (int k = 0; k < PdfReader.pageInhCandidates.length; k++) {
/* 4006 */           PdfObject obj = top.get(PdfReader.pageInhCandidates[k]);
/* 4007 */           if (obj != null)
/* 4008 */             acc.put(PdfReader.pageInhCandidates[k], obj); 
/*      */         } 
/* 4010 */         PdfArray kids = (PdfArray)PdfReader.getPdfObjectRelease(top.get(PdfName.KIDS));
/* 4011 */         for (Iterator<PdfObject> it = kids.listIterator(); it.hasNext(); ) {
/* 4012 */           PRIndirectReference ref = (PRIndirectReference)it.next();
/* 4013 */           PdfDictionary dic = (PdfDictionary)PdfReader.getPdfObject(ref);
/* 4014 */           int last = this.reader.lastXrefPartial;
/* 4015 */           PdfObject count = PdfReader.getPdfObjectRelease(dic.get(PdfName.COUNT));
/* 4016 */           this.reader.lastXrefPartial = last;
/* 4017 */           int acn = 1;
/* 4018 */           if (count != null && count.type() == 2)
/* 4019 */             acn = ((PdfNumber)count).intValue(); 
/* 4020 */           if (n < base + acn) {
/* 4021 */             if (count == null) {
/* 4022 */               dic.mergeDifferent(acc);
/* 4023 */               return ref;
/*      */             } 
/* 4025 */             this.reader.releaseLastXrefPartial();
/* 4026 */             top = dic;
/*      */             break;
/*      */           } 
/* 4029 */           this.reader.releaseLastXrefPartial();
/* 4030 */           base += acn;
/*      */         } 
/*      */       } 
/*      */     }
/*      */     
/*      */     private void selectPages(List<Integer> pagesToKeep) {
/* 4036 */       IntHashtable pg = new IntHashtable();
/* 4037 */       ArrayList<Integer> finalPages = new ArrayList<Integer>();
/* 4038 */       int psize = size();
/* 4039 */       for (Integer pi : pagesToKeep) {
/* 4040 */         int p = pi.intValue();
/* 4041 */         if (p >= 1 && p <= psize && pg.put(p, 1) == 0)
/* 4042 */           finalPages.add(pi); 
/*      */       } 
/* 4044 */       if (this.reader.partial) {
/* 4045 */         for (int j = 1; j <= psize; j++) {
/* 4046 */           getPageOrigRef(j);
/* 4047 */           resetReleasePage();
/*      */         } 
/*      */       }
/* 4050 */       PRIndirectReference parent = (PRIndirectReference)this.reader.catalog.get(PdfName.PAGES);
/* 4051 */       PdfDictionary topPages = (PdfDictionary)PdfReader.getPdfObject(parent);
/* 4052 */       ArrayList<PRIndirectReference> newPageRefs = new ArrayList<PRIndirectReference>(finalPages.size());
/* 4053 */       PdfArray kids = new PdfArray();
/* 4054 */       for (int k = 0; k < finalPages.size(); k++) {
/* 4055 */         int p = ((Integer)finalPages.get(k)).intValue();
/* 4056 */         PRIndirectReference pref = getPageOrigRef(p);
/* 4057 */         resetReleasePage();
/* 4058 */         kids.add(pref);
/* 4059 */         newPageRefs.add(pref);
/* 4060 */         getPageN(p).put(PdfName.PARENT, parent);
/*      */       } 
/* 4062 */       AcroFields af = this.reader.getAcroFields();
/* 4063 */       boolean removeFields = (af.getFields().size() > 0);
/* 4064 */       for (int i = 1; i <= psize; i++) {
/* 4065 */         if (!pg.containsKey(i)) {
/* 4066 */           if (removeFields)
/* 4067 */             af.removeFieldsFromPage(i); 
/* 4068 */           PRIndirectReference pref = getPageOrigRef(i);
/* 4069 */           int nref = pref.getNumber();
/* 4070 */           this.reader.xrefObj.set(nref, null);
/* 4071 */           if (this.reader.partial) {
/* 4072 */             this.reader.xref[nref * 2] = -1L;
/* 4073 */             this.reader.xref[nref * 2 + 1] = 0L;
/*      */           } 
/*      */         } 
/*      */       } 
/* 4077 */       topPages.put(PdfName.COUNT, new PdfNumber(finalPages.size()));
/* 4078 */       topPages.put(PdfName.KIDS, kids);
/* 4079 */       this.refsp = null;
/* 4080 */       this.refsn = newPageRefs;
/*      */     }
/*      */   }
/*      */   
/*      */   PdfIndirectReference getCryptoRef() {
/* 4085 */     if (this.cryptoRef == null)
/* 4086 */       return null; 
/* 4087 */     return new PdfIndirectReference(0, this.cryptoRef.getNumber(), this.cryptoRef.getGeneration());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasUsageRights() {
/* 4096 */     PdfDictionary perms = this.catalog.getAsDict(PdfName.PERMS);
/* 4097 */     if (perms == null)
/* 4098 */       return false; 
/* 4099 */     return (perms.contains(PdfName.UR) || perms.contains(PdfName.UR3));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void removeUsageRights() {
/* 4108 */     PdfDictionary perms = this.catalog.getAsDict(PdfName.PERMS);
/* 4109 */     if (perms == null)
/*      */       return; 
/* 4111 */     perms.remove(PdfName.UR);
/* 4112 */     perms.remove(PdfName.UR3);
/* 4113 */     if (perms.size() == 0) {
/* 4114 */       this.catalog.remove(PdfName.PERMS);
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
/*      */   public int getCertificationLevel() {
/* 4128 */     PdfDictionary dic = this.catalog.getAsDict(PdfName.PERMS);
/* 4129 */     if (dic == null)
/* 4130 */       return 0; 
/* 4131 */     dic = dic.getAsDict(PdfName.DOCMDP);
/* 4132 */     if (dic == null)
/* 4133 */       return 0; 
/* 4134 */     PdfArray arr = dic.getAsArray(PdfName.REFERENCE);
/* 4135 */     if (arr == null || arr.size() == 0)
/* 4136 */       return 0; 
/* 4137 */     dic = arr.getAsDict(0);
/* 4138 */     if (dic == null)
/* 4139 */       return 0; 
/* 4140 */     dic = dic.getAsDict(PdfName.TRANSFORMPARAMS);
/* 4141 */     if (dic == null)
/* 4142 */       return 0; 
/* 4143 */     PdfNumber p = dic.getAsNumber(PdfName.P);
/* 4144 */     if (p == null)
/* 4145 */       return 0; 
/* 4146 */     return p.intValue();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isOpenedWithFullPermissions() {
/* 4157 */     return (!this.encrypted || this.ownerPasswordUsed || unethicalreading);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getCryptoMode() {
/* 4164 */     if (this.decrypt == null) {
/* 4165 */       return -1;
/*      */     }
/* 4167 */     return this.decrypt.getCryptoMode();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isMetadataEncrypted() {
/* 4174 */     if (this.decrypt == null) {
/* 4175 */       return false;
/*      */     }
/* 4177 */     return this.decrypt.isMetadataEncrypted();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public byte[] computeUserPassword() {
/* 4188 */     if (!this.encrypted || !this.ownerPasswordUsed) return null; 
/* 4189 */     return this.decrypt.computeUserPassword(this.password);
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/PdfReader.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */