/*      */ package com.itextpdf.text.pdf;
/*      */ 
/*      */ import com.itextpdf.text.AccessibleElementId;
/*      */ import com.itextpdf.text.Chunk;
/*      */ import com.itextpdf.text.DocumentException;
/*      */ import com.itextpdf.text.Element;
/*      */ import com.itextpdf.text.ExceptionConverter;
/*      */ import com.itextpdf.text.Image;
/*      */ import com.itextpdf.text.Phrase;
/*      */ import com.itextpdf.text.Rectangle;
/*      */ import com.itextpdf.text.error_messages.MessageLocalization;
/*      */ import com.itextpdf.text.pdf.events.PdfPCellEventForwarder;
/*      */ import com.itextpdf.text.pdf.interfaces.IAccessibleElement;
/*      */ import java.util.ArrayList;
/*      */ import java.util.HashMap;
/*      */ import java.util.List;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class PdfPCell
/*      */   extends Rectangle
/*      */   implements IAccessibleElement
/*      */ {
/*   60 */   private ColumnText column = new ColumnText(null);
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   65 */   private int verticalAlignment = 4;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   70 */   private float paddingLeft = 2.0F;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   75 */   private float paddingRight = 2.0F;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   80 */   private float paddingTop = 2.0F;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   85 */   private float paddingBottom = 2.0F;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   90 */   private float fixedHeight = 0.0F;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   95 */   private float calculatedHeight = 0.0F;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private float minimumHeight;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private float cachedMaxHeight;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean noWrap = false;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private PdfPTable table;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  121 */   private int colspan = 1;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  128 */   private int rowspan = 1;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Image image;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private PdfPCellEvent cellEvent;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean useDescender = false;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean useBorderPadding = false;
/*      */ 
/*      */ 
/*      */   
/*      */   protected Phrase phrase;
/*      */ 
/*      */ 
/*      */   
/*      */   private int rotation;
/*      */ 
/*      */ 
/*      */   
/*  160 */   protected PdfName role = PdfName.TD;
/*  161 */   protected HashMap<PdfName, PdfObject> accessibleAttributes = null;
/*  162 */   protected AccessibleElementId id = new AccessibleElementId();
/*      */   
/*  164 */   protected ArrayList<PdfPHeaderCell> headers = null;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PdfPCell() {
/*  170 */     super(0.0F, 0.0F, 0.0F, 0.0F);
/*  171 */     this.borderWidth = 0.5F;
/*  172 */     this.border = 15;
/*  173 */     this.column.setLeading(0.0F, 1.0F);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PdfPCell(Phrase phrase) {
/*  183 */     super(0.0F, 0.0F, 0.0F, 0.0F);
/*  184 */     this.borderWidth = 0.5F;
/*  185 */     this.border = 15;
/*  186 */     this.column.addText(this.phrase = phrase);
/*  187 */     this.column.setLeading(0.0F, 1.0F);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PdfPCell(Image image) {
/*  197 */     this(image, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PdfPCell(Image image, boolean fit) {
/*  208 */     super(0.0F, 0.0F, 0.0F, 0.0F);
/*  209 */     this.borderWidth = 0.5F;
/*  210 */     this.border = 15;
/*  211 */     this.column.setLeading(0.0F, 1.0F);
/*  212 */     if (fit) {
/*  213 */       this.image = image;
/*  214 */       setPadding(this.borderWidth / 2.0F);
/*      */     } else {
/*  216 */       image.setScaleToFitLineWhenOverflow(false);
/*  217 */       this.column.addText(this.phrase = new Phrase(new Chunk(image, 0.0F, 0.0F, true)));
/*  218 */       setPadding(0.0F);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PdfPCell(PdfPTable table) {
/*  229 */     this(table, (PdfPCell)null);
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
/*      */   public PdfPCell(PdfPTable table, PdfPCell style) {
/*  242 */     super(0.0F, 0.0F, 0.0F, 0.0F);
/*  243 */     this.borderWidth = 0.5F;
/*  244 */     this.border = 15;
/*  245 */     this.column.setLeading(0.0F, 1.0F);
/*  246 */     this.table = table;
/*  247 */     table.setWidthPercentage(100.0F);
/*  248 */     table.setExtendLastRow(true);
/*  249 */     this.column.addElement((Element)table);
/*  250 */     if (style != null) {
/*  251 */       cloneNonPositionParameters(style);
/*  252 */       this.verticalAlignment = style.verticalAlignment;
/*  253 */       this.paddingLeft = style.paddingLeft;
/*  254 */       this.paddingRight = style.paddingRight;
/*  255 */       this.paddingTop = style.paddingTop;
/*  256 */       this.paddingBottom = style.paddingBottom;
/*  257 */       this.colspan = style.colspan;
/*  258 */       this.rowspan = style.rowspan;
/*  259 */       this.cellEvent = style.cellEvent;
/*  260 */       this.useDescender = style.useDescender;
/*  261 */       this.useBorderPadding = style.useBorderPadding;
/*  262 */       this.rotation = style.rotation;
/*      */     } else {
/*  264 */       setPadding(0.0F);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PdfPCell(PdfPCell cell) {
/*  274 */     super(cell.llx, cell.lly, cell.urx, cell.ury);
/*  275 */     cloneNonPositionParameters(cell);
/*  276 */     this.verticalAlignment = cell.verticalAlignment;
/*  277 */     this.paddingLeft = cell.paddingLeft;
/*  278 */     this.paddingRight = cell.paddingRight;
/*  279 */     this.paddingTop = cell.paddingTop;
/*  280 */     this.paddingBottom = cell.paddingBottom;
/*  281 */     this.phrase = cell.phrase;
/*  282 */     this.fixedHeight = cell.fixedHeight;
/*  283 */     this.minimumHeight = cell.minimumHeight;
/*  284 */     this.noWrap = cell.noWrap;
/*  285 */     this.colspan = cell.colspan;
/*  286 */     this.rowspan = cell.rowspan;
/*  287 */     if (cell.table != null) {
/*  288 */       this.table = new PdfPTable(cell.table);
/*      */     }
/*  290 */     this.image = Image.getInstance(cell.image);
/*  291 */     this.cellEvent = cell.cellEvent;
/*  292 */     this.useDescender = cell.useDescender;
/*  293 */     this.column = ColumnText.duplicate(cell.column);
/*  294 */     this.useBorderPadding = cell.useBorderPadding;
/*  295 */     this.rotation = cell.rotation;
/*  296 */     this.id = cell.id;
/*  297 */     this.role = cell.role;
/*  298 */     if (cell.accessibleAttributes != null) {
/*  299 */       this.accessibleAttributes = new HashMap<PdfName, PdfObject>(cell.accessibleAttributes);
/*      */     }
/*  301 */     this.headers = cell.headers;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addElement(Element element) {
/*  310 */     if (this.table != null) {
/*  311 */       this.table = null;
/*  312 */       this.column.setText(null);
/*      */     } 
/*  314 */     if (element instanceof PdfPTable) {
/*  315 */       ((PdfPTable)element).setSplitLate(false);
/*  316 */     } else if (element instanceof PdfDiv) {
/*  317 */       for (Element divChildElement : ((PdfDiv)element).getContent()) {
/*  318 */         if (divChildElement instanceof PdfPTable) {
/*  319 */           ((PdfPTable)divChildElement).setSplitLate(false);
/*      */         }
/*      */       } 
/*      */     } 
/*  323 */     this.column.addElement(element);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Phrase getPhrase() {
/*  332 */     return this.phrase;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPhrase(Phrase phrase) {
/*  341 */     this.table = null;
/*  342 */     this.image = null;
/*  343 */     this.column.setText(this.phrase = phrase);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getHorizontalAlignment() {
/*  352 */     return this.column.getAlignment();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setHorizontalAlignment(int horizontalAlignment) {
/*  362 */     this.column.setAlignment(horizontalAlignment);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getVerticalAlignment() {
/*  371 */     return this.verticalAlignment;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setVerticalAlignment(int verticalAlignment) {
/*  381 */     if (this.table != null) {
/*  382 */       this.table.setExtendLastRow((verticalAlignment == 4));
/*      */     }
/*  384 */     this.verticalAlignment = verticalAlignment;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getEffectivePaddingLeft() {
/*  394 */     if (isUseBorderPadding()) {
/*  395 */       float border = getBorderWidthLeft() / (isUseVariableBorders() ? 1.0F : 2.0F);
/*  396 */       return this.paddingLeft + border;
/*      */     } 
/*  398 */     return this.paddingLeft;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getPaddingLeft() {
/*  405 */     return this.paddingLeft;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPaddingLeft(float paddingLeft) {
/*  414 */     this.paddingLeft = paddingLeft;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getEffectivePaddingRight() {
/*  424 */     if (isUseBorderPadding()) {
/*  425 */       float border = getBorderWidthRight() / (isUseVariableBorders() ? 1.0F : 2.0F);
/*  426 */       return this.paddingRight + border;
/*      */     } 
/*  428 */     return this.paddingRight;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getPaddingRight() {
/*  437 */     return this.paddingRight;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPaddingRight(float paddingRight) {
/*  446 */     this.paddingRight = paddingRight;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getEffectivePaddingTop() {
/*  456 */     if (isUseBorderPadding()) {
/*  457 */       float border = getBorderWidthTop() / (isUseVariableBorders() ? 1.0F : 2.0F);
/*  458 */       return this.paddingTop + border;
/*      */     } 
/*  460 */     return this.paddingTop;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getPaddingTop() {
/*  469 */     return this.paddingTop;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPaddingTop(float paddingTop) {
/*  478 */     this.paddingTop = paddingTop;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getEffectivePaddingBottom() {
/*  488 */     if (isUseBorderPadding()) {
/*  489 */       float border = getBorderWidthBottom() / (isUseVariableBorders() ? 1.0F : 2.0F);
/*  490 */       return this.paddingBottom + border;
/*      */     } 
/*  492 */     return this.paddingBottom;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getPaddingBottom() {
/*  501 */     return this.paddingBottom;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPaddingBottom(float paddingBottom) {
/*  510 */     this.paddingBottom = paddingBottom;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPadding(float padding) {
/*  520 */     this.paddingBottom = padding;
/*  521 */     this.paddingTop = padding;
/*  522 */     this.paddingLeft = padding;
/*  523 */     this.paddingRight = padding;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isUseBorderPadding() {
/*  532 */     return this.useBorderPadding;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setUseBorderPadding(boolean use) {
/*  541 */     this.useBorderPadding = use;
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
/*  553 */     this.column.setLeading(fixedLeading, multipliedLeading);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getLeading() {
/*  562 */     return this.column.getLeading();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getMultipliedLeading() {
/*  571 */     return this.column.getMultipliedLeading();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setIndent(float indent) {
/*  580 */     this.column.setIndent(indent);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getIndent() {
/*  589 */     return this.column.getIndent();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getExtraParagraphSpace() {
/*  598 */     return this.column.getExtraParagraphSpace();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setExtraParagraphSpace(float extraParagraphSpace) {
/*  607 */     this.column.setExtraParagraphSpace(extraParagraphSpace);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setCalculatedHeight(float calculatedHeight) {
/*  616 */     this.calculatedHeight = calculatedHeight;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getCalculatedHeight() {
/*  625 */     return this.calculatedHeight;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasCalculatedHeight() {
/*  634 */     return (getCalculatedHeight() > 0.0F);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setFixedHeight(float fixedHeight) {
/*  644 */     this.fixedHeight = fixedHeight;
/*  645 */     this.minimumHeight = 0.0F;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getFixedHeight() {
/*  654 */     return this.fixedHeight;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasFixedHeight() {
/*  664 */     return (getFixedHeight() > 0.0F);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getCachedMaxHeight() {
/*  673 */     return this.cachedMaxHeight;
/*      */   }
/*      */   
/*      */   public boolean hasCachedMaxHeight() {
/*  677 */     return (this.cachedMaxHeight > 0.0F);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setMinimumHeight(float minimumHeight) {
/*  687 */     this.minimumHeight = minimumHeight;
/*  688 */     this.fixedHeight = 0.0F;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getMinimumHeight() {
/*  697 */     return this.minimumHeight;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasMinimumHeight() {
/*  707 */     return (getMinimumHeight() > 0.0F);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isNoWrap() {
/*  716 */     return this.noWrap;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setNoWrap(boolean noWrap) {
/*  725 */     this.noWrap = noWrap;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PdfPTable getTable() {
/*  735 */     return this.table;
/*      */   }
/*      */   
/*      */   void setTable(PdfPTable table) {
/*  739 */     this.table = table;
/*  740 */     this.column.setText(null);
/*  741 */     this.image = null;
/*  742 */     if (table != null) {
/*  743 */       table.setExtendLastRow((this.verticalAlignment == 4));
/*  744 */       this.column.addElement((Element)table);
/*  745 */       table.setWidthPercentage(100.0F);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getColspan() {
/*  755 */     return this.colspan;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setColspan(int colspan) {
/*  764 */     this.colspan = colspan;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getRowspan() {
/*  774 */     return this.rowspan;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setRowspan(int rowspan) {
/*  784 */     this.rowspan = rowspan;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setFollowingIndent(float indent) {
/*  793 */     this.column.setFollowingIndent(indent);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getFollowingIndent() {
/*  802 */     return this.column.getFollowingIndent();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setRightIndent(float indent) {
/*  811 */     this.column.setRightIndent(indent);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getRightIndent() {
/*  820 */     return this.column.getRightIndent();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getSpaceCharRatio() {
/*  829 */     return this.column.getSpaceCharRatio();
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
/*  843 */     this.column.setSpaceCharRatio(spaceCharRatio);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setRunDirection(int runDirection) {
/*  854 */     this.column.setRunDirection(runDirection);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getRunDirection() {
/*  865 */     return this.column.getRunDirection();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Image getImage() {
/*  874 */     return this.image;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setImage(Image image) {
/*  883 */     this.column.setText(null);
/*  884 */     this.table = null;
/*  885 */     this.image = image;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PdfPCellEvent getCellEvent() {
/*  894 */     return this.cellEvent;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setCellEvent(PdfPCellEvent cellEvent) {
/*  903 */     if (cellEvent == null) {
/*  904 */       this.cellEvent = null;
/*  905 */     } else if (this.cellEvent == null) {
/*  906 */       this.cellEvent = cellEvent;
/*  907 */     } else if (this.cellEvent instanceof PdfPCellEventForwarder) {
/*  908 */       ((PdfPCellEventForwarder)this.cellEvent).addCellEvent(cellEvent);
/*      */     } else {
/*  910 */       PdfPCellEventForwarder forward = new PdfPCellEventForwarder();
/*  911 */       forward.addCellEvent(this.cellEvent);
/*  912 */       forward.addCellEvent(cellEvent);
/*  913 */       this.cellEvent = (PdfPCellEvent)forward;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getArabicOptions() {
/*  923 */     return this.column.getArabicOptions();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setArabicOptions(int arabicOptions) {
/*  933 */     this.column.setArabicOptions(arabicOptions);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isUseAscender() {
/*  942 */     return this.column.isUseAscender();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setUseAscender(boolean useAscender) {
/*  951 */     this.column.setUseAscender(useAscender);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isUseDescender() {
/*  960 */     return this.useDescender;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setUseDescender(boolean useDescender) {
/*  969 */     this.useDescender = useDescender;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ColumnText getColumn() {
/*  978 */     return this.column;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<Element> getCompositeElements() {
/*  988 */     return (getColumn()).compositeElements;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setColumn(ColumnText column) {
/*  997 */     this.column = column;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getRotation() {
/* 1007 */     return this.rotation;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setRotation(int rotation) {
/* 1016 */     rotation %= 360;
/* 1017 */     if (rotation < 0) {
/* 1018 */       rotation += 360;
/*      */     }
/* 1020 */     if (rotation % 90 != 0) {
/* 1021 */       throw new IllegalArgumentException(MessageLocalization.getComposedMessage("rotation.must.be.a.multiple.of.90", new Object[0]));
/*      */     }
/* 1023 */     this.rotation = rotation;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getMaxHeight() {
/* 1033 */     boolean pivoted = (getRotation() == 90 || getRotation() == 270);
/* 1034 */     Image img = getImage();
/* 1035 */     if (img != null) {
/* 1036 */       img.scalePercent(100.0F);
/* 1037 */       float refWidth = pivoted ? img.getScaledHeight() : img.getScaledWidth();
/*      */       
/* 1039 */       float scale = (getRight() - getEffectivePaddingRight() - getEffectivePaddingLeft() - getLeft()) / refWidth;
/* 1040 */       img.scalePercent(scale * 100.0F);
/* 1041 */       float refHeight = pivoted ? img.getScaledWidth() : img.getScaledHeight();
/* 1042 */       setBottom(getTop() - getEffectivePaddingTop() - getEffectivePaddingBottom() - refHeight);
/*      */     }
/* 1044 */     else if ((pivoted && hasFixedHeight()) || getColumn() == null) {
/* 1045 */       setBottom(getTop() - getFixedHeight());
/*      */     } else {
/* 1047 */       float right, top, left, bottom; ColumnText ct = ColumnText.duplicate(getColumn());
/*      */       
/* 1049 */       if (pivoted) {
/* 1050 */         right = 20000.0F;
/* 1051 */         top = getRight() - getEffectivePaddingRight();
/* 1052 */         left = 0.0F;
/* 1053 */         bottom = getLeft() + getEffectivePaddingLeft();
/*      */       } else {
/* 1055 */         right = isNoWrap() ? 20000.0F : (getRight() - getEffectivePaddingRight());
/* 1056 */         top = getTop() - getEffectivePaddingTop();
/* 1057 */         left = getLeft() + getEffectivePaddingLeft();
/* 1058 */         bottom = hasCalculatedHeight() ? (getTop() + getEffectivePaddingBottom() - getCalculatedHeight()) : -1.07374182E9F;
/*      */       } 
/* 1060 */       PdfPRow.setColumn(ct, left, bottom, right, top);
/*      */       try {
/* 1062 */         ct.go(true);
/* 1063 */       } catch (DocumentException e) {
/* 1064 */         throw new ExceptionConverter(e);
/*      */       } 
/* 1066 */       if (pivoted) {
/* 1067 */         setBottom(getTop() - getEffectivePaddingTop() - getEffectivePaddingBottom() - ct.getFilledWidth());
/*      */       } else {
/* 1069 */         float yLine = ct.getYLine();
/* 1070 */         if (isUseDescender()) {
/* 1071 */           yLine += ct.getDescender();
/*      */         }
/* 1073 */         setBottom(yLine - getEffectivePaddingBottom());
/*      */       } 
/*      */     } 
/*      */     
/* 1077 */     float height = getHeight();
/* 1078 */     if (height == getEffectivePaddingTop() + getEffectivePaddingBottom()) {
/* 1079 */       height = 0.0F;
/*      */     }
/* 1081 */     if (hasFixedHeight()) {
/* 1082 */       height = getFixedHeight();
/* 1083 */     } else if (hasMinimumHeight() && height < getMinimumHeight()) {
/* 1084 */       height = getMinimumHeight();
/*      */     } 
/* 1086 */     this.cachedMaxHeight = height;
/* 1087 */     return height;
/*      */   }
/*      */   
/*      */   public PdfObject getAccessibleAttribute(PdfName key) {
/* 1091 */     if (this.accessibleAttributes != null) {
/* 1092 */       return this.accessibleAttributes.get(key);
/*      */     }
/* 1094 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setAccessibleAttribute(PdfName key, PdfObject value) {
/* 1099 */     if (this.accessibleAttributes == null) {
/* 1100 */       this.accessibleAttributes = new HashMap<PdfName, PdfObject>();
/*      */     }
/* 1102 */     this.accessibleAttributes.put(key, value);
/*      */   }
/*      */   
/*      */   public HashMap<PdfName, PdfObject> getAccessibleAttributes() {
/* 1106 */     return this.accessibleAttributes;
/*      */   }
/*      */   
/*      */   public PdfName getRole() {
/* 1110 */     return this.role;
/*      */   }
/*      */   
/*      */   public void setRole(PdfName role) {
/* 1114 */     this.role = role;
/*      */   }
/*      */   
/*      */   public AccessibleElementId getId() {
/* 1118 */     return this.id;
/*      */   }
/*      */   
/*      */   public void setId(AccessibleElementId id) {
/* 1122 */     this.id = id;
/*      */   }
/*      */   
/*      */   public boolean isInline() {
/* 1126 */     return false;
/*      */   }
/*      */   
/*      */   public void addHeader(PdfPHeaderCell header) {
/* 1130 */     if (this.headers == null) {
/* 1131 */       this.headers = new ArrayList<PdfPHeaderCell>();
/*      */     }
/* 1133 */     this.headers.add(header);
/*      */   }
/*      */   
/*      */   public ArrayList<PdfPHeaderCell> getHeaders() {
/* 1137 */     return this.headers;
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/PdfPCell.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */