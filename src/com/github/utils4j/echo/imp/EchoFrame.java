/*     */ package com.github.utils4j.echo.imp;
/*     */ 
/*     */ import com.github.utils4j.echo.IEcho;
/*     */ import com.github.utils4j.gui.imp.Images;
/*     */ import com.github.utils4j.gui.imp.SimpleFrame;
/*     */ import com.github.utils4j.gui.imp.SwingTools;
/*     */ import com.github.utils4j.imp.Args;
/*     */ import io.reactivex.Observable;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Component;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.LayoutManager;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.WindowAdapter;
/*     */ import java.awt.event.WindowEvent;
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
/*     */ 
/*     */ 
/*     */ public class EchoFrame
/*     */   extends SimpleFrame
/*     */ {
/*  53 */   private static final Dimension MININUM_SIZE = new Dimension(300, 350);
/*     */   
/*     */   private final IEcho panel;
/*     */   
/*     */   private final Disposable ticket;
/*     */   
/*     */   public EchoFrame(Observable<String> echoCallback) {
/*  60 */     this(echoCallback, "Echo", new EchoPanel());
/*     */   }
/*     */   
/*     */   public EchoFrame(Observable<String> echoCallback, IEcho panel) {
/*  64 */     this(echoCallback, "Echo", panel);
/*     */   }
/*     */   
/*     */   public EchoFrame(Observable<String> echoCallback, String title) {
/*  68 */     this(echoCallback, title, new EchoPanel());
/*     */   }
/*     */   
/*     */   public EchoFrame(Observable<String> echoCallback, String title, IEcho panel) {
/*  72 */     super(title);
/*  73 */     echoCallback = (Observable<String>)Args.requireNonNull(echoCallback, "requestCallback is null");
/*  74 */     this.panel = (IEcho)Args.requireNonNull(panel, "panel is null");
/*  75 */     this.ticket = echoCallback.subscribe(this::handleRequest);
/*  76 */     setup();
/*     */   }
/*     */ 
/*     */   
/*     */   public void dispose() {
/*  81 */     this.ticket.dispose();
/*  82 */     super.dispose();
/*     */   }
/*     */   
/*     */   private void handleRequest(String request) {
/*  86 */     if (request != null) {
/*  87 */       SwingTools.invokeLater(() -> this.panel.addRequest(request));
/*     */     }
/*     */   }
/*     */   
/*     */   private void setup() {
/*  92 */     setupLayout();
/*  93 */     setDefaultCloseOperation(0);
/*  94 */     setIconImage(Images.ECHO.asImage());
/*  95 */     setFixedMinimumSize(MININUM_SIZE);
/*  96 */     pack();
/*  97 */     toCenter();
/*  98 */     addWindowListener(new WindowAdapter() {
/*     */           public void windowClosing(WindowEvent windowEvent) {
/* 100 */             EchoFrame.this.onEscPressed(null);
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   private void setupLayout() {
/* 106 */     JPanel content = new JPanel();
/* 107 */     content.setLayout(new BorderLayout(0, 0));
/* 108 */     content.add(this.panel.asPanel(), "Center");
/* 109 */     content.add(south(), "South");
/* 110 */     setContentPane(content);
/*     */   }
/*     */   
/*     */   private Component south() {
/* 114 */     JButton cleanButton = new JButton("Limpar");
/* 115 */     cleanButton.addActionListener(e -> this.panel.clear());
/* 116 */     JButton closeButton = new JButton("Fechar");
/* 117 */     closeButton.addActionListener(this::onEscPressed);
/*     */     
/* 119 */     JPanel southPane = new JPanel();
/* 120 */     southPane.setLayout((LayoutManager)new MigLayout("fillx", "push[][]", "[][]"));
/* 121 */     southPane.add(cleanButton);
/* 122 */     southPane.add(closeButton);
/* 123 */     return southPane;
/*     */   }
/*     */   
/*     */   public static void main(String[] args) {}
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/echo/imp/EchoFrame.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */