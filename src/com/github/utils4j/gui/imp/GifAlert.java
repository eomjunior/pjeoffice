/*     */ package com.github.utils4j.gui.imp;
/*     */ 
/*     */ import com.github.utils4j.imp.Args;
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Color;
/*     */ import java.awt.Cursor;
/*     */ import java.awt.Font;
/*     */ import java.awt.Image;
/*     */ import java.awt.LayoutManager;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.MouseAdapter;
/*     */ import java.awt.event.MouseEvent;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.ImageIcon;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JLabel;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GifAlert
/*     */   extends SimpleDialog
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private final String message;
/*     */   private final ImageIcon animation;
/*     */   protected JPanel bodyPanel;
/*     */   
/*     */   protected GifAlert(String title, String message, Image icon, ImageIcon animation) {
/*  61 */     super(title, icon);
/*  62 */     this.message = Args.requireText(message, "message is empty");
/*  63 */     this.animation = (ImageIcon)Args.requireNonNull(animation, "animation is null");
/*  64 */     setWindowState();
/*  65 */     setupLayout();
/*     */   }
/*     */   
/*     */   protected void display() {
/*  69 */     setVisible(true);
/*  70 */     toCenter();
/*  71 */     showToFront();
/*     */   }
/*     */   
/*     */   private void setWindowState() {
/*  75 */     setResizable(false);
/*  76 */     setDefaultCloseOperation(2);
/*     */   }
/*     */   
/*     */   private void setupLayout() {
/*  80 */     setLayout((LayoutManager)new MigLayout());
/*  81 */     add(createMessage(), "wrap");
/*  82 */     add(createBody(), "growx,wrap");
/*  83 */     pack();
/*     */   }
/*     */   
/*     */   private JLabel createMessage() {
/*  87 */     return new JLabel("<html>" + this.message + "<br>&nbsp;</html>");
/*     */   }
/*     */   
/*     */   protected JPanel createBody() {
/*  91 */     this.bodyPanel = new JPanel();
/*  92 */     this.bodyPanel.setLayout(new BorderLayout(0, 5));
/*  93 */     this.bodyPanel.add(createSee(), "North");
/*  94 */     return this.bodyPanel;
/*     */   }
/*     */   
/*     */   protected JPanel createUnderstand() {
/*  98 */     JPanel south = new JPanel();
/*  99 */     JButton ok = new JButton("Ok, entendi!");
/* 100 */     ok.addActionListener(e -> dispose());
/* 101 */     south.add(ok);
/* 102 */     return south;
/*     */   }
/*     */   
/*     */   protected JLabel createGif() {
/* 106 */     JLabel animation = new JLabel(this.animation);
/* 107 */     animation.setBorder(BorderFactory.createLineBorder(new Color(0, 34, 61)));
/* 108 */     return animation;
/*     */   }
/*     */   
/*     */   private JLabel createSee() {
/* 112 */     final JLabel help = new JLabel("<html><u>Veja como</u></html>");
/* 113 */     help.setVerticalAlignment(3);
/* 114 */     help.setCursor(Cursor.getPredefinedCursor(12));
/* 115 */     help.setHorizontalAlignment(0);
/* 116 */     help.setVerticalAlignment(0);
/* 117 */     help.setForeground(Color.BLUE);
/* 118 */     help.setFont(new Font("Tahoma", 0, 12));
/* 119 */     help.addMouseListener(new MouseAdapter() {
/*     */           public void mouseClicked(MouseEvent e) {
/* 121 */             GifAlert.this.bodyPanel.remove(help);
/* 122 */             GifAlert.this.bodyPanel.add(GifAlert.this.createGif(), "Center");
/* 123 */             GifAlert.this.bodyPanel.add(GifAlert.this.createUnderstand(), "South");
/* 124 */             GifAlert.this.bodyPanel.updateUI();
/* 125 */             GifAlert.this.pack();
/* 126 */             help.removeMouseListener(this);
/*     */           }
/*     */         });
/* 129 */     return help;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/gui/imp/GifAlert.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */