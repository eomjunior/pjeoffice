/*    */ package com.github.utils4j.gui.imp;
/*    */ 
/*    */ import java.awt.Component;
/*    */ import java.awt.event.ActionEvent;
/*    */ import java.awt.event.ActionListener;
/*    */ import javax.swing.AbstractCellEditor;
/*    */ import javax.swing.JButton;
/*    */ import javax.swing.JTable;
/*    */ import javax.swing.UIManager;
/*    */ import javax.swing.table.TableCellEditor;
/*    */ import javax.swing.table.TableCellRenderer;
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
/*    */ public class ButtonRenderer
/*    */   extends AbstractCellEditor
/*    */   implements TableCellRenderer, TableCellEditor, ActionListener
/*    */ {
/*    */   private static final long serialVersionUID = -1L;
/*    */   private JButton renderButton;
/*    */   private JButton editButton;
/*    */   private String text;
/*    */   private ActionListener listener;
/*    */   
/*    */   public ButtonRenderer(ActionListener listener) {
/* 52 */     this.listener = listener;
/* 53 */     this.renderButton = new JButton();
/* 54 */     this.editButton = new JButton();
/* 55 */     this.editButton.setFocusPainted(false);
/* 56 */     this.editButton.addActionListener(this);
/*    */   }
/*    */   
/*    */   public void setViewEnabled(boolean enabled) {
/* 60 */     this.renderButton.setEnabled(enabled);
/* 61 */     this.editButton.setEnabled(enabled);
/*    */   }
/*    */   
/*    */   public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
/* 65 */     if (hasFocus) {
/* 66 */       this.renderButton.setForeground(table.getForeground());
/* 67 */       this.renderButton.setBackground(UIManager.getColor("Button.background"));
/*    */     }
/* 69 */     else if (isSelected) {
/* 70 */       this.renderButton.setForeground(table.getSelectionForeground());
/* 71 */       this.renderButton.setBackground(table.getSelectionBackground());
/*    */     } else {
/*    */       
/* 74 */       this.renderButton.setForeground(table.getForeground());
/* 75 */       this.renderButton.setBackground(UIManager.getColor("Button.background"));
/*    */     } 
/* 77 */     this.renderButton.setText((value == null) ? "" : value.toString());
/* 78 */     return this.renderButton;
/*    */   }
/*    */   
/*    */   public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
/* 82 */     this.text = (value == null) ? "" : value.toString();
/* 83 */     this.editButton.setText(this.text);
/* 84 */     return this.editButton;
/*    */   }
/*    */   
/*    */   public Object getCellEditorValue() {
/* 88 */     return this.text;
/*    */   }
/*    */   
/*    */   public void actionPerformed(ActionEvent e) {
/* 92 */     fireEditingStopped();
/* 93 */     this.listener.actionPerformed(e);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/gui/imp/ButtonRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */