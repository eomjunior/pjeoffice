/*    */ package com.github.utils4j.gui.imp;
/*    */ 
/*    */ import com.github.utils4j.imp.Strings;
/*    */ import java.net.URL;
/*    */ import javax.swing.BorderFactory;
/*    */ import javax.swing.Icon;
/*    */ import javax.swing.ImageIcon;
/*    */ import javax.swing.JButton;
/*    */ import javax.swing.JPanel;
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
/*    */ 
/*    */ public abstract class AbstractPanel
/*    */   extends JPanel
/*    */ {
/*    */   private final String baseIcons;
/*    */   
/*    */   public AbstractPanel() {
/* 45 */     this("/cp4j/icons/buttons/");
/*    */   }
/*    */   
/*    */   public AbstractPanel(String baseIcons) {
/* 49 */     this.baseIcons = Strings.text(baseIcons, "/");
/*    */   }
/*    */   
/*    */   protected class BigButton extends JButton {
/*    */     public BigButton() {
/* 54 */       setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
/* 55 */       setHideActionText(true);
/*    */     }
/*    */   }
/*    */   
/*    */   protected class StandardButton extends JButton {
/*    */     public StandardButton() {
/* 61 */       setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
/* 62 */       setHideActionText(true);
/*    */     }
/*    */   }
/*    */   
/*    */   protected Icon newIcon(String name) {
/* 67 */     URL location = getClass().getResource(this.baseIcons + name + ".png");
/* 68 */     return (location != null) ? new ImageIcon(location) : null;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/gui/imp/AbstractPanel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */