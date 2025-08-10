/*     */ package com.itextpdf.text.pdf;
/*     */ 
/*     */ import com.itextpdf.text.Chunk;
/*     */ import com.itextpdf.text.Image;
/*     */ import com.itextpdf.text.ListItem;
/*     */ import com.itextpdf.text.TabStop;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
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
/*     */ public class PdfLine
/*     */ {
/*     */   protected ArrayList<PdfChunk> line;
/*     */   protected float left;
/*     */   protected float width;
/*     */   protected int alignment;
/*     */   protected float height;
/*     */   protected boolean newlineSplit = false;
/*     */   protected float originalWidth;
/*     */   protected boolean isRTL = false;
/*  89 */   protected ListItem listItem = null;
/*     */   
/*  91 */   protected TabStop tabStop = null;
/*     */   
/*  93 */   protected float tabStopAnchorPosition = Float.NaN;
/*     */   
/*  95 */   protected float tabPosition = Float.NaN;
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
/*     */   PdfLine(float left, float right, int alignment, float height) {
/* 109 */     this.left = left;
/* 110 */     this.width = right - left;
/* 111 */     this.originalWidth = this.width;
/* 112 */     this.alignment = alignment;
/* 113 */     this.height = height;
/* 114 */     this.line = new ArrayList<PdfChunk>();
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
/*     */   PdfLine(float left, float originalWidth, float remainingWidth, int alignment, boolean newlineSplit, ArrayList<PdfChunk> line, boolean isRTL) {
/* 128 */     this.left = left;
/* 129 */     this.originalWidth = originalWidth;
/* 130 */     this.width = remainingWidth;
/* 131 */     this.alignment = alignment;
/* 132 */     this.line = line;
/* 133 */     this.newlineSplit = newlineSplit;
/* 134 */     this.isRTL = isRTL;
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
/*     */   PdfChunk add(PdfChunk chunk, float currentLeading) {
/* 151 */     if (chunk != null && !chunk.toString().equals(""))
/*     */     {
/* 153 */       if (!chunk.toString().equals(" ") && (
/* 154 */         this.height < currentLeading || this.line.isEmpty())) {
/* 155 */         this.height = currentLeading;
/*     */       }
/*     */     }
/* 158 */     return add(chunk);
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
/*     */   PdfChunk add(PdfChunk chunk) {
/* 172 */     if (chunk == null || chunk.toString().equals("")) {
/* 173 */       return null;
/*     */     }
/*     */ 
/*     */     
/* 177 */     PdfChunk overflow = chunk.split(this.width);
/* 178 */     this.newlineSplit = (chunk.isNewlineSplit() || overflow == null);
/* 179 */     if (chunk.isTab()) {
/* 180 */       Object[] tab = (Object[])chunk.getAttribute("TAB");
/* 181 */       if (chunk.isAttribute("TABSETTINGS")) {
/* 182 */         boolean isWhiteSpace = ((Boolean)tab[1]).booleanValue();
/* 183 */         if (!isWhiteSpace || !this.line.isEmpty()) {
/* 184 */           flush();
/* 185 */           this.tabStopAnchorPosition = Float.NaN;
/* 186 */           this.tabStop = PdfChunk.getTabStop(chunk, this.originalWidth - this.width);
/* 187 */           if (this.tabStop.getPosition() > this.originalWidth) {
/* 188 */             if (isWhiteSpace) {
/* 189 */               overflow = null;
/* 190 */             } else if (Math.abs(this.originalWidth - this.width) < 0.001D) {
/* 191 */               addToLine(chunk);
/* 192 */               overflow = null;
/*     */             } else {
/* 194 */               overflow = chunk;
/*     */             } 
/* 196 */             this.width = 0.0F;
/*     */           } else {
/* 198 */             chunk.setTabStop(this.tabStop);
/* 199 */             if (!this.isRTL && this.tabStop.getAlignment() == TabStop.Alignment.LEFT) {
/* 200 */               this.width = this.originalWidth - this.tabStop.getPosition();
/* 201 */               this.tabStop = null;
/* 202 */               this.tabPosition = Float.NaN;
/*     */             } else {
/* 204 */               this.tabPosition = this.originalWidth - this.width;
/* 205 */             }  addToLine(chunk);
/*     */           } 
/*     */         } else {
/* 208 */           return null;
/*     */         } 
/*     */       } else {
/* 211 */         Float tabStopPosition = Float.valueOf(((Float)tab[1]).floatValue());
/* 212 */         boolean newline = ((Boolean)tab[2]).booleanValue();
/* 213 */         if (newline && tabStopPosition.floatValue() < this.originalWidth - this.width) {
/* 214 */           return chunk;
/*     */         }
/* 216 */         chunk.adjustLeft(this.left);
/* 217 */         this.width = this.originalWidth - tabStopPosition.floatValue();
/* 218 */         addToLine(chunk);
/*     */       }
/*     */     
/*     */     }
/* 222 */     else if (chunk.length() > 0 || chunk.isImage()) {
/* 223 */       if (overflow != null)
/* 224 */         chunk.trimLastSpace(); 
/* 225 */       this.width -= chunk.width();
/* 226 */       addToLine(chunk);
/*     */     }
/*     */     else {
/*     */       
/* 230 */       if (this.line.size() < 1) {
/* 231 */         chunk = overflow;
/* 232 */         overflow = chunk.truncate(this.width);
/* 233 */         this.width -= chunk.width();
/* 234 */         if (chunk.length() > 0) {
/* 235 */           addToLine(chunk);
/* 236 */           return overflow;
/*     */         } 
/*     */ 
/*     */         
/* 240 */         if (overflow != null)
/* 241 */           addToLine(overflow); 
/* 242 */         return null;
/*     */       } 
/*     */ 
/*     */       
/* 246 */       this.width += ((PdfChunk)this.line.get(this.line.size() - 1)).trimLastSpace();
/*     */     } 
/* 248 */     return overflow;
/*     */   }
/*     */   
/*     */   private void addToLine(PdfChunk chunk) {
/* 252 */     if (chunk.changeLeading) {
/*     */       float f;
/* 254 */       if (chunk.isImage()) {
/* 255 */         Image img = chunk.getImage();
/*     */         
/* 257 */         f = chunk.getImageHeight() + chunk.getImageOffsetY() + img.getBorderWidthTop() + img.getSpacingBefore();
/*     */       } else {
/*     */         
/* 260 */         f = chunk.getLeading();
/*     */       } 
/* 262 */       if (f > this.height) this.height = f; 
/*     */     } 
/* 264 */     if (this.tabStop != null && this.tabStop.getAlignment() == TabStop.Alignment.ANCHOR && Float.isNaN(this.tabStopAnchorPosition)) {
/* 265 */       String value = chunk.toString();
/* 266 */       int anchorIndex = value.indexOf(this.tabStop.getAnchorChar());
/* 267 */       if (anchorIndex != -1) {
/* 268 */         float subWidth = chunk.width(value.substring(anchorIndex, value.length()));
/* 269 */         this.tabStopAnchorPosition = this.originalWidth - this.width - subWidth;
/*     */       } 
/*     */     } 
/* 272 */     this.line.add(chunk);
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
/*     */   public int size() {
/* 284 */     return this.line.size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Iterator<PdfChunk> iterator() {
/* 294 */     return this.line.iterator();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   float height() {
/* 304 */     return this.height;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   float indentLeft() {
/* 314 */     if (this.isRTL) {
/* 315 */       switch (this.alignment) {
/*     */         case 1:
/* 317 */           return this.left + this.width / 2.0F;
/*     */         case 2:
/* 319 */           return this.left;
/*     */         case 3:
/* 321 */           return this.left + (hasToBeJustified() ? 0.0F : this.width);
/*     */       } 
/*     */       
/* 324 */       return this.left + this.width;
/*     */     } 
/* 326 */     if (getSeparatorCount() <= 0) {
/* 327 */       switch (this.alignment) {
/*     */         case 2:
/* 329 */           return this.left + this.width;
/*     */         case 1:
/* 331 */           return this.left + this.width / 2.0F;
/*     */       } 
/*     */     }
/* 334 */     return this.left;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasToBeJustified() {
/* 344 */     return (((this.alignment == 3 && !this.newlineSplit) || this.alignment == 8) && this.width != 0.0F);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void resetAlignment() {
/* 355 */     if (this.alignment == 3) {
/* 356 */       this.alignment = 0;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   void setExtraIndent(float extra) {
/* 362 */     this.left += extra;
/* 363 */     this.width -= extra;
/* 364 */     this.originalWidth -= extra;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   float widthLeft() {
/* 374 */     return this.width;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int numberOfSpaces() {
/* 384 */     int numberOfSpaces = 0;
/* 385 */     for (PdfChunk pdfChunk : this.line) {
/* 386 */       String tmp = pdfChunk.toString();
/* 387 */       int length = tmp.length();
/* 388 */       for (int i = 0; i < length; i++) {
/* 389 */         if (tmp.charAt(i) == ' ') {
/* 390 */           numberOfSpaces++;
/*     */         }
/*     */       } 
/*     */     } 
/* 394 */     return numberOfSpaces;
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
/*     */   public void setListItem(ListItem listItem) {
/* 406 */     this.listItem = listItem;
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
/*     */   public Chunk listSymbol() {
/* 418 */     return (this.listItem != null) ? this.listItem.getListSymbol() : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float listIndent() {
/* 428 */     return (this.listItem != null) ? this.listItem.getIndentationLeft() : 0.0F;
/*     */   }
/*     */   
/*     */   public ListItem listItem() {
/* 432 */     return this.listItem;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 443 */     StringBuffer tmp = new StringBuffer();
/* 444 */     for (PdfChunk pdfChunk : this.line) {
/* 445 */       tmp.append(pdfChunk.toString());
/*     */     }
/* 447 */     return tmp.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getLineLengthUtf32() {
/* 456 */     int total = 0;
/* 457 */     for (PdfChunk element : this.line) {
/* 458 */       total += ((PdfChunk)element).lengthUtf32();
/*     */     }
/* 460 */     return total;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isNewlineSplit() {
/* 468 */     return (this.newlineSplit && this.alignment != 8);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getLastStrokeChunk() {
/* 476 */     int lastIdx = this.line.size() - 1;
/* 477 */     for (; lastIdx >= 0; lastIdx--) {
/* 478 */       PdfChunk chunk = this.line.get(lastIdx);
/* 479 */       if (chunk.isStroked())
/*     */         break; 
/*     */     } 
/* 482 */     return lastIdx;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfChunk getChunk(int idx) {
/* 491 */     if (idx < 0 || idx >= this.line.size())
/* 492 */       return null; 
/* 493 */     return this.line.get(idx);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getOriginalWidth() {
/* 501 */     return this.originalWidth;
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
/*     */   float[] getMaxSize(float fixedLeading, float multipliedLeading) {
/* 531 */     float normal_leading = 0.0F;
/* 532 */     float image_leading = -10000.0F;
/*     */     
/* 534 */     for (int k = 0; k < this.line.size(); k++) {
/* 535 */       PdfChunk chunk = this.line.get(k);
/* 536 */       if (chunk.isImage()) {
/* 537 */         Image img = chunk.getImage();
/* 538 */         if (chunk.changeLeading()) {
/* 539 */           float height = chunk.getImageHeight() + chunk.getImageOffsetY() + img.getSpacingBefore();
/* 540 */           image_leading = Math.max(height, image_leading);
/*     */         }
/*     */       
/* 543 */       } else if (chunk.changeLeading()) {
/* 544 */         normal_leading = Math.max(chunk.getLeading(), normal_leading);
/*     */       } else {
/* 546 */         normal_leading = Math.max(fixedLeading + multipliedLeading * chunk.font().size(), normal_leading);
/*     */       } 
/*     */     } 
/* 549 */     return new float[] { (normal_leading > 0.0F) ? normal_leading : fixedLeading, image_leading };
/*     */   }
/*     */   
/*     */   boolean isRTL() {
/* 553 */     return this.isRTL;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int getSeparatorCount() {
/* 563 */     int s = 0;
/*     */     
/* 565 */     for (PdfChunk element : this.line) {
/* 566 */       PdfChunk ck = element;
/* 567 */       if (ck.isTab()) {
/* 568 */         if (ck.isAttribute("TABSETTINGS")) {
/*     */           continue;
/*     */         }
/* 571 */         return -1;
/*     */       } 
/* 573 */       if (ck.isHorizontalSeparator()) {
/* 574 */         s++;
/*     */       }
/*     */     } 
/* 577 */     return s;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getWidthCorrected(float charSpacing, float wordSpacing) {
/* 587 */     float total = 0.0F;
/* 588 */     for (int k = 0; k < this.line.size(); k++) {
/* 589 */       PdfChunk ck = this.line.get(k);
/* 590 */       total += ck.getWidthCorrected(charSpacing, wordSpacing);
/*     */     } 
/* 592 */     return total;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getAscender() {
/* 601 */     float ascender = 0.0F;
/* 602 */     for (int k = 0; k < this.line.size(); k++) {
/* 603 */       PdfChunk ck = this.line.get(k);
/* 604 */       if (ck.isImage()) {
/* 605 */         ascender = Math.max(ascender, ck.getImageHeight() + ck.getImageOffsetY());
/*     */       } else {
/* 607 */         PdfFont font = ck.font();
/* 608 */         float textRise = ck.getTextRise();
/* 609 */         ascender = Math.max(ascender, ((textRise > 0.0F) ? textRise : 0.0F) + font.getFont().getFontDescriptor(1, font.size()));
/*     */       } 
/*     */     } 
/* 612 */     return ascender;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getDescender() {
/* 621 */     float descender = 0.0F;
/* 622 */     for (int k = 0; k < this.line.size(); k++) {
/* 623 */       PdfChunk ck = this.line.get(k);
/* 624 */       if (ck.isImage()) {
/* 625 */         descender = Math.min(descender, ck.getImageOffsetY());
/*     */       } else {
/* 627 */         PdfFont font = ck.font();
/* 628 */         float textRise = ck.getTextRise();
/* 629 */         descender = Math.min(descender, ((textRise < 0.0F) ? textRise : 0.0F) + font.getFont().getFontDescriptor(3, font.size()));
/*     */       } 
/*     */     } 
/* 632 */     return descender;
/*     */   }
/*     */   
/*     */   public void flush() {
/* 636 */     if (this.tabStop != null) {
/* 637 */       float textWidth = this.originalWidth - this.width - this.tabPosition;
/* 638 */       float tabStopPosition = this.tabStop.getPosition(this.tabPosition, this.originalWidth - this.width, this.tabStopAnchorPosition);
/* 639 */       this.width = this.originalWidth - tabStopPosition - textWidth;
/* 640 */       if (this.width < 0.0F)
/* 641 */         tabStopPosition += this.width; 
/* 642 */       if (!this.isRTL) {
/* 643 */         this.tabStop.setPosition(tabStopPosition);
/*     */       } else {
/* 645 */         this.tabStop.setPosition(this.originalWidth - this.width - this.tabPosition);
/* 646 */       }  this.tabStop = null;
/* 647 */       this.tabPosition = Float.NaN;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/PdfLine.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */