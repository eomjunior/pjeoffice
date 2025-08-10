/*    */ package com.github.utils4j.gui.imp;
/*    */ 
/*    */ import java.awt.Component;
/*    */ import java.awt.HeadlessException;
/*    */ import javax.swing.JDialog;
/*    */ import javax.swing.JFileChooser;
/*    */ import javax.swing.UIManager;
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
/*    */ public class DefaultFileChooser
/*    */   extends JFileChooser
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   static {
/* 41 */     UIManager.put("FileChooser.lookInLabelMnemonic", Integer.valueOf(69));
/* 42 */     UIManager.put("FileChooser.lookInLabelText", "Pesquisar em");
/* 43 */     UIManager.put("FileChooser.saveInLabelText", "Salvar em");
/* 44 */     UIManager.put("FileChooser.openButtonToolTipText", "Abrir");
/* 45 */     UIManager.put("FileChooser.openButtonAccessibleName", "Abrir");
/* 46 */     UIManager.put("FileChooser.openButtonText", "Abrir");
/* 47 */     UIManager.put("FileChooser.fileNameLabelMnemonic", Integer.valueOf(78));
/* 48 */     UIManager.put("FileChooser.fileNameLabelText", "Nome do arquivo");
/* 49 */     UIManager.put("FileChooser.filesOfTypeLabelMnemonic", Integer.valueOf(84));
/* 50 */     UIManager.put("FileChooser.filesOfTypeLabelText", "Arquivos do Tipo");
/* 51 */     UIManager.put("FileChooser.upFolderToolTipText", "Um nível acima");
/* 52 */     UIManager.put("FileChooser.upFolderAccessibleName", "Um nível acima");
/* 53 */     UIManager.put("FileChooser.homeFolderToolTipText", "Desktop");
/* 54 */     UIManager.put("FileChooser.homeFolderAccessibleName", "Desktop");
/* 55 */     UIManager.put("FileChooser.newFolderToolTipText", "Criar nova pasta");
/* 56 */     UIManager.put("FileChooser.newFolderAccessibleName", "Criar nova pasta");
/* 57 */     UIManager.put("FileChooser.listViewButtonToolTipText", "Lista");
/* 58 */     UIManager.put("FileChooser.listViewButtonAccessibleName", "Lista");
/* 59 */     UIManager.put("FileChooser.detailsViewButtonToolTipText", "Detalhes");
/* 60 */     UIManager.put("FileChooser.detailsViewButtonAccessibleName", "Detalhes");
/* 61 */     UIManager.put("FileChooser.fileNameHeaderText", "Nome");
/* 62 */     UIManager.put("FileChooser.fileSizeHeaderText", "Tamanho");
/* 63 */     UIManager.put("FileChooser.fileTypeHeaderText", "Tipo");
/* 64 */     UIManager.put("FileChooser.fileDateHeaderText", "Data");
/* 65 */     UIManager.put("FileChooser.fileAttrHeaderText", "Atributos");
/*    */   }
/*    */ 
/*    */   
/*    */   protected JDialog createDialog(Component parent) throws HeadlessException {
/* 70 */     JDialog dialog = super.createDialog(parent);
/* 71 */     dialog.setLocation(100, 100);
/* 72 */     dialog.setAlwaysOnTop(true);
/* 73 */     return dialog;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/gui/imp/DefaultFileChooser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */