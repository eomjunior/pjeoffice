/*      */ package com.itextpdf.text.pdf;
/*      */ 
/*      */ import com.itextpdf.awt.geom.Rectangle;
/*      */ import com.itextpdf.text.AccessibleElementId;
/*      */ import com.itextpdf.text.BaseColor;
/*      */ import com.itextpdf.text.DocListener;
/*      */ import com.itextpdf.text.DocWriter;
/*      */ import com.itextpdf.text.Document;
/*      */ import com.itextpdf.text.DocumentException;
/*      */ import com.itextpdf.text.ExceptionConverter;
/*      */ import com.itextpdf.text.Image;
/*      */ import com.itextpdf.text.ImgJBIG2;
/*      */ import com.itextpdf.text.ImgWMF;
/*      */ import com.itextpdf.text.Rectangle;
/*      */ import com.itextpdf.text.Version;
/*      */ import com.itextpdf.text.error_messages.MessageLocalization;
/*      */ import com.itextpdf.text.io.TempFileCache;
/*      */ import com.itextpdf.text.log.Counter;
/*      */ import com.itextpdf.text.log.CounterFactory;
/*      */ import com.itextpdf.text.pdf.collection.PdfCollection;
/*      */ import com.itextpdf.text.pdf.events.PdfPageEventForwarder;
/*      */ import com.itextpdf.text.pdf.interfaces.IAccessibleElement;
/*      */ import com.itextpdf.text.pdf.interfaces.PdfAnnotations;
/*      */ import com.itextpdf.text.pdf.interfaces.PdfDocumentActions;
/*      */ import com.itextpdf.text.pdf.interfaces.PdfEncryptionSettings;
/*      */ import com.itextpdf.text.pdf.interfaces.PdfIsoConformance;
/*      */ import com.itextpdf.text.pdf.interfaces.PdfPageActions;
/*      */ import com.itextpdf.text.pdf.interfaces.PdfRunDirection;
/*      */ import com.itextpdf.text.pdf.interfaces.PdfVersion;
/*      */ import com.itextpdf.text.pdf.interfaces.PdfViewerPreferences;
/*      */ import com.itextpdf.text.pdf.interfaces.PdfXConformance;
/*      */ import com.itextpdf.text.pdf.internal.PdfVersionImp;
/*      */ import com.itextpdf.text.pdf.internal.PdfXConformanceImp;
/*      */ import com.itextpdf.text.xml.xmp.XmpWriter;
/*      */ import com.itextpdf.xmp.XMPException;
/*      */ import com.itextpdf.xmp.options.PropertyOptions;
/*      */ import java.io.ByteArrayOutputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.OutputStream;
/*      */ import java.security.cert.Certificate;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.LinkedHashSet;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.TreeMap;
/*      */ import java.util.TreeSet;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ public class PdfWriter
/*      */   extends DocWriter
/*      */   implements PdfViewerPreferences, PdfEncryptionSettings, PdfVersion, PdfDocumentActions, PdfPageActions, PdfRunDirection, PdfAnnotations
/*      */ {
/*      */   public static final int GENERATION_MAX = 65535;
/*      */   
/*      */   public static class PdfBody
/*      */   {
/*      */     private static final int OBJSINSTREAM = 200;
/*      */     protected final TreeSet<PdfCrossReference> xrefs;
/*      */     protected int refnum;
/*      */     protected long position;
/*      */     protected final PdfWriter writer;
/*      */     protected ByteBuffer index;
/*      */     protected ByteBuffer streamObjects;
/*      */     protected int currentObjNum;
/*      */     
/*      */     public static class PdfCrossReference
/*      */       implements Comparable<PdfCrossReference>
/*      */     {
/*      */       private final int type;
/*      */       private final long offset;
/*      */       private final int refnum;
/*      */       private final int generation;
/*      */       
/*      */       public PdfCrossReference(int refnum, long offset, int generation) {
/*  136 */         this.type = 0;
/*  137 */         this.offset = offset;
/*  138 */         this.refnum = refnum;
/*  139 */         this.generation = generation;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public PdfCrossReference(int refnum, long offset) {
/*  149 */         this.type = 1;
/*  150 */         this.offset = offset;
/*  151 */         this.refnum = refnum;
/*  152 */         this.generation = 0;
/*      */       }
/*      */       
/*      */       public PdfCrossReference(int type, int refnum, long offset, int generation) {
/*  156 */         this.type = type;
/*  157 */         this.offset = offset;
/*  158 */         this.refnum = refnum;
/*  159 */         this.generation = generation;
/*      */       }
/*      */       
/*      */       public int getRefnum() {
/*  163 */         return this.refnum;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public void toPdf(OutputStream os) throws IOException {
/*  173 */         StringBuffer off = (new StringBuffer("0000000000")).append(this.offset);
/*  174 */         off.delete(0, off.length() - 10);
/*  175 */         StringBuffer gen = (new StringBuffer("00000")).append(this.generation);
/*  176 */         gen.delete(0, gen.length() - 5);
/*      */         
/*  178 */         off.append(' ').append(gen).append((this.generation == 65535) ? " f \n" : " n \n");
/*  179 */         os.write(DocWriter.getISOBytes(off.toString()));
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public void toPdf(int midSize, OutputStream os) throws IOException {
/*  189 */         os.write((byte)this.type);
/*  190 */         while (--midSize >= 0)
/*  191 */           os.write((byte)(int)(this.offset >>> 8 * midSize & 0xFFL)); 
/*  192 */         os.write((byte)(this.generation >>> 8 & 0xFF));
/*  193 */         os.write((byte)(this.generation & 0xFF));
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public int compareTo(PdfCrossReference other) {
/*  200 */         return (this.refnum < other.refnum) ? -1 : ((this.refnum == other.refnum) ? 0 : 1);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public boolean equals(Object obj) {
/*  208 */         if (obj instanceof PdfCrossReference) {
/*  209 */           PdfCrossReference other = (PdfCrossReference)obj;
/*  210 */           return (this.refnum == other.refnum);
/*      */         } 
/*      */         
/*  213 */         return false;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public int hashCode() {
/*  221 */         return this.refnum;
/*      */       }
/*      */     }
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
/*  239 */     protected int numObj = 0;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected PdfBody(PdfWriter writer) {
/*  248 */       this.xrefs = new TreeSet<PdfCrossReference>();
/*  249 */       this.xrefs.add(new PdfCrossReference(0, 0L, 65535));
/*  250 */       this.position = writer.getOs().getCounter();
/*  251 */       this.refnum = 1;
/*  252 */       this.writer = writer;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     void setRefnum(int refnum) {
/*  258 */       this.refnum = refnum;
/*      */     }
/*      */     
/*      */     protected PdfCrossReference addToObjStm(PdfObject obj, int nObj) throws IOException {
/*  262 */       if (this.numObj >= 200)
/*  263 */         flushObjStm(); 
/*  264 */       if (this.index == null) {
/*  265 */         this.index = new ByteBuffer();
/*  266 */         this.streamObjects = new ByteBuffer();
/*  267 */         this.currentObjNum = getIndirectReferenceNumber();
/*  268 */         this.numObj = 0;
/*      */       } 
/*  270 */       int p = this.streamObjects.size();
/*  271 */       int idx = this.numObj++;
/*  272 */       PdfEncryption enc = this.writer.crypto;
/*  273 */       this.writer.crypto = null;
/*  274 */       obj.toPdf(this.writer, this.streamObjects);
/*  275 */       this.writer.crypto = enc;
/*  276 */       this.streamObjects.append(' ');
/*  277 */       this.index.append(nObj).append(' ').append(p).append(' ');
/*  278 */       return new PdfCrossReference(2, nObj, this.currentObjNum, idx);
/*      */     }
/*      */     
/*      */     public void flushObjStm() throws IOException {
/*  282 */       if (this.numObj == 0)
/*      */         return; 
/*  284 */       int first = this.index.size();
/*  285 */       this.index.append(this.streamObjects);
/*  286 */       PdfStream stream = new PdfStream(this.index.toByteArray());
/*  287 */       stream.flateCompress(this.writer.getCompressionLevel());
/*  288 */       stream.put(PdfName.TYPE, PdfName.OBJSTM);
/*  289 */       stream.put(PdfName.N, new PdfNumber(this.numObj));
/*  290 */       stream.put(PdfName.FIRST, new PdfNumber(first));
/*  291 */       add(stream, this.currentObjNum);
/*  292 */       this.index = null;
/*  293 */       this.streamObjects = null;
/*  294 */       this.numObj = 0;
/*      */     }
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
/*      */     PdfIndirectObject add(PdfObject object) throws IOException {
/*  312 */       return add(object, getIndirectReferenceNumber());
/*      */     }
/*      */     
/*      */     PdfIndirectObject add(PdfObject object, boolean inObjStm) throws IOException {
/*  316 */       return add(object, getIndirectReferenceNumber(), 0, inObjStm);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public PdfIndirectReference getPdfIndirectReference() {
/*  325 */       return new PdfIndirectReference(0, getIndirectReferenceNumber());
/*      */     }
/*      */     
/*      */     protected int getIndirectReferenceNumber() {
/*  329 */       int n = this.refnum++;
/*  330 */       this.xrefs.add(new PdfCrossReference(n, 0L, 65535));
/*  331 */       return n;
/*      */     }
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
/*      */     PdfIndirectObject add(PdfObject object, PdfIndirectReference ref) throws IOException {
/*  351 */       return add(object, ref, true);
/*      */     }
/*      */     
/*      */     PdfIndirectObject add(PdfObject object, PdfIndirectReference ref, boolean inObjStm) throws IOException {
/*  355 */       return add(object, ref.getNumber(), ref.getGeneration(), inObjStm);
/*      */     }
/*      */     
/*      */     PdfIndirectObject add(PdfObject object, int refNumber) throws IOException {
/*  359 */       return add(object, refNumber, 0, true);
/*      */     }
/*      */     protected PdfIndirectObject add(PdfObject object, int refNumber, int generation, boolean inObjStm) throws IOException {
/*      */       PdfIndirectObject indirect;
/*  363 */       if (inObjStm && object.canBeInObjStm() && this.writer.isFullCompression()) {
/*  364 */         PdfCrossReference pxref = addToObjStm(object, refNumber);
/*  365 */         PdfIndirectObject pdfIndirectObject = new PdfIndirectObject(refNumber, object, this.writer);
/*  366 */         if (!this.xrefs.add(pxref)) {
/*  367 */           this.xrefs.remove(pxref);
/*  368 */           this.xrefs.add(pxref);
/*      */         } 
/*  370 */         return pdfIndirectObject;
/*      */       } 
/*      */ 
/*      */       
/*  374 */       if (this.writer.isFullCompression()) {
/*  375 */         indirect = new PdfIndirectObject(refNumber, object, this.writer);
/*  376 */         write(indirect, refNumber);
/*      */       } else {
/*      */         
/*  379 */         indirect = new PdfIndirectObject(refNumber, generation, object, this.writer);
/*  380 */         write(indirect, refNumber, generation);
/*      */       } 
/*  382 */       return indirect;
/*      */     }
/*      */ 
/*      */     
/*      */     protected void write(PdfIndirectObject indirect, int refNumber) throws IOException {
/*  387 */       PdfCrossReference pxref = new PdfCrossReference(refNumber, this.position);
/*  388 */       if (!this.xrefs.add(pxref)) {
/*  389 */         this.xrefs.remove(pxref);
/*  390 */         this.xrefs.add(pxref);
/*      */       } 
/*  392 */       indirect.writeTo(this.writer.getOs());
/*  393 */       this.position = this.writer.getOs().getCounter();
/*      */     }
/*      */     
/*      */     protected void write(PdfIndirectObject indirect, int refNumber, int generation) throws IOException {
/*  397 */       PdfCrossReference pxref = new PdfCrossReference(refNumber, this.position, generation);
/*  398 */       if (!this.xrefs.add(pxref)) {
/*  399 */         this.xrefs.remove(pxref);
/*  400 */         this.xrefs.add(pxref);
/*      */       } 
/*  402 */       indirect.writeTo(this.writer.getOs());
/*  403 */       this.position = this.writer.getOs().getCounter();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public long offset() {
/*  413 */       return this.position;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int size() {
/*  423 */       return Math.max(((PdfCrossReference)this.xrefs.last()).getRefnum() + 1, this.refnum);
/*      */     }
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
/*      */     public void writeCrossReferenceTable(OutputStream os, PdfIndirectReference root, PdfIndirectReference info, PdfIndirectReference encryption, PdfObject fileID, long prevxref) throws IOException {
/*  438 */       int refNumber = 0;
/*  439 */       if (this.writer.isFullCompression()) {
/*  440 */         flushObjStm();
/*  441 */         refNumber = getIndirectReferenceNumber();
/*  442 */         this.xrefs.add(new PdfCrossReference(refNumber, this.position));
/*      */       } 
/*  444 */       PdfCrossReference entry = this.xrefs.first();
/*  445 */       int first = entry.getRefnum();
/*  446 */       int len = 0;
/*  447 */       ArrayList<Integer> sections = new ArrayList<Integer>();
/*  448 */       for (PdfCrossReference pdfCrossReference : this.xrefs) {
/*  449 */         entry = pdfCrossReference;
/*  450 */         if (first + len == entry.getRefnum()) {
/*  451 */           len++; continue;
/*      */         } 
/*  453 */         sections.add(Integer.valueOf(first));
/*  454 */         sections.add(Integer.valueOf(len));
/*  455 */         first = entry.getRefnum();
/*  456 */         len = 1;
/*      */       } 
/*      */       
/*  459 */       sections.add(Integer.valueOf(first));
/*  460 */       sections.add(Integer.valueOf(len));
/*  461 */       if (this.writer.isFullCompression()) {
/*  462 */         int mid = 5;
/*  463 */         long mask = 1095216660480L;
/*  464 */         for (; mid > 1 && (
/*  465 */           mask & this.position) == 0L; mid--)
/*      */         {
/*  467 */           mask >>>= 8L;
/*      */         }
/*  469 */         ByteBuffer buf = new ByteBuffer();
/*      */         
/*  471 */         for (PdfCrossReference element : this.xrefs) {
/*  472 */           entry = element;
/*  473 */           entry.toPdf(mid, buf);
/*      */         } 
/*  475 */         PdfStream xr = new PdfStream(buf.toByteArray());
/*  476 */         buf = null;
/*  477 */         xr.flateCompress(this.writer.getCompressionLevel());
/*  478 */         xr.put(PdfName.SIZE, new PdfNumber(size()));
/*  479 */         xr.put(PdfName.ROOT, root);
/*  480 */         if (info != null) {
/*  481 */           xr.put(PdfName.INFO, info);
/*      */         }
/*  483 */         if (encryption != null)
/*  484 */           xr.put(PdfName.ENCRYPT, encryption); 
/*  485 */         if (fileID != null)
/*  486 */           xr.put(PdfName.ID, fileID); 
/*  487 */         xr.put(PdfName.W, new PdfArray(new int[] { 1, mid, 2 }));
/*  488 */         xr.put(PdfName.TYPE, PdfName.XREF);
/*  489 */         PdfArray idx = new PdfArray();
/*  490 */         for (int k = 0; k < sections.size(); k++)
/*  491 */           idx.add(new PdfNumber(((Integer)sections.get(k)).intValue())); 
/*  492 */         xr.put(PdfName.INDEX, idx);
/*  493 */         if (prevxref > 0L)
/*  494 */           xr.put(PdfName.PREV, new PdfNumber(prevxref)); 
/*  495 */         PdfEncryption enc = this.writer.crypto;
/*  496 */         this.writer.crypto = null;
/*  497 */         PdfIndirectObject indirect = new PdfIndirectObject(refNumber, xr, this.writer);
/*  498 */         indirect.writeTo(this.writer.getOs());
/*  499 */         this.writer.crypto = enc;
/*      */       } else {
/*      */         
/*  502 */         os.write(DocWriter.getISOBytes("xref\n"));
/*  503 */         Iterator<PdfCrossReference> i = this.xrefs.iterator();
/*  504 */         for (int k = 0; k < sections.size(); k += 2) {
/*  505 */           first = ((Integer)sections.get(k)).intValue();
/*  506 */           len = ((Integer)sections.get(k + 1)).intValue();
/*  507 */           os.write(DocWriter.getISOBytes(String.valueOf(first)));
/*  508 */           os.write(DocWriter.getISOBytes(" "));
/*  509 */           os.write(DocWriter.getISOBytes(String.valueOf(len)));
/*  510 */           os.write(10);
/*  511 */           while (len-- > 0) {
/*  512 */             entry = i.next();
/*  513 */             entry.toPdf(os);
/*      */           } 
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
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static class PdfTrailer
/*      */     extends PdfDictionary
/*      */   {
/*      */     long offset;
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
/*      */     public PdfTrailer(int size, long offset, PdfIndirectReference root, PdfIndirectReference info, PdfIndirectReference encryption, PdfObject fileID, long prevxref) {
/*  548 */       this.offset = offset;
/*  549 */       put(PdfName.SIZE, new PdfNumber(size));
/*  550 */       put(PdfName.ROOT, root);
/*  551 */       if (info != null) {
/*  552 */         put(PdfName.INFO, info);
/*      */       }
/*  554 */       if (encryption != null)
/*  555 */         put(PdfName.ENCRYPT, encryption); 
/*  556 */       if (fileID != null)
/*  557 */         put(PdfName.ID, fileID); 
/*  558 */       if (prevxref > 0L) {
/*  559 */         put(PdfName.PREV, new PdfNumber(prevxref));
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void toPdf(PdfWriter writer, OutputStream os) throws IOException {
/*  570 */       PdfWriter.checkPdfIsoConformance(writer, 8, this);
/*  571 */       os.write(DocWriter.getISOBytes("trailer\n"));
/*  572 */       super.toPdf((PdfWriter)null, os);
/*  573 */       os.write(10);
/*  574 */       PdfWriter.writeKeyInfo(os);
/*  575 */       os.write(DocWriter.getISOBytes("startxref\n"));
/*  576 */       os.write(DocWriter.getISOBytes(String.valueOf(this.offset)));
/*  577 */       os.write(DocWriter.getISOBytes("\n%%EOF\n"));
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*  582 */   protected static Counter COUNTER = CounterFactory.getCounter(PdfWriter.class); protected PdfDocument pdf; protected PdfContentByte directContent; protected PdfContentByte directContentUnder; protected PdfBody body; protected ICC_Profile colorProfile; protected PdfDictionary extraCatalog; protected PdfPages root; protected ArrayList<PdfIndirectReference> pageReferences; protected int currentPageNumber; protected PdfName tabs; protected PdfDictionary pageDictEntries; private PdfPageEvent pageEvent; protected long prevxref; protected byte[] originalFileID; protected List<HashMap<String, Object>> newBookmarks; public static final char VERSION_1_2 = '2'; public static final char VERSION_1_3 = '3'; public static final char VERSION_1_4 = '4'; public static final char VERSION_1_5 = '5'; public static final char VERSION_1_6 = '6'; public static final char VERSION_1_7 = '7';
/*      */   protected Counter getCounter() {
/*  584 */     return COUNTER;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected PdfWriter()
/*      */   {
/*  971 */     this.root = new PdfPages(this);
/*      */     
/*  973 */     this.pageReferences = new ArrayList<PdfIndirectReference>();
/*      */     
/*  975 */     this.currentPageNumber = 1;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  980 */     this.tabs = null;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  986 */     this.pageDictEntries = new PdfDictionary();
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
/* 1200 */     this.prevxref = 0L;
/*      */     
/* 1202 */     this.originalFileID = null;
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
/* 1473 */     this.pdf_version = new PdfVersionImp();
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
/*      */ 
/*      */ 
/*      */     
/* 1807 */     this.xmpMetadata = null;
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
/* 1826 */     this.xmpWriter = null;
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
/* 1862 */     this.pdfIsoConformance = initPdfIsoConformance();
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
/* 2223 */     this.fullCompression = false;
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
/* 2249 */     this.compressionLevel = -1;
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
/* 2275 */     this.documentFonts = new LinkedHashMap<BaseFont, FontDetails>();
/*      */ 
/*      */     
/* 2278 */     this.fontNumber = 1;
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
/* 2314 */     this.formXObjects = (HashMap)new HashMap<PdfIndirectReference, Object>();
/*      */ 
/*      */     
/* 2317 */     this.formXObjectsCounter = 1;
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
/* 2386 */     this.readerInstances = new HashMap<PdfReader, PdfReaderInstance>();
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
/* 2465 */     this.documentColors = new HashMap<ICachedColorSpace, ColorDetails>();
/*      */ 
/*      */     
/* 2468 */     this.colorNumber = 1;
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
/* 2495 */     this.documentPatterns = new HashMap<PdfPatternPainter, PdfName>();
/*      */ 
/*      */     
/* 2498 */     this.patternNumber = 1;
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
/* 2516 */     this.documentShadingPatterns = new HashSet<PdfShadingPattern>();
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
/* 2529 */     this.documentShadings = new HashSet<PdfShading>();
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
/* 2540 */     this.documentExtGState = (HashMap)new HashMap<PdfDictionary, PdfObject>();
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
/* 2551 */     this.documentProperties = (HashMap)new HashMap<Object, PdfObject>();
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
/* 2570 */     this.tagged = false;
/* 2571 */     this.taggingMode = 1;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2635 */     this.documentOCG = new LinkedHashSet<PdfOCG>();
/*      */     
/* 2637 */     this.documentOCGorder = new ArrayList<PdfOCG>();
/*      */ 
/*      */ 
/*      */     
/* 2641 */     this.OCGRadioGroup = new PdfArray();
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2646 */     this.OCGLocked = new PdfArray();
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
/*      */ 
/*      */ 
/*      */     
/* 2980 */     this.spaceCharRatio = 2.5F;
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
/* 3021 */     this.runDirection = 1;
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
/* 3062 */     this.defaultColorspace = new PdfDictionary();
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
/* 3090 */     this.documentSpotPatterns = new HashMap<ColorDetails, ColorDetails>();
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
/* 3175 */     this.imageDictionary = new PdfDictionary();
/*      */ 
/*      */     
/* 3178 */     this.images = new HashMap<Long, PdfName>();
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
/* 3329 */     this.JBIG2Globals = new HashMap<PdfStream, PdfIndirectReference>();
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
/* 3416 */     this.ttfUnicodeWriter = null; } protected PdfWriter(PdfDocument document, OutputStream os) { super(document, os); this.root = new PdfPages(this); this.pageReferences = new ArrayList<PdfIndirectReference>(); this.currentPageNumber = 1; this.tabs = null; this.pageDictEntries = new PdfDictionary(); this.prevxref = 0L; this.originalFileID = null; this.pdf_version = new PdfVersionImp(); this.xmpMetadata = null; this.xmpWriter = null; this.pdfIsoConformance = initPdfIsoConformance(); this.fullCompression = false; this.compressionLevel = -1; this.documentFonts = new LinkedHashMap<BaseFont, FontDetails>(); this.fontNumber = 1; this.formXObjects = (HashMap)new HashMap<PdfIndirectReference, Object>(); this.formXObjectsCounter = 1; this.readerInstances = new HashMap<PdfReader, PdfReaderInstance>(); this.documentColors = new HashMap<ICachedColorSpace, ColorDetails>(); this.colorNumber = 1; this.documentPatterns = new HashMap<PdfPatternPainter, PdfName>(); this.patternNumber = 1; this.documentShadingPatterns = new HashSet<PdfShadingPattern>(); this.documentShadings = new HashSet<PdfShading>(); this.documentExtGState = (HashMap)new HashMap<PdfDictionary, PdfObject>(); this.documentProperties = (HashMap)new HashMap<Object, PdfObject>(); this.tagged = false; this.taggingMode = 1; this.documentOCG = new LinkedHashSet<PdfOCG>(); this.documentOCGorder = new ArrayList<PdfOCG>(); this.OCGRadioGroup = new PdfArray(); this.OCGLocked = new PdfArray(); this.spaceCharRatio = 2.5F; this.runDirection = 1; this.defaultColorspace = new PdfDictionary(); this.documentSpotPatterns = new HashMap<ColorDetails, ColorDetails>(); this.imageDictionary = new PdfDictionary(); this.images = new HashMap<Long, PdfName>(); this.JBIG2Globals = new HashMap<PdfStream, PdfIndirectReference>(); this.ttfUnicodeWriter = null; this.pdf = document; this.directContentUnder = new PdfContentByte(this); this.directContent = this.directContentUnder.getDuplicate(); }
/*      */   public static PdfWriter getInstance(Document document, OutputStream os) throws DocumentException { PdfDocument pdf = new PdfDocument(); document.addDocListener((DocListener)pdf); PdfWriter writer = new PdfWriter(pdf, os); pdf.addWriter(writer); return writer; }
/*      */   public static PdfWriter getInstance(Document document, OutputStream os, DocListener listener) throws DocumentException { PdfDocument pdf = new PdfDocument(); pdf.addDocListener(listener); document.addDocListener((DocListener)pdf); PdfWriter writer = new PdfWriter(pdf, os); pdf.addWriter(writer); return writer; }
/* 3419 */   PdfDocument getPdfDocument() { return this.pdf; } public PdfDictionary getInfo() { return this.pdf.getInfo(); } public float getVerticalPosition(boolean ensureNewLine) { return this.pdf.getVerticalPosition(ensureNewLine); } public void setInitialLeading(float leading) throws DocumentException { if (this.open) throw new DocumentException(MessageLocalization.getComposedMessage("you.can.t.set.the.initial.leading.if.the.document.is.already.open", new Object[0]));  this.pdf.setLeading(leading); } public PdfContentByte getDirectContent() { if (!this.open) throw new RuntimeException(MessageLocalization.getComposedMessage("the.document.is.not.open", new Object[0]));  return this.directContent; } public PdfContentByte getDirectContentUnder() { if (!this.open) throw new RuntimeException(MessageLocalization.getComposedMessage("the.document.is.not.open", new Object[0]));  return this.directContentUnder; } void resetContent() { this.directContent.reset(); this.directContentUnder.reset(); } public ICC_Profile getColorProfile() { return this.colorProfile; } void addLocalDestinations(TreeMap<String, PdfDocument.Destination> desto) throws IOException { for (Map.Entry<String, PdfDocument.Destination> entry : desto.entrySet()) { String name = entry.getKey(); PdfDocument.Destination dest = entry.getValue(); PdfDestination destination = dest.destination; if (dest.reference == null) dest.reference = getPdfIndirectReference();  if (destination == null) { addToBody(new PdfString("invalid_" + name), dest.reference); continue; }  addToBody(destination, dest.reference); }  } public PdfIndirectObject addToBody(PdfObject object) throws IOException { PdfIndirectObject iobj = this.body.add(object); cacheObject(iobj); return iobj; } public PdfIndirectObject addToBody(PdfObject object, boolean inObjStm) throws IOException { PdfIndirectObject iobj = this.body.add(object, inObjStm); cacheObject(iobj); return iobj; } public PdfIndirectObject addToBody(PdfObject object, PdfIndirectReference ref) throws IOException { PdfIndirectObject iobj = this.body.add(object, ref); cacheObject(iobj); return iobj; } public PdfIndirectObject addToBody(PdfObject object, PdfIndirectReference ref, boolean inObjStm) throws IOException { PdfIndirectObject iobj = this.body.add(object, ref, inObjStm); cacheObject(iobj); return iobj; } public PdfIndirectObject addToBody(PdfObject object, int refNumber) throws IOException { PdfIndirectObject iobj = this.body.add(object, refNumber); cacheObject(iobj); return iobj; } public PdfIndirectObject addToBody(PdfObject object, int refNumber, boolean inObjStm) throws IOException { PdfIndirectObject iobj = this.body.add(object, refNumber, 0, inObjStm); cacheObject(iobj); return iobj; } protected void cacheObject(PdfIndirectObject iobj) {} public PdfIndirectReference getPdfIndirectReference() { return this.body.getPdfIndirectReference(); } protected int getIndirectReferenceNumber() { return this.body.getIndirectReferenceNumber(); } public OutputStreamCounter getOs() { return this.os; } protected PdfDictionary getCatalog(PdfIndirectReference rootObj) { PdfDictionary catalog = this.pdf.getCatalog(rootObj); buildStructTreeRootForTagged(catalog); if (!this.documentOCG.isEmpty()) { fillOCProperties(false); catalog.put(PdfName.OCPROPERTIES, this.OCProperties); }  return catalog; } protected void buildStructTreeRootForTagged(PdfDictionary catalog) { if (this.tagged) { try { getStructureTreeRoot().buildTree(); for (AccessibleElementId elementId : this.pdf.getStructElements()) { PdfStructureElement element = this.pdf.getStructElement(elementId, false); addToBody(element, element.getReference()); }  } catch (Exception e) { throw new ExceptionConverter(e); }  catalog.put(PdfName.STRUCTTREEROOT, this.structureTreeRoot.getReference()); PdfDictionary mi = new PdfDictionary(); mi.put(PdfName.MARKED, PdfBoolean.PDFTRUE); if (this.userProperties) mi.put(PdfName.USERPROPERTIES, PdfBoolean.PDFTRUE);  catalog.put(PdfName.MARKINFO, mi); }  } public PdfDictionary getExtraCatalog() { if (this.extraCatalog == null) this.extraCatalog = new PdfDictionary();  return this.extraCatalog; } public void addPageDictEntry(PdfName key, PdfObject object) { this.pageDictEntries.put(key, object); } public PdfDictionary getPageDictEntries() { return this.pageDictEntries; } public void resetPageDictEntries() { this.pageDictEntries = new PdfDictionary(); } public void setLinearPageMode() { this.root.setLinearMode(null); } public int reorderPages(int[] order) throws DocumentException { return this.root.reorderPages(order); } public PdfIndirectReference getPageReference(int page) { PdfIndirectReference ref; page--; if (page < 0) throw new IndexOutOfBoundsException(MessageLocalization.getComposedMessage("the.page.number.must.be.gt.eq.1", new Object[0]));  if (page < this.pageReferences.size()) { ref = this.pageReferences.get(page); if (ref == null) { ref = this.body.getPdfIndirectReference(); this.pageReferences.set(page, ref); }  } else { int empty = page - this.pageReferences.size(); for (int k = 0; k < empty; k++) this.pageReferences.add(null);  ref = this.body.getPdfIndirectReference(); this.pageReferences.add(ref); }  return ref; } public int getPageNumber() { return this.pdf.getPageNumber(); } PdfIndirectReference getCurrentPage() { return getPageReference(this.currentPageNumber); } public int getCurrentPageNumber() { return this.currentPageNumber; } public void setPageViewport(PdfArray vp) { addPageDictEntry(PdfName.VP, vp); } public void setTabs(PdfName tabs) { this.tabs = tabs; } public PdfName getTabs() { return this.tabs; } PdfIndirectReference add(PdfPage page, PdfContents contents) throws PdfException { PdfIndirectObject object; if (!this.open) throw new PdfException(MessageLocalization.getComposedMessage("the.document.is.not.open", new Object[0]));  try { object = addToBody(contents); } catch (IOException ioe) { throw new ExceptionConverter(ioe); }  page.add(object.getIndirectReference()); if (this.group != null) { page.put(PdfName.GROUP, this.group); this.group = null; } else if (this.rgbTransparencyBlending) { PdfDictionary pp = new PdfDictionary(); pp.put(PdfName.TYPE, PdfName.GROUP); pp.put(PdfName.S, PdfName.TRANSPARENCY); pp.put(PdfName.CS, PdfName.DEVICERGB); page.put(PdfName.GROUP, pp); }  this.root.addPage(page); this.currentPageNumber++; return null; } public void setPageEvent(PdfPageEvent event) { if (event == null) { this.pageEvent = null; } else if (this.pageEvent == null) { this.pageEvent = event; } else if (this.pageEvent instanceof PdfPageEventForwarder) { ((PdfPageEventForwarder)this.pageEvent).addPageEvent(event); } else { PdfPageEventForwarder forward = new PdfPageEventForwarder(); forward.addPageEvent(this.pageEvent); forward.addPageEvent(event); this.pageEvent = (PdfPageEvent)forward; }  } public PdfPageEvent getPageEvent() { return this.pageEvent; } public void open() { super.open(); try { this.pdf_version.writeHeader(this.os); this.body = new PdfBody(this); if (isPdfX() && ((PdfXConformanceImp)this.pdfIsoConformance).isPdfX32002()) { PdfDictionary sec = new PdfDictionary(); sec.put(PdfName.GAMMA, new PdfArray(new float[] { 2.2F, 2.2F, 2.2F })); sec.put(PdfName.MATRIX, new PdfArray(new float[] { 0.4124F, 0.2126F, 0.0193F, 0.3576F, 0.7152F, 0.1192F, 0.1805F, 0.0722F, 0.9505F })); sec.put(PdfName.WHITEPOINT, new PdfArray(new float[] { 0.9505F, 1.0F, 1.089F })); PdfArray arr = new PdfArray(PdfName.CALRGB); arr.add(sec); setDefaultColorspace(PdfName.DEFAULTRGB, addToBody(arr).getIndirectReference()); }  } catch (IOException ioe) { throw new ExceptionConverter(ioe); }  } public void close() { if (this.open) { if (this.currentPageNumber - 1 != this.pageReferences.size()) throw new RuntimeException("The page " + this.pageReferences.size() + " was requested but the document has only " + (this.currentPageNumber - 1) + " pages.");  this.pdf.close(); try { addSharedObjectsToBody(); for (PdfOCG layer : this.documentOCG) addToBody(layer.getPdfObject(), layer.getRef());  PdfIndirectReference rootRef = this.root.writePageTree(); PdfDictionary catalog = getCatalog(rootRef); if (!this.documentOCG.isEmpty()) checkPdfIsoConformance(this, 7, this.OCProperties);  if (this.xmpMetadata == null && this.xmpWriter != null) try { ByteArrayOutputStream baos = new ByteArrayOutputStream(); this.xmpWriter.serialize(baos); this.xmpWriter.close(); this.xmpMetadata = baos.toByteArray(); } catch (IOException exc) { this.xmpWriter = null; } catch (XMPException exc) { this.xmpWriter = null; }   if (this.xmpMetadata != null) { PdfStream xmp = new PdfStream(this.xmpMetadata); xmp.put(PdfName.TYPE, PdfName.METADATA); xmp.put(PdfName.SUBTYPE, PdfName.XML); if (this.crypto != null && !this.crypto.isMetadataEncrypted()) { PdfArray ar = new PdfArray(); ar.add(PdfName.CRYPT); xmp.put(PdfName.FILTER, ar); }  catalog.put(PdfName.METADATA, this.body.add(xmp).getIndirectReference()); }  getInfo().put(PdfName.PRODUCER, new PdfString(Version.getInstance().getVersion())); if (isPdfX()) { completeInfoDictionary(getInfo()); completeExtraCatalog(getExtraCatalog()); }  if (this.extraCatalog != null) catalog.mergeDifferent(this.extraCatalog);  writeOutlines(catalog, false); PdfIndirectObject indirectCatalog = addToBody(catalog, false); PdfIndirectObject infoObj = addToBody(getInfo(), false); PdfIndirectReference encryption = null; PdfObject fileID = null; this.body.flushObjStm(); boolean isModified = (this.originalFileID != null); if (this.crypto != null) { PdfIndirectObject encryptionObject = addToBody(this.crypto.getEncryptionDictionary(), false); encryption = encryptionObject.getIndirectReference(); fileID = this.crypto.getFileID(isModified); } else { fileID = PdfEncryption.createInfoId(isModified ? this.originalFileID : PdfEncryption.createDocumentId(), isModified); }  this.body.writeCrossReferenceTable(this.os, indirectCatalog.getIndirectReference(), infoObj.getIndirectReference(), encryption, fileID, this.prevxref); if (this.fullCompression) { writeKeyInfo(this.os); this.os.write(getISOBytes("startxref\n")); this.os.write(getISOBytes(String.valueOf(this.body.offset()))); this.os.write(getISOBytes("\n%%EOF\n")); } else { PdfTrailer trailer = new PdfTrailer(this.body.size(), this.body.offset(), indirectCatalog.getIndirectReference(), infoObj.getIndirectReference(), encryption, fileID, this.prevxref); trailer.toPdf(this, this.os); }  } catch (IOException ioe) { throw new ExceptionConverter(ioe); } finally { super.close(); }  }  getCounter().written(this.os.getCounter()); } protected void addXFormsToBody() throws IOException { for (Object[] objs : this.formXObjects.values()) { PdfTemplate template = (PdfTemplate)objs[1]; if (template != null && template.getIndirectReference() instanceof PRIndirectReference) continue;  if (template != null && template.getType() == 1) addToBody(template.getFormXObject(this.compressionLevel), template.getIndirectReference());  }  } protected void addSharedObjectsToBody() throws IOException { for (FontDetails details : this.documentFonts.values()) details.writeFont(this);  addXFormsToBody(); for (PdfReaderInstance element : this.readerInstances.values()) { this.currentPdfReaderInstance = element; this.currentPdfReaderInstance.writeAllPages(); }  this.currentPdfReaderInstance = null; for (ColorDetails color : this.documentColors.values()) addToBody(color.getPdfObject(this), color.getIndirectReference());  for (PdfPatternPainter pat : this.documentPatterns.keySet()) addToBody(pat.getPattern(this.compressionLevel), pat.getIndirectReference());  for (PdfShadingPattern shadingPattern : this.documentShadingPatterns) shadingPattern.addToBody();  for (PdfShading shading : this.documentShadings) shading.addToBody();  for (Map.Entry<PdfDictionary, PdfObject[]> entry : this.documentExtGState.entrySet()) { PdfDictionary gstate = entry.getKey(); PdfObject[] obj = entry.getValue(); addToBody(gstate, (PdfIndirectReference)obj[1]); }  for (Map.Entry<Object, PdfObject[]> entry : this.documentProperties.entrySet()) { Object prop = entry.getKey(); PdfObject[] obj = entry.getValue(); if (prop instanceof PdfLayerMembership) { PdfLayerMembership layer = (PdfLayerMembership)prop; addToBody(layer.getPdfObject(), layer.getRef()); continue; }  if (prop instanceof PdfDictionary && !(prop instanceof PdfLayer)) addToBody((PdfDictionary)prop, (PdfIndirectReference)obj[1]);  }  } public PdfOutline getRootOutline() { return this.directContent.getRootOutline(); } public void setOutlines(List<HashMap<String, Object>> outlines) { this.newBookmarks = outlines; } protected void writeOutlines(PdfDictionary catalog, boolean namedAsNames) throws IOException { if (this.newBookmarks == null || this.newBookmarks.isEmpty()) return;  PdfDictionary top = new PdfDictionary(); PdfIndirectReference topRef = getPdfIndirectReference(); Object[] kids = SimpleBookmark.iterateOutlines(this, topRef, this.newBookmarks, namedAsNames); top.put(PdfName.FIRST, (PdfIndirectReference)kids[0]); top.put(PdfName.LAST, (PdfIndirectReference)kids[1]); top.put(PdfName.COUNT, new PdfNumber(((Integer)kids[2]).intValue())); addToBody(top, topRef); catalog.put(PdfName.OUTLINES, topRef); } public static final PdfName PDF_VERSION_1_2 = new PdfName("1.2"); public static final PdfName PDF_VERSION_1_3 = new PdfName("1.3"); public static final PdfName PDF_VERSION_1_4 = new PdfName("1.4"); public static final PdfName PDF_VERSION_1_5 = new PdfName("1.5"); public static final PdfName PDF_VERSION_1_6 = new PdfName("1.6"); public static final PdfName PDF_VERSION_1_7 = new PdfName("1.7"); protected PdfVersionImp pdf_version; public static final int PageLayoutSinglePage = 1; public static final int PageLayoutOneColumn = 2; public static final int PageLayoutTwoColumnLeft = 4; public static final int PageLayoutTwoColumnRight = 8; public static final int PageLayoutTwoPageLeft = 16; public static final int PageLayoutTwoPageRight = 32; public static final int PageModeUseNone = 64; public static final int PageModeUseOutlines = 128; public static final int PageModeUseThumbs = 256; public static final int PageModeFullScreen = 512; public static final int PageModeUseOC = 1024; public static final int PageModeUseAttachments = 2048; public static final int HideToolbar = 4096; public static final int HideMenubar = 8192; public static final int HideWindowUI = 16384; public static final int FitWindow = 32768; public static final int CenterWindow = 65536; public static final int DisplayDocTitle = 131072; public static final int NonFullScreenPageModeUseNone = 262144; public static final int NonFullScreenPageModeUseOutlines = 524288; public static final int NonFullScreenPageModeUseThumbs = 1048576; public static final int NonFullScreenPageModeUseOC = 2097152; public static final int DirectionL2R = 4194304; public static final int DirectionR2L = 8388608; public static final int PrintScalingNone = 16777216; public void setPdfVersion(char version) { this.pdf_version.setPdfVersion(version); } public void setAtLeastPdfVersion(char version) { this.pdf_version.setAtLeastPdfVersion(version); } public void setPdfVersion(PdfName version) { this.pdf_version.setPdfVersion(version); } public void addDeveloperExtension(PdfDeveloperExtension de) { this.pdf_version.addDeveloperExtension(de); } PdfVersionImp getPdfVersion() { return this.pdf_version; } public void setViewerPreferences(int preferences) { this.pdf.setViewerPreferences(preferences); } public void addViewerPreference(PdfName key, PdfObject value) { this.pdf.addViewerPreference(key, value); } public void setPageLabels(PdfPageLabels pageLabels) { this.pdf.setPageLabels(pageLabels); } public void addNamedDestinations(Map<String, String> map, int page_offset) { for (Map.Entry<String, String> entry : map.entrySet()) { String dest = entry.getValue(); int page = Integer.parseInt(dest.substring(0, dest.indexOf(" "))); PdfDestination destination = new PdfDestination(dest.substring(dest.indexOf(" ") + 1)); addNamedDestination(entry.getKey(), page + page_offset, destination); }  } public void addNamedDestination(String name, int page, PdfDestination dest) { PdfDestination d = new PdfDestination(dest); d.addPage(getPageReference(page)); this.pdf.localDestination(name, d); } public void addJavaScript(PdfAction js) { this.pdf.addJavaScript(js); } public void addJavaScript(String code, boolean unicode) { addJavaScript(PdfAction.javaScript(code, this, unicode)); } public void addJavaScript(String code) { addJavaScript(code, false); } public void addJavaScript(String name, PdfAction js) { this.pdf.addJavaScript(name, js); } public void addJavaScript(String name, String code, boolean unicode) { addJavaScript(name, PdfAction.javaScript(code, this, unicode)); } public void addJavaScript(String name, String code) { addJavaScript(name, code, false); } public void addFileAttachment(String description, byte[] fileStore, String file, String fileDisplay) throws IOException { addFileAttachment(description, PdfFileSpecification.fileEmbedded(this, file, fileDisplay, fileStore)); } public void addFileAttachment(String description, PdfFileSpecification fs) throws IOException { this.pdf.addFileAttachment(description, fs); } public void addFileAttachment(PdfFileSpecification fs) throws IOException { addFileAttachment((String)null, fs); } public static final PdfName DOCUMENT_CLOSE = PdfName.WC; public static final PdfName WILL_SAVE = PdfName.WS; public static final PdfName DID_SAVE = PdfName.DS; public static final PdfName WILL_PRINT = PdfName.WP; public static final PdfName DID_PRINT = PdfName.DP; public static final int SIGNATURE_EXISTS = 1; public static final int SIGNATURE_APPEND_ONLY = 2; protected byte[] xmpMetadata; protected XmpWriter xmpWriter; public static final int PDFXNONE = 0; public static final int PDFX1A2001 = 1; public static final int PDFX32002 = 2; protected PdfIsoConformance pdfIsoConformance; public static final int STANDARD_ENCRYPTION_40 = 0; public static final int STANDARD_ENCRYPTION_128 = 1; public static final int ENCRYPTION_AES_128 = 2; public static final int ENCRYPTION_AES_256 = 3; static final int ENCRYPTION_MASK = 7; public static final int DO_NOT_ENCRYPT_METADATA = 8; public static final int EMBEDDED_FILES_ONLY = 24; public static final int ALLOW_PRINTING = 2052; public static final int ALLOW_MODIFY_CONTENTS = 8; public static final int ALLOW_COPY = 16; public static final int ALLOW_MODIFY_ANNOTATIONS = 32; public static final int ALLOW_FILL_IN = 256; public static final int ALLOW_SCREENREADERS = 512; public static final int ALLOW_ASSEMBLY = 1024; public static final int ALLOW_DEGRADED_PRINTING = 4; @Deprecated public static final int AllowPrinting = 2052; @Deprecated public static final int AllowModifyContents = 8; @Deprecated public static final int AllowCopy = 16; @Deprecated public static final int AllowModifyAnnotations = 32; @Deprecated public static final int AllowFillIn = 256; @Deprecated public static final int AllowScreenReaders = 512; @Deprecated public static final int AllowAssembly = 1024; @Deprecated public static final int AllowDegradedPrinting = 4; @Deprecated public static final boolean STRENGTH40BITS = false; @Deprecated public static final boolean STRENGTH128BITS = true; protected PdfEncryption crypto; protected boolean fullCompression; protected int compressionLevel; protected LinkedHashMap<BaseFont, FontDetails> documentFonts; protected int fontNumber; protected HashMap<PdfIndirectReference, Object[]> formXObjects; protected int formXObjectsCounter; protected TtfUnicodeWriter getTtfUnicodeWriter() { if (this.ttfUnicodeWriter == null)
/* 3420 */       this.ttfUnicodeWriter = new TtfUnicodeWriter(this); 
/* 3421 */     return this.ttfUnicodeWriter; }
/*      */   protected HashMap<PdfReader, PdfReaderInstance> readerInstances;
/*      */   protected PdfReaderInstance currentPdfReaderInstance;
/*      */   protected HashMap<ICachedColorSpace, ColorDetails> documentColors;
/* 3425 */   protected int colorNumber; protected HashMap<PdfPatternPainter, PdfName> documentPatterns; protected int patternNumber; protected HashSet<PdfShadingPattern> documentShadingPatterns; protected HashSet<PdfShading> documentShadings; protected HashMap<PdfDictionary, PdfObject[]> documentExtGState; protected HashMap<Object, PdfObject[]> documentProperties; public static final int markAll = 0; public static final int markInlineElementsOnly = 1; protected boolean tagged; protected int taggingMode; protected PdfStructureTreeRoot structureTreeRoot; protected LinkedHashSet<PdfOCG> documentOCG; protected ArrayList<PdfOCG> documentOCGorder; protected PdfOCProperties OCProperties; protected PdfArray OCGRadioGroup; protected PdfArray OCGLocked; public void setOpenAction(String name) { this.pdf.setOpenAction(name); } public void setOpenAction(PdfAction action) { this.pdf.setOpenAction(action); } public void setAdditionalAction(PdfName actionType, PdfAction action) throws DocumentException { if (!actionType.equals(DOCUMENT_CLOSE) && !actionType.equals(WILL_SAVE) && !actionType.equals(DID_SAVE) && !actionType.equals(WILL_PRINT) && !actionType.equals(DID_PRINT)) throw new DocumentException(MessageLocalization.getComposedMessage("invalid.additional.action.type.1", new Object[] { actionType.toString() }));  this.pdf.addAdditionalAction(actionType, action); } public void setCollection(PdfCollection collection) { setAtLeastPdfVersion('7'); this.pdf.setCollection(collection); } public PdfAcroForm getAcroForm() { return this.pdf.getAcroForm(); } public void addAnnotation(PdfAnnotation annot) { this.pdf.addAnnotation(annot); } void addAnnotation(PdfAnnotation annot, int page) { addAnnotation(annot); } public void addCalculationOrder(PdfFormField annot) { this.pdf.addCalculationOrder(annot); } public void setSigFlags(int f) { this.pdf.setSigFlags(f); } public void setLanguage(String language) { this.pdf.setLanguage(language); } public void setXmpMetadata(byte[] xmpMetadata) { this.xmpMetadata = xmpMetadata; } public void setPageXmpMetadata(byte[] xmpMetadata) throws IOException { this.pdf.setXmpMetadata(xmpMetadata); } public XmpWriter getXmpWriter() { return this.xmpWriter; } public void createXmpMetadata() { try { this.xmpWriter = createXmpWriter((ByteArrayOutputStream)null, this.pdf.getInfo()); if (isTagged()) try { this.xmpWriter.getXmpMeta().setPropertyInteger("http://www.aiim.org/pdfua/ns/id/", "part", 1, new PropertyOptions(1073741824)); } catch (XMPException e) { throw new ExceptionConverter(e); }   this.xmpMetadata = null; } catch (IOException ioe) { ioe.printStackTrace(); }  } protected PdfIsoConformance initPdfIsoConformance() { return (PdfIsoConformance)new PdfXConformanceImp(this); } public void setPDFXConformance(int pdfx) { if (!(this.pdfIsoConformance instanceof PdfXConformanceImp)) return;  if (((PdfXConformance)this.pdfIsoConformance).getPDFXConformance() == pdfx) return;  if (this.pdf.isOpen()) throw new PdfXConformanceException(MessageLocalization.getComposedMessage("pdfx.conformance.can.only.be.set.before.opening.the.document", new Object[0]));  if (this.crypto != null) throw new PdfXConformanceException(MessageLocalization.getComposedMessage("a.pdfx.conforming.document.cannot.be.encrypted", new Object[0]));  if (pdfx != 0) setPdfVersion('3');  ((PdfXConformance)this.pdfIsoConformance).setPDFXConformance(pdfx); } public int getPDFXConformance() { if (this.pdfIsoConformance instanceof PdfXConformanceImp) return ((PdfXConformance)this.pdfIsoConformance).getPDFXConformance();  return 0; } public boolean isPdfX() { if (this.pdfIsoConformance instanceof PdfXConformanceImp) return ((PdfXConformance)this.pdfIsoConformance).isPdfX();  return false; } public boolean isPdfIso() { return this.pdfIsoConformance.isPdfIso(); } public void setOutputIntents(String outputConditionIdentifier, String outputCondition, String registryName, String info, ICC_Profile colorProfile) throws IOException { checkPdfIsoConformance(this, 19, colorProfile); getExtraCatalog(); PdfDictionary out = new PdfDictionary(PdfName.OUTPUTINTENT); if (outputCondition != null) out.put(PdfName.OUTPUTCONDITION, new PdfString(outputCondition, "UnicodeBig"));  if (outputConditionIdentifier != null) out.put(PdfName.OUTPUTCONDITIONIDENTIFIER, new PdfString(outputConditionIdentifier, "UnicodeBig"));  if (registryName != null) out.put(PdfName.REGISTRYNAME, new PdfString(registryName, "UnicodeBig"));  if (info != null) out.put(PdfName.INFO, new PdfString(info, "UnicodeBig"));  if (colorProfile != null) { PdfStream stream = new PdfICCBased(colorProfile, this.compressionLevel); out.put(PdfName.DESTOUTPUTPROFILE, addToBody(stream).getIndirectReference()); }  out.put(PdfName.S, PdfName.GTS_PDFX); this.extraCatalog.put(PdfName.OUTPUTINTENTS, new PdfArray(out)); this.colorProfile = colorProfile; } public void setOutputIntents(String outputConditionIdentifier, String outputCondition, String registryName, String info, byte[] destOutputProfile) throws IOException { ICC_Profile colorProfile = (destOutputProfile == null) ? null : ICC_Profile.getInstance(destOutputProfile); setOutputIntents(outputConditionIdentifier, outputCondition, registryName, info, colorProfile); } public boolean setOutputIntents(PdfReader reader, boolean checkExistence) throws IOException { PdfDictionary catalog = reader.getCatalog(); PdfArray outs = catalog.getAsArray(PdfName.OUTPUTINTENTS); if (outs == null) return false;  if (outs.isEmpty()) return false;  PdfDictionary out = outs.getAsDict(0); PdfObject obj = PdfReader.getPdfObject(out.get(PdfName.S)); if (obj == null || !PdfName.GTS_PDFX.equals(obj)) return false;  if (checkExistence) return true;  PRStream stream = (PRStream)PdfReader.getPdfObject(out.get(PdfName.DESTOUTPUTPROFILE)); byte[] destProfile = null; if (stream != null) destProfile = PdfReader.getStreamBytes(stream);  setOutputIntents(getNameString(out, PdfName.OUTPUTCONDITIONIDENTIFIER), getNameString(out, PdfName.OUTPUTCONDITION), getNameString(out, PdfName.REGISTRYNAME), getNameString(out, PdfName.INFO), destProfile); return true; } protected static String getNameString(PdfDictionary dic, PdfName key) { PdfObject obj = PdfReader.getPdfObject(dic.get(key)); if (obj == null || !obj.isString()) return null;  return ((PdfString)obj).toUnicodeString(); } PdfEncryption getEncryption() { return this.crypto; } public void setEncryption(byte[] userPassword, byte[] ownerPassword, int permissions, int encryptionType) throws DocumentException { if (this.pdf.isOpen()) throw new DocumentException(MessageLocalization.getComposedMessage("encryption.can.only.be.added.before.opening.the.document", new Object[0]));  this.crypto = new PdfEncryption(); this.crypto.setCryptoMode(encryptionType, 0); this.crypto.setupAllKeys(userPassword, ownerPassword, permissions); } public void setEncryption(Certificate[] certs, int[] permissions, int encryptionType) throws DocumentException { if (this.pdf.isOpen()) throw new DocumentException(MessageLocalization.getComposedMessage("encryption.can.only.be.added.before.opening.the.document", new Object[0]));  this.crypto = new PdfEncryption(); if (certs != null) for (int i = 0; i < certs.length; i++) this.crypto.addRecipient(certs[i], permissions[i]);   this.crypto.setCryptoMode(encryptionType, 0); this.crypto.getEncryptionDictionary(); } @Deprecated public void setEncryption(byte[] userPassword, byte[] ownerPassword, int permissions, boolean strength128Bits) throws DocumentException { setEncryption(userPassword, ownerPassword, permissions, strength128Bits ? 1 : 0); } @Deprecated public void setEncryption(boolean strength, String userPassword, String ownerPassword, int permissions) throws DocumentException { setEncryption(getISOBytes(userPassword), getISOBytes(ownerPassword), permissions, strength ? 1 : 0); } @Deprecated public void setEncryption(int encryptionType, String userPassword, String ownerPassword, int permissions) throws DocumentException { setEncryption(getISOBytes(userPassword), getISOBytes(ownerPassword), permissions, encryptionType); } public boolean isFullCompression() { return this.fullCompression; } public void setFullCompression() throws DocumentException { if (this.open) throw new DocumentException(MessageLocalization.getComposedMessage("you.can.t.set.the.full.compression.if.the.document.is.already.open", new Object[0]));  this.fullCompression = true; setAtLeastPdfVersion('5'); } public int getCompressionLevel() { return this.compressionLevel; } public void setCompressionLevel(int compressionLevel) { if (compressionLevel < 0 || compressionLevel > 9) { this.compressionLevel = -1; } else { this.compressionLevel = compressionLevel; }  } FontDetails addSimple(BaseFont bf) { FontDetails ret = this.documentFonts.get(bf); if (ret == null) { checkPdfIsoConformance(this, 4, bf); if (bf.getFontType() == 4) { ret = new FontDetails(new PdfName("F" + this.fontNumber++), ((DocumentFont)bf).getIndirectReference(), bf); } else { ret = new FontDetails(new PdfName("F" + this.fontNumber++), this.body.getPdfIndirectReference(), bf); }  this.documentFonts.put(bf, ret); }  return ret; } void eliminateFontSubset(PdfDictionary fonts) { for (FontDetails element : this.documentFonts.values()) { FontDetails ft = element; if (fonts.get(ft.getFontName()) != null) ft.setSubset(false);  }  } PdfName addDirectTemplateSimple(PdfTemplate template, PdfName forcedName) { PdfIndirectReference ref = template.getIndirectReference(); Object[] obj = this.formXObjects.get(ref); PdfName name = null; try { if (obj == null) { if (forcedName == null) { name = new PdfName("Xf" + this.formXObjectsCounter); this.formXObjectsCounter++; } else { name = forcedName; }  if (template.getType() == 2) { PdfImportedPage ip = (PdfImportedPage)template; PdfReader r = ip.getPdfReaderInstance().getReader(); if (!this.readerInstances.containsKey(r)) this.readerInstances.put(r, ip.getPdfReaderInstance());  template = null; }  this.formXObjects.put(ref, new Object[] { name, template }); } else { name = (PdfName)obj[0]; }  } catch (Exception e) { throw new ExceptionConverter(e); }  return name; } public void releaseTemplate(PdfTemplate tp) throws IOException { PdfIndirectReference ref = tp.getIndirectReference(); Object[] objs = this.formXObjects.get(ref); if (objs == null || objs[1] == null) return;  PdfTemplate template = (PdfTemplate)objs[1]; if (template.getIndirectReference() instanceof PRIndirectReference) return;  if (template.getType() == 1) { addToBody(template.getFormXObject(this.compressionLevel), template.getIndirectReference()); objs[1] = null; }  } public PdfImportedPage getImportedPage(PdfReader reader, int pageNumber) { return getPdfReaderInstance(reader).getImportedPage(pageNumber); } protected PdfReaderInstance getPdfReaderInstance(PdfReader reader) { PdfReaderInstance inst = this.readerInstances.get(reader); if (inst == null) { inst = reader.getPdfReaderInstance(this); this.readerInstances.put(reader, inst); }  return inst; } public void freeReader(PdfReader reader) throws IOException { this.currentPdfReaderInstance = this.readerInstances.get(reader); if (this.currentPdfReaderInstance == null) return;  this.currentPdfReaderInstance.writeAllPages(); this.currentPdfReaderInstance = null; this.readerInstances.remove(reader); } public long getCurrentDocumentSize() { return this.body.offset() + (this.body.size() * 20) + 72L; } protected int getNewObjectNumber(PdfReader reader, int number, int generation) { if (this.currentPdfReaderInstance == null || this.currentPdfReaderInstance.getReader() != reader) this.currentPdfReaderInstance = getPdfReaderInstance(reader);  return this.currentPdfReaderInstance.getNewObjectNumber(number, generation); } RandomAccessFileOrArray getReaderFile(PdfReader reader) { return this.currentPdfReaderInstance.getReaderFile(); } PdfName getColorspaceName() { return new PdfName("CS" + this.colorNumber++); } ColorDetails addSimple(ICachedColorSpace spc) { ColorDetails ret = this.documentColors.get(spc); if (ret == null) { ret = new ColorDetails(getColorspaceName(), this.body.getPdfIndirectReference(), spc); if (spc instanceof IPdfSpecialColorSpace) ((IPdfSpecialColorSpace)spc).getColorantDetails(this);  this.documentColors.put(spc, ret); }  return ret; } PdfName addSimplePattern(PdfPatternPainter painter) { PdfName name = this.documentPatterns.get(painter); try { if (name == null) { name = new PdfName("P" + this.patternNumber); this.patternNumber++; this.documentPatterns.put(painter, name); }  } catch (Exception e) { throw new ExceptionConverter(e); }  return name; } void addSimpleShadingPattern(PdfShadingPattern shading) { if (!this.documentShadingPatterns.contains(shading)) { shading.setName(this.patternNumber); this.patternNumber++; this.documentShadingPatterns.add(shading); addSimpleShading(shading.getShading()); }  } void addSimpleShading(PdfShading shading) { if (!this.documentShadings.contains(shading)) { this.documentShadings.add(shading); shading.setName(this.documentShadings.size()); }  } PdfObject[] addSimpleExtGState(PdfDictionary gstate) { if (!this.documentExtGState.containsKey(gstate)) this.documentExtGState.put(gstate, new PdfObject[] { new PdfName("GS" + (this.documentExtGState.size() + 1)), getPdfIndirectReference() });  return this.documentExtGState.get(gstate); } PdfObject[] addSimpleProperty(Object prop, PdfIndirectReference refi) { if (!this.documentProperties.containsKey(prop)) { if (prop instanceof PdfOCG) checkPdfIsoConformance(this, 7, prop);  this.documentProperties.put(prop, new PdfObject[] { new PdfName("Pr" + (this.documentProperties.size() + 1)), refi }); }  return this.documentProperties.get(prop); } boolean propertyExists(Object prop) { return this.documentProperties.containsKey(prop); } public void setTagged() { setTagged(1); } public void setTagged(int taggingMode) { if (this.open) throw new IllegalArgumentException(MessageLocalization.getComposedMessage("tagging.must.be.set.before.opening.the.document", new Object[0]));  this.tagged = true; this.taggingMode = taggingMode; } public boolean needToBeMarkedInContent(IAccessibleElement element) { if ((this.taggingMode & 0x1) != 0) { if (element.isInline() || PdfName.ARTIFACT.equals(element.getRole())) return true;  return false; }  return true; } public void checkElementRole(IAccessibleElement element, IAccessibleElement parent) { if (parent != null && (parent.getRole() == null || PdfName.ARTIFACT.equals(parent.getRole()))) { element.setRole(null); } else if ((this.taggingMode & 0x1) != 0 && element.isInline() && element.getRole() == null && (parent == null || !parent.isInline())) { throw new IllegalArgumentException(MessageLocalization.getComposedMessage("inline.elements.with.role.null.are.not.allowed", new Object[0])); }  } public boolean isTagged() { return this.tagged; } protected void flushTaggedObjects() throws IOException {} protected void flushAcroFields() throws IOException, BadPdfFormatException {} public PdfStructureTreeRoot getStructureTreeRoot() { if (this.tagged && this.structureTreeRoot == null) this.structureTreeRoot = new PdfStructureTreeRoot(this);  return this.structureTreeRoot; } public PdfOCProperties getOCProperties() { fillOCProperties(true); return this.OCProperties; } public void addOCGRadioGroup(ArrayList<PdfLayer> group) { PdfArray ar = new PdfArray(); for (int k = 0; k < group.size(); k++) { PdfLayer layer = group.get(k); if (layer.getTitle() == null) ar.add(layer.getRef());  }  if (ar.size() == 0) return;  this.OCGRadioGroup.add(ar); } public void lockLayer(PdfLayer layer) { this.OCGLocked.add(layer.getRef()); } private static void getOCGOrder(PdfArray order, PdfLayer layer) { if (!layer.isOnPanel()) return;  if (layer.getTitle() == null) order.add(layer.getRef());  ArrayList<PdfLayer> children = layer.getChildren(); if (children == null) return;  PdfArray kids = new PdfArray(); if (layer.getTitle() != null) kids.add(new PdfString(layer.getTitle(), "UnicodeBig"));  for (int k = 0; k < children.size(); k++) getOCGOrder(kids, children.get(k));  if (kids.size() > 0) order.add(kids);  } private void addASEvent(PdfName event, PdfName category) { PdfArray arr = new PdfArray(); for (PdfOCG element : this.documentOCG) { PdfLayer layer = (PdfLayer)element; PdfDictionary usage = layer.getAsDict(PdfName.USAGE); if (usage != null && usage.get(category) != null) arr.add(layer.getRef());  }  if (arr.size() == 0) return;  PdfDictionary d = this.OCProperties.getAsDict(PdfName.D); PdfArray arras = d.getAsArray(PdfName.AS); if (arras == null) { arras = new PdfArray(); d.put(PdfName.AS, arras); }  PdfDictionary as = new PdfDictionary(); as.put(PdfName.EVENT, event); as.put(PdfName.CATEGORY, new PdfArray(category)); as.put(PdfName.OCGS, arr); arras.add(as); } protected void fillOCProperties(boolean erase) { if (this.OCProperties == null) this.OCProperties = new PdfOCProperties();  if (erase) { this.OCProperties.remove(PdfName.OCGS); this.OCProperties.remove(PdfName.D); }  if (this.OCProperties.get(PdfName.OCGS) == null) { PdfArray pdfArray = new PdfArray(); for (PdfOCG element : this.documentOCG) { PdfLayer layer = (PdfLayer)element; pdfArray.add(layer.getRef()); }  this.OCProperties.put(PdfName.OCGS, pdfArray); }  if (this.OCProperties.get(PdfName.D) != null) return;  ArrayList<PdfOCG> docOrder = new ArrayList<PdfOCG>(this.documentOCGorder); for (Iterator<PdfOCG> it = docOrder.iterator(); it.hasNext(); ) { PdfLayer layer = (PdfLayer)it.next(); if (layer.getParent() != null) it.remove();  }  PdfArray order = new PdfArray(); for (PdfOCG element : docOrder) { PdfLayer layer = (PdfLayer)element; getOCGOrder(order, layer); }  PdfDictionary d = new PdfDictionary(); this.OCProperties.put(PdfName.D, d); d.put(PdfName.ORDER, order); if (docOrder.size() > 0 && docOrder.get(0) instanceof PdfLayer) { PdfLayer l = (PdfLayer)docOrder.get(0); PdfString name = l.getAsString(PdfName.NAME); if (name != null) d.put(PdfName.NAME, name);  }  PdfArray gr = new PdfArray(); for (PdfOCG element : this.documentOCG) { PdfLayer layer = (PdfLayer)element; if (!layer.isOn()) gr.add(layer.getRef());  }  if (gr.size() > 0) d.put(PdfName.OFF, gr);  if (this.OCGRadioGroup.size() > 0) d.put(PdfName.RBGROUPS, this.OCGRadioGroup);  if (this.OCGLocked.size() > 0) d.put(PdfName.LOCKED, this.OCGLocked);  addASEvent(PdfName.VIEW, PdfName.ZOOM); addASEvent(PdfName.VIEW, PdfName.VIEW); addASEvent(PdfName.PRINT, PdfName.PRINT); addASEvent(PdfName.EXPORT, PdfName.EXPORT); d.put(PdfName.LISTMODE, PdfName.VISIBLEPAGES); } void registerLayer(PdfOCG layer) { checkPdfIsoConformance(this, 7, layer); if (layer instanceof PdfLayer) { PdfLayer la = (PdfLayer)layer; if (la.getTitle() == null) { if (!this.documentOCG.contains(layer)) { this.documentOCG.add(layer); this.documentOCGorder.add(layer); }  } else { this.documentOCGorder.add(layer); }  } else { throw new IllegalArgumentException(MessageLocalization.getComposedMessage("only.pdflayer.is.accepted", new Object[0])); }  } public Rectangle getPageSize() { return this.pdf.getPageSize(); } public void setCropBoxSize(Rectangle crop) { this.pdf.setCropBoxSize(crop); } public void setBoxSize(String boxName, Rectangle size) { this.pdf.setBoxSize(boxName, size); } public Rectangle getBoxSize(String boxName) { return this.pdf.getBoxSize(boxName); } public Rectangle getBoxSize(String boxName, Rectangle intersectingRectangle) { Rectangle pdfRectangle = this.pdf.getBoxSize(boxName); if (pdfRectangle == null || intersectingRectangle == null) return null;  Rectangle boxRect = new Rectangle(pdfRectangle); Rectangle intRect = new Rectangle(intersectingRectangle); Rectangle outRect = boxRect.intersection(intRect); if (outRect.isEmpty()) return null;  Rectangle output = new Rectangle((float)outRect.getX(), (float)outRect.getY(), (float)(outRect.getX() + outRect.getWidth()), (float)(outRect.getY() + outRect.getHeight())); output.normalize(); return output; } public void setPageEmpty(boolean pageEmpty) { if (pageEmpty) return;  this.pdf.setPageEmpty(pageEmpty); } public boolean isPageEmpty() { return this.pdf.isPageEmpty(); } public static final PdfName PAGE_OPEN = PdfName.O; public static final PdfName PAGE_CLOSE = PdfName.C; protected PdfDictionary group; public static final float SPACE_CHAR_RATIO_DEFAULT = 2.5F; public static final float NO_SPACE_CHAR_RATIO = 1.0E7F; private float spaceCharRatio; public static final int RUN_DIRECTION_DEFAULT = 0; public static final int RUN_DIRECTION_NO_BIDI = 1; public static final int RUN_DIRECTION_LTR = 2; public static final int RUN_DIRECTION_RTL = 3; protected int runDirection; protected PdfDictionary defaultColorspace; protected HashMap<ColorDetails, ColorDetails> documentSpotPatterns; protected ColorDetails patternColorspaceRGB; protected ColorDetails patternColorspaceGRAY; protected ColorDetails patternColorspaceCMYK; protected PdfDictionary imageDictionary; private final HashMap<Long, PdfName> images; protected HashMap<PdfStream, PdfIndirectReference> JBIG2Globals; private boolean userProperties; private boolean rgbTransparencyBlending; protected TtfUnicodeWriter ttfUnicodeWriter; public void setPageAction(PdfName actionType, PdfAction action) throws DocumentException { if (!actionType.equals(PAGE_OPEN) && !actionType.equals(PAGE_CLOSE)) throw new DocumentException(MessageLocalization.getComposedMessage("invalid.page.additional.action.type.1", new Object[] { actionType.toString() }));  this.pdf.setPageAction(actionType, action); } public void setDuration(int seconds) { this.pdf.setDuration(seconds); } public void setTransition(PdfTransition transition) { this.pdf.setTransition(transition); } public void setThumbnail(Image image) throws PdfException, DocumentException { this.pdf.setThumbnail(image); } public PdfDictionary getGroup() { return this.group; } public void setGroup(PdfDictionary group) { this.group = group; } public float getSpaceCharRatio() { return this.spaceCharRatio; } public void setSpaceCharRatio(float spaceCharRatio) { if (spaceCharRatio < 0.001F) { this.spaceCharRatio = 0.001F; } else { this.spaceCharRatio = spaceCharRatio; }  } public void setRunDirection(int runDirection) { if (runDirection < 1 || runDirection > 3) throw new RuntimeException(MessageLocalization.getComposedMessage("invalid.run.direction.1", runDirection));  this.runDirection = runDirection; } public int getRunDirection() { return this.runDirection; } public void setUserunit(float userunit) throws DocumentException { if (userunit < 1.0F || userunit > 75000.0F) throw new DocumentException(MessageLocalization.getComposedMessage("userunit.should.be.a.value.between.1.and.75000", new Object[0]));  addPageDictEntry(PdfName.USERUNIT, new PdfNumber(userunit)); setAtLeastPdfVersion('6'); } public PdfDictionary getDefaultColorspace() { return this.defaultColorspace; } public void setDefaultColorspace(PdfName key, PdfObject cs) { if (cs == null || cs.isNull()) this.defaultColorspace.remove(key);  this.defaultColorspace.put(key, cs); } ColorDetails addSimplePatternColorspace(BaseColor color) { int type = ExtendedColor.getType(color); if (type == 4 || type == 5) throw new RuntimeException(MessageLocalization.getComposedMessage("an.uncolored.tile.pattern.can.not.have.another.pattern.or.shading.as.color", new Object[0]));  try { ColorDetails details; ColorDetails patternDetails; switch (type) { case 0: if (this.patternColorspaceRGB == null) { this.patternColorspaceRGB = new ColorDetails(getColorspaceName(), this.body.getPdfIndirectReference(), null); PdfArray array = new PdfArray(PdfName.PATTERN); array.add(PdfName.DEVICERGB); addToBody(array, this.patternColorspaceRGB.getIndirectReference()); }  return this.patternColorspaceRGB;case 2: if (this.patternColorspaceCMYK == null) { this.patternColorspaceCMYK = new ColorDetails(getColorspaceName(), this.body.getPdfIndirectReference(), null); PdfArray array = new PdfArray(PdfName.PATTERN); array.add(PdfName.DEVICECMYK); addToBody(array, this.patternColorspaceCMYK.getIndirectReference()); }  return this.patternColorspaceCMYK;case 1: if (this.patternColorspaceGRAY == null) { this.patternColorspaceGRAY = new ColorDetails(getColorspaceName(), this.body.getPdfIndirectReference(), null); PdfArray array = new PdfArray(PdfName.PATTERN); array.add(PdfName.DEVICEGRAY); addToBody(array, this.patternColorspaceGRAY.getIndirectReference()); }  return this.patternColorspaceGRAY;case 3: details = addSimple(((SpotColor)color).getPdfSpotColor()); patternDetails = this.documentSpotPatterns.get(details); if (patternDetails == null) { patternDetails = new ColorDetails(getColorspaceName(), this.body.getPdfIndirectReference(), null); PdfArray array = new PdfArray(PdfName.PATTERN); array.add(details.getIndirectReference()); addToBody(array, patternDetails.getIndirectReference()); this.documentSpotPatterns.put(details, patternDetails); }  return patternDetails; }  throw new RuntimeException(MessageLocalization.getComposedMessage("invalid.color.type", new Object[0])); } catch (Exception e) { throw new RuntimeException(e.getMessage()); }  } public boolean isStrictImageSequence() { return this.pdf.isStrictImageSequence(); } public void setStrictImageSequence(boolean strictImageSequence) { this.pdf.setStrictImageSequence(strictImageSequence); } public void clearTextWrap() throws DocumentException { this.pdf.clearTextWrap(); } public PdfName addDirectImageSimple(Image image) throws PdfException, DocumentException { return addDirectImageSimple(image, (PdfIndirectReference)null); } public PdfName addDirectImageSimple(Image image, PdfIndirectReference fixedRef) throws PdfException, DocumentException { PdfName name; if (this.images.containsKey(image.getMySerialId())) { name = this.images.get(image.getMySerialId()); } else { if (image.isImgTemplate()) { name = new PdfName("img" + this.images.size()); if (image instanceof ImgWMF) try { ImgWMF wmf = (ImgWMF)image; wmf.readWMF(PdfTemplate.createTemplate(this, 0.0F, 0.0F)); } catch (Exception e) { throw new DocumentException(e); }   } else { PdfIndirectReference dref = image.getDirectReference(); if (dref != null) { PdfName rname = new PdfName("img" + this.images.size()); this.images.put(image.getMySerialId(), rname); this.imageDictionary.put(rname, dref); return rname; }  Image maskImage = image.getImageMask(); PdfIndirectReference maskRef = null; if (maskImage != null) { PdfName mname = this.images.get(maskImage.getMySerialId()); maskRef = getImageReference(mname); }  PdfImage i = new PdfImage(image, "img" + this.images.size(), maskRef); if (image instanceof ImgJBIG2) { byte[] globals = ((ImgJBIG2)image).getGlobalBytes(); if (globals != null) { PdfDictionary decodeparms = new PdfDictionary(); decodeparms.put(PdfName.JBIG2GLOBALS, getReferenceJBIG2Globals(globals)); i.put(PdfName.DECODEPARMS, decodeparms); }  }  if (image.hasICCProfile()) { PdfICCBased icc = new PdfICCBased(image.getICCProfile(), image.getCompressionLevel()); PdfIndirectReference iccRef = add(icc); PdfArray iccArray = new PdfArray(); iccArray.add(PdfName.ICCBASED); iccArray.add(iccRef); PdfArray colorspace = i.getAsArray(PdfName.COLORSPACE); if (colorspace != null) { if (colorspace.size() > 1 && PdfName.INDEXED.equals(colorspace.getPdfObject(0))) { colorspace.set(1, iccArray); } else { i.put(PdfName.COLORSPACE, iccArray); }  } else { i.put(PdfName.COLORSPACE, iccArray); }  }  add(i, fixedRef); name = i.name(); }  this.images.put(image.getMySerialId(), name); }  return name; } PdfIndirectReference add(PdfImage pdfImage, PdfIndirectReference fixedRef) throws PdfException { if (!this.imageDictionary.contains(pdfImage.name())) { checkPdfIsoConformance(this, 5, pdfImage); if (fixedRef instanceof PRIndirectReference) { PRIndirectReference r2 = (PRIndirectReference)fixedRef; fixedRef = new PdfIndirectReference(0, getNewObjectNumber(r2.getReader(), r2.getNumber(), r2.getGeneration())); }  try { if (fixedRef == null) { fixedRef = addToBody(pdfImage).getIndirectReference(); } else { addToBody(pdfImage, fixedRef); }  } catch (IOException ioe) { throw new ExceptionConverter(ioe); }  this.imageDictionary.put(pdfImage.name(), fixedRef); return fixedRef; }  return (PdfIndirectReference)this.imageDictionary.get(pdfImage.name()); } PdfIndirectReference getImageReference(PdfName name) { return (PdfIndirectReference)this.imageDictionary.get(name); } protected PdfIndirectReference add(PdfICCBased icc) { PdfIndirectObject object; try { object = addToBody(icc); } catch (IOException ioe) { throw new ExceptionConverter(ioe); }  return object.getIndirectReference(); } protected PdfIndirectReference getReferenceJBIG2Globals(byte[] content) { PdfIndirectObject ref; if (content == null) return null;  for (PdfStream pdfStream : this.JBIG2Globals.keySet()) { if (Arrays.equals(content, pdfStream.getBytes())) return this.JBIG2Globals.get(pdfStream);  }  PdfStream stream = new PdfStream(content); try { ref = addToBody(stream); } catch (IOException e) { return null; }  this.JBIG2Globals.put(stream, ref.getIndirectReference()); return ref.getIndirectReference(); } public boolean isUserProperties() { return this.userProperties; } public void setUserProperties(boolean userProperties) { this.userProperties = userProperties; } public boolean isRgbTransparencyBlending() { return this.rgbTransparencyBlending; } public void setRgbTransparencyBlending(boolean rgbTransparencyBlending) { this.rgbTransparencyBlending = rgbTransparencyBlending; } protected static void writeKeyInfo(OutputStream os) throws IOException { Version version = Version.getInstance(); String k = version.getKey(); if (k == null) k = "iText";  os.write(getISOBytes(String.format("%%%s-%s\n", new Object[] { k, version.getRelease() }))); } protected XmpWriter createXmpWriter(ByteArrayOutputStream baos, PdfDictionary info) throws IOException { return new XmpWriter(baos, info); }
/*      */ 
/*      */   
/*      */   protected XmpWriter createXmpWriter(ByteArrayOutputStream baos, HashMap<String, String> info) throws IOException {
/* 3429 */     return new XmpWriter(baos, info);
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
/*      */   public PdfAnnotation createAnnotation(Rectangle rect, PdfName subtype) {
/* 3441 */     PdfAnnotation a = new PdfAnnotation(this, rect);
/* 3442 */     if (subtype != null)
/* 3443 */       a.put(PdfName.SUBTYPE, subtype); 
/* 3444 */     return a;
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
/*      */   public PdfAnnotation createAnnotation(float llx, float lly, float urx, float ury, PdfString title, PdfString content, PdfName subtype) {
/* 3461 */     PdfAnnotation a = new PdfAnnotation(this, llx, lly, urx, ury, title, content);
/* 3462 */     if (subtype != null)
/* 3463 */       a.put(PdfName.SUBTYPE, subtype); 
/* 3464 */     return a;
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
/*      */   public PdfAnnotation createAnnotation(float llx, float lly, float urx, float ury, PdfAction action, PdfName subtype) {
/* 3480 */     PdfAnnotation a = new PdfAnnotation(this, llx, lly, urx, ury, action);
/* 3481 */     if (subtype != null)
/* 3482 */       a.put(PdfName.SUBTYPE, subtype); 
/* 3483 */     return a;
/*      */   }
/*      */   
/*      */   public static void checkPdfIsoConformance(PdfWriter writer, int key, Object obj1) {
/* 3487 */     if (writer != null)
/* 3488 */       writer.checkPdfIsoConformance(key, obj1); 
/*      */   }
/*      */   
/*      */   public void checkPdfIsoConformance(int key, Object obj1) {
/* 3492 */     this.pdfIsoConformance.checkPdfIsoConformance(key, obj1);
/*      */   }
/*      */   
/*      */   private void completeInfoDictionary(PdfDictionary info) {
/* 3496 */     if (isPdfX()) {
/* 3497 */       if (info.get(PdfName.GTS_PDFXVERSION) == null)
/* 3498 */         if (((PdfXConformanceImp)this.pdfIsoConformance).isPdfX1A2001()) {
/* 3499 */           info.put(PdfName.GTS_PDFXVERSION, new PdfString("PDF/X-1:2001"));
/* 3500 */           info.put(new PdfName("GTS_PDFXConformance"), new PdfString("PDF/X-1a:2001"));
/*      */         }
/* 3502 */         else if (((PdfXConformanceImp)this.pdfIsoConformance).isPdfX32002()) {
/* 3503 */           info.put(PdfName.GTS_PDFXVERSION, new PdfString("PDF/X-3:2002"));
/*      */         }  
/* 3505 */       if (info.get(PdfName.TITLE) == null) {
/* 3506 */         info.put(PdfName.TITLE, new PdfString("Pdf document"));
/*      */       }
/* 3508 */       if (info.get(PdfName.CREATOR) == null) {
/* 3509 */         info.put(PdfName.CREATOR, new PdfString("Unknown"));
/*      */       }
/* 3511 */       if (info.get(PdfName.TRAPPED) == null) {
/* 3512 */         info.put(PdfName.TRAPPED, new PdfName("False"));
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   private void completeExtraCatalog(PdfDictionary extraCatalog) {
/* 3518 */     if (isPdfX() && 
/* 3519 */       extraCatalog.get(PdfName.OUTPUTINTENTS) == null) {
/* 3520 */       PdfDictionary out = new PdfDictionary(PdfName.OUTPUTINTENT);
/* 3521 */       out.put(PdfName.OUTPUTCONDITION, new PdfString("SWOP CGATS TR 001-1995"));
/* 3522 */       out.put(PdfName.OUTPUTCONDITIONIDENTIFIER, new PdfString("CGATS TR 001"));
/* 3523 */       out.put(PdfName.REGISTRYNAME, new PdfString("http://www.color.org"));
/* 3524 */       out.put(PdfName.INFO, new PdfString(""));
/* 3525 */       out.put(PdfName.S, PdfName.GTS_PDFX);
/* 3526 */       extraCatalog.put(PdfName.OUTPUTINTENTS, new PdfArray(out));
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/* 3531 */   private static final List<PdfName> standardStructElems_1_4 = Arrays.asList(new PdfName[] { PdfName.DOCUMENT, PdfName.PART, PdfName.ART, PdfName.SECT, PdfName.DIV, PdfName.BLOCKQUOTE, PdfName.CAPTION, PdfName.TOC, PdfName.TOCI, PdfName.INDEX, PdfName.NONSTRUCT, PdfName.PRIVATE, PdfName.P, PdfName.H, PdfName.H1, PdfName.H2, PdfName.H3, PdfName.H4, PdfName.H5, PdfName.H6, PdfName.L, PdfName.LBL, PdfName.LI, PdfName.LBODY, PdfName.TABLE, PdfName.TR, PdfName.TH, PdfName.TD, PdfName.SPAN, PdfName.QUOTE, PdfName.NOTE, PdfName.REFERENCE, PdfName.BIBENTRY, PdfName.CODE, PdfName.LINK, PdfName.FIGURE, PdfName.FORMULA, PdfName.FORM });
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 3538 */   private static final List<PdfName> standardStructElems_1_7 = Arrays.asList(new PdfName[] { PdfName.DOCUMENT, PdfName.PART, PdfName.ART, PdfName.SECT, PdfName.DIV, PdfName.BLOCKQUOTE, PdfName.CAPTION, PdfName.TOC, PdfName.TOCI, PdfName.INDEX, PdfName.NONSTRUCT, PdfName.PRIVATE, PdfName.P, PdfName.H, PdfName.H1, PdfName.H2, PdfName.H3, PdfName.H4, PdfName.H5, PdfName.H6, PdfName.L, PdfName.LBL, PdfName.LI, PdfName.LBODY, PdfName.TABLE, PdfName.TR, PdfName.TH, PdfName.TD, PdfName.THEAD, PdfName.TBODY, PdfName.TFOOT, PdfName.SPAN, PdfName.QUOTE, PdfName.NOTE, PdfName.REFERENCE, PdfName.BIBENTRY, PdfName.CODE, PdfName.LINK, PdfName.ANNOT, PdfName.RUBY, PdfName.RB, PdfName.RT, PdfName.RP, PdfName.WARICHU, PdfName.WT, PdfName.WP, PdfName.FIGURE, PdfName.FORMULA, PdfName.FORM });
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
/*      */   public List<PdfName> getStandardStructElems() {
/* 3552 */     if (this.pdf_version.getVersion() < '7') {
/* 3553 */       return standardStructElems_1_4;
/*      */     }
/* 3555 */     return standardStructElems_1_7;
/*      */   }
/*      */ 
/*      */   
/*      */   public void useExternalCacheForTagStructure(TempFileCache fileCache) {
/* 3560 */     this.pdf.useExternalCache(fileCache);
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/PdfWriter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */