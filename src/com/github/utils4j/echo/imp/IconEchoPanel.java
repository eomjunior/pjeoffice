/*    */ package com.github.utils4j.echo.imp;
/*    */ 
/*    */ import java.awt.BorderLayout;
/*    */ import java.awt.Font;
/*    */ import javax.swing.ImageIcon;
/*    */ import javax.swing.JLabel;
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
/*    */ public abstract class IconEchoPanel
/*    */   extends EchoPanel
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   private JLabel header;
/*    */   
/*    */   public IconEchoPanel() {
/* 44 */     super("%s\n");
/*    */   }
/*    */   
/*    */   public IconEchoPanel(String headerItemFormat) {
/* 48 */     super(headerItemFormat);
/*    */   }
/*    */   
/*    */   private JPanel westIcon() {
/* 52 */     JPanel middle = new JPanel();
/* 53 */     middle.add("center", new JLabel(getIcon()));
/* 54 */     return middle;
/*    */   }
/*    */   
/*    */   private JPanel eastText() {
/* 58 */     this.header = new JLabel();
/* 59 */     this.header.setFont(new Font("Tahoma", 0, 22));
/* 60 */     JPanel east = new JPanel();
/* 61 */     east.add("center", this.header);
/* 62 */     return east;
/*    */   }
/*    */   
/*    */   protected void onNewItem(String item, int count) {
/* 66 */     this.header.setText("Requisição: " + count);
/*    */   }
/*    */ 
/*    */   
/*    */   protected JPanel north() {
/* 71 */     JPanel north = super.north();
/* 72 */     north.setLayout(new BorderLayout(0, 0));
/* 73 */     north.add(westIcon(), "West");
/* 74 */     north.add(eastText(), "Center");
/* 75 */     return north;
/*    */   }
/*    */   
/*    */   protected abstract ImageIcon getIcon();
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/echo/imp/IconEchoPanel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */