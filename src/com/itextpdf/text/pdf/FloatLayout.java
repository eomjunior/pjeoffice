/*     */ package com.itextpdf.text.pdf;
/*     */ 
/*     */ import com.itextpdf.text.DocumentException;
/*     */ import com.itextpdf.text.Element;
/*     */ import com.itextpdf.text.Paragraph;
/*     */ import com.itextpdf.text.WritableDirectElement;
/*     */ import com.itextpdf.text.api.Spaceable;
/*     */ import java.util.ArrayList;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FloatLayout
/*     */ {
/*     */   protected float maxY;
/*     */   protected float minY;
/*     */   protected float leftX;
/*     */   protected float rightX;
/*     */   protected float yLine;
/*     */   protected float floatLeftX;
/*     */   protected float floatRightX;
/*     */   protected float filledWidth;
/*     */   protected final ColumnText compositeColumn;
/*     */   protected final List<Element> content;
/*     */   protected final boolean useAscender;
/*     */   
/*     */   public float getYLine() {
/*  69 */     return this.yLine;
/*     */   }
/*     */   
/*     */   public void setYLine(float yLine) {
/*  73 */     this.yLine = yLine;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getFilledWidth() {
/*  83 */     return this.filledWidth;
/*     */   }
/*     */   
/*     */   public void setFilledWidth(float filledWidth) {
/*  87 */     this.filledWidth = filledWidth;
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
/*     */   public int getRunDirection() {
/*  99 */     return this.compositeColumn.getRunDirection();
/*     */   }
/*     */   
/*     */   public void setRunDirection(int runDirection) {
/* 103 */     this.compositeColumn.setRunDirection(runDirection);
/*     */   }
/*     */   
/*     */   public FloatLayout(List<Element> elements, boolean useAscender) {
/* 107 */     this.compositeColumn = new ColumnText(null);
/* 108 */     this.compositeColumn.setUseAscender(useAscender);
/* 109 */     this.useAscender = useAscender;
/* 110 */     this.content = elements;
/*     */   }
/*     */   
/*     */   public void setSimpleColumn(float llx, float lly, float urx, float ury) {
/* 114 */     this.leftX = Math.min(llx, urx);
/* 115 */     this.maxY = Math.max(lly, ury);
/* 116 */     this.minY = Math.min(lly, ury);
/* 117 */     this.rightX = Math.max(llx, urx);
/* 118 */     this.floatLeftX = this.leftX;
/* 119 */     this.floatRightX = this.rightX;
/* 120 */     this.yLine = this.maxY;
/* 121 */     this.filledWidth = 0.0F;
/*     */   }
/*     */   
/*     */   public int layout(PdfContentByte canvas, boolean simulate) throws DocumentException {
/* 125 */     this.compositeColumn.setCanvas(canvas);
/* 126 */     int status = 1;
/*     */     
/* 128 */     ArrayList<Element> floatingElements = new ArrayList<Element>();
/* 129 */     List<Element> content = simulate ? new ArrayList<Element>(this.content) : this.content;
/*     */     
/* 131 */     while (!content.isEmpty()) {
/* 132 */       if (content.get(0) instanceof PdfDiv) {
/* 133 */         PdfDiv floatingElement = (PdfDiv)content.get(0);
/* 134 */         if (floatingElement.getFloatType() == PdfDiv.FloatType.LEFT || floatingElement.getFloatType() == PdfDiv.FloatType.RIGHT) {
/* 135 */           floatingElements.add(floatingElement);
/* 136 */           content.remove(0); continue;
/*     */         } 
/* 138 */         if (!floatingElements.isEmpty()) {
/* 139 */           status = floatingLayout(floatingElements, simulate);
/* 140 */           if ((status & 0x1) == 0) {
/*     */             break;
/*     */           }
/*     */         } 
/*     */         
/* 145 */         content.remove(0);
/*     */         
/* 147 */         status = floatingElement.layout(canvas, this.useAscender, true, this.floatLeftX, this.minY, this.floatRightX, this.yLine);
/*     */         
/* 149 */         if (floatingElement.getKeepTogether() && (status & 0x1) == 0)
/*     */         {
/* 151 */           if ((this.compositeColumn.getCanvas().getPdfDocument()).currentHeight > 0.0F || this.yLine != this.maxY) {
/* 152 */             content.add(0, floatingElement);
/*     */             
/*     */             break;
/*     */           } 
/*     */         }
/* 157 */         if (!simulate) {
/* 158 */           canvas.openMCBlock(floatingElement);
/* 159 */           status = floatingElement.layout(canvas, this.useAscender, simulate, this.floatLeftX, this.minY, this.floatRightX, this.yLine);
/* 160 */           canvas.closeMCBlock(floatingElement);
/*     */         } 
/*     */         
/* 163 */         if (floatingElement.getActualWidth() > this.filledWidth) {
/* 164 */           this.filledWidth = floatingElement.getActualWidth();
/*     */         }
/* 166 */         if ((status & 0x1) == 0) {
/* 167 */           content.add(0, floatingElement);
/* 168 */           this.yLine = floatingElement.getYLine();
/*     */           break;
/*     */         } 
/* 171 */         this.yLine -= floatingElement.getActualHeight();
/*     */         
/*     */         continue;
/*     */       } 
/* 175 */       floatingElements.add(content.get(0));
/* 176 */       content.remove(0);
/*     */     } 
/*     */ 
/*     */     
/* 180 */     if ((status & 0x1) != 0 && !floatingElements.isEmpty()) {
/* 181 */       status = floatingLayout(floatingElements, simulate);
/*     */     }
/*     */     
/* 184 */     content.addAll(0, floatingElements);
/*     */     
/* 186 */     return status;
/*     */   }
/*     */   
/*     */   private int floatingLayout(List<Element> floatingElements, boolean simulate) throws DocumentException {
/* 190 */     int status = 1;
/* 191 */     float minYLine = this.yLine;
/* 192 */     float leftWidth = 0.0F;
/* 193 */     float rightWidth = 0.0F;
/*     */     
/* 195 */     ColumnText currentCompositeColumn = this.compositeColumn;
/* 196 */     if (simulate) {
/* 197 */       currentCompositeColumn = ColumnText.duplicate(this.compositeColumn);
/*     */     }
/*     */     
/* 200 */     boolean ignoreSpacingBefore = (this.maxY == this.yLine);
/*     */     
/* 202 */     while (!floatingElements.isEmpty()) {
/* 203 */       Element nextElement = floatingElements.get(0);
/* 204 */       floatingElements.remove(0);
/* 205 */       if (nextElement instanceof PdfDiv) {
/* 206 */         PdfDiv floatingElement = (PdfDiv)nextElement;
/* 207 */         status = floatingElement.layout(this.compositeColumn.getCanvas(), this.useAscender, true, this.floatLeftX, this.minY, this.floatRightX, this.yLine);
/* 208 */         if ((status & 0x1) == 0) {
/* 209 */           this.yLine = minYLine;
/* 210 */           this.floatLeftX = this.leftX;
/* 211 */           this.floatRightX = this.rightX;
/* 212 */           status = floatingElement.layout(this.compositeColumn.getCanvas(), this.useAscender, true, this.floatLeftX, this.minY, this.floatRightX, this.yLine);
/* 213 */           if ((status & 0x1) == 0) {
/* 214 */             floatingElements.add(0, floatingElement);
/*     */             break;
/*     */           } 
/*     */         } 
/* 218 */         if (floatingElement.getFloatType() == PdfDiv.FloatType.LEFT) {
/* 219 */           status = floatingElement.layout(this.compositeColumn.getCanvas(), this.useAscender, simulate, this.floatLeftX, this.minY, this.floatRightX, this.yLine);
/* 220 */           this.floatLeftX += floatingElement.getActualWidth();
/* 221 */           leftWidth += floatingElement.getActualWidth();
/* 222 */         } else if (floatingElement.getFloatType() == PdfDiv.FloatType.RIGHT) {
/* 223 */           status = floatingElement.layout(this.compositeColumn.getCanvas(), this.useAscender, simulate, this.floatRightX - floatingElement.getActualWidth() - 0.01F, this.minY, this.floatRightX, this.yLine);
/* 224 */           this.floatRightX -= floatingElement.getActualWidth();
/* 225 */           rightWidth += floatingElement.getActualWidth();
/*     */         } 
/* 227 */         minYLine = Math.min(minYLine, this.yLine - floatingElement.getActualHeight());
/*     */       } else {
/* 229 */         if (this.minY > minYLine) {
/* 230 */           status = 2;
/* 231 */           floatingElements.add(0, nextElement);
/* 232 */           if (currentCompositeColumn != null)
/* 233 */             currentCompositeColumn.setText(null); 
/*     */           break;
/*     */         } 
/* 236 */         if (nextElement instanceof Spaceable && (!ignoreSpacingBefore || !currentCompositeColumn.isIgnoreSpacingBefore() || ((Spaceable)nextElement).getPaddingTop() != 0.0F)) {
/* 237 */           this.yLine -= ((Spaceable)nextElement).getSpacingBefore();
/*     */         }
/* 239 */         if (simulate)
/* 240 */         { if (nextElement instanceof PdfPTable) {
/* 241 */             currentCompositeColumn.addElement((Element)new PdfPTable((PdfPTable)nextElement));
/*     */           } else {
/* 243 */             currentCompositeColumn.addElement(nextElement);
/*     */           }  }
/* 245 */         else { currentCompositeColumn.addElement(nextElement); }
/*     */ 
/*     */         
/* 248 */         if (this.yLine > minYLine) {
/* 249 */           currentCompositeColumn.setSimpleColumn(this.floatLeftX, this.yLine, this.floatRightX, minYLine);
/*     */         } else {
/* 251 */           currentCompositeColumn.setSimpleColumn(this.floatLeftX, this.yLine, this.floatRightX, this.minY);
/*     */         } 
/* 253 */         currentCompositeColumn.setFilledWidth(0.0F);
/*     */         
/* 255 */         status = currentCompositeColumn.go(simulate);
/* 256 */         if (this.yLine > minYLine && (this.floatLeftX > this.leftX || this.floatRightX < this.rightX) && (status & 0x1) == 0) {
/* 257 */           this.yLine = minYLine;
/* 258 */           this.floatLeftX = this.leftX;
/* 259 */           this.floatRightX = this.rightX;
/* 260 */           if (leftWidth != 0.0F && rightWidth != 0.0F) {
/* 261 */             this.filledWidth = this.rightX - this.leftX;
/*     */           } else {
/* 263 */             if (leftWidth > this.filledWidth) {
/* 264 */               this.filledWidth = leftWidth;
/*     */             }
/* 266 */             if (rightWidth > this.filledWidth) {
/* 267 */               this.filledWidth = rightWidth;
/*     */             }
/*     */           } 
/*     */           
/* 271 */           leftWidth = 0.0F;
/* 272 */           rightWidth = 0.0F;
/* 273 */           if (simulate && nextElement instanceof PdfPTable) {
/* 274 */             currentCompositeColumn.addElement((Element)new PdfPTable((PdfPTable)nextElement));
/*     */           }
/*     */           
/* 277 */           currentCompositeColumn.setSimpleColumn(this.floatLeftX, this.yLine, this.floatRightX, this.minY);
/* 278 */           status = currentCompositeColumn.go(simulate);
/* 279 */           minYLine = currentCompositeColumn.getYLine() + currentCompositeColumn.getDescender();
/* 280 */           this.yLine = minYLine;
/* 281 */           if (currentCompositeColumn.getFilledWidth() > this.filledWidth) {
/* 282 */             this.filledWidth = currentCompositeColumn.getFilledWidth();
/*     */           }
/*     */         } else {
/* 285 */           if (rightWidth > 0.0F) {
/* 286 */             rightWidth += currentCompositeColumn.getFilledWidth();
/* 287 */           } else if (leftWidth > 0.0F) {
/* 288 */             leftWidth += currentCompositeColumn.getFilledWidth();
/* 289 */           } else if (currentCompositeColumn.getFilledWidth() > this.filledWidth) {
/* 290 */             this.filledWidth = currentCompositeColumn.getFilledWidth();
/*     */           } 
/* 292 */           minYLine = Math.min(currentCompositeColumn.getYLine() + currentCompositeColumn.getDescender(), minYLine);
/* 293 */           this.yLine = currentCompositeColumn.getYLine() + currentCompositeColumn.getDescender();
/*     */         } 
/*     */         
/* 296 */         if ((status & 0x1) == 0) {
/* 297 */           if (!simulate) {
/* 298 */             floatingElements.addAll(0, currentCompositeColumn.getCompositeElements());
/* 299 */             currentCompositeColumn.getCompositeElements().clear(); break;
/*     */           } 
/* 301 */           floatingElements.add(0, nextElement);
/* 302 */           currentCompositeColumn.setText(null);
/*     */           
/*     */           break;
/*     */         } 
/* 306 */         currentCompositeColumn.setText(null);
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 311 */       if (nextElement instanceof Paragraph) {
/* 312 */         Paragraph p = (Paragraph)nextElement;
/* 313 */         for (Element e : p) {
/* 314 */           if (e instanceof WritableDirectElement) {
/* 315 */             WritableDirectElement writableElement = (WritableDirectElement)e;
/* 316 */             if (writableElement.getDirectElementType() == 1 && !simulate) {
/* 317 */               PdfWriter writer = this.compositeColumn.getCanvas().getPdfWriter();
/* 318 */               PdfDocument doc = this.compositeColumn.getCanvas().getPdfDocument();
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 323 */               float savedHeight = doc.currentHeight;
/* 324 */               doc.currentHeight = doc.top() - this.yLine - doc.indentation.indentTop;
/* 325 */               writableElement.write(writer, doc);
/* 326 */               doc.currentHeight = savedHeight;
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */       
/* 332 */       if (ignoreSpacingBefore && nextElement.getChunks().size() == 0) {
/* 333 */         if (nextElement instanceof Paragraph) {
/* 334 */           Paragraph p = (Paragraph)nextElement;
/* 335 */           Element e = (Element)p.get(0);
/* 336 */           if (e instanceof WritableDirectElement) {
/* 337 */             WritableDirectElement writableElement = (WritableDirectElement)e;
/* 338 */             if (writableElement.getDirectElementType() != 1)
/* 339 */               ignoreSpacingBefore = false; 
/*     */           }  continue;
/*     */         } 
/* 342 */         if (nextElement instanceof Spaceable) {
/* 343 */           ignoreSpacingBefore = false;
/*     */         }
/*     */         continue;
/*     */       } 
/* 347 */       ignoreSpacingBefore = false;
/*     */     } 
/*     */ 
/*     */     
/* 351 */     if (leftWidth != 0.0F && rightWidth != 0.0F) {
/* 352 */       this.filledWidth = this.rightX - this.leftX;
/*     */     } else {
/* 354 */       if (leftWidth > this.filledWidth) {
/* 355 */         this.filledWidth = leftWidth;
/*     */       }
/* 357 */       if (rightWidth > this.filledWidth) {
/* 358 */         this.filledWidth = rightWidth;
/*     */       }
/*     */     } 
/*     */     
/* 362 */     this.yLine = minYLine;
/* 363 */     this.floatLeftX = this.leftX;
/* 364 */     this.floatRightX = this.rightX;
/*     */     
/* 366 */     return status;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/FloatLayout.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */