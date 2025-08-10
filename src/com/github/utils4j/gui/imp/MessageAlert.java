/*     */ package com.github.utils4j.gui.imp;
/*     */ 
/*     */ import com.github.utils4j.imp.Strings;
/*     */ import java.awt.Image;
/*     */ import javax.swing.JDialog;
/*     */ import javax.swing.JOptionPane;
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
/*     */ public final class MessageAlert
/*     */ {
/*  41 */   private static final String[] OPTIONS = new String[] { "ENTENDI" }; private final JOptionPane jop;
/*     */   
/*     */   public static void showInfo(String message) {
/*  44 */     showInfo(message, null);
/*     */   }
/*     */   private final String[] options;
/*     */   public static void showInfo(String message, Image icon) {
/*  48 */     SwingTools.invokeLater(() -> display(message, icon));
/*     */   }
/*     */   
/*     */   public static void showFail(String message) {
/*  52 */     showFail(message, null);
/*     */   }
/*     */   
/*     */   public static void showFail(String message, Image icon) {
/*  56 */     SwingTools.invokeLater(() -> displayFail(message, icon));
/*     */   }
/*     */   
/*     */   public static void showInfo(String message, String textButton, Image icon) {
/*  60 */     SwingTools.invokeLater(() -> display(message, textButton, icon));
/*     */   }
/*     */   
/*     */   public static boolean display(String message, Image icon) {
/*  64 */     return (new MessageAlert(message, OPTIONS, 1)).show(icon);
/*     */   }
/*     */   
/*     */   public static boolean display(String message, String textButton, Image icon) {
/*  68 */     return (new MessageAlert(message, new String[] { textButton }, 1)).show(icon);
/*     */   }
/*     */   
/*     */   public static boolean displayFail(String message, Image icon) {
/*  72 */     return (new MessageAlert(message, OPTIONS, 0)).show(icon);
/*     */   }
/*     */   
/*     */   public static boolean displayFail(String message, String textButton, Image icon) {
/*  76 */     return (new MessageAlert(message, new String[] { textButton }, 0)).show(icon);
/*     */   }
/*     */   
/*     */   public static boolean display(String message, String textButton, int optionPane, Image icon) {
/*  80 */     return (new MessageAlert(message, new String[] { textButton }, optionPane)).show(icon);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private MessageAlert(String message) {
/*  88 */     this(message, 0);
/*     */   }
/*     */   
/*     */   private MessageAlert(String message, int optionPane) {
/*  92 */     this(message, OPTIONS, optionPane);
/*     */   }
/*     */   
/*     */   private MessageAlert(String message, String[] options, int optionPane) {
/*  96 */     this
/*  97 */       .jop = new JOptionPane(Strings.trim(message), optionPane, 0, null, (Object[])(this.options = options), options[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean show(Image icon) {
/* 107 */     JDialog dialog = this.jop.createDialog("Atenção!");
/* 108 */     dialog.setAlwaysOnTop(true);
/* 109 */     dialog.setModal(true);
/* 110 */     dialog.setIconImage(icon);
/* 111 */     dialog.setVisible(true);
/* 112 */     dialog.dispose();
/* 113 */     Object selectedValue = this.jop.getValue();
/* 114 */     return this.options[0].equals(selectedValue);
/*     */   }
/*     */   
/*     */   public static void main(String[] args) {
/* 118 */     showFail("leonardo", null);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/gui/imp/MessageAlert.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */