/*      */ package com.itextpdf.text.pdf;
/*      */ 
/*      */ import com.itextpdf.text.AccessibleElementId;
/*      */ import com.itextpdf.text.Chunk;
/*      */ import com.itextpdf.text.DocumentException;
/*      */ import com.itextpdf.text.Element;
/*      */ import com.itextpdf.text.ElementListener;
/*      */ import com.itextpdf.text.Image;
/*      */ import com.itextpdf.text.LargeElement;
/*      */ import com.itextpdf.text.Phrase;
/*      */ import com.itextpdf.text.Rectangle;
/*      */ import com.itextpdf.text.api.Spaceable;
/*      */ import com.itextpdf.text.error_messages.MessageLocalization;
/*      */ import com.itextpdf.text.log.Level;
/*      */ import com.itextpdf.text.log.Logger;
/*      */ import com.itextpdf.text.log.LoggerFactory;
/*      */ import com.itextpdf.text.pdf.events.PdfPTableEventForwarder;
/*      */ import com.itextpdf.text.pdf.interfaces.IAccessibleElement;
/*      */ import java.util.ArrayList;
/*      */ import java.util.HashMap;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class PdfPTable
/*      */   implements LargeElement, Spaceable, IAccessibleElement
/*      */ {
/*   79 */   private final Logger LOGGER = LoggerFactory.getLogger(PdfPTable.class);
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final int BASECANVAS = 0;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final int BACKGROUNDCANVAS = 1;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final int LINECANVAS = 2;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final int TEXTCANVAS = 3;
/*      */ 
/*      */ 
/*      */   
/*  103 */   protected ArrayList<PdfPRow> rows = new ArrayList<PdfPRow>();
/*  104 */   protected float totalHeight = 0.0F;
/*      */ 
/*      */ 
/*      */   
/*      */   protected PdfPCell[] currentRow;
/*      */ 
/*      */   
/*  111 */   protected int currentColIdx = 0;
/*  112 */   protected PdfPCell defaultCell = new PdfPCell((Phrase)null);
/*  113 */   protected float totalWidth = 0.0F;
/*      */ 
/*      */   
/*      */   protected float[] relativeWidths;
/*      */ 
/*      */   
/*      */   protected float[] absoluteWidths;
/*      */ 
/*      */   
/*      */   protected PdfPTableEvent tableEvent;
/*      */   
/*      */   protected int headerRows;
/*      */   
/*  126 */   protected float widthPercentage = 80.0F;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  131 */   private int horizontalAlignment = 1;
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean skipFirstHeader = false;
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean skipLastFooter = false;
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean isColspan = false;
/*      */ 
/*      */   
/*  146 */   protected int runDirection = 1;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean lockedWidth = false;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean splitRows = true;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected float spacingBefore;
/*      */ 
/*      */ 
/*      */   
/*      */   protected float spacingAfter;
/*      */ 
/*      */ 
/*      */   
/*      */   protected float paddingTop;
/*      */ 
/*      */ 
/*      */   
/*  173 */   private boolean[] extendLastRow = new boolean[] { false, false };
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean headersInEvent;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean splitLate = true;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean keepTogether;
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean complete = true;
/*      */ 
/*      */ 
/*      */   
/*      */   private int footerRows;
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean rowCompleted = true;
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean loopCheck = true;
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean rowsNotChecked = true;
/*      */ 
/*      */ 
/*      */   
/*  212 */   protected PdfName role = PdfName.TABLE;
/*  213 */   protected HashMap<PdfName, PdfObject> accessibleAttributes = null;
/*  214 */   protected AccessibleElementId id = new AccessibleElementId();
/*  215 */   private PdfPTableHeader header = null;
/*  216 */   private PdfPTableBody body = null;
/*  217 */   private PdfPTableFooter footer = null;
/*      */ 
/*      */ 
/*      */   
/*      */   private int numberOfWrittenRows;
/*      */ 
/*      */ 
/*      */   
/*      */   protected PdfPTable() {}
/*      */ 
/*      */ 
/*      */   
/*      */   public PdfPTable(float[] relativeWidths) {
/*  230 */     if (relativeWidths == null) {
/*  231 */       throw new NullPointerException(MessageLocalization.getComposedMessage("the.widths.array.in.pdfptable.constructor.can.not.be.null", new Object[0]));
/*      */     }
/*  233 */     if (relativeWidths.length == 0) {
/*  234 */       throw new IllegalArgumentException(MessageLocalization.getComposedMessage("the.widths.array.in.pdfptable.constructor.can.not.have.zero.length", new Object[0]));
/*      */     }
/*  236 */     this.relativeWidths = new float[relativeWidths.length];
/*  237 */     System.arraycopy(relativeWidths, 0, this.relativeWidths, 0, relativeWidths.length);
/*  238 */     this.absoluteWidths = new float[relativeWidths.length];
/*  239 */     calculateWidths();
/*  240 */     this.currentRow = new PdfPCell[this.absoluteWidths.length];
/*  241 */     this.keepTogether = false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PdfPTable(int numColumns) {
/*  250 */     if (numColumns <= 0) {
/*  251 */       throw new IllegalArgumentException(MessageLocalization.getComposedMessage("the.number.of.columns.in.pdfptable.constructor.must.be.greater.than.zero", new Object[0]));
/*      */     }
/*  253 */     this.relativeWidths = new float[numColumns];
/*  254 */     for (int k = 0; k < numColumns; k++) {
/*  255 */       this.relativeWidths[k] = 1.0F;
/*      */     }
/*  257 */     this.absoluteWidths = new float[this.relativeWidths.length];
/*  258 */     calculateWidths();
/*  259 */     this.currentRow = new PdfPCell[this.absoluteWidths.length];
/*  260 */     this.keepTogether = false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PdfPTable(PdfPTable table) {
/*  269 */     copyFormat(table); int k;
/*  270 */     for (k = 0; k < this.currentRow.length && 
/*  271 */       table.currentRow[k] != null; k++)
/*      */     {
/*      */       
/*  274 */       this.currentRow[k] = new PdfPCell(table.currentRow[k]);
/*      */     }
/*  276 */     for (k = 0; k < table.rows.size(); k++) {
/*  277 */       PdfPRow row = table.rows.get(k);
/*  278 */       if (row != null) {
/*  279 */         row = new PdfPRow(row);
/*      */       }
/*  281 */       this.rows.add(row);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void init() {
/*  286 */     this.LOGGER.info("Initialize row and cell heights");
/*      */     
/*  288 */     for (PdfPRow row : getRows()) {
/*  289 */       if (row == null)
/*  290 */         continue;  row.calculated = false;
/*  291 */       for (PdfPCell cell : row.getCells()) {
/*  292 */         if (cell != null) {
/*  293 */           cell.setCalculatedHeight(0.0F);
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
/*      */   public static PdfPTable shallowCopy(PdfPTable table) {
/*  306 */     PdfPTable nt = new PdfPTable();
/*  307 */     nt.copyFormat(table);
/*  308 */     return nt;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void copyFormat(PdfPTable sourceTable) {
/*  318 */     this.rowsNotChecked = sourceTable.rowsNotChecked;
/*  319 */     this.relativeWidths = new float[sourceTable.getNumberOfColumns()];
/*  320 */     this.absoluteWidths = new float[sourceTable.getNumberOfColumns()];
/*  321 */     System.arraycopy(sourceTable.relativeWidths, 0, this.relativeWidths, 0, getNumberOfColumns());
/*  322 */     System.arraycopy(sourceTable.absoluteWidths, 0, this.absoluteWidths, 0, getNumberOfColumns());
/*  323 */     this.totalWidth = sourceTable.totalWidth;
/*  324 */     this.totalHeight = sourceTable.totalHeight;
/*  325 */     this.currentColIdx = 0;
/*  326 */     this.tableEvent = sourceTable.tableEvent;
/*  327 */     this.runDirection = sourceTable.runDirection;
/*  328 */     if (sourceTable.defaultCell instanceof PdfPHeaderCell) {
/*  329 */       this.defaultCell = new PdfPHeaderCell((PdfPHeaderCell)sourceTable.defaultCell);
/*      */     } else {
/*  331 */       this.defaultCell = new PdfPCell(sourceTable.defaultCell);
/*      */     } 
/*  333 */     this.currentRow = new PdfPCell[sourceTable.currentRow.length];
/*  334 */     this.isColspan = sourceTable.isColspan;
/*  335 */     this.splitRows = sourceTable.splitRows;
/*  336 */     this.spacingAfter = sourceTable.spacingAfter;
/*  337 */     this.spacingBefore = sourceTable.spacingBefore;
/*  338 */     this.headerRows = sourceTable.headerRows;
/*  339 */     this.footerRows = sourceTable.footerRows;
/*  340 */     this.lockedWidth = sourceTable.lockedWidth;
/*  341 */     this.extendLastRow = sourceTable.extendLastRow;
/*  342 */     this.headersInEvent = sourceTable.headersInEvent;
/*  343 */     this.widthPercentage = sourceTable.widthPercentage;
/*  344 */     this.splitLate = sourceTable.splitLate;
/*  345 */     this.skipFirstHeader = sourceTable.skipFirstHeader;
/*  346 */     this.skipLastFooter = sourceTable.skipLastFooter;
/*  347 */     this.horizontalAlignment = sourceTable.horizontalAlignment;
/*  348 */     this.keepTogether = sourceTable.keepTogether;
/*  349 */     this.complete = sourceTable.complete;
/*  350 */     this.loopCheck = sourceTable.loopCheck;
/*  351 */     this.id = sourceTable.id;
/*  352 */     this.role = sourceTable.role;
/*  353 */     if (sourceTable.accessibleAttributes != null) {
/*  354 */       this.accessibleAttributes = new HashMap<PdfName, PdfObject>(sourceTable.accessibleAttributes);
/*      */     }
/*  356 */     this.header = sourceTable.getHeader();
/*  357 */     this.body = sourceTable.getBody();
/*  358 */     this.footer = sourceTable.getFooter();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setWidths(float[] relativeWidths) throws DocumentException {
/*  369 */     if (relativeWidths.length != getNumberOfColumns()) {
/*  370 */       throw new DocumentException(MessageLocalization.getComposedMessage("wrong.number.of.columns", new Object[0]));
/*      */     }
/*  372 */     this.relativeWidths = new float[relativeWidths.length];
/*  373 */     System.arraycopy(relativeWidths, 0, this.relativeWidths, 0, relativeWidths.length);
/*  374 */     this.absoluteWidths = new float[relativeWidths.length];
/*  375 */     this.totalHeight = 0.0F;
/*  376 */     calculateWidths();
/*  377 */     calculateHeights();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setWidths(int[] relativeWidths) throws DocumentException {
/*  388 */     float[] tb = new float[relativeWidths.length];
/*  389 */     for (int k = 0; k < relativeWidths.length; k++) {
/*  390 */       tb[k] = relativeWidths[k];
/*      */     }
/*  392 */     setWidths(tb);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void calculateWidths() {
/*  399 */     if (this.totalWidth <= 0.0F) {
/*      */       return;
/*      */     }
/*  402 */     float total = 0.0F;
/*  403 */     int numCols = getNumberOfColumns(); int k;
/*  404 */     for (k = 0; k < numCols; k++) {
/*  405 */       total += this.relativeWidths[k];
/*      */     }
/*  407 */     for (k = 0; k < numCols; k++) {
/*  408 */       this.absoluteWidths[k] = this.totalWidth * this.relativeWidths[k] / total;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setTotalWidth(float totalWidth) {
/*  418 */     if (this.totalWidth == totalWidth) {
/*      */       return;
/*      */     }
/*  421 */     this.totalWidth = totalWidth;
/*  422 */     this.totalHeight = 0.0F;
/*  423 */     calculateWidths();
/*  424 */     calculateHeights();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setTotalWidth(float[] columnWidth) throws DocumentException {
/*  435 */     if (columnWidth.length != getNumberOfColumns()) {
/*  436 */       throw new DocumentException(MessageLocalization.getComposedMessage("wrong.number.of.columns", new Object[0]));
/*      */     }
/*  438 */     this.totalWidth = 0.0F;
/*  439 */     for (int k = 0; k < columnWidth.length; k++) {
/*  440 */       this.totalWidth += columnWidth[k];
/*      */     }
/*  442 */     setWidths(columnWidth);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setWidthPercentage(float[] columnWidth, Rectangle pageSize) throws DocumentException {
/*  453 */     if (columnWidth.length != getNumberOfColumns()) {
/*  454 */       throw new IllegalArgumentException(MessageLocalization.getComposedMessage("wrong.number.of.columns", new Object[0]));
/*      */     }
/*  456 */     setTotalWidth(columnWidth);
/*  457 */     this.widthPercentage = this.totalWidth / (pageSize.getRight() - pageSize.getLeft()) * 100.0F;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getTotalWidth() {
/*  466 */     return this.totalWidth;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float calculateHeights() {
/*  477 */     if (this.totalWidth <= 0.0F) {
/*  478 */       return 0.0F;
/*      */     }
/*  480 */     this.totalHeight = 0.0F;
/*  481 */     for (int k = 0; k < this.rows.size(); k++) {
/*  482 */       this.totalHeight += getRowHeight(k, true);
/*      */     }
/*  484 */     return this.totalHeight;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void resetColumnCount(int newColCount) {
/*  494 */     if (newColCount <= 0) {
/*  495 */       throw new IllegalArgumentException(MessageLocalization.getComposedMessage("the.number.of.columns.in.pdfptable.constructor.must.be.greater.than.zero", new Object[0]));
/*      */     }
/*  497 */     this.relativeWidths = new float[newColCount];
/*  498 */     for (int k = 0; k < newColCount; k++) {
/*  499 */       this.relativeWidths[k] = 1.0F;
/*      */     }
/*  501 */     this.absoluteWidths = new float[this.relativeWidths.length];
/*  502 */     calculateWidths();
/*  503 */     this.currentRow = new PdfPCell[this.absoluteWidths.length];
/*  504 */     this.totalHeight = 0.0F;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PdfPCell getDefaultCell() {
/*  515 */     return this.defaultCell;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PdfPCell addCell(PdfPCell cell) {
/*      */     PdfPCell ncell;
/*  524 */     this.rowCompleted = false;
/*      */     
/*  526 */     if (cell instanceof PdfPHeaderCell) {
/*  527 */       ncell = new PdfPHeaderCell((PdfPHeaderCell)cell);
/*      */     } else {
/*  529 */       ncell = new PdfPCell(cell);
/*      */     } 
/*      */     
/*  532 */     int colspan = ncell.getColspan();
/*  533 */     colspan = Math.max(colspan, 1);
/*  534 */     colspan = Math.min(colspan, this.currentRow.length - this.currentColIdx);
/*  535 */     ncell.setColspan(colspan);
/*      */     
/*  537 */     if (colspan != 1) {
/*  538 */       this.isColspan = true;
/*      */     }
/*  540 */     int rdir = ncell.getRunDirection();
/*  541 */     if (rdir == 1) {
/*  542 */       ncell.setRunDirection(this.runDirection);
/*      */     }
/*      */     
/*  545 */     skipColsWithRowspanAbove();
/*      */     
/*  547 */     boolean cellAdded = false;
/*  548 */     if (this.currentColIdx < this.currentRow.length) {
/*  549 */       this.currentRow[this.currentColIdx] = ncell;
/*  550 */       this.currentColIdx += colspan;
/*  551 */       cellAdded = true;
/*      */     } 
/*      */     
/*  554 */     skipColsWithRowspanAbove();
/*      */     
/*  556 */     while (this.currentColIdx >= this.currentRow.length) {
/*  557 */       int numCols = getNumberOfColumns();
/*  558 */       if (this.runDirection == 3) {
/*  559 */         PdfPCell[] rtlRow = new PdfPCell[numCols];
/*  560 */         int rev = this.currentRow.length;
/*  561 */         for (int k = 0; k < this.currentRow.length; k++) {
/*  562 */           PdfPCell rcell = this.currentRow[k];
/*  563 */           int cspan = rcell.getColspan();
/*  564 */           rev -= cspan;
/*  565 */           rtlRow[rev] = rcell;
/*  566 */           k += cspan - 1;
/*      */         } 
/*  568 */         this.currentRow = rtlRow;
/*      */       } 
/*  570 */       PdfPRow row = new PdfPRow(this.currentRow);
/*  571 */       if (this.totalWidth > 0.0F) {
/*  572 */         row.setWidths(this.absoluteWidths);
/*  573 */         this.totalHeight += row.getMaxHeights();
/*      */       } 
/*  575 */       this.rows.add(row);
/*  576 */       this.currentRow = new PdfPCell[numCols];
/*  577 */       this.currentColIdx = 0;
/*  578 */       skipColsWithRowspanAbove();
/*  579 */       this.rowCompleted = true;
/*      */     } 
/*      */     
/*  582 */     if (!cellAdded) {
/*  583 */       this.currentRow[this.currentColIdx] = ncell;
/*  584 */       this.currentColIdx += colspan;
/*      */     } 
/*  586 */     return ncell;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void skipColsWithRowspanAbove() {
/*  596 */     int direction = 1;
/*  597 */     if (this.runDirection == 3) {
/*  598 */       direction = -1;
/*      */     }
/*  600 */     while (rowSpanAbove(this.rows.size(), this.currentColIdx)) {
/*  601 */       this.currentColIdx += direction;
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
/*      */   PdfPCell cellAt(int row, int col) {
/*  614 */     PdfPCell[] cells = ((PdfPRow)this.rows.get(row)).getCells();
/*  615 */     for (int i = 0; i < cells.length; i++) {
/*  616 */       if (cells[i] != null && 
/*  617 */         col >= i && col < i + cells[i].getColspan()) {
/*  618 */         return cells[i];
/*      */       }
/*      */     } 
/*      */     
/*  622 */     return null;
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
/*      */   boolean rowSpanAbove(int currRow, int currCol) {
/*  634 */     if (currCol >= getNumberOfColumns() || currCol < 0 || currRow < 1)
/*      */     {
/*      */       
/*  637 */       return false;
/*      */     }
/*  639 */     int row = currRow - 1;
/*  640 */     PdfPRow aboveRow = this.rows.get(row);
/*  641 */     if (aboveRow == null) {
/*  642 */       return false;
/*      */     }
/*  644 */     PdfPCell aboveCell = cellAt(row, currCol);
/*  645 */     while (aboveCell == null && row > 0) {
/*  646 */       aboveRow = this.rows.get(--row);
/*  647 */       if (aboveRow == null) {
/*  648 */         return false;
/*      */       }
/*  650 */       aboveCell = cellAt(row, currCol);
/*      */     } 
/*      */     
/*  653 */     int distance = currRow - row;
/*      */     
/*  655 */     if (aboveCell.getRowspan() == 1 && distance > 1) {
/*  656 */       int col = currCol - 1;
/*  657 */       aboveRow = this.rows.get(row + 1);
/*  658 */       distance--;
/*  659 */       aboveCell = aboveRow.getCells()[col];
/*  660 */       while (aboveCell == null && col > 0) {
/*  661 */         aboveCell = aboveRow.getCells()[--col];
/*      */       }
/*      */     } 
/*      */     
/*  665 */     return (aboveCell != null && aboveCell.getRowspan() > distance);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addCell(String text) {
/*  674 */     addCell(new Phrase(text));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addCell(PdfPTable table) {
/*  683 */     this.defaultCell.setTable(table);
/*  684 */     PdfPCell newCell = addCell(this.defaultCell);
/*  685 */     newCell.id = new AccessibleElementId();
/*  686 */     this.defaultCell.setTable(null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addCell(Image image) {
/*  696 */     this.defaultCell.setImage(image);
/*  697 */     PdfPCell newCell = addCell(this.defaultCell);
/*  698 */     newCell.id = new AccessibleElementId();
/*  699 */     this.defaultCell.setImage(null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addCell(Phrase phrase) {
/*  708 */     this.defaultCell.setPhrase(phrase);
/*  709 */     PdfPCell newCell = addCell(this.defaultCell);
/*  710 */     newCell.id = new AccessibleElementId();
/*  711 */     this.defaultCell.setPhrase(null);
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
/*      */   public float writeSelectedRows(int rowStart, int rowEnd, float xPos, float yPos, PdfContentByte[] canvases) {
/*  729 */     return writeSelectedRows(0, -1, rowStart, rowEnd, xPos, yPos, canvases);
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
/*      */   public float writeSelectedRows(int colStart, int colEnd, int rowStart, int rowEnd, float xPos, float yPos, PdfContentByte[] canvases) {
/*  753 */     return writeSelectedRows(colStart, colEnd, rowStart, rowEnd, xPos, yPos, canvases, true);
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
/*      */   public float writeSelectedRows(int colStart, int colEnd, int rowStart, int rowEnd, float xPos, float yPos, PdfContentByte[] canvases, boolean reusable) {
/*  781 */     if (this.totalWidth <= 0.0F) {
/*  782 */       throw new RuntimeException(MessageLocalization.getComposedMessage("the.table.width.must.be.greater.than.zero", new Object[0]));
/*      */     }
/*      */     
/*  785 */     int totalRows = this.rows.size();
/*  786 */     if (rowStart < 0) {
/*  787 */       rowStart = 0;
/*      */     }
/*  789 */     if (rowEnd < 0) {
/*  790 */       rowEnd = totalRows;
/*      */     } else {
/*  792 */       rowEnd = Math.min(rowEnd, totalRows);
/*      */     } 
/*  794 */     if (rowStart >= rowEnd) {
/*  795 */       return yPos;
/*      */     }
/*      */     
/*  798 */     int totalCols = getNumberOfColumns();
/*  799 */     if (colStart < 0) {
/*  800 */       colStart = 0;
/*      */     } else {
/*  802 */       colStart = Math.min(colStart, totalCols);
/*      */     } 
/*  804 */     if (colEnd < 0) {
/*  805 */       colEnd = totalCols;
/*      */     } else {
/*  807 */       colEnd = Math.min(colEnd, totalCols);
/*      */     } 
/*      */     
/*  810 */     if (this.LOGGER.isLogging(Level.INFO)) {
/*  811 */       this.LOGGER.info(String.format("Writing row %s to %s; column %s to %s", new Object[] { Integer.valueOf(rowStart), Integer.valueOf(rowEnd), Integer.valueOf(colStart), Integer.valueOf(colEnd) }));
/*      */     }
/*      */     
/*  814 */     float yPosStart = yPos;
/*      */     
/*  816 */     PdfPTableBody currentBlock = null;
/*  817 */     if (this.rowsNotChecked) {
/*  818 */       getFittingRows(Float.MAX_VALUE, rowStart);
/*      */     }
/*  820 */     List<PdfPRow> rows = getRows(rowStart, rowEnd);
/*  821 */     int k = rowStart;
/*  822 */     for (PdfPRow row : rows) {
/*  823 */       if ((getHeader()).rows != null && (getHeader()).rows.contains(row) && currentBlock == null) {
/*  824 */         currentBlock = openTableBlock(getHeader(), canvases[3]);
/*  825 */       } else if ((getBody()).rows != null && (getBody()).rows.contains(row) && currentBlock == null) {
/*  826 */         currentBlock = openTableBlock(getBody(), canvases[3]);
/*  827 */       } else if ((getFooter()).rows != null && (getFooter()).rows.contains(row) && currentBlock == null) {
/*  828 */         currentBlock = openTableBlock(getFooter(), canvases[3]);
/*      */       } 
/*  830 */       if (row != null) {
/*  831 */         row.writeCells(colStart, colEnd, xPos, yPos, canvases, reusable);
/*  832 */         yPos -= row.getMaxHeights();
/*      */       } 
/*  834 */       if ((getHeader()).rows != null && (getHeader()).rows.contains(row) && (k == rowEnd - 1 || !(getHeader()).rows.contains(rows.get(k + 1)))) {
/*  835 */         currentBlock = closeTableBlock(getHeader(), canvases[3]);
/*  836 */       } else if ((getBody()).rows != null && (getBody()).rows.contains(row) && (k == rowEnd - 1 || !(getBody()).rows.contains(rows.get(k + 1)))) {
/*  837 */         currentBlock = closeTableBlock(getBody(), canvases[3]);
/*  838 */       } else if ((getFooter()).rows != null && (getFooter()).rows.contains(row) && (k == rowEnd - 1 || !(getFooter()).rows.contains(rows.get(k + 1)))) {
/*  839 */         currentBlock = closeTableBlock(getFooter(), canvases[3]);
/*      */       } 
/*  841 */       k++;
/*      */     } 
/*      */     
/*  844 */     if (this.tableEvent != null && colStart == 0 && colEnd == totalCols) {
/*  845 */       float[] heights = new float[rowEnd - rowStart + 1];
/*  846 */       heights[0] = yPosStart;
/*  847 */       for (k = 0; k < rowEnd - rowStart; k++) {
/*  848 */         PdfPRow row = rows.get(k);
/*  849 */         float hr = 0.0F;
/*  850 */         if (row != null) {
/*  851 */           hr = row.getMaxHeights();
/*      */         }
/*  853 */         heights[k + 1] = heights[k] - hr;
/*      */       } 
/*  855 */       this.tableEvent.tableLayout(this, getEventWidths(xPos, rowStart, rowEnd, this.headersInEvent), heights, this.headersInEvent ? this.headerRows : 0, rowStart, canvases);
/*      */     } 
/*      */     
/*  858 */     return yPos;
/*      */   }
/*      */   
/*      */   private PdfPTableBody openTableBlock(PdfPTableBody block, PdfContentByte canvas) {
/*  862 */     if (canvas.writer.getStandardStructElems().contains(block.getRole())) {
/*  863 */       canvas.openMCBlock(block);
/*  864 */       return block;
/*      */     } 
/*  866 */     return null;
/*      */   }
/*      */   
/*      */   private PdfPTableBody closeTableBlock(PdfPTableBody block, PdfContentByte canvas) {
/*  870 */     if (canvas.writer.getStandardStructElems().contains(block.getRole())) {
/*  871 */       canvas.closeMCBlock(block);
/*      */     }
/*  873 */     return null;
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
/*      */   public float writeSelectedRows(int rowStart, int rowEnd, float xPos, float yPos, PdfContentByte canvas) {
/*  889 */     return writeSelectedRows(0, -1, rowStart, rowEnd, xPos, yPos, canvas);
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
/*      */   public float writeSelectedRows(int colStart, int colEnd, int rowStart, int rowEnd, float xPos, float yPos, PdfContentByte canvas) {
/*  910 */     return writeSelectedRows(colStart, colEnd, rowStart, rowEnd, xPos, yPos, canvas, true);
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
/*      */   public float writeSelectedRows(int colStart, int colEnd, int rowStart, int rowEnd, float xPos, float yPos, PdfContentByte canvas, boolean reusable) {
/*  935 */     int totalCols = getNumberOfColumns();
/*  936 */     if (colStart < 0) {
/*  937 */       colStart = 0;
/*      */     } else {
/*  939 */       colStart = Math.min(colStart, totalCols);
/*      */     } 
/*      */     
/*  942 */     if (colEnd < 0) {
/*  943 */       colEnd = totalCols;
/*      */     } else {
/*  945 */       colEnd = Math.min(colEnd, totalCols);
/*      */     } 
/*      */     
/*  948 */     boolean clip = (colStart != 0 || colEnd != totalCols);
/*      */     
/*  950 */     if (clip) {
/*  951 */       float w = 0.0F;
/*  952 */       for (int k = colStart; k < colEnd; k++) {
/*  953 */         w += this.absoluteWidths[k];
/*      */       }
/*  955 */       canvas.saveState();
/*  956 */       float lx = (colStart == 0) ? 10000.0F : 0.0F;
/*  957 */       float rx = (colEnd == totalCols) ? 10000.0F : 0.0F;
/*  958 */       canvas.rectangle(xPos - lx, -10000.0F, w + lx + rx, 20000.0F);
/*  959 */       canvas.clip();
/*  960 */       canvas.newPath();
/*      */     } 
/*      */     
/*  963 */     PdfContentByte[] canvases = beginWritingRows(canvas);
/*  964 */     float y = writeSelectedRows(colStart, colEnd, rowStart, rowEnd, xPos, yPos, canvases, reusable);
/*  965 */     endWritingRows(canvases);
/*      */     
/*  967 */     if (clip) {
/*  968 */       canvas.restoreState();
/*      */     }
/*      */     
/*  971 */     return y;
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
/*      */   public static PdfContentByte[] beginWritingRows(PdfContentByte canvas) {
/*  997 */     return new PdfContentByte[] { canvas, canvas
/*      */         
/*  999 */         .getDuplicate(), canvas
/* 1000 */         .getDuplicate(), canvas
/* 1001 */         .getDuplicate() };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void endWritingRows(PdfContentByte[] canvases) {
/* 1010 */     PdfContentByte canvas = canvases[0];
/* 1011 */     PdfArtifact artifact = new PdfArtifact();
/* 1012 */     canvas.openMCBlock(artifact);
/* 1013 */     canvas.saveState();
/* 1014 */     canvas.add(canvases[1]);
/* 1015 */     canvas.restoreState();
/* 1016 */     canvas.saveState();
/* 1017 */     canvas.setLineCap(2);
/* 1018 */     canvas.resetRGBColorStroke();
/* 1019 */     canvas.add(canvases[2]);
/* 1020 */     canvas.restoreState();
/* 1021 */     canvas.closeMCBlock(artifact);
/* 1022 */     canvas.add(canvases[3]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int size() {
/* 1031 */     return this.rows.size();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getTotalHeight() {
/* 1040 */     return this.totalHeight;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getRowHeight(int idx) {
/* 1050 */     return getRowHeight(idx, false);
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
/*      */   protected float getRowHeight(int idx, boolean firsttime) {
/* 1062 */     if (this.totalWidth <= 0.0F || idx < 0 || idx >= this.rows.size()) {
/* 1063 */       return 0.0F;
/*      */     }
/* 1065 */     PdfPRow row = this.rows.get(idx);
/* 1066 */     if (row == null) {
/* 1067 */       return 0.0F;
/*      */     }
/* 1069 */     if (firsttime) {
/* 1070 */       row.setWidths(this.absoluteWidths);
/*      */     }
/* 1072 */     float height = row.getMaxHeights();
/*      */ 
/*      */     
/* 1075 */     for (int i = 0; i < this.relativeWidths.length; i++) {
/* 1076 */       if (rowSpanAbove(idx, i)) {
/*      */ 
/*      */         
/* 1079 */         int rs = 1;
/* 1080 */         while (rowSpanAbove(idx - rs, i)) {
/* 1081 */           rs++;
/*      */         }
/* 1083 */         PdfPRow tmprow = this.rows.get(idx - rs);
/* 1084 */         PdfPCell cell = tmprow.getCells()[i];
/* 1085 */         float tmp = 0.0F;
/* 1086 */         if (cell != null && cell.getRowspan() == rs + 1) {
/* 1087 */           tmp = cell.getMaxHeight();
/* 1088 */           while (rs > 0) {
/* 1089 */             tmp -= getRowHeight(idx - rs);
/* 1090 */             rs--;
/*      */           } 
/*      */         } 
/* 1093 */         if (tmp > height)
/* 1094 */           height = tmp; 
/*      */       } 
/*      */     } 
/* 1097 */     row.setMaxHeights(height);
/* 1098 */     return height;
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
/*      */   public float getRowspanHeight(int rowIndex, int cellIndex) {
/* 1112 */     if (this.totalWidth <= 0.0F || rowIndex < 0 || rowIndex >= this.rows.size()) {
/* 1113 */       return 0.0F;
/*      */     }
/* 1115 */     PdfPRow row = this.rows.get(rowIndex);
/* 1116 */     if (row == null || cellIndex >= (row.getCells()).length) {
/* 1117 */       return 0.0F;
/*      */     }
/* 1119 */     PdfPCell cell = row.getCells()[cellIndex];
/* 1120 */     if (cell == null) {
/* 1121 */       return 0.0F;
/*      */     }
/* 1123 */     float rowspanHeight = 0.0F;
/* 1124 */     for (int j = 0; j < cell.getRowspan(); j++) {
/* 1125 */       rowspanHeight += getRowHeight(rowIndex + j);
/*      */     }
/* 1127 */     return rowspanHeight;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasRowspan(int rowIdx) {
/* 1136 */     if (rowIdx < this.rows.size() && getRow(rowIdx).hasRowspan()) {
/* 1137 */       return true;
/*      */     }
/* 1139 */     PdfPRow previousRow = (rowIdx > 0) ? getRow(rowIdx - 1) : null;
/* 1140 */     if (previousRow != null && previousRow.hasRowspan()) {
/* 1141 */       return true;
/*      */     }
/* 1143 */     for (int i = 0; i < getNumberOfColumns(); i++) {
/* 1144 */       if (rowSpanAbove(rowIdx - 1, i)) {
/* 1145 */         return true;
/*      */       }
/*      */     } 
/* 1148 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void normalizeHeadersFooters() {
/* 1157 */     if (this.footerRows > this.headerRows) {
/* 1158 */       this.footerRows = this.headerRows;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getHeaderHeight() {
/* 1169 */     float total = 0.0F;
/* 1170 */     int size = Math.min(this.rows.size(), this.headerRows);
/* 1171 */     for (int k = 0; k < size; k++) {
/* 1172 */       PdfPRow row = this.rows.get(k);
/* 1173 */       if (row != null) {
/* 1174 */         total += row.getMaxHeights();
/*      */       }
/*      */     } 
/* 1177 */     return total;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getFooterHeight() {
/* 1188 */     float total = 0.0F;
/* 1189 */     int start = Math.max(0, this.headerRows - this.footerRows);
/* 1190 */     int size = Math.min(this.rows.size(), this.headerRows);
/* 1191 */     for (int k = start; k < size; k++) {
/* 1192 */       PdfPRow row = this.rows.get(k);
/* 1193 */       if (row != null) {
/* 1194 */         total += row.getMaxHeights();
/*      */       }
/*      */     } 
/* 1197 */     return total;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean deleteRow(int rowNumber) {
/* 1207 */     if (rowNumber < 0 || rowNumber >= this.rows.size()) {
/* 1208 */       return false;
/*      */     }
/* 1210 */     if (this.totalWidth > 0.0F) {
/* 1211 */       PdfPRow row = this.rows.get(rowNumber);
/* 1212 */       if (row != null) {
/* 1213 */         this.totalHeight -= row.getMaxHeights();
/*      */       }
/*      */     } 
/* 1216 */     this.rows.remove(rowNumber);
/* 1217 */     if (rowNumber < this.headerRows) {
/* 1218 */       this.headerRows--;
/* 1219 */       if (rowNumber >= this.headerRows - this.footerRows) {
/* 1220 */         this.footerRows--;
/*      */       }
/*      */     } 
/* 1223 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean deleteLastRow() {
/* 1232 */     return deleteRow(this.rows.size() - 1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void deleteBodyRows() {
/* 1239 */     ArrayList<PdfPRow> rows2 = new ArrayList<PdfPRow>();
/* 1240 */     for (int k = 0; k < this.headerRows; k++) {
/* 1241 */       rows2.add(this.rows.get(k));
/*      */     }
/* 1243 */     this.rows = rows2;
/* 1244 */     this.totalHeight = 0.0F;
/* 1245 */     if (this.totalWidth > 0.0F) {
/* 1246 */       this.totalHeight = getHeaderHeight();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getNumberOfColumns() {
/* 1257 */     return this.relativeWidths.length;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getHeaderRows() {
/* 1266 */     return this.headerRows;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setHeaderRows(int headerRows) {
/* 1277 */     if (headerRows < 0) {
/* 1278 */       headerRows = 0;
/*      */     }
/* 1280 */     this.headerRows = headerRows;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<Chunk> getChunks() {
/* 1289 */     return new ArrayList<Chunk>();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int type() {
/* 1298 */     return 23;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isContent() {
/* 1306 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isNestable() {
/* 1314 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean process(ElementListener listener) {
/*      */     try {
/* 1326 */       return listener.add((Element)this);
/* 1327 */     } catch (DocumentException de) {
/* 1328 */       return false;
/*      */     } 
/*      */   }
/*      */   
/*      */   public String getSummary() {
/* 1333 */     return getAccessibleAttribute(PdfName.SUMMARY).toString();
/*      */   }
/*      */   
/*      */   public void setSummary(String summary) {
/* 1337 */     setAccessibleAttribute(PdfName.SUMMARY, new PdfString(summary));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getWidthPercentage() {
/* 1346 */     return this.widthPercentage;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setWidthPercentage(float widthPercentage) {
/* 1356 */     this.widthPercentage = widthPercentage;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getHorizontalAlignment() {
/* 1365 */     return this.horizontalAlignment;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setHorizontalAlignment(int horizontalAlignment) {
/* 1376 */     this.horizontalAlignment = horizontalAlignment;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PdfPRow getRow(int idx) {
/* 1386 */     return this.rows.get(idx);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ArrayList<PdfPRow> getRows() {
/* 1395 */     return this.rows;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getLastCompletedRowIndex() {
/* 1404 */     return this.rows.size() - 1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setBreakPoints(int... breakPoints) {
/* 1415 */     keepRowsTogether(0, this.rows.size());
/*      */     
/* 1417 */     for (int i = 0; i < breakPoints.length; i++) {
/* 1418 */       getRow(breakPoints[i]).setMayNotBreak(false);
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
/*      */   public void keepRowsTogether(int[] rows) {
/* 1430 */     for (int i = 0; i < rows.length; i++) {
/* 1431 */       getRow(rows[i]).setMayNotBreak(true);
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
/*      */   public void keepRowsTogether(int start, int end) {
/* 1444 */     if (start < end) {
/* 1445 */       while (start < end) {
/* 1446 */         getRow(start).setMayNotBreak(true);
/* 1447 */         start++;
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
/*      */   public void keepRowsTogether(int start) {
/* 1462 */     keepRowsTogether(start, this.rows.size());
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
/*      */   public ArrayList<PdfPRow> getRows(int start, int end) {
/* 1474 */     ArrayList<PdfPRow> list = new ArrayList<PdfPRow>();
/* 1475 */     if (start < 0 || end > size()) {
/* 1476 */       return list;
/*      */     }
/* 1478 */     for (int i = start; i < end; i++) {
/* 1479 */       list.add(adjustCellsInRow(i, end));
/*      */     }
/* 1481 */     return list;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected PdfPRow adjustCellsInRow(int start, int end) {
/* 1492 */     PdfPRow row = getRow(start);
/* 1493 */     if (row.isAdjusted()) {
/* 1494 */       return row;
/*      */     }
/* 1496 */     row = new PdfPRow(row);
/*      */     
/* 1498 */     PdfPCell[] cells = row.getCells();
/* 1499 */     for (int i = 0; i < cells.length; i++) {
/* 1500 */       PdfPCell cell = cells[i];
/* 1501 */       if (cell != null && cell.getRowspan() != 1) {
/*      */ 
/*      */         
/* 1504 */         int stop = Math.min(end, start + cell.getRowspan());
/* 1505 */         float extra = 0.0F;
/* 1506 */         for (int k = start + 1; k < stop; k++) {
/* 1507 */           extra += getRow(k).getMaxHeights();
/*      */         }
/* 1509 */         row.setExtraHeight(i, extra);
/*      */       } 
/* 1511 */     }  row.setAdjusted(true);
/* 1512 */     return row;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setTableEvent(PdfPTableEvent event) {
/* 1521 */     if (event == null) {
/* 1522 */       this.tableEvent = null;
/* 1523 */     } else if (this.tableEvent == null) {
/* 1524 */       this.tableEvent = event;
/* 1525 */     } else if (this.tableEvent instanceof PdfPTableEventForwarder) {
/* 1526 */       ((PdfPTableEventForwarder)this.tableEvent).addTableEvent(event);
/*      */     } else {
/* 1528 */       PdfPTableEventForwarder forward = new PdfPTableEventForwarder();
/* 1529 */       forward.addTableEvent(this.tableEvent);
/* 1530 */       forward.addTableEvent(event);
/* 1531 */       this.tableEvent = (PdfPTableEvent)forward;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PdfPTableEvent getTableEvent() {
/* 1541 */     return this.tableEvent;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float[] getAbsoluteWidths() {
/* 1550 */     return this.absoluteWidths;
/*      */   }
/*      */   
/*      */   float[][] getEventWidths(float xPos, int firstRow, int lastRow, boolean includeHeaders) {
/* 1554 */     if (includeHeaders) {
/* 1555 */       firstRow = Math.max(firstRow, this.headerRows);
/* 1556 */       lastRow = Math.max(lastRow, this.headerRows);
/*      */     } 
/* 1558 */     float[][] widths = new float[(includeHeaders ? this.headerRows : 0) + lastRow - firstRow][];
/* 1559 */     if (this.isColspan) {
/* 1560 */       int n = 0;
/* 1561 */       if (includeHeaders) {
/* 1562 */         for (int k = 0; k < this.headerRows; k++) {
/* 1563 */           PdfPRow row = this.rows.get(k);
/* 1564 */           if (row == null) {
/* 1565 */             n++;
/*      */           } else {
/* 1567 */             widths[n++] = row.getEventWidth(xPos, this.absoluteWidths);
/*      */           } 
/*      */         } 
/*      */       }
/* 1571 */       for (; firstRow < lastRow; firstRow++) {
/* 1572 */         PdfPRow row = this.rows.get(firstRow);
/* 1573 */         if (row == null) {
/* 1574 */           n++;
/*      */         } else {
/* 1576 */           widths[n++] = row.getEventWidth(xPos, this.absoluteWidths);
/*      */         } 
/*      */       } 
/*      */     } else {
/* 1580 */       int numCols = getNumberOfColumns();
/* 1581 */       float[] width = new float[numCols + 1];
/* 1582 */       width[0] = xPos; int k;
/* 1583 */       for (k = 0; k < numCols; k++) {
/* 1584 */         width[k + 1] = width[k] + this.absoluteWidths[k];
/*      */       }
/* 1586 */       for (k = 0; k < widths.length; k++) {
/* 1587 */         widths[k] = width;
/*      */       }
/*      */     } 
/* 1590 */     return widths;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isSkipFirstHeader() {
/* 1600 */     return this.skipFirstHeader;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isSkipLastFooter() {
/* 1611 */     return this.skipLastFooter;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setSkipFirstHeader(boolean skipFirstHeader) {
/* 1621 */     this.skipFirstHeader = skipFirstHeader;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setSkipLastFooter(boolean skipLastFooter) {
/* 1632 */     this.skipLastFooter = skipLastFooter;
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
/* 1643 */     switch (runDirection) {
/*      */       case 0:
/*      */       case 1:
/*      */       case 2:
/*      */       case 3:
/* 1648 */         this.runDirection = runDirection;
/*      */         return;
/*      */     } 
/* 1651 */     throw new RuntimeException(MessageLocalization.getComposedMessage("invalid.run.direction.1", runDirection));
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
/*      */   public int getRunDirection() {
/* 1663 */     return this.runDirection;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isLockedWidth() {
/* 1672 */     return this.lockedWidth;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setLockedWidth(boolean lockedWidth) {
/* 1683 */     this.lockedWidth = lockedWidth;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isSplitRows() {
/* 1692 */     return this.splitRows;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setSplitRows(boolean splitRows) {
/* 1703 */     this.splitRows = splitRows;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setSpacingBefore(float spacing) {
/* 1712 */     this.spacingBefore = spacing;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setSpacingAfter(float spacing) {
/* 1721 */     this.spacingAfter = spacing;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float spacingBefore() {
/* 1730 */     return this.spacingBefore;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float spacingAfter() {
/* 1739 */     return this.spacingAfter;
/*      */   }
/*      */   
/*      */   public float getPaddingTop() {
/* 1743 */     return this.paddingTop;
/*      */   }
/*      */   
/*      */   public void setPaddingTop(float paddingTop) {
/* 1747 */     this.paddingTop = paddingTop;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isExtendLastRow() {
/* 1756 */     return this.extendLastRow[0];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setExtendLastRow(boolean extendLastRows) {
/* 1766 */     this.extendLastRow[0] = extendLastRows;
/* 1767 */     this.extendLastRow[1] = extendLastRows;
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
/*      */   public void setExtendLastRow(boolean extendLastRows, boolean extendFinalRow) {
/* 1781 */     this.extendLastRow[0] = extendLastRows;
/* 1782 */     this.extendLastRow[1] = extendFinalRow;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isExtendLastRow(boolean newPageFollows) {
/* 1793 */     if (newPageFollows) {
/* 1794 */       return this.extendLastRow[0];
/*      */     }
/* 1796 */     return this.extendLastRow[1];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isHeadersInEvent() {
/* 1805 */     return this.headersInEvent;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setHeadersInEvent(boolean headersInEvent) {
/* 1814 */     this.headersInEvent = headersInEvent;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isSplitLate() {
/* 1823 */     return this.splitLate;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setSplitLate(boolean splitLate) {
/* 1833 */     this.splitLate = splitLate;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setKeepTogether(boolean keepTogether) {
/* 1844 */     this.keepTogether = keepTogether;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getKeepTogether() {
/* 1854 */     return this.keepTogether;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getFooterRows() {
/* 1863 */     return this.footerRows;
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
/*      */   public void setFooterRows(int footerRows) {
/* 1879 */     if (footerRows < 0) {
/* 1880 */       footerRows = 0;
/*      */     }
/* 1882 */     this.footerRows = footerRows;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void completeRow() {
/* 1891 */     while (!this.rowCompleted) {
/* 1892 */       addCell(this.defaultCell);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void flushContent() {
/* 1901 */     deleteBodyRows();
/*      */ 
/*      */     
/* 1904 */     if (this.numberOfWrittenRows > 0) {
/* 1905 */       setSkipFirstHeader(true);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void addNumberOfRowsWritten(int numberOfWrittenRows) {
/* 1916 */     this.numberOfWrittenRows += numberOfWrittenRows;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isComplete() {
/* 1924 */     return this.complete;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setComplete(boolean complete) {
/* 1932 */     this.complete = complete;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getSpacingBefore() {
/* 1939 */     return this.spacingBefore;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getSpacingAfter() {
/* 1946 */     return this.spacingAfter;
/*      */   }
/*      */   
/*      */   public boolean isLoopCheck() {
/* 1950 */     return this.loopCheck;
/*      */   }
/*      */   
/*      */   public void setLoopCheck(boolean loopCheck) {
/* 1954 */     this.loopCheck = loopCheck;
/*      */   }
/*      */   
/*      */   public PdfObject getAccessibleAttribute(PdfName key) {
/* 1958 */     if (this.accessibleAttributes != null) {
/* 1959 */       return this.accessibleAttributes.get(key);
/*      */     }
/* 1961 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setAccessibleAttribute(PdfName key, PdfObject value) {
/* 1966 */     if (this.accessibleAttributes == null) {
/* 1967 */       this.accessibleAttributes = new HashMap<PdfName, PdfObject>();
/*      */     }
/* 1969 */     this.accessibleAttributes.put(key, value);
/*      */   }
/*      */   
/*      */   public HashMap<PdfName, PdfObject> getAccessibleAttributes() {
/* 1973 */     return this.accessibleAttributes;
/*      */   }
/*      */   
/*      */   public PdfName getRole() {
/* 1977 */     return this.role;
/*      */   }
/*      */   
/*      */   public void setRole(PdfName role) {
/* 1981 */     this.role = role;
/*      */   }
/*      */   
/*      */   public AccessibleElementId getId() {
/* 1985 */     return this.id;
/*      */   }
/*      */   
/*      */   public void setId(AccessibleElementId id) {
/* 1989 */     this.id = id;
/*      */   }
/*      */   
/*      */   public boolean isInline() {
/* 1993 */     return false;
/*      */   }
/*      */   
/*      */   public PdfPTableHeader getHeader() {
/* 1997 */     if (this.header == null) {
/* 1998 */       this.header = new PdfPTableHeader();
/*      */     }
/* 2000 */     return this.header;
/*      */   }
/*      */   
/*      */   public PdfPTableBody getBody() {
/* 2004 */     if (this.body == null) {
/* 2005 */       this.body = new PdfPTableBody();
/*      */     }
/* 2007 */     return this.body;
/*      */   }
/*      */   
/*      */   public PdfPTableFooter getFooter() {
/* 2011 */     if (this.footer == null) {
/* 2012 */       this.footer = new PdfPTableFooter();
/*      */     }
/* 2014 */     return this.footer;
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
/*      */   public int getCellStartRowIndex(int rowIdx, int colIdx) {
/* 2027 */     int lastRow = rowIdx;
/* 2028 */     while (getRow(lastRow).getCells()[colIdx] == null && lastRow > 0) {
/* 2029 */       lastRow--;
/*      */     }
/* 2031 */     return lastRow;
/*      */   }
/*      */ 
/*      */   
/*      */   public static class FittingRows
/*      */   {
/*      */     public final int firstRow;
/*      */     
/*      */     public final int lastRow;
/*      */     
/*      */     public final float height;
/*      */     
/*      */     public final float completedRowsHeight;
/*      */     
/*      */     private final Map<Integer, Float> correctedHeightsForLastRow;
/*      */ 
/*      */     
/*      */     public FittingRows(int firstRow, int lastRow, float height, float completedRowsHeight, Map<Integer, Float> correctedHeightsForLastRow) {
/* 2049 */       this.firstRow = firstRow;
/* 2050 */       this.lastRow = lastRow;
/* 2051 */       this.height = height;
/* 2052 */       this.completedRowsHeight = completedRowsHeight;
/* 2053 */       this.correctedHeightsForLastRow = correctedHeightsForLastRow;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void correctLastRowChosen(PdfPTable table, int k) {
/* 2062 */       PdfPRow row = table.getRow(k);
/* 2063 */       Float value = this.correctedHeightsForLastRow.get(Integer.valueOf(k));
/* 2064 */       if (value != null) {
/* 2065 */         row.setFinalMaxHeights(value.floatValue());
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
/*      */   public static class ColumnMeasurementState
/*      */   {
/* 2078 */     public float height = 0.0F;
/*      */     
/* 2080 */     public int rowspan = 1, colspan = 1;
/*      */     
/*      */     public void beginCell(PdfPCell cell, float completedRowsHeight, float rowHeight) {
/* 2083 */       this.rowspan = cell.getRowspan();
/* 2084 */       this.colspan = cell.getColspan();
/* 2085 */       this.height = completedRowsHeight + Math.max(cell.hasCachedMaxHeight() ? cell.getCachedMaxHeight() : cell.getMaxHeight(), rowHeight);
/*      */     }
/*      */     
/*      */     public void consumeRowspan(float completedRowsHeight, float rowHeight) {
/* 2089 */       this.rowspan--;
/*      */     }
/*      */     
/*      */     public boolean cellEnds() {
/* 2093 */       return (this.rowspan == 1);
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
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public FittingRows getFittingRows(float availableHeight, int startIdx) {
/* 2113 */     if (this.LOGGER.isLogging(Level.INFO)) {
/* 2114 */       this.LOGGER.info(String.format("getFittingRows(%s, %s)", new Object[] { Float.valueOf(availableHeight), Integer.valueOf(startIdx) }));
/*      */     }
/* 2116 */     if (startIdx > 0 && startIdx < this.rows.size() && 
/* 2117 */       !$assertionsDisabled && getRow(startIdx).getCells()[0] == null) throw new AssertionError();
/*      */     
/* 2119 */     int cols = getNumberOfColumns();
/* 2120 */     ColumnMeasurementState[] states = new ColumnMeasurementState[cols];
/* 2121 */     for (int i = 0; i < cols; i++) {
/* 2122 */       states[i] = new ColumnMeasurementState();
/*      */     }
/* 2124 */     float completedRowsHeight = 0.0F;
/*      */     
/* 2126 */     float totalHeight = 0.0F;
/* 2127 */     Map<Integer, Float> correctedHeightsForLastRow = new HashMap<Integer, Float>();
/*      */     int k;
/* 2129 */     for (k = startIdx; k < size(); k++) {
/* 2130 */       PdfPRow row = getRow(k);
/* 2131 */       float rowHeight = row.getMaxRowHeightsWithoutCalculating();
/* 2132 */       float maxCompletedRowsHeight = 0.0F;
/* 2133 */       int j = 0;
/* 2134 */       while (j < cols) {
/* 2135 */         PdfPCell cell = row.getCells()[j];
/* 2136 */         ColumnMeasurementState state = states[j];
/* 2137 */         if (cell == null) {
/* 2138 */           state.consumeRowspan(completedRowsHeight, rowHeight);
/*      */         } else {
/* 2140 */           state.beginCell(cell, completedRowsHeight, rowHeight);
/* 2141 */           if (this.LOGGER.isLogging(Level.INFO)) {
/* 2142 */             this.LOGGER.info(String.format("Height after beginCell: %s (cell: %s)", new Object[] { Float.valueOf(state.height), Float.valueOf(cell.getCachedMaxHeight()) }));
/*      */           }
/*      */         } 
/* 2145 */         if (state.cellEnds() && state.height > maxCompletedRowsHeight) {
/* 2146 */           maxCompletedRowsHeight = state.height;
/*      */         }
/* 2148 */         for (int m = 1; m < state.colspan; m++) {
/* 2149 */           (states[j + m]).height = state.height;
/*      */         }
/* 2151 */         j += state.colspan;
/*      */       } 
/*      */       
/* 2154 */       float maxTotalHeight = 0.0F;
/* 2155 */       for (ColumnMeasurementState state : states) {
/* 2156 */         if (state.height > maxTotalHeight) {
/* 2157 */           maxTotalHeight = state.height;
/*      */         }
/*      */       } 
/* 2160 */       row.setFinalMaxHeights(maxCompletedRowsHeight - completedRowsHeight);
/*      */       
/* 2162 */       float remainingHeight = availableHeight - (isSplitLate() ? maxTotalHeight : maxCompletedRowsHeight);
/* 2163 */       if (remainingHeight < 0.0F) {
/*      */         break;
/*      */       }
/* 2166 */       correctedHeightsForLastRow.put(Integer.valueOf(k), Float.valueOf(maxTotalHeight - completedRowsHeight));
/* 2167 */       completedRowsHeight = maxCompletedRowsHeight;
/* 2168 */       totalHeight = maxTotalHeight;
/*      */     } 
/* 2170 */     this.rowsNotChecked = false;
/* 2171 */     return new FittingRows(startIdx, k - 1, totalHeight, completedRowsHeight, correctedHeightsForLastRow);
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/PdfPTable.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */