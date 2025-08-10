/*     */ package com.itextpdf.text.pdf;
/*     */ 
/*     */ import com.itextpdf.text.AccessibleElementId;
/*     */ import com.itextpdf.text.BaseColor;
/*     */ import com.itextpdf.text.DocumentException;
/*     */ import com.itextpdf.text.ExceptionConverter;
/*     */ import com.itextpdf.text.Image;
/*     */ import com.itextpdf.text.Phrase;
/*     */ import com.itextpdf.text.Rectangle;
/*     */ import com.itextpdf.text.log.Level;
/*     */ import com.itextpdf.text.log.Logger;
/*     */ import com.itextpdf.text.log.LoggerFactory;
/*     */ import com.itextpdf.text.pdf.interfaces.IAccessibleElement;
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
/*     */ public class PdfPRow
/*     */   implements IAccessibleElement
/*     */ {
/*  61 */   private final Logger LOGGER = LoggerFactory.getLogger(PdfPRow.class);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean mayNotBreak = false;
/*     */ 
/*     */ 
/*     */   
/*     */   public static final float BOTTOM_LIMIT = -1.07374182E9F;
/*     */ 
/*     */ 
/*     */   
/*     */   public static final float RIGHT_LIMIT = 20000.0F;
/*     */ 
/*     */ 
/*     */   
/*     */   protected PdfPCell[] cells;
/*     */ 
/*     */ 
/*     */   
/*     */   protected float[] widths;
/*     */ 
/*     */ 
/*     */   
/*     */   protected float[] extraHeights;
/*     */ 
/*     */ 
/*     */   
/*  90 */   protected float maxHeight = 0.0F;
/*     */   
/*     */   protected boolean calculated = false;
/*     */   
/*     */   protected boolean adjusted = false;
/*     */   
/*     */   private int[] canvasesPos;
/*  97 */   protected PdfName role = PdfName.TR;
/*  98 */   protected HashMap<PdfName, PdfObject> accessibleAttributes = null;
/*  99 */   protected AccessibleElementId id = new AccessibleElementId();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfPRow(PdfPCell[] cells) {
/* 108 */     this(cells, null);
/*     */   }
/*     */   
/*     */   public PdfPRow(PdfPCell[] cells, PdfPRow source) {
/* 112 */     this.cells = cells;
/* 113 */     this.widths = new float[cells.length];
/* 114 */     initExtraHeights();
/* 115 */     if (source != null) {
/* 116 */       this.id = source.id;
/* 117 */       this.role = source.role;
/* 118 */       if (source.accessibleAttributes != null) {
/* 119 */         this.accessibleAttributes = new HashMap<PdfName, PdfObject>(source.accessibleAttributes);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfPRow(PdfPRow row) {
/* 130 */     this.mayNotBreak = row.mayNotBreak;
/* 131 */     this.maxHeight = row.maxHeight;
/* 132 */     this.calculated = row.calculated;
/* 133 */     this.cells = new PdfPCell[row.cells.length];
/* 134 */     for (int k = 0; k < this.cells.length; k++) {
/* 135 */       if (row.cells[k] != null) {
/* 136 */         if (row.cells[k] instanceof PdfPHeaderCell) {
/* 137 */           this.cells[k] = new PdfPHeaderCell((PdfPHeaderCell)row.cells[k]);
/*     */         } else {
/* 139 */           this.cells[k] = new PdfPCell(row.cells[k]);
/*     */         } 
/*     */       }
/*     */     } 
/* 143 */     this.widths = new float[this.cells.length];
/* 144 */     System.arraycopy(row.widths, 0, this.widths, 0, this.cells.length);
/* 145 */     initExtraHeights();
/* 146 */     this.id = row.id;
/* 147 */     this.role = row.role;
/* 148 */     if (row.accessibleAttributes != null) {
/* 149 */       this.accessibleAttributes = new HashMap<PdfName, PdfObject>(row.accessibleAttributes);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean setWidths(float[] widths) {
/* 160 */     if (widths.length != this.cells.length) {
/* 161 */       return false;
/*     */     }
/* 163 */     System.arraycopy(widths, 0, this.widths, 0, this.cells.length);
/* 164 */     float total = 0.0F;
/* 165 */     this.calculated = false;
/* 166 */     for (int k = 0; k < widths.length; k++) {
/* 167 */       PdfPCell cell = this.cells[k];
/*     */       
/* 169 */       if (cell == null) {
/* 170 */         total += widths[k];
/*     */       }
/*     */       else {
/*     */         
/* 174 */         cell.setLeft(total);
/* 175 */         int last = k + cell.getColspan();
/* 176 */         for (; k < last; k++) {
/* 177 */           total += widths[k];
/*     */         }
/* 179 */         k--;
/* 180 */         cell.setRight(total);
/* 181 */         cell.setTop(0.0F);
/*     */       } 
/* 183 */     }  return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void initExtraHeights() {
/* 192 */     this.extraHeights = new float[this.cells.length];
/* 193 */     for (int i = 0; i < this.extraHeights.length; i++) {
/* 194 */       this.extraHeights[i] = 0.0F;
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
/*     */   public void setExtraHeight(int cell, float height) {
/* 206 */     if (cell < 0 || cell >= this.cells.length) {
/*     */       return;
/*     */     }
/* 209 */     this.extraHeights[cell] = height;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void calculateHeights() {
/* 216 */     this.maxHeight = 0.0F;
/* 217 */     this.LOGGER.info("calculateHeights");
/* 218 */     for (int k = 0; k < this.cells.length; k++) {
/* 219 */       PdfPCell cell = this.cells[k];
/* 220 */       float height = 0.0F;
/* 221 */       if (cell != null) {
/*     */ 
/*     */         
/* 224 */         if (cell.hasCalculatedHeight()) {
/* 225 */           height = cell.getCalculatedHeight();
/*     */         } else {
/*     */           
/* 228 */           height = cell.getMaxHeight();
/*     */         } 
/* 230 */         if (height > this.maxHeight && cell.getRowspan() == 1) {
/* 231 */           this.maxHeight = height;
/*     */         }
/*     */       } 
/*     */     } 
/* 235 */     this.calculated = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMayNotBreak(boolean mayNotBreak) {
/* 242 */     this.mayNotBreak = mayNotBreak;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isMayNotBreak() {
/* 249 */     return this.mayNotBreak;
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
/*     */   public void writeBorderAndBackground(float xPos, float yPos, float currentMaxHeight, PdfPCell cell, PdfContentByte[] canvases) {
/* 263 */     BaseColor background = cell.getBackgroundColor();
/* 264 */     if (background != null || cell.hasBorders()) {
/*     */       
/* 266 */       float right = cell.getRight() + xPos;
/* 267 */       float top = cell.getTop() + yPos;
/* 268 */       float left = cell.getLeft() + xPos;
/* 269 */       float bottom = top - currentMaxHeight;
/*     */       
/* 271 */       if (background != null) {
/* 272 */         PdfContentByte backgr = canvases[1];
/* 273 */         backgr.setColorFill(background);
/* 274 */         backgr.rectangle(left, bottom, right - left, top - bottom);
/* 275 */         backgr.fill();
/*     */       } 
/* 277 */       if (cell.hasBorders()) {
/* 278 */         Rectangle newRect = new Rectangle(left, bottom, right, top);
/*     */         
/* 280 */         newRect.cloneNonPositionParameters(cell);
/* 281 */         newRect.setBackgroundColor(null);
/*     */         
/* 283 */         PdfContentByte lineCanvas = canvases[2];
/* 284 */         lineCanvas.rectangle(newRect);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void saveAndRotateCanvases(PdfContentByte[] canvases, float a, float b, float c, float d, float e, float f) {
/* 293 */     int last = 4;
/* 294 */     if (this.canvasesPos == null) {
/* 295 */       this.canvasesPos = new int[last * 2];
/*     */     }
/* 297 */     for (int k = 0; k < last; k++) {
/* 298 */       ByteBuffer bb = canvases[k].getInternalBuffer();
/* 299 */       this.canvasesPos[k * 2] = bb.size();
/* 300 */       canvases[k].saveState();
/* 301 */       canvases[k].concatCTM(a, b, c, d, e, f);
/* 302 */       this.canvasesPos[k * 2 + 1] = bb.size();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void restoreCanvases(PdfContentByte[] canvases) {
/* 310 */     int last = 4;
/* 311 */     for (int k = 0; k < last; k++) {
/* 312 */       ByteBuffer bb = canvases[k].getInternalBuffer();
/* 313 */       int p1 = bb.size();
/* 314 */       canvases[k].restoreState();
/* 315 */       if (p1 == this.canvasesPos[k * 2 + 1]) {
/* 316 */         bb.setSize(this.canvasesPos[k * 2]);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static float setColumn(ColumnText ct, float left, float bottom, float right, float top) {
/* 325 */     if (left > right) {
/* 326 */       right = left;
/*     */     }
/* 328 */     if (bottom > top) {
/* 329 */       top = bottom;
/*     */     }
/* 331 */     ct.setSimpleColumn(left, bottom, right, top);
/* 332 */     return top;
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
/*     */   public void writeCells(int colStart, int colEnd, float xPos, float yPos, PdfContentByte[] canvases, boolean reusable) {
/* 350 */     if (!this.calculated) {
/* 351 */       calculateHeights();
/*     */     }
/* 353 */     if (colEnd < 0) {
/* 354 */       colEnd = this.cells.length;
/*     */     } else {
/* 356 */       colEnd = Math.min(colEnd, this.cells.length);
/*     */     } 
/* 358 */     if (colStart < 0) {
/* 359 */       colStart = 0;
/*     */     }
/* 361 */     if (colStart >= colEnd) {
/*     */       return;
/*     */     }
/*     */     
/*     */     int newStart;
/* 366 */     for (newStart = colStart; newStart >= 0 && 
/* 367 */       this.cells[newStart] == null; newStart--) {
/*     */ 
/*     */       
/* 370 */       if (newStart > 0) {
/* 371 */         xPos -= this.widths[newStart - 1];
/*     */       }
/*     */     } 
/*     */     
/* 375 */     if (newStart < 0) {
/* 376 */       newStart = 0;
/*     */     }
/* 378 */     if (this.cells[newStart] != null) {
/* 379 */       xPos -= this.cells[newStart].getLeft();
/*     */     }
/*     */     
/* 382 */     if (isTagged(canvases[3])) {
/* 383 */       canvases[3].openMCBlock(this);
/*     */     }
/* 385 */     for (int k = newStart; k < colEnd; k++) {
/* 386 */       PdfPCell cell = this.cells[k];
/* 387 */       if (cell == null) {
/*     */         continue;
/*     */       }
/* 390 */       if (isTagged(canvases[3])) {
/* 391 */         canvases[3].openMCBlock(cell);
/*     */       }
/* 393 */       float currentMaxHeight = this.maxHeight + this.extraHeights[k];
/*     */       
/* 395 */       writeBorderAndBackground(xPos, yPos, currentMaxHeight, cell, canvases);
/*     */       
/* 397 */       Image img = cell.getImage();
/*     */       
/* 399 */       float tly = cell.getTop() + yPos - cell.getEffectivePaddingTop();
/* 400 */       if (cell.getHeight() <= currentMaxHeight) {
/* 401 */         switch (cell.getVerticalAlignment()) {
/*     */           
/*     */           case 6:
/* 404 */             tly = cell.getTop() + yPos - currentMaxHeight + cell.getHeight() - cell.getEffectivePaddingTop();
/*     */             break;
/*     */           
/*     */           case 5:
/* 408 */             tly = cell.getTop() + yPos + (cell.getHeight() - currentMaxHeight) / 2.0F - cell.getEffectivePaddingTop();
/*     */             break;
/*     */         } 
/*     */ 
/*     */       
/*     */       }
/* 414 */       if (img != null) {
/* 415 */         if (cell.getRotation() != 0) {
/* 416 */           img = Image.getInstance(img);
/* 417 */           img.setRotation(img.getImageRotation() + (float)(cell.getRotation() * Math.PI / 180.0D));
/*     */         } 
/* 419 */         boolean vf = false;
/* 420 */         if (cell.getHeight() > currentMaxHeight) {
/* 421 */           if (!img.isScaleToFitHeight()) {
/*     */             continue;
/*     */           }
/* 424 */           img.scalePercent(100.0F);
/*     */ 
/*     */           
/* 427 */           float scale = (currentMaxHeight - cell.getEffectivePaddingTop() - cell.getEffectivePaddingBottom()) / img.getScaledHeight();
/* 428 */           img.scalePercent(scale * 100.0F);
/* 429 */           vf = true;
/*     */         } 
/*     */         
/* 432 */         float left = cell.getLeft() + xPos + cell.getEffectivePaddingLeft();
/* 433 */         if (vf) {
/* 434 */           switch (cell.getHorizontalAlignment()) {
/*     */ 
/*     */ 
/*     */ 
/*     */             
/*     */             case 1:
/* 440 */               left = xPos + (cell.getLeft() + cell.getEffectivePaddingLeft() + cell.getRight() - cell.getEffectivePaddingRight() - img.getScaledWidth()) / 2.0F;
/*     */               break;
/*     */ 
/*     */             
/*     */             case 2:
/* 445 */               left = xPos + cell.getRight() - cell.getEffectivePaddingRight() - img.getScaledWidth();
/*     */               break;
/*     */           } 
/*     */ 
/*     */           
/* 450 */           tly = cell.getTop() + yPos - cell.getEffectivePaddingTop();
/*     */         } 
/* 452 */         img.setAbsolutePosition(left, tly - img.getScaledHeight());
/*     */         try {
/* 454 */           if (isTagged(canvases[3])) {
/* 455 */             canvases[3].openMCBlock((IAccessibleElement)img);
/*     */           }
/* 457 */           canvases[3].addImage(img);
/* 458 */           if (isTagged(canvases[3])) {
/* 459 */             canvases[3].closeMCBlock((IAccessibleElement)img);
/*     */           }
/* 461 */         } catch (DocumentException e) {
/* 462 */           throw new ExceptionConverter(e);
/*     */         }
/*     */       
/*     */       }
/* 466 */       else if (cell.getRotation() == 90 || cell.getRotation() == 270) {
/* 467 */         float netWidth = currentMaxHeight - cell.getEffectivePaddingTop() - cell.getEffectivePaddingBottom();
/* 468 */         float netHeight = cell.getWidth() - cell.getEffectivePaddingLeft() - cell.getEffectivePaddingRight();
/* 469 */         ColumnText ct = ColumnText.duplicate(cell.getColumn());
/* 470 */         ct.setCanvases(canvases);
/* 471 */         ct.setSimpleColumn(0.0F, 0.0F, netWidth + 0.001F, -netHeight);
/*     */         try {
/* 473 */           ct.go(true);
/* 474 */         } catch (DocumentException e) {
/* 475 */           throw new ExceptionConverter(e);
/*     */         } 
/* 477 */         float calcHeight = -ct.getYLine();
/* 478 */         if (netWidth <= 0.0F || netHeight <= 0.0F) {
/* 479 */           calcHeight = 0.0F;
/*     */         }
/* 481 */         if (calcHeight > 0.0F) {
/* 482 */           if (cell.isUseDescender()) {
/* 483 */             calcHeight -= ct.getDescender();
/*     */           }
/* 485 */           if (reusable) {
/* 486 */             ct = ColumnText.duplicate(cell.getColumn());
/*     */           } else {
/* 488 */             ct = cell.getColumn();
/*     */           } 
/* 490 */           ct.setCanvases(canvases);
/* 491 */           ct.setSimpleColumn(-0.003F, -0.001F, netWidth + 0.003F, calcHeight);
/*     */ 
/*     */           
/* 494 */           if (cell.getRotation() == 90) {
/* 495 */             float pivotX, pivotY = cell.getTop() + yPos - currentMaxHeight + cell.getEffectivePaddingBottom();
/* 496 */             switch (cell.getVerticalAlignment()) {
/*     */               case 6:
/* 498 */                 pivotX = cell.getLeft() + xPos + cell.getWidth() - cell.getEffectivePaddingRight();
/*     */                 break;
/*     */               case 5:
/* 501 */                 pivotX = cell.getLeft() + xPos + (cell.getWidth() + cell.getEffectivePaddingLeft() - cell.getEffectivePaddingRight() + calcHeight) / 2.0F;
/*     */                 break;
/*     */               default:
/* 504 */                 pivotX = cell.getLeft() + xPos + cell.getEffectivePaddingLeft() + calcHeight;
/*     */                 break;
/*     */             } 
/* 507 */             saveAndRotateCanvases(canvases, 0.0F, 1.0F, -1.0F, 0.0F, pivotX, pivotY);
/*     */           } else {
/* 509 */             float pivotX, pivotY = cell.getTop() + yPos - cell.getEffectivePaddingTop();
/* 510 */             switch (cell.getVerticalAlignment()) {
/*     */               case 6:
/* 512 */                 pivotX = cell.getLeft() + xPos + cell.getEffectivePaddingLeft();
/*     */                 break;
/*     */               case 5:
/* 515 */                 pivotX = cell.getLeft() + xPos + (cell.getWidth() + cell.getEffectivePaddingLeft() - cell.getEffectivePaddingRight() - calcHeight) / 2.0F;
/*     */                 break;
/*     */               default:
/* 518 */                 pivotX = cell.getLeft() + xPos + cell.getWidth() - cell.getEffectivePaddingRight() - calcHeight;
/*     */                 break;
/*     */             } 
/* 521 */             saveAndRotateCanvases(canvases, 0.0F, -1.0F, 1.0F, 0.0F, pivotX, pivotY);
/*     */           } 
/*     */           try {
/* 524 */             ct.go();
/* 525 */           } catch (DocumentException e) {
/* 526 */             throw new ExceptionConverter(e);
/*     */           } finally {
/* 528 */             restoreCanvases(canvases);
/*     */           } 
/*     */         } 
/*     */       } else {
/* 532 */         ColumnText ct; float fixedHeight = cell.getFixedHeight();
/*     */         
/* 534 */         float rightLimit = cell.getRight() + xPos - cell.getEffectivePaddingRight();
/*     */         
/* 536 */         float leftLimit = cell.getLeft() + xPos + cell.getEffectivePaddingLeft();
/* 537 */         if (cell.isNoWrap()) {
/* 538 */           switch (cell.getHorizontalAlignment()) {
/*     */             case 1:
/* 540 */               rightLimit += 10000.0F;
/* 541 */               leftLimit -= 10000.0F;
/*     */               break;
/*     */             case 2:
/* 544 */               if (cell.getRotation() == 180) {
/* 545 */                 rightLimit += 20000.0F; break;
/*     */               } 
/* 547 */               leftLimit -= 20000.0F;
/*     */               break;
/*     */             
/*     */             default:
/* 551 */               if (cell.getRotation() == 180) {
/* 552 */                 leftLimit -= 20000.0F; break;
/*     */               } 
/* 554 */               rightLimit += 20000.0F;
/*     */               break;
/*     */           } 
/*     */ 
/*     */         
/*     */         }
/* 560 */         if (reusable) {
/* 561 */           ct = ColumnText.duplicate(cell.getColumn());
/*     */         } else {
/* 563 */           ct = cell.getColumn();
/*     */         } 
/* 565 */         ct.setCanvases(canvases);
/*     */ 
/*     */         
/* 568 */         float bry = tly - currentMaxHeight - cell.getEffectivePaddingTop() - cell.getEffectivePaddingBottom();
/* 569 */         if (fixedHeight > 0.0F && 
/* 570 */           cell.getHeight() > currentMaxHeight) {
/* 571 */           tly = cell.getTop() + yPos - cell.getEffectivePaddingTop();
/* 572 */           bry = cell.getTop() + yPos - currentMaxHeight + cell.getEffectivePaddingBottom();
/*     */         } 
/*     */         
/* 575 */         if ((tly > bry || ct.zeroHeightElement()) && leftLimit < rightLimit) {
/* 576 */           ct.setSimpleColumn(leftLimit, bry - 0.001F, rightLimit, tly);
/* 577 */           if (cell.getRotation() == 180) {
/* 578 */             float shx = leftLimit + rightLimit;
/* 579 */             float shy = yPos + yPos - currentMaxHeight + cell.getEffectivePaddingBottom() - cell.getEffectivePaddingTop();
/* 580 */             saveAndRotateCanvases(canvases, -1.0F, 0.0F, 0.0F, -1.0F, shx, shy);
/*     */           } 
/*     */           try {
/* 583 */             ct.go();
/* 584 */           } catch (DocumentException e) {
/* 585 */             throw new ExceptionConverter(e);
/*     */           } finally {
/* 587 */             if (cell.getRotation() == 180) {
/* 588 */               restoreCanvases(canvases);
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/*     */       
/* 594 */       PdfPCellEvent evt = cell.getCellEvent();
/* 595 */       if (evt != null) {
/*     */         
/* 597 */         Rectangle rect = new Rectangle(cell.getLeft() + xPos, cell.getTop() + yPos - currentMaxHeight, cell.getRight() + xPos, cell.getTop() + yPos);
/*     */         
/* 599 */         evt.cellLayout(cell, rect, canvases);
/*     */       } 
/* 601 */       if (isTagged(canvases[3]))
/* 602 */         canvases[3].closeMCBlock(cell); 
/*     */       continue;
/*     */     } 
/* 605 */     if (isTagged(canvases[3])) {
/* 606 */       canvases[3].closeMCBlock(this);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isCalculated() {
/* 616 */     return this.calculated;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getMaxHeights() {
/* 625 */     if (!this.calculated) {
/* 626 */       calculateHeights();
/*     */     }
/* 628 */     return this.maxHeight;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMaxHeights(float maxHeight) {
/* 638 */     this.maxHeight = maxHeight;
/*     */   }
/*     */ 
/*     */   
/*     */   float[] getEventWidth(float xPos, float[] absoluteWidths) {
/* 643 */     int n = 1;
/* 644 */     for (int k = 0; k < this.cells.length; ) {
/* 645 */       if (this.cells[k] != null) {
/* 646 */         n++;
/* 647 */         k += this.cells[k].getColspan(); continue;
/*     */       } 
/* 649 */       while (k < this.cells.length && this.cells[k] == null) {
/* 650 */         n++;
/* 651 */         k++;
/*     */       } 
/*     */     } 
/*     */     
/* 655 */     float[] width = new float[n];
/* 656 */     width[0] = xPos;
/* 657 */     n = 1;
/* 658 */     for (int i = 0; i < this.cells.length && n < width.length; ) {
/* 659 */       if (this.cells[i] != null) {
/* 660 */         int colspan = this.cells[i].getColspan();
/* 661 */         width[n] = width[n - 1];
/* 662 */         for (int j = 0; j < colspan && i < absoluteWidths.length; j++) {
/* 663 */           width[n] = width[n] + absoluteWidths[i++];
/*     */         }
/* 665 */         n++; continue;
/*     */       } 
/* 667 */       width[n] = width[n - 1];
/* 668 */       while (i < this.cells.length && this.cells[i] == null) {
/* 669 */         width[n] = width[n] + absoluteWidths[i++];
/*     */       }
/* 671 */       n++;
/*     */     } 
/*     */     
/* 674 */     return width;
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
/*     */   public void copyRowContent(PdfPTable table, int idx) {
/* 686 */     if (table == null) {
/*     */       return;
/*     */     }
/*     */     
/* 690 */     for (int i = 0; i < this.cells.length; i++) {
/* 691 */       int lastRow = idx;
/* 692 */       PdfPCell copy = table.getRow(lastRow).getCells()[i];
/* 693 */       while (copy == null && lastRow > 0) {
/* 694 */         copy = table.getRow(--lastRow).getCells()[i];
/*     */       }
/* 696 */       if (this.cells[i] != null && copy != null) {
/* 697 */         this.cells[i].setColumn(copy.getColumn());
/* 698 */         this.calculated = false;
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
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfPRow splitRow(PdfPTable table, int rowIndex, float new_height) {
/* 713 */     if (this.LOGGER.isLogging(Level.INFO)) {
/* 714 */       this.LOGGER.info(String.format("Splitting row %s available height: %s", new Object[] { Integer.valueOf(rowIndex), Float.valueOf(new_height) }));
/*     */     }
/*     */     
/* 717 */     PdfPCell[] newCells = new PdfPCell[this.cells.length];
/* 718 */     float[] calHs = new float[this.cells.length];
/* 719 */     float[] fixHs = new float[this.cells.length];
/* 720 */     float[] minHs = new float[this.cells.length];
/* 721 */     boolean allEmpty = true;
/*     */     int k;
/* 723 */     for (k = 0; k < this.cells.length; k++) {
/* 724 */       float newHeight = new_height;
/* 725 */       PdfPCell cell = this.cells[k];
/* 726 */       if (cell == null) {
/* 727 */         int index = rowIndex;
/* 728 */         if (table.rowSpanAbove(index, k)) {
/* 729 */           while (table.rowSpanAbove(--index, k)) {
/* 730 */             newHeight += table.getRow(index).getMaxHeights();
/*     */           }
/* 732 */           PdfPRow row = table.getRow(index);
/* 733 */           if (row != null && row.getCells()[k] != null) {
/* 734 */             newCells[k] = new PdfPCell(row.getCells()[k]);
/* 735 */             newCells[k].setColumn((ColumnText)null);
/* 736 */             newCells[k].setRowspan(row.getCells()[k].getRowspan() - rowIndex + index);
/* 737 */             allEmpty = false;
/*     */           } 
/*     */         } 
/*     */       } else {
/*     */         
/* 742 */         calHs[k] = cell.getCalculatedHeight();
/* 743 */         fixHs[k] = cell.getFixedHeight();
/* 744 */         minHs[k] = cell.getMinimumHeight();
/* 745 */         Image img = cell.getImage();
/* 746 */         PdfPCell newCell = new PdfPCell(cell);
/* 747 */         if (img != null) {
/* 748 */           float padding = cell.getEffectivePaddingBottom() + cell.getEffectivePaddingTop() + 2.0F;
/* 749 */           if ((img.isScaleToFitHeight() || img.getScaledHeight() + padding < newHeight) && newHeight > padding) {
/*     */             
/* 751 */             newCell.setPhrase((Phrase)null);
/* 752 */             allEmpty = false;
/*     */           } 
/*     */         } else {
/*     */           float y; int status;
/* 756 */           ColumnText ct = ColumnText.duplicate(cell.getColumn());
/* 757 */           float left = cell.getLeft() + cell.getEffectivePaddingLeft();
/* 758 */           float bottom = cell.getTop() + cell.getEffectivePaddingBottom() - newHeight;
/* 759 */           float right = cell.getRight() - cell.getEffectivePaddingRight();
/* 760 */           float top = cell.getTop() - cell.getEffectivePaddingTop();
/* 761 */           switch (cell.getRotation()) {
/*     */             case 90:
/*     */             case 270:
/* 764 */               y = setColumn(ct, bottom, left, top, right);
/*     */               break;
/*     */             default:
/* 767 */               y = setColumn(ct, left, bottom + 1.0E-5F, cell.isNoWrap() ? 20000.0F : right, top);
/*     */               break;
/*     */           } 
/*     */           
/*     */           try {
/* 772 */             status = ct.go(true);
/* 773 */           } catch (DocumentException e) {
/* 774 */             throw new ExceptionConverter(e);
/*     */           } 
/* 776 */           boolean thisEmpty = (ct.getYLine() == y);
/* 777 */           if (thisEmpty) {
/* 778 */             newCell.setColumn(ColumnText.duplicate(cell.getColumn()));
/* 779 */             ct.setFilledWidth(0.0F);
/* 780 */           } else if ((status & 0x1) == 0) {
/* 781 */             newCell.setColumn(ct);
/* 782 */             ct.setFilledWidth(0.0F);
/*     */           } else {
/* 784 */             newCell.setPhrase((Phrase)null);
/*     */           } 
/* 786 */           allEmpty = (allEmpty && thisEmpty);
/*     */         } 
/* 788 */         newCells[k] = newCell;
/* 789 */         cell.setCalculatedHeight(newHeight);
/*     */       } 
/* 791 */     }  if (allEmpty) {
/* 792 */       for (k = 0; k < this.cells.length; k++) {
/* 793 */         PdfPCell cell = this.cells[k];
/* 794 */         if (cell != null) {
/*     */ 
/*     */           
/* 797 */           cell.setCalculatedHeight(calHs[k]);
/* 798 */           if (fixHs[k] > 0.0F) {
/* 799 */             cell.setFixedHeight(fixHs[k]);
/*     */           } else {
/* 801 */             cell.setMinimumHeight(minHs[k]);
/*     */           } 
/*     */         } 
/* 804 */       }  return null;
/*     */     } 
/* 806 */     calculateHeights();
/* 807 */     PdfPRow split = new PdfPRow(newCells, this);
/* 808 */     split.widths = (float[])this.widths.clone();
/* 809 */     return split;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getMaxRowHeightsWithoutCalculating() {
/* 814 */     return this.maxHeight;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setFinalMaxHeights(float maxHeight) {
/* 819 */     setMaxHeights(maxHeight);
/* 820 */     this.calculated = true;
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
/*     */   public void splitRowspans(PdfPTable original, int originalIdx, PdfPTable part, int partIdx) {
/* 834 */     if (original == null || part == null) {
/*     */       return;
/*     */     }
/* 837 */     int i = 0;
/* 838 */     while (i < this.cells.length) {
/* 839 */       if (this.cells[i] == null) {
/* 840 */         int splittedRowIdx = original.getCellStartRowIndex(originalIdx, i);
/* 841 */         int copyRowIdx = part.getCellStartRowIndex(partIdx, i);
/*     */         
/* 843 */         PdfPCell splitted = original.getRow(splittedRowIdx).getCells()[i];
/*     */         
/* 845 */         PdfPCell copy = part.getRow(copyRowIdx).getCells()[i];
/* 846 */         if (splitted != null) {
/* 847 */           assert copy != null;
/* 848 */           this.cells[i] = new PdfPCell(copy);
/* 849 */           int rowspanOnPreviousPage = partIdx - copyRowIdx + 1;
/* 850 */           this.cells[i].setRowspan(copy.getRowspan() - rowspanOnPreviousPage);
/* 851 */           splitted.setRowspan(rowspanOnPreviousPage);
/* 852 */           this.calculated = false;
/*     */         } 
/* 854 */         i++; continue;
/*     */       } 
/* 856 */       i += this.cells[i].getColspan();
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
/*     */   public PdfPCell[] getCells() {
/* 869 */     return this.cells;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasRowspan() {
/* 878 */     for (int i = 0; i < this.cells.length; i++) {
/* 879 */       if (this.cells[i] != null && this.cells[i].getRowspan() > 1) {
/* 880 */         return true;
/*     */       }
/*     */     } 
/* 883 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isAdjusted() {
/* 887 */     return this.adjusted;
/*     */   }
/*     */   
/*     */   public void setAdjusted(boolean adjusted) {
/* 891 */     this.adjusted = adjusted;
/*     */   }
/*     */   
/*     */   public PdfObject getAccessibleAttribute(PdfName key) {
/* 895 */     if (this.accessibleAttributes != null) {
/* 896 */       return this.accessibleAttributes.get(key);
/*     */     }
/* 898 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setAccessibleAttribute(PdfName key, PdfObject value) {
/* 903 */     if (this.accessibleAttributes == null) {
/* 904 */       this.accessibleAttributes = new HashMap<PdfName, PdfObject>();
/*     */     }
/* 906 */     this.accessibleAttributes.put(key, value);
/*     */   }
/*     */   
/*     */   public HashMap<PdfName, PdfObject> getAccessibleAttributes() {
/* 910 */     return this.accessibleAttributes;
/*     */   }
/*     */   
/*     */   public PdfName getRole() {
/* 914 */     return this.role;
/*     */   }
/*     */   
/*     */   public void setRole(PdfName role) {
/* 918 */     this.role = role;
/*     */   }
/*     */   
/*     */   public AccessibleElementId getId() {
/* 922 */     return this.id;
/*     */   }
/*     */   
/*     */   public void setId(AccessibleElementId id) {
/* 926 */     this.id = id;
/*     */   }
/*     */   
/*     */   private static boolean isTagged(PdfContentByte canvas) {
/* 930 */     return (canvas != null && canvas.writer != null && canvas.writer.isTagged());
/*     */   }
/*     */   
/*     */   public boolean isInline() {
/* 934 */     return false;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/PdfPRow.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */