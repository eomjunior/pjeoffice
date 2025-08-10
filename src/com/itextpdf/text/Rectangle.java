/*     */ package com.itextpdf.text;
/*     */ 
/*     */ import com.itextpdf.text.pdf.GrayColor;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Rectangle
/*     */   implements Element
/*     */ {
/*     */   public static final int UNDEFINED = -1;
/*     */   public static final int TOP = 1;
/*     */   public static final int BOTTOM = 2;
/*     */   public static final int LEFT = 4;
/*     */   public static final int RIGHT = 8;
/*     */   public static final int NO_BORDER = 0;
/*     */   public static final int BOX = 15;
/*     */   protected float llx;
/*     */   protected float lly;
/*     */   protected float urx;
/*     */   protected float ury;
/* 102 */   protected int rotation = 0;
/*     */ 
/*     */   
/* 105 */   protected BaseColor backgroundColor = null;
/*     */ 
/*     */   
/* 108 */   protected int border = -1;
/*     */ 
/*     */   
/*     */   protected boolean useVariableBorders = false;
/*     */ 
/*     */   
/* 114 */   protected float borderWidth = -1.0F;
/*     */ 
/*     */   
/* 117 */   protected float borderWidthLeft = -1.0F;
/*     */ 
/*     */   
/* 120 */   protected float borderWidthRight = -1.0F;
/*     */ 
/*     */   
/* 123 */   protected float borderWidthTop = -1.0F;
/*     */ 
/*     */   
/* 126 */   protected float borderWidthBottom = -1.0F;
/*     */ 
/*     */   
/* 129 */   protected BaseColor borderColor = null;
/*     */ 
/*     */   
/* 132 */   protected BaseColor borderColorLeft = null;
/*     */ 
/*     */   
/* 135 */   protected BaseColor borderColorRight = null;
/*     */ 
/*     */   
/* 138 */   protected BaseColor borderColorTop = null;
/*     */ 
/*     */   
/* 141 */   protected BaseColor borderColorBottom = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Rectangle(float llx, float lly, float urx, float ury) {
/* 154 */     this.llx = llx;
/* 155 */     this.lly = lly;
/* 156 */     this.urx = urx;
/* 157 */     this.ury = ury;
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
/*     */   public Rectangle(float llx, float lly, float urx, float ury, int rotation) {
/* 171 */     this(llx, lly, urx, ury);
/* 172 */     setRotation(rotation);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Rectangle(float urx, float ury) {
/* 183 */     this(0.0F, 0.0F, urx, ury);
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
/*     */   public Rectangle(float urx, float ury, int rotation) {
/* 196 */     this(0.0F, 0.0F, urx, ury, rotation);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Rectangle(Rectangle rect) {
/* 205 */     this(rect.llx, rect.lly, rect.urx, rect.ury);
/* 206 */     cloneNonPositionParameters(rect);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Rectangle(com.itextpdf.awt.geom.Rectangle rect) {
/* 214 */     this((float)rect.getX(), (float)rect.getY(), (float)(rect.getX() + rect.getWidth()), (float)(rect.getY() + rect.getHeight()));
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
/*     */   public boolean process(ElementListener listener) {
/*     */     try {
/* 228 */       return listener.add(this);
/*     */     }
/* 230 */     catch (DocumentException de) {
/* 231 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int type() {
/* 241 */     return 30;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Chunk> getChunks() {
/* 250 */     return new ArrayList<Chunk>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isContent() {
/* 258 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isNestable() {
/* 266 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLeft(float llx) {
/* 277 */     this.llx = llx;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getLeft() {
/* 286 */     return this.llx;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getLeft(float margin) {
/* 296 */     return this.llx + margin;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRight(float urx) {
/* 305 */     this.urx = urx;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getRight() {
/* 314 */     return this.urx;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getRight(float margin) {
/* 324 */     return this.urx - margin;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getWidth() {
/* 333 */     return this.urx - this.llx;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTop(float ury) {
/* 342 */     this.ury = ury;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getTop() {
/* 351 */     return this.ury;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getTop(float margin) {
/* 361 */     return this.ury - margin;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBottom(float lly) {
/* 370 */     this.lly = lly;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getBottom() {
/* 379 */     return this.lly;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getBottom(float margin) {
/* 389 */     return this.lly + margin;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getHeight() {
/* 398 */     return this.ury - this.lly;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void normalize() {
/* 406 */     if (this.llx > this.urx) {
/* 407 */       float a = this.llx;
/* 408 */       this.llx = this.urx;
/* 409 */       this.urx = a;
/*     */     } 
/* 411 */     if (this.lly > this.ury) {
/* 412 */       float a = this.lly;
/* 413 */       this.lly = this.ury;
/* 414 */       this.ury = a;
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
/*     */   public int getRotation() {
/* 426 */     return this.rotation;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRotation(int rotation) {
/* 435 */     this.rotation = rotation % 360;
/* 436 */     switch (this.rotation) {
/*     */       case 90:
/*     */       case 180:
/*     */       case 270:
/*     */         return;
/*     */     } 
/* 442 */     this.rotation = 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Rectangle rotate() {
/* 453 */     Rectangle rect = new Rectangle(this.lly, this.llx, this.ury, this.urx);
/* 454 */     rect.setRotation(this.rotation + 90);
/* 455 */     return rect;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BaseColor getBackgroundColor() {
/* 466 */     return this.backgroundColor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBackgroundColor(BaseColor backgroundColor) {
/* 476 */     this.backgroundColor = backgroundColor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getGrayFill() {
/* 486 */     if (this.backgroundColor instanceof GrayColor)
/* 487 */       return ((GrayColor)this.backgroundColor).getGray(); 
/* 488 */     return 0.0F;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setGrayFill(float value) {
/* 497 */     this.backgroundColor = (BaseColor)new GrayColor(value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getBorder() {
/* 508 */     return this.border;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasBorders() {
/* 517 */     switch (this.border) {
/*     */       case -1:
/*     */       case 0:
/* 520 */         return false;
/*     */     } 
/* 522 */     return (this.borderWidth > 0.0F || this.borderWidthLeft > 0.0F || this.borderWidthRight > 0.0F || this.borderWidthTop > 0.0F || this.borderWidthBottom > 0.0F);
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
/*     */   public boolean hasBorder(int type) {
/* 534 */     if (this.border == -1)
/* 535 */       return false; 
/* 536 */     return ((this.border & type) == type);
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
/*     */   public void setBorder(int border) {
/* 549 */     this.border = border;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isUseVariableBorders() {
/* 560 */     return this.useVariableBorders;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUseVariableBorders(boolean useVariableBorders) {
/* 569 */     this.useVariableBorders = useVariableBorders;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void enableBorderSide(int side) {
/* 579 */     if (this.border == -1)
/* 580 */       this.border = 0; 
/* 581 */     this.border |= side;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void disableBorderSide(int side) {
/* 591 */     if (this.border == -1)
/* 592 */       this.border = 0; 
/* 593 */     this.border &= side ^ 0xFFFFFFFF;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getBorderWidth() {
/* 604 */     return this.borderWidth;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBorderWidth(float borderWidth) {
/* 613 */     this.borderWidth = borderWidth;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private float getVariableBorderWidth(float variableWidthValue, int side) {
/* 624 */     if ((this.border & side) != 0)
/* 625 */       return (variableWidthValue != -1.0F) ? variableWidthValue : this.borderWidth; 
/* 626 */     return 0.0F;
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
/*     */   private void updateBorderBasedOnWidth(float width, int side) {
/* 639 */     this.useVariableBorders = true;
/* 640 */     if (width > 0.0F) {
/* 641 */       enableBorderSide(side);
/*     */     } else {
/* 643 */       disableBorderSide(side);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getBorderWidthLeft() {
/* 652 */     return getVariableBorderWidth(this.borderWidthLeft, 4);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBorderWidthLeft(float borderWidthLeft) {
/* 661 */     this.borderWidthLeft = borderWidthLeft;
/* 662 */     updateBorderBasedOnWidth(borderWidthLeft, 4);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getBorderWidthRight() {
/* 671 */     return getVariableBorderWidth(this.borderWidthRight, 8);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBorderWidthRight(float borderWidthRight) {
/* 680 */     this.borderWidthRight = borderWidthRight;
/* 681 */     updateBorderBasedOnWidth(borderWidthRight, 8);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getBorderWidthTop() {
/* 690 */     return getVariableBorderWidth(this.borderWidthTop, 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBorderWidthTop(float borderWidthTop) {
/* 699 */     this.borderWidthTop = borderWidthTop;
/* 700 */     updateBorderBasedOnWidth(borderWidthTop, 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getBorderWidthBottom() {
/* 709 */     return getVariableBorderWidth(this.borderWidthBottom, 2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBorderWidthBottom(float borderWidthBottom) {
/* 718 */     this.borderWidthBottom = borderWidthBottom;
/* 719 */     updateBorderBasedOnWidth(borderWidthBottom, 2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BaseColor getBorderColor() {
/* 730 */     return this.borderColor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBorderColor(BaseColor borderColor) {
/* 739 */     this.borderColor = borderColor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BaseColor getBorderColorLeft() {
/* 748 */     if (this.borderColorLeft == null)
/* 749 */       return this.borderColor; 
/* 750 */     return this.borderColorLeft;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBorderColorLeft(BaseColor borderColorLeft) {
/* 759 */     this.borderColorLeft = borderColorLeft;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BaseColor getBorderColorRight() {
/* 768 */     if (this.borderColorRight == null)
/* 769 */       return this.borderColor; 
/* 770 */     return this.borderColorRight;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBorderColorRight(BaseColor borderColorRight) {
/* 779 */     this.borderColorRight = borderColorRight;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BaseColor getBorderColorTop() {
/* 788 */     if (this.borderColorTop == null)
/* 789 */       return this.borderColor; 
/* 790 */     return this.borderColorTop;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBorderColorTop(BaseColor borderColorTop) {
/* 799 */     this.borderColorTop = borderColorTop;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BaseColor getBorderColorBottom() {
/* 808 */     if (this.borderColorBottom == null)
/* 809 */       return this.borderColor; 
/* 810 */     return this.borderColorBottom;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBorderColorBottom(BaseColor borderColorBottom) {
/* 819 */     this.borderColorBottom = borderColorBottom;
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
/*     */   public Rectangle rectangle(float top, float bottom) {
/* 832 */     Rectangle tmp = new Rectangle(this);
/* 833 */     if (getTop() > top) {
/* 834 */       tmp.setTop(top);
/* 835 */       tmp.disableBorderSide(1);
/*     */     } 
/* 837 */     if (getBottom() < bottom) {
/* 838 */       tmp.setBottom(bottom);
/* 839 */       tmp.disableBorderSide(2);
/*     */     } 
/* 841 */     return tmp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void cloneNonPositionParameters(Rectangle rect) {
/* 851 */     this.rotation = rect.rotation;
/* 852 */     this.backgroundColor = rect.backgroundColor;
/* 853 */     this.border = rect.border;
/* 854 */     this.useVariableBorders = rect.useVariableBorders;
/* 855 */     this.borderWidth = rect.borderWidth;
/* 856 */     this.borderWidthLeft = rect.borderWidthLeft;
/* 857 */     this.borderWidthRight = rect.borderWidthRight;
/* 858 */     this.borderWidthTop = rect.borderWidthTop;
/* 859 */     this.borderWidthBottom = rect.borderWidthBottom;
/* 860 */     this.borderColor = rect.borderColor;
/* 861 */     this.borderColorLeft = rect.borderColorLeft;
/* 862 */     this.borderColorRight = rect.borderColorRight;
/* 863 */     this.borderColorTop = rect.borderColorTop;
/* 864 */     this.borderColorBottom = rect.borderColorBottom;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void softCloneNonPositionParameters(Rectangle rect) {
/* 874 */     if (rect.rotation != 0)
/* 875 */       this.rotation = rect.rotation; 
/* 876 */     if (rect.backgroundColor != null)
/* 877 */       this.backgroundColor = rect.backgroundColor; 
/* 878 */     if (rect.border != -1)
/* 879 */       this.border = rect.border; 
/* 880 */     if (this.useVariableBorders)
/* 881 */       this.useVariableBorders = rect.useVariableBorders; 
/* 882 */     if (rect.borderWidth != -1.0F)
/* 883 */       this.borderWidth = rect.borderWidth; 
/* 884 */     if (rect.borderWidthLeft != -1.0F)
/* 885 */       this.borderWidthLeft = rect.borderWidthLeft; 
/* 886 */     if (rect.borderWidthRight != -1.0F)
/* 887 */       this.borderWidthRight = rect.borderWidthRight; 
/* 888 */     if (rect.borderWidthTop != -1.0F)
/* 889 */       this.borderWidthTop = rect.borderWidthTop; 
/* 890 */     if (rect.borderWidthBottom != -1.0F)
/* 891 */       this.borderWidthBottom = rect.borderWidthBottom; 
/* 892 */     if (rect.borderColor != null)
/* 893 */       this.borderColor = rect.borderColor; 
/* 894 */     if (rect.borderColorLeft != null)
/* 895 */       this.borderColorLeft = rect.borderColorLeft; 
/* 896 */     if (rect.borderColorRight != null)
/* 897 */       this.borderColorRight = rect.borderColorRight; 
/* 898 */     if (rect.borderColorTop != null)
/* 899 */       this.borderColorTop = rect.borderColorTop; 
/* 900 */     if (rect.borderColorBottom != null) {
/* 901 */       this.borderColorBottom = rect.borderColorBottom;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 910 */     StringBuffer buf = new StringBuffer("Rectangle: ");
/* 911 */     buf.append(getWidth());
/* 912 */     buf.append('x');
/* 913 */     buf.append(getHeight());
/* 914 */     buf.append(" (rot: ");
/* 915 */     buf.append(this.rotation);
/* 916 */     buf.append(" degrees)");
/* 917 */     return buf.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 922 */     if (obj instanceof Rectangle) {
/* 923 */       Rectangle other = (Rectangle)obj;
/*     */ 
/*     */ 
/*     */       
/* 927 */       return (other.llx == this.llx && other.lly == this.lly && other.urx == this.urx && other.ury == this.ury && other.rotation == this.rotation);
/*     */     } 
/* 929 */     return false;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/Rectangle.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */