/*     */ package com.github.progress4j.imp;
/*     */ 
/*     */ import com.github.utils4j.gui.imp.SwingTools;
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Color;
/*     */ import java.awt.Container;
/*     */ import java.awt.Cursor;
/*     */ import java.awt.Font;
/*     */ import java.awt.GridLayout;
/*     */ import java.awt.LayoutManager;
/*     */ import java.awt.event.MouseAdapter;
/*     */ import java.awt.event.MouseEvent;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.JSeparator;
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
/*     */ 
/*     */ 
/*     */ class ProgressBox
/*     */   extends ProgressHandler<ProgressBox>
/*     */ {
/*     */   private static final String SHOW_DETAILS = "<html><u>Ver detalhes</u></html>";
/*     */   private static final String HIDE_DETAILS = "<html><u>Esconder detalhes</u></html>";
/*  54 */   private final JPanel southPane = new JPanel();
/*     */   
/*  56 */   private final JLabel detailsPane = new JLabel("<html><u>Ver detalhes</u></html>");
/*     */ 
/*     */   
/*     */   private Mode mode;
/*     */ 
/*     */   
/*     */   private void setupLayout() {
/*  63 */     setLayout(new BorderLayout());
/*  64 */     add(north(), "North");
/*  65 */     add(center(), "Center");
/*  66 */     add(south(), "South");
/*     */   }
/*     */ 
/*     */   
/*     */   public final ProgressBox asContainer() {
/*  71 */     return this;
/*     */   }
/*     */   
/*     */   private JScrollPane center() {
/*  75 */     this.scrollPane.setVisible(false);
/*  76 */     return this.scrollPane;
/*     */   }
/*     */   
/*     */   private JPanel south() {
/*  80 */     JButton cancelButton = new JButton("Cancelar");
/*  81 */     cancelButton.addActionListener(this::onCancel);
/*  82 */     JButton cleanButton = new JButton("Limpar");
/*  83 */     cleanButton.setPreferredSize(cancelButton.getPreferredSize());
/*  84 */     cleanButton.addActionListener(this::onClear);
/*  85 */     this.southPane.setLayout((LayoutManager)new MigLayout("fillx", "push[][]", "[][]"));
/*  86 */     this.southPane.add(cleanButton);
/*  87 */     this.southPane.add(cancelButton);
/*  88 */     this.southPane.setVisible(false);
/*  89 */     return this.southPane;
/*     */   }
/*     */   
/*     */   private JPanel north() {
/*  93 */     JPanel north = new JPanel();
/*  94 */     north.setLayout(new GridLayout(3, 1, 0, 12));
/*  95 */     north.add(this.progressBar);
/*  96 */     north.add(detail());
/*  97 */     north.add(new JSeparator());
/*  98 */     return north;
/*     */   }
/*     */   
/*     */   private JLabel detail() {
/* 102 */     this.detailsPane.setCursor(Cursor.getPredefinedCursor(12));
/* 103 */     this.detailsPane.setHorizontalAlignment(0);
/* 104 */     this.detailsPane.setVerticalAlignment(0);
/* 105 */     this.detailsPane.setForeground(Color.BLUE);
/* 106 */     this.detailsPane.setFont(new Font("Tahoma", 0, 12));
/* 107 */     this.detailsPane.addMouseListener(new MouseAdapter() {
/*     */           public void mouseClicked(MouseEvent e) {
/* 109 */             ProgressBox.this.detailStatus.onNext(Boolean.valueOf(ProgressBox.this.isShowDetail()));
/*     */           }
/*     */         });
/* 112 */     return this.detailsPane;
/*     */   }
/*     */   
/*     */   final boolean isShowDetail() {
/* 116 */     return "<html><u>Ver detalhes</u></html>".equals(this.detailsPane.getText());
/*     */   }
/*     */   
/*     */   final boolean isHideDetail() {
/* 120 */     return !isShowDetail();
/*     */   }
/*     */   ProgressBox() {
/* 123 */     this.mode = Mode.NORMAL;
/*     */     setupLayout();
/*     */   }
/*     */   public final void showSteps(boolean visible) {
/* 127 */     SwingTools.invokeLater(() -> {
/*     */           this.detailsPane.setText(visible ? "<html><u>Esconder detalhes</u></html>" : "<html><u>Ver detalhes</u></html>");
/* 129 */           this.southPane.setVisible((visible && Mode.NORMAL.equals(this.mode)));
/* 130 */           super.showSteps((visible && Mode.NORMAL.equals(this.mode)));
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   protected void setMode(Mode mode) {
/* 136 */     SwingTools.invokeLater(() -> {
/*     */           setVisible(!Mode.HIDDEN.equals(mode));
/*     */           if (Mode.BATCH.equals(this.mode)) {
/*     */             this.mode = mode;
/*     */             return;
/*     */           } 
/*     */           if (Mode.BATCH.equals(mode)) {
/*     */             if (isHideDetail()) {
/*     */               this.detailsPane.setText("<html><u>Ver detalhes</u></html>");
/*     */             }
/*     */             this.southPane.setVisible(false);
/*     */             super.showSteps(false);
/*     */           } else if (Mode.NORMAL.equals(mode)) {
/*     */           
/*     */           } 
/*     */           this.mode = mode;
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   protected Mode getMode() {
/* 157 */     return this.mode;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/progress4j/imp/ProgressBox.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */