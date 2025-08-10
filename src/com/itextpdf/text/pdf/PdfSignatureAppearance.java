/*      */ package com.itextpdf.text.pdf;
/*      */ 
/*      */ import com.itextpdf.text.Chunk;
/*      */ import com.itextpdf.text.DocumentException;
/*      */ import com.itextpdf.text.Element;
/*      */ import com.itextpdf.text.Font;
/*      */ import com.itextpdf.text.Image;
/*      */ import com.itextpdf.text.Paragraph;
/*      */ import com.itextpdf.text.Phrase;
/*      */ import com.itextpdf.text.Rectangle;
/*      */ import com.itextpdf.text.Version;
/*      */ import com.itextpdf.text.error_messages.MessageLocalization;
/*      */ import com.itextpdf.text.io.RASInputStream;
/*      */ import com.itextpdf.text.io.RandomAccessSource;
/*      */ import com.itextpdf.text.io.RandomAccessSourceFactory;
/*      */ import com.itextpdf.text.pdf.security.CertificateInfo;
/*      */ import java.io.EOFException;
/*      */ import java.io.File;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.OutputStream;
/*      */ import java.io.RandomAccessFile;
/*      */ import java.security.cert.Certificate;
/*      */ import java.security.cert.X509Certificate;
/*      */ import java.text.SimpleDateFormat;
/*      */ import java.util.Arrays;
/*      */ import java.util.Calendar;
/*      */ import java.util.GregorianCalendar;
/*      */ import java.util.HashMap;
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
/*      */ public class PdfSignatureAppearance
/*      */ {
/*      */   public static final int NOT_CERTIFIED = 0;
/*      */   public static final int CERTIFIED_NO_CHANGES_ALLOWED = 1;
/*      */   public static final int CERTIFIED_FORM_FILLING = 2;
/*      */   public static final int CERTIFIED_FORM_FILLING_AND_ANNOTATIONS = 3;
/*      */   private int certificationLevel;
/*      */   private String reasonCaption;
/*      */   private String locationCaption;
/*      */   private String reason;
/*      */   private String location;
/*      */   private Calendar signDate;
/*      */   private String signatureCreator;
/*      */   private String contact;
/*      */   private RandomAccessFile raf;
/*      */   private byte[] bout;
/*      */   private long[] range;
/*      */   private Certificate signCertificate;
/*      */   private PdfDictionary cryptoDictionary;
/*      */   private SignatureEvent signatureEvent;
/*      */   private String fieldName;
/*      */   private int page;
/*      */   private Rectangle rect;
/*      */   private Rectangle pageRect;
/*      */   private RenderingMode renderingMode;
/*      */   private Image signatureGraphic;
/*      */   private boolean acro6Layers;
/*      */   private PdfTemplate[] app;
/*      */   private boolean reuseAppearance;
/*      */   public static final String questionMark = "% DSUnknown\nq\n1 G\n1 g\n0.1 0 0 0.1 9 0 cm\n0 J 0 j 4 M []0 d\n1 i \n0 g\n313 292 m\n313 404 325 453 432 529 c\n478 561 504 597 504 645 c\n504 736 440 760 391 760 c\n286 760 271 681 265 626 c\n265 625 l\n100 625 l\n100 828 253 898 381 898 c\n451 898 679 878 679 650 c\n679 555 628 499 538 435 c\n488 399 467 376 467 292 c\n313 292 l\nh\n308 214 170 -164 re\nf\n0.44 G\n1.2 w\n1 1 0.4 rg\n287 318 m\n287 430 299 479 406 555 c\n451 587 478 623 478 671 c\n478 762 414 786 365 786 c\n260 786 245 707 239 652 c\n239 651 l\n74 651 l\n74 854 227 924 355 924 c\n425 924 653 904 653 676 c\n653 581 602 525 512 461 c\n462 425 441 402 441 318 c\n287 318 l\nh\n282 240 170 -164 re\nB\nQ\n";
/*      */   private Image image;
/*      */   private float imageScale;
/*      */   private String layer2Text;
/*      */   private Font layer2Font;
/*      */   private int runDirection;
/*      */   private String layer4Text;
/*      */   private PdfTemplate frm;
/*      */   private static final float TOP_SECTION = 0.3F;
/*      */   private static final float MARGIN = 2.0F;
/*      */   private PdfStamper stamper;
/*      */   private PdfStamperImp writer;
/*      */   private ByteBuffer sigout;
/*      */   private OutputStream originalout;
/*      */   private File tempFile;
/*      */   private HashMap<PdfName, PdfLiteral> exclusionLocations;
/*      */   private int boutLen;
/*      */   private boolean preClosed;
/*      */   private PdfSigLockDictionary fieldLock;
/*      */   
/*      */   PdfSignatureAppearance(PdfStamperImp writer) {
/*  115 */     this.certificationLevel = 0;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  137 */     this.reasonCaption = "Reason: ";
/*      */ 
/*      */     
/*  140 */     this.locationCaption = "Location: ";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  409 */     this.page = 1;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  556 */     this.renderingMode = RenderingMode.DESCRIPTION;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  577 */     this.signatureGraphic = null;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  598 */     this.acro6Layers = true;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  619 */     this.app = new PdfTemplate[5];
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  643 */     this.reuseAppearance = false;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  782 */     this.runDirection = 1;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1215 */     this.preClosed = false; this.writer = writer; this.signDate = new GregorianCalendar(); this.fieldName = getNewSigName(); this.signatureCreator = Version.getInstance().getVersion();
/*      */   } public void setCertificationLevel(int certificationLevel) { this.certificationLevel = certificationLevel; } public int getCertificationLevel() { return this.certificationLevel; } public String getReason() { return this.reason; } public void setReason(String reason) { this.reason = reason; } public void setReasonCaption(String reasonCaption) { this.reasonCaption = reasonCaption; } public String getLocation() { return this.location; } public void setLocation(String location) { this.location = location; } public void setLocationCaption(String locationCaption) { this.locationCaption = locationCaption; } public String getSignatureCreator() { return this.signatureCreator; } public void setSignatureCreator(String signatureCreator) { this.signatureCreator = signatureCreator; } public String getContact() { return this.contact; } public void setContact(String contact) { this.contact = contact; } public Calendar getSignDate() { return this.signDate; } public void setSignDate(Calendar signDate) { this.signDate = signDate; } public InputStream getRangeStream() throws IOException { RandomAccessSourceFactory fac = new RandomAccessSourceFactory(); return (InputStream)new RASInputStream(fac.createRanged(getUnderlyingSource(), this.range)); } private RandomAccessSource getUnderlyingSource() throws IOException { RandomAccessSourceFactory fac = new RandomAccessSourceFactory(); return (this.raf == null) ? fac.createSource(this.bout) : fac.createSource(this.raf); } public void addDeveloperExtension(PdfDeveloperExtension de) { this.writer.addDeveloperExtension(de); } public PdfDictionary getCryptoDictionary() { return this.cryptoDictionary; } public void setCryptoDictionary(PdfDictionary cryptoDictionary) { this.cryptoDictionary = cryptoDictionary; } public void setCertificate(Certificate signCertificate) { this.signCertificate = signCertificate; } public Certificate getCertificate() { return this.signCertificate; } public SignatureEvent getSignatureEvent() { return this.signatureEvent; } public void setSignatureEvent(SignatureEvent signatureEvent) { this.signatureEvent = signatureEvent; } public String getFieldName() { return this.fieldName; } public String getNewSigName() { AcroFields af = this.writer.getAcroFields(); String name = "Signature"; int step = 0; boolean found = false; while (!found) { step++; String n1 = name + step; if (af.getFieldItem(n1) != null) continue;  n1 = n1 + "."; found = true; for (String element : af.getFields().keySet()) { String fn = element; if (fn.startsWith(n1)) found = false;  }  }  name = name + step; return name; }
/*      */   public int getPage() { return this.page; }
/*      */   public Rectangle getRect() { return this.rect; }
/*      */   public Rectangle getPageRect() { return this.pageRect; }
/*      */   public boolean isInvisible() { return (this.rect == null || this.rect.getWidth() == 0.0F || this.rect.getHeight() == 0.0F); }
/*      */   public void setVisibleSignature(Rectangle pageRect, int page, String fieldName) { if (fieldName != null) { if (fieldName.indexOf('.') >= 0) throw new IllegalArgumentException(MessageLocalization.getComposedMessage("field.names.cannot.contain.a.dot", new Object[0]));  AcroFields af = this.writer.getAcroFields(); AcroFields.Item item = af.getFieldItem(fieldName); if (item != null) throw new IllegalArgumentException(MessageLocalization.getComposedMessage("the.field.1.already.exists", new Object[] { fieldName }));  this.fieldName = fieldName; }  if (page < 1 || page > this.writer.reader.getNumberOfPages()) throw new IllegalArgumentException(MessageLocalization.getComposedMessage("invalid.page.number.1", page));  this.pageRect = new Rectangle(pageRect); this.pageRect.normalize(); this.rect = new Rectangle(this.pageRect.getWidth(), this.pageRect.getHeight()); this.page = page; }
/*      */   public void setVisibleSignature(String fieldName) { AcroFields af = this.writer.getAcroFields(); AcroFields.Item item = af.getFieldItem(fieldName); if (item == null) throw new IllegalArgumentException(MessageLocalization.getComposedMessage("the.field.1.does.not.exist", new Object[] { fieldName }));  PdfDictionary merged = item.getMerged(0); if (!PdfName.SIG.equals(PdfReader.getPdfObject(merged.get(PdfName.FT)))) throw new IllegalArgumentException(MessageLocalization.getComposedMessage("the.field.1.is.not.a.signature.field", new Object[] { fieldName }));  this.fieldName = fieldName; PdfArray r = merged.getAsArray(PdfName.RECT); float llx = r.getAsNumber(0).floatValue(); float lly = r.getAsNumber(1).floatValue(); float urx = r.getAsNumber(2).floatValue(); float ury = r.getAsNumber(3).floatValue(); this.pageRect = new Rectangle(llx, lly, urx, ury); this.pageRect.normalize(); this.page = item.getPage(0).intValue(); int rotation = this.writer.reader.getPageRotation(this.page); Rectangle pageSize = this.writer.reader.getPageSizeWithRotation(this.page); switch (rotation) { case 90: this.pageRect = new Rectangle(this.pageRect.getBottom(), pageSize.getTop() - this.pageRect.getLeft(), this.pageRect.getTop(), pageSize.getTop() - this.pageRect.getRight()); break;case 180: this.pageRect = new Rectangle(pageSize.getRight() - this.pageRect.getLeft(), pageSize.getTop() - this.pageRect.getBottom(), pageSize.getRight() - this.pageRect.getRight(), pageSize.getTop() - this.pageRect.getTop()); break;case 270: this.pageRect = new Rectangle(pageSize.getRight() - this.pageRect.getBottom(), this.pageRect.getLeft(), pageSize.getRight() - this.pageRect.getTop(), this.pageRect.getRight()); break; }  if (rotation != 0) this.pageRect.normalize();  this.rect = new Rectangle(this.pageRect.getWidth(), this.pageRect.getHeight()); }
/*      */   public static interface SignatureEvent {
/*      */     void getSignatureDictionary(PdfDictionary param1PdfDictionary); }
/* 1225 */   public PdfSigLockDictionary getFieldLockDict() { return this.fieldLock; } public enum RenderingMode {
/*      */     DESCRIPTION, NAME_AND_DESCRIPTION, GRAPHIC_AND_DESCRIPTION, GRAPHIC; } public RenderingMode getRenderingMode() { return this.renderingMode; } public void setRenderingMode(RenderingMode renderingMode) { this.renderingMode = renderingMode; } public Image getSignatureGraphic() { return this.signatureGraphic; } public void setSignatureGraphic(Image signatureGraphic) { this.signatureGraphic = signatureGraphic; } public boolean isAcro6Layers() { return this.acro6Layers; } public void setAcro6Layers(boolean acro6Layers) { this.acro6Layers = acro6Layers; }
/*      */   public PdfTemplate getLayer(int layer) { if (layer < 0 || layer >= this.app.length) return null;  PdfTemplate t = this.app[layer]; if (t == null) { t = this.app[layer] = new PdfTemplate(this.writer); t.setBoundingBox(this.rect); this.writer.addDirectTemplateSimple(t, new PdfName("n" + layer)); }  return t; }
/*      */   public void setReuseAppearance(boolean reuseAppearance) { this.reuseAppearance = reuseAppearance; }
/*      */   public Image getImage() { return this.image; }
/*      */   public void setImage(Image image) { this.image = image; }
/*      */   public float getImageScale() { return this.imageScale; }
/*      */   public void setImageScale(float imageScale) { this.imageScale = imageScale; }
/*      */   public void setLayer2Text(String text) { this.layer2Text = text; }
/*      */   public String getLayer2Text() { return this.layer2Text; }
/*      */   public Font getLayer2Font() { return this.layer2Font; }
/* 1236 */   public void setFieldLockDict(PdfSigLockDictionary fieldLock) { this.fieldLock = fieldLock; }
/*      */   public void setLayer2Font(Font layer2Font) { this.layer2Font = layer2Font; }
/*      */   public void setRunDirection(int runDirection) { if (runDirection < 0 || runDirection > 3)
/*      */       throw new RuntimeException(MessageLocalization.getComposedMessage("invalid.run.direction.1", runDirection)); 
/*      */     this.runDirection = runDirection; }
/*      */   public int getRunDirection() {
/*      */     return this.runDirection;
/*      */   } public void setLayer4Text(String text) {
/*      */     this.layer4Text = text;
/* 1245 */   } public boolean isPreClosed() { return this.preClosed; }
/*      */   public String getLayer4Text() { return this.layer4Text; }
/*      */   public PdfTemplate getTopLayer() { if (this.frm == null) { this.frm = new PdfTemplate(this.writer); this.frm.setBoundingBox(this.rect); this.writer.addDirectTemplateSimple(this.frm, new PdfName("FRM")); }  return this.frm; }
/*      */   public PdfTemplate getAppearance() throws DocumentException { if (isInvisible()) { PdfTemplate t = new PdfTemplate(this.writer); t.setBoundingBox(new Rectangle(0.0F, 0.0F)); this.writer.addDirectTemplateSimple(t, null); return t; }  if (this.app[0] == null && !this.reuseAppearance) createBlankN0();  if (this.app[1] == null && !this.acro6Layers) { PdfTemplate t = this.app[1] = new PdfTemplate(this.writer); t.setBoundingBox(new Rectangle(100.0F, 100.0F)); this.writer.addDirectTemplateSimple(t, new PdfName("n1")); t.setLiteral("% DSUnknown\nq\n1 G\n1 g\n0.1 0 0 0.1 9 0 cm\n0 J 0 j 4 M []0 d\n1 i \n0 g\n313 292 m\n313 404 325 453 432 529 c\n478 561 504 597 504 645 c\n504 736 440 760 391 760 c\n286 760 271 681 265 626 c\n265 625 l\n100 625 l\n100 828 253 898 381 898 c\n451 898 679 878 679 650 c\n679 555 628 499 538 435 c\n488 399 467 376 467 292 c\n313 292 l\nh\n308 214 170 -164 re\nf\n0.44 G\n1.2 w\n1 1 0.4 rg\n287 318 m\n287 430 299 479 406 555 c\n451 587 478 623 478 671 c\n478 762 414 786 365 786 c\n260 786 245 707 239 652 c\n239 651 l\n74 651 l\n74 854 227 924 355 924 c\n425 924 653 904 653 676 c\n653 581 602 525 512 461 c\n462 425 441 402 441 318 c\n287 318 l\nh\n282 240 170 -164 re\nB\nQ\n"); }  if (this.app[2] == null) { String text; Font font; String signedBy; Rectangle sr2; float signedSize; ColumnText ct2; Image im; Paragraph p; float x, y; if (this.layer2Text == null) { StringBuilder buf = new StringBuilder(); buf.append("Digitally signed by "); String name = null; CertificateInfo.X500Name x500name = CertificateInfo.getSubjectFields((X509Certificate)this.signCertificate); if (x500name != null) { name = x500name.getField("CN"); if (name == null) name = x500name.getField("E");  }  if (name == null) name = "";  buf.append(name).append('\n'); SimpleDateFormat sd = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss z"); buf.append("Date: ").append(sd.format(this.signDate.getTime())); if (this.reason != null) buf.append('\n').append(this.reasonCaption).append(this.reason);  if (this.location != null) buf.append('\n').append(this.locationCaption).append(this.location);  text = buf.toString(); } else { text = this.layer2Text; }  PdfTemplate t = this.app[2] = new PdfTemplate(this.writer); t.setBoundingBox(this.rect); this.writer.addDirectTemplateSimple(t, new PdfName("n2")); if (this.image != null) if (this.imageScale == 0.0F) { t.addImage(this.image, this.rect.getWidth(), 0.0F, 0.0F, this.rect.getHeight(), 0.0F, 0.0F); } else { float usableScale = this.imageScale; if (this.imageScale < 0.0F) usableScale = Math.min(this.rect.getWidth() / this.image.getWidth(), this.rect.getHeight() / this.image.getHeight());  float w = this.image.getWidth() * usableScale; float h = this.image.getHeight() * usableScale; float f1 = (this.rect.getWidth() - w) / 2.0F; float f2 = (this.rect.getHeight() - h) / 2.0F; t.addImage(this.image, w, 0.0F, 0.0F, h, f1, f2); }   if (this.layer2Font == null) { font = new Font(); } else { font = new Font(this.layer2Font); }  float size = font.getSize(); Rectangle dataRect = null; Rectangle signatureRect = null; if (this.renderingMode == RenderingMode.NAME_AND_DESCRIPTION || (this.renderingMode == RenderingMode.GRAPHIC_AND_DESCRIPTION && this.signatureGraphic != null)) { signatureRect = new Rectangle(2.0F, 2.0F, this.rect.getWidth() / 2.0F - 2.0F, this.rect.getHeight() - 2.0F); dataRect = new Rectangle(this.rect.getWidth() / 2.0F + 1.0F, 2.0F, this.rect.getWidth() - 1.0F, this.rect.getHeight() - 2.0F); if (this.rect.getHeight() > this.rect.getWidth()) { signatureRect = new Rectangle(2.0F, this.rect.getHeight() / 2.0F, this.rect.getWidth() - 2.0F, this.rect.getHeight()); dataRect = new Rectangle(2.0F, 2.0F, this.rect.getWidth() - 2.0F, this.rect.getHeight() / 2.0F - 2.0F); }  } else if (this.renderingMode == RenderingMode.GRAPHIC) { if (this.signatureGraphic == null)
/*      */           throw new IllegalStateException(MessageLocalization.getComposedMessage("a.signature.image.should.be.present.when.rendering.mode.is.graphic.only", new Object[0]));  signatureRect = new Rectangle(2.0F, 2.0F, this.rect.getWidth() - 2.0F, this.rect.getHeight() - 2.0F); } else { dataRect = new Rectangle(2.0F, 2.0F, this.rect.getWidth() - 2.0F, this.rect.getHeight() * 0.7F - 2.0F); }  switch (this.renderingMode) { case NAME_AND_DESCRIPTION: signedBy = CertificateInfo.getSubjectFields((X509Certificate)this.signCertificate).getField("CN"); if (signedBy == null)
/*      */             signedBy = CertificateInfo.getSubjectFields((X509Certificate)this.signCertificate).getField("E");  if (signedBy == null)
/*      */             signedBy = "";  sr2 = new Rectangle(signatureRect.getWidth() - 2.0F, signatureRect.getHeight() - 2.0F); signedSize = ColumnText.fitText(font, signedBy, sr2, -1.0F, this.runDirection); ct2 = new ColumnText(t); ct2.setRunDirection(this.runDirection); ct2.setSimpleColumn(new Phrase(signedBy, font), signatureRect.getLeft(), signatureRect.getBottom(), signatureRect.getRight(), signatureRect.getTop(), signedSize, 0); ct2.go(); break;case GRAPHIC_AND_DESCRIPTION: if (this.signatureGraphic == null)
/*      */             throw new IllegalStateException(MessageLocalization.getComposedMessage("a.signature.image.should.be.present.when.rendering.mode.is.graphic.and.description", new Object[0]));  ct2 = new ColumnText(t); ct2.setRunDirection(this.runDirection); ct2.setSimpleColumn(signatureRect.getLeft(), signatureRect.getBottom(), signatureRect.getRight(), signatureRect.getTop(), 0.0F, 2); im = Image.getInstance(this.signatureGraphic); im.scaleToFit(signatureRect.getWidth(), signatureRect.getHeight()); p = new Paragraph(); x = 0.0F; y = -im.getScaledHeight() + 15.0F; x += (signatureRect.getWidth() - im.getScaledWidth()) / 2.0F; y -= (signatureRect.getHeight() - im.getScaledHeight()) / 2.0F; p.add((Element)new Chunk(im, x + (signatureRect.getWidth() - im.getScaledWidth()) / 2.0F, y, false)); ct2.addElement((Element)p); ct2.go(); break;case GRAPHIC: ct2 = new ColumnText(t); ct2.setRunDirection(this.runDirection); ct2.setSimpleColumn(signatureRect.getLeft(), signatureRect.getBottom(), signatureRect.getRight(), signatureRect.getTop(), 0.0F, 2); im = Image.getInstance(this.signatureGraphic); im.scaleToFit(signatureRect.getWidth(), signatureRect.getHeight()); p = new Paragraph(signatureRect.getHeight()); x = (signatureRect.getWidth() - im.getScaledWidth()) / 2.0F; y = (signatureRect.getHeight() - im.getScaledHeight()) / 2.0F; p.add((Element)new Chunk(im, x, y, false)); ct2.addElement((Element)p); ct2.go(); break; }  if (this.renderingMode != RenderingMode.GRAPHIC) { if (size <= 0.0F) { Rectangle sr = new Rectangle(dataRect.getWidth(), dataRect.getHeight()); size = ColumnText.fitText(font, text, sr, 12.0F, this.runDirection); }  ColumnText ct = new ColumnText(t); ct.setRunDirection(this.runDirection); ct.setSimpleColumn(new Phrase(text, font), dataRect.getLeft(), dataRect.getBottom(), dataRect.getRight(), dataRect.getTop(), size, 0); ct.go(); }  }  if (this.app[3] == null && !this.acro6Layers) { PdfTemplate t = this.app[3] = new PdfTemplate(this.writer); t.setBoundingBox(new Rectangle(100.0F, 100.0F)); this.writer.addDirectTemplateSimple(t, new PdfName("n3")); t.setLiteral("% DSBlank\n"); }  if (this.app[4] == null && !this.acro6Layers) { Font font; PdfTemplate t = this.app[4] = new PdfTemplate(this.writer); t.setBoundingBox(new Rectangle(0.0F, this.rect.getHeight() * 0.7F, this.rect.getRight(), this.rect.getTop())); this.writer.addDirectTemplateSimple(t, new PdfName("n4")); if (this.layer2Font == null) { font = new Font(); } else { font = new Font(this.layer2Font); }  String text = "Signature Not Verified"; if (this.layer4Text != null)
/*      */         text = this.layer4Text;  Rectangle sr = new Rectangle(this.rect.getWidth() - 4.0F, this.rect.getHeight() * 0.3F - 4.0F); float size = ColumnText.fitText(font, text, sr, 15.0F, this.runDirection); ColumnText ct = new ColumnText(t); ct.setRunDirection(this.runDirection); ct.setSimpleColumn(new Phrase(text, font), 2.0F, 0.0F, this.rect.getWidth() - 2.0F, this.rect.getHeight() - 2.0F, size, 0); ct.go(); }  int rotation = this.writer.reader.getPageRotation(this.page); Rectangle rotated = new Rectangle(this.rect); int n = rotation; while (n > 0) { rotated = rotated.rotate(); n -= 90; }  if (this.frm == null) { this.frm = new PdfTemplate(this.writer); this.frm.setBoundingBox(rotated); this.writer.addDirectTemplateSimple(this.frm, new PdfName("FRM")); float scale = Math.min(this.rect.getWidth(), this.rect.getHeight()) * 0.9F; float x = (this.rect.getWidth() - scale) / 2.0F; float y = (this.rect.getHeight() - scale) / 2.0F; scale /= 100.0F; if (rotation == 90) { this.frm.concatCTM(0.0F, 1.0F, -1.0F, 0.0F, this.rect.getHeight(), 0.0F); } else if (rotation == 180) { this.frm.concatCTM(-1.0F, 0.0F, 0.0F, -1.0F, this.rect.getWidth(), this.rect.getHeight()); } else if (rotation == 270) { this.frm.concatCTM(0.0F, -1.0F, 1.0F, 0.0F, 0.0F, this.rect.getWidth()); }  if (this.reuseAppearance) { AcroFields af = this.writer.getAcroFields(); PdfIndirectReference ref = af.getNormalAppearance(getFieldName()); if (ref != null) { this.frm.addTemplateReference(ref, new PdfName("n0"), 1.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F); } else { this.reuseAppearance = false; if (this.app[0] == null)
/*      */             createBlankN0();  }  }  if (!this.reuseAppearance)
/*      */         this.frm.addTemplate(this.app[0], 0.0F, 0.0F);  if (!this.acro6Layers)
/*      */         this.frm.addTemplate(this.app[1], scale, 0.0F, 0.0F, scale, x, y);  this.frm.addTemplate(this.app[2], 0.0F, 0.0F); if (!this.acro6Layers) { this.frm.addTemplate(this.app[3], scale, 0.0F, 0.0F, scale, x, y); this.frm.addTemplate(this.app[4], 0.0F, 0.0F); }  }  PdfTemplate napp = new PdfTemplate(this.writer); napp.setBoundingBox(rotated); this.writer.addDirectTemplateSimple(napp, null); napp.addTemplate(this.frm, 0.0F, 0.0F); return napp; }
/*      */   private void createBlankN0() { PdfTemplate t = this.app[0] = new PdfTemplate(this.writer); t.setBoundingBox(new Rectangle(100.0F, 100.0F)); this.writer.addDirectTemplateSimple(t, new PdfName("n0")); t.setLiteral("% DSBlank\n"); }
/*      */   public PdfStamper getStamper() { return this.stamper; }
/*      */   void setStamper(PdfStamper stamper) { this.stamper = stamper; }
/*      */   ByteBuffer getSigout() { return this.sigout; }
/*      */   void setSigout(ByteBuffer sigout) { this.sigout = sigout; }
/*      */   OutputStream getOriginalout() { return this.originalout; }
/*      */   void setOriginalout(OutputStream originalout) { this.originalout = originalout; }
/*      */   public File getTempFile() { return this.tempFile; }
/* 1265 */   void setTempFile(File tempFile) { this.tempFile = tempFile; } public void preClose(HashMap<PdfName, Integer> exclusionSizes) throws IOException, DocumentException { if (this.preClosed)
/* 1266 */       throw new DocumentException(MessageLocalization.getComposedMessage("document.already.pre.closed", new Object[0])); 
/* 1267 */     this.stamper.mergeVerification();
/* 1268 */     this.preClosed = true;
/* 1269 */     AcroFields af = this.writer.getAcroFields();
/* 1270 */     String name = getFieldName();
/* 1271 */     boolean fieldExists = af.doesSignatureFieldExist(name);
/* 1272 */     PdfIndirectReference refSig = this.writer.getPdfIndirectReference();
/* 1273 */     this.writer.setSigFlags(3);
/* 1274 */     PdfDictionary fieldLock = null;
/* 1275 */     if (fieldExists) {
/* 1276 */       PdfDictionary widget = af.getFieldItem(name).getWidget(0);
/* 1277 */       this.writer.markUsed(widget);
/* 1278 */       fieldLock = widget.getAsDict(PdfName.LOCK);
/*      */       
/* 1280 */       if (fieldLock == null && this.fieldLock != null) {
/* 1281 */         widget.put(PdfName.LOCK, this.writer.addToBody(this.fieldLock).getIndirectReference());
/* 1282 */         fieldLock = this.fieldLock;
/*      */       } 
/*      */       
/* 1285 */       widget.put(PdfName.P, this.writer.getPageReference(getPage()));
/* 1286 */       widget.put(PdfName.V, refSig);
/* 1287 */       PdfObject obj = PdfReader.getPdfObjectRelease(widget.get(PdfName.F));
/* 1288 */       int flags = 0;
/* 1289 */       if (obj != null && obj.isNumber())
/* 1290 */         flags = ((PdfNumber)obj).intValue(); 
/* 1291 */       flags |= 0x80;
/* 1292 */       widget.put(PdfName.F, new PdfNumber(flags));
/* 1293 */       PdfDictionary ap = new PdfDictionary();
/* 1294 */       ap.put(PdfName.N, getAppearance().getIndirectReference());
/* 1295 */       widget.put(PdfName.AP, ap);
/*      */     } else {
/*      */       
/* 1298 */       PdfFormField sigField = PdfFormField.createSignature(this.writer);
/* 1299 */       sigField.setFieldName(name);
/* 1300 */       sigField.put(PdfName.V, refSig);
/* 1301 */       sigField.setFlags(132);
/*      */       
/* 1303 */       if (this.fieldLock != null) {
/* 1304 */         sigField.put(PdfName.LOCK, this.writer.addToBody(this.fieldLock).getIndirectReference());
/* 1305 */         fieldLock = this.fieldLock;
/*      */       } 
/*      */       
/* 1308 */       int pagen = getPage();
/* 1309 */       if (!isInvisible()) {
/* 1310 */         sigField.setWidget(getPageRect(), (PdfName)null);
/*      */       } else {
/* 1312 */         sigField.setWidget(new Rectangle(0.0F, 0.0F), (PdfName)null);
/* 1313 */       }  sigField.setAppearance(PdfAnnotation.APPEARANCE_NORMAL, getAppearance());
/* 1314 */       sigField.setPage(pagen);
/* 1315 */       this.writer.addAnnotation(sigField, pagen);
/*      */     } 
/*      */     
/* 1318 */     this.exclusionLocations = new HashMap<PdfName, PdfLiteral>();
/* 1319 */     if (this.cryptoDictionary == null) {
/* 1320 */       throw new DocumentException("No crypto dictionary defined.");
/*      */     }
/*      */     
/* 1323 */     PdfLiteral lit = new PdfLiteral(80);
/* 1324 */     this.exclusionLocations.put(PdfName.BYTERANGE, lit);
/* 1325 */     this.cryptoDictionary.put(PdfName.BYTERANGE, lit);
/* 1326 */     for (Map.Entry<PdfName, Integer> entry : exclusionSizes.entrySet()) {
/* 1327 */       PdfName key = entry.getKey();
/* 1328 */       Integer v = entry.getValue();
/* 1329 */       lit = new PdfLiteral(v.intValue());
/* 1330 */       this.exclusionLocations.put(key, lit);
/* 1331 */       this.cryptoDictionary.put(key, lit);
/*      */     } 
/* 1333 */     if (this.certificationLevel > 0)
/* 1334 */       addDocMDP(this.cryptoDictionary); 
/* 1335 */     if (fieldLock != null)
/* 1336 */       addFieldMDP(this.cryptoDictionary, fieldLock); 
/* 1337 */     if (this.signatureEvent != null)
/* 1338 */       this.signatureEvent.getSignatureDictionary(this.cryptoDictionary); 
/* 1339 */     this.writer.addToBody(this.cryptoDictionary, refSig, false);
/*      */     
/* 1341 */     if (this.certificationLevel > 0) {
/*      */       
/* 1343 */       PdfDictionary docmdp = new PdfDictionary();
/* 1344 */       docmdp.put(new PdfName("DocMDP"), refSig);
/* 1345 */       this.writer.reader.getCatalog().put(new PdfName("Perms"), docmdp);
/*      */     } 
/* 1347 */     this.writer.close(this.stamper.getMoreInfo());
/*      */     
/* 1349 */     this.range = new long[this.exclusionLocations.size() * 2];
/* 1350 */     long byteRangePosition = ((PdfLiteral)this.exclusionLocations.get(PdfName.BYTERANGE)).getPosition();
/* 1351 */     this.exclusionLocations.remove(PdfName.BYTERANGE);
/* 1352 */     int idx = 1;
/* 1353 */     for (PdfLiteral pdfLiteral : this.exclusionLocations.values()) {
/* 1354 */       long n = pdfLiteral.getPosition();
/* 1355 */       this.range[idx++] = n;
/* 1356 */       this.range[idx++] = pdfLiteral.getPosLength() + n;
/*      */     } 
/* 1358 */     Arrays.sort(this.range, 1, this.range.length - 1);
/* 1359 */     for (int k = 3; k < this.range.length - 2; k += 2) {
/* 1360 */       this.range[k] = this.range[k] - this.range[k - 1];
/*      */     }
/* 1362 */     if (this.tempFile == null) {
/* 1363 */       this.bout = this.sigout.getBuffer();
/* 1364 */       this.boutLen = this.sigout.size();
/* 1365 */       this.range[this.range.length - 1] = this.boutLen - this.range[this.range.length - 2];
/* 1366 */       ByteBuffer bf = new ByteBuffer();
/* 1367 */       bf.append('[');
/* 1368 */       for (int i = 0; i < this.range.length; i++)
/* 1369 */         bf.append(this.range[i]).append(' '); 
/* 1370 */       bf.append(']');
/* 1371 */       System.arraycopy(bf.getBuffer(), 0, this.bout, (int)byteRangePosition, bf.size());
/*      */     } else {
/*      */ 
/*      */       
/* 1375 */       try { this.raf = new RandomAccessFile(this.tempFile, "rw");
/* 1376 */         long len = this.raf.length();
/* 1377 */         this.range[this.range.length - 1] = len - this.range[this.range.length - 2];
/* 1378 */         ByteBuffer bf = new ByteBuffer();
/* 1379 */         bf.append('[');
/* 1380 */         for (int i = 0; i < this.range.length; i++)
/* 1381 */           bf.append(this.range[i]).append(' '); 
/* 1382 */         bf.append(']');
/* 1383 */         this.raf.seek(byteRangePosition);
/* 1384 */         this.raf.write(bf.getBuffer(), 0, bf.size()); }
/*      */       
/* 1386 */       catch (IOException e) { 
/* 1387 */         try { this.raf.close(); } catch (Exception exception) {} 
/* 1388 */         try { this.tempFile.delete(); } catch (Exception exception) {}
/* 1389 */         throw e; }
/*      */     
/*      */     }  }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void addDocMDP(PdfDictionary crypto) {
/* 1401 */     PdfDictionary reference = new PdfDictionary();
/* 1402 */     PdfDictionary transformParams = new PdfDictionary();
/* 1403 */     transformParams.put(PdfName.P, new PdfNumber(this.certificationLevel));
/* 1404 */     transformParams.put(PdfName.V, new PdfName("1.2"));
/* 1405 */     transformParams.put(PdfName.TYPE, PdfName.TRANSFORMPARAMS);
/* 1406 */     reference.put(PdfName.TRANSFORMMETHOD, PdfName.DOCMDP);
/* 1407 */     reference.put(PdfName.TYPE, PdfName.SIGREF);
/* 1408 */     reference.put(PdfName.TRANSFORMPARAMS, transformParams);
/* 1409 */     if (this.writer.getPdfVersion().getVersion() < '6') {
/* 1410 */       reference.put(new PdfName("DigestValue"), new PdfString("aa"));
/* 1411 */       PdfArray loc = new PdfArray();
/* 1412 */       loc.add(new PdfNumber(0));
/* 1413 */       loc.add(new PdfNumber(0));
/* 1414 */       reference.put(new PdfName("DigestLocation"), loc);
/* 1415 */       reference.put(new PdfName("DigestMethod"), new PdfName("MD5"));
/*      */     } 
/* 1417 */     reference.put(PdfName.DATA, this.writer.reader.getTrailer().get(PdfName.ROOT));
/* 1418 */     PdfArray types = new PdfArray();
/* 1419 */     types.add(reference);
/* 1420 */     crypto.put(PdfName.REFERENCE, types);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void addFieldMDP(PdfDictionary crypto, PdfDictionary fieldLock) {
/* 1430 */     PdfDictionary reference = new PdfDictionary();
/* 1431 */     PdfDictionary transformParams = new PdfDictionary();
/* 1432 */     transformParams.putAll(fieldLock);
/* 1433 */     transformParams.put(PdfName.TYPE, PdfName.TRANSFORMPARAMS);
/* 1434 */     transformParams.put(PdfName.V, new PdfName("1.2"));
/* 1435 */     reference.put(PdfName.TRANSFORMMETHOD, PdfName.FIELDMDP);
/* 1436 */     reference.put(PdfName.TYPE, PdfName.SIGREF);
/* 1437 */     reference.put(PdfName.TRANSFORMPARAMS, transformParams);
/* 1438 */     reference.put(new PdfName("DigestValue"), new PdfString("aa"));
/* 1439 */     PdfArray loc = new PdfArray();
/* 1440 */     loc.add(new PdfNumber(0));
/* 1441 */     loc.add(new PdfNumber(0));
/* 1442 */     reference.put(new PdfName("DigestLocation"), loc);
/* 1443 */     reference.put(new PdfName("DigestMethod"), new PdfName("MD5"));
/* 1444 */     reference.put(PdfName.DATA, this.writer.reader.getTrailer().get(PdfName.ROOT));
/* 1445 */     PdfArray types = crypto.getAsArray(PdfName.REFERENCE);
/* 1446 */     if (types == null)
/* 1447 */       types = new PdfArray(); 
/* 1448 */     types.add(reference);
/* 1449 */     crypto.put(PdfName.REFERENCE, types);
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
/*      */   public void close(PdfDictionary update) throws IOException, DocumentException {
/*      */     try {
/* 1465 */       if (!this.preClosed)
/* 1466 */         throw new DocumentException(MessageLocalization.getComposedMessage("preclose.must.be.called.first", new Object[0])); 
/* 1467 */       ByteBuffer bf = new ByteBuffer();
/* 1468 */       for (PdfName key : update.getKeys()) {
/* 1469 */         PdfObject obj = update.get(key);
/* 1470 */         PdfLiteral lit = this.exclusionLocations.get(key);
/* 1471 */         if (lit == null)
/* 1472 */           throw new IllegalArgumentException(MessageLocalization.getComposedMessage("the.key.1.didn.t.reserve.space.in.preclose", new Object[] { key.toString() })); 
/* 1473 */         bf.reset();
/* 1474 */         obj.toPdf(null, bf);
/* 1475 */         if (bf.size() > lit.getPosLength())
/* 1476 */           throw new IllegalArgumentException(MessageLocalization.getComposedMessage("the.key.1.is.too.big.is.2.reserved.3", new Object[] { key.toString(), String.valueOf(bf.size()), String.valueOf(lit.getPosLength()) })); 
/* 1477 */         if (this.tempFile == null) {
/* 1478 */           System.arraycopy(bf.getBuffer(), 0, this.bout, (int)lit.getPosition(), bf.size()); continue;
/*      */         } 
/* 1480 */         this.raf.seek(lit.getPosition());
/* 1481 */         this.raf.write(bf.getBuffer(), 0, bf.size());
/*      */       } 
/*      */       
/* 1484 */       if (update.size() != this.exclusionLocations.size())
/* 1485 */         throw new IllegalArgumentException(MessageLocalization.getComposedMessage("the.update.dictionary.has.less.keys.than.required", new Object[0])); 
/* 1486 */       if (this.tempFile == null) {
/* 1487 */         this.originalout.write(this.bout, 0, this.boutLen);
/*      */       
/*      */       }
/* 1490 */       else if (this.originalout != null) {
/* 1491 */         this.raf.seek(0L);
/* 1492 */         long length = this.raf.length();
/* 1493 */         byte[] buf = new byte[8192];
/* 1494 */         while (length > 0L) {
/* 1495 */           int r = this.raf.read(buf, 0, (int)Math.min(buf.length, length));
/* 1496 */           if (r < 0)
/* 1497 */             throw new EOFException(MessageLocalization.getComposedMessage("unexpected.eof", new Object[0])); 
/* 1498 */           this.originalout.write(buf, 0, r);
/* 1499 */           length -= r;
/*      */         }
/*      */       
/*      */       } 
/*      */     } finally {
/*      */       
/* 1505 */       this.writer.reader.close();
/* 1506 */       if (this.tempFile != null) { 
/* 1507 */         try { this.raf.close(); } catch (Exception exception) {}
/* 1508 */         if (this.originalout != null)
/* 1509 */           try { this.tempFile.delete(); } catch (Exception exception) {}  }
/*      */       
/* 1511 */       if (this.originalout != null)
/* 1512 */         try { this.originalout.close(); } catch (Exception exception) {} 
/*      */     } 
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/PdfSignatureAppearance.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */