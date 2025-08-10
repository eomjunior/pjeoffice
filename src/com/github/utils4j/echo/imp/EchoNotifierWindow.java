/*     */ package com.github.utils4j.echo.imp;
/*     */ 
/*     */ import com.github.utils4j.echo.IEcho;
/*     */ import com.github.utils4j.gui.imp.SimpleFrame;
/*     */ import com.github.utils4j.gui.imp.SwingTools;
/*     */ import com.github.utils4j.imp.Args;
/*     */ import com.github.utils4j.imp.Strings;
/*     */ import com.github.utils4j.imp.Threads;
/*     */ import com.github.utils4j.imp.Throwables;
/*     */ import io.reactivex.Observable;
/*     */ import java.awt.event.ActionEvent;
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
/*     */ public class EchoNotifierWindow
/*     */   extends EchoNotifier
/*     */ {
/*     */   protected static final String DEFAULT_RECEIVE_HEADER_FORMAT = "==========================================================================\n Recebida requisição %s: \n==========================================================================\n";
/*     */   protected final String headerFormat;
/*     */   protected final String title;
/*     */   protected SimpleFrame frame;
/*     */   
/*     */   public EchoNotifierWindow() {
/*  55 */     this(Strings.empty());
/*     */   }
/*     */   
/*     */   public EchoNotifierWindow(String title) {
/*  59 */     this(title, "==========================================================================\n Recebida requisição %s: \n==========================================================================\n");
/*     */   }
/*     */   
/*     */   public EchoNotifierWindow(String title, String headerFormat) {
/*  63 */     this.title = (String)Args.requireNonNull(title, "title is null");
/*  64 */     this.headerFormat = (String)Args.requireNonNull(headerFormat, "headerFormat is null");
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doOpen() {
/*  69 */     super.doOpen();
/*  70 */     this.frame = createFrame();
/*     */   }
/*     */   
/*     */   protected SimpleFrame createFrame() {
/*  74 */     return new EchoFrame(getEcho(), this.title, createPanel())
/*     */       {
/*     */         protected void onEscPressed(ActionEvent e) {
/*  77 */           setVisible(false);
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   protected IEcho createPanel() {
/*  83 */     return new EchoPanel(this.headerFormat);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doClose() {
/*  88 */     super.doClose();
/*  89 */     if (!Threads.isShutdownHook()) {
/*  90 */       Throwables.quietly(this.frame::close);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void display() {
/*  96 */     SwingTools.invokeAndWait(this.frame::showToFront);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isDisplayed() {
/* 101 */     return this.frame.isVisible();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/echo/imp/EchoNotifierWindow.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */