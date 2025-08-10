/*    */ package com.github.utils4j.gui.imp;
/*    */ 
/*    */ import com.github.utils4j.gui.IResourceAction;
/*    */ import javax.swing.AbstractAction;
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
/*    */ public abstract class StandardAction
/*    */   extends AbstractAction
/*    */ {
/*    */   public StandardAction(IResourceAction resource) {
/* 38 */     putValue("Name", resource.name());
/* 39 */     putValue("MnemonicKey", resource.mnemonic());
/* 40 */     putValue("AcceleratorKey", resource.shortcut());
/* 41 */     putValue("ShortDescription", resource.tooltip());
/* 42 */     putValue("SmallIcon", resource.menuIcon());
/* 43 */     putValue("SwingLargeIconKey", resource.buttonIcon());
/*    */   }
/*    */   
/*    */   public StandardAction(String name) {
/* 47 */     putValue("Name", name);
/*    */   }
/*    */   
/*    */   public final void select(boolean select) {
/* 51 */     putValue("SwingSelectedKey", Boolean.valueOf(select));
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/gui/imp/StandardAction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */