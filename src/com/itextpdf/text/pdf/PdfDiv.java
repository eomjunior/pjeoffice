/*     */ package com.itextpdf.text.pdf;
/*     */ 
/*     */ import com.itextpdf.awt.geom.AffineTransform;
/*     */ import com.itextpdf.text.AccessibleElementId;
/*     */ import com.itextpdf.text.BaseColor;
/*     */ import com.itextpdf.text.Chunk;
/*     */ import com.itextpdf.text.DocumentException;
/*     */ import com.itextpdf.text.Element;
/*     */ import com.itextpdf.text.ElementListener;
/*     */ import com.itextpdf.text.Image;
/*     */ import com.itextpdf.text.Rectangle;
/*     */ import com.itextpdf.text.api.Spaceable;
/*     */ import com.itextpdf.text.pdf.interfaces.IAccessibleElement;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
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
/*     */ public class PdfDiv
/*     */   implements Element, Spaceable, IAccessibleElement
/*     */ {
/*     */   private ArrayList<Element> content;
/*     */   
/*     */   public enum FloatType
/*     */   {
/*  59 */     NONE, LEFT, RIGHT; }
/*     */   
/*  61 */   public enum PositionType { STATIC, ABSOLUTE, FIXED, RELATIVE; }
/*     */   
/*  63 */   public enum DisplayType { NONE, BLOCK, INLINE, INLINE_BLOCK, INLINE_TABLE, LIST_ITEM, RUN_IN, TABLE, TABLE_CAPTION, TABLE_CELL, TABLE_COLUMN_GROUP, TABLE_COLUMN, TABLE_FOOTER_GROUP,
/*  64 */     TABLE_HEADER_GROUP, TABLE_ROW, TABLE_ROW_GROUP; }
/*     */   
/*  66 */   public enum BorderTopStyle { DOTTED, DASHED, SOLID, DOUBLE, GROOVE, RIDGE, INSET, OUTSET; }
/*     */ 
/*     */ 
/*     */   
/*  70 */   private Float left = null;
/*     */   
/*  72 */   private Float top = null;
/*     */   
/*  74 */   private Float right = null;
/*     */   
/*  76 */   private Float bottom = null;
/*     */   
/*  78 */   private Float width = null;
/*     */   
/*  80 */   private Float height = null;
/*     */   
/*  82 */   private Float percentageHeight = null;
/*     */   
/*  84 */   private Float percentageWidth = null;
/*     */   
/*  86 */   private float contentWidth = 0.0F;
/*     */   
/*  88 */   private float contentHeight = 0.0F;
/*     */   
/*  90 */   private int textAlignment = -1;
/*     */   
/*  92 */   private float paddingLeft = 0.0F;
/*     */   
/*  94 */   private float paddingRight = 0.0F;
/*     */   
/*  96 */   private float paddingTop = 0.0F;
/*     */   
/*  98 */   private float paddingBottom = 0.0F;
/*     */   
/* 100 */   private FloatType floatType = FloatType.NONE;
/*     */   
/* 102 */   private PositionType position = PositionType.STATIC;
/*     */   
/*     */   private DisplayType display;
/*     */   
/* 106 */   private FloatLayout floatLayout = null;
/*     */   
/*     */   private BorderTopStyle borderTopStyle;
/*     */   
/*     */   private float yLine;
/*     */   
/* 112 */   protected int runDirection = 1;
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean keepTogether;
/*     */ 
/*     */   
/* 119 */   protected PdfName role = PdfName.DIV;
/* 120 */   protected HashMap<PdfName, PdfObject> accessibleAttributes = null;
/* 121 */   protected AccessibleElementId id = new AccessibleElementId();
/*     */   
/*     */   public float getContentWidth() {
/* 124 */     return this.contentWidth;
/*     */   }
/*     */   
/*     */   public void setContentWidth(float contentWidth) {
/* 128 */     this.contentWidth = contentWidth;
/*     */   }
/*     */   
/*     */   public float getContentHeight() {
/* 132 */     return this.contentHeight;
/*     */   }
/*     */   
/*     */   public void setContentHeight(float contentHeight) {
/* 136 */     this.contentHeight = contentHeight;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getActualHeight() {
/* 147 */     return (this.height != null && this.height.floatValue() >= this.contentHeight) ? this.height.floatValue() : this.contentHeight;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getActualWidth() {
/* 157 */     return (this.width != null && this.width.floatValue() >= this.contentWidth) ? this.width.floatValue() : this.contentWidth;
/*     */   }
/*     */   
/*     */   public Float getPercentageHeight() {
/* 161 */     return this.percentageHeight;
/*     */   }
/*     */   
/*     */   public void setPercentageHeight(Float percentageHeight) {
/* 165 */     this.percentageHeight = percentageHeight;
/*     */   }
/*     */   
/*     */   public Float getPercentageWidth() {
/* 169 */     return this.percentageWidth;
/*     */   }
/*     */   
/*     */   public void setPercentageWidth(Float percentageWidth) {
/* 173 */     this.percentageWidth = percentageWidth;
/*     */   }
/*     */ 
/*     */   
/*     */   public DisplayType getDisplay() {
/* 178 */     return this.display;
/*     */   }
/*     */   
/*     */   public void setDisplay(DisplayType display) {
/* 182 */     this.display = display;
/*     */   }
/*     */ 
/*     */   
/*     */   public BaseColor getBackgroundColor() {
/* 187 */     return this.backgroundColor;
/*     */   }
/*     */   
/*     */   public void setBackgroundColor(BaseColor backgroundColor) {
/* 191 */     this.backgroundColor = backgroundColor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBackgroundImage(Image image) {
/* 198 */     this.backgroundImage = image;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBackgroundImage(Image image, float width, float height) {
/* 205 */     this.backgroundImage = image;
/* 206 */     this.backgroundImageWidth = Float.valueOf(width);
/* 207 */     this.backgroundImageHeight = Float.valueOf(height);
/*     */   }
/*     */   
/*     */   public float getYLine() {
/* 211 */     return this.yLine;
/*     */   }
/*     */   
/*     */   public int getRunDirection() {
/* 215 */     return this.runDirection;
/*     */   }
/*     */   
/*     */   public void setRunDirection(int runDirection) {
/* 219 */     this.runDirection = runDirection;
/*     */   }
/*     */   
/*     */   public boolean getKeepTogether() {
/* 223 */     return this.keepTogether;
/*     */   }
/*     */   
/*     */   public void setKeepTogether(boolean keepTogether) {
/* 227 */     this.keepTogether = keepTogether;
/*     */   }
/*     */   
/* 230 */   private BaseColor backgroundColor = null;
/*     */ 
/*     */   
/*     */   private Image backgroundImage;
/*     */ 
/*     */   
/*     */   private Float backgroundImageWidth;
/*     */ 
/*     */   
/*     */   private Float backgroundImageHeight;
/*     */   
/*     */   protected float spacingBefore;
/*     */   
/*     */   protected float spacingAfter;
/*     */ 
/*     */   
/*     */   public PdfDiv() {
/* 247 */     this.content = new ArrayList<Element>();
/* 248 */     this.keepTogether = false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Chunk> getChunks() {
/* 257 */     return new ArrayList<Chunk>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int type() {
/* 266 */     return 37;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isContent() {
/* 274 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isNestable() {
/* 282 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean process(ElementListener listener) {
/*     */     try {
/* 294 */       return listener.add(this);
/*     */     }
/* 296 */     catch (DocumentException de) {
/* 297 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSpacingBefore(float spacing) {
/* 307 */     this.spacingBefore = spacing;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSpacingAfter(float spacing) {
/* 316 */     this.spacingAfter = spacing;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getSpacingBefore() {
/* 325 */     return this.spacingBefore;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getSpacingAfter() {
/* 334 */     return this.spacingAfter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getTextAlignment() {
/* 343 */     return this.textAlignment;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTextAlignment(int textAlignment) {
/* 353 */     this.textAlignment = textAlignment;
/*     */   }
/*     */   
/*     */   public void addElement(Element element) {
/* 357 */     this.content.add(element);
/*     */   }
/*     */   
/*     */   public Float getLeft() {
/* 361 */     return this.left;
/*     */   }
/*     */   
/*     */   public void setLeft(Float left) {
/* 365 */     this.left = left;
/*     */   }
/*     */   
/*     */   public Float getRight() {
/* 369 */     return this.right;
/*     */   }
/*     */   
/*     */   public void setRight(Float right) {
/* 373 */     this.right = right;
/*     */   }
/*     */   
/*     */   public Float getTop() {
/* 377 */     return this.top;
/*     */   }
/*     */   
/*     */   public void setTop(Float top) {
/* 381 */     this.top = top;
/*     */   }
/*     */   
/*     */   public Float getBottom() {
/* 385 */     return this.bottom;
/*     */   }
/*     */   
/*     */   public void setBottom(Float bottom) {
/* 389 */     this.bottom = bottom;
/*     */   }
/*     */   
/*     */   public Float getWidth() {
/* 393 */     return this.width;
/*     */   }
/*     */   
/*     */   public void setWidth(Float width) {
/* 397 */     this.width = width;
/*     */   }
/*     */   
/*     */   public Float getHeight() {
/* 401 */     return this.height;
/*     */   }
/*     */   
/*     */   public void setHeight(Float height) {
/* 405 */     this.height = height;
/*     */   }
/*     */   
/*     */   public float getPaddingLeft() {
/* 409 */     return this.paddingLeft;
/*     */   }
/*     */   
/*     */   public void setPaddingLeft(float paddingLeft) {
/* 413 */     this.paddingLeft = paddingLeft;
/*     */   }
/*     */   
/*     */   public float getPaddingRight() {
/* 417 */     return this.paddingRight;
/*     */   }
/*     */   
/*     */   public void setPaddingRight(float paddingRight) {
/* 421 */     this.paddingRight = paddingRight;
/*     */   }
/*     */   
/*     */   public float getPaddingTop() {
/* 425 */     return this.paddingTop;
/*     */   }
/*     */   
/*     */   public void setPaddingTop(float paddingTop) {
/* 429 */     this.paddingTop = paddingTop;
/*     */   }
/*     */   
/*     */   public float getPaddingBottom() {
/* 433 */     return this.paddingBottom;
/*     */   }
/*     */   
/*     */   public void setPaddingBottom(float paddingBottom) {
/* 437 */     this.paddingBottom = paddingBottom;
/*     */   }
/*     */   
/*     */   public FloatType getFloatType() {
/* 441 */     return this.floatType;
/*     */   }
/*     */   
/*     */   public void setFloatType(FloatType floatType) {
/* 445 */     this.floatType = floatType;
/*     */   }
/*     */   
/*     */   public PositionType getPosition() {
/* 449 */     return this.position;
/*     */   }
/*     */   
/*     */   public void setPosition(PositionType position) {
/* 453 */     this.position = position;
/*     */   }
/*     */   
/*     */   public ArrayList<Element> getContent() {
/* 457 */     return this.content;
/*     */   }
/*     */   
/*     */   public void setContent(ArrayList<Element> content) {
/* 461 */     this.content = content;
/*     */   }
/*     */   
/*     */   public BorderTopStyle getBorderTopStyle() {
/* 465 */     return this.borderTopStyle;
/*     */   }
/*     */   
/*     */   public void setBorderTopStyle(BorderTopStyle borderTopStyle) {
/* 469 */     this.borderTopStyle = borderTopStyle;
/*     */   }
/*     */ 
/*     */   
/*     */   public int layout(PdfContentByte canvas, boolean useAscender, boolean simulate, float llx, float lly, float urx, float ury) throws DocumentException {
/* 474 */     float leftX = Math.min(llx, urx);
/* 475 */     float maxY = Math.max(lly, ury);
/* 476 */     float minY = Math.min(lly, ury);
/* 477 */     float rightX = Math.max(llx, urx);
/* 478 */     this.yLine = maxY;
/* 479 */     boolean contentCutByFixedHeight = false;
/*     */     
/* 481 */     if (this.width != null && this.width.floatValue() > 0.0F) {
/* 482 */       if (this.width.floatValue() < rightX - leftX) {
/* 483 */         rightX = leftX + this.width.floatValue();
/* 484 */       } else if (this.width.floatValue() > rightX - leftX) {
/* 485 */         return 2;
/*     */       } 
/* 487 */     } else if (this.percentageWidth != null) {
/* 488 */       this.contentWidth = (rightX - leftX) * this.percentageWidth.floatValue();
/* 489 */       rightX = leftX + this.contentWidth;
/* 490 */     } else if (this.percentageWidth == null && 
/* 491 */       this.floatType == FloatType.NONE && (this.display == null || this.display == DisplayType.BLOCK || this.display == DisplayType.LIST_ITEM || this.display == DisplayType.RUN_IN)) {
/*     */ 
/*     */       
/* 494 */       this.contentWidth = rightX - leftX;
/*     */     } 
/*     */ 
/*     */     
/* 498 */     if (this.height != null && this.height.floatValue() > 0.0F) {
/* 499 */       if (this.height.floatValue() < maxY - minY) {
/* 500 */         minY = maxY - this.height.floatValue();
/* 501 */         contentCutByFixedHeight = true;
/* 502 */       } else if (this.height.floatValue() > maxY - minY) {
/* 503 */         return 2;
/*     */       } 
/* 505 */     } else if (this.percentageHeight != null) {
/* 506 */       if (this.percentageHeight.floatValue() < 1.0D) {
/* 507 */         contentCutByFixedHeight = true;
/*     */       }
/* 509 */       this.contentHeight = (maxY - minY) * this.percentageHeight.floatValue();
/* 510 */       minY = maxY - this.contentHeight;
/*     */     } 
/*     */     
/* 513 */     if (!simulate && this.position == PositionType.RELATIVE) {
/* 514 */       Float translationX = null;
/* 515 */       if (this.left != null) {
/* 516 */         translationX = this.left;
/* 517 */       } else if (this.right != null) {
/* 518 */         translationX = Float.valueOf(-this.right.floatValue());
/*     */       } else {
/* 520 */         translationX = Float.valueOf(0.0F);
/*     */       } 
/*     */       
/* 523 */       Float translationY = null;
/* 524 */       if (this.top != null) {
/* 525 */         translationY = Float.valueOf(-this.top.floatValue());
/* 526 */       } else if (this.bottom != null) {
/* 527 */         translationY = this.bottom;
/*     */       } else {
/* 529 */         translationY = Float.valueOf(0.0F);
/*     */       } 
/* 531 */       canvas.saveState();
/* 532 */       canvas.transform(new AffineTransform(1.0F, 0.0F, 0.0F, 1.0F, translationX.floatValue(), translationY.floatValue()));
/*     */     } 
/*     */     
/* 535 */     if (!simulate && (
/* 536 */       this.backgroundColor != null || this.backgroundImage != null) && getActualWidth() > 0.0F && getActualHeight() > 0.0F) {
/* 537 */       float backgroundWidth = getActualWidth();
/* 538 */       float backgroundHeight = getActualHeight();
/* 539 */       if (this.width != null) {
/* 540 */         backgroundWidth = (this.width.floatValue() > 0.0F) ? this.width.floatValue() : 0.0F;
/*     */       }
/*     */       
/* 543 */       if (this.height != null) {
/* 544 */         backgroundHeight = (this.height.floatValue() > 0.0F) ? this.height.floatValue() : 0.0F;
/*     */       }
/* 546 */       if (backgroundWidth > 0.0F && backgroundHeight > 0.0F) {
/* 547 */         Rectangle background = new Rectangle(leftX, maxY - backgroundHeight, backgroundWidth + leftX, maxY);
/* 548 */         if (this.backgroundColor != null) {
/* 549 */           background.setBackgroundColor(this.backgroundColor);
/* 550 */           PdfArtifact artifact = new PdfArtifact();
/* 551 */           canvas.openMCBlock(artifact);
/* 552 */           canvas.rectangle(background);
/* 553 */           canvas.closeMCBlock(artifact);
/*     */         } 
/* 555 */         if (this.backgroundImage != null) {
/* 556 */           if (this.backgroundImageWidth == null) {
/* 557 */             this.backgroundImage.scaleToFit(background);
/*     */           } else {
/* 559 */             this.backgroundImage.scaleAbsolute(this.backgroundImageWidth.floatValue(), this.backgroundImageHeight.floatValue());
/*     */           } 
/* 561 */           this.backgroundImage.setAbsolutePosition(background.getLeft(), background.getBottom());
/* 562 */           canvas.openMCBlock((IAccessibleElement)this.backgroundImage);
/* 563 */           canvas.addImage(this.backgroundImage);
/* 564 */           canvas.closeMCBlock((IAccessibleElement)this.backgroundImage);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 570 */     if (this.percentageWidth == null) {
/* 571 */       this.contentWidth = 0.0F;
/*     */     }
/* 573 */     if (this.percentageHeight == null) {
/* 574 */       this.contentHeight = 0.0F;
/*     */     }
/*     */     
/* 577 */     minY += this.paddingBottom;
/* 578 */     leftX += this.paddingLeft;
/* 579 */     rightX -= this.paddingRight;
/*     */     
/* 581 */     this.yLine -= this.paddingTop;
/*     */     
/* 583 */     int status = 1;
/*     */     
/* 585 */     if (!this.content.isEmpty()) {
/* 586 */       if (this.floatLayout == null) {
/* 587 */         ArrayList<Element> floatingElements = new ArrayList<Element>(this.content);
/* 588 */         this.floatLayout = new FloatLayout(floatingElements, useAscender);
/* 589 */         this.floatLayout.setRunDirection(this.runDirection);
/*     */       } 
/*     */       
/* 592 */       this.floatLayout.setSimpleColumn(leftX, minY, rightX, this.yLine);
/* 593 */       if (getBorderTopStyle() != null) {
/* 594 */         this.floatLayout.compositeColumn.setIgnoreSpacingBefore(false);
/*     */       }
/*     */       
/* 597 */       status = this.floatLayout.layout(canvas, simulate);
/* 598 */       this.yLine = this.floatLayout.getYLine();
/* 599 */       if (this.percentageWidth == null && this.contentWidth < this.floatLayout.getFilledWidth()) {
/* 600 */         this.contentWidth = this.floatLayout.getFilledWidth();
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 605 */     if (!simulate && this.position == PositionType.RELATIVE) {
/* 606 */       canvas.restoreState();
/*     */     }
/*     */     
/* 609 */     this.yLine -= this.paddingBottom;
/* 610 */     if (this.percentageHeight == null) {
/* 611 */       this.contentHeight = maxY - this.yLine;
/*     */     }
/*     */     
/* 614 */     if (this.percentageWidth == null) {
/* 615 */       this.contentWidth += this.paddingLeft + this.paddingRight;
/*     */     }
/*     */     
/* 618 */     return contentCutByFixedHeight ? 1 : status;
/*     */   }
/*     */   
/*     */   public PdfObject getAccessibleAttribute(PdfName key) {
/* 622 */     if (this.accessibleAttributes != null) {
/* 623 */       return this.accessibleAttributes.get(key);
/*     */     }
/* 625 */     return null;
/*     */   }
/*     */   
/*     */   public void setAccessibleAttribute(PdfName key, PdfObject value) {
/* 629 */     if (this.accessibleAttributes == null)
/* 630 */       this.accessibleAttributes = new HashMap<PdfName, PdfObject>(); 
/* 631 */     this.accessibleAttributes.put(key, value);
/*     */   }
/*     */   
/*     */   public HashMap<PdfName, PdfObject> getAccessibleAttributes() {
/* 635 */     return this.accessibleAttributes;
/*     */   }
/*     */   
/*     */   public PdfName getRole() {
/* 639 */     return this.role;
/*     */   }
/*     */   
/*     */   public void setRole(PdfName role) {
/* 643 */     this.role = role;
/*     */   }
/*     */   
/*     */   public AccessibleElementId getId() {
/* 647 */     return this.id;
/*     */   }
/*     */   
/*     */   public void setId(AccessibleElementId id) {
/* 651 */     this.id = id;
/*     */   }
/*     */   
/*     */   public boolean isInline() {
/* 655 */     return false;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/PdfDiv.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */