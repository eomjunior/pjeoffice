/*     */ package com.github.progress4j.imp;
/*     */ 
/*     */ import com.github.progress4j.IProgressFactory;
/*     */ import com.github.progress4j.IProgressView;
/*     */ import com.github.utils4j.ICanceller;
/*     */ import com.github.utils4j.IInterruptable;
/*     */ import com.github.utils4j.imp.Args;
/*     */ import com.github.utils4j.imp.Throwables;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
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
/*     */ public class MultiThreadedProgressFactory
/*     */   implements IProgressFactory, ICanceller
/*     */ {
/*     */   private StackProgressView stack;
/*     */   private Disposable stackTicketCancel;
/*     */   private final ThreadLocalProgressFactory threadLocal;
/*  52 */   private final AtomicInteger stackSize = new AtomicInteger(0);
/*     */   
/*  54 */   private final List<Runnable> codes = new ArrayList<>(2);
/*     */   
/*     */   public MultiThreadedProgressFactory() {
/*  57 */     this.threadLocal = new ThreadLocalProgressFactory();
/*     */   }
/*     */ 
/*     */   
/*     */   public final void cancelCode(Runnable code) {
/*  62 */     Args.requireNonNull(code, "code is null");
/*  63 */     this.codes.add(code);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean ifCanceller(Runnable code) {
/*  68 */     cancelCode(code);
/*  69 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void interrupt() {
/*  74 */     this.threadLocal.interrupt();
/*  75 */     this.codes.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public void cancel(Thread thread) {
/*  80 */     this.threadLocal.cancel(thread);
/*     */   }
/*     */ 
/*     */   
/*     */   public IProgressView get() {
/*  85 */     return this.threadLocal.get();
/*     */   }
/*     */ 
/*     */   
/*     */   public void display() {
/*  90 */     if (this.stack != null) {
/*  91 */       this.stack.display();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void undisplay() {
/*  97 */     if (this.stack != null) {
/*  98 */       this.stack.undisplay();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private class ThreadLocalProgressFactory
/*     */     extends ThreadLocal<IProgressView>
/*     */     implements IInterruptable
/*     */   {
/* 108 */     private final StackProgressFactory stackFactory = new StackProgressFactory();
/* 109 */     private final ProgressLineFactory lineFactory = new MultiThreadedProgressFactory.DisposerProgressFactory();
/*     */ 
/*     */ 
/*     */     
/*     */     protected IProgressView initialValue() {
/* 114 */       synchronized (MultiThreadedProgressFactory.this.stackSize) {
/* 115 */         ProgressLineView newLine = this.lineFactory.get();
/* 116 */         if (MultiThreadedProgressFactory.this.stack == null) {
/* 117 */           MultiThreadedProgressFactory.this.stack = this.stackFactory.get();
/* 118 */           MultiThreadedProgressFactory.this.stack.setMode(Mode.HIDDEN);
/* 119 */           MultiThreadedProgressFactory.this.stackTicketCancel = MultiThreadedProgressFactory.this.stack.cancelClick().subscribe(b -> MultiThreadedProgressFactory.this.codes.forEach(()));
/* 120 */           MultiThreadedProgressFactory.this.stack.display();
/* 121 */           MultiThreadedProgressFactory.this.stack.begin("Processando...");
/*     */         } else {
/* 123 */           MultiThreadedProgressFactory.this.stack.setMode(Mode.BATCH);
/*     */         } 
/*     */         try {
/* 126 */           MultiThreadedProgressFactory.this.stack.push(newLine);
/* 127 */         } catch (InterruptedException e) {
/* 128 */           newLine.interrupt();
/*     */         } 
/* 130 */         MultiThreadedProgressFactory.this.stackSize.incrementAndGet();
/* 131 */         return (IProgressView)newLine;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public final void interrupt() {
/* 137 */       if (MultiThreadedProgressFactory.this.stack != null) {
/* 138 */         MultiThreadedProgressFactory.this.stack.interrupt();
/*     */       }
/* 140 */       this.lineFactory.interrupt();
/*     */     }
/*     */     
/*     */     public final void cancel(Thread thread) {
/* 144 */       if (thread != null) {
/* 145 */         if (MultiThreadedProgressFactory.this.stack != null) {
/* 146 */           MultiThreadedProgressFactory.this.stack.cancel(thread);
/*     */         }
/* 148 */         this.lineFactory.cancel(thread);
/*     */       } 
/*     */     }
/*     */     
/*     */     private ThreadLocalProgressFactory() {} }
/*     */   
/*     */   private class DisposerProgressFactory extends ProgressLineFactory {
/*     */     DisposerProgressFactory() {
/* 156 */       super(true);
/*     */     }
/*     */ 
/*     */     
/*     */     protected void onDisposed(ProgressLineView pv) {
/* 161 */       synchronized (MultiThreadedProgressFactory.this.stackSize) {
/* 162 */         MultiThreadedProgressFactory.this.threadLocal.remove();
/* 163 */         int total = MultiThreadedProgressFactory.this.stackSize.decrementAndGet();
/* 164 */         Thread.interrupted();
/* 165 */         MultiThreadedProgressFactory.this.stack.info("Fim do processamento. Demanda => %s", new Object[] { pv.getName() });
/*     */         try {
/* 167 */           MultiThreadedProgressFactory.this.stack.remove(pv);
/*     */         } finally {
/* 169 */           if (total == 1) {
/* 170 */             MultiThreadedProgressFactory.this.stack.setMode(Mode.HIDDEN);
/* 171 */           } else if (total == 0) {
/*     */             try {
/* 173 */               MultiThreadedProgressFactory.this.stack.end();
/* 174 */               MultiThreadedProgressFactory.this.stack.undisplay();
/*     */             } finally {
/* 176 */               MultiThreadedProgressFactory.this.stack.dispose();
/* 177 */               MultiThreadedProgressFactory.this.stack = null;
/* 178 */               MultiThreadedProgressFactory.this.stackTicketCancel.dispose();
/* 179 */               MultiThreadedProgressFactory.this.stackTicketCancel = null;
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/progress4j/imp/MultiThreadedProgressFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */