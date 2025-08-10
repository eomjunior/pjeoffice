/*     */ package com.github.utils4j.imp;
/*     */ 
/*     */ import java.awt.GraphicsConfiguration;
/*     */ import java.awt.GraphicsDevice;
/*     */ import java.awt.MouseInfo;
/*     */ import java.awt.PointerInfo;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.Window;
/*     */ import java.awt.event.WindowAdapter;
/*     */ import java.awt.event.WindowEvent;
/*     */ import javax.swing.JFrame;
/*     */ import javax.swing.SwingUtilities;
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
/*     */ public class GuiTools
/*     */ {
/*     */   public static void mouseTracker(Window dialog) {
/*  48 */     mouseTracker(dialog, 1000L);
/*     */   }
/*     */   
/*     */   public static void mouseTracker(Window dialog, long delayMillis) {
/*  52 */     Args.requireNonNull(dialog, "dialog is null");
/*  53 */     dialog.addWindowListener(new MouseTracker(delayMillis, dialog));
/*     */   }
/*     */   
/*     */   public static void showOnMousePointer(Window dialog) {
/*  57 */     setMouseLocation(dialog);
/*  58 */     if (!dialog.isVisible())
/*  59 */       dialog.setVisible(true); 
/*     */   }
/*     */   
/*     */   private static class MouseTracker
/*     */     extends WindowAdapter
/*     */     implements Runnable {
/*     */     private final Window dialog;
/*     */     private final long delayMillis;
/*     */     private volatile boolean running = true;
/*  68 */     private final Thread thread = new Thread(this, "Mouse tracker");
/*     */     
/*     */     private MouseTracker(long delayMillis, Window dialog) {
/*  71 */       this.delayMillis = delayMillis;
/*  72 */       this.dialog = dialog;
/*  73 */       this.thread.setDaemon(true);
/*  74 */       this.thread.setPriority(1);
/*  75 */       this.thread.start();
/*     */     }
/*     */ 
/*     */     
/*     */     public void windowClosing(WindowEvent e) {
/*  80 */       this.running = false;
/*     */     }
/*     */ 
/*     */     
/*     */     public void run() {
/*  85 */       while (this.running) {
/*     */         try {
/*  87 */           Thread.sleep(this.delayMillis);
/*  88 */           SwingUtilities.invokeLater(() -> {
/*     */                 if (!this.dialog.isVisible() || !this.dialog.isDisplayable()) {
/*     */                   this.running = false;
/*     */                 }
/*     */                 if (this.running) {
/*     */                   GuiTools.setMouseLocation(this.dialog);
/*     */                 }
/*     */               });
/*  96 */         } catch (Throwable e) {
/*  97 */           this.running = false;
/*     */         } 
/*     */       } 
/*     */     } }
/*     */   
/*     */   public static void setMouseLocation(Window window) {
/* 103 */     Args.requireNonNull(window, "dialog is null");
/* 104 */     if (window instanceof JFrame) {
/* 105 */       JFrame frame = (JFrame)window;
/* 106 */       if (frame.getExtendedState() == 6) {
/*     */         return;
/*     */       }
/*     */     } 
/* 110 */     Throwables.call(MouseInfo::getPointerInfo).ifPresent(mouseInfo -> {
/*     */           GraphicsDevice mouseDevice = mouseInfo.getDevice();
/*     */           if (mouseDevice == null)
/*     */             return; 
/*     */           GraphicsConfiguration configuration = mouseDevice.getDefaultConfiguration();
/*     */           if (configuration == null)
/*     */             return; 
/*     */           Rectangle bounds = configuration.getBounds();
/*     */           if (bounds == null)
/*     */             return; 
/*     */           if (bounds.contains(window.getLocation()))
/*     */             return; 
/*     */           int width = bounds.width;
/*     */           int height = bounds.height;
/*     */           int xcoordinate = width / 2 - (window.getSize()).width / 2 + bounds.x;
/*     */           int ycoordinate = height / 2 - (window.getSize()).height / 2 + bounds.y;
/*     */           window.setLocation(xcoordinate, ycoordinate);
/*     */           window.toFront();
/*     */         });
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/imp/GuiTools.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */