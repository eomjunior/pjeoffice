/*     */ package com.github.utils4j.gui.imp;
/*     */ 
/*     */ import java.awt.Image;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
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
/*     */ public final class CancelAlert
/*     */ {
/*  40 */   public static String CANCELED_OPERATION_MESSAGE = "Operação cancelada!";
/*     */   
/*  42 */   public static String CANCELING_OPERATION_MESSAGE = "Operação em cancelamento! Aguarde...";
/*     */   
/*  44 */   private static final AtomicBoolean VISIBLE = new AtomicBoolean(false);
/*     */   
/*     */   public static void show() {
/*  47 */     show(null);
/*     */   }
/*     */   
/*     */   public static void show(Image icon) {
/*  51 */     SwingTools.invokeLater(() -> display(icon));
/*     */   }
/*     */   
/*     */   public static void showWaiting() {
/*  55 */     showWaiting(null);
/*     */   }
/*     */   
/*     */   public static void showWaiting(Image icon) {
/*  59 */     SwingTools.invokeLater(() -> displayWaiting(icon));
/*     */   }
/*     */   
/*     */   public static boolean display() {
/*  63 */     return display(null);
/*     */   }
/*     */   
/*     */   public static boolean display(Image icon) {
/*  67 */     return display(CANCELED_OPERATION_MESSAGE, icon);
/*     */   }
/*     */   
/*     */   public static boolean displayWaiting() {
/*  71 */     return displayWaiting(null);
/*     */   }
/*     */   
/*     */   public static boolean displayWaiting(Image icon) {
/*  75 */     return display(CANCELING_OPERATION_MESSAGE, icon);
/*     */   }
/*     */   
/*     */   private static boolean display(String message, Image icon) {
/*  79 */     if (!VISIBLE.getAndSet(true)) {
/*  80 */       return (new CancelAlert(message)).reveal(icon);
/*     */     }
/*  82 */     return false;
/*     */   }
/*     */   
/*  85 */   private static final String[] OPTIONS = new String[] { "OK" };
/*     */   
/*     */   private final JOptionPane jop;
/*     */   
/*     */   private CancelAlert(String message) {
/*  90 */     this.jop = new JOptionPane(message, 0, 0, null, (Object[])OPTIONS, OPTIONS[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean reveal(Image icon) {
/* 101 */     JDialog dialog = this.jop.createDialog("Atenção!");
/* 102 */     dialog.setAlwaysOnTop(true);
/* 103 */     dialog.setModal(true);
/* 104 */     dialog.setIconImage(icon);
/* 105 */     dialog.setVisible(true);
/* 106 */     dialog.dispose();
/* 107 */     VISIBLE.set(false);
/* 108 */     Object selectedValue = this.jop.getValue();
/* 109 */     return OPTIONS[0].equals(selectedValue);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/gui/imp/CancelAlert.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */