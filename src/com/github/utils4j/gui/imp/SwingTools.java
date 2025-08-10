/*     */ package com.github.utils4j.gui.imp;
/*     */ 
/*     */ import com.github.utils4j.imp.Args;
/*     */ import com.github.utils4j.imp.Jvms;
/*     */ import com.github.utils4j.imp.Stack;
/*     */ import com.github.utils4j.imp.Threads;
/*     */ import com.github.utils4j.imp.Throwables;
/*     */ import com.github.utils4j.imp.function.IProcedure;
/*     */ import com.github.utils4j.imp.function.IProvider;
/*     */ import java.awt.Dialog;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Window;
/*     */ import java.awt.event.ComponentAdapter;
/*     */ import java.awt.event.ComponentEvent;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.atomic.AtomicReference;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SwingTools
/*     */ {
/*     */   public static boolean isTrue(IProvider<Boolean> provider) {
/*  58 */     return ((Boolean)invokeAndWait(provider).orElse(Boolean.FALSE)).booleanValue();
/*     */   }
/*     */   
/*     */   public static boolean invokeAndWait(Runnable code) {
/*  62 */     return invokeAndWait(code, false);
/*     */   }
/*     */   
/*     */   public static boolean invokeAndWait(Runnable code, boolean defaultIfFail) {
/*  66 */     Args.requireNonNull(code, "code is null");
/*  67 */     IProcedure<Boolean, ?> p = SwingUtilities.isEventDispatchThread() ? (() -> { code.run(); return Boolean.valueOf(true);
/*     */       }) : (() -> {
/*     */         SwingUtilities.invokeAndWait(code); return Boolean.valueOf(true);
/*  70 */       }); return ((Boolean)Throwables.call(p, Boolean.valueOf(defaultIfFail))).booleanValue();
/*     */   }
/*     */   
/*     */   public static void invokeLater(Runnable code) {
/*  74 */     Args.requireNonNull(code, "code is null");
/*  75 */     if (SwingUtilities.isEventDispatchThread()) {
/*  76 */       code.run();
/*     */     } else {
/*  78 */       SwingUtilities.invokeLater(code);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static <T> Optional<T> invokeAndWaitT(IProvider<Optional<T>> provider) {
/*  83 */     return invokeAndWait(() -> ((Optional)provider.get()).orElse(null));
/*     */   }
/*     */   
/*     */   public static <T> T invokeAndWaitValue(IProvider<T> provider, T defaultFail) {
/*  87 */     return invokeAndWait(provider).orElse(defaultFail);
/*     */   }
/*     */   
/*     */   public static <T> Optional<T> invokeAndWait(IProvider<T> provider) {
/*  91 */     Args.requireNonNull(provider, "provider is null");
/*  92 */     IProcedure<Optional<T>, ?> p = SwingUtilities.isEventDispatchThread() ? (() -> Optional.ofNullable(provider.get())) : (() -> {
/*     */         AtomicReference<T> ref = new AtomicReference<>();
/*     */         
/*     */         SwingUtilities.invokeAndWait(());
/*     */         
/*     */         return Optional.ofNullable(ref.get());
/*     */       });
/*  99 */     return (Optional<T>)Throwables.call(p, Optional.empty());
/*     */   }
/*     */   
/*     */   public static void setFixedMinimumSize(final Window window, Dimension dimension) {
/* 103 */     Args.requireNonNull(window, "window is null");
/* 104 */     Args.requireNonNull(dimension, "dimension is null");
/* 105 */     window.setMinimumSize(dimension);
/* 106 */     window.addComponentListener(new ComponentAdapter()
/*     */         {
/*     */           public void componentResized(ComponentEvent e) {
/* 109 */             Dimension windowDimension = window.getSize();
/* 110 */             Dimension minimumDimension = window.getMinimumSize();
/* 111 */             windowDimension.width = Math.max(windowDimension.width, minimumDimension.width);
/* 112 */             windowDimension.height = Math.max(windowDimension.height, minimumDimension.height);
/* 113 */             window.setSize(windowDimension);
/*     */           }
/*     */         });
/*     */   }
/*     */   
/* 118 */   private static Stack<Window> stackOnTop = new Stack();
/*     */   
/*     */   public static void showToFront(Window window) {
/* 121 */     showToFront(window, true);
/*     */   }
/*     */   
/*     */   public static void showToFront(Window window, boolean force) {
/* 125 */     Args.requireNonNull(window, "window is null");
/*     */     
/* 127 */     if (window instanceof Dialog) {
/* 128 */       Dialog d = (Dialog)window;
/* 129 */       if (d.isModal()) {
/* 130 */         if (!stackOnTop.isEmpty())
/* 131 */           ((Window)stackOnTop.peek()).setAlwaysOnTop(false); 
/* 132 */         window.setAlwaysOnTop(true);
/* 133 */         stackOnTop.push(window);
/* 134 */         toFront(window, null, force);
/* 135 */         d.setVisible(true);
/* 136 */         stackOnTop.pop();
/* 137 */         if (!stackOnTop.isEmpty())
/* 138 */           ((Window)stackOnTop.peek()).setAlwaysOnTop(true); 
/*     */         return;
/*     */       } 
/*     */     } 
/* 142 */     boolean top = window.isAlwaysOnTop();
/* 143 */     if (force)
/* 144 */       window.setAlwaysOnTop(true); 
/* 145 */     window.setVisible(true);
/* 146 */     toFront(window, Boolean.valueOf(top), force);
/*     */   }
/*     */   
/*     */   private static void toFront(Window window, Boolean top, boolean force) {
/* 150 */     Threads.startDaemon(() -> invokeLater(()), 600L);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/gui/imp/SwingTools.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */