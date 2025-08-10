/*     */ package com.github.progress4j.imp;
/*     */ 
/*     */ import com.github.utils4j.gui.imp.SwingTools;
/*     */ import com.github.utils4j.imp.NotImplementedException;
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.Cursor;
/*     */ import java.awt.LayoutManager;
/*     */ import java.awt.event.MouseAdapter;
/*     */ import java.awt.event.MouseEvent;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JPanel;
/*     */ import net.miginfocom.swing.MigLayout;
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
/*     */ class ProgressLine
/*     */   extends ProgressHandler<ProgressLine>
/*     */ {
/*  46 */   private JButton cancelButton = new JButton();
/*     */   
/*  48 */   private JPanel center = new JPanel();
/*     */   
/*     */   protected ProgressLine() {
/*  51 */     this(true);
/*     */   }
/*     */   
/*     */   protected ProgressLine(boolean showCancel) {
/*  55 */     setupComponents();
/*  56 */     setupLayout(showCancel);
/*     */   }
/*     */ 
/*     */   
/*     */   public final ProgressLine asContainer() {
/*  61 */     return this;
/*     */   }
/*     */   
/*     */   private final void setupLayout(boolean showCancel) {
/*  65 */     setLayout(new BorderLayout());
/*  66 */     add(north(showCancel), "North");
/*  67 */     add(center(), "Center");
/*     */   }
/*     */   
/*     */   private void setupComponents() {
/*  71 */     setupCancel();
/*  72 */     setupProgress();
/*  73 */     setupLog();
/*     */   }
/*     */   
/*     */   private void setupLog() {
/*  77 */     this.center.setVisible(false);
/*     */   }
/*     */   
/*     */   private void setupCancel() {
/*  81 */     this.cancelButton.setIcon(Images.CANCEL.asIcon());
/*  82 */     this.cancelButton.setToolTipText("Cancelar esta operação.");
/*  83 */     this.cancelButton.addActionListener(this::onCancel);
/*     */   }
/*     */   
/*     */   private Component north(boolean showCancel) {
/*  87 */     JPanel north = new JPanel();
/*  88 */     north.setLayout((LayoutManager)new MigLayout());
/*  89 */     north.add(this.progressBar, "pushx, growx");
/*  90 */     if (showCancel) {
/*  91 */       north.add(this.cancelButton);
/*     */     }
/*  93 */     return north;
/*     */   }
/*     */   
/*     */   private void setupProgress() {
/*  97 */     this.progressBar.setToolTipText("Clique para ver os detalhes.");
/*  98 */     this.progressBar.setCursor(Cursor.getPredefinedCursor(12));
/*  99 */     this.progressBar.addMouseListener(new MouseAdapter() {
/*     */           public void mousePressed(MouseEvent e) {
/* 101 */             ProgressLine.this.detailStatus.onNext(Boolean.valueOf(!ProgressLine.this.center.isVisible()));
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   private JPanel center() {
/* 107 */     this.center.setLayout((LayoutManager)new MigLayout());
/* 108 */     this.center.add(this.scrollPane, "pushx, pushy, growx, growy");
/* 109 */     return this.center;
/*     */   }
/*     */ 
/*     */   
/*     */   public final void showSteps(boolean visible) {
/* 114 */     SwingTools.invokeLater(() -> this.center.setVisible(visible));
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isStepsVisible() {
/* 119 */     return this.center.isVisible();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void setMode(Mode mode) {
/* 124 */     throw new NotImplementedException("This feature not implemented for " + getClass().getCanonicalName());
/*     */   }
/*     */ 
/*     */   
/*     */   protected Mode getMode() {
/* 129 */     throw new NotImplementedException("This feature not implemented for " + getClass().getCanonicalName());
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/progress4j/imp/ProgressLine.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */