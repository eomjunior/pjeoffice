/*      */ package com.itextpdf.text.pdf;
/*      */ 
/*      */ import com.itextpdf.text.Chunk;
/*      */ import com.itextpdf.text.DocumentException;
/*      */ import com.itextpdf.text.Element;
/*      */ import com.itextpdf.text.ExceptionConverter;
/*      */ import com.itextpdf.text.Font;
/*      */ import com.itextpdf.text.Image;
/*      */ import com.itextpdf.text.List;
/*      */ import com.itextpdf.text.ListBody;
/*      */ import com.itextpdf.text.ListItem;
/*      */ import com.itextpdf.text.ListLabel;
/*      */ import com.itextpdf.text.Paragraph;
/*      */ import com.itextpdf.text.Phrase;
/*      */ import com.itextpdf.text.Rectangle;
/*      */ import com.itextpdf.text.error_messages.MessageLocalization;
/*      */ import com.itextpdf.text.log.Logger;
/*      */ import com.itextpdf.text.log.LoggerFactory;
/*      */ import com.itextpdf.text.pdf.draw.DrawInterface;
/*      */ import com.itextpdf.text.pdf.interfaces.IAccessibleElement;
/*      */ import java.util.ArrayList;
/*      */ import java.util.LinkedList;
/*      */ import java.util.List;
/*      */ import java.util.Stack;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class ColumnText
/*      */ {
/*   87 */   private final Logger LOGGER = LoggerFactory.getLogger(ColumnText.class);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final int AR_NOVOWEL = 1;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final int AR_COMPOSEDTASHKEEL = 4;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final int AR_LIG = 8;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final int DIGITS_EN2AN = 32;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final int DIGITS_AN2EN = 64;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final int DIGITS_EN2AN_INIT_LR = 96;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final int DIGITS_EN2AN_INIT_AL = 128;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final int DIGIT_TYPE_AN = 0;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final int DIGIT_TYPE_AN_EXTENDED = 256;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  144 */   protected int runDirection = 1;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final float GLOBAL_SPACE_CHAR_RATIO = 0.0F;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final int START_COLUMN = 0;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final int NO_MORE_TEXT = 1;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final int NO_MORE_COLUMN = 2;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected static final int LINE_STATUS_OK = 0;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected static final int LINE_STATUS_OFFLIMITS = 1;
/*      */ 
/*      */ 
/*      */   
/*      */   protected static final int LINE_STATUS_NOLINE = 2;
/*      */ 
/*      */ 
/*      */   
/*      */   protected float maxY;
/*      */ 
/*      */ 
/*      */   
/*      */   protected float minY;
/*      */ 
/*      */ 
/*      */   
/*      */   protected float leftX;
/*      */ 
/*      */ 
/*      */   
/*      */   protected float rightX;
/*      */ 
/*      */ 
/*      */   
/*  198 */   protected int alignment = 0;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected ArrayList<float[]> leftWall;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected ArrayList<float[]> rightWall;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected BidiLine bidiLine;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean isWordSplit;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected float yLine;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected float lastX;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  234 */   protected float currentLeading = 16.0F;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  239 */   protected float fixedLeading = 16.0F;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  244 */   protected float multipliedLeading = 0.0F;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected PdfContentByte canvas;
/*      */ 
/*      */ 
/*      */   
/*      */   protected PdfContentByte[] canvases;
/*      */ 
/*      */ 
/*      */   
/*      */   protected int lineStatus;
/*      */ 
/*      */ 
/*      */   
/*  261 */   protected float indent = 0.0F;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  266 */   protected float followingIndent = 0.0F;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  271 */   protected float rightIndent = 0.0F;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  276 */   protected float extraParagraphSpace = 0.0F;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  281 */   protected float rectangularWidth = -1.0F;
/*      */ 
/*      */   
/*      */   protected boolean rectangularMode = false;
/*      */ 
/*      */   
/*  287 */   private float spaceCharRatio = 0.0F;
/*      */ 
/*      */   
/*      */   private boolean lastWasNewline = true;
/*      */ 
/*      */   
/*      */   private boolean repeatFirstLineIndent = true;
/*      */ 
/*      */   
/*      */   private int linesWritten;
/*      */ 
/*      */   
/*      */   private float firstLineY;
/*      */   
/*      */   private boolean firstLineYDone = false;
/*      */   
/*  303 */   private int arabicOptions = 0;
/*      */   
/*      */   protected float descender;
/*      */   
/*      */   protected boolean composite = false;
/*      */   
/*      */   protected ColumnText compositeColumn;
/*      */   
/*      */   protected LinkedList<Element> compositeElements;
/*      */   
/*  313 */   protected int listIdx = 0;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  319 */   protected int rowIdx = 0;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  328 */   private int splittedRow = -2;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Phrase waitPhrase;
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean useAscender = false;
/*      */ 
/*      */ 
/*      */   
/*      */   private float filledWidth;
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean adjustFirstLine = true;
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean inheritGraphicState = false;
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean ignoreSpacingBefore = true;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ColumnText(PdfContentByte canvas) {
/*  359 */     this.canvas = canvas;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static ColumnText duplicate(ColumnText org) {
/*  369 */     ColumnText ct = new ColumnText(null);
/*  370 */     ct.setACopy(org);
/*  371 */     return ct;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ColumnText setACopy(ColumnText org) {
/*  381 */     if (org != null) {
/*  382 */       setSimpleVars(org);
/*  383 */       if (org.bidiLine != null) {
/*  384 */         this.bidiLine = new BidiLine(org.bidiLine);
/*      */       }
/*      */     } 
/*  387 */     return this;
/*      */   }
/*      */   
/*      */   protected void setSimpleVars(ColumnText org) {
/*  391 */     this.maxY = org.maxY;
/*  392 */     this.minY = org.minY;
/*  393 */     this.alignment = org.alignment;
/*  394 */     this.leftWall = null;
/*  395 */     if (org.leftWall != null) {
/*  396 */       this.leftWall = (ArrayList)new ArrayList<float>(org.leftWall);
/*      */     }
/*  398 */     this.rightWall = null;
/*  399 */     if (org.rightWall != null) {
/*  400 */       this.rightWall = (ArrayList)new ArrayList<float>(org.rightWall);
/*      */     }
/*  402 */     this.yLine = org.yLine;
/*  403 */     this.currentLeading = org.currentLeading;
/*  404 */     this.fixedLeading = org.fixedLeading;
/*  405 */     this.multipliedLeading = org.multipliedLeading;
/*  406 */     this.canvas = org.canvas;
/*  407 */     this.canvases = org.canvases;
/*  408 */     this.lineStatus = org.lineStatus;
/*  409 */     this.indent = org.indent;
/*  410 */     this.followingIndent = org.followingIndent;
/*  411 */     this.rightIndent = org.rightIndent;
/*  412 */     this.extraParagraphSpace = org.extraParagraphSpace;
/*  413 */     this.rectangularWidth = org.rectangularWidth;
/*  414 */     this.rectangularMode = org.rectangularMode;
/*  415 */     this.spaceCharRatio = org.spaceCharRatio;
/*  416 */     this.lastWasNewline = org.lastWasNewline;
/*  417 */     this.repeatFirstLineIndent = org.repeatFirstLineIndent;
/*  418 */     this.linesWritten = org.linesWritten;
/*  419 */     this.arabicOptions = org.arabicOptions;
/*  420 */     this.runDirection = org.runDirection;
/*  421 */     this.descender = org.descender;
/*  422 */     this.composite = org.composite;
/*  423 */     this.splittedRow = org.splittedRow;
/*  424 */     if (org.composite) {
/*  425 */       this.compositeElements = new LinkedList<Element>();
/*  426 */       for (Element element : org.compositeElements) {
/*  427 */         if (element instanceof PdfPTable) {
/*  428 */           this.compositeElements.add(new PdfPTable((PdfPTable)element)); continue;
/*      */         } 
/*  430 */         this.compositeElements.add(element);
/*      */       } 
/*      */       
/*  433 */       if (org.compositeColumn != null) {
/*  434 */         this.compositeColumn = duplicate(org.compositeColumn);
/*      */       }
/*      */     } 
/*  437 */     this.listIdx = org.listIdx;
/*  438 */     this.rowIdx = org.rowIdx;
/*  439 */     this.firstLineY = org.firstLineY;
/*  440 */     this.leftX = org.leftX;
/*  441 */     this.rightX = org.rightX;
/*  442 */     this.firstLineYDone = org.firstLineYDone;
/*  443 */     this.waitPhrase = org.waitPhrase;
/*  444 */     this.useAscender = org.useAscender;
/*  445 */     this.filledWidth = org.filledWidth;
/*  446 */     this.adjustFirstLine = org.adjustFirstLine;
/*  447 */     this.inheritGraphicState = org.inheritGraphicState;
/*  448 */     this.ignoreSpacingBefore = org.ignoreSpacingBefore;
/*      */   }
/*      */   
/*      */   private void addWaitingPhrase() {
/*  452 */     if (this.bidiLine == null && this.waitPhrase != null) {
/*  453 */       this.bidiLine = new BidiLine();
/*  454 */       for (Chunk c : this.waitPhrase.getChunks()) {
/*  455 */         this.bidiLine.addChunk(new PdfChunk(c, null, this.waitPhrase.getTabSettings()));
/*      */       }
/*  457 */       this.waitPhrase = null;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addText(Phrase phrase) {
/*  468 */     if (phrase == null || this.composite) {
/*      */       return;
/*      */     }
/*  471 */     addWaitingPhrase();
/*  472 */     if (this.bidiLine == null) {
/*  473 */       this.waitPhrase = phrase;
/*      */       return;
/*      */     } 
/*  476 */     for (Object element : phrase.getChunks()) {
/*  477 */       this.bidiLine.addChunk(new PdfChunk((Chunk)element, null, phrase.getTabSettings()));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setText(Phrase phrase) {
/*  488 */     this.bidiLine = null;
/*  489 */     this.composite = false;
/*  490 */     this.compositeColumn = null;
/*  491 */     this.compositeElements = null;
/*  492 */     this.listIdx = 0;
/*  493 */     this.rowIdx = 0;
/*  494 */     this.splittedRow = -1;
/*  495 */     this.waitPhrase = phrase;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addText(Chunk chunk) {
/*  505 */     if (chunk == null || this.composite) {
/*      */       return;
/*      */     }
/*  508 */     addText(new Phrase(chunk));
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
/*      */   public void addElement(Element element) {
/*      */     PdfPTable pdfPTable;
/*      */     Paragraph paragraph;
/*  522 */     if (element == null) {
/*      */       return;
/*      */     }
/*  525 */     if (element instanceof Image) {
/*  526 */       Image img = (Image)element;
/*  527 */       PdfPTable t = new PdfPTable(1);
/*  528 */       float w = img.getWidthPercentage();
/*  529 */       if (w == 0.0F) {
/*  530 */         t.setTotalWidth(img.getScaledWidth());
/*  531 */         t.setLockedWidth(true);
/*      */       } else {
/*  533 */         t.setWidthPercentage(w);
/*      */       } 
/*  535 */       t.setSpacingAfter(img.getSpacingAfter());
/*  536 */       t.setSpacingBefore(img.getSpacingBefore());
/*  537 */       switch (img.getAlignment()) {
/*      */         case 0:
/*  539 */           t.setHorizontalAlignment(0);
/*      */           break;
/*      */         case 2:
/*  542 */           t.setHorizontalAlignment(2);
/*      */           break;
/*      */         default:
/*  545 */           t.setHorizontalAlignment(1);
/*      */           break;
/*      */       } 
/*  548 */       PdfPCell c = new PdfPCell(img, true);
/*  549 */       c.setPadding(0.0F);
/*  550 */       c.setBorder(img.getBorder());
/*  551 */       c.setBorderColor(img.getBorderColor());
/*  552 */       c.setBorderWidth(img.getBorderWidth());
/*  553 */       c.setBackgroundColor(img.getBackgroundColor());
/*  554 */       t.addCell(c);
/*  555 */       pdfPTable = t;
/*      */     } 
/*  557 */     if (pdfPTable.type() == 10) {
/*  558 */       paragraph = new Paragraph((Chunk)pdfPTable);
/*  559 */     } else if (paragraph.type() == 11) {
/*  560 */       paragraph = new Paragraph((Phrase)paragraph);
/*  561 */     } else if (paragraph.type() == 23) {
/*  562 */       ((PdfPTable)paragraph).init();
/*      */     } 
/*  564 */     if (paragraph.type() != 12 && paragraph.type() != 14 && paragraph.type() != 23 && paragraph.type() != 55 && paragraph.type() != 37) {
/*  565 */       throw new IllegalArgumentException(MessageLocalization.getComposedMessage("element.not.allowed", new Object[0]));
/*      */     }
/*  567 */     if (!this.composite) {
/*  568 */       this.composite = true;
/*  569 */       this.compositeElements = new LinkedList<Element>();
/*  570 */       this.bidiLine = null;
/*  571 */       this.waitPhrase = null;
/*      */     } 
/*  573 */     if (paragraph.type() == 12) {
/*  574 */       Paragraph p = paragraph;
/*  575 */       this.compositeElements.addAll(p.breakUp());
/*      */       return;
/*      */     } 
/*  578 */     this.compositeElements.add(paragraph);
/*      */   }
/*      */   
/*      */   public static boolean isAllowedElement(Element element) {
/*  582 */     int type = element.type();
/*  583 */     if (type == 10 || type == 11 || type == 37 || type == 12 || type == 14 || type == 55 || type == 23)
/*      */     {
/*      */       
/*  586 */       return true;
/*      */     }
/*  588 */     if (element instanceof Image) {
/*  589 */       return true;
/*      */     }
/*  591 */     return false;
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
/*      */   protected ArrayList<float[]> convertColumn(float[] cLine) {
/*  605 */     if (cLine.length < 4) {
/*  606 */       throw new RuntimeException(MessageLocalization.getComposedMessage("no.valid.column.line.found", new Object[0]));
/*      */     }
/*  608 */     ArrayList<float[]> cc = (ArrayList)new ArrayList<float>();
/*  609 */     for (int k = 0; k < cLine.length - 2; k += 2) {
/*  610 */       float x1 = cLine[k];
/*  611 */       float y1 = cLine[k + 1];
/*  612 */       float x2 = cLine[k + 2];
/*  613 */       float y2 = cLine[k + 3];
/*  614 */       if (y1 != y2) {
/*      */ 
/*      */ 
/*      */         
/*  618 */         float a = (x1 - x2) / (y1 - y2);
/*  619 */         float b = x1 - a * y1;
/*  620 */         float[] r = new float[4];
/*  621 */         r[0] = Math.min(y1, y2);
/*  622 */         r[1] = Math.max(y1, y2);
/*  623 */         r[2] = a;
/*  624 */         r[3] = b;
/*  625 */         cc.add(r);
/*  626 */         this.maxY = Math.max(this.maxY, r[1]);
/*  627 */         this.minY = Math.min(this.minY, r[0]);
/*      */       } 
/*  629 */     }  if (cc.isEmpty()) {
/*  630 */       throw new RuntimeException(MessageLocalization.getComposedMessage("no.valid.column.line.found", new Object[0]));
/*      */     }
/*  632 */     return cc;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected float findLimitsPoint(ArrayList<float[]> wall) {
/*  643 */     this.lineStatus = 0;
/*  644 */     if (this.yLine < this.minY || this.yLine > this.maxY) {
/*  645 */       this.lineStatus = 1;
/*  646 */       return 0.0F;
/*      */     } 
/*  648 */     for (int k = 0; k < wall.size(); ) {
/*  649 */       float[] r = wall.get(k);
/*  650 */       if (this.yLine < r[0] || this.yLine > r[1]) {
/*      */         k++; continue;
/*      */       } 
/*  653 */       return r[2] * this.yLine + r[3];
/*      */     } 
/*  655 */     this.lineStatus = 2;
/*  656 */     return 0.0F;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected float[] findLimitsOneLine() {
/*  666 */     float x1 = findLimitsPoint(this.leftWall);
/*  667 */     if (this.lineStatus == 1 || this.lineStatus == 2) {
/*  668 */       return null;
/*      */     }
/*  670 */     float x2 = findLimitsPoint(this.rightWall);
/*  671 */     if (this.lineStatus == 2) {
/*  672 */       return null;
/*      */     }
/*  674 */     return new float[] { x1, x2 };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected float[] findLimitsTwoLines() {
/*      */     float[] x1, x2;
/*  685 */     boolean repeat = false;
/*      */     while (true) {
/*  687 */       if (repeat && this.currentLeading == 0.0F) {
/*  688 */         return null;
/*      */       }
/*  690 */       repeat = true;
/*  691 */       x1 = findLimitsOneLine();
/*  692 */       if (this.lineStatus == 1) {
/*  693 */         return null;
/*      */       }
/*  695 */       this.yLine -= this.currentLeading;
/*  696 */       if (this.lineStatus == 2) {
/*      */         continue;
/*      */       }
/*  699 */       x2 = findLimitsOneLine();
/*  700 */       if (this.lineStatus == 1) {
/*  701 */         return null;
/*      */       }
/*  703 */       if (this.lineStatus == 2) {
/*  704 */         this.yLine -= this.currentLeading;
/*      */         continue;
/*      */       } 
/*  707 */       if (x1[0] >= x2[1] || x2[0] >= x1[1])
/*      */         continue;  break;
/*      */     } 
/*  710 */     return new float[] { x1[0], x1[1], x2[0], x2[1] };
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
/*      */   public void setColumns(float[] leftLine, float[] rightLine) {
/*  723 */     this.maxY = -1.0E21F;
/*  724 */     this.minY = 1.0E21F;
/*  725 */     setYLine(Math.max(leftLine[1], leftLine[leftLine.length - 1]));
/*  726 */     this.rightWall = convertColumn(rightLine);
/*  727 */     this.leftWall = convertColumn(leftLine);
/*  728 */     this.rectangularWidth = -1.0F;
/*  729 */     this.rectangularMode = false;
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
/*      */   public void setSimpleColumn(Phrase phrase, float llx, float lly, float urx, float ury, float leading, int alignment) {
/*  744 */     addText(phrase);
/*  745 */     setSimpleColumn(llx, lly, urx, ury, leading, alignment);
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
/*      */   public void setSimpleColumn(float llx, float lly, float urx, float ury, float leading, int alignment) {
/*  759 */     setLeading(leading);
/*  760 */     this.alignment = alignment;
/*  761 */     setSimpleColumn(llx, lly, urx, ury);
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
/*      */   public void setSimpleColumn(float llx, float lly, float urx, float ury) {
/*  773 */     this.leftX = Math.min(llx, urx);
/*  774 */     this.maxY = Math.max(lly, ury);
/*  775 */     this.minY = Math.min(lly, ury);
/*  776 */     this.rightX = Math.max(llx, urx);
/*  777 */     this.yLine = this.maxY;
/*  778 */     this.rectangularWidth = this.rightX - this.leftX;
/*  779 */     if (this.rectangularWidth < 0.0F) {
/*  780 */       this.rectangularWidth = 0.0F;
/*      */     }
/*  782 */     this.rectangularMode = true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setSimpleColumn(Rectangle rect) {
/*  791 */     setSimpleColumn(rect.getLeft(), rect.getBottom(), rect.getRight(), rect.getTop());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setLeading(float leading) {
/*  800 */     this.fixedLeading = leading;
/*  801 */     this.multipliedLeading = 0.0F;
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
/*      */   public void setLeading(float fixedLeading, float multipliedLeading) {
/*  813 */     this.fixedLeading = fixedLeading;
/*  814 */     this.multipliedLeading = multipliedLeading;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getLeading() {
/*  823 */     return this.fixedLeading;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getMultipliedLeading() {
/*  832 */     return this.multipliedLeading;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setYLine(float yLine) {
/*  841 */     this.yLine = yLine;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getYLine() {
/*  850 */     return this.yLine;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getRowsDrawn() {
/*  857 */     return this.rowIdx;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAlignment(int alignment) {
/*  866 */     this.alignment = alignment;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getAlignment() {
/*  875 */     return this.alignment;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setIndent(float indent) {
/*  884 */     setIndent(indent, true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setIndent(float indent, boolean repeatFirstLineIndent) {
/*  895 */     this.indent = indent;
/*  896 */     this.lastWasNewline = true;
/*  897 */     this.repeatFirstLineIndent = repeatFirstLineIndent;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getIndent() {
/*  906 */     return this.indent;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setFollowingIndent(float indent) {
/*  915 */     this.followingIndent = indent;
/*  916 */     this.lastWasNewline = true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getFollowingIndent() {
/*  925 */     return this.followingIndent;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setRightIndent(float indent) {
/*  934 */     this.rightIndent = indent;
/*  935 */     this.lastWasNewline = true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getRightIndent() {
/*  944 */     return this.rightIndent;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getCurrentLeading() {
/*  953 */     return this.currentLeading;
/*      */   }
/*      */   
/*      */   public boolean getInheritGraphicState() {
/*  957 */     return this.inheritGraphicState;
/*      */   }
/*      */   
/*      */   public void setInheritGraphicState(boolean inheritGraphicState) {
/*  961 */     this.inheritGraphicState = inheritGraphicState;
/*      */   }
/*      */   
/*      */   public boolean isIgnoreSpacingBefore() {
/*  965 */     return this.ignoreSpacingBefore;
/*      */   }
/*      */   
/*      */   public void setIgnoreSpacingBefore(boolean ignoreSpacingBefore) {
/*  969 */     this.ignoreSpacingBefore = ignoreSpacingBefore;
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
/*      */   public int go() throws DocumentException {
/*  981 */     return go(false);
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
/*      */   public int go(boolean simulate) throws DocumentException {
/*  993 */     return go(simulate, null);
/*      */   }
/*      */   
/*      */   public int go(boolean simulate, IAccessibleElement elementToGo) throws DocumentException {
/*  997 */     this.isWordSplit = false;
/*  998 */     if (this.composite) {
/*  999 */       return goComposite(simulate);
/*      */     }
/*      */     
/* 1002 */     ListBody lBody = null;
/* 1003 */     if (isTagged(this.canvas) && elementToGo instanceof ListItem) {
/* 1004 */       lBody = ((ListItem)elementToGo).getListBody();
/*      */     }
/*      */     
/* 1007 */     addWaitingPhrase();
/* 1008 */     if (this.bidiLine == null) {
/* 1009 */       return 1;
/*      */     }
/* 1011 */     this.descender = 0.0F;
/* 1012 */     this.linesWritten = 0;
/* 1013 */     this.lastX = 0.0F;
/* 1014 */     boolean dirty = false;
/* 1015 */     float ratio = this.spaceCharRatio;
/* 1016 */     Object[] currentValues = new Object[2];
/* 1017 */     PdfFont currentFont = null;
/* 1018 */     Float lastBaseFactor = new Float(0.0F);
/* 1019 */     currentValues[1] = lastBaseFactor;
/* 1020 */     PdfDocument pdf = null;
/* 1021 */     PdfContentByte graphics = null;
/* 1022 */     PdfContentByte text = null;
/* 1023 */     this.firstLineY = Float.NaN;
/* 1024 */     int localRunDirection = this.runDirection;
/* 1025 */     if (this.canvas != null) {
/* 1026 */       graphics = this.canvas;
/* 1027 */       pdf = this.canvas.getPdfDocument();
/* 1028 */       if (!isTagged(this.canvas)) {
/* 1029 */         text = this.canvas.getDuplicate(this.inheritGraphicState);
/*      */       } else {
/* 1031 */         text = this.canvas;
/*      */       } 
/* 1033 */     } else if (!simulate) {
/* 1034 */       throw new NullPointerException(MessageLocalization.getComposedMessage("columntext.go.with.simulate.eq.eq.false.and.text.eq.eq.null", new Object[0]));
/*      */     } 
/* 1036 */     if (!simulate) {
/* 1037 */       if (ratio == 0.0F) {
/* 1038 */         ratio = text.getPdfWriter().getSpaceCharRatio();
/* 1039 */       } else if (ratio < 0.001F) {
/* 1040 */         ratio = 0.001F;
/*      */       } 
/*      */     }
/* 1043 */     if (!this.rectangularMode) {
/* 1044 */       float max = 0.0F;
/* 1045 */       for (PdfChunk c : this.bidiLine.chunks) {
/* 1046 */         max = Math.max(max, c.height());
/*      */       }
/* 1048 */       this.currentLeading = this.fixedLeading + max * this.multipliedLeading;
/*      */     } 
/* 1050 */     float firstIndent = 0.0F;
/*      */ 
/*      */     
/* 1053 */     int status = 0;
/* 1054 */     boolean rtl = false; while (true) {
/*      */       PdfLine line; float x1;
/* 1056 */       firstIndent = this.lastWasNewline ? this.indent : this.followingIndent;
/* 1057 */       if (this.rectangularMode) {
/* 1058 */         if (this.rectangularWidth <= firstIndent + this.rightIndent) {
/* 1059 */           status = 2;
/* 1060 */           if (this.bidiLine.isEmpty()) {
/* 1061 */             status |= 0x1;
/*      */           }
/*      */           break;
/*      */         } 
/* 1065 */         if (this.bidiLine.isEmpty()) {
/* 1066 */           status = 1;
/*      */           break;
/*      */         } 
/* 1069 */         line = this.bidiLine.processLine(this.leftX, this.rectangularWidth - firstIndent - this.rightIndent, this.alignment, localRunDirection, this.arabicOptions, this.minY, this.yLine, this.descender);
/* 1070 */         this.isWordSplit |= this.bidiLine.isWordSplit();
/* 1071 */         if (line == null) {
/* 1072 */           status = 1;
/*      */           break;
/*      */         } 
/* 1075 */         float[] maxSize = line.getMaxSize(this.fixedLeading, this.multipliedLeading);
/* 1076 */         if (isUseAscender() && Float.isNaN(this.firstLineY)) {
/* 1077 */           this.currentLeading = line.getAscender();
/*      */         } else {
/* 1079 */           this.currentLeading = Math.max(maxSize[0], maxSize[1] - this.descender);
/*      */         } 
/* 1081 */         if (this.yLine > this.maxY || this.yLine - this.currentLeading < this.minY) {
/* 1082 */           status = 2;
/* 1083 */           this.bidiLine.restore();
/*      */           break;
/*      */         } 
/* 1086 */         this.yLine -= this.currentLeading;
/* 1087 */         if (!simulate && !dirty) {
/*      */           
/* 1089 */           if (line.isRTL && this.canvas.isTagged()) {
/*      */             
/* 1091 */             this.canvas.beginMarkedContentSequence(PdfName.REVERSEDCHARS);
/* 1092 */             rtl = true;
/*      */           } 
/* 1094 */           text.beginText();
/* 1095 */           dirty = true;
/*      */         } 
/* 1097 */         if (Float.isNaN(this.firstLineY)) {
/* 1098 */           this.firstLineY = this.yLine;
/*      */         }
/* 1100 */         updateFilledWidth(this.rectangularWidth - line.widthLeft());
/* 1101 */         x1 = this.leftX;
/*      */       } else {
/* 1103 */         float yTemp = this.yLine - this.currentLeading;
/* 1104 */         float[] xx = findLimitsTwoLines();
/* 1105 */         if (xx == null) {
/* 1106 */           status = 2;
/* 1107 */           if (this.bidiLine.isEmpty()) {
/* 1108 */             status |= 0x1;
/*      */           }
/* 1110 */           this.yLine = yTemp;
/*      */           break;
/*      */         } 
/* 1113 */         if (this.bidiLine.isEmpty()) {
/* 1114 */           status = 1;
/* 1115 */           this.yLine = yTemp;
/*      */           break;
/*      */         } 
/* 1118 */         x1 = Math.max(xx[0], xx[2]);
/* 1119 */         float x2 = Math.min(xx[1], xx[3]);
/* 1120 */         if (x2 - x1 <= firstIndent + this.rightIndent) {
/*      */           continue;
/*      */         }
/* 1123 */         line = this.bidiLine.processLine(x1, x2 - x1 - firstIndent - this.rightIndent, this.alignment, localRunDirection, this.arabicOptions, this.minY, this.yLine, this.descender);
/* 1124 */         if (!simulate && !dirty) {
/*      */           
/* 1126 */           if (line.isRTL && this.canvas.isTagged()) {
/*      */             
/* 1128 */             this.canvas.beginMarkedContentSequence(PdfName.REVERSEDCHARS);
/* 1129 */             rtl = true;
/*      */           } 
/* 1131 */           text.beginText();
/* 1132 */           dirty = true;
/*      */         } 
/* 1134 */         if (line == null) {
/* 1135 */           status = 1;
/* 1136 */           this.yLine = yTemp;
/*      */           break;
/*      */         } 
/*      */       } 
/* 1140 */       if (isTagged(this.canvas) && elementToGo instanceof ListItem && 
/* 1141 */         !Float.isNaN(this.firstLineY) && !this.firstLineYDone) {
/* 1142 */         if (!simulate) {
/* 1143 */           ListLabel lbl = ((ListItem)elementToGo).getListLabel();
/* 1144 */           this.canvas.openMCBlock((IAccessibleElement)lbl);
/* 1145 */           Chunk symbol = new Chunk(((ListItem)elementToGo).getListSymbol());
/* 1146 */           symbol.setRole(null);
/* 1147 */           showTextAligned(this.canvas, 0, new Phrase(symbol), this.leftX + lbl.getIndentation(), this.firstLineY, 0.0F);
/* 1148 */           this.canvas.closeMCBlock((IAccessibleElement)lbl);
/*      */         } 
/* 1150 */         this.firstLineYDone = true;
/*      */       } 
/*      */       
/* 1153 */       if (!simulate) {
/* 1154 */         if (lBody != null) {
/* 1155 */           this.canvas.openMCBlock((IAccessibleElement)lBody);
/* 1156 */           lBody = null;
/*      */         } 
/* 1158 */         currentValues[0] = currentFont;
/* 1159 */         text.setTextMatrix(x1 + (line.isRTL() ? this.rightIndent : firstIndent) + line.indentLeft(), this.yLine);
/* 1160 */         this.lastX = pdf.writeLineToContent(line, text, graphics, currentValues, ratio);
/* 1161 */         currentFont = (PdfFont)currentValues[0];
/*      */       } 
/* 1163 */       this.lastWasNewline = (this.repeatFirstLineIndent && line.isNewlineSplit());
/* 1164 */       this.yLine -= line.isNewlineSplit() ? this.extraParagraphSpace : 0.0F;
/* 1165 */       this.linesWritten++;
/* 1166 */       this.descender = line.getDescender();
/*      */     } 
/* 1168 */     if (dirty) {
/* 1169 */       text.endText();
/* 1170 */       if (this.canvas != text) {
/* 1171 */         this.canvas.add(text);
/*      */       }
/* 1173 */       if (rtl && this.canvas.isTagged()) {
/* 1174 */         this.canvas.endMarkedContentSequence();
/*      */       }
/*      */     } 
/* 1177 */     return status;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isWordSplit() {
/* 1186 */     return this.isWordSplit;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getExtraParagraphSpace() {
/* 1195 */     return this.extraParagraphSpace;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setExtraParagraphSpace(float extraParagraphSpace) {
/* 1204 */     this.extraParagraphSpace = extraParagraphSpace;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void clearChunks() {
/* 1212 */     if (this.bidiLine != null) {
/* 1213 */       this.bidiLine.clearChunks();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getSpaceCharRatio() {
/* 1223 */     return this.spaceCharRatio;
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
/*      */   public void setSpaceCharRatio(float spaceCharRatio) {
/* 1237 */     this.spaceCharRatio = spaceCharRatio;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setRunDirection(int runDirection) {
/* 1246 */     if (runDirection < 0 || runDirection > 3) {
/* 1247 */       throw new RuntimeException(MessageLocalization.getComposedMessage("invalid.run.direction.1", runDirection));
/*      */     }
/* 1249 */     this.runDirection = runDirection;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getRunDirection() {
/* 1258 */     return this.runDirection;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getLinesWritten() {
/* 1267 */     return this.linesWritten;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getLastX() {
/* 1277 */     return this.lastX;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getArabicOptions() {
/* 1286 */     return this.arabicOptions;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setArabicOptions(int arabicOptions) {
/* 1296 */     this.arabicOptions = arabicOptions;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getDescender() {
/* 1305 */     return this.descender;
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
/*      */   public static float getWidth(Phrase phrase, int runDirection, int arabicOptions) {
/* 1318 */     ColumnText ct = new ColumnText(null);
/* 1319 */     ct.addText(phrase);
/* 1320 */     ct.addWaitingPhrase();
/* 1321 */     PdfLine line = ct.bidiLine.processLine(0.0F, 20000.0F, 0, runDirection, arabicOptions, 0.0F, 0.0F, 0.0F);
/* 1322 */     if (line == null) {
/* 1323 */       return 0.0F;
/*      */     }
/* 1325 */     return 20000.0F - line.widthLeft();
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
/*      */   public static float getWidth(Phrase phrase) {
/* 1337 */     return getWidth(phrase, 1, 0);
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
/*      */   public static void showTextAligned(PdfContentByte canvas, int alignment, Phrase phrase, float x, float y, float rotation, int runDirection, int arabicOptions) {
/*      */     float llx, urx;
/* 1353 */     if (alignment != 0 && alignment != 1 && alignment != 2)
/*      */     {
/* 1355 */       alignment = 0;
/*      */     }
/* 1357 */     canvas.saveState();
/* 1358 */     ColumnText ct = new ColumnText(canvas);
/* 1359 */     float lly = -1.0F;
/* 1360 */     float ury = 2.0F;
/*      */ 
/*      */     
/* 1363 */     switch (alignment) {
/*      */       case 0:
/* 1365 */         llx = 0.0F;
/* 1366 */         urx = 20000.0F;
/*      */         break;
/*      */       case 2:
/* 1369 */         llx = -20000.0F;
/* 1370 */         urx = 0.0F;
/*      */         break;
/*      */       default:
/* 1373 */         llx = -20000.0F;
/* 1374 */         urx = 20000.0F;
/*      */         break;
/*      */     } 
/* 1377 */     if (rotation == 0.0F) {
/* 1378 */       llx += x;
/* 1379 */       lly += y;
/* 1380 */       urx += x;
/* 1381 */       ury += y;
/*      */     } else {
/* 1383 */       double alpha = rotation * Math.PI / 180.0D;
/* 1384 */       float cos = (float)Math.cos(alpha);
/* 1385 */       float sin = (float)Math.sin(alpha);
/* 1386 */       canvas.concatCTM(cos, sin, -sin, cos, x, y);
/*      */     } 
/* 1388 */     ct.setSimpleColumn(phrase, llx, lly, urx, ury, 2.0F, alignment);
/* 1389 */     if (runDirection == 3) {
/* 1390 */       if (alignment == 0) {
/* 1391 */         alignment = 2;
/* 1392 */       } else if (alignment == 2) {
/* 1393 */         alignment = 0;
/*      */       } 
/*      */     }
/* 1396 */     ct.setAlignment(alignment);
/* 1397 */     ct.setArabicOptions(arabicOptions);
/* 1398 */     ct.setRunDirection(runDirection);
/*      */     try {
/* 1400 */       ct.go();
/* 1401 */     } catch (DocumentException e) {
/* 1402 */       throw new ExceptionConverter(e);
/*      */     } 
/* 1404 */     canvas.restoreState();
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
/*      */   public static void showTextAligned(PdfContentByte canvas, int alignment, Phrase phrase, float x, float y, float rotation) {
/* 1418 */     showTextAligned(canvas, alignment, phrase, x, y, rotation, 1, 0);
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
/*      */   public static float fitText(Font font, String text, Rectangle rect, float maxFontSize, int runDirection) {
/*      */     try {
/* 1433 */       ColumnText ct = null;
/* 1434 */       int status = 0;
/* 1435 */       if (maxFontSize <= 0.0F) {
/* 1436 */         int cr = 0;
/* 1437 */         int lf = 0;
/* 1438 */         char[] t = text.toCharArray();
/* 1439 */         for (int i = 0; i < t.length; i++) {
/* 1440 */           if (t[i] == '\n') {
/* 1441 */             lf++;
/* 1442 */           } else if (t[i] == '\r') {
/* 1443 */             cr++;
/*      */           } 
/*      */         } 
/* 1446 */         int minLines = Math.max(cr, lf) + 1;
/* 1447 */         maxFontSize = Math.abs(rect.getHeight()) / minLines - 0.001F;
/*      */       } 
/* 1449 */       font.setSize(maxFontSize);
/* 1450 */       Phrase ph = new Phrase(text, font);
/* 1451 */       ct = new ColumnText(null);
/* 1452 */       ct.setSimpleColumn(ph, rect.getLeft(), rect.getBottom(), rect.getRight(), rect.getTop(), maxFontSize, 0);
/* 1453 */       ct.setRunDirection(runDirection);
/* 1454 */       status = ct.go(true);
/* 1455 */       if ((status & 0x1) != 0) {
/* 1456 */         return maxFontSize;
/*      */       }
/* 1458 */       float precision = 0.1F;
/* 1459 */       float min = 0.0F;
/* 1460 */       float max = maxFontSize;
/* 1461 */       float size = maxFontSize;
/* 1462 */       for (int k = 0; k < 50; k++) {
/* 1463 */         size = (min + max) / 2.0F;
/* 1464 */         ct = new ColumnText(null);
/* 1465 */         font.setSize(size);
/* 1466 */         ct.setSimpleColumn(new Phrase(text, font), rect.getLeft(), rect.getBottom(), rect.getRight(), rect.getTop(), size, 0);
/* 1467 */         ct.setRunDirection(runDirection);
/* 1468 */         status = ct.go(true);
/* 1469 */         if ((status & 0x1) != 0) {
/* 1470 */           if (max - min < size * precision) {
/* 1471 */             return size;
/*      */           }
/* 1473 */           min = size;
/*      */         } else {
/* 1475 */           max = size;
/*      */         } 
/*      */       } 
/* 1478 */       return size;
/* 1479 */     } catch (Exception e) {
/* 1480 */       throw new ExceptionConverter(e);
/*      */     } 
/*      */   }
/*      */   
/*      */   protected int goComposite(boolean simulate) throws DocumentException {
/* 1485 */     PdfDocument pdf = null;
/* 1486 */     if (this.canvas != null) {
/* 1487 */       pdf = this.canvas.pdf;
/*      */     }
/* 1489 */     if (!this.rectangularMode) {
/* 1490 */       throw new DocumentException(MessageLocalization.getComposedMessage("irregular.columns.are.not.supported.in.composite.mode", new Object[0]));
/*      */     }
/* 1492 */     this.linesWritten = 0;
/* 1493 */     this.descender = 0.0F;
/* 1494 */     boolean firstPass = true;
/* 1495 */     boolean isRTL = (this.runDirection == 3);
/*      */     while (true) {
/* 1497 */       if (this.compositeElements.isEmpty()) {
/* 1498 */         return 1;
/*      */       }
/* 1500 */       Element element = this.compositeElements.getFirst();
/* 1501 */       if (element.type() == 12) {
/* 1502 */         Paragraph para = (Paragraph)element;
/* 1503 */         int status = 0;
/* 1504 */         for (int keep = 0; keep < 2; keep++) {
/* 1505 */           float lastY = this.yLine;
/* 1506 */           boolean createHere = false;
/* 1507 */           if (this.compositeColumn == null) {
/* 1508 */             this.compositeColumn = new ColumnText(this.canvas);
/* 1509 */             this.compositeColumn.setAlignment(para.getAlignment());
/* 1510 */             this.compositeColumn.setIndent(para.getIndentationLeft() + para.getFirstLineIndent(), false);
/* 1511 */             this.compositeColumn.setExtraParagraphSpace(para.getExtraParagraphSpace());
/* 1512 */             this.compositeColumn.setFollowingIndent(para.getIndentationLeft());
/* 1513 */             this.compositeColumn.setRightIndent(para.getIndentationRight());
/* 1514 */             this.compositeColumn.setLeading(para.getLeading(), para.getMultipliedLeading());
/* 1515 */             this.compositeColumn.setRunDirection(this.runDirection);
/* 1516 */             this.compositeColumn.setArabicOptions(this.arabicOptions);
/* 1517 */             this.compositeColumn.setSpaceCharRatio(this.spaceCharRatio);
/* 1518 */             this.compositeColumn.addText((Phrase)para);
/* 1519 */             if (!firstPass || !this.adjustFirstLine) {
/* 1520 */               this.yLine -= para.getSpacingBefore();
/*      */             }
/* 1522 */             createHere = true;
/*      */           } 
/* 1524 */           this.compositeColumn.setUseAscender(((firstPass || this.descender == 0.0F) && this.adjustFirstLine) ? this.useAscender : false);
/* 1525 */           this.compositeColumn.setInheritGraphicState(this.inheritGraphicState);
/* 1526 */           this.compositeColumn.leftX = this.leftX;
/* 1527 */           this.compositeColumn.rightX = this.rightX;
/* 1528 */           this.compositeColumn.yLine = this.yLine;
/* 1529 */           this.compositeColumn.rectangularWidth = this.rectangularWidth;
/* 1530 */           this.compositeColumn.rectangularMode = this.rectangularMode;
/* 1531 */           this.compositeColumn.minY = this.minY;
/* 1532 */           this.compositeColumn.maxY = this.maxY;
/* 1533 */           boolean keepCandidate = (para.getKeepTogether() && createHere && (!firstPass || !this.adjustFirstLine));
/* 1534 */           boolean s = (simulate || (keepCandidate && keep == 0));
/* 1535 */           if (isTagged(this.canvas) && !s) {
/* 1536 */             this.canvas.openMCBlock((IAccessibleElement)para);
/*      */           }
/* 1538 */           status = this.compositeColumn.go(s);
/* 1539 */           if (isTagged(this.canvas) && !s) {
/* 1540 */             this.canvas.closeMCBlock((IAccessibleElement)para);
/*      */           }
/* 1542 */           this.lastX = this.compositeColumn.getLastX();
/* 1543 */           updateFilledWidth(this.compositeColumn.filledWidth);
/* 1544 */           if ((status & 0x1) == 0 && keepCandidate) {
/* 1545 */             this.compositeColumn = null;
/* 1546 */             this.yLine = lastY;
/* 1547 */             return 2;
/*      */           } 
/* 1549 */           if (simulate || !keepCandidate) {
/*      */             break;
/*      */           }
/* 1552 */           if (keep == 0) {
/* 1553 */             this.compositeColumn = null;
/* 1554 */             this.yLine = lastY;
/*      */           } 
/*      */         } 
/* 1557 */         firstPass = false;
/* 1558 */         if (this.compositeColumn.getLinesWritten() > 0) {
/* 1559 */           this.yLine = this.compositeColumn.yLine;
/* 1560 */           this.linesWritten += this.compositeColumn.linesWritten;
/* 1561 */           this.descender = this.compositeColumn.descender;
/* 1562 */           this.isWordSplit |= this.compositeColumn.isWordSplit();
/*      */         } 
/* 1564 */         this.currentLeading = this.compositeColumn.currentLeading;
/* 1565 */         if ((status & 0x1) != 0) {
/* 1566 */           this.compositeColumn = null;
/* 1567 */           this.compositeElements.removeFirst();
/* 1568 */           this.yLine -= para.getSpacingAfter();
/*      */         } 
/* 1570 */         if ((status & 0x2) != 0)
/* 1571 */           return 2;  continue;
/*      */       } 
/* 1573 */       if (element.type() == 14) {
/* 1574 */         List list = (List)element;
/* 1575 */         ArrayList<Element> items = list.getItems();
/* 1576 */         ListItem item = null;
/* 1577 */         float listIndentation = list.getIndentationLeft();
/* 1578 */         int count = 0;
/* 1579 */         Stack<Object[]> stack = new Stack();
/* 1580 */         for (int k = 0; k < items.size(); k++) {
/* 1581 */           Object obj = items.get(k);
/* 1582 */           if (obj instanceof ListItem) {
/* 1583 */             if (count == this.listIdx) {
/* 1584 */               item = (ListItem)obj;
/*      */               break;
/*      */             } 
/* 1587 */             count++;
/*      */           }
/* 1589 */           else if (obj instanceof List) {
/* 1590 */             stack.push(new Object[] { list, Integer.valueOf(k), new Float(listIndentation) });
/* 1591 */             list = (List)obj;
/* 1592 */             items = list.getItems();
/* 1593 */             listIndentation += list.getIndentationLeft();
/* 1594 */             k = -1;
/*      */             continue;
/*      */           } 
/* 1597 */           while (k == items.size() - 1 && !stack.isEmpty()) {
/* 1598 */             Object[] objs = stack.pop();
/* 1599 */             list = (List)objs[0];
/* 1600 */             items = list.getItems();
/* 1601 */             k = ((Integer)objs[1]).intValue();
/* 1602 */             listIndentation = ((Float)objs[2]).floatValue();
/*      */           }  continue;
/*      */         } 
/* 1605 */         int status = 0;
/* 1606 */         boolean keepTogetherAndDontFit = false;
/* 1607 */         for (int keep = 0; keep < 2; keep++) {
/* 1608 */           float lastY = this.yLine;
/* 1609 */           boolean createHere = false;
/* 1610 */           if (this.compositeColumn == null) {
/* 1611 */             if (item == null) {
/* 1612 */               this.listIdx = 0;
/* 1613 */               this.compositeElements.removeFirst();
/*      */               break;
/*      */             } 
/* 1616 */             this.compositeColumn = new ColumnText(this.canvas);
/* 1617 */             this.compositeColumn.setUseAscender(((firstPass || this.descender == 0.0F) && this.adjustFirstLine) ? this.useAscender : false);
/* 1618 */             this.compositeColumn.setInheritGraphicState(this.inheritGraphicState);
/* 1619 */             this.compositeColumn.setAlignment(item.getAlignment());
/* 1620 */             this.compositeColumn.setIndent(item.getIndentationLeft() + listIndentation + item.getFirstLineIndent(), false);
/* 1621 */             this.compositeColumn.setExtraParagraphSpace(item.getExtraParagraphSpace());
/* 1622 */             this.compositeColumn.setFollowingIndent(this.compositeColumn.getIndent());
/* 1623 */             this.compositeColumn.setRightIndent(item.getIndentationRight() + list.getIndentationRight());
/* 1624 */             this.compositeColumn.setLeading(item.getLeading(), item.getMultipliedLeading());
/* 1625 */             this.compositeColumn.setRunDirection(this.runDirection);
/* 1626 */             this.compositeColumn.setArabicOptions(this.arabicOptions);
/* 1627 */             this.compositeColumn.setSpaceCharRatio(this.spaceCharRatio);
/* 1628 */             this.compositeColumn.addText((Phrase)item);
/* 1629 */             if (!firstPass || !this.adjustFirstLine) {
/* 1630 */               this.yLine -= item.getSpacingBefore();
/*      */             }
/* 1632 */             createHere = true;
/*      */           } 
/* 1634 */           this.compositeColumn.leftX = this.leftX;
/* 1635 */           this.compositeColumn.rightX = this.rightX;
/* 1636 */           this.compositeColumn.yLine = this.yLine;
/* 1637 */           this.compositeColumn.rectangularWidth = this.rectangularWidth;
/* 1638 */           this.compositeColumn.rectangularMode = this.rectangularMode;
/* 1639 */           this.compositeColumn.minY = this.minY;
/* 1640 */           this.compositeColumn.maxY = this.maxY;
/* 1641 */           boolean keepCandidate = (item.getKeepTogether() && createHere && (!firstPass || !this.adjustFirstLine));
/* 1642 */           boolean s = (simulate || (keepCandidate && keep == 0));
/* 1643 */           if (isTagged(this.canvas) && !s) {
/* 1644 */             item.getListLabel().setIndentation(listIndentation);
/* 1645 */             if (list.getFirstItem() == item || (this.compositeColumn != null && this.compositeColumn.bidiLine != null)) {
/* 1646 */               this.canvas.openMCBlock((IAccessibleElement)list);
/*      */             }
/* 1648 */             this.canvas.openMCBlock((IAccessibleElement)item);
/*      */           } 
/* 1650 */           status = this.compositeColumn.go(s, (IAccessibleElement)item);
/* 1651 */           if (isTagged(this.canvas) && !s) {
/* 1652 */             this.canvas.closeMCBlock((IAccessibleElement)item.getListBody());
/* 1653 */             this.canvas.closeMCBlock((IAccessibleElement)item);
/*      */           } 
/* 1655 */           this.lastX = this.compositeColumn.getLastX();
/* 1656 */           updateFilledWidth(this.compositeColumn.filledWidth);
/* 1657 */           if ((status & 0x1) == 0 && keepCandidate) {
/* 1658 */             keepTogetherAndDontFit = true;
/* 1659 */             this.compositeColumn = null;
/* 1660 */             this.yLine = lastY;
/*      */           } 
/* 1662 */           if (simulate || !keepCandidate || keepTogetherAndDontFit) {
/*      */             break;
/*      */           }
/* 1665 */           if (keep == 0) {
/* 1666 */             this.compositeColumn = null;
/* 1667 */             this.yLine = lastY;
/*      */           } 
/*      */         } 
/*      */         
/* 1671 */         if (isTagged(this.canvas) && !simulate && (
/* 1672 */           item == null || (list.getLastItem() == item && (status & 0x1) != 0) || (status & 0x2) != 0)) {
/* 1673 */           this.canvas.closeMCBlock((IAccessibleElement)list);
/*      */         }
/*      */         
/* 1676 */         if (keepTogetherAndDontFit) {
/* 1677 */           return 2;
/*      */         }
/* 1679 */         if (item == null) {
/*      */           continue;
/*      */         }
/*      */         
/* 1683 */         firstPass = false;
/* 1684 */         this.yLine = this.compositeColumn.yLine;
/* 1685 */         this.linesWritten += this.compositeColumn.linesWritten;
/* 1686 */         this.descender = this.compositeColumn.descender;
/* 1687 */         this.currentLeading = this.compositeColumn.currentLeading;
/* 1688 */         if (!isTagged(this.canvas) && 
/* 1689 */           !Float.isNaN(this.compositeColumn.firstLineY) && !this.compositeColumn.firstLineYDone) {
/* 1690 */           if (!simulate) {
/* 1691 */             if (isRTL) {
/* 1692 */               showTextAligned(this.canvas, 2, new Phrase(item.getListSymbol()), this.compositeColumn.lastX + item.getIndentationLeft(), this.compositeColumn.firstLineY, 0.0F, this.runDirection, this.arabicOptions);
/*      */             } else {
/* 1694 */               showTextAligned(this.canvas, 0, new Phrase(item.getListSymbol()), this.compositeColumn.leftX + listIndentation, this.compositeColumn.firstLineY, 0.0F);
/*      */             } 
/*      */           }
/*      */           
/* 1698 */           this.compositeColumn.firstLineYDone = true;
/*      */         } 
/*      */         
/* 1701 */         if ((status & 0x1) != 0) {
/* 1702 */           this.compositeColumn = null;
/* 1703 */           this.listIdx++;
/* 1704 */           this.yLine -= item.getSpacingAfter();
/*      */         } 
/* 1706 */         if ((status & 0x2) != 0)
/* 1707 */           return 2;  continue;
/*      */       } 
/* 1709 */       if (element.type() == 23) {
/*      */         float tableWidth;
/*      */ 
/*      */         
/* 1713 */         PdfPTable table = (PdfPTable)element;
/*      */         
/* 1715 */         int backedUpRunDir = this.runDirection;
/* 1716 */         this.runDirection = table.getRunDirection();
/* 1717 */         isRTL = (this.runDirection == 3);
/*      */ 
/*      */         
/* 1720 */         if (table.size() <= table.getHeaderRows()) {
/* 1721 */           this.compositeElements.removeFirst();
/*      */           
/*      */           continue;
/*      */         } 
/*      */         
/* 1726 */         float yTemp = this.yLine;
/* 1727 */         yTemp += this.descender;
/* 1728 */         if (this.rowIdx == 0 && this.adjustFirstLine) {
/* 1729 */           yTemp -= table.spacingBefore();
/*      */         }
/*      */ 
/*      */         
/* 1733 */         if (yTemp < this.minY || yTemp > this.maxY) {
/* 1734 */           return 2;
/*      */         }
/*      */ 
/*      */         
/* 1738 */         float yLineWrite = yTemp;
/* 1739 */         float x1 = this.leftX;
/* 1740 */         this.currentLeading = 0.0F;
/*      */ 
/*      */         
/* 1743 */         if (table.isLockedWidth()) {
/* 1744 */           tableWidth = table.getTotalWidth();
/* 1745 */           updateFilledWidth(tableWidth);
/*      */         } else {
/* 1747 */           tableWidth = this.rectangularWidth * table.getWidthPercentage() / 100.0F;
/* 1748 */           table.setTotalWidth(tableWidth);
/*      */         } 
/*      */ 
/*      */ 
/*      */         
/* 1753 */         table.normalizeHeadersFooters();
/* 1754 */         int headerRows = table.getHeaderRows();
/* 1755 */         int footerRows = table.getFooterRows();
/* 1756 */         int realHeaderRows = headerRows - footerRows;
/* 1757 */         float footerHeight = table.getFooterHeight();
/* 1758 */         float headerHeight = table.getHeaderHeight() - footerHeight;
/*      */ 
/*      */         
/* 1761 */         boolean skipHeader = (table.isSkipFirstHeader() && this.rowIdx <= realHeaderRows && (table.isComplete() || this.rowIdx != realHeaderRows));
/*      */         
/* 1763 */         if (!skipHeader) {
/* 1764 */           yTemp -= headerHeight;
/*      */         }
/*      */ 
/*      */ 
/*      */         
/* 1769 */         int k = 0;
/* 1770 */         if (this.rowIdx < headerRows) {
/* 1771 */           this.rowIdx = headerRows;
/*      */         }
/* 1773 */         PdfPTable.FittingRows fittingRows = null;
/*      */         
/* 1775 */         if (table.isSkipLastFooter())
/*      */         {
/* 1777 */           fittingRows = table.getFittingRows(yTemp - this.minY, this.rowIdx);
/*      */         }
/*      */ 
/*      */         
/* 1781 */         if (!table.isSkipLastFooter() || fittingRows.lastRow < table.size() - 1) {
/* 1782 */           yTemp -= footerHeight;
/* 1783 */           fittingRows = table.getFittingRows(yTemp - this.minY, this.rowIdx);
/*      */         } 
/*      */ 
/*      */         
/* 1787 */         if (yTemp < this.minY || yTemp > this.maxY) {
/* 1788 */           return 2;
/*      */         }
/*      */ 
/*      */         
/* 1792 */         k = fittingRows.lastRow + 1;
/* 1793 */         yTemp -= fittingRows.height;
/*      */ 
/*      */         
/* 1796 */         this.LOGGER.info("Want to split at row " + k);
/* 1797 */         int kTemp = k;
/* 1798 */         while (kTemp > this.rowIdx && kTemp < table.size() && table.getRow(kTemp).isMayNotBreak()) {
/* 1799 */           kTemp--;
/*      */         }
/*      */         
/* 1802 */         if (kTemp < table.size() - 1 && !table.getRow(kTemp).isMayNotBreak()) {
/* 1803 */           kTemp++;
/*      */         }
/*      */         
/* 1806 */         if ((kTemp > this.rowIdx && kTemp < k) || (kTemp == headerRows && table.getRow(headerRows).isMayNotBreak() && table.isLoopCheck())) {
/* 1807 */           yTemp = this.minY;
/* 1808 */           k = kTemp;
/* 1809 */           table.setLoopCheck(false);
/*      */         } 
/* 1811 */         this.LOGGER.info("Will split at row " + k);
/*      */ 
/*      */         
/* 1814 */         if (table.isSplitLate() && k > 0) {
/* 1815 */           fittingRows.correctLastRowChosen(table, k - 1);
/*      */         }
/*      */ 
/*      */ 
/*      */         
/* 1820 */         if (!table.isComplete()) {
/* 1821 */           yTemp += footerHeight;
/*      */         }
/*      */ 
/*      */         
/* 1825 */         if (!table.isSplitRows()) {
/* 1826 */           if (this.splittedRow != -1) {
/* 1827 */             this.splittedRow = -1;
/*      */           }
/* 1829 */           else if (k == this.rowIdx) {
/*      */             
/* 1831 */             if (k == table.size()) {
/* 1832 */               this.compositeElements.removeFirst();
/*      */ 
/*      */               
/*      */               continue;
/*      */             } 
/*      */             
/* 1838 */             if (table.isComplete() || k != 1) {
/* 1839 */               table.getRows().remove(k);
/*      */             }
/* 1841 */             return 2;
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*      */           }
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         }
/* 1852 */         else if (table.isSplitLate() && (this.rowIdx < k || (this.splittedRow == -2 && (table
/* 1853 */           .getHeaderRows() == 0 || table.isSkipFirstHeader())))) {
/* 1854 */           this.splittedRow = -1;
/*      */         }
/* 1856 */         else if (k < table.size()) {
/*      */ 
/*      */ 
/*      */           
/* 1860 */           yTemp -= fittingRows.completedRowsHeight - fittingRows.height;
/*      */ 
/*      */           
/* 1863 */           float h = yTemp - this.minY;
/*      */           
/* 1865 */           PdfPRow newRow = table.getRow(k).splitRow(table, k, h);
/*      */           
/* 1867 */           if (newRow == null) {
/* 1868 */             this.LOGGER.info("Didn't split row!");
/* 1869 */             this.splittedRow = -1;
/* 1870 */             if (this.rowIdx == k) {
/* 1871 */               return 2;
/*      */             }
/*      */           } else {
/*      */             
/* 1875 */             if (k != this.splittedRow) {
/* 1876 */               this.splittedRow = k + 1;
/* 1877 */               table = new PdfPTable(table);
/* 1878 */               this.compositeElements.set(0, table);
/* 1879 */               ArrayList<PdfPRow> rows = table.getRows();
/* 1880 */               for (int i = headerRows; i < this.rowIdx; i++) {
/* 1881 */                 rows.set(i, null);
/*      */               }
/*      */             } 
/* 1884 */             yTemp = this.minY;
/* 1885 */             table.getRows().add(++k, newRow);
/* 1886 */             this.LOGGER.info("Inserting row at position " + k);
/*      */           } 
/*      */         } 
/*      */ 
/*      */         
/* 1891 */         firstPass = false;
/*      */ 
/*      */         
/* 1894 */         if (!simulate) {
/*      */           
/* 1896 */           switch (table.getHorizontalAlignment()) {
/*      */             case 2:
/* 1898 */               if (!isRTL) {
/* 1899 */                 x1 += this.rectangularWidth - tableWidth;
/*      */               }
/*      */               break;
/*      */             case 1:
/* 1903 */               x1 += (this.rectangularWidth - tableWidth) / 2.0F;
/*      */               break;
/*      */             
/*      */             default:
/* 1907 */               if (isRTL) {
/* 1908 */                 x1 += this.rectangularWidth - tableWidth;
/*      */               }
/*      */               break;
/*      */           } 
/*      */           
/* 1913 */           PdfPTable nt = PdfPTable.shallowCopy(table);
/* 1914 */           ArrayList<PdfPRow> sub = nt.getRows();
/*      */           
/* 1916 */           if (!skipHeader && realHeaderRows > 0) {
/* 1917 */             ArrayList<PdfPRow> arrayList = table.getRows(0, realHeaderRows);
/* 1918 */             if (isTagged(this.canvas)) {
/* 1919 */               (nt.getHeader()).rows = arrayList;
/*      */             }
/* 1921 */             sub.addAll(arrayList);
/*      */           } else {
/* 1923 */             nt.setHeaderRows(footerRows);
/*      */           } 
/*      */ 
/*      */ 
/*      */           
/* 1928 */           ArrayList<PdfPRow> rows = table.getRows(this.rowIdx, k);
/* 1929 */           if (isTagged(this.canvas)) {
/* 1930 */             (nt.getBody()).rows = rows;
/*      */           }
/* 1932 */           sub.addAll(rows);
/*      */ 
/*      */           
/* 1935 */           boolean showFooter = !table.isSkipLastFooter();
/* 1936 */           boolean newPageFollows = false;
/* 1937 */           if (k < table.size()) {
/* 1938 */             nt.setComplete(true);
/* 1939 */             showFooter = true;
/* 1940 */             newPageFollows = true;
/*      */           } 
/*      */           
/* 1943 */           if (footerRows > 0 && nt.isComplete() && showFooter) {
/* 1944 */             ArrayList<PdfPRow> arrayList = table.getRows(realHeaderRows, realHeaderRows + footerRows);
/* 1945 */             if (isTagged(this.canvas)) {
/* 1946 */               (nt.getFooter()).rows = arrayList;
/*      */             }
/* 1948 */             sub.addAll(arrayList);
/*      */           } else {
/* 1950 */             footerRows = 0;
/*      */           } 
/*      */           
/* 1953 */           if (sub.size() - footerRows > 0) {
/*      */             
/* 1955 */             float rowHeight = 0.0F;
/* 1956 */             int lastIdx = sub.size() - 1 - footerRows;
/* 1957 */             PdfPRow last = sub.get(lastIdx);
/* 1958 */             if (table.isExtendLastRow(newPageFollows)) {
/* 1959 */               rowHeight = last.getMaxHeights();
/* 1960 */               last.setMaxHeights(yTemp - this.minY + rowHeight);
/* 1961 */               yTemp = this.minY;
/*      */             } 
/*      */ 
/*      */ 
/*      */             
/* 1966 */             if (newPageFollows) {
/* 1967 */               PdfPTableEvent tableEvent = table.getTableEvent();
/* 1968 */               if (tableEvent instanceof PdfPTableEventSplit) {
/* 1969 */                 ((PdfPTableEventSplit)tableEvent).splitTable(table);
/*      */               }
/*      */             } 
/*      */ 
/*      */             
/* 1974 */             if (this.canvases != null) {
/* 1975 */               if (isTagged(this.canvases[3])) {
/* 1976 */                 this.canvases[3].openMCBlock(table);
/*      */               }
/* 1978 */               nt.writeSelectedRows(0, -1, 0, -1, x1, yLineWrite, this.canvases, false);
/* 1979 */               if (isTagged(this.canvases[3])) {
/* 1980 */                 this.canvases[3].closeMCBlock(table);
/*      */               }
/*      */             } else {
/* 1983 */               if (isTagged(this.canvas)) {
/* 1984 */                 this.canvas.openMCBlock(table);
/*      */               }
/* 1986 */               nt.writeSelectedRows(0, -1, 0, -1, x1, yLineWrite, this.canvas, false);
/* 1987 */               if (isTagged(this.canvas)) {
/* 1988 */                 this.canvas.closeMCBlock(table);
/*      */               }
/*      */             } 
/*      */             
/* 1992 */             if (!table.isComplete()) {
/* 1993 */               table.addNumberOfRowsWritten(k);
/*      */             }
/*      */ 
/*      */ 
/*      */             
/* 1998 */             if (this.splittedRow == k && k < table.size()) {
/* 1999 */               PdfPRow splitted = table.getRows().get(k);
/* 2000 */               splitted.copyRowContent(nt, lastIdx);
/*      */             }
/* 2002 */             else if (k > 0 && k < table.size()) {
/*      */ 
/*      */               
/* 2005 */               PdfPRow row = table.getRow(k);
/* 2006 */               row.splitRowspans(table, k - 1, nt, lastIdx);
/*      */             } 
/*      */ 
/*      */ 
/*      */             
/* 2011 */             if (table.isExtendLastRow(newPageFollows)) {
/* 2012 */               last.setMaxHeights(rowHeight);
/*      */             }
/*      */ 
/*      */ 
/*      */             
/* 2017 */             if (newPageFollows) {
/* 2018 */               PdfPTableEvent tableEvent = table.getTableEvent();
/* 2019 */               if (tableEvent instanceof PdfPTableEventAfterSplit) {
/* 2020 */                 PdfPRow row = table.getRow(k);
/* 2021 */                 ((PdfPTableEventAfterSplit)tableEvent).afterSplitTable(table, row, k);
/*      */               }
/*      */             
/*      */             } 
/*      */           } 
/* 2026 */         } else if (table.isExtendLastRow() && this.minY > -1.07374182E9F) {
/* 2027 */           yTemp = this.minY;
/*      */         } 
/*      */         
/* 2030 */         this.yLine = yTemp;
/* 2031 */         this.descender = 0.0F;
/* 2032 */         this.currentLeading = 0.0F;
/* 2033 */         if (!skipHeader && !table.isComplete()) {
/* 2034 */           this.yLine += footerHeight;
/*      */         }
/* 2036 */         while (k < table.size() && 
/* 2037 */           table.getRowHeight(k) <= 0.0F && !table.hasRowspan(k))
/*      */         {
/*      */           
/* 2040 */           k++;
/*      */         }
/* 2042 */         if (k >= table.size()) {
/*      */           
/* 2044 */           if (this.yLine - table.spacingAfter() < this.minY) {
/* 2045 */             this.yLine = this.minY;
/*      */           } else {
/* 2047 */             this.yLine -= table.spacingAfter();
/*      */           } 
/* 2049 */           this.compositeElements.removeFirst();
/* 2050 */           this.splittedRow = -1;
/* 2051 */           this.rowIdx = 0;
/*      */         } else {
/* 2053 */           if (this.splittedRow > -1) {
/* 2054 */             ArrayList<PdfPRow> rows = table.getRows();
/* 2055 */             for (int i = this.rowIdx; i < k; i++) {
/* 2056 */               rows.set(i, null);
/*      */             }
/*      */           } 
/* 2059 */           this.rowIdx = k;
/* 2060 */           return 2;
/*      */         } 
/*      */ 
/*      */         
/* 2064 */         this.runDirection = backedUpRunDir;
/* 2065 */         isRTL = (this.runDirection == 3); continue;
/* 2066 */       }  if (element.type() == 55) {
/* 2067 */         if (!simulate) {
/* 2068 */           DrawInterface zh = (DrawInterface)element;
/* 2069 */           zh.draw(this.canvas, this.leftX, this.minY, this.rightX, this.maxY, this.yLine);
/*      */         } 
/* 2071 */         this.compositeElements.removeFirst(); continue;
/* 2072 */       }  if (element.type() == 37) {
/* 2073 */         ArrayList<Element> floatingElements = new ArrayList<Element>();
/*      */         do {
/* 2075 */           floatingElements.add(element);
/* 2076 */           this.compositeElements.removeFirst();
/* 2077 */           element = !this.compositeElements.isEmpty() ? this.compositeElements.getFirst() : null;
/* 2078 */         } while (element != null && element.type() == 37);
/*      */         
/* 2080 */         FloatLayout fl = new FloatLayout(floatingElements, this.useAscender);
/* 2081 */         fl.setSimpleColumn(this.leftX, this.minY, this.rightX, this.yLine);
/* 2082 */         fl.compositeColumn.setIgnoreSpacingBefore(isIgnoreSpacingBefore());
/* 2083 */         int status = fl.layout(this.canvas, simulate);
/*      */ 
/*      */         
/* 2086 */         this.yLine = fl.getYLine();
/* 2087 */         this.descender = 0.0F;
/* 2088 */         if ((status & 0x1) == 0) {
/* 2089 */           this.compositeElements.addAll(floatingElements);
/* 2090 */           return status;
/*      */         }  continue;
/*      */       } 
/* 2093 */       this.compositeElements.removeFirst();
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
/*      */   public PdfContentByte getCanvas() {
/* 2105 */     return this.canvas;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setCanvas(PdfContentByte canvas) {
/* 2115 */     this.canvas = canvas;
/* 2116 */     this.canvases = null;
/* 2117 */     if (this.compositeColumn != null) {
/* 2118 */       this.compositeColumn.setCanvas(canvas);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setCanvases(PdfContentByte[] canvases) {
/* 2128 */     this.canvases = canvases;
/* 2129 */     this.canvas = canvases[3];
/* 2130 */     if (this.compositeColumn != null) {
/* 2131 */       this.compositeColumn.setCanvases(canvases);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PdfContentByte[] getCanvases() {
/* 2141 */     return this.canvases;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean zeroHeightElement() {
/* 2151 */     return (this.composite && !this.compositeElements.isEmpty() && ((Element)this.compositeElements.getFirst()).type() == 55);
/*      */   }
/*      */   
/*      */   public List<Element> getCompositeElements() {
/* 2155 */     return this.compositeElements;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isUseAscender() {
/* 2165 */     return this.useAscender;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setUseAscender(boolean useAscender) {
/* 2174 */     this.useAscender = useAscender;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean hasMoreText(int status) {
/* 2181 */     return ((status & 0x1) == 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getFilledWidth() {
/* 2190 */     return this.filledWidth;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setFilledWidth(float filledWidth) {
/* 2200 */     this.filledWidth = filledWidth;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void updateFilledWidth(float w) {
/* 2210 */     if (w > this.filledWidth) {
/* 2211 */       this.filledWidth = w;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isAdjustFirstLine() {
/* 2221 */     return this.adjustFirstLine;
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
/*      */   public void setAdjustFirstLine(boolean adjustFirstLine) {
/* 2235 */     this.adjustFirstLine = adjustFirstLine;
/*      */   }
/*      */   
/*      */   private static boolean isTagged(PdfContentByte canvas) {
/* 2239 */     return (canvas != null && canvas.pdf != null && canvas.writer != null && canvas.writer.isTagged());
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/ColumnText.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */