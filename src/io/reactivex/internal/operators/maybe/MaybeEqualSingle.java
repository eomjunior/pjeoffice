/*     */ package io.reactivex.internal.operators.maybe;
/*     */ 
/*     */ import io.reactivex.MaybeObserver;
/*     */ import io.reactivex.MaybeSource;
/*     */ import io.reactivex.Single;
/*     */ import io.reactivex.SingleObserver;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.functions.BiPredicate;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
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
/*     */ public final class MaybeEqualSingle<T>
/*     */   extends Single<Boolean>
/*     */ {
/*     */   final MaybeSource<? extends T> source1;
/*     */   final MaybeSource<? extends T> source2;
/*     */   final BiPredicate<? super T, ? super T> isEqual;
/*     */   
/*     */   public MaybeEqualSingle(MaybeSource<? extends T> source1, MaybeSource<? extends T> source2, BiPredicate<? super T, ? super T> isEqual) {
/*  40 */     this.source1 = source1;
/*  41 */     this.source2 = source2;
/*  42 */     this.isEqual = isEqual;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(SingleObserver<? super Boolean> observer) {
/*  47 */     EqualCoordinator<T> parent = new EqualCoordinator<T>(observer, this.isEqual);
/*  48 */     observer.onSubscribe(parent);
/*  49 */     parent.subscribe(this.source1, this.source2);
/*     */   }
/*     */ 
/*     */   
/*     */   static final class EqualCoordinator<T>
/*     */     extends AtomicInteger
/*     */     implements Disposable
/*     */   {
/*     */     final SingleObserver<? super Boolean> downstream;
/*     */     
/*     */     final MaybeEqualSingle.EqualObserver<T> observer1;
/*     */     
/*     */     final MaybeEqualSingle.EqualObserver<T> observer2;
/*     */     final BiPredicate<? super T, ? super T> isEqual;
/*     */     
/*     */     EqualCoordinator(SingleObserver<? super Boolean> actual, BiPredicate<? super T, ? super T> isEqual) {
/*  65 */       super(2);
/*  66 */       this.downstream = actual;
/*  67 */       this.isEqual = isEqual;
/*  68 */       this.observer1 = new MaybeEqualSingle.EqualObserver<T>(this);
/*  69 */       this.observer2 = new MaybeEqualSingle.EqualObserver<T>(this);
/*     */     }
/*     */     
/*     */     void subscribe(MaybeSource<? extends T> source1, MaybeSource<? extends T> source2) {
/*  73 */       source1.subscribe(this.observer1);
/*  74 */       source2.subscribe(this.observer2);
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/*  79 */       this.observer1.dispose();
/*  80 */       this.observer2.dispose();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/*  85 */       return DisposableHelper.isDisposed(this.observer1.get());
/*     */     }
/*     */ 
/*     */     
/*     */     void done() {
/*  90 */       if (decrementAndGet() == 0) {
/*  91 */         Object o1 = this.observer1.value;
/*  92 */         Object o2 = this.observer2.value;
/*     */         
/*  94 */         if (o1 != null && o2 != null) {
/*     */           boolean b;
/*     */           
/*     */           try {
/*  98 */             b = this.isEqual.test(o1, o2);
/*  99 */           } catch (Throwable ex) {
/* 100 */             Exceptions.throwIfFatal(ex);
/* 101 */             this.downstream.onError(ex);
/*     */             
/*     */             return;
/*     */           } 
/* 105 */           this.downstream.onSuccess(Boolean.valueOf(b));
/*     */         } else {
/* 107 */           this.downstream.onSuccess(Boolean.valueOf((o1 == null && o2 == null)));
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*     */     void error(MaybeEqualSingle.EqualObserver<T> sender, Throwable ex) {
/* 113 */       if (getAndSet(0) > 0) {
/* 114 */         if (sender == this.observer1) {
/* 115 */           this.observer2.dispose();
/*     */         } else {
/* 117 */           this.observer1.dispose();
/*     */         } 
/* 119 */         this.downstream.onError(ex);
/*     */       } else {
/* 121 */         RxJavaPlugins.onError(ex);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static final class EqualObserver<T>
/*     */     extends AtomicReference<Disposable>
/*     */     implements MaybeObserver<T>
/*     */   {
/*     */     private static final long serialVersionUID = -3031974433025990931L;
/*     */     
/*     */     final MaybeEqualSingle.EqualCoordinator<T> parent;
/*     */     Object value;
/*     */     
/*     */     EqualObserver(MaybeEqualSingle.EqualCoordinator<T> parent) {
/* 137 */       this.parent = parent;
/*     */     }
/*     */     
/*     */     public void dispose() {
/* 141 */       DisposableHelper.dispose(this);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/* 146 */       DisposableHelper.setOnce(this, d);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSuccess(T value) {
/* 151 */       this.value = value;
/* 152 */       this.parent.done();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable e) {
/* 157 */       this.parent.error(this, e);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 162 */       this.parent.done();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/maybe/MaybeEqualSingle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */