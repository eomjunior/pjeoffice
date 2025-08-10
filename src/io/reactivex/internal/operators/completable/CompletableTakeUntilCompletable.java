/*     */ package io.reactivex.internal.operators.completable;
/*     */ 
/*     */ import io.reactivex.Completable;
/*     */ import io.reactivex.CompletableObserver;
/*     */ import io.reactivex.CompletableSource;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import java.util.concurrent.atomic.AtomicReference;
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
/*     */ public final class CompletableTakeUntilCompletable
/*     */   extends Completable
/*     */ {
/*     */   final Completable source;
/*     */   final CompletableSource other;
/*     */   
/*     */   public CompletableTakeUntilCompletable(Completable source, CompletableSource other) {
/*  36 */     this.source = source;
/*  37 */     this.other = other;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(CompletableObserver observer) {
/*  42 */     TakeUntilMainObserver parent = new TakeUntilMainObserver(observer);
/*  43 */     observer.onSubscribe(parent);
/*     */     
/*  45 */     this.other.subscribe(parent.other);
/*  46 */     this.source.subscribe(parent);
/*     */   }
/*     */ 
/*     */   
/*     */   static final class TakeUntilMainObserver
/*     */     extends AtomicReference<Disposable>
/*     */     implements CompletableObserver, Disposable
/*     */   {
/*     */     private static final long serialVersionUID = 3533011714830024923L;
/*     */     
/*     */     final CompletableObserver downstream;
/*     */     final OtherObserver other;
/*     */     final AtomicBoolean once;
/*     */     
/*     */     TakeUntilMainObserver(CompletableObserver downstream) {
/*  61 */       this.downstream = downstream;
/*  62 */       this.other = new OtherObserver(this);
/*  63 */       this.once = new AtomicBoolean();
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/*  68 */       if (this.once.compareAndSet(false, true)) {
/*  69 */         DisposableHelper.dispose(this);
/*  70 */         DisposableHelper.dispose(this.other);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/*  76 */       return this.once.get();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/*  81 */       DisposableHelper.setOnce(this, d);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/*  86 */       if (this.once.compareAndSet(false, true)) {
/*  87 */         DisposableHelper.dispose(this.other);
/*  88 */         this.downstream.onComplete();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable e) {
/*  94 */       if (this.once.compareAndSet(false, true)) {
/*  95 */         DisposableHelper.dispose(this.other);
/*  96 */         this.downstream.onError(e);
/*     */       } else {
/*  98 */         RxJavaPlugins.onError(e);
/*     */       } 
/*     */     }
/*     */     
/*     */     void innerComplete() {
/* 103 */       if (this.once.compareAndSet(false, true)) {
/* 104 */         DisposableHelper.dispose(this);
/* 105 */         this.downstream.onComplete();
/*     */       } 
/*     */     }
/*     */     
/*     */     void innerError(Throwable e) {
/* 110 */       if (this.once.compareAndSet(false, true)) {
/* 111 */         DisposableHelper.dispose(this);
/* 112 */         this.downstream.onError(e);
/*     */       } else {
/* 114 */         RxJavaPlugins.onError(e);
/*     */       } 
/*     */     }
/*     */     
/*     */     static final class OtherObserver
/*     */       extends AtomicReference<Disposable>
/*     */       implements CompletableObserver {
/*     */       private static final long serialVersionUID = 5176264485428790318L;
/*     */       final CompletableTakeUntilCompletable.TakeUntilMainObserver parent;
/*     */       
/*     */       OtherObserver(CompletableTakeUntilCompletable.TakeUntilMainObserver parent) {
/* 125 */         this.parent = parent;
/*     */       }
/*     */ 
/*     */       
/*     */       public void onSubscribe(Disposable d) {
/* 130 */         DisposableHelper.setOnce(this, d);
/*     */       }
/*     */ 
/*     */       
/*     */       public void onComplete() {
/* 135 */         this.parent.innerComplete();
/*     */       }
/*     */       
/*     */       public void onError(Throwable e)
/*     */       {
/* 140 */         this.parent.innerError(e); } } } static final class OtherObserver extends AtomicReference<Disposable> implements CompletableObserver { public void onError(Throwable e) { this.parent.innerError(e); }
/*     */ 
/*     */     
/*     */     private static final long serialVersionUID = 5176264485428790318L;
/*     */     final CompletableTakeUntilCompletable.TakeUntilMainObserver parent;
/*     */     
/*     */     OtherObserver(CompletableTakeUntilCompletable.TakeUntilMainObserver parent) {
/*     */       this.parent = parent;
/*     */     }
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/*     */       DisposableHelper.setOnce(this, d);
/*     */     }
/*     */     
/*     */     public void onComplete() {
/*     */       this.parent.innerComplete();
/*     */     } }
/*     */ 
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/completable/CompletableTakeUntilCompletable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */