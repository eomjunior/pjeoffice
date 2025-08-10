/*     */ package io.reactivex.internal.operators.observable;
/*     */ 
/*     */ import io.reactivex.ObservableSource;
/*     */ import io.reactivex.Observer;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import java.util.NoSuchElementException;
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
/*     */ public final class ObservableElementAt<T>
/*     */   extends AbstractObservableWithUpstream<T, T>
/*     */ {
/*     */   final long index;
/*     */   final T defaultValue;
/*     */   final boolean errorOnFewer;
/*     */   
/*     */   public ObservableElementAt(ObservableSource<T> source, long index, T defaultValue, boolean errorOnFewer) {
/*  29 */     super(source);
/*  30 */     this.index = index;
/*  31 */     this.defaultValue = defaultValue;
/*  32 */     this.errorOnFewer = errorOnFewer;
/*     */   }
/*     */ 
/*     */   
/*     */   public void subscribeActual(Observer<? super T> t) {
/*  37 */     this.source.subscribe(new ElementAtObserver<T>(t, this.index, this.defaultValue, this.errorOnFewer));
/*     */   }
/*     */ 
/*     */   
/*     */   static final class ElementAtObserver<T>
/*     */     implements Observer<T>, Disposable
/*     */   {
/*     */     final Observer<? super T> downstream;
/*     */     final long index;
/*     */     final T defaultValue;
/*     */     final boolean errorOnFewer;
/*     */     Disposable upstream;
/*     */     long count;
/*     */     boolean done;
/*     */     
/*     */     ElementAtObserver(Observer<? super T> actual, long index, T defaultValue, boolean errorOnFewer) {
/*  53 */       this.downstream = actual;
/*  54 */       this.index = index;
/*  55 */       this.defaultValue = defaultValue;
/*  56 */       this.errorOnFewer = errorOnFewer;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/*  61 */       if (DisposableHelper.validate(this.upstream, d)) {
/*  62 */         this.upstream = d;
/*  63 */         this.downstream.onSubscribe(this);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/*  69 */       this.upstream.dispose();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/*  74 */       return this.upstream.isDisposed();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/*  79 */       if (this.done) {
/*     */         return;
/*     */       }
/*  82 */       long c = this.count;
/*  83 */       if (c == this.index) {
/*  84 */         this.done = true;
/*  85 */         this.upstream.dispose();
/*  86 */         this.downstream.onNext(t);
/*  87 */         this.downstream.onComplete();
/*     */         return;
/*     */       } 
/*  90 */       this.count = c + 1L;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/*  95 */       if (this.done) {
/*  96 */         RxJavaPlugins.onError(t);
/*     */         return;
/*     */       } 
/*  99 */       this.done = true;
/* 100 */       this.downstream.onError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 105 */       if (!this.done) {
/* 106 */         this.done = true;
/* 107 */         T v = this.defaultValue;
/* 108 */         if (v == null && this.errorOnFewer) {
/* 109 */           this.downstream.onError(new NoSuchElementException());
/*     */         } else {
/* 111 */           if (v != null) {
/* 112 */             this.downstream.onNext(v);
/*     */           }
/* 114 */           this.downstream.onComplete();
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableElementAt.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */