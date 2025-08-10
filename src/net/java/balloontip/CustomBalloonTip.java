/*    */ package net.java.balloontip;
/*    */ 
/*    */ import java.awt.Point;
/*    */ import java.awt.Rectangle;
/*    */ import javax.swing.JButton;
/*    */ import javax.swing.JComponent;
/*    */ import javax.swing.SwingUtilities;
/*    */ import net.java.balloontip.positioners.BalloonTipPositioner;
/*    */ import net.java.balloontip.styles.BalloonTipStyle;
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
/*    */ public class CustomBalloonTip
/*    */   extends BalloonTip
/*    */ {
/* 28 */   private Rectangle offset = null;
/*    */ 
/*    */ 
/*    */   
/*    */   private static final long serialVersionUID = 2956673369456562797L;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public CustomBalloonTip(JComponent attachedComponent, JComponent component, Rectangle offset, BalloonTipStyle style, BalloonTip.Orientation orientation, BalloonTip.AttachLocation attachLocation, int horizontalOffset, int verticalOffset, boolean useCloseButton) {
/* 38 */     this.offset = offset;
/* 39 */     setup(attachedComponent, component, style, setupPositioner(orientation, attachLocation, horizontalOffset, verticalOffset), useCloseButton ? getDefaultCloseButton() : null);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public CustomBalloonTip(JComponent attachedComponent, JComponent component, Rectangle offset, BalloonTipStyle style, BalloonTipPositioner positioner, JButton closeButton) {
/* 51 */     this.offset = offset;
/* 52 */     setup(attachedComponent, component, style, positioner, closeButton);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setOffset(Rectangle offset) {
/* 61 */     this.offset = offset;
/* 62 */     notifyViewportListener();
/* 63 */     refreshLocation();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Rectangle getOffset() {
/* 71 */     return this.offset;
/*    */   }
/*    */   
/*    */   public Rectangle getAttachedRectangle() {
/* 75 */     Point location = SwingUtilities.convertPoint(this.attachedComponent, getLocation(), this);
/* 76 */     return new Rectangle(location.x + this.offset.x, location.y + this.offset.y, this.offset.width, this.offset.height);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/net/java/balloontip/CustomBalloonTip.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */