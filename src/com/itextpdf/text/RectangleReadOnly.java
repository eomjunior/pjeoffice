/*     */ package com.itextpdf.text;
/*     */ 
/*     */ import com.itextpdf.text.error_messages.MessageLocalization;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RectangleReadOnly
/*     */   extends Rectangle
/*     */ {
/*     */   public RectangleReadOnly(float llx, float lly, float urx, float ury) {
/*  75 */     super(llx, lly, urx, ury);
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
/*     */   public RectangleReadOnly(float llx, float lly, float urx, float ury, int rotation) {
/*  89 */     super(llx, lly, urx, ury);
/*  90 */     super.setRotation(rotation);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RectangleReadOnly(float urx, float ury) {
/* 101 */     super(0.0F, 0.0F, urx, ury);
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
/*     */   public RectangleReadOnly(float urx, float ury, int rotation) {
/* 114 */     super(0.0F, 0.0F, urx, ury);
/* 115 */     super.setRotation(rotation);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RectangleReadOnly(Rectangle rect) {
/* 124 */     super(rect.llx, rect.lly, rect.urx, rect.ury);
/* 125 */     super.cloneNonPositionParameters(rect);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void throwReadOnlyError() {
/* 132 */     throw new UnsupportedOperationException(MessageLocalization.getComposedMessage("rectanglereadonly.this.rectangle.is.read.only", new Object[0]));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRotation(int rotation) {
/* 142 */     throwReadOnlyError();
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
/*     */   public void setLeft(float llx) {
/* 154 */     throwReadOnlyError();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRight(float urx) {
/* 165 */     throwReadOnlyError();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTop(float ury) {
/* 175 */     throwReadOnlyError();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBottom(float lly) {
/* 185 */     throwReadOnlyError();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void normalize() {
/* 194 */     throwReadOnlyError();
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
/*     */   public void setBackgroundColor(BaseColor value) {
/* 206 */     throwReadOnlyError();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setGrayFill(float value) {
/* 216 */     throwReadOnlyError();
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
/*     */   public void setBorder(int border) {
/* 232 */     throwReadOnlyError();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUseVariableBorders(boolean useVariableBorders) {
/* 242 */     throwReadOnlyError();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void enableBorderSide(int side) {
/* 253 */     throwReadOnlyError();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void disableBorderSide(int side) {
/* 264 */     throwReadOnlyError();
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
/*     */   public void setBorderWidth(float borderWidth) {
/* 277 */     throwReadOnlyError();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBorderWidthLeft(float borderWidthLeft) {
/* 287 */     throwReadOnlyError();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBorderWidthRight(float borderWidthRight) {
/* 297 */     throwReadOnlyError();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBorderWidthTop(float borderWidthTop) {
/* 307 */     throwReadOnlyError();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBorderWidthBottom(float borderWidthBottom) {
/* 317 */     throwReadOnlyError();
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
/*     */   public void setBorderColor(BaseColor borderColor) {
/* 330 */     throwReadOnlyError();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBorderColorLeft(BaseColor borderColorLeft) {
/* 340 */     throwReadOnlyError();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBorderColorRight(BaseColor borderColorRight) {
/* 350 */     throwReadOnlyError();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBorderColorTop(BaseColor borderColorTop) {
/* 360 */     throwReadOnlyError();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBorderColorBottom(BaseColor borderColorBottom) {
/* 370 */     throwReadOnlyError();
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
/*     */   public void cloneNonPositionParameters(Rectangle rect) {
/* 383 */     throwReadOnlyError();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void softCloneNonPositionParameters(Rectangle rect) {
/* 394 */     throwReadOnlyError();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 403 */     StringBuffer buf = new StringBuffer("RectangleReadOnly: ");
/* 404 */     buf.append(getWidth());
/* 405 */     buf.append('x');
/* 406 */     buf.append(getHeight());
/* 407 */     buf.append(" (rot: ");
/* 408 */     buf.append(this.rotation);
/* 409 */     buf.append(" degrees)");
/* 410 */     return buf.toString();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/RectangleReadOnly.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */