/*     */ package com.github.progress4j.imp;
/*     */ 
/*     */ import com.github.progress4j.IProgressFactory;
/*     */ import com.github.progress4j.IProgressView;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Function;
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
/*     */ public enum ProgressFactories
/*     */   implements IProgressFactory
/*     */ {
/*     */   private static final Consumer<Throwable> NOTHING_BLOCK;
/*     */   private final IProgressFactory factory;
/*  37 */   IDLE(new ProgressIdleFactory()),
/*     */   
/*  39 */   LINE(new ProgressFrameLineFactory()),
/*     */   
/*  41 */   BOX(new ProgressFrameFactory()),
/*     */   
/*  43 */   SIMPLE(new ProgressFrameLineFactory(true)),
/*     */   
/*  45 */   THREAD(new MultiThreadedProgressFactory());
/*     */   static {
/*  47 */     NOTHING_BLOCK = (e -> {
/*     */       
/*     */       });
/*     */   }
/*     */   ProgressFactories(IProgressFactory factory) {
/*  52 */     this.factory = factory;
/*     */   }
/*     */   
/*     */   public IProgressView get() {
/*  56 */     return (IProgressView)this.factory.get();
/*     */   }
/*     */ 
/*     */   
/*     */   public void interrupt() {
/*  61 */     this.factory.interrupt();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean ifCanceller(Runnable code) {
/*  66 */     return this.factory.ifCanceller(code);
/*     */   }
/*     */ 
/*     */   
/*     */   public void cancel(Thread currentThread) {
/*  71 */     this.factory.cancel(currentThread);
/*     */   }
/*     */ 
/*     */   
/*     */   public void display() {
/*  76 */     this.factory.display();
/*     */   }
/*     */ 
/*     */   
/*     */   public void undisplay() {
/*  81 */     this.factory.undisplay();
/*     */   }
/*     */   
/*     */   public static <T> T withSimple(Function<IProgressView, T> function) {
/*  85 */     return withFactory(SIMPLE, function);
/*     */   }
/*     */   
/*     */   public static <T> T withLine(Function<IProgressView, T> function) {
/*  89 */     return withFactory(LINE, function);
/*     */   }
/*     */   
/*     */   public static <T> T withBox(Function<IProgressView, T> function) {
/*  93 */     return withFactory(BOX, function);
/*     */   }
/*     */   
/*     */   public static <T> T withThread(Function<IProgressView, T> function) {
/*  97 */     return withFactory(THREAD, function);
/*     */   }
/*     */   
/*     */   public static void withSimple(Consumer<IProgressView> consumer) {
/* 101 */     withFactory(SIMPLE, consumer, NOTHING_BLOCK);
/*     */   }
/*     */   
/*     */   public static void withLine(Consumer<IProgressView> consumer) {
/* 105 */     withFactory(LINE, consumer, NOTHING_BLOCK);
/*     */   }
/*     */   
/*     */   public static void withBox(Consumer<IProgressView> consumer) {
/* 109 */     withFactory(BOX, consumer, NOTHING_BLOCK);
/*     */   }
/*     */   
/*     */   public static void withThread(Consumer<IProgressView> consumer) {
/* 113 */     withFactory(THREAD, consumer, NOTHING_BLOCK);
/*     */   }
/*     */   
/*     */   public static void withSimple(Consumer<IProgressView> consumer, Consumer<Throwable> catchBlock) {
/* 117 */     withFactory(SIMPLE, consumer, catchBlock);
/*     */   }
/*     */   
/*     */   public static void withLine(Consumer<IProgressView> consumer, Consumer<Throwable> catchBlock) {
/* 121 */     withFactory(LINE, consumer, catchBlock);
/*     */   }
/*     */   
/*     */   public static void withBox(Consumer<IProgressView> consumer, Consumer<Throwable> catchBlock) {
/* 125 */     withFactory(BOX, consumer, catchBlock);
/*     */   }
/*     */   
/*     */   public static void withThread(Consumer<IProgressView> consumer, Consumer<Throwable> catchBlock) {
/* 129 */     withFactory(THREAD, consumer, catchBlock);
/*     */   }
/*     */   
/*     */   private static void withFactory(IProgressFactory factory, Consumer<IProgressView> consumer, Consumer<Throwable> catchBlock) {
/* 133 */     IProgressView progress = (IProgressView)factory.get();
/*     */     try {
/* 135 */       consumer.accept(progress);
/* 136 */     } catch (Throwable e) {
/* 137 */       catchBlock.accept(e);
/*     */     } finally {
/* 139 */       progress.dispose();
/*     */     } 
/*     */   }
/*     */   
/*     */   private static <T> T withFactory(IProgressFactory factory, Function<IProgressView, T> function) {
/* 144 */     IProgressView progress = (IProgressView)factory.get();
/*     */     try {
/* 146 */       return function.apply(progress);
/*     */     } finally {
/* 148 */       progress.dispose();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/progress4j/imp/ProgressFactories.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */