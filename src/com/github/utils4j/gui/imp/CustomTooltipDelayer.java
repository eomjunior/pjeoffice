/*    */ package com.github.utils4j.gui.imp;
/*    */ 
/*    */ import java.awt.event.MouseAdapter;
/*    */ import java.awt.event.MouseEvent;
/*    */ import javax.swing.JComponent;
/*    */ import javax.swing.ToolTipManager;
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
/*    */ 
/*    */ 
/*    */ public final class CustomTooltipDelayer
/*    */   extends MouseAdapter
/*    */ {
/* 38 */   private static final int DEFAULT_DISMISS_TIMEOUT = ToolTipManager.sharedInstance().getDismissDelay();
/*    */   
/*    */   private final int delay;
/*    */   
/*    */   public static CustomTooltipDelayer attach(JComponent component, int delay) {
/* 43 */     CustomTooltipDelayer delayer = new CustomTooltipDelayer(delay);
/* 44 */     component.addMouseListener(delayer);
/* 45 */     return delayer;
/*    */   }
/*    */   
/*    */   public static CustomTooltipDelayer attach(JComponent component, float ratio) {
/* 49 */     return attach(component, (int)(DEFAULT_DISMISS_TIMEOUT * ratio));
/*    */   }
/*    */   
/*    */   private CustomTooltipDelayer(int delay) {
/* 53 */     this.delay = delay;
/*    */   }
/*    */ 
/*    */   
/*    */   public void mouseEntered(MouseEvent e) {
/* 58 */     ToolTipManager.sharedInstance().setDismissDelay(this.delay);
/*    */   }
/*    */ 
/*    */   
/*    */   public void mouseExited(MouseEvent e) {
/* 63 */     ToolTipManager.sharedInstance().setDismissDelay(DEFAULT_DISMISS_TIMEOUT);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/gui/imp/CustomTooltipDelayer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */