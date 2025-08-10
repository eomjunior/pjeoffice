/*     */ package com.itextpdf.text;
/*     */ 
/*     */ import com.itextpdf.text.error_messages.MessageLocalization;
/*     */ import com.itextpdf.text.pdf.PdfName;
/*     */ import com.itextpdf.text.pdf.PdfObject;
/*     */ import com.itextpdf.text.pdf.interfaces.IAccessibleElement;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Document
/*     */   implements DocListener, IAccessibleElement
/*     */ {
/*     */   public static boolean compress = true;
/*     */   public static boolean plainRandomAccess = false;
/* 113 */   public static float wmfFontCorrection = 0.86F;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 119 */   protected ArrayList<DocListener> listeners = new ArrayList<DocListener>();
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean open;
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean close;
/*     */ 
/*     */   
/*     */   protected Rectangle pageSize;
/*     */ 
/*     */   
/* 133 */   protected float marginLeft = 0.0F;
/*     */ 
/*     */   
/* 136 */   protected float marginRight = 0.0F;
/*     */ 
/*     */   
/* 139 */   protected float marginTop = 0.0F;
/*     */ 
/*     */   
/* 142 */   protected float marginBottom = 0.0F;
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean marginMirroring = false;
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean marginMirroringTopBottom = false;
/*     */ 
/*     */ 
/*     */   
/* 154 */   protected String javaScript_onLoad = null;
/*     */ 
/*     */   
/* 157 */   protected String javaScript_onUnLoad = null;
/*     */ 
/*     */   
/* 160 */   protected String htmlStyleClass = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 165 */   protected int pageN = 0;
/*     */ 
/*     */   
/* 168 */   protected int chapternumber = 0;
/*     */   
/* 170 */   protected PdfName role = PdfName.DOCUMENT;
/* 171 */   protected HashMap<PdfName, PdfObject> accessibleAttributes = null;
/* 172 */   protected AccessibleElementId id = new AccessibleElementId();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Document() {
/* 181 */     this(PageSize.A4);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Document(Rectangle pageSize) {
/* 192 */     this(pageSize, 36.0F, 36.0F, 36.0F, 36.0F);
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
/*     */   public Document(Rectangle pageSize, float marginLeft, float marginRight, float marginTop, float marginBottom) {
/* 212 */     this.pageSize = pageSize;
/* 213 */     this.marginLeft = marginLeft;
/* 214 */     this.marginRight = marginRight;
/* 215 */     this.marginTop = marginTop;
/* 216 */     this.marginBottom = marginBottom;
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
/*     */   public void addDocListener(DocListener listener) {
/* 229 */     this.listeners.add(listener);
/* 230 */     if (listener instanceof IAccessibleElement) {
/* 231 */       IAccessibleElement ae = (IAccessibleElement)listener;
/* 232 */       ae.setRole(this.role);
/* 233 */       ae.setId(this.id);
/* 234 */       if (this.accessibleAttributes != null) {
/* 235 */         for (PdfName key : this.accessibleAttributes.keySet()) {
/* 236 */           ae.setAccessibleAttribute(key, this.accessibleAttributes.get(key));
/*     */         }
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
/*     */   public void removeDocListener(DocListener listener) {
/* 249 */     this.listeners.remove(listener);
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
/*     */   public boolean add(Element element) throws DocumentException {
/* 266 */     if (this.close) {
/* 267 */       throw new DocumentException(MessageLocalization.getComposedMessage("the.document.has.been.closed.you.can.t.add.any.elements", new Object[0]));
/*     */     }
/* 269 */     if (!this.open && element.isContent()) {
/* 270 */       throw new DocumentException(MessageLocalization.getComposedMessage("the.document.is.not.open.yet.you.can.only.add.meta.information", new Object[0]));
/*     */     }
/* 272 */     boolean success = false;
/* 273 */     if (element instanceof ChapterAutoNumber) {
/* 274 */       this.chapternumber = ((ChapterAutoNumber)element).setAutomaticNumber(this.chapternumber);
/*     */     }
/* 276 */     for (DocListener listener : this.listeners) {
/* 277 */       success |= listener.add(element);
/*     */     }
/* 279 */     if (element instanceof LargeElement) {
/* 280 */       LargeElement e = (LargeElement)element;
/* 281 */       if (!e.isComplete())
/* 282 */         e.flushContent(); 
/*     */     } 
/* 284 */     return success;
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
/*     */   public void open() {
/* 296 */     if (!this.close) {
/* 297 */       this.open = true;
/*     */     }
/* 299 */     for (DocListener listener : this.listeners) {
/* 300 */       listener.setPageSize(this.pageSize);
/* 301 */       listener.setMargins(this.marginLeft, this.marginRight, this.marginTop, this.marginBottom);
/*     */       
/* 303 */       listener.open();
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
/*     */   public boolean setPageSize(Rectangle pageSize) {
/* 316 */     this.pageSize = pageSize;
/* 317 */     for (DocListener listener : this.listeners) {
/* 318 */       listener.setPageSize(pageSize);
/*     */     }
/* 320 */     return true;
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
/*     */   public boolean setMargins(float marginLeft, float marginRight, float marginTop, float marginBottom) {
/* 339 */     this.marginLeft = marginLeft;
/* 340 */     this.marginRight = marginRight;
/* 341 */     this.marginTop = marginTop;
/* 342 */     this.marginBottom = marginBottom;
/* 343 */     for (DocListener listener : this.listeners) {
/* 344 */       listener.setMargins(marginLeft, marginRight, marginTop, marginBottom);
/*     */     }
/*     */     
/* 347 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean newPage() {
/* 358 */     if (!this.open || this.close) {
/* 359 */       return false;
/*     */     }
/* 361 */     for (DocListener listener : this.listeners) {
/* 362 */       listener.newPage();
/*     */     }
/* 364 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void resetPageCount() {
/* 372 */     this.pageN = 0;
/* 373 */     for (DocListener listener : this.listeners) {
/* 374 */       listener.resetPageCount();
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
/*     */   public void setPageCount(int pageN) {
/* 386 */     this.pageN = pageN;
/* 387 */     for (DocListener listener : this.listeners) {
/* 388 */       listener.setPageCount(pageN);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getPageNumber() {
/* 399 */     return this.pageN;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() {
/* 410 */     if (!this.close) {
/* 411 */       this.open = false;
/* 412 */       this.close = true;
/*     */     } 
/* 414 */     for (DocListener listener : this.listeners) {
/* 415 */       listener.close();
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
/*     */   public boolean addHeader(String name, String content) {
/*     */     try {
/* 433 */       return add(new Header(name, content));
/* 434 */     } catch (DocumentException de) {
/* 435 */       throw new ExceptionConverter(de);
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
/*     */   public boolean addTitle(String title) {
/*     */     try {
/* 449 */       return add(new Meta(1, title));
/* 450 */     } catch (DocumentException de) {
/* 451 */       throw new ExceptionConverter(de);
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
/*     */   public boolean addSubject(String subject) {
/*     */     try {
/* 465 */       return add(new Meta(2, subject));
/* 466 */     } catch (DocumentException de) {
/* 467 */       throw new ExceptionConverter(de);
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
/*     */   public boolean addKeywords(String keywords) {
/*     */     try {
/* 481 */       return add(new Meta(3, keywords));
/* 482 */     } catch (DocumentException de) {
/* 483 */       throw new ExceptionConverter(de);
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
/*     */   public boolean addAuthor(String author) {
/*     */     try {
/* 497 */       return add(new Meta(4, author));
/* 498 */     } catch (DocumentException de) {
/* 499 */       throw new ExceptionConverter(de);
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
/*     */   public boolean addCreator(String creator) {
/*     */     try {
/* 513 */       return add(new Meta(7, creator));
/* 514 */     } catch (DocumentException de) {
/* 515 */       throw new ExceptionConverter(de);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean addProducer() {
/*     */     try {
/* 527 */       return add(new Meta(5, Version.getInstance().getVersion()));
/* 528 */     } catch (DocumentException de) {
/* 529 */       throw new ExceptionConverter(de);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean addLanguage(String language) {
/*     */     try {
/* 540 */       return add(new Meta(8, language));
/* 541 */     } catch (DocumentException de) {
/* 542 */       throw new ExceptionConverter(de);
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
/*     */   public boolean addCreationDate() {
/*     */     try {
/* 555 */       SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
/*     */       
/* 557 */       return add(new Meta(6, sdf.format(new Date())));
/* 558 */     } catch (DocumentException de) {
/* 559 */       throw new ExceptionConverter(de);
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
/*     */   public float leftMargin() {
/* 572 */     return this.marginLeft;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float rightMargin() {
/* 582 */     return this.marginRight;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float topMargin() {
/* 592 */     return this.marginTop;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float bottomMargin() {
/* 602 */     return this.marginBottom;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float left() {
/* 612 */     return this.pageSize.getLeft(this.marginLeft);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float right() {
/* 622 */     return this.pageSize.getRight(this.marginRight);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float top() {
/* 632 */     return this.pageSize.getTop(this.marginTop);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float bottom() {
/* 642 */     return this.pageSize.getBottom(this.marginBottom);
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
/*     */   public float left(float margin) {
/* 654 */     return this.pageSize.getLeft(this.marginLeft + margin);
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
/*     */   public float right(float margin) {
/* 666 */     return this.pageSize.getRight(this.marginRight + margin);
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
/*     */   public float top(float margin) {
/* 678 */     return this.pageSize.getTop(this.marginTop + margin);
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
/*     */   public float bottom(float margin) {
/* 690 */     return this.pageSize.getBottom(this.marginBottom + margin);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Rectangle getPageSize() {
/* 700 */     return this.pageSize;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isOpen() {
/* 709 */     return this.open;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setJavaScript_onLoad(String code) {
/* 720 */     this.javaScript_onLoad = code;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getJavaScript_onLoad() {
/* 730 */     return this.javaScript_onLoad;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setJavaScript_onUnLoad(String code) {
/* 741 */     this.javaScript_onUnLoad = code;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getJavaScript_onUnLoad() {
/* 751 */     return this.javaScript_onUnLoad;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setHtmlStyleClass(String htmlStyleClass) {
/* 762 */     this.htmlStyleClass = htmlStyleClass;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getHtmlStyleClass() {
/* 772 */     return this.htmlStyleClass;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean setMarginMirroring(boolean marginMirroring) {
/* 783 */     this.marginMirroring = marginMirroring;
/*     */     
/* 785 */     for (DocListener element : this.listeners) {
/* 786 */       DocListener listener = element;
/* 787 */       listener.setMarginMirroring(marginMirroring);
/*     */     } 
/* 789 */     return true;
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
/*     */   public boolean setMarginMirroringTopBottom(boolean marginMirroringTopBottom) {
/* 801 */     this.marginMirroringTopBottom = marginMirroringTopBottom;
/*     */     
/* 803 */     for (DocListener element : this.listeners) {
/* 804 */       DocListener listener = element;
/* 805 */       listener.setMarginMirroringTopBottom(marginMirroringTopBottom);
/*     */     } 
/* 807 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isMarginMirroring() {
/* 816 */     return this.marginMirroring;
/*     */   }
/*     */   
/*     */   public PdfObject getAccessibleAttribute(PdfName key) {
/* 820 */     if (this.accessibleAttributes != null) {
/* 821 */       return this.accessibleAttributes.get(key);
/*     */     }
/* 823 */     return null;
/*     */   }
/*     */   
/*     */   public void setAccessibleAttribute(PdfName key, PdfObject value) {
/* 827 */     if (this.accessibleAttributes == null)
/* 828 */       this.accessibleAttributes = new HashMap<PdfName, PdfObject>(); 
/* 829 */     this.accessibleAttributes.put(key, value);
/*     */   }
/*     */   
/*     */   public HashMap<PdfName, PdfObject> getAccessibleAttributes() {
/* 833 */     return this.accessibleAttributes;
/*     */   }
/*     */   
/*     */   public PdfName getRole() {
/* 837 */     return this.role;
/*     */   }
/*     */   
/*     */   public void setRole(PdfName role) {
/* 841 */     this.role = role;
/*     */   }
/*     */   
/*     */   public AccessibleElementId getId() {
/* 845 */     return this.id;
/*     */   }
/*     */   
/*     */   public void setId(AccessibleElementId id) {
/* 849 */     this.id = id;
/*     */   }
/*     */   
/*     */   public boolean isInline() {
/* 853 */     return false;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/Document.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */