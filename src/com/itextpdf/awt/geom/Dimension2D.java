/*    */ package com.itextpdf.awt.geom;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class Dimension2D
/*    */   implements Cloneable
/*    */ {
/*    */   public abstract double getWidth();
/*    */   
/*    */   public abstract double getHeight();
/*    */   
/*    */   public abstract void setSize(double paramDouble1, double paramDouble2);
/*    */   
/*    */   public void setSize(Dimension2D d) {
/* 38 */     setSize(d.getWidth(), d.getHeight());
/*    */   }
/*    */ 
/*    */   
/*    */   public Object clone() {
/*    */     try {
/* 44 */       return super.clone();
/* 45 */     } catch (CloneNotSupportedException e) {
/* 46 */       throw new InternalError();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/awt/geom/Dimension2D.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */