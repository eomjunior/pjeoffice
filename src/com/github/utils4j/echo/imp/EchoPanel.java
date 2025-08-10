/*     */ package com.github.utils4j.echo.imp;
/*     */ 
/*     */ import com.github.utils4j.echo.IEcho;
/*     */ import com.github.utils4j.gui.imp.SwingTools;
/*     */ import com.github.utils4j.imp.Args;
/*     */ import com.github.utils4j.imp.Strings;
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Dimension;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.JTextArea;
/*     */ import javax.swing.border.EtchedBorder;
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
/*     */ public class EchoPanel
/*     */   extends JPanel
/*     */   implements IEcho
/*     */ {
/*  46 */   private static final Dimension DEFAULT_SIZE = new Dimension(500, 680);
/*     */   
/*     */   private static final int MAX_ITEM_COUNT = 20;
/*     */   
/*  50 */   private final JTextArea textArea = new JTextArea();
/*     */   
/*     */   private final String headerItemFormat;
/*     */   
/*  54 */   private int itemCount = 0;
/*     */   
/*     */   public EchoPanel() {
/*  57 */     this("%s\n");
/*     */   }
/*     */   
/*     */   public EchoPanel(String headerItemFormat) {
/*  61 */     this.headerItemFormat = (String)Args.requireNonNull(headerItemFormat, "headerItemFormat is null");
/*  62 */     setup();
/*     */   }
/*     */   
/*     */   protected JPanel north() {
/*  66 */     return new JPanel();
/*     */   }
/*     */ 
/*     */   
/*     */   public JPanel asPanel() {
/*  71 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public final void clear() {
/*  76 */     clean(0);
/*     */   }
/*     */   
/*     */   private void clean(int start) {
/*  80 */     SwingTools.invokeLater(() -> {
/*     */           this.textArea.setText(Strings.empty());
/*     */           this.itemCount = start;
/*     */           onNewItem(Strings.empty(), this.itemCount);
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public final void addRequest(String request) {
/*  89 */     if (request != null) {
/*  90 */       SwingTools.invokeLater(() -> addItem(request));
/*     */     }
/*     */   }
/*     */   
/*     */   private void addItem(String item) {
/*  95 */     if (this.itemCount >= 20) {
/*  96 */       clean(this.itemCount);
/*     */     }
/*  98 */     onNewItem(item, ++this.itemCount);
/*  99 */     this.textArea.append(String.format(this.headerItemFormat, new Object[] { Integer.valueOf(this.itemCount) }));
/* 100 */     this.textArea.append(item + "\n\r\n\r");
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onNewItem(String item, int count) {}
/*     */ 
/*     */   
/*     */   private void setup() {
/* 108 */     setupLayout();
/* 109 */     setupFrame();
/*     */   }
/*     */   
/*     */   private void setupFrame() {
/* 113 */     setBounds((getBounds()).x, (getBounds()).y, DEFAULT_SIZE.width, DEFAULT_SIZE.height);
/*     */   }
/*     */   
/*     */   private JScrollPane center() {
/* 117 */     JScrollPane centerPane = new JScrollPane();
/* 118 */     this.textArea.setRows(8);
/* 119 */     this.textArea.setEditable(false);
/* 120 */     centerPane.setViewportView(this.textArea);
/* 121 */     centerPane.setVisible(true);
/* 122 */     return centerPane;
/*     */   }
/*     */   
/*     */   private void setupLayout() {
/* 126 */     setBorder(new EtchedBorder(1, null, null));
/* 127 */     setLayout(new BorderLayout(0, 0));
/* 128 */     add(north(), "North");
/* 129 */     add(center(), "Center");
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/echo/imp/EchoPanel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */