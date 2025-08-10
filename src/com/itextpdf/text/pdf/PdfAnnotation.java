/*      */ package com.itextpdf.text.pdf;
/*      */ 
/*      */ import com.itextpdf.awt.geom.AffineTransform;
/*      */ import com.itextpdf.text.AccessibleElementId;
/*      */ import com.itextpdf.text.BaseColor;
/*      */ import com.itextpdf.text.Rectangle;
/*      */ import com.itextpdf.text.error_messages.MessageLocalization;
/*      */ import com.itextpdf.text.pdf.interfaces.IAccessibleElement;
/*      */ import java.io.IOException;
/*      */ import java.io.OutputStream;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class PdfAnnotation
/*      */   extends PdfDictionary
/*      */   implements IAccessibleElement
/*      */ {
/*   68 */   public static final PdfName HIGHLIGHT_NONE = PdfName.N;
/*      */   
/*   70 */   public static final PdfName HIGHLIGHT_INVERT = PdfName.I;
/*      */   
/*   72 */   public static final PdfName HIGHLIGHT_OUTLINE = PdfName.O;
/*      */   
/*   74 */   public static final PdfName HIGHLIGHT_PUSH = PdfName.P;
/*      */   
/*   76 */   public static final PdfName HIGHLIGHT_TOGGLE = PdfName.T;
/*      */   
/*      */   public static final int FLAGS_INVISIBLE = 1;
/*      */   
/*      */   public static final int FLAGS_HIDDEN = 2;
/*      */   
/*      */   public static final int FLAGS_PRINT = 4;
/*      */   
/*      */   public static final int FLAGS_NOZOOM = 8;
/*      */   
/*      */   public static final int FLAGS_NOROTATE = 16;
/*      */   
/*      */   public static final int FLAGS_NOVIEW = 32;
/*      */   
/*      */   public static final int FLAGS_READONLY = 64;
/*      */   
/*      */   public static final int FLAGS_LOCKED = 128;
/*      */   
/*      */   public static final int FLAGS_TOGGLENOVIEW = 256;
/*      */   
/*      */   public static final int FLAGS_LOCKEDCONTENTS = 512;
/*      */   
/*   98 */   public static final PdfName APPEARANCE_NORMAL = PdfName.N;
/*      */   
/*  100 */   public static final PdfName APPEARANCE_ROLLOVER = PdfName.R;
/*      */   
/*  102 */   public static final PdfName APPEARANCE_DOWN = PdfName.D;
/*      */   
/*  104 */   public static final PdfName AA_ENTER = PdfName.E;
/*      */   
/*  106 */   public static final PdfName AA_EXIT = PdfName.X;
/*      */   
/*  108 */   public static final PdfName AA_DOWN = PdfName.D;
/*      */   
/*  110 */   public static final PdfName AA_UP = PdfName.U;
/*      */   
/*  112 */   public static final PdfName AA_FOCUS = PdfName.FO;
/*      */   
/*  114 */   public static final PdfName AA_BLUR = PdfName.BL;
/*      */   
/*  116 */   public static final PdfName AA_JS_KEY = PdfName.K;
/*      */   
/*  118 */   public static final PdfName AA_JS_FORMAT = PdfName.F;
/*      */   
/*  120 */   public static final PdfName AA_JS_CHANGE = PdfName.V;
/*      */   
/*  122 */   public static final PdfName AA_JS_OTHER_CHANGE = PdfName.C;
/*      */ 
/*      */   
/*      */   public static final int MARKUP_HIGHLIGHT = 0;
/*      */ 
/*      */   
/*      */   public static final int MARKUP_UNDERLINE = 1;
/*      */ 
/*      */   
/*      */   public static final int MARKUP_STRIKEOUT = 2;
/*      */ 
/*      */   
/*      */   public static final int MARKUP_SQUIGGLY = 3;
/*      */ 
/*      */   
/*      */   protected PdfWriter writer;
/*      */   
/*      */   protected PdfIndirectReference reference;
/*      */   
/*      */   protected HashSet<PdfTemplate> templates;
/*      */   
/*      */   protected boolean form = false;
/*      */   
/*      */   protected boolean annotation = true;
/*      */   
/*      */   protected boolean used = false;
/*      */   
/*  149 */   private int placeInPage = -1;
/*      */   
/*  151 */   protected PdfName role = null;
/*  152 */   protected HashMap<PdfName, PdfObject> accessibleAttributes = null;
/*  153 */   private AccessibleElementId id = null;
/*      */ 
/*      */   
/*      */   public PdfAnnotation(PdfWriter writer, Rectangle rect) {
/*  157 */     this.writer = writer;
/*  158 */     if (rect != null) {
/*  159 */       put(PdfName.RECT, new PdfRectangle(rect));
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
/*      */   public PdfAnnotation(PdfWriter writer, float llx, float lly, float urx, float ury, PdfString title, PdfString content) {
/*  174 */     this.writer = writer;
/*  175 */     put(PdfName.SUBTYPE, PdfName.TEXT);
/*  176 */     put(PdfName.T, title);
/*  177 */     put(PdfName.RECT, new PdfRectangle(llx, lly, urx, ury));
/*  178 */     put(PdfName.CONTENTS, content);
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
/*      */   public PdfAnnotation(PdfWriter writer, float llx, float lly, float urx, float ury, PdfAction action) {
/*  192 */     this.writer = writer;
/*  193 */     put(PdfName.SUBTYPE, PdfName.LINK);
/*  194 */     put(PdfName.RECT, new PdfRectangle(llx, lly, urx, ury));
/*  195 */     put(PdfName.A, action);
/*  196 */     put(PdfName.BORDER, new PdfBorderArray(0.0F, 0.0F, 0.0F));
/*  197 */     put(PdfName.C, new PdfColor(0, 0, 255));
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
/*      */   public static PdfAnnotation createScreen(PdfWriter writer, Rectangle rect, String clipTitle, PdfFileSpecification fs, String mimeType, boolean playOnDisplay) throws IOException {
/*  213 */     PdfAnnotation ann = writer.createAnnotation(rect, PdfName.SCREEN);
/*  214 */     ann.put(PdfName.F, new PdfNumber(4));
/*  215 */     ann.put(PdfName.TYPE, PdfName.ANNOT);
/*  216 */     ann.setPage();
/*  217 */     PdfIndirectReference ref = ann.getIndirectReference();
/*  218 */     PdfAction action = PdfAction.rendition(clipTitle, fs, mimeType, ref);
/*  219 */     PdfIndirectReference actionRef = writer.addToBody(action).getIndirectReference();
/*      */     
/*  221 */     if (playOnDisplay) {
/*      */       
/*  223 */       PdfDictionary aa = new PdfDictionary();
/*  224 */       aa.put(new PdfName("PV"), actionRef);
/*  225 */       ann.put(PdfName.AA, aa);
/*      */     } 
/*  227 */     ann.put(PdfName.A, actionRef);
/*  228 */     return ann;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PdfIndirectReference getIndirectReference() {
/*  236 */     if (this.reference == null) {
/*  237 */       this.reference = this.writer.getPdfIndirectReference();
/*      */     }
/*  239 */     return this.reference;
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
/*      */   public static PdfAnnotation createText(PdfWriter writer, Rectangle rect, String title, String contents, boolean open, String icon) {
/*  252 */     PdfAnnotation annot = writer.createAnnotation(rect, PdfName.TEXT);
/*  253 */     if (title != null)
/*  254 */       annot.put(PdfName.T, new PdfString(title, "UnicodeBig")); 
/*  255 */     if (contents != null)
/*  256 */       annot.put(PdfName.CONTENTS, new PdfString(contents, "UnicodeBig")); 
/*  257 */     if (open)
/*  258 */       annot.put(PdfName.OPEN, PdfBoolean.PDFTRUE); 
/*  259 */     if (icon != null) {
/*  260 */       annot.put(PdfName.NAME, new PdfName(icon));
/*      */     }
/*  262 */     return annot;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected static PdfAnnotation createLink(PdfWriter writer, Rectangle rect, PdfName highlight) {
/*  273 */     PdfAnnotation annot = writer.createAnnotation(rect, PdfName.LINK);
/*  274 */     if (!highlight.equals(HIGHLIGHT_INVERT))
/*  275 */       annot.put(PdfName.H, highlight); 
/*  276 */     return annot;
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
/*      */   public static PdfAnnotation createLink(PdfWriter writer, Rectangle rect, PdfName highlight, PdfAction action) {
/*  288 */     PdfAnnotation annot = createLink(writer, rect, highlight);
/*  289 */     annot.putEx(PdfName.A, action);
/*  290 */     return annot;
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
/*      */   public static PdfAnnotation createLink(PdfWriter writer, Rectangle rect, PdfName highlight, String namedDestination) {
/*  302 */     PdfAnnotation annot = createLink(writer, rect, highlight);
/*  303 */     annot.put(PdfName.DEST, new PdfString(namedDestination, "UnicodeBig"));
/*  304 */     return annot;
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
/*      */   public static PdfAnnotation createLink(PdfWriter writer, Rectangle rect, PdfName highlight, int page, PdfDestination dest) {
/*  317 */     PdfAnnotation annot = createLink(writer, rect, highlight);
/*  318 */     PdfIndirectReference ref = writer.getPageReference(page);
/*  319 */     PdfDestination d = new PdfDestination(dest);
/*  320 */     d.addPage(ref);
/*  321 */     annot.put(PdfName.DEST, d);
/*  322 */     return annot;
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
/*      */   public static PdfAnnotation createFreeText(PdfWriter writer, Rectangle rect, String contents, PdfContentByte defaultAppearance) {
/*  334 */     PdfAnnotation annot = writer.createAnnotation(rect, PdfName.FREETEXT);
/*  335 */     annot.put(PdfName.CONTENTS, new PdfString(contents, "UnicodeBig"));
/*  336 */     annot.setDefaultAppearanceString(defaultAppearance);
/*  337 */     return annot;
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
/*      */   public static PdfAnnotation createLine(PdfWriter writer, Rectangle rect, String contents, float x1, float y1, float x2, float y2) {
/*  352 */     PdfAnnotation annot = writer.createAnnotation(rect, PdfName.LINE);
/*  353 */     annot.put(PdfName.CONTENTS, new PdfString(contents, "UnicodeBig"));
/*  354 */     PdfArray array = new PdfArray(new PdfNumber(x1));
/*  355 */     array.add(new PdfNumber(y1));
/*  356 */     array.add(new PdfNumber(x2));
/*  357 */     array.add(new PdfNumber(y2));
/*  358 */     annot.put(PdfName.L, array);
/*  359 */     return annot;
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
/*      */   public static PdfAnnotation createSquareCircle(PdfWriter writer, Rectangle rect, String contents, boolean square) {
/*      */     PdfAnnotation annot;
/*  372 */     if (square) {
/*  373 */       annot = writer.createAnnotation(rect, PdfName.SQUARE);
/*      */     } else {
/*  375 */       annot = writer.createAnnotation(rect, PdfName.CIRCLE);
/*  376 */     }  annot.put(PdfName.CONTENTS, new PdfString(contents, "UnicodeBig"));
/*  377 */     return annot;
/*      */   }
/*      */   
/*      */   public static PdfAnnotation createMarkup(PdfWriter writer, Rectangle rect, String contents, int type, float[] quadPoints) {
/*  381 */     PdfName name = PdfName.HIGHLIGHT;
/*  382 */     switch (type) {
/*      */       case 1:
/*  384 */         name = PdfName.UNDERLINE;
/*      */         break;
/*      */       case 2:
/*  387 */         name = PdfName.STRIKEOUT;
/*      */         break;
/*      */       case 3:
/*  390 */         name = PdfName.SQUIGGLY;
/*      */         break;
/*      */     } 
/*  393 */     PdfAnnotation annot = writer.createAnnotation(rect, name);
/*  394 */     annot.put(PdfName.CONTENTS, new PdfString(contents, "UnicodeBig"));
/*  395 */     PdfArray array = new PdfArray();
/*  396 */     for (int k = 0; k < quadPoints.length; k++)
/*  397 */       array.add(new PdfNumber(quadPoints[k])); 
/*  398 */     annot.put(PdfName.QUADPOINTS, array);
/*  399 */     return annot;
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
/*      */   public static PdfAnnotation createStamp(PdfWriter writer, Rectangle rect, String contents, String name) {
/*  411 */     PdfAnnotation annot = writer.createAnnotation(rect, PdfName.STAMP);
/*  412 */     annot.put(PdfName.CONTENTS, new PdfString(contents, "UnicodeBig"));
/*  413 */     annot.put(PdfName.NAME, new PdfName(name));
/*  414 */     return annot;
/*      */   }
/*      */   
/*      */   public static PdfAnnotation createInk(PdfWriter writer, Rectangle rect, String contents, float[][] inkList) {
/*  418 */     PdfAnnotation annot = writer.createAnnotation(rect, PdfName.INK);
/*  419 */     annot.put(PdfName.CONTENTS, new PdfString(contents, "UnicodeBig"));
/*  420 */     PdfArray outer = new PdfArray();
/*  421 */     for (int k = 0; k < inkList.length; k++) {
/*  422 */       PdfArray inner = new PdfArray();
/*  423 */       float[] deep = inkList[k];
/*  424 */       for (int j = 0; j < deep.length; j++)
/*  425 */         inner.add(new PdfNumber(deep[j])); 
/*  426 */       outer.add(inner);
/*      */     } 
/*  428 */     annot.put(PdfName.INKLIST, outer);
/*  429 */     return annot;
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
/*      */   public static PdfAnnotation createFileAttachment(PdfWriter writer, Rectangle rect, String contents, byte[] fileStore, String file, String fileDisplay) throws IOException {
/*  445 */     return createFileAttachment(writer, rect, contents, PdfFileSpecification.fileEmbedded(writer, file, fileDisplay, fileStore));
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
/*      */   public static PdfAnnotation createFileAttachment(PdfWriter writer, Rectangle rect, String contents, PdfFileSpecification fs) throws IOException {
/*  457 */     PdfAnnotation annot = writer.createAnnotation(rect, PdfName.FILEATTACHMENT);
/*  458 */     if (contents != null)
/*  459 */       annot.put(PdfName.CONTENTS, new PdfString(contents, "UnicodeBig")); 
/*  460 */     annot.put(PdfName.FS, fs.getReference());
/*  461 */     return annot;
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
/*      */   public static PdfAnnotation createPopup(PdfWriter writer, Rectangle rect, String contents, boolean open) {
/*  473 */     PdfAnnotation annot = writer.createAnnotation(rect, PdfName.POPUP);
/*  474 */     if (contents != null)
/*  475 */       annot.put(PdfName.CONTENTS, new PdfString(contents, "UnicodeBig")); 
/*  476 */     if (open)
/*  477 */       annot.put(PdfName.OPEN, PdfBoolean.PDFTRUE); 
/*  478 */     return annot;
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
/*      */   public static PdfAnnotation createPolygonPolyline(PdfWriter writer, Rectangle rect, String contents, boolean polygon, PdfArray vertices) {
/*  492 */     PdfAnnotation annot = null;
/*  493 */     if (polygon) {
/*  494 */       annot = writer.createAnnotation(rect, PdfName.POLYGON);
/*      */     } else {
/*  496 */       annot = writer.createAnnotation(rect, PdfName.POLYLINE);
/*  497 */     }  annot.put(PdfName.CONTENTS, new PdfString(contents, "UnicodeBig"));
/*  498 */     annot.put(PdfName.VERTICES, new PdfArray(vertices));
/*  499 */     return annot;
/*      */   }
/*      */   
/*      */   public void setDefaultAppearanceString(PdfContentByte cb) {
/*  503 */     byte[] b = cb.getInternalBuffer().toByteArray();
/*  504 */     int len = b.length;
/*  505 */     for (int k = 0; k < len; k++) {
/*  506 */       if (b[k] == 10)
/*  507 */         b[k] = 32; 
/*      */     } 
/*  509 */     put(PdfName.DA, new PdfString(b));
/*      */   }
/*      */   
/*      */   public void setFlags(int flags) {
/*  513 */     if (flags == 0) {
/*  514 */       remove(PdfName.F);
/*      */     } else {
/*  516 */       put(PdfName.F, new PdfNumber(flags));
/*      */     } 
/*      */   }
/*      */   public void setBorder(PdfBorderArray border) {
/*  520 */     put(PdfName.BORDER, border);
/*      */   }
/*      */   
/*      */   public void setBorderStyle(PdfBorderDictionary border) {
/*  524 */     put(PdfName.BS, border);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setHighlighting(PdfName highlight) {
/*  534 */     if (highlight.equals(HIGHLIGHT_INVERT)) {
/*  535 */       remove(PdfName.H);
/*      */     } else {
/*  537 */       put(PdfName.H, highlight);
/*      */     } 
/*      */   }
/*      */   public void setAppearance(PdfName ap, PdfTemplate template) {
/*  541 */     PdfDictionary dic = (PdfDictionary)get(PdfName.AP);
/*  542 */     if (dic == null)
/*  543 */       dic = new PdfDictionary(); 
/*  544 */     dic.put(ap, template.getIndirectReference());
/*  545 */     put(PdfName.AP, dic);
/*  546 */     if (!this.form)
/*      */       return; 
/*  548 */     if (this.templates == null)
/*  549 */       this.templates = new HashSet<PdfTemplate>(); 
/*  550 */     this.templates.add(template);
/*      */   }
/*      */   
/*      */   public void setAppearance(PdfName ap, String state, PdfTemplate template) {
/*  554 */     PdfDictionary dic, dicAp = (PdfDictionary)get(PdfName.AP);
/*  555 */     if (dicAp == null) {
/*  556 */       dicAp = new PdfDictionary();
/*      */     }
/*      */     
/*  559 */     PdfObject obj = dicAp.get(ap);
/*  560 */     if (obj != null && obj.isDictionary()) {
/*  561 */       dic = (PdfDictionary)obj;
/*      */     } else {
/*  563 */       dic = new PdfDictionary();
/*  564 */     }  dic.put(new PdfName(state), template.getIndirectReference());
/*  565 */     dicAp.put(ap, dic);
/*  566 */     put(PdfName.AP, dicAp);
/*  567 */     if (!this.form)
/*      */       return; 
/*  569 */     if (this.templates == null)
/*  570 */       this.templates = new HashSet<PdfTemplate>(); 
/*  571 */     this.templates.add(template);
/*      */   }
/*      */   
/*      */   public void setAppearanceState(String state) {
/*  575 */     if (state == null) {
/*  576 */       remove(PdfName.AS);
/*      */       return;
/*      */     } 
/*  579 */     put(PdfName.AS, new PdfName(state));
/*      */   }
/*      */   
/*      */   public void setColor(BaseColor color) {
/*  583 */     put(PdfName.C, new PdfColor(color));
/*      */   }
/*      */   
/*      */   public void setTitle(String title) {
/*  587 */     if (title == null) {
/*  588 */       remove(PdfName.T);
/*      */       return;
/*      */     } 
/*  591 */     put(PdfName.T, new PdfString(title, "UnicodeBig"));
/*      */   }
/*      */   
/*      */   public void setPopup(PdfAnnotation popup) {
/*  595 */     put(PdfName.POPUP, popup.getIndirectReference());
/*  596 */     popup.put(PdfName.PARENT, getIndirectReference());
/*      */   }
/*      */   
/*      */   public void setAction(PdfAction action) {
/*  600 */     put(PdfName.A, action);
/*      */   }
/*      */   
/*      */   public void setAdditionalActions(PdfName key, PdfAction action) {
/*      */     PdfDictionary dic;
/*  605 */     PdfObject obj = get(PdfName.AA);
/*  606 */     if (obj != null && obj.isDictionary()) {
/*  607 */       dic = (PdfDictionary)obj;
/*      */     } else {
/*  609 */       dic = new PdfDictionary();
/*  610 */     }  dic.put(key, action);
/*  611 */     put(PdfName.AA, dic);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isUsed() {
/*  618 */     return this.used;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setUsed() {
/*  624 */     this.used = true;
/*      */   }
/*      */   
/*      */   public HashSet<PdfTemplate> getTemplates() {
/*  628 */     return this.templates;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isForm() {
/*  635 */     return this.form;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isAnnotation() {
/*  642 */     return this.annotation;
/*      */   }
/*      */   
/*      */   public void setPage(int page) {
/*  646 */     put(PdfName.P, this.writer.getPageReference(page));
/*      */   }
/*      */   
/*      */   public void setPage() {
/*  650 */     put(PdfName.P, this.writer.getCurrentPage());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getPlaceInPage() {
/*  657 */     return this.placeInPage;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPlaceInPage(int placeInPage) {
/*  666 */     this.placeInPage = placeInPage;
/*      */   }
/*      */   
/*      */   public void setRotate(int v) {
/*  670 */     put(PdfName.ROTATE, new PdfNumber(v));
/*      */   }
/*      */   
/*      */   PdfDictionary getMK() {
/*  674 */     PdfDictionary mk = (PdfDictionary)get(PdfName.MK);
/*  675 */     if (mk == null) {
/*  676 */       mk = new PdfDictionary();
/*  677 */       put(PdfName.MK, mk);
/*      */     } 
/*  679 */     return mk;
/*      */   }
/*      */   
/*      */   public void setMKRotation(int rotation) {
/*  683 */     getMK().put(PdfName.R, new PdfNumber(rotation));
/*      */   }
/*      */   public static PdfArray getMKColor(BaseColor color) {
/*      */     CMYKColor cmyk;
/*  687 */     PdfArray array = new PdfArray();
/*  688 */     int type = ExtendedColor.getType(color);
/*  689 */     switch (type)
/*      */     { case 1:
/*  691 */         array.add(new PdfNumber(((GrayColor)color).getGray()));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  711 */         return array;case 2: cmyk = (CMYKColor)color; array.add(new PdfNumber(cmyk.getCyan())); array.add(new PdfNumber(cmyk.getMagenta())); array.add(new PdfNumber(cmyk.getYellow())); array.add(new PdfNumber(cmyk.getBlack())); return array;case 3: case 4: case 5: throw new RuntimeException(MessageLocalization.getComposedMessage("separations.patterns.and.shadings.are.not.allowed.in.mk.dictionary", new Object[0])); }  array.add(new PdfNumber(color.getRed() / 255.0F)); array.add(new PdfNumber(color.getGreen() / 255.0F)); array.add(new PdfNumber(color.getBlue() / 255.0F)); return array;
/*      */   }
/*      */   
/*      */   public void setMKBorderColor(BaseColor color) {
/*  715 */     if (color == null) {
/*  716 */       getMK().remove(PdfName.BC);
/*      */     } else {
/*  718 */       getMK().put(PdfName.BC, getMKColor(color));
/*      */     } 
/*      */   }
/*      */   public void setMKBackgroundColor(BaseColor color) {
/*  722 */     if (color == null) {
/*  723 */       getMK().remove(PdfName.BG);
/*      */     } else {
/*  725 */       getMK().put(PdfName.BG, getMKColor(color));
/*      */     } 
/*      */   }
/*      */   public void setMKNormalCaption(String caption) {
/*  729 */     getMK().put(PdfName.CA, new PdfString(caption, "UnicodeBig"));
/*      */   }
/*      */   
/*      */   public void setMKRolloverCaption(String caption) {
/*  733 */     getMK().put(PdfName.RC, new PdfString(caption, "UnicodeBig"));
/*      */   }
/*      */   
/*      */   public void setMKAlternateCaption(String caption) {
/*  737 */     getMK().put(PdfName.AC, new PdfString(caption, "UnicodeBig"));
/*      */   }
/*      */   
/*      */   public void setMKNormalIcon(PdfTemplate template) {
/*  741 */     getMK().put(PdfName.I, template.getIndirectReference());
/*      */   }
/*      */   
/*      */   public void setMKRolloverIcon(PdfTemplate template) {
/*  745 */     getMK().put(PdfName.RI, template.getIndirectReference());
/*      */   }
/*      */   
/*      */   public void setMKAlternateIcon(PdfTemplate template) {
/*  749 */     getMK().put(PdfName.IX, template.getIndirectReference());
/*      */   }
/*      */   
/*      */   public void setMKIconFit(PdfName scale, PdfName scalingType, float leftoverLeft, float leftoverBottom, boolean fitInBounds) {
/*  753 */     PdfDictionary dic = new PdfDictionary();
/*  754 */     if (!scale.equals(PdfName.A))
/*  755 */       dic.put(PdfName.SW, scale); 
/*  756 */     if (!scalingType.equals(PdfName.P))
/*  757 */       dic.put(PdfName.S, scalingType); 
/*  758 */     if (leftoverLeft != 0.5F || leftoverBottom != 0.5F) {
/*  759 */       PdfArray array = new PdfArray(new PdfNumber(leftoverLeft));
/*  760 */       array.add(new PdfNumber(leftoverBottom));
/*  761 */       dic.put(PdfName.A, array);
/*      */     } 
/*  763 */     if (fitInBounds)
/*  764 */       dic.put(PdfName.FB, PdfBoolean.PDFTRUE); 
/*  765 */     getMK().put(PdfName.IF, dic);
/*      */   }
/*      */   
/*      */   public void setMKTextPosition(int tp) {
/*  769 */     getMK().put(PdfName.TP, new PdfNumber(tp));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setLayer(PdfOCG layer) {
/*  777 */     put(PdfName.OC, layer.getRef());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setName(String name) {
/*  786 */     put(PdfName.NM, new PdfString(name));
/*      */   }
/*      */ 
/*      */   
/*      */   public void applyCTM(AffineTransform ctm) {
/*  791 */     PdfArray origRect = getAsArray(PdfName.RECT);
/*  792 */     if (origRect != null) {
/*      */       PdfRectangle rect;
/*  794 */       if (origRect.size() == 4) {
/*  795 */         rect = new PdfRectangle(origRect.getAsNumber(0).floatValue(), origRect.getAsNumber(1).floatValue(), origRect.getAsNumber(2).floatValue(), origRect.getAsNumber(3).floatValue());
/*      */       } else {
/*      */         
/*  798 */         rect = new PdfRectangle(origRect.getAsNumber(0).floatValue(), origRect.getAsNumber(1).floatValue());
/*      */       } 
/*  800 */       put(PdfName.RECT, rect.transform(ctm));
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
/*      */   public static class PdfImportedLink
/*      */   {
/*      */     float llx;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     float lly;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     float urx;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     float ury;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  853 */     HashMap<PdfName, PdfObject> parameters = new HashMap<PdfName, PdfObject>();
/*  854 */     PdfArray destination = null;
/*  855 */     int newPage = 0;
/*      */     PdfArray rect;
/*      */     
/*      */     PdfImportedLink(PdfDictionary annotation) {
/*  859 */       this.parameters.putAll(annotation.hashMap);
/*      */       try {
/*  861 */         this.destination = (PdfArray)this.parameters.remove(PdfName.DEST);
/*  862 */       } catch (ClassCastException ex) {
/*  863 */         throw new IllegalArgumentException(MessageLocalization.getComposedMessage("you.have.to.consolidate.the.named.destinations.of.your.reader", new Object[0]));
/*      */       } 
/*  865 */       if (this.destination != null) {
/*  866 */         this.destination = new PdfArray(this.destination);
/*      */       }
/*  868 */       PdfArray rc = (PdfArray)this.parameters.remove(PdfName.RECT);
/*  869 */       this.llx = rc.getAsNumber(0).floatValue();
/*  870 */       this.lly = rc.getAsNumber(1).floatValue();
/*  871 */       this.urx = rc.getAsNumber(2).floatValue();
/*  872 */       this.ury = rc.getAsNumber(3).floatValue();
/*      */       
/*  874 */       this.rect = new PdfArray(rc);
/*      */     }
/*      */     
/*      */     public Map<PdfName, PdfObject> getParameters() {
/*  878 */       return new HashMap<PdfName, PdfObject>(this.parameters);
/*      */     }
/*      */     
/*      */     public PdfArray getRect() {
/*  882 */       return new PdfArray(this.rect);
/*      */     }
/*      */     
/*      */     public boolean isInternal() {
/*  886 */       return (this.destination != null);
/*      */     }
/*      */     
/*      */     public int getDestinationPage() {
/*  890 */       if (!isInternal()) return 0;
/*      */ 
/*      */ 
/*      */       
/*  894 */       PdfIndirectReference ref = this.destination.getAsIndirectObject(0);
/*      */       
/*  896 */       PRIndirectReference pr = (PRIndirectReference)ref;
/*  897 */       PdfReader r = pr.getReader();
/*  898 */       for (int i = 1; i <= r.getNumberOfPages(); i++) {
/*  899 */         PRIndirectReference pp = r.getPageOrigRef(i);
/*  900 */         if (pp.getGeneration() == pr.getGeneration() && pp.getNumber() == pr.getNumber()) return i; 
/*      */       } 
/*  902 */       throw new IllegalArgumentException(MessageLocalization.getComposedMessage("page.not.found", new Object[0]));
/*      */     }
/*      */     
/*      */     public void setDestinationPage(int newPage) {
/*  906 */       if (!isInternal()) throw new IllegalArgumentException(MessageLocalization.getComposedMessage("cannot.change.destination.of.external.link", new Object[0])); 
/*  907 */       this.newPage = newPage;
/*      */     }
/*      */     
/*      */     public void transformDestination(float a, float b, float c, float d, float e, float f) {
/*  911 */       if (!isInternal()) throw new IllegalArgumentException(MessageLocalization.getComposedMessage("cannot.change.destination.of.external.link", new Object[0])); 
/*  912 */       if (this.destination.getAsName(1).equals(PdfName.XYZ)) {
/*  913 */         float x = this.destination.getAsNumber(2).floatValue();
/*  914 */         float y = this.destination.getAsNumber(3).floatValue();
/*  915 */         float xx = x * a + y * c + e;
/*  916 */         float yy = x * b + y * d + f;
/*  917 */         this.destination.set(2, new PdfNumber(xx));
/*  918 */         this.destination.set(3, new PdfNumber(yy));
/*      */       } 
/*      */     }
/*      */     
/*      */     public void transformRect(float a, float b, float c, float d, float e, float f) {
/*  923 */       float x = this.llx * a + this.lly * c + e;
/*  924 */       float y = this.llx * b + this.lly * d + f;
/*  925 */       this.llx = x;
/*  926 */       this.lly = y;
/*  927 */       x = this.urx * a + this.ury * c + e;
/*  928 */       y = this.urx * b + this.ury * d + f;
/*  929 */       this.urx = x;
/*  930 */       this.ury = y;
/*      */     }
/*      */     
/*      */     public PdfAnnotation createAnnotation(PdfWriter writer) {
/*  934 */       PdfAnnotation annotation = writer.createAnnotation(new Rectangle(this.llx, this.lly, this.urx, this.ury), null);
/*  935 */       if (this.newPage != 0) {
/*  936 */         PdfIndirectReference ref = writer.getPageReference(this.newPage);
/*  937 */         this.destination.set(0, ref);
/*      */       } 
/*  939 */       if (this.destination != null) annotation.put(PdfName.DEST, this.destination); 
/*  940 */       annotation.hashMap.putAll(this.parameters);
/*  941 */       return annotation;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String toString() {
/*  951 */       StringBuffer buf = new StringBuffer("Imported link: location [");
/*  952 */       buf.append(this.llx);
/*  953 */       buf.append(' ');
/*  954 */       buf.append(this.lly);
/*  955 */       buf.append(' ');
/*  956 */       buf.append(this.urx);
/*  957 */       buf.append(' ');
/*  958 */       buf.append(this.ury);
/*  959 */       buf.append("] destination ");
/*  960 */       buf.append(this.destination);
/*  961 */       buf.append(" parameters ");
/*  962 */       buf.append(this.parameters);
/*  963 */       if (this.parameters != null) {
/*  964 */         appendDictionary(buf, this.parameters);
/*      */       }
/*      */       
/*  967 */       return buf.toString();
/*      */     }
/*      */     
/*      */     private void appendDictionary(StringBuffer buf, HashMap<PdfName, PdfObject> dict) {
/*  971 */       buf.append(" <<");
/*  972 */       for (Map.Entry<PdfName, PdfObject> entry : dict.entrySet()) {
/*  973 */         buf.append(entry.getKey());
/*  974 */         buf.append(":");
/*  975 */         if (entry.getValue() instanceof PdfDictionary) {
/*  976 */           appendDictionary(buf, ((PdfDictionary)entry.getValue()).hashMap);
/*      */         } else {
/*  978 */           buf.append(entry.getValue());
/*  979 */         }  buf.append(" ");
/*      */       } 
/*      */       
/*  982 */       buf.append(">> ");
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void toPdf(PdfWriter writer, OutputStream os) throws IOException {
/*  989 */     PdfWriter.checkPdfIsoConformance(writer, 13, this);
/*  990 */     super.toPdf(writer, os);
/*      */   }
/*      */   
/*      */   public PdfObject getAccessibleAttribute(PdfName key) {
/*  994 */     if (this.accessibleAttributes != null) {
/*  995 */       return this.accessibleAttributes.get(key);
/*      */     }
/*  997 */     return null;
/*      */   }
/*      */   
/*      */   public void setAccessibleAttribute(PdfName key, PdfObject value) {
/* 1001 */     if (this.accessibleAttributes == null)
/* 1002 */       this.accessibleAttributes = new HashMap<PdfName, PdfObject>(); 
/* 1003 */     this.accessibleAttributes.put(key, value);
/*      */   }
/*      */   
/*      */   public HashMap<PdfName, PdfObject> getAccessibleAttributes() {
/* 1007 */     return this.accessibleAttributes;
/*      */   }
/*      */   
/*      */   public PdfName getRole() {
/* 1011 */     return this.role;
/*      */   }
/*      */   
/*      */   public void setRole(PdfName role) {
/* 1015 */     this.role = role;
/*      */   }
/*      */   
/*      */   public AccessibleElementId getId() {
/* 1019 */     if (this.id == null)
/* 1020 */       this.id = new AccessibleElementId(); 
/* 1021 */     return this.id;
/*      */   }
/*      */   
/*      */   public void setId(AccessibleElementId id) {
/* 1025 */     this.id = id;
/*      */   }
/*      */   
/*      */   public boolean isInline() {
/* 1029 */     return false;
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/PdfAnnotation.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */