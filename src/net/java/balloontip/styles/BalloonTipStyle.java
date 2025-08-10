/*    */ package net.java.balloontip.styles;
/*    */ 
/*    */ import java.awt.Component;
/*    */ import java.awt.Insets;
/*    */ import javax.swing.border.Border;
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
/*    */ public abstract class BalloonTipStyle
/*    */   implements Border
/*    */ {
/* 22 */   protected int horizontalOffset = 0;
/* 23 */   protected int verticalOffset = 0;
/*    */ 
/*    */   
/*    */   protected boolean flipX = false;
/*    */   
/*    */   protected boolean flipY = false;
/*    */ 
/*    */   
/*    */   public void setHorizontalOffset(int px) {
/* 32 */     this.horizontalOffset = px;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setVerticalOffset(int px) {
/* 40 */     this.verticalOffset = px;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getMinimalHorizontalOffset() {
/* 49 */     return this.verticalOffset;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void flipX(boolean flipX) {
/* 57 */     this.flipX = flipX;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void flipY(boolean flipY) {
/* 65 */     this.flipY = flipY;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void flip(boolean flipX, boolean flipY) {
/* 74 */     this.flipX = flipX;
/* 75 */     this.flipY = flipY;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isBorderOpaque() {
/* 83 */     return true;
/*    */   }
/*    */   
/*    */   public abstract Insets getBorderInsets(Component paramComponent);
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/net/java/balloontip/styles/BalloonTipStyle.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */