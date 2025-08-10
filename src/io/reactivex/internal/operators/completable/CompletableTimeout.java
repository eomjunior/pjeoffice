/*     */ package io.reactivex.internal.operators.completable;
/*     */ 
/*     */ import io.reactivex.Completable;
/*     */ import io.reactivex.CompletableObserver;
/*     */ import io.reactivex.CompletableSource;
/*     */ import io.reactivex.Scheduler;
/*     */ import io.reactivex.disposables.CompositeDisposable;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.internal.util.ExceptionHelper;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.TimeoutException;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
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
/*     */ public final class CompletableTimeout
/*     */   extends Completable
/*     */ {
/*     */   final CompletableSource source;
/*     */   final long timeout;
/*     */   final TimeUnit unit;
/*     */   final Scheduler scheduler;
/*     */   final CompletableSource other;
/*     */   
/*     */   public CompletableTimeout(CompletableSource source, long timeout, TimeUnit unit, Scheduler scheduler, CompletableSource other) {
/*  35 */     this.source = source;
/*  36 */     this.timeout = timeout;
/*  37 */     this.unit = unit;
/*  38 */     this.scheduler = scheduler;
/*  39 */     this.other = other;
/*     */   }
/*     */ 
/*     */   
/*     */   public void subscribeActual(CompletableObserver observer) {
/*  44 */     CompositeDisposable set = new CompositeDisposable();
/*  45 */     observer.onSubscribe((Disposable)set);
/*     */     
/*  47 */     AtomicBoolean once = new AtomicBoolean();
/*     */     
/*  49 */     Disposable timer = this.scheduler.scheduleDirect(new DisposeTask(once, set, observer), this.timeout, this.unit);
/*     */     
/*  51 */     set.add(timer);
/*     */     
/*  53 */     this.source.subscribe(new TimeOutObserver(set, once, observer));
/*     */   }
/*     */   
/*     */   static final class TimeOutObserver
/*     */     implements CompletableObserver {
/*     */     private final CompositeDisposable set;
/*     */     private final AtomicBoolean once;
/*     */     private final CompletableObserver downstream;
/*     */     
/*     */     TimeOutObserver(CompositeDisposable set, AtomicBoolean once, CompletableObserver observer) {
/*  63 */       this.set = set;
/*  64 */       this.once = once;
/*  65 */       this.downstream = observer;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/*  70 */       this.set.add(d);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable e) {
/*  75 */       if (this.once.compareAndSet(false, true)) {
/*  76 */         this.set.dispose();
/*  77 */         this.downstream.onError(e);
/*     */       } else {
/*  79 */         RxJavaPlugins.onError(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/*  85 */       if (this.once.compareAndSet(false, true)) {
/*  86 */         this.set.dispose();
/*  87 */         this.downstream.onComplete();
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   final class DisposeTask
/*     */     implements Runnable {
/*     */     private final AtomicBoolean once;
/*     */     final CompositeDisposable set;
/*     */     final CompletableObserver downstream;
/*     */     
/*     */     DisposeTask(AtomicBoolean once, CompositeDisposable set, CompletableObserver observer) {
/*  99 */       this.once = once;
/* 100 */       this.set = set;
/* 101 */       this.downstream = observer;
/*     */     }
/*     */ 
/*     */     
/*     */     public void run() {
/* 106 */       if (this.once.compareAndSet(false, true)) {
/* 107 */         this.set.clear();
/* 108 */         if (CompletableTimeout.this.other == null) {
/* 109 */           this.downstream.onError(new TimeoutException(ExceptionHelper.timeoutMessage(CompletableTimeout.this.timeout, CompletableTimeout.this.unit)));
/*     */         } else {
/* 111 */           CompletableTimeout.this.other.subscribe(new DisposeObserver());
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*     */     final class DisposeObserver
/*     */       implements CompletableObserver
/*     */     {
/*     */       public void onSubscribe(Disposable d) {
/* 120 */         CompletableTimeout.DisposeTask.this.set.add(d);
/*     */       }
/*     */ 
/*     */       
/*     */       public void onError(Throwable e) {
/* 125 */         CompletableTimeout.DisposeTask.this.set.dispose();
/* 126 */         CompletableTimeout.DisposeTask.this.downstream.onError(e);
/*     */       }
/*     */ 
/*     */       
/*     */       public void onComplete() {
/* 131 */         CompletableTimeout.DisposeTask.this.set.dispose();
/* 132 */         CompletableTimeout.DisposeTask.this.downstream.onComplete();
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/completable/CompletableTimeout.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */