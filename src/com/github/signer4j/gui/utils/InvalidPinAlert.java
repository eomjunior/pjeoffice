/*     */ package com.github.signer4j.gui.utils;
/*     */ 
/*     */ import com.github.utils4j.gui.imp.SwingTools;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class InvalidPinAlert
/*     */ {
/*     */   private static final String MESSAGE_FORMAT = "Senha inválida! Tentar novamente?";
/*     */   private static final String MESSAGE_FORMAT_ALERT = "Senha inválida!\nATENÇÃO:   Você já errou a senha por %s vezes!\nPor segurança o excesso de tentativas erradas\n\n=>BLOQUEARÁ O SEU DISPOSITIVO<=.\n\nSe você não possui uma senha de administração (PUK), procure um\nsuporte técnico ANTES que seu dispositivo seja bloqueado!\n\nDeseja tentar novamente? (Na dúvida, opte por NÃO)";
/*  50 */   private static final String[] OPTIONS = new String[] { "SIM", "NÃO" }; private final JOptionPane jop;
/*     */   
/*     */   public static boolean isYes(int times) {
/*  53 */     return SwingTools.isTrue(() -> Boolean.valueOf(display(times)));
/*     */   }
/*     */   private final Image icon;
/*     */   public static boolean isNo(int times) {
/*  57 */     return !isYes(times);
/*     */   }
/*     */   
/*     */   public static boolean display() {
/*  61 */     return display(0);
/*     */   }
/*     */   
/*     */   public static boolean display(int times) {
/*  65 */     return display(times, Images.LOCK.asImage());
/*     */   }
/*     */   
/*     */   public static boolean display(int times, Image icon) {
/*  69 */     return (new InvalidPinAlert(times, icon)).show();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private InvalidPinAlert(int times, Image icon) {
/*  77 */     this.icon = icon;
/*  78 */     this
/*     */ 
/*     */ 
/*     */       
/*  82 */       .jop = new JOptionPane(String.format((times < 2) ? "Senha inválida! Tentar novamente?" : "Senha inválida!\nATENÇÃO:   Você já errou a senha por %s vezes!\nPor segurança o excesso de tentativas erradas\n\n=>BLOQUEARÁ O SEU DISPOSITIVO<=.\n\nSe você não possui uma senha de administração (PUK), procure um\nsuporte técnico ANTES que seu dispositivo seja bloqueado!\n\nDeseja tentar novamente? (Na dúvida, opte por NÃO)", new Object[] { Integer.valueOf(times) }), 3, 0, Images.LOCK.asIcon(), (Object[])OPTIONS, OPTIONS[1]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean show() {
/*  89 */     JDialog dialog = this.jop.createDialog("Alerta de seguraça");
/*  90 */     dialog.setAlwaysOnTop(true);
/*  91 */     dialog.setModal(true);
/*  92 */     dialog.setIconImage(this.icon);
/*  93 */     dialog.setVisible(true);
/*  94 */     dialog.dispose();
/*  95 */     Object selectedValue = this.jop.getValue();
/*  96 */     return OPTIONS[0].equals(selectedValue);
/*     */   }
/*     */   
/*     */   public static void main(String[] args) {
/* 100 */     display(2);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/gui/utils/InvalidPinAlert.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */